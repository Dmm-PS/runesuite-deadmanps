package wind.model.players.impl;

import wind.Config;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.util.Misc;

public class Curses {
	private Client c;
	public Curses(Client client) {
		this.c = client;
	}
		public void masterDeactivate() {
		c.curseDefence = 0;
		c.curseStrength = 0;
		c.curseAttack = 0;
		c.curseMagic = 0;
		c.curseRange = 0;
		Player.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}
	public void prayDrop() {
	if (c.prayerPoint == 0) {
		c.curseDefence = 0;
		c.curseStrength = 0;
		c.curseAttack = 0;
		c.curseMagic = 0;
		c.curseRange = 0;
		Player.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}
	}

public void deactivateLeeches() {
				if(c.curseActive[LEECH_DEFENCE]) {
				c.curseDefence -= 5;
					}
				if(c.curseActive[LEECH_ATTACK]) {
					c.curseAttack -= 5;
						}
				if(c.curseActive[LEECH_MAGIC]) {
					c.curseMagic -= 5;
						}
				if(c.curseActive[LEECH_RANGED]) {
					c.curseRange -= 5;
						}
				if(c.curseActive[LEECH_STRENGTH]) {
					c.curseStrength -= 5;
						}
					}
		boolean deactive = false;
		
		public void turmBonus() {
		if (deactive == true) {
		c.curseDefence = 0;
		c.curseStrength = 0;
		c.curseAttack = 0;
		deactive = false;
		} else if (c.inCombat == true && deactive == false) {
		c.curseDefence = 30;
		c.curseStrength = 33;
		c.curseAttack = 30;
		} else if (c.inCombat == false && deactive == false) {
		c.curseDefence = 15;
		c.curseStrength = 23;
		c.curseAttack = 15;
		}
		Player.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
		}
		
		public void deactivateTurmoil() {
				if(c.curseActive[TURMOIL]) {
					deactive = true;
					turmBonus();
						}
					}
		public void curseStat(int curseId) {
		switch(curseId) {
		
			case TURMOIL:
			if(!c.curseActive[TURMOIL]) {
				turmBonus();
				}else {
				deactivateTurmoil();
				}
				deactivateLeeches();
				Player.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				prayDrop();
			break;
			
			case LEECH_ATTACK:
			deactivateTurmoil();
			if(!c.curseActive[LEECH_ATTACK]) {
				c.curseAttack += 5;
				}else if (c.curseActive[LEECH_ATTACK]) {
				c.curseAttack -= 5;
				}
				prayDrop();
				Player.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				break;
				
			case LEECH_RANGED:
			deactivateTurmoil();
				if(!c.curseActive[LEECH_RANGED]) {
					c.curseRange += 5;
					}else if (c.curseActive[LEECH_RANGED]) {
					c.curseRange -= 5;
					}
					prayDrop();
					Player.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
					
				break;
			case LEECH_MAGIC:
			deactivateTurmoil();
				if(!c.curseActive[LEECH_MAGIC]) {
					c.curseMagic += 5;
					}else if (c.curseActive[LEECH_MAGIC]) {
					c.curseMagic -= 5;
					}
					prayDrop();
					Player.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				break;
				
			case LEECH_STRENGTH:
			deactivateTurmoil();
				if(!c.curseActive[LEECH_STRENGTH]) {
					c.curseStrength += 5;
					}else if (c.curseActive[LEECH_STRENGTH]) {
					c.curseStrength -= 5;
					}
					prayDrop();
					Player.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				break;
				
			case LEECH_DEFENCE:
			deactivateTurmoil();
			if(!c.curseActive[LEECH_DEFENCE]) {
				c.curseDefence += 5;
				}else if (c.curseActive[LEECH_DEFENCE]) {
				c.curseDefence -= 5;
				}
				prayDrop();
				Player.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			break;
				case SAP_SPIRIT:
				case SAP_WARRIOR:
				case SAP_MAGE:
				case SAP_RANGER:
				deactivateLeeches();
				deactivateTurmoil();
				prayDrop();
					Player.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				break;
				
			}
	}
	@SuppressWarnings("unused")
	private boolean appendedLeeches;
	
	/* All of the configuration such as activation, curse names, etc. */
	
	public final int[] PRAYER_LEVEL_REQUIRED = 	{50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95};
	public final String[] CURSE_NAME = 		{"Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit", "Berserker", "Deflect Summoning", "Deflect Magic",
											"Deflect Missiles", "Deflect Melee", "Leech Attack", "Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength",
											"Leech Energy", "Leech Special Attack", "Wrath", "Soul Split", "Turmoil"};
	public final int[] GLOW = {83, 84, 85, 101, 102, 86, 87, 88, 89, 90, 91, 103, 104, 92, 93, 94, 95, 96, 97, 105};
	public final int[] HEADICONS = {-1, -1, -1, -1, -1, -1, 12, 10, 11, 9, -1, -1, -1, -1, -1, -1, -1, 16, 17, -1};
	public double[] curseData = {
        0.6, // Protect Item
        1, // Sap Warrior
        1, // Sap Range
        1, // Sap Mage
        1, // Sap Spirit
        1.5, // Berserker
        1.5, // Deflect Summoning
        1.5, // Deflect Mage
        1.5, // Deflect Range
        1.5, // Deflect Melee
        1.5, // Leech Attack
        1.5, // Leech Range
        1.5, // Leech Mage
        1.5, // Leech Defence
        1.5, // Leech Strength
        1.5, // Leech Energy
        1.5, // Leech Special
        1.5, // Wrath
        3, // Soul Split
        5, // Turmoil
    };
	
	public final int PROTECT_ITEM = 0, SAP_WARRIOR = 1, SAP_RANGER = 2, SAP_MAGE = 3, SAP_SPIRIT = 4, BERSERKER = 5, DEFLECT_SUMMONING = 6, DEFLECT_MAGIC = 7,
					DEFLECT_MISSILES = 8, DEFLECT_MELEE = 9, LEECH_ATTACK = 10, LEECH_RANGED = 11, LEECH_MAGIC = 12, LEECH_DEFENCE = 13, LEECH_STRENGTH = 14,
					LEECH_ENERGY = 15, LEECH_SPEC = 16, WRATH = 17, SOUL_SPLIT = 18, TURMOIL = 19;
					
	public void activateCurse(int i) {
		if(c.duelRule[7]){
			for(int p = 0; p < 19; p++) {
				c.curseActive[p] = false;
				c.getPA().sendFrame36(GLOW[p], 0);	
			}
			c.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (c.playerLevel[1] < 30) {
			c.getPA().sendFrame36(GLOW[i], 0);
			c.sendMessage("You need 30 Defence to use this prayer.");
			return;
		}
		int[] leeches = {LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPEC};
		int[] saps = {SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT};
		int[] isHeadIcon = {DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, WRATH, SOUL_SPLIT};
		if(c.playerLevel[5] > 0 || !Config.PRAYER_POINTS_REQUIRED){
			if(c.getPA().getLevelForXP(c.playerXP[5]) >= PRAYER_LEVEL_REQUIRED[i] || !Config.PRAYER_LEVEL_REQUIRED) {
				if (!c.curseActive[i])
					curseEmote(i);
					curseStat(i);
				boolean headIcon = false;
				switch(i) {
					case PROTECT_ITEM:
						c.lastProtItem = System.currentTimeMillis();
					break;
					
					case SAP_WARRIOR: case SAP_RANGER: case SAP_MAGE: case SAP_SPIRIT:
					if(!c.curseActive[i]) {
						for (int j = 0; j < leeches.length; j++) {
							if (leeches[j] != i)
								deactivate(leeches[j]);						
						}
						deactivate(TURMOIL);
					}
					break;
					
					case LEECH_ATTACK: case LEECH_RANGED: case LEECH_MAGIC: case LEECH_DEFENCE: case LEECH_STRENGTH: case LEECH_ENERGY: case LEECH_SPEC:
					if(!c.curseActive[i]) {
						for (int j = 0; j < saps.length; j++) {
							if (saps[j] != i) {
								deactivate(saps[j]);
							}								
						}
						deactivate(TURMOIL);
					}
					break;
					
					case DEFLECT_SUMMONING:	case DEFLECT_MAGIC: case DEFLECT_MISSILES: case DEFLECT_MELEE:
					if(System.currentTimeMillis() - c.stopPrayerDelay < 5000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						deactivate(i);
						return;
					}
					if (i == DEFLECT_MAGIC)
						c.protMageDelay = System.currentTimeMillis();
					else if (i == DEFLECT_MISSILES)
						c.protRangeDelay = System.currentTimeMillis();
					else if (i == DEFLECT_MELEE)
						c.protMeleeDelay = System.currentTimeMillis();
					case WRATH:
					case SOUL_SPLIT:
					headIcon = true;
					if(!c.curseActive[i]) {
						for (int j = 0; j < isHeadIcon.length; j++) {
							if (isHeadIcon[j] != i)
								deactivate(isHeadIcon[j]);						
						}
					}
					break;
					
					case TURMOIL:
					if (!c.curseActive[i]) {
						for (int j = 0; j < leeches.length; j++) {
							if (leeches[j] != i)
								deactivate(leeches[j]);						
						}
						for (int j = 0; j < saps.length; j++) {
							if (saps[j] != i)
								deactivate(saps[j]);					
						}
					}
					break;
				}
				if (i == DEFLECT_MAGIC)
					c.protMageDelay = System.currentTimeMillis();
				else if (i == DEFLECT_MISSILES)
					c.protRangeDelay = System.currentTimeMillis();
				else if (i == DEFLECT_MELEE)
						c.protMeleeDelay = System.currentTimeMillis();
				if(!headIcon) {
					if(!c.curseActive[i]) {
						c.curseActive[i] = true;
						c.getPA().sendFrame36(GLOW[i], 1);					
					} else {
						c.curseActive[i] = false;
						c.getPA().sendFrame36(GLOW[i], 0);
					}
				} else {
					if(!c.curseActive[i]) {
						c.curseActive[i] = true;
						c.getPA().sendFrame36(GLOW[i], 1);
						c.headIcon = HEADICONS[i];
						c.getPA().requestUpdates();
					} else {
						deactivate(i);
						c.headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				deactivate(i);
				c.getPA().sendFrame126("You need a @blu@Prayer level of "+PRAYER_LEVEL_REQUIRED[i]+" to use "+CURSE_NAME[i]+".", 357);
				c.getPA().sendFrame126("Click here to continue", 358);
				c.getPA().sendFrame164(356);
			}
		} else {
			c.getPA().sendFrame36(GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}	
				
	}
	
	public void curseButtons(int buttonId) {
		int[] buttonIds = {87231, 87233, 87235, 87237, 87239, 87241, 87243, 87245, 87247, 87249, 87251, 87253, 87255, 88001, 88003, 88005, 88007, 88009,
							88011, 88013};
		for (int i = 0; i < buttonIds.length; i++) {
			if (buttonIds[i] == buttonId)
				activateCurse(i);
		}
	}
	
	public void curseEmote(int curseId) {
		switch(curseId) {
			case PROTECT_ITEM:
				c.startAnimation(12567);
				c.gfx0(2213);
			break;
			case BERSERKER:
				c.startAnimation(12589);
				c.gfx0(2266);
			break;
			case TURMOIL:
				c.startAnimation(12565);
				c.gfx0(2226);
			break;
		}
	}
	
	public void deactivate(int i) {
		c.curseActive[i] = false;
		c.getPA().sendFrame36(GLOW[i], 0);
	}
	
	/* Actual curse content */
	public double getTurmoilMultiplier(String stat) {
		NPC n = null;
		Client c2 = null;
		double otherLevel = 0;
		double turmoilMultiplier = stat.equalsIgnoreCase("Strength") ? 1.23 : 1.15;
		if (c.oldPlayerIndex > 0)
			c2 = (Client)PlayerHandler.players[c.oldPlayerIndex];
		else if (c.oldNpcIndex > 0)
			n = NPCHandler.npcs[c.oldNpcIndex];
		if (stat.equalsIgnoreCase("Defence")) {
			if (c2 != null)
				otherLevel = c2.getLevelForXP(c2.playerXP[1]) * 0.15;
			else if (n != null)
				otherLevel = n.getCombatLevel() * 0.15;
			else
				otherLevel = 0;
		} else if (stat.equalsIgnoreCase("Strength")) {
			if (c2 != null)
				otherLevel = c2.getLevelForXP(c2.playerXP[2]) * 0.10;
			else if (n != null)
				otherLevel = n.getCombatLevel() * 0.10;
			else
				otherLevel = 0;
		} else if (stat.equalsIgnoreCase("Attack")) {
			if (c2 != null)
				otherLevel = c2.getLevelForXP(c2.playerXP[0]) * 0.15;
			else if (n != null)
				otherLevel = n.getCombatLevel() * 0.15;
			else
				otherLevel = 0;
		}
		if (otherLevel > 14)
			otherLevel = 14;
		turmoilMultiplier += otherLevel * .01;
		return turmoilMultiplier;
	}
	
	public void soulSplit(int id, int damage) {
		if (c.curseActive[SOUL_SPLIT] && c.ssDelay <= 0) {
			if (c.oldPlayerIndex > 0) {
				c.ssTarget = (Client)PlayerHandler.players[id];
				c.getPA().createPlayersProjectile(c.getX(), c.getY(), (c.getX() - c.ssTarget.getX()) * -1, (c.getY() - c.ssTarget.getY()) * -1, 50, 75, 2263, 25, 25, - id - 1, 0);
				if (c.ssTarget.playerLevel[5] >= 0)
				c.ssTarget.playerLevel[5] -= (int)damage/50;
				c.ssTarget.getPA().refreshSkill(5);
				} else if (c.oldNpcIndex > 0) {
				c.ssTargetNPC = NPCHandler.npcs[id];
				c.getPA().createPlayersProjectile(c.getX(), c.getY(), (c.getX() - c.ssTargetNPC.getX()) * -1, (c.getY() - c.ssTargetNPC.getY()) * -1, 50, 75, 2263, 25, 25, id + 1, 0);
			}
			c.ssHeal = (int)damage/5;
			c.ssDelay = 5;
		}
	}
		public void resetCurse() {
		for(int p = 0; p < c.curseActive.length; p++) {
			c.curseActive[p] = false;
			c.getPA().sendFrame36(c.CURSE_GLOW[p], 0);
		}
		c.headIcon = -1;
		c.getPA().requestUpdates();
	}
	public void appendRandomLeech(int id, int leechType) {
		int projectile = 0;
		int endGFX = 0;
		int[] curseTypes = {LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPEC};
		if (!c.curseActive[curseTypes[leechType]] || Misc.random(3) < 3) {
			c.leechDelay = 0;
			return;
		}
		switch(leechType) {
			case 0: // Leech attack
				projectile = 2231;
				endGFX = 2232;
			break;
			case 1: // Leech ranged
				projectile = 2236;
				endGFX = 2238;
			break;
			case 2: // Leech magic
				projectile = 2240;
				endGFX = 2242;
			break;
			case 3: // Leech defence
				projectile = 2244;
				endGFX = 2246;
			break;
			case 4: // Leech strength
				projectile = 2248;
				endGFX = 2250;
			break;
			case 5: // Leech energy
				projectile = 2252;
				endGFX = 2254;
			break;
			case 6: // Leech special
				projectile = 2256;
				endGFX = 2258;
			break;
		}
		if (c.oldPlayerIndex > 0) {
			c.leechTarget = (Client)PlayerHandler.players[id];
			c.getPA().createPlayersProjectile(c.getX(), c.getY(), (c.getX() - c.leechTarget.getX()) * -1, (c.getY() - c.leechTarget.getY()) * -1, 50, 75, projectile, 25, 25, - c.oldPlayerIndex - 1, 0);
		} else if (c.oldNpcIndex > 0) {
			c.leechTargetNPC = NPCHandler.npcs[id];
			c.getPA().createPlayersProjectile(c.getX(), c.getY(), (c.getX() - c.leechTargetNPC.getX()) * -1, (c.getY() - c.leechTargetNPC.getY()) * -1, 50, 75, projectile, 25, 25, c.oldNpcIndex + 1, 0);
		}
		c.startAnimation(12575);
		c.leechEndGFX = endGFX;
		c.leechType = leechType;
	}
	
	private void leechEffect(int leechType) {
	}
	
	private void leechEffectNPC(int leechType) {
	}
	
	public void deflect(Client c2, int damage, int damageType) {
		int[] gfx = {2230, 2229, 2228, 2228};
		if (Misc.random(3) == 0) {
			int deflect = (damage < 10) ? 1 : (int)(damage/10);
			c.getCombat().appendHit(c2, deflect, 0, 3, false, deflect);
			c.gfx0(gfx[damageType]);
			c.startAnimation(12573);
		}
	}
	
	public void deflectNPC(NPC n, int damage, int damageType) {
		int[] gfx = {2230, 2229, 2228, 2228};
		if (Misc.random(3) == 0) {
			int deflect = (damage < 10) ? 1 : (int)(damage/10);
			c.getCombat().appendHit(n, deflect, 0, 3, 2);
			c.gfx0(gfx[damageType]);
			c.startAnimation(12573);
		}
	}
	
	public void handleProcess() {
		if (c.ssDelay > 0) 
			c.ssDelay--;
		if (c.ssDelay == 3) {
			if (c.ssTarget != null) {
				c.getPA().createPlayersProjectile(c.ssTarget.getX(), c.ssTarget.getY(), (c.ssTarget.getY() - c.getY()) * -1, (c.ssTarget.getX() - c.getX()) * -1, 50, 75, 2263, 25, 25, - c.getId() - 1, 40);
				c.ssTarget.gfx0(2264);
			} else if (c.ssTargetNPC != null) {
				c.getPA().createPlayersProjectile(c.ssTargetNPC.getX(), c.ssTargetNPC.getY(), (c.ssTargetNPC.getY() - c.getY())* -1, (c.ssTargetNPC.getX() - c.getX())* -1, 50, 75, 2263, 25, 25, - c.getId() - 1, 40);
				c.ssTargetNPC.gfx0(2264);
			}
			if(c.constitution < c.maxConstitution)
				c.addToHp(c.ssHeal);
		}
		if (c.leechDelay > 0)
			c.leechDelay--;
		if (c.leechDelay == 5) {
			if (c.oldPlayerIndex > 0)
				appendRandomLeech(c.oldPlayerIndex, Misc.random(6));
			else if (c.oldNpcIndex > 0) 
				appendRandomLeech(c.oldNpcIndex, Misc.random(6));
		} else if (c.leechDelay == 3) {
			if (c.leechTarget != null) {
				c.leechTarget.gfx0(c.leechEndGFX);
				leechEffect(c.leechType);
			} else if (c.leechTargetNPC != null) {
				c.leechTargetNPC.gfx0(c.leechEndGFX);
				leechEffectNPC(c.leechType);
			}
		}
	}
}

