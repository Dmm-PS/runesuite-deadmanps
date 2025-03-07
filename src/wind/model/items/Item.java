package wind.model.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import wind.Config;

/**
 * Handles The Item Wielding Of The Server Used When Hardcoding Additional Items
 * Fixing Bugged Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class Item {

	public static boolean playerCapes(int itemId) {
		String[] data = { "Graceful cape", "TokHaar-Kal", "Ava's accumulator", "cloak", "cape",
				"Ava's attractor", "bonesack", "Bonesack", "Cape", "apparatus" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBoots(int itemId) {
		String[] data = { "Glaiven boots", "Graceful boots", "Ragefire boots", "Steadfast boots",
				"Flippers", "Shoes", "shoes", "boots", "Boots", "Dragon boots", "dragon boots" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerGloves(int itemId) {
		String[] data = { "Combat bracelet (4)", "Combat bracelet", "Bracelet",
				"Gloves", "gloves", "glove", "Graceful gloves", "Glove", "gauntlets", "Gauntlets",
				"vamb" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerShield(int itemId) {
		String[] data = { "lantern", "Toktz-ket-xil", "defender", "kiteshield",
				"Book", "book", "Kiteshield", "shield", "Shield", "Kite",
				"kite", "spirit", "Mages' book" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerAmulet(int itemId) {
		String[] data = { "Gnome scarf", "scarf", "Phoenix necklace",
				"necklace", "amulet", "Amulet", "Pendant", "pendant", "Symbol",
				"symbol", "Arcane stream" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerArrows(int itemId) {
		String[] data = { "Arrows", "arrows", "Arrow", "arrow", "Bolts",
				"bolts", "Bolt rack" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerRings(int itemId) {
		String[] data = { "ring", "rings", "Ring", "Rings", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerHats(int itemId) {
		String[] data = { "Wolf mask", "Bat mask", "Penguin mask", "Cat mask",
				"Guthix mitre", "Saradomin mitre", "Zamorak mitre", "mitre",
				"Feather headdress", "boater", "cowl", "peg", "coif", "helm",
				"coif", "mask", "hat", "headband", "hood", "headdress",
				"disguise", "cavalier", "full", "tiara", "Tiara", "helmet",
				"Hat", "ears", "partyhat", "helm(t)", "Sleeping cap",
				"helm(g)", "beret", "facemask", "sallet", "A powdered wig",
				"hat(g)", "hat(t)", "bandana", "Helm", "Bearhead", "halo", "helm", "Graceful hood" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if ((item.endsWith(data[i]) || item.contains(data[i]))
					&& (!item.contains("rystal bow full"))) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerLegs(int itemId) {
		String[] data = { "tassets", "chaps", "bottoms", "gown", "trousers",
				"platelegs", "robe", "plateskirt", "legs", "leggings",
				"shorts", "Skirt", "skirt", "cuisse", "Trousers", "Pantaloons", "Graceful legs" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if ((item.endsWith(data[i]) || item.contains(data[i]))
					&& (!item.contains("top") && (!item.contains("robe (g)") && (!item
							.contains("robe (t)") && (!item.contains("Doctor") && (!item
							.contains("Priest gown"))))))) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBody(int itemId) {
		String[] data = { "body", "top", "Priest gown", "apron", "shirt",
				"Doctors' gown", "platebody", "robetop", "body(g)", "body(t)",
				"Doctor", "Wizard robe (g)", "Wizard robe (t)", "body",
				"brassard", "blouse", "tunic", "leathertop", "Saradomin plate",
				"chainbody", "hauberk", "Shirt", "torso", "chestplate",
				"Saradomin", "Guthix", "Zamorak", "Graceful top" };
		String item = getItemName(itemId);
		if (item == null || item.contains("mjolnir")
				|| item.contains("aradomin sword") || item.contains("godsword")) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	/**
	 * Full body items that cover your arms. Names go here.
	 */
	private static String[] fullbody = { "wings", "Spined body", "plate",
			"Monk's robe", "torso", "top", "shirt", "platebody",
			"Ahrims robetop", "Karils leathertop", "brassard", "Robe top",
			"robetop", "platebody (t)", "platebody (g)", "chestplate", "torso",
			"hauberk", "Dragon chainbody" };

	/**
	 * Full hat items that cover your head go here. Names go here.
	 */
	private static String[] fullhat = { "helmet", "mage hat", "med helm",
			"coif", "Dharok's helm", "hood", "Initiate helm", "Coif",
			"Helm of neitiznot", "Armadyl helmet", "Berserker helm",
			"Archer helm", "Farseer helm", "Warrior helm", "Void"};

	/**
	 * Full mask items that cover your face go here. Names go here.
	 */
	private static String[] fullmask = { "helm (t)", "full helm(g)", "full",
			"heraldic", "heraldic helm", "full helm", "mask", "Verac's helm",
			"Guthan's helm", "Karil's coif", "mask", "Torag's helm", "Void",
			"sallet", "slayer", "Tanzanite helm", "Magma helm" };

	public static boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil")
				|| itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow")
				|| itemName.contains("ark bow")) {
			return true;
		}
		if (itemName.contains("crystal") || itemName.contains("maul")
				|| itemName.contains("cannon")) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("claw")
				|| itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear")) {
			return true;
		}
		if (itemName.contains("zaryte")) {
			return true;
		}
		switch (itemId) {
		case 6724: // seercull
		case 11838:
		case 4153:
		case 6528:
		case 10887:
		case 14484:

			return true;
		}
		return false;
	}

	/**
	 * Calls if an item is a full body item.
	 */
	public static boolean isFullBody(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullbody.length; i++) {
			if (weapon.endsWith(fullbody[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls if an item is a full helm item.
	 */
	public static boolean isFullHelm(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullhat.length; i++) {
			if (weapon.endsWith(fullhat[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls if an item is a full mask item.
	 */
	public static boolean isFullMask(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullmask.length; i++) {
			if (weapon.endsWith(fullmask[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets an item name from the itemlist.
	 */
	public static String getItemName(int ItemID) {
		return ItemTableManager.forID(ItemID).getName();
	}

	/**
	 * Checks if the item is stackable.
	 */
	public static boolean[] itemStackable = new boolean[Config.ITEM_LIMIT];

	/**
	 * Checks if the item can be made into a note.
	 */
	public static boolean[] itemIsNote = new boolean[Config.ITEM_LIMIT];

	/**
	 * Checks if the item can wield into a slot.
	 */
	public static int[] targetSlots = new int[Config.ITEM_LIMIT];
	static {
		int counter = 0;
		int c;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					Config.DATA_LOC + "data/stackable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemStackable[counter] = false;
				} else {
					itemStackable[counter] = true;
					itemStackable[12934] = true;
					itemStackable[12938] = true;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out
					.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					Config.DATA_LOC + "data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
					for (int i = 0; i < Config.ITEM_NOTABLE.length; i++)
						itemIsNote[i] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}

		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File(
					Config.DATA_LOC + "data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
	}

}