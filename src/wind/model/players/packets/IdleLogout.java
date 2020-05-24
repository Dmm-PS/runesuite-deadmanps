package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;

public class IdleLogout implements PacketType {
	
	@SuppressWarnings("unused")
	private static final int[] IDLE_EMOTES = {2756, 2761, 2763, 2764};

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		
        if(c.underAttackBy > 0 || c.underAttackBy2 > 0)
            return;       
        //c.startAnimation(IDLE_EMOTES[Misc.random(IDLE_EMOTES.length - 1)]);
	}
}