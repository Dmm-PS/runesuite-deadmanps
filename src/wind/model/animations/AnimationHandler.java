package wind.model.animations;

import wind.Config;
import wind.Constants;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

/**
 * @author 7Winds
 * 4/12/15
 * A class to handle custom animations.
 */
public class AnimationHandler {
	
	Client c;
	
	public AnimationHandler(Client c) {
		this.c = c;
	}
	
	public static int[] HomeTeleportGfx = {775, 802, 803, 804};
	public static int[] HomeTeleportAnimations = {4850, 4853, 4855, 4857, 4857};

	public void doHomeTeleport(final Client player) {
		if (c.isBusy() || c.inCombat)
			return;
		if (System.currentTimeMillis() - c.homeDelay < Constants.HomeTeleport_Timer) {
			return;
		}
		c.homeDelay = System.currentTimeMillis();
		player.sendMessage("Starting HomeTeleport");
		TaskHandler.submit(new Task(3, false) {
			int gfxIndex = 0;
			int animIndex = 0;

            @Override
            public void execute() {
            	if (c.inCombat || c.isBusy()) {
            		this.cancel();
            	}
            	c.usingHomeTeleport = true;
				player.startAnimation(HomeTeleportAnimations[animIndex]);
				player.gfx0(HomeTeleportGfx[gfxIndex], 0);
				gfxIndex++;
				animIndex++;
				if (gfxIndex > 3)
					gfxIndex = 3;
				if (animIndex > 4)
					animIndex = 4;
				if (gfxIndex == 3 && animIndex == 4)
					this.cancel();
			}            
            @Override
            public void onCancel() {
            	c.startAnimation(-1);
        		c.getPA().movePlayer(Config.HOME_X, Config.HOME_Y, 0);
            }
		});
	}
}
