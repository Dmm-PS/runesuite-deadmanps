package wind.model.players.content.minigames;

import wind.Config;
import wind.Server;
import wind.model.players.Client;
import wind.util.Misc;

/*
 *
 *@Author Ryan
 *
 */

public class WallSafe {
	@SuppressWarnings("unused")
	private Client c;

	public WallSafe(Client c) {
		this.c = c;
	}

	public static void getRandomReward(Client c) {
		int random = Misc.random(14);
		switch (random) {

		case 0:
			c.startAnimation(2247);
			c.getItems().addItem(995, 5000);
			c.getPA().addSkillXP(250 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);
			c.sendMessage("You crack the safe.");
			break;

		case 1:
			c.startAnimation(2247);
			c.getItems().addItem(995, 10000);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 2:
			c.startAnimation(2247);
			c.getItems().addItem(1617, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 3:
			c.startAnimation(2247);
			c.getItems().addItem(1619, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 4:
			c.startAnimation(2247);
			c.getItems().addItem(1621, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 5:
			c.startAnimation(2247);
			c.getItems().addItem(1623, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 6:
			c.startAnimation(2247);
			c.getItems().addItem(1625, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 7:
			c.startAnimation(2247);
			c.getItems().addItem(1627, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 8:
			c.startAnimation(2247);
			c.getItems().addItem(1629, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 9:
			c.startAnimation(2247);
			c.getItems().addItem(1631, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 10:
			c.startAnimation(2247);
			c.getItems().addItem(6571, 1);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 11:
			c.startAnimation(2247);
			c.getItems().addItem(995, 2500);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 12:
			c.startAnimation(2247);
			c.getItems().addItem(995, 6500);
			c.getPA().addSkillXP(500 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);

			c.sendMessage("You crack the safe.");
			break;

		case 13:
			appendHit(Misc.random(6), c);
			c.sendMessage("You fail to crack the safe.");
			break;

		}
	}

	@SuppressWarnings("static-access")
	public static void appendHit(int damage, Client c) {
		Server.playerHandler.players[c.playerId].setHitDiff(damage);
		Server.playerHandler.players[c.playerId].playerLevel[3] -= damage;
		c.getPA().refreshSkill(3);
		Server.playerHandler.players[c.playerId].setHitUpdateRequired(true);
		Server.playerHandler.players[c.playerId].updateRequired = true;
	}
}