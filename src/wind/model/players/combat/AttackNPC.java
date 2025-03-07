package wind.model.players.combat;

import wind.Config;
import wind.Server;
import wind.model.items.ItemAssistant;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.combat.range.RangeData;
import wind.model.players.content.minigames.PestControl;
import wind.music.SoundManager;
import wind.music.SoundManager.SoundType;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.ItemHandler;

public class AttackNPC {

	private CombatStyle combatStyle;

	public static void addNPCHit(final int i, final Client c) {
		c.getPA().cancelTeleportTask();
		c.isAttackingNpc = true;
		if (c.projectileStage == 0 && !c.usingMagic) {
			c.stopPlayerSkill = false;
			if (!c.usingClaws) {
				c.getCombat().applyNpcMeleeDamage(i, 1,
						Misc.random(c.getCombat().calculateMeleeMaxHit()));
				if (c.doubleHit) {
					if (!c.oldSpec) {
						c.getCombat().applyNpcMeleeDamage(
								i,
								2,
								Misc.random(c.getCombat()
										.calculateMeleeMaxHit()));
					} else {

						TaskHandler.submit(new Task(1, false) {

							@Override
							public void execute() {
								c.getCombat()
								.applyNpcMeleeDamage(i,	2, Misc.random(c.getCombat().calculateMeleeMaxHit()));
								this.cancel();
							}

							@Override
							public void onCancel() {
								c.isAttackingNpc = false;
							}

						});
					}
				}
			}
		}
	}
	public static boolean kalphite1(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 1158:
			return true;
		}
		return false;
	}

	public static boolean kalphite2(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 1160:
			return true;
		}
		return false;
	}
	
	public static void applyNpcMeleeDamage(Client c, int i, int damageMask,
			int damage) {
		if (!c.usingClaws) {
			damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		}
		c.previousDamage = damage;

		boolean fullVeracsEffect = c.getEquipment().isWearingVeracs(c)
				&& Misc.random(3) == 1;
		if (NPCHandler.npcs[i].HP - damage < 0) {
			damage = NPCHandler.npcs[i].HP;
		}
		if (NPCHandler.npcs[i].npcType == 5666) {
			damage = damage / 4;
			if (damage < 0) {
				damage = 0;
			}
		}
		if (NPCHandler.npcs[i].npcType == c.slayerTask) {
			if (c.playerEquipment[0] == 11864 || c.playerEquipment[0] == 11865)
				damage += Misc.random(5);	
		}

		if (!c.usingSpecial)
			SoundManager.sendSound(c, c.playerEquipment[c.playerWeapon],
					SoundType.MELEE_COMBAT);

		if (!fullVeracsEffect) {
			if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(c
					.getCombat().calculateMeleeAttack())) {
				damage = 0;
			} else if (NPCHandler.npcs[i].npcType == 2882
					|| NPCHandler.npcs[i].npcType == 2883) {
				damage = 0;
			}
		}

		boolean guthansEffect = false;
		if (c.getEquipment().isWearingGuthans(c)) {
			if (Misc.random(3) == 1) {
				guthansEffect = true;
			}
		}

		if (c.getEquipment().isWearingDharoks(c)) {
			if (Misc.random(3) == 1)
				damage = Misc.random((c.getCombat().calculateMeleeMaxHit())) * 2;
			else
				damage = Misc.random((c.getCombat().calculateMeleeMaxHit()));
		}

		if (NPCHandler.npcs[i].HP - damage < 0) {
			damage = NPCHandler.npcs[i].HP;
		}

		if (c.fightMode == 3) {
			c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 0);
			c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 1);
			c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 2);
			c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(0);
			c.getPA().refreshSkill(1);
			c.getPA().refreshSkill(2);
			c.getPA().refreshSkill(3);
		} else {
			c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE), c.fightMode);
			c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(c.fightMode);
			c.getPA().refreshSkill(3);
		}
		if (damage > 0 && PestControl.isInGame(c)) {
			if (NPCHandler.npcs[i].npcType == 6142) {
				c.pcDamage += damage;
				PestControl.portalHealth[0] -= damage;
			}
			if (NPCHandler.npcs[i].npcType == 6143) {
				c.pcDamage += damage;
				PestControl.portalHealth[1] -= damage;
			}
			if (NPCHandler.npcs[i].npcType == 6144) {
				c.pcDamage += damage;
				PestControl.portalHealth[2] -= damage;
			}
			if (NPCHandler.npcs[i].npcType == 6145) {
				c.pcDamage += damage;
				PestControl.portalHealth[3] -= damage;
			}
		}
		if (damage > 0 && guthansEffect) {
			c.playerLevel[3] += damage;
			if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
				c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			NPCHandler.npcs[i].gfx0(398);
		}

		NPCHandler.npcs[i].underAttack = true;
		c.killingNpcIndex = c.npcIndex;
		c.lastNpcAttacked = i;

		switch (c.specEffect) {
		case 4:
			if (damage > 0) {
				if (c.playerLevel[3] + damage > c.getLevelForXP(c.playerXP[3]))
					if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
						;
					else
						c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
				else
					c.playerLevel[3] += damage;
				c.getPA().refreshSkill(3);
			}
			c.specEffect = 0;
			break;
		}

		switch (damageMask) {
		case 1:
			NPCHandler.npcs[i].hitDiff = damage;
			NPCHandler.npcs[i].HP -= damage;
			c.totalDamageDealt += damage;
			NPCHandler.npcs[i].hitUpdateRequired = true;
			NPCHandler.npcs[i].updateRequired = true;
			break;

		case 2:
			NPCHandler.npcs[i].hitDiff2 = damage;
			NPCHandler.npcs[i].HP -= damage;
			c.totalDamageDealt += damage;
			NPCHandler.npcs[i].hitUpdateRequired2 = true;
			NPCHandler.npcs[i].updateRequired = true;
			c.doubleHit = false;
			break;

		}
	}

	public static void delayedHit(final Client c, final int i) {
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead) {
				c.npcIndex = 0;
				return;
			}

			NPCHandler.npcs[i].facePlayer(c.playerId);

			if (NPCHandler.npcs[i].underAttackBy > 0
					&& Server.npcHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = c.playerId;
			} else if (NPCHandler.npcs[i].underAttackBy < 0
					&& !Server.npcHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = c.playerId;
			}

			c.lastNpcAttacked = i;

			c.getCombat().addNPCHit(i, c);
			c.getCombat().addCharge();

			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(c.getCombat().rangeMaxHit());
				int damage2 = -1;

				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
					damage2 = Misc.random(c.getCombat().rangeMaxHit());
				if (c.playerEquipment[3] == 9185) {
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							c.boltDamage = damage;
							c.getCombat().crossbowSpecial(c, i);
							damage *= c.crossbowDamage;
						}
					}
				}
				if (c.playerEquipment[3] == 15010) {
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							c.boltDamage = damage;
							c.getCombat().crossbowSpecial(c, i);
							damage *= c.crossbowDamage;
						}
					}
				}
				if (Misc.random(NPCHandler.npcs[i].defence) > Misc
						.random(10 + c.getCombat().calculateRangeAttack())
						&& !c.ignoreDefence) {
					damage = 0;
				} else if (NPCHandler.npcs[i].npcType == 2881
						|| NPCHandler.npcs[i].npcType == 2883
						&& !c.ignoreDefence) {
					damage = 0;
				}
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(NPCHandler.npcs[i].defence) > Misc
							.random(10 + c.getCombat().calculateRangeAttack()))
						damage2 = 0;
				}
				if (c.dbowSpec) {
					NPCHandler.npcs[i].gfx100(c.lastArrowUsed == 11212 ? 1100
							: 1103);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				if (damage2 > 0) {
					if (damage == NPCHandler.npcs[i].HP
							&& NPCHandler.npcs[i].HP - damage2 > 0) {
						damage2 = 0;
					}
				}
				if (c.fightMode == 3) {
					c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
							4);
					c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
							1);
					c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
							3);
					c.getPA().refreshSkill(1);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				} else {
					c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE), 4);
					c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
							3);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				}
				if (damage > 0 && PestControl.isInGame(c)) {
					if (NPCHandler.npcs[i].npcType == 6142) {
						c.pcDamage += damage;
						PestControl.portalHealth[0] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6143) {
						c.pcDamage += damage;
						PestControl.portalHealth[1] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6144) {
						c.pcDamage += damage;
						PestControl.portalHealth[2] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6145) {
						c.pcDamage += damage;
						PestControl.portalHealth[3] -= damage;
					}
				}
				boolean dropArrows = true;

				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				for (int otherToxicBlowpipeId : c.TOXIC_BLOWPIPE) {
					if (c.playerEquipment[c.playerWeapon] == otherToxicBlowpipeId) {
						c.usingToxicBlowpipe = true;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowNpc();
					if (c.playerEquipment[3] == 11235) {
						c.getItems().dropArrowNpc();
					}
				}
				if (Server.npcHandler.getNPCs()[i].attackTimer < 9)
					NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i),
							i);
				c.rangeEndGFX = RangeData.getRangeEndGFX(c);

				if ((c.playerEquipment[3] == 10034 || c.playerEquipment[3] == 10033)) {
					for (int j = 0; j < NPCHandler.npcs.length; j++) {
						if (NPCHandler.npcs[j] != null
								&& NPCHandler.npcs[j].MaxHP > 0) {
							int nX = NPCHandler.npcs[j].getX();
							int nY = NPCHandler.npcs[j].getY();
							int pX = NPCHandler.npcs[i].getX();
							int pY = NPCHandler.npcs[i].getY();
							if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
									&& (nY - pY == -1 || nY - pY == 0 || nY
									- pY == 1)) {
								if (NPCHandler.npcs[i].inMulti()) {
									Client p = (Client) PlayerHandler.players[c.playerId];
									c.getCombat().appendMutliChinchompa(j);
									Server.npcHandler.attackPlayer(p, j);
								}
							}
						}
					}
				}
				if (!c.multiAttacking) {
					NPCHandler.npcs[i].underAttack = true;
					NPCHandler.npcs[i].hitDiff = damage;
					NPCHandler.npcs[i].HP -= damage;
					if (damage2 > -1) {
						NPCHandler.npcs[i].hitDiff2 = damage2;
						NPCHandler.npcs[i].HP -= damage2;
						c.totalDamageDealt += damage2;
					}
				}
				c.ignoreDefence = false;
				c.multiAttacking = false;

				if (c.rangeEndGFX > 0) {
					if (c.rangeEndGFXHeight) {
						NPCHandler.npcs[i].gfx100(c.rangeEndGFX);
					} else {
						NPCHandler.npcs[i].gfx0(c.rangeEndGFX);
					}
				}
				if (c.killingNpcIndex != c.oldNpcIndex) {
					c.totalDamageDealt = 0;
				}
				c.killingNpcIndex = c.oldNpcIndex;
				c.totalDamageDealt += damage;
				NPCHandler.npcs[i].hitUpdateRequired = true;
				if (damage2 > -1)
					NPCHandler.npcs[i].hitUpdateRequired2 = true;
				NPCHandler.npcs[i].updateRequired = true;

			} else if (c.projectileStage > 0) { // magic hit damage
				if (NPCHandler.npcs[i].HP <= 0) {
					return;
				}
				if (c.spellSwap) {
					c.spellSwap = false;
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.gfx0(-1);
				}
				int damage = 0;
				c.usingMagic = true;
				if (c.fullVoidMage()
						&& c.playerEquipment[c.playerWeapon] == 8841) {
					damage = Misc.random(c.getCombat().magicMaxHit() + 10);
				} else {
					damage = Misc.random(c.getCombat().magicMaxHit());
				}
				if (c.getCombat().godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				boolean magicFailed = false;
				if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc
						.random(c.getCombat().mageAtk())) {
					damage = 0;
					magicFailed = true;
				} else if (NPCHandler.npcs[i].npcType == 2881
						|| NPCHandler.npcs[i].npcType == 2882) {
					damage = 0;
					magicFailed = true;
				}
				for (int j = 0; j < NPCHandler.npcs.length; j++) {
					if (NPCHandler.npcs[j] != null
							&& NPCHandler.npcs[j].MaxHP > 0) {
						int nX = NPCHandler.npcs[j].getX();
						int nY = NPCHandler.npcs[j].getY();
						int pX = NPCHandler.npcs[i].getX();
						int pY = NPCHandler.npcs[i].getY();
						if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
								&& (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
							if (c.getCombat().multis()
									&& NPCHandler.npcs[i].inMulti()) {
								Client p = (Client) PlayerHandler.players[c.playerId];
								c.getCombat().appendMultiBarrageNPC(j,
										c.magicFailed);
								Server.npcHandler.attackPlayer(p, j);
							}
						}
					}
				}
				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				if (c.magicDef) {
					c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 2),
							1);
					c.getPA().refreshSkill(1);
				}
				c.getPA().addSkillXP(
						(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
								* Config.MAGIC_EXP_RATE), 6);
				c.getPA().addSkillXP(
						(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
								* Config.MAGIC_EXP_RATE / 3), 3);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				if (damage > 0 && PestControl.isInGame(c)) {
					if (NPCHandler.npcs[i].npcType == 6142) {
						c.pcDamage += damage;
						PestControl.portalHealth[0] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6143) {
						c.pcDamage += damage;
						PestControl.portalHealth[1] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6144) {
						c.pcDamage += damage;
						PestControl.portalHealth[2] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6145) {
						c.pcDamage += damage;
						PestControl.portalHealth[3] -= damage;
					}
				}
				if (c.getCombat().getEndGfxHeight() == 100 && !magicFailed) { // end
					// GFX
					NPCHandler.npcs[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
					if (Server.npcHandler.getNPCs()[i].attackTimer < 9)
						NPCHandler.startAnimation(
								c.getCombat().npcDefenceAnim(i), i);
				} else if (!magicFailed) {
					NPCHandler.npcs[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
				}

				if (magicFailed) {
					if (Server.npcHandler.getNPCs()[i].attackTimer < 9) {
						NPCHandler.startAnimation(
								c.getCombat().npcDefenceAnim(i), i);
					}
					NPCHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = c.getCombat().getFreezeTime();// freeze
					if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0) {
						NPCHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(
								c.playerXP[3])) {
							c.playerLevel[3] = c.getPA().getLevelForXP(
									c.playerXP[3]);
						} else {
							c.playerLevel[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;
					}

				}
				NPCHandler.npcs[i].underAttack = true;
				if (c.getCombat().magicMaxHit() != 0) {
					if (!c.multiAttacking) {
						NPCHandler.npcs[i].hitDiff = damage;
						NPCHandler.npcs[i].HP -= damage;
						NPCHandler.npcs[i].hitUpdateRequired = true;
						c.totalDamageDealt += damage;
					}
				}
				c.multiAttacking = false;
				c.killingNpcIndex = c.oldNpcIndex;
				NPCHandler.npcs[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				SoundManager.sendSound(c, c.oldSpellId, SoundType.MAGIC_COMBAT);
				c.oldSpellId = 0;
			}
		}
		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot == 1) {
			c.getCombat().fireProjectileNpc();
			c.hitDelay = 2;
			c.bowSpecShot = 0;
		}
	}

	public static void attackNpc(Client c, int i) {	
		c.getPA().cancelTeleportTask();
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].MaxHP <= 0) {
				c.usingMagic = false;
				c.faceUpdate(0);
				c.npcIndex = 0;
				return;
			}			

			if(c.respawnTimer > 0) {
				c.npcIndex = 0;
				return;
			}
			// System.out.println(NPCHandler.npcs[i].npcId);
			// System.out.println(c.playerEquipment[c.playerShield]);
			if ((NPCHandler.npcs[i].npcType == 2451) && (c.playerEquipment[c.playerShield] != 8844)) {
				c.sendMessage("You need to be weilding a @red@bronze defender@bla@ to fight this monster!");
				c.npcIndex = 0;
				return;
			}
			if ((NPCHandler.npcs[i].npcType == 2452) && (c.playerEquipment[c.playerShield] != 8845)) {
				c.sendMessage("You need to be weilding an @red@iron defender@bla@ to fight this monster!");
				c.npcIndex = 0;
				return;
			}
			if ((NPCHandler.npcs[i].npcType == 2453) && (c.playerEquipment[c.playerShield] != 8846)) {
				c.sendMessage("You need to be weilding a @red@steel defender@bla@ to fight this monster!");
				c.npcIndex = 0;
				return;
			}
			if ((NPCHandler.npcs[i].npcType == 2454) && (c.playerEquipment[c.playerShield] != 8847)) {
				c.sendMessage("You need to be weilding a @red@black defender@bla@ to fight this monster!");
				c.npcIndex = 0;
				return;
			}
			if ((NPCHandler.npcs[i].npcType == 2455) && (c.playerEquipment[c.playerShield] != 8848)) {
				c.sendMessage("You need to be weilding a @red@mithril defender@bla@ to fight this monster!");
				c.npcIndex = 0;
				return;
			}
			if ((NPCHandler.npcs[i].npcType == 2456) && (c.playerEquipment[c.playerShield] != 8849)) {
				c.sendMessage("You need to be weilding an @red@adamant defender@bla@ to fight this monster!");
				c.npcIndex = 0;
				return;
			}
			if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != c.playerId && !NPCHandler.npcs[i].inMulti()) {
				c.npcIndex = 0;
				c.sendMessage("This monster is already in combat.");
				return;
			}
			if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && c.underAttackBy2 != i && !c.inMulti()) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("I am already under attack.");
				return;
			}
			if (!c.getCombat().goodSlayer(i)) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (NPCHandler.npcs[i].spawnedBy != c.playerId && NPCHandler.npcs[i].spawnedBy > 0) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("This monster was not spawned for you.");
				return;
			}
			if(c.getX() == NPCHandler.npcs[i].getX() && c.getY() == NPCHandler.npcs[i].getY()) {
				c.getPA().walkTo(0,1);
			}
			c.followId2 = i;
			c.followId = 0;
			if(c.attackTimer <= 0) {
				c.usingBow = false;
				c.usingArrows = false;
				c.usingOtherRangeWeapons = false;
				c.usingToxicBlowpipe = false;
				c.usingCross = c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 11785 || c.playerEquipment[c.playerWeapon] == 15010;
				c.bonusAttack = 0;
				c.rangeItemUsed = 0;
				c.projectileStage = 0;
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if(c.spellId > 0) {
					c.usingMagic = true;
				}
				c.getItems();
				c.attackTimer = c.getCombat().getAttackDelay(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				if(!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if(c.playerEquipment[c.playerWeapon] == bowId) {
							c.usingBow = true;
							for (int arrowId : c.ARROWS) {
								if(c.playerEquipment[c.playerArrows] == arrowId) {
									c.usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if(c.playerEquipment[c.playerWeapon] == otherRangeId) {
							c.usingOtherRangeWeapons = true;
						}
					}
				}
				if((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2) && (c.getCombat().usingHally() && !c.usingOtherRangeWeapons && !c.usingBow && !c.usingMagic && !c.usingToxicBlowpipe)) ||(!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 4) && (c.usingOtherRangeWeapons && !c.usingBow && !c.usingMagic && !c.usingToxicBlowpipe)) || (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1) && (!c.usingOtherRangeWeapons && !c.getCombat().usingHally() && !c.usingBow && !c.usingMagic && !c.usingToxicBlowpipe)) || ((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 8) && (c.usingBow || c.usingMagic || c.usingToxicBlowpipe)))) {
					c.attackTimer = 2;
					return;
				}				
				SoundManager.sendSound(c, c.spellId, SoundType.CAST_SOUND);				
				if(!c.usingCross && !c.usingArrows && c.usingToxicBlowpipe && c.usingBow && (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223)) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				} 
				if(c.getCombat().correctBowAndArrows() < c.playerEquipment[c.playerArrows] && Config.CORRECT_ARROWS && c.usingBow && !c.getCombat().usingCrystalBow() && c.playerEquipment[c.playerWeapon] != 9185 && c.playerEquipment[c.playerWeapon] != 11785 && c.playerEquipment[c.playerWeapon] != 15010) {
					c.getItems();
					c.getItems();
					c.sendMessage("You can't use "+ItemAssistant.getItemName(c.playerEquipment[c.playerArrows]).toLowerCase()+"s with a "+ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()+".");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}

				if (c.playerEquipment[c.playerWeapon] == 9185 && c.playerEquipment[c.playerWeapon] == 11785 && !c.getCombat().properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;				
				}
				if (c.playerEquipment[c.playerWeapon] == 12926) {
					c.usingToxicBlowpipe = true;
				}
				if (c.playerEquipment[c.playerWeapon] == 15010 && !c.getCombat().properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;				
				}
				if(c.usingBow || c.castingMagic || c.usingToxicBlowpipe || c.usingOtherRangeWeapons || (c.getCombat().usingHally() && c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2))) {
					c.stopMovement();
				}
				if(!c.getCombat().checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				c.faceUpdate(i);
				NPCHandler.npcs[i].underAttackBy = c.playerId;
				NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				if(c.usingSpecial && !c.usingMagic) {
					if(c.getCombat().checkSpecAmount(c.playerEquipment[c.playerWeapon])){
						c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						c.getCombat().activateSpecial(c.playerEquipment[c.playerWeapon], i);
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.npcIndex = 0;
						return;
					}
				}
				c.specMaxHitIncrease = 0;
				if(c.playerLevel[3] > 0 && !c.isDead && NPCHandler.npcs[i].MaxHP > 0) {
					if(!c.usingMagic) {
						c.getItems();
						c.startAnimation(c.getCombat().getWepAnim(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()));
						if (Server.npcHandler.getNPCs()[i].attackTimer < 9) {
							NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
						}
					} else {
						c.startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
					}
				}
				c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
				c.lastArrowUsed = c.playerEquipment[c.playerArrows];
				if(!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons) { // melee hit delay
					c.followId2 = NPCHandler.npcs[i].npcId;
					c.getPA().followNpc();
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 0;
					c.oldNpcIndex = i;
				}
				if (c.playerEquipment[c.playerWeapon] == 14002) {
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				}
				if (c.usingToxicBlowpipe && !c.usingMagic && !c.usingBow && !c.usingOtherRangeWeapons) {
					if (c.dartsLoaded >= 1 && c.playerEquipment[c.playerCape] == 10499) {
					int removeDart = Misc.random(3);
					if (removeDart == 1) {
						c.dartsLoaded -= 1;
					}
					c.followId2 = NPCHandler.npcs[i].npcId;
					c.getPA().followNpc();
					//c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.startAnimation(5061);
					//c.startAnimation(222);
					c.lastArrowUsed = 0;
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							ItemAssistant
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.getCombat().fireProjectileNpc();
					
					} else if (c.dartsLoaded >= 1 && c.playerEquipment[c.playerCape] != 10499) {
					
					c.dartsLoaded -= 1;
					c.followId2 = NPCHandler.npcs[i].npcId;
					c.getPA().followNpc();
					//c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.startAnimation(5061);
					//c.startAnimation(222);
					c.lastArrowUsed = 0;
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							ItemAssistant
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.getCombat().fireProjectileNpc();
					} else {	
						c.sendMessage("You must load your blowpipe with darts in order to attack a NPC.");
					}
				}

				if(c.usingBow && !c.usingOtherRangeWeapons &&  !c.usingMagic || c.usingCross && c.usingToxicBlowpipe) { // range hit delay					
					if (c.usingCross)
						c.usingBow = true;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.followId2 = NPCHandler.npcs[i].npcId;
					c.getPA().followNpc();
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.gfx100(c.getCombat().getRangeStartGFX());	
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if(c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
						c.lastArrowUsed = 0;
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
						if(c.playerEquipment[3] == 11235) {
							c.getItems().deleteArrow();
						}	
					}
					c.getCombat().fireProjectileNpc();
				}


				if(c.usingOtherRangeWeapons && !c.usingMagic && !c.usingBow) {
					c.followId2 = NPCHandler.npcs[i].npcId;
					c.getPA().followNpc();	
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getItems().deleteEquipment();
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.lastArrowUsed = 0;
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.getCombat().fireProjectileNpc();	
				}

				if(c.usingMagic) {	// magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = NPCHandler.npcs[i].getX();
					int nY = NPCHandler.npcs[i].getY();
					int offX = (pY - nY)* -1;
					int offY = (pX - nX)* -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					c.stopMovement();
					if(c.MAGIC_SPELLS[c.spellId][3] > 0) {
						if(c.getCombat().getStartGfxHeight() == 100) {
							c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if(c.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, c.MAGIC_SPELLS[c.spellId][4], c.getCombat().getStartHeight(), c.getCombat().getEndHeight(), i + 1, 50);
					}
					c.getItems();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.oldNpcIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					if (!c.autocasting)
						c.npcIndex = 0;
				}

				if(c.usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal bow degrading
					if(c.playerEquipment[c.playerWeapon] == 4212) { // new crystal bow becomes full bow on the first shot
						c.getItems().wearItem(4214, 1, 3);
					}

					if(c.crystalBowArrowCount >= 250){
						switch(c.playerEquipment[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if(!c.getItems().addItem(4207, 1)) {
								ItemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.playerEquipment[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}	
				}
			}
		}
	}

	public CombatStyle getAttackStyle() {
		return combatStyle;
	}

	public void setAttackStyle(CombatStyle attackStyle) {
		this.combatStyle = attackStyle;
	}
}