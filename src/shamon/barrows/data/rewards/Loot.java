package shamon.barrows.data.rewards;

import wind.model.players.Client;

public interface Loot {

	public int getItemID();

	public default int getAmount() {
		return 1;
	}

	public static double getChance(Client client){
	return 10;
	}
}
