package wind.model.players.packets;

import com.johnmatczak.model.BankKey;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.PriceChecker;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readDWord();
		if (Xamount < 0) {
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0) {
			Xamount = 1;
		} 
	/*	if (c.attackSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 0;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.attackSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.defenceSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 1;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.defenceSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.strengthSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 2;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.strengthSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.healthSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 3;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.healthSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.rangeSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 4;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.rangeSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.prayerSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 5;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.prayerSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.mageSkill) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if ((c.playerEquipment[j] > 0) && c.getRights().lessOrEqual(Rights.DONATOR)) {
					c.sendMessage("Please remove all your equipment before using this command.");
					return;
				}
			}
			try {
				int skill = 6;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.mageSkill = false;
			} catch (Exception e) {
			} */
		{
		}
	
		switch (c.xInterfaceId) {
		
		case 22204:
			BankKey.Raid.Take(c, c.xRemoveId, Xamount);
			break;
		
		case 4393:
			PriceChecker.withdrawItem(c, c.price[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 5064:
	        if(!c.getItems().playerHasItem(c.xRemoveId, Xamount))
                return;
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 5382:
			c.getItems().fromBank(c.bankItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 7423:
			if (c.storing) {
				return;
			}
			if (c.isBanking)
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			else
				return;
			c.getItems().resetItems(7423);
			break;

		case 3322:
	        if(!c.getItems().playerHasItem(c.xRemoveId, Xamount))
                return;
			if (c.duelStatus <= 0) {
				if (Xamount > c.getItems().getItemAmount(c.xRemoveId))
					c.getTrade().tradeItem(c.xRemoveId, c.xRemoveSlot,
							c.getItems().getItemAmount(c.xRemoveId));
				else
					c.getTrade().tradeItem(c.xRemoveId, c.xRemoveSlot,
							Xamount);
			} else {
				if (Xamount > c.getItems().getItemAmount(c.xRemoveId))
					c.getDuel().stakeItem(c.xRemoveId, c.xRemoveSlot,
							c.getItems().getItemAmount(c.xRemoveId));
				else
					c.getDuel().stakeItem(c.xRemoveId, c.xRemoveSlot,
							Xamount);
			}
			break;

		case 3415:
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount))
				return;
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(c.xRemoveId, c.xRemoveSlot,
						Xamount);
			}
			break;

		case 6669:
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount))
				return;
			c.getDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;
			
		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				c.sendMessage("Interface ID: " + c.xInterfaceId);
			break;
			
		}
	}
}