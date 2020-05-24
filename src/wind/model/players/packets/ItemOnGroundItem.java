package wind.model.players.packets;

import wind.Server;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.skills.impl.firemaking.Firemaking;
import wind.util.Misc;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		int a1 = c.getInStream().readSignedWordBigEndian();
		int itemUsed = c.getInStream().readSignedWordA();
		int groundItem = c.getInStream().readUnsignedWord();
		int gItemY = c.getInStream().readSignedWordA();
		int itemUsedSlot = c.getInStream().readSignedWordBigEndianA();
		int gItemX = c.getInStream().readUnsignedWord();
        if(!c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
            return;
        }
        if(!Server.itemHandler.itemExists(groundItem, gItemX, gItemY)) {
            return;
        }
		switch (itemUsed) {
		case 590:
			Firemaking.attemptFire(c, itemUsed, groundItem, gItemX, gItemY,
					true);
			break;

		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				Misc.println("ItemUsed " + itemUsed + " on Ground Item "
						+ groundItem);
			break;
		}
	}

}
