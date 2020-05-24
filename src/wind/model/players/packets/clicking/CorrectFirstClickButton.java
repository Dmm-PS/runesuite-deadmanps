package wind.model.players.packets.clicking;

import wind.model.players.Client;
import wind.model.players.Rights;

public class CorrectFirstClickButton {

	public static void handleButton(Client client, int doNotUse) {
		int actionButtonID = intTohex(doNotUse);

		if(client.getBarrows().getPuzzle().handleClick(client, actionButtonID)) {
			return;
		}
		
		if (client.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
			System.out.println(client.playerName + ": Unhandled correct button: " + actionButtonID);
		}
	}
	
	/**
	 * Reverses the incorrect data type.
	 */
	private static int intTohex(int hex) {
		int first = hex / 1000;
		int second = hex - (first * 1000);
		return (first << 8) | second;
	}
}
