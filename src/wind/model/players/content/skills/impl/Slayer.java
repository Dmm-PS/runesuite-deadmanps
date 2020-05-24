package wind.model.players.content.skills.impl;

import java.util.ArrayList;

import wind.Config;
import wind.Server;
import wind.model.players.Client;
import wind.util.Misc;

/**
 * @author Jason @ http://www.rune-server.org/members/jason
 * @objective The main reason why i'm starting this on a clean slate is to
 *            incorporate as many features involved with RS Slayer as possible.
 **/

public class Slayer {

	public static final int EASY_TASK = 1, MEDIUM_TASK = 2, HARD_TASK = 3;

	public static ArrayList<Integer> easyTask = new ArrayList<Integer>();
	public static ArrayList<Integer> mediumTask = new ArrayList<Integer>();
	public static ArrayList<Integer> hardTask = new ArrayList<Integer>();

	private static Client c;

	public Slayer(Client c) {
		Slayer.c = c;
	}

	public enum Task {
		ABERRANT_SPECTRE(7, 60, 2, "Slayer Tower", 3425, 3551, 1), 
		ABYSSAL_DEMON(415, 85,3, "Slayer Tower", 3200, 3200, 0), 
		//KRAKEN(474, 87,3, "Nieve's Cave", 3200, 3200, 0), 
		BANSHEE(414, 15, 2, "Slayer Tower", 3200, 3200, 0), 
		BASILISK(417, 40, 2, "Fremennik Slayer Dungeon", 3200, 3200, 0), 
		GIANT_BAT(2834, 1, 1, "Wilderness", 3200, 3200, 0), 
		BLACK_DEMON(2052, 1, 2, "Taverly Dungeon", 3200, 3200, 0), 
		BLACK_DRAGON(259, 1, 3, "Taverly Dungeon", 3200, 3200, 0), 
		BLOODVELD(484, 50, 2, "Slayer Tower", 3200, 3200, 0), 
		BLUE_DRAGON(265, 1, 2, "Taverly Dungeon", 3200, 3200, 0), 
		BRONZE_DRAGON(270, 1, 3, "Brimhaven Dungeon", 3200, 3200, 0), 
		CAVE_CRAWLER(406, 10, 1,"Fremennik Slayer Dungeon", 3200, 3200, 0), 
		CAVE_HORROR(3209, 58, 2,"Trollheim", 3200, 3200, 0), 
		COCKATRICE(419, 25, 2,"Fremennik Slayer Dungon", 3200, 3200, 0), 
		CRAWLING_HAND(457, 5, 1,"Slayer Tower", 3200, 3200, 0), 
		GIANT_RAT(2856, 1, 1,"Lumbridge Swamp", 3200, 3200, 0),
		//DAGGANOTH(2455, 1, 1, "Waterbirth Island", 3200, 3200, 0), 
		DARK_BEAST(4005, 90, 3, "Slayer Tower", 3200, 3200, 0), 
		//DUST_DEVIL(423, 65, 2,"Slayer Tower", 3200, 3200, 0), 
		EARTH_WARRIOR(2840, 1, 1, "Edgeville Dungeon", 3200, 3200, 0), 
		FIRE_GIANT(2075, 1, 2, "Brimhaven Dungeon", 3200, 3200, 0), 
		GARGOYLE(413, 75, 3,"Slayer Tower", 3200, 3200, 0), 
		GHOST(85, 1, 1, "Taverly Dungeon", 3200, 3200, 0), 
		GREATER_DEMON(2025, 1, 2, "Wilderness", 3200, 3200, 0), 
		GREATER_DEMON2(2025, 1, 3,"Wilderness", 3200, 3200, 0), 
		GREEN_DRAGON(260, 1, 2, "The Wilderness", 3200, 3200, 0), 
		GREEN_DRAGON2(260, 1, 3, "The Wilderness", 3200, 3200, 0), 
		HELLHOUND(104, 1, 3,"Taverly Dungeon", 3200, 3200, 0), 
		HILL_GIANT(2098, 1, 2, "Edgeville Dungeon", 3200, 3200, 0), 
		ICE_GIANT(2085, 1, 2, "Wilderness", 3200, 3200, 0), 
		ICE_WARRIOR(2841, 1, 2,"Wilderness", 3200, 3200, 0), 
		INFERNAL_MAGE(447, 45, 2,"Slayer Tower", 3200, 3200, 0),
		IRON_DRAGON(272, 1, 3, "Brimhaven Dungeon", 3200, 3200, 0), 
		JELLY(437, 52, 2, "Fremennik Slayer Dungeon", 3200, 3200, 0), 
		//KALPHITE(1153, 1, 2,"Kalphite Lair", 3200, 3200, 0), 
		KURASK(123, 70, 3, "Fremennik Slayer Dungeon", 3200, 3200, 0),
		KURASK2(123, 70, 2, "Fremennik Slayer Dungeon", 3200, 3200, 0), 
		LESSER_DEMON(2005, 1, 2, "Taverly Dungeon", 3200, 3200, 0), 
		MOSS_GIANT(2090, 1, 2, "Varrock Sewers (Use Edgeville pipe shortcut.)", 3200, 3200, 0), 
		NECHRYAELS(8, 80, 3, "Slayer Tower", 3200, 3200, 0), 
		PYREFIEND(433, 30, 1, "Fremennik Slayer Dungeon", 3200, 3200, 0), 
		RED_DRAGON(247, 1, 2, "Brimhaven Dungeon", 3200, 3200, 0), 
		RED_DRAGON2(247, 1, 3, "Brimhaven Dungeon", 3200, 3200, 0), 
		ROCKSLUG(421, 20, 1, "Fremennik Slayer Dungeon", 3200, 3200, 0), 
		//SKELETON(90, 1, 1,"Edgeville Dungeon", 3200, 3200, 0), 
		STEEL_DRAGON(274, 1, 3,"Brimhaven Dungeon", 3200, 3200, 0), 
		STEEL_DRAGON2(274, 1, 2,"Brimhaven Dungeon", 3200, 3200, 0), 
		TUROTH(426, 55, 2,"Fremennik Slayer Dungeon", 3200, 3200, 0), 
		TUROTH2(426, 55, 3,"Fremennik Slayer Dungeon", 3200, 3200, 0);



		private int npcId, levelReq, diff;
		private String location;
		@SuppressWarnings("unused")
		private int x;
		@SuppressWarnings("unused")
		private int y;
		@SuppressWarnings("unused")
		private int z;

		private Task(int npcId, int levelReq, int difficulty, String location, int x, int y, int z) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.location = location;
			this.diff = difficulty;
			this.x = x;
			this.y = y;
			this.z = z;
			
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public int getDifficulty() {
			return diff;
		}

		public String getLocation() {
			return location;
		}
	}

	public void resizeTable(int difficulty) {
		if (easyTask.size() > 0 || hardTask.size() > 0 || mediumTask.size() > 0) {
			easyTask.clear();
			mediumTask.clear();
			hardTask.clear();
		}
		for (Task slayerTask : Task.values()) {
			if (slayerTask.getDifficulty() == EASY_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq())
					easyTask.add(slayerTask.getNpcId());
				continue;
			} else if (slayerTask.getDifficulty() == MEDIUM_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq())
					mediumTask.add(slayerTask.getNpcId());
				continue;
			} else if (slayerTask.getDifficulty() == HARD_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					hardTask.add(slayerTask.getNpcId());
				}
				continue;
			}
		}
	}

	public int getRequiredLevel(int npcId) {
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.levelReq;
		return -1;
	}

	public String getLocation(int npcId) {
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.location;
		return "";
	}

	public boolean isSlayerNpc(int npcId) {
		for (Task task : Task.values()) {
			if (task.getNpcId() == npcId)
				return true;
		}
		return false;
	}

	public boolean isSlayerTask(int npcId) {
		if (isSlayerNpc(npcId)) {
			if (c.slayerTask == npcId) {
				return true;
			}
		}
		return false;
	}

	public int getDifficulty(int npcId) {
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.getDifficulty();
		return 1;
	}

	public static String getTaskName(int npcId) {
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.name().replaceAll("_", " ").replaceAll("2", "")
						.toLowerCase();
		return "";
	}

	public int getTaskId(String name) {
		for (Task task : Task.values())
			if (task.name() == name)
				return task.npcId;
		return -1;
	}

	public boolean hasTask() {
		return c.slayerTask > 0 || c.taskAmount > 0;
	}

	public void generateTask() {
		if (hasTask() && !c.needsNewTask) {
			c.getDH().sendDialogues(228, 1597);
			return;
		}
		if (hasTask() && c.needsNewTask) {
			int difficulty = getDifficulty(c.slayerTask);
			if (difficulty == EASY_TASK) {
				c.getDH().sendDialogues(228, 1597);
				c.needsNewTask = false;
				return;
			}
		}
		int taskLevel = getSlayerDifficulty();
		System.out.println("EASY :" + easyTask + "\nMEDIUM: " + mediumTask
				+ "\nHARD: " + hardTask + "");
		for (Task slayerTask : Task.values()) {
			if (slayerTask.getDifficulty() == taskLevel) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					resizeTable(taskLevel);
					if (!c.needsNewTask) {
						int task = getRandomTask(taskLevel);
						for (int i = 0; i < c.removedTasks.length; i++) {
							if (task == c.removedTasks[i]) {
								c.sendMessage("Unavailable task: " + task);
								generateTask();
								return;
							}
						}
						c.slayerTask = task;
						c.taskAmount = getTaskAmount(taskLevel);
					} else {
						int task = getRandomTask(getDifficulty(taskLevel - 1));
						for (int i = 0; i < c.removedTasks.length; i++) {
							if (task == c.removedTasks[i]) {
								c.sendMessage("Unavailable task: " + task);
								generateTask();
								return;
							}
						}
						c.slayerTask = task;
						c.taskAmount = getTaskAmount(getDifficulty(c.slayerTask) - 1);
						c.needsNewTask = false;
					}
					c.getDH().sendDialogues(102, 1597);
					c.sendMessage("You have been assigned @red@" + c.taskAmount
							+ " " + getTaskName(c.slayerTask) + "(s),@bla@ good luck "
							+ c.playerName + ".");
					c.sendMessage("You can find your task at " + getLocation(c.slayerTask) + ".");
					c.getPA().sendFrame126(
							"@or2@Task: @gre@" + c.taskAmount + " "
									+ Server.npcHandler.getNpcListName(c.slayerTask)
									+ " ", 29172);
					return;
				}
			}
		}
	}

	public int getTaskAmount(int diff) {
		switch (diff) {
		case 1:
			return 25 + Misc.random(75);
		case 2:
			return 45 + Misc.random(55);
		case 3:
			return 55 + Misc.random(65);
		}
		return 50 + Misc.random(50);
	}

	public int getRandomTask(int diff) {
		if (diff == EASY_TASK) {
			return easyTask.get(Misc.random(easyTask.size() - 1));
		} else if (diff == MEDIUM_TASK) {
			return mediumTask.get(Misc.random(mediumTask.size() - 1));
		} else if (diff == HARD_TASK) {
			return hardTask.get(Misc.random(hardTask.size() - 1));
		}
		return easyTask.get(Misc.random(easyTask.size() - 1));
	}

	public int getSlayerDifficulty() {
		if (c.combatLevel > 0 && c.combatLevel <= 45) {
			return EASY_TASK;
		} else if (c.combatLevel > 45 && c.combatLevel <= 90) {
			return MEDIUM_TASK;
		} else if (c.combatLevel > 90) {
			return HARD_TASK;
		}
		return EASY_TASK;
	}

	public void handleInterface(String shop) {
		if (shop.equalsIgnoreCase("buy")) {
			c.getPA().sendFrame126("Slayer Points: " + c.slayerPoints, 41011);
			c.getPA().showInterface(41000);
		} else if (shop.equalsIgnoreCase("learn")) {
			c.getPA().sendFrame126("Slayer Points: " + c.slayerPoints, 41511);
			c.getPA().showInterface(41500);
		} else if (shop.equalsIgnoreCase("assignment")) {
			c.getPA().sendFrame126("Slayer Points: " + c.slayerPoints, 42011);
			updateCurrentlyRemoved();
			c.getPA().showInterface(42000);
		}
	}

	public void cancelTask() {
		if (!hasTask()) {
			c.sendMessage("You must have a task to cancel first.");
			return;
		}
		if (c.slayerPoints < 30) {
			c.sendMessage("This requires atleast 30 slayer points, which you don't have.");
			c.getPA().removeAllWindows();
			return;
		}
		c.sendMessage("You spend 30 points to cancel your task. You now have @red@"+c.slayerPoints+"@bla@ Slayer points left.");
		c.sendMessage("You have cancelled your current task of " + c.taskAmount
				+ " " + getTaskName(c.slayerTask) + "(s).");
		c.getPA().sendFrame126(
				"@or2@Task: @red@None", 29172);
		c.slayerTask = -1;
		c.taskAmount = 0;
		c.slayerPoints -= 30;
		c.getPA().closeAllWindows();
		c.getPA().cancelTeleportTask();
	}

	public void removeTask() {
		int counter = 0;
		if (!hasTask()) {
			c.sendMessage("You must have a task to remove first.");
			return;
		}
		if (c.slayerPoints < 100) {
			c.sendMessage("This requires atleast 100 slayer points, which you don't have.");
			return;
		}
		for (int i = 0; i < c.removedTasks.length; i++) {
			if (c.removedTasks[i] != -1) {
				counter++;
			}
			if (counter == 4) {
				c.sendMessage("You don't have any open slots left to remove tasks.");
				return;
			}
			if (c.removedTasks[i] == -1) {
				c.removedTasks[i] = c.slayerTask;
				c.slayerPoints -= 100;
				c.slayerTask = -1;
				c.taskAmount = 0;
				c.sendMessage("Your current slayer task has been removed, you can't obtain this task again.");
				updateCurrentlyRemoved();
				return;
			}
		}
	}

	public void updatePoints() {
		c.getPA().sendFrame126("Slayer Points: " + c.slayerPoints, 41011);
		c.getPA().sendFrame126("Slayer Points: " + c.slayerPoints, 41511);
		c.getPA().sendFrame126("Slayer Points: " + c.slayerPoints, 42011);
	}

	public void updateCurrentlyRemoved() {
		int line[] = { 42014, 42015, 42016, 42017 };
		for (int i = 0; i < c.removedTasks.length; i++) {
			if (c.removedTasks[i] != -1) {
				c.getPA().sendFrame126(Slayer.getTaskName(c.removedTasks[i]),
						line[i]);
			} else {
				c.getPA().sendFrame126("", line[i]);
			}
		}
	}

	public void buySlayerExperience() {
		if (System.currentTimeMillis() - c.buySlayerTimer < 500)
			return;
		if (c.slayerPoints < 50) {
			c.sendMessage("You need at least 50 slayer points to gain 32,500 Experience.");
			return;
		}
		c.buySlayerTimer = System.currentTimeMillis();
		c.slayerPoints -= 50;
		if (Config.WEEKEND_DOUBLE_EXP)
			c.getPA().addSkillXP(16250, 18);
		else
			c.getPA().addSkillXP(32500, 18);
		c.sendMessage("You spend 50 slayer points and gain 32,500 experience in slayer.");
		updatePoints();
	}

	public void buySlayerDart() {
		if (System.currentTimeMillis() - c.buySlayerTimer < 500)
			return;
		if (c.slayerPoints < 35) {
			c.sendMessage("You need at least 35 slayer points to buy Slayer darts.");
			return;
		}
		if (c.getEquipment().freeSlots() < 2
				&& !c.getItems().playerHasItem(560)
				&& !c.getItems().playerHasItem(558)) {
			c.sendMessage("You need at least 2 free lots to purchase this.");
			return;
		}

		c.buySlayerTimer = System.currentTimeMillis();
		c.slayerPoints -= 35;
		c.sendMessage("You spend 35 slayer points and aquire 250 casts of Slayer darts.");
		c.getItems().addItem(558, 1000);
		c.getItems().addItem(560, 250);
		updatePoints();
	}

	public void buyBroadArrows() {
		if (System.currentTimeMillis() - c.buySlayerTimer < 500)
			return;
		if (c.slayerPoints < 25) {
			c.sendMessage("You need at least 25 slayer points to buy Broad arrows.");
			return;
		}
		if (c.getEquipment().freeSlots() < 1
				&& !c.getItems().playerHasItem(4160)) {
			c.sendMessage("You need at least 1 free lot to purchase this.");
			return;
		}
		c.buySlayerTimer = System.currentTimeMillis();
		c.slayerPoints -= 25;
		c.sendMessage("You spend 35 slayer points and aquire 250 Broad arrows.");
		c.getItems().addItem(4160, 250);
		updatePoints();
	}

	public void buyRespite() {
		if (System.currentTimeMillis() - c.buySlayerTimer < 1000)
			return;
		if (c.slayerPoints < 400) {
			c.sendMessage("You need at least 400 slayer points to buy a Slayer Helmet");
			return;
		}
		c.buySlayerTimer = System.currentTimeMillis();
		c.slayerPoints -= 400;
		c.sendMessage("You spend 400 slayer points and aquire a useful Slayer's Helmet.");
		c.getItems().addItem(11864, 1);
		updatePoints();
	}

}