package wind.model.players.impl;

import java.util.Calendar;
import java.util.Date;

import wind.model.players.Client;

/**
 * Handles how long the player has played for
 * 
 * @author Zack/Optimum
 *
 */
public class TimePlayed {

	/**
	 * The player
	 */
	private final Client player;

	/**
	 * The start date
	 */
	private Calendar start = Calendar.getInstance();

	/**
	 * The end date
	 */
	private Calendar end = Calendar.getInstance();

	/**
	 * Constructor for the time played class
	 * 
	 * @param player
	 *            - The player
	 */
	public TimePlayed(final Client player) {
		this.player = player;
	}

	/**
	 * Records when the player logs in
	 */
	public void initiliseNewStart() {
		getStartDate().setTime(new Date());
	}

	/**
	 * Records when the player logs out.
	 */
	public void initiliseNewEnd() {
		getEndDate().setTime(new Date());
	}

	/**
	 * Gets the players start date
	 * 
	 * @return - the start date
	 */
	public Calendar getStartDate() {
		return start;
	}

	/**
	 * Gets the players end date
	 * 
	 * @return - The end date
	 */
	public Calendar getEndDate() {
		return end;
	}

	/**
	 * Gets the length of time that the player was logged in for
	 * 
	 * @return - Time
	 */
	public long getCurrentSession() {
		return end.getTimeInMillis() - start.getTimeInMillis();
	}

	/**
	 * Formats the long into a time format
	 * 
	 * @return - The new format
	 */
	public String formatPlayersTime() {
		long second = (player.timePlayed / 1000) % 60;
		long minute = (player.timePlayed / (1000 * 60)) % 60;
		long hour = (player.timePlayed / (1000 * 60 * 60)) % 24;
		String format = hour + " hours, " + minute + " minutes and " + second
				+ " seconds!";
		return format;
	}

}