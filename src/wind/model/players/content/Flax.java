package wind.model.players.content;

import java.util.ArrayList;

import wind.Server;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * Flaxpicking. Updated 2/10/15
 * 
 * @author Sunny++
 * 
 */
public class Flax {

	public static ArrayList<int[]> flaxRemoved = new ArrayList<int[]>();

	public static void pickFlax(final Client c, final int x, final int y) {
		TaskHandler.submit(new Task(1, true) {
			int pStage = 1;

			@Override
			public void execute() {
				if (pStage == 1) {
					if (System.currentTimeMillis() - c.pickDelay > 1500) {
						if (c.getEquipment().freeSlots() != 0) {
							c.getItems().addItem(1779, 1);
							c.startAnimation(827);
							c.sendMessage("You pick some flax.");
							if (Misc.random(3) == 1) {
								flaxRemoved.add(new int[] { x, y });
								Server.objectManager.removeObject(x, y);
							}
						} else {
							c.sendMessage("Not enough space in your inventory.");
							return;
						}
						c.pickDelay = System.currentTimeMillis();
					}

				}
				if (pStage == 0) {
					this.cancel();
				}
				if (pStage > 0) {
					pStage--;
				}
			}

			@Override
			public void onCancel() {
				pStage = 0;
			}
		});
	}

	public static void pickWheat(final Client c, final int x, final int y) {
		TaskHandler.submit(new Task(1, true) {
			int pStage = 1;

			@Override
			public void execute() {
				if (pStage == 1) {
					if (System.currentTimeMillis() - c.pickDelay > 1500) {
						if (c.getEquipment().freeSlots() != 0) {
							c.getItems().addItem(1947, 1);
							c.startAnimation(2292);
							c.sendMessage("You pick some Wheat.");
							if (Misc.random(3) == 1) {
								flaxRemoved.add(new int[] { x, y });
								Server.objectManager.removeObject(x, y);
							}
						} else {
							c.sendMessage("Not enough space in your inventory.");
							return;
						}
						c.pickDelay = System.currentTimeMillis();
					}

				}
				if (pStage == 0) {
					this.cancel();
				}
				if (pStage > 0) {
					pStage--;
				}
			}

			@Override
			public void onCancel() {
				pStage = 0;
			}
		});
	}

	public static void pickPotato(final Client c, final int x, final int y) {

		TaskHandler.submit(new Task(1, true) {
			int pStage = 1;

			@Override
			public void execute() {
				if (pStage == 1) {
					if (System.currentTimeMillis() - c.pickDelay > 1500) {
						if (c.getEquipment().freeSlots() != 0) {
							c.getItems().addItem(1942, 1);
							c.startAnimation(2292);
							c.sendMessage("You dug up some potatoes.");
							if (Misc.random(3) == 1) {
								flaxRemoved.add(new int[] { x, y });
								Server.objectManager.removeObject(x, y);
							}
						} else {
							c.sendMessage("Not enough space in your inventory.");
							return;
						}
						c.pickDelay = System.currentTimeMillis();
					}

				}
				if (pStage == 0) {
					this.cancel();
				}
				if (pStage > 0) {
					pStage--;
				}
			}

			@Override
			public void onCancel() {
				pStage = 0;
			}
		});
	}
}
