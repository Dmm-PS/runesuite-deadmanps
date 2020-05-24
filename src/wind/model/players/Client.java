package wind.model.players;

import java.util.HashMap;

import shamon.barrows.Barrows;
import shamon.region.GameObjectManager;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Future;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import wind.Config;
import wind.Constants;
import wind.Punishments;
import wind.Server;
import wind.event.CycleEvent;
import wind.event.CycleEventHandler;
import wind.model.animations.AnimationHandler;
import wind.model.items.Item;
import wind.model.items.ItemAssistant;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.players.achievement.Achievement;
import wind.model.players.combat.melee.CombatPrayer;
import wind.model.players.content.BankPin;
import wind.model.players.content.CombatAssistant;
import wind.model.players.content.ConnectedFrom;
import wind.model.players.content.DialogueHandler;
import wind.model.players.content.DuelHandler;
import wind.model.players.content.DwarfMultiCannon;
import wind.model.players.content.EmoteHandler;
import wind.model.players.content.FlowerGame;
import wind.model.players.content.ItemLottery;
import wind.model.players.content.PlayerKilling;
import wind.model.players.content.PotionMixing;
import wind.model.players.content.PriceChecker;
import wind.model.players.content.TradeHandler;
import wind.model.players.content.consumables.Food;
import wind.model.players.content.consumables.Potions;
import wind.model.players.content.minigames.CastleWars;
import wind.model.players.content.minigames.PestControl;
import wind.model.players.content.minigames.fightcave.FightCave;
import wind.model.players.content.music.MusicTab;
import wind.model.players.content.quest.QuestHandler;
import wind.model.players.content.skills.SkillInterfaces;
import wind.model.players.content.skills.impl.Agility;
import wind.model.players.content.skills.impl.Cooking;
import wind.model.players.content.skills.impl.Crafting;
import wind.model.players.content.skills.impl.Fletching;
import wind.model.players.content.skills.impl.Magic;
import wind.model.players.content.skills.impl.Mining;
import wind.model.players.content.skills.impl.Runecrafting;
import wind.model.players.content.skills.impl.Slayer;
import wind.model.players.content.skills.impl.Thieving;
import wind.model.players.content.skills.impl.farming.Farming;
import wind.model.players.content.skills.impl.firemaking.Firemaking;
import wind.model.players.content.skills.impl.herblore.Herblore;
import wind.model.players.content.skills.impl.hunter.Hunter;
import wind.model.players.content.skills.impl.smithing.Smithing;
import wind.model.players.content.skills.impl.smithing.SmithingInterface;
import wind.model.players.content.skills.impl.woodcutting.Woodcutting;
import wind.model.players.content.sound.Sounds;
import wind.model.players.impl.Curses;
import wind.model.players.impl.Equipment;
import wind.model.players.impl.TimePlayed;
import wind.model.players.plugin.impl.Color;
import wind.model.players.saving.PlayerSave;
import wind.model.players.teleport.Teleport;
import wind.model.shops.ShopAssistant;
import wind.net.Packet;
import wind.net.Packet.Type;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.util.Stream;
import wind.util.log.ChatLogger;
import wind.util.log.TradeLog;
import wind.world.Clan;
import wind.world.WorldMessenger;
import server.models.players.pvp.PvPHandler;

public class Client extends Player {

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private Channel session;
	private PvPHandler pvpHandler = new PvPHandler(this);
	private BankPin bankPin = new BankPin(this);
	private Woodcutting woodcutting = new Woodcutting();
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private TradeHandler playerTrade = new TradeHandler(this);
	private DuelHandler playerDuel = new DuelHandler(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private PlayerKilling playerKilling = new PlayerKilling(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Potions potions = new Potions(this);
	private TanHide tan = new TanHide(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	private SkillInterfaces skillInterfaces = new SkillInterfaces(this);
	private Achievement achievement = new Achievement(this);
	private TradeLog tradeLog = new TradeLog(this);
	private ChatLogger chatLog = new ChatLogger(this);
	private EmoteHandler emoteHandler = new EmoteHandler(this);
	private TimePlayed time = new TimePlayed(this);
	private Inventory inventory = new Inventory();
	private Equipment equipment = new Equipment(this);
	private ItemLottery itemlottery = new ItemLottery(this);
	private FlowerGame flowerGame = new FlowerGame();
	private Curses curses = new Curses(this);
	private WorldMessenger worldMessage = new WorldMessenger();
	private AnimationHandler animationHandler = new AnimationHandler(this);
	private Censor censor = new Censor(this);
	private final QuestHandler questHandler = new QuestHandler(this);
	private Hunter hunter = new Hunter(this);
	private InterfaceManager interfaceManager = new InterfaceManager(this);

	private final Barrows barrows = new Barrows(this);

	public Barrows getBarrows() {
		return barrows;
	}

	private final TanHide hide = new TanHide(this);

	public TanHide getHide() {
		return hide;
	}

	private final DwarfMultiCannon cannon = new DwarfMultiCannon(this);

	public DwarfMultiCannon getCannon() {
		return this.cannon;
	}

	public TimePlayed getTimePlayed() {
		return time;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public int rememberNpcIndex;
	public int objDebug = 20000;
	/**
	 * Skill instances
	 */
	private Slayer slayer = new Slayer(this);
	private Runecrafting runecrafting = new Runecrafting();
	private Agility agility = new Agility(this);
	private Cooking cooking = new Cooking();
	public Mining mining = new Mining();
	private Crafting crafting = new Crafting(this);
	private Fletching fletching = new Fletching(this);
	private Farming farming = new Farming(this);
	private Thieving thieving = new Thieving();
	private Smithing smith = new Smithing(this);
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Firemaking firemaking = new Firemaking();
	private Magic magic = new Magic(this);
	private Herblore herblore = new Herblore();

	public boolean hasNpc;
	public boolean isAttackingNpc;
	public boolean isAttackingPlayer;

	/* Godwars */
	public int ArmadylkillCount = 0;
	public int BandoskillCount = 0;
	public int SarakillCount = 0;
	public int ZammykillCount = 0;
	/* END of Godwars */

	public int openInterfaceId;
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	private Future<?> currentTask;
	public int currentRegion = 0;
	public long lastRoll;
	public int diceItem;
	public int page;
	public boolean slayerHelmetEffect, redSkull, inHelpCc, musicOn = true, soundsOn = true;
	public boolean storing = false;
	public boolean attackSkill = false;
	public boolean strengthSkill = false;
	public boolean defenceSkill = false;
	public boolean mageSkill = false;
	public boolean rangeSkill = false;
	public boolean prayerSkill = false;
	public boolean healthSkill = false;

	public int zulrahStage = 0;

	public Client getClient(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				Client client = getClient(i);
				if (client.playerName.toLowerCase().equalsIgnoreCase(name)) {
					return client;
				}
			}
		}
		return null;
	}

	public Client getClient(int id) {
		return (Client) PlayerHandler.players[id];
	}

	public boolean validClient(int id) {
		if (id < 0 || id > Config.MAX_PLAYERS) {
			return false;
		}
		return validClient(getClient(id));
	}

	public boolean validClient(String name) {
		return validClient(getClient(name));
	}

	public boolean validClient(Client client) {
		return (client != null && !client.disconnected);
	}

	public boolean validNpc(int index) {
		// if (index < 0 || index >= Config.MAX_NPCS) {
		// return false;
		// }
		NPC n = getNpc(index);
		if (n != null) {
			return true;
		}
		return false;
	}

	public NPC getNpc(int index) {
		return ((NPC) NPCHandler.npcs[index]);
	}

	public FightCave fightcave;

	public FightCave getFightCave() {
		if (fightcave == null)
			fightcave = new FightCave(this);
		return fightcave;
	}

	public int waveType;
	public int[] waveInfo = new int[3];

	public static int Barrows[] = { 4740, 4734, 4710, 4724, 4726, 4728, 4730, 4718, 4718, 4732, 4736, 4738, 4716, 4720,
			4722, 4753, 4747, 4755, 4757, 4759, 4708, 4712, 4714, 4745, 4749, 4751 };

	public static int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	public void resetBarrows() {
		barrowsNpcs[0][1] = 0;
		barrowsNpcs[1][1] = 0;
		barrowsNpcs[2][1] = 0;
		barrowsNpcs[3][1] = 0;
		barrowsNpcs[4][1] = 0;
		barrowsNpcs[5][1] = 0;
		barrowsKillCount = 0;
		barrowReset = 0;
	}

	private Teleport teleport = new Teleport(this);

	public Teleport getTeleportHandler() {
		return teleport;
	}

	public void resetPrayer() {
		CombatPrayer.resetPrayers(this);
	}

	private boolean prayerDisabled;

	public boolean isPrayerDisabled() {
		return prayerDisabled;
	}

	public void setPrayerDisabled(boolean prayerDisabled) {
		this.prayerDisabled = prayerDisabled;
	}

	/**
	 * 
	 * // outStream.createFrame(74);//this sends the frame //
	 * outStream.writeWordBigEndian(381);//this is the music id (varrock)
	 * 
	 * @param sound
	 * @param vol
	 * @param delay
	 */
	public void frame174(int sound, int vol, int delay) {
		outStream.createFrame(74);
		outStream.writeWord(sound);
		outStream.writeByte(vol);
		outStream.writeWord(delay);
		updateRequired = true;
		appearanceUpdateRequired = true;
	}

	public int soundVolume = 10;

	/**
	 * Outputs a send packet which is built from the data params provided
	 * towards a connected user client channel.
	 * 
	 * @param id
	 *            The identification number of the sound.
	 * @param volume
	 *            The volume amount of the sound (1-100)
	 * @param delay
	 *            The delay (0 = immediately 30 = 1/2cycle 60=full cycle) before
	 *            the sound plays.
	 */
	public void sendSound(int id, int volume, int delay) {
		try {
			outStream.createFrameVarSize(174);
			outStream.writeWord(id);
			outStream.writeByte(volume);
			outStream.writeWord(delay);
			updateRequired = true;
			appearanceUpdateRequired = true;
			outStream.endFrameVarSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Outputs a send packet which is built from the data params provided
	 * towards a connected user client channel.
	 * 
	 * @param id
	 *            The identification number of the sound.
	 * @param volume
	 *            The volume amount of the sound (1-100)
	 */
	public void sendSound(int id, int volume) {
		sendSound(id, 0, 0);
	}

	/**
	 * Outputs a send packet which is built from the data params provided
	 * towards a connected user client channel.
	 * 
	 * @param id
	 *            The identification number of the sound.
	 */
	public void sendSound(int id) {
		sendSound(id, 100);// pretty sure it's 100 just double check
		// otherwise it will be 1
	}

	public Sounds sounds = new Sounds(this);

	public Sounds getSounds() {
		return sounds;
	}

	public Client(Channel s, int _playerId) {
		super(_playerId);
		this.session = s;
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;

	}

	public void tryLottery() {
		if (getEquipment().freeSlots() >= 1) {
			if (getItems().playerHasItem(995, Constants.LOTTERY_FUND)) {
				getItems().deleteItem(995, Constants.LOTTERY_FUND);
				getItemLottery().Run();
			} else {
				getDH().sendDialogues(226, 220);
			}
		} else {
			getPA().removeAllWindows();
			getPA().sendStatement("You need atleast 1 free spot in your inventory.");
		}
	}

	public void joinHelpCc() {
		if (clan == null) {
			Clan localClan = Server.clanManager.getClan("help");
			if (localClan != null)
				localClan.addMember(this);
			else if ("help".equalsIgnoreCase(this.playerName))
				Server.clanManager.create(this);
			else {
				sendMessage(Misc.formatPlayerName("mod sunny") + " has disabled this clan for now.");
			}
			getPA().refreshSkill(21);
			getPA().refreshSkill(22);
			getPA().refreshSkill(23);
			inHelpCc = true;
		}
	}

	private Map<Integer, TinterfaceText> interfaceText = new HashMap<Integer, TinterfaceText>();

	public class TinterfaceText {
		public int id;
		public String currentState;

		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}

	}

	public boolean checkPacket126Update(String text, int id) {
		if (interfaceText.containsKey(id)) {
			TinterfaceText t = interfaceText.get(id);
			if (text.equals(t.currentState)) {
				return false;
			}
		}
		interfaceText.put(id, new TinterfaceText(text, id));
		return true;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		message = message.substring(0, 1).toUpperCase() + message.substring(1);
		clan = clan.substring(0, 1).toUpperCase() + clan.substring(1);
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 4, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, -1, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, /* 0 */-1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, /* 0 */4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};
	public static boolean IronOptiona = false;

	public void homeTeleport(int x, int y, int h) {
		if (homeTele == 9) {
			startAnimation(4850);
		} else if (homeTele == 7) {
			startAnimation(4853);
			gfx0(802);
		} else if (homeTele == 5) {
			startAnimation(4855);
			gfx0(803);
		} else if (homeTele == 3) {
			startAnimation(4857);
			gfx0(804);
		} else if (homeTele == 1) {
			homeTeleDelay = 0;
			homeTele = 0;
			teleportToX = x;
			teleportToY = y;
			heightLevel = h;
		}
	}

	@Override public void destruct() {
		CycleEventHandler.getSingleton().stopEvents(this);
		if (duelStatus >= 1 && duelStatus <= 5) {
			getDuel().bothDeclineDuel();
			saveCharacter = true;
			return;
		}
		PlayerSave.saveGame(this);
		/*
		 * if (playerRights != 3 || playerRights != 2) { Highscores.save(this);
		 * } if (disconnected == true && playerRights != 3 || playerRights != 2)
		 * { Highscores.save(this); }
		 */
		if (session == null)
			return;
		Server.panel.removeEntity(playerName);
		if (underAttackBy > 0 || underAttackBy2 > 0)
			return;
		if (duelStatus == 6) {
			getDuel().claimStakedItems();
		}
		if (duelStatus >= 1 && duelStatus <= 5) {
			Client od = (Client) PlayerHandler.players[duelingWith];
			getDuel().declineDuel(true);
			od.getDuel().declineDuel(true);
			saveCharacter = true;
			return;
		}
		if (clan != null) {
			clan.removeMember(this);
		}
		if (getDuel().atDuelInterface())
			getDuel().declineDuel(true);
		if (inPits) {
			Server.fightPits.removePlayerFromPits(playerId);
		}
		if (PestControl.isInGame(this)) {
			PestControl.removePlayerGame(this);
			getPA().movePlayer(2440, 3089, 0);
		}
		if (inFightCaves()) {
			getPA().movePlayer(2438, 5168, 0);
		}
		if (inTrade) {
			Client o = (Client) PlayerHandler.players[tradeWith];
			if (o != null)
				o.getTrade().declineTrade();
		}
		Misc.println("[Logged out]: " + playerName + " From " + connectedFrom);
		PriceChecker.clearConfig(this);
		TaskHandler.cancel(this);
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		super.destruct();
	}

	public void calcCombat() {
		int mag = (int) ((getLevelForXP(playerXP[6])) * 1.5);
		int ran = (int) ((getLevelForXP(playerXP[4])) * 1.5);
		int attstr = (int) ((double) (getLevelForXP(playerXP[0])) + (double) (getLevelForXP(playerXP[2])));

		combatLevel = 0;
		if (ran > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875));
		} else if (mag > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875));
		} else {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325) + ((getLevelForXP(playerXP[2])) * 0.325));
		}
	}

	public void addToHp(int toAdd) {
		if (constitution + toAdd >= maxConstitution)
			toAdd = maxConstitution - maxConstitution;
		constitution += toAdd;
	}

	public void removeFromPrayer(int toRemove) {
		if (toRemove > playerLevel[5])
			toRemove = playerLevel[5];
		playerLevel[5] -= toRemove;
		getPA().refreshSkill(5);
	}

	/** Restores player's hitpoints. */
	public void restoreHitpoints() {
		addToHp(this.playerHitpoints * 100);
		this.getPA().refreshSkill(3);
		constitution = playerLevel[3] * 10;
	}

	/** Restores prayer points. */
	public void restorePrayer() {
		playerLevel[5] = getPA().getLevelForXP(playerXP[5]);
		getPA().refreshSkill(5);
	}

	public void sendMessage(String s) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
		// }
	}

	/**
	 * Sends a coloured game message. Colours.RED e.g
	 * 
	 * @param message
	 * @param colour
	 */
	public void sendMessage(String message, Color colour) {
		sendMessage(colour.getColour() + message);
	}

	public void setSidebarInterface(int menuId, int form) {
		synchronized (this) {
			if (getOutStream() != null) {
				outStream.createFrame(71);
				outStream.writeWord(form);
				outStream.writeByteA(menuId);
			}
		}
	}

	public void getDuelHS() {
		getPA().showInterface(6308);
		// getPA().sendFrame126("Close Window", 6401);
		getPA().sendFrame126(" ", 6402);
		getPA().sendFrame126(" ", 6403);
		getPA().sendFrame126(" ", 6404);

		getPA().sendFrame126(" ", 6405);
		getPA().sendFrame126(Config.SERVER_NAME, 640);
		getPA().sendFrame126(" ", 6406);
		getPA().sendFrame126(" ", 6407);
		getPA().sendFrame126(" ", 6408);
		getPA().sendFrame126(" ", 6409);
		getPA().sendFrame126(" ", 6410);
		getPA().sendFrame126(" ", 6411);
		getPA().sendFrame126(" ", 8578);
		getPA().sendFrame126(" ", 8579);
		getPA().sendFrame126(" ", 8580);
		getPA().sendFrame126(" ", 8581);
		getPA().sendFrame126(" ", 8582);
		getPA().sendFrame126(" ", 8583);
		getPA().sendFrame126(" ", 8584);
		getPA().sendFrame126(" ", 8585);
		getPA().sendFrame126(" ", 8586);
		getPA().sendFrame126(" ", 8587);
		getPA().sendFrame126(" ", 8588);
		getPA().sendFrame126(" ", 8589);
		getPA().sendFrame126(" ", 8590);
		getPA().sendFrame126(" ", 8591);
		getPA().sendFrame126(" ", 8592);
		getPA().sendFrame126(" ", 8593);
		getPA().sendFrame126(" ", 8594);
		getPA().sendFrame126(" ", 8595);
		getPA().sendFrame126(" ", 8596);
		getPA().sendFrame126(" ", 8597);
		getPA().sendFrame126(" ", 8598);
		getPA().sendFrame126(" ", 8599);
		getPA().sendFrame126(" ", 8600);
		getPA().sendFrame126(" ", 8601);
		getPA().sendFrame126(" ", 8602);
		getPA().sendFrame126(" ", 8603);
		getPA().sendFrame126(" ", 8604);
		getPA().sendFrame126(" ", 8605);
		getPA().sendFrame126(" ", 8606);
		getPA().sendFrame126(" ", 8607);
		getPA().sendFrame126(" ", 8608);
		getPA().sendFrame126(" ", 8609);
		getPA().sendFrame126(" ", 8610);
		getPA().sendFrame126(" ", 8611);
		getPA().sendFrame126(" ", 8612);
		getPA().sendFrame126(" ", 8613);
		getPA().sendFrame126(" ", 8614);
		getPA().sendFrame126(" ", 8615);
		getPA().sendFrame126(" ", 8616);
		getPA().sendFrame126(" ", 8617);
	}

	public void sendLoginInterface() {
		// getPA().showInterface(15767);
		// getPA().showInterface(15244);
		getPA().sendFrame126("Welcome to Deadman RsPs", 15257);
		getPA().sendFrame126("You are currently logged in from:" + connectedFrom, 15258);
		getPA().sendFrame126(
				"Remember to change your password frequently!\\n\\nYour account is only as safe as you make it!\\n\\nFor more tips, visit the website.",
				15259);
		getPA().sendFrame126("Recent Updates!\\n\\nZulrah & resizable mode!", 15260);
		getPA().sendFrame126("Contact staff if you have any questions.", 15261);
		getPA().sendFrame126("Make sure to vote every day for great rewards", 15262);
		getPA().sendFrame126("Play " + Config.SERVER_NAME, 15263);
		getPA().sendFrame126("Want membership? Don't want to pay? Use in-game bonds!", 15264);
		getPA().sendFrame126("TXT TXT TXT", 15266);
		getPA().sendFrame126("Make sure to put your most valuable items in the safety box!", 15270);
	}
	Client player;
	public void handleLogin() {
		if (addStarter) {
			getPA().addStarter();
		//	getPA().showInterface(3559);
		//	canChangeAppearance = true;
		} else {
			getPA().showInterface(15244);
			getPA().sendFrame126("Welcome to " + Config.SERVER_NAME + "", 15257);
			getPA().sendFrame126("You are currently logged in from:" + connectedFrom, 15258);
			getPA().sendFrame126(
					"Remember to change your password frequently!\\nOther private server owners might log\\non your account if you use the same\\n password for everything!",
					15259);
			getPA().sendFrame126("Recent Updates!\\n\\nRemade the loading bar and login box.", 15260);
			getPA().sendFrame126("Enjoy your Stay! ", 15261);
			getPA().sendFrame126(
					"Voting every 24 hours will help " + Config.SERVER_NAME
							+ "\\nout greatly. If you can remember, please do.", 15262);
			getPA().sendFrame126("Play now ", 15263);
			getPA().sendFrame126(
					"Feel free to ask any golden or silver crown\\nuser for any help regarding the server.", 15264);
			getPA().sendFrame126("Our website will be coming soon\\nas soon as we can afford it!.", 15265);
			getPA().sendFrame126(" Vote Link ", 15266);
			getPA().sendFrame126("The server is currently in beta.", 15270);
		}
	}

	@Override public void initialize() {
		sendLoginInterface();
		// synchronized (this) {
		// handleLogin();
		if (inZulrahShrine())
			getPA().movePlayer(Config.HOME_X, Config.HOME_Y, 0);
		if (this.combatLevel == 1 && (getRights().equal(Rights.DEVELOPER))) {
			npcId2 = 2525;
			isNpc = true;
			updateRequired = true;
			setAppearanceUpdateRequired(true);
		}
		Server.panel.addEntity(playerName);
		if (InFightPitsArena() == true || InFightPitsWaiting() == true) {
			getPA().movePlayer(2399, 5177, 0);
			for (int j = 0; j < Server.fightPits.playerInPits.length; j++) {
				if (Server.fightPits.playerInPits[j] < 0)
					continue;
				if (PlayerHandler.players[Server.fightPits.playerInPits[j]] == null)
					continue;
				Server.fightPits.playerInPits[j] = -1;
				inPits = false;
			}
		}
		// if (PestControl.isInGame(this)) {
		// PestControl.removePlayerGame(this);
		// } else {
		// getPA().movePlayer(2657, 2639, 0);
		// }

		if (inMiscellania()) {
			getPA().showOption(3, 0, "Challenge", 1);
		}
		getPA().sendFrame36(507, 1);
		getPA().sendFrame36(166, 3);
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		Punishments.appendStarters();
		Punishments.appendStarters2();
		getPA().sendFrame126(runEnergy + "%", 149);
		isFullHelm = Item.isFullHelm(playerEquipment[playerHat]);
		isFullMask = Item.isFullMask(playerEquipment[playerHat]);
		isFullBody = Item.isFullBody(playerEquipment[playerChest]);
		getPA().sendFrame36(173, isRunning2 ? 1 : 0);
		getPA().handleLoginText();
		calculateCombatLevel();

		if (getDisplayName().equalsIgnoreCase("notset")) {
			setDisplayName(playerName);
		}
		/*
		 * if (playerRights != 3 || playerRights != 2) { Highscores.process(); }
		 */
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int i = 0; i < 25; i++) {
			getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getPA().refreshSkill(i);
		}
		for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
			prayerActive[p] = false;
			getPA().sendFrame36(PRAYER_GLOW[p], 0);
		}
		getPA().handleWeaponStyle();
		this.getTimePlayed().initiliseNewStart();
		accountFlagged = getPA().checkForFlags();
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame36(172, 1);
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options
		if (playerMagicBook == 0) {
			setSidebarInterface(6, 1151); // modern
		}
		if (playerMagicBook == 1) {
			setSidebarInterface(6, 12855); // ancient
		}
		if (playerMagicBook == 2) {
			setSidebarInterface(6, 29999); // lunar
		}
		getPA().setSidebarInterfaces(this, true);
		correctCoordinates();
		getPA().showOption(4, 0, "Follow", 4);
		getPA().showOption(5, 0, "Trade with", 3);
		getItems().resetItems(3214);
		getItems();
		getItems().sendWeapon(playerEquipment[playerWeapon], ItemAssistant.getItemName(playerEquipment[playerWeapon]));
		getEquipment().resetBonus();
		getEquipment().getBonus();
		getEquipment().writeBonus();
		getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
		getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
		getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
		getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
		getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
		getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
		getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
		getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
		getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
		getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
		getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
		getItems();
		getCombat().getPlayerAnimIndex(this, ItemAssistant.getItemName(playerEquipment[playerWeapon]).toLowerCase());
		redSkull = false;
		getItems().addSpecialBar(playerEquipment[playerWeapon]);
		// sendMessage(Double.toString(specAmount) + ":specialattack:");
		getPA().logIntoPM();
		saveTimer = Config.SAVE_TIMER;
		saveCharacter = true;
		Misc.println("[Logged in]: " + playerName + " From " + connectedFrom);
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
		// getPA().resetFollow();
		getPA().clearClanChat();
		getPA().resetFollow();
		getPA().setClanData();
		getPA().requestUpdates();
		// this.joinHelpCc();
		if (autoRet == 1) {
			getPA().sendFrame36(172, 1);
		} else {
			getPA().sendFrame36(172, 0);
		}
		loadRegion();
		if (petSummoned == true && petID != -1) {
			NPCHandler.summonPet(this, petID, absX - 1, absY - 1, heightLevel);
			sendMessage("Your pet was loyally waiting in the same spot you left them.");
		}

		// MusicTab.loadMusicTab(this);
		if (addStarter) {
			canWalk = true;
			// getPA().starterSideBars(this);
			// getPA().closeAllWindows();
			// getDH().sendDialogues(460, 2244);
			// startGuide();
			getPA().addStarter();
			// getPA().showInterface(3559);
			canChangeAppearance = true;
			MusicTab.initializeMusicBooleanFirstTime(this);
			if (lastClanChat != null && lastClanChat.length() > 0) {
				Clan clan = Server.clanManager.getClan(lastClanChat);
				if (clan != null)
					clan.addMember(this);
			}
		} else if (!addStarter) {

		}

		Task task = new Task(3, false) {
			public void execute() {
				if (absX >= 3521 && absX <= 3582 && absY >= 9664 && absY <= 9728) {
					barrows.getMaze().randomizeMaze();
				}
				cancel();
			}
		};
		TaskHandler.submit(task);
	}
	
	private void loadRegion() {
		// Music.playMusic(this);
		Server.itemHandler.reloadItems(this);
		clearLists();
		Server.objectManager.loadObjects(this);
		if (skullTimer > 0 && headIconPk < 1) {
			isSkulled = true;
			headIconPk = 0;
		}
	}

	@Override public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();

	}

	public void wildyWarning() {
		getPA().showInterface(1908);
	}

	public void logout() {
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		if (inZulrahShrine())
			getPA().movePlayer(Config.HOME_X, Config.HOME_Y, 0);
		if (underAttackBy > 0 || underAttackBy2 > 0)
			return;
		// synchronized (this) {
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			CycleEventHandler.getSingleton().stopEvents(this);
			/*
			 * if (playerRights != 3 || playerRights != 2) {
			 * Highscores.save(this); }
			 */
			if (duelStatus == 5) {
				Client o = (Client) PlayerHandler.players[duelingWith];
				sendMessage("@red@You cannot logout during a duel.");
				saveCharacter = true;
				o.sendMessage("@red@Your opponent tried to logout during this duel.");
				return;
			}
			this.getTimePlayed().initiliseNewEnd();
			outStream.createFrame(109);
			TaskHandler.cancel(this);
			properLogout = true;
			ConnectedFrom.addConnectedFrom(this, connectedFrom);
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	public int packetSize = 0, packetType = -1;

	public int totalPlaytime() {
		return (pTime / 2);
	}

	public String getPlaytime() {
		int DAY = (totalPlaytime() / 86400);
		int HR = (totalPlaytime() / 3600) - (DAY * 24);
		int MIN = (totalPlaytime() / 60) - (DAY * 1440) - (HR * 60);
		return ("Days:" + DAY + " Hours:" + HR + " Minutes:" + MIN + "");
	}

	public String getSmallPlaytime() {
		int DAY = (totalPlaytime() / 86400);
		int HR = (totalPlaytime() / 3600) - (DAY * 24);
		int MIN = (totalPlaytime() / 60) - (DAY * 1440) - (HR * 60);
		return ("Day:" + DAY + "/Hr:" + HR + "/Min:" + MIN + "");
	}

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		setAppearanceUpdateRequired(true);
	}

	public void fadeStarterTele3(final int x, final int y, final int height) {
		lastAction = System.currentTimeMillis();
		resetWalkingQueue();
		dialogueAction = -1;
		teleAction = -1;
		TaskHandler.submit(new Task(1, true) {
			int tStage = 6;

			@Override public void execute() {
				if (tStage == 6) {
					getPA().showInterface(18460);
				}
				if (tStage == 4) {
					getPA().movePlayer(x, y, height);
				}
				if (tStage == 3) {
					getPA().showInterface(18452);
				}
				if (tStage == 0) {
					this.cancel();
					return;
				}
				if (tStage > 0) {
					tStage--;
				}
			}

			@Override public void onCancel() {
				getPA().closeAllWindows();
				getPA().cancelTeleportTask();
				tStage = 0;
				getDH().sendDialogues(471, 2244);
			}
		});
	}

	/*
	 * public void fadeStarterTele2(final int x, final int y, final int height)
	 * { lastAction = System.currentTimeMillis(); resetWalkingQueue();
	 * dialogueAction = -1; teleAction = -1;
	 * CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() { int
	 * tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0;
	 * getDH().sendDialogues(598, 2244); } }, 1); }
	 * 
	 * public void fadeStarterTele(final int x, final int y, final int height) {
	 * lastAction = System.currentTimeMillis(); resetWalkingQueue();
	 * dialogueAction = -1; teleAction = -1;
	 * CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() { int
	 * tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0;
	 * getDH().sendDialogues(597, 2244); } }, 1); }
	 * 
	 * public void fade(final int x, final int y, final int height) { if
	 * (System.currentTimeMillis() - lastAction > 5000) { lastAction =
	 * System.currentTimeMillis(); resetWalkingQueue(); dialogueAction = -1;
	 * teleAction = -1; CycleEventHandler.getSingleton().addEvent(this, new
	 * CycleEvent() { int tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0; } }, 1); } }
	 * 
	 * public void fadeKQ(final int x, final int y, final int height) { if
	 * (System.currentTimeMillis() - lastAction > 5000) { lastAction =
	 * System.currentTimeMillis(); resetWalkingQueue(); dialogueAction = -1;
	 * teleAction = -1; CycleEventHandler.getSingleton().addEvent(this, new
	 * CycleEvent() { int tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0; if
	 * (!getItems().playerHasItem(954)) { getDH().sendStatement(
	 * "I may need a rope to enter this tunnel."); } } }, 1); } }
	 * 
	 * public void fadeDesert(final int x, final int y, final int height) { if
	 * (System.currentTimeMillis() - lastAction > 5000) { lastAction =
	 * System.currentTimeMillis(); resetWalkingQueue(); dialogueAction = -1;
	 * teleAction = -1; CycleEventHandler.getSingleton().addEvent(this, new
	 * CycleEvent() { int tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0;
	 * getDH().sendStatement(
	 * "The desert is too dry ... and you begin to see a Mirage."); } }, 1); } }
	 * 
	 * public void fadeDungeon(final int x, final int y, final int height) { if
	 * (System.currentTimeMillis() - lastAction > 5000) { lastAction =
	 * System.currentTimeMillis(); resetWalkingQueue(); dialogueAction = -1;
	 * teleAction = -1; CycleEventHandler.getSingleton().addEvent(this, new
	 * CycleEvent() { int tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0;
	 * getDH().sendStatement(
	 * "The dungeon begins to collapse ... and you fell down."); } }, 1); } }
	 * 
	 * public void fadeCrash(final int x, final int y, final int height) { if
	 * (System.currentTimeMillis() - lastAction > 5000) { lastAction =
	 * System.currentTimeMillis(); resetWalkingQueue(); dialogueAction = -1;
	 * teleAction = -1; CycleEventHandler.getSingleton().addEvent(this, new
	 * CycleEvent() { int tStage = 6;
	 * 
	 * public void execute(CycleEventContainer container) { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { container.stop(); return; } if (tStage > 0) { tStage--; }
	 * }
	 * 
	 * public void stop() { getPA().closeAllWindows(); tStage = 0;
	 * getDH().sendStatement(
	 * "The boat crashed ... and you reached the sea bottom."); } }, 1); } }
	 */

	@Override public void process() {
		GameObjectManager.processObjects(this);

		if (runEnergy < 100) {
			if (System.currentTimeMillis() > getPA().getAgilityRunRestore(this) + lastRunRecovery) {
				runEnergy++;
				lastRunRecovery = System.currentTimeMillis();
				getPA().sendFrame126(runEnergy + "%", 149);
			}
		}
		if (System.currentTimeMillis() - duelDelay > 1000 && duelCount > 0) {
			if (duelCount != 1) {
				forcedChat("" + (--duelCount));
				duelDelay = System.currentTimeMillis();
			} else {
				damageTaken = new int[Config.MAX_PLAYERS];
				forcedChat("FIGHT!");
				duelCount = 0;
			}
		}
		getPA().sendFrame126((int) (specAmount * 10) + "", 155);
		if (System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10)
				specAmount += 1;
			if (specAmount > 10)
				specAmount = 10;
			getItems().updateSpecialBar();

		}
		if (followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}
		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 5000) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 5000) {
			underAttackBy2 = 0;
		}

		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
						getPA().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}
		if (inWild()) {
		} else if (!inPvP()) {
			wildLevel = 12;
			getPA().walkableInterface(21400);
		} else if (inPvP()) {
			wildLevel = 12;
			getPA().walkableInterface(21300);
			pvpHandler.pvpLevels();
			getPA().showOption(3, 0, "Attack", 1);
		} else if (inSafeZone()) {
			getPA().walkableInterface(21200);
			pvpHandler.pvpLevels();
		} else if (inPcBoat()) {
			getPA().walkableInterface(21119);
		} else if (inPcGame()) {
			getPA().walkableInterface(21100);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (duelStatus == 5) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
		} else if (InFightPitsArena()) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (isViewingOrb) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (isInBarrows() || isInBarrows2()) {
			getPA().walkableInterface(16128);
			getPA().sendFrame126("" + barrowsKillCount, 16137);
			if (barrowsNpcs[2][1] == 2) {
				getPA().sendFrame126("@red@Karils", 16135);
			}
			if (barrowsNpcs[3][1] == 2) {
				getPA().sendFrame126("@red@Guthans", 16134);
			}
			if (barrowsNpcs[1][1] == 2) {
				getPA().sendFrame126("@red@Torags", 16133);
			}
			if (barrowsNpcs[5][1] == 2) {
				getPA().sendFrame126("@red@Ahrims", 16132);
			}
			if (barrowsNpcs[0][1] == 2) {
				getPA().sendFrame126("@red@Veracs", 16131);
			}
			if (barrowsNpcs[4][1] == 2) {
				getPA().sendFrame126("@red@Dharoks", 16130);
			}
		} else if (inGWD()) {
			getPA().walkableInterface(16210);
			getPA().sendFrame126("@cya@" + bandosKills, 16217);
			getPA().sendFrame126("@cya@" + zamorakKills, 16219);
			getPA().sendFrame126("@cya@" + saraKills, 16218);
			getPA().sendFrame126("@cya@" + armaKills, 16216);
		} else if (CastleWars.isInCw(this) || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (InFightPitsWaiting()) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (playerEquipment[playerWeapon] == 7449) {
			getPA().showOption(3, 0, "Ban", 2);
		} else if (inNMZ()) {
			getPA().walkableInterface(920);
		} else if (inCastlePKArea()) {
			getPA().walkableInterface(-1);
			getPA().sendFrame126("@gre@SafeZone", 199);
			getPA().showOption(3, 0, "Null", 1);
		} else if (inMiscellania()) {
			getPA().showOption(3, 0, "Challenge", 1);
			getPA().walkableInterface(201);
		} else if (!CastleWars.isInCwWait(this)) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}

		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}
		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}
		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
			poisonDamage = -1;
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}
		if (pTime != 2147000000) {
			pTime++;
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(this, oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(this, oldPlayerIndex);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
			getPA().sendFrame126("Combat Level: " + combatLevel, 19000);
			getPA().sendFrame126("" + constitution, 19001);
			curses().handleProcess();
		}
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public TradeHandler getTrade() {
		return playerTrade;
	}

	public DuelHandler getDuel() {
		return playerDuel;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public PlayerKilling getKill() {
		return playerKilling;
	}

	public TanHide getTanning() {
		return tan;
	}

	public Channel getSession() {
		return session;
	}

	public Achievement getAchievement() {
		return achievement;
	}

	public TradeLog getTradeLog() {
		return tradeLog;
	}

	public ChatLogger getChatLog() {
		return chatLog;
	}

	public Censor getCensor() {
		return censor;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Food getFood() {
		return food;
	}

	public boolean isWearingEquipment() {
		if ((playerEquipment[playerHat] == -1) && (playerEquipment[playerCape] == -1)
				&& (playerEquipment[playerAmulet] == -1) && (playerEquipment[playerChest] == -1)
				&& (playerEquipment[playerShield] == -1) && (playerEquipment[playerLegs] == -1)
				&& (playerEquipment[playerHands] == -1) && (playerEquipment[playerFeet] == -1)
				&& (playerEquipment[playerWeapon] == -1)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isBusy = false;
	private boolean isBusyHP = false;
	public boolean isBusyFollow = false;

	public boolean checkBusy() {
		/*
		 * if (getCombat().isFighting()) { return true; }
		 */
		if (isBusy) {
			// actionAssistant.sendMessage("You are too busy to do that.");
		}
		return isBusy;
	}

	public boolean checkBusyHP() {
		return isBusyHP;
	}

	public boolean checkBusyFollow() {
		return isBusyFollow;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusyFollow(boolean isBusyFollow) {
		this.isBusyFollow = isBusyFollow;
	}

	public void setBusyHP(boolean isBusyHP) {
		this.isBusyHP = isBusyHP;
	}

	public boolean isBusyHP() {
		return isBusyHP;
	}

	public boolean isBusyFollow() {
		return isBusyFollow;
	}

	public boolean canWalk = true;
	public boolean isDoingEmote;
	public long skillcapeDelay;
	public boolean isTeleporting;

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	public SkillInterfaces getSI() {
		return skillInterfaces;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public ItemLottery getItemLottery() {
		return itemlottery;
	}

	public FlowerGame getFlowerGame() {
		return flowerGame;
	}

	public WorldMessenger getwM() {
		return worldMessage;
	}

	/**
	 * Skill Constructors
	 */
	public Slayer getSlayer() {
		return slayer;
	}

	public Woodcutting getWoodcutting() {
		return woodcutting;
	}

	public Runecrafting getRunecrafting() {
		return runecrafting;
	}

	public BankPin getBankPin() {
		return bankPin;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Agility getAgility() {
		return agility;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public Farming getFarming() {
		return farming;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public Smithing getSmithing() {
		return smith;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public Firemaking getFiremaking() {
		return firemaking;
	}

	public Fletching getFletching() {
		return fletching;
	}
	public Magic getMagic() {
		return magic;
	}

	public Hunter getHunter() {
		return hunter;
	}

	public EmoteHandler getEmoteHandler() {
		return emoteHandler;
	}

	public AnimationHandler getanimationHandler() {
		return animationHandler;
	}

	public Curses curses() {
		return curses;
	}

	public QuestHandler getQuest() {
		return questHandler;
	}

	/**
	 * End of Skill Constructors
	 */

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public void setPacketsProcessed(int i) {
		packetsProcessed = i;
	}

	private int packetsProcessed;
	public String forcedChat;
	public int selectedSkill, theStryke;
	public boolean caIrontalk1;
	public boolean caIronOptiona;
	public int[] boss;
	public int bossSlot = -1;
	public boolean isMember;
	public int memberPoints;
	public int easyClueCount;
	public int mediumClueCount;
	public int hardClueCount;
	public int product;
	public int[] ingredient = new int[2];
	public String productName;
	public int tradeBond;
	public int untradeBond;
	public int petIndex = this.getId();
	public boolean acbSpec = false;
	

	@Override public boolean processQueuedPackets() {
		if (packetsProcessed >= Config.MAX_PACKETS_PROCESSED) {
			queuedPackets.clear();
			return false;
		}
		synchronized (queuedPackets) {
			Packet p = null;
			while ((p = queuedPackets.poll()) != null) {
				inStream.currentOffset = 0;
				packetType = p.getOpcode();
				packetSize = p.getLength();
				inStream.buffer = p.getPayload().array();
				if (packetType > 0) {
					PacketHandler.processPacket(this, packetType, packetSize);
					packetsProcessed++;
				}
			}
		}
		return true;
	}

	/*
	 * public void fadeStarterTele3(final int x, final int y, final int height)
	 * { lastAction = System.currentTimeMillis(); resetWalkingQueue();
	 * dialogueAction = -1; teleAction = -1; TaskHandler.submit(new Task(1,
	 * true) { int tStage = 6;
	 * 
	 * @Override public void execute() { if (tStage == 6) {
	 * getPA().showInterface(18460); } if (tStage == 4) { getPA().movePlayer(x,
	 * y, height); } if (tStage == 3) { getPA().showInterface(18452); } if
	 * (tStage == 0) { this.cancel(); return; } if (tStage > 0) { tStage--; } }
	 * 
	 * @Override public void onCancel() { getPA().closeAllWindows(); tStage = 0;
	 * getDH().sendDialogues(471, 2244); } }); }
	 */

	public void correctCoordinates() {
		if (inPcGame()) {
			getPA().movePlayer(2657, 2639, 0);
		}
		if (inFightCaves()) {
			getPA().movePlayer(absX, absY, playerId * 4);
			sendMessage("Your wave will start in 10 seconds.");
			TaskHandler.submit(new Task(20, true) {
				@Override public void execute() {
					this.cancel();
				}

				@Override public void onCancel() {

				}

			});
		}
		if (this.inFightCaves()) {
			getPA().movePlayer(absX, absY, playerId * 4);
			sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
			// getFightCave().spawn();
		}
	}

	public void resetPlayerAttack(final Client c) {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.faceUpdate(0);
		c.playerIndex = 0;
		c.getPA().resetFollow();
		// c.sendMessage("Reset att ack.");
	}

	public void fightPitsOrb(String setting, int frame) {
		setSidebarInterface(10, 3209);
		getPA().sendFrame126("@yel@Centre", 15239);
		getPA().sendFrame126("@yel@North-West", 15240);
		getPA().sendFrame126("@yel@North-East", 15241);
		getPA().sendFrame126("@yel@South-East", 15242);
		getPA().sendFrame126("@yel@South-West", 15243);
		getPA().sendFrame126("@whi@" + setting, frame);
		canWalk = false;
		getPA().ViewingOrbSideBar(this);
		getPA().requestUpdates();
	}

	public void hidePlayer() {
		if (isViewingOrb()) {
			if (heightLevel != 0) {
				heightLevel = 0;
			} else if (heightLevel == 0) {
				getPA().sendFrame99(2);
				npcId2 = 3505;
				isNpc = true;
				isViewingOrb = true;
				getPA().requestUpdates();
			}
		}
	}

	public void showPlayer() {
		if (isViewingOrb()) {
			return;
		} else {
			isNpc = false;
			isViewingOrb = false;
			getItems();
			ItemAssistant.getItemName(playerEquipment[playerWeapon]);
			getPA().requestUpdates();
		}
	}

	public void exitViewingOrb() {
		setSidebarInterface(10, 2449);
		teleportToX = 2399;
		teleportToY = 5173;
		heightLevel = 0;
		isNpc = false;
		getPA().setSidebarInterfaces(this, true);
		getPA().requestUpdates();
	}
	public String combatProtected1() {
		if (combatProtected1 == 0) {
			return "@red@Attack@bla@";
		}
		if (combatProtected1 == 1) {
			return "@red@Defense@bla@";
		}
		if (combatProtected1 == 2) {
			return "@red@Strength@bla@";
		}
		if (combatProtected1 == 3) {
			return "@red@Hitpoints@bla@";
		}
		if (combatProtected1 == 4) {
			return "@red@Range@bla@";
		}
		if (combatProtected1 == 5) {
			return "@red@Prayer@bla@";
		}
		if (combatProtected1 == 6) {
			return "@red@Magic@bla@";
		}
		return "none";
	}
	public String combatProtected2() {
		if (combatProtected2 == 0) {
			return "@red@Attack@bla@";
		}
		if (combatProtected2 == 1) {
			return "@red@Defense@bla@";
		}
		if (combatProtected2 == 2) {
			return "@red@Strength@bla@";
		}
		if (combatProtected2 == 3) {
			return "@red@Hitpoints@bla@";
		}
		if (combatProtected2 == 4) {
			return "@red@Range@bla@";
		}
		if (combatProtected2 == 5) {
			return "@red@Prayer@bla@";
		}
		if (combatProtected2 == 6) {
			return "@red@Magic@bla@";
		}
		return "none";
	}
	public String skillProtected1() {
		if (skillProtected1 == 7) {
			return "@red@Cooking@bla@";
		}
		if (skillProtected1 == 8) {
			return "@red@Woodcutting@bla@";
		}
		if (skillProtected1 == 9) {
			return "@red@Fletching@bla@";
		}
		if (skillProtected1 == 10) {
			return "@red@Fishing@bla@";
		}
		if (skillProtected1 == 11) {
			return "@red@Firemaking@bla@";
		}
		if (skillProtected1 == 12) {
			return "@red@Crafting@bla@";
		}
		if (skillProtected1 == 13) {
			return "@red@Smithing@bla@";
		}
		if (skillProtected1 == 14) {
			return "@red@Mining@bla@";
		}
		if (skillProtected1 == 15) {
			return "@red@Herblore@bla@";
		}
		if (skillProtected1 == 16) {
			return "@red@Agility@bla@";
		}
		if (skillProtected1 == 17) {
			return "@red@Thieving@bla@";
		}
		if (skillProtected1 == 18) {
			return "@red@Slayer@bla@";
		}
		if (skillProtected1 == 19) {
			return "@red@Farming@bla@";
		}
		if (skillProtected1 == 20) {
			return "@red@Runecrafting@bla@";
		}
		if (skillProtected1 == 21) {
			return "@red@Hunter@bla@";
		}
		if (skillProtected1 == 22) {
			return "@red@Construction@bla@";
		}
		return "none";
	}
	public String skillProtected2() {
		if (skillProtected2 == 7) {
			return "@red@Cooking@bla@";
		}
		if (skillProtected2 == 8) {
			return "@red@Woodcutting@bla@";
		}
		if (skillProtected2 == 9) {
			return "@red@Fletching@bla@";
		}
		if (skillProtected2 == 10) {
			return "@red@Fishing@bla@";
		}
		if (skillProtected2 == 11) {
			return "@red@Firemaking@bla@";
		}
		if (skillProtected2 == 12) {
			return "@red@Crafting@bla@";
		}
		if (skillProtected2 == 13) {
			return "@red@Smithing@bla@";
		}
		if (skillProtected2 == 14) {
			return "@red@Mining@bla@";
		}
		if (skillProtected2 == 15) {
			return "@red@Herblore@bla@";
		}
		if (skillProtected2 == 16) {
			return "@red@Agility@bla@";
		}
		if (skillProtected2 == 17) {
			return "@red@Thieving@bla@";
		}
		if (skillProtected2 == 18) {
			return "@red@Slayer@bla@";
		}
		if (skillProtected2 == 19) {
			return "@red@Farming@bla@";
		}
		if (skillProtected2 == 20) {
			return "@red@Runecrafting@bla@";
		}
		if (skillProtected2 == 21) {
			return "@red@Hunter@bla@";
		}
		if (skillProtected2 == 22) {
			return "@red@Construction@bla@";
		}
		return "none";
	}
	public String skillProtected3() {
		if (skillProtected3 == 7) {
			return "@red@Cooking@bla@";
		}
		if (skillProtected3 == 8) {
			return "@red@Woodcutting@bla@";
		}
		if (skillProtected3 == 9) {
			return "@red@Fletching@bla@";
		}
		if (skillProtected3 == 10) {
			return "@red@Fishing@bla@";
		}
		if (skillProtected3 == 11) {
			return "@red@Firemaking@bla@";
		}
		if (skillProtected3 == 12) {
			return "@red@Crafting@bla@";
		}
		if (skillProtected3 == 13) {
			return "@red@Smithing@bla@";
		}
		if (skillProtected3 == 14) {
			return "@red@Mining@bla@";
		}
		if (skillProtected3 == 15) {
			return "@red@Herblore@bla@";
		}
		if (skillProtected3 == 16) {
			return "@red@Agility@bla@";
		}
		if (skillProtected3 == 17) {
			return "@red@Thieving@bla@";
		}
		if (skillProtected3 == 18) {
			return "@red@Slayer@bla@";
		}
		if (skillProtected3 == 19) {
			return "@red@Farming@bla@";
		}
		if (skillProtected3 == 20) {
			return "@red@Runecrafting@bla@";
		}
		if (skillProtected3 == 21) {
			return "@red@Hunter@bla@";
		}
		if (skillProtected3 == 22) {
			return "@red@Construction@bla@";
		}
		return "none";
	}
	private CycleEvent skilling;
	public void setSkilling(CycleEvent event) {
		this.skilling = event;
	}

	public CycleEvent getSkilling() {
		return skilling;
	}
	private int skillTask;
	public int getTask() {
		skillTask++;
		if (skillTask > Integer.MAX_VALUE - 2) {
			skillTask = 0;
		}
		return skillTask;
	}

	public boolean checkTask(int task) {
		return task == skillTask;
	}

	public Mining getMining() {
		// TODO Auto-generated method stub
		return mining;
	}
}
