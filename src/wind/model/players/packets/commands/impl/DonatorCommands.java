package wind.model.players.packets.commands.impl;

import wind.model.players.Client;
import wind.model.players.Rights;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.CommandBuilder;

public class DonatorCommands extends CommandBuilder{

	@Override
	public Command[] commands() {
		return new Command[] {
			
			new PlayerCommand("switch") {
				@Override
				public void execute(Client c, String command) {
					if (c.playerMagicBook == 2) {
						c.sendMessage("You switch to modern magic.");
						c.setSidebarInterface(6, 1151);
						c.playerMagicBook = 0;
					} else if (c.playerMagicBook == 0) {
						c.sendMessage("You switch to ancient magic.");
						c.setSidebarInterface(6, 12855);
						c.playerMagicBook = 1;
					} else if (c.playerMagicBook == 1) {
						c.sendMessage("You switch to lunar magic.");
						c.setSidebarInterface(6, 29999);
						c.playerMagicBook = 2;
					}
				}
			},
			
			new PlayerCommand("dz") {
				@Override
				public void execute(Client c, String command) {
					c.getPA().startTeleport(2337, 9804, 0, "modern");
					c.sendMessage("Donator zone is currently in development.");
				}
			},
			
			new PlayerCommand("dshop") {
				@Override
				public void execute(Client c, String command) {
					c.getShops().openShop(11);
				}
			},
			
			new PlayerCommand("spec") {
				@Override
				public void execute(Client c, String command) {
					if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR)) {
						c.specAmount = 10.0;
						c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
					} else {
					if (System.currentTimeMillis() - c.specCom >= 60000) {
						if (c.inWild()) {
							c.sendMessage("You cannot restore special attack in the wilderness!");
							return;
						}
						if (c.duelStatus == 5) {
							c.sendMessage("You cannot restore your special attack during a duel.");
							return;
						}
						c.specCom = System.currentTimeMillis();
						c.specAmount = 10.0;
						c.getItems().updateSpecialBar();
						c.getItems().addSpecialBar(
								c.playerEquipment[c.playerWeapon]);
					} else {
						c.sendMessage("You must wait 60 seconds to restore your special attack.");
					}
				}
				}
			},
			
			new PlayerCommand("switch") {
				@Override
				public void execute(Client c, String command) {
					if (c.inWild()) {
						c.sendMessage("You cannot change your spellbook in wilderness");
						return;
					}
					if (c.duelStatus == 5) {
						c.sendMessage("You cannot change your spellbook during a duel.");
						return;
					}
					if (c.playerMagicBook == 2) {
						c.sendMessage("You switch to modern magic.");
						c.setSidebarInterface(6, 1151);
						c.playerMagicBook = 0;
					} else if (c.playerMagicBook == 0) {
						c.sendMessage("You switch to ancient magic.");
						c.setSidebarInterface(6, 12855);
						c.playerMagicBook = 1;
					} else if (c.playerMagicBook == 1) {
						c.sendMessage("You switch to lunar magic.");
						c.setSidebarInterface(6, 29999);
						c.playerMagicBook = 2;
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
			return Rights.DONATOR;
		}

		@Override
		public String[] key() {
			return keys;
		}
		
	}

}