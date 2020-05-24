package shamon.barrows.data;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum NpcCombatData {

	AHRIM(BarrowsConstants.AHRIMS_ID, 100, 25, 150, 150),
	DHAROK(BarrowsConstants.DHAROKS_ID, 100, 60, 150, 150),
	GUTHAN(BarrowsConstants.GUTHANS_ID, 100, 25, 150, 150),
	KARIL(BarrowsConstants.KARILS_ID, 100, 25, 150, 150),
	TORAG(BarrowsConstants.TORAGS_ID, 100, 25, 150, 150),
	VERAC(BarrowsConstants.VERACS_ID, 100, 25, 150, 150),
	CRYPT_RAT(BarrowsConstants.CRYPT_RAT_ID, 35, 6, 50, 50),
	GIANT_CRYPT_RAT(BarrowsConstants.GIANT_CRYPT_RAT_ID_1, 76, 6, 50, 50),
	GIANT_CRYPT_RAT_2(BarrowsConstants.GIANT_CRYPT_RAT_ID_2, 76, 6, 50, 50),
	GIANT_CRYPT_RAT_3(BarrowsConstants.GIANT_CRYPT_RAT_ID_3, 76, 6, 50, 50),
	CRYPT_SPIDER(BarrowsConstants.CRYPT_SPIDER_ID, 60, 6, 50, 50),
	GIANT_CRYPT_SPIDER(BarrowsConstants.GIANT_CRYPT_SPIDER_ID, 80, 6, 50, 50),
	SKELETON(BarrowsConstants.SKELETON_ID, 60, 10, 50, 50),
	SKELETON4(BarrowsConstants.SKELETON_ID_4, 60, 10, 50, 50),
	SKELETON2(BarrowsConstants.SKELETON_ID_2, 60, 10, 50, 50),
	SKELETON3(BarrowsConstants.SKELETON_ID_3, 60, 10, 50, 50),
	BLOODWORM(BarrowsConstants.BLOODWORM_ID, 45, 6, 50, 50);

	private final int npcID;
	private final int health;
	private final int maxHit;
	private final int attack;
	private final int defence;

	private NpcCombatData(int npcID, int health, int maxHit, int attack, int defence) {
		this.npcID = npcID;
		this.health = health;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
	}

	private static final Map<Integer, NpcCombatData> COMBAT_DATA = Arrays.stream(NpcCombatData.values())
			.collect(Collectors.toMap(NpcCombatData::getNpcID, Function.identity()));

	public static NpcCombatData lookup(int npcID) {
		return COMBAT_DATA.get(npcID);
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public int getNpcID() {
		return npcID;
	}
}
