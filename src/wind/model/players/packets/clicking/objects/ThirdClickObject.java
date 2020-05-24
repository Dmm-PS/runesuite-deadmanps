package wind.model.players.packets.clicking.objects;

import wind.model.players.Client;
import wind.util.ScriptManager;

public class ThirdClickObject {

	public static void handleClick(Client c, int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		// c.sendMessage("Object type: " + objectType);
		switch (objectType) {
		case 2884:
		c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel - 1);
			break;
		default:
			ScriptManager.callFunc("objectClick3_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}
	
}
