package wind.model.players.combat.melee;

import wind.model.players.*;
import wind.util.Misc;

public class MeleeMaxHit {

	@SuppressWarnings("unused")
	public static double calculateBaseDamage(Client c, boolean special) {

		double base = 0;
		double multiplier = 1;
		int attBonus = c.playerBonus[10]; // attack
		int attack = c.playerLevel[0]; // attack
		int strBonus = c.playerBonus[10]; // strength
		int strength = c.playerLevel[2]; // strength
		int defBonus = c.playerBonus[10]; // defense
		int defense = c.playerLevel[1]; // defense
		int attlvlForXP = c.getLevelForXP(c.playerXP[0]); // attack
		int strlvlForXP = c.getLevelForXP(c.playerXP[2]); // strength
		int deflvlForXP = c.getLevelForXP(c.playerXP[1]); // defense
		int lvlForXP = c.getLevelForXP(c.playerXP[2]);

		double effective = getEffectiveStr(c);
		double specialBonus = getSpecialStr(c);
		double strengthBonus = c.playerBonus[10];

		base = (13 + effective + (strengthBonus / 8) + ((effective * strengthBonus * multiplier) / 64)) / 10;

		if (c.usingSpecial) {
			base = (base * specialBonus);
		}
		if (hasObsidianEffect(c) || hasVoid(c)) {
			base = (base * 1.2);
		}
		if (c.getEquipment().isWearingDharoks(c)) {
			int hpLost = c.getLevelForXP(c.playerXP[c.playerHitpoints]) - c.playerLevel[c.playerHitpoints];
			if(c.isAttackingNpc) {
				multiplier = 2.25;
			} else {
				multiplier = 2.18;
			}
			base += (hpLost * 0.35) * multiplier;
		}
		if (c.prayerActive[1]) {
			strength += (int) (lvlForXP * .05);
		} else if (c.prayerActive[6]) {
			strength += (int) (lvlForXP * .1);
		} else if (c.prayerActive[14]) {
			strength += (int) (lvlForXP * .15);
		} else if (c.prayerActive[24]) {
			strength += (int) (lvlForXP * .18);
		} else if (c.prayerActive[25]) {
			strength += (int) (lvlForXP * .23);
		}
		return Math.floor(base);
	}

	public static double getEffectiveStr(Client c) {
		return ((c.playerLevel[2]) * getPrayerStr(c)) + getStyleBonus(c);
	}

	public static int getStyleBonus(Client c) {
		return c.fightMode == 2 ? 3 : c.fightMode == 3 ? 1
				: c.fightMode == 4 ? 3 : 0;
	}

	public static double getPrayerStr(Client c) {
		if (c.prayerActive[1])
			return 1.05;
		else if (c.prayerActive[6])
			return 1.1;
		else if (c.prayerActive[14])
			return 1.15;
		else if (c.prayerActive[24])
			return 1.18;
		else if (c.prayerActive[25])
			return 1.23;
		return 1;
	}

	public static final double[][] special = { { 5698, 1.1 }, { 5680, 1.1 },
			{ 1231, 1.15 }, { 1215, 1.15 }, { 5680, 1.15 }, { 5698, 1.15 },
			{ 3204, 1.10 }, { 1305, 1.15 }, { 1434, 1.45 }, { 11802, 1.375 },
			{ 11804, 1.21 }, { 11806, 1.10 }, { 11808, 1.10 }, { 861, 1.1 },
			{ 4151, 1.1 }, { 10887, 1.2933 }, {12926, 1.15} };

	public static double getSpecialStr(Client c) {
		for (double[] slot : special) {
			if (c.playerEquipment[3] == slot[0])
				return slot[1];
		}
		return 1;
	}

	public static final int[] obsidianWeapons = { 746, 747, 6523, 6525, 6526,
			6527, 6528 };

	public static boolean hasObsidianEffect(Client c) {
		if (c.playerEquipment[2] != 11128)
			return false;

		for (int weapon : obsidianWeapons) {
			if (c.playerEquipment[3] == weapon)
				return true;
		}
		return false;
	}

	public static boolean hasVoid(Client c) {
		return c.playerEquipment[c.playerHat] == 11665
				&& c.playerEquipment[c.playerLegs] == 8840
				&& c.playerEquipment[c.playerChest] == 8839
				&& c.playerEquipment[c.playerHands] == 8842;
	}

	public static int bestMeleeDef(Client c) {
		if (c.playerBonus[5] > c.playerBonus[6]
				&& c.playerBonus[5] > c.playerBonus[7]) {
			return 5;
		}
		if (c.playerBonus[6] > c.playerBonus[5]
				&& c.playerBonus[6] > c.playerBonus[7]) {
			return 6;
		}
		return c.playerBonus[7] <= c.playerBonus[5]
				|| c.playerBonus[7] <= c.playerBonus[6] ? 5 : 7;
	}

	public static int calculateMeleeDefence(Client c) {
		int defenceLevel = c.playerLevel[1];
		int i = c.playerBonus[bestMeleeDef(c)];
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[24]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[25]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		if (c.playerEquipment[c.playerShield] == 12817 && Misc.random(100) > 30) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public static int bestMeleeAtk(Client c) {
		if (c.playerBonus[0] > c.playerBonus[1]
				&& c.playerBonus[0] > c.playerBonus[2]) {
			return 0;
		}
		if (c.playerBonus[1] > c.playerBonus[0]
				&& c.playerBonus[1] > c.playerBonus[2]) {
			return 1;
		}
		return c.playerBonus[2] <= c.playerBonus[1]
				|| c.playerBonus[2] <= c.playerBonus[0] ? 0 : 2;
	}

	public static int calculateMeleeAttack(Client c) {
		int attackLevel = c.playerLevel[0];
		if (c.prayerActive[2]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.05;
		} else if (c.prayerActive[7]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.1;
		} else if (c.prayerActive[15]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		} else if (c.prayerActive[24]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		} else if (c.prayerActive[25]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.2;
		}
		if (c.fullVoidMelee()) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.1;
		}
		attackLevel *= c.specAccuracy;
		int i = c.playerBonus[bestMeleeAtk(c)];
		i += c.bonusAttack;
		if (hasObsidianEffect(c) || hasVoid(c)) {
			i *= 1.30;
		}
		return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.05));
	}
}