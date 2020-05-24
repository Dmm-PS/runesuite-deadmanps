package wind.model.players.packets;

import com.johnmatczak.model.BankKey;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.PriceChecker;
import wind.model.players.content.skills.impl.JewelryMaking;

/**
 * Bank 5 Items
 **/
public class Bank5 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readSignedWordBigEndianA();
		int removeId = c.getInStream().readSignedWordBigEndianA();
		int removeSlot = c.getInStream().readSignedWordBigEndian();

		switch (interfaceId) {
		
		case 22204:
			BankKey.Raid.Take(c, removeId, 5);
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
		
		case 1688:
			c.sendMessage("You can't offer worn items.");
			break;
		case 3214:
			switch (removeId) {
			case 13190:
				if ((System.currentTimeMillis() - c.tabTimer) > 1000) {
					c.tabTimer = System.currentTimeMillis();
					c.getItems().deleteItem(removeId, 1);
					c.tradeBond +=1;
					c.getPA().sendFrame126(""+c.tradeBond, 6603);
					c.sendMessage("You deposit a tradeable bond into your account.");
				}
				else
					c.sendMessage("You can only do this every few seconds. Please wait.");
				break;
			case 13192:
				if ((System.currentTimeMillis() - c.tabTimer) > 1000) {
					c.tabTimer = System.currentTimeMillis();
					c.getItems().deleteItem(removeId, 1);
					c.untradeBond +=1;
					c.getPA().sendFrame126(""+c.untradeBond, 6605);
					c.sendMessage("You deposit an untradeable bond into your account.");
				} 
				else
					c.sendMessage("You can only do this every few seconds. Please wait.");
				break;
			default:
				c.sendMessage("You can only add bonds, not other items!");
				break;
			}
			break;

		case 4393:
			PriceChecker.withdrawItem(c, removeId, removeSlot, 5);
			break;

		case 3823:
			if(!c.getItems().playerHasItem(removeId))
				return;
			c.getShops().sellItem(removeId, removeSlot, 1);
			break;

		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 1);
			break;

		case 5064:
			if(!c.getItems().playerHasItem(removeId))
				return;
			if (c.isBanking)
				c.getItems().bankItem(removeId, removeSlot, 5);
			else
				PriceChecker.depositItem(c, removeId, 5);
			break;

		case 5382:
			c.getItems().fromBank(removeId, removeSlot, 5);
			break;

		case 7423:
			if (c.storing) {
				return;
			}
			c.getItems().bankItem(removeId, removeSlot, 5);
			c.getItems().resetItems(7423);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 5);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(removeId, removeSlot, 5);
			} else {
				c.getDuel().stakeItem(removeId, removeSlot, 5);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(removeId, removeSlot, 5);
			}
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 5);
			break;

		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				c.sendMessage("Unhandled Interface ID: " + interfaceId);
			break;

		}
	}

}
