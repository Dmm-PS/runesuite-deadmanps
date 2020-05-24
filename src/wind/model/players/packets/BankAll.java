package wind.model.players.packets;

import com.johnmatczak.model.BankKey;

import wind.model.items.GameItem;
import wind.model.items.Item;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.PriceChecker;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int removeSlot = c.getInStream().readUnsignedWordA();
		int interfaceId = c.getInStream().readUnsignedWord();
		int removeId = c.getInStream().readUnsignedWordA();

		switch (interfaceId) {

		case 22204:
			BankKey.Raid.TakeAll(c);
			break;
		
		case 4393:
			PriceChecker.withdrawItem(c, removeId, removeSlot,
					c.priceN[removeSlot]);
			break;

		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			if (!c.getItems().playerHasItem(removeId))
				return;
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (!c.getItems().playerHasItem(removeId))
				return;
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if (c.isBanking) {
				if (Item.itemStackable[removeId]) {
					c.getItems().bankItem(c.playerItems[removeSlot],
							removeSlot, c.playerItemsN[removeSlot]);
				} else {
					c.getItems().bankItem(c.playerItems[removeSlot],
							removeSlot,
							c.getItems().itemAmount(c.playerItems[removeSlot]));
				}
			} else {
				if (Item.itemStackable[removeId] && !c.isBanking) {
					PriceChecker.depositItem(c, removeId, c.getItems()
							.itemAmount(c.playerItems[removeSlot]));
				} else {
					PriceChecker.depositItem(c, removeId, c.getItems()
							.itemAmount(c.playerItems[removeSlot]));
				}
			}
			break;

		case 5382:
			c.getItems().fromBank(c.bankItems[removeSlot], removeSlot,
					c.bankItemsN[removeSlot]);
			break;

		case 7423:
			if (c.storing) {

				return;
			}
			c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
					c.playerItemsN[removeSlot]);
			c.getItems().resetItems(7423);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				if (Item.itemStackable[removeId]) {
					c.getTrade().tradeItem(removeId, removeSlot,
							c.playerItemsN[removeSlot]);
				} else {
					c.getTrade().tradeItem(removeId, removeSlot, 28);
				}
			} else {
				if (Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
					c.getDuel().stakeItem(removeId, removeSlot,
							c.playerItemsN[removeSlot]);
				} else {
					c.getDuel().stakeItem(removeId, removeSlot, 28);
				}
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				if (Item.itemStackable[removeId]) {
					for (GameItem item : c.getTrade().offeredItems) {
						if (item.id == removeId) {
							c.getTrade().fromTrade(
									removeId,
									removeSlot,
									c.getTrade().offeredItems
											.get(removeSlot).amount);
						}
					}
				} else {
					for (GameItem item : c.getTrade().offeredItems) {
						if (item.id == removeId) {
							c.getTrade().fromTrade(removeId, removeSlot,
									28);
						}
					}
				}
			}
			break;

		case 7295:
			if (Item.itemStackable[removeId]) {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.playerItemsN[removeSlot]);
				c.getItems().resetItems(7423);
			} else {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.getItems().itemAmount(c.playerItems[removeSlot]));
				c.getItems().resetItems(7423);
			}
			break;

		case 6669:
			if (Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
				for (GameItem item : c.getDuel().stakedItems) {
					if (item.id == removeId) {
						c.getDuel()
								.fromDuel(
										removeId,
										removeSlot,
										c.getDuel().stakedItems
												.get(removeSlot).amount);
					}
				}

			} else {
				c.getDuel().fromDuel(removeId, removeSlot, 28);
			}
			break;

		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				c.sendMessage("Interface ID: " + interfaceId);
			break;
		}
	}

}
