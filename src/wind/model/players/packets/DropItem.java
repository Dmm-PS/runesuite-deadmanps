package wind.model.players.packets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

import org.apollo.cache.def.ItemDefinition;

import wind.Config;
import wind.Server;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.npcs.pets.PetHandler;
import wind.model.npcs.pets.PetHandler.petData;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;

/**
 * Drop Item by Ardi
 **/
public class DropItem implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {

		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();
		c.alchDelay = System.currentTimeMillis();
		if (c.arenas()) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}
		if (c.underAttackBy > 0) {
			c.sendMessage("You can't drop items during a combat.");
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}
		if (c.getRights().equal(Rights.ADMINISTRATOR)) {
			c.sendMessage("Administrator cannot drop items.");
			return;
		}

		if(!c.getItems().playerHasItem(itemId)) {
			return;
		}
		
		File d = new File("aros_data/logs/DropLog");
		if (!d.exists())
			d.mkdir();
		
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(new File("aros_data/logs/DropLog/" + c.playerName + ".txt"), true));
			
			String itemName = ItemDefinition.lookup(itemId).getName();
			if (itemName.length() > 20) // Workaround for the weird, "nulled" item names or whatever the fuck they are.
				itemName = null;
			
			out.println("--------------------------------------------------");
			out.println("Date : " + new Date());
			out.println("Position : " + c.absX + ", " + c.absY);
			out.println("Item : " + c.playerItemsN[slot] + " of item " + itemId + (itemName == null ? "" : " (" + itemName + ")"));
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (itemId >= 15000 && itemId <= 15004) { // Bank Keys
			if (c.underAttackBy > 0) {
				c.sendMessage("You may not drop bank keys whilst in combat.");
				return;
			}
			
			c.sendMessage("Your bank key and its loot disintegrates as you drop it.");
			c.getItems().deleteItem(itemId, 1);
			return;
		}
		
		for (petData i : PetHandler.petData.values()) {
			if (c.getPetSummoned()) {
				//c.sendMessage("You already have a pet following you.");
				return;
			}
			else
				if (itemId == i.getItem()) {
					c.sendMessage("You dropped your pet and it begins following you.");
					c.petID = i.getNpcID();
					c.petSummoned = true;
					c.pet = (NPC) NPCHandler.summonPet(c, i.getNpcID(), c.absX - 1, c.absY - 1, c.heightLevel);
					c.getItems().deleteItem(i.getItem(), 1);
				}
		}
		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}

		/*for (int i = 0; i < PetData.petData.length; i++) {
			if (PetData.petData[i][1] == itemId) {
				if (c.getPetSummoned()) {
					droppable = false;
					break;
				}
			}
		}
		DropPet.getInstance().dropPetRequirements(c, itemId, slot);*/

		if (c.playerItemsN[slot] != 0 && itemId != -1
				&& c.playerItems[slot] == itemId + 1) {
			// boolean destroy = true;
			for (int i : Config.DESTROY_ITEMS) {
				if (i == itemId) {
					c.droppedItem = itemId;
					c.getPA().destroyInterface(itemId);
					return;
				}
			}
			if (droppable) {
				if (c.underAttackBy > 0) {
					if (c.getShops().getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}

				Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.playerItemsN[slot], c.getId());
				c.getItems().deleteItem(itemId, c.playerItemsN[slot]);
				
				if (Config.SOUND) {
					c.getPA().sendSound(c.getSounds().DROPITEM);
				}
				wind.model.players.saving.PlayerSave.saveGame(c);
			} else {
				c.sendMessage("You can't drop this item.");
			}
		}
	}
}
