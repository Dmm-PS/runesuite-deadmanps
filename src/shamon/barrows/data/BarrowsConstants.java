package shamon.barrows.data;

import java.util.Arrays;
import java.util.List;

import shamon.region.Position;

public class BarrowsConstants {
	/*
	 * Spade 
	 */
	public static final int SPADE_ID = 952;
	public static final int SPADE_ANIMATION_ID = 831;
	public static final int STOP_ANIMATION_ID = 65535;
	
	/*
	 * Messages
	 */
	public static final String BARROWS_DIG_MESSAGE = "You dig a hole...";
	public static final String BARROWS_CRYPT_MESSAGE = "You've broken into a crypt!";
	public static final String NOTHING_FOUND = "You don't find anything.";

	public static final String BROTHER_AWAKEN = "You dare disturb my rest!";
	public static final String BROTHER_THEFT = "You dare steal from us!";
	public static final String EMPTY_CHEST = "The chest is empty.";

	public static final String PUZZLE_PROMPT = "The door is locked with a strange puzzle.";
	public static final String PUZZLE_SUCCESS = "You hear the doors' locking mechanism grind open.";
	public static final String PUZZLE_FAILURE = "You got the puzzle wrong! You can hear the catacombs moving around you.";

	/*
	 * Mounds
	 */
	public static final Position AHRIMS_MOUND_TOP = Position.create(3565, 3289);
	public static final Position DHAROKS_MOUND_TOP = Position.create(3575, 3298);
	public static final Position GUTHANS_MOUND_TOP = Position.create(3578, 3283);
	public static final Position KARILS_MOUND_TOP = Position.create(3566, 3277);
	public static final Position TORAGS_MOUND_TOP = Position.create(3554, 3283);
	public static final Position VERACS_MOUND_TOP = Position.create(3557, 3298);

	/*
	 * Stairs
	 */
	public static final int AHRIMS_STAIRS_ID = 20667;
	public static final int DHAROKS_STAIRS_ID = 20668;
	public static final int GUTHANS_STAIRS_ID = 20669;
	public static final int KARILS_STAIRS_ID = 20670;
	public static final int TORAGS_STAIRS_ID = 20671;
	public static final int VERACS_STAIRS_ID = 20672;

	public static final Position AHRIMS_STAIRS_POSITION = Position.create(3557, 9703, 3);
	public static final Position DHAROKS_STAIRS_POSITION = Position.create(3556, 9718, 3);
	public static final Position GUTHANS_STAIRS_POSITION = Position.create(3534, 9704, 3);
	public static final Position KARILS_STAIRS_POSITION = Position.create(3546, 9684, 3);
	public static final Position TORAGS_STAIRS_POSITION = Position.create(3568, 9683, 3);
	public static final Position VERACS_STAIRS_POSITION = Position.create(3578, 9706, 3);

	/*
	 * Tomb
	 */
	public static final int AHRIMS_TOMB_ID = 20770;
	public static final int DHAROKS_TOMB_ID = 20720;
	public static final int GUTHANS_TOMB_ID = 20722;
	public static final int KARILS_TOMB_ID = 20771;
	public static final int TORAGS_TOMB_ID = 20721;
	public static final int VERACS_TOMB_ID = 20772;

	/*
	 * Tunnels 
	 */
	public static final int CLOSED_LEFT_DOOR = 20678;
	public static final int CLOSED_RIGHT_DOOR = 20697;

	/*
	 * Brothers
	 */
	public static final int NUMBER_OF_BROTHERS = 6;

	public static final int AHRIMS_ID = 1672; // 2025;
	public static final int DHAROKS_ID = 1673; // 2026;
	public static final int GUTHANS_ID = 1674; // 2027;
	public static final int KARILS_ID = 1675; // 2028;
	public static final int TORAGS_ID = 1676; // 2029;
	public static final int VERACS_ID = 1677; // 2030;

	/*
	 * Crypt monsters;
	 */
	public static final int BLOODWORM_ID = 1678;
	public static final int CRYPT_RAT_ID = 1679;
	public static final int GIANT_CRYPT_RAT_ID_1 = 1680;
	public static final int GIANT_CRYPT_RAT_ID_2 = 1681;
	public static final int GIANT_CRYPT_RAT_ID_3 = 1682;
	public static final int CRYPT_SPIDER_ID = 1683;
	public static final int GIANT_CRYPT_SPIDER_ID = 1684;
	public static final int SKELETON_ID = 1685;
	public static final int SKELETON_ID_2 = 1686;
	public static final int SKELETON_ID_3 = 1687;
	public static final int SKELETON_ID_4 = 1688;

	public static final List<Integer> DOOR_NPCS = Arrays.asList(BLOODWORM_ID, CRYPT_RAT_ID, GIANT_CRYPT_RAT_ID_1,
			GIANT_CRYPT_RAT_ID_2, GIANT_CRYPT_RAT_ID_3, SKELETON_ID, SKELETON_ID_2, SKELETON_ID_3, SKELETON_ID_4);

	public static int KILLCOUNT_WIDGET_ID = 4536;
	public static int KILLCOUNT_TEXT_WIDGET_ID = 4536;

	public static final List<Integer> CHEST_DOORS = Arrays.asList(20704, 20689, 20690, 20708, 20692, 20709, 20711,
			20685);

	public static final List<Integer> OTHER_DOORS = Arrays.asList(20705, 20706, 20707, 20710, 20712, 20713, 20681,
			20714, 20682, 20683, 20715, 20684, 20686, 20687, 20688, 20691, 20693, 20694, 20695, 20696, 20700, 20701,
			20702, 20703);
}
