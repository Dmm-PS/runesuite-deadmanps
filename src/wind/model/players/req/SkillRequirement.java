package wind.model.players.req;

import wind.model.players.Player;
import wind.model.players.Requirement;

public final class SkillRequirement implements Requirement {
	
	private final int skillId;

	private final int requiredLevel;

	public SkillRequirement(int skillId, int requiredLevel) {
		this.skillId = skillId;
		this.requiredLevel = requiredLevel;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	@Override
	public boolean met(Player player) {
		return player.playerLevel[skillId] >= requiredLevel;
	}
	
}
