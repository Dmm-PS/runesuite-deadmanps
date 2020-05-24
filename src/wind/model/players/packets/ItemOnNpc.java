package wind.model.players.packets;

import wind.model.items.ItemAssistant;
import wind.model.items.UseItem;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.PacketType;

public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		int npcId = NPCHandler.npcs[i].npcType;
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		UseItem.ItemonNpc(c, itemId, npcId, slot);
		if (npcId == 4397) {
			if (c.deposit1 == -1) {
				c.deposit1 = itemId;
				c.getItems().deleteItem2(itemId, 1);
				c.getItems();
				c.sendMessage("You have stored @red@1x " + ItemAssistant.getItemName(itemId) + "@bla@.");
			} else if (!(c.deposit1 == -1) && c.deposit2 == -1) {
				c.deposit2 = itemId;
				c.getItems().deleteItem2(itemId, 1);
				c.getItems();
				c.sendMessage("You have stored @red@1x " + ItemAssistant.getItemName(itemId) + "@bla@.");
			} else if (!(c.deposit1 == -1) && !(c.deposit2 == -1) && c.deposit3 == -1) {
				c.deposit3 = itemId;
				c.getItems().deleteItem2(itemId, 1);
				c.getItems();
				c.sendMessage("You have stored @red@1x " + ItemAssistant.getItemName(itemId) + "@bla@.");
			} else if (!(c.deposit1 == -1) && !(c.deposit2 == -1) && !(c.deposit3 == -1) && c.deposit4 == -1) {
				c.deposit4 = itemId;
				c.getItems().deleteItem2(itemId, 1);
				c.getItems();
				c.sendMessage("You have stored @red@1x " + ItemAssistant.getItemName(itemId) + "@bla@.");
			} else if (!(c.deposit1 == -1) && !(c.deposit2 == -1) && !(c.deposit3 == -1) && !(c.deposit4 == -1) && c.deposit5 == -1) {
				c.deposit5 = itemId;
				c.getItems().deleteItem2(itemId, 1);
				c.getItems();
				c.sendMessage("You have stored @red@1x " + ItemAssistant.getItemName(itemId) + "@bla@.");
			} else {
				c.sendMessage("Your deposit box is full!");
			}
		}
	}
}
