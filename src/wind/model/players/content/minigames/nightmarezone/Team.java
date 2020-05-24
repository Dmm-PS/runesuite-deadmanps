package wind.model.players.content.minigames.nightmarezone;

import java.util.ArrayList;

import wind.model.players.Client;

public class Team {
	
	private static final Team singleton = new Team();
	
	public void delete() {
		
	}
	
	public void add(Client p) {
	team.add(p);
	}
	
	private ArrayList<Client> team = new ArrayList<>();


	public static Team getSingleton() {
		return singleton;
	}
	
	
}