package wind.model.players.packets;

import wind.Server;
import wind.model.players.Client;
import wind.model.players.PacketType;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.clearLists();
		Server.objectManager.loadObjects(c);
	}

}