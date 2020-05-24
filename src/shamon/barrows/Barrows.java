package shamon.barrows;

import shamon.barrows.impl.MoundHandler;
import shamon.barrows.impl.OpenTunnelDoorHandler;
import shamon.barrows.impl.TombObjectHandler;
import shamon.barrows.impl.TombStairsHandler;
import wind.model.players.Client;

public class Barrows {
	/**
	 * Controls the mazes tunnels and rooms.
	 */
	private final BarrowsMaze maze;

	/**
	 * Controls the loot.
	 */
	private final BarrowsChest chest;

	/**
	 * Randomizes the puzzle on doors.
	 */
	private final BarrowsPuzzleDisplay puzzle;

	/**
	 * Tracks kill count, brothers killed, and NPC spawns.
	 */
	private final BarrowsNpcController npcController;

	private final OpenTunnelDoorHandler doorHandler;

	private final MoundHandler moundHandler;

	private final TombObjectHandler tombObjectHandler;

	private final TombStairsHandler tombStairsHandler;

	public Barrows(Client client) {
		this.maze = new BarrowsMaze(client);
		this.chest = new BarrowsChest(client);
		this.npcController = new BarrowsNpcController(client);
		this.puzzle = new BarrowsPuzzleDisplay();

		this.doorHandler = new OpenTunnelDoorHandler(client);
		this.moundHandler = new MoundHandler(client);
		this.tombObjectHandler = new TombObjectHandler(client);
		this.tombStairsHandler = new TombStairsHandler(client);

		this.resetGame();
	}

	public void resetGame() {
		// Maze is reset on teleporting into tunnels.
		maze.randomizeSpawnPoint();
		chest.reset();
		npcController.reset();
		puzzle.reset();
	}

	public BarrowsNpcController getNpcController() {
		return npcController;
	}

	public BarrowsMaze getMaze() {
		return maze;
	}

	public BarrowsPuzzleDisplay getPuzzle() {
		return puzzle;
	}

	public BarrowsChest getChest() {
		return chest;
	}

	public OpenTunnelDoorHandler getDoorHandler() {
		return doorHandler;
	}

	public MoundHandler getMoundHandler() {
		return moundHandler;
	}

	public TombObjectHandler getTombObjectHandler() {
		return tombObjectHandler;
	}

	public TombStairsHandler getTombStairsHandler() {
		return tombStairsHandler;
	}
}
