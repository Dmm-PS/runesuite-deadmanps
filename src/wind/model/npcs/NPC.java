package wind.model.npcs;

import wind.util.Misc;
import wind.util.Stream;

public class NPC {

	public String dagColor = "";
	private NPCDefinitions npcDef = new NPCDefinitions();
	public int npcId;
	public int npcType;
	public int absX, absY;
	public int heightLevel;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction,
	walkingType;
	public int spawnX, spawnY;
	public int viewX, viewY;
	public int hp, maxHP;
	public long singleCombatDelay = 0;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public int attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, HP,
	MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn, respawns;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex,
	underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	public final int size = 0;



	public NPC(int _npcId, int _npcType) {
		npcId = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
	}
	public boolean isAggressive(int i) {
		switch (npcType) {
		case 494:
		case 2045:
		case 2044:
		case 2043:
		case 2042:
		case 239:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
			return true;
		}
		return false;
	}
	public void forceAnim(int number) {
		animNumber = number;
		animUpdateRequired = true;
		updateRequired = true;
	}

	public void faceTo(int playerId) {
		// this.rawDirection = 32768 + playerId;
		this.dirUpdateRequired = true;
		this.updateRequired = true;
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Graphics
	 **/

	public int mask80var1 = 0;
	public int mask80var2 = 0;
	protected boolean mask80update = false;

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	/**
	 * 
	 Face
	 * 
	 **/

	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!updateRequired)
			return;
		int updateMask = 0;
		if (animUpdateRequired)
			updateMask |= 0x10;
		if (hitUpdateRequired2)
			updateMask |= 8;
		if (mask80update)
			updateMask |= 0x80;
		if (dirUpdateRequired)
			updateMask |= 0x20;
		if (forcedChatRequired)
			updateMask |= 1;
		if (hitUpdateRequired)
			updateMask |= 0x40;
		if (FocusPointX != -1)
			updateMask |= 4;

		str.writeByte(updateMask);

		if (animUpdateRequired)
			appendAnimUpdate(str);
		if (hitUpdateRequired2)
			appendHitUpdate2(str);
		if (mask80update)
			appendMask80Update(str);
		if (dirUpdateRequired)
			appendFaceEntity(str);
		if (forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired)
			appendHitUpdate(str);
		if (FocusPointX != -1)
			appendSetFocusDestination(str);

	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NPCHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}

	public void appendHitUpdate(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteC(hitDiff);
		if (hitDiff > 0) {
			str.writeByteS(1);
		} else {
			str.writeByteS(0);
		}
		str.writeByteS(HP);
		str.writeByteC(MaxHP);
	}

	public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;

	public boolean summoned;

	public int summonedBy;

	public int lastX;

	public int lastY;

	public void appendHitUpdate2(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteA(hitDiff2);
		if (hitDiff2 > 0) {
			str.writeByteC(1);
		} else {
			str.writeByteC(0);
		}
		str.writeByteA(HP);
		str.writeByte(MaxHP);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public boolean inMulti() {
        if ((absX >= 2400 && absX <= 2517 && absY >= 5110 && absY <= 5189)
        		|| (absX >= 2303 && absX <= 2370 && absY >= 3661 && absY <= 3704)
        		|| (absX >= 2677 && absX <= 2834 && absY >= 2688 && absY <= 2821)
        		|| (absX >= 3155 && absX <= 3306 && absY >= 9849 && absY <= 9926)
        		|| (absX >= 2655 && absX <= 2731 && absY >= 3711 && absY <= 3740)
        		|| (absX >= 2502 && absX <= 2547 && absY >= 3204 && absY <= 3248)
        		|| (absX >= 2654 && absX <= 2680 && absY >= 3435 && absY <= 3449)
        		|| (absX >= 2647 && absX <= 2687 && absY >= 3411 && absY <= 3441)
        		|| (absX >= 2655 && absX <= 2681 && absY >= 3407 && absY <= 3419)
        		|| (absX >= 3326 && absX <= 3392 && absY >= 3202 && absY <= 3263)
        		|| (absX >= 3100 && absX <= 3116 && absY >= 3153 && absY <= 3167)
        		|| (absX >= 3131 && absX <= 3195 && absY >= 2954 && absY <= 3006)
        		|| (absX >= 3263 && absX <= 3327 && absY >= 3136 && absY <= 3200)
        		|| (absX >= 3111 && absX <= 3135 && absY >= 3253 && absY <= 3266)
        		|| (absX >= 3103 && absX <= 3135 && absY >= 3232 && absY <= 3254)
        		|| (absX >= 2880 && absX <= 2904 && absY >= 3525 && absY <= 3544)
        		|| (absX >= 2816 && absX <= 2881 && absY >= 3456 && absY <= 3519)
        		|| (absX >= 2929 && absX <= 2943 && absY >= 3512 && absY <= 3519)
        		|| (absX >= 2941 && absX <= 3008 && absY >= 3394 && absY <= 3436)
        		|| (absX >= 3061 && absX <= 3136 && absY >= 3391 && absY <= 3455)
                || (absX >= 3264 && absX <= 3381 && absY >= 4777 && absY <= 4863)
                || (absX >= 2936 && absX <= 3062 && absY >= 3309 && absY <= 3394)
                || (absX >= 2975 && absX <= 3000 && absY >= 4365 && absY <= 4400)
                || (absX >= 2502 && absX <= 2530 && absY >= 3024 && absY <= 3059)
                || (absX >= 2323 && absX <= 2369 && absY >= 3686 && absY <= 3715)
                || (absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
                || (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
                || (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
                || (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
                || (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
                || (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
                || (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369)
                || (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
                || (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
                || (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
                || (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
                || (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
                || (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
                || (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
            return true;
        }
        return false;
    }
	public void dealDamage(int damage) {
		if (damage > HP) {
			damage = HP;
		}
		HP -= damage;
	}
	public int getCombatLevel() {
		return npcDef.getNpcCombat();
	}
	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}
}
