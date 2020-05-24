package wind.model.players.req;

import wind.model.players.Player;
import wind.model.players.Requirement;

public class PlayerKillRequirement implements Requirement {

	private int killCount;
	
	public PlayerKillRequirement(int killCount) {
		this.killCount = killCount;
	}
	
	@Override
	public boolean met(Player player) {
		return player.killCount >= killCount;
	}

}
