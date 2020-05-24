package wind.model.players.content;



import wind.Config;
import wind.Constants;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.saving.PlayerSave;

public class DialogueHandler {

	private Client c;
	@SuppressWarnings("unused")
	private int next;
	public void setPlayer(wind.model.players.Player player) {
		this.player = player;
	}
	
	public void execute() {
	}

	public DialogueHandler(Client client) {
		this.c = client;
	}

	/**
	 * Handles all talking
	 * 
	 * @param dialogue
	 *            The dialogue you want to use
	 * @param npcId
	 *            The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch (dialogue) {
		case 5786:
			sendNpcChat("Would ya like to visit the mole tunnels?", 5786, "Simon Templeton");
			c.nextChat = 5787;
			break;
		case 5787:
			sendOption2("Yes, I'm not scared.", "No, I'm a bitch.");
			c.nextChat = 0;
			c.dialogueAction = 5787;
			break;
		case 4397:
			sendOption2("Withdraw Items", "Deposit Items");
			c.nextChat = 0;
			c.dialogueAction = 4397;
			break;
			case 350:
				sendStatement("You've found a hidden tunnel, do you want to enter?");
				c.nextChat = 351;
				break;
				
			case 351:
				sendOption2("Yeah I'm fearless!", "No way, that looks scary!");
				c.nextChat = 0;
				c.dialogueAction = 2134;
				break;
		case 228:
			sendNpcChat3("You already have a task.", "Would you like to reset your current one?", "@red@Keep in mind this costs 30 Slayer points.", 490, "Nieve");
			c.nextChat = 229;
			break;
		case 352:
			sendStatement("You loot the chest, return to the surface?");
			c.nextChat = 354;
			break;
		
			
		case 353:
			sendStatement("The chest is empty, return to the surface?");
			c.nextChat = 354;
			break;
			
		case 354:
			sendOption2("Yes", "No");
			c.nextChat = 0;
			c.dialogueAction = 2135;
			break;
		case 3343:
			sendNpcChat2("Greetings, are you looking for HP", "insurance or for stat protection?", 3343, "Surgeon General Tafani");
			c.nextChat = 3344;
			break;
		case 3344:
			sendOption2("HP Protection", "Stat Protection");
			c.nextChat = 0;
			c.dialogueAction = 3344;
			break;

	case 2135:
			c.getPA().removeAllWindows();
			c.getBarrows().getMaze().teleportToMaze();
			break;



		case 229:
			sendOption2("Yes", "No");
			c.nextChat = 0;
			c.dialogueAction = 31;
			break;

		case 220:
			sendNpcChat3("I am the Cash King! ", "You can exchange your gold with ",
					"my valuable tickets!", 220, "Cash King");
			c.nextChat = 221;
			break;

		case 221:
			sendNpcChat2("These tickets can be traded, staked or PKed.", "So becareful not to lose them.", 220, "Cash King");
			c.nextChat = 0;
			break;

			/**
			 * Fight Caves Enter
			 */
		case 2617:
			sendNpcChat2("You're on your own now Jal'Yt,"
					+ " prepare to fight for ", "your life", 2617, "TzHaar-Mej-Jal");
			break;
		case 5558:
			sendNpcChat2("Nice, you got a callisto pet. That's pretty cool.",
					 "Don't lose me! Donate or die! Sub to Sam!", 5558, "Callisto Cub");
			break;
		case 24:
			sendOption2("Pick the flowers", "Leave the flowers");
			c.nextChat = 0;
			c.dialogueAction = 24;
			break;

		case 13371:
			c.getPA().sendFrame126("Your Bet", 2493);
			c.getPA().sendFrame126("500K", 2494);
			c.getPA().sendFrame126("1000K", 2495);
			c.getPA().sendFrame126("2500K", 2496);
			c.getPA().sendFrame126("5000K", 2497);
			c.getPA().sendFrame126("10M", 2498);
			c.getPA().sendFrame164(2492);
			c.dialogueAction = 13371;
			break;
		case 13372:
			c.getPA().sendFrame126("Flower Colour", 2493);
			c.getPA().sendFrame126("Red", 2494);
			c.getPA().sendFrame126("Blue", 2495);
			c.getPA().sendFrame126("Yellow", 2496);
			c.getPA().sendFrame126("Purple", 2497);
			c.getPA().sendFrame126("Orange", 2498);
			c.getPA().sendFrame164(2492);
			c.dialogueAction = 13372;
			break;

		case 5150:
			sendNpcChat4(
					"Hey, " + c.playerName + " I can take you to your dream.",
					"In the dream you will be defeating bosses of " + Config.SERVER_NAME + ".",
					"Remember to be prepared for battle before this dream starts.",
					"", c.talkingNpc, "Surok Magic");
			c.nextChat = 5151;
			break;

		case 5151:
			sendOption4("Hard mode", "Medium mode", "Easy mode", "Nevermind, I haven't prepared for this..");
			c.dialogueAction = 171;
			c.nextChat = 0;
			break;

			/* Barrows */
		case 993:
			c.getDH()
			.sendStartInfo(
					"As you collect your reward, you notice an aweful smell.",
					"You look below the remaining debris to the bottom of the",
					"chest. You see a trapdoor. You open it and it leads to a ladder",
					"that goes down a long ways.", "Continue?");
			break;
		case 994:
			c.getDH().sendStatement("Would you like to continue?");
			c.nextChat = 995;
			break;
		case 995:
			c.dialogueAction = 25;
			c.getDH().sendOption2("Yes, I'm not afraid of anything!",
					"No way, the smell itself turns me away.");
			break;
		case 2300:
			sendNpcChat2("Welcome to the Blue Moon Inn!", "It's one of the few safe Inns in SamManMode.", 3069, "Barbarian");
			c.nextChat = 2301;
			break;
		case 2301:
			sendNpcChat2("Be careful, the basement is very dangerous,","although it can also be quite rewarding.", 3069, "Barbarian");
			c.nextChat = 0;
			break;
		case 996:
			c.getDH().sendStatement(
					"This is a very dangerous minigame, are you sure?");
			c.nextChat = 997;
			break;
		case 997:
			c.dialogueAction = 27;
			sendOption2("Yes, I'm a brave warrior!",
					"Maybe I shouldn't, I could lose my items!");
			break;
		case 998:
			c.getDH().sendStatement("Congratulations, You've completed the barrows challenge!");
			c.nextChat = 0;
			break;
		case 999:
			sendStatement("Are you ready to visit the chest room?");
			c.nextChat = 1000;
			c.dialogueAction = 29;
			break;
		case 1000:
			sendOption2("Yes, I've killed all the other brothers!",
					"No, I still need to kill more brothers");
			c.nextChat = 0;
			break;

		case 1001:
			c.getDH().sendStatement("Would you like to choose your reward?");
			c.nextChat = 1002;
			break;
		case 1002:
			c.dialogueAction = 1002;
			c.getDH().sendOption2("Barrows Chest.", "Random server reward.");
			break;

		case 1003:
			sendNpcChat("Hello, what can I do for you?", CONTENT, "Old Man");
			c.nextChat = 1004;
			break;

		case 1004:
			if (c.barrowsKillCount == 5) {
				sendPlayerChat("I have 5 killcount!", HAPPY);
			} else {
				sendPlayerChat("Umm...", CONTENT);
			}
			c.nextChat = 1005;
			break;

		case 1005:
			c.dialogueAction = 30;
			sendOption2("Fix Barrows Equipment", "Go into the tunnel");
			break;

		case 1006:
			sendNpcChat2("You need more kill count.",
					"Try using a spade on these mounds nearby.", 1696,
					"Old Man");
			c.nextChat = 0;
			break;

		case 1007:
			if (c.getEquipment().freeSlots() >= 1
			&& !c.getEquipment().playerHasItem(952)) {
				sendNpcChat("Here, take this. This may be useful to you.",
						HAPPY, "Old Crone");
				c.getItems().addItem(952, 1);
			} else {
				sendNpcChat2("I would suggest using that spade on",
						"those mounts over there", 1695, "Old Crone");
			}
			c.nextChat = 0;
			break;
			
		case 1008:
			sendOption2("Yeah, I'm sure!", "Nah, nevermind.");
			c.dialogueAction = 1008;
			break;
			
		case 1009:
			sendOption2("Yes.", "No thanks.");
			c.dialogueAction = 1009;
			break;

		case 1010:
			sendOption2("Yes.", "No thanks.");
			c.dialogueAction = 1010;
			break;
			/* END of Barrows */

			/* Start of Skill Master Dialogues */
			// HUNTER
		case 551:
			sendNpcChat("Hello, I'm the master of Hunter.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 552;
			break;

		case 552:
			sendOption3("Show me your store.", "I achieved 99 Hunter.",
					"Never mind");
			c.dialogueAction = 552;
			break;
			// CONSTRUCTION
		case 549:
			sendNpcChat("Hello, I'm the master of Construction.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 550;
			break;

		case 550:
			sendOption4("Show me your store.", "Teleport me to construction.",
					"I achieved 99 Construction.", "Never mind");
			c.dialogueAction = 550;
			break;
			// agility
		case 415:
			sendNpcChat("Hello, I'm the master of Agility.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 416;
			break;

		case 416:
			sendOption4("Show me your store.",
					"Teleport me to Gnome Agility Course.",
					"I achieved 99 Agility.", "Never mind");
			c.dialogueAction = 416;
			break;

			// herblore
		case 417:
			sendNpcChat("Hello, I'm the master of Herblore.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 418;
			break;

		case 418:
			sendOption3("Show me your store.", "I achieved 99 Herblore.",
					"Never mind");
			c.dialogueAction = 418;
			break;

			// thieving
		case 419:
			sendNpcChat("Hello, I'm the master of Thieving.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 420;
			break;

		case 420:
			sendOption4("Show me your store.",
					"Bring me to another thieving place.",
					"I achieved 99 Thieving.", "Never mind");
			c.dialogueAction = 420;
			break;

			// Runecrafting
		case 421:
			sendNpcChat("Hello, I'm the master of Runecrafting.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 422;
			break;

		case 422:
			sendOption4("Show me your store.", "Bring me to the abyss.",
					"I achieved 99 Runecrafting.", "Never mind");
			c.dialogueAction = 422;
			break;

			// Crafting
		case 423:
			sendNpcChat("Hello, I'm the master of Crafting.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 424;
			break;

		case 424:
			sendOption3("Show me your store.", "I achieved 99 Crafting.",
					"Never mind");
			c.dialogueAction = 424;
			break;

			// Fletching
		case 425:
			sendNpcChat("Hello, I'm the master of Fletching.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 426;
			break;

		case 426:
			sendOption3("Show me your store.", "I achieved 99 Fletching.",
					"Never mind");
			c.dialogueAction = 426;
			break;

			// Mining
		case 427:
			sendNpcChat("Hello, I'm the master of Mining.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 428;
			break;

		case 428:
			sendOption4("Show me your store.",
					"Bring me to another mining place.",
					"I achieved 99 Mining.", "Never mind");
			c.dialogueAction = 428;
			break;

			// Smithing
		case 429:
			sendNpcChat("Hello, I'm the master of Smithing.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 430;
			break;

		case 430:
			sendOption3("Show me your store.", "I achieved 99 Smithing.",
					"Never mind");
			c.dialogueAction = 430;
			break;

			// Fishing
		case 431:
			sendNpcChat("Hello, I'm the master of Fishing.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 432;
			break;

		case 432:
			sendOption4("Show me your store.",
					"Bring me to another fishing place.",
					"I achieved 99 Fishing.", "Never mind");
			c.dialogueAction = 432;
			break;

			// Cooking
		case 433:
			sendNpcChat("Hello, I'm the master of Cooking.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 434;
			break;

		case 434:
			sendOption3("Show me your store.", "I achieved 99 Cooking.",
					"Never mind");
			c.dialogueAction = 434;
			break;

			// firemaking
		case 435:
			sendNpcChat("Hello, I'm the master of Firemaking.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 436;
			break;

		case 436:
			sendOption3("Show me your store.", "I achieved 99 Firemaking.",
					"Never mind");
			c.dialogueAction = 436;
			break;

			// Woodcutting
		case 437:
			sendNpcChat("Hello, I'm the master of Woodcutting.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 438;
			break;

		case 438:
			sendOption3("Show me your store.", "I achieved 99 Woodcutting.",
					"Never mind");
			c.dialogueAction = 438;
			break;

			// farming
		case 439:
			sendNpcChat("Hello, I'm the master of Farming.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 440;
			break;

		case 440:
			sendOption3("Show me your store.", "I achieved 99 Farming.",
					"Never mind");
			c.dialogueAction = 440;
			break;

			// slayer
		case 442:
			sendNpcChat("Hello, I'm the master of Slayer.",
					"What can I do for you?", 490, "Nieve");
			c.nextChat = 443;
			break;

		case 443:
			sendOption4("Have you anything for sale?", "I achieved 99 Slayer.",
					"Can I upgrade my ring?", "Never mind, sorry.");
			c.dialogueAction = 443;
			break;
		case 666:
			sendNpcChat("Sure. Which ring would you like to upgrade?", CALM, "Nieve");
			c.nextChat = 667;
			break;
		case 667:
			sendOption4("Berserker Ring -> @blu@Berserker Ring (i)", "Seers' Ring -> @or2@Seers' Ring (i)", "Archers' Ring -> @gre@Archers' Ring (i)", "Never mind, thanks.");
			c.dialogueAction = 667;
			break;
		case 668:
			sendNpcChat3("Of course. However, this will replace the", "current ring in your inventory & cost @red@75@bla@ Slayer points.", "You currently have @red@"+c.slayerPoints+"@bla@ Slayer points.", 
					490, "Nieve");
			c.nextChat = 669;
			break;
		case 669:
			int[] rings = {6733, 6731, 6737};
			int ringCount = 0;
			sendOption2("Sure thing!", "No, thanks.");
			if (c.getItems().playerHasItem(6737, 1)) {
				c.dialogueAction = 669;
			}
			if  (c.getItems().playerHasItem(6731, 1)) {
				c.dialogueAction = 670;
			}
			if (c.getItems().playerHasItem(6733, 1)) {
				c.dialogueAction = 671;
			}
			for (int i = 0; i < rings.length; i++) {
				if (c.getItems().playerHasItem(rings[i], 1)) {
					ringCount++;
					System.out.println(ringCount);
				}
			}
			if (ringCount >=2) {
				sendStatement("You can only have one type of ring at a time while upgrading.");
				c.nextChat = 0;
			}
			break;
			// strength
		case 444:
			sendNpcChat("Hello, I'm the master of Strength.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 441;
			break;

		case 441:
			sendOption3("Show me your store.", "I achieved 99 Strength.",
					"Never mind");
			c.dialogueAction = 441;
			break;

			// attack
		case 445:
			sendNpcChat("Hello, I'm the master of Attack.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 446;
			break;

		case 446:
			sendOption3("Show me your store.", "I achieved 99 Attack.",
					"Never mind");
			c.dialogueAction = 446;
			break;

			// hp
		case 447:
			sendNpcChat("Hello, I'm the master of Hitpoints.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 448;
			break;

		case 448:
			sendOption3("Show me your store.", "I achieved 99 Hitpoints.",
					"Never mind");
			c.dialogueAction = 448;
			break;

			// Defence
		case 449:
			sendNpcChat("Hello, I'm the master of Defence.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 450;
			break;

		case 450:
			sendOption3("Show me your store.", "I achieved 99 Defence.",
					"Never mind");
			c.dialogueAction = 450;
			break;

			// Prayer
		case 451:
			sendNpcChat("Hello, I'm the master of Prayer.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 452;
			break;

		case 452:
			sendOption3("Show me your store.", "I achieved 99 Prayer.",
					"Never mind");
			c.dialogueAction = 452;
			break;

			// Ranging
		case 453:
			sendNpcChat("Hello, I'm the master of Ranging.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 454;
			break;

		case 454:
			sendOption3("Show me your store.", "I achieved 99 Ranging.",
					"Never mind");
			c.dialogueAction = 454;
			break;

			// Magic
		case 455:
			sendNpcChat("Hello, I'm the master of Magic.",
					"What can I do for you?", CALM, "Skill Master");
			c.nextChat = 456;
			break;
			/* END of Skill Master Dialogues */

		case 456:
			sendOption3("Show me your store.", "I achieved 99 Magic.",
					"Never mind");
			c.dialogueAction = 456;
			break;

		case 222:
			sendNpcChat3(
					"Welcome to the Item Lottery!",
					"You can spend cash, for a chance to win some valuable items.,",
					"You can either spend cash, or use donator points!", 5523, "Hatius Cosaintus");
			c.nextChat = 223;
			break;

		case 223:
			sendNpcChat1("Would you like to give it a try?", 5523, "Hatius Cosaintus");
			c.nextChat = 224;
			break;

		case 224:
			sendOption2("Yes", "No");
			c.dialogueAction = 17;
			break;

		case 225:
			sendOption2("Use 2.5M", "Use 250 Donator Points");
			c.dialogueAction = 18;
			break;

		case 226:
			sendNpcChat1("Sorry, you need " + Constants.LOTTERY_FUND
					+ " to play the Item Lottery.", 5523, "Hatius Cosaintus");
			c.nextChat = -1;
			break;

		case 460:
			sendNpcChat2("Welcome to " + Config.SERVER_NAME + "! ",
					"I will be your guide today, you can call me, Guide.",
					2244, Config.SERVER_NAME + " Guide");
			c.nextChat = 461;
			break;
		case 461:
			sendPlayerChat1("Nice to meet you!");
			c.nextChat = 462;
			break;
		case 462:
			sendNpcChat3("To start off, this is home,",
					"Miscellania is a wonderful place!",
					"Here on the island you have everything you need.", 2244,
					"Lumbridge Guide");
			c.nextChat = 463;
			break;
		case 9199:
			sendNpcChat3("Welcome to SamManMode,",
					"it truly is a wonderful place!",
					"I have a task for you!", 3393,
					"Lumbridge Guide");
			c.nextChat = 9201;
			break;
		case 9201:
			sendNpcChat1("If you see a scrub named Artz...", 3393, 
					"Lumbridge Guide");
			c.nextChat = 9202;
			break;
		case 9202:
			sendNpcChat1("Kill him.", 3393,
					"Lumbridge Guide");
			c.nextChat = 0;
			break;
		case 463:
			sendNpcChat2(
					"We have scattered all over, skills, teleports, minigames,",
					"Along with a few other stuff.", 2244, "Lumbridge Guide");
			c.nextChat = 464;
			break;
		case 464:
			sendNpcChat2("Here on " + Config.SERVER_NAME,
					" We thrive for a great server for everyone to enjoy.",
					2244, "Lumbridge Guide");
			c.nextChat = 465;
			break;
		case 465:
			sendNpcChat2("If you have any suggestions to improve "
					+ Config.SERVER_NAME,
					" Visit the forums, we would love your feedback.", 2244,
					"Lumbridge Guide");
			c.nextChat = 466;
			break;
		case 466:
			sendNpcChat2("Ok, I'm done rambling. ",
					"Would you like to see the world of " + Config.SERVER_NAME,
					2244, "Lumbridge Guide");
			c.nextChat = 467;
			break;

			// TODO
		case 467:
			sendOption2("Yes", "No");
			c.dialogueAction = 467;
			c.teleAction = 16;
			break;
		case 468:
			sendNpcChat2("Here is the castle, here you can teleport around "
					+ Config.SERVER_NAME,
					"There are also shops here for you to checkout!", 2244,
					"Lumbridge Guide");
			c.nextChat = 470;
			break;
		case 470:
			//c.fade(2516, 3855, 0);
			sendNpcChat(
					"Here is Dwarf, when you need mining supplies come to him.",
					2244, "Lumbridge Guide");
			c.nextChat = 599;
			break;
		case 599:
			//c.fadeStarterTele(2573, 3850, 0);
			c.nextChat = 597;
			break;
		case 700:
			//c.fadeStarterTele2(2535, 3868, 0);
			c.nextChat = 598;
			break;
		case 597:
			sendNpcChat2("Here you can travel to cities and monsters.",
					"Just talk to one of the NPCs which are standing here!",
					2244, "Lumbridge Guide");
			c.nextChat = 700;
			break;
		case 598:
			sendNpcChat2("Well there's a short tour of the island.",
					"I will let you be on your way now!", 2244,
					"Lumbridge Guide");
			c.nextChat = 701;
			break;
		case 701:
			sendNpcChat("I will change your appearence now!", 2244,
					"Lumbridge Guide");
			c.nextChat = 471;
			break;
		case 471:
		//	c.getPA().showInterface(3559);
		//	c.canChangeAppearance = true;
			c.nextChat = 472;
			break;
		case 472:
			sendNpcChat("Oh quick! I forgot to give you your reward!", 2244,
					"Lumbridge Guide");
			c.nextChat = 533;
			break;

		case 533:
			sendOption3("Adventurer", "Pker", "Skiller");
			c.dialogueAction = 515;
			break;
		case 473:
			sendPlayerChat1("Thank you sire!");
			c.nextChat = 516;
			c.completedTut = true;
			break;
		case 474:
			sendNpcChat1("I hope your journey is coming along well!", 2244,
					"Lumbridge Guide");
			c.nextChat = 0;
			break;
		case 516:
			sendNpcChat1("Let's change your appearance now.", 599,
					"Make-over mage");
			c.nextChat = 517;
			break;
		case 517:
		//	c.getPA().showInterface(3559);
		//	c.canChangeAppearance = true;
			c.nextChat = 0;
			break;
		case 518:
			sendNpcChat1("Goodluck, " + c.playerName + ".", 599,
					"Make-over mage");
			c.nextChat = 519;
			break;
		case 519:
			sendNpcChat1("Sorry I forgot something!", 2244, "Lumbridge Guide");
			c.nextChat = 520;
			break;
		case 520:
			sendNpcChat2("Since you're a special player of Biohazard, you may",
					"choose your Experience rate.", 2244, "Lumbridge Guide");
			c.nextChat = 521;
			break;
		case 521:
			sendStatement("The guide gives you 4 choices.");
			c.nextChat = 522;
			break;
		case 522:
			sendOption4("Easy", "Normal", "Hard", "Extreme (x4 Runescape's)");
			c.dialogueAction = 522;
			break;
		case 523:
			if (!c.canWalk)
				c.canWalk = true;
			c.getPA().setSidebarInterfaces(c, true);
			sendNpcChat1("Goodluck, " + c.playerName + ".", 2244,
					"Lumbridge Guide");
			c.sendMessage("Good luck, " + c.playerName + "!");
			c.nextChat = 0;
			break;

			// Slayer
		case 400:
			sendNpcChat("Hello, and what are you after then?", EVIL, "Vannaka");
			c.nextChat = 401;
			break;

		case 401:
			sendOption4("I want to see the stuff you have for sale.",
					"I need another assignment!",
					"Where is the location of my task?", "Err... Never mind.");
			c.dialogueAction = 401;
			break;

			// skills
		case 476:
			sendOption5("Firemaking", "Fishing", "Fletching", "Herblore",
					"Next ->");
			c.dialogueAction = 476;
			break;

		case 477:
			sendOption5("Mining", "Runecrafting", "Slayer", "Smithing",
					"Next ->");
			c.dialogueAction = 477;
			break;

		case 478:
			sendOption5("Thieving", "Woodcutting", "Construction", "Hunter",
					"Next ->");
			c.dialogueAction = 478;
			break;

		case 501: // minigames 2
			sendOption5("Tzhaar Cave", "Fishing Trawler", "Castle Wars",
					"Bounty Hunter", "- First Page -");
			c.dialogueAction = 501;
			c.teleAction = 14;
			break;

		case 504: // 2nd bosses
			sendOption4("Zulrah", " Kraken",
					"Venatis", "@red@- Coming Soon -");
			c.dialogueAction = 504;
			break;

		case 36899:
			sendOption4("What is this?", "Exchange Coins For Ticket",
					"Exchange Ticket For Coins", "Nevermind.");
			c.dialogueAction = 36899;
			break;

			/* Gambling */
		case 3001:
			sendOption5("Bet 100k?", "Bet 500k?", "Bet 1M?", "Bet 10mil?",
					"I don't want to bet");
			c.dialogueAction = 3001;
			//c.teleAction = 15;
			break;

		case 2244:
			sendNpcChat1("Do you want change your spellbooks?", c.talkingNpc,
					"High Priest");
			c.nextChat = 2245;
			break;
		case 2245:
			c.getDH().sendOption3(
					"Teleport me to Lunar Island, for lunars spellbook!",
					"Teleport me to Desert Pyramid, for ancients spellbook!",
					"No thanks, i will stay here.");
			c.dialogueAction = 2245;
			c.nextChat = 0;
			break;
		case 69:
			c.getDH().sendNpcChat1(
					"Hello! Do you want to choose your clothes?", c.talkingNpc,
					"Thessalia");
			c.sendMessage("@red@You must right-click Thessalia to change your clothes.");
			c.nextChat = 0;
			break;
		case 6500:
			c.getDH().sendNpcChat1("It's a damn good job I don't have arachnophobia.", 319, ""+ c.playerName +"");
			c.nextChat = 6501;
			break;
		case 6501:
			c.getDH().sendNpcChat2("We're misunderstood. Without us in your house,", "you'd be infested with flies and other REAL nasties.", c.talkingNpc, "Venenatis spiderling");
			c.nextChat = 6502;
			break;
		case 6502:
			c.getDH().sendNpcChat1("Thanks for that enlightening fact.", 319, ""+ c.playerName +"");
				c.nextChat = 6503;
				break;
		case 6503:
			c.getDH().sendNpcChat1("Everybody gets one.", c.talkingNpc, "Venenatis spiderling");
			c.nextChat = 0;
			break;
		case 6969:
			c.getDH().sendNpcChat2("I'm not working right now sir.",
					"If you wan't me to work, talk to Ardi give me a job.",
					c.talkingNpc, "Unemployed");
			c.sendMessage("This NPC do not have an action, if you have any suggestion for this NPC, post on forums.");
			c.nextChat = 0;
			break;
			/* LOGIN 1st TIME */
		case 769:
			c.getDH().sendNpcChat1(Config.WELCOME_MESSAGE + ", " + " + c.playerName + ",
					c.talkingNpc, Config.SERVER_NAME + " Guide");
			c.nextChat = 770;
			break;
		case 770:
			c.getDH()
			.sendNpcChat1(
					"Since we are in Beta, all your items & Stats will be reset before the official release.",
					c.talkingNpc, Config.SERVER_NAME + " Guide");
			c.nextChat = 771;
			break;
		case 771:
			c.getDH().sendNpcChat2(
					"If you see any bugs, please report them to Mod Sunny.",
					"Have fun!", c.talkingNpc, Config.SERVER_NAME + " Guide");
			c.getPA().addStarter();
			c.canWalk = true;
			break;
			/* END LOGIN */
		case 691:
			c.getDH().sendNpcChat2("Welcome to 2007remake.",
					"Please, read what i've to tell you...", c.talkingNpc,
					"Mod Ardi");
			c.nextChat = 692;
			// c.loggedIn = 1;
			break;
		case 692:
			sendNpcChat4("2007remake's on pre-alpha state.",
					"Then you can spawn items, and set your levels.",
					"But remember, it's just for pre-alpha sir...",
					"When we do the official release...", c.talkingNpc,
					"Mod Ardi");
			c.nextChat = 693;
			break;
		case 693:
			sendNpcChat4("We will have economy reset and,",
					"this commands will be removed too...",
					"Please, report glitches, and post suggestions",
					"on forums, for i can code, and we get 100% ready!",
					c.talkingNpc, "Mod Wind");
			c.sendMessage("@red@You're online in 2007remake pre-alpha.");
			c.sendMessage("@red@Pre-alpha's to find glitches, and post suggestions in forums...");
			c.sendMessage("@red@Then our developer 'Mod Ardi' can code it, and we get official release in less time.");
			c.sendMessage("@red@Thanks for your attention sir.");
			c.nextChat = 0;
			break;
			
			//D Scim Story
					case 1459:
						c.getDH().sendNpcChat1(
								"Thank goodness! I've been out here so long!",
								c.talkingNpc, "ZooKnock");
						c.nextChat = 1460;
						break;
					case 1460:
						c.getDH().sendPlayerChat1("What are you doing out here?");
						c.nextChat = 1461;
						break;
					case 1461:
						c.getDH().sendNpcChat1(
								"Caves collapsed all around me, I ran and grabbed what I could.",
								c.talkingNpc, "ZooKnock");
						c.nextChat = 1462;
						break;
					case 1462:
						c.getDH().sendPlayerChat1("What did you grab?");
						c.nextChat = 1463;
						break;
					case 1463:
						c.getDH().sendPlayerChat1("Here ill sell it to you for 250k gold coins.");
						c.nextChat = 1464;
						
					case 1464:
						c.getDH().sendOption2("Okay, I'll pay.",
								"No thanks, I can't afford that.");
						c.dialogueAction = 508;
						
					case 1465:
						c.getDH().sendPlayerChat1("No thanks, I can't afford that.");
						c.nextChat = 0;
						break;
						
					case 1466:
						if (!c.getItems().playerHasItem(995, 250000)) {
							c.getDH().sendPlayerChat1("I haven't got that much.");
							c.nextChat = 0;
						} else {
							c.getDH().sendNpcChat1(
									"Here is your Dragon Scimitar",
									c.talkingNpc, "ZooKnock");
							c.getItems().deleteItem2(995, 250000);
							c.getItems().addItem(4587, 1);
							c.sendMessage("You recieve the weapon");
							c.nextChat = 0;
						}
						break;
			/* AL KHARID */
		case 1022:
			c.getDH().sendPlayerChat1("Can I come through this gate?");
			c.nextChat = 1023;
			break;

		case 1023:
			c.getDH().sendNpcChat1(
					"You must pay a toll of 10 gold coins to pass.",
					c.talkingNpc, "Border Guard");
			c.nextChat = 1024;
			break;
		case 1024:
			c.getDH().sendOption3("Okay, I'll pay.",
					"Who does my money go to?", "No thanks, I'll walk around.");
			c.dialogueAction = 502;
			break;
		case 1025:
			c.getDH().sendPlayerChat1("Okay, I'll pay.");
			c.nextChat = 1026;
			break;
		case 1026:
			c.getDH().sendPlayerChat1("Who does my money go to?");
			c.nextChat = 1027;
			break;
		case 1027:
			c.getDH().sendNpcChat2("The money goes to the city of Al-Kharid.",
					"Will you pay the toll?", c.talkingNpc, "Border Guard");
			c.nextChat = 1028;
			break;
		case 1028:
			c.getDH().sendOption2("Okay, I'll pay.",
					"No thanks, I'll walk around.");
			c.dialogueAction = 508;
			break;
		case 1029:
			c.getDH().sendPlayerChat1("No thanks, I'll walk around.");
			c.nextChat = 0;
			break;

		case 1030:
			if (!c.getItems().playerHasItem(995, 10)) {
				c.getDH().sendPlayerChat1("I haven't got that much.");
				c.nextChat = 0;
			} else {
				c.getDH().sendNpcChat1(
						"As you wish. Don't get too close to the scorpions.",
						c.talkingNpc, "Border Guard");
				c.getItems().deleteItem2(995, 10);
				c.sendMessage("You pass the gate.");
				Special.movePlayer(c);
				Special.openKharid(c, c.objectId);
				c.turnPlayerTo(c.objectX, c.objectY);
				c.nextChat = 0;
			}
			break;

		case 1031:
			c.getDH().sendNpcChat1(
					"As you wish. Don't get too close to the scopions.",
					c.talkingNpc, "Border Guard");
			c.getItems().deleteItem2(995, 10);
			c.sendMessage("You pass the gate.");
			Special.movePlayer(c);
			Special.openKharid(c, c.objectId);
			c.turnPlayerTo(c.objectX, c.objectY);
			c.nextChat = 0;
			break;

		case 22:
			sendOption2("Pick the flowers", "Leave the flowers");
			c.nextChat = 0;
			c.dialogueAction = 22;
			break;
			/** Bank Settings **/
		case 1013:
			c.getDH().sendNpcChat1("Good day. How may I help you?",
					c.talkingNpc, "Banker");
			c.nextChat = 1014;
			break;
		case 1014:// bank open done, this place done, settings done, to do
			// delete pin
			c.getDH().sendOption3(
					"I'd like to access my bank account, please.",
					"I'd like to check my my P I N settings.",
					"What is this place?");
			c.dialogueAction = 251;
			break;
			/** What is this place? **/
		case 1015:
			c.getDH().sendPlayerChat1("What is this place?");
			c.nextChat = 1016;
			break;
		case 1016:
			c.getDH().sendNpcChat2("This is the bank of 2007remake.",
					"We have many branches in many towns.", c.talkingNpc,
					"Banker");
			c.nextChat = 0;
			break;
			/**
			 * Note on P I N. In order to check your "Pin Settings. You must have
			 * enter your Bank Pin first
			 **/
			/** I don't know option for Bank Pin **/
		case 1017:
			c.getDH()
			.sendStartInfo(
					"Since you don't know your P I N, it will be deleted in @red@3 days@bla@. If you",
					"wish to cancel this change, you may do so by entering your P I N",
					"correctly next time you attempt to use your bank.",
					"", "", false);
			c.nextChat = 0;
			break;
		case 1018:
			sendNpcChat1("Sss...yessssSSS?", 2130, "Snakeling");
			c.nextChat = 1019;
			break;
		case 1019:
			sendOption2("Metamorphosis", "Nothing, sorry.");
			c.dialogueAction = 1019;
			break;
		case 1020:
			sendOption2("How do I unlock your @blu@blue @bla@phase?", "How do I unlock your @red@red @bla@phase?");
			c.dialogueAction = 1020;
			break;
		case 1032:
			sendNpcChat1("Ssspeak to me when you have sssscalesss...", c.petID, "Snakeling");
			c.nextChat = 0;
			break;
		case 1033:
			sendPlayerChat("I have 100 scales with me. Can I transform you now?", HAPPY);
			c.nextChat = 1034;
			break;
		case 1034:
			sendNpcChat1("Sssssure...", c.petID, "Snakeling");
			c.nextChat = 1035;
			break;
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			c.dialogueAction = 1;
			c.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
			c.dialogueAction = 1;
			c.nextChat = 0;
			break;
		case 3:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.",
					"Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 4;
			break;
		case 5:
			sendNpcChat4("Hello adventurer...",
					"My name is Kolodion, the master of this mage bank.",
					"Would you like to play a minigame in order ",
					"to earn points towards recieving magic related prizes?",
					c.talkingNpc, "Kolodion");
			c.nextChat = 6;
			break;
		case 6:
			sendNpcChat4("The way the game works is as follows...",
					"You will be teleported to the wilderness,",
					"You must kill mages to recieve points,",
					"redeem points with the chamber guardian.", c.talkingNpc,
					"Kolodion");
			c.nextChat = 15;
			break;
		case 11:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.",
					"Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 12;
			break;
		case 12:
			sendOption2("Yes I would like a slayer task.",
					"No I would not like a slayer task.");
			c.dialogueAction = 5;
			c.nextChat = 0;
			break;
		case 13:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I see I have already assigned you a task to complete.",
					"Would you like me to give you an easier task?",
					c.talkingNpc, "Duradel");
			c.nextChat = 14;
			break;
		case 14:
			sendOption2("Yes I would like an easier task.",
					"No I would like to keep my task.");
			c.dialogueAction = 6;
			c.nextChat = 0;
			break;
		case 15:
			sendOption2("Yes I would like to play",
					"No, sounds too dangerous for me.");
			c.dialogueAction = 7;
			break;
		case 16:
			sendOption2("I would like to reset my barrows brothers.",
					"I would like to fix all my barrows");
			c.dialogueAction = 8;
			break;
		case 17:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			c.dialogueAction = 10;
			c.dialogueId = 17;
			c.teleAction = -1;
			break;
		case 18:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			c.dialogueAction = 11;
			c.dialogueId = 18;
			c.teleAction = -1;
			break;
		case 19:
			sendOption5("Nature", "Law", "Death", "Blood", "More");
			c.dialogueAction = 12;
			c.dialogueId = 19;
			c.teleAction = -1;
			break;
		case 20:
			sendNpcChat4(
					"Haha, hello",
					"My name is Wizard Distentor! I am the master of clue scroll reading.",
					"I can read the magic signs of a clue scroll",
					"You got to pay me 100K for reading the clue though!",
					c.talkingNpc, "Wizard Distentor");
			c.nextChat = 21;
			break;
		case 21:
			sendOption2("Yes I would like to pay 100K", "I don't think so sir");
			c.dialogueAction = 50;
			break;
		case 23:
			sendNpcChat4("Greetings, Adventure",
					"I'm the legendary Vesta seller",
					"With 120 noted Lime Stones, and 20 Million GP",
					"I'll be selling you the Vesta's Spear", c.talkingNpc,
					"Legends Guard");
			c.nextChat = 24;
			break;
		case 54:
			sendOption2("Buy Vesta's Spear", "I can't afford that");
			c.dialogueAction = 51;
			break;
		case 56:
			sendStatement("Hello " + c.playerName + ", you currently have "
					+ c.pkPoints + " PK points.");
			break;

		case 57:
			c.getPA().sendFrame126("Teleport to shops?", 2460);
			c.getPA().sendFrame126("Yes.", 2461);
			c.getPA().sendFrame126("No.", 2462);
			c.getPA().sendFrame164(2459);
			c.dialogueAction = 27;
			break;

			/**
			 * Recipe for disaster - Sir Amik Varze
			 **/

		case 25:
			sendOption2("Yes", "No");
			c.rfdOption = true;
			c.nextChat = 0;
			break;
		case 26:
			sendPlayerChat1("Yes");
			c.nextChat = 28;
			break;
		case 27:
			sendPlayerChat1("No");
			c.nextChat = 29;
			break;

		case 29:
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 30:
			sendNpcChat4("Congratulations!",
					"You have defeated all Recipe for Disaster bosses",
					"and have now gained access to the Culinaromancer's chest",
					"and the Culinaromancer's item store.", c.talkingNpc,
					"Sir Amik Varze");
			c.nextChat = 0;
			PlayerSave.saveGame(c);
			break;
		case 31:
			sendNpcChat4("", "You have been defeated!", "You made it to round "
					+ c.roundNpc, "", c.talkingNpc, "Sir Amik Varze");
			c.roundNpc = 0;
			c.nextChat = 0;
			break;

			/**
			 * Horror from the deep
			 **/
		case 32:
			sendNpcChat4("", "Would you like to start the quest",
					"Horror from the Deep?", "", c.talkingNpc, "Jossik");
			c.nextChat = 33;
			break;
		case 33:
			sendNpcChat4("", "You will have to be able to defeat a level-100 ",
					"Dagannoth mother with different styles of attacks.", "",
					c.talkingNpc, "Jossik");
			c.nextChat = 34;
			break;
		case 34:
			sendOption2("Yes I am willing to fight!",
					"No thanks, I am not strong enough.");
			c.horrorOption = true;
			break;
		case 35:
			sendPlayerChat1("Yes I am willing to fight!");
			c.nextChat = 37;
			break;
		case 36:
			sendPlayerChat1("No thanks, I am not strong enough.");
			c.nextChat = 0;
			break;
		case 37:
			c.horrorFromDeep = 1;
			c.height = (c.playerId * 4);
			c.getPA().movePlayer(2515, 10008, c.height);
			NPCHandler.spawnNpc(c, 1351, 2521, 10024, c.height, 0, 100,
					16, 75, 75, true, true);
			c.getPA().removeAllWindows();
			c.getPA().loadQuests();
			c.inHfd = true;
			break;

			/**
			 * Desert Treasure dialogue
			 */
		case 41:
			sendNpcChat4("", "Do you want to start the quest",
					"Desert treasure?", "", c.talkingNpc, "Archaeologist");
			c.nextChat = 42;
			break;
		case 42:
			sendNpcChat4("", "You will have to fight four high level bosses,",
					"after each boss you will be brought back",
					"here to refill your supplies if it is needed.",
					c.talkingNpc, "Archaeologist");
			c.nextChat = 43;
			break;
		case 43:
			sendOption2("Yes I want to fight!", "No thanks, I am not ready.");
			c.dtOption = true;
			break;
		case 44:
			sendPlayerChat1("Yes I want to fight!");
			c.nextChat = 51;
			break;
		case 45:
			sendPlayerChat1("No thanks, I am not ready.");
			c.nextChat = 0;
			break;

		case 48:
			sendOption2("Yes, I am ready!", "No, I am not ready.");
			c.dtOption2 = true;
			break;
		case 49:
			sendPlayerChat1("Yes, I am ready!");
			c.nextChat = 52;
			break;
		case 50:
			sendPlayerChat1("No, I am not ready.");
			c.nextChat = 0;
			break;
		case 51:
			c.desertT++;
			c.height = (c.playerId * 4);
			c.getPA().movePlayer(3310, 9376, c.height);
			NPCHandler.spawnNpc(c, 1977, 3318, 9376, c.height, 0, 130,
					40, 70, 90, true, true);
			c.getPA().removeAllWindows();
			c.getPA().loadQuests();
			c.inDt = true;
			break;
		case 9990:
			sendNpcChat4("Hi! Welcome to SamManMode! This is a dangerous, glitchy place.", "Most of the world is PvP, and you can fight anyone, no matter their level.", 
					"Click a chest to open your bank, and use a bank key on a chest", "to open your victim's bank. Good luck.", 119, "Doomsayer");
			c.nextChat = 0;
			break;
		case 3893:
			sendNpcChat4("Bernie Sanders is not a socialist.", "Contrary to popular belief, he doesn't support", "seizing the means of production", 
					"which is the definition of socialism.", c.talkingNpc, "Doric");
			c.nextChat = 0;
			break;
		case 588:
			sendNpcChat1("Nick's a fag.", c.talkingNpc, "Man");
			c.nextChat = 0;
			break;
		case 5050:
			sendNpcChat1("Hello there "+ c.playerName +"!", c.talkingNpc, "Paul");
			c.nextChat = 5051;
			break;
		case 5118:
			sendNpcChat1("Why are you talking to me, scrub?", c.talkingNpc, "Sylas");
			c.nextChat = 0;
			break;
		case 5932:
			sendNpcChat1("Pvvm is a fag", c.talkingNpc, "Sam");
			c.nextChat = 0;
			break;
		case 10013:
			sendNpcChat2("Hi " + c.playerName + "! You should subscribe", "to Sam on YouTube. He's better than Artz.", c.talkingNpc, "Civilian");
			c.nextChat = 0;
			break;
		case 3994:
			sendNpcChat1("Artz is such a scrub, amirite?", c.talkingNpc, "Boy");
			c.nextChat = 3995;
			break;
		case 3995:
			sendNpcChat1("Make sure to dislike his videos, and unsub!", c.talkingNpc, "Boy");
			c.nextChat = 0;
			break;
		case 5051:
			sendNpcChat2("For this week only I am giving out some free armour!",
					"Would you like to collect your Ironman Armour?",
					c.talkingNpc, "Paul");
			c.nextChat = 5052;
			break;
		case 5052:
			sendNpcChat2("If you would like some, feel free to right click me",
					"And click armour to receive it!",
					c.talkingNpc, "Paul");
			c.nextChat = -1;
		//	c.caIronOptiona = true;
		//	c.caIrontalk1 = true;
			break;
	/*	case 5053:
			sendPlayerChat1("Yes please Adam!");
			break;
		case 5054:
			sendPlayerChat1("No thanks, maybe later Adam!");
			break;*/



			/**
			 * Cook's Assistant
			 */
		case 100:
			sendNpcChat1("What am I to do?", c.talkingNpc, "Cook");
			c.nextChat = 101;
			break;
		case 101:
			sendOption4("What`s wrong?", "Can you make me a cake?",
					"You don`t look very happy.", "Nice hat!");
			c.caOption4a = true;
			c.caPlayerTalk1 = true;
			break;
		case 102:
			sendPlayerChat1("What`s wrong?");
			c.nextChat = 103;
			break;
		case 103:
			sendNpcChat3(
					"Oh dear, oh dear, oh dear, Im in a terrible terrible",
					"mess! It`s the Duke`s birthday today, and I should be",
					"making him a lovely big birthday cake.", c.talkingNpc,
					"Cook");
			c.nextChat = 104;
			break;
		case 104:
			sendNpcChat4(
					"I`ve forgotten to buy the ingredients. I`ll never get",
					"them in time now. He`ll sack me! What will I do? I have",
					"four children and a goat to look after. Would you help",
					"me? Please?", c.talkingNpc, "Cook");
			c.nextChat = 105;
			break;
		case 105:
			sendOption2("Im always happy to help an cook in distress.",
					"I can`t right now, Maybe later.");
			c.caOption2 = true;
			break;
		case 106:
			c.cooksA++;
			c.getPA().loadQuests();
			sendPlayerChat1("Yes, I`ll help you.");
			c.nextChat = 107;
			break;
		case 107:
			sendNpcChat2("Oh thank you, thank you. I need milk, an egg and",
					"flour. I`d be very grateful if you can get them for me.",
					c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 108:
			sendOption4("Where do I find some flour.", "How about some milk?",
					"And eggs? where are they found?",
					"Actually, I know where to find these stuff.");
			c.caOption4c = true;
			c.caOption4b = true;
			break;
		case 109:
			sendNpcChat1(
					"How are you getting on with finding the ingredients?",
					c.talkingNpc, "Cook");
			c.nextChat = 110;
			break;
		case 110:
			sendPlayerChat1("Here's a bucket of milk.");
			c.getItems().deleteItem(1927, 1);
			c.nextChat = 111;
			break;
		case 111:
			sendPlayerChat1("Here's a pot of flour.");
			c.getItems().deleteItem(1933, 1);
			c.nextChat = 112;
			break;
		case 112:
			c.cooksA++;
			c.getPA().loadQuests();
			sendPlayerChat1("Here's a fresh egg.");
			c.getItems().deleteItem(1944, 1);
			c.nextChat = 113;
			break;
		case 113:
			sendNpcChat2("You've brough me everything I need! I am saved!",
					"Thank you!", c.talkingNpc, "Cook");
			c.nextChat = 0;
			break;
			/*
			 * case 114: sendPlayerChat1("So do I get to go the Duke's Party?");
			 * c.nextChat = 115; break; case 115:
			 * sendNpcChat2("I'm afraid not, only the big cheeses get to dine with the"
			 * , "Duke.", c.talkingNpc, "Cook"); c.nextChat = 116; break; case 116:
			 * sendPlayerChat2
			 * ("Well, maybe one day I'll be important enough to sit on",
			 * "the Duke's table."); c.nextChat = 117; break; case 117:
			 * sendNpcChat1("Maybe, but I won't be holding my breath.",
			 * c.talkingNpc, "Cook"); c.cooksA++; c.cooksA++;
			 * c.getPA().loadQuests(); c.getAA2().COOK2(); c.nextChat = 0; break;
			 */

			// ** Getting Items - Cook's Assistant **//
		case 118:
			sendNpcChat3("There`s a mill fairly close, Go North then West.",
					"Mill Lane Mill is just off the road to Draynor. I",
					"usually get my flour from there.", c.talkingNpc, "Cook");
			c.nextChat = 119;
			break;
		case 119:
			sendNpcChat2(
					"Talk to Millie, she`ll help, she`s a lovely girl and a fine",
					"Miller.", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 120:
			sendNpcChat2(
					"There is a cattle field on the other side of the river,",
					"just across the road from the Groats` Farm.",
					c.talkingNpc, "Cook");
			c.nextChat = 121;
			break;
		case 121:
			sendNpcChat3(
					"Talk to Gillie Groats, she looks after the Dairy Cows -",
					"She`ll tell you everything you need to know about",
					"milking cows!", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 122:
			sendNpcChat2("I normally get my eggs from the Groats` farm on the",
					"other side of the river.", c.talkingNpc, "Cook");
			c.nextChat = 123;
			break;
		case 123:
			sendNpcChat1("But any chicken should lay eggs.", c.talkingNpc,
					"Cook");
			c.nextChat = 108;
			break;
		case 124:
			sendPlayerChat1("Actually, I know where to find these stuff");
			c.nextChat = 0;
			break;
		case 125:
			sendPlayerChat1("You're a cook why, don't you bake me a cake?");
			c.nextChat = 126;
			break;
		case 126:
			sendNpcChat1("*sniff* Dont talk to me about cakes...",
					c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
		case 127:
			sendPlayerChat1("You don't look very happy.");
			c.nextChat = 128;
			break;
		case 128:
			sendNpcChat2(
					"No, I`m not. The world is caving in around me - I am",
					"overcome by dark feelings of impending doom.",
					c.talkingNpc, "Cook");
			c.nextChat = 129;
			break;
		case 129:
			sendOption2("What's wrong?",
					"I'd take off the rest of the day if I were you.");
			c.caOption2a = true;
			break;
		case 130:
			sendPlayerChat1("Nice hat!");
			c.nextChat = 131;
			break;
		case 131:
			sendNpcChat1(
					"Err thank you. It`s a pretty ordinary cook`s hat really.",
					c.talkingNpc, "Cook");
			c.nextChat = 132;
			break;
		case 132:
			sendPlayerChat1("Still, suits you. The trousers are pretty special too.");
			c.nextChat = 133;
			break;
		case 133:
			sendNpcChat1("It`s all standard cook`s issue uniform...",
					c.talkingNpc, "Cook");
			c.nextChat = 134;
			break;
		case 134:
			sendPlayerChat2(
					"The whole hat, apron, stripey trousers ensemble -",
					"it works. It makes you look like a real cook.");
			c.nextChat = 135;
			break;
		case 135:
			sendNpcChat2(
					"I am a real cook!, I haven`t got time to be chatting",
					"about Culinary Fashion. I`m in desperate need of help.",
					c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
		case 136:
			sendPlayerChat1("I'd take off the rest of the day if I were you.");
			c.nextChat = 137;
			break;
		case 137:
			sendNpcChat2(
					"No, that`s the worst thing I could do. I`d get in terrible",
					"trouble.", c.talkingNpc, "Cook");
			c.nextChat = 138;
			break;
		case 138:
			sendPlayerChat1("Well maybe you need to take a holiday...");
			c.nextChat = 139;
			break;
		case 139:
			sendNpcChat2(
					"That would be nice but the duke doesn`t allow holidays",
					"for core staff.", c.talkingNpc, "Cook");
			c.nextChat = 140;
			break;
		case 140:
			sendPlayerChat2("Hmm, why not run away to the sea and start a new",
					"life as a Pirate.");
			c.nextChat = 141;
			break;
		case 141:
			sendNpcChat2(
					"My wife gets sea sick, and i have an irrational fear of",
					"eyepatches. I don`t see it working myself.", c.talkingNpc,
					"Cook");
			c.nextChat = 142;
			break;
		case 142:
			sendPlayerChat1("I`m afraid I've run out of ideas.");
			c.nextChat = 143;
			break;
		case 143:
			sendNpcChat1("I know I`m doomed.", c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;

			//

		case 144:
			sendNpcChat1("Nice day, isn't it?", c.talkingNpc, "");
			c.nextChat = 0;
			break;

			/*
			 * Doric's Quest
			 */

		case 300:
			sendNpcChat1("Why hello there adventurer, how can I help you?",
					c.talkingNpc, "Doric");
			c.nextChat = 301;
			break;

		case 301:
			sendOption3("I'm looking for a quest.", "Nice place you got here.",
					"Just passing by.");
			c.doricOption = true;
			break;

		case 299:
			sendPlayerChat1("I'm just passing by.");
			c.nextChat = 302;
			break;

		case 302:
			sendNpcChat1("Very well, so long.", c.talkingNpc, "Doric");
			c.nextChat = 0;
			break;

		case 303:
			sendPlayerChat1("Nice place you got here.");
			c.nextChat = 304;
			break;

		case 304:
			sendNpcChat1("Why thank you kind sir.", c.talkingNpc, "Doric");
			c.nextChat = 305;
			break;

		case 305:
			sendPlayerChat1("My pleasure.");
			c.nextChat = 0;
			break;

		case 306:
			sendPlayerChat1("I'm looking for a quest.");
			c.nextChat = 307;
			break;

		case 307:
			sendNpcChat2("A quest you say? Hmm...",
					"Can you run me a quick errand?", c.talkingNpc, "Doric");
			c.nextChat = 308;
			break;

		case 308:
			sendOption2("Of course.", "I need to go.");
			c.doricOption2 = true;
			break;

		case 309:
			sendPlayerChat1("I need to go.");
			c.nextChat = 0;
			break;

		case 310:
			sendPlayerChat1("Of course!");
			c.nextChat = 311;
			break;

		case 311:
			sendNpcChat3("Very good! I need some materials for a new ",
					"pickaxe I'm working on, is there any way you ",
					"could go get these?", c.talkingNpc, "Doric");
			c.nextChat = 312;
			break;

		case 312:
			sendPlayerChat1("Sure, what materials?");
			c.nextChat = 313;
			break;

		case 313:
			sendNpcChat3("6 lumps of clay,", "4 copper ores,",
					"and 2 iron ores.", c.talkingNpc, "Doric");
			c.nextChat = 314;
			break;

		case 314:
			sendPlayerChat1("Sounds good, I will be back soon!");
			c.nextChat = 315;
			c.doricQuest = 5;
			break;

		case 315:
			sendNpcChat1("Thank you adventurer, hurry back!", c.talkingNpc,
					"Doric");
			c.nextChat = 0;
			break;

		case 316:
			sendNpcChat1("Have you got all the materials yet?", c.talkingNpc,
					"Doric");
			c.nextChat = 317;
			break;

		case 317:
			sendPlayerChat1("Not all of them.");
			c.nextChat = 0;
			break;

		case 318:
			sendNpcChat1("Have you got all the materials yet?", c.talkingNpc,
					"Doric");
			c.nextChat = 319;
			break;

		case 319:
			sendPlayerChat1("Yep! Right here.");
			c.nextChat = 320;
			c.getItems().deleteItem(434, 6);
			c.getItems().deleteItem(436, 4);
			c.getItems().deleteItem(440, 2);
			break;

		case 320:
			sendNpcChat2("Thank you so much adventurer, heres a reward",
					"for any hardships you may have encountered.",
					c.talkingNpc, "Doric");
			c.nextChat = 0;
			c.sendMessage("Congradulations, you have completed Doric's Quest!");
			break;

		case 321:
			sendNpcChat1("Welcome to my home, feel free to use my anvils!",
					c.talkingNpc, "Doric");
			c.nextChat = 0;
			break;
			
		case 4000:
			sendNpcChat1("Sss..Yesss?", c.petID, "Snakeling");
			c.nextChat = 4001;
			break;
		case 4001:
			sendPlayerChat1("Uhh.. sorry about Zulrah.");
			c.nextChat = 4002;
			break;
		case 4002:
			sendNpcChat1("Who'sssss Zulrah? Sss...", c.petID, "Snakeling");
			c.nextChat = 4003;
			break;
		case 4003:
			sendPlayerChat1("..Yeah.. Nobody. Nevermind.");
			c.nextChat = 0;
			break;
		}
	}
	/*
	 * Information Box
	 */

	public void sendStartInfo(String text, String text1, String text2,
			String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}

	/*
	 * Options
	 */
	protected wind.model.players.Player player;
	public wind.model.players.Player getPlayer() {
		return player;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public void sendOption(String s) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126("Click here to continue", 2473);
		c.getPA().sendFrame164(13758);
	}

	public void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}

	public void sendOption3(String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendFrame164(2469);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}

	public void sendOption4CustomTitle(String title, String s, String s1,
			String s2, String s3) {
		c.getPA().sendFrame126(title, 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	public void sendStartInfo(String text, String text1, String text2,
			String text3, String title, boolean send) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}

	/*
	 * Statements
	 */

	public void sendStatement(String s) { // 1 line click here to continue chat
		// box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	/*
	 * Npc Chatting
	 */

	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}

	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc,
			String name) {
		c.getPA().sendFrame200(4894, 591);
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendFrame164(4893);
	}

	public void sendNpcChat4(String s, String s1, String s2, String s3,
			int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}

	/*
	 * Player Chating Back
	 */

	private void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}

	private void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}

	@SuppressWarnings("unused")
	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 591);
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}

	@SuppressWarnings("unused")
	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 591);
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}

	public void sendNpcChat2(String s, String s1, int emote) {
		c.getPA().sendFrame200(4888, emote);
		c.getPA().sendFrame126("Skill Master", 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(c.npcType, 4888);
		c.getPA().sendFrame164(4887);
	}

	@SuppressWarnings("unused")
	private void sendNpcChat(String s, String s1, String s2, int emote,
			String name) {
		c.getPA().sendFrame200(4894, emote);
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(c.npcType, 4894);
		c.getPA().sendFrame164(4893);
	}

	public void sendNpcChat(String s, int emote, String name) {
		c.getPA().sendFrame200(4883, emote);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(c.npcType, 4883);
		c.getPA().sendFrame164(4882);
	}

	public void sendNpcChat(String s, String s1, int emote, String name) {
		c.getPA().sendFrame200(4888, emote);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(c.npcType, 4888);
		c.getPA().sendFrame164(4887);
	}

	@SuppressWarnings("unused")
	private void sendNpcChat(String s, String s1, String s2, String s3,
			int emote, String name) {
		c.getPA().sendFrame200(4901, emote);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(c.npcType, 4901);
		c.getPA().sendFrame164(4900);
	}

	public void sendPlayerChat(String s, int emote) {
		c.getPA().sendFrame200(969, emote);
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}

	@SuppressWarnings("unused")
	private void sendPlayerChat(String s, String s1, String s2, int emote) {
		c.getPA().sendFrame200(980, emote);
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}

	@SuppressWarnings("unused")
	private void sendPlayerChat(String s, String s1, String s2, String s3,
			int emote) {
		c.getPA().sendFrame200(987, emote);
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}

	public final int HAPPY = 588, CALM = 589, CALM_CONTINUED = 590,
			CONTENT = 591, EVIL = 592, EVIL_CONTINUED = 593,
			DELIGHTED_EVIL = 594, ANNOYED = 595, DISTRESSED = 596,
			DISTRESSED_CONTINUED = 597, NEAR_TEARS = 598, SAD = 599,
			DISORIENTED_LEFT = 600, DISORIENTED_RIGHT = 601,
			UNINTERESTED = 602, SLEEPY = 603, PLAIN_EVIL = 604, LAUGHING = 605,
			LONGER_LAUGHING = 606, LONGER_LAUGHING_2 = 607, LAUGHING_2 = 608,
			EVIL_LAUGH_SHORT = 609, SLIGHTLY_SAD = 610, VERY_SAD = 611,
			OTHER = 612, NEAR_TEARS_2 = 613, ANGRY_1 = 614, ANGRY_2 = 615,
			ANGRY_3 = 616, ANGRY_4 = 617;
}
