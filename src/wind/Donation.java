package wind;

import java.sql.ResultSet;
import wind.model.players.Client;

public class Donation implements Runnable {

	public static final String HOST_ADDRESS = "deadmanps.net";
	public static final String USERNAME = "deadmanp_store";
	public static final String PASSWORD = "skl4klJDSOdkl4k!";
	public static final String DATABASE = "deadmanp_store";
	
	private static Client player;
	
	@SuppressWarnings("unused")
	public void run() {
		try {
			Database db = new Database(HOST_ADDRESS, USERNAME, PASSWORD, DATABASE);
			
			if (!db.init()) {
				System.err.println("[Donation] Failed to connect to database!");
				return;
			}
			
			String name = player.playerName.replace("_", " ");//may work but umm 
			ResultSet rs = db.executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND claimed=0");
			
			while(rs.next()) {
				String item_name = rs.getString("item_name");
				int item_number = rs.getInt("item_number");
				double amount = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				
				ResultSet result = db.executeQuery("SELECT * FROM products WHERE item_id="+item_number+" LIMIT 1");
				
				if (result == null || !result.next()
						//|| !result.getString("item_name").equalsIgnoreCase(item_name)
					//	|| result.getDouble("item_price") != amount
					//	|| quantity < 1 || quantity > Integer.MAX_VALUE) {
						) {
					System.out.println("[Donation] Invalid purchase for "+name+" (item: "+item_name+", id: "+item_number+")");
					continue;
				}
				
				handleItems(item_number);
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			
			db.destroyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void handleItems(int item_number) { // $10 scroll id 607 / $50 id 608 / $100 id 786
		switch(item_number) {
		case 11://this is the product Id located
			player.sendMessage("@red@You Have Recieved Your donation! Thank you for your support!");
            player.getItems().addItem(607, 1); 
			// handle item stuff, like adding items, points, etc.
			break;
		case 12://this is the product Id located
			player.sendMessage("@red@You Have Recieved Your donation! Thank you for your support!");
            player.getItems().addItem(608, 1); 
			// handle item stuff, like adding items, points, etc.
			break;
		case 13://this is the product Id located
			player.sendMessage("@red@You Have Recieved Your donation! Thank you for your support!");
            player.getItems().addItem(786, 1); 
			// handle item stuff, like adding items, points, etc.
			break;
		}
	}
	
	public Donation(Client player) {
		Donation.player = player;
	}
}