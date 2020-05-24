package wind.model.players.plugin.impl;

public enum Color {
	RED("@red@"), 
	GREEN("@gre@"),
	GREEN_LIME("@gr1@"), 
	YELLOW("@yel@"), 
	PURPLE("@pur@"), 
	WHITE("@whi@"), 
	BROWN("@br1@"), 
	PINK("@pin@"), 
	CYAN("@cya@"),
	ORANGE("@or1@"),
	ORANGE_2("@or2@"),
	DARK_RED("@dre@"), 
	BLUE("@blu@");

	private String colour;

	private Color(String colour) {
		this.colour = colour;
	}

	public String getColour() {
		return colour;
	}
}