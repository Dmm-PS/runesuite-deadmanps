package shamon.barrows;

import java.util.ArrayList;
import java.util.List;

import shamon.barrows.data.TunnelHallwayData;
import shamon.barrows.data.TunnelRoomData;
import shamon.barrows.data.TunnelSpawns;
import shamon.region.Position;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

public class BarrowsMaze {
	/**
	 * The tunnel you spawn in after you dig.
	 */
	private TunnelSpawns spawn = TunnelSpawns.SOUTHEAST_SPAWN;

	/**
	 * Keeps track of closed hallways.
	 */
	private final List<TunnelHallwayData> closedHallways = new ArrayList<>();

	private final Client client;

	public BarrowsMaze(Client client) {
		this.client = client;
	}

	/**
	 * Reopen any closed doors.
	 */
	public void resetHallways() {
		for (TunnelHallwayData hallway : closedHallways) {
			hallway.openDoors(client);
		}
		closedHallways.clear();
	}

	/**
	 * Close of hallways.
	 */
	public void randomizeMaze() {
		resetHallways();
		randomizeChestRoom();
		randomizeSpawnRoom();
	}

	/**
	 * Set a random spawn point.
	 */
	public void randomizeSpawnPoint() {
		spawn = TunnelSpawns.SPAWNS[(int) (Math.random() * TunnelSpawns.SPAWNS.length)];
	}

	/**
	 * Teleport inside of the maze after digging.
	 */
	public void teleportToMaze() {
		Position position = spawn.getPosition();
		client.teleportToX = position.getX();
		client.teleportToY = position.getY();
		client.heightLevel = position.getHeight();

		// Teleporting too fast, objects spawn on wrong level.
		Task task = new Task(1, false) {
			@Override public void execute() {
				randomizeMaze();
				cancel();
			}
		};
		TaskHandler.submit(task);
	}

	private void randomizeRoom(TunnelRoomData room) {
		int openDirection = (int) (Math.random() * 4);

		// Loop through the 4 cardinal directions and close off all but 1 hallway.
		for (int direction = 0; direction < 4; direction++) {
			TunnelHallwayData hallway = room.getHallway(direction);

			if (hallway == null || direction == openDirection) {
				continue;
			}
			hallway.closeDoors(client);
			closedHallways.add(hallway);
		}
	}

	private void randomizeChestRoom() {
		randomizeRoom(TunnelRoomData.CHEST);
	}

	private void randomizeSpawnRoom() {
		randomizeRoom(spawn.getRoom());
	}

	public TunnelSpawns getSpawn() {
		return spawn;
	}
}
