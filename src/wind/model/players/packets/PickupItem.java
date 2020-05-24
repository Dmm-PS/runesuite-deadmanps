package wind.model.players.packets;

import wind.Server;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.task.Task;
import wind.task.TaskHandler;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		c.walkingToItem = false;
		c.pItemY = c.getInStream().readSignedWordBigEndian();
		c.pItemId = c.getInStream().readUnsignedWord();
		c.pItemX = c.getInStream().readSignedWordBigEndian();
		if (Math.abs(c.getX() - c.pItemX) > 25
				|| Math.abs(c.getY() - c.pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
	    if(!Server.itemHandler.itemExists(c.pItemId, c.pItemX, c.pItemY)) {
	        c.stopMovement();
	        return;
	    }
		c.getCombat().resetPlayerAttack();
		if (c.getX() == c.pItemX && c.getY() == c.pItemY) {
			Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX,
					c.pItemY, true);
		} else {
			c.walkingToItem = true;
			
			TaskHandler.submit(new Task(1, true) {
				
				@Override
				public void execute() {
					if (!c.walkingToItem)
						this.cancel();
					if (c.getX() == c.pItemX && c.getY() == c.pItemY) {
						Server.itemHandler.removeGroundItem(c, c.pItemId,
								c.pItemX, c.pItemY, true);
						this.cancel();
					}
				}
				
				@Override
				public void onCancel() {
					c.walkingToItem = false;
				}
				
			});
		}
	}

}
