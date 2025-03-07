package wind.util;

import java.sql.*;

import wind.model.players.Client;
import wind.model.players.PlayerHandler;


public class PlayersOnline {

	public static Connection con = null;
	public static Statement stm;

	public static void createCon() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://WEBSITE/DATABASE", "DATABASEUSER", "DATABASEPASSWORD");
			stm = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stm.executeQuery(s);
				return rs;
			} else {
				stm.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void destroyCon() {
		try {
			stm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public static boolean offline(Client c) {
		try {
			query("DELETE FROM `online` WHERE id = 1;");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean online(Client c) {
		try {
			query("INSERT INTO `online` (id, currentlyonline) VALUES('1','"+PlayerHandler.getPlayerCount()+"');");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}