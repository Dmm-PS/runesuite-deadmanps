package wind.model.players.content;

import wind.clip.region.Region;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.ObjectHandler;

/*
 * @Author: Sunny++
 * Date: 3-11-15
 */

public class MithrilSeeds {

	public static int rFlower[] = { 2460, 2462, 2464, 2466, 2468, 2470, 2472,
			2474, 2476 };

	public static int flower() {
		return rFlower[(int) (Math.random() * rFlower.length)];
	}

	public static void plantMithrilSeed(Client c) {
		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;

		if (c.getItems().playerHasItem(299, 1)) {
			c.getItems().deleteItem2(299, 1);
			c.getPA().addSkillXP(2500, 19);
			c.startAnimation(827);
			c.canWalk = false;
			ObjectHandler.createAnObject(c, Player.randomFlower(), coords[0],
					coords[1]);
			c.getDH().sendDialogues(22, -1);
		}
	}

	public static void handleFlower(Client c) {
		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;
		int flower = c.getFlowerGame().flowerObjectID[Misc.random(4)];

		if (c.getItems().playerHasItem(299, 1)) {
			if (System.currentTimeMillis() - c.flowerDelay >= 1600) {
				c.flowerDelay = System.currentTimeMillis();
				c.getItems().deleteItem2(299, 1);
				c.getPA().object(flower, coords[0], coords[1], 0, 10);
				c.canWalk = true;
				if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel,
						-1, 0)) {
					c.getPA().walkTo(-1, 0);
				} else if (Region.getClipping(c.getX() + 1, c.getY(),
						c.heightLevel, 1, 0)) {
					c.getPA().walkTo(1, 0);
				} else if (Region.getClipping(c.getX(), c.getY() - 1,
						c.heightLevel, 0, -1)) {
					c.getPA().walkTo(0, -1);
				} else if (Region.getClipping(c.getX(), c.getY() + 1,
						c.heightLevel, 0, 1)) {
					c.getPA().walkTo(0, 1);
				}
				c.turnPlayerTo(coords[0] + 1, coords[1]);
			} else {
				return;
			}
		}
		/** 
		 * Creates a global flower right away.
		 */
		TaskHandler.submit(new Task(10, true) {
			@Override
			public void execute() {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client o = (Client) PlayerHandler.players[j];
						o.getPA().object(flower, coords[0], coords[1], 0, 10);
						c.getPA().object(flower, coords[0], coords[1], 0, 10);
						this.cancel();
					}
				}
			}

			@Override
			public void onCancel() {

			}
		});
		/**
		 * Removes global flowers after 5 minutes.
		 */
		TaskHandler.submit(new Task(300, false) {
			@Override
			public void execute() {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client o = (Client) PlayerHandler.players[j];
						o.getPA().object(-1, coords[0], coords[1], 0, 10);
						c.getPA().object(-1, coords[0], coords[1], 0, 10);
						this.cancel();
					}
				}
			}
			@Override
			public void onCancel() {
				
			}
		});
	}

	public static void pickupFlowers(Client c) {
		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;

		c.canWalk = true;
		ObjectHandler.createAnObject(c, -1, coords[0], coords[1]);
		c.getItems().addItem(flower(), 1);
	}
}