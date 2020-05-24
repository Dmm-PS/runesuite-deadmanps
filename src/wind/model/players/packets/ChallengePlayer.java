package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.PlayerHandler;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		switch (packetType) {
		case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
            if(PlayerHandler.players[answerPlayer] == null || answerPlayer == c.playerId)
                return;
			if (c.arenas() || c.duelStatus == 5) {
			
				
				c.sendMessage("You can't challenge inside the arena!");
				return;
			}

			//c.getDuel().requestDuel(answerPlayer);
			break;
		}
	}
}
