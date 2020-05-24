package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;

public class ClanPacket implements PacketType {

	@Override public void processPacket(Client c, int packetType, int packetSize) {
		String msg = c.getInStream().readString();

		c.sendMessage(msg);
	}

}
