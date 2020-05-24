package wind.model.players.packets.clicking.objects;

import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.Flax;
import wind.model.players.content.Special;
import wind.model.players.content.minigames.CastleWars;
import wind.model.players.content.skills.impl.Thieving;
import wind.util.Misc;

public class SecondClickObject {

	public static void handleClick(Client c, int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		switch (objectType) {

		case 172:
			c.getItemLottery().tryLottery();
			break;
			/* Barrows */

		case 11748:
			c.getPA().openUpBank();
			break;
		case 2884:
		case 16684:
			c.startAnimation(828);
			c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel + 1);
			break;
			
			
		case 2883:
			c.getItems().deleteItem2(995, 10);
			c.sendMessage("You pass the gate.");
			Special.movePlayer(c);
			Special.openKharid(c, c.objectId);
			c.turnPlayerTo(c.objectX, c.objectY);
			break;
		case 76:
			c.getShops().openShop(165);
			break;
	/*	case 20772:
			if (c.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1677, c.getX(), c.getY() - 1, 3,
						0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
				
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
				return;
			}
			break; 
			*/

		case 6772:
			if (c.barrowsNpcs[1][1] == 0) {
				NPCHandler.spawnNpc(c, 2029, c.getX() + 1, c.getY(), 3,
						0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6822:
			if (c.barrowsNpcs[2][1] == 0) {
				NPCHandler.spawnNpc(c, 2028, c.getX(), c.getY() - 1, 3,
						0, 90, 20, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6773:
			if (c.barrowsNpcs[3][1] == 0) {
				NPCHandler.spawnNpc(c, 2027, c.getX(), c.getY() - 1, 3,
						0, 120, 24, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6771:
			c.getDH().sendDialogues(996, 2026);
			break;

		case 6821:
			if (c.barrowsNpcs[5][1] == 0) {
				NPCHandler.spawnNpc(c, 2025, c.getX() - 1, c.getY(), 3,
						0, 90, 25, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		// castlewars
		case 4423:
		case 4424:
		case 4427:
		case 4428:
			CastleWars.attackDoor(c, objectType);
			break;
		// end
		case 2558:
		case 2557:
			if (System.currentTimeMillis() - c.lastLockPick < 1000
					|| c.freezeTimer > 0) {
				return;
			}
			c.lastLockPick = System.currentTimeMillis();
			if (c.getItems().playerHasItem(1523, 1)) {

				if (Misc.random(10) <= 3) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.objectX == 3044 && c.objectY == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo(1, 0);
					}

				} else if (c.objectX == 3038 && c.objectY == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo(-1, 0);
					}
				} else if (c.objectX == 3041 && c.objectY == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo(0, 1);
					}
				} else if (c.objectX == 3191 && c.objectY == 3963) {
					if (c.absY == 3963) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3962) {
						c.getPA().walkTo(0, 1);
					}
				} else if (c.objectX == 3190 && c.objectY == 3957) {
					if (c.absY == 3957) {
						c.getPA().walkTo(0, 1);
					} else if (c.absY == 3958) {
						c.getPA().walkTo(0, -1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		case 17010:
			if (c.playerMagicBook == 0) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 1) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			break;
		/*
		 * One stall that will give different amount of money depending on your
		 * thieving level, also different amount of xp.
		 */
		case 4705:
			if ((c.playerLevel[17]) >= 42) {
				Thieving.stealFromStall(c, 995, 2500, 25, 1, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			}
			break;

		case 4706:
			if ((c.playerLevel[17]) >= 2) {
				Thieving.stealFromStall(c, 995, 500, 10, 1, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			}
			break;

		case 2561:
			if ((c.playerLevel[17]) >= 0 && (c.playerLevel[17] <= 20)) { // level
																			// 1
																			// of
																			// thieving
				Thieving.stealFromStall(c, 995, 200, 10, 1, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 20 && (c.playerLevel[17] <= 40)) { // level
																					// 20
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 350, 20, 20, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 40 && (c.playerLevel[17] <= 60)) { // level
																					// 40
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 400, 30, 40, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 60 && (c.playerLevel[17] <= 70)) { // level
																					// 60
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 550, 40, 60, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 70 && (c.playerLevel[17] <= 80)) { // level
																					// 70
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 600, 50, 70, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 80 && (c.playerLevel[17] <= 90)) { // level
																					// 80
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 700, 60, 80, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 90 && (c.playerLevel[17] <= 96)) { // level
																					// 90
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 850, 70, 90, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 96 && (c.playerLevel[17] <= 98)) { // level
																					// 96
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 900, 80, 96, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) >= 98 && (c.playerLevel[17] <= 99)) { // level
																					// 98
																					// of
																					// thieving
				Thieving.stealFromStall(c, 995, 1050, 90, 96, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			} else if ((c.playerLevel[17]) == 99) {
				Thieving.stealFromStall(c, 995, 3000, 100, 99, objectType, obX,
						obY, obX == 2667 ? 3 : 1);
			}
			break;

		case 2781:
		case 26814:
		case 11666:
		case 3044:
			c.getSmithing().sendSmelting();
			break;
		case 2565:
			Thieving.stealFromStall(c, 995, 300, 400, 50, objectType, obX,
					obY, 2);
			break;
		case 2564:
			Thieving.stealFromStall(c, 995, 300, 500, 65, objectType, obX,
					obY, 0);
			break;
		case 2563:
			Thieving.stealFromStall(c, 995, 300, 300, 36, objectType, obX,
					obY, 0);
			break;
		case 2562:
			Thieving.stealFromStall(c, 995, 300, 600, 75, objectType, obX,
					obY, 3);
			break;
		case 2560:
			Thieving.stealFromStall(c, 995, 320, 24, 20, objectType, obX, obY,
					obX == 2662 ? 2 : 1);
			break;

		case 14011:
			Thieving.stealFromStall(c, 995, 300, 10, 1, objectType, obX, obY,
					3);
			break;
		case 7053:
			Thieving.stealFromStall(c, 995, 400, 18, 10, objectType, obX, obY,
					obX == 3079 ? 2 : 1);
			break;
		case 4874: // crafting stall
			Thieving.stealFromStall(c, 995, 300, 100, 1, objectType, obX, obY,
					1);
			break;
		case 4875: // food stall
			Thieving.stealFromStall(c, 995, 320, 130, 25, objectType, obX,
					obY, 1);
			break;
		case 4876: // general stall
			Thieving.stealFromStall(c, 995, 350, 160, 50, objectType, obX,
					obY, 1);
			break;
		case 4877: // magic stall
			Thieving.stealFromStall(c, 995, 350, 180, 75, objectType, obX,
					obY, 1);
			break;
		case 4878: // scimitar stall
			Thieving.stealFromStall(c, 995, 300, 250, 90, objectType, obX,
					obY, 1);
			break;
		case 6572:
			Thieving.stealFromStall(c, 995, 320, 650, 1, objectType, obX, obY,
					3);
			break;

		case 7134:
			Flax.pickFlax(c, obX, obY);
			break;

	/*	case 15508:
			Flax.pickWheat(c, obX, obY);
			break;

		case 312:
			Flax.pickPotato(c, obX, obY);
			break; */

		/**
		 * Opening the bank.
		 */
		case 2213:
		case 14367:
		case 11758:
		case 10517:
		case 26972:
		case 11744:
		case 25808:
		case 18491:
			c.getPA().openUpBank();
			break;

		}
	}
	
}
