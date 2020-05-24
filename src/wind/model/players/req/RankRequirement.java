package wind.model.players.req;

import wind.model.players.Player;
import wind.model.players.Requirement;
import wind.model.players.Rights;

public class RankRequirement implements Requirement {
	
	private final Rights requiredRights;
	
	public RankRequirement(Rights requiredRights) {
		this.requiredRights = requiredRights;
	}

	@Override
	public boolean met(Player player) {
		return player.getRights().ordinal() >= requiredRights.ordinal();
	}

	public Rights getRequiredRights() {
		return requiredRights;
	}

}
