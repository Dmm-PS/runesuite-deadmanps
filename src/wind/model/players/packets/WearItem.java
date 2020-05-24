package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		c.interfaceId = c.getInStream().readUnsignedWordA();
		c.alchDelay = System.currentTimeMillis();
        if(!c.getItems().playerHasItem(c.wearId, 1, c.wearSlot)) {
            return;
        }
		@SuppressWarnings("unused")
		int oldCombatTimer = c.attackTimer;
		if ((c.playerIndex > 0 || c.npcIndex > 0) && c.wearId != 4153)
			c.getCombat().resetPlayerAttack();
		if (c.canChangeAppearance) {
			c.sendMessage("You can't wear an item while changing appearence.");
			return;
		}
		/*
		 * if (c.wearId == 15098) { if (System.currentTimeMillis() - c.diceDelay
		 * >= 10000) { /*for (int index = 0; index < Config.MAX_PLAYERS;
		 * index++) { Client c2 = (Client) PlayerHandler.players[index]; if (c2
		 * != null) {
		 */
		// if (Clan.activeMembers.contains(c2.playerName)) {
		// Clan.sendMessage("Friends chat channel-mate @red@"+
		// Misc.ucFirst(c.playerName) + "@bla@ rolled @red@"+ Misc.random(100)
		// +"@bla@ on the percentile dice.");
		// c.diceDelay = System.currentTimeMillis();
		// }
		// }
		// }
		/*
		 * } else {
		 * c.sendMessage("You must wait 10 seconds to roll dice again."); }
		 * return; } if (c.wearId == 15088) { if (System.currentTimeMillis() -
		 * c.diceDelay >= 5000) { /*for (int index = 0; index <
		 * Config.MAX_PLAYERS; index++) { Client c2 = (Client)
		 * PlayerHandler.players[index]; if (c2 != null) {
		 */
		// if (Clan.activeMembers.contains(p.playerName)) {
		// Clan.sendMessage("Friends chat channel-mate @red@"+
		// Misc.ucFirst(c.playerName) + "@bla@ rolled @red@"+ Misc.random(12)
		// +"@bla@ on two six-sided dice.");
		// c.diceDelay = System.currentTimeMillis();
		// }
		// }
		// }
		/*
		 * } else {
		 * c.sendMessage("You must wait 5 seconds to roll dice again."); }
		 * return; }
		 */
		if (c.wearId >= 5509 && c.wearId <= 5515) {
			int pouch = -1;
			int a = c.wearId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().emptyPouch(pouch);
			return;
		}
		// c.attackTimer = oldCombatTimer;
		c.getItems().wearItem(c.wearId, c.wearSlot);
	}

}
