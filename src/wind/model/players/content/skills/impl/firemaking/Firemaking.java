package wind.model.players.content.skills.impl.firemaking;

import wind.Server;
import wind.clip.region.Region;
import wind.model.items.ItemAssistant;
import wind.model.objects.Object;
import wind.model.players.Client;
import wind.model.players.content.skills.impl.woodcutting.LogData.logData;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.ItemHandler;

public class Firemaking extends FireHandler {

	public static void attemptFire(final Client c, final int itemUsed,
			final int usedWith, final int x, final int y,
			final boolean groundObject) {
		if (!c.getItems().playerHasItem(590)) {
			c.sendMessage("You need a tinderbox to light a fire.");
			return;
		}
		if (isFiring[11] == true) {
			return;
		}
		for (final logData l : logData.values()) {
			final int logId = usedWith == 590 ? itemUsed : usedWith;
			if (logId == l.getLogId()) {
				if (c.playerLevel[11] < l.getLevel()) {
					c.getItems();
					c.sendMessage("You need a firemaking level of "
							+ l.getLevel() + " to light "
							+ ItemAssistant.getItemName(logId));
					return;
				}
				if (c.inBank()) {
					c.sendMessage("You cannot light a fire here.");
					return;
				}
				if (Server.objectManager.objectExists(c.absX, c.absY)) {
					c.sendMessage("You cannot light a fire here.");
					return;
				}
				isFiring[11] = true;
				boolean notInstant = (System.currentTimeMillis() - lastSkillingAction) > 2500;
				int cycle = 2;
				if (notInstant) {
					c.sendMessage("You attempt to light a fire.");
					if (groundObject == false) {
						c.getItems().deleteItem(logId,
								c.getItems().getItemSlot(logId), 1);
						ItemHandler.createGroundItem(c, logId, c.absX,
								c.absY, 1, c.playerId);
					}
					cycle = 3 + Misc.random(6);
				} else {
					if (groundObject == false) {
						c.getItems().deleteItem(logId,
								c.getItems().getItemSlot(logId), 1);
					}
				}
				final boolean walk;
				if (Region.getClipping((x - 1), y, c.heightLevel, -1, 0)) {
					walk = true;
				} else {
					walk = false;
				}
				c.startAnimation(733);
				c.getPA().walkTo(walk == true ? -1 : 1, 0);

				TaskHandler.submit(new Task(cycle, true) {
					@Override
					public void execute() {
						if (isFiring[11] == true) {
							Server.itemHandler.removeGroundItem(c, logId, x,
									c.absY, false);
							new Object(2732, x, y, 0, 0, 10, -1, 60 + Misc
									.random(30));
							c.fireslit++;
							if (c.fireslit == 100) {
								c.getAchievement().tinderboxPro();
							}
							c.sendMessage("The fire catches and the log beings to burn.");
							TaskHandler.submit(new Task(2, true) {
								@Override
								public void execute() {
									c.turnPlayerTo(walk == true ? (x + 1)
											: (x - 1), y);
									this.cancel();
								}

								@Override
								public void onCancel() {

								}
							});
							this.cancel();
						} else {
							return;
						}
					}

					@Override
					public void onCancel() {
						c.startAnimation(65535);
						c.getPA().addSkillXP((int) (l.getXp()), 11);
						lastSkillingAction = System.currentTimeMillis();
						isFiring[11] = false;
					}
				});
			}
		}
	}
}