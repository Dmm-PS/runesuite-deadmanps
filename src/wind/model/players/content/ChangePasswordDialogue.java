package wind.model.players.content;


import wind.model.players.Client;
import wind.model.players.Player;
import wind.model.players.packets.Dialogue;

public class ChangePasswordDialogue extends Dialogue {
	private final String password;

	public ChangePasswordDialogue(Player player, String password) {
		this.player = player;
		this.password = password;
	}

	
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			c.playerPass = password;
			sendStatement("Your password will now be: '" + password + "'");
			end();
			return true;
		case 9158:
			c.getPA().removeAllWindows();
			return true;
		}
		return false;
	}

	private void sendStatement(String string) {
		// TODO Auto-generated method stub
		
	}


	public void execute() {
		switch (next) {
		case 0:
			sendStatement("Your new password will be: '" + password + "' Are you sure you want to make this change?");
			next += 1;
			break;
		case 1:
			sendOption2("Yes.", "No.");
		}
	}
	private void sendOption2(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
	protected int next = 0;
	protected int option;
	protected Player player;
	Client c;
	public void end() {
		next = -1;
	}
	public wind.model.players.Player getPlayer() {
		return player;
	}

	public void setNext(int next) {
		this.next = next;
	}


	public void setPlayer(wind.model.players.Player player) {
		this.player = player;
	}
}
