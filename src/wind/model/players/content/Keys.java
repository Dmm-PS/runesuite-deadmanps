package wind.model.players.content;

import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * Class Keys handle Keys
 * 
 * @author Pandemia / Paprd1 - Finished 31.12.2014!
 */

public class Keys {

	private static final int[] CHEST_REWARDS = { 1079, 1093, 1215, 1969, 371,
			2363, 451 }; // red keys = very rare key
	private static final int[] CHEST_REWARDS1 = { 1079, 1093, 1215, 1969, 371,
			2363, 451 }; // blue keys = rare key
	private static final int[] CHEST_REWARDS2 = { 1079, 1093, 1215, 1969, 371,
			2363, 451 }; // green keys = uncommon but rare
	private static final int[] CHEST_REWARDS3 = { 1079, 1093, 1215, 1969, 371,
			2363, 451 }; // yellow keys = uncommon
	private static final int[] CHEST_REWARDS4 = { 1079, 1093, 1215, 1969, 371,
			2363, 451 }; // orange keys = common
	private static final int[] CHEST_REWARDS5 = { 1079, 1093, 1215, 1969, 371,
			2363, 451 }; // pink keys = very common
	public static final int KEYRED = 1543; // used
	public static final int KEYORANGE = 1544;// used
	public static final int KEYYELLOW = 1545;// used
	public static final int KEYBLUE = 1546; // used
	public static final int KEYPINK = 1547;
	public static final int KEYGREEN = 1548;// used
	private static final int REGULAR = 536;
	private static final int OPEN_ANIMATION = 881;
	private static final int OPEN_GFX = 542;

	public static boolean canOpen(Client c) {
		if (c.getItems().playerHasItem(KEYRED)) {
			if (c.getItems().playerHasItem(KEYORANGE)) {
				if (c.getItems().playerHasItem(KEYYELLOW)) {
					if (c.getItems().playerHasItem(KEYBLUE)) {
						if (c.getItems().playerHasItem(KEYPINK)) {
							if (c.getItems().playerHasItem(KEYGREEN)) {
							}
						}
					}
				}
				return true;

			} else {
				c.sendMessage("The chest is locked");
				return false;

			}

		}
		return true;
	}

	public static void rewardChest(final Client c, final int id, final int x,
			final int y) {
		if (canOpen(c)) {
			c.sendMessage("You completed the treasure and opened the chest!");
			c.getItems().deleteItem(KEYRED, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.gfx100(OPEN_GFX);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);

			TaskHandler.submit(new Task(3, true) {

				@Override
				public void execute() {
					c.getItems().addItem(REGULAR, 1);
					c.getItems().addItem(995, Misc.random(7500000));
					c.getItems().addItem(
							CHEST_REWARDS[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);

					this.cancel();
				}

				@Override
				public void onCancel() {

				}

			});
		}
	}

	public static void rewardChest1(final Client c, final int id, final int x,
			final int y) {
		if (canOpen(c)) {
			c.sendMessage("You completed the treasure and opened the chest!");
			c.getItems().deleteItem(KEYBLUE, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.gfx100(OPEN_GFX);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);
			TaskHandler.submit(new Task(3, true) {

				@Override
				public void execute() {
					c.getItems().addItem(REGULAR, 1);
					c.getItems().addItem(995, Misc.random(5350000));
					c.getItems().addItem(
							CHEST_REWARDS1[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);

					this.cancel();
				}
			});
		}
	}

	public static void rewardChest2(final Client c, final int id, final int x,
			final int y) {
		if (canOpen(c)) {
			c.sendMessage("You completed the treasure and opened the chest!");
			c.getItems().deleteItem(KEYGREEN, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.gfx100(OPEN_GFX);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);
			TaskHandler.submit(new Task(1, true) {

				@Override
				public void execute() {
					c.getItems().addItem(REGULAR, 1);
					c.getItems().addItem(995, Misc.random(3275000));
					c.getItems().addItem(
							CHEST_REWARDS2[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);

					this.cancel();
				}
			});
		}
	}

	public static void rewardChest3(final Client c, final int id, final int x,
			final int y) {
		if (canOpen(c)) {
			c.sendMessage("You completed the treasure and opened the chest!");
			c.getItems().deleteItem(KEYYELLOW, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.gfx100(OPEN_GFX);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);
			TaskHandler.submit(new Task(3, true) {

				@Override
				public void execute() {
					c.getItems().addItem(REGULAR, 1);
					c.getItems().addItem(995, Misc.random(1953000));
					c.getItems().addItem(
							CHEST_REWARDS3[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);

					this.cancel();
				}
			});
		}
	}

	public static void rewardChest4(final Client c, final int id, final int x,
			final int y) {
		if (canOpen(c)) {
			c.sendMessage("You completed the treasure and opened the chest!");
			c.getItems().deleteItem(KEYORANGE, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.gfx100(OPEN_GFX);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);
			TaskHandler.submit(new Task(3, true) {

				@Override
				public void execute() {
					c.getItems().addItem(REGULAR, 1);
					c.getItems().addItem(995, Misc.random(1278000));
					c.getItems().addItem(
							CHEST_REWARDS4[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);

					this.cancel();
				}
			});
		}
	}

	public static void rewardChest5(final Client c, final int id, final int x,
			final int y) {
		if (canOpen(c)) {
			c.sendMessage("You completed the treasure and opened the chest!");
			c.getItems().deleteItem(KEYPINK, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.gfx100(OPEN_GFX);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);
			TaskHandler.submit(new Task(3, true) {

				@Override
				public void execute() {
					c.getItems().addItem(REGULAR, 1);
					c.getItems().addItem(995, Misc.random(625000));
					c.getItems().addItem(
							CHEST_REWARDS5[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);
					this.cancel();
				}
			});
		}
	}

	public static int getLength() {
		return CHEST_REWARDS.length;
	}
}