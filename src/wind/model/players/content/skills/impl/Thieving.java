package wind.model.players.content.skills.impl;

import wind.Config;
import wind.Server;
import wind.model.players.Client;
import wind.model.players.Events;
import wind.model.players.Player;
import wind.model.players.Rights;
import wind.model.players.content.skills.SkillHandler;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * Class Thieving Handles Thieving
 * 
 * @author PapaDoc 00:28 01/09/2010
 */

public class Thieving extends SkillHandler {

	public static void stealFromStall(final Client c, final int id, int amount,
			int xp, int level, final int i, final int x, final int y,
			final int face) {
		if (System.currentTimeMillis() - c.lastThieve < 3000)
			return;
		if (c.underAttackBy > 0 || c.underAttackBy2 > 0) {
			c.sendMessage("You can't steal from a stall while in combat!");
			return;
		}
		if (Misc.random(170) == 0) {
			Events.Guard(c);
		}
		if (c.playerLevel[c.playerThieving] >= level) {
			if (c.getItems().addItem(id, amount)) {
				c.startAnimation(832);
				c.getPA().addSkillXP(xp * Config.THIEVING_EXPERIENCE,
						c.playerThieving);
				int zulrah = Misc.random(256);
				if (zulrah == 142) {
					c.getItems().addItemToBank(12938, 1);
					c.sendMessage("@red@A mysterious teleport has been added to your bank!");
				}
				c.lastThieve = System.currentTimeMillis();
				c.turnPlayerTo(c.objectX, c.objectY);
				c.sendMessage("You steal some coins.");
				Server.objectHandler.createAnObject(c, 634, x, y, 3);
				
				TaskHandler.submit(new Task(getRespawnTime(c, i), true) {
					@Override
					public void execute() {
						Server.objectHandler.createAnObject(c, i, x, y, 3);
						this.cancel();
					}
					
					@Override
					public void onCancel() {
						
					}
				});
			}
		} else {
			c.sendMessage("You must have a thieving level of " + level
					+ " to thieve from this stall.");
		}
	}

	private static int getRespawnTime(Client c, int i) {
		switch (i) {
		case 4706:
			return 3;
		case 4705:
			return 6;
		case 2561:
			return 3; // BAKER
		case 2560:
			return 12; // SILK
		case 7053:
			return 17; // SEED
		case 2563:
			return 5; // FUR
		case 2565:
			return 5; // SILVER
		case 2564:
			return 10; // SPICE
		case 2562:
			return 20; // GEM
		case 14011:
			return 5; // JUG
		default:
			return 5;
		}
	}

	private static int[][] pickpocket = { /*{ 1, 1, 8, 1000, 3, 10, 20, 30 },
			{ 2, 1, 8, 1000, 3, 10, 20, 30 }, { 3, 1, 8, 1000, 3, 10, 20, 30 },
			{ 3, 1, 8, 1000, 3, 10, 20, 30 }, { 5, 1, 8, 1000, 3, 10, 20, 30 },
			{ 6, 1, 8, 1000, 3, 10, 20, 30 },
			{ 3223, 1, 8, 1000, 3, 10, 20, 30 },
			{ 3224, 1, 8, 1000, 3, 10, 20, 30 },
			{ 3226, 1, 8, 1000, 3, 10, 20, 30 },
			{ 3227, 1, 8, 1000, 3, 10, 20, 30 },
			{ 3915, 1, 8, 1000, 3, 10, 20, 30 },
			{ 7, 10, 15, 1000, 3, 20, 30, 40 },
			{ 9, 40, 47, 1200, 3, 50, 60, 70 },
			{ 15, 25, 26, 1500, 3, 35, 45, 55 },
			{ 20, 70, 151, 6000, 8, 80, 90, 99 },
			{ 2256, 70, 151, 6000, 8, 80, 90, 99 },
			{ 21, 80, 275, 13000, 8, 90, 95, 99 },
			{ 23, 55, 84, 1000, 7, 65, 75, 85 },
			{ 26, 55, 84, 1000, 7, 65, 75, 85 },
			{ 18, 25, 26, 1200, 3, 35, 45, 55 },
			{ 3225, 10, 15, 1000, 3, 20, 30, 40 },
			{ 2234, 38, 43, 2000, 7, 48, 58, 68 },
			{ 2235, 38, 43, 2000, 7, 48, 58, 68 },
			{ 32, 40, 47, 1200, 3, 50, 60, 70 },
			{ 296, 40, 47, 1200, 3, 50, 60, 70 },
			{ 297, 40, 47, 1200, 3, 50, 60, 70 },
			{ 298, 40, 47, 1200, 3, 50, 60, 70 },
			{ 299, 40, 47, 1200, 3, 50, 60, 70 },
			{ 2699, 40, 47, 1200, 3, 50, 60, 70 },
			{ 2701, 40, 47, 1200, 3, 50, 60, 70 },
			{ 2702, 40, 47, 1200, 3, 50, 60, 70 },
			{ 2703, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3228, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3229, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3230, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3231, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3232, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3233, 40, 47, 1200, 3, 50, 60, 70 },
			{ 3241, 40, 47, 1200, 3, 50, 60, 70 },
			{ 4307, 40, 47, 1200, 3, 50, 60, 70 },
			{ 4308, 40, 47, 1200, 3, 50, 60, 70 },
			{ 4309, 40, 47, 1200, 3, 50, 60, 70 },
			{ 4310, 40, 47, 1200, 3, 50, 60, 70 },
			{ 4311, 40, 47, 1200, 3, 50, 60, 70 },
			{ 5919, 40, 47, 1200, 3, 50, 60, 70 },
			{ 5920, 40, 47, 1200, 3, 50, 60, 70 },*/
			{ 3078, 1, 8, 10, 3, 10, 20, 30 },
			{ 3083, 1, 8, 10, 3, 10, 20, 30 },};

	public static boolean pickpocketNPC(Client c, int npc) {
		for (int i = 0; i < pickpocket.length; i++) {
			if (npc == pickpocket[i][0]) {
				return true;
			}
		}
		return false;
	}
	
	private static void failThieve(final Client c, final int i) {
	TaskHandler.submit(new Task(2, true) {
		@Override
		public void execute() {
			c.setHitDiff(pickpocket[i][4]);
			c.setHitUpdateRequired(true);
			c.playerLevel[3] -= pickpocket[i][4];
			c.getPA().refreshSkill(3);

			c.gfx100(80);
			c.startAnimation(404);

			c.lastThieve = System.currentTimeMillis() + 7500;
			c.sendMessage("...you fail to pickpocket the "
					+ Server.npcHandler.getNpcListName(pickpocket[i][0])
					+ "!");
			if (c.getRights().less(Rights.ADMINISTRATOR)) {
				c.playerStun = true;
			}
			this.cancel();
		}
		@Override
		public void onCancel() {
			
		}
		
	});
	
	TaskHandler.submit(new Task(13, true) {
		
		@Override
		public void execute() {
			c.playerStun = false;
			this.cancel();
		}
		@Override
		public void onCancel() {
			
			}		
		});
	}

	private static void completeThieve(final Client c, final int i) {
		int loot = pickpocket[i][3];
		int chance = Misc.random(20);
		String message = "...You successfully pickpocket the "
				+ Server.npcHandler.getNpcListName(pickpocket[i][0]) + ".";
		int zulrah = Misc.random(256);
		if (zulrah == 142) {
			c.getItems().addItemToBank(12938, 1);
			c.sendMessage("@red@A mysterious teleport has been added to your bank!");
		}
		if (chance == 1) {
			if (c.playerLevel[Player.playerAgility] >= pickpocket[i][5]) {
				loot = pickpocket[i][3] * 2;
				message = "You steal a double loot from the the "
						+ Server.npcHandler.getNpcListName(pickpocket[i][0])
						+ "!";
			}
		} else if (chance == 5) {
			if (c.playerLevel[Player.playerAgility] >= pickpocket[i][6]) {
				loot = pickpocket[i][3] * 3;
				message = "You steal a triple loot from the "
						+ Server.npcHandler.getNpcListName(pickpocket[i][0])
						+ "!";
			}
		} else if (chance == 10) {
			if (c.playerLevel[Player.playerAgility] >= pickpocket[i][7]) {
				loot = pickpocket[i][3] * 4;
				message = "You steal a tribble loot from the "
						+ Server.npcHandler.getNpcListName(pickpocket[i][0])
						+ "!";
			}
		}
		final int loot2 = loot;
		final String message2 = message;
		
		TaskHandler.submit(new Task(2, true) {
			@Override
			public void execute() {				
				c.sendMessage(message2);
				c.getPA().addSkillXP(pickpocket[i][2] * THIEVING_XP, 17);
				c.getItems().addItem(995, loot2);
				this.cancel();				
			}
			
			@Override
			public void onCancel() {
				
			}
			
		});
	}

	public static void attemptToPickpocket(final Client c, int npcId) {
		if (System.currentTimeMillis() - c.lastThieve < 2000 || c.playerStun) {
			return;
		}
		if (c.underAttackBy > 0 || c.underAttackBy2 > 0) {
			c.sendMessage("You can't pickpocket while in combat!");
			return;
		}
		for (int i = 0; i < pickpocket.length; i++) {
			if (npcId == pickpocket[i][0]) {
				if (!hasRequiredLevel(c, 17, pickpocket[i][1], "thieving",
						"pickpocket this")) {
					return;
				}
				c.sendMessage("You attempt to pickpocket the "
						+ Server.npcHandler.getNpcListName(pickpocket[i][0])
						+ "...");
				c.startAnimation(881);
				if (Misc.random(c.playerLevel[17] + 5) < Misc
						.random(pickpocket[i][1])) {
					failThieve(c, i);
				} else {
					completeThieve(c, i);
				}
				c.lastThieve = System.currentTimeMillis();
			}
		}
	}
}