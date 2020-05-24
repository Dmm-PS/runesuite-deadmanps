package shamon.barrows.data.rewards;

public enum RegularRewardsData implements Loot {

	MIND_RUNE(558, 1, 600, 0.70),
	 CHAOS_RUNE(562, 1, 600, 0.70),
	 DEATH_RUNE(560, 1, 300, 0.60),
	 BLOOD_RUNE(565, 1, 200, 0.50),
	BOLT_RACK(4740, 1, 200, 0.40),
	SHARK(386, 1, 25, 0.50),
	ADDY_ARROW_P(891, 1, 125, 0.50),
	COINS(995, 5000, 15000, 0.50),
	RUNE_ARROW(892, 1, 75, 0.50);

	private final int itemID;
	private final int amountStart;
	private final int amountEnd;
	@SuppressWarnings("unused")
	private final double baseChance;

	private RegularRewardsData(int itemID, int amountStart, int amountEnd, double baseChance) {
		this.itemID = itemID;
		this.amountStart = amountStart;
		this.amountEnd = amountEnd;
		this.baseChance = baseChance;
	}
	private RegularRewardsData(int itemID, int amount, double baseChance) {
		this(itemID, amount, amount, baseChance);
	}

	public static final RegularRewardsData[] REWARDS = RegularRewardsData.values();

	@Override public int getItemID() {
		return itemID;
	}

	@Override public int getAmount() {
		return amountStart + (int) (Math.random() * (amountEnd - amountStart + 1));
	}

}
