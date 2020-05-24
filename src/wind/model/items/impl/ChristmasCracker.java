package wind.model.items.impl;

import java.util.Random;

import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.util.Misc;

public class ChristmasCracker {
	
	public static void handleCrackers(Client c, int itemId, int playerId) {
		Client usedOn = (Client) PlayerHandler.players[playerId];
		int phat = getRandomPhat();
		if (!c.getItems().playerHasItem(itemId))
			return;

		if (usedOn.getEquipment().freeSlots() < 1) {
			c.sendMessage("The other player doesn't have enough inventory space!");
			return;
		}
		c.gfx0(176);
		c.startAnimation(451);
		c.sendMessage("You pull the Christmas Cracker...");
		usedOn.sendMessage("You pull the Christmas Cracker...");
		c.turnPlayerTo(usedOn.absX, usedOn.absY);
		c.getItems().deleteItem(itemId, 1);
		if (Misc.random(1) == 0) {
			c.getItems().addItem(phat, 1);
			c.getItems();
			c.forcedChat("Hey! I got a " + ItemAssistant.getItemName(phat)+ "!");
			usedOn.sendMessage("You didn't get the prize.");
		} else {
			usedOn.getItems().addItem(phat, 1);
			c.getItems();
			usedOn.forcedChat("Hey! I got a " + ItemAssistant.getItemName(phat)+ "!");
			c.sendMessage("You didn't get the prize.");
		}
	}

	private static int getRandomPhat() {
		Random r = new Random();
		int[] phats = { 1038, 1040, 1042, 1044, 1046, 1048 };
		return phats[r.nextInt(phats.length)];
	}
}
