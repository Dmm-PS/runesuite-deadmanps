package wind.model.players;

public class TanHide {

	private Client c;

	public TanHide(Client c) {
		this.c = c;
	}
	
	private enum Tan {
		SOFTLEATHER(1739, 1741, 1),
		HARDLEATHER(1739, 1743, 3),
		GREENDHIDE(1753, 1745, 20),
		BLUEDHIDE(1751, 2505, 20),
		REDDHIDE(1749, 2507, 20),
		BLACKDHIDE(1747, 2509, 20);

		private int hide, tannedHide, cost;

		private Tan(int hide, int tannedHide, int cost) {
			this.hide = hide;
			this.tannedHide = tannedHide;
			this.cost = cost;
		}

		public int getHide() {
			return hide;
		}

		public int getTannedHide() {
			return tannedHide;
		}

		public int getCost() {
			return cost;
		}
	}

	private Tan forHide(int id) {
		for (Tan t : Tan.values()) {
			if (t.getTannedHide() == id) {
				return t;
			}
		}
		return null;
	}

	public void tanHide(Tan hide) {
		Tan t = forHide(hide.getTannedHide());
		if (t != null) {
			int amtOfHides, cost; 
			if (c.getItems().getItemCount(t.getHide()) > 0) {
				amtOfHides = c.getItems().getItemCount(t.getHide());
				cost = amtOfHides * t.getCost();
				if (c.getItems().playerHasItem(995, cost)) {
					amtOfHides = c.getItems().getItemCount(t.getHide());
				} else {
					c.sendMessage("You do not have enough money with you to tan all the hides at once.");
					return;
				}
				c.getItems().deleteItem(t.getHide(), amtOfHides);
				c.getItems().deleteItem(995, c.getItems().getItemSlot(995), cost);
				c.getItems().addItem(t.getTannedHide(), amtOfHides);
				c.sendMessage("You tan "+amtOfHides+(amtOfHides > 1 ? " hides" : " hide")+" for "+cost+" coins.");
			} else {
				c.sendMessage("You don't have any hides you can tan.");
			}
			resetCrafting();
		}
	}

	private void resetCrafting() {
		c.tanning = false;
	}

	public void handleActionButton(int abutton) {
		switch(abutton) {
		case 57225: //Soft leather 
			tanHide(Tan.SOFTLEATHER);
			break;
		case 57226: //Hard leather
			tanHide(Tan.HARDLEATHER);
			break;
		case 57227: //Green d-hide
			tanHide(Tan.GREENDHIDE);
			break;
		case 57228: //Blue d-hide
			tanHide(Tan.BLUEDHIDE);
			break;
		case 57229: //Red d-hide
			tanHide(Tan.REDDHIDE);
			break;
		case 57230: //Black d-hide
			tanHide(Tan.BLACKDHIDE);
			break;
		case 57231: //Unused
			c.sendMessage("This option is currently not featured.");
			break;
		case 57232: //Unused
			c.sendMessage("This option is currently not featured.");
			break;
		}
	}

	public void setupInterface() {
		for (int i = 14791; i < 14797; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("@dre@Leather", 14777);
		c.getPA().sendFrame126("@dre@Hard Leather", 14778);
		c.getPA().sendFrame126("@dre@Green D'hide", 14779);
		c.getPA().sendFrame126("@dre@Blue D'hide", 14780);
		c.getPA().sendFrame126("@dre@Red D'hide", 14781);
		c.getPA().sendFrame126("@dre@Black D'hide", 14782);
		c.getPA().sendFrame126("@dre@1 Coin", 14785);
		c.getPA().sendFrame126("@dre@3 Coins", 14786);
		c.getPA().sendFrame126("@dre@20 Coins", 14787);
		c.getPA().sendFrame126("@dre@20 Coins", 14788);
		c.getPA().sendFrame126("@dre@20 Coins", 14789);
		c.getPA().sendFrame126("@dre@20 Coins", 14790);

		c.getPA().sendFrame246(14769,  150, 1741);
		c.getPA().sendFrame246(14770,  150, 1743);
		c.getPA().sendFrame246(14771,  150, 1745);
		c.getPA().sendFrame246(14772,  150, 1751);
		c.getPA().sendFrame246(14773,  150, 1749);
		c.getPA().sendFrame246(14774,  150, 1747);
		c.getPA().showInterface(14670);
		c.tanning = true;
	}

}