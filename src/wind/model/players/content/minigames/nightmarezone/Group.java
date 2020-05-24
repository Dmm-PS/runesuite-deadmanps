package wind.model.players.content.minigames.nightmarezone;

import java.util.ArrayList;
import java.util.List;

import wind.model.players.Client;
import wind.model.players.Player;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class Group {

	/**
	 * The player instance
	 */
	private transient Client player;
	/**
	 * Is user creator?
	 */
	private boolean creator;
	/**
	 * Grabs the player name
	 */
	private String playerName;
	/**
	 * Team's list
	 */
	private List<Client> team;
	/**
	 * Group's Name
	 */
	private String groupName;
	/**
	 * Leader
	 */
	private Player leader;
	/***
	 * 
	 * @param leader
	 */
	public Group(Client leader) {
		   this.setLeader(leader);
		   this.team = new ArrayList<Client>();
		   setGroupName(""+leader.playerName+"'s team");
		 leader.sendMessage("You have created your own team!");
		}
	
	public List<Client> getPlayers() {
		  return team;
	}
	
	
	/***
	 *  This will add player to the group.
	 * @param player
	 */
	public void add(Client player) {
		   if(team.size() == 4) {
		     player.sendMessage("<col=ff0033>Groups: The max amount of players in team can be 4.");
		     return;
		} 
			team.add(player);
			for (Client teamMembers : team) 
				teamMembers.sendMessage("Team: "+player.playerName+" has joined to the team!");
		   }
		

	public void remove(Client player) {
		team.remove(player);
		for(Client teamMember : getTeam()) {
			teamMember.sendMessage("<col=ff0033>Groups: Player "+player.playerName+" has left from the team: "+this.getGroupName()+".");
		}
	
}
	
	public Player getLeader() {
		return leader;
	}

	public void setLeader(Player leader) {
		this.leader = leader;
	}

	public boolean isCreator() {
		return creator;
	}

	public void setCreator(boolean creator) {
		this.creator = creator;
	}
	
	public List<Client> getTeam() {
		  return team;
		}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Client player) {
		this.player = player;
	}

}