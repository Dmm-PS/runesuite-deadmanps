package shamon.barrows.impl;

import java.util.ArrayList;
import java.util.List;

import shamon.barrows.BarrowsNpcController;
import shamon.barrows.BarrowsPuzzleDisplay;
import shamon.barrows.TunnelDoor;
import shamon.barrows.data.BarrowsConstants;
import shamon.barrows.data.Brother;
import shamon.barrows.data.NpcCombatData;
import shamon.barrows.data.TunnelDoors;
import shamon.region.Rotation;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

public class OpenTunnelDoorHandler {

	private final Client client;

	private Task doorTask;

	private boolean moved;

	private int targetX;

	private int targetY;

	public OpenTunnelDoorHandler(Client client) {
		this.client = client;
	}

	public boolean openDoor(int objectID, int objectX, int objectY) {
		TunnelDoor door = TunnelDoors.getDoor(objectX, objectY);

		if (door == null || door.getID() != objectID) {
			return false;
		}

		if (doorTask != null) {
			doorTask.cancel();
		}

		doorTask = new Task(1, true) {
			@Override public void execute() {
				if (client.goodDistance(objectX, objectY, client.absX, client.absY, 1)) {
					BarrowsPuzzleDisplay puzzle = client.getBarrows().getPuzzle();

					if (BarrowsConstants.CHEST_DOORS.contains(objectID) && !puzzle.isSolved()) {
						puzzle.displayInterface(client);
						cancel();
					} else {
						walkThroughDoor(this, door, objectX, objectY);
					}
				}
			}

			@Override public void onCancel() {
				moved = false;
			}
		};
		TaskHandler.submit(doorTask);
		return true;
	}

	private void walkThroughDoor(Task task, TunnelDoor door, int objectX, int objectY) {
		if (!moved) {
			targetX = objectX;
			targetY = objectY;

			if (client.absX == objectX) {
				if (door.getRotation() == Rotation.EAST) {
					targetX = objectX - 1;
				} else if (door.getRotation() == Rotation.WEST) {
					targetX = objectX + 1;
				}
			}
			if (client.absY == objectY) {
				if (door.getRotation() == Rotation.NORTH) {
					targetY = objectY + 1;
				} else if (door.getRotation() == Rotation.SOUTH) {
					targetY = objectY - 1;
				}
			}

			int deltaX = targetX - client.absX;
			int deltaY = targetY - client.absY;
			client.getPA().walkTo(deltaX, deltaY);
			client.getPA().appendWalkingQueue(targetX, targetY);
			moved = true;
		}

		if (moved) {
			if (client.absX == targetX && client.absY == targetY) {
				spawnRandomMonster(targetX, targetY);
				task.cancel();
			}
		}
	}

	private void spawnRandomMonster(int x, int y) {
		BarrowsNpcController npcConroller = client.getBarrows().getNpcController();

		List<Integer> npcs = new ArrayList<>();
		npcs.addAll(BarrowsConstants.DOOR_NPCS);
		npcs.addAll(BarrowsConstants.DOOR_NPCS);
		npcs.addAll(BarrowsConstants.DOOR_NPCS);
		npcs.addAll(generateBarrowsNpcs());

		// Pick a random npc index.

		Task despawnTask = new Task(1, false) {
			int randomNpcID = npcs.get((int) (Math.random() * npcs.size()));
			Brother brother = Brother.lookup(randomNpcID);
			NPC npc = null;

			@Override public void onSubmit() {
				NpcCombatData cbData = NpcCombatData.lookup(randomNpcID);
				
				if(cbData == null) {
					System.err.println("Invalid npcId " + randomNpcID + " for NpcCombatData.");
					cancel();
					return;
				}
				
				if (brother != null) {
					npcConroller.registerSpawn(brother);
					npc = NPCHandler.spawnNpc(client, brother.getID(), x, y, client.heightLevel, 0, cbData.getHealth(), cbData.getMaxHit(), cbData.getAttack(), cbData.getDefence(), true,
							true);
				} else {
					npc = NPCHandler
							.spawnNpc(client, randomNpcID, x, y, client.heightLevel, 0, cbData.getHealth(), cbData.getMaxHit(), cbData.getAttack(), cbData.getDefence(), true, false);
				}
			}

			@Override public void execute() {
				if (client.distanceToPoint(npc.getX(), npc.getY()) > 4) {
					NPCHandler.relocate(npc, -1, -1, 0);
					NPCHandler.npcs[npc.npcId] = null;
					if (brother != null) {
						npcConroller.unregisterSpawn(brother);
					}
					cancel();
				}
			}
		};
		TaskHandler.submit(despawnTask);
	}

	private List<Integer> generateBarrowsNpcs() {
		BarrowsNpcController npcConroller = client.getBarrows().getNpcController();

		// If a brother isn't killed or isn't spawned, add to the list.
		List<Integer> npcs = new ArrayList<>();
		for (Brother brother : Brother.values()) {
			if (!npcConroller.checkIfKilled(brother) && !npcConroller.checkIfSpawned(brother)) {
				npcs.add(brother.getID());
			}
		}
		return npcs;
	}
}
