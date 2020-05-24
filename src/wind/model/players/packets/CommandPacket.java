package wind.model.players.packets;

import java.util.Optional;

import wind.Punishments;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.model.players.packets.commands.Command;
import wind.model.players.packets.commands.Commands;

public class CommandPacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String cmd = c.getInStream().readString();

		if (cmd.startsWith("/")) {
			
			if (!Punishments.isMuted(c)) {
				if (c.clan != null) {
					String message = cmd.substring(1);
					c.clan.sendChat(c, message);
				} else {
					c.sendMessage("You must be in a clan chat to do this.");
				}
			} else {
				c.sendMessage("You are muted for breaking a rule.");
			}
			
			return;
		}
		
		Optional<Command> command = Commands.get(cmd.split(" ")[0]);
		
		if (!command.isPresent()) {
			c.sendMessage("Unable to find command. " + cmd);
		} else {
				//check rights when finished
			
			if (c.getRights().greaterOrEqual(command.get().rank())) {
				command.get().execute(c, cmd);
			} else {
				return;
			}
		}
		
	}
	
}