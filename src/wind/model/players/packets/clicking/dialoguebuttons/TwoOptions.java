package wind.model.players.packets.clicking.dialoguebuttons;

import wind.Config;
import wind.Server;
import wind.model.items.ItemAssistant;
import wind.model.items.impl.MembershipBond;
import wind.model.players.Client;
import wind.model.players.content.MithrilSeeds;
import wind.model.players.content.Teles;

public class TwoOptions {

	public static void handleOption1(Client c) {

		switch (c.dialogueAction) {
		
		case 4397:
			c.getDH().sendOption5(ItemAssistant.getItemName(c.deposit1), ItemAssistant.getItemName(c.deposit2), ItemAssistant.getItemName(c.deposit3), ItemAssistant.getItemName(c.deposit4), 
					ItemAssistant.getItemName(c.deposit5));
			c.nextChat = 0;
			c.dialogueAction = 4398;
			break;
		case 300:
			c.getPA().startTeleport(3367, 3267, 0, "modern");
			Teles.rings(c);	
			c.nextChat = 0;
			c.usingROD = false;
			break;
		case 301:
			c.getPA().startTeleport(2898, 3562, 0, "modern");
			Teles.necklaces(c);
			c.nextChat = 0;
			c.usingGN = false;
			break;
		case 5787:
			c.getPA().movePlayer(1761, 5188, 0);
			c.nextChat = 0;
			c.getPA().closeAllWindows();
			break;
		case 3344:
			if (c.hpInsurance == 1) {
				c.getDH().sendNpcChat("Your HP is already insured!", 3343, "Surgeon General Tafani");
				c.nextChat = 0;
			} else if (c.hpInsurance == -1) {
				c.getDH().sendOption2("Pay 1,000,000gp to protect your hitpoints.", "Nevermind, that's too expensive.");
				c.nextChat = 0;
				c.dialogueAction = 3345;
			}
			break;
		case 3345:
			if (c.getItems().playerHasItem(995, 1000000)) {
			c.hpInsurance = 1;
			c.getItems().deleteItem2(995, 1000000);
			c.sendMessage("Your @red@hitpoints@bla@ are now insured forever! You don't have to protect them.");
			c.getPA().closeAllWindows();
			} else {
				c.getPA().closeAllWindows();
				c.sendMessage("You need 1,000,000 gp to insure your hitpoints.");
			}
			break;
			
			case 2134:
				c.getPA().removeAllWindows();
				c.getBarrows().getMaze().teleportToMaze();
				break;
			case 500: 
				c.getShops().openShop(54);
				break;

			case 1009:
				MembershipBond.redeem(c);
				break;

			case 1010:
				MembershipBond.convert(c);
				break;
			case 2135:
				c.getPA().removeAllWindows();
				c.getPA().movePlayer(3565, 3311, 0);
				break;


			case 1008:
				if (c.getItems().playerHasItem(c.ingredient[0], 1) && c.getItems().playerHasItem(c.ingredient[1], 1)) {
					c.getItems().deleteItem(c.ingredient[0], 1);
					c.getItems().deleteItem(c.ingredient[1], 1);
					c.getItems().addItem(c.product, 1);
					c.sendMessage("You mend the items and create a @dre@" + c.productName + "@bla@.");
					c.ingredient[0] = -1;
					c.ingredient[1] = -1;
					c.product = -1;
					c.productName = "";
					c.getPA().removeAllWindows();
				}
				break;

			case 1:
				c.getDH().sendDialogues(38, -1);
				break;

			case 5:
				c.getSlayer().generateTask();
				c.getPA().sendFrame126(
						"@whi@Task: @gre@" + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + " ",
						7383);
				c.getPA().closeAllWindows();
				break;

			case 6:
				c.sendMessage("Slayer will be enabled in some minutes.");
				// c.getSlayer().generateTask();
				// c.getPA().sendFrame126("@whi@Task: @gre@"+Server.npcHandler.getNpcListName(c.slayerTask)+
				// " ", 7383);
				// c.getPA().closeAllWindows();
				break;

			case 8:
				c.getPA().fixAllBarrows();
				c.getPA().closeAllWindows();
				break;

			case 17:
				c.tryLottery();
				c.dialogueAction = -1;
				break;

			case 24:
				MithrilSeeds.pickupFlowers(c);
				c.getPA().removeAllWindows();
				break;

			case 25:
				c.getDH().sendDialogues(996, 0);
				break;

			//ironman
			case 5053:
				c.getItems().addItem(12810, 1);
				c.getItems().addItem(12811, 1);
				c.getItems().addItem(12812, 1);
				break;

			case 31:
				c.getSlayer().cancelTask();
				break;

			case 162:
				c.sendMessage("You successfully emptied your inventory.");
				c.getPA().removeAllItems();
				c.dialogueAction = 0;
				c.getPA().closeAllWindows();
				break;

			case 508:
				c.getDH().sendDialogues(1030, 925);
				break;
			case 669:
				if (c.slayerPoints >= 75) {
					if (c.getItems().playerHasItem(6737, 1)) {
						c.getItems().deleteItem(6737, 1);
						c.slayerPoints -= 75;
						c.getItems().addItem(11773, 1);
						c.getDH().sendStatement("Nieve exchanges your old ring for an imbued one.");
						c.sendMessage("You exchanged your old ring and have @red@" + c.slayerPoints
								+ "@bla@ Slayer points left.");
						c.nextChat = 0;
					}
				} else {
					c.getDH().sendStatement("You need a ring and 75 slayer points to upgrade your ring.");
					c.nextChat = 0;
				}
				break;
			case 670:
				if (c.slayerPoints >= 75) {
					if (c.getItems().playerHasItem(6731, 1)) {
						c.getItems().deleteItem(6731, 1);
						c.slayerPoints -= 75;
						c.getItems().addItem(11770, 1);
						c.getDH().sendStatement("Nieve exchanges your old ring for an imbued one.");
						c.sendMessage("You exchanged your old ring and have @red@" + c.slayerPoints
								+ "@bla@ Slayer points left.");
						c.nextChat = 0;
					}
				} else {
					c.getDH().sendStatement("You need a ring and 75 slayer points to upgrade your ring.");
					c.nextChat = 0;
				}
				break;
			case 671:
				if (c.slayerPoints >= 75) {
					if (c.getItems().playerHasItem(6733, 1)) {
						c.getItems().deleteItem(6733, 1);
						c.slayerPoints -= 75;
						c.getItems().addItem(11771, 1);
						c.getDH().sendStatement("Nieve exchanges your old ring for an imbued one.");
						c.sendMessage("You exchanged your old ring and have @red@" + c.slayerPoints
								+ "@bla@ Slayer points left.");
						c.nextChat = 0;
					}
				} else {
					c.getDH().sendStatement("You need a ring and 75 slayer points to upgrade your ring.");
					c.nextChat = 0;
				}
				break;
			case 850:
				c.getPA().movePlayer(c.absX, c.absY, 2);
				c.startAnimation(828);
				break;

			case 851: // Stairs Up
				if (c.absX >= 2837 && c.absX <= 2842 && c.absY >= 3536 && c.absY <= 3540) {
					c.getPA().movePlayer(c.absX, c.absY, 2); // Warriors Guild
					c.startAnimation(828);
					c.getPA().closeAllWindows();
				} else {
					c.getPA().movePlayer(c.absX, c.absY, 1);
					c.startAnimation(828);
					c.getPA().closeAllWindows();
				}
				break;

			case 852:
				c.getPA().movePlayer(c.absX, c.absY, 1);
				c.startAnimation(827);
				break;

			case 1020:
				c.getDH().sendNpcChat2("Sss...scalessss", "Sss.. 100..sss", 0);
				if (c.getItems().playerHasItem(892, 100))
					c.nextChat = 1033;
				else
					c.nextChat = 1032;
				break;

			case 1002:
				if (c.barrowsRewardDelay > 0)
					return;
				if (c.getEquipment().freeSlots() > 0) {
					int itemGat = Client.randomBarrows();
					c.getItems().addItem(itemGat, 1);
					c.barrowsRewardDelay = 2;
					c.getPA().closeAllWindows();
					c.getPA().movePlayer(3564, 3290, 0);
					c.resetBarrows();
					c.sendMessage("<col=0000FF>You pull out the treasure.</col>");
					c.getItems();
					c.getItems();
					c.getwM().globalMessage(
							"<col=0000FF>" + c.playerName + "</col> opened barrows chest and received a <col=00AF33>"
									+ ItemAssistant.getItemName(itemGat) + "</col>!");
				} else {
					c.sendMessage("<col=FF0000>You need at least 1 free inventory space.</col>");
				}
				break;

			case 2258:
				c.getPA().startTeleport(3039, 4834, 0, "modern"); // first click
				// teleports
				// second
				// click
				// open
				// shops
				break;

		}
		if (c.newPlayerAct == 1) {
			// c.isNewPlayer = false;
			c.newPlayerAct = 0;
			c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
			c.getPA().removeAllWindows();
		}
		if (Client.IronOptiona) {
			c.getDH().sendDialogues(5053, 284);
			Client.IronOptiona = false;
		}
		if (c.usingROD) {
			c.getPA().startTeleport(3367, 3267, 0, "modern");
			Teles.rings(c);	
		}
		if (c.usingGN) {
			c.getPA().startTeleport(2898, 3562, 0, "modern");
			Teles.necklaces(c);
		}

		if (c.doricOption2) {
			c.getDH().sendDialogues(310, 284);
			c.doricOption2 = false;
		}
		if (c.rfdOption) {
			c.getDH().sendDialogues(26, -1);
			c.rfdOption = false;
		}
		if (c.horrorOption) {
			c.getDH().sendDialogues(35, -1);
			c.horrorOption = false;
		}
		if (c.dtOption) {
			c.getDH().sendDialogues(44, -1);
			c.dtOption = false;
		}
		if (c.dtOption2) {
			if (c.lastDtKill == 0) {
				c.getDH().sendDialogues(65, -1);
			} else {
				c.getDH().sendDialogues(49, -1);
			}
			c.dtOption2 = false;
		}

		if (c.caOption2) {
			c.getDH().sendDialogues(106, c.npcType);
			c.caOption2 = false;
		}
		if (c.caOption2a) {
			c.getDH().sendDialogues(102, c.npcType);
			c.caOption2a = false;
		}

	}

	public static void handleOption2(Client c) {

		switch (c.dialogueAction) {
			case 1019:
			case 1008:
			case 17:
				c.getPA().removeAllWindows();
				break;
			case 500: 
				c.getSlayer().handleInterface("buy");
				//	c.getShops().openShop(168);
					c.sendMessage("The Slayer's Respite is actually a Slayer Helmet. I'm just lazy.");
					c.sendMessage("You currently have @red@" + c.slayerPoints
							+ " @bla@slayer points.");
					break;
			case 3345:
				c.getPA().closeAllWindows();
				break;
			case 300:
				c.getPA().startTeleport(2441, 3090, 0, "modern");
				Teles.rings(c);	
				c.nextChat = 0;
				c.usingROD = false;
				break;
			case 301:
				c.getPA().startTeleport(2520, 3569, 0, "modern");
				Teles.necklaces(c);
				c.nextChat = 0;
				c.usingGN = false;
				break;

			case 24:
				MithrilSeeds.handleFlower(c);
				c.getPA().removeAllWindows();
				break;

			case 27:
				c.getPA().removeAllWindows();
				break;

			case 30:
				if (c.barrowsKillCount == 5)
					c.getPA().movePlayer(3551, 9694, 0);
				else
					c.getDH().sendDialogues(1006, 1696);
				break;
			case 5787:
				c.nextChat = 0;
				c.getPA().removeAllWindows();
				break;

			case 31:
				c.getPA().closeAllWindows();
				break;

			case 162:
				c.dialogueAction = 0;
				c.getPA().closeAllWindows();
				break;

			case 850:
				c.getPA().movePlayer(c.absX, c.absY, 0);
				c.startAnimation(827);
				break;
			case 4397:
				c.getDH().sendNpcChat("Use an item on me to store it!", 4397, "Financial Wizard");
				c.nextChat = 0;
				break;
			case 3344:
				if (!(c.inSafeZone())) {
					c.sendMessage("@red@You may only protect skills while in a safe zone.");
					return;
				}
				c.getPA().showInterface(2808);
				c.sendMessage("@red@Click the skill you'd like to protect!");
				c.getPA().sendFrame126("Choose the stats you wish to protect!",2810);
				break;
		}

		
		if (c.newPlayerAct == 1) {
			c.newPlayerAct = 0;
			c.sendMessage("<col=FF0000>There is nothing to do in Crandor, i must teleport home and start playing "
					+ Config.SERVER_NAME + "</col>");
			c.getPA().removeAllWindows();
		}
		if (c.usingROD) {
			c.getPA().startTeleport(2441, 3090, 0, "modern");
			Teles.rings(c);
		}
		if (c.usingGN) {
			c.getPA().startTeleport(2552, 3558, 0, "modern");
			Teles.necklaces(c);
		}
		if (c.doricOption2) {
			c.getDH().sendDialogues(309, 284);
			c.doricOption2 = false;
		}
		/*
		 * if (c.dialogueAction == 8) { c.getPA().fixAllBarrows(); } else {
		 * c.dialogueAction = 0; c.getPA().removeAllWindows(); }
		 */
		if (c.caOption2a) {
			c.getDH().sendDialogues(136, c.npcType);
			c.caOption2a = false;
		}
	}
	

}
