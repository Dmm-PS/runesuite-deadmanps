package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.DiceHandler;
import wind.model.players.content.Teles;
import wind.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (itemId >= DiceHandler.DICE_BAG && itemId <= 15100) {
			DiceHandler.putupDice(c, itemId);
		}
		switch (itemId) {
		
		case 11238:
		case 11256:
			c.getHunter().Lootimpjar(c, itemId, c.playerId);
			break;
		
		case 12926:
			int dart = 11230;
			int amount = c.dartsLoaded;
			if (amount >= 1) {
				c.getItems().addItem(dart, amount);
				c.sendMessage("@red@You receive " + c.dartsLoaded + " darts from your blowpipe.");
				c.dartsLoaded -= amount;
			} else
				c.sendMessage("You don't have any darts in your blowpipe.");
			return;
			
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			c.itemUsing = itemId;
			Teles.ROD(c);
			//c.getPA().startTeleport(3362, 3263, 0, "modern");
			break;
		case 3853:
		case 3855:
		case 3857:
		case 3859:
		case 3861:
		case 3863:
		case 3865:
		case 3867:
			c.itemUsing = itemId;
			Teles.GN(c);
		break;

		case 1712:
			c.getPA().handleGlory(itemId);
			c.isOperate = true;
			c.itemUsing = itemId;
			break;

		case 1710:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		case 1708:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		case 1706:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;
			
		case 5733:
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
			c.getPA().showInterface(22050);
			} else {
				c.sendMessage("You are not allowed to use this.");
				return;
			}
			break;

		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				Misc.println(c.playerName + " - Item3rdOption: " + itemId
						+ " : " + itemId11 + " : " + itemId1);
			break;
		}
	}

}
