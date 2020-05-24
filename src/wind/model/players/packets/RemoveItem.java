package wind.model.players.packets;

import com.johnmatczak.model.BankKey;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.content.PriceChecker;
import wind.model.players.content.skills.impl.JewelryMaking;

/**
 * Remove Item
 **/
public class RemoveItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();
		int removeId = c.getInStream().readUnsignedWordA();
		@SuppressWarnings("unused")
		int shop = 0;
		@SuppressWarnings("unused")
		int value = 0;
		@SuppressWarnings("unused")
		String name = "null";
		System.out.println("Interface: " + interfaceId);
		/*
		 * if(!c.getItems().playerHasItem(removeId, 1)) { return; }
		 */
		switch (interfaceId) {

		case 22204:
			BankKey.Raid.Take(c, removeId, 1);
			break;

		case 4393:
			PriceChecker.withdrawItem(c, removeId, removeSlot,
					c.priceN[removeSlot]);
			break;
		case 4233:
			JewelryMaking.jewelryMaking(c, "RING", removeId, 1);
			break;
		case 4239:
			JewelryMaking.jewelryMaking(c, "NECKLACE", removeId, 1);
			break;
		case 4245:
			JewelryMaking.jewelryMaking(c, "AMULET", removeId, 1);
			break;

		case 1688:
			c.getItems().removeItem(removeId, removeSlot);
			break;

		case 5064:
			if (c.isChecking) {
				PriceChecker.depositItem(c, removeId, 1);
				return;
			} else {
				c.getItems().bankItem(removeId, removeSlot, 1);
			}
			break;

		case 5382:
			c.getItems().fromBank(removeId, removeSlot, 1);
			break;

		case 7423:
			if (c.inTrade) {
				c.getTrade().declineTrade(true);
				return;
			}
			c.getItems().bankItem(removeId, removeSlot, 1);
			c.getItems().resetItems(7423);
			break;

		case 3900:
			c.getShops().buyFromShopPrice(removeId, removeSlot);
			break;

		case 3823:
			c.getShops().sellToShopPrice(removeId, removeSlot);
			break;

		case 3322:
			if (!c.inTrade) {
				return;
			}
			if (!c.canOffer) {
				return;
			}
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(removeId, removeSlot, 1);
			} else {
				c.getDuel().stakeItem(removeId, removeSlot, 1);
			}
			break;

		case 3415:
			if (!c.canOffer) {
				return;
			}
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(removeId, removeSlot, 1);
			}
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 1);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 1);
			break;

		}
	}

}
