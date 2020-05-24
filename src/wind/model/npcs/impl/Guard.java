package wind.model.npcs.impl;

import java.util.ArrayList;
import java.util.Random;

import wind.Server;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCDrops;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.map.Z;

@SuppressWarnings("unused")
public class Guard extends NPC {
	static int type;
	static int move;
	ArrayList<Guard> Guard = new ArrayList<Guard>();
	private static int HITPOINTS = 1000, COMBAT_LEVEL = 1337, ATTACK_VALUE = 3000, MAX_HIT = 75, DEFENSE_VALUE = 1000;

	private enum STAGE {
		FREEZE;
	}

	private STAGE stage;
	private static final int FREEZE = 2042;
	protected static final Client Client = null;

	//private static final int SPAWN_ANIM = 5071;
	// private static final int SPAWN_BELOW = 5073;

	public Guard(int npcSlot, int npcType) {
		super(npcSlot, npcType);
		type = Misc.random(3);
		switch (type) {
		case 1:
		case 2:
		case 3:
			stage = STAGE.FREEZE;
		break;
		}
	}
	
	public static int GUARD = 3010;
	//public static boolean guardSpawned = false;

	static NPC guard[] = new NPC[1];
	public static void constructGuard(Client player) { 
		if(player.guardSpawned == true) {
			return;
		}
		
		TaskHandler.submit(new Task(1, true) { // try this
			@Override
			public void execute() {
				guard[0] = (NPC) NPCHandler.spawnGuard(player, GUARD, player.absX, player.absY, player.heightLevel, 1, 5000, 40, 1250, 600, true, "Guard 1");

				player.sendMessage("NPC's Spawned"); 
				player.guardSpawned = true; 

				this.cancel();
			}

			@Override
			public void onCancel() {
				player.sendMessage("You are in a safezone with a skull!  Guards have spawned to kill you!");
			}
		});
		TaskHandler.submit(new Task(100, false) {
			@Override
			public void execute() {
				//if(player.isDead == true) 
				destructGuards(player);
				player.guardSpawned = false; 
				this.cancel();
			}

			@Override
			public void onCancel() {
				player.sendMessage("You are in a safezone with a skull!  Guards have spawned to kill you!");
			}
		}); //try this aight go
	}
	
	public static void destructGuards(Client player) { 

		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
					guard[0].absX = -1;
					guard[0].absY = -1;
					guard[0].isDead = true;
					//guard[0].needRespawn = false;
					guard[0].npcType = -1;
					//guard[0].respawns = false;
					//guard[0].applyDead = true;
					guard[0].updateRequired = true;
				this.cancel();
			}

			@Override
			public void onCancel() {
				player.sendMessage("Destruct guards test");
			}
		});
	}
}