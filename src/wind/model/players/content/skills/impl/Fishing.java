package wind.model.players.content.skills.impl;

import wind.event.CycleEventContainer;
import wind.Config;
import wind.model.items.Item;
import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.content.skills.SkillHandler;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

public class Fishing extends SkillHandler {
	
    public static final Object FISHING_KEY = new Object();

	public enum Spot {
		LURE(1506, new int[] { 335, 331 }, 309, 314, new int[] { 20, 30 },false, new int[] { 50, 70 }, 623), 
		CAGE(1510, new int[] { 377 },301, -1, new int[] { 40 }, false, new int[] { 90 }, 619), 
		BIGNET(1520, new int[] { 353, 341, 363 }, 305, -1, new int[] { 16, 23,46 }, false, new int[] { 20, 45, 100 }, 620), 
		SMALLNET(1497, new int[] { 317, 321 }, 303, -1, new int[] { 1, 15 },false, new int[] { 10, 40 }, 621),
		//MONKNET(326, new int[] { 7944 }, 303, -1, new int[] { 68 }, false, new int[] { 120 }, 621), 
		HARPOON(1510, new int[] { 359, 371 }, 311, -1, new int[] { 35, 50 }, true, new int[] { 80, 100 }, 618), 
		HARPOON2(1520, new int[] { 383 }, 311, -1, new int[] { 76 }, true, new int[] { 110 }, 618), 
		BAIT(1497, new int[] { 327, 345 }, 307, 313, new int[] { 5, 10 }, true, new int[] { 20, 30 }, 623);

		int npcId, equipment, bait, anim;
		int[] itemIDs, fishingReqs, xp;
		boolean second;

		Spot(int npcId, int[] itemIDs, int equipment, int bait, int[] fishingReqs, boolean second, int[] xp,
				int anim) {
			this.npcId = npcId;
			this.itemIDs = itemIDs;
			this.equipment = equipment;
			this.bait = bait;
			this.fishingReqs = fishingReqs;
			this.second = second;
			this.xp = xp;
			this.anim = anim;
		}

		public int getNPCId() {
			return npcId;
		}

		public int[] getRawFish() {
			return itemIDs;
		}

		public int getEquipment() {
			return equipment;
		}

		public int getBait() {
			return bait;
		}

		public int[] getLevelReq() {
			return fishingReqs;
		}

		public boolean getSecond() {
			return second;
		}

		public int[] getXp() {
			return xp;
		}

		public int getAnim() {
			return anim;
		}
	}

	public static Spot forSpot(int npcId, boolean secondClick) {
		for (Spot s : Spot.values()) {
			if (secondClick) {
				if (s.getSecond()) {
					if (s.getNPCId() == npcId) {
						if (s != null)
							return s;
					}
				}
			} else {
				if (s.getNPCId() == npcId) {
					if (s != null)
						return s;
				}
			}
		}
		return null;
	}

	public static void setupFishing(Client c, Spot s) {
		if (c.playerLevel[Player.playerFishing] >= s.getLevelReq()[0]) {
			if (c.getItems().playerHasItem(s.getEquipment())) {
				if (c.getEquipment().freeSlots() < 1) {
					c.sendMessage("Your inventory is full!");
					return;
				}
				if (s.getBait() != -1) {
					if (!c.isFishing)
						return;
					if (c.getItems().playerHasItem(s.getBait(), 1)) {
						startFishing(c, s);
					} else {
						c.sendMessage("You don't have enough bait to fish here.");
						c.startAnimation(65535);
					}
				} else {
					c.isFishing = true;
					startFishing(c, s);
				}
			} else {
				c.sendMessage("You need a "
						+ Item.getItemName(s.getEquipment()).toLowerCase()
						+ " to fish here.");
			}
		} else {
			c.sendMessage("You need a fishing level of at least "
					+ s.getLevelReq()[0] + " to fish here.");
		}
	}

	public static void startFishing(final Client c, final Spot s) {
		c.isFishing = false;
		final int wat = Misc.random(100) >= 70 ? getMax(c, s.fishingReqs)
				: (getMax(c, s.fishingReqs) != 0 ? getMax(c, s.fishingReqs) - 1
						: 0);
		c.startAnimation(s.getAnim());
		Task sFishing = (new Task(3, false) {
			int cycle = 2;			
			@Override
			public void execute() {
				if (cycle > 0) {
					cycle--;
				}
				if (c.getEquipment().freeSlots() < 1) {
					c.sendMessage("Your inventory is full!");
					resetFishing(c);
					this.cancel();
				}
				switch (cycle) {

				case 1:
					if (c.getEquipment().freeSlots() < 1) {
						c.isFishing = false;
						c.sendMessage("Your inventory is full!");
						this.cancel();
						return;
					} else {
						if (s.getBait() != -1) {
							c.getItems().deleteItem(s.getBait(),
									c.getItems().getItemSlot(s.getBait()), 1);
						}
						if (Misc.random(250) == 0) {
						//	RiverTroll.spawnRiverTroll(c);
							resetFishing(c);
							//this.cancel();
						}
						//c.isFishing = true;
						c.startAnimation(s.getAnim());
						// c.sendMessage("1");
						c.getItems().addItem(s.getRawFish()[wat], 1);
						c.sendMessage("You catch a "
								+ Item.getItemName(s.getRawFish()[wat])
								.toLowerCase().replace("_", " ") + ".");
						c.getPA().addSkillXP(
								s.getXp()[wat] * Config.FISHING_EXPERIENCE,
								Player.playerFishing);
						setupFishing(c, s);
					}
					break;

				case 2:
					if (c.getEquipment().freeSlots() < 1) {
						c.sendMessage("Your inventory is full!");
						c.isFishing = false;
						this.cancel();
						return;
					} else {
						if (s.getBait() != -1) {
							c.getItems().deleteItem(s.getBait(),
									c.getItems().getItemSlot(s.getBait()), 1);
						}
						if (Misc.random(250) == 0) {
						//	RiverTroll.spawnRiverTroll(c);
							resetFishing(c);
							this.cancel();
							return;
						}
						c.isFishing = true;
						c.startAnimation(s.getAnim());
						// c.sendMessage("2");
						c.getItems().addItem(s.getRawFish()[wat], 1);
						c.sendMessage("You catch a "
								+ Item.getItemName(s.getRawFish()[wat])
								.toLowerCase().replace("_", " ") + ".");
						c.getPA().addSkillXP(
								s.getXp()[wat] * Config.FISHING_EXPERIENCE,
								Player.playerFishing);
						setupFishing(c, s);
					}
					break;

				default:
					if (c.getEquipment().freeSlots() < 1) {
						// c.sendMessage("default");
						c.isFishing = false;
						resetFishing(c);
						this.cancel();
					} else {
						//c.isFishing = true;
						return;
					}
					break;
				}
			}

			@Override
			public void onCancel() {
				resetFishing(c);
				return;
			}			
		});
		c.getPA().closeAllWindows();
		sFishing.attach(FISHING_KEY);
		TaskHandler.cancel(FISHING_KEY);
		TaskHandler.submit(sFishing);
	}


	public static void resetFishing(Client c) {
		c.startAnimation(65535);
		c.turnPlayerTo(-1, -1);
		c.isFishing = false;
		c.doAmount = 0;
		for (int i = 0; i < 9; i++) {
			c.playerSkillProp[8][i] = -1;
		}
	}

	public static int getMax(Client c, int[] reqs) {
		int tempInt = -1;
		for (int i : reqs) {
			if (c.playerLevel[Player.playerFishing] >= i) {
				tempInt++;
			}
		}
		return tempInt;
	}
	private static boolean hasFishingEquipment(Client c, int equipment) {
		if (!c.getItems().playerHasItem(equipment)) {
			if (equipment == 311) {
				if (!c.getItems().playerHasItem(311)
						&& !c.getItems().playerHasItem(10129)
						&& c.playerEquipment[3] != 10129) {
					c.getItems();
					c.sendMessage("You need a "
							+ ItemAssistant.getItemName(equipment)
							+ " to fish here.");
					return false;
				}
			} else {
				c.getItems();
				c.sendMessage("You need a "
						+ ItemAssistant.getItemName(equipment)
						+ " to fish here.");
				return false;
			}
		}
		return true;
	}
	private final static int getTimer(Client c, int npcId) {
		switch (npcId) {
		case 1:
			return 2;
		case 2:
			return 3;
		case 3:
			return 4;
		case 4:
			return 4;
		case 5:
			return 4;
		case 6:
			return 5;
		case 7:
			return 5;
		case 8:
			return 5;
		case 9:
			return 5;
		case 10:
			return 5;
		case 11:
			return 9;
		case 12:
			return 9;
		case 13:
			return 6;
		default:
			return -1;
		}
	}
	public final static int fishingTimer(Client c, int npcId) {
		return getTimer(c, npcId) + 5 + c.playerLevel[Player.playerFishing];
	}
	private static int[][] data = {
			{ 1, 1, 303, -1, 317, 10, 621, 321, 15, 30 }, // SHRIMP + ANCHOVIES
			{ 2, 5, 309, 313, 327, 20, 622, 345, 10, 30 }, // SARDINE + HERRING
			{ 3, 16, 305, -1, 353, 20, 620, -1, -1, -1 }, // MACKEREL
			{ 3, 20, 309, -1, 335, 50, 622, 331, 30, 70 }, // TROUT
			{ 5, 23, 305, -1, 341, 45, 619, 363, 46, 100 }, // BASS + COD
			{ 6, 25, 309, 313, 349, 60, 622, -1, -1, -1 }, // PIKE
			{ 7, 35, 311, -1, 359, 80, 618, 371, 50, 100 }, // TUNA + SWORDIE
			{ 8, 40, 301, -1, 377, 90, 619, -1, -1, -1 }, // LOBSTER
			{ 9, 62, 305, -1, 7944, 120, 620, -1, -1, -1 }, // MONKFISH
			{ 10, 76, 311, -1, 383, 110, 618, -1, -1, -1 }, // SHARK
			{ 11, 86, 305, -1, 11934, 38, 621, -1, -1, -1 }, // DARK CRAB
			{ 12, 81, 305, -1, 389, 130, 621, -1, -1, -1 }, // MANTA RAY
			{ 13, 81, 305, -1, 3142, 130, 621, -1, -1, -1 }, // karambwan
	};

	public static void attemptdata(final Client c, int npcId) {
		if (!noInventorySpace(c, "fishing")) {
			c.sendMessage("You must have space in your inventory to start fishing.");
			return;
		}
		// resetFishing(c);
		for (int i = 0; i < data.length; i++) {
			if (npcId == data[i][0]) {
				if (c.playerLevel[Player.playerFishing] < data[i][1]) {
					c.sendMessage("You haven't got high enough fishing level to fish here!");
					c.sendMessage("You at list need the fishing level of "
							+ data[i][1] + ".");
					c.getPA().sendStatement(
							"You need the fishing level of " + data[i][1]
									+ " to fish here.");
					return;
				}
				if (data[i][3] > 0) {
					if (!c.getItems().playerHasItem(data[i][3])) {
						c.getItems();
						c.sendMessage("You haven't got any "
								+ ItemAssistant.getItemName(data[i][3]) + "!");
						c.getItems();
						c.sendMessage("You need "
								+ ItemAssistant.getItemName(data[i][3])
								+ " to fish here.");
						return;
					}
				}
				c.playerSkillProp[10][0] = data[i][6]; // ANIM
				c.playerSkillProp[10][1] = data[i][4]; // FISH
				c.playerSkillProp[10][2] = data[i][5]; // XP
				c.playerSkillProp[10][3] = data[i][3]; // BAIT
				c.playerSkillProp[10][4] = data[i][2]; // EQUIP
				c.playerSkillProp[10][5] = data[i][7]; // sFish
				c.playerSkillProp[10][6] = data[i][8]; // sLvl
				c.playerSkillProp[10][7] = data[i][4]; // FISH
				c.playerSkillProp[10][8] = data[i][9]; // sXP
				c.playerSkillProp[10][9] = Misc.random(1) == 0 ? 7 : 5;
				c.playerSkillProp[10][10] = data[i][0]; // INDEX

				if (!hasFishingEquipment(c, c.playerSkillProp[10][4])) {
					return;
				}

				c.turnPlayerTo(c.objectX, c.objectY);
				c.sendMessage("You start fishing.");
				c.startAnimation(c.playerSkillProp[10][0]);

				/*if (c.playerSkilling[10]) {
					System.out.println("PLS");
					resetFishing(c);
					return;
				}
				c.stopPlayerSkill = true;
				c.playerSkilling[10] = true;*/
				final int task = c.getTask();
				c.setSkilling(new wind.event.CycleEvent() {

					@Override
					public void execute(
							CycleEventContainer container) {
						if (!c.checkTask(task)) {
							resetFishing(c);
							container.stop();
							return;
						}
						/*if (!c.stopPlayerSkill) {
							resetFishing(c);
							container.stop();
						}
						if (!c.playerSkilling[10]) {
							resetFishing(c);
							container.stop();
						}*/
						if (c.playerSkillProp[10][5] > 0) {
							if (c.playerLevel[Player.playerFishing] >= c.playerSkillProp[10][6]) {
								c.playerSkillProp[10][1] = c.playerSkillProp[10][Misc
										.random(1) == 0 ? 7 : 5];
							}
						}
						if (c.playerSkillProp[10][1] > 0) {
							c.getItems();
							c.sendMessage("You catch a "
									+ ItemAssistant
											.getItemName(
													c.playerSkillProp[10][1])
									+ ".");
						}
						if (c.playerSkillProp[10][1] > 0) {
							c.getItems().addItem(
									c.playerSkillProp[10][1], 1);
							c.startAnimation(c.playerSkillProp[10][0]);
						}
						if (c.playerSkillProp[10][2] > 0) {
							c.getPA().addSkillXP(
									c.playerSkillProp[10][2]
											* FISHING_XP,
									Player.playerFishing);
						}
						if (c.playerSkillProp[10][3] > 0) {
							c.getItems()
									.deleteItem(
											c.playerSkillProp[10][3],
											c.getItems()
													.getItemSlot(
															c.playerSkillProp[10][3]),
											1);
							if (!c.getItems().playerHasItem(
									c.playerSkillProp[10][3])) {
								c.getItems();
								c.sendMessage("You haven't got any "
										+ ItemAssistant
												.getItemName(
														c.playerSkillProp[10][3])
										+ " left!");
								c.getItems();
								c.sendMessage("You need "
										+ ItemAssistant
												.getItemName(
														c.playerSkillProp[10][3])
										+ " to fish here.");
								container.stop();
							}
						}
						if (!hasFishingEquipment(c,
								c.playerSkillProp[10][4])) {
							container.stop();
						}
						if (!noInventorySpace(c, "fishing")) {
							container.stop();
						}
						if (Misc.random(15) == 0) {
							container.stop();
						}
					}

					public void stop() {
						resetFishing(c);
					}

					
				});
				wind.event.CycleEventHandler.getSingleton().addEvent(c, c.getSkilling(), fishingTimer(c, npcId));
			}
		}
	}
}