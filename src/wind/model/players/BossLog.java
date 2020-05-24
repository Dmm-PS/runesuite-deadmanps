package wind.model.players;

import wind.model.npcs.NPCHandler;

public class BossLog {

	public static int[] bossID = {0, 102};

	public static void handle() {
		
		System.out.println("waxt");
	}

	public static boolean boss(Client c) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] == null) {
				continue;
			}
			for (int ID = 0; ID < bossID.length; ID++) {
				if (NPCHandler.npcs[i].npcType == ID) {
					return true;
				}
			}
		}
		return false;
	}
}
