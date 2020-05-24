package com.johnmatczak.model;

import java.util.ArrayList;
import java.util.Arrays;

import wind.Config;
import wind.model.items.PlayerItem;
import wind.model.players.Client;
import wind.model.shops.ShopAssistant;

public class BankKey {
	
	public static class Raid {
		
		/**
		 * Opens the bank raid interface for the client for the specified loot id.
		 * 
		 * @param c
		 * @param id
		 */
		public static void OpenInterface(Client c, int id) {
			c.currentlyRaiding = id;
			
			c.getPA().sendInterface(22200);
			Refresh(c);
		}
		
		/**
		 * Take an item from the raid interface.
		 * 
		 * @param c
		 * @param id
		 */
		public static void Take(Client c, int id, int x) {
			if (c.currentlyRaiding != -1) {
				PlayerItem[] loot = c.getLoot(c.currentlyRaiding);

				if (loot != null) {
					ArrayList<PlayerItem> items = new ArrayList<PlayerItem>(Arrays.asList(loot));
					
					for (PlayerItem item : items) {
						if (item.getId() == id) {
							items.remove(item);
							
							if (x > item.getAmount()) {
								x = item.getAmount();
							}
							
							if (item.getAmount() > x) {
								items.add(new PlayerItem(item.getId(), item.getAmount() - x));
							}
							
							BankKey.Bank.Deposit(c, new PlayerItem(id, x));
							
							for (int i = 0; items.size() > i; i++) {
								c.getPA().sendFrame34a(22204, -1, i, 0);
							}
							
							c.getPA().showInterface(22200);
							
							for (int i = 0; items.size() > i; i++) {
								c.getPA().sendFrame34a(22204, items.get(i).getId(), i, items.get(i).getAmount());
							}

							c.bankKeyLoot[c.currentlyRaiding] = items.toArray(new PlayerItem[0]);
							BankKey.Raid.Refresh(c);
							break;
						}
					}
				}
			}
		}
		
		/**
		 * Take all items from the raid interface.
		 * 
		 * @param c
		 */
		public static void TakeAll(Client c) {
			if (c.currentlyRaiding != -1) {
				PlayerItem[] loot = c.getLoot(c.currentlyRaiding);
				
				if (loot != null) {
					ArrayList<PlayerItem> items = new ArrayList<PlayerItem>(Arrays.asList(loot));
					
					for (PlayerItem item : items) {
						items.remove(item);
						
						Bank.Deposit(c, item);
						
						for (int i = 0; items.size() > i; i++) {
							c.getPA().sendFrame34a(22204, -1, i, 0);
						}
						
						c.getPA().showInterface(22200);
						
						for (int i = 0; items.size() > i; i++) {
							c.getPA().sendFrame34a(22204, items.get(i).getId(), i, items.get(i).getAmount());
						}
						
						c.bankKeyLoot[c.currentlyRaiding] = items.toArray(new PlayerItem[0]);
						BankKey.Raid.Refresh(c);
						break;
					}
				}
			}
		}
		
		/**
		 * Refreshes the bank raid interface.
		 * 
		 * @param c
		 * @param id
		 */
		public static void Refresh(Client c) {
			for (int i = 0; 28 > i; i++) {
				c.getPA().sendFrame34a(22204, -1, i, 0);
			}
			
			for (int i = 0; c.getLoot(c.currentlyRaiding).length > i; i++) {
				c.getPA().sendFrame34a(22204, c.getLoot(c.currentlyRaiding)[i].getId(), i, c.getLoot(c.currentlyRaiding)[i].getAmount());
			}
			
			if (c.getLoot(c.currentlyRaiding).length == 0) {
				Remove(c, c.currentlyRaiding);
			}
		}
		
	}
	
	public static class Bank {
		
		/**
		 * Returns an organized array of PlayerItem starting from highest valued item to lowest.
		 * 
		 */
		public static PlayerItem[] SortBankItems(Client c) {
			if (c != null) {
				ArrayList<PlayerItem> items = new ArrayList<PlayerItem>();
				
				for (int i = 0; c.bankItems.length > i; i++) {
					if (c.bankItems[i] == 0)
						continue;
					
					for (int untradable : Config.ITEM_TRADEABLE)
						if (c.bankItems[i] - 1 == untradable)
							continue;
					
					items.add(new PlayerItem(c.bankItems[i] - 1, c.bankItemsN[i]));
				}
				
				// TODO: Sort by price
				/* Collections.sort(items, new Comparator<PlayerItem>() {
					@Override
					public int compare(PlayerItem i1, PlayerItem i2) {
						return (i1.getValue() > i2.getValue() ? i1.getValue() : i2.getValue());
					}
				}); */
				
				return items.toArray(new PlayerItem[items.toArray().length]);
			}
			return null;
		}
		
		/**
		 * Returns an array of PlayerItem[] consisting of the loot the client will lose to a bank raid.
		 * 
		 * @param c
		 * @return
		 */
		public static PlayerItem[] GetLosses(Client c) {
			PlayerItem[] sortedBank = SortBankItems(c);
			int value=0;
			if (c != null && sortedBank != null) {
				ArrayList<PlayerItem> items = new ArrayList<PlayerItem>();
				
				for (int i = 0; sortedBank.length > i; i++) {
					if (i > 28) break;
					value = value+(ShopAssistant.getItemShopValue(sortedBank[i].getId())*sortedBank[i].getAmount());
					
					if (sortedBank[i].getId() > 0 && sortedBank[i].getAmount() > 0) {
						items.add(new PlayerItem(sortedBank[i].getId(), sortedBank[i].getAmount()));

					}
					if (i==28){
						c.sendMessage("@red@Value lost from bank: "+value);
					}
				}
				
				for (int i = 0; c.bankKeyLoot.length > i; i++) {
					
					// For each bank key the player had on them.
					if (c.getItems().playerHasItem(i + 15000)) {
						
						// Add all of the loot to the items lost.
						for (PlayerItem item : c.bankKeyLoot[i]) {
							items.add(item);
						}
						
						// Remove the loot from the player.
						BankKey.Remove(c, i);
					}
					
				}
				
				return items.toArray(new PlayerItem[items.size()]);	
			}
			return null;
		}
		
		/**
		 * Removes all items in the loot[] array from the client's bank.
		 * 
		 * @param c
		 * @param loot
		 */
		public static void Remove(Client c, PlayerItem[] loot) {
			for (int i = 0; c.bankItems.length > i; i++) {
				
				if (c.bankItems[i] == 0 || c.bankItemsN[i] == 0)
					continue;
				
				for (PlayerItem item : loot) {
					if (c.bankItems[i] - 1 == item.getId()) {
						c.bankItems[i] = 0;
						c.bankItemsN[i] = 0;
					}
				}
				
			}
		}
		
		/**
		 * Deposit an item into client's bank.
		 * 
		 * @param c
		 * @param item
		 */
		public static void Deposit(Client c, PlayerItem item) {
			if (c != null && item != null) {
				c.getItems().addItemToBank(item.getId(), item.getAmount());
			}
		}
		
	}
	
	public static class Session {
		
		/**
		 * Saves the loot to the client's current session. 
		 * 
		 * @param c
		 * @param id
		 */
		public static void SaveLoot(Client c, int id, PlayerItem[] loot) {
			if (c != null && (id <= 5 && id >= 0)) {
				c.bankKeyLoot[id] = loot;
			}
		}
		
	}
	
	/**
	 * Remove the key from the player along with its loot.
	 * 
	 * @param c
	 * @param id
	 */
	public static void Remove(Client c, int id) {
		if (c.getItems().playerHasItem(id + 15000))
			c.getItems().deleteItem(id + 15000, 1);
		
		c.bankKeyLoot[id] = null;
	}
	
}
