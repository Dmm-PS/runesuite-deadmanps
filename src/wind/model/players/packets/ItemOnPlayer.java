package wind.model.players.packets;

import wind.model.items.impl.ChristmasCracker;
import wind.model.players.Client;
import wind.model.players.PacketType;

/**
 * @author JaydenD12/Jaydennn
 */

public class ItemOnPlayer implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int playerId = c.inStream.readUnsignedWord();
		int itemId = c.playerItems[c.inStream.readSignedWordBigEndian()] - 1;

		switch (itemId) {
		case 962:
			ChristmasCracker.handleCrackers(c, itemId, playerId);
			break;
		default:
			c.sendMessage("Nothing interesting happens.");
			break;
		}

	}

}