package wind.model.items.impl;

import wind.Constants;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * 
 * @author Cizzle - Modified & Fixed up by Fuzen Seth,
 * Implemented Task System by Sunny++
 */

public class TeleTab {

	//final static int ANIM = 9597, GFX = 1680;
	final static int ANIM = 4731, GFX = 678;

	public enum TabData {
		VARROCK(8007, 3210, 3424),
		LUMBRIDGE(8008, 3222, 3218),
		FALADOR(8009, 2964, 3378),
		CAMELOT(8010, 2747, 3477),
		ARDOUGNE(8011, 2662, 3305),
		WATCHTOWER(8012, 2549, 3112),
		HOME(8013, 3087, 3499);

		private int itemId, posX, posY;

		TabData(int itemId, int posX, int posY) {
			this.itemId = itemId;
			this.posX = posX;
			this.posY = posY;
		}

		public int getItemId() {
			return itemId;
		}

		public int getPosX() {
			return posX;
		}

		public int getPosY() {
			return posY;
		}

		public static TabData forId(int itemId) {
			for (TabData data : TabData.values()) {
				if (data.itemId == itemId)
					return data;
			}
			return null;
		}
	}	

	public static void useTeleTab(final Client c, int slot, final TabData data) {
		if (c.isTeleporting)
			return;
		c.startAnimation(ANIM);
		c.resetWalkingQueue();
		c.gfx0(GFX);
		TaskHandler.submit(new Task(1, false) {
			@Override
			public void execute() {
				
				for (int stage = 0; stage < 3; stage++)
					
				switch (stage) {
				case 0:
					c.damageTaken[0] = 0;
					c.isTeleporting = true;
					c.getItems().deleteItem(data.getItemId(), slot, 1);
					break;
					
				case 1:
					c.getPA().movePlayer(data.posX, data.posY, 0);
					c.isTeleporting = false;
					c.getPA().walkableInterface(-1);
						break;						
				case 2:
					this.cancel();
					break;
					
					default:
						stage = 0;
						break;
				}
			}

			@Override
			public void onCancel() {
				c.startAnimation(-1);
			}
		});
	}
	
	/**
	 * Bones to Peaches/Bananas
	 */

	private static enum BoneTabs {
		BANANAS(8014, 1963), PEACHES(8015, 6883);

		private int tab, item;

		private BoneTabs(final int tab, final int item) {
			this.tab = tab;
			this.item = item;
		}

		private int getTab() {
			return tab;
		}

		private int getItem() {
			return item;
		}

		private final String getName() {
			return Misc.optimizeText(toString().toLowerCase());
		}

		private static BoneTabs getTabID(final int ID) {
			for (BoneTabs t : BoneTabs.values()) {
				if (t.getTab() == ID) {
					return t;
				}
			}
			return null;
		}
	}
	
	public static void useBoneTab(final Client c, final int item) {
		final BoneTabs b = BoneTabs.getTabID(item);
		int amount = -1;
		if (b == null || !c.getItems().playerHasItem(b.getTab())) {
			return;
		}
		if ((System.currentTimeMillis() - c.tabTimer) > 1500) {
			c.tabTimer = System.currentTimeMillis();
			for (int i = 0; i < Constants.BONES.length; i++) {
				if (c.getItems().playerHasItem(Constants.BONES[i][0])) {
					amount = c.getItems().getItemAmount(Constants.BONES[i][0]);
					c.getItems().deleteItem2(b.getTab(), 1);
					c.getItems().deleteItem2(Constants.BONES[i][0], amount);
					c.sendMessage("You break the teleport tab and your bones turn to "
							+ b.getName() + ".");
					c.getItems().addItem(b.getItem(), amount);
				}
			}
			if (amount <= 0) {
				c.sendMessage("You must have bones in your inventory to use this tab!");
				return;
			}
		}
	}
	
	
}