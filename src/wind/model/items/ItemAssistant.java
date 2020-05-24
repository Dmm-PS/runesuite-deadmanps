package wind.model.items;

import java.util.Iterator;
import java.util.Map;

import com.johnmatczak.model.BankKey;

import wind.Config;
import wind.Server;
import wind.model.items.Item;
import wind.model.items.impl.MembershipBond;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.saving.PlayerSave;
import wind.model.shops.ShopAssistant;
import wind.util.Misc;
import wind.world.ItemHandler;

/**
 * Indicates Several Usage Of Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class ItemAssistant {

	private Client c;

	public ItemAssistant(Client client) {
		this.c = client;
	}
	/**
	 * Check all slots and determine whether or
	 * not a slot is accompanied by that item
	 */
	public boolean isWearingItem(int itemID) {
		for(int i = 0; i < 12; i++) {
			if(c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}
	/**
     * Add the items kept on death to the player's inventory.
     */
    public void itemsToInventory()
    {
    	Iterator<?> iter = Player.itemsToInventory.entrySet().iterator();
		while (iter.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry mEntry = (Map.Entry) iter.next();
        	addItem((int) mEntry.getKey(), (int) mEntry.getValue());
		}
    }
    
    /**
     * Store the data of the items that will be sent to inventory.
     */
    public void getItemsToInventory()
    {
        Player.itemsToInventory.clear();
		for (int item = 0; item < Config.ITEM_TRADEABLE.length; item++)
        {
                int itemId = Config.ITEM_TRADEABLE[item];
                int itemAmount = getItemAmount(itemId) + getWornItemAmount(itemId);
                if (playerHasItem(itemId) || isWearingItem(itemId))
                {
                	Player.itemsToInventory.put(itemId, itemAmount);
                }
        }
    }
	
	/**
	 * Check all slots and determine the amount
	 * of said item worn in that slot
	 */
	public int getWornItemAmount(int itemID) {
		for(int i = 0; i < 12; i++) {
			if(c.playerEquipment[i] == itemID) {
				return c.playerEquipmentN[i];
			}
		}
		return 0;
	}

	public int[] Nests = { 5291, 5292, 5293, 5294, 5295, 5296, 5297, 5298,
			5299, 5300, 5301, 5302, 5303, 5304 };

	public void handleNests(int itemId) {
		int reward = Nests[Misc.random(14)];
		addItem(reward, 3 + Misc.random(5));
		deleteItem(itemId, 1);
		c.sendMessage("You search the nest");
	}

	// TODO
	public void keepUntradeables() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if (tradeable(c.playerEquipment[i1])) {
				deleteEquipment(c.playerEquipment[i1], i1);
			}
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			if (tradeable(c.playerItems[i] - 1)) {
				deleteItem(c.playerItems[i] - 1,
						getItemSlot(c.playerItems[i] - 1), c.playerItemsN[i]);
			}
		}
	}

	/**
	 * Trimmed and untrimmed skillcapes.
	 */
	public int[][] skillcapes = { { 9747, 9748 }, // Attack
			{ 9753, 9754 }, // Defence
			{ 9750, 9751 }, // Strength
			{ 9768, 9769 }, // Hitpoints
			{ 9756, 9757 }, // Range
			{ 9759, 9760 }, // Prayer
			{ 9762, 9763 }, // Magic
			{ 9801, 9802 }, // Cooking
			{ 9807, 9808 }, // Woodcutting
			{ 9783, 9784 }, // Fletching
			{ 9798, 9799 }, // Fishing
			{ 9804, 9805 }, // Firemaking
			{ 9780, 9781 }, // Crafting
			{ 9795, 9796 }, // Smithing
			{ 9792, 9793 }, // Mining
			{ 9774, 9775 }, // Herblore
			{ 9771, 9772 }, // Agility
			{ 9777, 9778 }, // Thieving
			{ 9786, 9787 }, // Slayer
			{ 9810, 9811 }, // Farming
			{ 9765, 9766 } // Runecraft
	};

	/**
	 * Broken barrows items.
	 */
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4794 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	/**
	 * Empties all of (a) player's items.
	 */
	public void resetItems(int WriteFrame) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(53);
				c.getOutStream().writeWord(WriteFrame);
				c.getOutStream().writeWord(c.playerItems.length);
				for (int i = 0; i < c.playerItems.length; i++) {
					if (c.playerItemsN[i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
					} else {
						c.getOutStream().writeByte(c.playerItemsN[i]);
					}
					c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}

	public void addItemToBank(int itemId, int amount) {
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0 || c.bankItems[i] == itemId + 1
					&& c.bankItemsN[i] + amount < Integer.MAX_VALUE) {
				c.bankItems[i] = itemId + 1;
				c.bankItemsN[i] += amount;
				resetBank();
				return;
			}
		}
	}

	public int getRiskCarried() {
		int toReturn = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			c.getShops();
			toReturn += ShopAssistant.getItemShopValue(c.playerEquipment[i])
					* c.playerEquipmentN[i];
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			c.getShops();
			toReturn += ShopAssistant.getItemShopValue(c.playerItems[i])
					* c.playerItemsN[i];
			// toReturn += c.getShops().getItemShopValue(c.playerItems[i]);
			// toReturn +=
			// (c.getShops().getItemShopValue([c.playerItems[i]].ShopValue *
			// c.playerItemsN[i]));
		}
		if (toReturn > Integer.MAX_VALUE)
			toReturn = Integer.MAX_VALUE;
		return toReturn;
	}

	/**
	 * Counts (a) player's items.
	 * 
	 * @param itemID
	 * @return count start
	 */
	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == itemID + 1) {
				count += c.playerItemsN[j];
			}
		}
		return count;
	}

	public void replaceItem(Client c, int i, int l) {
		for (int k = 0; k < c.playerItems.length; k++) {
			if (playerHasItem(i, 1)) {
				deleteItem(i, getItemSlot(i), 1);
				addItem(l, 1);
			}
		}
	}

	/**
	 * Gets the total count of (a) player's items.
	 * 
	 * @param itemID
	 * @return
	 */
	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (Item.itemIsNote[itemID + 1]) {
				if (itemID + 2 == c.playerItems[j])
					count += c.playerItemsN[j];
			}
			if (!Item.itemIsNote[itemID + 1]) {
				if (itemID + 1 == c.playerItems[j]) {
					count += c.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < c.bankItems.length; j++) {
			if (c.bankItems[j] == itemID + 1) {
				count += c.bankItemsN[j];
			}
		}
		return count;
	}

	/**
	 * Send the items kept on death.
	 */
	public void sendItemsKept() {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(53);
				c.getOutStream().writeWord(6963);
				c.getOutStream().writeWord(c.itemKeptId.length);
				for (int i = 0; i < c.itemKeptId.length; i++) {
					if (c.playerItemsN[i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(1);
					} else {
						c.getOutStream().writeByte(1);
					}
					if (c.itemKeptId[i] > 0) {
						c.getOutStream().writeWordBigEndianA(
								c.itemKeptId[i] + 1);
					} else {
						c.getOutStream().writeWordBigEndianA(0);
					}
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}

	/**
	 * Item kept on death
	 **/
	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] - 1 > 0) {
				c.getShops();
				int inventoryItemValue = ShopAssistant.getItemShopValue(
						c.playerItems[i] - 1);
				if (inventoryItemValue > value && (!c.invSlot[i])) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if (c.playerEquipment[i1] > 0) {
				c.getShops();
				int equipmentItemValue = ShopAssistant.getItemShopValue(
						c.playerEquipment[i1]);
				if (equipmentItemValue > value && (!c.equipSlot[i1])) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(c.playerItems[slotId] - 1,
						getItemSlot(c.playerItems[slotId] - 1), 1);
			}
		} else {
			c.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		c.itemKeptId[keepItem] = item;
	}

	/**
	 * Reset items kept on death.
	 **/
	public void resetKeepItems() {
		for (int i = 0; i < c.itemKeptId.length; i++) {
			c.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.invSlot.length; i1++) {
			c.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.equipSlot.length; i2++) {
			c.equipSlot[i2] = false;
		}
	}

	/**
	 * Deletes all of a player's items.
	 **/
	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			deleteEquipment(c.playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			deleteItem(c.playerItems[i] - 1, getItemSlot(c.playerItems[i] - 1),
					c.playerItemsN[i]);
		}
	}

	/**
	 * Drops all items for a killer.
	 **/
	public void dropAllItems() {
		Client o = (Client) PlayerHandler.players[c.killerId];
		//c.KC = (c.KC +1);
		for (int i = 0; i < c.playerItems.length; i++) {
			if (o != null) {
				if (tradeable(c.playerItems[i] - 1)) {
					ItemHandler.createGroundItem(o,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.killerId);
				} else {
					if (specialCase(c.playerItems[i] - 1))
						ItemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(),
								getUntradePrice(c.playerItems[i] - 1),
								c.killerId);
			/*		ItemHandler.createGroundItem(c,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.playerId); */
				}
			} else {
				ItemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (o != null) {
				if (tradeable(c.playerEquipment[e])) {
					ItemHandler.createGroundItem(o,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.killerId);
				} else {
					if (specialCase(c.playerEquipment[e]))
						ItemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(),
								getUntradePrice(c.playerEquipment[e]),
								c.killerId);
					ItemHandler.createGroundItem(c,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.playerId);
				}
			} else {
				ItemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			}
		}
		if (o != null) {
			Client victim = c;
			Client killer = o;
			int lowestValueId = 0;
			int lowestValueKey = 0;
			int keyValue = 0;
			int victimValue = 0;
			
			killer.setLastPlayerKilled(victim);
			
			PlayerItem[] loot = BankKey.Bank.GetLosses(victim);

			BankKey.Bank.Remove(victim, loot);
			
			killer.asClient().sendMessage("You've slain " + victim.playerName + "!");
			o.KC = (o.KC + 1);
			
			for (int i = 0; 5 > i; i++) {
				
				if (killer.getLoot(i) == null) { // If a loot slot is available
					BankKey.Session.SaveLoot(killer, i, loot);
					ItemHandler.createGroundItem(killer, (15000 + i), victim.getX(), victim.getY(), 1, victim.killerId); // Drop the key
					return;
				}
				if(i==4){
					for (int l = 0; 5 > l; l++) {
						if (killer.getLoot(l) != null) {
							PlayerItem[] lowest = killer.getLoot(l);
							keyValue = 0;
							for (int j = 0; lowest.length > j; j++) {
								keyValue = keyValue+(ShopAssistant.getItemShopValue(lowest[j].getId())*lowest[j].getAmount());
							}
							if (l==0){
								lowestValueId=l;
								lowestValueKey=keyValue;
							}
							else if(keyValue<lowestValueKey){
								lowestValueId=l;
								lowestValueKey=keyValue;
							}
						}
					}
					
					for (int m = 0; loot.length>m; m++) {
						victimValue = victimValue+(ShopAssistant.getItemShopValue(loot[m].getId())*loot[m].getAmount());
					}
					
					if (victimValue>lowestValueKey){
						BankKey.Session.SaveLoot(killer, lowestValueId, loot);
						killer.asClient().sendMessage("@blu@Your lowest value key has been replaced with a more precious one.");
					}
					else {
						killer.asClient().sendMessage("@blu@Victim's key is worth less than all carried keys, and has been destroyed.");
					}
				}
			}
		}
	}

	/**
	 * Untradable items with a special currency. (Tokkel, etc)
	 * 
	 * @param item
	 * @return amount
	 */
	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	/**
	 * Special items with currency.
	 */
	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	/**
	 * Voided items. (Not void knight items..)
	 * 
	 * @param itemId
	 */
	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			c.voidStatus[0]++;
			break;
		case 2520:
			c.voidStatus[1]++;
			break;
		case 2522:
			c.voidStatus[2]++;
			break;
		case 2524:
			c.voidStatus[3]++;
			break;
		case 2526:
			c.voidStatus[4]++;
			break;
		}
	}

	/**
	 * Handles tradable items.
	 */
	public boolean tradeable(int itemId) {
		for (int j = 0; j < Config.ITEM_TRADEABLE.length; j++) {
			if (itemId == Config.ITEM_TRADEABLE[j])
				return false;
		}
		return true;
	}

	public boolean updateInventory = false;

	public void updateInventory() {
		updateInventory = false;
		resetItems(3214);
	}

	/**
	 * Add Item
	 **/
	public boolean forceAddItem(int item, int amount) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		int freeSlotId = freeSlotId();
		boolean freeSlot = freeSlotId != -1;
		int itemSlot = getItemSlot(item);
		if (((freeSlot || itemSlot != -1) && Item.itemStackable[item])
				|| ((freeSlot) && !Item.itemStackable[item])) {
			if (itemSlot != -1 && Item.itemStackable[item]
					&& (c.playerItems[itemSlot] == (item + 1))) {
				c.playerItems[itemSlot] = (item + 1);
				if (((c.playerItemsN[itemSlot] + amount) < Config.MAXITEM_AMOUNT)
						&& ((c.playerItemsN[itemSlot] + amount) > -1)) {
					c.playerItemsN[itemSlot] += amount;
				} else {
					c.sendMessage("The item has dropped because you had the maximum of that item.");
					ItemHandler.createGroundItem(c, item, c.absX, c.absY,
							amount, c.playerId);
					return false;
				}
				updateInventory = true;
				return true;
			}
			if (c.lastSlot > -1) {
				if (c.playerItems[c.lastSlot] <= 0) {
					c.playerItems[c.lastSlot] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[c.lastSlot] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							c.lastSlot = -1;
							return true;
						}
					} else {
						c.playerItemsN[c.lastSlot] = Config.MAXITEM_AMOUNT;
					}
					updateInventory = true;
					c.lastSlot = -1;
					return true;
				}
			}
			if (c.playerItems[freeSlotId] > 0)
				return false;
			c.playerItems[freeSlotId] = item + 1;
			if (amount < Config.MAXITEM_AMOUNT) {
				if (Item.itemStackable[item]) {
					c.playerItemsN[freeSlotId] = amount;
				} else {
					c.playerItemsN[freeSlotId] = 1;
					if (amount > 1) {
						c.getItems().addItem(item, amount - 1);
						return true;
					}
				}
			} else
				c.playerItemsN[freeSlotId] = Config.MAXITEM_AMOUNT;
			updateInventory = true;
			return true;
		} else {
			c.sendMessage("The item has dropped because you had no inventory space.");
			ItemHandler.createGroundItem(c, item, c.absX, c.absY, amount,
					c.playerId);
			return false;
		}
	}

	/**
	 * Adds an item to a player's inventory.
	 **/


public boolean addItem(int item, int amount) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if ((((c.getEquipment().freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((c.getEquipment().freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if ((c.playerItems[i] == (item + 1))
						&& Item.itemStackable[item] && (c.playerItems[i] > 0)) {
					c.playerItems[i] = (item + 1);
					if (((c.playerItemsN[i] + amount) < Config.MAXITEM_AMOUNT)
							&& ((c.playerItemsN[i] + amount) > -1)) {
						c.playerItemsN[i] += amount;
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else {
							c.getOutStream().writeByte(c.playerItemsN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}


	public void deleteItem(int id, int amount) {
		if (id <= 0)
			return;
		
		
		for (int j = 0; j < c.playerItems.length; j++) {
			if (amount <= 0)
				break;
			if (c.playerItems[j] == id + 1) {
				c.playerItems[j] = 0;
				c.playerItemsN[j] = 0;
				amount--;
			}
		}
		resetItems(3214);
	}

	public String itemType(int item) {
		if (Item.playerCapes(item)) {
			return "cape";
		}
		if (Item.playerBoots(item)) {
			return "boots";
		}
		if (Item.playerGloves(item)) {
			return "gloves";
		}
		if (Item.playerShield(item)) {
			return "shield";
		}
		if (Item.playerAmulet(item)) {
			return "amulet";
		}
		if (Item.playerArrows(item)) {
			return "arrows";
		}
		if (Item.playerRings(item)) {
			return "ring";
		}
		if (Item.playerHats(item)) {
			return "hat";
		}
		if (Item.playerLegs(item)) {
			return "legs";
		}
		if (Item.playerBody(item)) {
			return "body";
		}
		return "weapon";
	}

	/**
	 * Item bonuses.
	 **/
	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic",
			"Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength",
	"Prayer" };

	/**
	 * Weapon type.
	 **/
	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		/**
		 * Attack styles.
		 */
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855);
			c.getPA().sendFrame126(WeaponName, 5857);

		} else if (WeaponName.contains("whip")) {
			c.setSidebarInterface(0, 12290);
			c.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.contains("tentacle")) {
			c.setSidebarInterface(0, 12290);
			c.getPA().sendFrame126(WeaponName, 12293);


		} else if (WeaponName.contains("bow") || WeaponName.contains("10")
				|| WeaponName.contains("full")
				|| WeaponName.contains("Seercull") || WeaponName.contains("blowpipe")) {
			c.setSidebarInterface(0, 1764);
			c.getPA().sendFrame126(WeaponName, 1767);

		} else if (WeaponName2.contains("halberd")
				|| WeaponName2.contains("of light")) {
			c.setSidebarInterface(0, 8460);
			c.getPA().sendFrame126(WeaponName, 8463);

		} else if (WeaponName.contains("Staff") || WeaponName.contains("staff")
				|| WeaponName.contains("wand")) {
			c.setSidebarInterface(0, 328);
			c.getPA().sendFrame126(WeaponName, 355);

		} else if (WeaponName2.contains("dart")
				|| WeaponName2.contains("knife")
				|| WeaponName2.contains("javelin")
				|| WeaponName.equalsIgnoreCase("toktz-xil-ul")
				|| WeaponName.equalsIgnoreCase("Morrigan's throwing axe")) {
			c.setSidebarInterface(0, 4446);
			c.getPA().sendFrame126(WeaponName, 4449);

		} else if (WeaponName2.contains("dagger")
				|| WeaponName2.contains("sword")) {
			c.setSidebarInterface(0, 2276);
			c.getPA().sendFrame126(WeaponName, 2279);

		} else if (WeaponName2.contains("warhammer")
				|| WeaponName2.contains("maul")) {
			c.setSidebarInterface(0, 425);
			c.getPA().sendFrame126(WeaponName, 428);

		} else if (WeaponName2.contains("pickaxe")) {
			c.setSidebarInterface(0, 5570);
			c.getPA().sendFrame126(WeaponName, 5573);

		} else if (WeaponName2.contains("axe")
				|| WeaponName2.contains("battleaxe")
				|| WeaponName2.contains("hatchet")) {
			c.setSidebarInterface(0, 1698);
			c.getPA().sendFrame126(WeaponName, 1701);

		} else if (WeaponName2.contains("Scythe")) {
			c.setSidebarInterface(0, 776);
			c.getPA().sendFrame126(WeaponName, 779);

		} else if (WeaponName2.contains("spear")) {
			c.setSidebarInterface(0, 4679);
			c.getPA().sendFrame126(WeaponName, 4682);

		} else if (WeaponName2.contains("mace")
				|| WeaponName2.contains("anchor")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame126(WeaponName, 3799);

		} else if (WeaponName2.contains("claw")) {
			c.setSidebarInterface(0, 7762);
			c.getPA().sendFrame126(WeaponName, 7765);

		} else if (WeaponName2.contains("chinchompa")) {
			c.setSidebarInterface(0, 24055);
			c.getPA().sendFrame126(WeaponName, 24056);

		} else if (WeaponName2.contains("salamander")) {
			c.setSidebarInterface(0, 24074);
			c.getPA().sendFrame126(WeaponName, 24075);

		} else {
			c.setSidebarInterface(0, 2423);
			c.getPA().sendFrame126(WeaponName, 2426);
		}

	}

	/**
	 * Weapon requirements.
	 **/
	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 20;
				c.attackLevelReq = 40;
			} else {
				c.magicLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}
		if (itemName.contains("slayer helmet")) {
			c.defenceLevelReq = 10;
		}
		if (itemName.contains("initiate")) {
			c.defenceLevelReq = 20;
		}
		if (itemName.contains("infinity")) {
			c.magicLevelReq = 50;
			c.defenceLevelReq = 25;
		}
		if (itemName.contains("splitbark")) {
			c.magicLevelReq = 40;
			c.defenceLevelReq = 40;
		}
		if (itemName.contains("blowpipe")) {
			c.rangeLevelReq = 75;
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("d'hide")) {
				c.rangeLevelReq = 70;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Green")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 40;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Blue")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 50;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Red")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 60;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 70;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("varrock")) {
				c.defenceLevelReq = 65;
		}
		if (itemName.contains("Dragon defender")) {
			c.defenceLevelReq = 60;
		}
	
		if (itemName.contains("serpentine")) {
			c.defenceLevelReq = 75;
	}
		if (itemName.contains("magma")) {
			c.defenceLevelReq = 75;
	}
		if (itemName.contains("tanzanite")) {
			c.defenceLevelReq = 75;
	}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("cavalier") && !itemName.contains("knife")
					&& !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")) {
				c.attackLevelReq = c.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant") || itemName.contains("adam")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rock-shell")) {
			c.defenceLevelReq = 40;
		}
		if (itemName.contains("zamorak")) {
			if (!itemName.contains("robe") && !itemName.contains("mitre")
					&& !itemName.contains("stole")
					&& !itemName.contains("godsword")) {
				c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("saradomin")) {
			if (!itemName.contains("robe") && !itemName.contains("mitre")
					&& !itemName.contains("stole")
					&& !itemName.contains("godsword")) {
				c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("guthix")) {
			if (!itemName.contains("robe") && !itemName.contains("mitre")
					&& !itemName.contains("stole")
					&& !itemName.contains("godsword")) {
				c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("gloves") && !itemName.contains("knife")
					&& !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")) {
				c.attackLevelReq = c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				c.attackLevelReq = c.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				c.defenceLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 70;
				c.attackLevelReq = 70;
			} else {
				c.magicLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				c.rangeLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("godsword")) {
			c.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			c.defenceLevelReq = 60;
		}
		if (itemName.contains("Initiate")) {
			c.defenceLevelReq = 20;
		}
		if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
		}

		switch (itemId) {
		case 11720:
		case 11718:
		case 11722:
			c.defenceLevelReq = 70;
			c.rangeLevelReq = 70;
			return;
		case 10887:
			c.strengthLevelReq = 60;
			return;
		case 2497:
		case 2491:
			c.rangeLevelReq = 70;
			return;
		case 6528:
			c.strengthLevelReq = 60;
			return;
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			c.attackLevelReq = 42;
			c.rangeLevelReq = 42;
			c.strengthLevelReq = 42;
			c.magicLevelReq = 42;
			c.defenceLevelReq = 42;
			return;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			c.defenceLevelReq = 40;
			return;
		case 11235:
		case 6522:
			c.rangeLevelReq = 60;
			break;
		case 6524:
			c.defenceLevelReq = 60;
			break;
		case 11284:
			c.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			c.magicLevelReq = 60;
			break;
		case 861:
			c.rangeLevelReq = 50;
			break;
		case 10828:
			c.defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			c.defenceLevelReq = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			c.defenceLevelReq = 40;
			break;

		case 11283:
			c.defenceLevelReq = 75;
			break;

		case 851:
		case 853:
			c.rangeLevelReq = 30;
			break;

		case 847:
		case 849:
			c.rangeLevelReq = 20;
			break;

		case 845:
		case 843:
			c.rangeLevelReq = 5;
			break;

		case 5698:
			c.attackLevelReq = 60;
			break;

		case 7462:
		case 7461:
			c.defenceLevelReq = 40;
			break;
		case 8846:
			c.defenceLevelReq = 5;
			break;
		case 8847:
			c.defenceLevelReq = 10;
			break;
		case 8848:
			c.defenceLevelReq = 20;
			break;
		case 8849:
			c.defenceLevelReq = 30;
			break;
		case 8850:
			c.defenceLevelReq = 40;
			break;

		case 7460:
			c.defenceLevelReq = 40;
			break;

		case 837:
			c.rangeLevelReq = 61;
			break;

		case 14006:
			c.attackLevelReq = 70;
			return;

		case 4151:
			c.attackLevelReq = 70;
			return;

		case 6724:
			c.rangeLevelReq = 60;
			return;
		case 4153:
			c.attackLevelReq = 50;
			c.strengthLevelReq = 50;
			return;
		}
	}

	/**
	 * Two handed weapon check.
	 **/
	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil")
				|| itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow")
				|| itemName.contains("ark bow") || itemName.contains("blowpipe")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword")
				|| itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear")) {
			return true;
		}
		switch (itemId) {
		case 6724:
		case 11838:
		case 4153:
		case 6528:
		case 10887:
		case 11777:
			return true;
		}
		return false;
	}

	/**
	 * Adds special attack bar to special attack weapons. Removes special attack
	 * bar to weapons that do not have special attacks.
	 **/
	public void addSpecialBar(int weapon) {
		switch (weapon) {
		case 1215: // Dragon Dagger
		case 1231: // Dragon Dagger(p)
		case 5680: // Dragon Dagger(p+)
		case 5698: // Dragon Dagger(s)
		case 1305: // Dragon Longsword
		case 19780: // Korasi Sword
		case 13899: // Vesta's Longsword
		case 11838: // Saradomin Sword
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;

		case 4587: // Dragon Scimitar
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;

		case 1434: // Dragon Mace
		case 10887: // Barrelchest Anchor
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;
		case 11785:
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
		break;

		case 1377: // Dragon Battleaxe
		case 6739: // Dragon Hatchet
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;

		case 3204: // Dragon Halberd
		case 15486: // Staff of light
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;

		case 1249: // Dragon Spear
		case 1263: // Dragon Spear(p)
		case 5716: // Dragon Spear(p+)
		case 5730: // Dragon Spear(s)
		case 11716: // Zamorakian Spear
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;

		case 7158: // Dragon 2h Sword
		case 11802: // Armadyl Godsword
		case 11804: // Bandos Godsword
		case 11806: // Saradomin Godsword
		case 11808: // Zamorak Godsword
			// case 11730: //Saradomin Sword
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;

		case 14484: // Dragon Claws
			c.getPA().sendFrame171(0, 7800);
			specialAmount(weapon, c.specAmount, 7812);
			break;

		case 15259: // Dragon Pickaxe
			c.getPA().sendFrame171(0, 7724);
			specialAmount(weapon, c.specAmount, 7736);
			break;

		case 4151:
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;

		case 14006:
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;


		case 4153: // Granite Maul
		case 13902: // Statius Warhammer
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;

		case 859: // Magic Longbow
		case 861: // Magic Shortbow
		case 12926:
		case 11235: // Dark Bow
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;

		case 13883: // Morrigan's Throwing Axe
		case 13879: // Morrigan's Javelin
			c.getPA().sendFrame171(0, 7649);
			specialAmount(weapon, c.specAmount, 7661);
			break;

		}
	}

	/**
	 * Special attack bar filling amount.
	 **/
	/*public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text.
	 **/
	/*public void updateSpecialBar() {
		String percent = Double.toString(c.specAmount);
		if (percent.contains(".")) {
			percent = percent.replace(".", "");
		}
		if (percent.startsWith("0") && !percent.equals("00")) {
			percent = percent.replace("0", "");
		}
		if (percent.startsWith("0") && percent.equals("00")) {
			percent = percent.replace("00", "0");
		}
		//c.sendMessage(Double.toString(c.specAmount) + ":specialattack:");
		c.getPA()
		.sendFrame126(
				c.usingSpecial ? "@yel@Special Attack (" + percent
						+ "%)" : "@bla@Special Attack (" + percent
						+ "%)", c.specBarId);
	}*/
	
	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text.
	 **/
	public void updateSpecialBar() {
		String percent = Double.toString(c.specAmount);
		if (percent.contains(".")) {
			percent = percent.replace(".", "");
		}
		if (percent.startsWith("0") && !percent.equals("00")) {
			percent = percent.replace("0", "");
		}
		if (percent.startsWith("0") && percent.equals("00")) {
			percent = percent.replace("00", "0");
		}
		c.getPA().sendFrame126(c.usingSpecial ? "@yel@Special Attack (" + percent + "%)" : "@bla@Special Attack (" + percent + "%)", c.specBarId);
	}

	/**
	 * Wielding items.
	 **/
	public boolean wearItem(int wearID, int slot) {
		int targetSlot = 0;
		boolean canWearItem = true;
		PlayerItem item = ItemTableManager.forID(wearID);
		if (c.playerItems[slot] == (wearID + 1)) {
			getRequirements(getItemName(wearID).toLowerCase(), wearID);
			String wearSlot = item.getEquipSlot();
			targetSlot = Item.targetSlots[wearID];
			if (wearSlot.equalsIgnoreCase("cape")) {
				targetSlot = 1;
			} else if (wearSlot.equalsIgnoreCase("hat")) {
				targetSlot = 0;
			} else if (wearSlot.equalsIgnoreCase("amulet")) {
				targetSlot = 2;
			} else if (wearSlot.equalsIgnoreCase("arrows")) {
				targetSlot = 13;
			} else if (wearSlot.equalsIgnoreCase("body")) {
				targetSlot = 4;
			} else if (wearSlot.equalsIgnoreCase("shield")) {
				targetSlot = 5;
			} else if (wearSlot.equalsIgnoreCase("legs")) {
				targetSlot = 7;
			} else if (wearSlot.equalsIgnoreCase("gloves")) {
				targetSlot = 9;
			} else if (wearSlot.equalsIgnoreCase("boots")) {
				targetSlot = 10;
			} else if (wearSlot.equalsIgnoreCase("ring")) {
				targetSlot = 12;
			} else {
				targetSlot = 3;

			}
			switch (wearID) {
			/* Gloves */
			case 10336:
			case 9922:
			case 8842:
				targetSlot = 9;
				break;
				/* Arrows */
			case 9144:
				targetSlot = 13;
				break;
				/* Capes */
			case 9813:
			case 9747:
			case 9748:
			case 9750:
			case 9790:
			case 9751:
			case 9753:
			case 9754:
			case 9756:
			case 9757:
			case 9759:
			case 9760:
			case 9762:
			case 9763:
			case 9765:
			case 9766:
			case 9768:
			case 9769:
			case 9771:
			case 9772:
			case 9774:
			case 9775:
			case 10446:
			case 10448:
			case 10450:
			case 9777:
			case 9778:
			case 9780:
			case 9781:
			case 9783:
			case 9784:
			case 9786:
			case 9787:
			case 9792:
			case 9793:
			case 9795:
			case 9796:
			case 9798:
			case 9799:
			case 9801:
			case 9802:
			case 9804:
			case 9805:
			case 9807:
			case 9808:
			case 9810:
			case 9811:
			case 10499:
				targetSlot = 1;
				break;

				/* Arrows */
			case 9244:
				targetSlot = 13;
				break;

				/* Boots */
			case 15037:
			case 14605:
			case 11019:
			case 9921:
			case 11728:
			case 10839:
				targetSlot = 10;
				break;

				/* Legs */
			case 11726:
			case 11722:
			case 9678:
			case 9923:
			case 9676:
			case 10394:
			case 8840:
			case 15035:
			case 10332:
			case 15036:
			case 14603:
			case 14938:
			case 14077:
			case 10346:
			case 10372:
			case 10838:
			case 11022:
			case 10388:
			case 10380:
			case 10340:
			case 15425:
			case 13360:
			case 13352:
			case 13346:
				targetSlot = 7;
				break;

				/* Amulets */
			case 6861:
			case 6859:
			case 6863:
			case 9470:
			case 6857:
			case 10344:
			case 11128:
				targetSlot = 2;
				break;

				/* Shields */
			case 8850:
			case 8849:
			case 8848:
			case 8847:
			case 8846:
			case 8845:
			case 8844:
			case 11283:
			case 10352:
				targetSlot = 5;
				break;

				/* Bodies */
			case 10551:
			case 10348:
			case 9674:
			case 10837:
			case 14936:
			case 15034:
			case 10386:
			case 10370:
			case 11720:
			case 10330:
			case 15423:
			case 14076:
			case 11020:
			case 14595:
			case 8839:
			case 10338:
			case 13348:
			case 13354:
			case 13358:
			case 9924:
			case 11724:
				targetSlot = 4;
				break;

				/* Helms */
			case 13263:
			case 9920:
			case 10507:
			case 10836:
			case 10828:
			case 9672:
			case 10334:
			case 10350:
			case 10390:
			case 11718:
			case 10374:
			case 11021:
			case 15422:
			case 15033:
			case 9925:
			case 13362:
			case 11663:
			case 11664:
			case 11665:
			case 13355:
			case 13350:
			case 10342:
			case 1037:
			case 11335:
			case 10548:
			case 9749:
			case 9752:
			case 9755:
			case 9758:
			case 9761:
			case 9764:
			case 9767:
			case 9770:
			case 9773:
			case 9776:
			case 9779:
			case 9782:
			case 9785:
			case 9788:
			case 9791:
			case 9794:
			case 9797:
			case 9800:
			case 9803:
			case 9806:
			case 13199:
			case 13197:
			case 9809:
			case 9812:
				targetSlot = 0;
				break;

				/* Boots */
			case 11732:
				targetSlot = 10;
				break;
			}

			if (wearID == 5733) {
				canWearItem = false;
				if (c.getRights().greaterOrEqual(Rights.DEVELOPER)) {
					c.getPA().sendInterface(6600);
					c.getPA().sendFrame126(""+c.tradeBond, 6603);
					c.getPA().sendFrame126(""+c.untradeBond, 6605);
				}
				else {
					c.getItems().deleteItem(5733, 28);
					c.sendMessage("How did you get that?");
				}
				return false;
			}
			
			if (wearID == 13192) {
				MembershipBond.initConvert(c);
				canWearItem = false;
				return false;
			}

			if (c.duelRule[11] && targetSlot == 0) {
				c.sendMessage("Wearing hats has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[12] && targetSlot == 1) {
				c.sendMessage("Wearing capes has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[13] && targetSlot == 2) {
				c.sendMessage("Wearing amulets has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[14] && targetSlot == 3) {
				c.sendMessage("Wielding weapons has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[15] && targetSlot == 4) {
				c.sendMessage("Wearing bodies has been disabled in this duel!");
				return false;
			}
			if ((c.duelRule[16] && targetSlot == 5)
					|| (c.duelRule[16] && is2handed(getItemName(wearID)
							.toLowerCase(), wearID))) {
				c.sendMessage("Wearing shield has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[17] && targetSlot == 7) {
				c.sendMessage("Wearing legs has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[18] && targetSlot == 9) {
				c.sendMessage("Wearing gloves has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[19] && targetSlot == 10) {
				c.sendMessage("Wearing boots has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[20] && targetSlot == 12) {
				c.sendMessage("Wearing rings has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[21] && targetSlot == 13) {
				c.sendMessage("Wearing arrows has been disabled in this duel!");
				return false;
			}

			if (Config.itemRequirements) {
				if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5
						|| targetSlot == 4 || targetSlot == 0
						|| targetSlot == 9 || targetSlot == 10) {
					if (c.defenceLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[1]) < c.defenceLevelReq) {
							c.sendMessage("You need a defence level of "
									+ c.defenceLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (c.rangeLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
							c.sendMessage("You need a range level of "
									+ c.rangeLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (c.magicLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
							c.sendMessage("You need a magic level of "
									+ c.magicLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
				}
				if (targetSlot == 3) {
					if (c.attackLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[0]) < c.attackLevelReq) {
							c.sendMessage("You need an attack level of "
									+ c.attackLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.rangeLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
							c.sendMessage("You need a range level of "
									+ c.rangeLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.magicLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
							c.sendMessage("You need a magic level of "
									+ c.magicLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
				}
			}

			if (!canWearItem) {
				return false;
			}
			if (Config.SOUND)
				c.getPA().sendSound(230);
			int wearAmount = c.playerItemsN[slot];
			if (wearAmount < 1) {
				return false;
			}

			if (targetSlot == c.playerWeapon) {
				c.autocasting = false;
				c.autocastId = 0;
				c.getPA().sendFrame36(108, 0);
			}

			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.playerItems[slot];
				int toEquipN = c.playerItemsN[slot];
				int toRemove = c.playerEquipment[targetSlot];
				int toRemoveN = c.playerEquipmentN[targetSlot];
				if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(
							getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase(),
							c.playerEquipment[c.playerWeapon]);
					@SuppressWarnings("unused")
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					if (wearing2h) {
						toRemove = c.playerEquipment[c.playerWeapon];
						toRemoveN = c.playerEquipmentN[c.playerWeapon];
						c.playerEquipment[c.playerWeapon] = -1;
						c.playerEquipmentN[c.playerWeapon] = 0;
						c.getEquipment().updateSlot(c.playerWeapon);
					}
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(),
							wearID);
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					boolean wearingWeapon = c.playerEquipment[c.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (c.getEquipment().freeSlots() > 0) {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(c.playerEquipment[c.playerShield],
										c.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							c.playerItems[slot] = c.playerEquipment[c.playerShield] + 1;
							c.playerItemsN[slot] = c.playerEquipmentN[c.playerShield];
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.playerEquipment[c.playerShield] = -1;
							c.playerEquipmentN[c.playerShield] = 0;
							c.getEquipment().updateSlot(c.playerShield);
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				resetItems(3214);
			}
			if (targetSlot == 3) {
				c.usingSpecial = false;
				addSpecialBar(wearID);
			}
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}

				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			sendWeapon(c.playerEquipment[c.playerWeapon],
					getItemName(c.playerEquipment[c.playerWeapon]));
			c.getEquipment().resetBonus();
			c.getEquipment().getBonus();
			c.getEquipment().writeBonus();
			c.getItems();
			c.getCombat().getPlayerAnimIndex(
					c,
					ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon])
					.toLowerCase());
			c.getPA().requestUpdates();
			c.isFullHelm = Item.isFullHelm(c.playerEquipment[c.playerHat]);
			c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
			c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indicates the action to wear an item.
	 * 
	 * @param wearID
	 * @param wearAmount
	 * @param targetSlot
	 */
	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (wearAmount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(wearAmount);
				} else {
					c.getOutStream().writeByte(wearAmount);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipment[targetSlot] = wearID;
				c.playerEquipmentN[targetSlot] = wearAmount;
				c.getItems();
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						ItemAssistant.getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getEquipment().resetBonus();
				c.getEquipment().getBonus();
				c.getEquipment().writeBonus();
				c.getItems();
				c.getCombat().getPlayerAnimIndex(
						c,
						ItemAssistant
						.getItemName(c.playerEquipment[c.playerWeapon])
						.toLowerCase());
				Player.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			}
		}
	}

	/**
	 * Removes a wielded item.
	 **/
	public void removeItem(int wearID, int slot) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {
				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[c.playerWeapon],
							getItemName(c.playerEquipment[c.playerWeapon]));
					c.getEquipment().resetBonus();
					c.getEquipment().getBonus();
					c.getEquipment().writeBonus();
					c.getItems();
					c.getCombat().getPlayerAnimIndex(
							c,
							ItemAssistant
							.getItemName(
									c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					Player.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
					c.isFullHelm = Item.isFullHelm(c.playerEquipment[c.playerHat]);
					c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
					c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
				}
			}
		}
		// }
	}

	/**
	 * Items in your bank.
	 */
	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}

		for (int i = 0; i <= highestSlot; i++) {
			if (c.bankItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (c.bankItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.bankItems[j - spots] = c.bankItems[j];
							c.bankItemsN[j - spots] = c.bankItemsN[j];
							stop = true;
							c.bankItems[j] = 0;
							c.bankItemsN[j] = 0;
						}
					}
				}
			}
		}

		int totalItemsAfter = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItemsAfter++;
			}
		}

		if (totalItems != totalItemsAfter)
			c.disconnected = true;
	}

	/**
	 * Items displayed on the armor interface.
	 * 
	 * @param id
	 * @param amount
	 */
	public void itemOnInterface(int id, int amount) {
		// synchronized (c) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(2274);
		c.getOutStream().writeWord(1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord_v2(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().writeWordBigEndianA(id);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	/**
	 * Reseting your bank.
	 */
	public void resetBank() {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5382); // Bank
			c.getOutStream().writeWord(Config.BANK_SIZE);
			for (int i = 0; i < Config.BANK_SIZE; i++) {
				if (c.bankItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.bankItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.bankItemsN[i]);
				}
				if (c.bankItemsN[i] < 1) {
					c.bankItems[i] = 0;
				}
				if (c.bankItems[i] > Config.ITEM_LIMIT || c.bankItems[i] < 0) {
					c.bankItems[i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.bankItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Resets temporary worn items. Used in minigames, etc
	 */
	public void resetTempItems() {
		synchronized (c) {
			int itemCount = 0;
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] > -1) {
					itemCount = i;
				}
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5064);
			c.getOutStream().writeWord(itemCount + 1);
			for (int i = 0; i < itemCount + 1; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				if (c.playerItems[i] > Config.ITEM_LIMIT
						|| c.playerItems[i] < 0) {
					c.playerItems[i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Banking your item.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 * @return
	 */
	public boolean bankItem(int itemID, int fromSlot, int amount) {
		if (c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if(!c.isBanking)
			return false;
		if (!Item.itemIsNote[c.playerItems[fromSlot] - 1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
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
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankItems[toBankSlot] = c.playerItems[fromSlot];
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				}

				else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItems[toBankSlot] = c.playerItems[firstPossibleSlot];
							c.bankItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
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
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
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
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[c.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[c.playerItems[fromSlot] - 2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankItems[toBankSlot] = (c.playerItems[fromSlot] - 1);
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItems[toBankSlot] = (c.playerItems[firstPossibleSlot] - 1);
							c.bankItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
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
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
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
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.playerItems[fromSlot] - 1));
			return false;
		}
	}
	/**
	 * Checks if you have free bank slots.
	 */
	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void specialDeleteItem(int id, int amount) {
		int slot = getItemSlot(id);
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
		}
	}

	public void specialDeleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
		}
	}

	/**
	 * Getting items from your bank.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 */
	public void fromBank(int itemID, int fromSlot, int amount) {
		if (!c.isBanking) {
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			c.isBanking = false;
			return;
		}
		if (amount > 0) {
			if (c.bankItems[fromSlot] > 0) {
				if (!c.takeAsNote) {
					if (Item.itemStackable[c.bankItems[fromSlot] - 1]) {
						if (c.bankItemsN[fromSlot] > amount) {
							if (addItem((c.bankItems[fromSlot] - 1), amount)) {
								c.bankItemsN[fromSlot] -= amount;
								resetBank();
								c.getItems().resetItems(5064);
							}
						} else {
							if (addItem((c.bankItems[fromSlot] - 1),
									c.bankItemsN[fromSlot])) {
								c.bankItems[fromSlot] = 0;
								c.bankItemsN[fromSlot] = 0;
								resetBank();
								c.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.bankItemsN[fromSlot] > 0) {
								if (addItem((c.bankItems[fromSlot] - 1), 1)) {
									c.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						c.getItems().resetItems(5064);
					}
				} else if (c.takeAsNote
						&& Item.itemIsNote[c.bankItems[fromSlot]]) {
					if (c.bankItemsN[fromSlot] > amount) {
						if (addItem(c.bankItems[fromSlot], amount)) {
							c.bankItemsN[fromSlot] -= amount;
							resetBank();
							c.getItems().resetItems(5064);
						}
					} else {
						if (addItem(c.bankItems[fromSlot],
								c.bankItemsN[fromSlot])) {
							c.bankItems[fromSlot] = 0;
							c.bankItemsN[fromSlot] = 0;
							resetBank();
							c.getItems().resetItems(5064);
						}
					}
				} else {
					c.sendMessage("This item can't be withdrawn as a note.");
					if (Item.itemStackable[c.bankItems[fromSlot] - 1]) {
						if (c.bankItemsN[fromSlot] > amount) {
							if (addItem((c.bankItems[fromSlot] - 1), amount)) {
								c.bankItemsN[fromSlot] -= amount;
								resetBank();
								c.getItems().resetItems(5064);
							}
						} else {
							if (addItem((c.bankItems[fromSlot] - 1),
									c.bankItemsN[fromSlot])) {
								c.bankItems[fromSlot] = 0;
								c.bankItemsN[fromSlot] = 0;
								resetBank();
								c.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.bankItemsN[fromSlot] > 0) {
								if (addItem((c.bankItems[fromSlot] - 1), 1)) {
									c.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						c.getItems().resetItems(5064);
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
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				tempAmount += c.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	/**
	 * Checks if the item is stackable.
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isStackable(int itemID) {
		return ItemTableManager.forID(itemID).isStackable();
	}

	/**
	 * Updates the equipment tab.
	 **/
	public void setEquipment(int wearID, int amount, int targetSlot) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot] = wearID;
			c.playerEquipmentN[targetSlot] = amount;
			c.isFullHelm = Item.isFullHelm(c.playerEquipment[c.playerHat]);
			c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
			c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
			Player.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Moving Items in your bag.
	 **/
	public void moveItems(int from, int to, int moveWindow, boolean insertMode) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];
			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
		}

		if (moveWindow == 5382 && from >= 0 && to >= 0
				&& from < Config.BANK_SIZE && to < Config.BANK_SIZE
				&& to < Config.BANK_SIZE) {
			if (insertMode) {
				int tempFrom = from;
				for (int tempTo = to; tempFrom != tempTo;)
					if (tempFrom > tempTo) {
						swapBankItem(tempFrom, tempFrom - 1);
						tempFrom--;
					} else if (tempFrom < tempTo) {
						swapBankItem(tempFrom, tempFrom + 1);
						tempFrom++;
					}
			} else {
				swapBankItem(from, to);
			}
		}

		if (moveWindow == 5382) {
			resetBank();
		}
		if (moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];

			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3214) {
			resetItems(3214);
		}

	}

	public void swapBankItem(int from, int to) {
		int tempI = c.bankItems[from];
		int tempN = c.bankItemsN[from];
		c.bankItems[from] = c.bankItems[to];
		c.bankItemsN[from] = c.bankItemsN[to];
		c.bankItems[to] = tempI;
		c.bankItemsN[to] = tempN;
	}

	/**
	 * Delete item equipment.
	 **/
	public void deleteEquipment(int i, int j) {
		synchronized (c) {
			if (PlayerHandler.players[c.playerId] == null) {
				return;
			}
			if (i < 0) {
				return;
			}

			c.playerEquipment[j] = -1;
			c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
			c.getOutStream().createFrame(34);
			c.getOutStream().writeWord(6);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(j);
			c.getOutStream().writeWord(0);
			c.getOutStream().writeByte(0);
			c.getEquipment().getBonus();
			if (j == c.playerWeapon) {
				sendWeapon(-1, "Unarmed");
			}
			c.getEquipment().resetBonus();
			c.getEquipment().getBonus();
			c.getEquipment().writeBonus();
			Player.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Delete items.
	 * 
	 * @param id
	 * @param amount
	 */
//	public void deleteItem(int id, int amount) {
//		deleteItem(id, getItemSlot(id), amount);
//	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			PlayerSave.saveGame(c);
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (c.playerItems[i] == (id + 1)) {
				if (c.playerItemsN[i] > amount) {
					c.playerItemsN[i] -= amount;
					break;
				} else {
					c.playerItems[i] = 0;
					c.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete arrows.
	 **/
	public void deleteArrow() {
		synchronized (c) {
			if (c.playerEquipment[c.playerCape] == 10499 && Misc.random(5) != 1
					&& c.playerEquipment[c.playerArrows] != 4740)
				return;
			if (c.playerEquipmentN[c.playerArrows] == 1) {
				c.getItems().deleteEquipment(c.playerEquipment[c.playerArrows],
						c.playerArrows);
			}
			if (c.playerEquipmentN[c.playerArrows] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.playerArrows);
				c.getOutStream().writeWord(
						c.playerEquipment[c.playerArrows] + 1);
				if (c.playerEquipmentN[c.playerArrows] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(
							c.playerEquipmentN[c.playerArrows] - 1);
				} else {
					c.getOutStream().writeByte(
							c.playerEquipmentN[c.playerArrows] - 1);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipmentN[c.playerArrows] -= 1;
			}
			Player.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	public void deleteEquipment() {
		synchronized (c) {
			if (c.playerEquipmentN[c.playerWeapon] == 1) {
				c.getItems().deleteEquipment(c.playerEquipment[c.playerWeapon],
						c.playerWeapon);
			}
			if (c.playerEquipmentN[c.playerWeapon] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.playerWeapon);
				c.getOutStream().writeWord(
						c.playerEquipment[c.playerWeapon] + 1);
				if (c.playerEquipmentN[c.playerWeapon] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(
							c.playerEquipmentN[c.playerWeapon] - 1);
				} else {
					c.getOutStream().writeByte(
							c.playerEquipmentN[c.playerWeapon] - 1);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipmentN[c.playerWeapon] -= 1;
			}
			Player.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Dropping arrows
	 **/
	public void dropArrowNpc() {
		if (c.playerEquipment[c.playerCape] == 10499)
			return;
		int enemyX = NPCHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[c.oldNpcIndex].getY();
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed,
					enemyX, enemyY) == 0) {
				ItemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.playerName,
					c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName,
						c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, false);
				ItemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, amount + 1, c.getId());
			}
		}
	}

	/**
	 * Ranging arrows.
	 */
	public void dropArrowPlayer() {
		int enemyX = PlayerHandler.players[c.oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[c.oldPlayerIndex].getY();
		if (c.playerEquipment[c.playerCape] == 10499)
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed,
					enemyX, enemyY) == 0) {
				ItemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.playerName,
					c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName,
						c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, false);
				ItemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, amount + 1, c.getId());
			}
		}
	}

	/**
	 * Removes all items from player's equipment.
	 */
	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	public int freeSlotId() {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds the item.
	 * 
	 * @param id
	 * @param items
	 * @param amounts
	 * @return
	 */
	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the item name from the item.cfg
	 * 
	 * @param ItemID
	 * @return
	 */
	public static String getItemName(int ItemID) {
		return ItemTableManager.forID(ItemID).getName();
	}

	/**
	 * Gets the item ID from the item.cfg
	 * 
	 * @param itemName
	 * @return
	 */
	public int getItemId(String itemName) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName
						.equalsIgnoreCase(itemName)) {
					return Server.itemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}

	public int getItemId(int itemName) {
		return ItemTableManager.forName(itemName).getId();
	}

	/**
	 * Gets the item slot.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the item amount.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				itemCount += c.playerItemsN[i];
			}
		}
		return itemCount;
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
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == itemID) {
					if (c.playerItemsN[i] >= amt) {
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

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				if (c.playerItemsN[i] >= amt) {
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
	 * Dropping items
	 **/
	public void createGroundItem(int itemID, int itemX, int itemY,
			int itemAmount) {
		synchronized (c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(itemID);
			c.getOutStream().writeWord(itemAmount);
			c.getOutStream().writeByte(0);
			c.flushOutStream();
		}
	}

	public void handleSpecialPickup(int itemId) {
		// c.sendMessage("My " + getItemName(itemId) +
		// " has been recovered. I should talk to the void knights to get it back.");
		// c.getItems().addToVoidList(itemId);
	}

	/**
	 * Pickup items from the ground.
	 **/
	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		synchronized (c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(156);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWord(itemID);
			c.flushOutStream();
		}
	}

	/**
	 * Checks if a player owns a cape.
	 * 
	 * @return
	 */
	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412, 1)
				|| c.getItems().playerHasItem(2413, 1)
				|| c.getItems().playerHasItem(2414, 1))
			return true;
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] == 2412 || c.bankItems[j] == 2413
					|| c.bankItems[j] == 2414)
				return true;
		}
		if (c.playerEquipment[c.playerCape] == 2413
				|| c.playerEquipment[c.playerCape] == 2414
				|| c.playerEquipment[c.playerCape] == 2415)
			return true;
		return false;
	}

	public boolean hasPetItem(int id) {
		if (c.getItems().playerHasItem(id, 1))
			return true;
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] == id)
				return true;
		}
		/*for (petData data : PetHandler.petData.values()) {
			if (id == data.getItem()) {
				if (c.petID == data.getNpcID());
				return true;
			}
		}*/
		return false;
	}

	/**
	 * Checks if the player has all the shards.
	 * 
	 * @return
	 */
	public boolean hasAllShards() {
		return playerHasItem(11712, 1) && playerHasItem(11712, 1)
				&& playerHasItem(11714, 1);
	}

	/**
	 * Makes the godsword blade.
	 */
	public void makeBlade() {
		deleteItem(11710, 1);
		deleteItem(11712, 1);
		deleteItem(11714, 1);
		addItem(11690, 1);
		c.sendMessage("You combine the shards to make a blade.");
	}

	/**
	 * Makes the godsword.
	 * 
	 * @param i
	 */
	public void makeGodsword(int i) {
		@SuppressWarnings("unused")
		int godsword = i - 8;
		if (playerHasItem(11690) && playerHasItem(i)) {
			deleteItem(11690, 1);
			deleteItem(i, 1);
			addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	/**
	 * Checks if the item is a godsword hilt.
	 * 
	 * @param i
	 * @return
	 */
	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i % 2 == 0;
	}

	public boolean playerHasEquipped(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}

}