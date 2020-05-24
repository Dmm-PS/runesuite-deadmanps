package wind.model.players.content.skills.impl.rooftopagility;

import wind.model.players.Client;
import wind.model.players.content.skills.impl.Agility;

/**
 * 
 * @author Biocide
 *
 */
public class Varrock extends Agility {
	/**
	 * 
	 * @param c - the player
	 */
	public Varrock(Client c) {
		super(c);
	}
	public static final int ROUGH_WALL = 10586;
	public static final int CLOTHES_LINE = 10587;
	public static final int GAP = 10642;
	public static final int WALL = 10777;
	public static final int GAP2 = 10779;
	public static final int GAP3 = 10780;
	public static final int LEDGE = 10781;
	public static final int EDGE = 10817;
	
	/**
	 * 
	 * @param c
	 * @param objectID
	 * USAGE: Agility.perform(c, anim, newX, newY, height, S/E, S/E, S/E);
	 * USAGE: Agility.performWalkingAnimation(c, anim, newX, newY, height, S/E, S/E, S/E);
	 */
	public static void performAction(Client c, int objectID) {
		switch(objectID) {
		case ROUGH_WALL:
			Agility.performBasic(c, 828, 3219, 3414, 3, "rough wall", "climb", "the rooftop");
			c.getPA().addSkillXP(2500, 16);
			break;
		case CLOTHES_LINE:
			if (c.absX != 3214 && c.absY != 3414 && c.heightLevel != 3)
				return;
			else
				Agility.performWalking(c, 762, -6, 0, 3, "clothes line", "cross", "next building");	
			c.getPA().addSkillXP(2500, 16);
			break;
		case GAP:
				Agility.performBasic(c, 769, 3197, 3416, 1, "gap", "jump", "next building");
				c.getPA().addSkillXP(2500, 16);
			break;
		case WALL:
			Agility.performBasic(c, 769, 3194, 3397, 3, "gap", "jump", "next building");
			c.getPA().addSkillXP(2500, 16);
			break;
		case GAP2:
			Agility.performBasic(c, 769, 3218, 3398, 3, "gap", "jump", "next building");
			c.getPA().addSkillXP(2500, 16);
		break;
		case GAP3:
			Agility.performBasic(c, 769, 3236, 3403, 3, "gap", "jump", "next building");
			c.getPA().addSkillXP(2500, 16);
		break;
		case LEDGE:
			Agility.performJump(c, 769, 3236, 3410, 3, "gap", "jump", "next building");
			c.getPA().addSkillXP(2500, 16);
		break;
		case EDGE:
			Agility.performJump(c, 769, 3236, 3417, 0, "gap", "jump", "next building");
			c.getPA().addSkillXP(2500, 16);
		break;
		}
	}
}
