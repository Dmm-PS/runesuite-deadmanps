package wind.model.players;

import java.util.ArrayList;

import wind.model.players.Client;
import wind.util.Misc;
import wind.Server;

 /**
  * Flaxpicking. OUTDATED
  * @author Derive
  *
  */
public class Flax {
	
	public static ArrayList <int[]> flaxRemoved = new ArrayList<int[]>();
	
	public static void pickFlax(final Client c, final int x, final int y) {
		if (c.getItems().freeSlotId() != 0) {
            c.getItems().addItem(1779, 1);
            c.startAnimation(827);
			c.sendMessage("You pick some flax.");
			if (Misc.random(3) == 1) {
				flaxRemoved.add(new int[] { x, y });
				Server.objectManager.removeObject(x, y);
			}
		} else {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}
	
	}
}