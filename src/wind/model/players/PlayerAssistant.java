package wind.model.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import wind.Config;
import wind.Constants;
import wind.Punishments;
import wind.Server;
import wind.clip.region.Region;
import wind.model.items.Item;
import wind.model.items.ItemAssistant;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.npcs.impl.Guard;
import wind.model.npcs.impl.Zulrah;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.content.Teles;
import wind.model.players.saving.PlayerSave;
import wind.model.shops.ShopAssistant;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.Clan;

public class PlayerAssistant {

	private Client c;

	public PlayerAssistant(Client Client) {
		this.c = Client;
	}

	public void appendWalkingQueue(int x, int y) {
		c.addToWalkingQueue(x - (c.mapRegionX * 8), y - (c.mapRegionY * 8));
	}

	public void setWidgetModel(int interfaceID, int modelID) {
		c.getOutStream().createFrame(8);
		c.getOutStream().writeWordBigEndianA(interfaceID);
		c.getOutStream().writeWord(modelID);
	}

	public int CraftInt, Dcolor, FletchInt;

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */

	public void multiWay(int i1) {
		// synchronized(c) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		Player.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void ditchJump(final Client c, final int x, final int y) {
		c.getPA().walkTo(x, y);
	//	c.isRunning2 = false;
	//	c.startAnimation(6132);
		c.getPA().requestUpdates();
	}

	public void resetDitchJump(final Client c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.getItems();
	//	c.getCombat().getPlayerAnimIndex(c, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		c.getPA().requestUpdates();
	}
	public static int rareDropTable[] = { 1615, 2363, 1615, 985, 987, 989, 1319, 1373, 1185, 1201, 1149, 1247, 1249, 2366 };
	public static int uncommonDropTable[] =  { 1617, 219, 217, 215, 213 };
	public static int commonDropTable[] = { 1623, 1621, 1619, 442, 199, 201, 203, 205 };

	public static int lowLevelReward[] = { 1077, 1089, 1107, 1125, 1131, 1129, 1133, 1511, 1168, 1165, 1179, 1195, 1217,
			1283, 1297, 1313, 1327, 1341, 1361, 1367, 1426, 2633, 2635, 2637, 7388, 7386, 7392, 7390, 7396, 7394, 2631,
			7364, 7362, 7368, 7366, 2583, 2585, 2587, 2589, 2591, 2593, 2595, 2597, 7332, 7338, 7350, 7356 };
	public static int mediemLevelReward[] = { 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 7334, 7340, 7346, 7352,
			7358, 7319, 7321, 7323, 7325, 7327, 7372, 7370, 7380, 7378, 2645, 2647, 2649, 2579, 1073, 1091, 1099,
			1111, 1135, 1124, 1145, 1161, 1169, 1183, 1199, 1211, 1245, 1271, 1287, 1301, 1317, 1332, 1357, 1371, 1430,
			6916, 6918, 6920, 6922, 6924, 10400, 10402, 10416, 10418, 10420, 10422, 10436, 10438, 6889 };
	public static int highLevelReward[] = { 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 1275, 1303, 1319, 1333,
			1359, 1373, 2491, 2497, 2503, 861, 859, 2651, 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201,
			1275, 1303, 1319, 1333, 1359, 1373, 2491, 2497, 2503, 861, 859, 2577, 2651, 2615, 2617, 2619, 2621,
			2623, 2625, 2627, 2629, 2639, 2641, 2643, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671,
			2673, 2675, 7342, 7348, 7454, 7460, 7374, 7376, 7382, 7384, 7398, 7399, 7400, 3481, 3483, 3485, 3486, 3488,
			1079, 1093, 1113, 1127, 1148, 1164, 1185, 1201, 1213, 1247, 1275, 1289, 1303, 1319, 1333, 1347, 1359, 1374,
			1432, 2615, 2617, 2619, 2621, 2623, 10368, 10376, 10384, 10370, 10378,
			10386, 10372, 10380, 10374, 10382, 10390, 10470, 10472, 10474, 10440, 10442, 10444, 6914 };
	public static int lowLevelRare[] = { 3827, 3828, 3829, 3830, 3831, 3832, 3833, 3834, 3835, 3836, 3837, 3838 };
	public static int mediemLevelRare[] = { 3827, 3828, 3829, 3830, 3831, 3832, 3833, 3834, 3835, 3836, 3837, 3838, 2577, 2579, 2581,
			 10446, 10448, 10450, 10452, 10454, 10456 };
	public static int highLevelRare[] = { 3827, 3828, 3829, 3830, 3831, 3832, 3833, 3834, 3835, 3836, 3837, 3838, 10330, 10332, 10334, 10336, 10338,
			 10340, 10342, 10344, 10346, 10348, 10350, 10352 };
	

	public static int lowLevelStacks[] = { 995, 380, 561, 886, };
	public static int mediumLevelStacks[] = { 995, 374, 561, 563, 890, };
	public static int highLevelStacks[] = { 995, 386, 561, 563, 560, 892 };

	public void setSidebarInterfaces(Client c, boolean i) {
		if (i) {
			c.getPA().handleWeaponStyle();
			c.setSidebarInterface(1, 3917);
			c.setSidebarInterface(2, 638);
			c.setSidebarInterface(3, 3213);
			c.setSidebarInterface(4, 1644);
			c.setSidebarInterface(5, 5608);
			if (c.playerMagicBook == 0)
				c.setSidebarInterface(6, 1151); // modern
			else if (c.playerMagicBook == 1)
				c.setSidebarInterface(6, 12855); // ancient
			else if (c.playerMagicBook == 2)
				c.setSidebarInterface(6, 29999);
			// c.setSidebarInterface(7, 50128); //clan chat
			c.setSidebarInterface(7, 18128); // clan chat
			c.setSidebarInterface(8, 5065);
			c.setSidebarInterface(9, 5715);
			c.setSidebarInterface(10, 2449);
			// setSidebarInterface(11, 4445); // wrench tab
			c.setSidebarInterface(11, 904); // wrench tab
			c.setSidebarInterface(12, 147); // run tab
			c.setSidebarInterface(13, 962);
			c.setSidebarInterface(0, 2423);
		} else {
			c.setSidebarInterface(1, -1);
			c.setSidebarInterface(2, -1);
			c.setSidebarInterface(3, 6014);//
			c.setSidebarInterface(4, -1);
			c.setSidebarInterface(5, -1);
			if (c.playerMagicBook == 0) {
				c.setSidebarInterface(6, -1); // modern
			} else {
				c.setSidebarInterface(6, -1); // ancient
			}
			// c.setSidebarInterface(7, 50128);
			c.setSidebarInterface(7, 18128); // clan chat
			c.setSidebarInterface(8, 5065);
			c.setSidebarInterface(9, 5715);
			c.setSidebarInterface(10, -1);
			// setSidebarInterface(11, 4445); // wrench tab
			c.setSidebarInterface(11, -1); // wrench tab
			c.setSidebarInterface(12, -1); // run tab
			c.setSidebarInterface(13, -1);
			c.setSidebarInterface(14, -1);
			c.setSidebarInterface(0, -1);
		}
	}

	public void ViewingOrbSideBar(Client c) {
		c.setSidebarInterface(0, -1);
		c.setSidebarInterface(1, -1);
		c.setSidebarInterface(2, -1);
		c.setSidebarInterface(3, -1);//
		c.setSidebarInterface(4, -1);
		c.setSidebarInterface(5, -1);
		c.setSidebarInterface(6, -1);
		c.setSidebarInterface(7, -1);
		c.setSidebarInterface(8, -1);
		c.setSidebarInterface(9, -1);
		c.setSidebarInterface(11, -1); // wrench tab
		c.setSidebarInterface(12, -1); // run tab
		c.setSidebarInterface(13, -1);
		c.setSidebarInterface(14, -1);
		c.setSidebarInterface(0, -1);
	}

	public void starterSideBars(Client c) {
		c.setSidebarInterface(1, -1);
		c.setSidebarInterface(2, -1);
		c.setSidebarInterface(3, -1);//
		c.setSidebarInterface(4, -1);
		c.setSidebarInterface(5, -1);
		if (c.playerMagicBook == 0) {
			c.setSidebarInterface(6, -1); // modern
		} else {
			c.setSidebarInterface(6, -1); // ancient
		}
		c.setSidebarInterface(7, -1);
		c.setSidebarInterface(8, -1);
		c.setSidebarInterface(9, -1);
		c.setSidebarInterface(10, -1);
		// setSidebarInterface(11, 4445); // wrench tab
		c.setSidebarInterface(11, -1); // wrench tab
		c.setSidebarInterface(12, -1); // run tab
		c.setSidebarInterface(13, -1);
		c.setSidebarInterface(14, -1);
		c.setSidebarInterface(0, -1);
	}

	int tmpNWCX[] = new int[50];
	int tmpNWCY[] = new int[50];

	public void walkTo3(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.absX + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
		int l = c.absY + j;
		l -= c.mapRegionY * 8;
		c.isRunning2 = false;
		c.isRunning = false;
		c.getNewWalkCmdX()[0] += k;
		c.getNewWalkCmdY()[0] += l;
		c.poimiY = l;
		c.poimiX = k;
	}

	/*
	 * Tells the Client which skill the player is doing.
	 */
	public void sendSkillXP(int skillID, int xp) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(124);
			c.outStream.writeWord(skillID);
			c.outStream.writeWord(xp);
			c.outStream.endFrameVarSize();
		}
	}

	public boolean addSkillXP(double amount, int skill) {
		/*
		 * if (c.playerRights == 5 || c.playerRights == 7 || c.playerRights ==
		 * 8) { amount *= 1.25; }
		 */
		if (c.expLock) {
			return false;
		}
		if (amount + c.playerXP[skill] < 0 || c.playerXP[skill] > 200000000) {
			if (c.playerXP[skill] > 200000000) {
				c.playerXP[skill] = 200000000;
			}
			return false;
		}

		amount *= Config.SERVER_EXP_BONUS;
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public boolean checkDisplayName(String name) {
		try {
			File list = new File(Config.DATA_LOC + "/displaynames.txt");
			FileReader read = new FileReader(list);
			BufferedReader reader = new BufferedReader(read);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equalsIgnoreCase(name)) {
					reader.close();
					return true;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createDisplayName(String name) {
		BufferedWriter names = null;
		try {
			names = new BufferedWriter(new FileWriter(Config.DATA_LOC + "/displaynames.txt", true));
			names.write(name);
			names.newLine();
			names.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (names != null) {
				try {
					names.close();
				} catch (IOException e2) {
				}
			}
		}
	}
	

	public boolean playerNameExists(String name) {
		try {
			File names = new File("./Data/characters/" + name + ".txt");
			if (names.exists()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void sendInterface(int interfaceid) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();
		}
	}

	public void sendItemsOnDialogue2(String header, String one, String two, int item, int zoom) {
		sendFrame246(4888, zoom, item);
		sendFrame126(header, 4889);
		sendFrame126(one, 4890);
		sendFrame126(two, 4891);
		sendFrame164(4887);
		c.nextChat = 0;
	}

	public void sendChatBoxInterface(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			// c.flushOutStream();
		}
	}

	public void sendCameraShake(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		c.getOutStream().createFrame(35);
		c.getOutStream().writeByte(verticleAmount);
		c.getOutStream().writeByte(verticleSpeed);
		c.getOutStream().writeByte(horizontalAmount);
		c.getOutStream().writeByte(horizontalSpeed);
	}

	/**
	 * Resets screen shake.
	 */
	public void resetCameraShake() {
		sendCameraShake(1, 0, 0, 0);
	}

	public void addStarter2() {
		if (!Punishments.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			Punishments.addIpToStarterList1(PlayerHandler.players[c.playerId].connectedFrom);
			Punishments.addIpToStarter1(PlayerHandler.players[c.playerId].connectedFrom);
			c.getItems().addItem(1323, 1);
			c.getItems().addItem(1309, 1);
			c.getItems().addItem(1333, 1);
			c.getItems().addItem(1319, 1);
			c.getItems().addItem(7458, 1);
			c.getItems().addItem(380, 100);
			c.getItems().addItem(1265, 1);
			c.getItems().addItem(1351, 1);
			c.getItems().addItem(1205, 1);
			c.getItems().addItem(1277, 1);
			c.getItems().addItem(1171, 1);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(882, 100);
			c.getItems().addItem(556, 100);
			c.getItems().addItem(558, 100);
			c.getItems().addItem(555, 100);
			c.getItems().addItem(557, 100);
			c.getItems().addItem(559, 100);
			c.getItems().addItem(554, 100);
			c.getItems().addItem(1381, 1);
			c.getItems().addItem(1712, 1);
			c.getItems().addItem(995, 1500000);
		} else if (Punishments.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)
				&& !Punishments.hasRecieved2ndStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			Punishments.addIpToStarterList2(PlayerHandler.players[c.playerId].connectedFrom);
			Punishments.addIpToStarter2(PlayerHandler.players[c.playerId].connectedFrom);
			c.getItems().addItem(1323, 1);
			c.getItems().addItem(1309, 1);
			c.getItems().addItem(1333, 1);
			c.getItems().addItem(1319, 1);
			c.getItems().addItem(7458, 1);
			c.getItems().addItem(380, 100);
			c.getItems().addItem(1265, 1);
			c.getItems().addItem(1351, 1);
			c.getItems().addItem(1205, 1);
			c.getItems().addItem(1277, 1);
			c.getItems().addItem(1171, 1);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(882, 100);
			c.getItems().addItem(556, 100);
			c.getItems().addItem(558, 100);
			c.getItems().addItem(555, 100);
			c.getItems().addItem(557, 100);
			c.getItems().addItem(559, 100);
			c.getItems().addItem(554, 100);
			c.getItems().addItem(1381, 1);
			c.getItems().addItem(1712, 1);
			c.getItems().addItem(995, 1500000);
		} else if (Punishments.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)
				&& Punishments.hasRecieved2ndStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.sendMessage("You have already received 2 starters from this IP."); // recieved
			// received
		}
	}

	public boolean attackingRanged() { // ranged
		int w = c.playerEquipment[c.playerWeapon];
		if (w == 767 || w == 837 || w == 839 || w == 841 || w == 843 || w == 845 || w == 847 || w == 851 || w == 853
				|| w == 855 || w == 857 || w == 859 || w == 2883 || w == 4236 || w == 4734)
			return true;
		return false;

	}

	public void destroyInterface(int itemId) {// Destroy item created by Remco
		itemId = c.droppedItem;// The item u are dropping
		c.getItems();
		String itemName = ItemAssistant.getItemName(c.droppedItem);
		String[][] info = { // The info the dialogue gives
				{ "Are you sure you want to drop this item?", "14174" }, { "Yes.", "14175" }, { "No.", "14176" },
				{ "", "14177" }, { "This item is valuable, you will not", "14182" },
				{ "get it back once clicked Yes.", "14183" }, { itemName, "14184" } };
		sendFrame34(itemId, 0, 14171, 1);
		for (int i = 0; i < info.length; i++)
			sendFrame126(info[i][0], Integer.parseInt(info[i][1]));
		sendFrame164(14170);
	}

	public void destroyItem(int itemId) {
		itemId = c.droppedItem;
		c.getItems();
		String itemName = ItemAssistant.getItemName(itemId);
		c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId),
				c.playerItemsN[c.getItems().getItemSlot(itemId)]);
		c.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
		removeAllWindows();
	}

	boolean failed = false;

	public void bankAll() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.getItems().bankItem(c.playerItems[i], i, c.playerItemsN[i]);
		}
	}

	public void bankEquip() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			int itemId = c.playerEquipment[i];
			int itemAmount = c.playerEquipmentN[i];
			if (c.getEquipment().freeSlots() > 0) {
				c.getItems().removeItem(itemId, i);
				c.getItems().bankItem(itemId, c.getItems().getItemSlot(itemId), itemAmount);
			} else {
				failed = true;
				continue;
			}
		}
		if (failed)
			c.sendMessage("You need free space in your inventory to use this");
		failed = false;
	}

	public void handleStairs() {
		c.getDH().sendOption2("Climb Up", "Climb Down");
		c.dialogueAction = 850;
	}

	public void handleUp() {
		c.getDH().sendOption2("Climb Up", "");
		c.dialogueAction = 851;
	}

	public void handleDown() {
		c.getDH().sendOption2("Climb Down", "");
		c.dialogueAction = 852;
	}

	public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		if (c == null) {
			return;
		}
		try {
			c.outStream.createFrame(85);
			c.outStream.writeByteC(Y - (c.mapRegionY * 8));
			c.outStream.writeByteC(X - (c.mapRegionX * 8));
			int x = 0;
			int y = 0;
			c.outStream.createFrame(160);
			c.outStream.writeByteS(((x & 7) << 4) + (y & 7));// tiles away -
			// could just
			// send 0
			c.outStream.writeByteS((tileObjectType << 2) + (orientation & 3));
			c.outStream.writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void objectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client person = (Client) PlayerHandler.players[i];
				if (person != null && person.distanceToPoint(X, Y) <= 25) {
					person.getPA().createPlayersObjectAnim(X, Y, animationID, tileObjectType, orientation);
				}
			}
		}
	}

	public void sendConfig(Client client, int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			client.getOutStream().createFrame(86);
			client.getOutStream().writeWordBigEndian(id);
			client.getOutStream().writeDWord(value);
		} else {
			client.getOutStream().createFrame(36);
			client.getOutStream().writeWordBigEndian(id);
			client.getOutStream().writeByte(value);
		}
	}

	public NPC getNpcWithinDistance(final Player player, final int tiles) {
		NPC npc = null;
		final int myX = player.cannonBaseX;
		final int myY = player.cannonBaseY;
		boolean status = true;
		for (final NPC n : NPCHandler.npcs) {
			if (n == null) {
				continue;
			}
			if (player.WithinDistance(n.getX(), n.getY(), player.getX(), player.getY(), tiles)) {
				if (n.isDead && n.heightLevel != player.heightLevel && n.isDead && n.HP == 0 && n.npcType == 1266
						&& n.npcType == 1268) {
					continue;
				}
				for (final int element : Constants.NON_ATTAKABLE_NPCS) {
					if (element == n.npcType) {
						status = false;
						break;
					}
				}
				if (!status) {
					return null;
				}
				if (npc != null) {
					break;
				}
				final int theirX = n.absX;
				final int theirY = n.absY;
				switch (player.rotation) {
				case 1: // north
					if (theirY > myY && theirX >= myX - 1 && theirX <= myX + 1) {
						npc = n;
					}
					break;
				case 2: // north-east
					if (theirX >= myX + 1 && theirY >= myY + 1) {
						npc = n;
					}
					break;
				case 3: // east
					if (theirX > myX && theirY >= myY - 1 && theirY <= myY + 1) {
						npc = n;
					}
					break;
				case 4: // south-east
					if (theirY <= myY - 1 && theirX >= myX + 1) {
						npc = n;
					}
					break;
				case 5: // south
					if (theirY < myY && theirX >= myX - 1 && theirX <= myX + 1) {
						npc = n;
					}
					break;
				case 6: // south-west
					if (theirX <= myX - 1 && theirY <= myY - 1) {
						npc = n;
					}
					break;
				case 7: // west
					if (theirX < myX && theirY >= myY - 1 && theirY <= myY + 1) {
						npc = n;
					}
					break;
				case 8: // north-west
					if (theirX <= myX - 1 && theirY >= myY + 1) {
						npc = n;
					}
					break;
				}
			}
		}
		return npc;
	}

	/**
	 * Play sounds
	 * 
	 * @param SOUNDID
	 *            : ID
	 * @param delay
	 *            : SOUND DELAY
	 */
	public void playSound(int SOUNDID, int delay) {
		if (Config.SOUND) {
			if (c.soundVolume <= -1) {
				return;
			}
			/**
			 * Deal with regions We dont need to play this again because you are
			 * in the current region
			 */
			if (c != null) {
				if (c.soundVolume >= 0) {
					if (c.goodDistance(c.absX, c.absY, this.absX, this.absY, 2)) {
						System.out.println(
								"Playing sound " + c.playerName + ", Id: " + SOUNDID + ", Vol: " + c.soundVolume);
						c.getOutStream().createFrame(174);
						c.getOutStream().writeWord(SOUNDID);
						c.getOutStream().writeByte(c.soundVolume);
						c.getOutStream().writeWord(/* delay */0);
					}
				}
			}

		}
	}

	public void sendString(final String s, final int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	public void sendSong(int id) {
		if (c.getOutStream() != null && c != null && id != -1) {
			c.getOutStream().createFrame(74);
			c.getOutStream().writeWordBigEndian(id);
		}
	}

	public void sendQuickSong(int id, int songDelay) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(121);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeWordBigEndian(songDelay);
			c.flushOutStream();
		}
	}

	public void sendColor(int id, int color) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(122);
			c.outStream.writeWordBigEndianA(id);
			c.outStream.writeWordBigEndianA(color);
		}
	}

	public void sendSound(int soundId) {
		if (soundId > 0 & c != null && c.outStream != null) {
			c.outStream.createFrame(174);
			c.outStream.writeWord(soundId);
			c.outStream.writeByte(100);
			c.outStream.writeWord(5);
		}
	}

	public void testInterface() {
		itemOnInterface(c, 6604, 0, 4151, 1);
		showInterface(c, 6600);
	}

	public void sound(int soundId) {
		if (soundId > 0 && c.outStream != null) {
			c.outStream.createFrame(174);
			c.outStream.writeWord(soundId);
			c.outStream.writeByte(100);
			c.outStream.writeWord(5);
		}
	}

	public void sendSound2(int i1, int i2, int i3) {
		c.outStream.createFrame(174);
		c.outStream.writeWord(i1); // id
		c.outStream.writeByte(i2); // volume, just set it to 100 unless you play
		// around with your client after this
		c.outStream.writeWord(i3); // delay
		Player.updateRequired = true;
		c.appearanceUpdateRequired = true;
		c.flushOutStream();
	}

	public void sendMusic(Client client, int soundId) {
		client.outStream.createFrame(74);
		client.outStream.writeWordBigEndian(soundId);
	}

	public int gfxId;

	public void handleBigfireWork(final Client c) {

		TaskHandler.submit(new Task(200, true) {

			@Override
			public void execute() {
				gfxId = 1634;
				c.gfx0(gfxId);
				if (gfxId == 1637 || c.disconnected) {
					this.cancel();
					return;
				}
				gfxId++;
				c.gfx0(gfxId);
			}

			@Override
			public void onCancel() {

			}

		});
	}

	public static void addClueReward(Client c, int clueLevel) {
		int chanceReward = Misc.random(10);
		if (clueLevel == 0) {
			switch (chanceReward) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				displayReward(c, lowLevelReward[Misc.random(16)], 1, lowLevelReward[Misc.random(16)], 1,
						lowLevelStacks[Misc.random(3)], 1 + Misc.random(150));
				c.easyClueCount++;
				c.sendMessage(
						"Congratulations! You've now completed " + c.easyClueCount + " @gre@easy@bla@ clue scrolls.");
				break;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				displayReward(c, lowLevelReward[Misc.random(16)], 1, lowLevelStacks[Misc.random(3)],
						1 + Misc.random(150), -1, 1);
				c.easyClueCount++;
				c.sendMessage(
						"Congratulations! You've now completed " + c.easyClueCount + " @gre@easy@bla@ clue scrolls.");
				break;
			case 10:
				displayReward(c, lowLevelRare[Misc.random(12)], 1, lowLevelReward[Misc.random(16)], 1, -1, 1);
				c.easyClueCount++;
				c.sendMessage(
						"Congratulations! You've now completed " + c.easyClueCount + " @gre@easy@bla@ clue scrolls.");
				break;
			}
		} else if (clueLevel == 1) {
			switch (chanceReward) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				displayReward(c, mediemLevelReward[Misc.random(61)], 1, mediemLevelReward[Misc.random(61)], 1,
						mediumLevelStacks[Misc.random(4)], 1 + Misc.random(200));
				c.mediumClueCount++;
				c.sendMessage("Congratulations! You've now completed " + c.mediumClueCount
						+ " @blu@medium@bla@ clue scrolls.");
				break;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				displayReward(c, mediemLevelReward[Misc.random(61)], 1, mediumLevelStacks[Misc.random(4)],
						1 + Misc.random(200), -1, 1);
				c.mediumClueCount++;
				c.sendMessage("Congratulations! You've now completed " + c.mediumClueCount
						+ " @blu@medium@bla@ clue scrolls.");
				break;
			case 10:
				displayReward(c, mediemLevelRare[Misc.random(21)], 1, mediemLevelReward[Misc.random(61)], 1, -1, 1);
				c.mediumClueCount++;
				c.sendMessage("Congratulations! You've now completed " + c.mediumClueCount
						+ "@blu@medium@bla@ clue scrolls.");
				break;
			}
		} else if (clueLevel == 2) {
			switch (chanceReward) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				displayReward(c, highLevelReward[Misc.random(72)], 1, highLevelReward[Misc.random(60)], 1,
						highLevelStacks[Misc.random(5)], 1 + Misc.random(350));
				c.hardClueCount++;
				c.sendMessage(
						"Congratulations! You've now completed " + c.hardClueCount + " @dre@hard@bla@ clue scrolls.");
				break;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				displayReward(c, highLevelReward[Misc.random(52)], 1, highLevelStacks[Misc.random(5)],
						1 + Misc.random(350), -1, 1);
				c.hardClueCount++;
				c.sendMessage(
						"Congratulations! You've now completed " + c.hardClueCount + " @dre@hard@bla@ clue scrolls.");
				break;
			case 10:
				displayReward(c, highLevelRare[Misc.random(24)], 1, highLevelReward[Misc.random(60)], 1, -1, 1);
				c.hardClueCount++;
				c.sendMessage(
						"Congratulations! You've now completed " + c.hardClueCount + " @dre@hard@bla@ clue scrolls.");
				break;
			}
		}
		if (c.hardClueCount == 50 || c.hardClueCount == 100 || c.hardClueCount == 200 || c.hardClueCount == 300) {
			c.getwM().serverMessage(
					c.playerName + " has completed " + c.hardClueCount + " @dre@hard@dbl@ clues! Nice job!");
		}
		if (c.mediumClueCount == 50 || c.mediumClueCount == 100 || c.mediumClueCount == 200
				|| c.mediumClueCount == 300) {
			c.getwM().serverMessage(
					c.playerName + " has completed " + c.mediumClueCount + " @blu@medium@dbl@ clues! Awesome!");
		}
		if (c.easyClueCount == 50 || c.easyClueCount == 100 || c.easyClueCount == 200 || c.easyClueCount == 300) {
			c.getwM()
					.serverMessage(c.playerName + " has completed " + c.easyClueCount + " @gre@easy@dbl@ clues! Cool!");
		}
	}

	public static void displayReward(Client c, int item, int amount, int item2, int amount2, int item3, int amount3) {
		int[] items = { item, item2, item3 };
		int[] amounts = { amount, amount2, amount3 };
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(6963);
		c.outStream.writeWord(items.length);
		for (int i = 0; i < items.length; i++) {
			if (c.playerItemsN[i] > 254) {
				c.outStream.writeByte(255);
				c.outStream.writeDWord_v2(amounts[i]);
			} else {
				c.outStream.writeByte(amounts[i]);
			}
			if (items[i] > 0) {
				c.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
		c.getItems().addItem(item, amount);
		c.getItems().addItem(item2, amount2);
		c.getItems().addItem(item3, amount3);
		c.getPA().showInterface(6960);
	}

	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		c.getItems().resetItems(3214);
	}

	public void setConfig(int id, int state) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(36);
				c.getOutStream().writeWordBigEndian(id);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}
		}
	}

	public double getAgilityRunRestore(Client c) {
		if (c.playerEquipment[c.playerCape] == 11852 && c.playerEquipment[c.playerHat] == 11850 && c.playerEquipment[c.playerChest] == 11854 && c.playerEquipment[c.playerLegs] == 11856
				 && c.playerEquipment[c.playerFeet] == 11860 && c.playerEquipment[c.playerHands] == 11858) {
			return 2260 - (c.playerLevel[16] * 20);
		} 
		else if (c.playerEquipment[c.playerCape] == 11852 || c.playerEquipment[c.playerHat] == 11850 || c.playerEquipment[c.playerChest] == 11854 || c.playerEquipment[c.playerLegs] == 11856
				 || c.playerEquipment[c.playerFeet] == 11860 || c.playerEquipment[c.playerHands] == 11858) {
			return 2260 - (c.playerLevel[16] * 12);
		} else if (c.getRights().equals(Rights.DONATOR)) {
			return 2260 - (c.playerLevel[16] * 11);
		} else if (c.getRights().equals(Rights.SUPER_DONATOR)) {
			return 2260 - (c.playerLevel[16] * 12);
		} else if (c.getRights().equals(Rights.EXTREME_DONATOR)) {
			return 2260 - (c.playerLevel[16] * 13);
		} else if (c.getRights().equals(Rights.GOD_OF_ALL_DONATORS)) {
			return 2260 - (c.playerLevel[16] * 14);
		} else {
		return 2260 - (c.playerLevel[16] * 10);
	}
	}

	public String getTotalAmount(Client c, int j) {
		if (j >= 10000 && j < 10000000) {
			return j / 1000 + "K";
		} else if (j >= 10000000 && j <= 2147483647) {
			return j / 1000000 + "M";
		} else {
			return "" + j + " gp";
		}
	}

	public Client getClient(String playerName) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.playerName.equalsIgnoreCase(playerName)) {
					return (Client) p;
				}
			}
		}
		return null;
	}

	public static void itemOnInterface(final Client c, final int frame, final int slot, final int id,
			final int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(id + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public void yell(String string) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage(string);
			}
		}
	}

	public void normYell(String Message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendClan(Misc.optimizeText(c.playerName), Message, "Mod Sunny", c.getRights().getProtocolValue());
			}
		}
	}

	public void admYell(String Message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendClan(Misc.optimizeText(c.playerName), Message, "Developer", c.getRights().getProtocolValue());
			}
		}
	}

	public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(interfaceChild);
			c.getOutStream().writeWord(zoom);
			c.getOutStream().writeWord(itemId);
			c.flushOutStream();
		}
	}

	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	/**
	 * If the player is using melee and is standing diagonal from the opponent,
	 * then move towards opponent.
	 */

	public void movePlayerDiagonal(int i) {
		Client c2 = (Client) PlayerHandler.players[i];
		boolean hasMoved = false;
		int c2X = c2.getX();
		int c2Y = c2.getY();
		if (c.goodDistance(c2X, c2Y, c.getX(), c.getY(), 1)) {
			if (c.getX() != c2.getX() && c.getY() != c2.getY()) {
				if (c.getX() > c2.getX() && !hasMoved) {
					if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
						hasMoved = true;
						walkTo(-1, 0);
					}
				} else if (c.getX() < c2.getX() && !hasMoved) {
					if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
						hasMoved = true;
						walkTo(1, 0);
					}
				}

				if (c.getY() > c2.getY() && !hasMoved) {
					if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
						hasMoved = true;
						walkTo(0, -1);
					}
				} else if (c.getY() < c2.getY() && !hasMoved) {
					if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
						hasMoved = true;
						walkTo(0, 1);
					}
				}
			}
		}
		hasMoved = false;
	}

	public Clan getClan() {
		if (Server.clanManager.clanExists(c.playerName)) {
			return Server.clanManager.getClan(c.playerName);
		}
		return null;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		if (rights >= 3)
			rights--;
		c.outStream.createFrameVarSizeWord(217);
		c.outStream.writeString(name);
		c.outStream.writeString(Misc.formatPlayerName(message));
		c.outStream.writeString(clan);
		c.outStream.writeWord(rights);
		c.outStream.endFrameVarSize();
	}

	public void clearClanChat() {
		c.clanId = -1;
		c.getPA().sendString("Talking in: ", 18139);
		c.getPA().sendString("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			c.getPA().sendString("", j);
		}
	}

	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(c.playerName);
		if (!exists || c.clan == null) {
			sendString("Join chat", 18135);
			sendString("Talking in: Not in chat", 18139);
			sendString("Owner: None", 18140);
		}
		if (!exists) {
			sendString("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				sendString(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				sendString("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				sendString("", 18424 + index);
			}
			return;
		}
		Clan clan = Server.clanManager.getClan(c.playerName);
		sendString(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			sendString(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					sendString(clan.rankedMembers.get(index), 18323 + index);
				} else {
					sendString("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					sendString(clan.bannedMembers.get(index), 18424 + index);
				} else {
					sendString("", 18424 + index);
				}
			}
		}
	}

	public void resetAutocast() {
		c.autocastId = 0;
		c.autocasting = false;
		c.getPA().sendFrame36(108, 0);
	}

	public int getItemSlot(int itemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == itemID) {
				return i;
			}
		}
		return -1;
	}

	public boolean isItemInBag(int itemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == itemID) {
				return true;
			}
		}
		return false;
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void turnTo(int pointX, int pointY) {
		c.focusPointX = 2 * pointX + 1;
		c.focusPointY = 2 * pointY + 1;
		Player.updateRequired = true;
	}

	public void movePlayer(int x, int y, int h) {
		if (c.inNMZ())
			;
		walkableInterface(-1);
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		requestUpdates();

	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int absX, absY;
	public int heightLevel;

	public static void QuestReward(Client c, String questName, int PointsGain, String Line1, String Line2, String Line3,
			String Line4, String Line5, String Line6, int itemID) {
		c.getPA().sendFrame126("You have completed " + questName + "!", 12144);
		sendQuest(c, "" + (c.QuestPoints), 12147);
		// c.QuestPoints += PointsGain;
		sendQuest(c, Line1, 12150);
		sendQuest(c, Line2, 12151);
		sendQuest(c, Line3, 12152);
		sendQuest(c, Line4, 12153);
		sendQuest(c, Line5, 12154);
		sendQuest(c, Line6, 12155);
		c.getPlayerAssistant().sendFrame246(12145, 250, itemID);
		showInterface(c, 12140);
		Server.getStillGraphicsManager().stillGraphics(c, c.getX(), c.getY(), c.getHeightLevel(), 199, 0);
	}

	public static void showInterface(Client client, int i) {
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(i);
		client.flushOutStream();
	}

	public static void sendQuest(Client client, String s, int i) {
		client.getOutStream().createFrameVarSizeWord(126);
		client.getOutStream().writeString(s);
		client.getOutStream().writeWordA(i);
		client.getOutStream().endFrameVarSizeWord();
		client.flushOutStream();
	}

	public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(y - (c.mapRegionY * 8));
		c.getOutStream().writeByteC(x - (c.mapRegionX * 8));
		c.getOutStream().createFrame(4);
		c.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
		// //Tiles away from
		// absX and absY.
		c.getOutStream().writeWord(id); // Graphic ID.
		c.getOutStream().writeByte(heightS); // Height of the graphic when
		// cast.
		c.getOutStream().writeWord(timeBCS); // Time before the graphic
		// plays.
		c.flushOutStream();
	}

	public void createArrow(int type, int id) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(type); // 1=NPC, 10=Player
			c.getOutStream().writeWord(id); // NPC/Player ID
			c.getOutStream().write3Byte(0); // Junk
		}
	}

	public void createArrow(int x, int y, int height, int pos) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(pos); // Position on Square(2 = middle, 3
			// = west, 4 = east, 5 = south,
			// 6 = north)
			c.getOutStream().writeWord(x); // X-Coord of Object
			c.getOutStream().writeWord(y); // Y-Coord of Object
			c.getOutStream().writeByte(height); // Height off Ground
		}
	}

	public void sendQuest(String s, int i) {
		c.getOutStream().createFrameVarSizeWord(126);
		c.getOutStream().writeString(s);
		c.getOutStream().writeWordA(i);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	/**
	 * Custom Quest Tab By: Sunny++
	 */
	public void rights() {
		if (c.getRights().equal(Rights.DONATOR)) {
			c.sendMessage("You are now a donator and you've donated $" + c.donPoints + ".");
		} else if (c.getRights().equal(Rights.SUPER_DONATOR)) {
			c.sendMessage("You are now a super donator and you've donated $" + c.donPoints + ".");
		} else if (c.getRights().equal(Rights.EXTREME_DONATOR)) {
			c.sendMessage("You are now an extreme donator and you've donated $" + c.donPoints + ".");
		} else if (c.getRights().equal(Rights.GOD_OF_ALL_DONATORS)) {
			c.sendMessage("You are now a legendary donator and you've donated $" + c.donPoints + ".");
		} else {
			c.sendMessage("How'd you get this message?");
		}
	}

	public void loadQuests() {
		sendFrame126(Config.SERVER_NAME, 29155);
		sendFrame126("", 682); // Member's Quests
		sendFrame126("@or2@Uptime: @gre@ ", 29162);
		sendFrame126("@or2@Players Online: @whi@" + PlayerHandler.getPlayerCount(), 29163);
		sendFrame126("@or2@Username: @whi@" + c.playerName + " ", 29164);
		sendFrame126("@or2@Rank: @whi@" + playerRanks(), 29165);
		sendFrame126("@or2@Donator Status: @whi@" + c.donPoints + " ", 29166);
		sendFrame126("@or2@Pk Points: @whi@" + c.pkp + " ", 29168);
		sendFrame126("@or2@Kills: @whi@" + c.KC + " ", 29169);
		sendFrame126("@or2@Deaths: @whi@" + c.DC + " ", 29170);
		if (c.KC == 0 && c.DC == 0) {
			sendFrame126("@or2@KDR:@red@ " + c.KC + "@or1@/@red@" + c.DC, 29171);
		} else {
			sendFrame126("@or2@KDR:@gre@ " + c.KC + "@yel@/@gre@" + c.DC, 29171);
		}
		if (c.slayerTask <= 0) {
			c.getPA().sendFrame126("@or2@Task: @red@Empty ", 29172);
		} else {
			c.getPA().sendFrame126(
					"@or2@Task: @gre@" + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + " ",
					29172);
		}
		sendFrame126("@or2@Donator Points: @whi@" + c.donPoints + " ", 29175); // Prince
		// Ali
		// Rescue
		sendFrame126("@or2@Time Played: @whi@ " + c.getTimePlayed().formatPlayersTime(), 29176); // The
																									// Restless
		// Ghost
		sendFrame126("", 29175); // Romeo & Juliet
		sendFrame126("", 29175); // Rune Mysteries Quest
	}

	public void sendFrame126(String s, int id) {
		if (!c.checkPacket126Update(s, id)) {
			// int bytesSaved = (s.length() + 4);
			// Misc.println("Bytes Saved " + bytesSaved);
			return;
		}
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	public void sendLink(String s) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}

	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
		// }
	}

	public void sendFrame106(int sideIcon) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		// synchronized(c) {
		if (id == c.getActiveWalkable()) {
			return;
		}
		c.setActiveWalkable(id);
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}

	}

	public void sendFrame185(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void showInterface(int interfaceid) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();

		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();

		}
	}

	public void sendFrame246(int MainFrame, int zoom, int itemID) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(zoom);
			c.getOutStream().writeWord(itemID);
			c.flushOutStream();

		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();

		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}

	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}

	}

	public void sendFrame164(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}

	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}

	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}

	}

	public void sendFrame87(int id, int state) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}

	}

	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSize(196);
			c.getOutStream().writeQWord(name);
			c.getOutStream().writeDWord(c.lastChatId++);
			c.getOutStream().writeByte(rights);
			c.getOutStream().writeBytes(chatmessage, messagesize, 0);
			c.getOutStream().endFrameVarSize();
			c.flushOutStream();
			@SuppressWarnings("unused")
			String chatmessagegot = Misc.textUnpack(chatmessage, messagesize);
			@SuppressWarnings("unused")
			String target = Misc.longToPlayerName(name);
		}

	}

	public void createPlayerHints(int type, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}

	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}

	}

	public void loadPM(long playerName, int world) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (world != 0) {
				world += 9;
			} else if (!Config.WORLD_LIST_FIX) {
				world += 1;
			}
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(playerName);
			c.getOutStream().writeByte(world);
			c.flushOutStream();
		}

	}

	public void removeAllWindows() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getPA().resetVariables();
			c.getOutStream().createFrame(219);
			c.flushOutStream();
		}

	}

	public void closeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.isBanking = false;
			c.getTrade().declineTrade();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34); // init item to smith screen
			c.outStream.writeWord(column); // Column Across Smith Screen
			c.outStream.writeByte(4); // Total Rows?
			c.outStream.writeDWord(slot); // Row Down The Smith Screen
			c.outStream.writeWord(id + 1); // item
			c.outStream.writeByte(amount); // how many there are?
			c.outStream.endFrameVarSizeWord();
		}

	}

	public void walkableInterface(int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}

	}

	public int mapStatus = 0;

	public void sendFrame71(int a, int b) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(71);
			c.outStream.writeWord(a);
			c.outStream.writeByteA(b);
		}
	}

	// }

	public void sendFrame99(int state) { // used for disabling map
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (mapStatus != state) {
				mapStatus = state;
				c.getOutStream().createFrame(99);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}

		}
	}

	public void sendCrashFrame() { // used for crashing cheat clients
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(123);
				c.flushOutStream();
			}
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client person = (Client) PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}

			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();

		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}

	}

	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight,
			int endHeight, int speed, int AtkIndex) {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client p = (Client) PlayerHandler.players[i];
				if (p.WithinDistance(c.absX, c.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == c.heightLevel) {
						if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeWord(AtkIndex);
							p.outStream.writeWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeWord(51);
							p.outStream.writeWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == c.heightLevel)
								person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
										endHeight, lockon, time);
						}
					}
				}

			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}

		}
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeWord(time);
			c.flushOutStream();
		}

	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}

		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60)
			return;
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	public void showOption(int i, int l, String s, int a) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}

		}
	}

	/**
	 * Open bank
	 **/
	public void openUpBank() {
		// synchronized(c) {
		if (c.requestPinDelete) {
			if (c.enterdBankpin) {
				c.requestPinDelete = false;
				c.sendMessage("[Notice] Your PIN pending deletion has been cancelled");
			} else if (c.lastLoginDate >= c.pinDeleteDateRequested && c.hasBankpin) {
				c.hasBankpin = false;
				c.requestPinDelete = false;
				c.sendMessage("[Notice] Your PIN has been deleted. It is recommended " + "to have one.");
			}
		}
		if (!c.enterdBankpin && c.hasBankpin) {
			c.getBankPin().openPin();
			return;
		} else if (!c.enterdBankpin && !c.hasBankPin) {
			// c.getDH().sendDialogues(1013, 494);
			// c.sendMessage("You do not have a bankpin set, you may want to set
			// one.");
		}
		if (c.inTrade || c.tradeStatus > 0) {
			Client o = (Client) PlayerHandler.players[c.tradeWith];
			if (o != null) {
				o.getTrade().declineTrade();
			}
		}
		if (c.duelStatus > 0) {
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o != null) {
				o.getDuel().resetDuel();
			}
		}
		if (c.getOutStream() != null && c != null) {
			c.getItems().resetItems(5064);
			c.getItems().rearrangeBank();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(5292); // Interface for the bank
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
			c.isBanking = true;
		}
		// }
	}

	/**
	 * Private Messaging
	 **/
	public void logIntoPM() {
		setPrivateMessaging(2);
		for (int i1 = 0; i1 < Config.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Client o = (Client) p;
				if (o != null) {
					o.getPA().updatePM(c.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				for (int i2 = 1; i2 < Config.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == c.friends[i]) {
						Client o = (Client) p;
						if (o != null) {
							if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR) || p.privateChat == 0
									|| (p.privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
								loadPM(c.friends[i], 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(c.friends[i], 0);
				}
				pmLoaded = false;
			}
			for (int i1 = 1; i1 < Config.MAX_PLAYERS; i1++) {
				Player p = PlayerHandler.players[i1];
				if (p != null && p.isActive) {
					Client o = (Client) p;
					if (o != null) {
						o.getPA().updatePM(c.playerId, 1);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Client o = (Client) p;
		if (o == null) {
			return;
		}
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.privateChat == 0) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privateChat == 1) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						if (o.getPA().isInPM(Misc.playerNameToInt64(c.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privateChat == 2) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i] && c.getRights().less(Rights.ADMINISTRATOR)) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public boolean isInPM(long l) {
		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				if (l == c.friends[i]) {
					return true;
				}
			}
		}
		return false;
	}

	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		c.getItems();
		c.attackTimer = c.getCombat()
				.getAttackDelay(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		if (c.duelRule[5]) {
			c.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!c.isDead && System.currentTimeMillis() - c.foodDelay > 2000) {
			if (c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.getItems();
				c.sendMessage("You drink the " + ItemAssistant.getItemName(itemId).toLowerCase() + ".");
				c.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/

	public void magicOnItems(int slot, int itemId, int spellId) {

		switch (spellId) {
		case 1162: // low alch
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (!c.getItems().playerHasItem(itemId, 1, slot) || itemId == 995) {
					return;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getShops();
				c.getItems().addItem(995, ShopAssistant.getItemShopValue(itemId) / 3);
				c.startAnimation(c.MAGIC_SPELLS[49][2]);
				c.gfx100(c.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[49][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (!c.getItems().playerHasItem(itemId, 1, slot) || itemId == 995) {
					return;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getShops();
				c.getItems().addItem(995, (int) (ShopAssistant.getItemShopValue(itemId) * .75));
				c.startAnimation(c.MAGIC_SPELLS[50][2]);
				c.gfx100(c.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[50][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
			}
			break;
		case 1155: //Lvl-1 enchant sapphire
		case 1165: //Lvl-2 enchant emerald
		case 1176: //Lvl-3 enchant ruby
		case 1180: //Lvl-4 enchant diamond
		case 1187: //Lvl-5 enchant dragonstone
		case 6003: //Lvl-6 enchant onyx
			c.getMagic().enchantItem(itemId, spellId);
			break;
		//case 1155:
		//	Obelisks(itemId);
		//	break;
		}
	}

	/**
	 * Dieing
	 **/

	public void applyDead() {
		// int i;
		c.isFullHelm = Item.isFullHelm(c.playerEquipment[c.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
		c.getDuel().stakedItems.clear();
		c.getPA().requestUpdates();
		c.respawnTimer = 15;
		c.isDead = false;
		c.freezeTimer = 60;
		c.DC = (c.DC + 1);
		if (c.getRights().equal(Rights.DONATOR)) {
			if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
				c.playerXP[0] -= c.playerXP[0] * 0.2; 
			if (c.combatProtected1 != 1 && c.combatProtected2 != 1)
				c.playerXP[1] -= c.playerXP[1] * 0.2; 
			if (c.combatProtected1 != 2 && c.combatProtected2 != 2)
				c.playerXP[2] -= c.playerXP[2] * 0.2;
			if (c.combatProtected1 != 3 && c.combatProtected2 != 3 && c.hpInsurance != 1)
				c.playerXP[3] -= c.playerXP[3] * 0.2; 
			if (c.combatProtected1 != 4 && c.combatProtected2 != 4)
				c.playerXP[4] -= c.playerXP[4] * 0.2; 
			if (c.combatProtected1 != 5 && c.combatProtected2 != 5)
				c.playerXP[5] -= c.playerXP[5] * 0.2; 
			if (c.combatProtected1 != 6 && c.combatProtected2 != 6)
				c.playerXP[6] -= c.playerXP[6] * 0.2;
			if (c.skillProtected1 != 7 && c.skillProtected2 != 7 && c.skillProtected3 != 7)
				c.playerXP[7] -= c.playerXP[7] * 0.2;
			if (c.skillProtected1 != 8 && c.skillProtected2 != 8 && c.skillProtected3 != 8)
				c.playerXP[8] -= c.playerXP[8] * 0.2;
			if (c.skillProtected1 != 9 && c.skillProtected2 != 9 && c.skillProtected3 != 9)
				c.playerXP[9] -= c.playerXP[9] * 0.2;
			if (c.skillProtected1 != 10 && c.skillProtected2 != 10 && c.skillProtected3 != 10)
				c.playerXP[10] -= c.playerXP[10] * 0.2;
			if (c.skillProtected1 != 11 && c.skillProtected2 != 11 && c.skillProtected3 != 11)
				c.playerXP[11] -= c.playerXP[11] * 0.2;
			if (c.skillProtected1 != 12 && c.skillProtected2 != 12 && c.skillProtected3 != 12)
				c.playerXP[12] -= c.playerXP[12] * 0.2;
			if (c.skillProtected1 != 13 && c.skillProtected2 != 13 && c.skillProtected3 != 13)
				c.playerXP[13] -= c.playerXP[13] * 0.2;
			if (c.skillProtected1 != 14 && c.skillProtected2 != 14 && c.skillProtected3 != 14)
				c.playerXP[14] -= c.playerXP[14] * 0.2;
			if (c.skillProtected1 != 15 && c.skillProtected2 != 15 && c.skillProtected3 != 15)
				c.playerXP[15] -= c.playerXP[15] * 0.2;
			if (c.skillProtected1 != 16 && c.skillProtected2 != 16 && c.skillProtected3 != 16)
				c.playerXP[16] -= c.playerXP[16] * 0.2;
			if (c.skillProtected1 != 17 && c.skillProtected2 != 17 && c.skillProtected3 != 17)
				c.playerXP[17] -= c.playerXP[17] * 0.2;
			if (c.skillProtected1 != 18 && c.skillProtected2 != 18 && c.skillProtected3 != 18)
				c.playerXP[18] -= c.playerXP[18] * 0.2;
			if (c.skillProtected1 != 19 && c.skillProtected2 != 19 && c.skillProtected3 != 19)
				c.playerXP[19] -= c.playerXP[19] * 0.2;
			if (c.skillProtected1 != 20 && c.skillProtected2 != 20 && c.skillProtected3 != 20)
				c.playerXP[20] -= c.playerXP[20] * 0.2;
		} else if (c.getRights().equal(Rights.SUPER_DONATOR)) {
			if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
				if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
					c.playerXP[0] -= c.playerXP[0] * 0.15; 
				if (c.combatProtected1 != 1 && c.combatProtected2 != 1)
					c.playerXP[1] -= c.playerXP[1] * 0.15; 
				if (c.combatProtected1 != 2 && c.combatProtected2 != 2)
					c.playerXP[2] -= c.playerXP[2] * 0.15;
				if (c.combatProtected1 != 3 && c.combatProtected2 != 3 && c.hpInsurance != 1)
					c.playerXP[3] -= c.playerXP[3] * 0.15; 
				if (c.combatProtected1 != 4 && c.combatProtected2 != 4)
					c.playerXP[4] -= c.playerXP[4] * 0.15; 
				if (c.combatProtected1 != 5 && c.combatProtected2 != 5)
					c.playerXP[5] -= c.playerXP[5] * 0.15; 
				if (c.combatProtected1 != 6 && c.combatProtected2 != 6)
					c.playerXP[6] -= c.playerXP[6] * 0.15;
				if (c.skillProtected1 != 7 && c.skillProtected2 != 7 && c.skillProtected3 != 7)
					c.playerXP[7] -= c.playerXP[7] * 0.15;
				if (c.skillProtected1 != 8 && c.skillProtected2 != 8 && c.skillProtected3 != 8)
					c.playerXP[8] -= c.playerXP[8] * 0.15;
				if (c.skillProtected1 != 9 && c.skillProtected2 != 9 && c.skillProtected3 != 9)
					c.playerXP[9] -= c.playerXP[9] * 0.15;
				if (c.skillProtected1 != 10 && c.skillProtected2 != 10 && c.skillProtected3 != 10)
					c.playerXP[10] -= c.playerXP[10] * 0.15;
				if (c.skillProtected1 != 11 && c.skillProtected2 != 11 && c.skillProtected3 != 11)
					c.playerXP[11] -= c.playerXP[11] * 0.15;
				if (c.skillProtected1 != 12 && c.skillProtected2 != 12 && c.skillProtected3 != 12)
					c.playerXP[12] -= c.playerXP[12] * 0.15;
				if (c.skillProtected1 != 13 && c.skillProtected2 != 13 && c.skillProtected3 != 13)
					c.playerXP[13] -= c.playerXP[13] * 0.15;
				if (c.skillProtected1 != 14 && c.skillProtected2 != 14 && c.skillProtected3 != 14)
					c.playerXP[14] -= c.playerXP[14] * 0.15;
				if (c.skillProtected1 != 15 && c.skillProtected2 != 15 && c.skillProtected3 != 15)
					c.playerXP[15] -= c.playerXP[15] * 0.15;
				if (c.skillProtected1 != 16 && c.skillProtected2 != 16 && c.skillProtected3 != 16)
					c.playerXP[16] -= c.playerXP[16] * 0.15;
				if (c.skillProtected1 != 17 && c.skillProtected2 != 17 && c.skillProtected3 != 17)
					c.playerXP[17] -= c.playerXP[17] * 0.15;
				if (c.skillProtected1 != 18 && c.skillProtected2 != 18 && c.skillProtected3 != 18)
					c.playerXP[18] -= c.playerXP[18] * 0.15;
				if (c.skillProtected1 != 19 && c.skillProtected2 != 19 && c.skillProtected3 != 19)
					c.playerXP[19] -= c.playerXP[19] * 0.15;
				if (c.skillProtected1 != 20 && c.skillProtected2 != 20 && c.skillProtected3 != 20)
					c.playerXP[20] -= c.playerXP[20] * 0.15;
		} else if (c.getRights().equal(Rights.EXTREME_DONATOR)) {
			if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
				if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
					c.playerXP[0] -= c.playerXP[0] * 0.1; 
				if (c.combatProtected1 != 1 && c.combatProtected2 != 1)
					c.playerXP[1] -= c.playerXP[1] * 0.1; 
				if (c.combatProtected1 != 2 && c.combatProtected2 != 2)
					c.playerXP[2] -= c.playerXP[2] * 0.1;
				if (c.combatProtected1 != 3 && c.combatProtected2 != 3 && c.hpInsurance != 1)
					c.playerXP[3] -= c.playerXP[3] * 0.1; 
				if (c.combatProtected1 != 4 && c.combatProtected2 != 4)
					c.playerXP[4] -= c.playerXP[4] * 0.1; 
				if (c.combatProtected1 != 5 && c.combatProtected2 != 5)
					c.playerXP[5] -= c.playerXP[5] * 0.1; 
				if (c.combatProtected1 != 6 && c.combatProtected2 != 6)
					c.playerXP[6] -= c.playerXP[6] * 0.1;
				if (c.skillProtected1 != 7 && c.skillProtected2 != 7 && c.skillProtected3 != 7)
					c.playerXP[7] -= c.playerXP[7] * 0.1;
				if (c.skillProtected1 != 8 && c.skillProtected2 != 8 && c.skillProtected3 != 8)
					c.playerXP[8] -= c.playerXP[8] * 0.1;
				if (c.skillProtected1 != 9 && c.skillProtected2 != 9 && c.skillProtected3 != 9)
					c.playerXP[9] -= c.playerXP[9] * 0.1;
				if (c.skillProtected1 != 10 && c.skillProtected2 != 10 && c.skillProtected3 != 10)
					c.playerXP[10] -= c.playerXP[10] * 0.1;
				if (c.skillProtected1 != 11 && c.skillProtected2 != 11 && c.skillProtected3 != 11)
					c.playerXP[11] -= c.playerXP[11] * 0.1;
				if (c.skillProtected1 != 12 && c.skillProtected2 != 12 && c.skillProtected3 != 12)
					c.playerXP[12] -= c.playerXP[12] * 0.1;
				if (c.skillProtected1 != 13 && c.skillProtected2 != 13 && c.skillProtected3 != 13)
					c.playerXP[13] -= c.playerXP[13] * 0.1;
				if (c.skillProtected1 != 14 && c.skillProtected2 != 14 && c.skillProtected3 != 14)
					c.playerXP[14] -= c.playerXP[14] * 0.1;
				if (c.skillProtected1 != 15 && c.skillProtected2 != 15 && c.skillProtected3 != 15)
					c.playerXP[15] -= c.playerXP[15] * 0.1;
				if (c.skillProtected1 != 16 && c.skillProtected2 != 16 && c.skillProtected3 != 16)
					c.playerXP[16] -= c.playerXP[16] * 0.1;
				if (c.skillProtected1 != 17 && c.skillProtected2 != 17 && c.skillProtected3 != 17)
					c.playerXP[17] -= c.playerXP[17] * 0.1;
				if (c.skillProtected1 != 18 && c.skillProtected2 != 18 && c.skillProtected3 != 18)
					c.playerXP[18] -= c.playerXP[18] * 0.1;
				if (c.skillProtected1 != 19 && c.skillProtected2 != 19 && c.skillProtected3 != 19)
					c.playerXP[19] -= c.playerXP[19] * 0.1;
				if (c.skillProtected1 != 20 && c.skillProtected2 != 20 && c.skillProtected3 != 20)
					c.playerXP[20] -= c.playerXP[20] * 0.1;
		} else if (c.getRights().equal(Rights.GOD_OF_ALL_DONATORS)) {
			if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
				if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
					c.playerXP[0] -= c.playerXP[0] * 0.05; 
				if (c.combatProtected1 != 1 && c.combatProtected2 != 1)
					c.playerXP[1] -= c.playerXP[1] * 0.05; 
				if (c.combatProtected1 != 2 && c.combatProtected2 != 2)
					c.playerXP[2] -= c.playerXP[2] * 0.05;
				if (c.combatProtected1 != 3 && c.combatProtected2 != 3 && c.hpInsurance != 1)
					c.playerXP[3] -= c.playerXP[3] * 0.05; 
				if (c.combatProtected1 != 4 && c.combatProtected2 != 4)
					c.playerXP[4] -= c.playerXP[4] * 0.05; 
				if (c.combatProtected1 != 5 && c.combatProtected2 != 5)
					c.playerXP[5] -= c.playerXP[5] * 0.05; 
				if (c.combatProtected1 != 6 && c.combatProtected2 != 6)
					c.playerXP[6] -= c.playerXP[6] * 0.05;
				if (c.skillProtected1 != 7 && c.skillProtected2 != 7 && c.skillProtected3 != 7)
					c.playerXP[7] -= c.playerXP[7] * 0.05;
				if (c.skillProtected1 != 8 && c.skillProtected2 != 8 && c.skillProtected3 != 8)
					c.playerXP[8] -= c.playerXP[8] * 0.05;
				if (c.skillProtected1 != 9 && c.skillProtected2 != 9 && c.skillProtected3 != 9)
					c.playerXP[9] -= c.playerXP[9] * 0.05;
				if (c.skillProtected1 != 10 && c.skillProtected2 != 10 && c.skillProtected3 != 10)
					c.playerXP[10] -= c.playerXP[10] * 0.05;
				if (c.skillProtected1 != 11 && c.skillProtected2 != 11 && c.skillProtected3 != 11)
					c.playerXP[11] -= c.playerXP[11] * 0.05;
				if (c.skillProtected1 != 12 && c.skillProtected2 != 12 && c.skillProtected3 != 12)
					c.playerXP[12] -= c.playerXP[12] * 0.05;
				if (c.skillProtected1 != 13 && c.skillProtected2 != 13 && c.skillProtected3 != 13)
					c.playerXP[13] -= c.playerXP[13] * 0.05;
				if (c.skillProtected1 != 14 && c.skillProtected2 != 14 && c.skillProtected3 != 14)
					c.playerXP[14] -= c.playerXP[14] * 0.05;
				if (c.skillProtected1 != 15 && c.skillProtected2 != 15 && c.skillProtected3 != 15)
					c.playerXP[15] -= c.playerXP[15] * 0.05;
				if (c.skillProtected1 != 16 && c.skillProtected2 != 16 && c.skillProtected3 != 16)
					c.playerXP[16] -= c.playerXP[16] * 0.05;
				if (c.skillProtected1 != 17 && c.skillProtected2 != 17 && c.skillProtected3 != 17)
					c.playerXP[17] -= c.playerXP[17] * 0.05;
				if (c.skillProtected1 != 18 && c.skillProtected2 != 18 && c.skillProtected3 != 18)
					c.playerXP[18] -= c.playerXP[18] * 0.05;
				if (c.skillProtected1 != 19 && c.skillProtected2 != 19 && c.skillProtected3 != 19)
					c.playerXP[19] -= c.playerXP[19] * 0.05;
				if (c.skillProtected1 != 20 && c.skillProtected2 != 20 && c.skillProtected3 != 20)
					c.playerXP[20] -= c.playerXP[20] * 0.05;
		} else if (c.getRights().greaterOrEqual(Rights.DEVELOPER)) {
			c.sendMessage("You don't lose XP because you're a developer!");
		} else {
			if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
				if (c.combatProtected1 != 0 && c.combatProtected2 != 0)
					c.playerXP[0] -= c.playerXP[0] * 0.25; 
				if (c.combatProtected1 != 1 && c.combatProtected2 != 1)
					c.playerXP[1] -= c.playerXP[1] * 0.25; 
				if (c.combatProtected1 != 2 && c.combatProtected2 != 2)
					c.playerXP[2] -= c.playerXP[2] * 0.25;
				if (c.combatProtected1 != 3 && c.combatProtected2 != 3 && c.hpInsurance != 1)
					c.playerXP[3] -= c.playerXP[3] * 0.25; 
				if (c.combatProtected1 != 4 && c.combatProtected2 != 4)
					c.playerXP[4] -= c.playerXP[4] * 0.25; 
				if (c.combatProtected1 != 5 && c.combatProtected2 != 5)
					c.playerXP[5] -= c.playerXP[5] * 0.25; 
				if (c.combatProtected1 != 6 && c.combatProtected2 != 6)
					c.playerXP[6] -= c.playerXP[6] * 0.25;
				if (c.skillProtected1 != 7 && c.skillProtected2 != 7 && c.skillProtected3 != 7)
					c.playerXP[7] -= c.playerXP[7] * 0.25;
				if (c.skillProtected1 != 8 && c.skillProtected2 != 8 && c.skillProtected3 != 8)
					c.playerXP[8] -= c.playerXP[8] * 0.25;
				if (c.skillProtected1 != 9 && c.skillProtected2 != 9 && c.skillProtected3 != 9)
					c.playerXP[9] -= c.playerXP[9] * 0.25;
				if (c.skillProtected1 != 10 && c.skillProtected2 != 10 && c.skillProtected3 != 10)
					c.playerXP[10] -= c.playerXP[10] * 0.25;
				if (c.skillProtected1 != 11 && c.skillProtected2 != 11 && c.skillProtected3 != 11)
					c.playerXP[11] -= c.playerXP[11] * 0.25;
				if (c.skillProtected1 != 12 && c.skillProtected2 != 12 && c.skillProtected3 != 12)
					c.playerXP[12] -= c.playerXP[12] * 0.25;
				if (c.skillProtected1 != 13 && c.skillProtected2 != 13 && c.skillProtected3 != 13)
					c.playerXP[13] -= c.playerXP[13] * 0.25;
				if (c.skillProtected1 != 14 && c.skillProtected2 != 14 && c.skillProtected3 != 14)
					c.playerXP[14] -= c.playerXP[14] * 0.25;
				if (c.skillProtected1 != 15 && c.skillProtected2 != 15 && c.skillProtected3 != 15)
					c.playerXP[15] -= c.playerXP[15] * 0.25;
				if (c.skillProtected1 != 16 && c.skillProtected2 != 16 && c.skillProtected3 != 16)
					c.playerXP[16] -= c.playerXP[16] * 0.25;
				if (c.skillProtected1 != 17 && c.skillProtected2 != 17 && c.skillProtected3 != 17)
					c.playerXP[17] -= c.playerXP[17] * 0.25;
				if (c.skillProtected1 != 18 && c.skillProtected2 != 18 && c.skillProtected3 != 18)
					c.playerXP[18] -= c.playerXP[18] * 0.25;
				if (c.skillProtected1 != 19 && c.skillProtected2 != 19 && c.skillProtected3 != 19)
					c.playerXP[19] -= c.playerXP[19] * 0.25;
				if (c.skillProtected1 != 20 && c.skillProtected2 != 20 && c.skillProtected3 != 20)
					c.playerXP[20] -= c.playerXP[20] * 0.25;
		}
		if (c.guardSpawned == true) { // try that
			Guard.destructGuards(c);
			c.guardSpawned = false;
		}
		if (c.inFightCaves()) {
			c.getPA().resetTzhaar();
		}
		if (Config.ADMIN_DROP_ITEMS) {
			return;
		}

	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[c.playerId] = 0;
			}
		}
	}

	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;
	}

	public void handleStatus(int i, int i2, int i3) {
		if (i == 1)
			c.getItems().addItem(i2, i3);
		else if (i == 2) {
			c.playerXP[i2] = c.getPA().getXPForLevel(i3) + 5;
			c.playerLevel[i2] = c.getPA().getLevelForXP(c.playerXP[i2]);
		}
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == c.playerId) {
					Client c = (Client) PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}

	public void giveLife() {
		c.isDead = false;
		c.faceUpdate(-1);
		c.freezeTimer = 1;
		//ItemHandler.keepUntradeables(c, Config.ITEM_TRADEABLE);
	//	c.getItems().keepItem(Config.ITEM_TRADEABLE, true);
		if (c.duelStatus <= 4 && !c.InFightPitsWaiting()) { // if we are not in
			// a duel we must be
			// in wildy so
			// remove items
			if (!c.inPits && !c.inFightCaves() && !c.getRights().greaterOrEqual(Rights.ADMINISTRATOR) && !c.inNMZ()) {
				c.getItems().resetKeepItems();
				/*for(int item = 0; item < Config.ITEM_TRADEABLE.length; item++) {
					int itemId = Config.ITEM_TRADEABLE[item];
					int itemAmount = c.getItems().getItemAmount(itemId) + c.getItems().getWornItemAmount(itemId);
					if(c.getItems().playerHasItem(itemId) || c.getItems().isWearingItem(itemId)) {
						c.getItems();
						c.sendMessage("You kept "+itemAmount+" "+ItemAssistant.getItemName(itemId).toLowerCase()+", it was transferred to your bank.");
						c.getItems().addItemToBank(itemId, itemAmount);
					}
				} */
				c.getItems().getItemsToInventory();
				c.getItems().dropAllItems(); // drop all items
				c.getItems().deleteAllItems(); // delete all items
				c.getItems().resetKeepItems();
				c.getItems().itemsToInventory();
			} else if (c.inPits) {
				c.duelStatus = 0;
				Server.fightPits.removePlayerFromPits(c.playerId);
				c.pitsStatus = 1;
			}
		}
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (c.pitsStatus == 1) {
			movePlayer(2399, 5173, 0);
		} else if (c.duelStatus <= 4) { // if we are not in a duel repawn to
			// wildy
			if (c.inNMZ())
				movePlayer(2608, 3114, 0);
			else
				movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();
		} else if (c.inFightCaves()) {
			c.getPA().resetTzhaar();
		} else { // we are in a duel, respawn outside of arena
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o != null) {
				o.getPA().createPlayerHints(10, -1);
				if (o.duelStatus == 6) {
					o.getDuel().duelVictory();
				}
			}
			c.getPA().movePlayer(Config.DUELING_RESPAWN_X + (Misc.random(Config.RANDOM_DUELING_RESPAWN)),
					Config.DUELING_RESPAWN_Y + (Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
			o.getPA().movePlayer(Config.DUELING_RESPAWN_X + (Misc.random(Config.RANDOM_DUELING_RESPAWN)),
					Config.DUELING_RESPAWN_Y + (Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
			if (c.duelStatus != 6) { // if we have won but have died, don't
				// reset the duel status.
				c.getDuel().resetDuel();
			}
		}
		// PlayerSaving.getSingleton().requestSave(c.playerId);
		PlayerSave.saveGame(c);
		c.getCombat().resetPlayerAttack();
		resetAnimation();
		c.startAnimation(65535);
		frame1();
		resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.getPA().requestUpdates();
		removeAllWindows();
		closeAllWindows();
		c.getPA().cancelTeleportTask();
		c.tradeResetNeeded = true;
		c.targetIndex = 0;
		c.targetName = "";
		c.isFullHelm = Item.isFullHelm(c.playerEquipment[c.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
	}

	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (c.newLocation) {
		case 1:
			sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		c.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/
	public void spellTeleport(int x, int y, int height) {
		if (c.inZulrahShrine()) {
			c.sendMessage("The only way out of here is victory or death.");
			return;
		}
		if (c.inNMZ())
			c.getPlayerAssistant().walkableInterface(-1);
		c.getPA().startTeleport(x, y, height, c.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void startLeverTeleport(int x, int y, int height, String teleportType) {
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}

		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			if (teleportType.equalsIgnoreCase("lever")) {
				c.startAnimation(2140);
				c.teleTimer = 8;
				c.sendMessage("You pull the lever..");
			}
		}
	}

	private Task logoutDelay;

	// TODO: Call this where you want to cancel it.
	public void cancelLogoutTask() {
		if (logoutDelay != null) {
			logoutDelay.cancel();
		}
	}

	public void startLogout() {
		if (logoutDelay == null) {
			logoutDelay = new Task(2, true) {
				int count = 10;
				

				@Override
				public void execute() {
					if (count >= 0 && c.isSkulled) {
						c.getDH().sendStatement("There are another " + (count--) + " seconds before you can logout.");
					} else if (count >= 5 && !c.isSkulled) {
						c.getDH().sendStatement("There are another " + (count-- - 5) + " seconds before you can logout.");
					} else {
						cancel();
						c.sendMessage(":STOP:");
						c.logout();
					}
				}

				@Override
				public void onCancel() {
					logoutDelay = null;
				}
			};
			TaskHandler.submit(logoutDelay);
		}
	}

	private Task teleportDelay;

	// TODO: Call this where you want to cancel it.
	public void cancelTeleportTask() {
		if (teleportDelay != null) {
			teleportDelay.cancel();

		}
		if (logoutDelay != null) {
			logoutDelay.cancel();
		}
	}

	public void startTeleport(int x, int y, int height, String teleportType) {
		if (teleportDelay == null) {
			teleportDelay = new Task(2, true) {
				int count = 10;

				@Override
				public void execute() {
					if (count >= 0 && c.isSkulled) {
						c.getDH().sendStatement("There are another " + (count--) + " seconds before you can teleport.");
					} else if (count >= 5 && !c.isSkulled) {
						c.getDH().sendStatement("There are another " + (count-- - 5) + " seconds before you can teleport.");
					} else {
						if (c.inNMZ())
							c.getPlayerAssistant().walkableInterface(-1);
						c.isWc = false;
						if (c.duelStatus == 5) {
							c.sendMessage("You can't teleport during a duel!");
							return;
						}
						if (c.isInJail()) {
							c.sendMessage("You cannot teleport out of jail.");
							return;
						}
						if (c.isViewingOrb) {
							return;
						}
						if (c.duelStatus != 0) {
							c.sendMessage("You can't teleport during a duel!");
							return;
						}
						if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL && !c.inNMZ()) {
							c.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL
									+ " in the wilderness.");
							return;
						}
						if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
							c.sendMessage("You are teleblocked and can't teleport.");
							return;
						}
						if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
							if (c.playerIndex > 0 || c.npcIndex > 0)
								c.getCombat().resetPlayerAttack();
							c.stopMovement();
							removeAllWindows();
							c.teleX = x;
							c.teleY = y;
							c.npcIndex = 0;
							c.playerIndex = 0;
							c.sendMessage("Donating has great benefits! Check them out at ::perks");
							c.faceUpdate(0);
							c.teleHeight = height;
							if (teleportType.equalsIgnoreCase("modern")) {
								c.startAnimation(714);
								if (Config.SOUND)
									c.getPA().sendSound(c.sounds.TELEPORTM);
								if (c.inZulrahShrine()) {
								Zulrah.destruct(c);
								}
								c.teleTimer = 11;
								c.teleGfx = 308;
								c.teleEndAnimation = 715;
							}
							if (c.isInPestcontrol()) {
								c.sendMessage("You cannot teleport out of pest control!.");
								return;

							}
							if (teleportType.equalsIgnoreCase("ancient")) {
								c.startAnimation(1979);
								c.teleGfx = 0;
								c.teleTimer = 9;
								c.teleEndAnimation = 0;
								c.gfx0(392);
							}

						}
						cancel();
					}
				}

				@Override
				public void onCancel() {
					teleportDelay = null;
				}
			};
			TaskHandler.submit(teleportDelay);
		}
	}

	public void teleTabTeleport(int x, int y, int height, String teleportType) {
		if (teleportDelay == null) {
			teleportDelay = new Task(2, true) {
				int count = 10;

				@Override
				public void execute() {
					if (count >= 0 && c.isSkulled) {
						c.getDH().sendStatement("There are another " + (count--) + " seconds before you can teleport.");
					} else if (count >= 5 && !c.isSkulled) {
						c.getDH().sendStatement("There are another " + (count-- - 5) + " seconds before you can teleport.");
					} else {
						if (c.inPits) {
							c.sendMessage("You can't teleport during Fight Pits.");
							return;
						}
						if (c.isInPestcontrol()) {
							c.sendMessage("You cannot teleport out of pest control!.");
							return;

						}
						if (!c.startPack) {
							c.sendMessage("You must select your starter package before doing any action.");
							return;
						}

						if (c.InFightPitsWaiting()) {
							c.sendMessage("You can't teleport during Fight Pits.");
							return;
						}
						if (c.inTrade) {
							c.sendMessage("You can't teleport during a trade!");
							return;
						}
						if (c.duelStatus != 0) {
							c.sendMessage("You cannot teleport during a duel!");
							return;
						}
						if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
							c.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL
									+ " in the wilderness.");
							return;
						}
						if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
							c.sendMessage("You are teleblocked and can't teleport.");
							return;
						}

						if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
							if (c.playerIndex > 0 || c.npcIndex > 0)
								c.getCombat().resetPlayerAttack();
							c.stopMovement();
							// removeAllWindows();
							resetInterfaceStatus();
							c.teleX = x;
							c.teleY = y;
							c.npcIndex = 0;
							c.playerIndex = 0;
							c.faceUpdate(0);
							c.teleHeight = height;
							if (c.inZulrahShrine()) {
								Zulrah.destruct(c);
								}
							if (teleportType.equalsIgnoreCase("teleTab")) {
								c.startAnimation(4731);
								c.teleEndAnimation = 0;
								c.teleTimer = 8;
								c.gfx0(678);
							}

						}
						cancel();
					}
				}

				@Override
				public void onCancel() {
					teleportDelay = null;
				}
			};
			TaskHandler.submit(teleportDelay);
		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		// if (!c.startPack) {
		// c.sendMessage("You must select your starter package before doing any
		// action.");
		// return;
		// }
		if (c.duelStatus != 0) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (c.isInPestcontrol()) {
			c.sendMessage("You cannot teleport out of pest control!.");
			return;
		}
		if (c.inZulrahShrine()) {
			c.sendMessage("The only way out of here is victory or death.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			TaskHandler.submit(new Task(1, true) {
				@Override
				public void execute() {
					c.stopMovement();
					removeAllWindows();
					c.teleX = x;
					c.teleY = y;
					c.teleGfx = 308;
					c.startAnimation(714);
					c.teleEndAnimation = 715;
					c.npcIndex = 0;
					c.playerIndex = 0;
					c.faceUpdate(0);
					c.teleHeight = height;
					c.teleTimer = 11;
					this.cancel();
				}

				@Override
				public void onCancel() {
					System.out.println("Teleport complete.");
				}
			});
		}
	}

	public void specialTeleport(int x, int y, int height) {
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (c.duelStatus != 0) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (c.isInPestcontrol()) {
			c.sendMessage("You cannot teleport out of pest control!.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			TaskHandler.submit(new Task(1, true) {
				@Override
				public void execute() {
					c.stopMovement();
					removeAllWindows();
					c.teleX = x;
					c.teleY = y;
					c.startAnimation(714);
					c.teleEndAnimation = 715;
					c.npcIndex = 0;
					c.playerIndex = 0;
					c.faceUpdate(0);
					c.teleHeight = height;
					if (c.inZulrahShrine()) {
						Zulrah.destruct(c);
						}
					c.teleTimer = 11;
					c.teleGfx = 308;
					this.cancel();
				}

				@Override
				public void onCancel() {
					System.out.println("Teleport complete.");
				}
			});
		}
	}

	/*
	 * For admins
	 */
	public void startAdminTeleport(int x, int y, int height) {
		if (c.duelStatus != 0) {
			c.sendMessage("You cannot teleport during a duel!");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(714);
			c.teleTimer = 11;
			c.teleGfx = 308;
			c.teleEndAnimation = 715;
		}
	}

	public void processTeleport() {
		c.teleportToX = c.teleX;
		c.teleportToY = c.teleY;
		c.heightLevel = c.teleHeight;
		if (c.teleEndAnimation > 0) {
			c.startAnimation(c.teleEndAnimation);
		}
	}

	public void resetInterfaceStatus() {
		if (c.getOutStream() != null && c != null) {
			c.getPA().resetVariables();
			c.getOutStream().createFrame(219);
		}
	}

	public void followPlayer() {
		if (PlayerHandler.players[c.followId] == null || PlayerHandler.players[c.followId].isDead) {
			resetFollow();
			return;
		}
		if (c.underAttackBy > 0 || c.underAttackBy2 > 0) {
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		int otherX = PlayerHandler.players[c.followId].getX();
		int otherY = PlayerHandler.players[c.followId].getY();

		boolean sameSpot = (c.absX == otherX && c.absY == otherY);

		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);

		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 4);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 6);
		boolean mageDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 7);

		boolean castingMagic = (c.usingMagic || c.mageFollow || c.autocasting || c.spellId > 0) && mageDistance;
		boolean playerRanging = (c.usingRangeWeapon) && rangeWeaponDistance;
		boolean playerBowOrCross = (c.usingBow) && bowDistance;
		boolean ranging = false;
		for (int bowId : c.BOWS) {
			if (c.playerEquipment[c.playerWeapon] == bowId) {
				ranging = true;
			}
		}

		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId = 0;
			resetFollow();
			return;
		}
		c.faceUpdate(c.followId + 32768);
		if (!sameSpot) {
			if (c.playerIndex > 0 && !c.usingSpecial && c.inWild()) {
				if (c.usingSpecial && (playerRanging || playerBowOrCross)) {
					c.stopMovement();
					return;
				}
				if (castingMagic || playerRanging || playerBowOrCross || (ranging && bowDistance)) {
					c.stopMovement();
					return;
				}
				if (c.getCombat().usingHally() && hallyDistance) {
					c.stopMovement();
					return;
				}
			}
		}
		if (otherX == c.absX && otherY == c.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (c.isRunning2) {
			if (otherY > c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				playerWalk(otherX - 1, otherY + 1);
			}
		}
		c.faceUpdate(c.followId + 32768);
	}

	public void followNpc() {
		if (NPCHandler.npcs[c.followId2] == null || NPCHandler.npcs[c.followId2].isDead) {
			c.followId2 = 0;
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		int otherX = NPCHandler.npcs[c.followId2].getX();
		int otherY = NPCHandler.npcs[c.followId2].getY();
		boolean withinDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1);
		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 10);
		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 10);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId2 = 0;
			return;
		}
		if (c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
			if (otherX != c.getX() && otherY != c.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((c.usingBow || c.mageFollow || (c.npcIndex > 0 && c.autocastId > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		c.faceUpdate(c.followId2);
		if (otherX == c.absX && otherY == c.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (c.isRunning2 && !withinDistance) {
			if (otherY > c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if (otherY < c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1), 0);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), 0);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1),
						getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1),
						getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if (otherX < c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1),
						getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if (otherX > c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1),
						getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			}
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY - 1));
			} else if (otherY < c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1), 0);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1), 0);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY - 1));
			} else if (otherX < c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1));
			} else if (otherX > c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY + 1));
			}
		}
		c.faceUpdate(c.followId2);
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void resetFollow() {
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
	}

	public void walkTo(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		if (c.freezeTimer > 0) // player can't move
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - c.getY();
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 4000)
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public void sendStatement(String s) {
		sendFrame126(s, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		c.getItems();
		c.getCombat().getPlayerAnimIndex(c, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		c.startAnimation(c.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		Player.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void Obelisks(int id) {
		if (!c.getItems().playerHasItem(id)) {
			c.getItems().addItem(id, 1);
		}
	}

	/*
	 * public void levelUp(int skill) { switch (skill) { case 0: sendFrame126(
	 * "Congratulations! You've just advanced a Attack level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + "!", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a attack level.");
	 * sendFrame164(6247); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS]@red@ The player @blu@" + c.playerName +
	 * " just advanced to 99 @blu@Attack@bla@."); } break;
	 * 
	 * case 1: sendFrame126(
	 * "Congratulations! You've just advanced a Defence level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Defence level.");
	 * sendFrame164(6253); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " just advanced to 99 @blu@Defence@bla@."); } break;
	 * 
	 * case 2: sendFrame126(
	 * "Congratulations! You've just advanced a Strength level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Strength level.");
	 * sendFrame164(6206); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Strength@bla@."); } break;
	 * 
	 * case 3: sendFrame126(
	 * "Congratulations! You've just advanced a Hitpoints level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Hitpoints level." );
	 * sendFrame164(6216); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Hitpoints@bla@."); }
	 * break;
	 * 
	 * case 4: sendFrame126(
	 * "Congratulations! You've just advanced a Ranged level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Ranging level.");
	 * sendFrame164(4443); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Ranged@bla@."); } break;
	 * 
	 * case 5: sendFrame126(
	 * "Congratulations! You've just advanced a Prayer level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Prayer level.");
	 * sendFrame164(6242); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Prayer@bla@."); } break;
	 * 
	 * case 6: sendFrame126(
	 * "Congratulations! You've just advanced a Magic level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Magic level.");
	 * sendFrame164(6211); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Magic@bla@."); } break;
	 * 
	 * case 7: sendFrame126(
	 * "Congratulations! You've just advanced a Cooking level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Cooking level.");
	 * sendFrame164(6226); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Cooking@bla@."); } break;
	 * 
	 * case 8: sendFrame126(
	 * "Congratulations! You've just advanced a Woodcutting level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Woodcutting level." );
	 * sendFrame164(4272); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Woodcutting@bla@."); }
	 * break;
	 * 
	 * case 9: sendFrame126(
	 * "Congratulations! You've just advanced a Fletching level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Fletching level." );
	 * sendFrame164(6231); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Fletching@bla@."); }
	 * break;
	 * 
	 * case 10: sendFrame126(
	 * "Congratulations! You've just advanced a Fishing level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Fishing level.");
	 * sendFrame164(6258); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Fishing@bla@."); } break;
	 * 
	 * case 11: sendFrame126(
	 * "Congratulations! You've just advanced a Fire making level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Fire making level." );
	 * sendFrame164(4282); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Firemaking@bla@."); }
	 * break;
	 * 
	 * case 12: sendFrame126(
	 * "Congratulations! You've just advanced a Crafting level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Crafting level.");
	 * sendFrame164(6263); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Crafting@bla@."); } break;
	 * 
	 * case 13: sendFrame126(
	 * "Congratulations! You've just advanced a Smithing level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Smithing level.");
	 * sendFrame164(6221); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Smithing@bla@."); } break;
	 * 
	 * case 14: sendFrame126(
	 * "Congratulations! You've just advanced a Mining level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Mining level.");
	 * sendFrame164(4416); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Mining@bla@."); } break;
	 * 
	 * case 15: sendFrame126(
	 * "Congratulations! You've just advanced a Herblore level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Herblore level.");
	 * sendFrame164(6237); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Herblore@bla@."); } break;
	 * 
	 * case 16: sendFrame126(
	 * "Congratulations! You've just advanced a Agility level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Agility level.");
	 * sendFrame164(4277); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Agility@bla@."); } break;
	 * 
	 * case 17: sendFrame126(
	 * "Congratulations! You've just advanced a Thieving level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Thieving level.");
	 * sendFrame164(4261); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Thieving@bla@."); } break;
	 * 
	 * case 18: sendFrame126(
	 * "Congratulations! You've just advanced a Slayer level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Slayer level.");
	 * sendFrame164(12122); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Slayer@bla@."); } break;
	 * 
	 * case 19: sendFrame126(
	 * "Congratulations! You've just advanced a Farming level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Farming level.");
	 * sendFrame164(5267); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Farming@bla@."); } break;
	 * 
	 * case 20: sendFrame126(
	 * "Congratulations! You've just advanced a Runecrafting level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Runecrafting level." );
	 * sendFrame164(4267); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Runecrafting@bla@."); }
	 * break;
	 * 
	 * case 21: sendFrame126(
	 * "Congratulations! You've just advanced a Construction level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Construction level." );
	 * sendFrame164(7267); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Construction@bla@."); }
	 * break;
	 * 
	 * case 22: sendFrame126(
	 * "Congratulations! You've just advanced a Hunter level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Hunter level.");
	 * sendFrame164(8267); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Hunter@bla@."); } break;
	 * 
	 * case 23: sendFrame126(
	 * "Congratulations! You've just advanced a Summoning level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Summoning level." );
	 * sendFrame164(9267); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Summoning@bla@."); }
	 * break;
	 * 
	 * case 24: sendFrame126(
	 * "Congratulations! You've just advanced a Dungeoneering level!", 4268);
	 * sendFrame126("You have now reached level " +
	 * getLevelForXP(c.playerXP[skill]) + ".", 4269); c.sendMessage(
	 * "Congratulations! You've just advanced a Dungeoneering level." );
	 * sendFrame164(10267); if (getLevelForXP(c.playerXP[skill]) == 99) {
	 * c.globalMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" +
	 * c.playerName + " @bla@just advanced to 99 @blu@Dungeoneering@bla@."); }
	 * break; } c.dialogueAction = 0; c.nextChat = 0; sendFrame126(
	 * "Click here to continue", 358); }
	 * 
	 * public void refreshSkill(int i) { c.calcCombat(); switch (i) { case 0:
	 * sendFrame126("" + c.playerLevel[0] + "", 4004); sendFrame126("" +
	 * getLevelForXP(c.playerXP[0]) + "", 4005); sendFrame126("" + c.playerXP[0]
	 * + "", 4044); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[0])
	 * + 1) + "", 4045); break;
	 * 
	 * case 1: sendFrame126("" + c.playerLevel[1] + "", 4008); sendFrame126("" +
	 * getLevelForXP(c.playerXP[1]) + "", 4009); sendFrame126("" + c.playerXP[1]
	 * + "", 4056); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[1])
	 * + 1) + "", 4057); break;
	 * 
	 * case 2: sendFrame126("" + c.playerLevel[2] + "", 4006); sendFrame126("" +
	 * getLevelForXP(c.playerXP[2]) + "", 4007); sendFrame126("" + c.playerXP[2]
	 * + "", 4050); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[2])
	 * + 1) + "", 4051); break;
	 * 
	 * case 3: sendFrame126("" + c.playerLevel[3] + "", 4016); sendFrame126("" +
	 * getLevelForXP(c.playerXP[3]) + "", 4017); sendFrame126("" + c.playerXP[3]
	 * + "", 4080); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[3])
	 * + 1) + "", 4081); break;
	 * 
	 * case 4: sendFrame126("" + c.playerLevel[4] + "", 4010); sendFrame126("" +
	 * getLevelForXP(c.playerXP[4]) + "", 4011); sendFrame126("" + c.playerXP[4]
	 * + "", 4062); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[4])
	 * + 1) + "", 4063); break;
	 * 
	 * case 5: sendFrame126("" + c.playerLevel[5] + "", 4012); sendFrame126("" +
	 * getLevelForXP(c.playerXP[5]) + "", 4013); sendFrame126("" + c.playerXP[5]
	 * + "", 4068); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[5])
	 * + 1) + "", 4069); sendFrame126("" + c.playerLevel[5] + "/" +
	 * getLevelForXP(c.playerXP[5]) + "", 687);// Prayer frame break;
	 * 
	 * case 6: sendFrame126("" + c.playerLevel[6] + "", 4014); sendFrame126("" +
	 * getLevelForXP(c.playerXP[6]) + "", 4015); sendFrame126("" + c.playerXP[6]
	 * + "", 4074); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[6])
	 * + 1) + "", 4075); break;
	 * 
	 * case 7: sendFrame126("" + c.playerLevel[7] + "", 4034); sendFrame126("" +
	 * getLevelForXP(c.playerXP[7]) + "", 4035); sendFrame126("" + c.playerXP[7]
	 * + "", 4134); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[7])
	 * + 1) + "", 4135); break;
	 * 
	 * case 8: sendFrame126("" + c.playerLevel[8] + "", 4038); sendFrame126("" +
	 * getLevelForXP(c.playerXP[8]) + "", 4039); sendFrame126("" + c.playerXP[8]
	 * + "", 4146); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[8])
	 * + 1) + "", 4147); break;
	 * 
	 * case 9: sendFrame126("" + c.playerLevel[9] + "", 4026); sendFrame126("" +
	 * getLevelForXP(c.playerXP[9]) + "", 4027); sendFrame126("" + c.playerXP[9]
	 * + "", 4110); sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[9])
	 * + 1) + "", 4111); break;
	 * 
	 * case 10: sendFrame126("" + c.playerLevel[10] + "", 4032); sendFrame126(""
	 * + getLevelForXP(c.playerXP[10]) + "", 4033); sendFrame126("" +
	 * c.playerXP[10] + "", 4128); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[10]) + 1) + "", 4129); break;
	 * 
	 * case 11: sendFrame126("" + c.playerLevel[11] + "", 4036); sendFrame126(""
	 * + getLevelForXP(c.playerXP[11]) + "", 4037); sendFrame126("" +
	 * c.playerXP[11] + "", 4140); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[11]) + 1) + "", 4141); break;
	 * 
	 * case 12: sendFrame126("" + c.playerLevel[12] + "", 4024); sendFrame126(""
	 * + getLevelForXP(c.playerXP[12]) + "", 4025); sendFrame126("" +
	 * c.playerXP[12] + "", 4104); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[12]) + 1) + "", 4105); break;
	 * 
	 * case 13: sendFrame126("" + c.playerLevel[13] + "", 4030); sendFrame126(""
	 * + getLevelForXP(c.playerXP[13]) + "", 4031); sendFrame126("" +
	 * c.playerXP[13] + "", 4122); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[13]) + 1) + "", 4123); break;
	 * 
	 * case 14: sendFrame126("" + c.playerLevel[14] + "", 4028); sendFrame126(""
	 * + getLevelForXP(c.playerXP[14]) + "", 4029); sendFrame126("" +
	 * c.playerXP[14] + "", 4116); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[14]) + 1) + "", 4117); break;
	 * 
	 * case 15: sendFrame126("" + c.playerLevel[15] + "", 4020); sendFrame126(""
	 * + getLevelForXP(c.playerXP[15]) + "", 4021); sendFrame126("" +
	 * c.playerXP[15] + "", 4092); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[15]) + 1) + "", 4093); break;
	 * 
	 * case 16: sendFrame126("" + c.playerLevel[16] + "", 4018); sendFrame126(""
	 * + getLevelForXP(c.playerXP[16]) + "", 4019); sendFrame126("" +
	 * c.playerXP[16] + "", 4086); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[16]) + 1) + "", 4087); break;
	 * 
	 * case 17: sendFrame126("" + c.playerLevel[17] + "", 4022); sendFrame126(""
	 * + getLevelForXP(c.playerXP[17]) + "", 4023); sendFrame126("" +
	 * c.playerXP[17] + "", 4098); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[17]) + 1) + "", 4099); break;
	 * 
	 * case 18: sendFrame126("" + c.playerLevel[18] + "", 12166);
	 * sendFrame126("" + getLevelForXP(c.playerXP[18]) + "", 12167);
	 * sendFrame126("" + c.playerXP[18] + "", 12171); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[18]) + 1) + "", 12172); break;
	 * 
	 * case 19: sendFrame126("" + c.playerLevel[19] + "", 13926);
	 * sendFrame126("" + getLevelForXP(c.playerXP[19]) + "", 13927);
	 * sendFrame126("" + c.playerXP[19] + "", 13921); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[19]) + 1) + "", 13922); break;
	 * 
	 * case 20: sendFrame126("" + c.playerLevel[20] + "", 4152); sendFrame126(""
	 * + getLevelForXP(c.playerXP[20]) + "", 4153); sendFrame126("" +
	 * c.playerXP[20] + "", 4157); sendFrame126("" +
	 * getXPForLevel(getLevelForXP(c.playerXP[20]) + 1) + "", 4158); break; } }
	 * 
	 * public int getXPForLevel(int level) { int points = 0; int output = 0;
	 * 
	 * for (int lvl = 1; lvl <= level; lvl++) { points += Math.floor(lvl + 300.0
	 * Math.pow(2.0, lvl / 7.0)); if (lvl >= level) return output; output =
	 * (int) Math.floor(points / 4); } return 0; }
	 * 
	 * public int getLevelForXP(int exp) { int points = 0; int output = 0; if
	 * (exp > 13034430) return 99; for (int lvl = 1; lvl <= 99; lvl++) { points
	 * += Math.floor(lvl + 300.0 Math.pow(2.0, lvl / 7.0)); output = (int)
	 * Math.floor(points / 4); if (output >= exp) { return lvl; } } return 0; }
	 * 
	 * public boolean addSkillXP(int amount, int skill) { if
	 * (Config.LOCK_EXPERIENCE) { return false; } if (c.expLock == true) {
	 * return false; } if (amount + c.playerXP[skill] < 0 || c.playerXP[skill] >
	 * 200000000) { if (c.playerXP[skill] > 200000000) { c.playerXP[skill] =
	 * 200000000; } return false; } if (skill == 11) { amount = 20; } if (skill
	 * == 12) { amount = 20; } amount = Config.SERVER_EXP_BONUS; int oldLevel =
	 * getLevelForXP(c.playerXP[skill]); c.playerXP[skill] += amount; if
	 * (oldLevel < getLevelForXP(c.playerXP[skill])) { if (c.playerLevel[skill]
	 * < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
	 * c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
	 * levelUp(skill); c.gfx100(199); requestUpdates(); } setSkillLevel(skill,
	 * c.playerLevel[skill], c.playerXP[skill]); refreshSkill(skill); return
	 * true; }
	 */

	public void levelUp(int skill) {
		switch (skill) {
		case 0:
			sendFrame126("Congratulations! You've just advanced an Attack level!", 6248);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + "!", 6249);
			c.sendMessage("Congratulations! You've just advanced a attack level.");
			sendFrame164(6247);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Attack@bla@.");
					}
				}
			}
			break;

		case 1:
			sendFrame126("Congratulations! You've just advanced a Defence level!", 6254);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6255);
			c.sendMessage("Congratulations! You've just advanced a Defence level.");
			sendFrame164(6253);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Defence@bla@.");
					}
				}
			}
			break;

		case 2:
			sendFrame126("Congratulations! You've just advanced a Strength level!", 6207);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6208);
			c.sendMessage("Congratulations! You've just advanced a Strength level.");
			sendFrame164(6206);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Strength@bla@.");
					}
				}
			}
			break;

		case 3:
			sendFrame126("Congratulations! You've just advanced a Hitpoints level!", 6217);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6218);
			c.sendMessage("Congratulations! You've just advanced a Hitpoints level.");
			sendFrame164(6216);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Hitpoints@bla@.");
					}
				}
			}
			break;

		case 4:
			sendFrame126("Congratulations! You've just advanced a Ranged level!", 4444);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4445);
			c.sendMessage("Congratulations! You've just advanced a Ranging level.");
			sendFrame164(4443);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Ranged@bla@.");
					}
				}
			}
			break;

		case 5:
			sendFrame126("Congratulations! You've just advanced a Prayer level!", 6243);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6244);
			c.sendMessage("Congratulations! You've just advanced a Prayer level.");
			sendFrame164(6242);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Prayer@bla@.");
					}
				}
			}
			break;

		case 6:
			sendFrame126("Congratulations! You've just advanced a Magic level!", 6212);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6213);
			c.sendMessage("Congratulations! You've just advanced a Magic level.");
			sendFrame164(6211);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Magic@bla@.");
					}
				}
			}
			break;

		case 7:
			sendFrame126("Congratulations! You've just advanced a Cooking level!", 6227);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6228);
			c.sendMessage("Congratulations! You've just advanced a Cooking level.");
			sendFrame164(6226);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Cooking@bla@.");
					}
				}
			}
			break;

		case 8:
			sendFrame126("Congratulations! You've just advanced a Woodcutting level!", 4273);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4274);
			c.sendMessage("Congratulations! You've just advanced a Woodcutting level.");
			sendFrame164(4272);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Woodcutting@bla@.");
					}
				}
			}
			break;

		case 9:
			sendFrame126("Congratulations! You've just advanced a Fletching level!", 6232);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6233);
			c.sendMessage("Congratulations! You've just advanced a Fletching level.");
			sendFrame164(6231);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Fletching@bla@.");
					}
				}
			}
			break;

		case 10:
			sendFrame126("Congratulations! You've just advanced a Fishing level!", 6259);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6260);
			c.sendMessage("Congratulations! You've just advanced a Fishing level.");
			sendFrame164(6258);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Fishing@bla@.");
					}
				}
			}
			break;

		case 11:
			sendFrame126("Congratulations! You've just advanced a Fire making level!", 4283);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4284);
			c.sendMessage("Congratulations! You've just advanced a Fire making level.");
			sendFrame164(4282);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Firemaking@bla@.");
					}
				}
			}
			break;

		case 12:
			sendFrame126("Congratulations! You've just advanced a Crafting level!", 6264);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6265);
			c.sendMessage("Congratulations! You've just advanced a Crafting level.");
			sendFrame164(6263);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Crafting@bla@.");
					}
				}
			}
			break;

		case 13:
			sendFrame126("Congratulations! You've just advanced a Smithing level!", 6222);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6223);
			c.sendMessage("Congratulations! You've just advanced a Smithing level.");
			sendFrame164(6221);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Smithing@bla@.");
					}
				}
			}
			break;

		case 14:
			sendFrame126("Congratulations! You've just advanced a Mining level!", 4417);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4418);
			c.sendMessage("Congratulations! You've just advanced a Mining level.");
			sendFrame164(4416);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Mining@bla@.");
					}
				}
			}
			break;

		case 15:
			sendFrame126("Congratulations! You've just advanced a Herblore level!", 6238);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 6239);
			c.sendMessage("Congratulations! You've just advanced a Herblore level.");
			sendFrame164(6237);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Herblore@bla@.");
					}
				}
			}
			break;

		case 16:
			sendFrame126("Congratulations! You've just advanced a Agility level!", 4278);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4279);
			c.sendMessage("Congratulations! You've just advanced a Agility level.");
			sendFrame164(4277);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Agility@bla@.");
					}
				}
			}
			break;

		case 17:
			sendFrame126("Congratulations! You've just advanced a Thieving level!", 4262);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4263);
			c.sendMessage("Congratulations! You've just advanced a Thieving level.");
			sendFrame164(4261);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Thieving@bla@.");
					}
				}
			}
			break;

		case 18:
			sendFrame126("Congratulations! You've just advanced a Slayer level!", 12123);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 12124);
			c.sendMessage("Congratulations! You've just advanced a Slayer level.");
			sendFrame164(12122);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Slayer@bla@.");
					}
				}
			}
			break;

		case 19:
			sendFrame126("Congratulations! You've just advanced a Farming level!", 5268);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 5269);
			c.sendMessage("Congratulations! You've just advanced a Farming level.");
			sendFrame164(5267);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Farming@bla@.");
					}
				}
			}
			break;

		case 20:
			sendFrame126("Congratulations! You've just advanced a Runecrafting level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Runecrafting level.");
			sendFrame164(4267);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Runecrafting@bla@.");
					}
				}
			}
			break;

		case 21:
			sendFrame126("Congratulations! You've just advanced a Construction level!", 7268);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 7269);
			c.sendMessage("Congratulations! You've just advanced a Construction level.");
			sendFrame164(7267);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Construction@bla@.");
					}
				}
			}
			break;

		case 22:
			sendFrame126("Congratulations! You've just advanced a Hunter level!", 8268);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 8269);
			c.sendMessage("Congratulations! You've just advanced a hunter level.");
			sendFrame164(8267);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Hunter@bla@.");
					}
				}
			}
			break;

		case 23:
			sendFrame126("Congratulations! You've just advanced a Summoning level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Summoning level.");
			sendFrame164(9267);
			break;

		case 24:
			sendFrame126("Congratulations! You've just advanced a Dungeoneering level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Dungeoneering level.");
			sendFrame164(10267);
			break;
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
		sendFrame126("Click here to continue", 358);
	}

	public void refreshSkill(int i) {
		switch (i) {
		case 0:
			sendFrame126("" + c.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			sendFrame126("" + c.playerXP[0] + "", 4044);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1) + "", 4045);
			break;

		case 1:
			sendFrame126("" + c.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			sendFrame126("" + c.playerXP[1] + "", 4056);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1) + "", 4057);
			break;

		case 2:
			sendFrame126("" + c.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			sendFrame126("" + c.playerXP[2] + "", 4050);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1) + "", 4051);
			break;

		case 3:
			sendFrame126("" + c.playerLevel[3] + "", 4016);
			sendFrame126("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			sendFrame126("" + c.playerXP[3] + "", 4080);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[3]) + 1) + "", 4081);
			break;

		case 4:
			sendFrame126("" + c.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			sendFrame126("" + c.playerXP[4] + "", 4062);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1) + "", 4063);
			break;

		case 5:
			sendFrame126("" + c.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			sendFrame126("" + c.playerXP[5] + "", 4068);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1) + "", 4069);
			sendFrame126("" + c.playerLevel[5] + "/" + getLevelForXP(c.playerXP[5]) + "", 687);// Prayer
																								// frame
			break;

		case 6:
			sendFrame126("" + c.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			sendFrame126("" + c.playerXP[6] + "", 4074);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1) + "", 4075);
			break;

		case 7:
			sendFrame126("" + c.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			sendFrame126("" + c.playerXP[7] + "", 4134);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[7]) + 1) + "", 4135);
			break;

		case 8:
			sendFrame126("" + c.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			sendFrame126("" + c.playerXP[8] + "", 4146);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1) + "", 4147);
			break;

		case 9:
			sendFrame126("" + c.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			sendFrame126("" + c.playerXP[9] + "", 4110);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1) + "", 4111);
			break;

		case 10:
			sendFrame126("" + c.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			sendFrame126("" + c.playerXP[10] + "", 4128);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1) + "", 4129);
			break;

		case 11:
			sendFrame126("" + c.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			sendFrame126("" + c.playerXP[11] + "", 4140);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1) + "", 4141);
			break;

		case 12:
			sendFrame126("" + c.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			sendFrame126("" + c.playerXP[12] + "", 4104);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1) + "", 4105);
			break;

		case 13:
			sendFrame126("" + c.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			sendFrame126("" + c.playerXP[13] + "", 4122);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1) + "", 4123);
			break;

		case 14:
			sendFrame126("" + c.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			sendFrame126("" + c.playerXP[14] + "", 4116);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1) + "", 4117);
			break;

		case 15:
			sendFrame126("" + c.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			sendFrame126("" + c.playerXP[15] + "", 4092);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1) + "", 4093);
			break;

		case 16:
			sendFrame126("" + c.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			sendFrame126("" + c.playerXP[16] + "", 4086);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1) + "", 4087);
			break;

		case 17:
			sendFrame126("" + c.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			sendFrame126("" + c.playerXP[17] + "", 4098);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1) + "", 4099);
			break;

		case 18:
			sendFrame126("" + c.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			sendFrame126("" + c.playerXP[18] + "", 12171);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1) + "", 12172);
			break;

		case 19:
			sendFrame126("" + c.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			sendFrame126("" + c.playerXP[19] + "", 13921);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1) + "", 13922);
			break;

		case 20:
			sendFrame126("" + c.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			sendFrame126("" + c.playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1) + "", 4158);
			break;

		case 21:
			sendFrame126("@yel@" + c.playerLevel[21] + "", 18166); // hunter
			sendFrame126("@yel@" + c.playerLevel[21] + "", 18170); // hunter
			break;

		case 22: // construction
			sendFrame126("@yel@" + c.playerLevel[22] + "", 18165);
			sendFrame126("@yel@" + c.playerLevel[22] + "", 18169);
			break;
		}
		c.calcCombat();
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(int amount, int skill) {
		if (Config.LOCK_EXPERIENCE || c.trollSpawned || c.treeSpawned || c.zombieSpawned || c.golemSpawned) {
			return false;
		}
		if (c.expLock == true) {
			return false;
		}
		if (amount + c.playerXP[skill] < 0 || c.playerXP[skill] > 200000000) {
			if (c.playerXP[skill] > 200000000) {
				c.playerXP[skill] = 200000000;
			}
			return false;
		}
		if (skill == 11) {
			amount *= 20;
		}
		if (skill == 12) {
			amount *= 20;
		}
		amount *= Config.SERVER_EXP_BONUS;
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.randomCoffin = Misc.random(3) + 1;
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 100000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 100000;
					}
					c.playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
					c.getItems();
					c.sendMessage(
							"You repair your " + ItemAssistant.getItemName(c.getItems().brokenBarrows[i][0] + 1) + ".");
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), totalCost);
	}

	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		// synchronized(c) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeWord(j);
			c.outStream.writeWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeWord(k);
			c.outStream.writeWord(l);
			c.outStream.writeByte(j);
		}

	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].npcId == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	public void handleGlory(int gloryId) {
		c.getDH().sendOption4("Edgeville", "Al Kharid", "Karamja", "Draynor");
		c.sendMessage("You rub the amulet...");
		c.usingGlory = true;
	}
	public static void ROD(Client c) {
		c.getPA().removeAllWindows();
		c.getDH().sendOption2("Duel Arena", "Castle Wars");
		c.usingROD = true;
		c.sendMessage("You rub the Ring of Dueling...");
	}

	public static void GN(Client c) {
		c.getPA().removeAllWindows();
		c.getDH().sendOption2("Burthrope Games Room", "Barbarian Outpost");
		c.usingGN = true;
		c.sendMessage("You rub the Games Necklace...");
	}

	public void resetVariables() {
		if (c.isBanking)
			c.isBanking = false;
		if (c.isShopping)
			c.isShopping = false;
		if (c.inTrade)
			c.inTrade = false;
		if (c.openDuel)
			c.openDuel = false;
		if (c.duelStatus == 1)
			c.duelStatus = 0;
		c.usingGlory = false;
		c.smeltInterface = false;
		// c.smeltAmount = 0;
		if (c.dialogueAction > -1)
			c.dialogueAction = -1;
		if (c.teleAction > -1)
			c.teleAction = -1;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public int antiFire() {
		int anti = 0;
		if (c.antiFirePot)
			anti = 2;
		if (c.playerEquipment[c.playerShield] == 1540 || c.prayerActive[12]
				|| c.playerEquipment[c.playerShield] == 11283)
			anti = 2;
		return anti;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	/*
	 * Vengeance
	 */
	public void castVeng() {
		if (c.playerLevel[6] < 94) {
			c.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (c.playerLevel[1] < 40) {
			c.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!c.getItems().playerHasItem(9075, 4) || !c.getItems().playerHasItem(557, 10)
				|| !c.getItems().playerHasItem(560, 2)) {
			c.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - c.lastCast < 30000) {
			c.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (c.vengOn) {
			c.sendMessage("You already have vengeance casted.");
			return;
		}
		if (c.duelRule[4]) {
			c.sendMessage("You can't cast this spell because magic has been disabled.");
			return;
		}
		c.startAnimation(4410);
		c.gfx100(726);
		c.getItems().deleteItem2(9075, 4);
		c.getItems().deleteItem2(557, 10);
		c.getItems().deleteItem2(560, 2);
		addSkillXP(10000, 6);
		refreshSkill(6);
		c.vengOn = true;
		c.lastCast = System.currentTimeMillis();
	}

	public void vengMe() {
		if (System.currentTimeMillis() - c.lastVeng > 30000) {
			if (c.getItems().playerHasItem(557, 10) && c.getItems().playerHasItem(9075, 4)
					&& c.getItems().playerHasItem(560, 2)) {
				c.vengOn = true;
				c.lastVeng = System.currentTimeMillis();
				c.startAnimation(4410);
				c.gfx100(726);
				c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 10);
				c.getItems().deleteItem(560, c.getItems().getItemSlot(560), 2);
				c.getItems().deleteItem(9075, c.getItems().getItemSlot(9075), 4);
			} else {
				c.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			c.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void addStarter() {
		if (!Punishments.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.getItems().addItem(995, 6000);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(882, 100);
			c.getItems().addItem(1291, 1);
			c.getItems().addItem(1351, 1);
			c.getItems().addItem(1265, 1);
			c.getItems().addItem(1171, 1);
			c.getItems().addItem(590, 1);
			c.getItems().addItem(2347, 1);
			c.getItems().addItem(303, 1);
			c.getItems().addItem(333, 4);
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			Punishments.addIpToStarterList1(PlayerHandler.players[c.playerId].connectedFrom);
			Punishments.addIpToStarter1(PlayerHandler.players[c.playerId].connectedFrom);
		} else if (Punishments.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)
				&& !Punishments.hasRecieved2ndStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.getItems().addItem(995, 6000);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(882, 100);
			c.getItems().addItem(1291, 1);
			c.getItems().addItem(1351, 1);
			c.getItems().addItem(1265, 1);
			c.getItems().addItem(1171, 1);
			c.getItems().addItem(590, 1);
			c.getItems().addItem(2347, 1);
			c.getItems().addItem(303, 1);
			c.getItems().addItem(333, 4);
			c.sendMessage("You have recieved 2 out of 2 starter packages on this IP address.");
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			Punishments.addIpToStarterList2(PlayerHandler.players[c.playerId].connectedFrom);
			Punishments.addIpToStarter2(PlayerHandler.players[c.playerId].connectedFrom);
		} else if (Punishments.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)
				&& Punishments.hasRecieved2ndStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.sendMessage("You have already recieved 2 starters!");
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
		}
	}

	public void sendFrame34P2(int item, int slot, int frame, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void useOperate(int itemId) {
		switch (itemId) {
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			c.isOperate2 = true;
			c.itemUsing = itemId;
			Teles.ROD(c);
		break;
		
		case 3853:
		case 3855:
		case 3857:
		case 3859:
		case 3861:
		case 3863:
		case 3865:
		case 3867:
			c.isOperate = true;
			c.itemUsing = itemId;
			Teles.GN(c);
		break;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		// moveCheck(x,y);
		c.lastSpear = System.currentTimeMillis();
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}

	public int findKiller() {
		int killer = c.playerId;
		int damage = 0;
		for (int j = 0; j < Config.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == c.playerId)
				continue;
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY + 9400, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40))
				if (c.damageTaken[j] > damage) {
					damage = c.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}

	public void resetTzhaar() {
		c.waveId = -1;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
		c.getPA().movePlayer(2438, 5168, 0);
	}

	public void enterCaves() {
		c.getPA().movePlayer(2413, 5117, c.playerId * 4);
		c.waveId = 0;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
		TaskHandler.submit(new Task(20, true) {

			@Override
			public void execute() {
				Server.fightCaves.spawnNextWave((Client) PlayerHandler.players[c.playerId]);
				this.cancel();
			}

			@Override
			public void onCancel() {

			}

		});
	}

	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned.");
			c.poisonDamage = damage;
		}
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0)
			return;
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.POUCH_SIZE[i] - c.pouches[i])
			toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.pouches[i];
		if (toAdd > c.getEquipment().freeSlots()) {
			toAdd = c.getEquipment().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.pouches[i] -= toAdd;
		}
	}

	/*
	 * public void fixAllBarrows() { int totalCost = 0; int cashAmount =
	 * c.getItems().getItemAmount(995); for (int j = 0; j <
	 * c.playerItems.length; j++) { boolean breakOut = false; for (int i = 0; i
	 * < c.getItems().brokenBarrows.length; i++) { if (c.playerItems[j]-1 ==
	 * c.getItems().brokenBarrows[i][1]) { if (totalCost + 80000 > cashAmount) {
	 * breakOut = true; c.sendMessage("You have run out of money."); break; }
	 * else { totalCost += 80000; } c.playerItems[j] =
	 * c.getItems().brokenBarrows[i][0]+1; } } if (breakOut) break; } if
	 * (totalCost > 0) c.getItems().deleteItem(995,
	 * c.getItems().getItemSlot(995), totalCost); }
	 */

	/*
	 * Player Ranks for Quest Tab By: Sunny++
	 */

	public String playerRanks() {
		return Misc.capitalize(c.getRights().name().toLowerCase().replace("_", " "));
	}

	public void getPlayersOnline() {
		if (PlayerHandler.getPlayerCount() == 0) {
			c.sendMessage("@red@There are currently no players online.");
		}
		if (PlayerHandler.getPlayerCount() == 1) {
			c.sendMessage("There is currently @red@" + PlayerHandler.getPlayerCount() + "@bla@ player online.");
		} else {
			c.sendMessage("There are currently @gre@" + PlayerHandler.getPlayerCount() + "@bla@ players online.");
		}
	}

	public void handleLoginText() {
		c.sendMessage("Welcome to " + Config.SERVER_NAME + ".");
		loadQuests();
		// getPlayersOnline();
		c.getPA().sendFrame126("Varrock Teleport", 19641);
		c.getPA().sendFrame126("Teleports you to Varrock.", 19642);
		c.getPA().sendFrame126("Lumbridge Teleport", 19722);
		c.getPA().sendFrame126("Teleports you to Lumbridge.", 19723);
		c.getPA().sendFrame126("Falador Teleport", 19803);
		c.getPA().sendFrame126("Teleports you to Falador.", 19804);
		c.getPA().sendFrame126("Camelot Teleport", 19960);
		c.getPA().sendFrame126("Teleports you to Camelot.", 19961);
		c.getPA().sendFrame126("Ardougne Teleport", 20195);
		c.getPA().sendFrame126("Teleports you to Ardougne.", 20196);
		c.getPA().sendFrame126("Watchtower Teleport", 20354);
		c.getPA().sendFrame126("Teleports you to Watchtower.", 20355);
		c.getPA().sendFrame126("Trollheim Teleport", 7457);
		c.getPA().sendFrame126("Teleports you to Trollheim", 7458);

		c.getPA().sendFrame126("Level 54: Paddewwa Teleport", 21833);
		c.getPA().sendFrame126("Edgeville Dungeon Entrance", 21834);
		c.getPA().sendFrame126("Level 60: Senntisten Teleport", 21933);
		c.getPA().sendFrame126("Digsite Exam Centre", 21934);
		c.getPA().sendFrame126("Level 66: Kharyrll Teleport", 22052);
		c.getPA().sendFrame126("Teleports you to Canafis", 22053);
		c.getPA().sendFrame126("Level 72: Lassar Teleport", 22123);
		c.getPA().sendFrame126("Peak of the Ice Mountain", 22124);
		c.getPA().sendFrame126("Level 78: Dareeyak Teleport", 22232);
		c.getPA().sendFrame126("Ruins west of the Bandit Camp", 22233);
		c.getPA().sendFrame126("Level 84: Carrallangar Teleport", 22307);
		c.getPA().sendFrame126("Graveyard of Shadows", 22308);
		/*
		 * c.getPA().sendFrame126("Falador Teleport", 13037);
		 * c.getPA().sendFrame126("Sailing & Traveling Teleport", 13047);
		 * c.getPA().sendFrame126("Watchtower Teleport", 13055);
		 * c.getPA().sendFrame126("Camelot Teleport", 13063);
		 * 
		 * c.getPA().sendFrame126("Skilling Guilds", 13081); //
		 * c.getPA().sendFrame126("City Teleports", 13089);
		 * c.getPA().sendFrame126("Level 96 : Ghorrock Teleport", 13097);
		 * c.getPA().sendFrame126("Falador Teleport", 1300);
		 * c.getPA().sendFrame126("Teleports you to Varrock.", 1301);
		 * c.getPA().sendFrame126("Sailing & Traveling Teleport", 1325);
		 * c.getPA().sendFrame126("Teleports you to various places", 1326);
		 * c.getPA().sendFrame126("Watchtower Teleport", 1350);
		 * c.getPA().sendFrame126("Teleports you to the Watchtower", 1351);
		 * c.getPA().sendFrame126("Camelot Teleport", 1382);
		 * c.getPA().sendFrame126("Teleports you to the Watchtower", 1383);
		 * c.getPA().sendFrame126("Varrock Teleport", 1415);
		 * c.getPA().sendFrame126("Teleports you to Varrock", 1416);
		 * c.getPA().sendFrame126("Skilling Guilds", 1454);
		 * c.getPA().sendFrame126("Teleports you to any skilling guild", 1455);
		 */
	}

	public void handleWeaponStyle() {
		if (c.fightMode == 0) {
			c.getPA().sendFrame36(43, c.fightMode);
		} else if (c.fightMode == 1) {
			c.getPA().sendFrame36(43, 3);
		} else if (c.fightMode == 2) {
			c.getPA().sendFrame36(43, 1);
		} else if (c.fightMode == 3) {
			c.getPA().sendFrame36(43, 2);
		}
	}

	public void Change() {
		if (c.getItems().playerHasItem(995, 1000000000)) {
			c.sendMessage("You exchange <col=255>1,000,000,000</col> coins for <col=255>billion ticket</col>.");
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 1000000000);
			c.getItems().addItem(620, 1);
		} else if (!c.getItems().playerHasItem(995, 1000000000)) {
			c.sendMessage("You need <col=255>1,000,000,000 </col> coins in order to do this!");
			c.getPA().removeAllWindows();
		}
	}

	public void UnChange() {
		if (c.getItems().playerHasItem(620, 1)) {
			c.sendMessage("You exchange your <col=255>billion ticket </col>for <col=255>1,000,000,000 </col>coins.");
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(620, c.getItems().getItemSlot(620), 1);
			c.getItems().addItem(995, 1000000000);
		} else if (!c.getItems().playerHasItem(620, 1)) {
			c.sendMessage("You need a <col=255>billion ticket </col>in order to do this!");
			c.getPA().removeAllWindows();
		}
	}

	public void displayItemOnInterface(int frame, int item, int slot, int amount) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34);
			c.outStream.writeWord(frame);
			c.outStream.writeByte(slot);
			c.outStream.writeWord(item + 1);
			c.outStream.writeByte(255);
			c.outStream.writeDWord(amount);
			c.outStream.endFrameVarSizeWord();
		}
		// }
	}

	public void hitPlayers(int x, int xx, int y, int yy, int damage) {
		if (c.inArea(x, xx, y, yy)) {
			c.gfx0(287);
			c.handleHitMask(damage);
			c.dealDamage(damage);
		}
	}

	public void climbLadder(final boolean up, final int x, final int y, final int height) {
		c.startAnimation(up ? 828 : 827);
		TaskHandler.submit(new Task(1, true) {

			@Override
			public void execute() {
				c.getPA().movePlayer(x, y, height);
				this.cancel();
			}

			@Override
			public void onCancel() {

			}
		});
	}

}
