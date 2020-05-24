package wind.util.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import wind.Config;
import wind.model.players.Client;
import wind.model.players.Rights;
import wind.util.Misc;

/**
 * @author 7Winds
 * @information Used to log chat
 */
public class ChatLogger {

	Client c;

	public ChatLogger(Client c) {
		this.c = c;
	}

	public void logMasterChat(String message) {
		if (c.getRights().lessOrEqual(Rights.DONATOR)) {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Config.DATA_LOC + "logs/ChatLog/Master/Player/"
								+ c.playerName + ".txt", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Config.DATA_LOC + "logs/ChatLog/Master/Mod/"
								+ c.playerName + ".txt", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void logChat(String message) {
		if (c.getRights().lessOrEqual(Rights.DONATOR)) {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Config.DATA_LOC + "logs/ChatLog/Badword/Player/" + c.playerName
								+ ".txt", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Config.DATA_LOC + "logs/ChatLog/Badword/Mod/" + c.playerName
								+ ".txt", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
