package wind.model.players.content.skills.impl.woodcutting;

import java.util.Random;

import wind.model.players.Client;
import wind.world.ItemHandler;

/**
 * @author Zack/optimum
 *
 */
public class BirdsNests {

	/**
	 * This will randomly generate a percentage
	 */
	static Random rnd = new Random();

	/**
	 * This contains all of the nest item ids.
	 */
	static final int[] NESTS = { 5070, 5071, 5072, 5073, 5074, 5075 };

	/**
	 * This holds all the ring data: ItemId, Percentage
	 */
	static final int[][] RINGS = { { 1635, 35 }, { 1637, 75 }, { 1639, 90 },
			{ 1641, 99 }, { 1643, 100 } };

	/**
	 * This holds all the seeds data: ItemId, Percentage
	 */
	static final int[][] SEEDS = { { 5317, 1 }, { 5290, 3 }, { 5289, 5 },
			{ 5288, 9 }, { 5287, 13 }, { 5286, 19 }, { 5285, 28 },
			{ 5284, 38 }, { 5283, 54 }, { 5316, 55 }, { 5315, 58 },
			{ 5314, 64 }, { 5313, 79 }, { 5312, 100 }, };

	/**
	 * This method will check if the item being clicked is a nest.
	 * 
	 * @param itemId
	 *            - Is the item being clicked
	 * @return if the itemIf is a nest
	 */
	public boolean isNest(int itemId) {
		for (int i : NESTS) {
			if (i == itemId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This handles the opening of a birds nest
	 * 
	 * @param player
	 * @param itemId
	 *            - this is the item id thats being clicked
	 */
	public void handleOpen(Client player, int itemId) {
		if (player.getEquipment().freeSlots() < 1) {
			player.sendMessage("You need atleast 1 free inventory space to open this!");
			return;
		}
		switch (itemId) {
		case 5073:
			openSeedNest(player);
			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(5075, 1);
			return;
		case 5074:
			openRingNest(player);
			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(5075, 1);
			return;
		case 5070:
		case 5071:
		case 5072:
			player.getItems().addItem(itemId + 6, 1);
			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(5075, 1);
			return;
		}
	}

	/**
	 * This opens the Seed Nests and gives the player a random seed item based
	 * on the seed percentage.
	 * 
	 * @param player
	 */
	void openSeedNest(Client player) {
		int random = 1 + rnd.nextInt(100);
		for (int i = 0; i < SEEDS.length; i++) {
			if (random <= SEEDS[i][1]) {
				player.getItems().addItem(SEEDS[i][0], 1);
				return;
			}
		}
	}

	/**
	 * This opens the Ring Nests and gives the player a random ring item based
	 * on the ring percentage.
	 * 
	 * @param player
	 */
	void openRingNest(Client player) {
		int random = 1 + rnd.nextInt(100);
		for (int i = 0; i < RINGS.length; i++) {
			if (random <= RINGS[i][1]) {
				player.getItems().addItem(RINGS[i][0], 1);
				return;
			}
		}
	}

	/**
	 * This method drops a birds nest right at a players feet at a chance of 500
	 */
	public static void dropNest(Client player) {
		if (rnd.nextInt(500) == 0) {
			ItemHandler.createGroundItem(player,
					NESTS[rnd.nextInt(NESTS.length)], player.getX(),
					player.getY(), 1, player.playerId);
			return;
		}
	}
}