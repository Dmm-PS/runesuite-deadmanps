package wind.model.players.content.quest.quests;

import wind.model.players.Client;

/**
* Doric's Quest
* @author Acquittal
*/

public class DoricsQuest {

	public Client client;
	
	public DoricsQuest(Client client) {
		this.client = client;
	}
	public void doricReward() {
		client.getPA().sendFrame126("You have completed A Monk's Friend!" ,12144);
				client.questPoints += 1;
		client.getPA().sendFrame126(""+(client.questPoints) ,12147);
		client.getPA().sendFrame126("" ,12150);
		client.getPA().sendFrame126("The Chivalry & Piety prayers" ,12151);
		client.getPA().sendFrame126("aswell as one quest point!" ,12152);
		client.getPA().sendFrame126("" ,12153);
		client.getPA().sendFrame126("" ,12154);
		client.getPA().sendFrame126("" ,12155);
		client.getPlayerAssistant().sendFrame246(12145, 200, 1718);
		client.getPA().showInterface(12140);
		client.doricQuest = 4;
	}
	
	public void showInformation() {
		for(int i = 8144; i < 8195; i++) {
			client.getPA().sendFrame126("", i);
		}
		client.getPA().sendFrame126("@dre@A Monk's Friend", 8144);
		client.getPA().sendFrame126("", 8145);
		if(client.doricQuest == 0) {
			client.getPA().sendFrame126("To start the quest, talk to the High Priest", 8147);
			client.getPA().sendFrame126("who is wandering about the Monastery.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("You need the following skills:", 8150);
			if (client.playerLevel[1] >= 70)
			client.getPA().sendFrame126("@str@70 Defence", 8151);
			else
			client.getPA().sendFrame126("70 Defence", 8151);
			if (client.playerLevel[5] >= 60)
			client.getPA().sendFrame126("@str@60 Prayer", 8152);
			else
			client.getPA().sendFrame126("60 Prayer", 8152);
			if (client.playerLevel[12] >= 78)
			client.getPA().sendFrame126("@str@78 Crafting", 8153);
			else
			client.getPA().sendFrame126("78 Crafting", 8153);
		} else if(client.doricQuest == 1) {
			client.getPA().sendFrame126("@str@To start the quest, talk to the High Priest", 8147);
			client.getPA().sendFrame126("@str@who is wandering about the Monastery.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("You need to go find the priests brother.", 8150);
			client.getPA().sendFrame126("He is located in a cold place nearby.", 8151);
			client.getPA().sendFrame126("Where may that be?", 8152);
			client.getPA().sendFrame126("", 8153);
		} else if(client.doricQuest == 2) {
			client.getPA().sendFrame126("@str@To start the quest, you should talk with Doric", 8147);
			client.getPA().sendFrame126("@str@He is in his home north of Falador.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@You need to go find the priests brother.", 8150);
			client.getPA().sendFrame126("@str@He is located in a cold place nearby.", 8151);
			client.getPA().sendFrame126("@str@Where may that be?", 8152);
			client.getPA().sendFrame126("", 8153);
			client.getPA().sendFrame126("I need to return to the priest with the holy water.", 8154);
		} else if(client.doricQuest == 3) {
						client.getPA().sendFrame126("@str@To start the quest, you should talk with Doric", 8147);
			client.getPA().sendFrame126("@str@He is in his home north of Falador.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@You need to go find the priests brother.", 8150);
			client.getPA().sendFrame126("@str@He is located in a cold place nearby.", 8151);
			client.getPA().sendFrame126("@str@Where may that be?", 8152);
			client.getPA().sendFrame126("", 8153);
			client.getPA().sendFrame126("@str@I need to return to the priest with the holy water.", 8154);
			client.getPA().sendFrame126("", 8155);
			client.getPA().sendFrame126("I should create a holy symbol for the priest", 8156);
			client.getPA().sendFrame126("by using my crafting skills and the holy water.", 8157);
			} else if(client.doricQuest == 4) {
						client.getPA().sendFrame126("@str@To start the quest, you should talk with Doric", 8147);
			client.getPA().sendFrame126("@str@He is in his home north of Falador.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@You need to go find the priests brother.", 8150);
			client.getPA().sendFrame126("@str@He is located in a cold place nearby.", 8151);
			client.getPA().sendFrame126("@str@Where may that be?", 8152);
			client.getPA().sendFrame126("", 8153);
			client.getPA().sendFrame126("@str@I need to return to the priest with the holy water.", 8154);
			client.getPA().sendFrame126("", 8155);
			client.getPA().sendFrame126("@str@I should create a holy symbol for the priest", 8156);
			client.getPA().sendFrame126("@str@by using my crafting skills and the holy water.", 8157);
			client.getPA().sendFrame126("", 8158);
			client.getPA().sendFrame126("@red@QUEST COMPLETE!", 8159);
		}
		client.getPA().showInterface(8134);
	}
	
}
