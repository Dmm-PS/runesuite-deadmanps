package wind.model.players.packets.commands.impl;

import wind.Config;
import wind.Punishments;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.CommandBuilder;

public class SupportCommands extends CommandBuilder {

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
						} catch (Exception e) {
							c.sendMessage("Player Must Be Offline.");
						}
					}
				},
				
			/*	new PlayerCommand("teletome") {
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
				}, */
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
			return Rights.DEVELOPER;
		}

		@Override
		public String[] key() {
			return keys;
		}
	}
	}
		