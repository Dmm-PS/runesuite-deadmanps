package wind.model.players;

import wind.Config;
import wind.Server;
import wind.model.items.Item;
import wind.model.players.saving.PlayerSave;

/**
 * 
 * @author Fuzen Seth
 * @information Proper inventory file.
 * @since 30.6.2014
 */
public class Inventory {

	private Client player;

	/**
	 * Checks if you have a free slot.
	 * 
	 * @return
	 */
	public int getFreeSlots() {
		int freeS = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void addItemToBank(int itemId, int amount) {
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (player.bankItems[i] <= 0 || player.bankItems[i] == itemId + 1
					&& player.bankItemsN[i] + amount < Integer.MAX_VALUE) {
				player.bankItems[i] = itemId + 1;
				player.bankItemsN[i] += amount;
				resetBank();
				return;
			}
		}
	}

	public boolean addItem(int item, int amount) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if ((((getFreeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((getFreeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if ((player.playerItems[i] == (item + 1))
						&& Item.itemStackable[item]
						&& (player.playerItems[i] > 0)) {
					player.playerItems[i] = (item + 1);
					if (((player.playerItemsN[i] + amount) < Config.MAXITEM_AMOUNT)
							&& ((player.playerItemsN[i] + amount) > -1)) {
						player.playerItemsN[i] += amount;
					} else {
						player.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					if (player.getOutStream() != null && player != null) {
						player.getOutStream().createFrameVarSizeWord(34);
						player.getOutStream().writeWord(3214);
						player.getOutStream().writeByte(i);
						player.getOutStream().writeWord(player.playerItems[i]);
						if (player.playerItemsN[i] > 254) {
							player.getOutStream().writeByte(255);
							player.getOutStream().writeDWord(
									player.playerItemsN[i]);
						} else {
							player.getOutStream().writeByte(
									player.playerItemsN[i]);
						}
						player.getOutStream().endFrameVarSizeWord();
						player.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] <= 0) {
					player.playerItems[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						player.playerItemsN[i] = 1;
						if (amount > 1) {
							player.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						player.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			player.sendMessage("Not enough space in your inventory.");
			return false;
		}
		// }
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean isEmpty() {
		if (player.playerEquipment[player.playerRing] == -1
				&& player.playerEquipment[player.playerAmulet] == -1
				&& player.playerEquipment[player.playerArrows] == -1
				&& player.playerEquipment[player.playerHat] == -1
				&& player.playerEquipment[player.playerCape] == -1
				&& player.playerEquipment[player.playerChest] == -1
				&& player.playerEquipment[player.playerHands] == -1
				&& player.playerEquipment[player.playerLegs] == -1
				&& player.playerEquipment[player.playerShield] == -1
				&& player.playerEquipment[player.playerWeapon] == -1
				&& player.playerEquipment[player.playerFeet] == -1) {
			return true;
		}
		return false;
	}

	/**
	 * Empties all of (a) player's items.
	 */
	public void resetItems(int WriteFrame) {
		synchronized (player) {
			if (player.getOutStream() != null && player != null) {
				player.getOutStream().createFrameVarSizeWord(53);
				player.getOutStream().writeWord(WriteFrame);
				player.getOutStream().writeWord(player.playerItems.length);
				for (int i = 0; i < player.playerItems.length; i++) {
					if (player.playerItemsN[i] > 254) {
						player.getOutStream().writeByte(255);
						player.getOutStream().writeDWord_v2(
								player.playerItemsN[i]);
					} else {
						player.getOutStream().writeByte(player.playerItemsN[i]);
					}
					player.getOutStream().writeWordBigEndianA(
							player.playerItems[i]);
				}
				player.getOutStream().endFrameVarSizeWord();
				player.flushOutStream();
			}
		}
	}

	/**
	 * Counts (a) player's items.
	 * 
	 * @param itemID
	 * @return count start
	 */
	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (player.playerItems[j] == itemID + 1) {
				count += player.playerItemsN[j];
			}
		}
		return count;
	}

	public void replaceItem(Client c, int i, int l) {
		for (int k = 0; k < player.playerItems.length; k++) {
			if (playerHasItem(i, 1)) {
				deleteItem(i, getItemSlot(i), 1);
				addItem(l, 1);
			}
		}
	}

	/**
	 * Gets the item slot.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the total count of (a) player's items.
	 * 
	 * @param itemID
	 * @return
	 */
	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (Item.itemIsNote[itemID + 1]) {
				if (itemID + 2 == player.playerItems[j])
					count += player.playerItemsN[j];
			}
			if (!Item.itemIsNote[itemID + 1]) {
				if (itemID + 1 == player.playerItems[j]) {
					count += player.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < player.bankItems.length; j++) {
			if (player.bankItems[j] == itemID + 1) {
				count += player.bankItemsN[j];
			}
		}
		return count;
	}

	public boolean getUnnoted(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID) {

				found++;
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	/**
	 * Reset items kept on death.
	 **/
	public void resetKeepItems() {
		for (int i = 0; i < player.itemKeptId.length; i++) {
			player.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < player.invSlot.length; i1++) {
			player.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < player.equipSlot.length; i2++) {
			player.equipSlot[i2] = false;
		}
	}

	/**
	 * Deletes all of a player's items.
	 **/
	public void deleteAllItems() {
		for (int i1 = 0; i1 < player.playerEquipment.length; i1++) {
			deleteEquipment(player.playerEquipment[i1], i1);
		}
		for (int i = 0; i < player.playerItems.length; i++) {
			deleteItem(player.playerItems[i] - 1,
					getItemSlot(player.playerItems[i] - 1),
					player.playerItemsN[i]);
		}
	}

	private void deleteEquipment(int i, int i1) {
		// TODO Auto-generated method stub

	}

	public boolean addBankItem(int itemID, int fromSlot, int amount) {
		if (player.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[player.playerItems[fromSlot] - 1]) {
			if (player.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[player.playerItems[fromSlot] - 1]
					|| player.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (player.bankItems[i] == player.playerItems[fromSlot]) {
						if (player.playerItemsN[fromSlot] < amount)
							amount = player.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				/*
				 * Checks if you already have the same item in your bank.
				 */
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					player.bankItems[toBankSlot] = player.playerItems[fromSlot];
					if (player.playerItemsN[fromSlot] < amount) {
						amount = player.playerItemsN[fromSlot];
					}
					if ((player.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						player.sendMessage("Bank full!");
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				}

				else if (alreadyInBank) {
					if ((player.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						player.sendMessage("Bank full!");
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = player.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (player.bankItems[i] == player.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItems[toBankSlot] = player.playerItems[firstPossibleSlot];
							player.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(player.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(player.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[player.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[player.playerItems[fromSlot] - 2]) {
			if (player.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[player.playerItems[fromSlot] - 1]
					|| player.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (player.bankItems[i] == (player.playerItems[fromSlot] - 1)) {
						if (player.playerItemsN[fromSlot] < amount)
							amount = player.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					player.bankItems[toBankSlot] = (player.playerItems[fromSlot] - 1);
					if (player.playerItemsN[fromSlot] < amount) {
						amount = player.playerItemsN[fromSlot];
					}
					if ((player.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((player.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = player.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (player.bankItems[i] == (player.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItems[toBankSlot] = (player.playerItems[firstPossibleSlot] - 1);
							player.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(player.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(player.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			player.sendMessage("Item not supported "
					+ (player.playerItems[fromSlot] - 1));
			return false;
		}
	}

	/**
	 * Reseting your bank.
	 */
	public void resetBank() {
		synchronized (player) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(5382); // Bank
			player.getOutStream().writeWord(Config.BANK_SIZE);
			for (int i = 0; i < Config.BANK_SIZE; i++) {
				if (player.bankItemsN[i] > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(player.bankItemsN[i]);
				} else {
					player.getOutStream().writeByte(player.bankItemsN[i]);
				}
				if (player.bankItemsN[i] < 1) {
					player.bankItems[i] = 0;
				}
				if (player.bankItems[i] > Config.ITEM_LIMIT
						|| player.bankItems[i] < 0) {
					player.bankItems[i] = Config.ITEM_LIMIT;
				}
				player.getOutStream().writeWordBigEndianA(player.bankItems[i]);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	/**
	 * Delete items.
	 * 
	 * @param id
	 * @param amount
	 */
	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemSlot(id), amount);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (player.playerItems[slot] == (id + 1)) {
			if (player.playerItemsN[slot] > amount) {
				player.playerItemsN[slot] -= amount;
			} else {
				player.playerItemsN[slot] = 0;
				player.playerItems[slot] = 0;
			}
			PlayerSave.saveGame(player);
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (player.playerItems[i] == (id + 1)) {
				if (player.playerItemsN[i] > amount) {
					player.playerItemsN[i] -= amount;
					break;
				} else {
					player.playerItems[i] = 0;
					player.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Resets temporary worn items. Used in minigames, etc
	 */
	public void resetTempItems() {
		synchronized (player) {
			int itemCount = 0;
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] > -1) {
					itemCount = i;
				}
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(5064);
			player.getOutStream().writeWord(itemCount + 1);
			for (int i = 0; i < itemCount + 1; i++) {
				if (player.playerItemsN[i] > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(player.playerItemsN[i]);
				} else {
					player.getOutStream().writeByte(player.playerItemsN[i]);
				}
				if (player.playerItems[i] > Config.ITEM_LIMIT
						|| player.playerItems[i] < 0) {
					player.playerItems[i] = Config.ITEM_LIMIT;
				}
				player.getOutStream()
						.writeWordBigEndianA(player.playerItems[i]);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	/**
	 * Checks if you have free bank slots.
	 */
	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (player.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	/**
	 * Getting items from your bank.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 */
	public void fromBank(int itemID, int fromSlot, int amount) {
		if (!player.isBanking) {
			player.getPA().closeAllWindows();
			player.getPA().cancelTeleportTask();
			player.isBanking = false;
			return;
		}
		if (amount > 0) {
			if (player.bankItems[fromSlot] > 0) {
				if (!player.takeAsNote) {
					if (Item.itemStackable[player.bankItems[fromSlot] - 1]) {
						if (player.bankItemsN[fromSlot] > amount) {
							if (addItem((player.bankItems[fromSlot] - 1),
									amount)) {
								player.bankItemsN[fromSlot] -= amount;
								resetBank();
								player.getItems().resetItems(5064);
							}
						} else {
							if (addItem((player.bankItems[fromSlot] - 1),
									player.bankItemsN[fromSlot])) {
								player.bankItems[fromSlot] = 0;
								player.bankItemsN[fromSlot] = 0;
								resetBank();
								player.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (player.bankItemsN[fromSlot] > 0) {
								if (addItem((player.bankItems[fromSlot] - 1), 1)) {
									player.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						player.getItems().resetItems(5064);
					}
				} else if (player.takeAsNote
						&& Item.itemIsNote[player.bankItems[fromSlot]]) {
					if (player.bankItemsN[fromSlot] > amount) {
						if (addItem(player.bankItems[fromSlot], amount)) {
							player.bankItemsN[fromSlot] -= amount;
							resetBank();
							player.getItems().resetItems(5064);
						}
					} else {
						if (addItem(player.bankItems[fromSlot],
								player.bankItemsN[fromSlot])) {
							player.bankItems[fromSlot] = 0;
							player.bankItemsN[fromSlot] = 0;
							resetBank();
							player.getItems().resetItems(5064);
						}
					}
				} else {
					player.sendMessage("This item can't be withdrawn as a note.");
					if (Item.itemStackable[player.bankItems[fromSlot] - 1]) {
						if (player.bankItemsN[fromSlot] > amount) {
							if (addItem((player.bankItems[fromSlot] - 1),
									amount)) {
								player.bankItemsN[fromSlot] -= amount;
								resetBank();
								player.getItems().resetItems(5064);
							}
						} else {
							if (addItem((player.bankItems[fromSlot] - 1),
									player.bankItemsN[fromSlot])) {
								player.bankItems[fromSlot] = 0;
								player.bankItemsN[fromSlot] = 0;
								resetBank();
								player.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (player.bankItemsN[fromSlot] > 0) {
								if (addItem((player.bankItems[fromSlot] - 1), 1)) {
									player.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						player.getItems().resetItems(5064);
					}
				}
			}
		}
	}

	/**
	 * Checking item amounts.
	 * 
	 * @param itemID
	 * @return
	 */
	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID) {
				tempAmount += player.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID) {
				if (player.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	/**
	 * Getting un-noted items.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					NotedName = Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName == NotedName) {
					if (Server.itemHandler.ItemList[i].itemDescription
							.startsWith("Swap this note at any bank for a") == false) {
						NewID = Server.itemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	/**
	 * Checks if the player has the item.
	 * 
	 * @param itemID
	 * @param amt
	 * @param slot
	 * @return
	 */
	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (player.playerItems[slot] == (itemID)) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] == itemID) {
					if (player.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Checks if the player has an item in his inventory and/or equipment.
	 * 
	 * @param itemId
	 *            The item id needed
	 * @param inventory
	 *            If the item needs to be in inventory, or in equipment
	 * @return returns if it has the item.
	 */
	public boolean hasItem(Client c, int itemId, boolean inventory) {
		return inventory ? player.getItems().playerHasItem(itemId) : player
				.getItems().playerHasItem(itemId)
				|| player.playerEquipment[5] == itemId;
	}

	public static boolean isFull(Client c) {
		if (c.getEquipment().freeSlots() == 0) {
			return true;
		}
		return false;
	}
}