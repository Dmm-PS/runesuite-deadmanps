package wind.model.players.packets.commands;

import wind.model.players.Client;
import wind.model.players.Rights;

public interface Command {

	public void execute(Client c, String command);
	
	public Rights rank();
	
	public String[] key();
}
