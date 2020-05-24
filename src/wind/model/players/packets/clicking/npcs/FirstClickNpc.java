package wind.model.players.packets.clicking.npcs;

import wind.model.players.Client;
import wind.model.players.Rights;
import wind.model.players.content.minigames.Sailing;
import wind.model.players.content.skills.impl.Fishing;
import wind.model.players.content.skills.impl.Fishing.Spot;
import wind.util.ScriptManager;

public class FirstClickNpc {

	public static void handleClick(Client c, int npcId) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0; 

		//Pet.pickUpPetRequirements(c, npcId);
		if(c.getHunter().hasReqs(npcId)){
			c.getHunter().Checking(npcId);
		} else {
			//c.sendMessage("They don't have anything interesting to say.");
		}

		for (Spot i : Fishing.Spot.values()) {
			if (npcId == i.getNPCId())
				Fishing.setupFishing(c, i);
		}
			 
		

		switch (npcId) {
		case 3004: //Neitznot -> Port Sarim
		case 3006:
			c.getPA().movePlayer(3038, 3193, 0);
			break;
		case 3007: //Port Sarim -> Neitznot
			c.getPA().movePlayer(2310, 3780, 0);
			break;
		case 3005: //Port Sarim -> Neitznot
			c.getPA().movePlayer(2423, 3782, 0);
			break;
		case 2804:
		case 2801:
			if (c.getItems().playerHasItem(5603)) {
				c.getItems().addItem(1759, 1);
			} else {
				c.sendMessage("You need shears for this!");
			}
			break;
		case 3249:
			c.getShops().openShop(172);
			break;
		case 2184:
			c.getShops().openShop(111);
			break;
		case 2183:
			c.getShops().openShop(112);
			break;
		case 2180:
			c.getShops().openShop(113);
			break;
		case 5786:
			c.getDH().sendDialogues(5786, 5786);
			break;
		case 3247:
			c.getShops().openShop(169);
			break;
		case 4423:
			c.getShops().openShop(171);
			break;
		case 4397:
			c.getDH().sendDialogues(4397, 4397);
			break;
		case 3343:
			c.getDH().sendDialogues(3343, 3343);
			break;
		/*case 2130:
		case 2131:
		case 2132:
			c.getDH().sendDialogues(4000, c.petID); 
			break;*/
/*deadman npcsa*/
			//starter gear
		case 1580:
		case 2182:
			c.getPA().openUpBank();
			break;
			// shops added 4/23/16
		case 6059:
			c.getShops().openShop(141);
			break;
		case 1177:
		case 1178:
			c.getShops().openShop(29);
			break;
		case 2366:
			c.getShops().openShop(160);
			break;
		case 2989:
			c.getShops().openShop(106);
			break;
		case 1032:
			c.getShops().openShop(61);
			break;
		case 4407:
			c.getShops().openShop(170);
			break;
			
			//
			
		case 524:
			c.getShops().openShop(43);
			break;
		case 1033:
			c.getShops().openShop(139);
			break;
		case 1035:
			c.getShops().openShop(140);
			break;
		case 1601:
			c.getShops().openShop(169);
			break;
		case 3953:
			c.getShops().openShop(41);
			break;
		case 3994:
			c.getDH().sendDialogues(3994, 3994);
			break;
		case 385:
			c.getDH().sendDialogues(588, 385);
			break;
		case 3893:
			c.getDH().sendDialogues(3893, 3893);
			break;
		case 3083:
		case 3078:
			c.getDH().sendDialogues(10013, 3083);
			break;
		case 5118:
			c.getDH().sendDialogues(5118, 5118);
			break;
		case 2658:
		c.getShops().openShop(162);
		break;
		case 1174:
			c.getShops().openShop(167);
			break;
		case 6060:
			c.getShops().openShop(7);
			break;
		case 537:
			c.getShops().openShop(4);
			break;
		case 3069:
			c.getDH().sendDialogues(2300, 3069);
			break;
		case 3246:
			c.getShops().openShop(166);
			break;
		case 506:
		case 507:
			c.getShops().openShop(156);
			break;
		case 1044:
			c.getShops().openShop(59);
			break;
		case 529:
			c.getShops().openShop(5);
			break;
		case 4642:
			c.getShops().openShop(71);
			break;
		case 3231:
			c.getHide().setupInterface();
			break;
		case 317:
			c.getDH().sendDialogues(5050, 317);
			break;
		case 1045:
			c.getShops().openShop(10);
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
		case 530:
			c.getShops().openShop(38);
			break;
		case 5557:
			c.getDH().sendDialogues(6500, 5557);
			break;
		case 5558:
			c.getDH().sendDialogues(5558, 5558);
			break;
		case 5527:
			c.getShops().openShop(164);
					c.sendMessage("You have  "+ c.memberPoints +" member points");
			break;
			
		case 6088:
			c.getPA().showInterface(802);
		//	c.getDH().sendDialogues(5050, 6088);
break;
		case 1398: //PARTY PETE
			c.getDH().sendDialogues(13371, 0);
			break;

		case 5834: //NMZ
			c.getDH().sendDialogues(5150, 5834);
			break;

		case 2897:
		case 2898:
		case 2633:
			c.getPA().openUpBank();
			break;
		case 1696: //Old Man (Barrows)
			c.getDH().sendDialogues(1003, 1696);
			break;

		case 2996:
			c.getDH().sendDialogues(1007, 1695);
			break;

		case 4297: // str
			c.getDH().sendDialogues(444, npcId);
			break;

		case 4288: // atack
			c.getDH().sendDialogues(445, npcId);
			break;

		case 705: // def
			c.getDH().sendDialogues(449, npcId);
			break;

		case 961: // hp
			c.getDH().sendDialogues(447, npcId);
			break;

		case 1658: // magic
			c.getDH().sendDialogues(455, npcId);
			break;

		case 802: // pray
			c.getDH().sendDialogues(451, npcId);
			break;

		case 682: // ranged
			c.getDH().sendDialogues(453, npcId);
			break;

		case 490: // slayer
			c.getDH().sendDialogues(442, 490);
			break;

		case 3295: // mining
			c.getDH().sendDialogues(427, npcId);
			break;

			// case 553: //rc
			// c.getDH().sendDialogues(421, npcType);
			// break;

		case 604:// smithing
			c.getDH().sendDialogues(429, npcId);
			break;

		case 308: // fishing
			c.getDH().sendDialogues(431, npcId);
			break;

		case 4946:// fm
			c.getDH().sendDialogues(435, npcId);
			break;
		case 4626:
			c.getDH().sendDialogues(100, npcId);
			break;
		case 847: // cook
			c.getDH().sendDialogues(433, npcId);
			break;

		case 575: // fletching
			c.getDH().sendDialogues(425, npcId);
			break;

		case 805: // crafting
			c.getDH().sendDialogues(423, npcId);
			break;

		case 3299: // farming
			c.getDH().sendDialogues(439, npcId);
			break;

		case 4906: // wc
			c.getDH().sendDialogues(437, npcId);
			break;

		case 455: // herbaloreh
			c.getDH().sendDialogues(417, npcId);
			break;

		case 2270: // theivng
			c.getDH().sendDialogues(419, npcId);
			break;

		case 437: // agility
			c.getDH().sendDialogues(415, npcId);
			break;
		case 118:
			c.getItems().addItem(983, 1);
			c.sendMessage("@red@Ya boi Ignatus hooks you up with a brass key!");
			break;

		case 1704:
			Sailing.startTravel(c, 2);
			break;

		case 2138:
			Sailing.startTravel(c, 3);
			break;

		case 1304:
			Sailing.startTravel(c, 4);
			break;

		case 220:
			c.getDH().sendDialogues(36899, npcId);
			break;

		case 3001: // Gambler
			c.getDH().sendDialogues(3001, 3001);
			break;

			// FISHING
		case 319:
		case 329:
		case 323:
			// case 325:
		case 326:
		case 327:
		case 330:
		case 332: // NET + BAIT
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, false));
			else
				return;
			break;
		case 325:
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, false));
			else
				return;
			break;
	//	case 334:
	//	case 313: // NET + HARPOON
	//		if (c.isFishing == false)
	//			Fishing.setupFishing(c, Fishing.forSpot(npcId, false));
	//		else
	//			return;
	//		break;
		case 322: // NET + HARPOON
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, false));
			else
				return;
			break;

		/*case 309: // LURE
		case 310:
		case 311:
		case 314:
		case 315:
			//case 317:
		case 318:
		case 328:
		case 331:
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, false));
			else
				return;
			break;

		case 312:
		case 321:
		case 324: // CAGE + HARPOON
			if (c.isFishing == false)
				Fishing.setupFishing(c, Fishing.forSpot(npcId, false));
			else
				return;
			break;
			*/
		case 1518: // Shrimp + Anchovies
			Fishing.attemptdata(c, 1);
			break;
		case 4928:
			Fishing.attemptdata(c, 11);
			break;
		case 6731:
			Fishing.attemptdata(c, 13);
			break;
		case 1517:
			Fishing.attemptdata(c, 12);
			break;
		case 334:
		case 313: // NET + HARPOON
			Fishing.attemptdata(c, 3);
			break;
		case 1520:
			Fishing.attemptdata(c, 5);
			break;
			/*case 6599:
			c.getDH().sendOption3("Can you Exchange my Emblems for PKP?",
					"Can you Exchange my Emblems for Gold?", " ");
			c.dialogueAction = 1758;
			break;*/
			
		
		case 309: // LURE fissheysSS
		case 310:
		case 311:
		case 314:
	//	case 315:
	//	case 317:
		case 318:
		case 328:
		case 331:
			Fishing.attemptdata(c, 4);
			break;

		case 1510:
			Fishing.attemptdata(c, 8);
			break;

		case 944:
			c.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons",
					"Chaos Dwarf", "-- Next Page --");
			c.teleAction = 7;
			break;

		case 382:
			c.getShops().openShop(33);
			break;
		case 659:
			c.getShops().openShop(11);
			break;

		case 505:
			c.getShops().openShop(7);
			break;
		case 538:
			c.getShops().openShop(1);
			break;
		case 535:
			c.getShops().openShop(8);
			break;
		case 536:
			c.getShops().openShop(7);
			break;

		case 2109:
			c.getShops().openShop(18);
			break;
		case 534:
			c.getShops().openShop(2);
			break;


			/*case 490: 
			if (c.slayerTask <= 0) { 
				c.getDH().sendDialogues(11, c.npcType); 
				c.sendMessage("You must complete or reset your slayer task before start another.");
			} 
			break;*/



		case 400: //banker
			c.getDH().sendDialogues(1013, c.npcType);
			break;
		case 1986:
			c.getDH().sendDialogues(2244, c.npcType);
			break;
		case 306:
			c.getDH().sendDialogues(9199, 2244);
			break;

		case 5523:
			c.getDH().sendDialogues(222, 659);
	break;
		case 1936:
			c.getShops().openShop(58);
			break;

		case 550:
		case 637:
			c.getShops().openShop(6);
			break;
		case 119:
			c.getDH().sendDialogues(9990, 119);
			break;
		case 1617:
			c.getDH().sendDialogues(504, 1617);
			break;

		case 3916:
			c.getShops().openShop(8);
			break;

		case 1309:
			c.getShops().openShop(6);
			break;

		case 5000:
			c.getShops().openShop(6);
			break;
		case 519:
			c.getShops().openShop(6);
			break;
		case 5036:
			c.getShops().openShop(55);
			break;
		case 814: //champs
			c.getShops().openShop(173);
			break;
		case 810: //hero
			c.getShops().openShop(174);
			break;
		case 811: //warrior
			c.getShops().openShop(175);
			break;
		case 4500: //Fremmy
			c.getShops().openShop(176);
			break;
		case 1459: //Scimi
			c.getDH().sendDialogues(1460, 925);
			break;
		case 548:
			c.getDH().sendDialogues(69, c.npcType);
			break;

		case 2258:
			c.getDH().sendOption2("Teleport me to Runecrafting Abyss.",
					"I want to stay here, thanks.");
			c.dialogueAction = 2258;
			break;
		case 532:
			c.getShops().openShop(3);
			break;

		case 541:
			c.getShops().openShop(2);
			break;

		case 198:
			c.getShops().openSkillCape();
			break;

		default:
			ScriptManager.callFunc("npcClick1_" + npcId, c, npcId);
			c.getDH().sendDialogues(2000, npcId);
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
				c.sendMessage("First Click Npc : " + npcId);
			}
			break;
		}
	}
}
