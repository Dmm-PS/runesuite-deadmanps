package wind.model.players.packets;

import wind.Config;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int tradeId = c.getInStream().readSignedWordBigEndian();
		c.getPA().resetFollow();
		if (c.arenas()) {
			c.sendMessage("You can't trade inside the arena!");
			return;
		}
		if (c.inTrade)
			return;
		if (c.getRights().equal(Rights.ADMINISTRATOR) && !Config.ADMIN_CAN_TRADE) {
			c.sendMessage("Trading as an admin has been disabled.");
			return;
		}
        if(tradeId < 1)
            return;
       if (tradeId != c.playerId)
            c.getTrade().requestTrade(tradeId);
	}

}
