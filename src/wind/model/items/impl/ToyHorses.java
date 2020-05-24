package wind.model.items.impl;

import wind.model.players.Client;
import wind.util.Misc;

public class ToyHorses {
	
	/**
	 * String contains the data for the random messages that the player
	 * says when the player clicks on a toy horse.
	 */
	
	public static String toyHorseText() {
		int random = Misc.random(4);
		switch (random) {
			case 0: return "Come on Dobbin, we can win the race!";
			case 1: return "Neaahhhyyy!";
			case 2: return "Giddy-up horsey!";
			default: return "Hi-ho Silver, and away!";
		}
	}
	
	/**
	 * This array contains the data for the toy horses
	 * contains itemID, then the animation.
	 */
	
	private static int[][] TOY_ID = {
		{2520, 918}, 
		{2522, 919},
		{2524, 920},
		{2526, 921},
	};
	
	/**
	 * This boolean contains the data so it will read the array,
	 * reads the itemID then in contains the animation start, 
	 * text, and removing windows.
	 */
	
	public static boolean handleToyHorsesActions(Client c, int itemId) {
		for (int[] data : TOY_ID) {
			if (data[0] == itemId) {
				c.startAnimation(data[1]);
				c.forcedChat(toyHorseText());
				c.getPA().removeAllWindows();
				return true;
			}
		}
		return false;
	}

}