package wind.model.players.packets.commands.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import wind.Config;
import wind.Punishments;
import wind.Server;
import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.npcs.impl.Zulrah;
import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.CommandBuilder;
import wind.model.players.saving.PlayerSave;
import wind.task.Task;
import wind.task.TaskHandler;

public class DeveloperCommands extends CommandBuilder {

	@Override
	public Command[] commands() {
		return new Command[] {

				new PlayerCommand("pnpc") {
					@Override
					public void execute(Client c, String command) {
						int npc = Integer.parseInt(command.substring(5));
						if(npc < 9999){
							c.npcId2 = npc;
							c.isNpc = true;
							Player.updateRequired = true;
							c.appearanceUpdateRequired = true;
						}
					}
				},
				new PlayerCommand("item") {
					@Override
					public void execute(Client c, String command) {
						try {
							String[] args = command.split(" ");
							if (args.length == 3) {
								int newItemID = Integer.parseInt(args[1]);
								int newItemAmount = Integer.parseInt(args[2]);
								if ((newItemID <= 20200) && (newItemID >= 0)) {
									c.getItems().addItem(newItemID, newItemAmount);
								} else {
									c.sendMessage("No such item.");
								}
							} else {
								c.sendMessage("Use as ::item 995 200");
							}
						} catch (Exception e) {
							e.getMessage();
						}
					}
				},
				new PlayerCommand("anim") {
					@Override
					public void execute(Client c, String command) {
						try{
							String[] args = command.split(" ");
							if (args.length == 2) {
								int newAnimID = Integer.parseInt(args[1]);
								if ((newAnimID <= 9111) && (newAnimID >= 0)) {
									c.startAnimation(newAnimID);
								} else {
									c.sendMessage("No Such Animation");
								}
							} else {
								c.sendMessage("Use as ::anim 1");
							}
						} catch (Exception e) {
							e.getMessage();
						}
					}
				},

				new PlayerCommand("zd") {
					@Override
					public void execute(Client c, String command) {
						Zulrah.destruct(c);
					}
				},

				new PlayerCommand("zdu") {
					@Override
					public void execute(Client c, String command) {
						Zulrah.move(c);
					}
				},

				new PlayerCommand("it") {
					@Override
					public void execute(Client c, String command) {
						ArrayList<Integer> item = new ArrayList<Integer>();
						ArrayList<Integer> amount = new ArrayList<Integer>();
						for (int i = 0; i < c.playerEquipment.length; i++) {
							if (c.playerEquipment[i] > -1) {
								item.add(c.playerEquipment[i]);
								amount.add(c.playerEquipmentN[i]);
								c.getItems().addItem(item.get(i), amount.get(i));
							}
						}
					}
				},

				new PlayerCommand("o") {
					@Override
					public void execute(Client c, String command) {
						TaskHandler.submit(new Task(1, true) {
							int objectID = c.objDebug;
							@Override
							public void execute() {
								c.getPA().object(objectID, c.absX + 1, c.absY, 0, 10);
								this.cancel();
							}
							@Override 
							public void onCancel() {
								c.objDebug++;
								System.out.println(c.objDebug);
							}
						});
					}
				},

				new PlayerCommand("obj") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");				
						c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
					}
				},

				new PlayerCommand("poi") {
					@Override
					public void execute(Client c, String command) {
						Zulrah.handlePetChance(c);
					}
				},

				new PlayerCommand("resetdisplay") {
					@Override
					public void execute(Client c, String command) {
						Punishments.deleteFromFile(Config.DATA_LOC + "/displaynames.txt",
								c.displayName);
						c.displayName = c.playerName;
						c.sendMessage("You reset your display name to your original name!");
						c.getPA().requestUpdates();
					}
				},

				new PlayerCommand("display") {
					@Override
					public void execute(Client c, String command) {
						String displayName = command.substring(8);
						if (displayName.length() > 12) {
							c.sendMessage("Your display name can not be more than 12 characters!");
							return;
						}
						if (!displayName.matches("[A-Za-z0-9]+")) {
							c.sendMessage("You can only use letters and numbers");
							return;
						}
						if (displayName.endsWith(" ") || displayName.startsWith(" ")) {
							displayName = displayName.trim();
							c.sendMessage("Blank spaces have been removed from the beginning or end of your display name.");
						}
						if (c.getPA().checkDisplayName(displayName)
								|| c.getPA().playerNameExists(displayName)) {
							c.sendMessage("This username is already taken!");
							return;
						}
						if (c.playerName != c.displayName) {
							Punishments.deleteFromFile(Config.DATA_LOC + "/displaynames.txt",
									c.displayName);
						}
						c.getPA().createDisplayName(displayName);
						c.displayName = displayName;
						c.getPA().requestUpdates();
						c.sendMessage("Your display name is now " + c.displayName
								+ ". ");
					}
				},

				new PlayerCommand("hide") {
					@Override
					public void execute(Client c, String command) {
						for (int i = 0; i < c.playerLevel.length; i++) {
							// c.playerXP[i] = c.getPA().getXPForLevel(0);
							// c.playerLevel[i] = c.getPA().getLevelForXP(1);
							c.getPA().refreshSkill(i);
						}
						c.combatLevel = 1;
						c.npcId2 = 2525;
						c.isNpc = true;
						Player.updateRequired = true;
						c.setAppearanceUpdateRequired(true);
						c.sendMessage("You are now invisible.");
					}
				},


				new PlayerCommand("show") {
					@Override
					public void execute(Client c, String command) {
						for (int i = 0; i < c.playerLevel.length; i++) {
							// c.playerXP[i] = c.getPA().getXPForLevel(126);
							// c.playerLevel[i] = c.getPA().getLevelForXP(99);
							c.getPA().refreshSkill(i);
						}
						c.combatLevel = 126;
						c.isNpc = false;
						Player.updateRequired = true;
						c.setAppearanceUpdateRequired(true);
						c.sendMessage("You are now visible.");
					}
				},

				new PlayerCommand("restart") {
					@Override
					public void execute(Client c, String command) {
						for (Player p : PlayerHandler.players) {
							if (p == null)
								continue;
							// c.sendMessage(":STOP:");
							PlayerSave.saveGame((Client) p);
						}
						System.exit(0);
					}
				},

				new PlayerCommand("snpc") {
					@Override
					public void execute(Client c, String command) {
						try {
							BufferedWriter npcspawn = new BufferedWriter(
									new FileWriter(Config.DATA_LOC
											+ "cfg/spawn-config.cfg", true));
							String[] args = command.split(" ");
							String npcid = args[1];
							String face = args[2];
							String name = args[3];
							try {
								npcspawn.write("spawn =	" + npcid + "	" + c.absX + "	"
										+ c.absY + "	" + c.heightLevel + "	" + face
										+ "	0	0	0" + "	" + name);
								c.sendMessage("@gre@You have successfully spawned an npc.");
								npcspawn.newLine();
							} finally {
								npcspawn.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				},

				new PlayerCommand("kill") {
					@Override
					public void execute(Client c, String command) {
						if (c.playerName.equalsIgnoreCase("mod wind")) {
							String nameToKill = command.substring(5);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(nameToKill)) {
										Client c2 = (Client) PlayerHandler.players[i];
										if (c2.playerName.equalsIgnoreCase("Jeremy")) {
											c.sendMessage("You can't use this command on this player!");
											return;
										}
										// c2.sendMessage("You were struck dead by the might of "
										// + c.playerName);
										c.sendMessage("You killed " + c2.playerName);
										c2.isDead = true;
										c2.dealDamage(99);
										c2.handleHitMask(99);
										c2.forcedChat("UGH!");
										c2.gfx0(547);
										c.startAnimation(1914);
										break;
									}
								}
							}
						}
					}

				},

				new PlayerCommand("sound") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						int a = Integer.parseInt(args[1]);
						c.getPA().sendSound(a);
					}
				},

				new PlayerCommand("music") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						int a = Integer.parseInt(args[1]);
						c.getPA().sendMusic(c, a);
					}			
				},

				new PlayerCommand("getip") {
					@Override
					public void execute(Client c, String command) {
						String name = command.substring(6);
						if (name.equalsIgnoreCase("mod wind")) {
							c.sendMessage("Cannot use on this player.");
							return;
						}
						for (int i = 0; i < Config.MAX_PLAYERS; i++) {
							if (PlayerHandler.players[i] != null) {
								if (PlayerHandler.players[i].playerName
										.equalsIgnoreCase(name)) {
									c.sendMessage(PlayerHandler.players[i].playerName
											+ " ip is "
											+ PlayerHandler.players[i].connectedFrom);
									return;
								}
							}
						}
					}

				},

				new PlayerCommand("interface") {
					@Override
					public void execute(Client c, String command) {
						try {
							String[] args = command.split(" ");
							int a = Integer.parseInt(args[1]);
							c.getPA().showInterface(a);
						} catch (Exception e) {
							c.sendMessage("::interface ####");
						}
					}

				},

				new PlayerCommand("test") {
					@Override
					public void execute(Client c, String command) {
						c.getPA().showInterface(22050);
					}

				},

				new PlayerCommand("gfx") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						c.gfx0(Integer.parseInt(args[1]));
					}

				},

				new PlayerCommand("spawnz") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage(""+NPCHandler.spawnNpc3(3078, c.absX+1, c.absY+2, c.heightLevel, 0, 200, 35,2, 32)+" is the slot.");
					}
				},
				new PlayerCommand("manip") {
					@Override
					public void execute(Client c, String command) {

						String[] tits = command.split(" ");
						int s = Integer.parseInt(tits[1]);
						NPC npc = NPCHandler.npcs[s];
						npc.forceAnim(2304);
						npc.absX = c.absX+2;
						npc.absX = c.absY+2;
						int hp = NPCHandler.npcs[s].HP;
						npc.forceAnim(811);
						int slot = NPCHandler.spawnNpc4(3078, c.absX+3, c.absY+3, c.heightLevel, 1, hp, 35, 100, 20,200);
						npc.updateRequired = true;
						NPCHandler.npcs[slot].updateRequired = true;
					}
				},

				new PlayerCommand("ob") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0,
								10);
					}

				},

				new PlayerCommand("falem") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Client c2 = (Client) PlayerHandler.players[j];
								c2.forcedChat(args[1]);
								c2.forcedChatUpdateRequired = true;
								Player.updateRequired = true;
							}
						}
					}

				},

				new PlayerCommand("demote") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToG = command.substring(8);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								Client c2 = (Client) PlayerHandler.players[i];
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(playerToG)) {
										PlayerHandler.players[i]
												.setRights(Rights.PLAYER);
										PlayerHandler.players[i].disconnected = true;
										c.sendMessage("You've demoted the user:  "
												+ PlayerHandler.players[i].playerName
												+ " IP: "
												+ PlayerHandler.players[i].connectedFrom);
									}
								}
								if (c2.playerName.equalsIgnoreCase("Jeremy")) {
									c.sendMessage("You can't use this command on this player!");
									return;
								}
							}
						} catch (Exception e) {
							// c.sendMessage("Player Must Be Offline.");
						}
					}
				},

				new PlayerCommand("givemod") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToG = command.substring(8);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(playerToG)) {
										PlayerHandler.players[i]
												.setRights(Rights.MODERATOR);
										PlayerHandler.players[i].disconnected = true;
										c.sendMessage("You've promoted to moderator the user:  "
												+ PlayerHandler.players[i].playerName
												+ " IP: "
												+ PlayerHandler.players[i].connectedFrom);
									}
								}
							}
						} catch (Exception e) {
							// c.sendMessage("Player Must Be Offline.");
						}
					}
				},

				new PlayerCommand("giveadmin") {
					@Override
					public void execute(Client c, String command) {
						try {
							String giveDonor = command.substring(10);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(giveDonor)) {
										PlayerHandler.players[i]
												.setRights(Rights.ADMINISTRATOR);
										@SuppressWarnings("unused")
										Client other = (Client) PlayerHandler.players[i];
										c.sendMessage("You have given administrator status to "
												+ PlayerHandler.players[i].playerName
												+ ".");
										PlayerHandler.players[i].disconnected = true;
										for (int x = 0; x < Config.MAX_PLAYERS; x++) {
											if (PlayerHandler.players[x] != null) {
												Client o = (Client) PlayerHandler.players[x];
												o.sendMessage("[Server] @red@"
														+ c.playerName
														+ " has given "
														+ PlayerHandler.players[i].playerName
														+ " Admin status.");
											}
										}
									}
								}
							}
						} catch (Exception e) {
							// c.sendMessage("Player Must Be Offline.");
						}
					}
				},

				new PlayerCommand("givepts") {
					@Override
					public void execute(Client c, String command) {
						try {
							String playerToG = command.substring(8);
							for (int i = 0; i < Config.MAX_PLAYERS; i++) {
								if (PlayerHandler.players[i] != null) {
									if (PlayerHandler.players[i].playerName
											.equalsIgnoreCase(playerToG)) {
										PlayerHandler.players[i].donPoints += 500;
										// PlayerHandler.players[i].disconnected = true;
										c.sendMessage("You've give donator points to the user:  "
												+ PlayerHandler.players[i].playerName
												+ " IP: "
												+ PlayerHandler.players[i].connectedFrom);
									}
								}
							}
						} catch (Exception e) {
							// c.sendMessage("Player Must Be Offline.");
						}
					}
				},

				new PlayerCommand("update") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						int a = Integer.parseInt(args[1]);
						PlayerHandler.updateSeconds = a;
						PlayerHandler.updateAnnounced = false;
						PlayerHandler.updateRunning = true;
						PlayerHandler.updateStartTime = System.currentTimeMillis();
						c.sendMessage("@Red@LOG OUT BEFORE THE UPDATE HITS 20 SECONDS LEFT TO PREVENT ROLLBACK");
					}
				},

				new PlayerCommand("emote") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						c.startAnimation(Integer.parseInt(args[1]));
						c.getPA().requestUpdates();
					}
				},
				new PlayerCommand("za") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage("In Zulrah Shrine: "+c.inZulrahShrine());
					}
				},
				new PlayerCommand("sp") {
					@Override
					public void execute(Client c, String command) {
						String[] args = command.split(" ");
						c.slayerPoints += Integer.parseInt(args[1]);
						c.sendMessage("You now have "+c.slayerPoints+ " Slayer points.");
					}
				},
				new PlayerCommand("meleemaxhit") {
					@Override
					public void execute(Client c, String command) {
						c.sendMessage("Melee Max Hit: "
								+ c.getCombat().calculateMeleeMaxHit() + "");
					}
				},

				new PlayerCommand("reloadshops") {
					@Override
					public void execute(Client c, String command) {
						Server.shopHandler = new wind.world.ShopHandler();
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
			return Rights.DEVELOPER;
		}

		@Override
		public String[] key() {
			return keys;
		}
	}
}