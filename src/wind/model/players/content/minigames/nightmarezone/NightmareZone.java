package wind.model.players.content.minigames.nightmarezone;

import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.content.minigames.nightmarezone.Mode.BattleState;
import wind.util.Misc;

/**
 * 
 * @author Fuzen Seth
 * @info Represents the Nightmare zone minigame.
 * @since 27.7.2014
 */
public class NightmareZone {

	private static final NightmareZone instance = new NightmareZone();
	/**
	 * The NPC ids used in nightmare zone.
	 */
	private static final int HELL_HOUND = 49, BLACK_DRAGON = 54, BLUE_DRAGON = 55,
			LESSER_DEMON = 82, GREATER_DEMON = 93, BLACK_DEMON = 84, VAMPIRE = 1225, FLIGHT_KILISA = 6227,
			TREE_SPIRIT = 443, JUNGLE_DEMON = 1472;
			
	/**
	 * The waves inside nightmare zone.
	 */
	private final int[][] MEDIUM_WAVES = { {HELL_HOUND, HELL_HOUND, LESSER_DEMON}, {VAMPIRE, LESSER_DEMON, GREATER_DEMON},
	{BLACK_DEMON, FLIGHT_KILISA, HELL_HOUND}, {LESSER_DEMON, GREATER_DEMON, BLACK_DEMON}, {BLUE_DRAGON, TREE_SPIRIT},
	{VAMPIRE, LESSER_DEMON, TREE_SPIRIT}, {BLACK_DRAGON, BLUE_DRAGON, VAMPIRE}, {FLIGHT_KILISA, GREATER_DEMON, HELL_HOUND},
	{HELL_HOUND, HELL_HOUND, TREE_SPIRIT, VAMPIRE}, {FLIGHT_KILISA, FLIGHT_KILISA, LESSER_DEMON}};
	
	@SuppressWarnings("unused")
	private final int[][] PRATICE_WAVES = {{VAMPIRE}, {TREE_SPIRIT}, {GREATER_DEMON}, {BLACK_DEMON}};
	
	private int[][] coordinates = { {2272, 4695}, { 2282,4695}};
	
	private int matchPoints = 0;
	public void displayPoints(Client c) {
		c.getPlayerAssistant().closeAllWindows();
		c.getPA().cancelTeleportTask();
		c.getDH().sendStatement("You currently have "+c.nmzPoints+" points in total.");
	}
	
	public void selectType(Client c) {
		c.getDH().sendOption4CustomTitle("Select an option", "Pratice", "Endurance", "Rumble", "I don't want to pick any.");
		c.dialogueAction = 4584;
	}
	
	public void updateOverlay(Client c) {
//		if (Mode.getInstance().getBattleState() != BattleState.HARD)
//			c.getPA().sendString("NMZ - Hard Mode", 4969);
//		else if (Mode.getInstance().getBattleState() != BattleState.MEDIUM)
//			c.getPA().sendString("NMZ - Medium Mode", 4969);
//		else if (Mode.getInstance().getBattleState() != BattleState.ENDURANCE)
//			c.getPA().sendString("NMZ - Endurance Mode", 4969);
//		else if (Mode.getInstance().getBattleState() != BattleState.PRATICE)
//			c.getPA().sendString("NMZ - Pratice Mode", 4969);
//		c.getPA().sendString("Total points:", 4963);
//		c.getPA().sendString(""+c.nmzPoints, 4964);
//		c.getPA().sendString("Points:", 4965);
//		c.getPA().sendString(""+matchPoints, 4966);
//		c.getPA().sendString("Waves:", 4967);
//		c.getPA().sendString(""+c.wavesDone, 4968);
		c.getPA().sendFrame126("Points:", 923);
		c.getPA().sendFrame126(""+matchPoints, 922);
		c.getPA().walkableInterface(920);
	}
	
	public void closeOverlay(Client c) {
		c.getPA().walkableInterface(-1);
	}
	
	public void play(Client c, BattleState battleStates) {
		c.getPlayerAssistant().closeAllWindows();
		c.getPA().cancelTeleportTask();
		c.getPA().movePlayer(2271,4682, c.playerId * 4);
		updateOverlay(c);
		c.nightmareWave = (int) Misc.randomDouble(0, 9);
		c.wavesDone = 0;
		c.nzKilled = -1;
		c.nzStage = (int) Misc.randomDouble(0, 9);
		playNextWave(c);
	}
	/**
	 * Plays the next wave.
	 * @param c
	 */
	@SuppressWarnings("unused")
	public void playNextWave(Client c) {
		if (Mode.getInstance().getBattleState() != BattleState.MEDIUM) {
		if (c != null) {
			if (c.nightmareWave >= MEDIUM_WAVES.length) {
				c.nightmareWave = (int) Misc.randomDouble(0, 9);
				return;
			}
			if (c.nightmareWave < 0) 
				return;
			int currentWave = c.nightmareWave = (int) Misc.randomDouble(0, 9);
			int npcAmount = MEDIUM_WAVES[c.nightmareWave].length;
			for (int j = 0; j < npcAmount; j++) {
				int npc = MEDIUM_WAVES[c.nightmareWave][j];
				int X = coordinates[1][0];
				int Y = coordinates[1][1];
				int H = c.heightLevel;
				int walkType = walkTypes(npc);
				int hp = getHp(npc);
				int max = getMax(npc);
				int atk = getAtk(npc);
				int def = getDef(npc);
				NPCHandler.spawnNpc(c, npc, X, Y, H, walkType, hp, max, atk,
						def, true, false);
				
			}
			c.nzStage = npcAmount;
			c.wavesDone++;
			c.nzKilled = 0;
			updateOverlay(c);
		}
		}
	}
	
	public int walkTypes(int id) {		
		return 0; //Default type
	}
	
	/**
	 * Leaves from the minigame.
	 * @param c
	 */
	public void stop(Client c) {
		closeOverlay(c);
		c.sendMessage("You have left from the Nightmare zone.");
	}
	
	public int getHp(int npc) {
		switch (npc) {
		case 2627:
			return 10;
		case 2630:
			return 20;
		case 2631:
			return 40;
		case 2741:
			return 80;
		case 2743:
			return 150;
		case 2745:
			return 250;
		}
		return 100;
	}

	public int getMax(int npc) {
		switch (npc) {
		case JUNGLE_DEMON:			
			return 30;
		
		case HELL_HOUND:
			return 13;
			
		case BLACK_DRAGON:
			return 21;
		
		case BLUE_DRAGON:
			return 30;
		
		case LESSER_DEMON:
			return 8; 
		
		case BLACK_DEMON:			
			return 18;
			
		case 449: //Tree Spirit
			return 10;
			
		case VAMPIRE: //Vampire
			return 9;
		
		case 2627:
			return 4;
		case 2630:
			return 7;
		case 2631:
			return 13;
		case 2741:
			return 28;
		case 2743:
			return 54;
		case 2745:
			return 97;
			
		case FLIGHT_KILISA: //Kilisa
			return 37;
		}
		return 10;
	}

	public int getAtk(int npc) {
		switch (npc) {
		
		case HELL_HOUND:			
			return 130;
			
		case BLACK_DRAGON:			
			return 500;
			
		case BLUE_DRAGON:
			return 300;
			
		case LESSER_DEMON:
			return 100;
			
		case GREATER_DEMON :
			return 150;
			
		case BLACK_DEMON:			
			return 180;
			
		case JUNGLE_DEMON:
			return 300;
			
		case FLIGHT_KILISA:
			return 500;
		
		case 2627:
			return 30;
		case 2630:
			return 50;
		case 2631:
			return 100;
		case 2741:
			return 150;
		case 2743:
			return 450;
		case 2745:
			return 650;
		}
		return 100;
	}

	public int getDef(int npc) {
		switch (npc) {
		case 2627:
			return 30;
		case 2630:
			return 50;
		case 2631:
			return 100;
		case 2741:
			return 150;
		case 2743:
			return 300;
		case 2745:
			return 500;
		}
		return 100;
	}

	public static NightmareZone getInstance() {
		return instance;
	}
}
