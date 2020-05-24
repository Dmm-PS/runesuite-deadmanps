package wind.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import wind.Config;
import wind.Server;
import wind.clip.region.Region;
import wind.model.items.GameItem;
import wind.model.items.Item;
import wind.model.items.drop.Drop;
import wind.model.npcs.NPCSpawns.NpcSpawnBuilder;
import wind.model.npcs.impl.KalphiteQueen;
import wind.model.npcs.impl.Zulrah;
import wind.model.npcs.pets.Pet;
import wind.model.players.BossLog;
import wind.model.players.Client;
import wind.model.players.Events;
import wind.model.players.Player;
import wind.model.players.PlayerAssistant;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.content.minigames.PestControl;
import wind.model.players.content.minigames.fightcave.Wave;
import wind.model.players.content.minigames.nightmarezone.NightmareZone;
import wind.model.players.content.randomevents.RockGolem;
import wind.model.players.content.sound.Sounds;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;
import wind.world.ItemHandler;

public class NPCHandler {
	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static int npcCombat[] = new int[maxNPCs];

	public NPCHandler() {
		for (int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
			NPCDefinitions.getDefinitions()[i] = null;
		}
		loadNPCList("./aros_data/cfg/npc.cfg");
		
		build();
	}

	public void build() {
		NPCSpawns[] loaded = NpcSpawnBuilder.deserialize();
		try {
			loadAutoSpawn("./aros_data/cfg/spawn-config.cfg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(Config.SYSTEM_NAME + "Loaded: "+ loaded.length + " Npc Spawns");

		for (NPCSpawns spawn : loaded) {
			newNPC(spawn);
		}

	}

	public String[] voidKnightTalk = {
			"We must not fail!", 
			"Take down the portals", 
			"The Void Knights will not fall!", 
			"Hail the Void Knights!", 
			"We are beating these scum!"
	};

	public void newNPC(NPCSpawns s) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		NPC newNPC = new NPC(slot, s.getNpcId());
		newNPC.absX = s.getXPos();
		newNPC.absY = s.getYPos();
		newNPC.makeX = s.getXPos();
		newNPC.makeY = s.getYPos();
		newNPC.heightLevel = s.getHeight();
		newNPC.walkingType = s.getWalkType();
		int hp = getNpcListHP(s.getNpcId());
		newNPC.HP = hp;
		newNPC.MaxHP = hp;
		newNPC.maxHit = s.getMaxHit();
		newNPC.attack = s.getAttack();
		newNPC.defence = s.getDefence();
		npcs[slot] = newNPC;
	}

	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY,
						npcs[i].absX, npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
							getProjectileSpeed(i), npcs[i].projectileId, 43,
							31, -c.getId() - 1, 65);
				}
			}
		}
	}

	public void sendDrop(Client player, Drop drop, int i) {
		/*
		 * Since I dumped drops from rswiki it contains items that your server
		 * might not support.
		 */
		/*
		 * This is to stop those items from dropping, If you load higher
		 * revision items, I suggest you modify this.
		 */
		if (drop.getItemId() >= Config.ITEM_LIMIT) {
			return;
		}
		if (Item.getItemName(drop.getItemId()) == null) {
			return;
		}
		GameItem item = new GameItem(drop.getItemId(), 1).stackable ? new GameItem(
				drop.getItemId(), (drop.getMinAmount() * Config.DROP_RATE)
				+ Misc.random(drop.getExtraAmount() * Config.DROP_RATE))
		: new GameItem(drop.getItemId(), drop.getMinAmount()
				+ Misc.random(drop.getExtraAmount()));

				ItemHandler.createGroundItem(player, item.id, npcs[i].absX,
						npcs[i].absY, item.amount, player.playerId);

	}

	public void dropItems(int i) {
		Client killer = (Client) PlayerHandler.players[npcs[i].killedBy];
		Drop[] drops = NPCDrops.getDrops(npcs[i].npcType);
		int clueDrop = Misc.random(1024);
		int rareDropTable = Misc.random(2048);
		if (drops == null)
			return;
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		/*if (c.inNMZ()) {
			return;
		}*/
		if (c != null) {
			if (rareDropTable == 1) {
				ItemHandler.createGroundItem(c, PlayerAssistant.rareDropTable[Misc.random(PlayerAssistant.rareDropTable.length)], npcs[i].absX, npcs[i].absY, 1, c.playerId);
			}
			if (rareDropTable >= 2 && rareDropTable <= 10) {
				ItemHandler.createGroundItem(c, PlayerAssistant.uncommonDropTable[Misc.random(PlayerAssistant.uncommonDropTable.length)], npcs[i].absX, npcs[i].absY, 1, c.playerId);
			}
			if (rareDropTable >= 11 && rareDropTable <= 35) {
				ItemHandler.createGroundItem(c, PlayerAssistant.commonDropTable[Misc.random(PlayerAssistant.commonDropTable.length)], npcs[i].absX, npcs[i].absY, 1, c.playerId);
			}
			if (clueDrop == 1) {
				ItemHandler.createGroundItem(c, 2714, npcs[i].absX, npcs[i].absY, 1, c.playerId);
				c.sendMessage("You've recieved an@red@ Easy Clue Scroll@bla@! Good luck!");
			}
			if (clueDrop == 2) {
				ItemHandler.createGroundItem(c, 2802, npcs[i].absX, npcs[i].absY, 1, c.playerId);
				c.sendMessage("You've recieved a @red@Medium Clue Scroll@bla@! Good luck!");
			}
			if (clueDrop == 3) {
				ItemHandler.createGroundItem(c, 2775, npcs[i].absX, npcs[i].absY, 1, c.playerId);
				c.sendMessage("You've recieved a @red@Hard Clue Scroll@bla@! Good luck!");
			}
			if (clueDrop == 4 && c.getRights().greaterOrEqual(Rights.DONATOR)) {
				ItemHandler.createGroundItem(c, 2714, npcs[i].absX, npcs[i].absY, 1, c.playerId);
				c.sendMessage("You've recieved an@red@ Easy Clue Scroll@bla@! Good luck!");
			}
			if (clueDrop == 5 && c.getRights().greaterOrEqual(Rights.DONATOR)) {
				ItemHandler.createGroundItem(c, 2802, npcs[i].absX, npcs[i].absY, 1, c.playerId);
				c.sendMessage("You've recieved a @red@Medium Clue Scroll@bla@! Good luck!");
			}
			if (clueDrop == 6 && c.getRights().greaterOrEqual(Rights.DONATOR)) {
				ItemHandler.createGroundItem(c, 2775, npcs[i].absX, npcs[i].absY, 1, c.playerId);
				c.sendMessage("You've recieved a @red@Hard Clue Scroll@bla@! Good luck!");
			}
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913
					|| npcs[i].npcType == 914)
				c.magePoints += 1;
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100)
					sendDrop(killer, drop, i);
				else {
					if ((Misc.random(99) + 1) <= drop.getRate() * 1.0)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0)
				sendDrop(killer,
						possibleDrops[Misc.random(possibleDropsCount - 1)], i);

		}
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2551:
		case 2552:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
			return true;

		}

		return false;
	}

	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY,
						npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == 2) {
						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(c
									.getCombat().mageDef())) {
								int dam = Misc.random(max);
								c.dealDamage(dam);
								c.handleHitMask(dam);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						} else {
							c.dealDamage(0);
							c.handleHitMask(0);
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.prayerActive[17]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(c
									.getCombat().calculateRangeDefence())) {
								c.dealDamage(dam);
								c.handleHitMask(dam);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						} else {
							c.dealDamage(0);
							c.handleHitMask(0);
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(PlayerHandler.players[j].absX,
						PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, npcs[i].size, 2 + distanceRequired(i)
						+ followDistance(i))
						|| isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}
		}
		return 0;
	}

	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (goodDistance(PlayerHandler.players[j].absX,
						PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, npcs[i].size, 2 + distanceRequired(i)
						+ followDistance(i))
						|| isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	public int npcSize(int i) {
		switch (npcs[i].npcType) {
		case 2883:
		case 2882:
		case 2881:
			return 3;
		case 494:
			return 10;
		}
		return 0;
	}

	public boolean isAggressive(int i) {
		switch (npcs[i].npcType) {
		case 494:
		case 2045:
		case 2044:
		case 2043:
		case 2042:
		case 239:
		case 3753:
		case 3739:
		case 3767:
		case 3731: //Splatter
		case 3740: //Shifter
		case 3746: //Ravenger
		case 3750: //Spinner
		case 3754: //Torcher
		case 3770: //Defiler
		case 3776: //Brawler		
		case 2550:
		case 2551:
		case 2552:
		case 2553:
		case 3162:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2565:
		case 3163:
		case 3164:
		case 3165:
		case 3130:
		case 3131:
		case 3132:
		case 2206:
		case 2207:
		case 2208:
		case 2216:
		case 2217:
		case 2218:
		case 2892:
		case 2894:
		case 2881:
		case 2882:
		case 2883:
		case 2035:
		case 6250:// Npcs That Give ArmaKC
		case 6230:
		case 6231:
		case 6229:
		case 6232:
		case 6240:
		case 6241:
		case 6242:
		case 6233:
		case 6234:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
		case 6238:
		case 6239:
		case 6227:
		case 6625:
		case 6223:
		case 6222: // end of armadyl npcs
		case 122:// Npcs That Give BandosKC
		case 100:// Npcs That Give BandosKC
		case 6278:
		case 6277:
		case 6276:
		case 6283:
		case 6282:
		case 6281:
		case 6280:
		case 6279:
		case 6271:
		case 6272:
		case 6273:
		case 6274:
		case 6269:
		case 6270:
		case 6268:
		case 6265:
		case 6263:
		case 6261:
		case 2215: // end of bandos npcs
		case 3129:
		case 6219:
		case 6220:
		case 6217:
		case 6216:
		case 6215:
		case 6214:
		case 6213:
		case 6212:
		case 6211:
		case 6218:
		case 6208:
		case 6206:
		case 6204:
		case 6203:
		case 6275:
		case 6257:// Npcs That Give SaraKC
		case 6255:
		case 6256:
		case 6258:
		case 6259:
		case 6254: //try that
		case 6252:
		case 6248:
		case 2205:
			return true;
		}
		if (npcs[i].inWild() && npcs[i].MaxHP > 0)
			return true;
		if (isFightCaveNpc(i))
			return true;
		if (zulrahSpawns(i))
			return true;
		return false;
	}

	public void appendBossKC(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] bossKC = {50, 2054, 2881, 2882, 2883, 1158, 1159, 1160, 2205, 6222, 6203, 2215, 8133, 8349, 8350, 8351 };
			for (int j : bossKC) {
				if (npcs[i].npcType == j) {
					c.bossKills += 1;
					if (c.bossKills == 99) {
						c.sendMessage("You now have 100 boss kills and recieve <col=255>two achievement points<col=0!");
						c.aPoints += 2; 
					}
					if (c.bossKills == 249) {
						c.sendMessage("You now have 250 boss kills and recieve <col=255>three achievement points<col=0!");
						c.aPoints += 3;
					}
					if (c.bossKills == 499) {
						c.sendMessage("You now have 500 boss kills and recieve <col=255>five achievement points<col=0!");
						c.aPoints += 5;
					}
					if (c.bossKills == 999) {
						c.sendMessage("You now have 1000 boss kills and recieve <col=255>8 achievement points<col=0!");
						c.aPoints += 8;
					}
					break;
				}
			}
		}
	}

	public void appendBandosKC(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] bandosGodKC = { 6271, 6272, 6273, 6274, 6275, 6268, 122,
					6279, 6280, 6281, 6282, 6283, 6269, 6270, 6276, 6277, 6278 };
			for (int j : bandosGodKC) {
				if (npcs[i].npcType == j) {
					if (c.bandosKills < 15) {
						c.bandosKills++;
						c.getPA().sendFrame126("@cya@" + c.bandosKills, 16217);
						//c.sendMessage("Bandos Killcount: " + c.bandosGwdKC + ".");
					}

					break;
				}
			}
		}
	}

	public void appendSaradominKC(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] saraGodKC = { 6254, 6255, 6256, 6257, 6258, 6259, 96, 97,
					111, 125, 913 };
			for (int j : saraGodKC) {
				if (npcs[i].npcType == j) {
					if (c.saraKills < 15) {
						c.saraKills++;
						c.getPA().sendFrame126("@cya@" + c.saraKills, 16218);
						// c.sendMessage("Saradomin Killcount: " + c.saraGwdKC);
					}

					break;
				}
			}
		}
	}

	public void appendZamorakKC(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] zamGodKC = { 6210, 6211, 6212, 6214, 3129, 6220, 6219, 6218, 104, 82, 2025, 2052,
					94, 92, 75, 78, 912,  };
			for (int j : zamGodKC) {
				if (npcs[i].npcType == j) {
					if (c.zamorakKills < 15) {
						c.zamorakKills++;
						c.getPA().sendFrame126("@cya@" + c.zamorakKills, 16219);
						// c.sendMessage("Zamorak Killcount: " + c.zamGwdKC);
					}

					break;
				}
			}
		}
	}

	public void appendArmadylKC(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] armaGodKC = { 6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236,
					6237, 6238, 6239, 6240, 6241, 6242, 6243, 6244, 6245, 6246,
					275, 274, };
			for (int j : armaGodKC) {
				if (npcs[i].npcType == j) {
					if (c.armaKills < 15) {
						c.armaKills++;
						c.getPA().sendFrame126("@cya@" + c.armaKills, 16216);
						// c.sendMessage("Armadyl Killcount: " + c.armaGwdKC);
					}

					break;
				}
			}
		}
	}
	
	
	public static NPC spawnGuard(Client c, int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence,  boolean attackPlayer, String chat) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		//newNPC.respawns = false;
		//newNPC.needRespawn = false;
		newNPC.spawnedBy = c.getId();
		newNPC.isAggressive(npcType);
		npcs[slot] = newNPC;
		newNPC.forceChat(chat);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		
		} 
		return newNPC;
	}
	/**
	 * Summon npc, barrows, etc
	 **/
	public static NPC spawnNpc(final Client c, int npcType, int x, int y,
			int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		
		
	
		
		


		int[] animatedArmor = { 4278, 4279, 4280, 4281, 4282, 4283, 4284 };
		for (int anAnimatedArmor : animatedArmor) {
			if (newNPC.npcType == anAnimatedArmor) {
				((NPC) newNPC).forceAnim(4410);
				newNPC.forceChat("I'M ALIVE!");
			}
		}
		for (int[] Guard : Events.NPCs) {
			if (newNPC.npcType == Guard[2]) {
				newNPC.forceChat("Halt, Thief!");

				TaskHandler.submit(new Task(200, false) {
					@Override
					public void execute() {
						// Kill the random event npc.
						newNPC.isDead = true;
						newNPC.updateRequired = true;
						c.hasEvent = false;

						// *** ALWAYS CANCEL TASK.
						this.cancel();
					}
				});
			}
		}
		for (int[] aRockGolem : RockGolem.rockGolem) {
			if (newNPC.npcType == aRockGolem[2]) {
				newNPC.forceChat("Raarrrgghh! Flee human!");

				TaskHandler.submit(new Task(200, false) {
					@Override
					public void execute() {
						this.cancel();
					}
					@Override
					public void onCancel() {
						// if(!newNPC.isDead) {
						newNPC.isDead = true;
						newNPC.updateRequired = true;
						c.golemSpawned = false;
						// }
					}

				});
			}
		}
	

				
	//	for (int[] aZombie : Zombie.zombie) {
		//	if (newNPC.npcType == aZombie[2]) {
			//	newNPC.forceChat("Braaaainssss!");

			//	TaskHandler.submit(new Task(200, false) {
			//		@Override
			//		public void execute() {
			//			this.cancel();
			//		}					
			//		@Override
			//		public void onCancel() {
						// if(!newNPC.isDead) {
			//			newNPC.isDead = true;
			//			newNPC.updateRequired = true;
			//			c.zombieSpawned = false;
						// }
			//		}					
		//		});
			//}
	//	}
		return npcs[slot] = newNPC;
	}

	/**
	 * Emotes
	 **/

	public static int getAttackEmote(int i) {
		if (npcs[i].npcType == 3761)
			return 3880;
		if (npcs[i].npcType == 3760)
			return 3880;
		if (npcs[i].npcType == 3771)
			return 3922;
		switch (npcs[i].npcType) {
		//Zeruths anims :)
		case 4261: //goblin
		case 2484:
		case 3073:
		case 3074:
		case 3075:
		case 3076:
		case 2485:
		case 3050: //hobgogblin
			return 6184;
		case 2837: // Unicorn
			return 6376;
		case 3134:	// Imp
			return 169;
		case 2098: // Hill Giant
			return 4652;
		case 3017: // Giant Spiders
		case 2477:
		case 3022: //Ice Spider
		case 3021: //Red spider
		case 3023: // Poison spider
			return 5327;
		case 102: // Rock Crab
			return 1312;
		case 135: //Hellhound
			return 6579;
		case 2474: // Catablepon
			return 4271;
		case 3204:	// Jungle horrors
		case 3206:
		case 3208:
			return 4235;
		case 3205:	// Jungle horrors-small
		case 3207:
			return 4234;
			
			
			
			
		case 3731: //Splatter
			return 3891; 
		case 3740: //Shifter
			return 3901;
		case 3746: //Ravenger
			return 3915;
		case 3750: //Spinner
			return 3908;
		case 3754: //Torcher
			return 3882;
		case 3770: //Defiler
			return 3920;
		case 3776: //Brawler
			return 3897;

			// GODWARS
		case 3247: // Hobgoblin
			return 6184;

		case 6270: // Cyclops
		case 6269: // Ice cyclops
			return 4652;

		case 6219: // Spiritual Warrior
		case 6255: // Spiritual Warrior
			return 451;

		case 6229: // Spirtual Warrior arma
			return 6954;

		case 6218: // Gorak
			return 4300;

		case 6212: // Werewolf
			return 6536;

		case 6220: // Spirtual Ranger
		case 6256: // Spirtual Ranger
			return 426;

		case 6257: // Spirtual Mage
		case 3129: // Spirtual Mage
			return 811;

		case 6276: // Spirtual Ranger
		case 6278: // Spirtual Mage
		case 6272: // Ork
		case 6274: // Ork
		case 6277: // Spirtual Warrior bandos
			return 4320;

		case 6230: // Spirtual Ranger
		case 6233: // Aviansie
		case 6239: // Aviansie
		case 6232: // Aviansie
			return 6953;

		case 6254: // Saradomin Priest
			return 440;
		case 6258: // Saradomin Knight
			return 7048;
		case 6231: // Spirtual Mage
			return 6952;

		case 2215:
			if (npcs[i].attackType == 0)
				return 7060;
			else
				return 7063;
		case 2042:
			return 5069;	
		case 2044:
			return 5069;
		case 2043:
			return 5806;
		case 2060:// slash bash
			return 359;
		case 5902:// inadequacy
			return 6318;
		case 5421:// mutant tarn
			return 5617;
		case 5666:// barrelchest
			return 5894;
		case 5665:
		case 5664:// zombiepirate
			return 5889;
		case 3847: // stq
			return 3991;
		case 2054: // chaos ele
			return 3146;
		case 115: // ogre
			return 359;
		case 5779: // giant mole
			return 3312;
		case 4972: // giant roc
			return 5024;
		case 4971: // baby roc
			return 5031;
		case 1608: // kurask
			return 1512;
		case 417: // basilisk
			return 1546;

		case 1632: // turoth
			return 1595;
		case 1622: // rockslug
			return 1564;
		case 1317:
		case 2267:
		case 2268:
		case 2269:
		case 3181:
		case 3182:
		case 3183:
		case 3184:
		case 3185:
			return 451;
		case 6279:
		case 6280:
		case 6281:
		case 6282:
			return 6185;
		case 5529:
			return 5782;
		case 6116: // Seagull
			return 6811;
		case 5247:
			return 5411;
		case 6261:
		case 6263:
		case 6265:
			return 6154;
		case 6222:
			return 6976;
		case 6225:
			return 6953;
		case 6223:
			return 6952;
		case 6227:
			return 6954;
			// end of arma gwd
			// sara gwd
		case 2205:
			return 6964;
		case 6248:
			return 6376;
		case 6250:
			return 7018;
		case 6252:
			return 7009;
		case 6203:
			if (npcs[i].attackType == 0)
				return 6945;
			else
				return 6947; // end
		case 6204:
			return 64;
		case 6208:
			return 6947;
		case 6206:
			return 6945;
		case 5219:
		case 5218:
		case 5217:
		case 5216:
		case 5215:
		case 5214:
		case 5213:
			return 5097; // Penance
		case 5233:
		case 5232:
		case 5231:
		case 5230:
			return 5395;
		case 3761:
		case 3760:
			return 3880;
		case 3771:
			return 3922;
		case 3062:
			return 2955;
		case 131:
			return 5669;
		case 4404:
		case 4405:
		case 4406:
			return 4266;
		case 1019:
			return 1029;
		case 1020:
			return 1035;
		case 1021:
			return 1040;
		case 1022:
			return 1044;
		case 1678:
			return 1612;
		case 195:
		case 196:
		case 202:
		case 204:
		case 203:
			return 412;
		case 4353:
		case 4354:
		case 4355:
			return 4234;
		case 2709:
		case 2710:
		case 2711:
		case 2712:
			return 1162;

		case 1007:
			return 811;
		case 3058:
			return 2930;
		case 113:
			return 128;
		case 114:
			return 359;
		case 1265:
			return 1312;
		case 118:
			return 99;
		case 127:
			return 185;
		case 914:
			return 197;
		case 1620:
		case 1621:
			return 1562;
		case 678:
			return 426;
		case 1192:
			return 1245;
		case 119:
			return 99;
		case 2263:
			return 2182;
		case 3347:
		case 3346:
			return 3326;
		case 3063:
			return 2930;
		case 3061:
			return 2958;
		case 3066:
			return 2927;
		case 3452:// penance queen
			return 5411;
		case 4005:// dark beast
			return 2731;
		case 908:
			return 128;
		case 909:
			return 143;
		case 911:
			return 64;
		case 1624:
		case 1625:
			return 1557;
		case 3060:
			return 2962;
		case 2598:
		case 2599:
		case 2600:
		case 2601:
		case 2602:
		case 2603:
		case 2604:// tzhar-hur
			return 2609;
		case 2892:// Spinolyp
			return 2869;
		case 2881: // supreme
			return 2855;
		case 2882: // prime
			return 2854;
		case 2883: // rex
			return 2853;
		case 2457:
			return 2365;
		case 1341:
			return 1341;
		case 2606:// tzhaar-xil
			return 2609;
		case 66:
		case 67:
		case 168:
		case 169:
		case 162:
		case 68:// gnomes
			return 190;
		case 913:
		case 912:
			return 1162;
		case 160:
		case 161:
			return 191;
		case 163:
		case 164:
			return 192;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 94;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 284;
		case 413:
		//case 414:
		case 416:// rock golem
		case 418:
			return 153;

		case 3741: // Pest control
			return 3901;
		case 3751:
		case 3749:
		case 3748:
		case 3747:
			return 3908;
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
		case 1158: // kalphite
			return 6223;
		case 1160: // kalphite
			return 6235;

		case 2734:
		case 2627:// tzhaar
			return 2621;
		case 2630:
		case 2738:
		case 2736:
		case 2629:
			return 2625;
		case 2631:
		case 2632:
			return 2633;
		case 2741:
			return 2636;

		case 60:
		case 62:
		case 64:
		case 59:
		case 61:
		case 63:
		case 134:
		case 1009:
		case 2035: // spider
			return 5327;

		case 6006: // wolfman
			return 6547;

		case 414:// banshee
			return 1523;

		case 457:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1653:
		case 1654:
		case 1655:
		case 1656:
		case 1657:// crawling hand
			return 1592;

		case 7:
		case 1605:
		case 1606:
		case 1607:// aberrant specter
			return 1507;

		case 1618:
		case 1619:// bloodveld
			return 1552;

		case 1643:
		case 1644:
		case 1645:
		case 1646:
		case 1647:// infernal mage
			return 429;

		case 1613:// nechryael
			return 1528;

	
		case 1611:// gargoyle
			return 1517;

		case 415:// abyssal demon
			return 1537;

		case 1633: // pyrefiend
		case 3406:
			return 1582;

		case 1459:// monkey guard
			return 1402;

		case 1456:// monkey archer
			return 1394;

		case 1125:// dad
			return 284;

		case 1096:
		case 1097:
		case 1098:
		case 1106:
		case 1942:
		case 1108:
		case 1109:
		case 1110:
		case 1111:
		case 1112:
		case 1116:
		case 1117:
		case 1101:// troll
			return 284;
		case 1095:
			return 1142;

		case 104:// hellhound
		case 142:
		case 95:
		case 96:
			return 6581;

		case 74:
		case 75:
		case 76:
			return 5571;

		//case 73:
		//case 751: // zombie
		//case 77:
		//case 419:
		//case 420:
		//case 421:
		//case 422:
		//case 423:
		//case 424:
		//return 5568;

		case 103:
		
		case 655:
		case 749:
		case 491: // ghost
			return 5540;

		case 708: // imp
			return 169;

		case 397:
			return 59;

		case 172:
			return 1162;

		case 86:
		case 87:
			return 186;
		case 47:// rat
			return 2705;

		case 122:// hobgoblin
			return 164;

		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101:
		case 2678:
		case 2679:
		case 1774:
		case 1775:
		case 1769:
		case 1776:// goblins
			return 6184;

		case 141:
			return 6579;
		case 1593:
			return 6562;
		case 1954:
		case 152:
		case 45:
		case 1558: // wolf
			return 75;

		case 1560: // troll
		case 1566:
			return 284;

		case 89:
			return 6376;
		case 133: // unicorns
			return 289;

		case 1585:
		case 110:
		case 1582:
		case 1583:
		case 1584:
		case 1586: // giant
		case 4291:
		case 4292:
			return 4666;
		case 111:
		case 4687:
			return 4672;
		case 4690:
		case 4691:
		case 4692:
		case 4693:
		case 117:
		case 116:
		case 112:
		case 1587:
		case 1588:
			return 4652;

		case 128: // snake
		case 1479:
			return 275;

		case 2591:
		case 2620: // tzhaar
			return 2609;

		case 2610:
		case 2619: // tzhaar
			return 2610;

		case 2033: // rat
		case 748:
			return 138;

		case 484: // bloodveld
			return 2070;

		case 90:
		case 91:
		case 5359:
		case 5384:
		case 92:
		case 93: // skeleton
			return 5485;

		case 1766:
		case 1767:
		case 81: // cow
			return 5849;
		case 3065:
			return 2721;
		case 21: // hero
			return 451;

		case 1017:
		case 2693:
		case 41: // chicken
			return 5387;

		case 82:
		case 2025:
		case 752:
		case 3064:
		case 4694:
		case 4695:
		case 4696:
		case 4697:
		case 2052:
		case 4702:
		case 4703:
		case 4704:
		case 4705: // lesser
			return 64;

		case 123: // hobgoblin
			return 163;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 2456:
			return 1343;
		case 2455:
		case 2454:
		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1341;

		case 239:// drags
		case 5363:
		case 742:
		case 53:
		case 259:
		case 258:
		case 265:
		case 260:
		case 55:
		case 941:
		case 4682:
		case 5362:
		case 1590:
		case 1591:
		case 1592:
		case 3590:
		case 6593:
			return 80;

		case 1092:
		case 19: // white knight
			return 406;

		case 125: // ice warrior
			return 451;

		case 78:
		case 412: // bat
			return 4915;

		case 105:
		case 1195:
		case 1196:// bear
		case 106:
			return 4922;
		case 2889:
			return 2859;
		case 132: // monkey
			return 220;

		case 406:// cave crawler
			return 227;

		case 108:// scorpion
		case 1477:
			return 6254;
		case 107:
		case 144:
			return 6254;

		case 1675: // karil
			return 2075;

		case 1672: // ahrim
			return 729;

		case 1673: // dharok
			return 2067;

		case 1674: // guthan
			return 2080;

		case 1676: // torag
			return 0x814;

		case 1677: // verac
			return 2062;
		case 2745: // verac
			if (npcs[i].attackType == 1)
				return 2652;
			if (npcs[i].attackType == 2)
				return 2656;
			if (npcs[i].attackType == 0)
				return 2655;

		case 6267:
			return 359;
		case 871:// Ogre Shaman
		case 5181:// Ogre Shaman
		case 5184:// Ogre Shaman
		case 5187:// Ogre Shaman
		case 5190:// Ogre Shaman
		case 5193:// Ogre Shaman
			return 359;
		case 2746:
			return 2637;
		case 2607:
			return 2611;
		case 2743:// 360
			return 2647;
		case 13: // wizards
			return 711;
		case 2840: // earth warrior
			return 390;

		case 803: // monk
			return 422;
		case 58: // Shadow Spider
			return 41;
		case 2452:
			return 1312;

		default:
			return 0x326;
		}
	}

	public int getDeadEmote(int i) {
	
		if (npcs[i].npcType == 3761)
			return 3883;
		if (npcs[i].npcType == 3760)
			return 3883;
		if (npcs[i].npcType == 3741)
			return 3903;
		if (npcs[i].npcType == 3776)
			return 3894;
		if (npcs[i].npcType == 3751 || npcs[i].npcType == 3750
				|| npcs[i].npcType == 3749 || npcs[i].npcType == 3748
				|| npcs[i].npcType == 3747)
			return 3910;
		if (npcs[i].npcType == 3771)
			return 3922;
		switch (npcs[i].npcType) {
		//zeruths anims :)
		case 4261: //goblin
		case 2484:
		case 3073:
		case 3074:
		case 3075:
		case 3076:
		case 2485:
		case 3050: //hobgogblin
			return 6182;
		case 2837: //Unicorn
			return 6377;
		case 3134: //Imp
			return 172;
		case 2098: // Hill Giant
			return 4653;
		case 2477: //Giant Spiders
		case 3017:
		case 3022: //Ice Spider
		case 3021: //Red spider
		case 3023: // Poison spider
			return 5329;
		case 102: // Rock crab
			return 1314;
		case 135: // Hellhound
			return 6558;
		case 2474: // Catablepon
			return 4270;
		case 3204:	// Jungle horrors-big
		case 3206:
		case 3208:
		case 3205:	// Jungle horrors-small
		case 3207:
			return 4233;
			
			
		case 2042: //zulrah
		case 2043:
		case 2044:
			return 5072;
		case 3731: //Splatter
			return 3891; 
		case 3740: //Shifter
			return 3903;
		case 3746: //Ravenger
			return 3915;
		case 3750: //Spinner
			return 39010;
		case 3754: //Torcher
			return 3881;
		case 3770: //Defiler
			return 3922;
		case 3776: //Brawler
			return 3894;

		case 3247: // Hobgoblin
			return 6182;

		case 6270: // Cyclops
		case 6269: // Ice cyclops
			return 4653;

		case 6218: // Gorak
			return 4302;

		case 6212: // Werewolf
			return 6537;

		case 6276: // Spirtual Ranger
		case 6278: // Spirtual Mage
		case 6272: // Ork
		case 6274: // Ork
		case 6277: // Spirtual Warrior bandos
			return 4321;

		case 6230: // Spirtual Ranger
		case 6233: // Aviansie
		case 6239: // Aviansie
		case 6232: // Aviansie
		case 6231: // Spirtual Mage
		case 6229: // Spirtual Warrior arma

		case 6223: // Aviansie
		case 6225: // Spirtual Mage
		case 6227: // Spirtual Warrior arma
			return 6956;
		case 6252:
			return 7011;
		case 6222:
			return 6975;
		case 6203:
		case 6204:
		case 6206:
		case 6208:
			return 6946;
		case 6250: // sara lion
			return 7016;
		case 2205: // sara boss
			return 6965;
		case 6248: // sara horse
			return 6377;
		case 6261:
		case 6263:
		case 6265:
			return 6156;
		case 2060:// slash bash
			return 361;
		case 5902:// inadequacy
			return 6322;
		case 5421:// mutant tarn
			return 5619;
		case 5666:// barrelchest
			return 5898;
		case 3847: // stq
			return 3993;
		case 103:
		case 104:
		case 655:
		case 749:
		case 491: // ghost
			return 5542;
		case 1158:
			return 6242;
		case 1160:
			return 6233;
		case 2054: // chaos ele
			return 3147;
		case 115: // ogre
			return 361;
		case 5779: // giant mole
			return 3313;
		case 4972: // giant roc
			return 5027;
		case 4971: // baby roc
			return 5033;
		case 1608: // kurask
			return 1513;
		case 417: // basilisk
			return 1548;
		case 1632: // turoth
			return 1597;

		case 1622: // rockslug
			return 1568;
		case 406: // cave crawler
			return 228;

		case 6279:
		case 6280:
		case 6281:
		case 6282:
			return 6182;
		case 5529:
			return 5784;
		case 6116: // Seagull
			return 6812;
		case 5247:
			return 5412;
		case 5233:
		case 5232:
		case 5231:
		case 5230:
			return 5397;
		case 1019:
			return 1031;
		case 1020:
			return 1037;
		case 1021:
			return 1041;
		case 1022:
			return 1048;
		case 5219:
		case 5218:
		case 5217:
		case 5216:
		case 5215:
		case 5214:
		case 5213:
			return 5098; // Penance
		case 4353:
		case 4354:
		case 4355:
			return 4233;
		case 113:
			return 131;
		case 114:
			return 361;
		case 3058:
			return 2938;
		case 3057:
			return 2945;
		case 3063:
			return 2938;
		case 131:
			return 5671;

		case 1676:
			return 1628;
		case 1677:
			return 1618;
		case 1678:
			return 1614;

		case 4404:
		case 4405:
		case 4406:
			return 4265;
		case 914:
			return 196;
		case 3065:
			return 2728;
		case 1620:
		case 1621:
			return 1563;
		case 3066:
			return 2925;
		case 1265:
			return 1314;
		case 118:
			return 102;
		case 2263:
			return 2183;
		case 2598:
		case 2599:
		case 2600:
		case 2601:
		case 2602:
		case 2603:
		case 2606:// tzhaar-xil
		case 2591:
		case 2604:// tzhar-hur
			return 2607;
		case 3347:
		case 3346:
			return 3327;
		case 1192:
			return 1246;
		case 1624:
		case 1625:
			return 1558;
		case 2892:
			return 2865;
		case 127:
			return 188;
		case 119:
			return 102;
		case 2881: // supreme
		case 2882: // prime
		case 2883: // rex
			return 2856;
		case 4005:// dark beast
			return 2733;
		case 3452:// penance queen
			return 5412;
		case 2889:
			return 2862;
		case 1354:// dagnnoth mother
		case 1341:
			return 1342;
		case 2457:
			return 2367;

		case 66:
		case 67:
		case 168:
		case 169:
		case 162:
		case 68:// gnomes
			return 196;
		case 160:
		case 161:
			return 196;
		case 163:
		case 164:
			return 196;
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 6230;

		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 97;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 287;
		
		//case 414:
		
		case 416:
		// rock golem
		case 418:
			return 156;

		case 3127:
			return 2654;
		case 2743:
			return 2646;

		case 2734:
		case 2627:// tzhaar
			return 2620;
		case 2630:
		case 2629:
		case 2736:
		case 2738:
			return 2627;
		case 2631:
		case 2632:
			return 2630;
		case 2741:
			return 2638;

		case 908:
			return 131;
		case 909:
			return 146;
		case 911:
			return 67;

		case 60:
		case 59:
		case 61:
		case 63:
		case 64:
		case 134:
		case 2035: // spider
		case 62:
		case 1009:
			return 5329;

		case 6006: // wolfman
			return 6537;

		case 414:// banshee
			return 1524;

		case 457:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1653:
		case 1654:
		case 1655:
		case 1656:
		case 1657:// crawling hand
			return 1590;

		case 7:
		case 1605:
		case 1606:
		case 1607:// aberrant specter
			return 1508;

		case 484:
		case 1619:// bloodveld
			return 1553;

		case 1643:
		case 1644:
		case 1645:
		case 1646:
		case 1647:// infernal mage
			return 2304;

		case 1613:// nechryael
			return 1530;

		case 413:
		case 1611:// gargoyle
			return 1518;

		case 415:// abyssal demon
			return 1538;

		case 3406:
		case 1633: // pyrefiend
			return 1580;

		case 1456:// monkey archer
			return 1397;

		case 122:// hobgoblin
			return 167;

		case 1125:// dad
			return 287;

		case 1096:
		case 1097:
		case 1098:
		case 1942:
		case 1108:
		case 1109:
		case 1110:
		case 1111:
		case 1112:
		case 1116:
		case 1117:
		case 1101:// troll
		case 1106:
		case 1095:
			return 287;

	// hellhound
		case 142:
		case 95:
		case 96:
			return 6576;

		case 74:
		case 75:
		case 76:
			return 5569;

		//case 73:
		//case 751: // zombie
		//case 77:
		//case 419:
		//case 420:
		//case 421:
		//case 422:
		//case 423:
		//case 424:
		//	return 5569;

		case 5665:
		case 5664:// zombiepirate
			return 5891;

		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101:
		case 2678:
		case 2679:
		case 1774:
		case 1775:
		case 1769:
		case 1776:// goblins
			return 6182;

		case 708: // imp
		case 3062:
			return 172;

		case 81: // cow
		case 397:
			return 5851;

		case 86:
		case 87:
			return 141;
		case 47:// rat
			return 2707;

		case 2620: // tzhaar
		case 2610:
		case 2619:
			return 2607;

		case 1560: // troll
		case 1566:
			return 287;

		case 89:
			return 6377;
		case 133: // unicorns
			return 292;

		case 2033: // rat
		case 748:
			return 141;

		case 2031: // bloodvel
			return 2073;

		case 105:
		case 1195:
		case 1196: // bear
			return 4930;

		case 128: // snake
		case 1479:
			return 278;

		case 132: // monkey
			return 223;

		case 108:// scorpion
		case 1477:
			return 6256;
		case 144:
		case 107:
			return 6256;

		case 90:
		case 91:
		case 5359:
		case 5384:
		case 92:
		case 93: // skeleton
			return 5491;

		case 82:
		case 3064:
		case 4694:
		case 4695:
		case 4696:
		case 4697:
		case 2025:
		case 752:
		case 2052:
		case 4702:
		case 4703:
		case 4704:
		case 4705: // lesser
			return 67;

		case 4669:
		case 4670:
		case 4671:
		case 1589:
		case 52:
			return 92;
		case 2215:
			return 7062;
		case 123: // hobgoblin
		case 3061:
			return 167;
		case 141:
			return 6570;
		case 1593:
			return 6564;
		case 152:
		case 45:
		case 1558: // wolf
		case 1954:
			return 78;

		case 1459:// monkey guard
			return 1404;

		case 78:
		case 412: // bat
			return 4917;

		case 1766:
		case 1767:
			return 0x03E;

		case 1017:
		case 2693:
		case 41: // chicken
			return 5389;

		case 1585:
		case 110:
		case 1582:
		case 1583:
		case 1584:
		case 1586: // giant
		case 4291:
		case 4292:
			return 4673;
		case 111:
		case 4687:
			return 4673;
		case 4690:
		case 4691:
		case 4692:
		case 4693:
		case 117:
		case 116:
		case 112:
		case 1587:
		case 1588:
			return 4653;

		case 2455:
		case 2454:
		case 2456:
		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;

		case 125: // ice warrior
			return 843;
		case 871:// Ogre Shaman
			return 361;
		case 6142:
			return 1;
		case 6143:
			return 1;
		case 6144:
			return 1;
		case 6145:
			return 1;
		case 3742:// Ravager
		case 3743:
		case 3744:
		case 3745:
			return 3916;
		case 3772:// Brawler
		case 3773:
		case 3774:
		case 3775:
			return 3894;
		case 6267:
			return 357;
		case 6268:
			return 2938;
		case 6271:
			return 4321;
		case 6273:
			return 4321;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 2607:
			return 2607;
		case 2746:
			return 2638;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
			return 1597;
		case 3376:
			return 28;
		case 1634:
		case 1635:
		case 1636:
			return 1580;
		case 100:

		case 239:// drags
		case 5363:
		case 742:
		case 53:
		case 259:
		case 265:
		case 260:// green dragon
		case 4682:
		case 5362:
		case 1590:// metal dragon
		case 1591:
		case 1592:
		case 3590:
		case 6593:
			return 92;


			// case 103:
		default:
			return 2304;
		}
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case 1672:
		case 1675:
			return 7;

		case 3127:
			return 8;

		case 3162:
		case 2559:
		case 2560:
		case 2561:
		case 2215:
			return 6;
			// saradomin gw boss
		case 2205:
			return 2;

		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 2054:
		case 2892:
		case 2894:
			return 3;

		case 2743:
		case 2631:
		case 3162:
		case 2559:
		case 2560:
			return 3;

		case 3127:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;

		case 1672:
			return 4;
		case 1675:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {
		case 6142:
		case 6143:
		case 6144:
		case 6145:
		case 2042:
		case 2043:
		case 2044:
		case 2045:
			return -1;		
		case 2881:
		case 2882:
		case 2883:
		case 3162:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2215:
		case 6222:
		case 6203:
		case 2205:
			return 60;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return 500;
		default:
			return 25;
		}
	}

	public void newNPC(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	public void newNPCList(int npcType, String npcName, int combat, int HP) {

		NPCDefinitions newNPCList = new NPCDefinitions(npcType);
		newNPCList.setNpcName(npcName);
		newNPCList.setNpcCombat(combat);
		newNPCList.setNpcHealth(HP);
		NPCDefinitions.getDefinitions()[npcType] = newNPCList;
	}

	public void handleClipping(int i) {
		NPC npc = npcs[i];
		if (npc.moveX == 1 && npc.moveY == 1) {
			if ((Region
					.getClipping(npc.absX + 1, npc.absY + 1, npc.heightLevel) & 0x12801e0) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
					npc.moveY = 1;
				else
					npc.moveX = 1;
			}
		} else if (npc.moveX == -1 && npc.moveY == -1) {
			if ((Region
					.getClipping(npc.absX - 1, npc.absY - 1, npc.heightLevel) & 0x128010e) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
					npc.moveY = -1;
				else
					npc.moveX = -1;
			}
		} else if (npc.moveX == 1 && npc.moveY == -1) {
			if ((Region
					.getClipping(npc.absX + 1, npc.absY - 1, npc.heightLevel) & 0x1280183) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
					npc.moveY = -1;
				else
					npc.moveX = 1;
			}
		} else if (npc.moveX == -1 && npc.moveY == 1) {
			if ((Region
					.getClipping(npc.absX - 1, npc.absY + 1, npc.heightLevel) & 0x128013) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
					npc.moveY = 1;
				else
					npc.moveX = -1;
			}
		} // Checking Diagonal movement.

		if (npc.moveY == -1) {
			if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) != 0)
				npc.moveY = 0;
		} else if (npc.moveY == 1) {
			if ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) != 0)
				npc.moveY = 0;
		} // Checking Y movement.
		if (npc.moveX == 1) {
			if ((Region.getClipping(npc.absX + 1, npc.absY, npc.heightLevel) & 0x1280180) != 0)
				npc.moveX = 0;
		} else if (npc.moveX == -1) {
			if ((Region.getClipping(npc.absX - 1, npc.absY, npc.heightLevel) & 0x1280108) != 0)
				npc.moveX = 0;
		} // Checking X movement.
	}

	public static boolean isSpawnedBy(Client player, NPC npc) {
		if (player != null && npc != null)
			if (npc.spawnedBy == player.playerId
			|| npc.killerId == player.playerId)
				return true;
		return false;
	}

	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null)
				continue;
			npcs[i].clearUpdateFlags();

		}	
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}

				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}

				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}

				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}				
				if (npcs[i].npcType == 3782 && PestControl.gameStarted){
					if (Misc.random(10) == 4)
						npcs[i].forceChat(voidKnightTalk[Misc.random(voidKnightTalk.length)]);
				}

				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}
				NPC NPC = npcs[i];
				Client petOwner = (Client) PlayerHandler.players[NPC.summonedBy];
				if (petOwner == null && NPC.summoned) {
					Pet.deletePet(NPC);
				}
				if (petOwner != null && petOwner.isDead) {
					Pet.deletePet(NPC);
				}
				if (petOwner != null && petOwner.getPetSummoned() && NPC.summoned) {
					if (petOwner.goodDistance(NPC.getX(), NPC.getY(), petOwner.absX, petOwner.absY, 15)) {
						Server.npcHandler.followPlayer(i, petOwner.playerId);
					} else {
						Pet.deletePet(NPC);
						NPCHandler.summonPet(petOwner, petOwner.petID, petOwner.absX, petOwner.absY - 1, petOwner.heightLevel);
					}
				}

				if (npcs[i].spawnedBy > 0) { // delete summons npc
					if (PlayerHandler.players[npcs[i].spawnedBy] == null
							|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
							|| PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
							|| !PlayerHandler.players[npcs[i].spawnedBy]
									.goodDistance(
											npcs[i].getX(),
											npcs[i].getY(),
											PlayerHandler.players[npcs[i].spawnedBy]
													.getX(),
													PlayerHandler.players[npcs[i].spawnedBy]
															.getY(), 20)) {

						if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
							for (int o = 0; o < PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
								if (npcs[i].npcType == PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
									if (PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1)
										PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
								}
							}
						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;
				if (npcs[i].lastX != npcs[i].getX()
						|| npcs[i].lastY != npcs[i].getY()) {
					npcs[i].lastX = npcs[i].getX();
					npcs[i].lastY = npcs[i].getY();
				}

				/**
				 * Attacking player
				 **/
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead
						&& !switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				} else if (isAggressive(i) && !npcs[i].underAttack
						&& !npcs[i].isDead && switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				}
				
				if (NPC.isAggressive(i) && !NPC.underAttack && !NPC.isDead
						&& !switchesAttackers(i)) {
					NPC.killerId = getCloseRandomPlayer(i);
				} else if (NPC.isAggressive(i) && !NPC.underAttack
						&& !NPC.isDead && switchesAttackers(i)) {
					NPC.killerId = getCloseRandomPlayer(i);
				}

				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
					npcs[i].underAttackBy = 0;

				if ((npcs[i].killerId > 0 || npcs[i].underAttack)
						&& !npcs[i].walkingHome && retaliates(npcs[i].npcType)) {
					if (!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if (PlayerHandler.players[p] != null) {
							Client c = (Client) PlayerHandler.players[p];
							followPlayer(i, c.playerId);
							if (npcs[i] == null)
								continue;
							if (npcs[i].attackTimer == 0) {
								if (c != null) {
									attackPlayer(c, i);
								}
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}

				/**
				 * Random walking and walking home
				 **/
				if (npcs[i] == null)
					continue;
				if ((!npcs[i].underAttack || npcs[i].walkingHome)
						&& npcs[i].randomWalk && !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;
					if (npcs[i].spawnedBy == 0) {
						if ((npcs[i].absX > npcs[i].makeX
								+ Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absX < npcs[i].makeX
										- Config.NPC_RANDOM_WALK_DISTANCE)
										|| (npcs[i].absY > npcs[i].makeY
												+ Config.NPC_RANDOM_WALK_DISTANCE)
												|| (npcs[i].absY < npcs[i].makeY
														- Config.NPC_RANDOM_WALK_DISTANCE)) {
							npcs[i].walkingHome = true;
						}
					}

					if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX
							&& npcs[i].absY == npcs[i].makeY) {
						npcs[i].walkingHome = false;
					} else if (npcs[i].walkingHome) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
						npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
						handleClipping(i);
						npcs[i].getNextNPCMovement(i);
						npcs[i].updateRequired = true;
					}
					if (npcs[i].walkingType == 1) {
						if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
							int MoveX = 0;
							int MoveY = 0;
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}

							if (MoveX == 1) {
								if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}

							if (MoveX == -1) {
								if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}

							if (MoveY == 1) {
								if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}

							if (MoveY == -1) {
								if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}

							// if (npcs[i].heightLevel, npcs[i].absX,
							// npcs[i].absY, x, y, 0))
							handleClipping(i);
							npcs[i].getNextNPCMovement(i);
							// else
							// {
							// npcs[i].moveX = 0;
							// npcs[i].moveY = 0;
							// }
							npcs[i].updateRequired = true;
						}
					}
				}

				if (npcs[i].isDead == true) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false
							&& npcs[i].needRespawn == false) {
						npcs[i].updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						npcs[i].animNumber = getDeadEmote(i); // dead emote
						npcs[i].animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						

						Client player = (Client) PlayerHandler.players[npcs[i].killedBy];
						player.getBarrows().getNpcController().registerKill(npcs[i].npcType);
//<<<<<<< HEAD
					//	appendSlayerExperience(i);
//=======
//>>>>>>> origin/master
						//Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
					//	if (c.getSlayer().isSlayerTask(npcs[i].npcType)) {
						//	c.taskAmount--;
						//	c.getPA().addSkillXP(npcs[i].MaxHP * Config.SLAYER_EXPERIENCE,
							//		18);
							//if (c.taskAmount <= 0) {
						player.getPA().addSkillXP(npcs[i].MaxHP * Config.SLAYER_EXPERIENCE, 18);
							//	int points = c.getSlayer().getDifficulty(c.slayerTask) * 5;
							//	c.slayerTask = -1;
							//	c.slayerPoints += points;
							//	c.sendMessage("You completed your slayer task. You obtain "
							//			+ points
							//			+ " slayer points. Please talk to Nieve.");
						//	}
					//	}
						
						//killedBarrow(i);
						/*
						 * if (isFightCaveNpc(i)) killedTzhaar(i);
						 */
						if (isNightmareNPC(i))
							killedNightmareNPC(i);
						if (isBoss(i))
							BossLog.handle();
						npcs[i].actionTimer = 4; // delete time
						resetPlayersInCombat(i);
					} else if (npcs[i].actionTimer == 0
							&& npcs[i].applyDead == true
							&& npcs[i].needRespawn == false 
							//&& npcs[i].npcType != 1158
							&& npcs[i].npcType != 6142
							&& npcs[i].npcType != 6143
							&& npcs[i].npcType != 6144
							&& npcs[i].npcType != 6145) {
						Client p = (Client) PlayerHandler.players[NPCHandler.npcs[i].killedBy];
						if (NPCHandler.npcs[i] != null && p != null) {
							tzhaarDeathHandler(p, i);
						}

						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						dropItems(i);
						if (npcs[i].npcType == 2042 || npcs[i].npcType == 2043 || npcs[i].npcType == 2044 && p.inZulrahShrine())
							Zulrah.handleDrops(p);
						//Zulrah.handleClueDrop(p);
						appendBandosKC(i);
						appendArmadylKC(i);
						appendBossKC(i);
						appendZamorakKC(i);
						appendSaradominKC(i);
						appendKillCount(i);
						killedKQ(i);				
						// appendJailKc(i);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].HP = npcs[i].MaxHP;
						npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						npcs[i].animUpdateRequired = true;
						if (npcs[i].npcType == 3127) {
							handleJadDeath(i);
						}
					} else if (npcs[i].actionTimer == 0
							&& npcs[i].needRespawn == true) {
						Client player = (Client) PlayerHandler.players[npcs[i].spawnedBy];
						if (player != null) {
							npcs[i] = null;
						} else {
							int old1 = npcs[i].npcType;
							int old2 = npcs[i].makeX;
							int old3 = npcs[i].makeY;
							int old4 = npcs[i].heightLevel;
							int old5 = npcs[i].walkingType;
							int old6 = npcs[i].MaxHP;
							int old7 = npcs[i].maxHit;
							int old8 = npcs[i].attack;
							int old9 = npcs[i].defence;

							npcs[i] = null;
							newNPC(old1, old2, old3, old4, old5, old6, old7,
									old8, old9);
						}
					}
				}
			}
		}
	}
	

	public boolean isBoss(int i) {
		for (int ID = 0; ID < BossLog.bossID.length; ID++) {
			if (npcs[i].npcType == ID)
				return true;
		}
		return false;
	}

	public boolean isNightmareNPC(int i) {
		switch (npcs[i].npcType) {
		case 104:
		case 259:
		case 265:
		case 82:
		case 93:
		case 2052:
		case 1225:
		case 6227:
		case 443:
		case 1472:
			return true;
		}
		return false;
	}

	private void killedKQ(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			switch (npcs[i].npcType) {
			case 1158:
				KalphiteQueen.spawnSecondForm(c, i);
				break;
			case 1160:
				KalphiteQueen.spawnFirstForm(c, i);
				break;
			}
		}
	}

	private void killedNightmareNPC(int i) {
		final Client c2 = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		if (c2 != null) {
			if (!c2.inNMZ())
				return;
			c2.nzKilled++;
			if (c2.nzKilled == c2.nzStage) {
				c2.nightmareWave++;
				Server.nightmareZone.playNextWave(c2);
				final int points = 1;
					//(int) Misc.randomDouble(10, 25);
				c2.nmzPoints += points;
				c2.sendMessage("You have received " + points + " points by completing the wave. You have now "+c2.nmzPoints+ " points in total.");
				NightmareZone.getInstance().updateOverlay(c2);
			}

		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2215:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 1158: // kq
			if (npcs[i].attackType == 2)
				return true;
		case 1160: // kq
			if (npcs[i].attackType == 1)
				return true;		
		case 3162:
			return true;
		case 2215:
			if (npcs[i].attackType == 1)
				return true;
		case 6261:// bandos?
			return true;

		case 2205:// saradomin?
			if (npcs[i].attackType == 2)
				return true;			
		case 6260: //armadyl
			if (npcs[i].attackType == 1)
				return true;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id?
	 **/

	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private void killedBarrow(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowsNpcs.length; o++) {
				if (npcs[i].npcType == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.barrowsKillCount++;
				}
			}
		}
	}

	private void tzhaarDeathHandler(Client player, int i) {// hold a vit plz
		if (npcs[i] != null) {
			if (player != null) {
				if (player.getFightCave() != null) {
					if (isFightCaveNpc(i))
						killedTzhaar(player, i);
					if (npcs[i] != null && npcs[i].npcType == 3127)
						player.getFightCave().stop();
				}
			}
		}
	}

	/**
	 * Raises the count of tzhaarKilled, if tzhaarKilled is equal to the amount
	 * needed to kill to move onto the next wave it raises wave id then starts
	 * next wave.
	 * 
	 * @param i
	 *            The npc.
	 */
	private void killedTzhaar(Client player, int i) {
		if (player.getFightCave() != null) {
			player.getFightCave().setKillsRemaining(
					player.getFightCave().getKillsRemaining() - 1);
			if (player.getFightCave().getKillsRemaining() == 0) {
				player.waveId++;
				player.getFightCave().spawn();
			}
		}
	}

	/**
	 * Checks if something is a fight cave npc.
	 * 
	 * @param i
	 *            The npc.
	 * @return Whether or not it is a fight caves npc.
	 */
	public static boolean isFightCaveNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case Wave.TZ_KEK_SPAWN:
		case Wave.TZ_KIH:
		case Wave.TZ_KEK:
		case Wave.TOK_XIL:
		case Wave.YT_MEJKOT:
		case Wave.KET_ZEK:
		case Wave.TZTOK_JAD:
			return true;
		}
		return false;
	}

	public static boolean zulrahSpawns(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case 2042:
		case 2043:
		case 2044:
		case 2045:
			return true;
		}
		return false;
	}

	public void handleJadDeath(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		c.getItems().addItem(6570, 1);
		c.sendMessage("Congratulations on completing the fight caves minigame!");
		c.getPA().resetTzhaar();
		c.waveId = 300;
	}

	// id of bones dropped by npcs
	public int boneDrop(int type) {
		switch (type) {
		case 1:// normal bones
		case 9:
		case 100:
		case 12:
		case 17:
		case 803:
		case 18:
		case 81:
		case 101:
		case 41:
		case 19:
		case 90:
		case 75:
		case 86:
		case 78:
		case 912:
		case 913:
		case 914:
		case 457:
		case 1643:
		case 484:
		case 1624:
		case 181:
		case 119:
		case 104:
		case 26:
		case 1341:
			return 526;
		case 117:
			return 532;// big bones
		case 239:// drags
		case 53:
		case 259:
		case 265:
		case 260:
		case 1590:
		case 1591:
		case 1592:
			return 536;
		case 6593:
			return 11943;
		case 2052:
		case 415:
		case 1613:
		case 82:
		case 2054:
			return 592;
		case 2881:
		case 2882:
		case 2883:
			return 6729;
		default:
			return -1;
		}
	}

	public void appendKillCount(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] kcMonsters = { 122, 104, 3162, 2559, 2560, 2561, 2550, 2551,
					2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.killCount < 20) {
						// c.killCount++;
						// c.sendMessage("Killcount: " + c.killCount);
					} else {
						// c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
		case 995:
			switch (npcId) {
			case 1:
				return 50 + Misc.random(50);
			case 9:
				return 133 + Misc.random(100);
			case 1624:
				return 1000 + Misc.random(300);
			case 484:
				return 1000 + Misc.random(300);
			case 1643:
				return 1000 + Misc.random(300);
			case 413:
				return 1000 + Misc.random(1000);
			case 1613:
				return 1500 + Misc.random(1250);
			case 415:
				return 3000;
			case 18:
				return 500;
			case 101:
				return 60;
			case 913:
			case 912:
			case 914:
				return 750 + Misc.random(500);
			case 414:
				return 250 + Misc.random(500);
			case 457:
				return 250 + Misc.random(250);
			case 90:
				return 200;
			case 82:
				return 1000 + Misc.random(455);
			case 52:
				return 400 + Misc.random(200);
			case 104:
				return 1500 + Misc.random(2000);
			case 1341:
				return 1500 + Misc.random(500);
			case 26:
				return 500 + Misc.random(100);
			case 20:
				return 750 + Misc.random(100);
			//case 21:
			//	return 890 + Misc.random(125);
			case 117:
				return 500 + Misc.random(250);
			case 2607:
				return 500 + Misc.random(350);
			}
			break;
		case 11212:
			return 10 + Misc.random(4);
		case 565:
		case 561:
			return 10;
		case 560:
		case 563:
		case 562:
			return 15;
		case 555:
		case 554:
		case 556:
		case 557:
			return 20;
		case 892:
			return 40;
		case 886:
			return 100;
		case 6522:
			return 6 + Misc.random(5);

		}

		return 1;
	}

	/**
	 * Slayer Experience
	 **/
	public void appendSlayerExperience(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			if (c.getSlayer().isSlayerTask(npcs[i].npcType)) {
				c.taskAmount--;
				c.getPA().addSkillXP(npcs[i].MaxHP * Config.SLAYER_EXPERIENCE,
						18);
				if (c.taskAmount <= 0) {
					c.getPA().addSkillXP(
							(npcs[i].MaxHP * 8) * Config.SLAYER_EXPERIENCE, 18);
					int points = c.getSlayer().getDifficulty(c.slayerTask) * 5;
					c.slayerTask = -1;
					c.slayerPoints += points;
					c.sendMessage("You completed your slayer task. You obtain "
							+ points
							+ " slayer points. Please talk to Vanakka.");
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].underAttackBy2 == i)
					PlayerHandler.players[j].underAttackBy2 = 0;
		}
	}

	/**
	 * Npc names
	 **/

	public static String getNpcName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "None";
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcName();
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {

		case 6142: //PC Portals
		case 6143:
		case 6144:
		case 6145:
			return false;

		case 2892:
		case 2894:
			return false;
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}

		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}

		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY,
				distanceRequired(i)))
			return;
		if ((npcs[i].spawnedBy > 0)
				|| ((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE)
						&& (npcs[i].absX > npcs[i].makeX
								- Config.NPC_FOLLOW_DISTANCE)
								&& (npcs[i].absY < npcs[i].makeY
										+ Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY > npcs[i].makeY
												- Config.NPC_FOLLOW_DISTANCE))) {
			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					if (playerY < npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerY > npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX < npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX > npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX == npcs[i].absX
							|| playerY == npcs[i].absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
							break;

						case 1:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
							break;

						case 2:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;

						case 3:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						}
					}
					npcs[i].facePlayer(playerId);
					// if (checkClipping(i))
					handleClipping(i);
					npcs[i].getNextNPCMovement(i);
					/*
					 * else { npcs[i].moveX = 0; npcs[i].moveY = 0; }
					 */
					npcs[i].facePlayer(playerId);
					npcs[i].updateRequired = true;
				}
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX-playerX <= distance && objectX-playerX >= -distance) && (objectY-playerY <= distance && objectY-playerY >= -distance));
	}

	/**
	 * load spell
	 **/
	public void loadSpell2(int i) {
		npcs[i].attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; // red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; // green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; // white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; // blue
			npcs[i].endGfx = 428;
		}
	}

	public void loadProjectile(int i) {
		switch (npcs[i].npcType) {
		case 2044:
			npcs[i].projectileId = 1046;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 1046;
			break;
		case 3010:
			//npcs[i].projectileId = -1;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 369;
			npcs[i].freezeTimer = 15;
		break;
		case 2042:
			npcs[i].projectileId = 1044;
			npcs[i].attackType = 1;
			npcs[i].endGfx = 1044;
			break;
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = 1;
			break;
		case 239:
			int random = Misc.random(4);
			if (random == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
			} else if (random == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = 3;
			} else if (random == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = 3;
			} else if (random == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = 3;
			} else if (random == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = 0;
			}
			break;
		case 3129:// zamorak strike
			npcs[i].attackType = 2;
			npcs[i].endGfx = 78;
			break;
		case 6231:// arma
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1199;
			break;
		case 6227:// kilisa
			npcs[i].attackType = 0;
			break;
		case 2205: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = 0;
			break;
		case 2215:// bandos
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				npcs[i].attackType = 0;
			} else {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1200;
			}
			break;
		case 6203:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1211;
			}
			break;
			// arma npcs
		case 2561:
			npcs[i].attackType = 0;
			break;
		case 2560:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 3162:
			random = Misc.random(1);
			npcs[i].attackType = 1 + random;
			if (npcs[i].attackType == 1) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1198;
			}
			break;			
		case 1158:// kq first form
			int kqRandom = Misc.random(3);
			if (kqRandom == 2) {
				npcs[i].projectileId = 280; // gfx
				npcs[i].attackType = 2;
				npcs[i].endGfx = 279;
			} else {
				npcs[i].endGfx = -1;
				npcs[i].projectileId = -1;
				npcs[i].attackType = 0;
			}
			break;
		case 1160:// kq secondform
			int kqRandom2 = Misc.random(3);
			if (kqRandom2 == 2) {
				npcs[i].projectileId = 279; // gfx
				npcs[i].attackType = 1 + Misc.random(1);
				npcs[i].endGfx = 278;
			} else {
				npcs[i].endGfx = -1;
				npcs[i].projectileId = -1;
				npcs[i].attackType = 0;

			}
			break;

			/*
			 * Better Dragons
			 */
		case 5363: // Mithril-Dragon
		case 259: // Black-Dragon
		case 265: // Blue-Dragon
		case 1591:
		case 1592:
		case 260: // Green-Dragon
		case 53:
		case 1590:
		case 4682:
		case 3068:
		case 6593:
		case 5362:
			int random1 = Misc.random(3);
			switch (random1) {
			case 1:
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
				break;
			default:
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = 0;
				break;
			}
			break;

			// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = 0;
			break;
		case 2563: // star
			npcs[i].attackType = 0;
			break;
		case 2564: // growler
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
			// bandos npcs
			//		case 6260:
			//			random = Misc.random(2);
			//			if (random == 0 || random == 1)
			//				npcs[i].attackType = 0;
			//			else {
			//				npcs[i].attackType = 1;
			//				npcs[i].endGfx = 1211;
			//				npcs[i].projectileId = 288;
			//			}
			//			break;
		case 2881:// supreme
			npcs[i].attackType = 1;
			npcs[i].projectileId = 298;
			break;

		case 2882:// prime
			npcs[i].attackType = 2;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;
		case 2551:
			npcs[i].attackType = 0;
			break;
		case 2552:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1206;
			break;
		case 1672:
			npcs[i].attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;

		case 1675:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 27;
			break;

		case 2054:
		//	Client c2 = (Client) PlayerHandler.players[i];
			int r2 = Misc.random(2);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
		//		c2.getPA().movePlayer(3262 + Misc.random(10), 3902 + Misc.random(10), 0);
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 3127:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, npcs[i].size,
					PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}
			break;
		case 2743:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case 2631:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 443;
			break;
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		int distanceNeeded = 1;
		if (NPCHandler.npcs[i].attackType == 1) {
			return distanceNeeded += 7;
		} else if (NPCHandler.npcs[i].attackType == 2) {
			return distanceNeeded += 9;
		} else if (NPCHandler.npcs[i].attackType > 2) {
			return distanceNeeded += 4;
		}
		switch (npcs[i].npcType) {
		case 2042:
		case 2043:
		case 2044:
			return distanceNeeded +=15;
		case 494:
			return distanceNeeded +=25;
		case 6256:
		case 6257:
		case 2631 : // Tok-Xil (Tzhaar ranging guy)
		case 1183 : // Elf ranger
		case 1675: // Karil
			return distanceNeeded += 7;
		case 260 : // Green drag
		case 239:
		case 2562:
		case 6593:
			return distanceNeeded += 5;
		case 907 : // Kolodian
		case 908 :
		case 909 :
		case 910 :
		case 911 :
		case 912 : // Zammy battlemage
		case 913 : // Sara battlemage
		case 914 : // Guthix battlemage
		case 2881:// dag kings
		case 2882:
		case 2054:// chaos ele
		case 1158 : // Kalphite queen form 1
		case 1160 : // Kalphite queen form 2
		case 1672 : // Ahrim
		case 2743: //Ket-Zek (Tzhaar mage guy)
		case 3127: //TzTok-Jad
			return distanceNeeded += 9;
		case 2883:// rex
			return distanceNeeded;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 3162:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return distanceNeeded += 8;
		case 8133:
		case 6222:
		case 6223:
		case 6227:
		case 6225:
			return distanceNeeded += 9;
			// things around dags
		case 2892:
		case 2894:
			return distanceNeeded += 9;
		default:
			return distanceNeeded;
		}
	}

	public int followDistance(int i) {
		switch (npcs[i].npcType) {

		case 6142: //PC Portals
		case 6143:
		case 6144:
		case 6145:
		case 494:
		case 2042:
		case 2043:
		case 2044:
			return -1;
		case 3731: //Splatter
		case 3740: //Shifter
		case 3746: //Ravenger
		case 3750: //Spinner
		case 3754: //Torcher
		case 3770: //Defiler
		case 3776: //Brawler
			return 10;

		case 3732:
		case 3772:
		case 3762:
		case 3742:
		case 3752:
			return 15;
		case 2215:
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;
		case 4173:
		case 4172:
			return 20;
		case 4176:
			return 10;
		}
		return 0;

	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 2054:
			return 85;
		case 2044:
			return 90;
		case 3127:
			return 130;

		case 239:
			return 90;

		case 1672:
			return 85;

		case 1675:
			return 80;

		default:
			return 85;
		}
	}

	/**
	 * NPC Attacking Player
	 **/

	public void attackPlayer(Client c, int i) {
		if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
			return;
		}
		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0
					&& npcs[i].underAttackBy != c.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			
			if (!npcs[i].inMulti()
					&& (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			if (c.hasPin) {
				return;
			}
			npcs[i].facePlayer(c.playerId);
			boolean special = false;// specialCase(c,i);
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), npcs[i].size,
					c.getX(), c.getY(), distanceRequired(i))
					|| special) {
				if (npcs[i].npcType == 494)
					npcs[i].attackType = 2;
				if (c.respawnTimer <= 0) {
					npcs[i].facePlayer(c.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;
					if (special)
						loadSpell2(i);
					else
						loadProjectile(i);
					if (npcs[i].attackType == 3)
						npcs[i].hitDelayTimer += 2;
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						startAnimation(getAttackEmote(i), i);
						if (Config.SOUND) {
							c.getPA().sendSound(Sounds
									.getNpcAttackSounds(npcs[i].npcType));
						}
						npcs[i].oldIndex = c.playerId;
						return;
					}
					if (npcs[i].projectileId > 0) {
						int nX = NPCHandler.npcs[i].getX() + offset(i);
						int nY = NPCHandler.npcs[i].getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						c.getPA().createPlayersProjectile(nX, nY, offX, offY,
								50, getProjectileSpeed(i),
								npcs[i].projectileId, 43, 31, -c.getId() - 1,
								65);
					}
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = c.playerId;
					startAnimation(getAttackEmote(i), i);
					c.getPA().removeAllWindows();
					if (c.teleporting) {
						c.startAnimation(65535);
						c.teleporting = false;
						c.isRunning = false;
						c.gfx0(-1);
						c.startAnimation(-1);
					}
				}
			}
		}
	}

	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 239:
			return 2;
		case 2881:
		case 2882:
			return 1;
		case 3127:
		case 2743:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Client c, int i) { // responsible for npcs that
		// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), npcs[i].size,
				c.getX(), c.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), npcs[i].size,
						c.getX(), c.getY(), distanceRequired(i)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780
				&& !(npcType >= 2440 && npcType <= 2446);
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			Client c = (Client) PlayerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0
					&& c.oldNpcIndex == 0) {
				c.startAnimation(c.getCombat().getBlockEmote());
			}
			if (c.respawnTimer <= 0) {
				int damage = 0;
				if (npcs[i].attackType == 0) {
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (npcs[i].npcType == 2043 && c.prayerActive[18]) { // protect from melee
						damage = Misc.random(npcs[i].maxHit) / 2;
					}
					if (c.prayerActive[18]) { // protect from melee
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 1) { // range
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (npcs[i].npcType == 2042 && c.prayerActive[17]) { // protect from rnge
						damage = Misc.random(npcs[i].maxHit) / 2;
					}
					if (c.prayerActive[17]) { // protect from range
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 2) { // magic
					damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
						magicFailed = true;
					}
					if (npcs[i].npcType == 3010) {
						damage = 50;
					}
					if (c.prayerActive[16] && !(npcs[i].npcType == 3010)) { // protect from magic
						damage = 0;
						magicFailed = true;
					}
					if (npcs[i].npcType == 2044 && c.prayerActive[16]) { // protect from mage
						damage = Misc.random(npcs[i].maxHit) / 2;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					if (npcs[i].endGfx > 0
							&& (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
				}

				if (npcs[i].attackType == 3) { // fire breath
					int anti = c.getPA().antiFire();
					if (anti == 0) {
						damage = Misc.random(30) + 10;
						c.sendMessage("You are badly burnt by the dragon fire!");
					}

					if (c.playerEquipment[c.playerShield] == 11284
							&& c.dfsCharge <= 49) {
						c.dfsCharge += 1;
						c.gfx0(1164);
						c.startAnimation(6695);
						c.sendMessage("Your dragonfire Shield Absorbs the Dragon breath");
					}
					if (c.playerEquipment[c.playerShield] == 11283
							&& c.dfsCharge <= 49) {
						c.dfsCharge += 1;
						c.startAnimation(6696);
						c.gfx0(1164);
						c.sendMessage("Your dragonfire Shield Absorbs the Dragon breath.");
					}

					else if (anti == 1)
						damage = Misc.random(12);
					else if (anti == 2)
						damage = Misc.random(6);
					if (c.playerLevel[3] - damage < 0)
						damage = c.playerLevel[3];
					// c.gfx100(npcs[i].endGfx);
				}
				handleSpecialEffects(c, i, damage);
				c.logoutDelay = System.currentTimeMillis(); // logout delay
				// c.setHitDiff(damage);
				c.handleHitMask(damage);
				c.playerLevel[3] -= damage;
				c.getPA().refreshSkill(3);
				Player.updateRequired = true;
				// c.setHitUpdateRequired(true);
			}
		}
	}

	public void handleSpecialEffects(Client c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
						c.getPA().appendPoison(12);
					}
				}
			}
		}

	}
	public static void relocate(NPC npc, int x, int y, int height) {
		npc.makeX = 0; 
		npc.makeY = 0;
		npc.updateRequired = true;
		TaskHandler.submit(new Task(3, false) {
			@Override
			public void execute() {
				npc.absX = x; 
				npc.absY = y; 
				npc.heightLevel = height;
				npc.updateRequired = true;
				this.cancel();
			}
			@Override
			public void onCancel() {
				System.out.println("NPC moved.");
			}
		});
	}
	public static void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}

	public NPC[] getNPCs() {
		return npcs;
	}

	public boolean goodDistance(int npcX, int npcY, int npcSize, int playerX,
			int playerY, int distance) {
		return playerX >= (npcX - distance)
				&& playerX <= (npcX + npcSize + distance)
				&& playerY >= (npcY - distance)
				&& playerY <= (npcY + npcSize + distance);
	}

	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
		case 3162:
			if (npcs[i].attackType == 2)
				return 28;
			else
				return 68;
		case 2562:
			return 31;
		case 3010: 
			if(npcs[i].attackType == 2)
				return 5;
			break;
			
		case 2215:
			if(npcs[i].attackType == 1)
				return 35;
			else
			return 60;
		}
		return 1;
	}
	public boolean loadAutoSpawn(String FileName) throws IOException {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			characterfile.close();
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])),
							Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]),
							Integer.parseInt(token3[7]));

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public static int getNpcListHP(int npcId) {
		if (npcId <= -1 || npcId > NPCDefinitions.getDefinitions().length) {
			return 0;
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return 0;
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcHealth();

	}

	public String getNpcListName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "None";
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcName();
	}

	public boolean loadNPCList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./"
					+ FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			try {
				characterfile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]), Integer.parseInt(token3[3]));
					npcCombat[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public static void spawnNpc2(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}
	public static int spawnNpc3(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return slot;
	}
	public static int spawnNpc4(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence,int maxHP) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = maxHP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return slot;
	}
	public static NPC summonPet(Client player, int npcType, int x, int y,
			int heightLevel) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		NPC newNPC = new NPC(slot, npcType);
		NPCHandler.npcs[slot] = newNPC;
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = 0;
		newNPC.HP = 0;
		newNPC.MaxHP = 0;
		newNPC.maxHit = 0;
		newNPC.attack = 0;
		newNPC.defence = 0;
		newNPC.spawnedBy = player.getId();
		newNPC.underAttack = true;
		newNPC.facePlayer(player.getId());
		newNPC.summoned = true;
		newNPC.summonedBy = player.getId();
		player.petID = npcType;
		player.petIndex = player.getId();
		player.setPetSummoned(true);
		return newNPC;
	}
	public static NPC spawnZulrah(Client c, int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence, int anim, String chat) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		NPC newNPC = new Zulrah(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.respawns = false;
		newNPC.needRespawn = false;
		c.bossSlot = slot;
		newNPC.isAggressive(npcType);
		npcs[slot] = newNPC;
		newNPC.forceAnim(anim);
		newNPC.forceChat(chat);
		return newNPC;
	}

	public static NPC spawnSnakeling(Client c, int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence, String chat) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.respawns = false;
		newNPC.needRespawn = false;
		newNPC.isAggressive(npcType);
		npcs[slot] = newNPC;
		newNPC.forceChat(chat);
		return newNPC;
	}
}
