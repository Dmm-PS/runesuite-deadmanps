package wind;

import wind.util.Misc;

/**
 * The Configuration File Of The Server
 * 
 * @author Sanity Revision by Shawn Notes by Shawn & Sunny++
 */
public class Config {
	
	/**
	 * The amount of time in minutes to automatically save players' states.
	 */
	public static final int AUTOSAVE_TIME = 5;
	
	/**
	 * The amount of players required to be online in order to trigger an
	 * auto save.
	 */
	public static final int AUTOSAVE_MINIMUM_ONLINE = 1;
	
	

	public static boolean SERVER_DEBUG = false;

	/*
	 * Ability to switch ports false = 43594, true = 5555
	 */
	public static final boolean PORT_SWITCH = false;

	public static final boolean CONTROL_PANEL = true;

	public static String SERVER_NAME = "Deadman mode, in this world you die";
	
	public static final int SERVER_PORT = 43594;
	
	public static final String DATA_LOC = "./aros_data/";

	//public static final String DATA_LOC = System.getProperty("user.home") + "./Project Wind/Project Wind Data/";
	
	public static String SYSTEM_NAME = "["+ Misc.getDate() + "] ";

	public static final String WELCOME_MESSAGE = "Welcome to deadman mode, in this world you die.";
	

	public static final String FORUMS = "http://www.deadmanps.net/forums";

	/**
	 * The client version you are using. Not necessary needed.
	 */
	public static final int CLIENT_VERSION = 317;

	/**
	 * The delay it takes to type and or send a message.
	 */
	public static int MESSAGE_DELAY = 6000;

	/**
	 * The highest amount ID. Change is not needed here unless loading items
	 * higher than the 667 revision.
	 */
	public static final int ITEM_LIMIT = 2000000;

	/**
	 * An integer needed for the above code.
	 */
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	/**
	 * The size of a players bank.
	 */
	public static final int BANK_SIZE = 352;

	/**
	 * The max amount of players until your server is full.
	 */
	public static final int MAX_PLAYERS = 1000;

	/**
	 * The delay of logging in from connections. Do not change this.
	 */
	public static final int CONNECTION_DELAY = 400;

	/**
	 * How many IP addresses can connect from the same network until the server
	 * rejects another.
	 */
	public static final int IPS_ALLOWED = 10000;

	/**
	 * Change to true if you want to stop the world --8. Can cause screen
	 * freezes on SilabSoft Clients. Change is not needed.
	 */
	public static final boolean WORLD_LIST_FIX = false;

	public static final int[] ITEM_RARE = {4151, 11732, 6737, 11802, 11692, 11690, 11804, 11688, 6585, 4700, 4702, 4704, 4706, 4708, 4710, 4712,
        4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4736, 4738, 4740, 4742, 4744, 11286, 11724,
		   11726, 11718, 11720, 11722, 11995, 11994, 15019, 15023, 15020, 15021, 15022, 12009, 12011, 11989, 11990,
		   11999, 11988, 11987, 11986, 12000, 15030, 13263, 10556, 10557, 10558, 10559};
	
	/**
	 * Items that can not be sold in any stores.
	 */
	public static final int[] ITEM_SELLABLE = { 1050, 1051, 1044, 1045, 1046,
			1047, 1048, 1049, 1052, 11704, 11705, 11696, 11697, 11724, 11725,
			11694, 11726, 11727, 11728, 11729, 3842, 3844, 3840, 8844, 8845,
			8846, 8847, 8848, 8849, 8850, 10551, 10548, 6570, 7462, 7461, 7460,
			7459, 7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757,
			9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775,
			9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761,
			9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773,
			9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762,
			9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777,
			9786, 9810, 9765, 8839, 8840, 8842, 11663, 11664, 11665, 10499,
			995, 7449 };

	/**
	 * Items that can not be traded or staked.
	 */
	public static final int[] ITEM_TRADEABLE = { 3842, 3844, 3840, 8844, 8845,
			8846, 8847, 8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459,
			7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760,
			9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772,
			9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764,
			9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779,
			9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
			9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786,
			9810, 9765, 8839, 8840, 8842, 10548, 10551, 11663, 11664, 11665, 10499, 7449, 11864,
			12954, 11852, 11854, 11850, 11856, 11858, 11860, 13120, 13124, 13103, 13132, 12921, 
			12935, 13140, 6529, 2415, 2416, 2417, 13107, 2412, 2413, 2414, 1052, 3839, 3841, 3843,
			2714, 2802, 2775 };
	
	/**
	 * Notables (hardcoded)
	 */
	public static final int[] ITEM_NOTABLE = { 12923 };
	/**
	 * Items that can not be dropped.
	 */
	public static final int[] UNDROPPABLE_ITEMS = {7449};
	
	/*
	 * Items that will display the destory item interface.
	 */
	public static final int[] DESTROY_ITEMS	= { 3842, 3844, 3840, 8844, 8845,
		8846, 8847, 8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459,
		7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760,
		9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772,
		9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764,
		9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779,
		9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
		9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786,
		9810, 9765, 8839, 8840, 8842, 11663, 11664, 11665, 10499, 7449 };

	/**
	 * Items that are listed as fun weapons for dueling.
	 */
	public static final int[] FUN_WEAPONS = { 2460, 2461, 2462, 2463, 2464,
			2465, 2466, 2467, 2468, 2469, 2470, 2471, 2471, 2473, 2474, 2475,
			2476, 2477 };

	/**
	 * If administrators can trade or not.
	 */
	public static boolean ADMIN_CAN_TRADE = true;

	/**
	 * If administrators can sell items or not.
	 */
	public static boolean ADMIN_CAN_SELL_ITEMS = false;

	/**
	 * If administrators can drop items or not.
	 */
	public static boolean ADMIN_DROP_ITEMS = true;

	public static final int HOME_X = 3224;
	public static final int HOME_Y = 3218;

	public static final int POH_X = 1899;
	public static final int POH_Y = 5099;
	
	/**
	 * Nightmarezone
	 */	
	public static final int NMZ_X = 2607;
	public static final int NMZ_Y = 3115;
	
	/**
	 * Bounty Hunter
	 */
	public static final int BH_X = 3179;
	public static final int BH_Y = 3684;

	/**
	 * Warriors Guild
	 */
	public static final int WG_X = 2869;
	public static final int WG_Y = 3546;
	
	/**
	 * Barrows
	 */
	public static final int BARROWS_X = 3565;
	public static final int BARROWS_Y = 3314;

	/**
	 * Fight Pits
	 */	
	public static final int FIGHTPITS_X = 2399;
	public static final int FIGHTPITS_Y = 5177;
	
	/**
	 * Castle Wars
	 */
	public static final int CASTLEWARS_X = 2440;
	public static final int CASTLEWARS_Y = 3088;

	/**
	 * Fight Pits/Fight Caves
	 */
	public static final int TZHAAR_X = 2444;
	public static final int TZHAAR_Y = 5170;
	
	/** 
	 * Pest Control
	 */
	public static final int PC_X = 2662;
	public static final int PC_Y = 2650;
	
	/**
	 * Duel Arena
	 */
	public static final int DUEL_X = 3365;
	public static final int DUEL_Y = 3265;
	
	/**
	 * Rouges Den
	 */
	public static final int RD_X = 3047;
	public static final int RD_Y = 4975;

	public static final int FISHING_X = 2604;
	public static final int FISHING_Y = 3414;

	public static final int MINING_X = 3046;
	public static final int MINING_Y = 9751;

	public static final int CRABS_X = 2679;
	public static final int CRABS_Y = 3718;

	public static final int EDGE_X = 3087;
	public static final int EDGE_Y = 3499;

	public static final int DICING_X = 2605;
	public static final int DICING_Y = 3093;

	public static final int STAFFZONE_X = 2912;
	public static final int STAFFZONE_Y = 5475;
	
	//	public static final int GAMESNEC_X = 2898;
	//	public static final int GAMESNEC_Y = 3553;

	/**
	 * The starting location of your server.
	 */
	public static final int START_LOCATION_X = 3224;
	public static final int START_LOCATION_Y = 3218;
	/**
	 * The re-spawn point of when someone dies.
	 */
	public static final int RESPAWN_X = 3224;
	public static final int RESPAWN_Y = 3218;

	/**
	 * The re-spawn point of when a duel ends.
	 */
	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;

	/**
	 * The point in where you spawn in a duel. Do not change this.
	 */
	public static final int RANDOM_DUELING_RESPAWN = 0;

	/**
	 * The level in which you can not teleport in the wild, and higher.
	 */
	public static final int NO_TELEPORT_WILD_LEVEL = 70;

	/**
	 * The timer in which you are skulled goes away. Seconds x2 Ex. 60x2=120
	 * Skull timer would be 1 minute.
	 */
	public static final int SKULL_TIMER = 2400;

	/**
	 * How long the teleport block effect takes.
	 */
	public static final int TELEBLOCK_DELAY = 20000;

	/**
	 * Single and multi player killing zones.
	 */
	public static final boolean SINGLE_AND_MULTI_ZONES = true;

	/**
	 * Wilderness levels and combat level differences. Used when attacking
	 * players.
	 */
	public static final boolean COMBAT_LEVEL_DIFFERENCE = false;

	/**
	 * Combat level requirements needed to wield items.
	 */
	public static final boolean itemRequirements = true;

	/**
	 * Combat experience rates.
	 */
	public static final int MELEE_EXP_RATE = 150;
	public static final int RANGE_EXP_RATE = 150;
	public static final int MAGIC_EXP_RATE = 150;

	/**
	 * Special server experience bonus rates. (Double experience weekend etc)
	 */
	public static final int SERVER_EXP_BONUS = 1;

	/**
	 * How fast the special attack bar refills.
	 */
	public static final int INCREASE_SPECIAL_AMOUNT = 17500; // 17.5 seconds

	/**
	 * If you need more than one prayer point to use prayer.
	 */
	public static final boolean PRAYER_POINTS_REQUIRED = true;

	/**
	 * If you need a certain prayer level to use a certain prayer.
	 */
	public static final boolean PRAYER_LEVEL_REQUIRED = true;

	/**
	 * If you need a certain magic level to use a certain spell.
	 */
	public static final boolean MAGIC_LEVEL_REQUIRED = true;

	/**
	 * How long the god charge spell lasts.
	 */
	public static final int GOD_SPELL_CHARGE = 300000;

	/**
	 * If you need runes to use magic spells.
	 */
	public static final boolean RUNES_REQUIRED = true;

	/**
	 * If you need correct arrows to use with bows.
	 */
	public static final boolean CORRECT_ARROWS = true;

	/**
	 * If the crystal bow degrades.
	 */
	public static final boolean CRYSTAL_BOW_DEGRADES = true;

	/**
	 * How often the server saves data.
	 */
	public static final int SAVE_TIMER = 30; // Saves every one minute.

	/**
	 * How far NPCs can walk.
	 */
	public static final int NPC_RANDOM_WALK_DISTANCE = 5; // 5x5 square, NPCs
															// would be able to
															// walk 25 squares
															// around.

	/**
	 * How far NPCs can follow you when attacked.
	 */
	public static final int NPC_FOLLOW_DISTANCE = 10; // 10 squares

	/**
	 * NPCs that act as if they are dead. (For salve amulet, etc)
	 */
	public static final int[] UNDEAD_NPCS = { 90, 91, 92, 93, 94, 103, 104, 73,
			74, 75, 76, 77 };

	/**
	 * Glory locations.
	 */
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3500;
	public static final String EDGEVILLE = "";
	public static final int AL_KHARID_X = 3293;
	public static final int AL_KHARID_Y = 3174;
	public static final String AL_KHARID = "";
	public static final int KARAMJA_X = 2913;
	public static final int KARAMJA_Y = 3169;
	public static final String KARAMJA = "";
	public static final int DRAYNOR_X = 3106;
	public static final int DRAYNOR_Y = 3251;
	public static final String DRAYNOR = "";

	/**
	 * Teleport spells.
	 **/
	public static final int EDGEVILLE_PK_X = 3102;
	public static final int EDGEVILLE_PK_Y = 3517;

	public static final int CASTLE_PK_X = 3029;
	public static final int CASTLE_PK_Y = 3632;

	public static final int TRAWLER_X = 2662;
	public static final int TRAWLER_Y = 3161;
	/*
	 * Modern spells
	 */
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";

	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";

	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";

	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";

	public static final int CANFIS_X = 3687;
	public static final int CANFIS_Y = 3502;

	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";

	public static final int WATCHTOWER_X = 2547;
	public static final int WATCHTOWER_Y = 3112;
	public static final String WATCHTOWER = "";

	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";

	/*
	 * Ancient spells
	 */
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;

	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;

	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;

	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;

	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;

	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;

	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;

	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;

	public static final int MISCELLANIA_X = 3012;
	public static final int MISCELLANIA_Y = 3363;

	/**
	 * Timeout time.
	 */
	public static final int TIMEOUT = 20;

	/**
	 * Cycle time.
	 */
	public static final int CYCLE_TIME = 600;

	/**
	 * Buffer size.
	 */
	public static final int BUFFER_SIZE = 512;

	/**
	 * Slayer Variables.
	 */
	public static final int[][] SLAYER_TASKS = { { 1, 87, 90, 4, 5 }, // Low
																		// tasks
			{ 6, 7, 8, 9, 10 }, // Medium tasks
			{ 11, 12, 13, 14, 15 }, // High tasks
			{ 1, 1, 15, 20, 25 }, // Low requirements
			{ 30, 35, 40, 45, 50 }, // Medium requirements
			{ 60, 75, 80, 85, 90 } }; // High requirements

	
	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 16;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	
	/**
	 * Skill experience multipliers.
	 */
	public static final int WOODCUTTING_EXPERIENCE = 40;
	public static final int MINING_EXPERIENCE = 50;
	public static final int SMITHING_EXPERIENCE = 40;
	public static final int FARMING_EXPERIENCE = 40;
	public static final int FIREMAKING_EXPERIENCE = 40;
	public static final int HERBLORE_EXPERIENCE = 40;
	public static final int FISHING_EXPERIENCE = 40;
	public static final int AGILITY_EXPERIENCE = 40;
	public static final int PRAYER_EXPERIENCE = 40;
	public static final int RUNECRAFTING_EXPERIENCE = 60;
	public static final int CRAFTING_EXPERIENCE = 40;
	public static final int THIEVING_EXPERIENCE = 40;
	public static final int SLAYER_EXPERIENCE = 40;
	public static final int COOKING_EXPERIENCE = 40;
	public static final int FLETCHING_EXPERIENCE = 40;

	public static final boolean SOUND = true; // TODO

	public static final boolean WEEKEND_DOUBLE_EXP = true;

	public static final int DROP_RATE = 1;

	public static final int MAX_PACKETS_PROCESSED = 10;

	@SuppressWarnings("static-access")
	public static int MAX_NPCS = Server.npcHandler.maxNPCs;

	public static String DEATH_MESSAGE = "Oh dear you are a Deadman!";

	public static boolean MINI_GAMES = false;

	public static boolean LOCK_EXPERIENCE = false;

	// TODO

	public static boolean DOUBLE_EXP = false;

	// TODO
	public static String LOGOUT_MESSAGE = "Thanks for playing " + SERVER_NAME;

	public static boolean sendServerPackets = false;

}