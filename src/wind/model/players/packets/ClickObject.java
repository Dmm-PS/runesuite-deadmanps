package wind.model.players.packets;

import shamon.barrows.Barrows;
import wind.Server;
import wind.model.objects.Doors;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Player;
import wind.model.players.Rights;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252,
			THIRD_CLICK = 70;

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		c.clickObjectType = c.objectX = c.objectId = c.objectY = 0;
		c.objectYOffset = c.objectXOffset = 0;
		c.getPA().resetFollow();
		if (c.hasPin) {
			return;
		}
		switch (packetType) {

		case FIRST_CLICK:
			int objectX = c.objectX = c.getInStream().readSignedWordBigEndianA();
			int objectID = c.objectId = c.getInStream().readUnsignedWord();
			int objectY = c.objectY = c.getInStream().readUnsignedWordA();
			c.objectDistance = 1;

			Barrows barrows = c.getBarrows();
			if (barrows.getDoorHandler().openDoor(objectID, objectX, objectY)) {
				return;
			}

			if (barrows.getChest().openChest(objectID, objectX, objectY)) {
				return;
			}

			if (barrows.getTombStairsHandler().useStairs(objectID)) {
				return;
			}

			if (barrows.getTombObjectHandler().openTomb(objectID, objectX, objectY)) {
				return;
			}
			
			if (c.goodDistance(c.getX(), c.getY(), c.objectX, c.objectY, 1)) {
				if (Doors.getSingleton().handleDoor(c.objectId, c.objectX,
						c.objectY, c.heightLevel)) {
					return;
				}
			}
			
			if (c.getRights().equal(Rights.ADMINISTRATOR)) {
				System.out.println("[Game Engine] " + ": clicked object: "
						+ c.objectId + " (X:" + c.objectX + ", Y: " + c.objectY
						+ ").");
				Misc.println("[ADMIN] " + c.playerName + " : clicked object: "
						+ c.objectId + " (X: " + c.objectX + ", Y: "
						+ c.objectY + ").");
			} else if (c.getRights().equal(Rights.DEVELOPER)) {
				System.out.println("[Game Engine] " + ": clicked object: "
						+ c.objectId + " (X:" + c.objectX + ", Y: " + c.objectY
						+ ").");
			}
			if (Math.abs(c.getX() - c.objectX) > 25
					|| Math.abs(c.getY() - c.objectY) > 25) {
				c.resetWalkingQueue();
				break;
			}
			switch (c.objectId) {
			case 21512: //Fremmy Ladder 1
				c.objectDistance = 0;
				c.getPA().movePlayer(2374, 3800, 0);
				break;
			case 21395: //Fremmy Ladder 2
				c.objectDistance = 0;
				c.getPA().movePlayer(2362, 3799, 0);
				break;
			case 21405: //fremmy door
				c.objectDistance = 0;
				if (c.absX==2412&&c.absY==3797)
					c.getPA().movePlayer(2412, 3796, 0);
				else if(c.absX==2412&&c.absY==3796)
					c.getPA().movePlayer(2412, 3797, 0);
				else if(c.absX==2387&&c.absY==3799)
					c.getPA().movePlayer(2388, 3799, 0);
				else if(c.absX==2388&&c.absY==3799)
					c.getPA().movePlayer(2387, 3799, 0);
				break;
			case 21403: //fremmy door
				c.objectDistance = 0;
				if (c.absX==2413&&c.absY==3796)
					c.getPA().movePlayer(2413, 3797, 0);
				else if(c.absX==2413&&c.absY==3797)
					c.getPA().movePlayer(2413, 3796, 0);
				else if(c.absX==2387&&c.absY==3798)
					c.getPA().movePlayer(2388, 3798, 0);
				else if(c.absX==2388&&c.absY==3798)
					c.getPA().movePlayer(2387, 3798, 0);
				break;
			case 21505: //neitznot door
				c.objectDistance = 0;
				if (c.absX==2329&&c.absY==3804)
					c.getPA().movePlayer(2328, 3804, 0);
				else if(c.absX==2328&&c.absY==3804)
					c.getPA().movePlayer(2329, 3804, 0);
				break;
			case 21507: //neitznot door

				c.objectDistance = 0;
				if (c.absX==2329&&c.absY==3805)
					c.getPA().movePlayer(2328, 3805, 0);
				else if(c.absX==2328&&c.absY==3805)
					c.getPA().movePlayer(2329, 3805, 0);
				break;
			case 8143:
				c.getFarming().pickHerb();
				break;
			case 24101:
			case 21301:
			case 11748:
				c.objectDistance = 1;
				c.getPA().openUpBank();
				break;
		
			case 26518:
				if (c.absY < 5340 && c.absY > 5330) {
					c.getPA().movePlayer(2885, 5345, 2);
					c.sendMessage("You're now in Zamorak's encampment.");
				} else {
					c.getPA().movePlayer(2885, 5330, 2);
					c.sendMessage("You exit Zamorak's encampment.");
				}
				break;
			case 26505:
				if (c.absY < 5334 && c.absY > 5332) {
					//if (c.zamorakKills >= 40) {
						c.getPA().movePlayer(2925, 5331, 2);
					//	c.zamorakKills -= 40;
					}
					//else
					//	c.sendMessage("You need 40 Zamorak KC to enter.");
			//	}
				else if (c.absY < 5332 && c.absY > 5330){
					c.getPA().movePlayer(2925, 5335, 2);
				}
				break;
			case 26502:
				if(c.getY() == 5294) {
					c.getPA().movePlayer(2839, 5295, 2);
				} else if (c.getY() == 5295) {
					c.getPA().movePlayer(2839, 5294, 2);
				}
				break;
			case 26561:
				c.getPA().movePlayer(2914, 5300, 1);
				break;
			case 26562:
				c.getPA().movePlayer(2920, 5274, 0);
				break; 
			case 26504:
				if (c.getX() == 2909) {
					c.getPA().movePlayer(2908, 5265, 0);
				} else if (c.getX() == 2908) {
					c.getPA().movePlayer(2909, 5265, 0);
				}
				break;
			case 7:
			case 8:
			case 9:
				c.getCannon().pickUpCannon();
				break;

			case 6:
				if(!c.cannonIsShooting) {
					c.getCannon().shootCannon();
				}
				break;
			case 26415:
				c.objectDistance = 3;
				break;
			case 26380:
				c.objectYOffset = 8;
				c.objectDistance = 100;
				break;

			case 1738:
				if (c.objectX == 3204 && c.objectY == 3207) {
					c.objectXOffset = 1;
					c.objectYOffset = 2;
				}
				if (c.objectX == 3204 && c.objectY == 3229) {
					c.objectXOffset = 2;
				}
				if (c.objectX == 2839 && c.objectY == 3537) {
					c.objectXOffset = 2;
					c.objectYOffset = 2;
				}
				break;

			case 13291:
				c.sendMessage("Coming soon!");
				break;

			case 3192:
				c.getDuelHS();
				c.sendMessage("Dueling scoreboard coming soon!");
				break;

			case 9368:
				if (c.InFightPitsWaiting() == true) {
					c.sendMessage("You cannot go through here.");
					return;
				} else if (c.InFightPitsWaiting() == false) {
					Server.fightPits.removePlayerFromPits(c.playerId);
				}
				break;
			case 9369:
				if (c.getY() == 5177) {
					c.getPA().movePlayer(2399, 5175, 0);
				} else if (c.getY() == 5175) {
					c.getPA().movePlayer(2399, 5177, 0);
				}
				break;

			case 9391:
				if (c.getX() > 2397 && c.getX() < 2401 && c.getY() > 5170
						&& c.getY() < 5174) {
					c.getPA().sendFrame71(10, 3209);
					c.getPA().sendFrame106(10);
					c.getPA().sendFrame99(2);
					c.fightPitsOrb("Centre", 15239);
					c.getPA().movePlayer(2398, 5150, 0);
					c.hidePlayer();
					c.isNpc = true;
					c.isViewingOrb = true;
					Player.updateRequired = true;
					c.appearanceUpdateRequired = true;
				} else {
					return;
				}
				break;
			case 9398:// deposit
				c.getPA().sendFrame126("Deadman - Deposit Box", 7421);
				c.getPA().sendFrame248(4465, 197);// 197 just because you can't
				// see it =\
				c.getItems().resetItems(7423);
				break;

			case 1733:
				c.objectYOffset = 2;
				break;
			case 23271:
				c.objectDistance = 3;
				break;

			case 4705:
				if (c.objectX == 2513 && c.objectY == 3862)
					c.objectYOffset = 3;			
				break;

			case 4706:
				if (c.objectX == 2517 && c.objectY == 3862)
					c.objectYOffset = 3;			
				break;

			case 3044:
			case 21764:
			case 17010:
				c.objectDistance = 3;
				break;

			case 245:
				c.objectYOffset = -1;
				c.objectDistance = 0;
				break;

			case 272:
				c.objectYOffset = 1;
				c.objectDistance = 0;
				break;
			case 15653:
			case 15654:
				c.objectDistance = 2;
				break;

			case 273:
				c.objectYOffset = 1;
				c.objectDistance = 0;
				break;

			case 246:
				c.objectYOffset = 1;
				c.objectDistance = 0;
				break;

			case 4493:
			case 4494:
			case 4496:
			case 4495:
				c.objectDistance = 5;
				break;
			case 10229:
			case 6522:
				c.objectDistance = 2;
				break;
			case 8959:
				c.objectYOffset = 1;
				break;
			case 4417:
				if (c.objectX == 2425 && c.objectY == 3074)
					c.objectYOffset = 2;
				break;
			case 4420:
				if (c.getX() >= 2383 && c.getX() <= 2385) {
					c.objectYOffset = 1;
				} else {
					c.objectYOffset = -2;
				}
			case 6552:
			case 409:
				c.objectDistance = 2;
				break;
			case 2879:
			case 2878:
				c.objectDistance = 3;
				break;
			case 2558:
				c.objectDistance = 0;
				if (c.absX > c.objectX && c.objectX == 3044)
					c.objectXOffset = 1;
				if (c.absY > c.objectY)
					c.objectYOffset = 1;
				if (c.absX < c.objectX && c.objectX == 3038)
					c.objectXOffset = -1;
				break;
			case 9356:
				c.objectDistance = 2;
				break;
			case 5959:
			case 1815:
			case 5960:
				c.objectDistance = 0;
				break;

			case 9293:
				c.objectDistance = 2;
				break;
			case 4418:
				if (c.objectX == 2374 && c.objectY == 3131)
					c.objectYOffset = -2;
				else if (c.objectX == 2369 && c.objectY == 3126)
					c.objectXOffset = 2;
				else if (c.objectX == 2380 && c.objectY == 3127)
					c.objectYOffset = 2;
				else if (c.objectX == 2369 && c.objectY == 3126)
					c.objectXOffset = 2;
				else if (c.objectX == 2374 && c.objectY == 3131)
					c.objectYOffset = -2;
				break;
			case 9706:
				c.objectDistance = 0;
				c.objectXOffset = 1;
				break;
			case 9707:
				c.objectDistance = 0;
				c.objectYOffset = -1;
				break;
			case 2114:
			case 2118:
			case 2119:
			case 2120:
				c.objectDistance = 4;
			case 2123:
			case 2141:
				c.objectDistance = 2;
			case 16509:
				c.objectDistance = 3;
			case 4419:
			case 20672: // verac
				c.objectYOffset = 3;
				break;
			case 20772:
				c.objectDistance = 2;
				c.objectYOffset = 1;
				break;
			case 11833:
				c.objectDistance = 4;

			case 20671: // torag
				c.objectXOffset = 2;
				break;
			case 20721:
				c.objectDistance = 2;
				c.objectYOffset = 1;
				break;

			case 20670: // karils
				c.objectYOffset = -1;
				break;
			case 20771:
				c.objectDistance = 2;
				c.objectYOffset = 1;
				break;

			case 20669: // guthan stairs
				c.objectYOffset = -1;
				break;
			case 20722:
				c.objectDistance = 2;
				c.objectXOffset = 1;
				c.objectYOffset = 1;
				break;

			case 20668: // dharok stairs
				c.objectXOffset = -1;
				break;
			case 20720:
				c.objectDistance = 2;
				c.objectXOffset = 1;
				c.objectYOffset = 1;
				break;

			case 20667: // ahrim stairs
				c.objectXOffset = -1;
				break;
			case 20770:
				c.objectDistance = 2;
				c.objectXOffset = 1;
				c.objectYOffset = 1;
				break;
			case 1276:
			case 1278:// trees
			case 1281: // oak
			case 11756://oak osrs
			case 11759://willow osrs
			case 1308: // willow
			case 1307: // maple
			case 11758://yew osrs
			case 1309: // yew
			case 1306: // yew
			case 11764://magic osrs
				c.objectDistance = 3;
				break;
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;
			}
			if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
					+ c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
				c.getActions().firstClickObject(c.objectId, c.objectX,
						c.objectY);
			} else {
				c.clickObjectType = 1;

				TaskHandler.submit(new Task(1, true) {

					@Override
					public void execute() {
						if (c.clickObjectType == 1
								&& c.goodDistance(c.objectX + c.objectXOffset,
										c.objectY + c.objectYOffset, c.getX(),
										c.getY(), c.objectDistance)) {
							c.getActions().firstClickObject(c.objectId,
									c.objectX, c.objectY);
							this.cancel();
						}
						if (c.clickObjectType > 1 || c.clickObjectType == 0)
							this.cancel();
					}

					@Override
					public void onCancel() {
						c.clickObjectType = 0;
					}

				});
			}
			break;

		case SECOND_CLICK:
			c.objectId = c.getInStream().readUnsignedWordBigEndianA();
			c.objectY = c.getInStream().readSignedWordBigEndian();
			c.objectX = c.getInStream().readUnsignedWordA();
			c.objectDistance = 1;

			if (c.getRights().equal(Rights.ADMINISTRATOR)) {
				System.out.println("2objectId: " + c.objectId + "  ObjectX: "
						+ c.objectX + "  objectY: " + c.objectY + " Xoff: "
						+ (c.getX() - c.objectX) + " Yoff: "
						+ (c.getY() - c.objectY));
			}

			if (c.getRights().equal(Rights.ADMINISTRATOR)) {
				System.out.println("[Game Engine] " + ": clicked object: "
						+ c.objectId + " (X:" + c.objectX + ", Y: " + c.objectY
						+ ").");
				Misc.println("[ADMIN] " + c.playerName + " : clicked object: "
						+ c.objectId + " (X: " + c.objectX + ", Y: "
						+ c.objectY + ").");
			} else if (c.getRights().equal(Rights.DEVELOPER)
					|| c.playerName.equalsIgnoreCase("mod sunny")) {
				System.out.println("[Game Engine] " + ": clicked object: "
						+ c.objectId + " (X:" + c.objectX + ", Y: " + c.objectY
						+ ").");
			}

			switch (c.objectId) {

			case 6:
				c.getCannon().pickUpCannon();
				break;
			case 24101:
				c.getPA().openUpBank();
				break;
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2565:
			case 2478:
			case 2483:
			case 2484:
				c.objectDistance = 3;
				break;
			case 6163:
			case 6165:
			case 6166:
			case 6164:
			case 6162:
				c.objectDistance = 2;
				break;
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;

			}
			if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
					+ c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
				c.getActions().secondClickObject(c.objectId, c.objectX,
						c.objectY);
			} else {
				c.clickObjectType = 2;

				TaskHandler.submit(new Task(1, true) {					
					@Override
					public void execute() {
						if (c.clickObjectType == 2
								&& c.goodDistance(c.objectX + c.objectXOffset,
										c.objectY + c.objectYOffset, c.getX(),
										c.getY(), c.objectDistance)) {
							c.getActions().secondClickObject(c.objectId,
									c.objectX, c.objectY);
							this.cancel();
						}
						if (c.clickObjectType < 2 || c.clickObjectType > 2)
							this.cancel();
					}

					@Override
					public void onCancel() {
						c.clickObjectType = 0;
					}

				});
			}
			break;

		case THIRD_CLICK:
			c.objectX = c.getInStream().readSignedWordBigEndian();
			c.objectY = c.getInStream().readUnsignedWord();
			c.objectId = c.getInStream().readUnsignedWordBigEndianA();

			if (c.getRights().equal(Rights.DEVELOPER)) {
				Misc.println("objectId: " + c.objectId + "  ObjectX: "
						+ c.objectX + "  objectY: " + c.objectY + " Xoff: "
						+ (c.getX() - c.objectX) + " Yoff: "
						+ (c.getY() - c.objectY));
			}

			switch (c.objectId) {
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;
			}
			if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
					+ c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
				c.getActions().secondClickObject(c.objectId, c.objectX,
						c.objectY);
			} else {
				c.clickObjectType = 3;

				TaskHandler.submit(new Task(1, true) {

					@Override
					public void execute() {
						if (c.clickObjectType == 3
								&& c.goodDistance(c.objectX + c.objectXOffset,
										c.objectY + c.objectYOffset, c.getX(),
										c.getY(), c.objectDistance)) {
							c.getActions().thirdClickObject(c.objectId,
									c.objectX, c.objectY);
							this.cancel();
						}
						if (c.clickObjectType < 3)
							this.cancel();
					}


					@Override
					public void onCancel() {
						c.clickObjectType = 0;
					}


				});
			}
			break;
		}

	}

	public void handleSpecialCase(Client c, int id, int x, int y) {

	}

}
