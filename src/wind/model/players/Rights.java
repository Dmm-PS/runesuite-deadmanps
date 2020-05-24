package wind.model.players;

/*
 * Author: Sunny++
 * Date: 2/18/15
 * Credits: Lare96
 */



public enum Rights {

	/*
	 * Protocol Value, Server value
	 */
	PLAYER(0, 0),
	DONATOR(1, 1),
	SUPER_DONATOR(2, 2),
	EXTREME_DONATOR(3, 3),
	GOD_OF_ALL_DONATORS(4, 4),
	SUPPORT(5, 5),
	MODERATOR(6, 6),
	ADMINISTRATOR(7, 7),
	ADVISOR(8, 8),
	DEVELOPER(9, 9),
	OWNER(10, 10);
	/*
	 * value client-sided
	 */
	private final int protocolValue;
	/*
	 * rights server-sided
	 */
	private final int values;

	/**
	 * Create a new {@link Rights}.
	 *
	 * @param protocolValue
	 *            the value of this rank as seen by the protocol.
	 * @param value
	 *            the value of this rank as seen by the server.
	 */
	private Rights(int protocalValue, int values) {
		this.protocolValue = protocalValue;
		this.values = values;
	}

	/**
	 * Determines if this right is greater than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is greater, {@code false} otherwise.
	 */
	public final boolean greater(Rights other) {
		if (ordinal() >= DONATOR.getValues()){
			if (other.ordinal() >= SUPPORT.getValues()){
					return false;
				}
			}
		
		return ordinal() > other.ordinal();
	}

	public final boolean greaterOrEqual(Rights other) {
		if (ordinal() >= DONATOR.getValues()){
			if (other.ordinal() >= SUPPORT.getValues()){
				if (other.ordinal() < DONATOR.getValues()){
					return false;
				}
			}
		}
		return ordinal() >= other.ordinal();
	}
	
	/**
	 * Determines if this right is lesser than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean less(Rights other) {
		return ordinal() < other.ordinal();
	}
	
	public final boolean lessOrEqual(Rights other) {
		return ordinal() <= other.ordinal();
	}

	/**
	 * Determines if this right is equal in power to the argued right. Please
	 * note that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is equal, {@code false} otherwise.
	 */
	public final boolean equal(Rights other) {
		return values == other.values;
	}

	/**
	 * Gets the value of this rank as seen by the protocol.
	 *
	 * @return the protocol value of this rank.
	 */
	public final int getProtocolValue() {
		return protocolValue;
	}

	/**
	 * Gets the value of this rank as seen by the server.
	 *
	 * @return the server value of this rank.
	 */
	public final int getValues() {
		return values;
	}

	public static Rights forValue(int value) {
		for (Rights r: Rights.values()) {
			if (r.values == value) {
				return r;
			}
		}
		return Rights.PLAYER;
	}
	
}
