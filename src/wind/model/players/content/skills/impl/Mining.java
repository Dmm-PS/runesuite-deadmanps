package wind.model.players.content.skills.impl;

import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.players.Inventory;
import wind.model.players.content.skills.SkillHandler;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.world.ObjectHandler;

public class Mining extends SkillHandler{
//Authored by zeruth
	
    public static final Object MINING_KEY = new Object();
	
	public static void mine(final Client c, final int object,
			final int obX, final int obY) {
		c.getInventory();
		c.getInventory();
		if (Inventory.isFull(c)) {
			c.sendMessage("Your inventory is full.");;
			return;
		}
		if (!hasPickaxe(c)) {
			c.turnPlayerTo(c.objectX, c.objectY);
			c.sendMessage("You need a Mining pickaxe that you have the Mining level to use.");
			return;
		}
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				if (playerMiningLevel(c)<data[i][2]){
					c.sendMessage("You need level "+data[i][2]+" Mining to collect this resource.");
					return;
				}
			}
			}
		c.turnPlayerTo(c.objectX, c.objectY);
		c.startAnimation(getAnimation(c));
				
		Task mining = new Task(getMineTime(c, object), false) {	
				    
			@Override
			public void execute() {
				c.getItems().addItem(data[getRock(c, object)][1], 1);
				c.updateInventory=true;
				c.sendMessage("You manage to mine some "
						+ ItemAssistant
								.getItemName(
										data[getRock(c, object)][1])
								.toLowerCase() + ".");
				c.getPA().addSkillXP(
						data[getRock(c, object)][5] * MINING_XP,
						c.playerMining);
				ObjectHandler.createAnObject(c, 451, obX,
						obY);
				this.cancel();
			}
			
			@Override
			public void onCancel() {
				c.startAnimation(-1);
			}					
		};
		c.getPA().closeAllWindows();
		mining.attach(MINING_KEY);
		TaskHandler.cancel(MINING_KEY);
		TaskHandler.submit(mining);
	}
	
	private static int playerMiningLevel(Client c) {
		System.out.println((10 - (int) Math.floor(c.playerLevel[14] / 10)));
		return (10 - (int) Math.floor(c.playerLevel[14] / 10));
	}
	
	private static int getMineTime(Client c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return data[i][3];
			}
		}
		return -1;
	}
	private static int getRock(Client c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return i;
			}
		}
		return -1;
	}

	public static boolean hasPickaxe(Client c) {
		for (int i = 0; i < animation.length; i++) {
			if (c.getItems().playerHasItem(animation[i][0])
					|| c.playerEquipment[3] == animation[i][0]) {
				return true;
			}
		}
		return false;
	}
	
	private static int getAnimation(Client c) {
		for (int i = 0; i < animation.length; i++) {
			if (c.getItems().playerHasItem(animation[i][0])
					|| c.playerEquipment[3] == animation[i][0]) {
				return animation[i][1];
			}
		}
		return -1;
	}
	
	public static boolean miningRocks(Client c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return true;
			}
		}
		return false;
	}
	
	private static int[][] animation = { { 1275, 6746 }, { 1271, 6750 },
			{ 1273, 6751 }, { 1269, 6749 }, { 1267, 6748 }, { 1265, 6747 }, };

	@SuppressWarnings("unused")
	private static int[][] pickaxe = { { 1275, 41, 0 }, // RUNE
			{ 1271, 31, 1 }, // ADDY
			{ 1273, 21, 2 }, // MITH
			{ 1269, 6, 3 }, // STEEL
			{ 1267, 1, 3 }, // IRON
			{ 1265, 1, 4 }, // BRONZE
	};

	private static int[][] data = { 
			{ 14912, 7936, 1, 1, 1, 5 }, // Pure Ess
			{ 2491, 7936, 1, 1, 1, 5 }, // 
			{ 9711, 434, 1, 1, 1, 5 }, // CLAY
			{ 9713, 434, 1, 1, 1, 5 }, //
			{ 11174, 434, 1, 1, 1, 5 }, //
			{ 11175, 434, 1, 1, 1, 5 }, //
			{ 13458, 434, 1, 1, 1, 5 }, //
			{ 13457, 434, 1, 1, 1, 5 }, //
			{ 13456, 434, 1, 1, 1, 5 }, //
			{ 14181, 434, 1, 1, 1, 5 }, //
			{ 2094, 438, 1, 3, 1, 5 }, // TIN
			{ 2095, 438, 1, 3, 1, 5 }, // 
			{ 9714, 438, 1, 3, 1, 5 }, // 
			{ 9716, 438, 1, 3, 1, 5 }, // 
			{ 11959, 438, 1, 3, 1, 5 }, // 
			{ 11957, 438, 1, 3, 1, 5 }, // 
			{ 11958, 438, 1, 3, 1, 5 }, // 
			{ 13449, 438, 1, 3, 1, 5 }, // 
			{ 13448, 438, 1, 3, 1, 5 }, // 
			{ 13447, 438, 1, 3, 1, 5 }, // 
			{ 13712, 438, 1, 3, 1, 5 }, // 
			{ 13713, 438, 1, 3, 1, 5 }, // 
			{ 13716, 438, 1, 3, 1, 5 }, // 
			{ 14863, 438, 1, 3, 1, 5 }, // 
			{ 14864, 438, 1, 3, 1, 5 }, // 
			{ 14883, 438, 1, 3, 1, 5 }, // 
			{ 13708, 436, 1, 3, 1, 5 }, // COPPER
			{ 2091, 436, 1, 3, 1, 5 }, //
			{ 2090, 436, 1, 3, 1, 5 }, // 
			{ 11961, 436, 1, 3, 1, 5 }, // 
			{ 11962, 436, 1, 3, 1, 5 }, // 
			{ 11960, 436, 1, 3, 1, 5 }, // 
			{ 13450, 436, 1, 3, 1, 5 }, // 
			{ 13451, 436, 1, 3, 1, 5 }, // 
			{ 13452, 436, 1, 3, 1, 5 }, // 
			{ 14886, 436, 1, 3, 1, 5 }, // 
			{ 14885, 436, 1, 3, 1, 5 }, // 
			{ 14884, 436, 1, 3, 1, 5 }, // 
			{ 2093, 440, 15, 6, 2, 5 }, // IRON
			{ 2092, 440, 15, 6, 2, 5 }, // 
			{ 11956, 440, 15, 6, 2, 5 }, // 
			{ 11954, 440, 15, 6, 2, 5 }, // 
			{ 11955, 440, 15, 6, 2, 5 }, // 
			{ 13444, 440, 15, 6, 2, 5 }, // 
			{ 13445, 440, 15, 6, 2, 5 }, // 
			{ 13710, 440, 15, 6, 2, 5 }, // 
			{ 13711, 440, 15, 6, 2, 5 }, // 
			{ 13444, 440, 15, 6, 2, 5 }, //
			{ 13445, 440, 15, 6, 2, 5 }, //
			{ 13446, 440, 15, 6, 2, 5 }, //
			{ 9717, 453, 30, 8, 3, 8 }, // COAL
			{ 9719, 453, 30, 8, 3, 8 }, // 
			{ 13714, 453, 30, 8, 3, 8 }, // 
			{ 13706, 453, 30, 8, 3, 8 }, // 
			{ 9718, 453, 30, 8, 3, 8 }, // 
			{ 2095, 453, 30, 8, 3, 8 }, // 
			{ 2096, 453, 30, 8, 3, 8 }, // 
			{ 13716, 442, 20, 8, 5, 5 }, // SILVER
			{ 13717, 442, 20, 8, 5, 5 }, // 
			{ 13438, 442, 20, 8, 5, 5 }, // 	
			{ 13439, 442, 20, 8, 5, 5 }, // 
			{ 13440, 442, 20, 8, 5, 5 }, // 
			{ 2098, 444, 40, 8, 3, 10 }, // GOLD
			{ 2099, 444, 40, 8, 3, 10 }, // 
			{ 13707, 444, 40, 8, 3, 10 }, // 
			{ 13715, 444, 40, 8, 3, 10 }, // 
			{ 9722, 444, 40, 8, 3, 10 }, // 
			{ 9720, 444, 40, 8, 3, 10 }, // 
			{ 2103, 447, 55, 10, 5, 25 }, // MITH
			{ 2102, 447, 55, 10, 5, 25 }, // 
			{ 13718, 447, 55, 10, 5, 25 }, // 
			{ 13719, 447, 55, 10, 5, 25 }, // 
			{ 2105, 449, 70, 12, 7, 50 }, // ADDY
			{ 13720, 449, 70, 12, 7, 50 }, //
			{ 14168, 449, 70, 12, 7, 50 }, // 
			{ -1, 451, 85, 14, 40, 100 }, // RUNE
	};

}
