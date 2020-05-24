package wind.model.players.packets.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Commands {

	private static final Map<String, Command> COMMANDS = new HashMap<String, Command>();
	
	public static int count() {
		return COMMANDS.size();
	}
	
	public static void addCommand(final Command command) {
		for (String s : command.key()) {
			COMMANDS.put(s, command);
		}
	}
	
	public static Optional<Command> get(String command) {
		return Optional.of(COMMANDS.get(command));
	}
	
}
