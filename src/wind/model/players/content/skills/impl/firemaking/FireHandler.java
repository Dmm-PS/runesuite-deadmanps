package wind.model.players.content.skills.impl.firemaking;

public class FireHandler {

	public static boolean[] isFiring = new boolean[25];

	public static long lastSkillingAction;

	public static void resetSkillingVariables() {
		for (int skill = 0; skill < isFiring.length; skill++) {
			isFiring[skill] = false;
		}
	}
}