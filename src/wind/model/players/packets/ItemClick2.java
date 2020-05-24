package wind.model.players.packets;

import wind.model.items.impl.MembershipBond;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.Rights;
import wind.model.players.content.skills.impl.Slayer;
import wind.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();

		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		switch (itemId) {
		case 13190:
		case 13192:
			MembershipBond.openInterface(c);
			break;
		case 15098:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(15088, 1);
			break;
		case 15088:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(15098, 1);
			break;
		case 11802:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 12926:
			c.sendMessage("@red@You currently have " + c.dartsLoaded + " darts in your blowpipe.");
		break;
		case 11804:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 11806:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 11808:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 4155:
			c.sendMessage("You currently have a task of " + c.taskAmount
					+ " " + Slayer.getTaskName(c.slayerTask) + "(s).");
			break;
		default:
			if (c.getRights().equal(Rights.DEVELOPER))
				Misc.println(c.playerName + " - Item2ndOption: " + itemId);
			break;
		}

	}

}
