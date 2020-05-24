package wind.model.players.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import shamon.barrows.Barrows;
import shamon.barrows.data.Brother;
import wind.Config;
import wind.model.items.PlayerItem;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.content.music.Music;
import wind.util.Misc;

public class PlayerSave {

	/**
	 * Tells us whether or not the player exists for the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean playerExists(String name) {
		File dir = new File (Config.DATA_LOC + "characters/");
		if (!dir.exists()){
			        dir.mkdir();
		}
		File file = new File(Config.DATA_LOC + "characters/" + name + ".txt");
		return file.exists();
	}

	/**
	 * Tells use whether or not the specified name has the friend added.
	 * 
	 * @param name
	 * @param friend
	 * @return
	 */
	public static boolean isFriend(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a characters friends in the form of a long array.
	 * 
	 * @param name
	 * @return
	 */
	public static long[] getFriends(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean end = false;
		int readMode = 0;
		BufferedReader file = null;
		boolean file1 = false;
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		try {
			file = new BufferedReader(new FileReader(
					Config.DATA_LOC + "characters/" + name + ".txt"));
			file1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (file1) {
			new File(Config.DATA_LOC + "characters/" + name + ".txt");
		} else {
			return null;
		}
		try {
			line = file.readLine();
		} catch (IOException ioexception) {
			return null;
		}
		while (end == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (readMode) {
				case 0:
					if (token.equals("character-friend")) {
						readFriends[Integer.parseInt(token3[0])] = Long
								.parseLong(token3[1]);
						totalFriends++;
					}
					break;
				}
			} else {
				if (line.equals("[FRIENDS]")) {
					readMode = 0;
				} else if (line.equals("[EOF]")) {
					try {
						file.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = file.readLine();
			} catch (IOException ioexception1) {
				end = true;
			}
		}
		try {
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int index = 0; index < totalFriends; index++) {
					friends[index] = readFriends[index];
				}
				return friends;
			}
			file.close();
		} catch (IOException ioexception) {
		}
		return null;
	}

	
	/**
	 * Loading
	 **/
	public static int loadGame(Client p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;
		try {
			characterfile = new BufferedReader(new FileReader(
					Config.DATA_LOC + "characters/" + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}
		if (playerExists(Misc.removeSpaces(playerName))
				&& !playerExists(playerName)) {
			p.disconnected = true;
			return 3;
		}

		if (File1) {
			// new File ("./characters/"+playerName+".txt");
		} else {
			Misc.println(playerName + ": character file not found.");
			p.newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2)
								|| Misc.basicEncrypt(playerPass).equals(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if(token.equals("barrows-broskilled")) {
						int bros= Integer.parseInt(token2);
						p.getBarrows().getNpcController().setKilledBrothers(bros);
					} else if(token.equals("barrows-target")) {
						int brotherID = Integer.parseInt(token2);
						p.getBarrows().getNpcController().setTargetBrother(Brother.lookup(brotherID));
					} else if(token.equals("barrows-looted")) {
						boolean looted = Boolean.parseBoolean(token2);
						p.getBarrows().getChest().setLooted(looted);
					} else if(token.equals("barrows-kc")) {
						int kc = Integer.parseInt(token2);
						p.getBarrows().getNpcController().setKillCount(kc);
					} else  if (token.equals("timePlayed")) {
						p.timePlayed = Integer.parseInt(token2);
					} else if (token.equals("lastLoginDate")) {
						p.lastLoginDate = Integer.parseInt(token2);
					} else if (token.equals("character-height")) {
						p.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210
								: Integer.parseInt(token2));
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424
								: Integer.parseInt(token2));
					} else if (token.equals("character-rights")) {
						p.setRights(Rights.forValue(Integer.parseInt(token2)));
					} else if (token.equals("isMember")) {
						p.isMember = Boolean.parseBoolean(token2);
					} else if (token.equals("memberPoints")) {
						p.memberPoints = Integer.parseInt(token2);
					} else if (token.equals("easyClueCount")) {
						p.easyClueCount = Integer.parseInt(token2);
					} else if (token.equals("mediumClueCount")) {
						p.mediumClueCount = Integer.parseInt(token2);
					} else if (token.equals("hardClueCount")) {
						p.hardClueCount = Integer.parseInt(token2);
					} else if (token.equals("character-title")) {
						p.playerTitle = token2;
					} else if (token.equals("character-title-color")) {
						p.titleColor = Integer.parseInt(token2);
					} else if (token.equals("killed-players")) {
						p.lastKilledPlayers.add(token2);
					} else if (token.equals("connected-from")) {
						p.lastConnectedFrom.add(token2);
					} else if (token.equals("nmzPoints")) {
						p.nmzPoints = Integer.parseInt(token2);
					} else if (token.equals("desert-treasure")) {
						p.desertT = Integer.parseInt(token2);
					} else if (token.equals("dt-kill")) {
						p.lastDtKill = Integer.parseInt(token2);
					} else if (line.startsWith("displayName")) {
						p.displayName = token2;
					} else if (token.equals("darts-loaded")) {
						p.dartsLoaded = Integer.parseInt(token2);
					} else if (token.equals("horror-from-deep")) {
						p.horrorFromDeep = Integer.parseInt(token2);
					} else if (token.equals("rfd-round")) {
						p.rfdRound = Integer.parseInt(token2);
					} else if (token.equals("musicOn")) {
						p.musicOn = Boolean.parseBoolean(token2);
					} else if (token.equals("soundsOn")) {
							p.soundsOn = Boolean.parseBoolean(token2);
					} else if (token.equals("music-unlocked")) {
						for (int j = 0; j < token3.length; j++) {
							Music.unlocked[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("quest-points")) {
						p.questPoints = Integer.parseInt(token2);
					} else if (token.equals("cooks-assistant")) {
						p.cooksA = Integer.parseInt(token2);
					} else if (token.equals("startPack")) {
						p.startPack = Boolean.parseBoolean(token2);
					} else if (token.equals("pinAttempts")) {
						p.pinAttempts = Integer.parseInt(token2);
					} else if (token.equals("lockoutTime")) {
						p.lockoutTime = Integer.parseInt(token2);
					} else if (token.equals("setPin")) {
						p.setPin = Boolean.parseBoolean(token2);
					} else if (token.equals("hasBankpin")) {
						p.hasBankpin = Boolean.parseBoolean(token2);
					} else if (token.equals("pinRegisteredDeleteDay")) {
						p.pinDeleteDateRequested = Integer.parseInt(token2);
					} else if (token.equals("requestPinDelete")) {
						p.requestPinDelete = Boolean.parseBoolean(token2);
					} else if (token.equals("bankPin1")) {
						p.bankPin1 = Integer.parseInt(token2);
					} else if (token.equals("bankPin2")) {
						p.bankPin2 = Integer.parseInt(token2);
					} else if (token.equals("bankPin3")) {
						p.bankPin3 = Integer.parseInt(token2);
					} else if (token.equals("bankPin4")) {
						p.bankPin4 = Integer.parseInt(token2);
					} else if (token.equals("tutorial-progress")) {
						p.tutorial = Integer.parseInt(token2);
					} else if (token.equals("crystal-bow-shots")) {
						p.crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("skull-timer")) {
						p.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("redSkull")) {
						p.redSkull = Boolean.parseBoolean(token2);
					} else if (token.equals("magic-book")) {
						p.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("brother-info")) {
						p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer
								.parseInt(token3[1]);
					} else if (token.equals("special-amount")) {
						p.specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("barrows-killcount")) {
						p.pkPoints = Integer.parseInt(token2);
					} else if (token.equals("Dfs-Charge")) {
						p.dfsCharge = Integer.parseInt(token2);
					} else if (token.equals("pkp")) {
						p.pkp = Integer.parseInt(token2);
					} else if (token.equals("donP")) {
						p.donPoints = Integer.parseInt(token2);
					} else if (token.equals("xpLock")) {
						Config.LOCK_EXPERIENCE = Boolean.parseBoolean(token2);
					} else if (token.equals("petID")) {
						p.petID = Integer.parseInt(token2);
					} else if (token.equals("petSummoned")) {
						p.setPetSummoned(Boolean.parseBoolean(token2));
					} else if (line.startsWith("KC")) {
						p.KC = Integer.parseInt(token2);
					} else if (line.startsWith("DC")) {
						p.DC = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						p.teleBlockDelay = System.currentTimeMillis();
						p.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.pcPoints = Integer.parseInt(token2);
					} else if (line.startsWith("crabsKilled")) {
						p.crabsKilled = Integer.parseInt(token2);
					} else if (line.startsWith("fireslit")) {
						p.fireslit = Integer.parseInt(token2);
					} else if (token.equals("mute-end")) {
						p.muteEnd = Long.parseLong(token2);
					} else if (token.equals("slayerTask")) {
						p.slayerTask = Integer.parseInt(token2);
					} else if (token.equals("slayerPoints")) {
						p.slayerPoints = Integer.parseInt(token2);
					} else if (token.equals("taskAmount")) {
						p.taskAmount = Integer.parseInt(token2);
					} else if (token.equals("magePoints")) {
						p.magePoints = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.autoRet = Integer.parseInt(token2);
					} else if (token.equals("barrowskillcount")) {
						p.barrowsKillCount = Integer.parseInt(token2);
					} else if (token.equals("combatProtected1")) {
						p.combatProtected1 = Integer.parseInt(token2);
					} else if (token.equals("combatProtected2")) {
						p.combatProtected2 = Integer.parseInt(token2);
					} else if (token.equals("skillProtected1")) {
						p.skillProtected1 = Integer.parseInt(token2);
					} else if (token.equals("skillProtected2")) {
						p.skillProtected2 = Integer.parseInt(token2);
					} else if (token.equals("skillProtected3")) {
						p.skillProtected3 = Integer.parseInt(token2);
					} else if (token.equals("deposit1")) {
						p.deposit1 = Integer.parseInt(token2);
					} else if (token.equals("hpInsurance")) {
						p.hpInsurance = Integer.parseInt(token2);
					} else if (token.equals("deposit2")) {
						p.deposit2 = Integer.parseInt(token2);
					} else if (token.equals("deposit3")) {
						p.deposit3 = Integer.parseInt(token2);
					} else if (token.equals("deposit4")) {
						p.deposit4 = Integer.parseInt(token2);
					} else if (token.equals("deposit5")) {
						p.deposit5 = Integer.parseInt(token2);
					} else if (token.equals("flagged")) {
						p.accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("removedTask0")) {
						p.removedTasks[0] = Integer.parseInt(token2);
					} else if (token.equals("removedTask1")) {
						p.removedTasks[1] = Integer.parseInt(token2);
					} else if (token.equals("removedTask2")) {
						p.removedTasks[2] = Integer.parseInt(token2);
					} else if (token.equals("removedTask3")) {
						p.removedTasks[3] = Integer.parseInt(token2);
					} else if (token.equals("xpLock")) {
						p.expLock = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("gwkc")) {
						p.killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.fightMode = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.playerEquipment[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.playerAppearance[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.playerLevel[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerXP[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.playerItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.bankItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.friends[Integer.parseInt(token3[0])] = Long
								.parseLong(token3[1]);
					}
					break;
				case 9:
					/*
					 * if (token.equals("character-ignore")) {
					 * ignores[Integer.parseInt(token3[0])] =
					 * Long.parseLong(token3[1]); }
					 */
					break;
				case 10:
					if (token.equals("raid-loot")) {
						ArrayList<PlayerItem> loot = new ArrayList<PlayerItem>();
						int id = Integer.parseInt(token3[0].substring(0, 1));
						
						String tokenStr = token3[0].substring(2);
						
						if (tokenStr.contains(";")) {
							if (tokenStr.contains(",")) {
								String[] parts = tokenStr.split(",");
								
								for (String part : parts) {
									int itemId = Integer.parseInt(part.split(";")[0]);
									int amount = Integer.parseInt(part.split(";")[1]);
									
									System.out.println(itemId + " x" + amount);
									
									loot.add(new PlayerItem(itemId, amount));
								}
							} else {
								int itemId = Integer.parseInt(tokenStr.split(";")[0]);
								int amount = Integer.parseInt(tokenStr.split(";")[1]);
								
								loot.add(new PlayerItem(itemId, amount));
							}
							
							p.bankKeyLoot[id - 1] = loot.toArray(new PlayerItem[0]);
						}
					}
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[IGNORES]")) {
					ReadMode = 9;
				} else if (line.equals("[RAID]")) {
					ReadMode = 10;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return 13;
	}

	private static <T> void write(BufferedWriter characterfile, String name, T value) throws IOException {
		String delimeter = name + " = ";
		characterfile.write(delimeter);
		characterfile.write(value.toString());
		characterfile.newLine();
	}
	
	/**
	 * Saving
	 **/
	public static boolean saveGame(Client p) {
		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (p.playerName == null || PlayerHandler.players[p.playerId] == null) {
			// System.out.println("second");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(
					Config.DATA_LOC + "characters/" + p.playerName + ".txt"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(p.playerPass, 0, p.playerPass.length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			
			// Barrows
			Barrows barrows = p.getBarrows();
			write(characterfile, "barrows-broskilled", barrows.getNpcController().getKilledBrothers());
			write(characterfile, "barrows-target", barrows.getNpcController().getTargetBrother().getID());
					write(characterfile, "barrows-looted", barrows.getChest().hasLooted());
			write(characterfile, "barrows-kc", barrows.getNpcController().getKillCount());
			
			
			characterfile.write("timePlayed = ");
			characterfile.write(Long.toString(p.timePlayed), 0,
					Long.toString(p.timePlayed).length());
			characterfile.newLine();
			characterfile.write("lastLoginDate = ", 0, 16);
			characterfile.write(Integer.toString(p.lastLoginDate), 0, Integer
					.toString(p.lastLoginDate).length());
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.heightLevel), 0, Integer
					.toString(p.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0,
					Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0,
					Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(p.getRights().getValues()), 0, Integer
					.toString(p.getRights().getValues()).length());
			characterfile.newLine();
			characterfile.write("isMember = ", 0, 11);
			characterfile.write(Boolean.toString(p.isMember), 0, Boolean
					.toString(p.isMember).length());
			characterfile.newLine();
			characterfile.write("memberPoints = ", 0, 15);
			characterfile.write(Integer.toString(p.memberPoints), 0, Integer
					.toString(p.memberPoints).length());
			characterfile.newLine();
			characterfile.write("easyClueCount = ", 0, 16);
			characterfile.write(Integer.toString(p.easyClueCount), 0, Integer
					.toString(p.easyClueCount).length());
			characterfile.newLine();
			characterfile.write("mediumClueCount = ", 0, 18);
			characterfile.write(Integer.toString(p.mediumClueCount), 0, Integer
					.toString(p.mediumClueCount).length());
			characterfile.newLine();
			characterfile.write("hardClueCount = ", 0, 16);
			characterfile.write(Integer.toString(p.hardClueCount), 0, Integer
					.toString(p.hardClueCount).length());
			characterfile.newLine();
			characterfile.write("UUID = ", 0, 7);
			characterfile.write(p.UUID, 0, p.UUID.length());
			characterfile.newLine();
			characterfile.write("character-title = ", 0, 18);
			characterfile.write(p.playerTitle, 0, p.playerTitle.length());
			characterfile.newLine();
			characterfile.write("character-title-color = ", 0, 24);
			characterfile.write(Integer.toString(p.titleColor), 0, Integer
					.toString(p.titleColor).length());
			characterfile.newLine();
			for (int i = 0; i < p.lastConnectedFrom.size(); i++) {
				characterfile.write("connected-from = ", 0, 17);
				characterfile.write(p.lastConnectedFrom.get(i), 0,
						p.lastConnectedFrom.get(i).length());
				characterfile.newLine();
			}
			characterfile.write("nmzPoints = ", 0, 12);
			characterfile.write(Integer.toString(p.nmzPoints), 0, Integer
					.toString(p.nmzPoints).length());
			characterfile.newLine();
			for (int j = 0; j < p.lastKilledPlayers.size(); j++) {
				characterfile.write("killed-players = ", 0, 17);
				characterfile.write(p.lastKilledPlayers.get(j), 0,
						p.lastKilledPlayers.get(j).length());
				characterfile.newLine();
			}
			for (int i = 0; i < p.removedTasks.length; i++) {
				characterfile.write("removedTask" + i + " = ", 0, 15);
				characterfile.write(Integer.toString(p.removedTasks[i]), 0,
						Integer.toString(p.removedTasks[i]).length());
				characterfile.newLine();
			}
			// start quests
			characterfile.write("pinAttempts = ", 0, 14);
			characterfile.write(Integer.toString(p.pinAttempts), 0, Integer
					.toString(p.pinAttempts).length());
			characterfile.newLine();
			characterfile.write("lockoutTime = ", 0, 14);
			characterfile.write(Integer.toString(p.lockoutTime), 0, Integer
					.toString(p.lockoutTime).length());
			characterfile.newLine();
			characterfile.write("desert-treasure = ", 0, 18);
			characterfile.write(Integer.toString(p.desertT), 0, Integer
					.toString(p.desertT).length());
			characterfile.newLine();
			characterfile.write("dt-kill = ", 0, 10);
			characterfile.write(Integer.toString(p.lastDtKill), 0, Integer
					.toString(p.lastDtKill).length());
			characterfile.newLine();
			characterfile.write("horror-from-deep = ", 0, 19);
			characterfile.write(Integer.toString(p.horrorFromDeep), 0, Integer
					.toString(p.horrorFromDeep).length());
			characterfile.newLine();
			characterfile.write("rfd-round = ", 0, 12);
			characterfile.write(Integer.toString(p.rfdRound), 0, Integer
					.toString(p.rfdRound).length());
			characterfile.newLine();
			characterfile.write("quest-points = ", 0, 15);
			characterfile.write(Integer.toString(p.questPoints), 0, Integer
					.toString(p.questPoints).length());
			characterfile.newLine();
			if (p.displayName != null) {
				characterfile.write("displayName = ", 0, 14);
				characterfile.write(p.displayName, 0, p.displayName.length());
				characterfile.newLine();
			}
			characterfile.write("cooks-assistant = ", 0, 18);
			characterfile.write(Integer.toString(p.cooksA), 0, Integer
					.toString(p.cooksA).length());
			characterfile.newLine();
			characterfile.write("startPack = ", 0, 12);
			characterfile.write(Boolean.toString(p.startPack), 0, Boolean
					.toString(p.startPack).length());
			characterfile.newLine();
			characterfile.write("setPin = ", 0, 9);
			characterfile.write(Boolean.toString(p.setPin), 0, Boolean
					.toString(p.setPin).length());
			characterfile.newLine();
			characterfile.write("bankPin1 = ", 0, 11);
			characterfile.write(Integer.toString(p.bankPin1), 0, Integer
					.toString(p.bankPin1).length());
			characterfile.newLine();
			characterfile.write("bankPin2 = ", 0, 11);
			characterfile.write(Integer.toString(p.bankPin2), 0, Integer
					.toString(p.bankPin2).length());
			characterfile.newLine();
			characterfile.write("bankPin3 = ", 0, 11);
			characterfile.write(Integer.toString(p.bankPin3), 0, Integer
					.toString(p.bankPin3).length());
			characterfile.newLine();
			characterfile.write("bankPin4 = ", 0, 11);
			characterfile.write(Integer.toString(p.bankPin4), 0, Integer
					.toString(p.bankPin4).length());
			characterfile.newLine();
			characterfile.write("hasBankpin = ", 0, 13);
			characterfile.write(Boolean.toString(p.hasBankpin), 0, Boolean
					.toString(p.hasBankpin).length());
			characterfile.newLine();
			characterfile.write("pinRegisteredDeleteDay = ", 0, 25);
			characterfile.write(Integer.toString(p.pinDeleteDateRequested), 0,
					Integer.toString(p.pinDeleteDateRequested).length());
			characterfile.newLine();
			characterfile.write("requestPinDelete = ", 0, 19);
			characterfile.write(Boolean.toString(p.requestPinDelete), 0,
					Boolean.toString(p.requestPinDelete).length());
			characterfile.newLine();
			characterfile.write("slayerPoints = ", 0, 14);
			characterfile.write(Integer.toString(p.slayerPoints), 0, Integer
					.toString(p.slayerPoints).length());
			characterfile.newLine();
			characterfile.write("petSummoned = ", 0, 14);
            characterfile.write(Boolean.toString(p.getPetSummoned()), 0, Boolean.toString(p.getPetSummoned()).length());
            characterfile.newLine();
            characterfile.write("petID = ", 0, 8);
            characterfile.write(Integer.toString(p.petID), 0, Integer.toString(p.petID).length());
            characterfile.newLine();			
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.crystalBowArrowCount), 0,
					Integer.toString(p.crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer
					.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("redSkull = ", 0, 11);
			characterfile.write(Boolean.toString(p.redSkull), 0, Boolean
					.toString(p.redSkull).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer
					.toString(p.playerMagicBook).length());
			characterfile.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				characterfile.write("brother-info = ", 0, 15);
				characterfile.write(Integer.toString(b), 0, Integer.toString(b)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(
						p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0)
								: Integer.toString(p.barrowsNpcs[b][1]), 0,
						Integer.toString(p.barrowsNpcs[b][1]).length());
				characterfile.newLine();
			}
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double
					.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.randomCoffin), 0, Integer
					.toString(p.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("KC = ", 0, 4);
			characterfile.write(Integer.toString(p.KC), 0,
					Integer.toString(p.KC).length());
			characterfile.newLine();
			characterfile.write("DC = ", 0, 4);
			characterfile.write(Integer.toString(p.DC), 0,
					Integer.toString(p.DC).length());
			characterfile.newLine();
			characterfile.write("pkp = ", 0, 6);
			characterfile.write(Integer.toString(p.pkp), 0,
					Integer.toString(p.pkp).length());
			characterfile.newLine();
			characterfile.write("donP = ", 0, 6);
			characterfile.write(Integer.toString(p.donPoints), 0, Integer
					.toString(p.donPoints).length());
			characterfile.newLine();
			characterfile.write("Dfs-Charge = ", 0, 13);
			characterfile.write(Integer.toString(p.dfsCharge), 0, Integer
					.toString(p.dfsCharge).length());
			characterfile.newLine();
			characterfile.write("xpLock = ", 0, 9);
			characterfile.write(Boolean.toString(Config.LOCK_EXPERIENCE), 0,
					Boolean.toString(Config.LOCK_EXPERIENCE).length());
			characterfile.newLine();
			characterfile.write("xpLock = ", 0, 9);
			characterfile.write(Boolean.toString(p.expLock), 0, Boolean
					.toString(p.expLock).length());
			characterfile.newLine();
			characterfile.write("barrows-killcount = ", 0, 20);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0,
					Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.pcPoints), 0, Integer
					.toString(p.pcPoints).length());
			characterfile.newLine();
			characterfile.write("mute-end = ", 0, 11);
			characterfile.write(Long.toString(p.muteEnd), 0,
					Long.toString(p.muteEnd).length());
			characterfile.newLine();
			characterfile.write("slayerTask = ", 0, 13);
			characterfile.write(Integer.toString(p.slayerTask), 0, Integer
					.toString(p.slayerTask).length());
			characterfile.newLine();
			characterfile.write("taskAmount = ", 0, 13);
			characterfile.write(Integer.toString(p.taskAmount), 0, Integer
					.toString(p.taskAmount).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.magePoints), 0, Integer
					.toString(p.magePoints).length());
			characterfile.newLine();
			characterfile.write("autoRet = ");
			characterfile.write(Integer.toString(p.autoRet), 0, Integer
					.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("barrowskillcount = ", 0, 19);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean
					.toString(p.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.waveId), 0, Integer
					.toString(p.waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.killCount), 0, Integer
					.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.fightMode), 0, Integer
					.toString(p.fightMode).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t"
					+ p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t"
					+ p.voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.write("firesLit = ", 0, 11);
			characterfile.write(Integer.toString(p.fireslit), 0, Integer
					.toString(p.fireslit).length());
			characterfile.newLine();
			characterfile.write("darts-loaded = ", 0, 15);
			characterfile.write(Integer.toString(p.dartsLoaded), 0, Integer
					.toString(p.dartsLoaded).length());
			characterfile.newLine();
			characterfile.write("combatProtected1 = ", 0, 19);
			characterfile.write(Integer.toString(p.combatProtected1), 0, Integer
					.toString(p.combatProtected1).length());
			characterfile.newLine();
			characterfile.write("combatProtected2 = ", 0, 19);
			characterfile.write(Integer.toString(p.combatProtected2), 0, Integer
					.toString(p.combatProtected2).length());
			characterfile.newLine();
			characterfile.write("skillProtected1 = ", 0, 18);
			characterfile.write(Integer.toString(p.skillProtected1), 0, Integer
					.toString(p.skillProtected1).length());
			characterfile.newLine();
			characterfile.write("skillProtected2 = ", 0, 18);
			characterfile.write(Integer.toString(p.skillProtected2), 0, Integer
					.toString(p.skillProtected2).length());
			characterfile.newLine();
			characterfile.write("skillProtected3 = ", 0, 18);
			characterfile.write(Integer.toString(p.skillProtected3), 0, Integer
					.toString(p.skillProtected3).length());
			characterfile.newLine();
			characterfile.write("deposit1 = ", 0, 11);
			characterfile.write(Integer.toString(p.deposit1), 0, Integer
					.toString(p.deposit1).length());
			characterfile.newLine();
			characterfile.write("deposit2 = ", 0, 11);
			characterfile.write(Integer.toString(p.deposit2), 0, Integer
					.toString(p.deposit2).length());
			characterfile.newLine();
			characterfile.write("deposit3 = ", 0, 11);
			characterfile.write(Integer.toString(p.deposit3), 0, Integer
					.toString(p.deposit3).length());
			characterfile.newLine();
			characterfile.write("deposit4 = ", 0, 11);
			characterfile.write(Integer.toString(p.deposit4), 0, Integer
					.toString(p.deposit4).length());
			characterfile.newLine();
			characterfile.write("deposit5 = ", 0, 11);
			characterfile.write(Integer.toString(p.deposit5), 0, Integer
					.toString(p.deposit5).length());
			characterfile.newLine();
			characterfile.write("hpInsurance = ", 0, 14);
			characterfile.write(Integer.toString(p.hpInsurance), 0, Integer
					.toString(p.hpInsurance).length());
			characterfile.newLine();
			characterfile.write("crabsKilled = ", 0, 14);
			characterfile.write(Integer.toString(p.crabsKilled), 0, Integer
					.toString(p.crabsKilled).length());
			characterfile.newLine();
			characterfile.write("music-unlocked = ", 0, 17);
			String music = "";
			for (int i = 0; i < Music.unlocked.length; i++)
				music += Music.unlocked[i] + "\t";
			characterfile.write(music);
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipment[i]), 0,
						Integer.toString(p.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0,
						Integer.toString(p.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerAppearance[i]), 0,
						Integer.toString(p.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerLevel[i]), 0,
						Integer.toString(p.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer
						.toString(p.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItems[i]), 0,
							Integer.toString(p.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItemsN[i]), 0,
							Integer.toString(p.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.bankItems.length; i++) {
				if (p.bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems[i]), 0,
							Integer.toString(p.bankItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItemsN[i]), 0,
							Integer.toString(p.bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			/* RAID LOOT */
			characterfile.write("[RAID]", 0, 6);
			characterfile.newLine();
			for (int i = 0; p.bankKeyLoot.length > i; i++) {
				characterfile.write("raid-loot = ", 0, 12);
				characterfile.write(Integer.toString(i + 1) + ":", 0, Integer.toString(i).length() + 1);
				
				if (p.bankKeyLoot[i] != null) {
					for (int ii = 0; p.bankKeyLoot[i].length > ii; ii++) {
						if (ii > 0)
							characterfile.write(",", 0, 1);
						characterfile.write(Integer.toString(p.bankKeyLoot[i][ii].getId()) + ";" + Integer.toString(p.bankKeyLoot[i][ii].getAmount()));
					}	
				}
				
				characterfile.newLine();
			}
			characterfile.newLine();

			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.friends.length; i++) {
				if (p.friends[i] > 0) {
					characterfile.write("character-friend = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write("" + p.friends[i]);
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* IGNORES */
			/*
			 * characterfile.write("[IGNORES]", 0, 9); characterfile.newLine();
			 * for (int i = 0; i < ignores.length; i++) { if (ignores[i] > 0) {
			 * characterfile.write("character-ignore = ", 0, 19);
			 * characterfile.write(Integer.toString(i), 0,
			 * Integer.toString(i).length()); characterfile.write("	", 0, 1);
			 * characterfile.write(Long.toString(ignores[i]), 0,
			 * Long.toString(ignores[i]).length()); characterfile.newLine(); } }
			 * characterfile.newLine();
			 */
			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			return false;
		}
		return true;
	}

}