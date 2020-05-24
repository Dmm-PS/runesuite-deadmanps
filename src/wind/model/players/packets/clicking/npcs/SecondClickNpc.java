package wind.model.players.packets.clicking.npcs;

import wind.Server;
import wind.model.npcs.pets.Pet;
import wind.model.players.Client;
import wind.model.players.Rights;
import wind.model.players.content.skills.impl.Fishing;
import wind.model.players.content.skills.impl.Thieving;
import wind.util.ScriptManager;

public class SecondClickNpc {

	public static void handleClick(Client c, int npcId) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (Thieving.pickpocketNPC(c, npcId)) {
			Thieving.attemptToPickpocket(c, npcId);
			return;
		}
		if (c.petSummoned == true && c.petID > 0) {
			if (c.petID == npcId) {
				Pet.pickUpPet(c, npcId);
			}
			else
				c.sendMessage("That's not your pet.");
		}

		/*
		 * if(Fishing.fishingNPC(c, npcType)) { Fishing.fishingNPC(c, 2,
		 * npcType); return; }
		 */
		switch (npcId) {
		case 490:
			if (c.slayerTask > 0) {
				c.getDH().sendDialogues(228, 3100);
			} else if (c.slayerTask == 0 || c.slayerTask == -1) {
				c.getSlayer().generateTask();
				c.getDH().sendNpcChat("Very well. You have been assigned a task.", "Check your chatbox for your assignment.", 588, "Slayer Master");
				c.getPA().sendFrame126(	"@whi@Task: @gre@" + c.taskAmount + " "
						+ Server.npcHandler.getNpcListName(c.slayerTask)
						+ " ", 7383);
				c.nextChat = 0;
			}
			break;
		case 3249:
			c.getShops().openShop(172);
			break;
		case 4423:
			c.getShops().openShop(171);
			break;
		case 2897:
		case 2898:
		case 2633:
		case 2182:
			c.getPA().openUpBank();
			break;
		case 1601:
			c.getShops().openShop(169);
			break;
		case 1033:
			c.getShops().openShop(139);
			break;
		case 1035:
			c.getShops().openShop(140);
			break;
		case 1580:
			c.getPA().openUpBank();
			break;
		case 317:
			c.getItems().addItem(12810, 1);
			c.getItems().addItem(12811, 1);
			c.getItems().addItem(12812, 1);
			c.sendMessage("@red@You recieve Ironman Armour, from Paul!");
			break;
	    case 1174:
			c.getShops().openShop(167);
			break; 
	//	case 437:
		//	SkillMasters.addSkillCape(c);
		//	break;


//		case 376:
//			Sailing.startTravel(c, 1);
//			break;

			/**case 1306:
			if (!c.isWearingEquipment()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;**/

		case 326:
		case 327:
		case 330:
		case 332:
		case 316: // BAIT + NET
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, true));
			else
				return;
			break;
		case 536:
			c.getShops().openShop(7);
			break;
		case 534:
			c.getShops().openShop(2);
			break;
		case 535:
			c.getShops().openShop(8);
			break;
		case 532:
			c.getShops().openShop(3);
			break;
		case 507:
			c.getShops().openShop(156);
			break;
		case 527:
			c.getShops().openShop(36);
			break;
		case 531:
			c.getShops().openShop(13);
			break;
		case 528:
			c.getShops().openShop(37);
			break;
		case 4642:
			c.getShops().openShop(71);
			break;
		case 530:
			c.getShops().openShop(38);
			break;
		//case 319:
		//case 323:
		//case 325: // BAIT + NET
		//	if (c.isFishing == false)
		//		Fishing.setupFishing(c, Fishing.forSpot(npcId, true));
		//	else
		//		return;
		//	break;
		case 310:
		case 311:
		case 314:
		case 315:
			//case 317: iron manf
		case 318:
		case 328:
		case 329:
		case 331:
		case 309: // BAIT + LURE
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, true));
			else
				return;
			break;
		case 312:
		case 321:
		case 324:// SWORDIES+TUNA-CAGE+HARPOON
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, true));
			else
				return;
			break;
		case 3231:
			c.getHide().setupInterface();
			break;
		case 313:
		case 322:
		case 334: // NET+HARPOON
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, true));
			else
				return;
			break;
		case 546:
			c.getShops().openShop(5);
			break;
		case 3247:
			c.getShops().openShop(163);
			break;
		case 506:
			c.getShops().openShop(156);
			break;

		case 953: // Banker
		case 2574: // Banker
		case 166: // Gnome Banker
		case 1702: // Ghost Banker // Banker
		case 495: // Banker
		case 496: // Banker
		case 497: // Banker
		case 498: // Banker
		case 499: // Banker
		case 567: // Banker
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
		case 400: //Banker
			c.getPA().openUpBank();
			break;

		case 548:
			c.getDH().sendDialogues(69, c.npcType);
			break;

		case 2258:
			c.sendMessage("This NPC do not have a shop so you cant trade him.");
			// c.getPA().startTeleport(3039, 4834, 0, "modern"); //first click
			// teleports second click open shops
			break;
		case 550:
			c.getPA().startTeleport2(2898, 4819, 0);
			break;
		case 637:
			c.getShops().openShop(6);
			break;

		case 1597:
			c.getDH().sendDialogues(400, c.npcType);
			break;
			
			/* Start of shops */
			case 538:
				c.getShops().openShop(1);
				break;
			case 537:
				c.getShops().openShop(4);
				break;

		default:
			ScriptManager.callFunc("npcClick2_" + npcId, c, npcId);
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
				c.sendMessage("Second Click Npc : " + npcId);
			}
			break;
		}
	}
}