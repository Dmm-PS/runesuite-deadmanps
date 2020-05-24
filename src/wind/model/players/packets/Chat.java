package wind.model.players.packets;

import wind.Punishments;
import wind.model.players.Censor;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.util.Misc;

/**
 * Chat
 **/
public class Chat implements PacketType {
	// Anti-Spam Check By: A Duck Tale
	int spamsAllowed = 9000; // amount of spams allowed
	int timeAllowed = 5 * 1000; // time to wait in milliseconds.

	// Initialize variables
	int spamCount = 0;
	long timeWaited = 0;
	long startTime = 0;
	long endTime = 0;
	byte[] message = null;
	byte[] storeMessage = null;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		message = c.getChatText();
		
		String chatText = Misc.textUnpack(message, c.getChatTextSize());
		
		Censor.performCensor(c, chatText);
		
		if (storeMessage == null) {
			storeMessage = message;
		} else if (storeMessage != null) {
			if (storeMessage == message) {
				spamCount++;
				if (startTime == 0) {
					startTime = System.currentTimeMillis();
				}
				if (spamCount >= spamsAllowed) {
					endTime = System.currentTimeMillis();
					timeWaited = endTime - startTime;
					if (timeWaited < timeAllowed) {
						System.out.println("Kicking " + c.playerName
								+ " For spamming.");
						startTime = 0;
						spamCount = 0;
						timeWaited = 0;
						endTime = 0;
						storeMessage = null;
						message = null;
						c.logout();
					} else if (timeWaited >= timeAllowed) {
						startTime = 0;
						spamCount = 0;
						timeWaited = 0;
						endTime = 0;
						storeMessage = null;
						message = null;
					}
				}
			} else if (storeMessage != message) {
				spamCount = 0;
				startTime = 0;
				storeMessage = message;
			}
		}
		if (System.currentTimeMillis() < c.muteEnd) {
			c.sendMessage("You are muted for breaking a rule.");
			return;
		} else {
			c.muteEnd = 0;
		}
		if (!Punishments.isMuted(c)) {
			ReportHandler.addText(c.playerName, c.getChatText(), packetSize - 2);
			c.setChatTextUpdateRequired(true);
		} else {
			c.sendMessage("You are muted for breaking a rule.");
			return;
		}
	}
}
