package wind.model.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;





import wind.Config;
import wind.Server;

/*Add your imports*/
import com.google.gson.Gson;

/**
 * ItemTableManager.java
 * 
 * Loads individual Items from JSON Files
 * 
 * @author Dragonking
 *
 */
public class ItemTableManager {

	public static final HashMap<Integer, PlayerItem> ITEMS = new HashMap<>();

	public static final PlayerItem forID(final int npcID) {
		if (!ITEMS.containsKey(npcID))
			return ITEMS.get(1);

		return ITEMS.get(npcID);
	}
	
	public static final PlayerItem forName(final int itemId) {
		if (!ITEMS.containsKey(itemId))
			return ITEMS.get(1);
		return ITEMS.get(itemId);
	}

	public static final void load() {
		final Gson gson = new Gson();
		final File dir = new File(Config.DATA_LOC + "json/items");

		for (final File file : dir.listFiles()) {
			try (final BufferedReader parse = new BufferedReader(
					new FileReader(file))) {
				// System.out.println("Loading: "+file.getName());
				if (!file.getName().contains("DS_Store")) {
					final PlayerItem table = gson.fromJson(parse,
							PlayerItem.class);
					ITEMS.put(table.getId(), table);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

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
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword")
				|| itemName.contains("aradomin sword")
				|| itemName.contains("ara sword") || itemName.contains("2h")
				|| itemName.contains("spear")) {
			return true;
		}
		switch (itemId) {
		case 6724:
		case 11838:
		case 4153:
		case 6528:
		case 10887:
		case 11777:
		case 15070:
		case 15084:
		case 15102:
		case 15106:
		case 15107:
		case 15167:
			return true;
		}
		return false;
	}

	public static boolean addSpecialBar(int weapon) {
		switch (weapon) {

		case 4151: // whip
		case 15137: // Tent whip
		case 859: // magic bows
		case 861:
		case 12926:
		case 11235:
		case 4587:
		case 3204:
		case 1377:
		case 4153:
		case 1249:
		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long:
		case 11730:
		case 11696:
		case 10887:
		case 15167:
		case 15171:
		case 11802:
		case 11804:
		case 11806:
		case 11808:
		case 1434:
			return true;
		default:
			return false;
		}
	}

	public static int getBlockEmote(String itemName, int itemID) {
		if (itemName.contains("defender"))
			return 4177;
		if (itemName.contains("2h"))
			return 7050;
		if (itemName.contains("book")
				|| (itemName.contains("wand") || (itemName.contains("staff"))))
			return 420;
		if (itemName.contains("shield"))
			return 1156;
		switch (itemID) {
		case 4755:
			return 2063;
		case 15241:
			return 12156;
		case 13899:
			return 13042;
		case 18355:
			return 13046;
		case 14484:
			return 397;
		case 11716:
			return 12008;
		case 4153:
			return 1666;
		case 4151:
			return 1659;
		case 15137:
			return 1659;
		case 15486:
			return 12806;
		case 18349:
			return 12030;
		case 18353:
			return 13054;
		case 18351:
			return 13042;

		case 11802:
		case 11806:
		case 11808:
		case 11804:
		case 11838:
		case 15167:
			return 7050;
		case -1:
			return 424;
		default:
			return 0;
		}
	}

	public static int[] getWepAnim(String weaponName, int itemID) {
		weaponName = weaponName.toLowerCase();
		int[] combatAnims = new int[5];
		combatAnims[0] = 0;
		combatAnims[1] = 0;
		combatAnims[2] = 0;
		combatAnims[3] = 0;
		combatAnims[4] = getBlockEmote(weaponName, itemID);
		if (weaponName.contains("sword") && !weaponName.contains("training")) {
			combatAnims[0] = 451;
			combatAnims[1] = 451;
			combatAnims[2] = 451;
			combatAnims[3] = 451;
		}
		if (weaponName.contains("dart")) {
			combatAnims[0] = 6600;
			combatAnims[1] = 6600;
			combatAnims[2] = 582;
			combatAnims[3] = 6600;
		}
		if (weaponName.contains("knife") || weaponName.contains("javelin")
				|| weaponName.contains("thrownaxe")) {
			combatAnims[0] = 806;
			combatAnims[1] = 806;
			combatAnims[2] = 806;
			combatAnims[3] = 806;
		}
		if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
			combatAnims[0] = 4230;
			combatAnims[1] = 4230;
			combatAnims[2] = 4230;
			combatAnims[3] = 4230;
		}
		if (weaponName.contains("halberd")) {
			combatAnims[0] = 440;
			combatAnims[1] = 440;
			combatAnims[2] = 440;
			combatAnims[3] = 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			combatAnims[0] = 402;
			combatAnims[1] = 402;
			combatAnims[2] = 402;
			combatAnims[3] = 402;
		}
		if (weaponName.endsWith("dagger")) {
			combatAnims[0] = 412;
			combatAnims[1] = 412;
			combatAnims[2] = 412;
			combatAnims[3] = 412;
		}

		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("aradomin sword")
				|| weaponName.contains("ara sword")) {
			combatAnims[0] = 7046;
			combatAnims[1] = 7055;
			combatAnims[2] = 7045;
			combatAnims[3] = 7045;
		}
		if (weaponName.contains("dharok")) {
			combatAnims[0] = 2067;
			combatAnims[1] = 2067;
			combatAnims[2] = 2067;
			combatAnims[3] = 2066;
		}

		if (weaponName.contains("karil")) {
			combatAnims[0] = 2075;
			combatAnims[1] = 2075;
			combatAnims[2] = 2075;
			combatAnims[3] = 2075;
		}
		if (weaponName.contains("bow") && !weaponName.contains("'bow")) {
			combatAnims[0] = 426;
			combatAnims[1] = 426;
			combatAnims[2] = 426;
			combatAnims[3] = 426;
		}
		if (weaponName.contains("'bow")) {
			combatAnims[0] = 4230;
			combatAnims[1] = 4230;
			combatAnims[2] = 4230;
			combatAnims[3] = 4230;
		}
		if (weaponName.contains("hasta")) {
			combatAnims[0] = 4198;
			combatAnims[1] = 4198;
			combatAnims[2] = 4198;
			combatAnims[3] = 4198;
		}
		switch (itemID) { // if you don't want to use
		// strings
		case 9703:
			combatAnims[0] = 412;
			combatAnims[1] = 412;
			combatAnims[2] = 412;
			combatAnims[3] = 412;
			break;

		case 6522:
			combatAnims[0] = 2614;
			combatAnims[1] = 2614;
			combatAnims[2] = 2614;
			combatAnims[3] = 2614;
			break;
		case 10034:
		case 10033:
			combatAnims[0] = 2779;
			combatAnims[1] = 2779;
			combatAnims[2] = 2779;
			combatAnims[3] = 2779;
			break;

		case 8004:
		case 7960:
			combatAnims[0] = 2075;
			combatAnims[1] = 2075;
			combatAnims[2] = 2075;
			combatAnims[3] = 2075;
			break;

		case 4153: // granite maul
			combatAnims[0] = 1665;
			combatAnims[1] = 1665;
			combatAnims[2] = 1665;
			combatAnims[3] = 1665;
			break;
		case 4726: // guthan
			combatAnims[0] = 2080;
			combatAnims[1] = 2080;
			combatAnims[2] = 2080;
			combatAnims[3] = 2080;
			break;
		case 4747: // torag
			combatAnims[0] = 0x814;
			combatAnims[1] = 0x814;
			combatAnims[2] = 0x814;
			combatAnims[3] = 0x814;
			break;
		case 4710: // ahrim
			combatAnims[0] = 406;
			combatAnims[1] = 406;
			combatAnims[2] = 406;
			combatAnims[3] = 406;
			break;
		case 4755: // verac
			combatAnims[0] = 2062;
			combatAnims[1] = 2062;
			combatAnims[2] = 2062;
			combatAnims[3] = 2062;
			break;
		case 4734: // karil
			combatAnims[0] = 2075;
			combatAnims[1] = 2075;
			combatAnims[2] = 2075;
			combatAnims[3] = 2075;
			break;
		case 4151:
			combatAnims[0] = 1658;
			combatAnims[1] = 1658;
			combatAnims[2] = 1658;
			combatAnims[3] = 1658;
			break;
		case 15137:
			combatAnims[0] = 1658;
			combatAnims[1] = 1658;
			combatAnims[2] = 1658;
			combatAnims[3] = 1658;
			break;
		case 6528:
			combatAnims[0] = 2661;
			combatAnims[1] = 2661;
			combatAnims[2] = 2661;
			combatAnims[3] = 2661;
			break;
		case 10887:
			combatAnims[0] = 5865;
			combatAnims[1] = 5865;
			combatAnims[2] = 5865;
			combatAnims[3] = 5865;
			break;
		}

		return combatAnims;
	}

	public static int[] getPlayerAnimIndex(String weaponName, int itemID) {

		int[] anims = new int[7];
		anims[0] = 0;// playerStandIndex
		anims[1] = 0;// playerTurnIndex
		anims[2] = 0;// playerWalkIndex
		anims[3] = 0;// playerTurn180Index
		anims[4] = 0;// playerTurn90CWIndex
		anims[5] = 0;// playerTurn90CCWIndex
		anims[6] = 0;// playerRunIndex
		weaponName = weaponName.toLowerCase();
		if (weaponName.contains("halberd") || weaponName.contains("hasta")
				|| weaponName.contains("guthan")
				|| weaponName.contains("sceptre")) {
			anims[0] = 809;
			anims[2] = 1146;
		}
		if (weaponName.contains("sled")) {
			anims[0] = 1461;
			anims[2] = 1468;
			anims[6] = 1467;
		}
		if (weaponName.contains("dharok")) {
			anims[0] = 0x811;
			anims[2] = 2064;
		}
		if (weaponName.contains("ahrim")) {
			anims[0] = 809;
			anims[2] = 1146;
			anims[6] = 1210;
		}
		if (weaponName.contains("verac")) {
			anims[0] = 1832;
			anims[2] = 1830;
			anims[6] = 1831;
		}
		if (weaponName.contains("wand") || weaponName.contains("staff")) {
			anims[0] = 809;
			anims[6] = 1210;
			anims[2] = 1146;
		}
		if (weaponName.contains("karil")) {
			anims[0] = 2074;
			anims[2] = 2076;
			anims[6] = 2077;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("saradomin sw")) {
			anims[0] = 7047;
			anims[2] = 7046;
			anims[6] = 7039;
			anims[1] = 7044;
			anims[3] = 7044;
			anims[4] = 7044;
			anims[5] = 7044;
		}
		if (weaponName.contains("bow")) {
			anims[0] = 808;
			anims[2] = 819;
			anims[6] = 824;
		}

		switch (itemID) {
		case 4151:
			anims[2] = 1660;
			anims[6] = 1661;
			break;
		case 15137:
			anims[2] = 1660;
			anims[6] = 1661;
			break;
		case 8004:
		case 7960:
			anims[0] = 2065;
			anims[2] = 2064;
			break;
		case 6528:
			anims[0] = 0x811;
			anims[2] = 2064;
			anims[6] = 1664;
			break;
		case 4153:
			anims[0] = 1662;
			anims[2] = 1663;
			anims[6] = 1664;
			break;
		case 10887:
			anims[0] = 5869;
			anims[2] = 5867;
			anims[6] = 5868;
			break;
		case 11802:
		case 11804:
		case 11806:
		case 11808:
		case 11838:
		case 15167:
			anims[0] = 7047;
			anims[2] = 7046;
			anims[6] = 7039;
			anims[1] = 7044;
			anims[3] = 7044;
			anims[4] = 7044;
			anims[5] = 7044;
			break;
		case 1305:
		case 15084:
		case 15171:
			anims[0] = 809;
			break;
		}
		return anims;
	}

	public static String itemType(int item, String itemName) {
		if (Item.playerCapes(item) || itemName.contains("cape")) {
			return "cape";
		}
		if (Item.playerBoots(item) || itemName.contains("boots")) {
			return "boots";
		}
		if (Item.playerGloves(item) || itemName.contains("gloves")) {
			return "gloves";
		}
		if (Item.playerShield(item) || itemName.contains("shield")) {
			return "shield";
		}
		if (Item.playerAmulet(item) || itemName.contains("amulet")) {
			return "amulet";
		}
		if (Item.playerArrows(item) || itemName.contains("arrows")) {
			return "arrows";
		}
		if (Item.playerRings(item) || itemName.contains("ring")) {
			return "ring";
		}
		if (Item.playerHats(item) || itemName.contains("hat")) {
			return "hat";
		}
		if (Item.playerLegs(item) || itemName.contains("legs")) {
			return "legs";
		}
		if (Item.playerBody(item) || itemName.contains("body")) {
			return "body";
		}
		return "weapon";
	}

	public static int getAttackDelay(String s, int itemID) {
		switch (itemID) {
		case 11235:
			return 9;
		case 11730:
			return 4;
		case 15167:
			return 3;
		case 6528:
			return 7;
		case 10033:
		case 10034:
			return 5;
		case 9703:
			return 2;
		}
		if (s.endsWith("greataxe"))
			return 7;
		else if (s.equals("torags hammers"))
			return 5;
		else if (s.equals("barrelchest anchor"))
			return 7;
		else if (s.equals("guthans warspear"))
			return 5;
		else if (s.equals("veracs flail"))
			return 5;
		else if (s.equals("ahrims staff"))
			return 6;
		else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix")
					|| s.contains("saradomian") || s.contains("slayer")
					|| s.contains("ancient"))
				return 4;
			else
				return 5;
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("aril"))
				return 4;
			else if (s.contains("Ogre"))
				return 8;
			else if (s.contains("short") || s.contains("hunt")
					|| s.contains("sword"))
				return 4;
			else if (s.contains("long") || s.contains("crystal"))
				return 6;
			else if (s.contains("'bow"))
				return 7;

			return 5;
		} else if (s.contains("dagger"))
			return 4;
		else if (s.contains("godsword") || s.contains("2h"))
			return 6;
		else if (s.contains("longsword"))
			return 5;
		else if (s.contains("sword"))
			return 4;
		else if (s.contains("scimitar"))
			return 4;
		else if (s.contains("mace"))
			return 5;
		else if (s.contains("battleaxe"))
			return 6;
		else if (s.contains("pickaxe"))
			return 5;
		else if (s.contains("thrownaxe"))
			return 5;
		else if (s.contains("axe"))
			return 5;
		else if (s.contains("warhammer"))
			return 6;
		else if (s.contains("2h"))
			return 7;
		else if (s.contains("spear"))
			return 5;
		else if (s.contains("claw"))
			return 4;
		else if (s.contains("halberd"))
			return 7;
		else if (s.equals("granite maul"))
			return 7;
		else if (s.equals("toktz-xil-ak"))// sword
			return 4;
		else if (s.equals("tzhaar-ket-em"))// mace
			return 5;
		else if (s.equals("tzhaar-ket-om"))// maul
			return 7;
		else if (s.equals("toktz-xil-ek"))// knife
			return 4;
		else if (s.equals("toktz-xil-ul"))// rings
			return 4;
		else if (s.equals("toktz-mej-tal"))// staff
			return 6;
		else if (s.contains("whip"))
			return 4;
		else if (s.contains("dart"))
			return 3;
		else if (s.contains("knife"))
			return 3;
		else if (s.contains("javelin"))
			return 6;
		return 5;
	}

	public static int[] getRequirements(String itemName, int itemId) {
		int[] reqLvl = new int[5];
		reqLvl[0] = 0;// attack
		reqLvl[1] = 0;// defence
		reqLvl[2] = 0;// strength
		reqLvl[3] = 0;// range
		reqLvl[4] = 0;// magic
		reqLvl[0] = reqLvl[1] = reqLvl[2] = reqLvl[3] = reqLvl[4] = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				reqLvl[4] = 20;
				reqLvl[0] = 40;
			} else {
				reqLvl[4] = 20;
				reqLvl[1] = 20;
			}
		}
		if (itemName.contains("slayer helmet")) {
			reqLvl[1] = 10;
		}
		if (itemName.contains("initiate")) {
			reqLvl[1] = 20;
		}
		if (itemName.contains("varrock")) {
			reqLvl[1] = 65;
		}
		if (itemName.contains("Dragon defender")) {
			reqLvl[1] = 60;
		}
		if (itemName.contains("serpentine")) {
			reqLvl[1] = 75;
		}
		if (itemName.contains("magma")) {
			reqLvl[1] = 75;
		}
		if (itemName.contains("tanzanite")) {
			reqLvl[1] = 75;
		}
		if (itemName.contains("infinity")) {
			reqLvl[4] = 50;
			reqLvl[1] = 25;
		}
		if (itemName.contains("splitbark")) {
			reqLvl[4] = 40;
			reqLvl[1] = 40;
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("d'hide"))
				reqLvl[3] = 70;
			if (itemName.contains("body"))
				reqLvl[1] = 40;

		}
		if (itemName.contains("Green")) {
			if (itemName.contains("hide")) {
				reqLvl[3] = 40;
				if (itemName.contains("body"))
					reqLvl[1] = 40;

			}
		}
		if (itemName.contains("Blue")) {
			if (itemName.contains("hide")) {
				reqLvl[3] = 50;
				if (itemName.contains("body"))
					reqLvl[1] = 40;

			}
		}
		if (itemName.contains("Red")) {
			if (itemName.contains("hide")) {
				reqLvl[3] = 60;
				if (itemName.contains("body"))
					reqLvl[1] = 40;

			}
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("hide")) {
				reqLvl[3] = 70;
				if (itemName.contains("body"))
					reqLvl[1] = 40;

			}
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				reqLvl[0] = reqLvl[1] = 1;
			}

		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				reqLvl[0] = reqLvl[1] = 1;
			}

		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				reqLvl[0] = reqLvl[1] = 5;
			}

		}
		if (itemName.contains("black")) {
			if (!itemName.contains("cavalier") && !itemName.contains("knife")
					&& !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")) {
				reqLvl[0] = reqLvl[1] = 10;
			}

		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				reqLvl[0] = reqLvl[1] = 20;
			}

		}
		if (itemName.contains("adamant") || itemName.contains("adam")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				reqLvl[0] = reqLvl[1] = 30;
			}

		}
		if (itemName.contains("rock-shell")) {
			reqLvl[1] = 40;
		}
		if (itemName.contains("zamorak")) {
			if (!itemName.contains("robe") && !itemName.contains("mitre")
					&& !itemName.contains("stole")
					&& !itemName.contains("godsword")) {
				reqLvl[1] = 40;
			}

		}
		if (itemName.contains("saradomin")) {
			if (!itemName.contains("robe") && !itemName.contains("mitre")
					&& !itemName.contains("stole")
					&& !itemName.contains("godsword")) {
				reqLvl[1] = 40;
			}

		}
		if (itemName.contains("guthix")) {
			if (!itemName.contains("robe") && !itemName.contains("mitre")
					&& !itemName.contains("stole")
					&& !itemName.contains("godsword")) {
				reqLvl[1] = 40;
			}

		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("gloves") && !itemName.contains("knife")
					&& !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")) {
				reqLvl[0] = reqLvl[1] = 40;
			}

		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				reqLvl[0] = reqLvl[1] = 60;

			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				reqLvl[1] = 70;
			} else {
				reqLvl[3] = 70;
			}

		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				reqLvl[4] = 70;
				reqLvl[0] = 70;
			} else {
				reqLvl[4] = 70;
				reqLvl[1] = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				reqLvl[3] = 70;
			} else {
				reqLvl[3] = 70;
				reqLvl[1] = 70;
			}
		}
		if (itemName.contains("godsword")) {
			reqLvl[0] = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			reqLvl[1] = 60;
		}
		if (itemName.contains("Initiate")) {
			reqLvl[1] = 20;
		}
		if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				reqLvl[0] = 70;
				reqLvl[2] = 70;
			} else if (itemName.contains("axe")) {
				reqLvl[0] = 70;
				reqLvl[2] = 70;
			} else if (itemName.contains("warspear")) {
				reqLvl[0] = 70;
				reqLvl[2] = 70;
			} else if (itemName.contains("flail")) {
				reqLvl[0] = 70;
				reqLvl[2] = 70;
			} else {
				reqLvl[1] = 70;
			}
		}
		if (itemName.contains("arcane horror")) {
			reqLvl[1] = 90;
			reqLvl[4] = 90;
		}
		if (itemName.contains("artifact")) {
			reqLvl[1] = 90;
		}
		if (itemName.contains("draconic")) {
			reqLvl[1] = 90;
			reqLvl[3] = 90;
		}

		switch (itemId) {
		case 11720:
		case 11718:
		case 11722:
			reqLvl[1] = 70;
			reqLvl[3] = 70;
			break;
		case 10887:
			reqLvl[2] = 60;
			break;
		case 2497:
		case 2491:
			reqLvl[3] = 70;
			break;
		case 6528:
			reqLvl[2] = 60;
			break;
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			reqLvl[0] = 42;
			reqLvl[3] = 42;
			reqLvl[2] = 42;
			reqLvl[4] = 42;
			reqLvl[1] = 42;
			break;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			reqLvl[1] = 40;
			break;
		case 11235:
		case 6522:
			reqLvl[3] = 60;
			break;
		case 6524:
			reqLvl[1] = 60;
			break;
		case 11284:
			reqLvl[1] = 75;
			break;
		case 6889:
		case 6914:
			reqLvl[4] = 60;
			break;
		case 861:
			reqLvl[3] = 50;
			break;
		case 10828:
			reqLvl[1] = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			reqLvl[1] = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			reqLvl[1] = 40;
			break;

		case 11283:
			reqLvl[1] = 75;
			break;

		case 851:
		case 853:
			reqLvl[3] = 30;
			break;

		case 847:
		case 849:
			reqLvl[3] = 20;
			break;

		case 845:
		case 843:
			reqLvl[3] = 5;
			break;

		case 5698:
			reqLvl[0] = 60;
			break;

		case 7462:
		case 7461:
			reqLvl[1] = 40;
			break;
		case 8846:
			reqLvl[1] = 5;
			break;
		case 8847:
			reqLvl[1] = 10;
			break;
		case 8848:
			reqLvl[1] = 20;
			break;
		case 8849:
			reqLvl[1] = 30;
			break;
		case 8850:
			reqLvl[1] = 40;
			break;

		case 7460:
			reqLvl[1] = 40;
			break;

		case 837:
			reqLvl[3] = 61;
			break;

		case 4151:
			reqLvl[0] = 70;
			break;
		case 15137:
			reqLvl[0] = 75;
			break;

		case 6724:
			reqLvl[3] = 60;
			break;
		case 4153:
			reqLvl[0] = 50;
			reqLvl[2] = 50;
			break;
		case 150045:
			reqLvl[1] = 60;
			break;
		case 15046:
		case 15047:
		case 15048:
			reqLvl[1] = 78;
			break;
		case 15049:
			reqLvl[0] = 78;
			break;
		case 15050:
		case 15051:
			reqLvl[1] = 78;
			break;
		case 15052:
		case 15053:
			reqLvl[0] = 78;
			break;
		case 15054:
		case 15055:
		case 15056:
			reqLvl[1] = 78;
			reqLvl[3] = 78;
			break;
		case 15057:
		case 15058:
			reqLvl[3] = 78;
			break;
		case 15059:
		case 15060:
		case 15061:
			reqLvl[1] = 78;
			reqLvl[4] = 78;
			break;
		case 15062:
			reqLvl[4] = 78;
			break;
		case 15010:
		case 15011:
		case 15012:
		case 15013:
		case 15014:
		case 15015:
		case 15021:
		case 15022:
			reqLvl[1] = 99;
			break;
		case 15016:
		case 15017:
		case 15018:
		case 15019:
		case 15020:
		case 15023:
		case 15024:
			reqLvl[0] = 99;
			break;
		case 15063:
		case 15064:
			reqLvl[0] = 80;
			break;
		case 15065:
			reqLvl[0] = 80;
			reqLvl[2] = 80;
			break;
		case 15067:
			reqLvl[3] = 80;
			break;
		case 15068:
		case 15069:
			reqLvl[1] = 80;
			break;
		case 15070:
			reqLvl[3] = 90;
			break;
		case 15072:
			reqLvl[1] = 40;
			break;
		case 15073:
			reqLvl[1] = 40;
			break;
		case 15074:
		case 15075:
		case 15076:
		case 15077:
			reqLvl[1] = 75;
			break;
		case 15083:
			reqLvl[4] = 75;
			reqLvl[0] = 60;
			break;
		case 15084:
			reqLvl[0] = 95;
			break;
		case 15085:
			reqLvl[3] = 70;
			break;
		case 15086:
			reqLvl[0] = 99;
			reqLvl[1] = 99;
			reqLvl[2] = 99;
			reqLvl[4] = 99;
			reqLvl[3] = 99;
			break;
		case 15088:
			reqLvl[1] = 70;
			break;
		case 15089:
			reqLvl[0] = 10;
			break;
		case 15093:
			reqLvl[4] = 50;
			break;
		case 15167:
			reqLvl[0] = 75;
			break;
		case 15171:
			reqLvl[0] = 75;
			break;
		case 15172:
			reqLvl[4] = 77;
			break;
		}

		return reqLvl;
	}

	public static final void dump() {
		ItemList[] items = Server.itemHandler.ItemList;
		for (ItemList item : items) {
			if (item != null) {
				try {
					System.out.println("Dumping: " + item.itemId);
					PrintWriter writer = new PrintWriter(
							Config.DATA_LOC + "json/items/" + item.itemId
									+ ".json", "UTF-8");
					int[] combatAnims = getWepAnim(item.itemName, item.itemId);
					int[] itemAnims = getPlayerAnimIndex(item.itemName,
							item.itemId);
					int[] lvlReqs = getRequirements(item.itemName, item.itemId);
					writer.println("{");
					writer.println("\t\"id\": " + item.itemId + ",");
					writer.println("\t\"name\": \"" + item.itemName + "\",");
					writer.println("\t\"description\": \""
							+ item.itemDescription.replaceAll("\"", "\'")
							+ "\",");
					writer.println("\t\"value\": \"" + (int) item.ShopValue
							+ "\",");
					int sum = 0;
					for (int i = 0; i < item.Bonuses.length; i++) {
						sum += item.Bonuses[i];
					}

					String itemType = itemType(item.itemId,
							item.itemName.toLowerCase());
					if (item.itemId == 2571)
						System.out.println("Item :" + item.itemId + " "
								+ itemType);
					if (item.itemDescription.toLowerCase().contains("bank")) {
						itemType = "weapon";
					}
					if (sum != 0 || itemType != "weapon") {
						writer.println("\t\"equipSlot\": " + "\"" + itemType
								+ "\"" + ",");
						writer.println("\t\"bonuses\" : [");
						for (int bonus = 0; bonus < item.Bonuses.length; bonus++)
							writer.println("\t\t"
									+ item.Bonuses[bonus]
									+ (bonus < item.Bonuses.length - 1 ? ","
											: ""));
						writer.println("\t],");
						writer.println("\t\"attackDelay\": \""
								+ getAttackDelay(item.itemName, item.itemId)
								+ "\",");
						if (!item.itemDescription.toLowerCase()
								.contains("bank")) {
							writer.println("\t\"levelRequirements\" : [");
							for (int i = 0; i < lvlReqs.length; i++)
								writer.println("\t\t" + lvlReqs[i]
										+ (i < lvlReqs.length - 1 ? "," : ""));
							writer.println("\t],");
						}

					}
					if (itemAnims[0] != 0
							&& !item.itemDescription.toLowerCase().contains(
									"bank")) {
						writer.println("\t\"animations\" : [");
						for (int anims = 0; anims < itemAnims.length; anims++)
							writer.println("\t\t" + itemAnims[anims]
									+ (anims < itemAnims.length - 1 ? "," : ""));
						writer.println("\t],");
					}
					if (combatAnims[0] != 0
							&& !item.itemDescription.toLowerCase().contains(
									"bank")) {
						writer.println("\t\"combatAnimations\" : [");
						for (int ac = 0; ac < combatAnims.length; ac++)
							writer.println("\t\t" + combatAnims[ac]
									+ (ac < combatAnims.length - 1 ? "," : ""));
						writer.println("\t],");
					}
					if (combatAnims[0] != 0
							&& !item.itemDescription.toLowerCase().contains(
									"bank")) {
						writer.println("\t\"twoHanded\": "
								+ is2handed(item.itemName, item.itemId) + ",");
						writer.println("\t\"special\": "
								+ addSpecialBar(item.itemId) + ",");
					}

					writer.println("\t\"stackable\": "
							+ Item.itemStackable[item.itemId]);
					writer.println("}");
					writer.close();
				} catch (Exception e) {

				}
			}
		}
	}
}
