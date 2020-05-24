package wind.model.players.content.quest.quests;

import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;

/**
* Horror from the Deep
* @author Acquittal
*/
public class HorrorFromTheDeep {

	public Client client;
	
	public HorrorFromTheDeep(Client client) {
		this.client = client;
	}
	
	public void changeMother(int i) {
		int npcType = getNextDag(NPCHandler.npcs[i].npcType);
		int x = NPCHandler.npcs[i].absX;
		int y = NPCHandler.npcs[i].absY;
		int heightLevel = NPCHandler.npcs[i].heightLevel;
		int WalkingType = NPCHandler.npcs[i].walkingType;
		int HP = NPCHandler.npcs[i].HP;
		int MaxHP = NPCHandler.npcs[i].MaxHP;
		int maxHit = NPCHandler.npcs[i].maxHit;
		int attack = NPCHandler.npcs[i].attack;
		int defence = NPCHandler.npcs[i].defence;
		NPCHandler.npcs[i].absX = 0;
		NPCHandler.npcs[i].absY = 0;
		NPCHandler.npcs[i] = null;
		int slot = -1;
		for (int j = 1; j < NPCHandler.maxNPCs; j++) {
			if (NPCHandler.npcs[j] == null) {
				slot = j;
				break;
			}
		}

		if(slot == -1) return;
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = MaxHP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		NPCHandler.npcs[slot] = newNPC;
		NPCHandler.npcs[slot].updateRequired = true;
		client.getPA().requestUpdates();
		NPCHandler.npcs[slot].spawnedBy = client.playerId;
		NPCHandler.npcs[slot].killerId = client.playerId;
		NPCHandler.npcs[slot].underAttack = true;
		NPCHandler.npcs[slot].dagColor = getDagColor(npcType);
		client.getPA().drawHeadicon(1, slot, 0, 0);
		client.sendMessage("Color: "+NPCHandler.npcs[slot].dagColor);
	}
	
	public int getNextDag(int lastDag) {
		switch(lastDag) {
			case 1351:
				return 1352;
			case 1352:
				return 1356;
			case 1356:
				return 1353;
			case 1353:
				return 1354;
			case 1354:
				return 1355;
			case 1355:
				return 1351;
			default:
			return -1;
		}
	}
	
	public String getDagColor(int dag) {
		switch(dag) {
			case 1351:
				return "white";
			case 1352:
				return "blue";
			case 1356:
				return "brown";
			case 1353:
				return "red";
			case 1354:
				return "orange";
			case 1355:
				return "green";
			default:
			return "";
		}
	}
	
	public void showInformation() {
		for(int i = 8144; i < 8195; i++) {
			client.getPA().sendFrame126("", i);
		}
		client.getPA().sendFrame126("@dre@Lunar's Legacy", 8144);
		client.getPA().sendFrame126("", 8145);
		if(client.horrorFromDeep == 0) {
			client.getPA().sendFrame126("Talk to Lunar at home", 8147);
			client.getPA().sendFrame126("to start this quest.", 8148);
			client.getPA().sendFrame126("", 8149);
			if (client.playerLevel [6] >= 80) {
			client.getPA().sendFrame126("@str@80 Magic", 8150);
			} else {
			client.getPA().sendFrame126("80 Magic", 8150);
			}
			if (client.playerLevel [14] >= 65) {
			client.getPA().sendFrame126("@str@65 Mining", 8151);
			} else {
			client.getPA().sendFrame126("65 Mining", 8151);
			}
		} else if(client.horrorFromDeep == 1) {
			client.getPA().sendFrame126("@str@Talk to Lunar at home", 8147);
			client.getPA().sendFrame126("@str@to start this quest.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("Lunar wants you to bring him a", 8150);
			client.getPA().sendFrame126("special ore. But where is it?", 8151);
		} else if(client.horrorFromDeep == 2) {
			client.getPA().sendFrame126("@str@Talk to Jossik in Draynor", 8147);
			client.getPA().sendFrame126("@str@To start this quest.", 8148);
			client.getPA().sendFrame126("", 8149);
			client.getPA().sendFrame126("@str@Lunar wants you to bring him a", 8150);
			client.getPA().sendFrame126("@str@special ore. But where is it?", 8151);
			client.getPA().sendFrame126("", 8152);
			client.getPA().sendFrame126("@red@QUEST COMPLETE!", 8153);
		}
		client.getPA().showInterface(8134);
	}
	public void questReward() {
		client.getPA().sendFrame126("You have completed Lunar's Legacy!" ,12144);
				client.questPoints += 2;
		client.getPA().sendFrame126(""+(client.questPoints) ,12147);
		client.getPA().sendFrame126("" ,12150);
		client.getPA().sendFrame126("The lunar prayers" ,12151);
		client.getPA().sendFrame126("aswell as two quest points!" ,12152);
		client.getPA().sendFrame126("" ,12153);
		client.getPA().sendFrame126("" ,12154);
		client.getPA().sendFrame126("" ,12155);
		client.getPlayerAssistant().sendFrame246(12145, 200, 9075);
		client.getPA().showInterface(12140);
		client.horrorFromDeep = 2;
	}
	public boolean checkForDagChange(int i) {
		int[] dags = {
			1351, 1352, 1353, 1354, 1355, 1356
		};
		for(int dag : dags) {
			if(NPCHandler.npcs[i] != null) {
				if(NPCHandler.npcs[i].npcType == dag && !NPCHandler.npcs[i].isDead && NPCHandler.npcs[i].killerId > 0) {
					if(client.lastDagChange == -1) {
						client.lastDagChange = System.currentTimeMillis();
					}
					if(System.currentTimeMillis() - client.lastDagChange >= 10000) {
						changeMother(i);
						client.lastDagChange = System.currentTimeMillis();
						return true;
					}
				}
			}
		}
		return false;
	}
	
}

