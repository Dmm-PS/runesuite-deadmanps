package wind.world;

import java.io.FileInputStream;
import java.util.Properties;

import wind.Config;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * @author 7Winds 
 * Date: 4/8/15
 * Used to notify players of announcements.
 */
public class WorldMessenger {

	Client c;
	
	public void yell(String s) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (c.validClient(i)) {
				c.getClient(i).sendMessage(s);
			}
		}
	}

	/**
	 * Sends a message to all players.
	 */
	public void globalMessage(String message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage("" + message);
			}
		}
	}
	
	public void serverMessage(String message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage("@dbl@[DMPS] " + message);
			}
		}
	}

	/**
	 * Gets user properties.
	 */
	Properties p = new Properties();
	
	/**
	 * Main method used to randomize announcements
	 */
	public void Announcements() {
		try {
			loadIni();
			TaskHandler.submit(new Task(15, false) {
				@Override
				public void execute() {
					loadIni();
					int msg = 0;
					msg = Misc.random(5);
					switch (msg) {

					case 1:
						if (p.getProperty("announcement1").length() > 0){
							c.sendMessage(p.getProperty("announcement1")); 
						}
						break;

					case 2:
						if (p.getProperty("announcement2").length() > 0) {
							c.sendMessage(p.getProperty("announcement2"));
						}
						break;

					case 3:
						if (p.getProperty("announcement3").length() > 0) {
							c.sendMessage(p.getProperty("announcement3"));
						}
						break;

					case 4:
						if (p.getProperty("announcement4").length() > 0) {
							c.sendMessage(p.getProperty("announcement4"));
						}
						break;

					default:
						if (p.getProperty("announcement0").length() > 0) {
							c.sendMessage(p.getProperty("announcement0"));
						}
						break;
					}
					this.cancel();
				}

				@Override
				public void onCancel() {
					System.out.println("[Announcement message sent]");
				}

			});
		} catch (Exception e) {
		}
	}

	/**
	 * Loads the Announcements.ini, in users dropbox.
	 */
	private void loadIni() {
		try {
			p.load(new FileInputStream(Config.DATA_LOC + "/Announcements.ini"));
		} catch (Exception e) {
		}
	}
}
