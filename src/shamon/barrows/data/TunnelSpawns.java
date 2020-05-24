package shamon.barrows.data;

import shamon.region.Position;

public enum TunnelSpawns {

	NORTHWEST_SPAWN(TunnelRoomData.NORTHWEST, Position.create(3534, 9711)),
	NORTHEAST_SPAWN(TunnelRoomData.NORTHEAST, Position.create(3569, 9711)),
	SOUTHWEST_SPAWN(TunnelRoomData.SOUTHWEST, Position.create(3534, 9677)),
	SOUTHEAST_SPAWN(TunnelRoomData.SOUTHEAST, Position.create(3569, 9677));

	private final TunnelRoomData room;
	private final Position position;

	public static final TunnelSpawns[] SPAWNS = TunnelSpawns.values();

	private TunnelSpawns(TunnelRoomData room, Position position) {
		this.room = room;
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public TunnelRoomData getRoom() {
		return room;
	}
}
