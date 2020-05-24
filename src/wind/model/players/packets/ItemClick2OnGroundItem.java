package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.content.skills.impl.firemaking.Firemaking;
import wind.model.players.content.skills.impl.woodcutting.LogData.logData;

public class ItemClick2OnGroundItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int itemX = c.getInStream().readSignedWordBigEndian();
		final int itemY = c.getInStream().readSignedWordBigEndianA();
		final int itemId = c.getInStream().readUnsignedWordA();
		System.out.println("ItemClick2OnGroundItem - " + c.playerName + " - "
				+ itemId + " - " + itemX + " - " + itemY);
		for (logData l : logData.values()) {
			if (itemId == l.getLogId()) {
				Firemaking.attemptFire(c, 590, itemId, itemX, itemY, true);
			}
		}
	}
}