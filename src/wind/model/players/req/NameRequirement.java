package wind.model.players.req;

import wind.model.players.Player;
import wind.model.players.Requirement;

public class NameRequirement implements Requirement {

	private String name; 


	public NameRequirement(String name) {
		this.name = name;
	}
	
	@Override
	public boolean met(Player player) {
		return player.playerName.equalsIgnoreCase(name);
	} 

}
