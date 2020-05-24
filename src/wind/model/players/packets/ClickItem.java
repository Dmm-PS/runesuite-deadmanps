package wind.model.players.packets;

import shamon.barrows.Barrows;
import wind.Server;
import wind.model.items.impl.MembershipBond;
import wind.model.items.impl.TeleTab;
import wind.model.items.impl.ToyHorses;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.PlayerAssistant;
import wind.model.players.Rights;
import wind.model.players.content.DiceHandler;
import wind.model.players.content.ExperienceLamp;
import wind.model.players.content.MithrilSeeds;
import wind.model.players.content.skills.impl.Prayer;
import wind.model.players.content.skills.impl.herblore.Herblore;
import wind.model.players.content.skills.impl.woodcutting.BirdsNests;
import wind.util.Misc;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@SuppressWarnings("static-access") @Override public void processPacket(Client c, int packetType, int packetSize) {

		@SuppressWarnings("unused")
		int junk = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();

		Barrows barrows = c.getBarrows();
		if (barrows.getMoundHandler().dig(itemId)) {
			return;
		}

		@SuppressWarnings("unused")
		int ROLL_TIMER = 3000;
		if (itemId == 13190) {
			MembershipBond ms = new MembershipBond(c);
			ms.clickedItem = 13190;
			ms.handle(c);
		}
		if (itemId == 5733) {
			if (c.getRights().equal(Rights.DEVELOPER)) {
				c.getItems().addItem(995, 150000);
				c.getItems().addItemToBank(13190, 5000);
				c.getItems().addItemToBank(13192, 5000);
			}

			else {
				c.sendMessage("What are you doing with this?!");
				c.getItems().deleteItem(itemId, 28);
			}
		}
		if (c.getItems().playerHasItem(12728)) {
			c.getItems().deleteItem(12728, 1);
			c.getItems().addItem(556, 250);
			c.sendMessage("You've exchanged your rune pack for runes!");
		}

		if (c.getItems().playerHasItem(12730)) {
			c.getItems().deleteItem(12730, 1);
			c.getItems().addItem(555, 250);
			c.sendMessage("You've exchanged your rune pack for runes!");
		}

		if (c.getItems().playerHasItem(12732)) {
			c.getItems().deleteItem(12732, 1);
			c.getItems().addItem(557, 250);
			c.sendMessage("You've exchanged your rune pack for runes!");
		}

		if (c.getItems().playerHasItem(12734)) {
			c.getItems().deleteItem(12734, 1);
			c.getItems().addItem(554, 250);
			c.sendMessage("You've exchanged your rune pack for runes!");
		}
		if (c.getItems().playerHasItem(12736)) {
			c.getItems().deleteItem(12736, 1);
			c.getItems().addItem(558, 500);
			c.sendMessage("You've exchanged your rune pack for runes!");
		}
		if (c.getItems().playerHasItem(13193)) {
			c.getItems().deleteItem(13193, 1);
			c.getItems().addItem(8882, 100);
			c.sendMessage("You've exchanged your bolt pack for bolts!");
		}
		if (c.getItems().playerHasItem(13193)) {
			c.getItems().deleteItem(13193, 1);
			c.getItems().addItem(8882, 100);
			c.sendMessage("You've exchanged your bolt pack for bolts!");
		}
		if (c.getItems().playerHasItem(11879)) {
			c.getItems().deleteItem(11879, 1);
			c.getItems().addItem(228, 100);
			c.sendMessage("You've exchanged your vial pack for vials of water!");
		}
		if (c.getItems().playerHasItem(12859)) {
			c.getItems().deleteItem(12859, 1);
			c.getItems().addItem(222, 100);
			c.sendMessage("You've exchanged your newt pack for eyes of newt!");
		}
		if (itemId == 13192) {
			MembershipBond ms = new MembershipBond(c);
			ms.clickedItem = 13192;
			ms.handle(c);
		}
		if (!c.getItems().playerHasItem(itemId, 1)) { //, itemSlot
			return;
		}

		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (c.hasPin) {
			return;
		}

		ToyHorses.handleToyHorsesActions(c, itemId);
		BirdsNests nest = new BirdsNests();
		Herblore.cleanHerb(c, itemId, itemSlot);
		Prayer.handleBurying(c, itemId, itemSlot);
		TeleTab.useBoneTab(c, itemId);

		switch (itemId) {
			case 12938:
						c.getItems().deleteItem(itemId, itemSlot, 1);
						c.getPA().startTeleport(2201 + Misc.random(2), 3056 + Misc.random(2), 0, "modern");
						c.sendMessage("@dre@You teleport to Zul-Randa.");
				break;
			case 6:
				c.getCannon().setUpCannon();
				break;
			case 4155:
				c.sendMessage("You currently have a task of " + c.taskAmount + " "
						+ c.getSlayer().getTaskName(c.slayerTask) + "(s).");
				break;

			case 299:
				MithrilSeeds.handleFlower(c);
				break;

			case 405:
				if (c.getItems().playerHasItem(405, 1)) {
					c.getItems().deleteItem(405, 1);
					c.getItems().addItem(995, 25000);
				}
				break;

			case 2714: // Easy Clue Scroll Casket
				c.getItems().deleteItem(itemId, 1);
				PlayerAssistant.addClueReward(c, 0);
				//c.easyClueCount += 1;
				//c.sendMessage("You've opened " + c.easyClueCount + " easy clue scrolls!");
				break;

			case 2802: // Medium Clue Scroll Casket
				c.getItems().deleteItem(itemId, 1);
				PlayerAssistant.addClueReward(c, 1);
				//c.mediumClueCount += 1;
				//c.sendMessage("You've opened " + c.mediumClueCount + " medium clue scrolls!");
				break;

			case 2775: // Hard Clue Scroll Casket
				c.getItems().deleteItem(itemId, 1);
				PlayerAssistant.addClueReward(c, 2);
				//c.hardClueCount += 1;
				//c.sendMessage("You've opened " + c.hardClueCount + " hard clue scrolls!");

				break;

			case 2677:
				int randomClue = Misc.random(11);

				switch (randomClue) {

					case 0:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2714, 1);
						c.sendMessage("You've recieved a easy clue scroll casket.");
						break;

					case 1:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2714, 1);
						c.sendMessage("You've recieved a easy clue scroll casket.");
						break;
					case 2:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2714, 1);
						c.sendMessage("You've recieved a easy clue scroll casket.");
						break;
					case 3:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2714, 1);
						c.sendMessage("You've recieved a easy clue scroll casket.");
						break;
					case 4:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2714, 1);
						c.sendMessage("You've recieved a easy clue scroll casket.");
						break;
					case 5:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2714, 1);
						c.sendMessage("You've recieved a easy clue scroll casket.");
						break;
					case 6:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2802, 1);
						c.sendMessage("You've recieved a medium clue scroll casket.");
						break;
					case 7:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2802, 1);
						c.sendMessage("You've recieved a medium clue scroll casket.");
						break;
					case 8:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2802, 1);
						c.sendMessage("You've recieved a medium clue scroll casket.");
						break;
					case 9:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2775, 1);
						c.sendMessage("You've recieved a hard clue scroll casket.");
						break;
					case 10:
						c.getItems().deleteItem(itemId, 1);
						c.getItems().addItem(2775, 1);
						c.sendMessage("You've recieved a hard clue scroll casket.");
						break;
				}
				break;
			case 607:
				c.memberPoints += 10;
				c.donPoints += 10;
				c.getItems().deleteItem(607, 1);
				c.getwM().serverMessage(
						"@dre@" + c.playerName + " has just redeemed a $10 scroll! Thank you for donating!");
				c.sendMessage("You now have " + c.memberPoints
						+ " member points. Speak to Twiggy to redeem them for cosmetics.");
				c.saveCharacter = true;
				c.saveFile = true;
				if (c.donPoints <= 9) {
					return;
				} else if (c.donPoints >= 10 && c.donPoints <= 49 && !c.getRights().equal(Rights.DONATOR)) {
					c.setRights(Rights.DONATOR);
				} else if (c.donPoints >= 50 && c.donPoints <= 99 && !c.getRights().equal(Rights.SUPER_DONATOR)) {
					c.setRights(Rights.SUPER_DONATOR);
				} else if (c.donPoints >= 100 && c.donPoints <= 249 && !c.getRights().equal(Rights.EXTREME_DONATOR)) {
					c.setRights(Rights.EXTREME_DONATOR);
				} else if (c.donPoints >= 250 && !c.getRights().equal(Rights.GOD_OF_ALL_DONATORS)) {
					c.setRights(Rights.GOD_OF_ALL_DONATORS);
				}
				c.getPA().rights();
				c.saveCharacter = true;
				c.saveFile = true;
				break;
			case 608:
				c.memberPoints += 50;
				c.donPoints += 50;
				c.getItems().deleteItem(608, 1);
				c.getwM().serverMessage(
						"@dre@" + c.playerName + " has just redeemed a $50 scroll! Thank you for donating!");
				c.sendMessage("You now have " + c.memberPoints
						+ " member points. Speak to Twiggy to redeem them for cosmetics.");
				c.saveCharacter = true;
				c.saveFile = true;
				if (c.donPoints <= 9) {
					return;
				} else if (c.donPoints >= 10 && c.donPoints <= 49 && !c.getRights().equal(Rights.DONATOR)) {
					c.setRights(Rights.DONATOR);
				} else if (c.donPoints >= 50 && c.donPoints <= 99 && !c.getRights().equal(Rights.SUPER_DONATOR)) {
					c.setRights(Rights.SUPER_DONATOR);
				} else if (c.donPoints >= 100 && c.donPoints <= 249 && !c.getRights().equal(Rights.EXTREME_DONATOR)) {
					c.setRights(Rights.EXTREME_DONATOR);
				} else if (c.donPoints >= 250 && !c.getRights().equal(Rights.GOD_OF_ALL_DONATORS)) {
					c.setRights(Rights.GOD_OF_ALL_DONATORS);
				}
				c.getPA().rights();
				c.saveCharacter = true;
				c.saveFile = true;
				break;
			case 786:
				c.memberPoints += 100;
				c.donPoints += 100;
				c.getItems().deleteItem(786, 1);
				c.getwM().serverMessage(
						"@dre@" + c.playerName + " has just redeemed a $100 scroll! Thank you for donating!");
				c.sendMessage("You now have " + c.memberPoints
						+ " member points. Speak to Twiggy to redeem them for cosmetics.");
				c.saveCharacter = true;
				c.saveFile = true;
				if (c.donPoints <= 9) {
					return;
				} else if (c.donPoints >= 10 && c.donPoints <= 49 && !c.getRights().equal(Rights.DONATOR)) {
					c.setRights(Rights.DONATOR);
				} else if (c.donPoints >= 50 && c.donPoints <= 99 && !c.getRights().equal(Rights.SUPER_DONATOR)) {
					c.setRights(Rights.SUPER_DONATOR);
				} else if (c.donPoints >= 100 && c.donPoints <= 249 && !c.getRights().equal(Rights.EXTREME_DONATOR)) {
					c.setRights(Rights.EXTREME_DONATOR);
				} else if (c.donPoints >= 250 && !c.getRights().equal(Rights.GOD_OF_ALL_DONATORS)) {
					c.setRights(Rights.GOD_OF_ALL_DONATORS);
				}
				c.getPA().rights();
				c.saveCharacter = true;
				c.saveFile = true;
				break;

			case 2528:
				c.usingLamp = true;
				c.normalLamp = true;
				c.antiqueLamp = false;
				ExperienceLamp.showInterface(c);
				break;

			case 4447:
				c.usingLamp = true;
				c.antiqueLamp = true;
				c.normalLamp = false;
				c.sendMessage("You rub the antique lamp of 13 million experience...");
				c.getPA().showInterface(2808);
				break;

			case 5073:
				c.getItems().addItem(5075, 1);
				c.getItems().deleteItem(5073, 1);
				c.getItems().handleNests(itemId);
				break;

			case 6199:
				int mysteryReward = Misc.random(15);
				switch (mysteryReward) {

					case 1:
						c.getItems().addItemToBank(9921, 1);
						c.getItems().addItemToBank(9922, 1);
						c.getItems().addItemToBank(9923, 1);
						c.getItems().addItemToBank(9924, 1);
						c.getItems().addItemToBank(9925, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A completed full skeleton!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 2:
						c.getItems().addItemToBank(11019, 1);
						c.getItems().addItemToBank(11020, 1);
						c.getItems().addItemToBank(11021, 1);
						c.getItems().addItemToBank(11022, 1);
						c.getItems().addItemToBank(4566, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A completed full chicken!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 3:
						c.getItems().addItemToBank(6654, 1);
						c.getItems().addItemToBank(6655, 1);
						c.getItems().addItemToBank(6656, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A completed full camo!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 4:
						c.getItems().addItemToBank(6666, 1);
						c.getItems().addItemToBank(7003, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A flippers and a camel mask!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 5:
						c.getItems().addItemToBank(9920, 1);
						c.getItems().addItemToBank(10507, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A jack lantern hat and a reindeer hat!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 6:
						c.getItems().addItemToBank(1037, 1);
						c.getItems().addItemToBank(1961, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A bunny ears and a easter egg!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 7:
						c.getItems().addItemToBank(1419, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A scythe!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 8:
						c.getItems().addItemToBank(4565, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A basket of eggs!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 9:
						c.getItems().addItemToBank(5607, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A grain!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 10:
						c.getItems().addItemToBank(10836, 1);
						c.getItems().addItemToBank(10837, 1);
						c.getItems().addItemToBank(10838, 1);
						c.getItems().addItemToBank(10839, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A completed full silly jester!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 11:
						c.getItems().addItemToBank(6858, 1);
						c.getItems().addItemToBank(6859, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A jester hat and scarf!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 12:
						c.getItems().addItemToBank(6856, 1);
						c.getItems().addItemToBank(6857, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A bobble hat and scarf!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 13:
						c.getItems().addItemToBank(6860, 1);
						c.getItems().addItemToBank(6861, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A tri-jester hat and scarf!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 14:
						c.getItems().addItemToBank(6862, 1);
						c.getItems().addItemToBank(6863, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A wolly hat and scarf!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

					case 15:
						c.getItems().addItemToBank(9470, 1);
						c.getItems().addItemToBank(10394, 1);
						c.getItems().deleteItem(6199, 1);
						c.sendMessage("You've gained: @blu@A gnome scarf and a flared trousers!");
						c.sendMessage("@red@The items has been added to your bank.");
						break;

				}
				break;

			case 6950:
				if (c.playerMagicBook == 0) {
					if (c.playerLevel[6] >= 94) {
						if (System.currentTimeMillis() - c.lastVeng > 30000) {
							c.vengOn = true;
							c.lastVeng = System.currentTimeMillis();
							c.startAnimation(4410);
							c.gfx100(726);
						} else {
							c.sendMessage("You have to wait 30 seconds before you can use this spell again.");
						}
					} else {
						c.sendMessage("Your magic level has to be over 94 to use this spell.");
					}
				} else {
					c.sendMessage("You must be on the regular spellbook to use this spell.");
				}
				break;
			case 8007:
				c.getPA().startTeleport(3210, 3424, 0, "modern");
				break;
			case 8008:
				c.getPA().startTeleport(3222, 3218, 0, "modern");
				break;
			case 8009:
				c.getPA().startTeleport(2964, 3378, 0, "modern");
				break;
			case 8010:
				c.getPA().startTeleport(2747, 3477, 0, "modern");
				break;
			case 8011:
				c.getPA().startTeleport(2662, 3305, 0, "modern");
				break;
			case 8012:
				c.getPA().startTeleport(2549, 3112, 0, "modern");
				break;
			case 8013:
				c.getPA().startTeleport(3225, 3218, 0, "modern");
				break;

			case DiceHandler.DICE_BAG:
				if (itemId > DiceHandler.DICE_BAG && itemId <= 15100) {
					if (c.getRights().equal(Rights.PLAYER)) {
						c.sendMessage("You must be at least bronze donator to roll a dice.");
						return;
					}
					//DiceHandler.rollDice(c, itemId, false);
				}
				DiceHandler.selectDice(c, itemId);
				break;

			case 15088:
				if (System.currentTimeMillis() - c.diceDelay >= 5000) {
					for (int j = 0; j < Server.playerHandler.players.length; j++) {
						if (Server.playerHandler.players[j] != null) {
							Client c2 = (Client) Server.playerHandler.players[j];
							c2.sendMessage("Ardi channel mate " + Misc.ucFirst(c.playerName) + " rolled @red@"
									+ Misc.random(12) + "@bla@ on two six-sided dice.");
							c.diceDelay = System.currentTimeMillis();
						}
					}
				} else {
					c.sendMessage("You must wait 10 seconds to roll dice again.");
				}
				break;

			case 15098:
				if (System.currentTimeMillis() - c.diceDelay >= 5000) {
					for (int j = 0; j < Server.playerHandler.players.length; j++) {
						if (Server.playerHandler.players[j] != null) {
							Client c2 = (Client) Server.playerHandler.players[j];
							c2.sendMessage("Ardi channel mate " + Misc.ucFirst(c.playerName) + " rolled @red@"
									+ Misc.random(100) + "@bla@ on the percentile dice.");
							c.diceDelay = System.currentTimeMillis();
						}
					}
				} else {
					c.sendMessage("You must wait 10 seconds to roll dice again.");
				}
				break;

			default:
				if (c.getRights().equal(Rights.DEVELOPER))
					Misc.println(c.playerName + " - Item1stOption: " + itemId + " : " + itemSlot);
				break;
		}

		if (nest.isNest(itemId)) {
			nest.handleOpen(c, itemId);
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			int a = itemId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().fillPouch(pouch);
			return;
		}
		if (c.getFood().isFood(itemId))
			c.getFood().eat(itemId, itemSlot);
		if (c.getPotions().isPotion(itemId))
			c.getPotions().handlePotion(itemId, itemSlot);
	}
}
