package wind.model.players.content;

import wind.model.players.Client;
import wind.util.Misc;

public class FlowerGame {

	//public int[] flowerID = {2462, 2464, 2466, 2468, 2470};
	public int[] flowerObjectID = {2981, 2982, 2983, 2984, 2985};
	
	public void startGame(final Client c) {
		if (c.getItems().getItemAmount(995) >= c.flowerBetAmount) {
			int flower = flowerObjectID[Misc.random(4)];
			if (c.flowerGuess == flower) {
				c.getItems().addItem(995, c.flowerBetAmount*2);
				c.sendMessage("Congratulations, you've won!");
			}
			else {
				c.getItems().deleteItem(995, c.flowerBetAmount);
				c.sendMessage("Sorry, you've lost.");
			}
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			c.getPA().object(flower, c.absX, c.absY, 0, 10);
		} else {
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			c.sendMessage("Not enough coins in your inventory.");
		}
	}	
}
