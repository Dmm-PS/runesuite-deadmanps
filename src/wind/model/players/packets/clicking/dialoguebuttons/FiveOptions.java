package wind.model.players.packets.clicking.dialoguebuttons;

import wind.Config;
import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.players.content.Gamble;
import wind.util.Misc;

public class FiveOptions {

	public static void handleOption1(Client c) {
		switch (c.teleAction) {

		case 1: // rock crabs
			c.getPA().spellTeleport(2678 + Misc.random(3), 3718 + Misc.random(3), 0);
			break;
		

		case 2: // minigames
			c.getPA().spellTeleport(2892 + Misc.random(2), 3679 + Misc.random(2), 0);
			break;

		case 3: // bosses //gwd

			c.getPA().spellTeleport(2902, 3724, 0);
			break;

		case 4: // PK //Edgeville
			c.getPA().spellTeleport(Config.EDGEVILLE_PK_X,
					Config.EDGEVILLE_PK_Y, 0);
			break;

		case 5: // Skills //skills

			c.getPA().spellTeleport(2809, 3192, 0);
			break;

		case 7:
			c.getPA().spellTeleport(3118, 9851, 0);
			break;

		case 8:
			c.getPA().spellTeleport(3293, 3178, 0);
			break;

		case 9:
			c.getPA().spellTeleport(3226, 3263, 0);
			break;

		case 10:
			c.getPA().spellTeleport(2705, 9487, 0);
			break;

		case 11:
			c.getPA().spellTeleport(3228, 9392, 0);
			break;

		case 12:
			c.getPA().spellTeleport(3302, 9361, 0);
			break;

		case 13:
			if (c.playerMagicBook != 1) {
				c.getTeleportHandler().submitModern(Config.LUMBY_X,
						Config.LUMBY_Y);
			} else {
				c.getTeleportHandler().submitAncient(Config.LUMBY_X,
						Config.LUMBY_Y);
			}
			break;

		case 14:
			if (c.playerMagicBook != 1) {
				c.getTeleportHandler().submitModern(Config.TZHAAR_X,
						Config.TZHAAR_Y);
			} else {
				c.getTeleportHandler().submitAncient(Config.TZHAAR_X,
						Config.TZHAAR_Y);
			}
			break;

		case 15:
			if (c.dialogueAction == 3001) {
				new Gamble(c, 100000);
				c.getPA().removeAllWindows();
			}
			break;

		case 16:
			if (c.dialogueAction == 467) {
				// TODO
				//c.fade(2516, 3855, 0);
				c.nextChat = 468;
			}
			break;

		case 20:
			c.getPA().spellTeleport(3222, 3218, 0);// 3222 3218
			break;

		default:
			System.out.println("Unhandled teleAction: " + c.teleAction);
			break;
		}

		switch (c.dialogueAction) {
		case 4398:
			if (c.deposit1 == -1) {
				c.sendMessage("You have nothing stored in this slot!");
				c.getPA().closeAllWindows();
				return;
			} else {
			c.getItems().addItemToBank(c.deposit1, 1);
			c.getItems();
			c.sendMessage("@red@1x " + ItemAssistant.getItemName(c.deposit1) + "@bla@ has been placed in your bank.");
			c.deposit1 = -1;
			c.getPA().closeAllWindows();
			}
			break;
		case 10:
			c.getPA().spellTeleport(2845, 4832, 0);
			c.dialogueAction = -1;
			break;

		case 11:
			c.getPA().spellTeleport(2786, 4839, 0);
			c.dialogueAction = -1;
			break;

		case 12:
			c.getPA().spellTeleport(2398, 4841, 0);
			c.dialogueAction = -1;
			break;

		case 476: // skills2 //firemaking
			c.getPA().spellTeleport(2715, 3422, 0);
			break;

		case 477:
			// skills3 //mining
			c.getPA().spellTeleport(3016, 3339, 0);
			break;

		case 690: // skiling5 //strength
			c.getPA().spellTeleport(2846, 3541, 0);
			break;
			
		case 3001:
			new Gamble(c, 100000);
			c.getPA().removeAllWindows();
			break;
			
		case 13371:
			if (c.getItems().getItemAmount(995) >= 500000) { //500k
				c.flowerBetAmount = 500000;
				c.getDH().sendDialogues(13372, 0);
			}
			break;
			
		case 13372:
			c.flowerGuess = 2981; //Red
			c.getFlowerGame().startGame(c);
			c.dialogueAction = 0;
			break;

		default:
			System.out.println("Unhandled dialogueAction: "
					+ c.dialogueAction);
			break;
		}
	}
	
	public static void handleOptions2(Client c) {
		switch (c.teleAction) {

		case 1: // slay dungeon
			c.getPA().spellTeleport(2908, 9736, 0);
			break;

		case 2: // DUEL ARENA
			c.getPA().spellTeleport(3366, 3266, 0);
			break;

		case 3:
			// bosses //kbd
			c.getPA().spellTeleport(3007, 3849, 0);
			break;

		case 4: // PK //castle
			c.getPA().spellTeleport(Config.CASTLE_PK_X, Config.CASTLE_PK_Y,
					0);
			c.getPA().resetAutocast();
			break;

		case 5: // skills //cooking
			c.getPA().spellTeleport(3209, 3215, 0);
			break;

		case 7:
			c.getPA().spellTeleport(2859, 9843, 0);
			break;

		case 8:
			c.getPA().spellTeleport(2903, 9849, 0);
			break;

		case 9:
			c.getPA().spellTeleport(2916, 9800, 0);
			break;

		case 10:
			c.getPA().spellTeleport(3219, 9366, 0);
			break;

		case 11:
			c.getPA().spellTeleport(3237, 9384, 0);
			break;

		case 12:
			c.getPA().spellTeleport(2908, 9694, 0);
			break;

		case 13:
			c.getPA().spellTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0);
			break;

		case 20:
			c.getPA().spellTeleport(3210, 3424, 0);// 3210 3424
			break;

		default:
			System.out.println("Unhandled teleaction " + c.teleAction);
			break;
		}

		switch (c.dialogueAction) {
		
		

		case 10:
			c.getPA().spellTeleport(2796, 4818, 0);
			c.dialogueAction = -1;
			break;

		case 11:
			c.getPA().spellTeleport(2527, 4833, 0);
			c.dialogueAction = -1;
			break;

		case 12:
			c.getPA().spellTeleport(2464, 4834, 0);
			c.dialogueAction = -1;
			break;

		case 467:
			c.getPA().spellTeleport(Config.HOME_X, Config.HOME_Y, 0);
			c.dialogueAction = -1;
			break;

		case 476: // skills2 //fishing
			c.getPA().spellTeleport(2597, 3402, 0);
			break;

		case 477: // skills3 //rc
			c.getPA().spellTeleport(3083, 3512, 0);
			break;

		case 478: // skills4 //woodcutting
			c.getPA().spellTeleport(2969, 3423, 0);
			break;

		case 501:
			c.getPA().spellTeleport(Config.TRAWLER_X, Config.TRAWLER_Y, 0);
			break;

		case 690: // skiling5 //prayer
			c.getPA().spellTeleport(3052, 3481, 0);
			break;
		case 4398:
			if (c.deposit2 == -1) {
				c.sendMessage("You have nothing stored in this slot!");
				c.getPA().closeAllWindows();
				return;
			} else {
			c.getItems().addItemToBank(c.deposit2, 1);
			c.getItems();
			c.sendMessage("@red@1x " + ItemAssistant.getItemName(c.deposit2) + "@bla@ has been placed in your bank.");
			c.deposit2 = -1;
			c.getPA().closeAllWindows();
			}
			break;

		case 3001:
			new Gamble(c, 500000);
			c.getPA().removeAllWindows();
			break;
			
		case 13371:
			if (c.getItems().getItemAmount(995) >= 1000000) { //1M
				c.flowerBetAmount = 1000000;
				c.getDH().sendDialogues(13372, 0);
			}
			break;
			
		case 13372:
			c.flowerGuess = 2982; //Blue
			c.getFlowerGame().startGame(c);
			c.dialogueAction = 0;
			break;

		default:
			System.out.println("Unhandled dialogueAction "
					+ c.dialogueAction);
			break;

		}
	}
	
	public static void handleOption3(Client c) {
		switch (c.teleAction) {

		case 1: // Slayer monster options
			c.getDH().sendOption5("Hill Giants", "Hellhounds",
					"Lesser Demons", "Chaos Dwarf", "-- Next Page --");
			c.teleAction = 7;
			break;

		case 2:
			// minigames //tzhaar
			// c.getPA().spellTeleport(2444, 5170, 0);
			// barrows
			c.getPA().spellTeleport(3565, 3314, 0);
			break;

		case 3:
			// bosses //dag kings
			c.getPA().spellTeleport(2547, 3758, 0);
			break;

		case 4:
			// pk //magebank
			c.getPA().spellTeleport(2539, 4716, 0);
			c.getPA().resetAutocast();
			break;

		case 5:
			// skills //crafting
			c.getPA().spellTeleport(2935, 3283, 0);
			break;
		

		case 7:
			c.getPA().spellTeleport(2843, 9555, 0);
			break;

		case 8:
			c.getPA().spellTeleport(2912, 9831, 0);
			break;

		case 9:
			c.getPA().spellTeleport(3159, 9895, 0);
			break;

		case 10:
			c.getPA().spellTeleport(3241, 9364, 0);
			break;

		case 11:
			c.getPA().spellTeleport(3280, 9372, 0);
			break;

		case 12:
			c.getPA().spellTeleport(2739, 5088, 0);
			break;

		case 13:
			if (c.playerMagicBook != 1) {
				c.getTeleportHandler().submitModern(Config.CAMELOT_X,
						Config.CAMELOT_Y);
			} else {
				c.getTeleportHandler().submitAncient(Config.CAMELOT_X,
						Config.CAMELOT_Y);
			}
			break;

		case 20:
			c.getPA().spellTeleport(2757, 3477, 0);
			break;
		default:
			System.out.println("Unhandled teleAction: " + c.teleAction);
			break;
		}

		switch (c.dialogueAction) {
		case 4398:
			if (c.deposit3 == -1) {
				c.sendMessage("You have nothing stored in this slot!");
				c.getPA().closeAllWindows();
				return;
			} else {
			c.getItems().addItemToBank(c.deposit3, 1);
			c.getItems();
			c.sendMessage("@red@1x " + ItemAssistant.getItemName(c.deposit3) + "@bla@ has been placed in your bank.");
			c.deposit3 = -1;
			c.getPA().closeAllWindows();
			}
			break;
		case 10:
			c.getPA().spellTeleport(2713, 4836, 0);
			c.dialogueAction = -1;
			break;

		case 11:
			c.getPA().spellTeleport(2162, 4833, 0);
			c.dialogueAction = -1;
			break;

		case 12:
			c.getPA().spellTeleport(2207, 4836, 0);
			c.dialogueAction = -1;
			break;

		case 476:
			// skills2 //fletching
			c.getPA().spellTeleport(2822, 3441, 0);
			break;

		case 477:
			// skills3 //slayer
			c.getPA().spellTeleport(2873, 2980, 0);
			break;

		case 478:
			// skills4 //construction
			c.getPA().spellTeleport(2953, 3221, 0);
			break;

		case 501:
			c.getPA().spellTeleport(Config.CASTLEWARS_X,
					Config.CASTLEWARS_Y, 0);
			break;

		case 690:
			// skiling5 //defence
			c.getPA().spellTeleport(3221, 3237, 0);
			break;

		case 3001:
			new Gamble(c, 1000000);
			c.getPA().removeAllWindows();
			break;
			
		case 13371:
			if (c.getItems().getItemAmount(995) >= 2500000) { //2.5M
				c.flowerBetAmount = 2500000;
				c.getDH().sendDialogues(13372, 0);
			}
			break;
			
		case 13372:
			c.flowerGuess = 2983; //Yellow
			c.getFlowerGame().startGame(c);
			c.dialogueAction = 0;
			break;

		default:
			System.out.println("Unhandled dialogueAction: "
					+ c.dialogueAction);
			break;
		}
	}
	
	public static void handleOption4(Client c) {
		switch (c.teleAction) {

		case 1:
			// brimhaven dungeon
			c.getPA().spellTeleport(2710, 9466, 0);
			break;

		case 2:
			// minigames //duelarena
			c.getPA().spellTeleport(2662, 2650, 0);
			break;

		case 3:
			// bosses //KQ
			c.getPA().spellTeleport(3310, 3109, 0);
			break;

		case 4:
			// pk //hill giants
			c.getPA().spellTeleport(3288, 3631, 0);
			c.getPA().resetAutocast();
			break;

		case 5:
			// skills //farming
			c.getPA().spellTeleport(2815, 3338, 0);
			break;

		case 7:
			c.getPA().spellTeleport(2923, 9759, 0);
			break;

		case 8:
			c.getDH().sendOption5("Hill Giants", "Hellhounds",
					"Lesser Demons", "Chaos Dwarf", "-- Next Page --");
			c.teleAction = 7;
			break;

		case 9:
			c.getDH().sendOption5("Al-kharid warrior", "Ghosts",
					"Giant Bats", "-- Previous Page --", "-- Next Page --");
			c.teleAction = 8;
			break;

		case 10:
			c.getDH()
					.sendOption5("Goblins", "Baby blue dragon",
							"Moss Giants", "-- Previous Page --",
							"-- Next Page --");
			c.teleAction = 9;
			break;

		case 11:
			c.getDH().sendOption5("Black Demon", "Dust Devils",
					"Nechryael", "-- Previous Page --", "-- Next Page --");
			c.teleAction = 10;
			break;

		case 12:
			c.getDH().sendOption5("GarGoyle", "Bloodveld", "Banshee",
					"-- Previous Page --", "-- Next Page --");
			c.teleAction = 11;
			break;

		case 13:
			if (c.playerMagicBook != 1) {
				c.getTeleportHandler().submitModern(Config.FALADOR_X,
						Config.FALADOR_Y);
			} else {
				c.getTeleportHandler().submitAncient(Config.FALADOR_X,
						Config.FALADOR_Y);
			}
			break;

		case 3001:
			new Gamble(c, 10000000);
			c.getPA().removeAllWindows();
			break;

		default:
			System.out.println("Unhandled teleaction: " + c.teleAction);
			break;
		}

		switch (c.dialogueAction) {
		case 4398:
			if (c.deposit4 == -1) {
				c.sendMessage("You have nothing stored in this slot!");
				c.getPA().closeAllWindows();
				return;
			} else {
			c.getItems().addItemToBank(c.deposit4, 1);
			c.getItems();
			c.sendMessage("@red@1x " + ItemAssistant.getItemName(c.deposit4) + "@bla@ has been placed in your bank.");
			c.deposit4 = -1;
			c.getPA().closeAllWindows();
			}
			break;
		case 10:
			c.getPA().spellTeleport(2660, 4839, 0);
			c.dialogueAction = -1;
			break;

		case 11:
			// c.getPA().spellTeleport(2527, 4833, 0); astrals here
			// c.getRunecrafting().craftRunes(2489);
			c.dialogueAction = -1;
			break;

		case 12:
			// c.getPA().spellTeleport(2464, 4834, 0); bloods here
			// c.getRunecrafting().craftRunes(2489);
			c.dialogueAction = -1;
			break;

		case 476:
			// skills2 //herblore
			c.getPA().spellTeleport(2898, 3430, 0);
			break;

		case 477:
			// skills3 //smithing
			c.getPA().spellTeleport(2996, 3145, 0);
			break;

		case 478:
			// skills4 //hunter
			c.getPA().spellTeleport(2775, 2887, 0);
			break;

		case 501:
			// c.getPA().spellTeleport(Config.CASTLEWARS_X,
			// Config.CASTLEWARS_Y, 0);
			c.sendMessage("Coming soon.");
			break;

		case 690:
			// skiling5 //ranging
			c.getPA().spellTeleport(2667, 3427, 0);
			break;
			
		case 3001:
			new Gamble(c, 10000000);
			c.getPA().removeAllWindows();
			break;
			
		case 13371:
			if (c.getItems().getItemAmount(995) >= 5000000) { //5M
				c.flowerBetAmount = 5000000;
				c.getDH().sendDialogues(13372, 0);
			}
			break;
			
		case 13372:
			c.flowerGuess = 2984; //Purple
			c.getFlowerGame().startGame(c);
			c.dialogueAction = 0;
			break;

		default:
			System.out.println("Unhandled dialogueAction: "
					+ c.dialogueAction);
			break;
		}
	}
	
	public static void handleOption5(Client c) {

		switch (c.teleAction) {

		case 1:
			// traverly
			// c.getPA().spellTeleport(3297, 9824, 0);
			// c.sendMessage("@red@There's just frost dragons, if you want to kill green dragons you must go wilderness.");
			c.getPA().startTeleport(2884, 9798, 0, "modern");
			break;

		case 2:
			// minigames //more
			c.teleAction = -1;
			c.dialogueAction = -1;
			c.getDH().sendDialogues(501, -1);
			break;

		case 3:
			c.getDH().sendDialogues(504, -1);
			c.teleAction = -1;
			break;

		case 4:
			// pk //ardy lever
			c.getPA().spellTeleport(2561, 3311, 0);
			c.getPA().resetAutocast();
			break;

		case 5:
			// skills1
			c.teleAction = -1;
			c.dialogueAction = -1;
			c.getDH().sendDialogues(476, -1);
			break;

		case 7:
			c.getDH().sendOption5("Al-kharid warrior", "Ghosts",
					"Giant Bats", "-- Previous Page --", "-- Next Page --");
			c.teleAction = 8;
			break;

		case 8:
			c.getDH()
					.sendOption5("Goblins", "Baby blue dragon",
							"Moss Giants", "-- Previous Page --",
							"-- Next Page --");
			c.teleAction = 9;
			break;

		case 9:
			c.getDH().sendOption5("Black Demon", "Dust Devils",
					"Nechryael", "-- Previous Page --", "-- Next Page --");
			c.teleAction = 10;
			break;

		case 10:
			c.getDH().sendOption5("GarGoyle", "Bloodveld", "Banshee",
					"-- Previous Page --", "-- Next Page --");
			c.teleAction = 11;
			break;

		case 11:
			c.getDH().sendOption5("Infernal Mage", "Dark Beasts",
					"Abyssal Demon", "-- Previous Page --", "");
			c.teleAction = 12;
			break;

		case 13:
			if (c.playerMagicBook != 1) {
				c.getTeleportHandler().submitModern(Config.CANFIS_X,
						Config.CANFIS_Y);
			} else {
				c.getTeleportHandler().submitAncient(Config.CANFIS_X,
						Config.CANFIS_Y);
			}
			break;

		default:
			System.out.println("Unhandled teleaction: " + c.teleAction);
			break;

		}

		switch (c.dialogueAction) {
		case 4398:
			if (c.deposit5 == -1) {
				c.sendMessage("You have nothing stored in this slot!");
				c.getPA().closeAllWindows();
				return;
			} else {
			c.getItems().addItemToBank(c.deposit5, 1);
			c.getItems();
			c.sendMessage("@red@1x " + ItemAssistant.getItemName(c.deposit5) + "@bla@ has been placed in your bank.");
			c.deposit5 = -1;
			c.getPA().closeAllWindows();
			}
			break;
		case 10:
		case 11:
			c.dialogueId++;
			c.getDH().sendDialogues(c.dialogueId, 0);
			break;

		case 12:
			c.dialogueId = 17;
			c.getDH().sendDialogues(c.dialogueId, 0);
			break;

		case 476:
			// skills2
			c.teleAction = -1;
			c.dialogueAction = -1;
			c.getDH().sendDialogues(477, -1);
			break;

		case 477:
			// skills3
			c.teleAction = -1;
			c.dialogueAction = -1;
			c.getDH().sendDialogues(478, -1);
			break;

		case 478:
			// skilling4 //next
			c.getDH().sendDialogues(690, -1);
			break;

		case 690:
			// skilling5 //first page
			c.getDH().sendOption5("Agility", "Cooking", "Crafting",
					"Farming", "Next ->");
			c.teleAction = 5;
			c.dialogueAction = -1;
			break;
			
		case 3001:
			c.sendMessage("Come back when you're feeling lucky.");
			c.getPA().removeAllWindows();
			break;
			
		case 13371:
			if (c.getItems().getItemAmount(995) >= 10000000) { //10M
				c.flowerBetAmount = 10000000;
				c.getDH().sendDialogues(13372, 0);
			}
			break;
			
		case 13372:
			c.flowerGuess = 2985; //Orange
			c.getFlowerGame().startGame(c);
			c.dialogueAction = 0;
			break;

		default:
			System.out.println("Unhandled dialogueAction: "
					+ c.dialogueAction);
			break;
		}
	}
}
