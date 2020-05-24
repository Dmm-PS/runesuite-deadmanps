package wind.model.items.impl;

import wind.model.players.Client;
import wind.util.Misc;

public class MembershipBond {

	final static int TRADEABLE_BOND = 13190;

	final static int UNTRADEABLE_BOND = 13192;

	final static int EXCHANGE_RATE = 150000;

	public static int clickedItem;

	public MembershipBond(Client p) {
		super();
		handle(p);
	}
	public void handle(Client p) {
		switch (clickedItem) {
		case TRADEABLE_BOND:
			p.getPA().sendItemsOnDialogue2("Membership Bond", "You are about to redeem a bond. This will grant", 
					"you @dre@membership@bla@ & @dre@5 membership points.@bla@ Are you sure?", clickedItem, 200);
			p.nextChat = 1009;
			break;
		case UNTRADEABLE_BOND:
			p.getPA().sendItemsOnDialogue2("Membership Bond", "You are about to redeem a bond. This will grant", 
					"you @dre@membership@bla@ & @dre@3 membership points.@bla@ Are you sure?", clickedItem, 200);
			p.nextChat = 1009;
			break;
		}
	}
	public static void openInterface(Client p) {
		p.getPA().sendInterface(6600);
		updateInterface(p);
	}
	public static void redeem(Client p) {
		switch (clickedItem) {
		case TRADEABLE_BOND:
			p.memberPoints += 5;
			break;
		case UNTRADEABLE_BOND:
			p.memberPoints += 3;
			break;
		}
		p.getPA().removeAllWindows();
		p.getItems().deleteItem(clickedItem, 1);
		p.gfx0(332);
		p.startAnimation(866, 5);
		p.isMember = true;
		p.getwM().serverMessage("@dre@"+p.playerName+" has just redeemed a bond! Thank you for donating!");
		p.sendMessage("You now have "+p.memberPoints+" member points. Speak to Twiggy to redeem them for cosmetics.");
		p.saveCharacter = true;
		p.saveFile = true;
	}
	public static void initConvert(Client c) {
		c.getPA().sendItemsOnDialogue2("Convert bond", "You are about to convert your bond to a tradeable state.", "This currently"
				+ " costs 150K gp. Are you sure?", UNTRADEABLE_BOND, 200);
		c.nextChat = 1010;
	}
	public static void convert(Client c) {
		if (!c.getItems().playerHasItem(995, EXCHANGE_RATE)) {
			c.getDH().sendStatement("@dre@You don't have enough money to convert the bond.");
			c.nextChat = 0;
			return;
		}
		if (c.getItems().playerHasItem(995, EXCHANGE_RATE)) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(13192, 1);
			c.getItems().deleteItem(995, EXCHANGE_RATE);
			c.getItems().addItem(13190, 1);
			c.sendMessage("@dre@You've exchanged your untradeable bond for a tradeable one.");
		}
}
	public static void redeemFromInterface(Client p, int bondType) {
		switch (bondType) {
		case 13190:
			p.tradeBond -= 1;
			p.memberPoints += 5;
			break;
		case 13192:
			p.untradeBond -= 1;
			p.memberPoints += 3;
			break;
		}
		p.isMember = true;
		p.getwM().serverMessage("@dre@"+p.playerName+" has just redeemed a bond! Thank you for donating!");
		p.sendMessage("You now have "+p.memberPoints+" member points. Speak to Twiggy to redeem them for cosmetics.");
		p.saveCharacter = true;
		p.saveFile = true;
		updateInterface(p);
	}
	public static void updateInterface(Client c) {
		c.getPA().sendFrame126(""+c.tradeBond, 6603);
		c.getPA().sendFrame126(""+c.untradeBond, 6605);
		c.getPA().sendFrame126("@lre@Aros Bond Program: @whi@\\n\\nThe Aros bond program allows our players \\nto purchase membership"
				+ " status and\\ncontent without spending a dime!\\n\\nThe way this works is our players purchase\\na bond from the website,"
				+ " log in to Aros,\\nand have the ability to redeem it\\nimmediately for points, or they have the\\noption to convert it"
				+ " to a tradeable state\\nto redeem for extra points, or trade\\nto other players. Points can only be used\\nto purchase cosmetic items or non-pay-to\\n-win content.\\n\\n"
				+ "Current conversion rate: "+Misc.parseLargeAmount(EXCHANGE_RATE), 6615);
	}
	public static void convertFromInterface(Client c) {
		if (!c.getItems().playerHasItem(995, EXCHANGE_RATE)) {
			c.getDH().sendStatement("@dre@You don't have enough money to convert the bond.");
			c.nextChat = 0;
			return;
		}
		if (c.untradeBond <= 0) {
			c.sendMessage("@red@You don't have any untradeable bonds to convert.");
			return;
		}
		if (c.getItems().playerHasItem(995, EXCHANGE_RATE)) {
			c.untradeBond -= 1;
			c.getItems().deleteItem(995, EXCHANGE_RATE);
			c.tradeBond += 1;
			c.sendMessage("@dre@You've exchanged your untradeable bond for a tradeable one and has been auto-");
			c.sendMessage("@dre@added to your tradeable bonds in exchange for "+Misc.parseLargeAmount(EXCHANGE_RATE)+" GP.");
		}
		updateInterface(c);
	}
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public static double randomDouble(double min, double max) {
		return (Math.random() * (max - min) + min);
	}
	public final static String parseLargeAmount(int amount) {
		if (amount >= 0 && amount < 10000)
			return String.valueOf(amount);
		if (amount >= 10000 && amount < 10000000)
			return amount / 1000 + "K";
		if (amount >= 10000000 && amount < 999999999)
			return amount / 1000000 + "M";
		if (amount >= 999999999)
			return amount / 1000000000 + "B";
		else
			return "?";
	}
	
	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}
	 */
}
