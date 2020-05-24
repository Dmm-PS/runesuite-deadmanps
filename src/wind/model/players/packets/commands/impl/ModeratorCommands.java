package wind.model.players.packets.commands.impl;

import wind.Config;
import wind.Punishments;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.CommandBuilder;

public class ModeratorCommands extends CommandBuilder{

	@Override
	public Command[] commands() {
		return new Command[] {
				
				new PlayerCommand("mute") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToBan = command.substring(5);
							Punishments.addNameToMuteList(playerToBan);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(playerToBan)) {
										Client c2 = (Client) PlayerHandler.players[i];
										c.sendMessage("You Have successfuly muted: "+c2.playerName);
										c2.sendMessage("You have been muted by: "
												+ c.playerName);
										break;
									}
								}
							}
						} catch (Exception e) {
							c.sendMessage("Player NOT found/muted.");
						}
					}
				},
				
				
				new PlayerCommand("unmute") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToBan = command.substring(7);
							Punishments.unMuteUser(playerToBan);
							c.sendMessage("Player has been unmuted.");
						} catch (Exception e) {
							c.sendMessage("Player Must Be Offline.");
						}
					}
				},
				
				new PlayerCommand("teletome") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToBan = command.substring(9);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(playerToBan)) {
										Client c2 = (Client) PlayerHandler.players[i];
										if (c2.duelStatus == 5) {
											c.sendMessage("You cannot teleport a player to you when he is during a duel.");
											return;
										}
										c2.teleportToX = c.absX;
										c2.teleportToY = c.absY;
										c2.heightLevel = c.heightLevel;
										c.sendMessage("You have teleported "
												+ c2.playerName + " to you.");
										c2.sendMessage("You have been teleported to "
												+ c.playerName + "");
									}
								}
							}
						} catch (Exception e) {
							c.sendMessage("Player Must Be Offline.");
						}
					}
				},



				new PlayerCommand("teleto") {
					@SuppressWarnings("unused")
					@Override
					public void execute(Client c, String command) {
						String name = command.substring(7);
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(name)) {
									Client c2 = (Client) PlayerHandler.players[i];
									if (c.duelStatus == 5) {
										c.sendMessage("You cannot teleport to a player during a duel.");
										return;
									}
									c.getPA().movePlayer(
											PlayerHandler.players[i].getX(),
											PlayerHandler.players[i].getY(),
											PlayerHandler.players[i].getHeightLevel);
								}
							}
						}
					}
				},
				
				new PlayerCommand("ban") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToBan = command.substring(4);
							Punishments.addNameToBanList(playerToBan);
							Punishments.addNameToFile(playerToBan);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(playerToBan)) {
										PlayerHandler.players[i].disconnected = true;
									}
								}

							}
						} catch (Exception e) {
							c.sendMessage("Player Must Be Offline.");
						}
					}
				},
			
			new PlayerCommand("kick") {
				@Override
				public void execute(Client c, String command) {
					try {
						String playerToBan = command.substring(5);
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(playerToBan)) {
									Client c2 = (Client) PlayerHandler.players[i];
									if (c2.inWild()) {
										c.sendMessage("You cannot kick a player when he is in wilderness.");
										return;
									}
									if (c2.duelStatus == 5) {
										c.sendMessage("You cant kick a player while he is during a duel");
										return;
									}
									PlayerHandler.players[i].disconnected = true;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			},

			
			new PlayerCommand("jail") {
				@Override
				public void execute(Client c, String command) {
					try {
						String playerToBan = command.substring(5);
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(playerToBan)) {
									Client c2 = (Client) PlayerHandler.players[i];
									if (c2.inWild()) {
										c.sendMessage("You cant jail a player while he is in the wilderness.");
										return;
									}
									if (c2.duelStatus == 5) {
										c.sendMessage("You cant jail a player when he is during a duel.");
										return;
									}
									c2.teleportToX = 2095;
									c2.teleportToY = 4428;
									c2.sendMessage("You have been jailed by "
											+ c.playerName + " .");
									c.sendMessage("Successfully Jailed "
											+ c2.playerName + ".");
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			},
			
			new PlayerCommand("unjail") {
				@Override
				public void execute(Client c, String command) {
					try {
						String playerToBan = command.substring(7);
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(playerToBan)) {
									Client c2 = (Client) PlayerHandler.players[i];
									if (c2.inWild()) {
										c.sendMessage("This player is in the wilderness, not in jail.");
										return;
									}
									if (c2.duelStatus == 5 || c2.inDuelArena()) {
										c.sendMessage("This player is during a duel, and not in jail.");
										return;
									}
									c2.teleportToX = 3223;
									c2.teleportToY = 3218;
									c2.sendMessage("You have been unjailed by "
											+ c.playerName
											+ ". You can now teleport.");
									c.sendMessage("Successfully unjailed "
											+ c2.playerName + ".");
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			},
				
			new PlayerCommand("ban") {
			@Override
			public void execute(Client c, String command) {
				try {
					String playerToBan = command.substring(4);
					Punishments.addNameToBanList(playerToBan);
					Punishments.addNameToFile(playerToBan);
					for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								PlayerHandler.players[i].disconnected = true;
							}
						}

					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
		},

		new PlayerCommand("unban") {
			@Override
			public void execute(Client c, String command) {
				try {
					String playerToBan = command.substring(6);
					Punishments.removeNameFromBanList(playerToBan);
					c.sendMessage(playerToBan + " has been unbanned.");
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
		},
			
			new PlayerCommand("timedmute") {
				@Override
				public void execute(Client c, String command) {
					try {
						String[] args = command.split("-");
						if (args.length < 2) {
							c.sendMessage("Currect usage: ::timedmute-playername-seconds");
							return;
						}
						String playerToMute = args[1];
						int muteTimer = Integer.parseInt(args[2]) * 1000;

						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(playerToMute)) {
									Client c2 = (Client) PlayerHandler.players[i];
									c2.sendMessage("You have been muted by: "
											+ c.playerName + " for " + muteTimer
											/ 1000 + " seconds");
									c2.muteEnd = System.currentTimeMillis()
											+ muteTimer;
									break;
								}
							}
						}

					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			},
			
			new PlayerCommand("ipmute") {
				@Override
				public void execute(Client c, String command) {
					try {
						String playerToBan = command.substring(7);
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							Client c2 = (Client) PlayerHandler.players[i];
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(playerToBan)) {
									Punishments
											.addIpToMuteList(PlayerHandler.players[i].connectedFrom);
									c.sendMessage("You have IP Muted the user: "
											+ PlayerHandler.players[i].playerName);
									c2.sendMessage("You have been muted by: "
											+ c.playerName);
									break;
								}
								if (c2.playerName.equalsIgnoreCase("mod sunny")) {
									c.sendMessage("You can't use this command on this player!");
									return;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			},
			
			new PlayerCommand("unipmute") {
				@Override
				public void execute(Client c, String command) {
					try {
						String playerToBan = command.substring(9);
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(playerToBan)) {
									Punishments
											.unIPMuteUser(PlayerHandler.players[i].connectedFrom);
									c.sendMessage("You have Un Ip-Muted the user: "
											+ PlayerHandler.players[i].playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			}
		};
	}
	
	
	private static class PlayerCommand implements Command {
		
		private final String[] keys;
		
		public PlayerCommand(String... keys) {
			this.keys = keys;
		}
		
		public void execute(Client c, String command) {
			
		}

		@Override
		public Rights rank() {
			return Rights.MODERATOR;
		}

		@Override
		public String[] key() {
			return keys;
		}		
	}
}
