package shamon.barrows.impl;

import shamon.barrows.data.BarrowsConstants;
import wind.model.players.Client;

public class HideMiniMap {

	public static boolean toggle(Client client) {
		// 3520 9664
		
		// 3521 9728
		if ((client.absX >= 3521 && client.absX <= 3582) && (client.absY >= 9664 && client.absY <= 9728)) {
			client.getPA().sendFrame99(2);
			client.getPA().walkableInterface(BarrowsConstants.KILLCOUNT_WIDGET_ID);
			client.getPA().sendFrame126("Killcount: " + client.getBarrows().getNpcController().getKillCount(),
					BarrowsConstants.KILLCOUNT_TEXT_WIDGET_ID);
			return true;
		} else {
			client.getPA().sendFrame99(0);
			return false;
		}
	}
}
