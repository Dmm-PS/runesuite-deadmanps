package wind.model.players.combat.melee;

import wind.*;
import wind.model.players.*;
import wind.task.Task;
import wind.task.TaskHandler;

public class MeleeRequirements {

	public static int getKillerId(Client c, int playerId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {
					if (PlayerHandler.players[i]
							.withinDistance(PlayerHandler.players[playerId])) {
						if (PlayerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
							oldDamage = PlayerHandler.players[i].totalPlayerDamageDealt;
							killerId = i;
						}
					}
					PlayerHandler.players[i].totalPlayerDamageDealt = 0;
					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	public static int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return (combat1 - combat2);
		}
		if (combat2 > combat1) {
			return (combat2 - combat1);
		}
		return 0;
	}

	/*
	 * public static boolean checkReqs(Client c) {
	 * if(PlayerHandler.players[c.playerIndex] == null) { return false; } if
	 * (c.playerIndex == c.playerId) return false; Client opponent = (Client)
	 * PlayerHandler.players[c.playerIndex];
	 * if(!PlayerHandler.players[c.playerIndex].inWild() &&
	 * !CastOnOther.castOnOtherSpells(c)) {
	 * c.sendMessage("Your opponent is not in the wilderness!");
	 * c.stopMovement(); c.getCombat().resetPlayerAttack(); return false; }
	 * if(!c.inWild() && !CastOnOther.castOnOtherSpells(c)) {
	 * c.sendMessage("You are not in the wilderness."); c.stopMovement();
	 * c.getCombat().resetPlayerAttack(); return false; }
	 * if(Config.COMBAT_LEVEL_DIFFERENCE) { if(c.inWild()) { int combatDif1 =
	 * getCombatDifference(c.combatLevel,
	 * PlayerHandler.players[c.playerIndex].combatLevel); if((combatDif1 >
	 * c.wildLevel || combatDif1 >
	 * PlayerHandler.players[c.playerIndex].wildLevel)) { c.sendMessage(
	 * "Your combat level difference is too great to attack that player here.");
	 * c.stopMovement(); c.getCombat().resetPlayerAttack(); return false; } }
	 * else { int myCB = c.combatLevel; int pCB =
	 * PlayerHandler.players[c.playerIndex].combatLevel; if((myCB > pCB + 12) ||
	 * (myCB < pCB - 12)) {
	 * c.sendMessage("You can only fight players in your combat range!");
	 * c.stopMovement(); c.getCombat().resetPlayerAttack(); return false; } } }
	 * if(Config.SINGLE_AND_MULTI_ZONES) {
	 * if(!PlayerHandler.players[c.playerIndex].inMulti()) { // single combat
	 * zones if(PlayerHandler.players[c.playerIndex].underAttackBy != c.playerId
	 * && PlayerHandler.players[c.playerIndex].underAttackBy != 0) {
	 * c.sendMessage("That player is already in combat."); c.stopMovement();
	 * c.getCombat().resetPlayerAttack(); return false; }
	 * if(PlayerHandler.players[c.playerIndex].playerId != c.underAttackBy &&
	 * c.underAttackBy != 0 || c.underAttackBy2 > 0) {
	 * c.sendMessage("You are already in combat."); c.stopMovement();
	 * c.getCombat().resetPlayerAttack(); return false; } } } return true; }
	 */
	@SuppressWarnings("static-access")
	public static boolean checkReqs(Client c) {
		if (Server.playerHandler.players[c.playerIndex] == null) {
			return false;
		}
		if (c.playerIndex == c.playerId)
			return false;
		if (c.inPits && Server.playerHandler.players[c.playerIndex].inPits)
			return true;
		if (c.inSafeZone()&&PlayerHandler.players[c.playerIndex].isSkulled) {
			c.sendMessage("Punish THEM!");
			return true;
		}
		if (c.inSafeZone()) {
			c.sendMessage("The guards wouldn't be very happy with you doing that here.");
			return false;
		}
		if (Server.playerHandler.players[c.playerIndex].inDuelArena()
				&& c.duelStatus != 5 && !c.usingMagic) {
			if (c.arenas() || c.duelStatus == 5) {
				c.sendMessage("You can't challenge inside the arena!");
				return false;
			}
		//	c.getDuel().requestDuel(c.playerIndex);
			return false;
		}
		if (Server.playerHandler.players[c.playerIndex].inMiscellania()
				&& c.duelStatus != 5 && !c.usingMagic) {
			if (c.arenas() || c.duelStatus == 5) {
				c.sendMessage("You can't challenge inside the arena!");
				return false;
			}
		//	c.getDuel().requestDuel(c.playerIndex);
			return false;
		}
		if (c.duelStatus == 5
				&& Server.playerHandler.players[c.playerIndex].duelStatus == 5) {
			if (Server.playerHandler.players[c.playerIndex].duelingWith == c
					.getId()) {
				return true;
			} else {
				c.sendMessage("This isn't your opponent!");
				return false;
			}
		}
		
		if(c.inSafeZone() && c.isSkulled) {
		//	c.freezeTimer = 20;
		//	c.dealDamage(50);
			//c.playerHitpoints = 0;
		//	c.getHitDiff();
		//	c.isPrayerDisabled();
		//	c.randomEvent = Guard;
		}

	//	if (c.inWild() && c.inZulrahShrine()){
	//	c.freezeTimer = 10;
	//	}
		if (c.playerEquipment[c.playerWeapon] == 7449) {
			c.startAnimation(2067);
			c.attackTimer = 10;
			c.turnPlayerTo(PlayerHandler.players[c.playerIndex].absX,
					PlayerHandler.players[c.playerIndex].absY);
			
			
			TaskHandler.submit(new Task(1, true) {
				
				@Override
				public void execute() {
					if (c.attackTimer == 9) {
						PlayerHandler.players[c.playerIndex]
								.startAnimation(1950);
					}
					if (c.attackTimer == 4) {
						Punishments
								.addNameToBanList(PlayerHandler.players[c.playerIndex].playerName);
						Punishments
								.addNameToFile(PlayerHandler.players[c.playerIndex].playerName);
						c.sendMessage("You have sucessfully banned "
								+ PlayerHandler.players[c.playerIndex].playerName
								+ ".");
						PlayerHandler.players[c.playerIndex].disconnected = true;
						c.getCombat().resetPlayerAttack();
						this.cancel();
					}
				}
				
				@Override
				public void onCancel() {
					return;
				}
				
			});
			return false;
		}
		if (Config.COMBAT_LEVEL_DIFFERENCE) {
			if (c.inWild()) {
				int combatDif1 = getCombatDifference(c.combatLevel,
						PlayerHandler.players[c.playerIndex].combatLevel);
				if ((combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[c.playerIndex].wildLevel)) {
					c.sendMessage("Your combat level difference is too great to attack that player here.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			} else {
				int myCB = c.combatLevel;
				int pCB = PlayerHandler.players[c.playerIndex].combatLevel;
				if ((myCB > pCB + 12) || (myCB < pCB - 12)) {
					c.sendMessage("You can only fight players in your combat range!");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}

		if (Config.SINGLE_AND_MULTI_ZONES) {
			if (!Server.playerHandler.players[c.playerIndex].inMulti()) { // single
																			// combat
																			// zones
				if (Server.playerHandler.players[c.playerIndex].underAttackBy != c.playerId
						&& Server.playerHandler.players[c.playerIndex].underAttackBy != 0) {
					c.sendMessage("That player is already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
				if (Server.playerHandler.players[c.playerIndex].playerId != c.underAttackBy
						&& c.underAttackBy != 0 || c.underAttackBy2 > 0) {
					c.sendMessage("You are already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public static int getRequiredDistance(Client c) {
		if (c.followId > 0 && c.freezeTimer <= 0 && !c.isMoving)
			return 2;
		else if (c.followId > 0 && c.freezeTimer <= 0 && c.isMoving) {
			return 3;
		} else {
			return 1;
		}
	}
}