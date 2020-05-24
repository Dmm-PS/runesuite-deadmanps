package wind.model.players.content.quest.quests;

import wind.model.players.Client;

/**
* Doric's Quest
* @author Acquittal
*/

public class Grandmaster {

	public Client client;
	
	public Grandmaster(Client client) {
		this.client = client;
	}
	public void grandReward() {
		client.getPA().sendFrame126("You have completed Grandmaster!" ,12144);
		client.questPoints += 2;
		client.getItems().addItem(6746, 1);
		client.getPA().sendFrame126(""+(client.questPoints) ,12147);
		client.getPA().sendFrame126("" ,12150);
		client.getPA().sendFrame126("The darklight sword" ,12151);
		client.getPA().sendFrame126("the quest cape" ,12152);
		client.getPA().sendFrame126("aswell as two quest points!" ,12153);
		client.getPA().sendFrame126("" ,12154);
		client.getPA().sendFrame126("" ,12155);
		client.getPlayerAssistant().sendFrame246(12145, 200, 1718);
		client.getPA().showInterface(12140);
		client.grandMaster = 3;
	}
	
	public void showInformation() {
		for(int i = 8144; i < 8195; i++) {
			client.getPA().sendFrame126("", i);
		}
		client.getPA().sendFrame126("@dre@Grandmaster", 8144);
		client.getPA().sendFrame126("", 8145);
		if(client.grandMaster == 0) {
			client.getPA().sendFrame126("To start the quest, talk to the OS Prime Guide", 8147);
			client.getPA().sendFrame126("who is located at home.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("You need to have the following to start the quest:", 8150);
			if (client.playerLevel[1] >= 99)
			client.getPA().sendFrame126("@str@99 Defence", 8151);
			else
			client.getPA().sendFrame126("99 Defence", 8151);
			if (client.playerLevel[5] >= 80)
			client.getPA().sendFrame126("@str@80 Prayer", 8152);
			else
			client.getPA().sendFrame126("80 Prayer", 8152);
				if (client.playerLevel[0] >= 99)
			client.getPA().sendFrame126("@str@99 Attack", 8153);
			else
			client.getPA().sendFrame126("99 Attack", 8153);
				if (client.playerLevel[2] >= 99)
			client.getPA().sendFrame126("@str@99 Strength", 8154);
			else
			client.getPA().sendFrame126("99 Strength", 8154);
				if (client.totalLevel >= 1882)
			client.getPA().sendFrame126("@str@1882 Total", 8155);
			else
			client.getPA().sendFrame126("1882 Total", 8155);
		} else if(client.grandMaster == 1) {
			client.getPA().sendFrame126("@str@To start the quest, talk to the OS Prime Guide", 8147);
			client.getPA().sendFrame126("@str@who is located at home.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("You need to go see Sir Prysin.", 8150);
			client.getPA().sendFrame126("He is located somewhere", 8151);
			client.getPA().sendFrame126("in the varrock castle.", 8152);
			client.getPA().sendFrame126("", 8153);
		} else if(client.grandMaster == 2) {
			client.getPA().sendFrame126("@str@To start the quest, you should talk with Doric", 8147);
			client.getPA().sendFrame126("@str@He is in his home north of Falador.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@You need to go see Sir Prysin.", 8150);
			client.getPA().sendFrame126("@str@He is located somewhere", 8151);
			client.getPA().sendFrame126("@str@in the varrock castle.", 8152);
			client.getPA().sendFrame126("", 8153);
			client.getPA().sendFrame126("I need to face the demons and defeat them,", 8154);
			client.getPA().sendFrame126("once and for all!", 8155);
		} else if(client.grandMaster == 3) {
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
			} else if(client.grandMaster == 4) {
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

