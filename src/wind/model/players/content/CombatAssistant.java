package wind.model.players.content;

import wind.model.items.ItemTableManager;
import wind.model.items.PlayerItem;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.combat.AttackNPC;
import wind.model.players.combat.AttackPlayer;
import wind.model.players.combat.magic.MagicData;
import wind.model.players.combat.magic.MagicExtras;
import wind.model.players.combat.magic.MagicMaxHit;
import wind.model.players.combat.magic.MagicRequirements;
import wind.model.players.combat.melee.CombatPrayer;
import wind.model.players.combat.melee.MeleeData;
import wind.model.players.combat.melee.MeleeExtras;
import wind.model.players.combat.melee.MeleeMaxHit;
import wind.model.players.combat.melee.MeleeRequirements;
import wind.model.players.combat.melee.MeleeSpecial;
import wind.model.players.combat.range.RangeData;
import wind.model.players.combat.range.RangeExtras;
import wind.model.players.combat.range.RangeMaxHit;

public class CombatAssistant {

	private Client c;

	public CombatAssistant(Client Client) {
		this.c = Client;
	}

	public int[][] slayerReqs = { { 1648, 5 }, { 1612, 15 }, { 443, 45 }, { 446, 45 }, { 447, 45 },
			{ 1618, 50 }, { 423, 65 }, { 424, 65 }, { 412, 75 }, { 413, 75 }, { 1543, 75 }, { 8, 80 }, { 11, 80 },
			{ 415, 85 }, { 4005, 90 } };

	public boolean goodSlayer(int i) {
		for (int j = 0; j < slayerReqs.length; j++) {
			if (slayerReqs[j][0] == NPCHandler.npcs[i].npcType) {
				if (slayerReqs[j][1] > c.playerLevel[c.playerSlayer]) {
					c.sendMessage("You need a slayer level of "
							+ slayerReqs[j][1] + " to harm this NPC.");
					return false;
				}
			}
		}
		return true;
	}
	public boolean krakenBounds(Client c, int i) {
		switch (NPCHandler.npcs[i].npcType) {
			case 494:
				if (c.absX >= 3692 && c.absX <= 3699 && c.absY >= 5807 && c.absY <= 5806)
					return true;
				return true;
		}
		return false;
	}
	public boolean kalphite1(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 1158:
			return true;
		}
		return false;
	}

	public boolean kalphite2(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 1160:
			return true;
		}
		return false;
	}

	public void resetPlayerAttack() {
		MeleeData.resetPlayerAttack(c);
	}

	public int getCombatDifference(int combat1, int combat2) {
		return MeleeRequirements.getCombatDifference(combat1, combat2);
	}

	public int getKillerId(int playerId) {
		return MeleeRequirements.getKillerId(c, playerId);
	}

	public boolean checkReqs() {
		return MeleeRequirements.checkReqs(c);
	}

	public boolean checkMultiBarrageReqs(int i) {
		return MagicExtras.checkMultiBarrageReqs(c, i);
	}

	public int getRequiredDistance() {
		return MeleeRequirements.getRequiredDistance(c);
	}

	public void multiSpellEffectNPC(int npcId, int damage) {
		MagicExtras.multiSpellEffectNPC(c, npcId, damage);
	}

	public boolean checkMultiBarrageReqsNPC(int i) {
		return MagicExtras.checkMultiBarrageReqsNPC(i);
	}

	public void appendMultiBarrageNPC(int npcId, boolean splashed) {
		MagicExtras.appendMultiBarrageNPC(c, npcId, splashed);
	}

	public void attackNpc(int i) {
		AttackNPC.attackNpc(c, i);
	}

	public void delayedHit(final Client c, final int i) {
		AttackNPC.delayedHit(c, i);
	}

	public void applyNpcMeleeDamage(int i, int damageMask, int damage) {
		AttackNPC.applyNpcMeleeDamage(c, i, damageMask, damage);
	}

	public void attackPlayer(int i) {
		AttackPlayer.attackPlayer(c, i);
	}

	public void playerDelayedHit(final Client c, final int i) {
		AttackPlayer.playerDelayedHit(c, i);
	}

	public void applyPlayerMeleeDamage(int i, int damageMask, int damage) {
		AttackPlayer.applyPlayerMeleeDamage(c, i, damageMask, damage);
	}

	public void addNPCHit(int i, Client c) {
		AttackNPC.addNPCHit(i, c);
	}

	public void applyPlayerHit(Client c, final int i) {
		AttackPlayer.applyPlayerHit(c, i);
	}

	public void fireProjectileNpc() {
		RangeData.fireProjectileNpc(c);
	}

	public void fireProjectilePlayer() {
		RangeData.fireProjectilePlayer(c);
	}

	public boolean usingCrystalBow() {
		return c.playerEquipment[c.playerWeapon] >= 4212
				&& c.playerEquipment[c.playerWeapon] <= 4223;
	}

	public boolean multis() {
		return MagicData.multiSpells(c);
	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		MagicExtras.appendMultiBarrage(c, playerId, splashed);
	}

	public void multiSpellEffect(int playerId, int damage) {
		MagicExtras.multiSpellEffect(c, playerId, damage);
	}

	public void applySmite(int index, int damage) {
		MeleeExtras.applySmite(c, index, damage);
	}

	public boolean usingDbow() {
		return c.playerEquipment[c.playerWeapon] == 11235;
	}

	public boolean usingHally() {
		return MeleeData.usingHally(c);
	}

	public void getPlayerAnimIndex(Client c, String weaponName) {
		PlayerItem item = ItemTableManager
				.forID(c.playerEquipment[c.playerWeapon]);
		c.playerStandIndex = item.getStandAnim();
		c.playerWalkIndex = item.getWalkAnim();
		c.playerRunIndex = item.getRunAnim();
		c.playerTurnIndex = item.getWalkTurnAnim();
		c.playerTurn180Index = item.getWalkTurn180Anim();
		c.playerTurn90CWIndex = item.getWalkTurn90CWAnim();
		c.playerTurn90CCWIndex = item.getWalkTurn90CCAnim();
	}

	public int getWepAnim(String weaponName) {
		return MeleeData.getWepAnim(c, weaponName);
	}

	public int getBlockEmote() {
		return MeleeData.getBlockEmote(c);
	}

	public int getAttackDelay(String s) {
		return MeleeData.getAttackDelay(c, s);
	}

	public int getHitDelay(int i, String weaponName) {
		return MeleeData.getHitDelay(c, i, weaponName);
	}

	public int npcDefenceAnim(int i) {
		return MeleeData.npcDefenceAnim(i);
	}

	public int calculateMeleeAttack() {
		return MeleeMaxHit.calculateMeleeAttack(c);
	}

	public int bestMeleeAtk() {
		return MeleeMaxHit.bestMeleeAtk(c);
	}

	public int calculateMeleeMaxHit() {
		return (int) MeleeMaxHit.calculateBaseDamage(c, c.usingSpecial);
	}

	public int calculateMeleeDefence() {
		return MeleeMaxHit.calculateMeleeDefence(c);
	}

	public int bestMeleeDef() {
		return MeleeMaxHit.bestMeleeDef(c);
	}

	public void addCharge() {
		MeleeExtras.addCharge(c);
	}

	public void handleDfs(final Client c) {
		MeleeExtras.handleDragonFireShield(c);
	}

	public void handleDfsNPC(final Client c) {
		MeleeExtras.handleDragonFireShieldNPC(c);
	}

	public void appendVengeanceNPC(int otherPlayer, int damage) {
		MeleeExtras.appendVengeanceNPC(c, otherPlayer, damage);
	}

	public void appendVengeance(int otherPlayer, int damage) {
		MeleeExtras.appendVengeance(c, otherPlayer, damage);
	}

	public void applyRecoilNPC(int damage, int i) {
		MeleeExtras.applyRecoilNPC(c, damage, i);
	}

	public void applyRecoil(int damage, int i) {
		MeleeExtras.applyRecoil(c, damage, i);
	}

	public void removeRecoil(Client c) {
		MeleeExtras.removeRecoil(c);
	}

	public void handleGmaulPlayer() {
		MeleeExtras.graniteMaulSpecial(c);
	}

	public void activateSpecial(int weapon, int i) {
		MeleeSpecial.activateSpecial(c, weapon, i);
	}

	public boolean checkSpecAmount(int weapon) {
		return MeleeSpecial.checkSpecAmount(c, weapon);
	}

	public int calculateRangeAttack() {
		return RangeMaxHit.calculateRangeAttack(c);
	}

	public int calculateRangeDefence() {
		return RangeMaxHit.calculateRangeDefence(c);
	}

	public int rangeMaxHit() {
		return RangeMaxHit.maxHit(c);
	}

	public int getRangeStr(int i) {
		return RangeData.getRangeStr(i);
	}

	public int getRangeStartGFX() {
		return RangeData.getRangeStartGFX(c);
	}

	public int getRangeProjectileGFX() {
		return RangeData.getRangeProjectileGFX(c);
	}

	public int correctBowAndArrows() {
		return RangeData.correctBowAndArrows(c);
	}

	public int getProjectileShowDelay() {
		return RangeData.getProjectileShowDelay(c);
	}

	public int getProjectileSpeed() {
		return RangeData.getProjectileSpeed(c);
	}

	public void crossbowSpecial(Client c, int i) {
		RangeExtras.crossbowSpecial(c, i);
	}

	public void appendMutliChinchompa(int npcId) {
		RangeExtras.appendMutliChinchompa(c, npcId);
	}

	public boolean properBolts() {
		return usingBolts(c.playerEquipment[c.playerArrows]);
	}

	public boolean usingBolts(int i) {
		return (i >= 9140 && i <= 9145) || (i >= 9236 && i <= 9245);
	}

	public int mageAtk() {
		return MagicMaxHit.mageAttack(c);
	}

	public int mageDef() {
		return MagicMaxHit.mageDefefence(c);
	}

	public int magicMaxHit() {
		return MagicMaxHit.magiMaxHit(c);
	}

	public boolean wearingStaff(int runeId) {
		return MagicRequirements.wearingStaff(c, runeId);
	}

	public boolean checkMagicReqs(int spell) {
		return MagicRequirements.checkMagicReqs(c, spell);
	}

	public int getMagicGraphic(Client c, int i) {
		return MagicData.getMagicGraphic(c, i);
	}

	public int getFreezeTime() {
		return MagicData.getFreezeTime(c);
	}

	public int getStartHeight() {
		return MagicData.getStartHeight(c);
	}

	public int getEndHeight() {
		return MagicData.getEndHeight(c);
	}

	public int getStartDelay() {
		return MagicData.getStartDelay(c);
	}

	public int getStaffNeeded() {
		return MagicData.getStaffNeeded(c);
	}

	public boolean godSpells() {
		return MagicData.godSpells(c);
	}

	public int getEndGfxHeight() {
		return MagicData.getEndGfxHeight(c);
	}

	public int getStartGfxHeight() {
		return MagicData.getStartGfxHeight(c);
	}

	public void handlePrayerDrain() {
		CombatPrayer.handlePrayerDrain(c);
	}

	public void reducePrayerLevel() {
		CombatPrayer.reducePrayerLevel(c);
	}

	public void resetPrayers() {
		CombatPrayer.resetPrayers(c);
	}
	
	public void activatePrayer(int i) {
		CombatPrayer.activatePrayer(c, i);
	}
	
	public int finalMagicDamage(Client c) {
		return MagicMaxHit.finalMagicDamage(c);
	}

 	public void appendHit(Client c2, int damage, int mask, int icon, boolean playerHitting, int soak) {
		@SuppressWarnings("unused")
		boolean maxHit = false;
		if (playerHitting) {
			switch (icon) {
				case 0:
					maxHit = damage >= calculateMeleeMaxHit() - 20;
					break;
				case 1:
					maxHit = damage >= rangeMaxHit() - 20;
					break;
				case 2:
					maxHit = damage == finalMagicDamage(c);
					break;
			}
		}
		if(damage > c2.constitution)
			damage = c2.constitution;
		c2.handleHitMask(damage);
		c2.dealDamage(damage);
	}

	public void appendHit(NPC n, int deflect, int i, int j, int k) {
		// TODO Auto-generated method stub
		
	}
}