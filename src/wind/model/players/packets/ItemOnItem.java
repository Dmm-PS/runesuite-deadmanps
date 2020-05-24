package wind.model.players.packets;

/**
 * @author Ryan / Lmctruck30
 */

import wind.model.items.UseItem;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.content.CrystalChest;
import wind.model.players.content.skills.impl.JewelryMaking;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
        if(!c.getItems().playerHasItem(useWith, 1, usedWithSlot) || !c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
            return;
        }
		UseItem.ItemonItem(c, itemUsed, useWith, itemUsedSlot, usedWithSlot);

		if (itemUsed == CrystalChest.TOOTH_HALVE
				&& useWith == CrystalChest.LOOP_HALVE
				|| itemUsed == CrystalChest.LOOP_HALVE
				&& useWith == CrystalChest.TOOTH_HALVE) {
			CrystalChest.makeKey(c);
		}
		if (itemUsed == 11818
				&& useWith == 11820
				|| itemUsed == 11820
				&& useWith == 11818) {
			c.getItems().deleteItem(11818, 1);
			c.getItems().deleteItem(11820, 1);
			c.getItems().addItem(11794, 1);
		}
		if (itemUsed == 11822
				&& useWith == 11818
				|| itemUsed == 11818
				&& useWith == 11822) {
			c.getItems().deleteItem(11818, 1);
			c.getItems().deleteItem(11822, 1);
			c.getItems().addItem(11796, 1);
		}
		if (itemUsed == 11822
				&& useWith == 11820
				|| itemUsed == 11820
				&& useWith == 11822) {
			c.getItems().deleteItem(11822, 1);
			c.getItems().deleteItem(11820, 1);
			c.getItems().addItem(11800, 1);
		}
		if (itemUsed == 11794
				&& useWith == 11800
				|| itemUsed == 11800
				&& useWith == 11794) {
			c.getItems().deleteItem(11794, 1);
			c.getItems().deleteItem(11800, 1);
			c.getItems().addItem(11798, 1);
		}
		if (itemUsed == 11794
				&& useWith == 11796
				|| itemUsed == 11796
				&& useWith == 11794) {
			c.getItems().deleteItem(11794, 1);
			c.getItems().deleteItem(11796, 1);
			c.getItems().addItem(11798, 1);
		}
		if (itemUsed == 11800
				&& useWith == 11796
				|| itemUsed == 11796
				&& useWith == 11800) {
			c.getItems().deleteItem(11800, 1);
			c.getItems().deleteItem(11796, 1);
			c.getItems().addItem(11798, 1);
		}
		if (itemUsed == 11798
				&& useWith == 11810
				|| itemUsed == 11810
				&& useWith == 11798) {
			c.getItems().deleteItem(11810, 1);
			c.getItems().deleteItem(11798, 1);
			c.getItems().addItem(11802, 1);
		}
		if (itemUsed == 11798
				&& useWith == 11812
				|| itemUsed == 11812
				&& useWith == 11798) {
			c.getItems().deleteItem(11812, 1);
			c.getItems().deleteItem(11798, 1);
			c.getItems().addItem(11804, 1);
		}
		if (itemUsed == 11798
				&& useWith == 11814
				|| itemUsed == 11814
				&& useWith == 11798) {
			c.getItems().deleteItem(11814, 1);
			c.getItems().deleteItem(11798, 1);
			c.getItems().addItem(11806, 1);
		}
		if (itemUsed == 11798
				&& useWith == 11816
				|| itemUsed == 11816
				&& useWith == 11798) {
			c.getItems().deleteItem(11816, 1);
			c.getItems().deleteItem(11798, 1);
			c.getItems().addItem(11808, 1);
		}
		if (itemUsed == 1755 && useWith == 12927) {
			if (!(c.playerLevel[c.playerCrafting] >= 51)) {
				c.sendMessage("You need 51 crafting do this.");
				return;
			}
			if (c.getItems().playerHasItem(1755, 1)
					&& c.getItems().playerHasItem(12927, 1)) {
			c.sendMessage("You've made a serpentine helm!");
			c.getItems().deleteItem(12927, 1);
			c.getItems().addItem(12929, 1);
			c.getPA().addSkillXP(100000, 12);
			}
			
		}
		if ((itemUsed == 3837 || itemUsed == 3836 || itemUsed == 3835 || itemUsed == 3838) && useWith == 3843) {
			if(c.getItems().playerHasItem(3837) && c.getItems().playerHasItem(3835) && c.getItems().playerHasItem(3836) && c.getItems().playerHasItem(3838) && c.getItems().playerHasItem(3843)) {
				c.getItems().deleteItem(3837, 1);
				c.getItems().deleteItem(3835, 1);
				c.getItems().deleteItem(3836, 1);
				c.getItems().deleteItem(3838, 1);
				c.getItems().deleteItem(3843, 1);
				c.getItems().addItem(3844, 1);
				c.sendMessage("You've filled the @gre@Damaged Book@bla@ with pages and made a @gre@Book of Balance@bla@!");
			}
		}
		if ((itemUsed == 3827 || itemUsed == 3828 || itemUsed == 3829 || itemUsed == 3830) && useWith == 3839) {
			if(c.getItems().playerHasItem(3827) && c.getItems().playerHasItem(3828) && c.getItems().playerHasItem(3829) && c.getItems().playerHasItem(3830) && c.getItems().playerHasItem(3839)) {
				c.getItems().deleteItem(3827, 1);
				c.getItems().deleteItem(3828, 1);
				c.getItems().deleteItem(3829, 1);
				c.getItems().deleteItem(3830, 1);
				c.getItems().deleteItem(3839, 1);
				c.getItems().addItem(3840, 1);
				c.sendMessage("You've filled the @blu@Damaged Book@bla@ with pages and made a @blu@Holy Book@bla@!");
			}
		}
		if ((itemUsed == 3831 || itemUsed == 3832 || itemUsed == 3833 || itemUsed == 3834) && useWith == 3841) {
			if(c.getItems().playerHasItem(3831) && c.getItems().playerHasItem(3832) && c.getItems().playerHasItem(3833) && c.getItems().playerHasItem(3834) && c.getItems().playerHasItem(3841)) {
				c.getItems().deleteItem(3831, 1);
				c.getItems().deleteItem(3832, 1);
				c.getItems().deleteItem(3833, 1);
				c.getItems().deleteItem(3834, 1);
				c.getItems().deleteItem(3841, 1);
				c.getItems().addItem(3842, 1);
				c.sendMessage("You've filled the @red@Damaged Book@bla@ with pages and made a @red@Unholy Book@bla@!");
			}
		}
		if (itemUsed == 12934 && useWith == 12929) {
			if (!(c.playerLevel[c.playerCrafting] >= 51)) {
				c.sendMessage("You need 51 crafting do this.");
				return;
			}
			if (c.getItems().playerHasItem(12934, 20000)
					&& c.getItems().playerHasItem(12929, 1)) {
			c.sendMessage("You've charged your serpentine helm!");
			c.getItems().deleteItem(12934, 20000);
			c.getItems().deleteItem(12929, 1);
			c.getItems().addItem(12931, 1);
			c.getPA().addSkillXP(100000, 12);
			} else {
				c.sendMessage("You need 20,000 zulrah scales to charge this!");
			}
			
		}
		if (itemUsed == 1759 || useWith == 1759) {
			JewelryMaking.stringAmulet(c, itemUsed, useWith);
		}
		if (itemUsed == CrystalChest.SHIELD_LEFT_HALF
				&& useWith == CrystalChest.SHIELD_RIGHT_HALF
				|| itemUsed == CrystalChest.SHIELD_RIGHT_HALF
				&& useWith == CrystalChest.SHIELD_LEFT_HALF) {
			CrystalChest.makeShield(c);
			c.sendMessage("You've made a Dragon Sq Shield!");
		}
	}
}
