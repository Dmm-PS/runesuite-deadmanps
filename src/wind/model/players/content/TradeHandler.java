package wind.model.players.content;

import java.util.concurrent.CopyOnWriteArrayList;

import wind.Config;
import wind.model.items.GameItem;
import wind.model.items.Item;
import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.saving.PlayerSave;
import wind.model.shops.ShopAssistant;
import wind.util.Misc;

/**
 * @information Modified by 7Winds
 */
public class TradeHandler {

	private Client c;

	public TradeHandler(Client Client) {
		this.c = Client;
	}

	/**
	 * Trading
	 **/
	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<GameItem>();

	public void displayWAndI(Client c) {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		c.playerTradeWealth = 0;
		o.playerTradeWealth = 0;
		for (GameItem item : c.getTrade().offeredItems) {
			c.getShops();
			c.playerTradeWealth += (ShopAssistant.getItemShopValue(item.id) * item.amount);
		}

		for (GameItem item : o.getTrade().offeredItems) {
			o.getShops();
			o.playerTradeWealth += (ShopAssistant.getItemShopValue(item.id) * item.amount);
		}

		int playerDifference1 = (c.playerTradeWealth - o.playerTradeWealth);
		int playerDifference2 = (o.playerTradeWealth - c.playerTradeWealth);

		boolean player1HasMore = (playerDifference1 > playerDifference2);
		boolean equalsSame = (c.playerTradeWealth == o.playerTradeWealth);

		if (c.playerTradeWealth < -1) {
			c.playerTradeWealth = 2147483647;
		}
		if (o.playerTradeWealth < -1) {
			o.playerTradeWealth = 2147483647;
		}

		String playerValue1 = ""
				+ c.getPA().getTotalAmount(c, c.playerTradeWealth) + " ("
				+ Misc.format(c.playerTradeWealth) + ")";
		String playerValue2 = ""
				+ c.getPA().getTotalAmount(o, o.playerTradeWealth) + " ("
				+ Misc.format(o.playerTradeWealth) + ")";

		if (c.playerTradeWealth < -1) {
			playerValue1 = "+" + playerValue1;
		}
		if (o.playerTradeWealth < -1) {
			playerValue2 = "+" + playerValue2;
		}
		if (equalsSame) {
			c.getPA().sendFrame126("@yel@Equal Trade", 23504);
			o.getPA().sendFrame126("@yel@Equal Trade", 23504);
		} else if (player1HasMore) {
			c.getPA().sendFrame126(
					"-@red@" + c.getPA().getTotalAmount(c, playerDifference1)
							+ " (" + Misc.format(playerDifference1) + ")",
					23504);
			o.getPA().sendFrame126(
					"+@yel@" + o.getPA().getTotalAmount(o, playerDifference1)
							+ " (" + Misc.format(playerDifference1) + ")",
					23504);
		} else if (!player1HasMore) {
			c.getPA().sendFrame126(
					"+@yel@" + c.getPA().getTotalAmount(c, playerDifference2)
							+ " (" + Misc.format(playerDifference2) + ")",
					23504);
			o.getPA().sendFrame126(
					"-@red@" + o.getPA().getTotalAmount(o, playerDifference2)
							+ " (" + Misc.format(playerDifference2) + ")",
					23504);
		}
		c.getPA().sendFrame126(playerValue1, 23506);
		c.getPA().sendFrame126(playerValue2, 23507);
		o.getPA().sendFrame126(playerValue2, 23506);
		o.getPA().sendFrame126(playerValue1, 23507);
//		c.getPA().sendFrame126(
//				Misc.formatPlayerName(o.playerName) + " has\\n "
//						+ o.getEquipment().freeSlots()
//						+ " free\\n inventory slots.", 23505);
//		o.getPA().sendFrame126(
//				Misc.formatPlayerName(c.playerName) + " has\\n "
//						+ c.getEquipment().freeSlots()
//						+ " free\\n inventory slots.", 23505);
//		o.getPA().sendFrame126(
//				"Trading with: " + Misc.formatPlayerName(c.playerName) + "",
//				3417);
		o.getPA().sendFrame126(
				"Trading with: " + Misc.formatPlayerName(c.playerName)
						+ " who has @gre@"
						+ c.getEquipment().freeSlots()
						+ " free slots", 3417);
//		c.getPA().sendFrame126(
//				"Trading with: " + Misc.formatPlayerName(o.playerName) + "",
//				3417);
	}

	public void requestTrade(int id) {
		try {
			Client o = (Client) PlayerHandler.players[id];
			if (o == null)
				return;
			else if (c.playerIsBusy())
				return;
			else if (o.playerIsBusy()) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			} else if (System.currentTimeMillis() - c.logoutDelay < 10000) {
				c.sendMessage("You cannot trade anyone while incombat!");
				return;
			} else if (System.currentTimeMillis() - o.logoutDelay < 10000) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			} else if (c.trade11 > 0) {
				c.sendMessage("You must wait 15 minutes before trading anyone!");
				c.sendMessage("this is because you're a new player. (Stops transfering gold)");
				return;
			} else if (id == c.playerId)
				return;
			c.turnPlayerTo(o.absX, o.absY);
			c.tradeWith = id;
			if (!c.inTrade && o.tradeRequested && o.tradeWith == c.playerId) {
				c.getTrade().openTrade();
				o.getTrade().openTrade();
			} else if (!c.inTrade) {
				c.tradeRequested = true;
				c.sendMessage("Sending trade request...");
				o.sendMessage(Misc.optimizeText(c.playerName) + ":tradereq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting trade.");
		}
	}

	public void openTrade() {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			// c.sendMessage("You must wait " +c.trade11+
			// " more seconds untill you can trade anyone.");
			return;
		}

		if (c.isBanking || o.isBanking) {
			c.getPA().closeAllWindows();
			o.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			o.getPA().cancelTeleportTask();
			return;
		}
		c.inTrade = true;
		c.canOffer = true;
		c.tradeStatus = 1;
		c.tradeRequested = false;
		c.getItems().resetItems(3322);
		resetTItems(3415);
		resetOTItems(3416);
		String out = o.playerName;

		if (o.getRights().equal(Rights.MODERATOR)) {
			out = "@cr1@" + out;
		} else if (o.getRights().equal(Rights.ADMINISTRATOR)) {
			out = "@cr2@" + out;
		}
		// c.getPA().sendFrame126("Trading with: " + o.playerName+"" ,3417);
		displayWAndI(c);
		c.getPA().sendFrame126("", 3431);
		c.getPA().sendFrame126("Are you sure you want to make this trade?",
				3535);
		c.getPA().sendFrame248(3323, 3321);
	}

	public void resetTItems(int WriteFrame) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			int len = offeredItems.toArray().length;
			int current = 0;
			c.getOutStream().writeWord(len);
			for (GameItem item : offeredItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}
			if (current < 27) {
				for (int i = current; i < 28; i++) {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(-1);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
		}
	}

	public boolean fromTrade(int itemID, int fromSlot, int amount) {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return false;
		}
		try {
			if (!c.inTrade || !c.canOffer) {
				declineTrade();
				return false;
			}
			c.tradeConfirmed = false;
			o.tradeConfirmed = false;
			if (!Item.itemStackable[itemID]) {
				for (int a = 0; a < amount; a++) {
					for (GameItem item : offeredItems) {
						if (item.id == itemID) {
							if (!item.stackable) {
								offeredItems.remove(item);
								c.getItems().addItem(itemID, 1);
								o.getPA().sendFrame126(
										"Trading with: " + Misc.formatPlayerName(c.playerName)
												+ " who has @gre@"
												+ c.getEquipment().freeSlots()
												+ " free slots", 3417);
							} else {
								if (item.amount > amount) {
									item.amount -= amount;
									c.getItems().addItem(itemID, amount);
									o.getPA().sendFrame126(
											"Trading with: " + Misc.formatPlayerName(c.playerName)
													+ " who has @gre@"
													+ c.getEquipment().freeSlots()
													+ " free slots", 3417);
								} else {
									amount = item.amount;
									offeredItems.remove(item);
									c.getItems().addItem(itemID, amount);
									o.getPA().sendFrame126(
											"Trading with: " + Misc.formatPlayerName(c.playerName)
													+ " who has @gre@"
													+ c.getEquipment().freeSlots()
													+ " free slots", 3417);
								}
							}
							break;
						}
						o.getPA().sendFrame126(
								"Trading with: " + Misc.formatPlayerName(c.playerName)
										+ " who has @gre@"
										+ c.getEquipment().freeSlots()
										+ " free slots", 3417);
						c.tradeConfirmed = false;
						o.tradeConfirmed = false;
						c.getItems().resetItems(3322);
						resetTItems(3415);
						o.getTrade().resetOTItems(3416);
						c.getPA().sendFrame126("", 3431);
						o.getPA().sendFrame126("", 3431);
					}
				}
			}
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					if (!item.stackable) {
					} else {
						if (item.amount > amount) {
							item.amount -= amount;
							c.getItems().addItem(itemID, amount);
							o.getPA().sendFrame126(
									"Trading with: " + Misc.formatPlayerName(c.playerName)
											+ " who has @gre@"
											+ c.getEquipment().freeSlots()
											+ " free slots", 3417);
						} else {
							amount = item.amount;
							offeredItems.remove(item);
							c.getItems().addItem(itemID, amount);
							o.getPA().sendFrame126(
									"Trading with: " + Misc.formatPlayerName(c.playerName)
											+ " who has @gre@"
											+ c.getEquipment().freeSlots()
											+ " free slots", 3417);
						}
					}
					break;
				}
			}
			o.getPA().sendFrame126(
					"Trading with: " + Misc.formatPlayerName(c.playerName)
							+ " who has @gre@"
							+ c.getEquipment().freeSlots()
							+ " free slots", 3417);
			c.tradeConfirmed = false;
			o.tradeConfirmed = false;
			c.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTrade().resetOTItems(3416);
			c.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		} catch (Exception e) {
		}
		return true;
	}

	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		for (int i : Config.ITEM_TRADEABLE) {
			if (i == itemID) {
				c.sendMessage("You can't trade this item.");
				return false;
			}
		}
		if (itemID == 15000 || itemID == 15001 || itemID == 15002 || itemID == 15003 || itemID == 15004) {
			c.sendMessage("You can't trade this item.");
			return false;
		}
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return false;
		} else if (amount <= 0)
			return false;

		int itemAmount = c.getItems().getItemCount(itemID);
		if (itemAmount < amount) {
			if (itemAmount <= 0)
				return false;
			amount = itemAmount;
		}

		if (!c.inTrade || !c.canOffer) {
			declineTrade();
			return false;
		}

		c.tradeConfirmed = false;
		o.tradeConfirmed = false;
		if (amount > c.playerItemsN[fromSlot])
			return false;
		if (!Item.itemStackable[itemID] && !Item.itemIsNote[itemID]) {
			for (int a = 0; a < amount && a < 28; a++) {
				c.getItems().specialDeleteItem(itemID, 1);
				offeredItems.add(new GameItem(itemID, 1));
			}
			resetTItems(3415);
			o.getTrade().resetOTItems(3416);
			displayWAndI(c);
			c.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		}

		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean inTrade = false;
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					inTrade = true;
					item.amount += amount;
					c.getItems().specialDeleteItem(itemID, amount);
					break;
				}
			}
			if (!inTrade) {
				offeredItems.add(new GameItem(itemID, amount));
				c.getItems().deleteItem2(itemID, amount);
				// o.getPA().sendFrame126("Trading with: " +
				// c.playerName+" who has @gre@"+c.getEquipment().freeSlots()+" free slots"
				// ,3417);
			}
		}

		// o.getPA().sendFrame126("Trading with: " + c.playerName+"" ,3417);
		c.getItems().resetItems(3322);
		c.getItems().updateInventory();
		resetTItems(3415);
		o.getTrade().resetOTItems(3416);
		displayWAndI(c);
		c.getPA().sendFrame126("", 3431);
		o.getPA().sendFrame126("", 3431);
		return true;
	}

	public void resetTrade() {
		offeredItems.clear();
		c.inTrade = false;
		c.tradeWith = 0;
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		c.acceptedTrade = false;
		c.getPA().resetInterfaceStatus();
		c.tradeResetNeeded = false;
		c.getPA().sendFrame126("Are you sure you want to make this trade?",
				3535);
	}

	public void declineTrade() {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o != null){
		c.tradeStatus = 0;
		declineTrade(true);
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		offeredItems.clear();
		c.tradeWith = 0;
		c.getItems().updateInventory();
		o.getItems().updateInventory();
		c.getPA().resetInterfaceStatus();
		o.getPA().resetInterfaceStatus();
		c.getPA().closeAllWindows();
		o.getPA().closeAllWindows();
		}
	}

	public void declineTrade(boolean tellOther) {
		c.inTrade = false;
		c.getPA().resetInterfaceStatus();
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (tellOther && o != null) {
			o.getTrade().declineTrade(false);
		}
		for (GameItem item : offeredItems) {
			if (item.amount < 1) {
				continue;
			}
			if (item.stackable) {
				c.getItems().forceAddItem(item.id, item.amount);
			} else {
				for (int i = 0; i < item.amount; i++) {
					c.getItems().forceAddItem(item.id, 1);
				}
			}
		}

		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		offeredItems.clear();
		c.tradeWith = 0;

	}


	public void resetOTItems(int WriteFrame) {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(WriteFrame);
		int len = o.getTrade().offeredItems.toArray().length;
		int current = 0;
		c.getOutStream().writeWord(len);
		for (GameItem item : o.getTrade().offeredItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255); // item's stack count. if over
													// 254, write byte 255
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1); // item id
			current++;
		}
		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
	}

	public void confirmScreen() {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		if (o.tradeWith != c.playerId)
			return;
		c.canOffer = false;
		c.getItems().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount = "";
		int Count = 0;
		for (GameItem item : offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@("
							+ Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000)
							+ " million @whi@(" + Misc.format(item.amount)
							+ ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

				if (Count == 0) {
					c.getItems();
					c.getItems();
					SendTrade = ItemAssistant.getItemName(item.id);
				} else {
					c.getItems();
					c.getItems();
					SendTrade = SendTrade + "\\n"
							+ ItemAssistant.getItemName(item.id);
				}

				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}

		c.getPA().sendFrame126(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;

		for (GameItem item : o.getTrade().offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@("
							+ Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000)
							+ " million @whi@(" + Misc.format(item.amount)
							+ ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

				if (Count == 0) {
					c.getItems();
					c.getItems();
					SendTrade = ItemAssistant.getItemName(item.id);
				} else {
					c.getItems();
					c.getItems();
					SendTrade = SendTrade + "\\n"
							+ ItemAssistant.getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		c.getPA().sendFrame126(SendTrade, 3558);
		c.getPA().sendFrame248(3443, 197);
	}

	public void giveItems() {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		if (o.tradeWith != c.playerId)
			return;
		try {
			for (GameItem item : o.getTrade().offeredItems) {
				if (item.id > 0) {
					c.getItems().forceAddItem(item.id, item.amount);
					c.getItems();
					c.getTradeLog().tradeReceived(
							ItemAssistant.getItemName(item.id), item.amount);
					c.getItems();
					o.getTradeLog().tradeGive(
							ItemAssistant.getItemName(item.id), item.amount);
				}
			}
			o.getTrade().offeredItems.clear();
			c.tradeResetNeeded = true;
		} catch (Exception e) {
		}
		c.getItems().updateInventory();
		PlayerSave.saveGame(c);
		PlayerSave.saveGame(o);
	}
}