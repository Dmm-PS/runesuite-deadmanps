package wind.model.players.content.skills.impl.herblore;

import wind.model.players.Client;

public class Decanting {
	public enum Potion {
		
		STRENGTH(113, 115, 117, 119),
		ATTACK(2428, 121, 123, 125),
		RESTORE(2430, 127, 129, 131),
		DEFENCE(2432, 133, 135, 137),
		PRAYER(2434, 139, 141, 143),
		FISHING(2438, 151, 153, 155),
		RANGING(2444, 169, 171, 173),
		ANTIFIRE(2452, 2454, 2456, 2458),
		ENERGY(3008, 3010, 3012, 3014),
		AGILITY(3032, 3034, 3036, 3038),
		MAGIC(3040, 3042, 3044, 3046),
		COMBAT(9739, 9741, 9743, 9745),
		SUMMONING(12140, 12142, 12144, 12146),
		SUPER_ATTACK(2436, 145, 147, 149),
		SUPER_STRENGTH(2440, 157, 159, 161),
		SUPER_DEFENCE(2442, 163, 165, 167),
		SUPER_ENERGY(3016, 3018, 3020, 3022),
		SUPER_RESTORE(3024, 3026, 3028, 3030);
		Potion(int fullId, int threeQuartersId, int halfId, int quarterId) {
			this.quarterId = quarterId;
			this.halfId = halfId;
			this.threeQuartersId = threeQuartersId;
			this.fullId = fullId;
		}
		
		private int quarterId, halfId, threeQuartersId, fullId;
		
		public int getQuarterId() {
			return this.quarterId;
		}
		
		public int getHalfId() {
			return this.halfId;
		}
		
		public int getThreeQuartersId() {
			return this.threeQuartersId;
		}
		
		public int getFullId() {
			return this.fullId;
		}


	}
	public static void startDecanting(Client c) {
		for(Potion p : Potion.values()) {
			int full = p.getFullId();
			int half = p.getHalfId();
			int quarter = p.getQuarterId();
			int threeQuarters = p.getThreeQuartersId();
			int totalDoses = 0;
			int remainder = 0;
			int totalEmptyPots = 0;
			if(c.getItems().playerHasItem(threeQuarters)) {
				totalDoses += (3 * c.getItems().getItemAmount(threeQuarters));
				totalEmptyPots += c.getItems().getItemAmount(threeQuarters);
				c.getItems().deleteItem(threeQuarters, c.getItems().getItemAmount(threeQuarters));
			}
			if(c.getItems().playerHasItem(half)) {
				totalDoses += (2 * c.getItems().getItemAmount(half));
				totalEmptyPots += c.getItems().getItemAmount(half);
				c.getItems().deleteItem(half, c.getItems().getItemAmount(half));
			}
			if(c.getItems().playerHasItem(quarter)) {
				totalDoses += (1 * c.getItems().getItemAmount(quarter));
				totalEmptyPots += c.getItems().getItemAmount(quarter);
				c.getItems().deleteItem(quarter, c.getItems().getItemAmount(quarter));
			}
			if(totalDoses > 0) {
				if(totalDoses >= 4)
					c.getItems().addItem(full, totalDoses / 4);
				else if(totalDoses == 3)
					c.getItems().addItem(threeQuarters, 1);
				else if(totalDoses == 2)
					c.getItems().addItem(half, 1);
				else if(totalDoses == 1)
					c.getItems().addItem(quarter, 1);
				if((totalDoses % 4) != 0) {
					totalEmptyPots -= 1;
					remainder = totalDoses % 4;
					if(remainder == 3)
						c.getItems().addItem(threeQuarters, 1);
					else if(remainder == 2)
						c.getItems().addItem(half, 1);
					else if(remainder == 1)
						c.getItems().addItem(quarter, 1);
				}
				totalEmptyPots -= (totalDoses / 4);
				c.getItems().addItem(229, totalEmptyPots);
			}
		}
	}


}