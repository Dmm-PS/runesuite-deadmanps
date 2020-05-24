package wind.model.players.content.skills.impl;

import wind.model.players.Client;
import wind.util.Misc;

public class Crafting {

	Client c;

	public Crafting(Client c) {
		this.c = c;
	}

	public int hideType = 0, makeId = 0, amount = 0, craftType = 0, exp = 0,
			index = 0;

	public void resetCrafting() {
		hideType = 0;
		makeId = 0;
		amount = 0;
		c.craftingLeather = false;
		craftType = 0;
	}

	public void handleChisel(int id1, int id2) {
		if (id1 == 1755)
			cutGem(id2);
		else
			cutGem(id1);
	}

	public int[][] gems = { { 1623, 1607, 1, 50 }, { 1621, 1605, 27, 68 },
			{ 1619, 1603, 34, 85 }, { 1617, 1601, 43, 108 },
			{ 1631, 1615, 55, 138 }, { 6571, 6573, 67, 168 } };

	public void cutGem(int id) {
		for (int j = 0; j < gems.length; j++) {
			if (gems[j][0] == id) {
				if (c.playerLevel[c.playerCrafting] >= gems[j][2]) {
					
					c.getItems()
							.deleteItem(id, c.getItems().getItemSlot(id), 1);
					c.getItems().addItem(gems[j][1], 1);
					c.getPA().addSkillXP(
							gems[j][3] * 5,
							c.playerCrafting);
					int zulrah = Misc.random(256);
					if (zulrah == 142) {
						c.getItems().addItemToBank(12938, 1);
						c.sendMessage("@red@A mysterious teleport has been added to your bank!");
					}
					break;
				}
			}
		}
	}

	public void handleCraftingClick(int clickId) {
		for (int j = 0; j < buttons.length; j++) {
			if (buttons[j][0] == clickId) {
				craftType = buttons[j][1];
				amount = buttons[j][2];
				checkRequirements();
				break;
			}
		}
	}

	public void checkRequirements() {
		for (int j = 0; j < expsAndLevels.length; j++) {
			if (expsAndLevels[j][0] == hideType) {
				if (c.playerLevel[c.playerCrafting] >= expsAndLevels[j][1]) {
					if (c.getItems().playerHasItem(hideType, craftAmt())) {
						c.getPA().closeAllWindows();
						c.getPA().cancelTeleportTask();
						exp = expsAndLevels[j][2];
						index = j;
						craftHides(hideType);
					}
				} else {
					c.sendMessage("You need a crafting level of "
							+ expsAndLevels[j][1] + " to craft this.");
				}
			}
		}
	}

	public void craftHides(int id) {
		for (int j = 0; j < amount; j++) {
			if (!c.getItems().playerHasItem(id, craftAmt())) {
				c.sendMessage("You don't have enough hides to make that!");
				return;
			}
			c.getItems().deleteItem2(id, craftAmt());
			c.sendMessage("You use " + craftAmt() + " hide(s).");
			c.startAnimation(1249);
			if (getItemToAdd() <= 0)
				break;
			c.getItems().addItem(getItemToAdd(), 1);
			c.getPA().addSkillXP(exp * 5,
					c.playerCrafting);
			int zulrah = Misc.random(256);
			if (zulrah == 142) {
				c.getItems().addItemToBank(12938, 1);
				c.sendMessage("@red@A mysterious teleport has been added to your bank!");
			}
		}
		resetCrafting();
	}

	public int getItemToAdd() {
		if (craftType == 1) {
			return vambs[index];
		} else if (craftType == 2) {
			return chaps[index];
		} else if (craftType == 3) {
			return bodys[index];
		} else if (craftType == 4) {
			return gloves[index];
		} else if (craftType == 5) {
			return coif[index];
		} else if (craftType == 6) {
			return cowl[index];
		} else if (craftType == 7) {
			return boots[index];
		}
		return -1;
	}
	public int craftAmt() {
		if (getItemToAdd() == vambs[index] || getItemToAdd() == gloves[index] || getItemToAdd() == coif[index] || getItemToAdd() == cowl[index] || getItemToAdd() == boots[index]) {
			return 1;
		} else if (getItemToAdd() == chaps[index]) {
			return 2;
		} else if (getItemToAdd() == bodys[index]) {
			return 3;
		}
		return -1;
	}

	public int[] vambs = { 1065, 2487, 2489, 2491, 1063 };
	public int[] chaps = { 1099, 2493, 2495, 2497, 1095 };
	public int[] bodys = { 1135, 2499, 2501, 2503, 1129 };
	public int[] gloves = { -1, -1, -1, -1, 1059 };
	public int[] coif = { -1, -1, -1, -1, 1169 };
	public int[] cowl = { -1, -1, -1, -1, 1167 };
	public int[] boots = { -1, -1, -1, -1, 1061 };

	public void handleLeather(int item1, int item2) {
		if (item1 == 1733) {
			openLeather(item2);
		} else {
			openLeather(item1);
		}
	}

	public int[][] buttons = { { 34185, 1, 1 }, { 34184, 1, 5 },
			{ 34183, 1, 10 }, { 34182, 1, 27 }, { 34189, 2, 1 },
			{ 34188, 2, 5 }, { 34187, 2, 10 }, { 34186, 2, 27 },
			{ 34193, 3, 1 }, { 34192, 3, 5 }, { 34191, 3, 10 },
			{ 34190, 3, 27 }, {33187, 3, 1}, {33186, 3, 5}, {33185, 3, 10}, {33190, 4, 1}, {33189, 4, 5}, {33188, 4, 10},
			{33193, 7, 1}, {33192, 7, 5}, {33191, 7, 10}, {33196, 1, 1}, {33195, 1, 5}, {33194, 1, 10},
			{33199, 2, 1}, {33198, 2, 5}, {33197, 2, 10}, {33202, 5, 1}, {33201, 5, 5}, {33200, 5, 10}, 
			{33205, 6, 1}, {33204, 6, 5}, {33203, 6, 10} };

	public int[][] expsAndLevels = { { 1745, 62, 57 }, { 2505, 66, 70 },
			{ 2507, 73, 78 }, { 2509, 79, 86 }, { 1741, 1, 20 } };

	public void openLeather(int item) {
		if (item == 1745) {
			c.getPA().sendFrame164(8880); // green dhide
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 1099); // middle
			c.getPA().sendFrame246(8883, 250, 1065); // left picture
			c.getPA().sendFrame246(8885, 250, 1135); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		} else if (item == 1741) {
			c.getPA().showInterface(2311);
			hideType = item;
		} else if (item == 2505) {
			c.getPA().sendFrame164(8880); // blue
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 2493); // middle
			c.getPA().sendFrame246(8883, 250, 2487); // left picture
			c.getPA().sendFrame246(8885, 250, 2499); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		} else if (item == 2507) {
			c.getPA().sendFrame164(8880);
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 2495); // middle
			c.getPA().sendFrame246(8883, 250, 2489); // left picture
			c.getPA().sendFrame246(8885, 250, 2501); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		} else if (item == 2509) {
			c.getPA().sendFrame164(8880);
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 2497); // middle
			c.getPA().sendFrame246(8883, 250, 2491); // left picture
			c.getPA().sendFrame246(8885, 250, 2503); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		}
		c.craftingLeather = true;
	}

}