package wind.model.players.content;

import wind.Config;
import wind.model.players.Client;
import wind.model.shops.ShopAssistant;
import wind.util.Misc;

public class PriceChecker {
	
	private static int getFramesForSlot[][] = {{ 0, 22015 }, { 1, 22018 },
		{ 2, 22021 }, { 3, 22024 }, { 4, 22027 }, { 5, 22030 },
		{ 6, 22033 }, { 7, 22036 }, { 8, 22039 }, { 9, 22042 },
		{ 10, 22045 }, { 11, 22048 }, { 12, 22051 }, { 13, 22054 },
		{ 14, 22057 }, { 15, 22060 }, { 16, 22063 }, { 17, 22066 },
		{ 18, 22069 }, { 19, 22072 }};

	public static int arraySlot(final Client c, final int[] array,
			final int target) {
		int spare = -1;
		for (int x = 0; x < array.length; x++) {
			c.getItems();
			if (array[x] == target && c.getItems().isStackable(target)) {
				return x;
			} else if (spare == -1 && array[x] <= 0) {
				spare = x;
			}
		}
		return spare;
	}

	public static void clearConfig(final Client c) {
		for (int x = 0; x < c.price.length; x++) {
			if (c.price[x] > 0) {
				PriceChecker.withdrawItem(c, c.price[x], x, c.priceN[x]);
			}
		}
		c.getItems().updateInventory = true;
		c.getItems().resetItems(5064);
	}
	
	public static void handleClose(final Client c, int actionButtonId) {
		if (actionButtonId == 85242) {
			for (int x = 0; x < c.price.length; x++) {
				c.getPA().closeAllWindows();
				c.getPA().cancelTeleportTask();
				PriceChecker.withdrawItem(c, c.price[x], x, c.priceN[x]);
			}
		}
	}
	
//	public static void depositAll(Client c) {
//		for (int i = 0; i < c.playerItems.length; i++) {
//			depositItem(c, c.playerItems[i], c.playerItemsN[i]);
//		}
//	}

	public static void depositItem(final Client c, final int id, int amount) {
		final int slot = PriceChecker.arraySlot(c, c.price, id);
		for (final int element : Config.ITEM_TRADEABLE) {
			if (id == element) {
				c.sendMessage("This item is untrade-able.");
				return;
			}
		}
		if (c.getItems().getItemAmount(id) < amount) {
			amount = c.getItems().getItemAmount(id);
		}
		if (slot == -1) {
			c.sendMessage("The price-checker is currently full.");
			return;
		}
		c.getItems();
		if (!c.getItems().isStackable(id)) {
			amount = 1;
		}
		if (!c.getItems().playerHasItem(id, amount)) {
			return;
		}
		c.getItems().deleteItem2(id, amount);
		if (c.price[slot] != id) {
			c.price[slot] = id;
			c.priceN[slot] = amount;
		} else {
			c.price[slot] = id;
			c.priceN[slot] += amount;
		}
		c.getShops();
		c.total += ShopAssistant.getItemShopValue(id) * amount;
		PriceChecker.updateChecker(c);
	}

	public static void itemOnInterface(final Client c, final int frame,
			final int slot, final int id, final int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(id + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public static void open(final Client c) {
		c.isChecking = true;
		c.total = 0;
//		c.getPA().sendFrame126(
//				"" + Misc.insertCommasToNumber(Integer.toString(c.total)) + "",
//				18351);
//		c.getPA()
//				.sendFrame126(
//						"Click on items in your inventory to check their values",
//						18352);
		PriceChecker.updateChecker(c);
		PriceChecker.resetFrames(c);
		c.getItems().resetItems(5064);
		c.getPA().sendFrame248(22000, 5063);
	}

	public static void resetFrames(final Client c) {
		for (int x = 0; x < 20; x++) {
			if (c.price[x] <= 1) {
				PriceChecker.setFrame(c, x,
						PriceChecker.getFramesForSlot[x][1], c.price[x],
						c.priceN[x], false);
			}
		}
	}

	private static void setFrame(final Client player, final int slotId,
			final int frameId, final int itemId, final int amount,
			final boolean store) {
		player.getShops();
		final int totalAmount = ShopAssistant.getItemShopValue(itemId)
				* amount;
		final String total = Misc.insertCommasToNumber(Integer
				.toString(totalAmount));
		if (!store) {
			player.getPA().sendFrame126("", frameId);
			player.getPA().sendFrame126("", frameId + 1);
			//player.getPA().sendFrame126("", frameId + 2);
			return;
		}
		player.getItems();
		if (player.getItems().isStackable(itemId)) {
			player
					.getShops();
			player.getPA().sendFrame126("" + amount + " x " +  Misc.insertCommasToNumber(Integer.toString(ShopAssistant.getItemShopValue(itemId))), frameId);
			player.getPA().sendFrame126("" + " =" + total, frameId + 1);
			//player.getPA().sendFrame126("", frameId + 2);
		} else {
			player.getShops();
			player.getPA()
					.sendFrame126(
							""
									+ Misc.insertCommasToNumber(Integer
											.toString(ShopAssistant
													.getItemShopValue(itemId)))
									+ "", frameId);
			player.getPA().sendFrame126("", frameId + 1);
			//player.getPA().sendFrame126("", frameId + 2);
		}
	}

	public static void updateChecker(final Client c) {
		c.getItems().resetItems(5064);
		for (int x = 0; x < 20; x++) {
			if (c.priceN[x] <= 0) {
				PriceChecker.itemOnInterface(c, 22099, x, -1, 0);
			} else {
				PriceChecker.itemOnInterface(c, 22099, x, c.price[x],
						c.priceN[x]);
				//c.getPA().sendFrame126("", 18352);
				for (final int[] element : PriceChecker.getFramesForSlot) {
					if (x == element[0]) {
						PriceChecker.setFrame(c, x, element[1], c.price[x],
								c.priceN[x], true);
					}
				}
			}
		}
		c.getPA().sendFrame126(
				""
						+ Misc.insertCommasToNumber(Integer
								.toString(c.total < 0 ? 0 : c.total)) + "",
								22013);
	}

	public static void withdrawItem(final Client c, final int removeId,
			final int slot, int amount) {
		if (!c.isChecking) {
			return;
		}
		if (c.price[slot] != removeId) {
			return;
		}
		c.getItems();
		if (!c.getItems().isStackable(c.price[slot])) {
			amount = 1;
		}
		c.getItems();
		if (amount > c.priceN[slot] && c.getItems().isStackable(c.price[slot])) {
			amount = c.priceN[slot];
		}
		if (c.price[slot] >= 0 && c.getEquipment().freeSlots() > 0) {
			c.getItems().addItem(c.price[slot], amount);
			c.getItems();
			if (c.getItems().isStackable(c.price[slot])
					&& c.getItems().playerHasItem(c.price[slot], amount)) {
				c.priceN[slot] -= amount;
				c.price[slot] = c.priceN[slot] <= 0 ? 0 : c.price[slot];
			} else {
				c.priceN[slot] = 0;
				c.price[slot] = 0;
			}
		}
		c.getShops();
		c.total -= ShopAssistant.getItemShopValue(removeId) * amount;
		for (final int[] element : PriceChecker.getFramesForSlot) {
			if (slot == element[0]) {
				if (c.priceN[slot] >= 1) {
					PriceChecker.setFrame(c, slot, element[1], c.price[slot],
							c.priceN[slot], true);
				} else {
					PriceChecker.setFrame(c, slot, element[1], c.price[slot],
							c.priceN[slot], false);
				}
			}
		}
		PriceChecker.updateChecker(c);
	}

}