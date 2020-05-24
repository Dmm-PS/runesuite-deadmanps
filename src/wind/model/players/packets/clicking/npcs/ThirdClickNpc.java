package wind.model.players.packets.clicking.npcs;

import wind.model.npcs.pets.Pet;
import wind.model.players.Client;
import wind.model.players.Rights;
import wind.util.ScriptManager;

public class ThirdClickNpc {

	public static void handleClick(Client c, int npcId) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcId) {
		case 2897:
		case 2898:
			c.sendMessage("The Grand Exchange is still in development, thank you for being patient.");
			break;
		case 4642:
			c.sendMessage("Trade me to buy a pass.");
			break;
		case 637:
			c.getPA().startTeleport2(2898, 4819, 0);
			break;
		case 2130: //g
			if (c.petSummoned && c.petID == npcId)
				c.getPA().showInterface(22700);
			break;
		case 2131: //r
			if (c.petSummoned && c.petID == npcId)
			c.getPA().showInterface(44600);
			break;
		case 2132: //b
			if (c.petSummoned && c.petID == npcId)
			c.getPA().showInterface(22800);
			break;
			case 534:
			if (!c.isWearingEquipment()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;
			
		case 5536:
			if (c.petSummoned) {
			//c.petSummoned = false;
			c.setPetSummoned(false);
			
			} else {
			c.getPetSummoned();
			c.petID = 5537;
			c.petSummoned = true;
			c.setPetSummoned(true);
			}
			break;
			
		case 6637:
			Pet.pickUpPetRequirements(c, npcId);
			break;
		case 1306:
			if (!c.isWearingEquipment()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;

		case 490:
			c.getDH().sendOption2("Show me what you've got for sale.",
			"Show me your reward shop.");
			c.dialogueAction = 500;
			

				//c.getShops().openShop(54);
			break;

		case 548:
			if (!c.isWearingEquipment()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;

		case 836:
			c.getShops().openShop(103);
			break;

		default:
			ScriptManager.callFunc("npcClick3_" + npcId, c, npcId);
			if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR))
				c.sendMessage("Third Click NPC : " + npcId);
			break;
		}
	}

}
