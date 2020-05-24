package wind.model.npcs.impl;

import java.util.ArrayList;
import java.util.Random;

import wind.Server;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCDrops;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.ItemHandler;
import wind.world.map.Z; 

@SuppressWarnings("unused")
public class Zulrah extends NPC {
	static int type;
	static int move;
	ArrayList<Zulrah> ActiveZulrah = new ArrayList<Zulrah>();
	private static int HITPOINTS = 500,
			ATTACK_VALUE = 300,
			MAX_HIT = 35,
			DEFENSE_VALUE= 300;
	private enum STAGE {
		MAGE,RANGE,MINION,MELEE;
	}
	private STAGE stage;
	private static final int RANGE = 2042;
	private static final int MELEE = 2043;
	private static final int MAGE = 2044;
	private static final int SNAKELING = 2045;

	private static final int SPAWN_ANIM = 5071;
	private static final int SPAWN_BELOW = 5073;

	public Zulrah(int npcSlot, int npcType) {
		super(npcSlot, npcType);
		type = Misc.random(3);
		switch (type) {
		case 1:
			stage = STAGE.RANGE;
			break;
		case 2:
			stage = STAGE.MAGE;
			break;
		case 3:
			stage = STAGE.MELEE;
			break;
		default:
		case 0:
			stage = STAGE.MINION;
			break;
		}
	}
	static long startTime;
	public static void setupTimer(Client player) {
		startTime = System.currentTimeMillis();
		//player.getPA().walkableInterface(8888);
		handleTimer(player);
	}
	static String finalTime;
	private static void handleTimer(Client player) {
		TaskHandler.submit(new Task(0, true) {
			@Override
			public void execute() {
				if (player.zulrah.isDead)
					return;
				long elapsedTime = System.currentTimeMillis() - startTime;
				long elapsedSeconds = elapsedTime / 1000;
				long secondsDisplay = elapsedSeconds % 60;
				long elapsedMinutes = elapsedSeconds / 60;
				finalTime = elapsedMinutes+":"+secondsDisplay;
			//	player.getPA().walkableInterface(8888);
			//	int meleeInterface = 8888;
			//	int magicInterface = 8900;
			//	int rangeInterface = 9000;
				switch(player.zulrah.npcType) {
				case MELEE:
				//	player.getPA().walkableInterface(meleeInterface);
					player.getPA().sendFrame126("Time: "+finalTime, 8889);
					break;
				case RANGE:
				//	player.getPA().walkableInterface(rangeInterface);
					player.getPA().sendFrame126("Time: "+finalTime, 9002);
					break;
				case MAGE:
				//	player.getPA().walkableInterface(magicInterface);
					player.getPA().sendFrame126("Time: "+finalTime, 8902);
					break;
				}
			}
		});		
	}
	public static void endTimer(Client player) {
		String displayFinalTime;
		if (player.zulrah.isDead) {
			displayFinalTime = finalTime;
			player.sendMessage("@dre@Final time: "+displayFinalTime);
			//player.getPA().walkableInterface(8888);
			player.getPA().sendFrame126(displayFinalTime+"", 8889);
		}
	}
	public static void init(Client player) {
		Zulrah z;
		TaskHandler.submit(new Task(3, true) {
			@Override
			public void execute() {
				player.getPA().startTeleport(2267, 3070, player.playerId * 4, "modern");
				player.sendMessage("@dre@You teleport to Zulrah's Shrine.");
				this.cancel();
			}
			@Override
			public void onCancel() {
				System.out.println(player.playerName + ": teleport to Zulrah.");
			}
		});
		TaskHandler.submit(new Task(10, false) {

			@Override
			public void execute() {
				player.zulrah = (Zulrah)NPCHandler.spawnZulrah(player, 2042, 2266, 3072, player.playerId * 4, 0, HITPOINTS, 35, ATTACK_VALUE, DEFENSE_VALUE, SPAWN_ANIM, "Ssss..");
				setupTimer(player);
				this.cancel();
			}
			@Override
			public void onCancel() {
				System.out.println(player.playerName + ": Zulrah spawned.");
			}
		});

		TaskHandler.submit(new Task(100, false) {

			@Override
			public void execute() {
				move(player);
				this.cancel();
			}
			@Override
			public void onCancel() {
			}
		});
		z = player.zulrah;
	}
	public static void destruct(Client player) {
		Zulrah z = player.zulrah;
		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
				player.zulrah.absX = 0;
				player.zulrah.absY = 0;
				player.zulrah.updateRequired = true;
				player.zulrah.npcType = -1;
				player.zulrah.isDead = true;
				player.zulrah.updateRequired = true;
				this.cancel();
			}
			@Override 
			public void onCancel() {
				System.out.println(player.playerName + ": Zulrah destruct.");
			}
		});
		//player.sendMessage("@lre@Zulrah took notice of your cowardly efforts..");
	}
	static NPC snake[] = new NPC[5];
	private static void constructMinions(Client player) {
		if (player.zulrah.isDead == true)
			return;
		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
				snake[0] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2263, 3075, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[1] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2263, 3071, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[2] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2268, 3069, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[3] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2273, 3071, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[4] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2273, 3077, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				this.cancel();
			}

			@Override
			public void onCancel() {
				player.sendMessage("Zulrah has spawned her minions!");
			}
		});
		TaskHandler.submit(new Task(100, false) {
			@Override
			public void execute() {
				destructMinions(player);
				this.cancel();
			}

			@Override
			public void onCancel() {
				player.sendMessage("Zulrah has spawned her minions!");
			}
		});
	}
	private static void destructMinions(Client player) {
		if (player.zulrah.isDead == true)
			return;
		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
				for (int i = 0; i < snake.length; i++) {
					snake[i].absX = 0;
					snake[i].absY = 0;
					snake[i].isDead = true;
					snake[i].needRespawn = false;
					snake[i].respawns = false;
					snake[i].updateRequired = true;
				}
				this.cancel();
			}

			@Override
			public void onCancel() {
				//player.sendMessage("Zulrah minions destruct");
			}
		});
	}
	private static void constructClouds(Client player) {
		if (player.zulrah.isDead == true)
			return;
		int cloud = 11700;
		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
				player.getPA().object(cloud, 2263, 3075, 1, 10);
				player.getPA().object(cloud, 2263, 3071, 1, 10);
				player.getPA().object(cloud, 2268, 3069, 1, 10);
				player.getPA().object(cloud, 2273, 3071, 1, 10);
				player.getPA().object(cloud, 2273, 3077, 1, 10);
				player.getPA().object(cloud, 2273, 3075, 1, 10);
				this.cancel();
			}
			@Override
			public void onCancel() {
				player.sendMessage("Zulrah has excreted poisonous clouds!");
			}
		});
		TaskHandler.submit(new Task(75, false) {
			@Override
			public void execute() {
				destructClouds(player);
				this.cancel();
			}
			@Override
			public void onCancel() {
			}
		});

	}
	private static void destructClouds(Client player) {
		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
				player.getPA().object(-1, 2263, 3075, 1, 10);
				player.getPA().object(-1, 2263, 3071, 1, 10);
				player.getPA().object(-1, 2268, 3069, 1, 10);
				player.getPA().object(-1, 2273, 3071, 1, 10);
				player.getPA().object(-1, 2273, 3077, 1, 10);
				player.getPA().object(-1, 2273, 3075, 1, 10);
				this.cancel();
			}
			@Override
			public void onCancel() {
			}
		});
	}
	public static void move(Client player) {
		Zulrah z = player.zulrah;
		if (player.zulrah.isDead == true)
			return;
		int possibleX[] = {2266, 2256, 2276, 2273};
		int possibleY[] = {3072, 3073, 3074, 3064};
		Random r = new Random();
		int next = r.nextInt(possibleX.length);
		System.out.println(next);
		TaskHandler.submit(new Task(1, true) {
			@Override
			public void execute() {
				z.forceChat("Sssss...");
				z.forceAnim(5072);
				player.sendMessage("Zulrah dives into the swamp..");
				this.cancel();
			}
			@Override 
			public void onCancel() {
				System.out.println(player.playerName + ": Zulrah move method called.");
			}
		});
		TaskHandler.submit(new Task(6, false) {
			@Override
			public void execute() {
				z.absX = 0;
				z.absY = 0;
				z.updateRequired = true;
				this.cancel();
			}
			@Override 
			public void onCancel() {
				System.out.println(player.playerName + ": Zulrah has moved.");
			}
		});
		TaskHandler.submit(new Task(8, false) {
			@Override
			public void execute() {
				z.absX = possibleX[next];
				z.absY = possibleY[next];
				z.makeX = possibleX[next];
				z.makeY = possibleY[next];
				z.heightLevel = player.heightLevel;
				z.updateRequired = true;
				z.npcType = getNextForm();
				z.turnNpc(player.absX, player.absY);
				z.forceAnim(SPAWN_ANIM);
				this.cancel();
			}
			private int getNextForm() {
				Random r = new Random();
				int next = r.nextInt(3) + 1;
				switch (next) {
				case 1:
					System.out.println(player.playerName + " [Zulrah] MELEE");
					constructClouds(player);
					beginMoveTimer();
					return MELEE;
				case 2:
					System.out.println(player.playerName + " [Zulrah] MAGE");
					constructMinions(player);
					beginMoveTimer();
					return MAGE;
				case 3:
					System.out.println(player.playerName + " [Zulrah] RANGE");
					beginMoveTimer();
					return RANGE;
				}
				return 0;
			}
			private void beginMoveTimer() {
				if (!player.inZulrahShrine()) {
					Zulrah.destruct(player);
					return;
				}
				TaskHandler.submit(new Task(100, false) {

					@Override
					public void execute() {
						move(player);
						this.cancel();
					}
					@Override
					public void onCancel() {
						System.out.println(player.playerName + ": Zulrah move began.");
					}
				});

			}
			@Override 
			public void onCancel() {
				System.out.println(player.playerName + ": Zulrah has reappeared on the map.");
			}
		});
	}
	public static void handleClueDrop(Client player) {
		int unique = Misc.random(128);
		int uniqueList[] = {2714, 2802, 2775};
		if (unique == 1)
			ItemHandler.createGroundItem(player, uniqueList[Misc.random(uniqueList.length) + 1], player.absX, player.absY, 1, player.getId());
	}
	
	
	@SuppressWarnings("static-access")
	public static void handleDrops(Client player) {
		Random r = new Random();
		int teleportAmt = r.nextInt(5);		
		endTimer(player);
		int uniqueList[] = {12932, 12927, 6571, 12926, 12935, 12936, 13200, 13201, 4207};
		int uniqueList2[] ={3204, 1149};
		int uniqueList3[] ={537, 8783, 384, 1392, 2362, 6686, 1780, 454, 1514, 9193, 452, 2999, 268, 270, 3001, 563, 560, 562, 565, 7937};
		int unique = Misc.random(128);
		int unique2 = Misc.random(128);
		int unique3 = Misc.random(512);
		int unique4 = Misc.random(16);
		if (unique == 1)
			Server.itemHandler.createGroundItem(player, uniqueList[Misc.random(uniqueList.length) + 1], player.absX, player.absY, 1, player.getId());
		if (unique2 == 1)
			Server.itemHandler.createGroundItem(player, uniqueList[Misc.random(uniqueList.length) + 1], player.absX, player.absY, 1, player.getId());
		if (unique3 == 1)
			Server.itemHandler.createGroundItem(player, 12921, player.absX, player.absY, 1, player.getId());
		if (unique4 == 1) 
			Server.itemHandler.createGroundItem(player, uniqueList2[Misc.random(uniqueList2.length) + 1], player.absX, player.absY, 1, player.getId());
		if (unique4 != 1)
			Server.itemHandler.createGroundItem(player, uniqueList3[Misc.random(uniqueList3.length) + 1], player.absX, player.absY, Misc.random(90) + 10, player.getId());
		Server.itemHandler.createGroundItem(player, 12934, player.absX, player.absY, Misc.random(200) + 100, player.getId());
		Server.itemHandler.createGroundItem(player, 12938, player.absX, player.absY, teleportAmt, player.getId());
		cleanUp(player);
		handlePetChance(player);
	}
	private static void cleanUp(Client player) {
		player.zulrah.absX = 0;
		player.zulrah.absY = 0;
		player.zulrah.respawns = false;
		player.zulrah.isDead = true;
		player.zulrah.updateRequired = true;
		destructClouds(player);
		destructMinions(player);
		System.out.println("Zulrah clean-up complete.");

	}
	public static void handlePetChance(Client player) {
		int petChance = Misc.random(200);
		if (petChance == 200) {
			if (player.getItems().hasPetItem(12921))
				return;
			else {
				player.getItems().addItemToBank(12921, 1);
				player.sendMessage("Your pet Snakeling has been added to your bank!");
				player.getwM().globalMessage(player.playerName + " has received a @dre@Pet Zulrah @bla@drop! Congratulations.");
			}
		}
	}
}
