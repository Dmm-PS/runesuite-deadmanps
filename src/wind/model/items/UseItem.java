package wind.model.items;

import com.johnmatczak.model.BankKey;

import wind.Config;
import wind.model.items.Attachables.attachData;
import wind.model.players.Client;
import wind.model.players.Rights;
import wind.model.players.content.Keys;
import wind.model.players.content.skills.impl.farming.herbs.HerbFarming;
import wind.model.players.content.skills.impl.farming.trees.TreeFarming;
import wind.model.players.content.skills.impl.firemaking.Firemaking;
import wind.model.players.content.skills.impl.herblore.Grinding;
import wind.model.players.content.skills.impl.herblore.Herblore;
import wind.util.Misc;

/**
 * @author Sanity
 * @author Ryan
 * @author Lmctruck30 Revised by Shawn Notes by Shawn
 */

public class UseItem {

	/**
	 * Using items on an object.
	 * 
	 * @param c
	 * @param objectID
	 * @param objectX
	 * @param objectY
	 * @param itemId
	 */
	public static void ItemonObject(Client c, int objectID, int objectX, int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		if (c.hasPin) {
			return;
		}
		switch (objectID) {
		case 2790:
			if (itemId >= 15000 && itemId <= 15004) {
				if (c.bankKeyLoot[itemId - 15000] != null) {
					BankKey.Raid.OpenInterface(c, itemId - 15000);
				}
			}
			break;

		case 14472:
			if (itemId == Keys.KEYRED)
				Keys.rewardChest(c, objectID, objectX, objectY);
			break;
		case 170:
			if (itemId == Keys.KEYBLUE)
				Keys.rewardChest1(c, objectID, objectX, objectY);
			break;
		case 375:
			if (itemId == Keys.KEYGREEN)
				Keys.rewardChest2(c, objectID, objectX, objectY);
			break;
		case 376:
			if (itemId == Keys.KEYYELLOW)
				Keys.rewardChest3(c, objectID, objectX, objectY);
			break;
		case 377:
			if (itemId == Keys.KEYORANGE)
				Keys.rewardChest4(c, objectID, objectX, objectY);
			break;
		case 2183:
			if (itemId == Keys.KEYPINK)
				Keys.rewardChest5(c, objectID, objectX, objectY);
			break;
		case 2783:
			c.getSmithingInt().showSmithInterface(itemId);
			break;

		case 8389:
		case 8132:
		case 7848: // /flower patch catherby
			c.sendMessage("This should probably be raked first.");
			break;
		case 8392:
		case 8151:
			if (c.getItems().playerHasItem(5343)) {
				if (TreeFarming.isTreeSeed(c, itemId)) {
					TreeFarming.farmTree(c, itemId);
				} else if (HerbFarming.isHerbSeed(c, itemId)) {
					HerbFarming.farmHerbs(c, itemId);
				} else {
					c.sendMessage("I need a tree seed or herb seed to plant in this patch.");
				}
			} else {
				c.sendMessage("I need a seed dibber in order to plant a seed.");
			}
			break;

		case 409:
			break;
		/*
		 * case 2728: case 12269: c.getCooking().itemOnObject(itemId); break;
		 */
		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				Misc.println("Player At Object id: " + objectID + " with Item id: " + itemId);
			break;
		}

	}

	/**
	 * Using items on items.
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */
	// public static void ItemonItem(Client c, int itemUsed, int useWith) {
	public static void ItemonItem(final Client c, final int itemUsed, final int useWith, final int itemUsedSlot,
			final int usedWithSlot) {
		Grinding.grindItem(c, itemUsed, useWith);
		Herblore.makePotions(c, useWith, itemUsed);
		c.getItems();
		c.getItems();
		if (ItemAssistant.getItemName(itemUsed).contains("(") && ItemAssistant.getItemName(useWith).contains("("))
			c.getPotMixing().mixPotion2(itemUsed, useWith);
		if (itemUsed == 1733 || useWith == 1733)
			c.getCrafting().handleLeather(itemUsed, useWith);
		if (itemUsed == 1755 || useWith == 1755)
			c.getCrafting().handleChisel(itemUsed, useWith);
		if (itemUsed == 946 || useWith == 946)
			c.getFletching().handleLog(itemUsed, useWith);
		if (itemUsed == 53 || useWith == 53 || itemUsed == 52 || useWith == 52)
			c.getFletching().makeArrows(itemUsed, useWith);
		if ((itemUsed == 1540 && useWith == 11286) || (itemUsed == 11286 && useWith == 1540)) {
			if (c.playerLevel[c.playerSmithing] >= 95) {
				c.getItems().deleteItem(1540, c.getItems().getItemSlot(1540), 1);
				c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286), 1);
				c.getItems().addItem(11284, 1);
				c.sendMessage("You combine the two materials to create a dragonfire shield.");
				c.getPA().addSkillXP(500 * Config.SMITHING_EXPERIENCE, c.playerSmithing);
			} else {
				c.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
			}
		}
		for (attachData attachables : Attachables.attachData.values()) {
			if (attachables.getIngredient1() == itemUsed && attachables.getIngredient2() == useWith
					|| (attachables.getIngredient1() == useWith && attachables.getIngredient2() == itemUsed)) {
				c.getPA().sendItemsOnDialogue2("You are about to create a new item!",
						"This process is @dre@irreversible!",
						"Are you sure you want to create a " + attachables.getItemName() + "?",
						attachables.getProduct(), 280);
				c.product = attachables.getProduct();
				c.ingredient[0] = attachables.getIngredient1();
				c.ingredient[1] = attachables.getIngredient2();
				c.productName = attachables.getItemName();
				c.nextChat = 1008;
			}
		}
	/*	if (itemUsed == Blowpipe.BLOWPIPE && useWith == Blowpipe.BRONZE_DART
				|| useWith == Blowpipe.IRON_DART
				|| useWith == Blowpipe.STEEL_DART
				|| useWith == Blowpipe.MITH_DART
				|| useWith == Blowpipe.ADDY_DART
				|| useWith == Blowpipe.RUNE_DART
				|| useWith == Blowpipe.DRAGON_DART
				|| itemUsed == Blowpipe.BRONZE_DART
				|| itemUsed == Blowpipe.IRON_DART
				|| itemUsed == Blowpipe.STEEL_DART
				|| itemUsed == Blowpipe.MITH_DART
				|| itemUsed == Blowpipe.ADDY_DART
				|| itemUsed == Blowpipe.RUNE_DART
				|| itemUsed == Blowpipe.DRAGON_DART
				&& useWith == Blowpipe.BLOWPIPE)
			Blowpipe.addDart(c, itemUsed, useWith); */
		if (itemUsed == 314 && useWith == 819
				|| useWith == 820
				|| useWith == 821
				|| useWith == 822
				|| useWith == 823
				|| useWith == 824)
			if (c.playerLevel[c.playerFletching] >= 2) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(useWith - 13, boltsMade);
				c.getPA().addSkillXP(boltsMade * 6 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 2 to fletch this item.");
			}
		
		if (itemUsed == 9142 && useWith == 9190 || itemUsed == 9190 && useWith == 9142) {
			if (c.playerLevel[c.playerFletching] >= 58) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9241, boltsMade);
				c.getPA().addSkillXP(boltsMade * 6 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 11230 && useWith == 12926 || itemUsed == 12926 && useWith == 11230) {
			int dart = 11230;
			int amount = c.getItems().getItemAmount(dart);
			c.getItems().deleteItem(dart, amount);
			c.dartsLoaded += amount;
			c.sendMessage("@red@You load " + amount + " of dart(s) into your blowpipe.");

		}
		if (itemUsed == 9143 && useWith == 9191 || itemUsed == 9191 && useWith == 9143) {
			if (c.playerLevel[c.playerFletching] >= 63) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9242, boltsMade);
				c.getPA().addSkillXP(boltsMade * 7 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 9143 && useWith == 9192 || itemUsed == 9192 && useWith == 9143) {
			if (c.playerLevel[c.playerFletching] >= 65) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9243, boltsMade);
				c.getPA().addSkillXP(boltsMade * 7 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 9144 && useWith == 9193 || itemUsed == 9193 && useWith == 9144) {
			if (c.playerLevel[c.playerFletching] >= 71) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9244, boltsMade);
				c.getPA().addSkillXP(boltsMade * 10 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 9144 && useWith == 9194 || itemUsed == 9194 && useWith == 9144) {
			if (c.playerLevel[c.playerFletching] >= 58) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9245, boltsMade);
				c.getPA().addSkillXP(boltsMade * 13 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 1601 && useWith == 1755 || itemUsed == 1755 && useWith == 1601) {
			if (c.playerLevel[c.playerFletching] >= 63) {
				c.getItems().deleteItem(1601, c.getItems().getItemSlot(1601), 1);
				c.getItems().addItem(9192, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 1607 && useWith == 1755 || itemUsed == 1755 && useWith == 1607) {
			if (c.playerLevel[c.playerFletching] >= 65) {
				c.getItems().deleteItem(1607, c.getItems().getItemSlot(1607), 1);
				c.getItems().addItem(9189, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 1605 && useWith == 1755 || itemUsed == 1755 && useWith == 1605) {
			if (c.playerLevel[c.playerFletching] >= 71) {
				c.getItems().deleteItem(1605, c.getItems().getItemSlot(1605), 1);
				c.getItems().addItem(9190, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 1603 && useWith == 1755 || itemUsed == 1755 && useWith == 1603) {
			if (c.playerLevel[c.playerFletching] >= 73) {
				c.getItems().deleteItem(1603, c.getItems().getItemSlot(1603), 1);
				c.getItems().addItem(9191, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed == 590 || useWith == 590) {
			Firemaking.attemptFire(c, itemUsed, useWith, c.absX, c.absY, false);
		}
		if (itemUsed == 1615 && useWith == 1755 || itemUsed == 1755 && useWith == 1615) {
			if (c.playerLevel[c.playerFletching] >= 73) {
				c.getItems().deleteItem(1615, c.getItems().getItemSlot(1615), 1);
				c.getItems().addItem(9193, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed >= 11710 && itemUsed <= 11714 && useWith >= 11710 && useWith <= 11714) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			}
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
		}

		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11690) {
				c.getItems().makeGodsword(hilt);
			}
		}

		switch (itemUsed) {
		/*
		 * case 1511: case 1521: case 1519: case 1517: case 1515: case 1513:
		 * case 590: c.getFiremaking().checkLogType(itemUsed, useWith); break;
		 */

		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				Misc.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
			break;
		}
	}

	/**
	 * Using items on NPCs.
	 * 
	 * @param c
	 * @param itemId
	 * @param npcId
	 * @param slot
	 */
	public static void ItemonNpc(Client c, int itemId, int npcId, int slot) {
		switch (itemId) {

		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				Misc.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}
