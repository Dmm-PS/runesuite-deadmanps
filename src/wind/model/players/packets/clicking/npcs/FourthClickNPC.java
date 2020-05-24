package wind.model.players.packets.clicking.npcs;

import wind.model.players.Client;
import wind.model.players.Rights;
import wind.util.ScriptManager;

public class FourthClickNPC {
	public static void handleClick(Client c, int npcId) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcId) {
		
		case 490:
			c.getSlayer().handleInterface("buy");
		//	c.getShops().openShop(168);
			c.sendMessage("The Slayer's Respite is actually a Slayer Helmet. I'm just lazy.");
			c.sendMessage("You currently have @red@" + c.slayerPoints
					+ " @bla@slayer points.");
			break;
		
		default:
			ScriptManager.callFunc("npcClick4_" + npcId, c, npcId);
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR))
				c.sendMessage("Fourth Click NPC : " + npcId);
			break;
		}
	}

}
