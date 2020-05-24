package wind.model.players.packets;

import wind.Server;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.content.music.Music;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		Server.objectHandler.updateObjects(c);
		Server.itemHandler.reloadItems(c);
		c.clearLists();
		Server.objectManager.loadObjects(c);
		c.getPA().castleWarsObjects();
		Music.playMusic(c);
		c.saveFile = true;
		if (c.skullTimer > 0 && c.headIconPk < 1) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}

	}

}
