package wind.model.players.content.wilderness;

import wind.model.players.Client;
import wind.model.players.PlayerHandler;

/**
 * 
 * @author FuzenSeth
 *
 */
public class KillingStreak {

	/** The maxinum killstreak. */
	public static final int MAXINUM_STREAK = 1500;

	/** The KillingStreak's instance. */
	@SuppressWarnings("unused")
	private static KillingStreak streak = new KillingStreak();

	/***
	 * Processes the action on death.
	 */
	public static void processOnDeath(Client deadPlayer, Client player) {
		deadPlayer.setKillStreak(0);
		player.setKillStreak(player.getKillStreak() + 1);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				if (deadPlayer.getKillStreak() > 4)
					c2.sendMessage("@red@" + player.playerName
							+ " has just lost a kill streak of "
							+ deadPlayer.getKillStreak() + "!");

				switch (player.getKillStreak()) {
				case 2:
					c2.sendMessage("@red@News: "
							+ player.playerName
							+ " is on a [DOUBLE-KILL] Killstreak after defeating 2 enemies!");
					break;
				case 5:
					c2.sendMessage("@red@News: "
							+ player.playerName
							+ " is on a [KILLING-SPREE] Killstreak after defeating 5 enemies!");
					break;
				case 10:
					c2.sendMessage("@red@News: "
							+ player.playerName
							+ " is on a [UNTOUCHABLE] Killstreak after defeating 5 enemies!");

					break;
				case 20:
					c2.sendMessage("@red@News: " + player.playerName
							+ " is on a [RAMPAGE] Killstreak after defeating "
							+ player.getKillStreak() + " enemies!");
					break;
				}

			}
		}
		deadPlayer.setKillStreak(0);
	}

}

