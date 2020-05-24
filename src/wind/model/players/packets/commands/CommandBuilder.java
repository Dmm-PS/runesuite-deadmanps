package wind.model.players.packets.commands;

import wind.Config;
import wind.model.players.packets.commands.impl.AdminCommands;
import wind.model.players.packets.commands.impl.DeveloperCommands;
import wind.model.players.packets.commands.impl.DonatorCommands;
import wind.model.players.packets.commands.impl.ModeratorCommands;
import wind.model.players.packets.commands.impl.PlayerCommands;

public abstract class CommandBuilder {

	public abstract Command[] commands();
	
	public void build() {
		for (Command c : commands()) {
			Commands.addCommand(c);
		}
	}
	
	public static void buildAll() {		
		CommandBuilder[] builders = new CommandBuilder[] {
				new PlayerCommands(),
				new DonatorCommands(),
				new ModeratorCommands(),
				new AdminCommands(),
				new DeveloperCommands(),
		};
		for (CommandBuilder b : builders) {
			b.build();
		}
		int amount = Commands.count();
		System.out.println(Config.SYSTEM_NAME + "Loaded: " + amount + " commands!");
	}
	
}
