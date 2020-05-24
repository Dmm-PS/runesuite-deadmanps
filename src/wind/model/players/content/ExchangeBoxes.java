package wind.model.players.content;

public class ExchangeBoxes {
	
	public enum boxInformation {
		CANNON(12863, 6, 8, 10, 12, -1),
		GREEN_DRAGONHIDE(12865, 1135, 1099, -1, -1, 1065),
		BLUE_DRAGONHIDE(12867, 2499, 2493, -1, -1, 2487),
		RED_DRAGONHIDE(12869, 2501, 2495, -1, -1, 2489),
		BLACK_DRAGONHIDE(12871, 2503, 2497, -1, -1, 2491),
		GUTHANS(12873, 4728, 4730, 4724, 4726, -1),
		VERACS(12875, 4757, 4759, 4753, 4755, -1),
		DHAROKS(12877, 4720, 4722, 4716, 4718, -1), 
		TORAGS(12879, 4749, 4751, 4745, 4747, -1),
		AHRIM(12881, 4712, 4714, 4708, 4710, -1),
		KARIL(12883, 4736, 4738, 4732, 4734, -1)
		;
		private int itemID, bodyID, legID, helmetID, weaponID, gloveID;
		/**
		 * 
		 * @param itemID - item ID
		 * @param bodyID - body item ID
		 * @param legID - leg item ID
		 * @param helmetID - helmet item ID
		 * @param weaponID - weapon item ID
		 * @param gloves - gloves item ID
		 */
		private boxInformation(int itemID, int bodyID, int legID, int helmetID, int weaponID, int gloveID) {
			this.itemID = itemID;
			this.bodyID = bodyID;
			this.legID = legID;
			this.helmetID = helmetID;
			this.weaponID = weaponID;
			this.gloveID = gloveID;
		}
		/**
		 * @itemID - item ID
		 * @return
		 */
		public int getItemID() {
			return itemID;
		}
		public void setItemID(int itemID) {
			this.itemID = itemID;
		}
		/**
		 * @bodyID - body ID
		 * @return
		 */
		public int getBodyID() {
			return bodyID;
		}
		public void setBodyID(int bodyID) {
			this.bodyID = bodyID;
		}
		/**
		 * @legID leg ID
		 * @return
		 */
		public int getLegID() {
			return legID;
		}
		public void setLegID(int legID) {
			this.legID = legID;
		}
		public int getHelmetID() {
			return helmetID;
		}
		public void setHelmetID(int helmetID) {
			this.helmetID = helmetID;
		}
		public int getWeaponID() {
			return weaponID;
		}
		public void setWeaponID(int weaponID) {
			this.weaponID = weaponID;
		}
		public int getGloveID() {
			return gloveID;
		}
		public void setGloveID(int gloveID) {
			this.gloveID = gloveID;
		}
	}
}
