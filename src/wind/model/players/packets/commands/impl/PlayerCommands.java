package wind.model.players.packets.commands.impl;

import wind.Donation;
import wind.Punishments;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.CommandBuilder;
import wind.util.Misc;

public class PlayerCommands extends CommandBuilder {

	@Override
	public Command[] commands() {
		return new Command[] {
				new PlayerCommand("players") {				
					@Override
					public void execute(Client c, String command) {
						int amount = PlayerHandler.getPlayerCount();
						c.sendMessage(amount > 1 ? "There are currently " + amount  + " players online." : "You are the only one online!");
					}
				},
				new PlayerCommand("kd") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage("Kills: @red@" + c.KC + " @bla@Deaths: @red@" + c.DC + "");
					}
				},
				new PlayerCommand("perks") {
					@Override
					public void execute(Client c, String command) {
						c.getPA().sendFrame126("http://deadmanps.net/forums/index.php/topic/224-donation-info/", 12000);
					}
				},
				new PlayerCommand("store") {
					@Override
					public void execute(Client c, String command) {
						c.getPA().sendFrame126("http://deadmanps.net/store/index.php", 12000);
					}
				},
				new PlayerCommand("donate") {
					@Override
					public void execute(Client c, String command) {
						c.getPA().sendFrame126("http://deadmanps.net/store/index.php", 12000);
					}
				},
				new PlayerCommand("forums") {
					@Override
					public void execute(Client c, String command) {
						c.getPA().sendFrame126("http://deadmanps.net/forums/", 12000);
					}
				},
				new PlayerCommand("kdr") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage("Kills: @red@" + c.KC + " @bla@Deaths: @red@" + c.DC + "");
					}
				},
				new PlayerCommand("commands") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage("@red@Command List:");
						c.sendMessage("::yell ::players ::changepassword ::protect ::protecting ::forums");
					}
				},
				new PlayerCommand("forums") {
					@Override
					public void execute(Client c, String command) {
						c.getPA().sendFrame126("http://deadmanps.net/forums/", 12000);
					}
				},
				new PlayerCommand("protect") {
					@Override
					public void execute(Client c, String command) {
					if (!(c.inSafeZone())) {
						c.sendMessage("@red@You may only protect skills while in a safe zone.");
						return;
					}
					c.getPA().showInterface(2808);
					c.sendMessage("@red@Click the skill you'd like to protect!");
					c.getPA().sendFrame126("Choose the stats you wish to protect!",2810);
					}
				},
				new PlayerCommand("check") {
					@Override
					public void execute(Client c, String command) {
					//	c.sendMessage("Am I working?");
						new Thread(new Donation(c)).start();
					//	c.sendMessage("Yeet?");
					}
				},
				new PlayerCommand("claim") {
					@Override
					public void execute(Client c, String command) {
						//c.sendMessage("Am I working?");
						new Thread(new Donation(c)).start();
					//	c.sendMessage("Yeet?");
					}
				},
				new PlayerCommand("donated") {
					@Override
					public void execute(Client c, String command) {
					//	c.sendMessage("Am I working?");
						new Thread(new Donation(c)).start();
					//	c.sendMessage("Yeet?");
					}
				},
				new PlayerCommand("protecting") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage("You are currently protecting " + c.combatProtected1() + ", " + c.combatProtected2() + ", "
								+ c.skillProtected1() + ", " + c.skillProtected2() + " and " + c.skillProtected3() + ".");
					}
				},

				new PlayerCommand("yell") {
					@Override
					public void execute(Client c, String command) {
						//String rank = "";
						String user = Misc.capitalize(c.playerName) + ": @bla@";
						String Message = command.substring(4);

						if (c.getRights().equal(Rights.PLAYER)) {
							c.sendMessage("Donators are only allowed to yell");
							return;
						}
						if (Punishments.isMuted(c)) {
							c.sendMessage("You are muted for breaking a rule.");
							return;
						}

						else
							switch (c.getRights()) {

							case DONATOR:
								c.getwM().globalMessage("@red@[Donator] @bla@" + user + Message);		
								break;
								
							case SUPER_DONATOR:
								c.getwM().globalMessage("@red@[Super] @bla@" + user + Message);		
								break;
							
							case EXTREME_DONATOR:
								c.getwM().globalMessage("@red@[Extreme] @bla@" + user + Message);		
								break;
								
							case GOD_OF_ALL_DONATORS:
								c.getwM().globalMessage("@red@[Legendary] @bla@" + user + Message);		
								break;

							case MODERATOR:
								c.getwM().globalMessage("@red@[Moderator] @bla@" + user + Message);		
								break;

							case ADMINISTRATOR: 
								c.getwM().globalMessage("@red@[Admin] @bla@" + user + Message);		
								break;

							case DEVELOPER:
								c.getwM().globalMessage("@red@[Developer] @bla@" + user + Message);	
								break;
								
							case OWNER:
								c.getwM().globalMessage("@red@[Owner] @bla@" + user + Message);	
								break;
								
							case SUPPORT:
								c.getwM().globalMessage("@red@[Support] @bla@" + user + Message);	
								break;

							default:						
								System.out.println("Unhandled Yell Type: " + user + Message);
								break;

							}	
					}

				},
				

				new PlayerCommand("/") {				
					@Override
					public void execute(Client c, String command) {					
						if (Punishments.isMuted(c)) {
							c.sendMessage("You are muted for breaking a rule.");
							return;
						}
						if (c.clan != null) {
							String message = command.substring(1);
							c.clan.sendChat(c, Misc.ucFirst(message));
						} else {
							c.sendMessage("You can only do this in a clan chat..");
						}
					}

				},



				new PlayerCommand("changepassword") {
					@Override
					public void execute(Client c, String command) {
						String password = Misc.getFilteredInput(command);
						if (command.length() > 15 && password.matches("[A-Za-z0-9]+")) {
							c.playerPass = command.substring(15);
							c.sendMessage("Your password is now: " + c.playerPass);
						} else {
							c.sendMessage("use as ::changepassword yourpasswordhere");
						}
					}
				},

	

		};
				}
		};
	


	class PlayerCommand implements Command {

		private final String[] keys;

		public PlayerCommand(String... keys) {
			this.keys = keys;
		}

		public void execute(Client c, String command) {

		}

		@Override
		public Rights rank() {
			return Rights.PLAYER;
		}

		@Override
		public String[] key() {
			return keys;
		}

	}


