package wind.model.players.content.skills.impl.farming.trees;

import wind.model.players.Client;
import wind.model.players.content.skills.impl.farming.Farming;
import wind.util.Misc;

public class TreePicking {

	public static void treePicking(Client c, int seed) {
		for (final Trees.Seeds t : Trees.Seeds.values()) {
			if (seed == t.getLog()) {
				if (c.treeAmount <= 0)
					return;
				if (c.getEquipment().freeSlots() < 1) {
					c.sendMessage("You don't have any free space to do this!");
					return;
				}
				if (c.treeAmount == 1) {
					c.currentTree = -1;
					c.treeAmount = -1;
					c.getPA().object(Farming.PATCH_WEEDS, c.objectX, c.objectY,
							-1, 10);
					c.sendMessage("You finish picking all the resources off the tree.");
				}
				c.startAnimation(2286);
				c.treeAmount--;
				c.getItems().addItem(t.getLog(), 1);
				c.getPA().addSkillXP(t.getExp(), 19);
			}
		}
	}

	public static int getTreeAmount(Client client, int seed) {
		return (seed < 5286) ? 4 + Misc.random(4) : 2 + Misc.random(6);
	}

	public static void removeDeadTree(Client client) {
		client.getPA().object(Farming.PATCH_WEEDS, client.objectX,
				client.objectY, -1, 10);
		client.sendMessage("You remove the dead tree from the patch.");
	}

}
