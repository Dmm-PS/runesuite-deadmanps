package shamon.barrows;

import shamon.region.GameObject;
import shamon.region.GameObjectManager;
import shamon.region.Position;
import shamon.region.Rotation;
import wind.model.players.Client;

public class TunnelDoor extends GameObject {

	public TunnelDoor(int doorID, Position position, Rotation rotation) {
		super(doorID, position, 0, rotation);
	}

	public static TunnelDoor create(int doorID, Position position, Rotation rotation) {
		return new TunnelDoor(doorID, position, rotation);
	}

	public void resetDoor(Client client) {
		GameObjectManager.placeLocalObject(client, this);
	}

	public void closeDoor(Client client, int closeID) {
		GameObjectManager.placeLocalObject(client, new GameObject(closeID, getPosition(), 0, getRotation()));
	}
}
