package shamon.region;

import java.util.HashSet;
import java.util.Set;

import wind.model.players.Client;
import wind.model.players.Player;

public final class GameObjectManager {
	/**
	 * The amount of objects to spawn per server tick.
	 */
	private static final int SPAWNS_PER_TICK = 32;

	/**
	 * Packet ID for removing objects.
	 */
	private static final int REMOVE_OBJECT_PACKET = 101;

	/**
	 * Packet ID for adding objects.
	 */
	private static final int ADD_OBJECT_PACKET = 151;

	/**
	 * Packet ID for the local position.
	 */
	private static final int LOCAL_POSITION_PACKET = 85;

	/**
	 * Packet ID for the object animation.
	 */
	private static final int OBJECT_ANIMATION_PACKET = 160;

	/**
	 * Position offset. Doesn't matter since we send packet 85.
	 * First 4 bits is the y offset the next four are the x offset.
	 * Ex: (xOff << 4) | yOff
	 * 
	 * The max offset can only be 16 tiles away so it's not that useful.
	 * Maybe for doors or building a rectangle of objects (use packet 60).
	 * */
	private static final int POSITION_OFFSET = 0;

	/**
	 * Place an object in the world for everyone to see.
	 */
	public static void placeGlobalObject(GameObject object) {
		Region region = RegionManager.getRegionByLocation(object.getPosition());
		region.registerDynamicObject(object);
	}

	/**
	 * Place an object in the world for only the user to see.
	 */
	public static void placeLocalObject(Client client, GameObject object) {
		sendLocalPosition(client, object);
		sendAddObject(client, object);
	}

	/**
	 * Play an animation for the user to see.
	 */
	public static void playAnimation(Client client, int animationID, GameObject object) {
		sendLocalPosition(client, object);
		sendObjectAnimation(client, animationID, object);
	}

	/**
	 * Remove an object in the world from everyone.
	 */
	public static void removeGlobalObject(GameObject object) {
		Region region = RegionManager.getRegionByLocation(object.getPosition());
		region.unregisterDynamicObject(object);
	}

	/**
	 * Remove an object in the world a user.
	 */
	public static void removeLocalObject(Client client, GameObject object) {
		sendLocalPosition(client, object);
		sendRemoveObject(client, object);
	}

	/**
	 * Queue in objects based on the region the players in.
	 */
	public static void queueObjects(Player player) {
		Position position = new Position(player.absX, player.absY, player.heightLevel);

		Set<Region> nextRegions = new HashSet<>();
		for (Region region : RegionManager.getSurroundingRegions(position)) {
			nextRegions.add(region);
		}
		// Find intersecting regions between the new and cached.
		Set<Region> intersect = new HashSet<>(player.getCachedRegions());
		intersect.retainAll(nextRegions);

		Set<Region> toRemove = new HashSet<>(player.getCachedRegions());
		Set<Region> toAdd = new HashSet<>(nextRegions);

		// Remove the intersections so we have the misfits.
		toRemove.removeAll(intersect);
		toAdd.removeAll(intersect);

		// Remove objects outside of the current region and add in any new regions.
		player.getCachedRegions().removeAll(toRemove);
		player.getCachedRegions().addAll(toAdd);

		// Any new regions added queue in their objects.
		toAdd.forEach(region -> {
			player.getObjectQueue().addAll(region.getDynamicObjects());
		});
	}

	/**
	 * Spawn in SPAWNS_PER_TICK objects every game tick.
	 */
	public static void processObjects(Client client) {
		// No objects to spawn do nothing.
		if (client.getObjectQueue().isEmpty()) {
			return;
		}

		for (int count = 0; count < SPAWNS_PER_TICK; count++) {
			GameObject object = client.getObjectQueue().poll();

			// Queued in all the objects possible.
			if (object == null) {
				return;
			}

			int objectID = object.getID();

			// Send objects local position to client.
			sendLocalPosition(client, object);

			if (objectID == -1) {
				sendRemoveObject(client, object);
			} else {
				sendAddObject(client, object);
			}
		}
	}

	private static void sendObjectAnimation(Client client, int animationID, GameObject object) {
		int objectDetails = (object.getType() << 2) + (object.getRotation().offset() & 3);

		client.getOutStream().createFrame(OBJECT_ANIMATION_PACKET);
		client.getOutStream().writeByteS(POSITION_OFFSET);
		client.getOutStream().writeByteS(objectDetails);
		client.getOutStream().writeWordA(animationID);
	}

	/**
	 * Send an objects local position to the client.
	 */
	private static void sendLocalPosition(Client client, GameObject object) {
		int objectX = object.getPosition().getX();
		int objectY = object.getPosition().getY();
		int realMapX = client.getMapRegionX() << 3;
		int realMapY = client.getMapRegionY() << 3;
		int localX = objectX - realMapX;
		int localY = objectY - realMapY;

		client.getOutStream().createFrame(LOCAL_POSITION_PACKET);
		client.getOutStream().writeByteC(localY);
		client.getOutStream().writeByteC(localX);
	}

	/**
	 * Remove an object from the map. Objects ID must be -1.
	 * 
	 * Don't use this, this is private use. Instead use removeObject.
	 */
	private static void sendRemoveObject(Client client, GameObject object) {
		int objectID = object.getID();

		if (objectID == -1) {
			int objectDetails = (object.getType() << 2) + (object.getRotation().offset() & 3);
			client.getOutStream().createFrame(REMOVE_OBJECT_PACKET);
			client.getOutStream().writeByteC(objectDetails);
			client.getOutStream().writeByte(POSITION_OFFSET);
			client.flushOutStream();
		}
	}

	/**
	 * Add and object to the map.
	 * 
	 * Don't use this, this is private use. Instead use placeObject.
	 */
	private static void sendAddObject(Client client, GameObject object) {
		int objectID = object.getID();
		int objectDetails = (object.getType() << 2) + (object.getRotation().offset() & 3);

		client.getOutStream().createFrame(ADD_OBJECT_PACKET);
		client.getOutStream().writeByte(POSITION_OFFSET);
		client.getOutStream().writeWordBigEndian(objectID); // I renamed this this because BigEndian is wrong.
		client.getOutStream().writeByteS(objectDetails);
		client.flushOutStream();
	}
}
