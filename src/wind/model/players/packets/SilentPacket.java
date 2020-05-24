package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;

/**
 * Slient Packet
 **/
public class SilentPacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {

	}
}
