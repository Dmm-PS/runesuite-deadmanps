package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.PlayerHandler;
import wind.util.Misc;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.inTrade) {
			if (!c.tradeConfirmed2) {
				Client o = (Client) PlayerHandler.players[c.tradeWith];
				c.getTrade().declineTrade();
				o.getTrade().declineTrade();
			}
		}
		if (c.inTrade) {
			if (!c.acceptedTrade) {
				Client o = (Client) PlayerHandler.players[c.tradeWith];
				Misc.println("trade reset");
				c.getTrade().declineTrade();
				o.getTrade().declineTrade();
			}
		}
		if (c.hasPin) {
			return;
		}		
        if (c.isBanking)
            c.isBanking = false;
        if(c.isShopping)
            c.isShopping = false;
        if(c.openDuel && c.duelStatus >= 1 && c.duelStatus <= 4) {
            Client o = (Client) PlayerHandler.players[c.duelingWith];
            if(o != null)
                o.getDuel().declineDuel(true);
            c.getDuel().declineDuel(true);
        }
        if(c.duelStatus == 6)
            c.getDuel().claimStakedItems();
        if (c.inTrade) {
            if(!c.acceptedTrade) {
                c.getTrade().declineTrade();
            }
        }
	}

}