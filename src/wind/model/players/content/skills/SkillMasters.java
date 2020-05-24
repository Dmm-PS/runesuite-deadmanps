package wind.model.players.content.skills;

import wind.model.players.Client;

public class SkillMasters {

	public enum MasterData {
		HUNTER(5113, 9948, SkillConstants.HUNTER), CONSTRUCTION(4247, 9789,
				SkillConstants.CONSTRUCTION), SLAYER(1599, 9786,
				SkillConstants.SLAYER), RUNECRAFTING(553, 9765,
				SkillConstants.RUNECRAFTING), STRENGTH(4297, 9750,
				SkillConstants.STRENGTH), ATTACK(4288, 9747,
				SkillConstants.ATTACK), DEFENCE(705, 9753,
				SkillConstants.DEFENCE), MAGIC(1658, 9762, SkillConstants.MAGIC), PRAYER(
				802, 9759, SkillConstants.PRAYER), RANGED(682, 9756,
				SkillConstants.RANGE), HITPOINTS(961, 9768,
				SkillConstants.HITPOINTS), MINING(3295, 9792,
				SkillConstants.MINING), SMITH(604, 9795,
				SkillConstants.SMITHING), FISHING(308, 9798,
				SkillConstants.FISHING), FM(4946, 9804,
				SkillConstants.FIREMAKING), COOKING(847, 9801,
				SkillConstants.COOKING), FLETCHING(575, 9783,
				SkillConstants.FLETCHING), CRAFTING(805, 9780,
				SkillConstants.CRAFTING), FARMING(3299, 9810,
				SkillConstants.FARMING), WC(4906, 9807,
				SkillConstants.WOODCUTTING), HERBLORE(455, 9774,
				SkillConstants.HERBLORE), THIEVING(2270, 9777,
				SkillConstants.THIEVING), AGILITY(437, 9771,
				SkillConstants.AGILITY);

		private int masterId, capeId, skillId;

		MasterData(int masterId, int capeId, int skillId) {
			this.masterId = masterId;
			this.capeId = capeId;
			this.skillId = skillId;
		}

		public int getMaster() {
			return masterId;
		}

		public int getCape() {
			return capeId;
		}

		public int getSkill() {
			return skillId;
		}
	}

	public static int getMaster() {
		for (MasterData m : MasterData.values()) {
			if (m.getMaster() == m.getMaster()) {
				return m.getMaster();
			}
		}
		return -1;
	}

	public static int getSkill(Client c) {
		for (MasterData m : MasterData.values()) {
			if (c.talkingNpc == m.getMaster()) {
				return m.getSkill();
			}
		}
		return -1;
	}

	public static String getSkillName(Client c) {
		for (MasterData m : MasterData.values()) {
			if (c.talkingNpc == m.getMaster()) {
				return m.name().toLowerCase();
			}
		}
		return "";
	}

	public static int checkMaxedSkills(Client c) {
		int maxed = 0;
		for (int j = 0; j < c.playerLevel.length; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				maxed++;
			}
		}
		return maxed;
	}

	public static void addSkillCape(Client c) {
		if (c.getEquipment().freeSlots() < 2) {
			c.getDH()
					.sendStatement(
							"You need at least 2 free inventory spaces to buy a skillcape.");
			c.nextChat = -1;
			return;
		}
		int maxed = checkMaxedSkills(c);
		for (MasterData m : MasterData.values()) {
			if (c.talkingNpc == m.getMaster()) {
				if (c.getPA().getLevelForXP(c.playerXP[getSkill(c)]) >= 99) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (maxed > 1) {
							c.getItems().addItem(m.capeId + 1, 1);
						} else {
							c.getItems().addItem(m.capeId, 1);
						}
						c.getItems().addItem(m.capeId + 2, 1);
						c.getItems().deleteItem(995, 99000);
						c.getPA().closeAllWindows();
						c.getPA().cancelTeleportTask();
					} else {
						c.getDH().sendStatement(
								"You need 99,000 coins to buy a "
										+ getSkillName(c) + " skillcape.");
						c.nextChat = -1;
						return;
					}
				} else {
					c.getDH().sendStatement(
							"You need 99 " + getSkillName(c)
									+ " to buy this skillcape.");
					c.nextChat = -1;
					return;
				}
			}
		}
	}

	public static void startDialogue(Client c) {
		for (MasterData m : MasterData.values()) {
			if (m.getMaster() == m.getMaster()) {
				c.getDH().sendDialogues(5, m.getMaster());
			}
		}
	}

	public static void masterDialogue(Client c) {
		for (MasterData m : MasterData.values()) {
			if (c.talkingNpc == m.getMaster()) {
				c.npcType = c.talkingNpc;
				c.getDH().sendNpcChat2(
						"Hello I'm the " + getSkillName(c)
								+ " master, would you like to",
						"buy a skillcape?", c.getDH().CALM);
				c.nextChat = 441;
			}
		}
	}

	public static void masterOptions(Client c) {
		for (MasterData m : MasterData.values()) {
			if (c.talkingNpc == m.getMaster()) {
				c.getDH().sendOption2("I want to buy a skill cape.",
						"No thanks.");
				c.dialogueAction = 10000;
			}
		}
	}

}
