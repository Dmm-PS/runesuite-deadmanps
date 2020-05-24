package wind.model.players.content.quest.quests;

import wind.Punishments;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;

/**
* Cooks' Assistant
* @author Acquittal
*/

public class CooksAssistant {

	public Client client;
	
	public CooksAssistant(Client client) {
		this.client = client;
	}
		public void questReward() {
		if (Punishments.hasRecieved1stStarter(PlayerHandler.players[client.playerId].connectedFrom) && Punishments.hasRecieved2ndStarter(PlayerHandler.players[client.playerId].connectedFrom)) {
		client.getPA().sendFrame126("You have completed Learning the ropes!" ,12144);
		client.questPoints += 1;
		client.getPA().sendFrame126(""+(client.questPoints) ,12147);
		client.getPA().sendFrame126("" ,12150);
		client.getPA().sendFrame126("@str@A 500k bonus in your starter" ,12151);
		client.getPA().sendFrame126("aswell as a quest point!" ,12152);
		client.getPA().sendFrame126("" ,12153);
		client.getPA().sendFrame126("" ,12154);
		client.getPA().sendFrame126("" ,12155);
		client.getPlayerAssistant().sendFrame246(12145, 250, 1856);
		client.getPA().showInterface(12140);
	client.completedTut = true;
	client.inTut = false;
		client.cooksA = 2;		
	} else {
		client.getPA().addStarter2();
				client.getPA().sendFrame126("You have completed Learning the ropes!" ,12144);
				client.questPoints += 1;
		client.getPA().sendFrame126(""+(client.questPoints) ,12147);
		client.getPA().sendFrame126("" ,12150);
		client.getPA().sendFrame126("A 500k bonus in your starter" ,12151);
		client.getPA().sendFrame126("aswell as a quest point!" ,12152);
		client.getPA().sendFrame126("" ,12153);
		client.getPA().sendFrame126("" ,12154);
		client.getPA().sendFrame126("" ,12155);
		client.getPlayerAssistant().sendFrame246(12145, 250, 1856);
		client.getPA().showInterface(12140);
	client.completedTut = true;
		client.inTut = false;
		client.cooksA = 2;		
		}
		}
	public void showInformation() {
		for(int i = 8144; i < 8195; i++) {
			client.getPA().sendFrame126("", i);
		}
		client.getPA().sendFrame126("@dre@Learning the ropes", 8144);
		client.getPA().sendFrame126("", 8145);
		if(client.cooksA == 0) {
			client.getPA().sendFrame126("To start this quest, talk to the OS Prime Guide", 8147);
			client.getPA().sendFrame126("at home and follow his instructions.", 8148);
		} else if(client.cooksA == 1) {
			client.getPA().sendFrame126("@str@To start this quest, talk to the OS Prime Guide", 8147);
			client.getPA().sendFrame126("@str@at home and follow his instructions.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("You should take and finish the tour to recieve", 8150);
			client.getPA().sendFrame126("a reward to get you started.", 8151);
			client.getPA().sendFrame126("", 8152);
			client.getPA().sendFrame126("", 8153);
		} else if(client.cooksA == 2) {
			client.getPA().sendFrame126("@str@To start this quest, talk to the OS Prime Guide", 8147);
			client.getPA().sendFrame126("@str@at home and follow his instructions.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@You should take and finish the tour to recieve", 8150);
			client.getPA().sendFrame126("@str@a reward to get you started.", 8151);
			client.getPA().sendFrame126("", 8152);
			client.getPA().sendFrame126("@red@QUEST COMPLETE!", 8153);
		} else if(client.cooksA == 3) {
			client.getPA().sendFrame126("@str@To start the quest, you should talk with Cook", 8147);
			client.getPA().sendFrame126("@str@found in Lumbridge.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@Cook have asked you to get the following items:", 8150);
			client.getPA().sendFrame126("@str@A bucket of milk", 8151);
			client.getPA().sendFrame126("@str@A pot of flour", 8152);
			client.getPA().sendFrame126("@str@An egg", 8153);
			client.getPA().sendFrame126("", 8154);
			client.getPA().sendFrame126("You have completed this quest!", 8155);
			client.getPA().sendFrame126("You have been rewarded with ", 8156);
		}
		client.getPA().showInterface(8134);
	}
	
}
