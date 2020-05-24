package wind.model.players.packets.clicking;

import wind.Config;
import wind.Server;
import wind.model.items.GameItem;
import wind.model.items.ItemAssistant;
import wind.model.items.impl.MembershipBond;
import wind.model.npcs.pets.PetHandler;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.content.ExperienceLamp;
import wind.model.players.content.PriceChecker;
import wind.model.players.content.QuickCurses;
import wind.model.players.content.QuickPrayers;
import wind.model.players.content.Teles;
import wind.model.players.content.minigames.pestcontrol.PestControlRewards;
import wind.model.players.content.music.MusicTab;
import wind.model.players.content.skills.SkillMasters;
import wind.model.players.content.skills.impl.Cooking;
import wind.model.players.packets.clicking.dialoguebuttons.FiveOptions;
import wind.model.players.packets.clicking.dialoguebuttons.FourOptions;
import wind.model.players.packets.clicking.dialoguebuttons.TwoOptions;
import wind.model.players.teleport.Teleports;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.Clan.Rank;

/**
 * @author 7Winds Used to organize clicking buttons.
 */
public class FirstClickButton {

	/**
	 * @param c
	 * @param actionButtonId
	 *            Handles all first click buttons in game
	 */
	public static void handleClick(Client c, int actionButtonId) {
		int itemId = c.getInStream().readUnsignedWordA();
		if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
			Misc.println(c.playerName + " - actionbutton: " + actionButtonId);
		}
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
		if (c.duelStatus > 0 && c.duelStatus < 5) {
			c.getPA().cancelTeleportTask();
			switch (actionButtonId) {
			/*
			 * case 83093: c.getPA().showInterface(15106); //
			 * c.getItems().writeBonus(); break;
			 */
			
			case 9157:
				if (c.usingROD) {
					c.sendMessage("hi");
					c.getPA().startTeleport(3367, 3267, 0, "modern");
					Teles.rings(c);	
				}
				if (c.dialogueAction == 50) {
					c.getPA().startTeleport(2898, 3562, 0, "modern");
					Teles.necklaces(c);
				}
				break;
			case 9158:
				if (c.usingROD)  {
					c.getPA().startTeleport(2441, 3090, 0, "modern");
					Teles.rings(c);	
				}
				if (c.dialogueAction == 50) {
					c.getPA().startTeleport(2552, 3558, 0, "modern");
					Teles.necklaces(c);
				}
				break;
			case 9178:
				if (c.dialogueAction == 51)
					c.getPA().startTeleport(3088, 3500, 0, "modern");
					Teles.necklaces(c);
					break;
			case 9179:
				if (c.dialogueAction == 51)
					c.getPA().startTeleport(3293, 3174, 0, "modern");
					Teles.necklaces(c);
					break;
			case 9180:
				if (c.dialogueAction == 51)
					c.getPA().startTeleport(2911, 3152, 0, "modern");
					Teles.necklaces(c);
					break;
			case 9181:
				if (c.dialogueAction == 51)
					c.getPA().startTeleport(3103, 3249, 0, "modern");
					Teles.necklaces(c);
					break;
			case 10252:
				ExperienceLamp.buttonClick(c, 1);
				break;
			case 10253:
				ExperienceLamp.buttonClick(c, 2);
				break;
			case 10254:
				ExperienceLamp.buttonClick(c, 3);
				break;
			case 10255:
				ExperienceLamp.buttonClick(c, 4);
				break;
			case 11000:
				ExperienceLamp.buttonClick(c, 5);
				break;
			case 11001:
				ExperienceLamp.buttonClick(c, 6);
				break;
			case 11002:
				ExperienceLamp.buttonClick(c, 7);
				break;
			case 11003:
				ExperienceLamp.buttonClick(c, 8);
				break;
			case 11004:
				ExperienceLamp.buttonClick(c, 9);
				break;
			case 11005:
				ExperienceLamp.buttonClick(c, 10);
				break;
			case 11006:
				ExperienceLamp.buttonClick(c, 11);
				break;
			case 11007:
				ExperienceLamp.buttonClick(c, 12);
				break;
			case 47002:
				ExperienceLamp.buttonClick(c, 20);
				break;
			case 54090:
				ExperienceLamp.buttonClick(c, 21);
				break;
			case 11008:
				ExperienceLamp.buttonClick(c, 13);
				break;
			case 11009:
				ExperienceLamp.buttonClick(c, 14);
				break;
			case 11010:
				ExperienceLamp.buttonClick(c, 15);
				break;
			case 11011:
				ExperienceLamp.buttonClick(c, 16);
				break;
			case 11012:
				ExperienceLamp.buttonClick(c, 17);
				break;
			case 11013:
				ExperienceLamp.buttonClick(c, 18);
				break;
			case 11014:
				ExperienceLamp.buttonClick(c, 19);
				break;
			case 11015:
				ExperienceLamp.buttonClick(c, 20);
				break;
			case 33206:
				c.getPA().showInterface(8714);
				break;

			default:
				if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR))
					c.sendMessage("Unhandled actionButtonId: " + actionButtonId);
				else
					System.out.println("Unhandled actionButtonId: " + actionButtonId);
				break;
			}
		}

		if (actionButtonId >= 67050 && actionButtonId <= 67075) {
			if (c.CursesOn == false)
				QuickPrayers.clickPray(c, actionButtonId);
			else
				QuickCurses.clickCurse(c, actionButtonId);
		}
		PestControlRewards.handlePestButtons(c, actionButtonId);
		Teleports.handleClick(c, actionButtonId);
		MusicTab.handleClick(c, actionButtonId);
		PriceChecker.handleClose(c, actionButtonId);
		switch (actionButtonId) {
		/*
		 * case 3205: MembershipBond.openInterface(c); break;
		 */
		case 25222:
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			break;
		case 25219:
			TaskHandler.submit(new Task(0, true) {

				@Override
				public void execute() {
					c.sendMessage("Redirecting you to the purchase page...");
					this.cancel();
				}

				@Override
				public void onCancel() {

				}
			});
			TaskHandler.submit(new Task(9, false) {

				@Override
				public void execute() {
					c.getPA().sendFrame126("www.deadmanps.net/forums/", 6600);
					this.cancel();

				}

				@Override
				public void onCancel() {
					System.out.println("Directed " + c.playerName + " to the donation page.");
				}
			});
			break;
		case 25216:
			if ((System.currentTimeMillis() - c.tabTimer) > 1000) {
				c.tabTimer = System.currentTimeMillis();
				MembershipBond.convertFromInterface(c);
			} else
				c.sendMessage("You can only do this every couple seconds.");
			break;
		case 25212:
			if ((System.currentTimeMillis() - c.tabTimer) > 1000) {
				c.tabTimer = System.currentTimeMillis();
				if (c.tradeBond > 0) {
					MembershipBond.redeemFromInterface(c, 13190);
				} else
					c.sendMessage("You don't have any bonds to redeem.");
			}
			break;
		case 83093:
			c.getPA().showInterface(21172);
			break;
		case 83051:
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			break;
		case 25206:
			if (c.tradeBond > 0) {
				if ((System.currentTimeMillis() - c.tabTimer) > 1000) {
					c.tabTimer = System.currentTimeMillis();
					c.tradeBond -= 1;
					c.sendMessage("You've withdrawn a tradeable bond.");
					c.getItems().addItem(13190, 1);
				}
				MembershipBond.updateInterface(c);
			} else
				c.sendMessage("You don't have any bonds to withdraw.");
			break;
		case 25209:
			if (c.untradeBond > 0) {
				if ((System.currentTimeMillis() - c.tabTimer) > 1000) {
					c.tabTimer = System.currentTimeMillis();
					c.untradeBond -= 1;
					c.sendMessage("You've withdrawn an untradeable bond.");
					c.getItems().addItem(13192, 1);
				}
				MembershipBond.updateInterface(c);
			} else
				c.sendMessage("You don't have any bonds to withdraw.");
			break;
		case 71074:
			break;

		case 59163:
		case 9118:
			c.getPA().closeAllWindows();
			break;

		// Zulrah metamorphosis
		case 88174:
			c.getPA().closeAllWindows();
			break;
		case 87230:
			c.getPA().closeAllWindows();
			break;
		case 88074:
			c.getPA().closeAllWindows();
			break;
		case 89018:
			c.getPA().closeAllWindows();
			break;

		case 57226: // Soft leather
		case 57225: // Hard leather
		case 57227: // Green d-hide
		case 57228: // Blue d-hide
		case 57229: // Red d-hide
		case 57230: // Black d-hide
		case 57231: // Unused
		case 57232: // Unused
			// if (c.tanning) {
			c.getTanning().handleActionButton(actionButtonId);
			// }
			break;
		// green
		/*
		 * case 89025: c.getPA().showInterface(22700); break; case 88081:
		 * c.getPA().showInterface(22700); break;
		 */
		// red
		/*
		 * case 89028: c.getPA().showInterface(22600); break; case 88184:
		 * c.getPA().showInterface(22600); break;
		 */
		case 87237:
		case 174065:
			c.getPA().showInterface(22700);
			c.setColorSelect("green");
			break;
		case 87240:
			c.getPA().showInterface(44600);
			c.setColorSelect("red");
			break;
		case 87234:
		case 174062:
			c.getPA().showInterface(22800);
			c.setColorSelect("blue");
			break;
		case 89035:
		case 88191:
		case 88091:
		case 174075:
			int newId = 0;
			switch (c.getColorSelect()) {
			case "none":
			case "":
				newId = c.petID;
				break;
			case "green":
				newId = 2130;
				break;
			case "red":
				newId = 2131;
				break;
			case "blue":
				newId = 2132;
				break;
			}
			PetHandler.morph(c, newId);
			c.sendMessage("Your newly morphed pet begins to follow you delightfully.");
			break;

		case 59148:
			// c.sendMessage("You have set the title.");
			break;

		case 59151:
			c.clan.whoCanJoin = Rank.ANYONE;
			c.getPA().sendFrame126("Anyone", 15255);
			c.sendMessage("Anyone can join your clan now.");
			break;

		case 62135:
			c.clan.whoCanJoin = Rank.FRIEND;
			c.getPA().sendFrame126("Friend", 15255);
			c.sendMessage("Only friends can join your clan.");
			break;

		case 121029:
			c.setSidebarInterface(11, 31040); // wrench tab
			break;
		case 121032:
			c.setSidebarInterface(11, 31060); // wrench tab
			break;

		case 121035:
			c.setSidebarInterface(11, 31080); // wrench tab
			break;

		case 121065:
			c.setSidebarInterface(11, 904); // wrench tab
			break;
		case 89232:
		case 226162: // Deposit Inventory
			for (int i = 0; i < c.playerItems.length; i++) {
				c.getItems().bankItem(c.playerItems[i], i, c.playerItemsN[i]);

			}
			break;
		case 89236:
		case 226170:// depositworn items
			for (int r = 0; r < c.playerEquipment.length; r++) {
				try {
					int item2 = c.playerEquipment[r];
					if ((item2 > 0) && (item2 < 29999)) {
						c.getItems().removeItem(item2, r);
					}
				} catch (Exception e) {

				}
			}
			for (int z = 0; z < 101; z++) {
				for (int t = 0; t < 28; t++) {
					c.getItems().bankItem(z, t, 2147000000);
				}
			}
			break;

		case 121068:
		case 121085:
			break;

		case 71071: // Price Checker Exit
			if (c.isChecking) {
				c.getPA().closeAllWindows();
			} else {
				return;
			}
			break;

		// Clan Chat - Join Clan
		case 70209:
			if (c.clanId >= 0) {
				c.sendMessage("You are already in a clan.");
				break;
			}
			if (c.clan != null) {
				c.clan.removeMember(c);
			} else if (c.getOutStream() != null) {
				c.getOutStream().createFrame(187);
				c.flushOutStream();
			}
			break;
		/**
		 * Swapping from Quest Tab
		 */
		case 114119:
		case 113228:
			c.setSidebarInterface(2, 29265); // Swap to Achievements
			break;
		case 114118:
		case 114083:
			c.setSidebarInterface(2, 638); // Swap to Quests
			break;
		case 114087:
		case 114086:
			c.setSidebarInterface(2, 29300); // Swap to Minigames
			break;
		case 114121:
			if (c.crabsKilled == 25 || c.crabsKilled >= 25) {
				c.sendMessage("You have completed this Task.");
			} else {
				c.sendMessage("Amount of Rock crabs killed: " + c.crabsKilled + ".");
				c.sendMessage("You must kill 25 Rock crabs to complete this Task");
			}
			break;
		// case 114122:
		// if (c.playersKilled == 25 || c.playersKilled >= 25) {
		// c.sendMessage("You have completed this Task.");
		// } else {
		// c.sendMessage("Amount of Players killed: "+c.playersKilled+".");
		// c.sendMessage("You must kill 25 Players to complete this Task");
		// }
		// break;

		/* Rotten Potato */
		case 86040: // Open Bank
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
				c.getPA().openUpBank();
			} else {
				c.sendMessage("You cannot use this.");
				return;
			}
			break;

		case 86041: // Reset Bank PIN to 2468
			c.sendMessage("Coming soon.");
			break;

		case 86042: // Wipe Bank
			c.sendMessage("Coming soon.");
			break;

		case 86043: // Sunny's PKer
			c.sendMessage("Coming soon.");
			break;

		case 86044: // Toggle Invisibility
			c.sendMessage("Coming soon.");
			break;

		case 86045: // Holiday Item Spawn
			c.sendMessage("Coming soon.");
			break;
		/* Rotten Potato */

		case 70212:
			c.getPA().showInterface(15456);
			break;

		/** Prayers **/
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;
		case 77100: // range
			c.getCombat().activatePrayer(3);
			break;
		case 77102: // mage
			c.getCombat().activatePrayer(4);
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			break;
		case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;
		case 77104: // 26 range
			c.getCombat().activatePrayer(11);
			break;
		case 77106: // 27 mage
			c.getCombat().activatePrayer(12);
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
		case 77109: // 44 range
			c.getCombat().activatePrayer(19);
			break;
		case 77111: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;
		case 2171: // retrui
			c.getCombat().activatePrayer(21);
			break;
		case 2172: // redem
			c.getCombat().activatePrayer(22);
			break;
		case 2173: // smite
			c.getCombat().activatePrayer(23);
			break;
		case 77113: // chiv
			c.getCombat().activatePrayer(24);
			break;
		case 77115: // piety
			c.getCombat().activatePrayer(25);
			break;

		/*
		 * case 33206:// Attack button case 34142: SkillGuides.atkInterface(c);
		 * break; case 33209:// str button case 34119:
		 * SkillGuides.strInterface(c); break; case 33212: //Defence case 34120:
		 * SkillGuides.defInterface(c); break; case 34133: case 33215: //Range
		 * SkillGuides.rangeInterface(c); break; case 34123: case 33207:
		 * //Hitpoints //SkillGuides.hpInterface(c); break; case 34139: case
		 * 33218: //Prayer SkillGuides.prayInterface(c); break; case 34136: case
		 * 33221: //Magic
		 * 
		 * SkillGuides.mageInterface(c); break; case 34155: case 33224:
		 * //Runecrafting SkillGuides.rcInterface(c); break; case 34158: case
		 * 33210: //Agility SkillGuides.agilityInterface(c); break; case 34161:
		 * case 33213: //Herblore SkillGuides.herbloreInterface(c); break; case
		 * 59199: case 33216: //Theiving SkillGuides.thievingInterface(c);
		 * break; case 59202: case 33219: //craft
		 * SkillGuides.craftingInterface(c); break; case 33222: //Fletching
		 * SkillGuides.fletchingInterface(c); break; case 59205: case 47130:
		 * //Slayer SkillGuides.slayerInterface(c); break; case 33208: //Mining
		 * SkillGuides.miningInterface(c); break; case 33211: //Smithing
		 * SkillGuides.smithingInterface(c); break; case 33214: //Fishing
		 * SkillGuides.fishingInterface(c); break; case 33217: //Cooking
		 * SkillGuides.cookingInterface(c); break; case 33220: //Firemaking
		 * SkillGuides.firemakingInterface(c); break; case 33223: //Woodcutting
		 * SkillGuides.woodcuttingInterface(c); break; case 54104: //Farming
		 * SkillGuides.farmingInterface(c); break;
		 */

		case 55095:
			c.getPA().destroyItem(itemId);
			break;

		case 55096:
			c.getPA().closeAllWindows();
			break;

		case 74212:
		case 121046:
			c.getPA().startTeleport(Config.POH_X, Config.POH_Y, 0, "modern");
			break;
		case 3057:
			if (c.getItems().playerHasItem(995, 1000)) {
			c.getPA().closeAllWindows();
			c.getPA().startTeleport(2465, 3501, 3, "modern");
			c.getItems().deleteItem2(995, 1000);
		} else {
			c.sendMessage("You must pay 1000 gp to ride this glider.");
		}
			break;
		case 3058:
			if (c.getItems().playerHasItem(995, 1000)) {
			c.getPA().closeAllWindows();
			c.getPA().startTeleport(2848, 3497, 0, "modern");
			c.getItems().deleteItem2(995, 1000);
		} else {
			c.sendMessage("You must pay 1000 gp to ride this glider.");
		}
			break;
		case 3059:
			if (c.getItems().playerHasItem(995, 1000)) {
			c.getPA().closeAllWindows();
			c.getPA().startTeleport(3321, 3427, 0, "modern");
			c.getItems().deleteItem2(995, 1000);
			} else {
				c.sendMessage("You must pay 1000 gp to ride this glider.");
			}
			break;
		case 3060:
			if (c.getItems().playerHasItem(995, 1000)) {
			c.getPA().closeAllWindows();
			c.getPA().startTeleport(3278, 3212, 0, "modern");
			c.getItems().deleteItem2(995, 1000);
		} else {
			c.sendMessage("You must pay 1000 gp to ride this glider.");
		}
			break;
		case 3056:
			if (c.getItems().playerHasItem(995, 1000)) {
			c.getPA().closeAllWindows();
			c.getPA().startTeleport(2894, 2730, 0, "modern");
			c.getItems().deleteItem2(995, 1000);
	} else {
		c.sendMessage("You must pay 1000 gp to ride this glider.");
	}
			break;
		case 48054:
			if (c.getItems().playerHasItem(995, 1000)) {
			c.getPA().closeAllWindows();
			c.getPA().startTeleport(2544, 2970, 0, "modern");
			c.getItems().deleteItem2(995, 1000);
} else {
	c.sendMessage("You must pay 1000 gp to ride this glider.");
}
			break;

		case 121047:
			c.sendMessage("Coming soon.");
			break;

		case 21008:
			c.getPA().closeAllWindows();
			break;

		case 164034:
			c.removedTasks[0] = -1;
			c.getSlayer().updateCurrentlyRemoved();
			break;

		case 164035:
			c.removedTasks[1] = -1;
			c.getSlayer().updateCurrentlyRemoved();
			break;

		case 164036:
			c.removedTasks[2] = -1;
			c.getSlayer().updateCurrentlyRemoved();
			break;

		case 164037:
			c.removedTasks[3] = -1;
			c.getSlayer().updateCurrentlyRemoved();
			break;

		case 164028:
			c.getSlayer().cancelTask();
			break;
		case 164029:
			c.getSlayer().removeTask();
			break;
		case 160052:
			c.getSlayer().buySlayerExperience();
			break;
		case 160053:
			c.getSlayer().buyRespite();
			break;
		case 160054:
			c.getSlayer().buySlayerDart();
			break;
		case 160055:
			c.getSlayer().buyBroadArrows();
			break;

		case 160045:
		case 162033:
		case 164021:
			if (c.interfaceId != 41000)
				c.getSlayer().handleInterface("buy");
			break;

		case 160047:
		case 162035:
		case 164023:
			if (c.interfaceId != 41500)
				c.getSlayer().handleInterface("learn");
			break;

		case 160049:
		case 162037:
		case 164025:
			if (c.interfaceId != 42000)
				c.getSlayer().handleInterface("assignment");
			break;

		case 162030:
		case 164018:
		case 160042:
			c.getPA().removeAllWindows();
			break;

		case 59135:// center
			c.fightPitsOrb("Centre", 15239);
			c.getPA().movePlayer(2398, 5150, 0);
			c.hidePlayer();
			break;

		case 59136:// north-west
			c.fightPitsOrb("North-West", 15240);
			c.getPA().movePlayer(2384, 5157, 0);
			c.hidePlayer();
			break;

		case 59137:// north-east
			c.fightPitsOrb("North-East", 15241);
			c.getPA().movePlayer(2409, 5158, 0);
			c.hidePlayer();
			break;

		case 59138:// south-east
			c.fightPitsOrb("South-East", 15242);
			c.getPA().movePlayer(2411, 5137, 0);
			c.hidePlayer();
			break;
		case 59139:// south-west
			c.fightPitsOrb("South-West", 15243);
			c.getPA().movePlayer(2388, 5138, 0);
			c.hidePlayer();
			break;
		case 17111: // Stop Viewing
			c.getPA().movePlayer(2399, 5173, 0);
			c.isNpc = false;
			c.canWalk = true;
			c.exitViewingOrb();
			c.showPlayer();
			break;
		/*
		 * case 74108: //skillcapeshere SkillcapeData.handleSkillcape(c); break;
		 */

		/* Lamp */
		case 10252:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 0;
				c.sendMessage("You are now protecting @red@Attack@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 0;
				c.sendMessage("You are now protecting @red@Attack@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 0;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Attack@bla@, you may protect one more combat skill.");
			}
			break;
		case 10253:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 2;
				c.sendMessage("You are now protecting @red@Strength@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 2;
				c.sendMessage("You are now protecting @red@Strength@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 2;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Strength@bla@, you may protect one more combat skill.");
			}
			break;
		case 10254:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 4;
				c.sendMessage("You are now protecting @red@Range@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 4;
				c.sendMessage("You are now protecting @red@Range@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 4;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Range@bla@, you may protect one more combat skill.");
			}
			break;
		case 10255:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 6;
				c.sendMessage("You are now protecting @red@Magic@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 6;
				c.sendMessage("You are now protecting @red@Magic@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 6;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Magic@bla@, you may protect one more combat skill.");
			}
			break;
		case 11000:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 1;
				c.sendMessage("You are now protecting @red@Defense@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 1;
				c.sendMessage("You are now protecting @red@Defense@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 1;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Defense@bla@, you may protect one more combat skill.");
			}
			break;
		case 11001:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 3;
				c.sendMessage("You are now protecting @red@Hitpoints@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 3;
				c.sendMessage("You are now protecting @red@Hitpoints@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 3;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Hitpoints@bla@, you may protect one more combat skill.");
			}
			break;
		case 11002:
			if (c.combatProtected1 == -1) {
				c.combatProtected1 = 5;
				c.sendMessage("You are now protecting @red@Prayer@bla@, you may protect one more combat skill.");
			} else if (!(c.combatProtected1 == -1) && c.combatProtected2 == -1) {
				c.combatProtected2 = 5;
				c.sendMessage("You are now protecting @red@Prayer@bla@ and " + c.combatProtected1() + ".");
			} else if (!(c.combatProtected1 == -1) && !(c.combatProtected2 == -1)) {
				c.combatProtected1 = 5;
				c.combatProtected2 = -1;
				c.sendMessage("You are now protecting @red@Prayer@bla@, you may protect one more combat skill.");
			}
			break;
		case 11003:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 16;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 16;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 16;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 16;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11004:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 15;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 15;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 15;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 15;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11005:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 17;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 17;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 17;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 17;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11006:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 12;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 12;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 12;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 12;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11007:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 20;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 20;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 20;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 20;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 47002:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 18;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 18;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 18;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 18;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 54090:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 19;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 19;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 19;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 19;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11008:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 14;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 14;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 14;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 14;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11009:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 13;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 13;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 13;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 13;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11010:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 10;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 10;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 10;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 10;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11011:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 7;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 7;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 7;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 7;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11012:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 11;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 11;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 11;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 11;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11013:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 8;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 8;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 8;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 8;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11014:
			if (c.skillProtected1 == -1) {
				c.skillProtected1 = 9;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && c.skillProtected2 == -1) {
				c.skillProtected2 = 9;
				c.sendMessage("You are now protecting " + c.skillProtected1() + "and " + c.skillProtected2() + ", you may protect two more skills.");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && c.skillProtected3 == -1) {
				c.skillProtected3 = 9;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", " + c.skillProtected2() + "and " + c.skillProtected3() + ".");
			} else if (!(c.skillProtected1 == -1) && !(c.skillProtected2 == -1) && !(c.skillProtected3 == -1)) {
				c.skillProtected1 = 9;
				c.skillProtected2 = -1;
				c.skillProtected3 = -1;
				c.sendMessage("You are now protecting " + c.skillProtected1() + ", you may protect two more skills.");
			}
			break;
		case 11015:
			if (c.usingLamp) {
				if (c.antiqueLamp && !c.normalLamp) {
					c.usingLamp = false;
					c.getPA().addSkillXP(50000, c.antiqueSelect);
					c.getItems().deleteItem2(4447, 1);
					c.sendMessage("The lamp mysteriously vanishes...");
					c.getPA().closeAllWindows();
				}
				if (c.normalLamp && !c.antiqueLamp) {
					c.usingLamp = false;
					c.getPA().addSkillXP(10000, c.antiqueSelect);
					c.getItems().deleteItem2(2528, 1);
					c.sendMessage("The lamp mysteriously vanishes...");
					c.getPA().closeAllWindows();
				}
			} else {
				c.getPA().closeAllWindows();
				c.sendMessage("You are currently protecting " + c.combatProtected1() + ", " + c.combatProtected2() + ", "
						+ c.skillProtected1() + ", " + c.skillProtected2() + " and " + c.skillProtected3() + ".");
				return;
			}
			break;
		/* End lamp */

		case 90076: // Bank All (Inventory)
			c.getPA().bankAll();
			break;

		case 90080: // Bank Equipment
			c.getPA().bankEquip();
			break;

		case 67079: // quick curse confirm
			QuickCurses.clickConfirm(c);
			break;

		case 19136: // Toggle quick prayers
			if (c.quickPray || c.quickCurse) {
				QuickCurses.turnOffQuicks(c);
				return;
			}
			QuickCurses.turnOnQuicks(c);
			break;

		case 19137: // select your quick prayers/curses
			if (c.CursesOn) {
				QuickCurses.selectQuickInterface(c);
				c.getPA().sendFrame106(5);
			} else if (!c.CursesOn) {
				QuickCurses.selectQuickInterface(c);
				c.getPA().sendFrame106(5);
			}
			break;

		/* Quest Tab by Sunny++ */
		case 28164:
			c.sendMessage(
					"There is currently <col=FF0000>" + PlayerHandler.getPlayerCount() + "</col> players online.");
			break;

		case 28165:
			c.sendMessage("Your username is: <col=FF0000>" + c.playerName + "<col>.");
			break;

		case 28166:
			c.sendMessage("Your rank is : <col=FF0000>" + c.getPA().playerRanks() + "</col>.");
			break;

		case 28168:
			c.sendMessage("You currently have <col=FF0000>" + c.donPoints + "</col> .");
			break;

		case 28171:
			c.sendMessage("You have <col=FF0000>" + c.pkp + " </col>Pk Points.");
			break;
		case 28170:
			c.sendMessage("You have killed: <col=FF0000>" + c.KC + " </col>players.");
			break;
		case 28172:
			c.sendMessage("You have died: <col=FF0000>" + c.DC + " </col>times.");
			break;

		case 28178:
			if (c.KC == 0 && c.DC == 0) {
				c.sendMessage("Your KDR is: <col=FF0000>" + c.KC + "</col>/<col=FF0000>" + c.DC + "</col> .");
			} else {
				c.sendMessage("Your KDR is: <col=00AF33>" + c.KC + "</col/<col=00AF33>" + c.DC + "</col> .");
			}
			break;

		case 28215:
			if (c.expLock == false) {
				c.expLock = true;
				c.sendMessage("Your experience is now <col=FF0000>locked</col>. You will not gain experience.");
				c.getPA().sendFrame126("@whi@EXP: @red@LOCKED", 7383);
			} else {
				c.expLock = false;
				c.sendMessage("Your experience is now <col=00AF33>unlocked</col>. You will gain experience.");
				c.getPA().sendFrame126("@whi@EXP: @gre@UNLOCKED", 7383);
			}
			break;
		case 28173:
			if (c.slayerTask <= 0) {
				c.sendMessage("You do not have a task, please talk with a slayer master.");
			} else {
				c.forcedText = "I must slay another " + c.taskAmount + " "
						+ Server.npcHandler.getNpcListName(c.slayerTask) + ".";
				c.forcedChatUpdateRequired = true;
				Player.updateRequired = true;
			}
			break;
		case 28174:
			c.sendMessage("You have <col=FF0000>" + c.donPoints + "</col>.");
			break;
		/* End Quest */

		case 15147:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15146:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 5;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15247:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 10;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 9110:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 28;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15151:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = c.getItems().getItemCount(440);
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15149:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 10;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15150:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 5;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15148:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 28;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15159:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15158:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 5;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15157:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 10;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15156:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 28;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29017:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29016:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 5;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 24253:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 10;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 16062:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 28;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 29022:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29020:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 5;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 29019:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 10;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 29018:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 28;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 29026:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29025:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 5;
				c.getSmithing().startSmelting(c.smeltType);
			}

		case 29024:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 10;
				c.getSmithing().startSmelting(c.smeltType);
			}

		case 29023:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 28;
				c.getSmithing().startSmelting(c.smeltType);
			}
		case 58074:
			c.getBankPin().closeBankPin();
			break;
		case 58073:
			if (c.hasBankpin && !c.requestPinDelete) {
				c.requestPinDelete = true;
				c.getBankPin().dateRequested();
				c.getBankPin().dateExpired();
				// FreeDialogues.handledialogue(c, 1017, 1);
				// c.getDH().sendDialogues(1017, c.npcType);
				c.getDH().sendDialogues(1017, 494);
				c.sendMessage("[Notice] A PIN delete has been requested. Your PIN will be deleted in "
						+ c.getBankPin().recovery_Delay + " days.");
				c.sendMessage("To cancel this change just type in the correct PIN.");
			} else {
				c.sendMessage("[Notice] Your PIN is already pending deletion. Please wait the entire 2 days.");
				c.getPA().closeAllWindows();
			}
			break;
		case 14067:
			c.appearanceUpdateRequired = true;
			break;
		case 58025:
		case 58026:
		case 58027:
		case 58028:
		case 58029:
		case 58030:
		case 58031:
		case 58032:
		case 58033:
		case 58034:
			c.getBankPin().bankPinEnter(actionButtonId);
			break;

		case 58230:
		case 20174: // Bank Pin
			/*
			 * if (!c.hasBankpin) { c.getBankPin().openPin(); } else if
			 * (c.hasBankpin && c.enterdBankpin) {
			 * c.getBankPin().resetBankPin(); c.sendMessage(
			 * "Your PIN has been deleted as requested."); } else {
			 * c.sendMessage(
			 * "Please enter your Bank Pin before requesting a delete.");
			 * c.sendMessage(
			 * "You can do this by simply opening your bank. This is to verify it's really you."
			 * ); c.getPA().closeAllWindows(); }
			 */
			break;

		/*
		 * case 58025: case 58026: case 58027: case 58028: case 58029: case
		 * 58030: case 58031: case 58032: case 58033: case 58034:
		 * c.getBankPin().pinEnter(actionButtonId); break;
		 */

		case 53152:
			Cooking.getAmount(c, 1);
			break;
		case 53151:
			Cooking.getAmount(c, 5);
			break;
		case 53150:
			Cooking.getAmount(c, 10);
			break;
		case 53149:
			Cooking.getAmount(c, 28);
			break;

		case 33206: // Attack
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.attackSkill = true;
			c.calculateCombatLevel();
			break;
		case 33209: // Strength
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.strengthSkill = true;
			c.calculateCombatLevel();
			break;
		case 33212: // Defence
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.defenceSkill = true;
			c.calculateCombatLevel();
			break;
		case 33215: // Range
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.rangeSkill = true;
			c.calculateCombatLevel();
			break;
		case 33218: // Prayer
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.prayerSkill = true;
			c.calculateCombatLevel();
			break;
		case 33221: // Mage
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.mageSkill = true;
			c.calculateCombatLevel();
			break;
		case 33207: // HP
			if (c.duelStatus == 5 || c.inWild()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.healthSkill = true;
			c.calculateCombatLevel();
			break;

		case 33224: // runecrafting
			c.getSI().runecraftingComplex(1);
			c.getSI().selected = 6;
			break;

		case 33210: // agility
			c.getSI().agilityComplex(1);
			c.getSI().selected = 8;
			break;
		case 33213: // herblore
			c.getSI().herbloreComplex(1);
			c.getSI().selected = 9;
			break;
		case 33216: // theiving
			c.getSI().thievingComplex(1);
			c.getSI().selected = 10;
			break;
		case 33219: // crafting
			c.getSI().craftingComplex(1);
			c.getSI().selected = 11;
			break;
		case 33222: // fletching
			c.getSI().fletchingComplex(1);
			c.getSI().selected = 12;
			break;
		case 47130:// slayer
			c.getSI().slayerComplex(1);
			c.getSI().selected = 13;
			break;
		case 33214: // fishing
			c.getSI().fishingComplex(1);
			c.getSI().selected = 16;
			break;
		case 33217: // cooking
			c.getSI().cookingComplex(1);
			c.getSI().selected = 17;
			break;
		case 33220: // firemaking
			c.getSI().firemakingComplex(1);
			c.getSI().selected = 18;
			break;
		case 33223: // woodcut
			c.getSI().woodcuttingComplex(1);
			c.getSI().selected = 19;
			break;
		case 54104: // farming
			c.getSI().farmingComplex(1);
			c.getSI().selected = 20;
			break;

		case 34142: // tab 1
			c.getSI().menuCompilation(1);
			break;

		case 34119: // tab 2
			c.getSI().menuCompilation(2);
			break;

		case 34120: // tab 3
			c.getSI().menuCompilation(3);
			break;

		case 34123: // tab 4
			c.getSI().menuCompilation(4);
			break;

		case 34133: // tab 5
			c.getSI().menuCompilation(5);
			break;

		case 34136: // tab 6
			c.getSI().menuCompilation(6);
			break;

		case 34139: // tab 7
			c.getSI().menuCompilation(7);
			break;

		case 34155: // tab 8
			c.getSI().menuCompilation(8);
			break;

		case 34158: // tab 9
			c.getSI().menuCompilation(9);
			break;

		case 34161: // tab 10
			c.getSI().menuCompilation(10);
			break;

		case 59199: // tab 11
			c.getSI().menuCompilation(11);
			break;

		case 59202: // tab 12
			c.getSI().menuCompilation(12);
			break;
		case 59203: // tab 13
			c.getSI().menuCompilation(13);
			break;

		case 89061:
		case 150:
			if (c.autoRet == 0)
				c.autoRet = 1;
			else
				c.autoRet = 0;
			break;

		case 9190:
			FiveOptions.handleOption1(c);
			break;

		case 9191:
			FiveOptions.handleOptions2(c);
			break;

		case 9192:
			FiveOptions.handleOption3(c);
			break;

		case 9193:
			FiveOptions.handleOption4(c);
			break;

		case 9194:
			FiveOptions.handleOption5(c);
			break;

		case 34185:
		case 34184:
		case 34183:
		case 34182:
		case 34189:
		case 34188:
		case 34187:
		case 34186:
		case 34193:
		case 34192:
		case 34191:
		case 34190:
		case 33187:
		case 33190:
		case 33193:
		case 33196:
		case 33199:
		case 33202:
		case 33205:
		case 33186: //x5 leather
		case 33189:
		case 33192:
		case 33195:
		case 33198:
		case 33201:
		case 33204:
		case 33185: //x10 leather
		case 33188:
		case 33191:
		case 33194:
		case 33197:
		case 33200:
		case 33203:
			 if (c.craftingLeather)
			 c.getCrafting().handleCraftingClick(actionButtonId);
			if (c.getFletching().fletching)
				c.getFletching().handleFletchingClick(actionButtonId);
			break;

		// case 58253:
		case 108005:
			if (c.inTrade) {
				return;
			}
			c.getPA().showInterface(15106);
			// c.getEquipment().writeBonus();
			break;
		case 108006: // items kept on death
			if (c.inTrade) {
				return;
			}
			c.getPA().sendFrame126("Item's Kept on Death", 17103);
			c.StartBestItemScan(c);
			c.EquipStatus = 0;
			for (int k = 0; k < 4; k++)
				c.getPA().sendFrame34a(10494, -1, k, 1);
			for (int k = 0; k < 39; k++)
				c.getPA().sendFrame34a(10600, -1, k, 1);
			if (c.WillKeepItem1 > 0)
				c.getPA().sendFrame34a(10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
			if (c.WillKeepItem2 > 0)
				c.getPA().sendFrame34a(10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
			if (c.WillKeepItem3 > 0)
				c.getPA().sendFrame34a(10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
			if (c.WillKeepItem4 > 0 && c.prayerActive[10])
				c.getPA().sendFrame34a(10494, c.WillKeepItem4, 3, 1);
			for (int ITEM = 0; ITEM < 28; ITEM++) {
				if (c.playerItems[ITEM] - 1 > 0
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM]);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt1) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt2) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt3) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)
						&& c.playerItemsN[ITEM] > 1) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - 1);
					c.EquipStatus += 1;
				}
			}
			for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
				if (c.playerEquipment[EQUIP] > 0
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt1 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt2 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt3 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - 1 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - 1);
					c.EquipStatus += 1;
				}
			}
			c.ResetKeepItems();
			c.getPA().showInterface(17100);
			break;

		case 108003: // PriceChecker
			PriceChecker.open(c);
			break;

		case 59107:
			c.getPA().removeAllWindows();
			break;

		case 59004:
			c.getPA().removeAllWindows();
			break;

		case 9178:
			FourOptions.handleOption1(c);
			break;

		case 9179:
			FourOptions.handleOption2(c);
			break;

		case 9180:
			FourOptions.handleOption3(c);
			break;

		case 9181:
			FourOptions.handleOption4(c);
			break;

		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getPA().resetAutocast();
			} else {
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[c.playerWeapon] == 4675)
						c.setSidebarInterface(0, 1689);
					else
						c.sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[c.playerWeapon] == 4170) {
						c.setSidebarInterface(0, 12050);
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		/**
		 * Dialogue Handling
		 */
		case 9157:
			TwoOptions.handleOption1(c);
			break;

		case 9158:
			TwoOptions.handleOption2(c);
			break;

		case 9167:
			switch (c.dialogueAction) {
			case 251:
				c.getPA().openUpBank();
				break;

			case 426: // Fletching Shop
				c.getShops().openShop(48);
				break;

			case 436: // Firemaking Shop
				c.getShops().openShop(47);
				break;

			case 508:
				c.getDH().sendDialogues(1030, 925);
				break;

			case 502:
				c.getDH().sendDialogues(1030, 925);
				break;

			case 2245:
				c.getPA().startTeleport(2110, 3915, 0, "modern");
				c.sendMessage("High Priest teleported you to <col=FF0000>Lunar Island</col>.");
				c.getPA().closeAllWindows();
				break;
			}
			if (c.doricOption) {
				c.getDH().sendDialogues(306, 284);
				c.doricOption = false;
			}
			break;
		case 306:
			c.getDH().sendDialogues(9199, 2244);
			break;
		case 9168:
			switch (c.dialogueAction) {

			case 251:
				c.getBankPin().bankPinSettings();
				break;

			case 416:
			case 418:
			case 424:
			case 426:
			case 430:
			case 434:
			case 436:
			case 438:
			case 440:
			case 443:
			case 441:
			case 446:
			case 448:
			case 450:
			case 452:
			case 454:
			case 456:
			case 552:
				SkillMasters.addSkillCape(c);
				break;

			case 502:
				c.getDH().sendDialogues(1027, 925);
				break;

			case 508:
				c.getDH().sendDialogues(1027, 925);
				break;

			case 2245:
				c.getPA().startTeleport(3230, 2915, 0, "modern");
				c.sendMessage("High Priest teleported you to <col=FF0000>Desert Pyramid</col>.");
				c.getPA().closeAllWindows();
				break;

			}
			if (c.doricOption) {
				c.getDH().sendDialogues(303, 284);
				c.doricOption = false;
			}
			break;
		case 9169:
			switch (c.dialogueAction) {

			case 251:
				c.getDH().sendDialogues(1015, 494);
				break;

			case 418:
			case 424:
			case 426:
			case 430:
			case 434:
			case 436:
			case 438:
			case 440:
			case 443:
			case 441:
			case 446:
			case 448:
			case 450:
			case 452:
			case 454:
			case 456:
			case 552:
				c.nextChat = 0;
				c.getPA().closeAllWindows();
				break;

			case 502:
				c.nextChat = 0;
				c.getPA().closeAllWindows();
				break;

			case 508:
				c.nextChat = 0;
				c.getPA().closeAllWindows();
				break;
				
			case 2245:
				c.getPA().closeAllWindows();
				break;

			}
			if (c.doricOption) {
				c.getDH().sendDialogues(299, 284);
			}
			break;

		/* VENG */
		case 118098:
			c.getPA().castVeng();
			break;
		/** Specials **/
		case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
			// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29038:
			if (c.playerEquipment[c.playerWeapon] == 4153) {
				c.specBarId = 7486;
				c.getCombat().handleGmaulPlayer();
				c.getItems().updateSpecialBar();
			} else {
				c.specBarId = 7486;
				c.usingSpecial = !c.usingSpecial;
				c.getItems().updateSpecialBar();
			}
			break;

		case 29063:
			if (c.inDuelArena() || c.inMiscellania()) {
				c.sendMessage("You can't use dragon battleaxe special attack in Duel Arena, sorry sir.");
			} else {
				if (c.getCombat().checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
					c.gfx0(246);
					c.forcedChat("Raarrrrrgggggghhhhhhh!");
					c.startAnimation(1056);
					c.playerLevel[2] = c.getLevelForXP(c.playerXP[2]) + (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
					c.getPA().refreshSkill(2);
					c.getItems().updateSpecialBar();
				} else {
					c.sendMessage("You don't have the required special energy to use this attack.");
				}
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		/** Specials **/
		/* Daggers & Swords */
		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Scimitar */
		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Mace */
		case 29199:
			c.specBarId = 7636;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Battleaxe & Hatchets */
		case 29074:
			c.specBarId = 7511;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Halberd $ Staff of Light */
		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Spear */
		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Godswords & 2h Swords */
		case 30007:
			c.specBarId = 7711;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Claws */
		case 30108:
			c.specBarId = 7812;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Whip */
		case 48034:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Warhammer & Mauls */
		case 29049:
			c.specBarId = 7486;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Pickaxe */
		case 30043:
			c.specBarId = 7736;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Bows */
		case 29124:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Throwing Axe & Javelins */
		case 29213:
			c.specBarId = 7661;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/** End of Specials **/

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			c.duelSlot = -1;
			c.getDuel().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			c.duelSlot = -1;
			c.getDuel().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			c.duelSlot = -1;
			c.getDuel().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			c.duelSlot = -1;
			c.getDuel().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			c.duelSlot = -1;
			c.getDuel().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			c.duelSlot = -1;
			c.getDuel().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			c.duelSlot = -1;
			c.getDuel().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			c.duelSlot = -1;
			c.getDuel().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			c.duelSlot = -1;
			c.getDuel().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			c.duelSlot = -1;
			c.getDuel().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			c.duelSlot = -1;
			c.getDuel().selectRule(10);
			break;

		case 53245: // no helm
			c.duelSlot = 0;
			c.getDuel().selectRule(11);
			break;

		case 53246: // no cape
			c.duelSlot = 1;
			c.getDuel().selectRule(12);
			break;

		case 53247: // no ammy
			c.duelSlot = 2;
			c.getDuel().selectRule(13);
			break;

		case 53249: // no weapon.
			c.duelSlot = 3;
			c.getDuel().selectRule(14);
			break;

		case 53250: // no body
			c.duelSlot = 4;
			c.getDuel().selectRule(15);
			break;

		case 53251: // no shield
			c.duelSlot = 5;
			c.getDuel().selectRule(16);
			break;

		case 53252: // no legs
			c.duelSlot = 7;
			c.getDuel().selectRule(17);
			break;

		case 53255: // no gloves
			c.duelSlot = 9;
			c.getDuel().selectRule(18);
			break;

		case 53254: // no boots
			c.duelSlot = 10;
			c.getDuel().selectRule(19);
			break;

		case 53253: // no rings
			c.duelSlot = 12;
			c.getDuel().selectRule(20);
			break;

		case 53248: // no arrows
			c.duelSlot = 13;
			c.getDuel().selectRule(21);
			break;

		/*
		 * Accepting Duel Interface Fixed by: Ardi Remember to click thanks
		 * button & karma (reputation) for Ardi, if you're using this.
		 */
		case 26018:
			if (c.duelStatus == 5) {
				// c.sendMessage("This glitch has been fixed by Ardi, sorry
				// sir.");
				return;
			}
			if (c.inDuelArena() || c.inMiscellania()) {
				Client o = (Client) PlayerHandler.players[c.duelingWith];
				if (o == null) {
					c.getDuel().declineDuel(true);
					return;
				} // if you forcetele? it has to do with this, the pi duel
					// system
					// lacks checks especially for
					// if you are in duel, or opponent is in duel, it still
					// allows to give duelid to him
					// whilst he is in duel.
					// look the above code, imagine your duel status = 0,
					// someone has done cft
					// you know what cft is? force teleport, then 3rd person
					// duels him, he gets his id
					// then opponents duel gets declined without him dueling the
					// 3rd person
					// opponent = the one who is in duel with someone else
					// (possibly id = 5 status

				if (c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
					c.sendMessage("You won't be able to attack the player with the rules you have set.");
					break;
				}
				c.duelStatus = 2;
				if (c.duelStatus == 2) {
					c.getPA().sendFrame126("Waiting for other player...", 6684);
					o.getPA().sendFrame126("Other player has accepted.", 6684);
				}
				if (o.duelStatus == 2) {
					o.getPA().sendFrame126("Waiting for other player...", 6684);
					c.getPA().sendFrame126("Other player has accepted.", 6684);
				}

				if (c.duelStatus == 2 && o.duelStatus == 2) {
					c.canOffer = false;
					o.canOffer = false;
					c.duelStatus = 3;
					o.duelStatus = 3;
					c.getDuel().confirmDuel();
					o.getDuel().confirmDuel();
				}
			} else {
				Client o = (Client) PlayerHandler.players[c.duelingWith];
				c.getDuel().declineDuel(true);
				o.getDuel().declineDuel(true);
				c.sendMessage("You can't stake out of Duel Arena.");
			}
			break;

		case 25120:
			if (c.duelStatus == 5) {
				// c.sendMessage("This glitch has been fixed by Ardi, sorry
				// sir.");
				return;
			}
			if (c.inDuelArena() || c.inMiscellania()) {
				if (c.duelStatus == 5) {
					break;
				}
				Client o1 = (Client) PlayerHandler.players[c.duelingWith];
				if (o1 == null) {
					c.getDuel().declineDuel(true);
					return;
				}

				c.duelStatus = 4;
				if (o1.duelStatus == 4 && c.duelStatus == 4) {
					c.getDuel().startDuel();
					o1.getDuel().startDuel();
					o1.duelCount = 4;
					c.duelCount = 4;
					c.duelDelay = System.currentTimeMillis();
					o1.duelDelay = System.currentTimeMillis();
				} else {
					c.getPA().sendFrame126("Waiting for other player...", 6571);
					o1.getPA().sendFrame126("Other player has accepted", 6571);
				}
			} else {
				Client o = (Client) PlayerHandler.players[c.duelingWith];
				c.getDuel().declineDuel(true);
				o.getDuel().declineDuel(true);
				c.sendMessage("You can't stake out of Duel Arena.");
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(c.MAGIC_SPELLS[48][3]);
			c.startAnimation(c.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		/*
		 * case 152: c.isRunning2 = !c.isRunning2; int frame = c.isRunning2 ==
		 * true ? 1 : 0; c.getPA().sendFrame36(173,frame); break;
		 */
		case 121042:
		case 74214:
		case 152:
		case 153:
			if (c.isRunning2) {
				c.isRunning2 = false;
				c.getPA().sendFrame36(173, 0);
				c.getPA().sendFrame36(504, 1);
			} else if (!c.isRunning2 && c.runEnergy > 0) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(173, 1);
				c.getPA().sendFrame36(504, 0);
			}
			break;

		case 32195:// 1
		case 32196:
			c.getAgility().gnomeTicketCounter(c, "1", 2996, 1, 1000);
			break;
		case 32203:// 10
		case 32197:
			c.getAgility().gnomeTicketCounter(c, "10", 2996, 10, 10000);
			break;
		case 32204:// 25
		case 32198:
			c.getAgility().gnomeTicketCounter(c, "25", 2996, 25, 25000);
			break;
		case 32199:// 100
		case 32205:
			c.getAgility().gnomeTicketCounter(c, "100", 2996, 100, 100000);
			break;
		case 32200:// 1000
		case 32206:
			c.getAgility().gnomeTicketCounter(c, "1000", 2996, 1000, 1000000);
			break;
		case 32192:// toadflex
		case 32190:
		case 32202:// snapdragon
		case 32201:
		case 32193:// piratehook
		case 32189:
			c.sendMessage("Coming soon!");
			break;

		case 9154:
			c.getPA().startLogout();
			break;

		case 86088:
		case 86102:
		case 85248:
			c.takeAsNote = !c.takeAsNote;
			break;
			
		case 85244:
			break;
		case 85245:
			PriceChecker.open(c);
			break;

		case 4171:
		case 117048:
		case 75010:
		case 50056:
		case 84237:
			c.getPA().startTeleport(Config.HOME_X + Misc.random(3), Config.HOME_Y + Misc.random(3), 0, "modern");
			c.sendMessage("You teleport home.");
			break;

		/* TELEPORTS */
		case 29031: // trollheim
			if (c.getItems().playerHasItem(554, 2) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 60) {
				c.getPA().startTeleport(2888, 3674, 0, "modern");
				c.getPA().addSkillXP(200, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(554, 2);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 4140: // varrock
			if (c.getItems().playerHasItem(554, 1) && c.getItems().playerHasItem(563, 1)
					&& c.getItems().playerHasItem(556, 3) && c.playerLevel[6] > 24) {
				c.getPA().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0,
						"modern");
				c.getPA().addSkillXP(100, 6);
				c.getItems().deleteItem2(563, 1);
				c.getItems().deleteItem2(554, 1);
				c.getItems().deleteItem2(556, 3);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * if(c.isMoving) { c.sendMessage("You cannot teleport whilst moving!");
		 * return; } c.sendMessage("Teleport sequence started."); c.sendMessage(
		 * "You will be teleported in 10 seconds."); TaskHandler.submit(new
		 * Task(17, false) {
		 * 
		 * @Override public void execute() {
		 * c.getPA().startTeleport2(Config.VARROCK_X + Misc.random(3),
		 * Config.VARROCK_Y + Misc.random(3), 0); c.sendMessage(
		 * "You teleport to Varrock."); this.cancel(); }
		 * 
		 * @Override public void onCancel() { System.out.println(
		 * "Teleport to Varrock."); } }); break;
		 */
		case 4143: // Lumby
			if (c.getItems().playerHasItem(557, 1) && c.getItems().playerHasItem(563, 1)
					&& c.getItems().playerHasItem(556, 3) && c.playerLevel[6] > 30) {
				c.getPA().startTeleport(Config.HOME_X + Misc.random(3), Config.HOME_Y + Misc.random(3), 0, "modern");
				c.getPA().addSkillXP(124, 6);
				c.getItems().deleteItem2(563, 1);
				c.getItems().deleteItem2(557, 1);
				c.getItems().deleteItem2(556, 3);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * if(c.isMoving) { c.sendMessage("You cannot teleport whilst moving!");
		 * return; } c.sendMessage("Teleport sequence started."); c.sendMessage(
		 * "You will be teleported in 10 seconds."); TaskHandler.submit(new
		 * Task(17, false) {
		 * 
		 * @Override public void execute() {
		 * c.getPA().startTeleport2(Config.HOME_X + Misc.random(3),
		 * Config.HOME_Y + Misc.random(3), 0); c.sendMessage(
		 * "You teleport to Lumbridge."); this.cancel(); }
		 * 
		 * @Override public void onCancel() { System.out.println(
		 * "Teleport to Lumbridge."); } }); break;
		 */
		case 4146: // Fally
			if (c.getItems().playerHasItem(555, 1) && c.getItems().playerHasItem(563, 1)
					&& c.getItems().playerHasItem(556, 3) && c.playerLevel[6] > 36) {
				c.getPA().startTeleport(Config.FALADOR_X + Misc.random(3), Config.FALADOR_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(148, 6);
				c.getItems().deleteItem2(563, 1);
				c.getItems().deleteItem2(555, 1);
				c.getItems().deleteItem2(556, 3);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * if(c.isMoving) { c.sendMessage("You cannot teleport whilst moving!");
		 * return; } c.sendMessage("Teleport sequence started."); c.sendMessage(
		 * "You will be teleported in 10 seconds."); TaskHandler.submit(new
		 * Task(17, false) {
		 * 
		 * @Override public void execute() {
		 * c.getPA().startTeleport2(Config.FALADOR_X + Misc.random(3),
		 * Config.FALADOR_Y + Misc.random(3), 0); c.sendMessage(
		 * "You teleport to Falador."); this.cancel(); }
		 * 
		 * @Override public void onCancel() { System.out.println(
		 * "Teleport to Falador."); } }); break;
		 */
		case 4150: // Cammy
			if (c.getItems().playerHasItem(563, 1) && c.getItems().playerHasItem(556, 5) && c.playerLevel[6] > 44) {
				c.getPA().startTeleport(Config.CAMELOT_X + Misc.random(3), Config.CAMELOT_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(180, 6);
				c.getItems().deleteItem2(563, 1);
				c.getItems().deleteItem2(556, 5);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * if(c.isMoving) { c.sendMessage("You cannot teleport whilst moving!");
		 * return; } c.sendMessage("Teleport sequence started."); c.sendMessage(
		 * "You will be teleported in 10 seconds."); TaskHandler.submit(new
		 * Task(17, false) {
		 * 
		 * @Override public void execute() {
		 * c.getPA().startTeleport2(Config.CAMELOT_X + Misc.random(3),
		 * Config.CAMELOT_Y + Misc.random(3), 0); c.sendMessage(
		 * "You teleport to Camelot."); this.cancel(); }
		 * 
		 * @Override public void onCancel() { System.out.println(
		 * "Teleport to Camelot."); } }); break;
		 */
		case 6004: // Ardy
			if (c.getItems().playerHasItem(555, 2) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 51) {
				c.getPA().startTeleport(Config.ARDOUGNE_X + Misc.random(3), Config.ARDOUGNE_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(208, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(555, 2);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * if(c.isMoving) { c.sendMessage("You cannot teleport whilst moving!");
		 * return; } c.sendMessage("Teleport sequence started."); c.sendMessage(
		 * "You will be teleported in 10 seconds."); TaskHandler.submit(new
		 * Task(17, false) {
		 * 
		 * @Override public void execute() {
		 * c.getPA().startTeleport2(Config.ARDOUGNE_X + Misc.random(3),
		 * Config.ARDOUGNE_Y + Misc.random(3), 0); c.sendMessage(
		 * "You teleport to Ardougne."); this.cancel(); }
		 * 
		 * @Override public void onCancel() { System.out.println(
		 * "Teleport to Ardougne."); } }); break;
		 */
		case 6005: // Edge/Watchtower: actually tele's you to edgeville
			if (c.getItems().playerHasItem(557, 2) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 57) {
				c.getPA().startTeleport(Config.WATCHTOWER_X + Misc.random(3), Config.WATCHTOWER_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(232, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(557, 2);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * if(c.isMoving) { c.sendMessage("You cannot teleport whilst moving!");
		 * return; } c.sendMessage("Teleport sequence started."); c.sendMessage(
		 * "You will be teleported in 10 seconds."); TaskHandler.submit(new
		 * Task(17, false) {
		 * 
		 * @Override public void execute() {
		 * c.getPA().startTeleport2(Config.WATCHTOWER_X + Misc.random(3),
		 * Config.WATCHTOWER_Y + Misc.random(3), 0); c.sendMessage(
		 * "You teleport to the Watchtower."); this.cancel(); }
		 * 
		 * @Override public void onCancel() { System.out.println(
		 * "Teleport to the Watchtower."); } }); break;
		 */
		case 50235: // Paddewwa
			if (c.getItems().playerHasItem(556, 1) && c.getItems().playerHasItem(563, 1)
					&& c.getItems().playerHasItem(554, 1) && c.playerLevel[6] > 53) {
				c.getPA().startTeleport(Config.PADDEWWA_X + Misc.random(3), Config.PADDEWWA_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(216, 6);
				c.getItems().deleteItem2(563, 1);
				c.getItems().deleteItem2(554, 1);
				c.getItems().deleteItem2(556, 1);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 50245: // Senntisten
			if (c.getItems().playerHasItem(566, 1) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 59) {
				c.getPA().startTeleport(Config.SENNTISTEN_X + Misc.random(3), Config.SENNTISTEN_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(240, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(566, 1);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 50253: // Kharyrll
			if (c.getItems().playerHasItem(565, 1) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 65) {
				c.getPA().startTeleport(Config.KHARYRLL_X + Misc.random(3), Config.KHARYRLL_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(264, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(565, 1);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 51005: // Lassar
			if (c.getItems().playerHasItem(555, 4) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 71) {
				c.getPA().startTeleport(Config.LASSAR_X + Misc.random(3), Config.LASSAR_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(288, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(555, 4);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 51013: // Dareeyak
			if (c.getItems().playerHasItem(556, 2) && c.getItems().playerHasItem(563, 2)
					&& c.getItems().playerHasItem(554, 3) && c.playerLevel[6] > 77) {
				c.getPA().startTeleport(Config.DAREEYAK_X + Misc.random(3), Config.DAREEYAK_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(312, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(554, 3);
				c.getItems().deleteItem2(556, 2);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 51023: // Carrallangar
			if (c.getItems().playerHasItem(566, 2) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 83) {
				c.getPA().startTeleport(Config.CARRALLANGAR_X + Misc.random(3), Config.CARRALLANGAR_Y + Misc.random(3),
						0, "modern");
				c.getPA().addSkillXP(336, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(566, 2);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 51031: // Annakarl
			if (c.getItems().playerHasItem(565, 2) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 89) {
				c.getPA().startTeleport(Config.ANNAKARL_X + Misc.random(3), Config.ANNAKARL_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(360, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(565, 2);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		case 51039: // Ghorrock
			if (c.getItems().playerHasItem(555, 8) && c.getItems().playerHasItem(563, 2) && c.playerLevel[6] > 95) {
				c.getPA().startTeleport(Config.GHORROCK_X + Misc.random(3), Config.GHORROCK_Y + Misc.random(3), 0,
						"modern");
				c.getPA().addSkillXP(384, 6);
				c.getItems().deleteItem2(563, 2);
				c.getItems().deleteItem2(555, 8);
			} else {
				c.sendMessage("You don't have the required items and or level.");
			}
			break;
		/*
		 * case 50235: case 4140: case 117112: if (System.currentTimeMillis() -
		 * c.lastTeleport > 5000) { c.getDH().sendOption5("Edgeville Pk",
		 * "Castle Pk", "Magebank Pk", "Hill Giants Pk", "Ardy Lever");
		 * c.teleAction = 4; } break;
		 * 
		 * case 4143: case 50245: case 117123:
		 * c.getDH().sendOption5("Lumbridge", "Varrock", "Camelot", "Falador",
		 * "Canifis"); // TODO c.teleAction = 13; break;
		 * 
		 * case 4146: case -2: case -3: if (System.currentTimeMillis() -
		 * c.lastTeleport > 5000) { c.getDH().sendOption5("Rock Crabs",
		 * "Hill Giants", "Slayer Monsters", "Brimhaven Dungeon",
		 * "Traverly Dungeon"); c.teleAction = 1; } break;
		 * 
		 * case 4150: case 51005: case 117154: if (System.currentTimeMillis() -
		 * c.lastTeleport > 5000) { c.getDH().sendOption5("Godwars Dungeon",
		 * "King Black Dragon", "Dagannoth Lair", "Kalphite Lair", "Next ->");
		 * c.teleAction = 3; } break;
		 * 
		 * case 6004: case 51013: case 117162: if (System.currentTimeMillis() -
		 * c.lastTeleport > 5000) { c.getDH().sendOption5("Agility", "Cooking",
		 * "Crafting", "Farming", "Next ->"); c.teleAction = 5; } break;
		 * 
		 * case 6005: case 51023: case 117186: if (System.currentTimeMillis() -
		 * c.lastTeleport > 5000) { c.getDH().sendOption5("Barbarian Assault",
		 * "Duel Arena", "Barrows", "Pest Control", "Next ->"); c.teleAction =
		 * 2; } break;
		 */
		case 9125: // Accurate
		case 6221: // range accurate
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 22230: // punch
			c.fightMode = 0;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
			// case 22231: //unarmed
		case 22228: // unarmed
			c.fightMode = 1;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
			c.fightMode = 3;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
		case 22229: // kick
			c.fightMode = 2;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		/*
		 * bank tabs below - just the button clicking for the unused tabs
		 */
		case 89241:
		case 89242:
		case 89243:
		case 89244:
		case 89245:
		case 89246:
		case 89247:
		case 89248:
		case 89249:
			c.sendMessage("Bank tabs are currently unavailable, we apologize for this inconvenience.");
			break;
		case 13114:
			Client o = (Client) PlayerHandler.players[c.tradeWith];
			c.getTrade().declineTrade(true);
			c.getItems().updateInventory();
			o.getItems().updateInventory();
			break;

		case 13092:
			if (c.inWild()) {
				c.getTrade().declineTrade(true);
				break;
			}
			if (System.currentTimeMillis() - c.lastButton < 400) {

				c.lastButton = System.currentTimeMillis();

				break;

			} else {

				c.lastButton = System.currentTimeMillis();

			}
			Client ot = (Client) PlayerHandler.players[c.tradeWith];
			if (ot == null) {
				c.getTrade().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			c.getPA().sendFrame126("Waiting for other player...", 3431);
			ot.getPA().sendFrame126("Other player has accepted", 3431);
			c.goodTrade = true;
			ot.goodTrade = true;

			for (GameItem item : c.getTrade().offeredItems) {
				if (item.id > 0) {
					if (ot.getEquipment().freeSlots() < c.getTrade().offeredItems.size()) {
						c.sendMessage(ot.playerName + " only has " + ot.getEquipment().freeSlots()
								+ " free slots, please remove "
								+ (c.getTrade().offeredItems.size() - ot.getEquipment().freeSlots()) + " items.");
						ot.sendMessage(c.playerName + " has to remove "
								+ (c.getTrade().offeredItems.size() - ot.getEquipment().freeSlots())
								+ " items or you could offer them "
								+ (c.getTrade().offeredItems.size() - ot.getEquipment().freeSlots()) + " items.");
						c.goodTrade = false;
						ot.goodTrade = false;
						c.getPA().sendFrame126("Not enough inventory space...", 3431);
						ot.getPA().sendFrame126("Not enough inventory space...", 3431);
						break;
					} else {
						c.getPA().sendFrame126("Waiting for other player...", 3431);
						ot.getPA().sendFrame126("Other player has accepted", 3431);
						c.goodTrade = true;
						ot.goodTrade = true;
					}
				}
			}
			if (c.inTrade && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
				c.tradeConfirmed = true;
				if (ot.tradeConfirmed) {
					c.getTrade().confirmScreen();
					ot.getTrade().confirmScreen();
					break;
				}

			}

			break;

		case 13218:
			if (System.currentTimeMillis() - c.lastButton < 400) {
				c.lastButton = System.currentTimeMillis();
				break;
			} else {
				c.lastButton = System.currentTimeMillis();

			}
			c.tradeAccepted = true;
			Client ot1 = (Client) PlayerHandler.players[c.tradeWith];
			if (ot1 == null) {
				c.getTrade().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}

			if (c.inTrade && c.tradeConfirmed && ot1.tradeConfirmed && !c.tradeConfirmed2) {
				c.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2) {
					c.getPA().resetInterfaceStatus();
					ot1.getPA().resetInterfaceStatus();
					c.acceptedTrade = true;
					ot1.acceptedTrade = true;
					c.getTrade().giveItems();
					ot1.getTrade().giveItems();
					c.sendMessage("Trade accepted.");
					ot1.sendMessage("Trade accepted.");
					break;
				}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				c.getPA().sendFrame126("Waiting for other player...", 3535);
			}

			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */

		/**
		 * Mouse Button
		 */
		case 74176:
		case 121108:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;

		/**
		 * Split Chat
		 */
		case 74184:
		case 121092:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;

		/**
		 * Chat Effects
		 */
		case 121088:
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;

		/**
		 * Accept aid
		 */
		case 74188:
		case 121038:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		/**
		 * Run
		 */
		// case 74192:
		// case 121042:
		// if (!c.isRunning2) {
		// c.isRunning2 = true;
		// c.getPA().sendFrame36(504, 1);
		// c.getPA().sendFrame36(173, 1);
		// } else {
		// c.isRunning2 = false;
		// c.getPA().sendFrame36(504, 0);
		// c.getPA().sendFrame36(173, 0);
		// }
		// break;

		/**
		 * Attack option Priority
		 */
		case 121117:
			c.getPA().sendFrame36(315, 0);
			c.getPA().sendFrame36(316, 0);
			c.getPA().sendFrame36(317, 0);
			break;

		case 121121:
			c.getPA().sendFrame36(315, 1);
			c.getPA().sendFrame36(316, 1);
			c.getPA().sendFrame36(317, 0);
			break;

		case 121125:
			c.getPA().sendFrame36(315, 1);
			c.getPA().sendFrame36(316, 0);
			c.getPA().sendFrame36(317, 1);
			break;

		case 121061:// brightness1
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 121133:// brightness2
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 121137:// brightness3
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 121141:// brightness4
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;

		/*
		 * case 74201:// brightness1 c.getPA().sendFrame36(505, 1);
		 * c.getPA().sendFrame36(506, 0); c.getPA().sendFrame36(507, 0);
		 * c.getPA().sendFrame36(508, 0); c.getPA().sendFrame36(166, 1); break;
		 * case 74203:// brightness2 c.getPA().sendFrame36(505, 0);
		 * c.getPA().sendFrame36(506, 1); c.getPA().sendFrame36(507, 0);
		 * c.getPA().sendFrame36(508, 0); c.getPA().sendFrame36(166, 2); break;
		 * 
		 * case 74204:// brightness3 c.getPA().sendFrame36(505, 0);
		 * c.getPA().sendFrame36(506, 0); c.getPA().sendFrame36(507, 1);
		 * c.getPA().sendFrame36(508, 0); c.getPA().sendFrame36(166, 3); break;
		 * 
		 * case 74205:// brightness4 c.getPA().sendFrame36(505, 0);
		 * c.getPA().sendFrame36(506, 0); c.getPA().sendFrame36(507, 0);
		 * c.getPA().sendFrame36(508, 1); c.getPA().sendFrame36(166, 4); break;
		 */

		default:
			c.getEmoteHandler().startEmote(actionButtonId);
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR))
				c.sendMessage("Unhandled actionButtonId: " + actionButtonId);
			else
				System.out.println("Unhandled actionButtonId: " + actionButtonId);
			break;

		case 24017:
			c.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon],
					ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;
		}
	}
}
