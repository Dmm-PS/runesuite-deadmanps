package wind.model.shops;

import wind.Config;
import wind.model.items.Item;
import wind.model.items.ItemAssistant;
import wind.model.items.ItemTableManager;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.world.ShopHandler;

public class ShopAssistant {

	private Client c;

	public ShopAssistant(Client client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true
						&& PlayerHandler.players[i].myShopId == c.myShopId
						&& i != c.playerId) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		synchronized (c) {
			int TotalItems = 0;
			for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0) {
					TotalItems++;
				}
			}
			if (TotalItems > ShopHandler.MaxShopItems) {
				TotalItems = ShopHandler.MaxShopItems;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(3900);
			c.getOutStream().writeWord(TotalItems);
			int TotalCount = 0;
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0
						|| i <= ShopHandler.ShopItemsStandard[ShopID]) {
					if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(
								ShopHandler.ShopItemsN[ShopID][i]);
					} else {
						c.getOutStream().writeByte(
								ShopHandler.ShopItemsN[ShopID][i]);
					}
					if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT
							|| ShopHandler.ShopItems[ShopID][i] < 0) {
						ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
					}
					c.getOutStream().writeWordBigEndianA(
							ShopHandler.ShopItems[ShopID][i]);
					TotalCount++;
				}
				if (TotalCount > TotalItems) {
					break;
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public int getItemShopValue(int ItemID, int Type, int fromSlot) {
		return ItemTableManager.forID(ItemID).getValue();
	}

	public static int getItemShopValue(int itemId) {
		return ItemTableManager.forID(itemId).getValue();
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0,
				removeSlot));
		ShopValue *= 1.15;
		String ShopAdd = "";
		if (c.myShopId == 168) {
			c.getItems();
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " slayer points.");
			return;
		}
		if (c.myShopId == 164) {
			c.getItems();
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " member points.");
			return;
		}
		if (c.myShopId == 15) {
			c.sendMessage("This item current costs "
					+ c.getItems().getUntradePrice(removeId) + " coins.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		}
		c.getItems();
		c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs "
				+ ShopValue + " coins" + ShopAdd);
	}

	public int getSpecialItemValue(int id) {
		switch (id) {
		/*
		 * PK STORE
		 */
		case 4333:
		case 4353:
		case 4373:
		case 4393:
		case 4413:
		case 11212:
			return 2;
		case 7459:
			return 5;
		case 962:
			return 30;
		case 11862:
		case 11863:
		case 12399:
			return 50;
		case 1038:
		case 1040:
		case 1042:
		case 1044:
		case 1046:
		case 1048:
			return 35;
		case 405:
			return 25;
		case 8849:
			return 15;
		case 7460:
		case 8848:
			return 10;
		case 7462:
			return 10;
		case 8850:
		case 7398:
		case 7399:
		case 7400:
		case 12926:
			return 20;
		case 10551:
			return 20;
		case 10548:
			return 15;
		case 6570:
		case 12954:
			return 20;
		case 11864:
			return 15;
		case 6731:
		case 6733:
		case 6737:
		case 6585:
		case 11732:
		case 6916:
		case 6918:
		case 6920:
		case 6922:
		case 6924:
		case 6914:
		case 6889:
		case 2417:
		case 2415:
		case 11235:
		case 2416:
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			return 50;
		case 10338:
		case 10342:
		case 10340:
		case 1989:
			return 500;
		case 11838:
		case 4151:
		case 1419:
		case 6199:
		case 1961:
		case 13263:
			return 100;
		case 10352:
		case 10350:
		case 10348:
		case 10346:
			return 1000;

		case 11696:
		case 11283:
		case 1959:
		case 9703:
			return 200;
		case 11694:
			return 500;
		case 11724:
		case 11726:
		case 11698:
			return 300;
		case 11700:
			return 400;
		case 1053:
			return 15;
		case 1057:
			return 15;
		case 1055:
			return 15;
		case 1050:
			return 20;
		case 989:
			return 10;
		case 8007:
		case 8008:
		case 8009:
		case 8010:
		case 8011:
		case 8012:
		case 8013:
			return 1;
		case 13124:
		case 13120:
		case 13103:
		case 13132:
			return 10;
		case 12004:
			return 15;
		case 11850:
		case 11852:
		case 11854:
		case 11856:
		case 11858:
		case 11860:
			return 7;
			

		}
		return 0;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		for (int i : Config.ITEM_SELLABLE) {
			if (i == removeId) {
				c.getItems();
				c.sendMessage("You can't sell "
						+ ItemAssistant.getItemName(removeId).toLowerCase()
						+ ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.getItems();
			c.sendMessage("You can't sell "
					+ ItemAssistant.getItemName(removeId).toLowerCase()
					+ " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1,
					removeSlot));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			c.getItems();
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": shop will buy for " + ShopValue + " coins" + ShopAdd);
		}
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (c.inTrade) {
			return false;
		}
        if(!c.isShopping)
            return false;
		if (itemID == 863 || itemID == 11230 || itemID == 869 || itemID == 868
				|| itemID == 867 || itemID == 866 || itemID == 4740
				|| itemID == 9244 || itemID == 11212 || itemID == 892
				|| itemID == 9194 || itemID == 9243 || itemID == 9242
				|| itemID == 9241 || itemID == 9240 || itemID == 9239
				|| itemID == 882 || itemID == 884 || itemID == 886
				|| itemID == 888 || itemID == 890) {
			c.sendMessage("You can't sell bolts or arrows.");
			return false;
		}
		if (c.myShopId == 14)
			return false;
		for (int i : Config.ITEM_SELLABLE) {
			if (i == itemID) {
				c.getItems();
				c.sendMessage("You can't sell "
						+ ItemAssistant.getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (c.getRights().greater(Rights.ADMINISTRATOR) && !Config.ADMIN_CAN_SELL_ITEMS) {
			c.sendMessage("Selling items as an admin has been disabled.");
			return false;
		}

		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.getItems();
					c.sendMessage("You can't sell "
							+ ItemAssistant.getItemName(itemID).toLowerCase()
							+ " to this store.");
					return false;
				}
			}

			if (amount > c.playerItemsN[fromSlot]
					&& (Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == true || Item.itemStackable[(c.playerItems[fromSlot] - 1)] == true)) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID)
					&& Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == false
					&& Item.itemStackable[(c.playerItems[fromSlot] - 1)] == false) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1,
						fromSlot));
				if (c.getEquipment().freeSlots() > 0
						|| c.getItems().playerHasItem(995)) {
					if (Item.itemIsNote[itemID] == false) {
						c.getItems().deleteItem(itemID,
								c.getItems().getItemSlot(itemID), 1);
					} else {
						c.getItems().deleteItem(itemID, fromSlot, 1);
					}
					c.getItems().addItem(995, TotPrice2);
					addShopItem(itemID, 1);
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					break;
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (Item.itemIsNote[itemID] == true) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (c.myShopId == 14) {
			skillBuy(itemID);
			return false;

		} else if (c.myShopId == 15) {
			buyVoid(itemID);
			return false;
		}
        if(!c.isShopping)
            return false;
		if (!shopSellsItem(itemID))
			return false;
		if (amount > 0) {
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;// Tokkul
			if (c.myShopId == 168) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 164) {
				handleOtherShop(itemID);
				return false;
			}
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0,
						fromSlot));
				Slot = c.getItems().getItemSlot(995);
				Slot1 = c.getItems().getItemSlot(6529);
				if (Slot == -1 && c.myShopId != 29 && c.myShopId != 30
						&& c.myShopId != 31) {
					c.sendMessage("You don't have enough coins.");
					break;
				}
				if (Slot1 == -1 && c.myShopId == 111 || c.myShopId == 112
						|| c.myShopId == 113) {
					c.sendMessage("You don't have enough tokkul.");
					break;
				}
			
			else if(c.myShopId == 164) {
	                	if (c.memberPoints >= TotPrice2) {
							if (c.getItems().freeSlotId() > 0) {
								//Delay = System.currentTimeMillis();
								c.memberPoints -= TotPrice2;
								c.getItems().addItem(itemID, 1);
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) >ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								break;
							}
						} else {
							c.sendMessage("You don't have enough Member Points.");
							break;
						}
			 }
				if (TotPrice2 <= 1) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0,
							fromSlot));
					TotPrice2 *= 1.66;
				}
				if (c.myShopId != 29 || c.myShopId != 30 || c.myShopId != 31) {
					if (c.playerItemsN[Slot] >= TotPrice2) {
						if (c.getEquipment().freeSlots() > 0) {
							c.getItems().deleteItem(995,
									c.getItems().getItemSlot(995), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						break;
					}
				}
				if (c.myShopId == 111 || c.myShopId == 112 || c.myShopId == 113) {
					if (c.playerItemsN[Slot1] >= TotPrice2) {
						if (c.getEquipment().freeSlots() > 0) {
							c.getItems().deleteItem(6529,
									c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	public void handleOtherShop(int itemID) {
		if (c.myShopId == 164) {
			if (c.memberPoints >= getSpecialItemValue(itemID)) {
				if (c.getEquipment().freeSlots() > 0) {
					c.memberPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		} else if (c.myShopId == 168) {
			if (c.slayerPoints >= getSpecialItemValue(itemID)) {
				if (c.getEquipment().freeSlots() > 0) {
					c.slayerPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 14;
		setupSkillCapes(capes, get99Count());
	}

	/*
	 * public int[][] skillCapes =
	 * {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680
	 * ,4359,2682},{3,2701,4341,2703
	 * },{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
	 * {7,2737,4325,2733
	 * },{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730
	 * },{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727},
	 * {14,2722,4345
	 * ,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361,
	 * 2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};
	 */
	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
			9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786,
			9810, 9765 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < c.playerLevel.length; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes, int capes2) {
		synchronized (c) {
			c.getItems().resetItems(3823);
			c.isShopping = true;
			c.myShopId = 14;
			c.getPA().sendFrame248(3824, 3822);
			c.getPA().sendFrame126("Skillcape Shop", 3901);

			int TotalItems = 0;
			TotalItems = capes2;
			if (TotalItems > ShopHandler.MaxShopItems) {
				TotalItems = ShopHandler.MaxShopItems;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(3900);
			c.getOutStream().writeWord(TotalItems);
			for (int i = 0; i < 21; i++) {
				if (c.getLevelForXP(c.playerXP[i]) < 99)
					continue;
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getEquipment().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (c.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995,
									c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99k to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public void openVoid() {
	}

	public void buyVoid(int item) {
	}

}
