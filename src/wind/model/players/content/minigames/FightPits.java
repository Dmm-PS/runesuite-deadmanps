package wind.model.players.content.minigames;

import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.util.Misc;

/**
 * @author Sanity
 */

public class FightPits {

	public int[] playerInPits = new int[200];

	private int GAME_TIMER = 140;
	private int GAME_START_TIMER = 40;

	private int gameTime = -1;
	private int gameStartTimer = 30;
	private int properTimer = 0;
	public int playersRemaining = 0;

	public String pitsChampion = "JalYt-Ket-";

	public void process() {
		if (properTimer > 0) {
			properTimer--;
			return;
		} else {
			properTimer = 4;
		}
		if (gameStartTimer > 0) {
			gameStartTimer--;
			updateWaitRoom();
		}
		if (gameStartTimer == 0) {
			startGame();
		}
		if (gameTime > 0) {
			gameTime--;
			if (playersRemaining == 1)
				endPitsGame(getLastPlayerName());
		} else if (gameTime == 0)
			endPitsGame("JalYt-Ket-");
	}

	public String getLastPlayerName() {
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] > 0)
				return PlayerHandler.players[playerInPits[j]].playerName;
		}
		return "JalYt-Ket-";
	}

	public void updateWaitRoom() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.InFightPitsWaiting()) {
					c.getPA().sendFrame126(
							"Next Game Begins In : "
									+ ((gameStartTimer * 3) + (gameTime * 3))
									+ " seconds.", 2805);
					c.getPA().sendFrame126("Champion: " + pitsChampion, 2806);
					c.getPA().sendFrame36(560, 1);
					c.getPA().walkableInterface(2804);
				}
			}
		}
	}

	// TODO add

	/**
	 * @note Updates players in game interfaces etc.
	 */
	/*
	 * public static boolean updateGame(Player c) {
	 * c.getPA().sendFrame126("Players Remaining: " + getListCount(PLAYING),
	 * 2805); c.getPA().sendFrame126("Champion: JalYt-Ket-" + pitsChampion,
	 * 2806); c.getPA().sendFrame36(560, 1); c.getPA().walkableInterface(2804);
	 * return true; }
	 */

	public void startGame() {
		if (getWaitAmount() < 2) {
			gameStartTimer = GAME_START_TIMER / 2;
			return;
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.InFightPitsWaiting())
					addToPitsGame(j);
			}
		}
		System.out.println("Fight Pits game started.");
		gameStartTimer = GAME_START_TIMER + GAME_TIMER;
		gameTime = GAME_TIMER;
	}

	public int getWaitAmount() {
		int count = 0;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.InFightPitsWaiting())
					count++;
			}
		}
		return count;
	}

	public void removePlayerFromPits(int playerId) {
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] == playerId) {
				Client c = (Client) PlayerHandler.players[playerInPits[j]];
				c.getPA().movePlayer(2399, 5173, 0);
				playerInPits[j] = -1;
				playersRemaining--;
				c.inPits = false;
				break;
			}
		}
	}

	public void endPitsGame(String champion) {
		@SuppressWarnings("unused")
		boolean giveReward = false;
		if (playersRemaining == 1)
			giveReward = true;

		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] < 0)
				continue;
			if (PlayerHandler.players[playerInPits[j]] == null)
				continue;
			Client c = (Client) PlayerHandler.players[playerInPits[j]];
			c.getPA().movePlayer(2399, 5173, 0);
			c.inPits = false;
		}
		playerInPits = new int[200];
		pitsChampion = champion;
		playersRemaining = 0;
		pitsSlot = 0;
		gameStartTimer = GAME_START_TIMER;
		gameTime = -1;
		System.out.println("Fight Pits game ended.");
	}

	private int pitsSlot = 0;

	public void addToPitsGame(int playerId) {
		if (PlayerHandler.players[playerId] == null)
			return;
		playersRemaining++;
		Client c = (Client) PlayerHandler.players[playerId];
		c.getPA().walkableInterface(-1);
		playerInPits[pitsSlot++] = playerId;
		c.getPA().movePlayer(2392 + Misc.random(12), 5139 + Misc.random(25), 0);
		c.inPits = true;
	}
}