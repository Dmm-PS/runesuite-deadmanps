package wind.model.players.content.minigames.nightmarezone;


public class Mode {

	
	private static Mode instance = new Mode();
	
	private BattleState battleState;
	
	public enum BattleState {
		/** Solo modes */ 
		PRATICE, ENDURANCE, MEDIUM, HARD,
		/** Team modes */
		TEAM_MEDIUM, TEAM_HARD;
	}


	public static Mode getInstance() {
		return instance;
	}

	public static void setInstance(Mode instance) {
		Mode.instance = instance;
	}

	public BattleState getBattleState() {
		return battleState;
	}

	public void setBattleState(BattleState battleState) {
		this.battleState = battleState;
	}
	
}