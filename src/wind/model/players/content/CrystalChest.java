package wind.model.players.content;

import java.util.Random;

import wind.model.players.Client;
import wind.util.Misc;

/**
 * 
 * @author Fuzen Seth: Modifed by Sunny++
 * @info Represents simple crystal chest system.
 * @Since 2/10/15
 */
public class CrystalChest {

	private static final int ANIMATION = 881;
	private static int KEY = 989;
	private static int DRAGONSTONE = 1631;
	public static int TOOTH_HALVE = 985;
	public static int LOOP_HALVE = 987;
	private static int GOLD = 995;
	private static int RUNITE_BAR = 2363;
	private static int RUNITE_NOTED_BARS = 2364;
	public static int SHIELD_LEFT_HALF = 2366;
	public static int SHIELD_RIGHT_HALF = 2368;
	private static int SHIELD = 1187;

	/**
	 * The mininum cash reward if the loot is coins.
	 */
	private static final int MININUM_CASH_REWARD = 25000;

	/**
	 * The maxinum cash reward if the loot is coins.
	 */
	private static final int MAXINUM_CASH_REWARD = 250000;

	private static final int MININUM_BARS_REWARD = 1;
	private static final int MAXINUM_BARS_REWARD = 25;

	/**
	 * Rewards.
	 */
	private int[] REWARDS = { GOLD, 1712, 3751, 10828, 1127, 1079, 1215, 5698,
			DRAGONSTONE, TOOTH_HALVE, LOOP_HALVE, 1093, 526, 1969, 371,
			RUNITE_BAR, RUNITE_BAR, RUNITE_BAR };
	private int[] REWARDS2 = { GOLD, 1712, 3751, 10828, 1127, 1079, 1215, 5698,
			DRAGONSTONE, TOOTH_HALVE, LOOP_HALVE, 1093, 526, 1969, 371,
			RUNITE_BAR, RUNITE_BAR, RUNITE_BAR };
	private int[] REWARDS3 = { GOLD, 1712, 3751, 10828, 1127, 1079, 1215, 5698,
			DRAGONSTONE, TOOTH_HALVE, LOOP_HALVE, 1093, 526, 1969, 371,
			RUNITE_BAR, RUNITE_BAR, RUNITE_BAR };

	/**
	 * The singleton.
	 */
	private static CrystalChest singleton = new CrystalChest();
	/**
	 * The Random instance.
	 */
	private Random random = new Random();

	/**
	 * Opens the crystal chest.
	 * 
	 * @Param c
	 */
	public void openChest(Client c) {
		final int rewardItem = REWARDS[random.nextInt(REWARDS.length - 1)];
		final int rewardItem2 = REWARDS2[random.nextInt(REWARDS.length - 1)];
		final int rewardItem3 = REWARDS3[random.nextInt(REWARDS.length - 1)];
		if (!c.getItems().playerHasItem(KEY)) {
			c.getDH().sendStatement("The chest is locked.");
			return;
		}

		if (rewardItem == GOLD || rewardItem2 == GOLD || rewardItem3 == GOLD) {
			c.startAnimation(ANIMATION);
			c.getItems().addItem(
					GOLD,
					(int) Misc.randomDouble(MININUM_CASH_REWARD,
							MAXINUM_CASH_REWARD));
		}
		if (rewardItem == RUNITE_NOTED_BARS || rewardItem2 == RUNITE_NOTED_BARS
				|| rewardItem3 == RUNITE_NOTED_BARS) {
			c.startAnimation(ANIMATION);
			c.getItems().addItem(
					RUNITE_NOTED_BARS,
					(int) Misc.randomDouble(MININUM_BARS_REWARD,
							MAXINUM_BARS_REWARD));
		} else {
			c.startAnimation(ANIMATION);
			c.getItems().addItem(rewardItem, 1);
			c.getItems().addItem(rewardItem2, Misc.random(1));
			c.getItems().addItem(rewardItem3, Misc.random(1));
			c.getItems().deleteItem(KEY, 1);
			c.getDH().sendStatement("You have managed to loot the chest.");
			c.sendMessage("You loot the crystal chest!");
		}
	}

	public static void makeKey(Client c) {
		if (c.getItems().playerHasItem(TOOTH_HALVE, 1)
				&& c.getItems().playerHasItem(LOOP_HALVE, 1)) {
			c.getItems().deleteItem(TOOTH_HALVE, 1);
			c.getItems().deleteItem(LOOP_HALVE, 1);
			c.getItems().addItem(KEY, 1);
		}
	}
	public static void makeShield(Client c) {
		if (c.getItems().playerHasItem(SHIELD_LEFT_HALF, 1)
				&& c.getItems().playerHasItem(SHIELD_RIGHT_HALF, 1)) {
			c.getItems().deleteItem(SHIELD_RIGHT_HALF, 1);
			c.getItems().deleteItem(SHIELD_LEFT_HALF, 1);
			c.getItems().addItem(SHIELD, 1);
		}
	}

	/**
	 * Loads crystal chest singleton.
	 * 
	 * @return
	 */
	public static CrystalChest getSingleton() {
		return singleton;
	}
}