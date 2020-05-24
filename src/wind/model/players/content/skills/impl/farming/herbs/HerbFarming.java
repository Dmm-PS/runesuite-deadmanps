package wind.model.players.content.skills.impl.farming.herbs;

import wind.model.players.Client;

public class HerbFarming {

	public static void farmHerbs(Client c, int seed) {
		for (final Herbs.Seeds h : Herbs.Seeds.values()) {
			if (h.getSeed() == seed) {
				if (c.playerLevel[19] < h.getLevelReq()) {
					c.sendMessage("You need a Farming level of "
							+ h.getLevelReq() + " to plant this seed.");
					return;
				}
				c.startAnimation(2291);
				c.sendMessage("You plant the seed into the ground, and it grows into a herb.");
				c.getItems().deleteItem2(h.getSeed(), 1);
				c.getPA().addSkillXP(h.getSeedExp(), 19);
				c.currentHerb = h.getHerb();
				c.herbAmount = HerbPicking.getHerbAmount(c, h.getHerb());
				c.getPA().object(Herbs.HERB_PICKING, 2813, 3463, -1, 10);
			}
		}
	}

	public static boolean isHerbSeed(Client client, int seed) {
		for (final Herbs.Seeds h : Herbs.Seeds.values()) {
			if (seed == h.getSeed()) {
				return true;
			}
		}
		return false;
	}

}
