package wind.model.players.packets.clicking.objects;

import wind.Config;
import wind.Server;
import wind.event.CycleEvent;
import wind.event.CycleEventContainer;
import wind.event.CycleEventHandler;
import wind.model.npcs.NPCHandler;
import wind.model.objects.Doors;
import wind.model.objects.Object;
import wind.model.players.Client;
import wind.model.players.Flax;
import wind.model.players.Rights;
import wind.model.players.content.minigames.CastleWarObjects;
import wind.model.players.content.minigames.PestControl;
import wind.model.players.content.minigames.Sailing;
import wind.model.players.content.minigames.WallSafe;
import wind.model.players.content.minigames.WarriorsGuild;
import wind.model.players.content.skills.impl.Agility;
import wind.model.players.content.skills.impl.Mining;
import wind.model.players.content.skills.impl.Runecrafting;
import wind.model.players.content.skills.impl.farming.herbs.HerbPicking;
import wind.model.players.content.skills.impl.farming.trees.TreePicking;
import wind.model.players.content.skills.impl.woodcutting.Woodcutting;
import wind.util.ScriptManager;

public class FirstClickObject {

	public static void handleClick(Client c, int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		if (Woodcutting.playerTrees(c, objectType)) {
			Woodcutting.attemptData(c, objectType, obX, obY);
			return;
		}
		if (Mining.miningRocks(c, objectType)) {
			Mining.mine(c, objectType, obX, obY);
			return;
		}
		if (Agility.agilityObject(c, objectType)) {
			Agility.perform(c, objectType);
		}
		if (c.goodDistance(c.getX(), c.getY(), c.objectX, c.objectY, 1)) {
			if (Doors.getSingleton().handleDoor(c.objectId, c.objectX,
					c.objectY, c.heightLevel)) {
				return;
			}
		}
		// castlewars
		if (CastleWarObjects.handleObject(c, objectType, obX, obY))
			return;
		Runecrafting rc = new Runecrafting();
		switch (objectType) {
		case 24318:
			if ((c.playerLevel[0] + c.playerLevel[2]) < 130) {
				c.sendMessage("@red@You need a combined strength and attack of level 130!");
				return;
			} else {
				if (c.getX() == 2877) {
					c.getPA().movePlayer(2876, 3546, 0);
				} else if (c.getX() == 2876) {
					c.getPA().movePlayer(2877, 3546, 0);
				}
			}
			break;
		case 26415:
			if (c.playerLevel[2] > 59) {
				if (c.getY() < 3717) {
					c.getPA().movePlayer(2898, 3719, 0);
				} else {
					c.getPA().movePlayer(2898, 3715, 0);
				}
			} else {
				c.sendMessage("You need a strength level of 60 to move this boulder!");
			}
			break;
		case 26419:
			c.getPA().movePlayer(2881, 5311, 2);
			break;
		case 26370:
			c.getPA().movePlayer(2916, 3745, 0);
			break;
		case 23271:
			if (c.absY > 3520) {
			    c.getPA().ditchJump(c, 0, - 3);
			}
			if (c.absY <= 3518) {
			    c.getPA().ditchJump(c, 0, 5);
			}
			if (c.absY == 3520) {
			    c.getPA().ditchJump(c, 0, 3);
			}
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {@Override
			    public void execute(CycleEventContainer container) {
			        c.getPA().resetDitchJump(c);
			        container.stop();
			        c.getPA().resetAnimation();
			    }@Override
			    public void stop() {
			    	c.getPA().resetAnimation();

			    }
			}, 4);
			break;
		case 21735:
			if (Woodcutting.hasAxe(c)) {
			if (c.getX() > 2694) {
				c.getPA().movePlayer(2693, 9482,  0);
			} else {
				c.getPA().movePlayer(2695, 9482,  0);
			}
			} else {
				c.sendMessage("You need an axe to pass these vines.");
			}
			break;
		case 21734:
			if (Woodcutting.hasAxe(c)) {
			if (c.getX() > 2675) {
				c.getPA().movePlayer(2674, 9479, 0);
			} else {
				c.getPA().movePlayer(2676, 9479, 0);
			}
			} else {
				c.sendMessage("You need an axe to pass these vines.");
			}
			break;
		case 21733:
			if (Woodcutting.hasAxe(c)) {
			if (c.getX() > 2673) {
				c.getPA().movePlayer(2672, 9499, 0);
			} else {
				c.getPA().movePlayer(2674, 9499, 0);
			}
			} else {
				c.sendMessage("You need an axe to pass these vines.");
			}
			break;
		case 21731:
			if (Woodcutting.hasAxe(c)) {
			if (c.getX() > 2690) {
				c.getPA().movePlayer(2689, 9564, 0);
			} else {
				c.getPA().movePlayer(2691, 9564, 0);
			}
			} else {
				c.sendMessage("You need an axe to pass these vines.");
			}
			break;
		case 20878:
			c.getPA().movePlayer(2744, 3152, 0);
			break;
		case 20877:
			if (c.getItems().playerHasItem(995, 875)) {
			c.getPA().movePlayer(2713, 9564, 0);
			c.getItems().deleteItem2(995, 875);
			} else {
				c.sendMessage("You must have 875 coins to enter this cave!");
			}
			break;
		case 20882:
			if (c.playerLevel[16] < 43) {
				c.sendMessage("You need at least level 43 agility to pass this obstacle.");
			} else {
				c.getPA().movePlayer(2687, 9506, 0);
			}
			break;
		case 20884:
			if (c.playerLevel[16] < 43) {
				c.sendMessage("You need at least level 43 agility to pass this obstacle.");
			} else {
			c.getPA().movePlayer(2682, 9506, 0);
			}
		break;
		case 21739:
			if (c.playerLevel[16] < 43) {
				c.sendMessage("You need at least level 43 agility to pass this obstacle.");
			} else {
			c.getPA().movePlayer(2649, 9562, 0);
			}
		break;
		case 21738:
			if (c.playerLevel[16] < 43) {
				c.sendMessage("You need at least level 43 agility to pass this obstacle.");
			} else {
			c.getPA().movePlayer(2647, 9557, 0);
			}
		break;

		case 8929:
			c.getPA().movePlayer(2442, 10147, 0);
			break;
		case 11804:
			c.getPA().movePlayer(3232, 3401, 0);
			break;
		case 11803:
			c.getPA().movePlayer(3232, 9801, 0);
			break;
		case 20668:
			if (c.getY() == 9718 && c.getX() == 3556) {
				c.getPA().movePlayer(3574, 3297, 0);
			}
			break;
		case 24222:
			if (c.getX() == 2936) {
				c.startAnimation(839);
				c.getPA().turnTo(2935, 3355);
				c.getPA().movePlayer(2935, 3355, 0);
			} else if (c.getX() == 2934) {
				c.startAnimation(839);
				c.getPA().turnTo(2936, 3355);
				c.getPA().movePlayer(2936, 3355, 0);
			} else {
				c.sendMessage("Stand next to the bricks.");
			}
			break;
		case 16680:
			c.getPA().movePlayer(2885, 9797, 0);
			break;
		case 15653:
			c.getPA().movePlayer(2732, 3380, 1);
			break;
		case 16683:
			c.startAnimation(828);
			c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel + 1);
			break;
		case 7134:
			Flax.pickFlax(c, obX, obY);
		break;
		case 16679:
		case 2884:
		case 16684:
			c.startAnimation(828);
			c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel - 1);
			break;
		case 15654:
			c.getPA().movePlayer(2732, 3376, 0);
			break;
		case 11836:
			c.getPA().movePlayer(2862, 9572, 0);
			break;
		case 11835:
			c.getPA().movePlayer(2480, 5175, 0);
			break;
		case 18969:
			c.getPA().movePlayer(2857, 3167, 0);
			break;
		case 11441:
			c.getPA().movePlayer(2855, 9569, 0);
			break;
		case 20672:
			c.getPA().movePlayer(3557, 3297, 0);
			break;
		case 20667:
			c.getPA().movePlayer(3565, 3290, 0);
			break;
		case 20669:
			c.getPA().movePlayer(3577, 3280, 0);
			break;
		case 20670:
			c.getPA().movePlayer(3566, 3277, 0);
			break;
		case 20671:
			c.getPA().movePlayer(3553, 3283, 0);
			break;
		case 2120:
				c.getPA().movePlayer(3412, 3541, 1);
			break;
		case 2119:
			c.getPA().movePlayer(3417, 3541, 2);
			break;
		case 2118:
			c.getPA().movePlayer(3438, 3537, 0);
			break;
		case 2114:
			c.getPA().movePlayer(3433, 3537, 1);
			break;
		case 2123:
			c.getPA().movePlayer(2808, 10002, 0);
			break;
		case 2141:
			c.getPA().movePlayer(2796, 3615, 0);
			break;
		case 16537:
			if (c.playerLevel[16] < 59) {
				c.sendMessage("You need at least level 59 agility to climb this chain.");
			} else {
					c.getPA().movePlayer(3423, 3550, 1);
					c.getPA().addSkillXP(100, 16);
			}
			break;
		case 16538:
			if (c.playerLevel[16] < 59) {
				c.sendMessage("You need at least level 59 agility to climb this chain.");
			} else {
					c.getPA().movePlayer(3423, 3550, 0);
					c.getPA().addSkillXP(3000, 16);
			}
			break;

		/*
		 * case 23271: if (c.absY > 3520) { c.getPA().ditchJump(c, 0, - 3); } if
		 * (c.absY <= 3518) { c.getPA().ditchJump(c, 0, 5); } if (c.absY ==
		 * 3521) { c.getPA().ditchJump(c, 0, 3); } c.getPA().resetDitchJump(c);
		 * break;
		 */
		case 26380:
				if (c.getY() == 5279) {
					c.getPA().movePlayer(2872, 5269, 2);
				} else if (c.getY() == 5269) {
					c.getPA().movePlayer(2872, 5279, 2);
				}
			break;

		case 26503:
			if (c.getX() == 2862) {
				if (c.bandosKills >= 20) {
					c.getPA().movePlayer(2863, 5354, 2);
					c.bandosKills -= 20;
				} else {
					c.sendMessage("You need a killcount of 20 to enter.");
				}
			} else if (c.getX() == 2864) {
				c.getPA().movePlayer(2862, 5354, 2);
			}
			break;
		case 1804:
			if (!c.getItems().playerHasItem(983)) {
				c.sendMessage("This door requires a brass key to open.");
				return;
			}
			if (c.getItems().playerHasItem(983) && c.getY() == 3449) {
				c.getPA().movePlayer(3115, 3450, 0);
			} else if (c.getY() == 3450 && c.getItems().playerHasItem(983)) {
				c.getPA().movePlayer(3115, 3449, 0);
			}
			break;

		case 26461: // Godwars Bandos Door. Hammer = 2347
			if (!c.getItems().playerHasItem(2347)) {
				c.sendMessage("This door requires a hammer to open.");
				return;
			}
			if (c.getX() == 2851) {
				c.getPA().movePlayer(2850, 5333, 2);

			} else if (c.getX() == 2850) {
				c.getPA().movePlayer(2851, 5333, 2);
			}
			break;

		case 8958:
			if (c.getX() == 2490 && (c.getY() == 10164 || c.getY() == 10162)) {
				if (c.getPA().checkForPlayer(2490, c.getY() == 10164 ? 10162 : 10164)) {
					new Object(6951, c.objectX, c.objectY, c.heightLevel, 1, 10, 8958, 15);
				}
			}
			if (c.getX() == 2492 && c.getY() == 10147) {
				c.getPA().movePlayer(2490, 10147, 0);
			}
			break;

		case 8959:
			if (c.getX() == 2490 && (c.getY() == 10146 || c.getY() == 10148)) {
				if (c.getPA().checkForPlayer(2490, c.getY() == 10146 ? 10148 : 10146)) {
					new Object(6951, c.objectX, c.objectY, c.heightLevel, 1, 10, 8959, 15);
				}
			}
			if (c.getX() == 2492 && c.getY() == 10147) {
				c.getPA().movePlayer(2490, 10147, 0);
			}
			break;

		/*
		 * Agility
		 */
		case 2492:
			if (c.killCount >= 20) {
				c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin", "Zamorak");
				c.dialogueAction = 20;
			} else {
				c.sendMessage("You need 20 kill count before teleporting to a boss chamber.");
			}
			break;
		case 2288:
			break;
		case 2309:
			if (c.getX() == 2998 && c.getY() == 3916) {
				c.getAgility().doWildernessEntrance(c);
			}
			break;
		case 23145:
			if (c.getX() == 2474 && c.getY() == 3436) {
				c.getAgility().doGnomeLog(c);
			}
			break;
		case 23134: // NET1
			c.getAgility().doGnomeNet1(c);
			break;
		case 23559: // BRANCH1
			c.getAgility().doGnomeBranch1(c);
			break;
		case 23557: // ROPE
			if (c.getX() == 2477 && c.getY() == 3420) {
				c.getAgility().doGnomeRope(c);
			}
			break;

		case 23560: // BRANCH2
			c.getAgility().doGnomeBranch2(c);
			break;
		case 23135: // NET2
			c.getAgility().doGnomeNet2(c);
			break;
		case 23138: // PIPE1
			if (c.getX() == 2484 && c.getY() == 3430) {
				c.getAgility().doGnomePipe1(c);
			}
			break;
		case 23139: // PIPE2
			if (c.getX() == 2487 && c.getY() == 3430) {
				c.getAgility().doGnomePipe2(c);
			}
			break;
		/*
		 * END OF AGILITY
		 */

		case 1816:
			c.objectDistance = 0;
			c.getPA().startTeleport2(2271, 4680, 0);
			break;

		// castlewars
		case 4467:
			c.getPA().movePlayer(c.absX == 2834 ? 2385 : 2384, 3134, 0);
			break;

		case 4427:
			c.getPA().movePlayer(2373, c.absY == 3120 ? 3119 : 3120, 0);
			break;

		case 4428:
			c.getPA().movePlayer(2372, c.absY == 3120 ? 3119 : 3120, 0);
			break;

		case 4465:
			c.getPA().movePlayer(c.absX == 2414 ? 2415 : 2414, 3073, 0);
			break;

		case 4424:
			c.getPA().movePlayer(2427, c.absY == 3087 ? 3088 : 3087, 0);
			break;

		case 4423:
			c.getPA().movePlayer(2427, c.absY == 3087 ? 3088 : 3087, 0);
			break;
		case 4411:
		case 4415:
		case 4417:
		case 4418:
		case 4420:
		case 4469:
		case 4470:
		case 4419:
		case 4911:
		case 4912:
		case 1747:
		case 1757:
		case 4437:
		case 6281:
		case 6280:
		case 4472:
		case 4471:
		case 4406:
		case 4407:
		case 4458:
		case 4902:
		case 4903:
		case 4900:
		case 4901:
		case 4461:
		case 4463:
		case 4464:
		case 4377:
		case 4378:
			CastleWarObjects.handleObject(c, objectType, obX, obY);
		case 1568:
			if (obX == 3097 && obY == 3468)
				c.getPA().movePlayer(3097, 9868, 0);
			else
				CastleWarObjects.handleObject(c, obY, obY, obY);
			break;
		// end

		case 3742:
			c.sendMessage("@red@BEWARE: Stepping past this sign players can kill you.");
			break;

		case 1766:
			c.getPA().movePlayer(3016, 3849, 0);
			break;

		case 1765:
			c.getPA().movePlayer(3067, 10256, 0);
			break;

		case 2465:
			c.getPA().movePlayer(3056, 4967, 1);
			break;

		case 7236:
			if (System.currentTimeMillis() - c.lastThieve < 2500)
				return;
			c.lastThieve = System.currentTimeMillis();
			c.turnPlayerTo(c.objectX, c.objectY);
			WallSafe.getRandomReward(c);
			break;

		/** Farming */

		case 7960:
		case 8018:
		case 8076:
		case 8045:
		case 8103:
		case 8501:
		case 8461:
		case 8534:
		case 8434:
			TreePicking.removeDeadTree(c);
			break;

		case 8091:
		case 7948:
		case 8006:
		case 8064:
		case 8033:
		case 8488:
		case 8444:
		case 8513:
		case 8409:
			TreePicking.treePicking(c, c.currentTree);
			break;

		case 8143:
			HerbPicking.herbPicking(c, c.currentHerb);
			break;

		case 18987:
			c.startAnimation(828);
			c.getPA().movePlayer(2271, 4680, 0);
			break;
		case 1817:
			c.getPA().startTeleport2(3017, 3850, 0);
			break;

		case 20772: // verac
			if (c.barrowsNpcs[0][1] == 0) {
				NPCHandler.spawnNpc(c, 1677, c.getX(), c.getY() - 1, 3, 0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;

			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
				return;
			}
			break;

		case 20721: //torag
			if (c.barrowsNpcs[1][1] == 0) {
				NPCHandler.spawnNpc(c, 1676, c.getX() + 1, c.getY(), 3, 0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20771: //karil
			if (c.barrowsNpcs[2][1] == 0) {
				NPCHandler.spawnNpc(c, 1675, c.getX(), c.getY() - 1, 3, 0, 90, 20, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20722: //guth
			if (c.barrowsNpcs[3][1] == 0) {
				NPCHandler.spawnNpc(c, 1674, c.getX(), c.getY() - 1, 3, 0, 120, 24, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20720: // dh
			c.getDH().sendDialogues(996, 1673);
			break;

		case 20770:
			if (c.barrowsNpcs[5][1] == 0) { // ahrims
				NPCHandler.spawnNpc(c, 1672, c.getX() - 1, c.getY(), 3, 0, 90, 25, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		/* Warriors Guild */
		case 15638:
			if (c.getX() <= 2850) {
				c.getPA().movePlayer(2841, 3538, c.heightLevel);
				c.heightLevel = 0;
			} else {
				c.getPA().startTeleport2(3252, 3400, 0);
			}
			break;
		case 15644:
		case 15641:
			if (c.heightLevel == 2) {
				if (c.absX == 2846) {
					WarriorsGuild.enterRoom(c);
				} else {
					c.inDefenderRoom = false;
					// CycleEventHandler.getSingleton().stopEvents(c);
					c.getPA().movePlayer(2846, 3540, c.heightLevel);
				}
			} else {
				if (c.getY() == 3545) {
					c.getPA().movePlayer(c.getX(), c.getY() + 1, c.heightLevel);
				} else {
					c.getPA().movePlayer(c.getX(), c.getY() - 1, c.heightLevel);
				}
			}
			break;
		/* End of Warriors Guild */

		case 9398:// deposit
		case 26969:
			c.getPA().sendFrame126("The Bank of " + Config.SERVER_NAME + " - Deposit Box", 7421);
			c.getPA().sendFrame248(4465, 197);
			c.getItems().resetItems(7423);
			break;
		case 1733:
			c.getPA().movePlayer(c.absX, c.absY + 6393, 0);
			break;
		case 1734:
			c.getPA().movePlayer(c.absX, c.absY - 6396, 0);
			break;

		/**
		 * Pest Control
		 */
		case 14314:
			if (c.inPcBoat())
				PestControl.leaveWaitingBoat(c);
			c.getPA().walkableInterface(-1);
			break;
		case 14315:
			if (c.absX == 2657 && c.absY == 2639 && c.teleTimer <= 0)
				PestControl.addToWaitRoom(c);
			break;

		case 14235:
		case 14233:
			if (c.objectX == 2670) {
				if (c.absX <= 2670) {
					c.absX = 2671;
				} else {
					c.absX = 2670;
				}
			}
			if (c.objectX == 2643) {
				if (c.absX >= 2643) {
					c.absX = 2642;
				} else {
					c.absX = 2643;
				}
			}
			if (c.absX <= 2585) {
				c.absY += 1;
			} else {
				c.absY -= 1;
			}
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		/**
		 * End of Pest Control
		 */

		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
			break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
			break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
			break;
		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		case 1742: // Miscellania stairs
			if (c.absX == 2506 && c.absY == 3870) {
				c.getPA().movePlayer(2506, 3870, 1);
			}
			if (c.absX == 2506 && c.absY == 3850) {
				c.getPA().movePlayer(2506, 3850, 1);
			}
			break;

		case 1743: // Miscellania stairs
			if (c.absX == 2506 && c.absY == 3870) {
				c.getPA().movePlayer(2506, 3870, 0);
			}
			if (c.absX == 2505 && c.absY == 3850) {
				c.getPA().movePlayer(2505, 3850, 0);
			}
			break;

		// Godwars Door
		case 26426:
			if (c.getRights().greaterOrEqual(Rights.DONATOR)) {
				if (c.armaKills < 5) {
					c.sendMessage("You need 5 Armadyl kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2839, 5296, 6);
			} else if (c.getRights().equals(Rights.DONATOR)) {
				if (c.armaKills < 15) {
					c.sendMessage("You need 15 Armadyl kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2839, 5296, 6);
			}
			break;
		case 26425:
			if (c.getRights().greaterOrEqual(Rights.DONATOR)) {
				if (c.bandosKills < 5) {
					c.sendMessage("You need 5 Bandos kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2864, 5354, 6);
			} else if (c.getRights().equals(Rights.PLAYER)) {
				if (c.bandosKills < 15) {
					c.sendMessage("You need 15 Bandos kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2864, 5354, 6);
			}
			break;
		case 26428:
			if (c.getRights().greaterOrEqual(Rights.DONATOR)) {
				if (c.zamorakKills < 5) {
					c.sendMessage("You need 5 Zamorak kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2925, 5331, 6);
			} else if (c.getRights().equals(Rights.PLAYER)) {
				if (c.zamorakKills < 15) {
					c.sendMessage("You need 15 Zamorak kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2925, 5331, 6);
			}
			break;
		case 26427:
			if (c.getRights().greaterOrEqual(Rights.DONATOR)) {
				if (c.saraKills < 5) {
					c.sendMessage("You need 5 Saradomin kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2907, 5265, 4);
			} else if (c.getRights().equals(Rights.PLAYER)) {
				if (c.saraKills < 15) {
					c.sendMessage("You need 15 Saradomin kills to enter the boss' chamber.");
					return;
				}
				c.getPA().movePlayer(2907, 5265, 4);
			}
			break;

		case 5960:
			if (c.leverClicked == false) {
				c.getDH().sendDialogues(114, 9985);
				c.leverClicked = true;
			} else {
				c.getPA().startLeverTeleport(3090, 3956, 0, "lever");
			}
			break;
		case 3223:
		case 21764:
		case 411:
		case 26289:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				// c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			}
			c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			c.startAnimation(645);
			c.specAmount = 10.0;
			c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
			c.sendMessage("@red@You've recharged your special attack, prayer points and hitpoints.");
			break;
		case 5959:
			c.getPA().startLeverTeleport(2539, 4712, 0, "lever");
			break;
		case 1814:
			c.getPA().startLeverTeleport(3153, 3923, 0, "lever");
			break;
		case 1815:
			c.getPA().startLeverTeleport(3087, 3500, 0, "lever");
			break;
		/* Start Brimhavem Dungeon */
		case 2879:
			c.getPA().movePlayer(2542, 4718, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5083:
			c.getPA().movePlayer(2713, 9564, 0);
			c.sendMessage("You enter the dungeon.");
			break;

		case 5103:
			if (c.absX == 2691 && c.absY == 9564) {
				c.getPA().movePlayer(2689, 9564, 0);
			} else if (c.absX == 2689 && c.absY == 9564) {
				c.getPA().movePlayer(2691, 9564, 0);
			}
			break;

		case 5106:
			if (c.absX == 2674 && c.absY == 9479) {
				c.getPA().movePlayer(2676, 9479, 0);
			} else if (c.absX == 2676 && c.absY == 9479) {
				c.getPA().movePlayer(2674, 9479, 0);
			}
			break;

		case 5105:
			if (c.absX == 2672 && c.absY == 9499) {
				c.getPA().movePlayer(2674, 9499, 0);
			} else if (c.absX == 2674 && c.absY == 9499) {
				c.getPA().movePlayer(2672, 9499, 0);
			}
			break;

		case 5107:
			if (c.absX == 2693 && c.absY == 9482) {
				c.getPA().movePlayer(2695, 9482, 0);
			} else if (c.absX == 2695 && c.absY == 9482) {
				c.getPA().movePlayer(2693, 9482, 0);
			}
			break;

		case 5104:
			if (c.absX == 2683 && c.absY == 9568) {
				c.getPA().movePlayer(2683, 9570, 0);
			} else if (c.absX == 2683 && c.absY == 9570) {
				c.getPA().movePlayer(2683, 9568, 0);
			}
			break;

		case 5100:
			if (c.absY <= 9567) {
				c.getPA().movePlayer(2655, 9573, 0);
			} else if (c.absY >= 9572) {
				c.getPA().movePlayer(2655, 9566, 0);
			}
			break;

		case 5099:
			if (c.absY <= 9493) {
				c.getPA().movePlayer(2698, 9500, 0);
			} else if (c.absY >= 9499) {
				c.getPA().movePlayer(2698, 9492, 0);
			}
			break;

		case 5088:
			if (c.absX <= 2682) {
				c.getPA().movePlayer(2687, 9506, 0);
			} else if (c.absX >= 2687) {
				c.getPA().movePlayer(2682, 9506, 0);
			}
			break;
		case 4031:
			if (c.absY <= 3116 || c.absY == 3115) {
				c.getPA().movePlayer(3304, 3117, 0);
			} else if (c.absY >= 3116 || c.absY == 3117) {
				c.getPA().movePlayer(3304, 3115, 0);
			}
			break;

		case 5110:
			c.getPA().movePlayer(2647, 9557, 0);
			c.sendMessage("You cross the lava");
			break;

		case 5111:
			c.getPA().movePlayer(2649, 9562, 0);
			c.sendMessage("You cross the lava");
			break;

		case 5084:
			c.getPA().movePlayer(2744, 3151, 0);
			c.sendMessage("You exit the dungeon.");
			break;
		/* End Brimhavem Dungeon */
		case 6481:
			c.getPA().movePlayer(3233, 9315, 0);
			break;
		case 17010:
			if (c.playerMagicBook == 0) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 1) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			break;

		case 1551:
			if (c.absX == 3252 && c.absY == 3266) {
				c.getPA().movePlayer(3253, 3266, 0);
			}
			if (c.absX == 3253 && c.absY == 3266) {
				c.getPA().movePlayer(3252, 3266, 0);
			}
			break;
		case 1553:
			if (c.absX == 3252 && c.absY == 3267) {
				c.getPA().movePlayer(3253, 3266, 0);
			}
			if (c.absX == 3253 && c.absY == 3267) {
				c.getPA().movePlayer(3252, 3266, 0);
			}
			break;

		case 2491:
			//Mining.mineEss(c, objectType);
			break;
		case 3044:
		case 24009:
			c.getSmithing().sendSmelting();
			break;

		/* Runecrafting */
		case 2478:
			rc.craftRunes(c, 1, 556, 1, 4, 5527, 10);
			break;
		case 2479:
			rc.craftRunes(c, 2, 558, 1, 5, 5529, 14);
			break;
		case 2480:
			rc.craftRunes(c, 5, 555, 1, 6, 5531, 19);
			break;
		case 2581:
			rc.craftRunes(c, 9, 557, 1, 7, 5535, 26);
			break;
		case 2582:
			rc.craftRunes(c, 14, 554, 1, 8, 5537, 35);
			break;
		case 2583:
			rc.craftRunes(c, 20, 559, 1, 9, 5533, 46);
			break;
		case 2584:
			rc.craftRunes(c, 27, 564, 1, 10, 5539, 59);
			break;
		case 2585:
			rc.craftRunes(c, 35, 562, 1, 11, 5543, 72);
			break;
		case 2586:
			rc.craftRunes(c, 44, 561, 1, 12, 5541, 91);
			break;
		case 2587:
			rc.craftRunes(c, 54, 563, 1, 14, 5545, 100);
			break;
		case 2588:
			rc.craftRunes(c, 65, 560, 1, 15, 5547, 100);
			break;
		case 2589:
			rc.craftRunes(c, 77, 565, 1, 16, 5549, 100);
			break;
		// abyss rifts
		case 7129: // fire riff
			rc.craftRunes(c, 14, 7, 554, 50, 60, 70);
			break;
		case 7130: // earth riff
			rc.craftRunes(c, 9, 7, 557, 45, 55, 65);
			break;
		case 7131: // body riff
			rc.craftRunes(c, 20, 8, 559, 55, 65, 75);
			break;
		case 7132: // cosmic riff
			rc.craftRunes(c, 35, 10, 564, 72, 84, 96);
			break;
		case 7133: // nat riff
			rc.craftRunes(c, 44, 9, 561, 60, 74, 91);
			break;
	//	case 7134: // chaos riff
		//	rc.craftRunes(c, 35, 9, 562, 60, 70, 80);
		//	break;
		case 7135: // law riff
			rc.craftRunes(c, 54, 10, 563, 65, 79, 93);
			break;
		case 7136: // death riff
			rc.craftRunes(c, 65, 10, 560, 72, 84, 96);
			break;
		case 7137: // water riff
			rc.craftRunes(c, 5, 6, 555, 30, 45, 60);
			break;
		case 7138: // soul riff
			rc.craftRunes(c, 65, 10, 566, 72, 84, 96);
			break;
		case 7139: // air riff
			rc.craftRunes(c, 1, 5, 556, 30, 45, 60);
			break;
		case 7140: // mind riff
			rc.craftRunes(c, 1, 5, 558, 30, 45, 60);
			break;
		case 7141: // blood rift
			rc.craftRunes(c, 77, 11, 565, 89, 94, 99);
			break;
		/* End of Runecrafting */

		/* AL KHARID */
		case 2883:
		case 2882:
			c.getDH().sendDialogues(1023, 925);
			break;
		case 2412:
			Sailing.startTravel(c, 1);
			break;
		case 2414:
			Sailing.startTravel(c, 2);
			break;
		case 2083:
			Sailing.startTravel(c, 5);
			break;
		case 2081:
			Sailing.startTravel(c, 6);
			break;
		case 14304:
			Sailing.startTravel(c, 14);
			break;
		case 14306:
			Sailing.startTravel(c, 15);
			break;
		case 11338:	//Bank Booth
		case 2213:	//Bank Booth
		case 2214:	//Bank Booth
		case 3045:	//Bank Booth
		case 5276:	//Bank Booth
		case 6084:	//Bank Booth
		case 3193:	//Bank Chest
		case 3194:	//Bank Chest
		case 4483:	//Bank Chest
			c.getPA().openUpBank();
		break;
		case 24101:
		case 11744:
		case 11748:
			c.sendMessage("hi");
			c.getPA().openUpBank();
			break;
		case 18491:
			c.getPA().openUpBank();
			break;
		case 18493:
			c.sendMessage("@red@Players can kill you here... You are not safe..");
			break;
		case 2790:
		case 76:
		case 2693:
			c.getPA().openUpBank();
			break;
		case 17385:
			if (c.getY() >= 9615 && c.getY() <= 9617 && c.getX() >= 3208 && c.getX() <= 3210) {
				c.getPA().movePlayer(3210, 3216, 0);
			}
			if (c.getY() >= 9851 && c.getY() <= 9853 && c.getX() >= 3115 && c.getX() <= 3117) {
				c.getPA().movePlayer(3117, 3452, 0);
			}
			if (c.getY() >= 9866 && c.getY() <= 9868 && c.getX() >= 3096 && c.getX() <= 3098) {
				c.getPA().movePlayer(3096, 3468, 0);
			}
			if (c.getY() >= 9900 && c.getY() <= 9910 && c.getX() >= 3400 && c.getX() <= 3410) {
				c.getPA().movePlayer(3405, 3506, 0);
			}
			if (c.getY() >= 9790 && c.getY() <= 9800 && c.getX() >= 2880 && c.getX() <= 2890) {
				c.getPA().movePlayer(2885, 3397, 0);
			}
			break;
		case 14880:
			if (c.getY() >= 3215 && c.getY() <= 3217 && c.getX() >= 3208 && c.getX() <= 3210) {
				c.getPA().movePlayer(3210, 9616, 0);
			}
			break;
		case 23566:
			if (c.playerLevel[16] < 15) {
				c.sendMessage("You need at least level 15 agility to cross these monkey bars.");
			} else {
				if (c.getX() <= 3121 && c.getX() >= 3120 && c.getY() >= 9963 && c.getY() <= 9964) {
					c.getPA().movePlayer(3120, 9970, 0);
					c.getPA().addSkillXP(1000, 16);
				}
				if (c.getX() <= 3121 && c.getX() >= 3120 && c.getY() >= 9969 && c.getY() <= 9970) {
					c.getPA().movePlayer(3121, 9963, 0);
					c.getPA().addSkillXP(1000, 16);
				}
			}
			break;
		case 16510:
			if (c.playerLevel[16] < 80) {
				c.sendMessage("You need at least level 80 agility to cross these monkey bars.");
			} else {
				if (c.getX() == 2880) {
					c.getPA().movePlayer(2878, 9813, 0);
					c.getPA().addSkillXP(1000, 16);
				}
				if (c.getX() == 2878) {
					c.getPA().movePlayer(2880, 9813, 0);
					c.getPA().addSkillXP(1000, 16);
				}
			}
		case 16509:
			if (c.playerLevel[16] < 70) {
				c.sendMessage("You need at least level 70 agility to cross these monkey bars.");
			} else {
				if (c.getX() <= 2893 && c.getX() >= 2891 && c.getY() >= 9798 && c.getY() <= 9800) {
					c.getPA().movePlayer(2886, 9799, 0);
					c.getPA().addSkillXP(1000, 16);
				}
				if (c.getX() <= 2888 && c.getX() >= 2886 && c.getY() >= 9798 && c.getY() <= 9800) {
					c.getPA().movePlayer(2892, 9799, 0);
					c.getPA().addSkillXP(1000, 16);
				}
			}
			break;
		case 3432:
			c.getPA().movePlayer(3440, 9887, 0);
			break;
		case 3443:
			c.getPA().movePlayer(3423, 3485, 0);
			break;
		/*case 16511:
			if (c.playerLevel[16] < 40) {
				c.sendMessage("You need at least level 40 agility to squeeze through this pipe.");
			} else {
				if (c.getX() <= 3151 && c.getX() >= 3149 && c.getY() >= 9905 && c.getY() <= 9907) {
					c.getPA().movePlayer(3155, 9906, 0);
					c.getPA().addSkillXP(1300, 16);
				}
				if (c.getX() <= 3156 && c.getX() >= 3153 && c.getY() >= 9904 && c.getY() <= 9908) {
					c.getPA().movePlayer(3149, 9906, 0);
					c.getPA().addSkillXP(1300, 16);
				}
			}
			break; */
		case 17384:
			if (c.getY() >= 3451 && c.getY() <= 3453 && c.getX() >= 3115 && c.getX() <= 3117) {
				c.getPA().movePlayer(3117, 9852, 0);
			}
			break;
		case 7179:
			if (c.getY() >= 3500 && c.getY() <= 3510 && c.getX() >= 3400 && c.getX() <= 3410) {
				c.getPA().movePlayer(3405, 9906, 0);
			} else {
			c.getPA().movePlayer(3097, 9868, 0);
			}
			break;
		case 16671:
			if (c.getX() >= 3200 && c.getX() <= 3210) {
			c.getPA().movePlayer(3206, 3208, 2);
			}
			break;
		case 16673:
			c.getPA().movePlayer(3206, 3208, 0);
			break;
		/**
		 * Entering the Fight Caves.
		 */
		case 11833:
			c.sendMessage("The fight caves are buggy right now, harp Sam about it and he'll fix it.");
			//if (c.waveId > 0) {
			//	c.waveId = 0;
			//	return;
		//	}
			//c.getFightCave().create(9);
			break;

		/**
		 * Leaving Fight Caves
		 */
		case 9357:
			c.getFightCave().leaveGame();
			break;

		/**
		 * Clicking on the Ancient Altar.
		 */
		case 6552:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.autocasting = false;
				c.sendMessage("An ancient wisdomin fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		/**
		 * Recharing prayer points.
		 */
		case 409:
		case 6817:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;

		/**
		 * Aquring god capes.
		 */
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;

		/**
		 * Oblisks in the wilderness.
		 */
		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:
			Server.objectManager.startObelisk(objectType);
			break;

		/**
		 * Clicking certain doors.
		 */
		case 6749:
			if (obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;

		case 6730:
			if (obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;

		case 6727:
			if (obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;

		case 6746:
			if (obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;

		case 6748:
			if (obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;

		case 6729:
			if (obX == 3545 && obY == 9677) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;

		case 6726:
			if (obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;

		case 6745:
			if (obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;

		case 6743:
			if (obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;

		case 6724:
			if (obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;

		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
				break;
			}
			// TODO Doors
		case 1530:
		case 1531:
		case 1533:
		case 1534:
		case 11712:
		case 11711:
		case 11707:
		case 11708:
		case 6725:
		case 3198:
		case 3197:
		case 7122:
			if ((c.playerLevel[0] + c.playerLevel[2]) < 150 && (c.getX() == 2838 || c.getX() == 2837)) {
				c.sendMessage("You need a combined strength and attack level of 150!");
			} else if (c.getX() == 2838){
				c.getPA().movePlayer(2837, 3549, 0);
			} else if (c.getX() == 2837) {
				c.getPA().movePlayer(2838, 3549, 0);
			} else {
			Server.objectHandler.doorHandling(objectType, c.objectX, c.objectY, 0);
			}
			break;

		case 1739:
		case 12537:
			c.getPA().handleStairs();
			c.dialogueAction = 850;
			break;

		case 1738:
		case 12536:
			c.getPA().handleUp();
			c.dialogueAction = 851;
			break;

		case 1740:
		case 12538:
			c.getPA().handleDown();
			c.dialogueAction = 852;
			break;

		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			break;
		case 1558:
			if (c.absX == 3041 && c.absY == 10308) {
				c.getPA().movePlayer(3040, 10308, 0);
			} else if (c.absX == 3040 && c.absY == 10308) {
				c.getPA().movePlayer(3041, 10308, 0);
			} else if (c.absX == 3040 && c.absY == 10307) {
				c.getPA().movePlayer(3041, 10307, 0);
			} else if (c.absX == 3041 && c.absY == 10307) {
				c.getPA().movePlayer(3040, 10307, 0);
			} else if (c.absX == 3044 && c.absY == 10341) {
				c.getPA().movePlayer(3045, 10341, 0);
			} else if (c.absX == 3045 && c.absY == 10341) {
				c.getPA().movePlayer(3044, 10341, 0);
			} else if (c.absX == 3044 && c.absY == 10342) {
				c.getPA().movePlayer(3045, 10342, 0);
			} else if (c.absX == 3045 && c.absY == 10342) {
				c.getPA().movePlayer(3044, 10343, 0);
			}
			break;
		case 1557:
			if (c.absX == 3023 && c.absY == 10312) {
				c.getPA().movePlayer(3022, 10312, 0);
			} else if (c.absX == 3022 && c.absY == 10312) {
				c.getPA().movePlayer(3023, 10312, 0);
			} else if (c.absX == 3023 && c.absY == 10311) {
				c.getPA().movePlayer(3022, 10311, 0);
			} else if (c.absX == 3022 && c.absY == 10311) {
				c.getPA().movePlayer(3023, 10311, 0);
			}
			break;
		case 2558:
			c.sendMessage("This door is locked.");
			break;

		case 9294:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
			} else if (c.absX > c.objectX) {
				c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
			}
			break;

		case 9293:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;

		case 10529:
		case 10527:
			if (c.absY <= c.objectY)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;
		case 11747:// deposit
			c.getPA().sendFrame126("Deadman - Safety Deposit Box", 7421);
			//c.getPA().sendFrame248(4465, 197);// 197 just because you can't see it
			c.getPA().showInterface(11942);
			//c.getItems().resetItems(7423);
			break;

		case 733:
			c.startAnimation(451);

			if (c.objectX == 3158 && c.objectY == 3951) {
				new Object(734, c.objectX, c.objectY, c.heightLevel, 3, 10, 733, 50);
			} else {
				new Object(734, c.objectX, c.objectY, c.heightLevel, 0, 10, 733, 50);
			}
			break;

		default:
			ScriptManager.callFunc("objectClick1_" + objectType, c, objectType, obX, obY);
			System.out.println("Unhandled First Click Object " + objectType + " " + objectType + " " + obX + " " + obY);
			break;

		/**
		 * Forfeiting a duel.
		 */
		case 3203:
			c.sendMessage("Forfeiting has been disabled.");
			break;

		}

	}
}
