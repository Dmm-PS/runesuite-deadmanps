package wind.model.items;

public class Attachables {
	public enum attachData {
		SPECTRAL_SHIELD(12831, 12823, 12821, "Spectral Spirit shield"),
		TOXIC_SOTD(12932, 11791, 12904, "Toxic Staff of the Dead"),
		TOS(11907, 12932, 12899, "Trident of the Swamp"),
		BLOWPIPE(12922, 1755, 12926, "Toxic blowpipe"),
		;
		int ingredient_1, ingredient_2, product;
		String itemName;

		private attachData(int ingredient_1, int ingredient_2, int product, String itemName) {
			this.ingredient_1 = ingredient_1;
			this.ingredient_2 = ingredient_2;
			this.product = product;
			this.itemName = itemName;
		}
		public int getIngredient1() {
			return ingredient_1;
		}
		public int getIngredient2() {
			return ingredient_2;
		}
		public int getProduct() {
			return product;
		}
		public String getItemName() {
			return itemName;
		}
	}
}
