package wind.model.players.content.consumables;

import java.util.HashMap;

import wind.Config;
import wind.model.players.Client;

/**
 * @author Sanity
 */

public class Food {

	private Client c;

	public Food(Client c) {
		this.c = c;
	}

	public static enum FoodToEat {
		Easter_Egg(1961, 12, "Easter Egg"), Pumpkin(1959, 14, "Pumpkin"), Half_Jug_of_Wine(
				1989, 7, "Half Full Wine Jug"), Wine(1993, 11, "Wine"), MANTA(
				391, 22, "Manta Ray"), SHARK(385, 20, "Shark"),Dark_Crab(11936, 23, "Dark Crab"), LOBSTER(379,
				12, "Lobster"), BEER(1917, 1, "Beer"), GREENMANS_ALE(1909, 1,
				"Greenman's Ale"), TROUT(333, 7, "Trout"), SALMON(329, 9,
				"Salmon"), SWORDFISH(373, 14, "Swordfish"), TUNA(361, 10,
				"Tuna"), MONKFISH(7946, 16, "Monkfish"), SEA_TURTLE(397, 21,
				"Sea Turtle"), CABBAGE(1965, 1, "Cabbage"), CAKE(1891, 4,
				"Cake"), BASS(365, 13, "Bass"), COD(339, 7, "Cod"), POTATO(
				1942, 1, "Potato"), BAKED_POTATO(6701, 4, "Baked Potato"), POTATO_WITH_CHEESE(
				6705, 16, "Potato with Cheese"), EGG_POTATO(7056, 16,
				"Egg Potato"), CHILLI_POTATO(7054, 14, "Chilli Potato"), MUSHROOM_POTATO(
				7058, 20, "Mushroom Potato"), TUNA_POTATO(7060, 22,
				"Tuna Potato"), SHRIMPS(315, 3, "Shrimps"), HERRING(347, 5,
				"Herring"), SARDINE(325, 4, "Sardine"), CHOCOLATE_CAKE(1897, 5,
				"Chocolate Cake"), HALF_CHOCOLATE_CAKE(1899, 5,
				"2/3 Chocolate Cake"), CHOCOLATE_SLICE(1901, 5,
				"Chocolate Slice"), ANCHOVIES(319, 1, "Anchovies"), PLAIN_PIZZA(
				2289, 7, "Plain Pizza"), HALF_PLAIN_PIZZA(2291, 7,
				"1/2 Plain pizza"), MEAT_PIZZA(2293, 8, "Meat Pizza"), HALF_MEAT_PIZZA(
				2295, 8, "1/2 Meat Pizza"), ANCHOVY_PIZZA(2297, 9,
				"Anchovy Pizza"), HALF_ANCHOVY_PIZZA(2299, 9,
				"1/2 Anchovy Pizza"), PINEAPPLE_PIZZA(2301, 11,
				"Pineapple Pizza"), HALF_PINEAPPLE_PIZZA(2303, 11,
				"1/2 Pineapple Pizza"), BREAD(2309, 5, "Bread"), APPLE_PIE(
				2323, 7, "Apple Pie"), HALF_APPLE_PIE(2335, 7, "Half Apple Pie"), REDBERRY_PIE(
				2325, 5, "Redberry Pie"), HALF_REDBERRY_PIE(2333, 5,
				"Half Redberry Pie"), Ugthanki_kebab(1883, 2, "Ugthanki kebab"), MEAT_PIE(
				2327, 6, "Meat Pie"), HALF_MEAT_PIE(2331, 6, "Half Meat Pie"), SUMMER_PIE(
				7218, 11, "Summer Pie"), HALF_SUMMER_PIE(7220, 11,
				"Half Summer Pie"), PIKE(351, 8, "Pike"), POTATO_WITH_BUTTER(
				6703, 14, "Potato with Butter"), BANANA(1963, 2, "Banana"), PEACH(
				6883, 8, "Peach"), ORANGE(2108, 2, "Orange"), PINEAPPLE_RINGS(
				2118, 2, "Pineapple Rings"), PINEAPPLE_CHUNKS(2116, 2,
				"Pineapple Chunks"), EASTER_EGG(7928, 1, "Easter Egg"), EASTER_EGG2(
				7929, 1, "Easter Egg"), EASTER_EGG3(7930, 1, "Easter Egg"), EASTER_EGG4(
				7931, 1, "Easter Egg"), EASTER_EGG5(7932, 1, "Easter Egg"), EASTER_EGG6(
				7933, 1, "Easter Egg"), PURPLE_SWEETS(10476, 9, "Purple Sweets"), POT_OF_CREAM(
				2130, 1, "Pot of cream"), BANDAGES(4049, 3, "Bandages");

		private int id;
		private int heal;
		private String name;

		private FoodToEat(int id, int heal, String name) {
			this.id = id;
			this.heal = heal;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public int getHeal() {
			return heal;
		}

		public String getName() {
			return name;
		}

		public static HashMap<Integer, FoodToEat> food = new HashMap<Integer, FoodToEat>();

		public static FoodToEat forId(int id) {
			return food.get(id);
		}

		static {
			for (FoodToEat f : FoodToEat.values())
				food.put(f.getId(), f);
		}
	}

	public void eat(int id, int slot) {
		if (c.duelRule[6]) {
			c.sendMessage("You may not eat in this duel.");
			return;
		}
		if (c.isDead) {
			return;
		}
		if (System.currentTimeMillis() - c.foodDelay >= 1550
				&& c.playerLevel[3] > 0) {
			c.getCombat().resetPlayerAttack();
			c.attackTimer += 2;
			c.startAnimation(829);
			c.getItems().deleteItem(id, slot, 1);
			FoodToEat f = FoodToEat.food.get(id);
			if (c.playerLevel[3] < c.getLevelForXP(c.playerXP[3])) {
				c.playerLevel[3] += f.getHeal();
				if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
					c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
			}
			c.foodDelay = System.currentTimeMillis();
			if (Config.SOUND) {
				c.getPA().sendSound(c.getSounds().FOODEAT);
			}
			c.getPA().refreshSkill(3);
			c.sendMessage("You eat the " + f.getName() + ".");
		}
	}

	public boolean isFood(int id) {
		return FoodToEat.food.containsKey(id);
	}

}