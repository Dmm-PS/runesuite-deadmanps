package wind.model.players.content;

import wind.model.players.Client;

public class ExperienceLamp {

	final static int MULTIPLIER = 25;
	
	final static String[] name = 
		{"Attack", "Defence", "Strength", "Hitpoints", "Ranged",
		 "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching",
		 "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
		 "Herblore", "Agility", "Thieving", "Slayer", "Farming",
		 "Runecrafting"};
	
	public static void buttonClick(Client c, int button) {
		if(c.getItems().playerHasItem(2528)) {
			if(button != 20) {
				c.getPA().setConfig(261, button);
				c.getPA().sendFrame126("You have chosen: "+getSkillName(getSkillId(button - 1))+".",2810);
				//c.getPA().sendFrame126((c.playerLevel[getSkillId(button - 1)] * 10) * MULTIPLIER+" - "+getSkillName(getSkillId(button - 1))+" Experience",2810);
				c.selectedSkill  = button - 1;
			} else {
				if(c.selectedSkill != 1) {
					c.getItems().deleteItem(2528,1);
					c.getPA().closeAllWindows();
					c.getPA().cancelTeleportTask();
					showItem(c, 2528, "You recieve "+(c.playerLevel[c.selectedSkill] * 10) * MULTIPLIER+" "+getSkillName(getSkillId(c.selectedSkill))+" experience.");
					c.getPA().addSkillXP((c.playerLevel[c.selectedSkill] * 10) * MULTIPLIER, getSkillId(c.selectedSkill));
					c.selectedSkill = -1;
				}
			}
		} else {
			c.getPA().closeAllWindows();
			c.getPA().cancelTeleportTask();
		}
	}
	
	public static void showInterface(Client c) {
		c.selectedSkill = -1;
		c.getPA().setConfig(261, 0);
		c.getPA().showInterface(2808);
		c.getPA().sendFrame126("Choose the stats you wish to protect!",2810);
	}
	
	private static int getSkillId(int button) {
		switch(button) {
		case 0://Attack
			return 0;
		case 1://Strength
			return 2;
		case 2://Range
			return 4;
		case 3://Mage
			return 6;
		case 4://Def
			return 1;
		case 5://HP
			return 3;
		case 6://Prayer
			return 5;
		case 7://Agility
			return 16;
		case 8://herblore
			return 15;
		case 9://Thieving
			return 17;
		case 10://Crafting
			return 12;
		case 11://RC
			return 20;
		case 12://SLAY
			return 14;
		case 13://FARM
			return 13;
		case 14://mining
			return 10;
		case 15://smith
			return 7;
		case 16://fish
			return 11;
		case 17://Cook
			return 8;
		case 18://Fm
			return 9;
		case 19://WC
			return 18;
		case 20://Fletch
			return 19;
		}
		return -1;
	}
	
	public static String getSkillName(int i) {
		return name[i];
	}
	
	public static void showItem(Client c, int item, String s1) {
		c.getPA().sendFrame246(11860, 150, item);
		c.getPA().sendFrame126(s1, 11861);
		c.getPA().sendFrame164(11859);
		c.nextChat = 0;
	}
	
}