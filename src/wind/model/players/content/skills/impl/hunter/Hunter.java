package wind.model.players.content.skills.impl.hunter;

import java.util.HashMap;

import wind.model.items.Item;
import wind.model.players.Client;
import wind.util.Misc;

public class Hunter {
	
	/**
	 * @author Imsuperman05
	 * @version 1.5;
	 * 
	 * I give permission for RiiPiiNFtW to release this
	 * on rune-server.org.
	 * 
	 * I have no use for it anymore, and I want to
	 * give back to the rune-server community that
	 * I have treated horribly.
	 * 
	 * We have some room to improve.
	 * C&P suggestions from forums onto here.
	 * 
	 */

	final Client c;

	public Hunter(final Client Client) {
		c = Client;
	}
	
	/**
	 * Constant variables that can be changed at any time
	 * instead of changing every single calling method
	 * handling it.
	 */
	int HunterAnim = 6999;
	int netNeeded = 10010;
	
	static enum Implings {
		/**
		 * Baby Impling.
		 */
		BABY( "Baby Impling", 11238, 1500, 1, 1635 ),
		/**
		 * Young Impling.
		 */
		YOUNG( "Young Impling", 11240, 3500, 17, 1636 ),
		/**
		 * Gourmet Impling.
		 */
		GOURMET( "Gourmet Impling", 11244, 5000, 34, 1637),
		/**
		 * Earth Impling.
		 */
		EARTH( "Earth Impling", 11244, 5000, 34, 1638 ),
		/**
		 * Essence Impling.
		 */
		ESSENCE( "Essence Impling", 11246, 6000, 40, 1639 ),
		/**
		 * Electic Impling.
		 */
		ELECTIC( "Electic Impling", 11248, 8000, 50, 1640),
		/**
		 * Nature Impling.
		 */
		NATURE( "Nature Impling", 11250, 10000, 58,1641 ),
		/**
		 * Magpie Impling.
		 */
		MAGPIE( "Magpie Impling", 11252, 12500, 65, 1642 ),
		/**
		 * Ninja Impling.
		 */
		NINJA( "Ninja Impling", 11254, 14000, 74, 1643 );
		
		/**
		 * Variables.
		 */
		String name;
		int JarAdded, XPAdded, LevelNeed, Npc;
		
		/**
		 * Creating the HashMap.
		 */
		static HashMap<Integer, Implings> hunted = new HashMap<Integer, Implings>();
		
		/**
		 * Populates the map.
		 */
		static {
			for(Implings i : Implings.values())
				Implings.hunted.put(i.JarAdded, i);
		}

		/**
		 * Creating the Impling.
		 * @param name
		 * @param JarAdded
		 * @param XPAdded
		 * @param LevelNeed
		 * @param Npc
		 */
		Implings(String name, int JarAdded, int XPAdded, int LevelNeed, int Npc) {
			this.name = name;
			this.JarAdded = JarAdded;
			this.XPAdded = XPAdded;
			this.LevelNeed = LevelNeed;
			this.Npc = Npc;
		}
	}

	static enum Otherhunternpcs {
		/**
		 * Black warlock.
		 */
		BLACKWARLOCK( "Black Warlock", 10014, 18000, 85, 5553 ),
		/**
		 * Snowy Knight,
		 */
		SNOWY( "Snowy Knight", 10016, 15000, 75, 5554 ),
		/**
		 * Sapphire Glacialis.
		 */
		SAPPHIRE( "Sapphire Glacialis", 10018, 7500, 45, 5555 ),
		/**
		 * Ruby Harvast.
		 */
		RUBY( "Ruby Harvast", 10020, 5000, 30, 5556);
		
		/**
		 * Variables.
		 */
		String name;
		int JarAdded, XPAdded, LevelNeed, Npc;
		
		/**
		 * Creating the HashMap.
		 */
		static HashMap<Integer, Otherhunternpcs> hunted = new HashMap<Integer, Otherhunternpcs>();
		
		/**
		 * Populates the map.
		 */
		static {
			for(Otherhunternpcs i : Otherhunternpcs.values())
				Otherhunternpcs.hunted.put(i.JarAdded, i);
		}

		/**
		 * Creating the Other hunted npcs.
		 * @param name
		 * @param JarAdded
		 * @param XPAdded
		 * @param LevelNeed
		 * @param Npc
		 */
		Otherhunternpcs(String name, int JarAdded, int XPAdded, int LevelNeed, int Npc) {
			this.name = name;
			this.JarAdded = JarAdded;
			this.XPAdded = XPAdded;
			this.LevelNeed = LevelNeed;
			this.Npc = Npc;
		}
	}

	/**
	 * Boolean to check everything.
	 * @param NpcClicked
	 * @return
	 */
	public boolean hasReqs(int NpcClicked) {
		for(Otherhunternpcs o : Otherhunternpcs.values()) {
		if(NpcClicked == o.Npc) {
			if(c.playerLevel[c.playerHunter] >= o.LevelNeed) {
				if(c.getItems().playerHasItem(netNeeded)) {
					if (c.getEquipment().freeSlots() > 0) {
							return true;
					} else {
						c.sendMessage("You have no space..");
						return false;
					}
				} else {
					c.sendMessage("You need a butterfly net to catch this imp pimp!");
					return false;
				}
			} else {
				c.sendMessage("You need a Hunter level of " + o.LevelNeed + " to catch this imp pimp!");
				return false;
				}
			}
		}
		for(Implings o : Implings.values()) {
			if(NpcClicked == o.Npc) {
				if(c.playerLevel[c.playerHunter] >= o.LevelNeed) {
					if(c.getItems().playerHasItem(netNeeded)) {
						if (c.getEquipment().freeSlots() > 0) {
								return true;
						} else {
							c.sendMessage("You have no space..");
							return false;
						}
					} else {
						c.sendMessage("You need a butterfly net to catch this imp pimp!");
						return false;
					}
				} else {
					c.sendMessage("You need a Hunter level of " + o.LevelNeed + " to catch this imp pimp!");
					return false;
					}
				}
			}
		return false;
	}
	
	/**
	 * Much simpler.
	 * @param NpcClicked
	 */
	public void Checking(int NpcClicked) {
		for(Otherhunternpcs o : Otherhunternpcs.values()) {
				if(NpcClicked == o.Npc) {
						CatchOthers(o.name, o.JarAdded, o.XPAdded, o.LevelNeed);
			}
		}
		for(Implings i : Implings.values()) {
				if(NpcClicked == i.Npc) {
						CatchImps(i.name, i.JarAdded, i.XPAdded, i.LevelNeed);
			}
		}
	}
	/**
	 * Cathing Imps method.
	 * @param ImpName
	 * @param JarAdded
	 * @param XPAdded
	 * @param LevelNeeded
	 */
	
	public void CatchImps(String ImpName, int JarAdded, int XPAdded, int LevelNeeded) {
			if (System.currentTimeMillis() - c.foodDelay >= 1500) {
				int RandomImp = Misc.random(20);
				switch(RandomImp) {
				case 10:
					c.sendMessage("You catch a MEDIUM imp, DOUBLE EXP FOR MY DOG");
					c.startAnimation(HunterAnim);
					c.sendMessage("Your Hunter Level is "+ c.playerLevel[c.playerHunter] +".");
					c.getPA().addSkillXP(XPAdded * 2, 21);
				break;
				case 15:
					c.sendMessage("You catch a HUGE ASS imp, but not big enough!");
					c.startAnimation(HunterAnim);
					c.sendMessage("Your Hunter Level is "+ c.playerLevel[c.playerHunter] +".");
					c.getPA().addSkillXP(XPAdded * 3, 21);
				break;
				case 20:
					c.sendMessage("YOU CATCH THE BIGGEST MTFKING IMP BRO.");
					c.startAnimation(HunterAnim);
					c.sendMessage("Your Hunter Level is "+ c.playerLevel[c.playerHunter] +".");
					c.getPA().addSkillXP(XPAdded * 4, 21);
					break;
				/*case 8:
					c.sendMessage("Oh noes!");
					EvilChicken.spawnEvil(c);
				break;*/
				default:
						c.sendMessage(new StringBuilder().append("You catch a regular ").append(ImpName) .append(", and receive a ") .append(Item.getItemName(JarAdded).toLowerCase()) .append(".").toString());
							c.getPA().addSkillXP(XPAdded, 21);
							c.startAnimation(HunterAnim);
							c.sendMessage("Your Hunter Level is "+ c.playerLevel[c.playerHunter] +".");
							c.getItems().addItem(JarAdded, 1);
						break;
					}
				c.foodDelay = System.currentTimeMillis();
			}
	}
	
	/**
	 * Catching other hunted npcs that aren't implings.
	 * @param ImpName
	 * @param JarAdded
	 * @param XPAdded
	 * @param LevelNeeded
	 */
	public void CatchOthers(String ImpName, int JarAdded, int XPAdded, int LevelNeeded) {
			if (System.currentTimeMillis() - c.foodDelay >= 1500) {
				int RandomImp = Misc.random(20);
				switch(RandomImp) {
				case 10:
					c.sendMessage("You catch a MEDium thing, DOUBLE EXP FOR MY DOG");
					c.startAnimation(HunterAnim);
					c.getPA().addSkillXP(XPAdded * 2, 21);
				break;
				case 15:
					c.sendMessage("You catch a HUGE ASS thing, but not big enough!");
					c.startAnimation(HunterAnim);
					c.getPA().addSkillXP(XPAdded * 3, 21);
				break;
				case 20:
					c.sendMessage("YOU CATCH THE BIGGEST MTFKING THING BRO.");
					c.startAnimation(HunterAnim);
					c.getPA().addSkillXP(XPAdded * 4, 21);
					break;
				/*case 8:
					c.sendMessage("Oh noes!");
					EvilChicken.spawnEvil(c);
				break;*/
				default:
						c.sendMessage(new StringBuilder() .append("You catch a regular ")
								.append(ImpName) .append(", and receive a ") 
								.append(Item.getItemName(JarAdded).toLowerCase()) .append(".").toString());
							c.getPA().addSkillXP(XPAdded, 21);
							c.startAnimation(HunterAnim);
							c.getItems().addItem(JarAdded, 1);
						break;
					}
				c.foodDelay = System.currentTimeMillis();
			}
		}	

	int[] DragonImpRare = {1704};

	public int DragonImpRare() {
		return DragonImpRare[(int) (Math.random() * DragonImpRare.length)];
	}

	int[] DragonImpCommon = {/* item ID's */};

	public int DragonImpCommon() {
		return DragonImpCommon[(int) (Math.random() * DragonImpCommon.length)];
	}

	int[] DragonImpBones = {18830};

	public int DragonImpBones() {
		return DragonImpBones[(int) (Math.random() * DragonImpBones.length)];
	}
	
	/**
	 * Looting Impling Jars
	 */
	
	public void Lootimpjar(Client c, int itemId, int playerId) {
		
		switch(itemId) {		
		case 11238:
			
			break;
		
		case 11256: /*0.01% chance on the first try*/
			if (Misc.random(1000) <= (1 + Misc.random(c.ImpLuck))) {
				c.getItems().addItem(DragonImpRare(), 1);
				c.getItems().deleteItem(11256, 1);
				c.sendMessage("You open the jar and find a <shad=15695415>RARE </col>item");
				c.ImpLuck = 0;
			}else{
				if (Misc.random(250) <= (10 + Misc.random(c.ImpLuck))) {
					c.getItems().addItem(DragonImpCommon(), 1);
					c.getItems().deleteItem(11256, 1);
					c.sendMessage("You open the jar and find a <col=1532693>COMMON </col>item");
					c.ImpLuck -= 2; //takes away 2 luck when a player gets a common item from the jar
				}else{
					c.getItems().addItem(DragonImpBones(), 1);
					c.getItems().deleteItem(11256, 1);
					c.sendMessage("You open the jar and find some bones");//tells the player what item he got
					//sendMessage("impluck : "+ImpLuck+".");  for testing my luck method
					c.ImpLuck += 1;// adds luck after each fail
					}
				}
			break;		
			}
		}
	}