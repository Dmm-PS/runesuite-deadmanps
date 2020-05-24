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
import wind.util.Misc;
import wind.world.ItemHandler;

/**
 * Modified by 7Winds
 */
public class DuelHandler {
	
	Client c;
	
	public DuelHandler(Client c) {
		this.c = c;
	}
	
	/**
	 * Dueling
	 **/

	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();

	public void bothDeclineDuel() {
		Client c3 = (Client) PlayerHandler.players[c.duelingWith];
		declineDuel(true);
		c3.getDuel().declineDuel(true);
		c.sendMessage("@red@The duel has been declined.");
		c3.sendMessage("@red@The duel has been declined.");
	}
	
	public void requestDuel(int id) {
		try {
			if (id == c.playerId)
				return;
			Client o = (Client) PlayerHandler.players[id];
			if (o == null) {
				return;
			}
			if (c.teleTimer > 0)
				return;
			/*
			 * if (c.trade11 > 0) { c.sendMessage("You must wait "+c.trade11+
			 * " seconds to be able to duel."); return; }
			 */
			if ((c.duelStatus > 0 && c.duelStatus < 5) || !c.inDuelArena()
					|| !o.inDuelArena())
				if (!c.inMiscellania() || !c.inMiscellania()) {
					declineDuel(true);
					return;
				}
			if (c.playerIsBusy())
				return;
			if (o.playerIsBusy()) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			}
			resetDuel();
			resetDuelItems();
			c.duelingWith = id;
			c.duelRequested = true;
			if (c.duelStatus == 0 && o.duelStatus == 0 && c.duelRequested
					&& o.duelRequested && c.duelingWith == o.getId()
					&& o.duelingWith == c.getId()) {
				if (Misc.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), 1)) {
					c.getDuel().openDuel();
					o.getDuel().openDuel();
				} else {
					c.sendMessage("You need to get closer to your opponent to start the duel.");
				}

			} else {
				c.sendMessage("Sending duel request...");
				o.sendMessage(c.playerName + ":duelreq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting duel.");
		}
	}

	public void openDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		} else if ((!c.inDuelArena() || !o.inDuelArena())
				&& (!c.inMiscellania() || !o.inMiscellania()))
			return;
		c.duelStatus = 1;
		refreshduelRules();
		refreshDuelScreen();
		c.canOffer = true;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			sendDuelEquipment(c.playerEquipment[i], c.playerEquipmentN[i], i);
		}
		c.getPA().sendFrame126(
				"Dueling with: " + o.playerName + " (level-" + o.combatLevel
						+ ") ", 6671);
		c.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame126("No Sp. Atk", 28005);
		c.getPA().sendFrame248(6575, 3321);
		c.getItems().resetItems(3322);
	}

	public void sendDuelEquipment(int itemId, int amount, int slot) {
		synchronized (c) {
			if (itemId != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(13824);
				c.getOutStream().writeByte(slot);
				c.getOutStream().writeWord(itemId + 1);

				if (amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(amount);
				} else {
					c.getOutStream().writeByte(amount);
				}
				c.getOutStream().endFrameVarSizeWord();
				// c.flushOutStream();
			}
		}
	}

	public void refreshduelRules() {
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getPA().sendFrame87(286, 0);
		c.duelOption = 0;
	}

	public void refreshDuelScreen() {
		synchronized (c) {
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o == null) {
				return;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6669);
			c.getOutStream().writeWord(stakedItems.toArray().length);
			int current = 0;
			for (GameItem item : stakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > Config.ITEM_LIMIT || item.id < 0) {
					item.id = Config.ITEM_LIMIT;
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
			// c.flushOutStream();

			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6670);
			c.getOutStream().writeWord(
					o.getDuel().stakedItems.toArray().length);
			current = 0;
			for (GameItem item : o.getDuel().stakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > Config.ITEM_LIMIT || item.id < 0) {
					item.id = Config.ITEM_LIMIT;
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
			// c.flushOutStream();
		}
	}

	public boolean stakeItem(int itemID, int fromSlot, int amount) {
		for (int i : Config.ITEM_TRADEABLE) {
			if (i == itemID) {
				c.sendMessage("You can't trade this item.");
				return false;
			}
		}
		Client o = (Client) PlayerHandler.players[c.duelingWith];

		if (o == null) {
			declineDuel(true);
			return false;
		}
		
		if (c.connectedFrom.equals(o.connectedFrom)) {
			c.sendMessage("You cannot stake same ip address.");
			return false;
		}
		
		if (itemID != c.playerItems[fromSlot] - 1) {
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount))
			return false;
		if (c.getRights().equal(Rights.ADMINISTRATOR)) {
			c.sendMessage("Administrator can't stake items in duel arena.");
			return false;
		}
		if (itemID == 863 || itemID == 11230 || itemID == 869 || itemID == 868
				|| itemID == 867 || itemID == 866 || itemID == 4740
				|| itemID == 9244 || itemID == 11212 || itemID == 892
				|| itemID == 9194 || itemID == 9243 || itemID == 9242
				|| itemID == 9241 || itemID == 9240 || itemID == 9239
				|| itemID == 882 || itemID == 884 || itemID == 886
				|| itemID == 888 || itemID == 890) {
			c.sendMessage("You can't stake bolts, arrows or knifes.");
			return false;
		}
		if (itemID != 995 & c.playerItems[fromSlot] == 996) {
			return false;
		}
		if (!((c.playerItems[fromSlot] == itemID + 1) && (c.playerItemsN[fromSlot] >= amount))) {
			c.sendMessage("You don't have that amount!");
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount))
			return false;
		if (c.playerItems[fromSlot] - 1 != itemID
				&& c.playerItems[fromSlot] != itemID) { // duel dupe fix by
														// Aleksandr
			if (c.playerItems[fromSlot] == 0)
				return false;
			return false;
		}
		if (amount <= 0)
			return false;
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			c.getDuel().declineDuel(true);
			o.getDuel().declineDuel(true);
			return false;
		}
		if (!c.canOffer) {
			return false;
		}
		changeDuelStuff();
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					stakedItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID,
							c.getItems().getItemSlot(itemID), 1);
				}
			}

			c.getItems().resetItems(3214);
			c.getItems().resetItems(3322);
			o.getItems().resetItems(3214);
			o.getItems().resetItems(3322);
			refreshDuelScreen();
			o.getDuel().refreshDuelScreen();
			c.getPA().sendFrame126("", 6684);
			o.getPA().sendFrame126("", 6684);
		}

		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean found = false;
			for (GameItem item : stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					c.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				c.getItems().deleteItem(itemID, fromSlot, amount);
				stakedItems.add(new GameItem(itemID, amount));
			}
		}

		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		refreshDuelScreen();
		o.getDuel().refreshDuelScreen();
		c.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		return true;
	}

	public boolean fromDuel(int itemID, int fromSlot, int amount) {
		if (amount <= 0) {
			return false;
		}
		Client o = (Client) PlayerHandler.players[c.duelingWith];

		if (o == null) {
			declineDuel(false);
			return false;
		}

		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel(true);
			return false;
		}

		if (Item.itemStackable[itemID]) {
			if (c.getEquipment().freeSlots() - 1 < (c.duelSpaceReq)) {
				c.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if (o.duelStatus >= 2 || c.duelStatus >= 2) {
			return false;
		}

		if (!c.canOffer) {
			return false;
		}

		changeDuelStuff();
		boolean goodSpace = true;
		if (!Item.itemStackable[itemID]) {
			if (amount > 28) {
				amount = 28;
			}
			for (int a = 0; a < amount; a++) {
				for (GameItem item : stakedItems) {
					if (item.id == itemID && item.amount > 0) {
						if (!item.stackable) {
							if (c.getEquipment().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							stakedItems.remove(item);
							c.getItems().forceAddItem(itemID, 1);
						} else {
							if (c.getEquipment().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								c.getItems().forceAddItem(itemID, amount);
							} else {
								if (c.getEquipment().freeSlots() - 1 < (c.duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								stakedItems.remove(item);
								c.getItems().forceAddItem(itemID, amount);
							}
						}
						break;
					}
				}
			}
		} else {
			for (GameItem item : stakedItems) {
				if (item.id == itemID && item.amount > 0) {
					if (item.amount > amount) {
						item.amount -= amount;
						c.getItems().forceAddItem(itemID, amount);
					} else {
						amount = item.amount;
						stakedItems.remove(item);
						c.getItems().forceAddItem(itemID, amount);
					}
					break;
				}
			}
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		c.getDuel().refreshDuelScreen();
		o.getDuel().refreshDuelScreen();
		o.getPA().sendFrame126("", 6684);
		if (!goodSpace) {
			c.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		return true;
	}

	public void confirmDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			declineDuel(false);
			return;
		}
		String itemId = "";
		for (GameItem item : stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems();
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + " x "
						+ Misc.format(item.amount) + "\\n";
			} else {
				c.getItems();
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6516);
		itemId = "";
		for (GameItem item : o.getDuel().stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems();
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + " x "
						+ Misc.format(item.amount) + "\\n";
			} else {
				c.getItems();
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6517);
		c.getPA().sendFrame126("", 8242);
		for (int i = 8238; i <= 8253; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Hitpoints will be restored.", 8250);
		c.getPA().sendFrame126("Boosted stats will be restored.", 8238);
		if (c.duelRule[8]) {
			c.getPA().sendFrame126("There will be obstacles in the arena.",
					8239);
		}
		c.getPA().sendFrame126("", 8240);
		c.getPA().sendFrame126("", 8241);

		String[] rulesOption = { "Players cannot forfeit!",
				"Players cannot move.", "Players cannot use range.",
				"Players cannot use melee.", "Players cannot use magic.",
				"Players cannot drink pots.", "Players cannot eat food.",
				"Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (c.duelRule[i]) {
				c.getPA().sendFrame126("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		c.getPA().sendFrame126("", 6571);
		c.getPA().sendFrame248(6412, 197);
		// c.getPA().showInterface(6412);
	}

	public void startDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		c.freezeTimer = 1;
		c.getPA().resetFollow();
		if (o == null) {
			duelVictory();
		}
		c.headIconHints = 2;
		c.vengOn = false;
		c.lastVeng = 0;
		o.vengOn = false;
		o.lastVeng = 0;
		for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
			c.prayerActive[p] = false;
			c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
		}
		c.headIcon = -1;
		c.getPA().requestUpdates();
		c.getCombat().resetPrayers();
		// c.getCurse().resetCurse();
		if (c.duelRule[11]) {
			c.getItems().removeItem(c.playerEquipment[0], 0);
		}
		if (c.duelRule[12]) {
			c.getItems().removeItem(c.playerEquipment[1], 1);
		}
		if (c.duelRule[13]) {
			c.getItems().removeItem(c.playerEquipment[2], 2);
		}
		if (c.duelRule[14]) {
			c.getItems().removeItem(c.playerEquipment[3], 3);
		}
		if (c.duelRule[15]) {
			c.getItems().removeItem(c.playerEquipment[4], 4);
		}
		if (c.duelRule[16]) {
			c.getItems().removeItem(c.playerEquipment[5], 5);
			c.getItems();
			c.getItems();
			boolean wearing2h = Item.is2handed(
					ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase(), c.playerEquipment[c.playerWeapon]);
			if (wearing2h) {
				c.getItems().removeItem(c.playerEquipment[3], 3);
			}
		}
		if (c.duelRule[17]) {
			c.getItems().removeItem(c.playerEquipment[7], 7);
		}
		if (c.duelRule[18]) {
			c.getItems().removeItem(c.playerEquipment[9], 9);
		}
		if (c.duelRule[19]) {
			c.getItems().removeItem(c.playerEquipment[10], 10);
		}
		if (c.duelRule[20]) {
			c.getItems().removeItem(c.playerEquipment[12], 12);
		}
		if (c.duelRule[21]) {
			c.getItems().removeItem(c.playerEquipment[13], 13);
		}
		c.duelStatus = 5;
		c.getPA().resetInterfaceStatus();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3366 + Misc.random(12),
						3246 + Misc.random(6), 0);
			}
		} else {
			if (c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3337 + Misc.random(12),
						3246 + Misc.random(11), 0);
			}
		}

		c.getPA().createPlayerHints(10, o.playerId);
		c.getPA().showOption(3, 0, "Attack", 1);
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		for (GameItem item : o.getDuel().stakedItems) {
			otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		c.getPA().requestUpdates();
	}

	public void duelVictory() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		c.isDead = false;
		if (o != null) {
			c.getPA().sendFrame126("" + o.combatLevel, 6839);
			c.getPA().sendFrame126(o.playerName, 6840);
			o.duelStatus = 0;
			c.freezeTimer = 3;
		} else {
			c.getPA().sendFrame126("", 6839);
			c.getPA().sendFrame126("", 6840);
		}
		c.duelStatus = 6;
		c.getCombat().resetPrayers();
		/*
		 * for (int i = 0; i < 20; i++) { c.playerLevel[i] =
		 * c.getPA().getLevelForXP(c.playerXP[i]); c.getPA().refreshSkill(i); }
		 */
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
			o.getPA().refreshSkill(i);
		}
		c.getPA().refreshSkill(3);
		c.sendMessage("You have defeated <col=16723968>"
				+ Misc.optimizeText(o.playerName)
				+ "</col> in the <col=16723968>Duel Arena</col>.");
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		duelRewardInterface();
		PlayerSave.saveGame(c);
		c.getPA().showInterface(6733);
		c.getPA().movePlayer(3362, 3263, 0);
		c.getPA().requestUpdates();
		c.getPA().showOption(3, 0, "Challenge", 3);
		o.getPA().showOption(3, 0, "Challenge", 3);
		c.getPA().createPlayerHints(10, -1);
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.freezeTimer = 1;
		c.getPA().resetFollow();
		c.getCombat().resetPlayerAttack();
		c.poisonDamage = 0;
		c.poisonDelay = -1;
		c.lastPoisonSip = System.currentTimeMillis();
		c.duelRequested = false;
	}

	public void duelRewardInterface() {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6822);
			c.getOutStream().writeWord(otherStakedItems.toArray().length);
			for (GameItem item : otherStakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > Config.ITEM_LIMIT || item.id < 0) {
					item.id = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void claimStakedItems() {
		for (GameItem item : otherStakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						ItemHandler.createGroundItem(c, item.id,
								c.getX(), c.getY(), item.amount, c.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							ItemHandler.createGroundItem(c, item.id,
									c.getX(), c.getY(), 1, c.getId());
						}
					}
				}
			}
		}
		for (GameItem item : stakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						ItemHandler.createGroundItem(c, item.id,
								c.getX(), c.getY(), item.amount, c.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							ItemHandler.createGroundItem(c, item.id,
									c.getX(), c.getY(), 1, c.getId());
						}
					}
				}
			}
		}
		resetDuel();
		resetDuelItems();
		c.duelStatus = 0;
	}

	public void declineDuel(boolean notifyOther) {
		Client o2 = (Client) PlayerHandler.players[c.duelingWith];
		// you need to add way more check tbh like
		if (c.duelStatus >= 5 || c.duelStatus == 0)
			return;
		if (notifyOther) {
			if (o2 != null && o2.duelingWith == c.playerId)
				o2.getDuel().declineDuel(false);
		}
		c.getPA().resetInterfaceStatus();
		c.canOffer = true;
		c.duelStatus = 0;
		c.duelingWith = 0;
		c.duelSpaceReq = 0;
		c.duelRequested = false;
		for (GameItem item : stakedItems) {
			if (item.amount < 1)
				continue;
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems().forceAddItem(item.id, item.amount);
			} else {
				c.getItems().forceAddItem(item.id, 1);
			}
		}
		stakedItems.clear();
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.sendMessage("@red@The duel has been declined.");
		o2.sendMessage("@red@The duel has been declined.");
	}

	public void resetDuel() {
		c.getPA().showOption(3, 0, "Challenge", 3);
		c.headIconHints = 0;
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getPA().createPlayerHints(10, -1);
		c.duelStatus = 0;
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getPA().requestUpdates();
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}

	public boolean atDuelInterface() {
		return c.duelStatus < 5 && c.duelStatus > 0;
	}

	public void resetDuelItems() {
		stakedItems.clear();
		otherStakedItems.clear();
	}

	public void changeDuelStuff() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		o.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame126("", 6684);
	}

	public void selectRule(int i) { // rules
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		if (!c.canOffer)
			return;
		changeDuelStuff();
		o.duelSlot = c.duelSlot;
		if (i >= 11 && c.duelSlot > -1) {
			c.getItems();
			c.getItems();
			if (i == 16
					&& c.playerEquipment[c.playerWeapon] > 0
					&& Item.is2handed(
							ItemAssistant
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase(),
							c.playerEquipment[c.playerWeapon])) {
				if (!c.duelRule[i]) {
					c.duelSpaceReq++;
				} else {
					c.duelSpaceReq--;
				}
			}
			if (c.playerEquipment[c.duelSlot] > 0) {
				if (!c.duelRule[i]) {
					c.duelSpaceReq++;
				} else {
					c.duelSpaceReq--;
				}
			}
			if (o.playerEquipment[o.duelSlot] > 0) {
				if (!o.duelRule[i]) {
					o.duelSpaceReq++;
				} else {
					o.duelSpaceReq--;
				}
			}
		}

		if (i >= 11) {
			if (c.getEquipment().freeSlots() < (c.duelSpaceReq)
					|| o.getEquipment().freeSlots() < (o.duelSpaceReq)) {
				c.sendMessage("You or your opponent don't have the required space to set this rule.");
				if (c.playerEquipment[c.duelSlot] > 0) {
					c.duelSpaceReq--;
				}
				if (o.playerEquipment[o.duelSlot] > 0) {
					o.duelSpaceReq--;
				}
				return;
			}
		}

		if (!c.duelRule[i]) {
			c.duelRule[i] = true;
			c.duelOption += c.DUEL_RULE_ID[i];
		} else {
			c.duelRule[i] = false;
			c.duelOption -= c.DUEL_RULE_ID[i];
		}

		c.getPA().sendFrame87(286, c.duelOption);
		o.duelOption = c.duelOption;
		o.duelRule[i] = c.duelRule[i];
		o.getPA().sendFrame87(286, o.duelOption);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.duelTeleX = 3366 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3247 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		} else {
			if (c.duelRule[1]) {
				c.duelTeleX = 3337 + Misc.random(12);
				o.duelTeleX = c.duelTeleX + 1;
				c.duelTeleY = 3246 + Misc.random(11);
				o.duelTeleY = c.duelTeleY;
			}
		}

	}

}
