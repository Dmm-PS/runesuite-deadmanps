package wind.model.players.teleport;

/**
 * Contains all the teleport types and all their data
 * 
 * @author Zack/Optimum
 *
 */

public enum TeleportType {

	MODERN_TELEPORT(new int[] { 714, -1, 0 }, 1, new int[] { -1, 308, 100 }, 3,
			new int[] { 715, -1, 0 }, 6), ANICENT_TELEPORT(new int[] { 9599,
			1681, 0 }, 5, new int[] { -1, -1, 0 }, 5, new int[] { -1, -1, 0 },
			5), TELETAB_TELEPORT(new int[] { 9597, 1680, 0 }, 2, new int[] {
			4731, -1, 0 }, 3, new int[] { 9598, -1, 0 }, 4);

	int[] startGfxAnim;
	int tickTimer1Start;
	int[] timer1GfxAnim;
	int tickTimer2Start;
	int[] timer2GfxAnim;
	int stopTimer;

	TeleportType(int[] startGfxAnim, int tickTimer1Start, int[] timer1GfxAnim,
			int tickTimer2Start, int[] timer2GfxAnim, int stopTimer) {
		this.startGfxAnim = startGfxAnim;
		this.tickTimer1Start = tickTimer1Start;
		this.timer1GfxAnim = timer1GfxAnim;
		this.tickTimer2Start = tickTimer2Start;
		this.timer2GfxAnim = timer2GfxAnim;
		this.stopTimer = stopTimer;
	}

	public int[] getStartGfxAnim() {
		return startGfxAnim;
	}

	public int getTickTimer1Start() {
		return tickTimer1Start;
	}

	public int[] getTimer1GfxAnim() {
		return timer1GfxAnim;
	}

	public int getTickTimer2Start() {
		return tickTimer2Start;
	}

	public int[] getTimer2GfxAnim() {
		return timer2GfxAnim;
	}

	public int getStopTimer() {
		return stopTimer;
	}
}