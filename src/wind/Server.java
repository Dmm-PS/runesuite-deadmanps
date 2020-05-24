package wind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;

import wind.event.CycleEventHandler;
import wind.model.npcs.NPCHandler;
import wind.model.objects.Doors;
import wind.model.players.Censor;
import wind.model.players.PlayerHandler;
import wind.model.players.content.minigames.FightCaves;
import wind.model.players.content.minigames.FightPits;
import wind.model.players.content.minigames.PestControl;
import wind.model.players.content.minigames.nightmarezone.NightmareZone;
import wind.net.SendKeys;
import wind.net.login.RS2Decoder;
import wind.task.TaskHandler;
import wind.util.ControlPanel;
import wind.util.MadTurnipConnection;
import wind.world.ClanManager;
import wind.world.ItemHandler;
import wind.world.ObjectHandler;
import wind.world.ObjectManager;
import wind.world.PlayerManager;
import wind.world.ShopHandler;
import wind.world.StillGraphicsManager;
import wind.world.WorldMessenger;

/**
 * The main class needed to start the server.
 * 
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmctruck30 Revised by Shawn Notes by Shawn
 */
public class Server {

	public static int restartTime;
	public static ControlPanel panel = new ControlPanel(true);
	public static ClanManager clanManager = new ClanManager();
	public static PlayerManager playerManager = null;
	private static StillGraphicsManager stillGraphicsManager = null;
	public static boolean UpdateServer = false;
	public static long lastMassSave = System.currentTimeMillis();
	public static boolean shutdownServer = false;
	public static boolean shutdownClientHandler;
	public static boolean canLoadObjects = false;
	public static int serverlistenerPort;

	/*
	 * Donation System
	 */
	public static MadTurnipConnection md;

	public static ItemHandler itemHandler = new ItemHandler();
	public static PlayerHandler playerHandler = new PlayerHandler();
	public static NPCHandler npcHandler = new NPCHandler();
	public static ShopHandler shopHandler = new ShopHandler();
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();
	public static FightPits fightPits = new FightPits();
	public static PestControl pestControl = new PestControl();
	public static FightCaves fightCaves = new FightCaves();
	public static NightmareZone nightmareZone = new NightmareZone();
	public static WorldMessenger worldMessage = new WorldMessenger();
	public static Censor censor = new Censor();

	static {
		
	}

	/**
	 * Starts the server.
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static void main(java.lang.String args[])
			throws Exception {
		long startTime = System.currentTimeMillis();

		// md = new MadTurnipConnection();
		// md.start();
		/*
		 * Highscores.process(); if (Highscores.connected) { System.out
		 * .println("Highscores has been connect sucessfuly."); } else {
		 * System.out .println("Highscores failed connecting database."); }
		 */
		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();
		stillGraphicsManager = new StillGraphicsManager();

		// startup the server.
		new ServerBuilder(Config.SERVER_PORT, Runtime.getRuntime().availableProcessors() > 1 ? true : false).build();		
		Doors.getSingleton().load();
		CycleEventHandler.getSingleton().process();
		long endTime = System.currentTimeMillis();
		long elapsed = endTime - startTime;
		addLog(Config.SERVER_NAME + ": Online on port " + Config.SERVER_PORT);
		 RS2Decoder.genRSA();
         System.out.println("Server/Client keys generated:");
         System.out.println("CM: "+RS2Decoder.clientModulus.toString());
         System.out.println("CE: "+RS2Decoder.clientExponent.toString());
         System.out.println("SM: "+RS2Decoder.serverModulus.toString());
         System.out.println("SE: "+RS2Decoder.serverExponent.toString());
         SendKeys.sendKeys();
		try {
			addLog("Public IP Address: " + getIP());
		} catch (Exception e) {
			addLog("Error fetching public IP address.");
		}
	}
	
	/**
	 * Fetches the public IPv4 address.
	 */
	public static String getIP() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://api.ipify.org/").openStream()));
		return br.readLine();
	}
	
	/**
	 * Log to debug file.
	 */
	public static void addLog(String entry) {
		System.out.println("[" + new Date() + "] " + entry);
		
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("debug.log", true)))) {
			out.println("[" + new Date() + "] " + entry);
		} catch (IOException e) {
			System.out.println("[Debug] Unable to write to log file!");
		}
	}
	
	/**
	 * Gets the Graphics manager.
	 */
	public static StillGraphicsManager getStillGraphicsManager() {
		return stillGraphicsManager;
	}

	/**
	 * Gets the Player manager.
	 */
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	/**
	 * Gets the Object manager.
	 */
	public static ObjectManager getObjectManager() {
		return objectManager;
	}

	protected static void process() throws Exception {
		playerHandler.process();
		itemHandler.process();
		npcHandler.process();
		shopHandler.process();
		objectManager.process();
		fightPits.process();
		pestControl.process();
		TaskHandler.sequence();
	}

}
