package wind.model.players.teleport;

import wind.Config;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.task.Task;
import wind.task.TaskHandler;

/**
 * This class will handle all everything to do with the teleporting it will
 * check if the player is eligible to teleport and handle all the animations and
 * gfxs.
 * 
 * @author Zack/Optimum
 *
 */
public class Teleport {

	/**
	 * The player teleporting
	 */
	private Client player;

	/**
	 * The targeted x, y and h coordinates
	 */
	private int targetX, targetY, targetH;

	/**
	 * Constructor for teleporting
	 * 
	 * @param player
	 *            - the player
	 */
	public Teleport(Client player) {
		this.player = player;
	}

	/**
	 * Submits a new teleport tab type
	 * 
	 * @param tarX
	 *            - the new x coordinates
	 * @param tarY
	 *            - the new y coordinates
	 */
	public void submitTab(final int tarX, final int tarY) {
		this.targetX = tarX;
		this.targetY = tarY;
		startTeleport(TeleportType.TELETAB_TELEPORT);
	}

	/**
	 * Submits a new ancient teleport type
	 * 
	 * @param tarX
	 *            - the new x coordinates
	 * @param tarY
	 *            - the new y coordinates
	 */
	public void submitAncient(final int tarX, final int tarY) {
		this.targetX = tarX;
		this.targetY = tarY;
		startTeleport(TeleportType.ANICENT_TELEPORT);
	}

	/**
	 * Submits a new ancient teleport type
	 * 
	 * @param tarX
	 *            - the new x coordinates
	 * @param tarY
	 *            - the new y coordinates
	 * @param tarH
	 *            - the new height
	 */
	public void submitAncient(final int tarX, final int tarY, final int tarH) {
		this.targetX = tarX;
		this.targetY = tarY;
		this.targetH = tarH;
		startTeleport(TeleportType.ANICENT_TELEPORT);
	}
	
	/**
	 * Assigns a teleport type based on what magic book you have open.
	 * 
	 * @param tarX
	 * 			- the new x coordinates
	 * @param tarY
	 * 			- the new y coordinates
	 * @param tarH
	 * 			- the new height
	 */
	public void submitTeleport(final int tarX, final int tarY) {
		if (player.inZulrahShrine()) {
			player.sendMessage("The only way out of here is victory or death.");
			return;
		}
		this.targetX = tarX;
		this.targetY = tarY;
		if (player.playerMagicBook != 1)
			this.submitModern(tarX,	tarY);
		else
			this.submitAncient(tarX, tarY);
	}
	
	/**
	 * Assigns a teleport type based on what magic book you have open.
	 * 
	 * @param tarX
	 * 			- the new x coordinates
	 * @param tarY
	 * 			- the new y coordinates
	 * @param tarH
	 * 			- the new height
	 */
	public void submitTeleport(final int tarX, final int tarY, final int tarH) {
		if (player.inZulrahShrine()) {
			player.sendMessage("The only way out of here is victory or death.");
			return;
		}
		this.targetX = tarX;
		this.targetY = tarY;
		this.targetH = tarH;
		if (player.playerMagicBook != 1)
			this.submitModern(tarX,	tarY, tarH);
		else
			this.submitAncient(tarX, tarY, tarH);
	}

	/**
	 * Submits a new modern teleport
	 * 
	 * @param tarX
	 *            - the new x coordinates
	 * @param tarY
	 *            - the new y coordinates
	 */
	public void submitModern(final int tarX, final int tarY) {
		this.targetX = tarX;
		this.targetY = tarY;
		if (Config.SOUND)
			player.getPA().sendSound(player.sounds.TELEPORTM);
		startTeleport(TeleportType.MODERN_TELEPORT);
	}

	/**
	 * Submits a new modern teleport
	 * 
	 * @param tarX
	 *            - the new x coordinates
	 * @param tarY
	 *            - the new y coordinates
	 * @param tarH
	 *            - the new height
	 */
	public void submitModern(final int tarX, final int tarY, final int tarH) {
		this.targetX = tarX;
		this.targetY = tarY;
		this.targetH = tarH;
		if (Config.SOUND)
			player.getPA().sendSound(player.sounds.TELEPORTM);
		startTeleport(TeleportType.MODERN_TELEPORT);
	}

	/*
	 * TaskHandler.submit(new Task(200, true) {
	 * 
	 * @Override public void execute() { // Kill the random event npc.
	 * newNPC.isDead = true; newNPC.updateRequired = true; c.hasEvent = false;
	 * 
	 * // *** ALWAYS CANCEL TASK. this.cancel(); } });
	 */

	public void startTeleport(final TeleportType teleType) {

		if (!canTeleport(player))
			return;
		final int startStage = teleType.getTickTimer1Start();
		final int middleStage = teleType.getTickTimer2Start();
		final int stopStage = teleType.getStopTimer();
		cancelTradeAndDuel(player);
		player.stopMovement();
		player.isTeleporting = true;
		player.startAnimation(teleType.getStartGfxAnim()[0]);
		if (teleType.getStartGfxAnim()[2] == 100)
			player.gfx100(teleType.getStartGfxAnim()[1]);
		else
			player.gfx0(teleType.getStartGfxAnim()[1]);

		TaskHandler.submit(new Task(1, true) {
			int stage = 0;

			@Override
			public void execute() {
				if (stage == startStage) {
					player.startAnimation(teleType.getTimer1GfxAnim()[0]);
					if (teleType.getTimer1GfxAnim()[2] == 100)
						player.gfx100(teleType.getTimer1GfxAnim()[1]);
					else
						player.gfx0(teleType.getTimer1GfxAnim()[1]);
				}
				if (stage == middleStage) {
					player.startAnimation(teleType.getTimer2GfxAnim()[0]);
					if (teleType.getTimer2GfxAnim()[2] == 100)
						player.gfx100(teleType.getTimer2GfxAnim()[1]);
					else
						player.gfx0(teleType.getTimer2GfxAnim()[1]);
					player.getPA().movePlayer(targetX, targetY, targetH);
				}
				if (stage == stopStage) {
					player.startAnimation(65535);
					player.resetWalkingQueue();
					this.cancel();
				}
				stage++;
				if (Config.SERVER_DEBUG)
				System.out.println("Teleport Stage: " + stage);
			}

			@Override
			public void onCancel() {
				player.isTeleporting = false;
			}
		});
	}

	/**
	 * Checks to see if the player is eligible for teleporting
	 * 
	 * @param player
	 *            - The target player
	 * @return true if the player can teleport
	 */
	public boolean canTeleport(Client player) {
		if (player == null)
			return false;
		if (player.isDead)
			return false;
		if (isDueling(player))
			return false;
		if (isTeleBlocked(player))
			return false;
		if (player.isTeleporting)
			return false;
		if (wildyLevelCheck(player))
			return false;
		return true;
	}

	/**
	 * Checks to see if the player is in a duel
	 * 
	 * @param player
	 *            - The target player
	 * @return true if the player is in a duel
	 */
	public boolean isDueling(Client player) {
		if (player.duelStatus == 5) {
			player.sendMessage("You can't teleport during a duel!");
			return true;
		} else
			return false;
	}

	/**
	 * Checks if the player is teleblocked
	 * 
	 * @param player
	 *            - The target player
	 * @return true if player is teleblocked
	 */
	public boolean isTeleBlocked(Client player) {
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return true;
		} else
			return false;
	}

	/**
	 * Cancels the trade and duel for {@link player} and the opponent he is
	 * dueling / trading with
	 * 
	 * @param player
	 *            - the target player
	 */
	public void cancelTradeAndDuel(Client player) {
		if (player.duelStatus >= 1 && player.duelStatus <= 4) {
			Client o = (Client) PlayerHandler.players[player.duelingWith];
			player.duelStatus = 0;
			o.duelStatus = 0;
			o.getDuel().bothDeclineDuel();
		}
	}

	/**
	 * Checks the current player wilderness level and returns true if the player
	 * is above the max teleport wilderness level
	 * 
	 * @param player
	 *            - The target player
	 * @return if player is in wildy > 20
	 */
	public boolean wildyLevelCheck(Client player) {
		if (player.inWild() && player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			player.sendMessage("You can't teleport above level "
					+ Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return true;
		} else
			return false;
	}

}