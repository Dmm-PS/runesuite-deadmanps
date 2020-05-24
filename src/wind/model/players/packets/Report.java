package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;

public class Report implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		try {
			ReportHandler.handleReport(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}