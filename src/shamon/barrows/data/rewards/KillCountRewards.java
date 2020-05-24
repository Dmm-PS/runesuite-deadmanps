package shamon.barrows.data.rewards;

public enum KillCountRewards implements Loot {

	KEY_TOOTH(985),
	KEY_LOOP(987),
	DMED(1149),
	EASY_CASKET(2714),
	MED_CASKET(2802),
	HARD_CASKET(2775),
	ZULRAH_TELE(12938);

	private final int itemID;

	private KillCountRewards(int itemID) {
		this.itemID = itemID;
	}

	public static final KillCountRewards[] REWARDS = KillCountRewards.values();

	@Override public int getItemID() {
		return itemID;
	}
}
