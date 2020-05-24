package wind.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import wind.model.players.Client;
import wind.model.players.Rights;

public class MadTurnipConnection extends Thread {

	public static Connection con = null;
	public static Statement stm;

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://os-falador.com/",
					"osf1", "root");
			stm = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
	}

	public MadTurnipConnection() {

	}

	@Override
	public void run() {
		while (true) {
			try {
				if (con == null)
					createConnection();
				else
					ping();
				Thread.sleep(10000);// 10 seconds
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void ping() {
		try {
			String query = "SELECT * FROM donation WHERE username = 'null'";
			query(query);
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
	}

	public static void addDonateItems(final Client c, final String name) {
		if (con == null) {
			if (stm != null) {
				try {
					stm = con.createStatement();
				} catch (Exception e) {
					con = null;
					stm = null;
					// put a sendmessage here telling them to relog in 30
					// seconds
					c.sendMessage("You must relog in 30 seconds");
					return;
				}
			} else {
				// put a sendmessage here telling them to relog in 30 seconds
				c.sendMessage("You must relog in 30 seconds");
				return;
			}
		}
		new Thread() {
			@Override
			public void run() {
				try {
					String name2 = name.replaceAll(" ", "_");
					String query = "SELECT * FROM donation WHERE username = '"
							+ name2 + "'";
					ResultSet rs = query(query);
					boolean b = false;
					while (rs.next()) {
						int prod = Integer.parseInt(rs.getString("productid"));
						int price = Integer.parseInt(rs.getString("price"));
						if (prod == 1 && price == 5) {
							c.setRights(Rights.DONATOR);
							c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
							c.sendMessage("You've donated for: @red@Donator Rank @bla@logout to recieve rank.");
							b = true;
						} else if (prod == 2 && price == 10) {
							c.setRights(Rights.SUPER_DONATOR);
							c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
							c.sendMessage("You've donated for: @red@Super Donator Rank @bla@logout to recieve rank.");
							b = true;
						} else if (prod == 3 && price == 20) {
							c.setRights(Rights.EXTREME_DONATOR);
							c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
							c.sendMessage("You've donated for: @red@Extreme Donator Rank@bla@logout to recieve rank.");
							b = true;
						} else if (prod == 4 && price == 10) {
							if (c.getRights().equal(Rights.PLAYER)) {
								c.setRights(Rights.DONATOR);
								c.donPoints += 500;
								c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
								c.sendMessage("You've donated for: @red@500 donator points@bla@.");
								c.sendMessage("You also got for free: @red@Donator Rank @bla@logout to recieve rank.");
							} else {
								c.donPoints += 500;
							}
							b = true;
						} else if (prod == 5 && price == 20) {
							if (!c.getRights().equal(Rights.DONATOR)) {
								c.setRights(Rights.DONATOR);
								c.donPoints += 1000;
								c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
								c.sendMessage("You've donated for: @red@1000 donator points@bla@.");
								c.sendMessage("You also got for free: @red@Donator Rank @bla@logout to recieve rank.");
							} else {
								c.donPoints += 1000;
								c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
								c.sendMessage("You've donated for: @red@1000 donator points@bla@.");
							}
							b = true;
						} else if (prod == 6 && price == 35) {
							if (!c.getRights().equal(Rights.DONATOR)) {
								c.setRights(Rights.SUPER_DONATOR);
								c.donPoints += 2000;
								c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
								c.sendMessage("You've donated for: @red@2000 donator points@bla@.");
								c.sendMessage("You also got for free: @red@Super Donator Rank @bla@logout to recieve rank.");
							} else {
								c.donPoints += 2000;
								c.sendMessage("@red@Ardi@bla@: @blu@Thanks for donation!");
								c.sendMessage("You've donated for: @red@2000 donator points@bla@.");
							}
							b = true;
						}
					}
					if (b) {
						query("DELETE FROM `donation` WHERE `username` = '"
								+ name2 + "';");
					}
				} catch (Exception e) {
					e.printStackTrace();
					con = null;
					stm = null;
				}
			}
		}.start();
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
			con = null;
			stm = null;
		}
		return null;
	}
}