package wind.model.players.content.skills.impl.herblore;

import wind.Config;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

public class Grinding extends HerbloreData {

	public static void grindItem(final Client c, final int itemUsed,
			final int usedWith) {
		for (final GrindableData g : GrindableData.values()) {
			if (itemUsed == PESTLE && usedWith == g.getInput()
					|| itemUsed == g.getInput() && usedWith == PESTLE) {
				c.itemToDelete = g.getInput();
				c.itemToAdd = g.getOutput();
				if (c.playerSkilling[c.playerHerblore]) {
					return;
				}
				if (c.itemToDelete <= 0) {
					return;
				}
				c.playerSkilling[c.playerHerblore] = true;
				c.stopPlayerSkill = true;
				c.getPA().removeAllWindows();
				c.startAnimation(ANIM2);
				
				TaskHandler.submit(new Task(3, true) {
					
					@Override
					public void execute() {
						if (!c.getItems().playerHasItem(c.itemToDelete, 1)) {
							this.cancel();
						}
						c.getItems().deleteItem(c.itemToDelete, 1);
						c.getItems().addItem(c.itemToAdd, 1);
						c.sendMessage("You grind the ingredient with your pestle and mortar.");
						c.getPA().addSkillXP(1 * Config.HERBLORE_EXPERIENCE,
								c.playerHerblore);
						c.startAnimation(ANIM2);
					}
					@Override
					public void onCancel() {
						resetGrinding(c);
					}					
				});
			}
		}
	}

	public static void resetGrinding(final Client c) {
		c.itemToDelete = -1;
		c.itemToAdd = -1;
		c.playerSkilling[c.playerHerblore] = false;
		c.stopPlayerSkill = false;
	}

}
