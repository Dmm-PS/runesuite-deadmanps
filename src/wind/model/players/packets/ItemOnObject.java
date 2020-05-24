package wind.model.players.packets;



/**
 * @author Ryan / Lmctruck30
 */

import wind.model.items.UseItem;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.content.CrystalChest;
import wind.model.players.content.ExchangeBoxes;
import wind.model.players.content.ExchangeBoxes.boxInformation;
import wind.model.players.content.minigames.WarriorsGuild;
import wind.model.players.content.skills.impl.Cooking;
import wind.model.players.content.skills.impl.JewelryMaking;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		/*
		 * a = ? b = ?
		 */

		@SuppressWarnings("unused")
		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readSignedWordBigEndian();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		@SuppressWarnings("unused")
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		switch (objectId) {
		case 9372:
			for (boxInformation i : ExchangeBoxes.boxInformation.values()) {
				if (itemId == i.getItemID()) {
					c.getItems().deleteItem(i.getItemID(), 1);
					c.getItems().addItem(i.getBodyID(), 1);
					c.getItems().addItem(i.getLegID(), 1);
					c.getItems().addItem(i.getGloveID(), 1);
					c.getItems().addItem(i.getHelmetID(), 1);
					c.getItems().addItem(i.getWeaponID(), 1);
					c.sendMessage("You exchange your box for the corresponding items.");
				}
			}
			break;
		case 6:
			c.getCannon().loadCannon();
		break;

		case 15621:
			WarriorsGuild.startArmourAnimation(c, itemId);
			break;

		case 172:
			CrystalChest.getSingleton().openChest(c);
			break;
		case 2097:
			c.getSmithingInt().showSmithInterface(itemId);
			break;
		case 24009:
			if (itemId == 438 ||itemId == 440 ||itemId == 444 ||itemId == 449 ||itemId == 451 ||itemId == 436) {
				c.getSmithing().sendSmelting();
			}
			if (itemId == 2357) {
				JewelryMaking.jewelryInterface(c);
			}
			break;

		/*
		 * case ###: //Glory recharging if (itemId == 1710 || itemId == 1708 ||
		 * itemId == 1706 || itemId == 1704) { int amount =
		 * (c.getItems().getItemCount(1710) + c.getItems().getItemCount(1708) +
		 * c.getItems().getItemCount(1706) + c.getItems().getItemCount(1704));
		 * int[] glories = {1710, 1708, 1706, 1704}; for (int i : glories) {
		 * c.getItems().deleteItem(i, c.getItems().getItemCount(i)); }
		 * c.startAnimation(832); c.getItems().addItem(1712, amount); } break;
		 */

		case 12269:
		case 2732:
		case 3039:
		case 114:
		case 2728:
		case 26181:
			Cooking.cookThisFood(c, itemId, objectId);
			break;
			
		//Farming
		case 8389:
		case 8132:
		case 8150:
		case 7848: ///flower patch catherby
			c.getFarming().checkItemOnObject(itemId);
		break;
		}

	}

}
