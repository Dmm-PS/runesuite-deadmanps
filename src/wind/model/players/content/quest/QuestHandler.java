package wind.model.players.content.quest;

import wind.Constants;
import wind.model.players.Client;
import wind.model.players.content.quest.quests.DesertTreasure;
import wind.model.players.content.quest.quests.LunarDiplomacy;
import wind.model.players.content.quest.quests.TheStrykeWyrm;
import wind.world.ItemHandler;

public class QuestHandler {

	/**
	 * @author Ochroid
	 */

	private final Client c;

	public QuestHandler(final Client Client) {
		c = Client;
	}

	/**
	 * Variable Declarations
	 */

	public static final int MAX_QUEST_POINTS = 3;

	public int questId;

	/*
	 * Rewards - Quest ID || Quest Points Awarded || Coins Awarded || Skill For
	 * Reward || XP Amount For Skill || Item Reward || Item Amount
	 */
	private final int[][] getQuestRewards = { { 0, 1, 200000, 0, 0, 0, 0 },
			{ 1, 2, 2000000, 6, 300000, 0, 0 },
			{ 2, 1, 500000, 6, 300000, 0, 0 } };

	public void quest(String quest, String... line) {
		if (line.length > 6) {
			return;
		}
		c.getPA().showInterface(12140);
		c.getPA().sendFrame126("You have completed " + quest + ".", 12144);
		c.getPA().sendFrame126("" + c.questPoints + "", 12147);
		for (int i = 0; i < line.length; i++) {
			c.getPA().sendFrame126(line[i], 12150 + i);
		}
	}

	public void getStatusFrame(Client c) {
		if (c.aGoodStartStatus >= 1 && c.aGoodStartStatus <= 19) {
			// c.getPA().sendFrame126("@yel@A Fresh Start", 16034);
		} else if (c.aGoodStartStatus == 20) {
			// c.getPA().sendFrame126("@gre@A Fresh Start", 16034);
		} else {
			// c.getPA().sendFrame126("@red@A Fresh Start", 16034);
		}
	}

	public boolean questItem(int item) {
		switch (item) {
		case 1508:
			return true;
		default:
			return false;
		}
	}

	public void handleItem(Client c, int item) {
		switch (item) {
		case 1508:
			c.sendMessage("This note is none of your business.");
			break;
		}
	}

	public void clearQuestInterface() {
		for (int x = 0; x < Constants.QuestInterface.length; x++) {
			c.getPA().sendFrame126("", Constants.QuestInterface[x]);
		}
	}

	public void getName(int quest) {
		switch (quest) {
		case 0:
			c.sendMessage("Congratulations, you have completed the quest, The Strykewyrm.");
			break;

		case 1:
			c.sendMessage("Congratulations, you have completed the quest, Lunar Diplomacy.");
			break;

		case 2:
			c.sendMessage("Congratulations, you have completed the quest, Desert Treasure.");
			break;
		}
	}

	public void getCompletionInterface(int quest) {
		switch (quest) {
		case 0:
			TheStrykeWyrm.endInt(c);
			break;
		case 1:
			LunarDiplomacy.endInt(c);
			break;
		case 2:
			DesertTreasure.endInt(c);
			break;
		}
	}

	public void getCompletion(int quest) {
		switch (quest) {
		case 0:
			quest("The Strykewyrm", "1 Quest Point", "200K Coins",
					"Ability to slay Strykewyrms", " ", " ", " ");
			break;
		case 1:
			quest("Lunar Diplomacy", "2 Quest Points", "2M Coins",
					"300K Magic Experience", "Ability to use Lunar spellbook",
					" ", " ");
			break;
		case 2:
			quest("Desert Treasure", "1 Quest Point", "500K Coins",
					"300K Magic Experience",
					"Ability to use Ancient spellbook", " ", " ");
			break;
		}
	}

	public void getStartingInterface(int quest) {
		switch (quest) {
		case 0:
			TheStrykeWyrm.startInt(c);
			break;
		case 1:
			LunarDiplomacy.startInt(c);
			break;
		case 2:
			DesertTreasure.startInt(c);
			break;
		}
	}

	/**
	 * Handles Everything When Quest Is Complete
	 **/

	public void completeQuest(Client c, int quest) {
		getQuestRewards(quest);
		getCompletion(quest);
		getStatusFrame(c);
	}

	/**
	 * Handles Giving Basic Rewards
	 **/

	public void getQuestRewards(int quest) {
		boolean hasRecieved = false;
		if (hasRecieved == true) {
			return;
		}
		hasRecieved = true;
		final int coins = getQuestRewards[quest][2];
		final int qPoints = getQuestRewards[quest][1];
		final int skill = getQuestRewards[quest][3];
		final int XP = getQuestRewards[quest][4];
		final int item = getQuestRewards[quest][5];
		final int amount = getQuestRewards[quest][6];
		int invSlotCount = c.getEquipment().freeSlots();
		getName(quest);
		c.getPA().addSkillXP(XP, skill);
		c.questPoints += qPoints;
		if (invSlotCount >= 2) {
			c.getItems().addItem(995, coins);
			c.getItems().addItem(item, amount);
		} else {
			c.sendMessage("Your reward was dropped on the ground, as you did not have enough inventory space.");
			ItemHandler.createGroundItem(c, 995, c.getX(), c.getY(),
					coins, c.playerId);
			ItemHandler.createGroundItem(c, item, c.getX(), c.getY(),
					amount, c.playerId);
		}
	}

}
