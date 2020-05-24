package wind.model.players.packets.clicking.dialoguebuttons;

import wind.Config;
import wind.model.npcs.impl.Zulrah;
import wind.model.players.Client;
import wind.model.players.content.minigames.nightmarezone.NightmareZone;
import wind.model.players.content.minigames.nightmarezone.Mode.BattleState;
import wind.model.players.content.skills.SkillMasters;

public class FourOptions {

	public static void handleOption1(Client c) {

		switch (c.teleAction) {

		case 2:
			// barrows
			c.getPA().spellTeleport(3565, 3314, 0);
			break;

		case 13:
			c.getPA().spellTeleport(2839, 5293, 2);
			c.sendMessage("You must know it's not easy, get a team to own that boss!");
			break;
		}

		switch (c.dialogueAction) {

		case 2:
			c.getPA().startTeleport(3428, 3538, 0, "modern");
			break;

		case 3:
			c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0,
					"modern");
			break;

		case 4:
			c.getPA().startTeleport(3565, 3314, 0, "modern");
			break;

		case 20:
			c.getPA().startTeleport(2897, 3618, 4, "modern");
			c.killCount = 0;
			break;
		case 443:
			c.getShops().openShop(54);
			break;
		case 504:
			if (c.inZulrahShrine())
				return;
			Zulrah.init(c);
			break;
			
		case 505:
			c.getPA().startTeleport(3694, 5799, 0, "modern");
			break;
		case 667:
			if (c.getItems().playerHasItem(6737))
				c.getDH().sendDialogues(668, 490);
			else if (!c.getItems().playerHasItem(6737)) {
				c.getDH().sendStatement("You need a ring to upgrade first!");
				c.nextChat = 0;
			}
			break;
		case 171:
			c.dialogueAction = 0;
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			NightmareZone.getInstance().play(c, BattleState.HARD);
			break;

		case 416:
			c.getPA().showInterface(8292);
			break;

		case 1658:
			if (!c.getItems().playerHasItem(995, 2230000)) {
				c.sendMessage("You must have 2,230,000 coins to buy this package.");
				c.getPA().removeAllWindows();
				c.dialogueAction = 0;
				break;
			}
			c.dialogueAction = 0;
			c.getItems().addItemToBank(560, 4000);
			c.getItems().addItemToBank(565, 2000);
			c.getItems().addItemToBank(555, 6000);
			c.getItems()
					.deleteItem(995, c.getItems().getItemSlot(995), 2230000);
			c.sendMessage("<col=FF0000>The runes has been added to your bank.</col>");
			c.getPA().removeAllWindows();
			break;

		case 36899: //Cash King
			c.getDH().sendDialogues(220, 220);
			c.getPA().removeAllWindows();
			break;
			
		}
		if (c.usingGlory)
			// c.getPA().useCharge();
			c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0,
					"modern");
		if (c.caOption4a) {
			c.getDH().sendDialogues(102, c.npcType);
			c.caOption4a = false;
		}
		if (c.caOption4c) {
			c.getDH().sendDialogues(118, c.npcType);
			c.caOption4c = false;
		}
	}

	public static void handleOption2(Client c) {

		switch (c.teleAction) {

		case 2:
			// assault
			c.getPA().spellTeleport(2605, 3153, 0);
			break;

		case 13:
			c.getPA().spellTeleport(2860, 5354, 2);
			c.sendMessage("You must know it's not easy, get a team to own that boss!");
			break;
		}

		switch (c.dialogueAction) {

		case 2:
			c.getPA().startTeleport(2884, 3395, 0, "modern");
			break;

		case 3:
			c.getPA().startTeleport(3243, 3513, 0, "modern");
			break;

		case 4:
			c.getPA().startTeleport(2444, 5170, 0, "modern");
			break;

		case 20:
			c.getPA().startTeleport(2897, 3618, 12, "modern");
			c.killCount = 0;
			break;

		case 171:
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			c.sendMessage("This mode is not active yet, current working modes: hard.");
			c.dialogueAction = 0;
			break;

		case 416:
			c.getPA().movePlayer(2473, 3438, 0);
			break;
		case 443:
			if (c.playerLevel[18] == 99) {
				c.getDH().sendNpcChat("Indeed you have. A cape is available in my shop for you.", c.getDH().HAPPY, "Nieve");
			} else if (c.playerLevel[18] <=98) {
				c.getDH().sendNpcChat("It appears you're only level "+c.playerLevel[18]+" Slayer, sorry.", c.getDH().ANNOYED, "Nieve");
			}
			break;
		case 504:
			c.getPA().startAdminTeleport(3696, 5806, 0);
			c.sendMessage("@dre@Welcome. The @or2@Kraken@dre@ is currently in [@whi@DEV@dre@] mode.");
			break;
		case 667:
			if (c.getItems().playerHasItem(6731))
				c.getDH().sendDialogues(668, 490);
			else if (!c.getItems().playerHasItem(6731)) {
				c.getDH().sendStatement("You need a ring to upgrade first!");
				c.nextChat = 0;
			}
			break;
		case 1658:
			if (!c.getItems().playerHasItem(995, 912000)) {
				c.sendMessage("You must have 912,000 coins to buy this package.");
				c.getPA().removeAllWindows();
				c.dialogueAction = 0;
				break;
			}
			c.dialogueAction = 0;
			c.getItems().addItemToBank(560, 2000);
			c.getItems().addItemToBank(9075, 4000);
			c.getItems().addItemToBank(557, 10000);
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 912000);
			c.sendMessage("<col=FF0000>The runes has been added to your bank.</col>");
			c.getPA().removeAllWindows();
			break;

		case 36899:
			c.getPA().Change();
			break;

		}
		if (c.usingGlory)
			// c.getPA().useCharge();
			c.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0,
					"modern");
		if (c.caOption4c) {
			c.getDH().sendDialogues(120, c.npcType);
			c.caOption4c = false;
		}
		if (c.caPlayerTalk1) {
			c.getDH().sendDialogues(125, c.npcType);
			c.caPlayerTalk1 = false;
		}
	}

	public static void handleOption3(Client c) {

		switch (c.teleAction) {

		case 2: // duel arena
			c.getPA().spellTeleport(3366, 3266, 0);
			break;

		case 13:
			c.getPA().spellTeleport(2925, 5334, 2);
			c.sendMessage("You must know it's not easy, get a team to own that boss!");
			break;

		}

		switch (c.dialogueAction) {

		case 2:
			c.getPA().startTeleport(2471, 10137, 0, "modern");
			break;

		case 3:
			c.getPA().startTeleport(3363, 3676, 0, "modern");
			break;

		case 4:
			c.getPA().startTeleport(2659, 2676, 0, "modern");
			break;

		case 20:
			c.getPA().startTeleport(2897, 3618, 8, "modern");
			c.killCount = 0;
			break;

		case 171:
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			c.sendMessage("This mode is not active yet, current working modes: hard.");
			c.dialogueAction = 0;
			break;
		case 443:
			c.getDH().sendPlayerChat("Can I upgrade one of my rings?", c.getDH().HAPPY);
			c.nextChat = 666;
			break;
		case 416:
		case 420:
		case 422:
		case 428:
		case 432:
		case 550:
			SkillMasters.addSkillCape(c);
			break;
		case 667:
			if (c.getItems().playerHasItem(6733))
				c.getDH().sendDialogues(668, 490);
			else if (!c.getItems().playerHasItem(6733)) {
				c.getDH().sendStatement("You need a ring to upgrade first!");
				c.nextChat = 0;
			}
			break;
		case 1658:
			if (!c.getItems().playerHasItem(995, 1788000)) {
				c.sendMessage("You must have 1,788,000 coins to buy this package.");
				c.getPA().removeAllWindows();
				c.dialogueAction = 0;
				break;
			}
			c.dialogueAction = 0;
			c.getItems().addItemToBank(556, 1000);
			c.getItems().addItemToBank(554, 1000);
			c.getItems().addItemToBank(558, 1000);
			c.getItems().addItemToBank(557, 1000);
			c.getItems().addItemToBank(555, 1000);
			c.getItems().addItemToBank(560, 1000);
			c.getItems().addItemToBank(565, 1000);
			c.getItems().addItemToBank(566, 1000);
			c.getItems().addItemToBank(9075, 1000);
			c.getItems().addItemToBank(562, 1000);
			c.getItems().addItemToBank(561, 1000);
			c.getItems().addItemToBank(563, 1000);
			c.getItems()
					.deleteItem(995, c.getItems().getItemSlot(995), 1788000);
			c.sendMessage("<col=FF0000>The runes has been added to your bank.</col>");
			c.getPA().removeAllWindows();
			break;

		case 36899:
			c.getPA().UnChange();
			break;

		}
		if (c.usingGlory)
			// c.getPA().useCharge();
			c.getPA().startTeleport(Config.KARAMJA_X, Config.KARAMJA_Y, 0,
					"modern");
		if (c.caOption4c) {
			c.getDH().sendDialogues(122, c.npcType);
			c.caOption4c = false;
		}
		if (c.caPlayerTalk1) {
			c.getDH().sendDialogues(127, c.npcType);
			c.caPlayerTalk1 = false;
		}
	}
	
	public static void handleOption4(Client c) {

		switch (c.teleAction) {

		case 2: // tzhaar
			c.getPA().spellTeleport(2444, 5170, 0);
			break;

		case 13:
			c.getPA().spellTeleport(2912, 5266, 0);
			c.sendMessage("You must know it's not easy, get a team to own that boss!");
			break;

		}

		switch (c.dialogueAction) {
		case 2:
			c.getPA().startTeleport(2669, 3714, 0, "modern");
			break;

		case 3:
			c.getPA().startTeleport(2540, 4716, 0, "modern");
			break;

		case 4:
			c.getPA().startTeleport(3366, 3266, 0, "modern");
			c.sendMessage("Dueling is at your own risk. Refunds will not be given for items lost due to glitches.");
			break;

		case 20:
			// c.getPA().startTeleport(3366, 3266, 0, "modern");
			// c.killCount = 0;
			c.sendMessage("This will be added shortly");
			break;
			
		case 171:
			c.sendMessage("You did not enter to the dream.");
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			c.dialogueAction = 0;
		break;
		case 443:
		case 416:
		case 420:
		case 422:
		case 428:
		case 432:
		case 485:
		case 550:
		case 667:
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
			break;

		case 1658:
			c.getShops().openShop(5);
			c.dialogueAction = 0;
			break;

		case 36899:
			c.getPA().removeAllWindows();
			break;
		}

		if (c.usingGlory)
			// c.getPA().useCharge();
			c.getPA().startTeleport(Config.DRAYNOR_X, Config.DRAYNOR_Y,
					0, "modern");
		if (c.caOption4c) {
			c.getDH().sendDialogues(124, c.npcType);
			c.caOption4c = false;
		}
		if (c.caPlayerTalk1) {
			c.getDH().sendDialogues(130, c.npcType);
			c.caPlayerTalk1 = false;
		}
	}

}
