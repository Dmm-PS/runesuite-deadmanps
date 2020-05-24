package wind.model.players.packets;

import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.packets.clicking.CorrectFirstClickButton;
import wind.model.players.packets.clicking.FirstClickButton;
import wind.util.Misc;

/**
 * Clicking most buttons
 * @param c
 * @param packetType
 * @param packetSize
 **/
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0,
				packetSize);		
		if (c.isDead)
			return;
		if (c.hasPin)
			return;

		CorrectFirstClickButton.handleButton(c, actionButtonId);
		
		FirstClickButton.handleClick(c, actionButtonId);
	}

}
