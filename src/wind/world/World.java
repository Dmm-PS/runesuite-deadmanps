package wind.world;

import wind.Constants;
import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;

public class World {
		
	public static final void announce(Client c, int id) {
	c.getItems();
	String item = ItemAssistant.getItemName(id).toString();
	for (String superRares : Constants.SUPER_RARES) {
		if (item.toLowerCase().contains(superRares)) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage("@red@"+c.playerName+" has received x1 "+item+" as a extreme rare drop!");
				}
			}
			return;
			}
		}
	for (String rares : Constants.RARES) {
		if (item.toLowerCase().contains(rares)) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage("@red@"+c.playerName+" has received x1 "+item+" as a rare drop.");
					}
			}
		}
	}
}

}
