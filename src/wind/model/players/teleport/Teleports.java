package wind.model.players.teleport;

import wind.Config;
import wind.model.players.Client;

/**
 * @author 7Winds
 * Class for all the teleports in the game.
 * 
 */
public class Teleports {

	/**
	 * @param c
	 * @param actionButtonId
	 * Handles the clicking for clicking buttons
	 */
	public static void handleClick(Client c, int actionButtonId) {
		switch (actionButtonId) {
		
		case 114168:
			c.getTeleportHandler().submitTeleport(Config.BARROWS_X,
					Config.BARROWS_Y, 0);
			break;

		case 114169:
			c.getTeleportHandler().submitTeleport(Config.CASTLEWARS_X,
					Config.CASTLEWARS_Y, 0);
			break;

		case 114167:
			c.getTeleportHandler().submitTeleport(Config.DUEL_X, Config.DUEL_Y, 0);
			break;

		case 114170:
			c.getTeleportHandler().submitTeleport(Config.TZHAAR_X,
					Config.TZHAAR_Y, 0);
			break;

		case 114171:
			c.getTeleportHandler().submitTeleport(Config.FIGHTPITS_X,
					Config.FIGHTPITS_Y, 0);
			break;

		case 114172:
			c.getTeleportHandler().submitTeleport(Config.PC_X, Config.PC_Y, 0);
			break;

		case 114173:
			c.getTeleportHandler().submitTeleport(Config.NMZ_X, Config.NMZ_Y, 0);
			break;

		case 114174:
			c.getTeleportHandler().submitTeleport(Config.RD_X, Config.RD_Y, 1);
			break;

		case 114175:
			c.getTeleportHandler().submitTeleport(Config.WG_X, Config.WG_Y, 0);
			break;
		}
	}

}
