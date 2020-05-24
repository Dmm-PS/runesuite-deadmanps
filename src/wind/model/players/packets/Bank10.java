package wind.model.players.packets;

import com.johnmatczak.model.BankKey;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.PriceChecker;
import wind.model.players.content.skills.impl.JewelryMaking;

/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndian();
		int removeId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();

		switch (interfaceId) {
		
		case 22204:
			BankKey.Raid.Take(c, removeId, 10);
			break;
		case 4233:
			JewelryMaking.jewelryMaking(c, "RING", removeId, 5);
			break;
		case 4239:
			JewelryMaking.jewelryMaking(c, "NECKLACE", removeId, 5);
			break;
		case 4245:
			JewelryMaking.jewelryMaking(c, "AMULET", removeId, 5);
			break;

		case 4393:
			PriceChecker.withdrawItem(c, removeId, removeSlot, 10);
			break;

		case 1688:
			if (c.inTrade) {
				c.getTrade().declineTrade(true);
			}
			c.getPA().useOperate(removeId);
			break;

		case 5382:
			if (c.inTrade) {
				c.getTrade().declineTrade(true);
			}
			c.getItems().fromBank(removeId, removeSlot, 10);
			break;

		case 5064:
            if(!c.getItems().playerHasItem(removeId))
                return;
			if (c.inTrade) {
				c.getTrade().declineTrade(true);
			}
			if (c.isBanking)
			c.getItems().bankItem(removeId, removeSlot, 10);
			else
				PriceChecker.depositItem(c, removeId, 10);
			break;

		case 3823:
            if(!c.getItems().playerHasItem(removeId))
                return;
		c.getShops().sellItem(removeId, removeSlot, 5);
		break;	

		case 3900:
		c.getShops().buyItem(removeId, removeSlot, 5);
		break;

		case 7423:
			if (c.storing) {
				return;
			}
			c.getItems().bankItem(removeId, removeSlot, 10);
			c.getItems().resetItems(7423);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 10);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(removeId, removeSlot, 10);
			} else {
				c.getDuel().stakeItem(removeId, removeSlot, 10);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(removeId, removeSlot, 10);
			}
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 10);
			break;
			
		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				c.sendMessage("Interface ID: " + interfaceId);
			break;

		}
	}

}
