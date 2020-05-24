package wind.model.players.content.skills.impl.farming.herbs;

import wind.model.players.Client;
import wind.model.players.content.skills.impl.farming.Farming;
import wind.util.Misc;

public class HerbPicking {

	public static void herbPicking(Client c, int herb) {
		for (final Herbs.Seeds h : Herbs.Seeds.values()) {
			if (herb == h.getHerb()) {
				if (c.herbAmount <= 0)
					return;
				if (c.getEquipment().freeSlots() < 1) {
					c.sendMessage("You don't have any free space to do this!");
					return;
				}
				if (c.herbAmount == 1) {
					c.currentHerb = -1;
					c.herbAmount = -1;
					c.getPA().object(Farming.PATCH_WEEDS, c.objectX, c.objectY,
							-1, 10);
					c.sendMessage("You finish picking all the herbs off of the patch.");
				}
				c.startAnimation(2286);
				c.herbAmount--;
				c.getItems().addItem(h.getHerb(), 1);
				c.getPA().addSkillXP(h.getSeedExp(), 19);
			}
		}
	}

	public static int getHerbAmount(Client client, int herb) {
		return (herb < 5300) ? 4 + Misc.random(4) : 2 + Misc.random(6);
	}

}
