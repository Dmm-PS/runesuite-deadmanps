package wind.world;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import wind.Server;
import wind.model.objects.Object;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.util.Misc;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();
	public CopyOnWriteArrayList<Object> object = new CopyOnWriteArrayList<Object>();

	public void process() {
		for (final Object o : objects) {
			if (o.tick > 0) {
				o.tick--;
			} else {
				updateObject(o);
				toRemove.add(o);
			}
		}
		for (final Object o : toRemove) {
			/*
			 * if (o.objectId == 2732) { for (final Player player :
			 * PlayerHandler.players) { if (player != null) { final Client c =
			 * (Client)player; Server.itemHandler.createGroundItem(c, 592,
			 * o.objectX, o.objectY, 1, c.playerId); } } }
			 */
			if (isObelisk(o.newId)) {
				final int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);
		}
		toRemove.clear();
	}

	public boolean objectExists(final int x, final int y) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y) {
				return true;
			}
		}
		return false;
	}

	public void removeObject(int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	public void updateObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public void placeObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face,
							o.type);
			}
		}
	}

	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}
		return null;
	}

	public void loadObjects(Client c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o, c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face,
						o.type);
		}
		loadCustomSpawns(c);
		Deletewalls(c);
	}

	public void Deletewalls(Client c) {	
		c.getPA().checkObjectSpawn(-1, 3183, 3447, -1, 0);
	}

	public static void loadCustomSpawns(Client c) {
		Server.objectHandler.updateObjects(c);
		c.getPA().checkObjectSpawn(2790, 3180, 3439, 1, 10);
		c.getPA().checkObjectSpawn(2790, 3252, 3423, 0, 10);
//		c.getPA().checkObjectSpawn(2790, 3210, 3218, 2, 10);
		c.getPA().checkObjectSpawn(8987, 3098, 3497, 0, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		c.getPA().checkObjectSpawn(2790, 3222, 3217, 3, 10); //home bank
	//	c.getPA().checkObjectSpawn(2790, 3222, 3220, 3, 10);
		c.getPA().checkObjectSpawn(409, 3090, 3494, 1, 10);
		c.getPA().checkObjectSpawn(410, 3303, 2764, 1, 10);
		c.getPA().checkObjectSpawn(409, 2539, 4718, 1, 10);
		c.getPA().checkObjectSpawn(6552, 3298, 2756, 1, 10);
		c.getPA().checkObjectSpawn(76, 3218, 9622, 2, 10);
		c.getPA().checkObjectSpawn(411, 2340, 9806, 2, 10);
		c.getPA().checkObjectSpawn(2732, 2606, 3401, 3, 10);
		c.getPA().checkObjectSpawn(-1, 2950, 3450, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3103, 3499, 0, 0);
		c.getPA().checkObjectSpawn(-1, 3046, 9756, 0, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		c.getPA().checkObjectSpawn(1516, 3217, 3218, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		c.getPA().checkObjectSpawn(1530, 2964, 3206, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
	//	c.getPA().checkObjectSpawn(409, 3228, 3217, 4, 10);
		/* END OF WALL SAFE */

		if (c.heightLevel == 0)
			c.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		else
			c.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10);
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 14827, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 },
			{ 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 }, { 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
			}
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}

	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(),
						obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2,
						1)) {
					c.getPA().startTeleport2(
							obeliskCoords[random][0] + xOffset,
							obeliskCoords[random][1] + yOffset, 0);
				}
			}
		}
	}

	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60
				&& c.heightLevel == o.height;
	}

	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

	/**
	 * Removing Minigame Objects
	 * 
	 * @param objectClass
	 *            - (1 = CastleWars objects)
	 */
	public void removeGameObjects(int objectClass) {
		for (Object o : object) {
			if (o.objectClass == objectClass)
				object.remove(o);
		}
	}

}