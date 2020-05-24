package wind.model.players;

import wind.model.players.packets.clicking.npcs.FirstClickNpc;
import wind.model.players.packets.clicking.npcs.FourthClickNPC;
import wind.model.players.packets.clicking.npcs.SecondClickNpc;
import wind.model.players.packets.clicking.npcs.ThirdClickNpc;
import wind.model.players.packets.clicking.objects.FirstClickObject;
import wind.model.players.packets.clicking.objects.SecondClickObject;
import wind.model.players.packets.clicking.objects.ThirdClickObject;

/**
 * @author 7Winds
 * @date 4.1.15
 */

public class ActionHandler {

	private Client c;

	public ActionHandler(Client Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		FirstClickObject.handleClick(c, objectType, obX, obY);		
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		SecondClickObject.handleClick(c, objectType, obX, obY);
	}
	
	public void thirdClickObject(int objectType, int obX, int obY) {
		ThirdClickObject.handleClick(c, objectType, obX, obY);
	}
	
	public void firstClickNpc(int npcType) {
		FirstClickNpc.handleClick(c, npcType);
	}
	
	public void secondClickNpc(int npcType) {
		SecondClickNpc.handleClick(c, npcType);
	}

	public void thirdClickNpc(int npcType) {
		ThirdClickNpc.handleClick(c, npcType);
	}
	public void fourthClickNpc(int npcType) {
		FourthClickNPC.handleClick(c, npcType);
	}

}