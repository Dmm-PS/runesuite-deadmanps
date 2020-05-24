package wind.model.players.packets;

import wind.model.npcs.impl.Guard;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.PlayerHandler;
import wind.model.players.content.skills.SkillHandler;
import wind.model.players.content.skills.impl.Fishing;
import wind.model.players.content.skills.impl.woodcutting.Woodcutting;

/**
 * Walking packet
 **/
public class Walking implements PacketType {
	
	public void processPacket(Client c, int packetType, int packetSize) {
		
		c.usingAltar = false;
		c.isMining = false;
		c.isCooking = false;
		//int i;
		c.playerSkilling[7] = false;
		if (c.canWalk == false) {
			return;
		}
		if (c.stopPlayerSkill) {
			SkillHandler.resetPlayerSkillVariables(c);
		}
		
		if (c.isDoingEmote) {
			c.startAnimation(65535);
			c.gfx0(65535);
			c.isDoingEmote = false;
		}
		if (c.usingEmote) {
			c.startAnimation(65535);
			c.gfx0(65535);
			c.usingEmote = false;
		}

		if (c.hasPin) {
			c.sendMessage("@red@You must enter the pin before you can play the game");
			return;
		}
		 /* if (c.usingHomeTeleport && c.isMoving) {
			    c.teleporting = false;
			    c.isTeleporting = false;
			    c.sendMessage("You cannot teleport while moving.");
			  }*/
		if (c.isWc) {
			Woodcutting.resetWoodcutting(c);
		}
		if (c.isFishing) {
			Fishing.resetFishing(c);
		}
		if (c.duelCount != 0) {
			c.sendMessage("You cannot walk for another " + c.duelCount
					+ " seconds.");
			return;
		} else {
			c.canWalk = true;
		}
		if (c.inWild()) {
			c.getPA().resetDamageDone();
		}
	//	if (c.inWild() && !c.EP_ACTIVE)
		//	BountyHunter.handleEP(c);
		if (packetType == 248 || packetType == 164) {
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0)
				c.getPA().resetFollow();
		}
		c.getPA().removeAllWindows();
		c.getPA().cancelTeleportTask();
		if (c.duelRule[1] && c.duelStatus == 5) {
			if (PlayerHandler.players[c.duelingWith] != null) {
				if (!c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.duelingWith].getX(),
						PlayerHandler.players[c.duelingWith].getY(), 1)
						|| c.attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.playerIndex = 0;
			return;
		}

		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 1)
						&& packetType != 98) {
					c.playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.playerIndex = 0;
			}
			return;
		}

		if (c.inTrade) {
			if (!c.acceptedTrade) {
				c.getTrade().declineTrade(true);
			} else if (c.acceptedTrade) {
				return;
			}
		}

		if (c.openDuel && c.duelStatus != 5) {
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o != null)
				o.getDuel().declineDuel(true);
			c.getDuel().declineDuel(true);
		}
		if ((c.duelStatus >= 1 && c.duelStatus <= 4) || c.duelStatus == 6) {
			if (c.duelStatus == 6) {
				c.getDuel().claimStakedItems();
			}
			return;
		}
		if (c.isBanking || c.isShopping || c.inTrade)
			c.isBanking = c.isShopping = c.inTrade = false;

		if (System.currentTimeMillis() - c.lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}

		if (packetType == 98) {
			c.mageAllowed = true;
		}

		if ((c.duelStatus >= 1 && c.duelStatus <= 4) || c.duelStatus == 6) {
			if (c.duelStatus == 6) {
				c.getDuel().claimStakedItems();
			}
			return;
		}

		if(c.isSkulled && c.inSafeZone()){ 
			Guard.constructGuard(c); 
			c.freezeTimer = 60;
		}
		
		if (c.respawnTimer > 3) {
			return;
		}
		if (packetType == 248) {
			packetSize -= 14;
		}
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;

		int firstStepX = c.getInStream().readSignedWordBigEndianA()
				- c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
			// c.homeTimer = 0;
		}
		

		int firstStepY = c.getInStream().readSignedWordBigEndian()
				- c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
			// c.homeTimer = 0;
		}
		
	}

}