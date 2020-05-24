package wind.model.npcs.impl;

import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

/**
 * @information Class that handles the KalphiteQueens forms.
 */
public class KalphiteQueen {
	
	/**
	* KQ respawn first form
	*/
	public static void spawnFirstForm(Client c, final int i) {
		
		TaskHandler.submit(new Task(17, false) {
			@Override
			public void execute() {
				this.cancel();
			}
			@Override
			public void onCancel() {
				NPCHandler.spawnNpc2(1158, NPCHandler.npcs[i].absX, NPCHandler.npcs[i].absY, 0, 1, 230, 45, 500, 300);
			}			
		});		
	}
	
	public static void spawnSecondForm(Client c, final int i) {
		NPCHandler.npcs[i].gfx0(1055);
		
		TaskHandler.submit(new Task(5, false) {
			@Override
			public void execute() {
				this.cancel();
			}
			@Override
			public void onCancel() {
				NPCHandler.spawnNpc2(1160, NPCHandler.npcs[i].absX, NPCHandler.npcs[i].absY, 0, 1, 230, 45, 500, 300);
			}		
		});
	}
	
	public static boolean KQnpc(int i) {
		switch (NPCHandler.npcs[i].npcType) {
			case 1158:
			return true;	
		}
		return false;	
	}
	
	public static boolean fullVerac(Client c) {
		return c.playerEquipment[c.playerHat] == 4753 && c.playerEquipment[c.playerLegs] == 4759 && c.playerEquipment[c.playerChest] == 4757 && c.playerEquipment[c.playerWeapon] == 4755;
	}

}
