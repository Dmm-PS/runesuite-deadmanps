package wind.model.players.content.minigames.fightcave;

import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 17, 2013
 */
public class FightCave {

	private Client player;
	private int killsRemaining;
	private int fctimer = 16;

	public FightCave(Client player) {
		this.player = player;
	}

	public void spawn() {
		final int[][] type = Wave.getWaveForType(player);
		if (player.waveId >= type.length && player.waveType > 0
				&& player.isInFightCaves()) {
			stop();
			return;
		}
		
		TaskHandler.submit(new Task(fctimer, false) {
			@Override
			public void execute() {
				if (player == null) {
					this.cancel();
					return;
				}
				if (!player.isInFightCaves()) {
					player.waveId = 0;
					player.waveType = 0;
					this.cancel();
					return;
				}
				if (player.waveId >= type.length && player.waveType > 0) {
					stop();
					this.cancel();
					return;
				}
				if (player.waveId != 0 && player.waveId < type.length)
					player.sendMessage("You are now on wave @red@"
							+ (player.waveId + 1) + "@bla@ of " + type.length + ".");
				setKillsRemaining(type[player.waveId].length);
				for (int i = 0; i < getKillsRemaining(); i++) {
					int npcType = type[player.waveId][i];
					int index = Misc.random(Wave.SPAWN_DATA.length - 1);
					int x = Wave.SPAWN_DATA[index][0];
					int y = Wave.SPAWN_DATA[index][1];
					NPCHandler.spawnNpc(player, npcType, x, y,
							player.playerId * 4, 1, Wave.getHp(npcType),
							Wave.getMax(npcType), Wave.getAtk(npcType),
							Wave.getDef(npcType), true, false);
				}
				this.cancel();
			}
			
			@Override
			public void onCancel() {
				
			}
			
		});
	}

	public void leaveGame() {
		this.player.sendMessage("You have left the Fight Cave minigame.");
		this.player.getPA().movePlayer(2438, 5168, 0);
		this.player.waveType = 0;
		this.player.waveId = 0;
	}

	public void create(int type) {		
		this.player.getPA().removeAllWindows();
		this.player.getPA().movePlayer(2413, 5117, player.playerId * 4);
		this.player.waveType = 1;
		this.player.getDH().sendDialogues(2617, 2617);
		this.player.sendMessage("Welcome to the Fight Cave minigame. Your first wave will start in @red@" + fctimer + "@bla@ seconds.");
		this.player.waveId = 9;
		spawn();
	}

	public void stop() {
		reward();
		this.player.getPA().movePlayer(2438, 5168, 0);
		this.player.getDH().sendStatement(
				"Congratulations for finishing Fight Caves on level ["
						+ player.waveType + "]");
		this.player.waveInfo[player.waveType - 1] += 1;
		this.player.waveType = 0;
		this.player.waveId = 0;
		this.player.nextChat = 0;
		killAllSpawns();
	}

	public void handleDeath() {
		this.player.getPA().movePlayer(2438, 5168, 0);
		this.player.getDH().sendStatement(
				"Unfortunately you died on wave " + player.waveId
						+ ". Better luck next time.");
		this.player.nextChat = 0;
		this.player.waveType = 0;
		this.player.waveId = 0;
		killAllSpawns();
	}

	public void killAllSpawns() {
		for (int i = 0; i < NPCHandler.npcs.length; i++)
			if (NPCHandler.npcs[i] != null)
				if (NPCHandler.isFightCaveNpc(i))
					if (NPCHandler.isSpawnedBy(player, NPCHandler.npcs[i])) {
						NPCHandler.npcs[i] = null;
					}
	}

	public static final int FIRE_CAPE = 6570;
	public static final int TOKHAAR_KAL = 19111;

	private static final int[][] REWARDS = { { 6568, 6523, 6524, 6525, 6526,
			6527 }, };

	public void reward() {
		switch (player.waveType) {
		case 1:
			this.player.getItems().addItem(FIRE_CAPE, 1);
			this.player.getItems().addItem(
					REWARDS[0][Misc.random(REWARDS[0].length - 1)], 1);
			break;
		case 2:
			this.player.getItems().addItem(FIRE_CAPE, 1);
			this.player.getItems().addItem(FIRE_CAPE, 1);
			this.player.getItems().addItem(
					REWARDS[0][Misc.random(REWARDS[0].length - 1)], 1);
			break;
		case 3:
			this.player.getItems().addItem(FIRE_CAPE, 1);
			this.player.getItems().addItem(TOKHAAR_KAL, 1);
			this.player.getItems().addItem(
					REWARDS[0][Misc.random(REWARDS[0].length - 1)], 1);
			break;
		}
		this.player.getItems().addItem(6529,
				(10000 * player.waveType) + Misc.random(5000));
	}

	public int getKillsRemaining() {
		return killsRemaining;
	}

	public void setKillsRemaining(int remaining) {
		this.killsRemaining = remaining;
	}

}
