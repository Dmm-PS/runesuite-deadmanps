package wind.model.players.content.skills.impl;

import java.util.HashMap;
import java.util.Map;

import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

public class Prayer {
	
	private static enum Data {
		REGULAR(526, 100),
		BAT(530, 200),
		WOLF(2859, 300),
		DAGANNOTH(6729, 400),
		BIG(532, 500),
		OURG(4834, 600),
		BABY_DRAGON(534, 700),
		DRAGON(536, 800),
		ZOGRE(4812, 1000),
		LAVA(11943, 1500);
		
		Data(int boneId, int EXP) {
			this.boneId = boneId;
			this.EXP = EXP;
		}
		
		private int boneId, EXP;
		
		public int getBoneId() {
			return boneId;
		}
		
		public int getEXP() {
			return EXP;
		}
		
		private static Map<Object, Data> data = new HashMap<Object, Data>();

		static {
			for (Data d : Data.values()) {
				Data.data.put(d.getBoneId(), d);
			}
		}
	}

	public static void handleBurying(final Client player, int boneId, final int slot) {
		final Data data = Data.data.get(boneId);
		
		if (data == null) {
			return;
		}
		
		/**
		 * Is the player already skilling?
		 */
		if (!player.isSkilling()) {
			
			/**
			 * We set the player's status as skilling.
			 */
			player.setSkilling(true);
			
			player.startAnimation(827);
			player.getItems().deleteItem(data.getBoneId(), slot, 1);
	        player.sendMessage("You dig a hole in the ground...");
	        
	        TaskHandler.submit(new Task(2, false) {
	        	
	        	@Override
	        	public void execute() {
    				if(Misc.random(2) == 0) {
    					//Zombie.spawnZombie(player);
    				}
					/**
					 * Finishes burying the bone
					 */
					player.sendMessage("You bury the bones.");
					player.getPA().addSkillXP(data.getEXP(), player.playerPrayer);
					player.setSkilling(false);
					this.cancel();
	        	}
	        	@Override
	        	public void onCancel() {
	        		
	        	}
	        	
	        });
		}
	}
	
	public static void handleAltar(final Client player, int boneId) {
		final Data data = Data.data.get(boneId);
		
		if (data == null) {
			return;
		}
		
		/**
		 * Is the player already skilling?
		 */
		if (!player.isSkilling()) {
			
			/**
			 * We set the player's status as skilling.
			 */
			player.setSkilling(true);
			
			/**
			 * We make sure the player is facing the altar.
			 */
			player.turnPlayerTo(player.objectX, player.objectY);
			
			player.getPA().stillGfx(624, player.getX(), player.getY(), player.heightLevel, 0);
			player.startAnimation(2044);
			
			TaskHandler.submit(new Task(2, false) {
				@Override
				public void execute() {
					
					/**
					 *  Randomly spawns a zombie.
					 */
    				
					
					/**
					 * If the player walks away from the altar, or somehow stops skilling, 
					 * we end the tick.
					 */
					if (!player.isSkilling()) {
						player.startAnimation(65535);
						this.cancel();
						return;
					}
					
					/**
					 * We check if the player has anymore bones
					 */
					if (!player.getItems().playerHasItem(data.getBoneId())) {
						player.sendMessage("You ran out of bones.");
						player.setSkilling(false);
						player.startAnimation(65535);
						this.cancel();
					} else {
						
						/**
						 * Finishes giving the bone to the gods.
						 */
						player.getItems().deleteItem(data.getBoneId(), player.getItems().getItemSlot(data.getBoneId()), 1);
						player.sendMessage("The gods are pleased with your offering.");
						player.getPA().addSkillXP(data.getEXP() * 4, player.playerPrayer);
						player.getPA().stillGfx(624, player.getX(), player.getY(), player.heightLevel, 0);
						player.startAnimation(2044);
					}
				}
				@Override
				public void onCancel() {
					
				}
				
			});
		}
	}
}