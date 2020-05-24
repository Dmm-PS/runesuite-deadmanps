package shamon.barrows.data;

public enum TunnelRoomData {

	NORTHWEST(
			TunnelHallwayData.HIGHWAY_NW_TO_NE,
			TunnelHallwayData.MAZE_W_TO_NW,
			TunnelHallwayData.MAZE_NW_TO_N,
			TunnelHallwayData.HIGHWAY_SW_TO_NW),
	NORTH(null, TunnelHallwayData.CHEST_MID_TO_N, TunnelHallwayData.MAZE_N_TO_NE, TunnelHallwayData.MAZE_NW_TO_N),
	NORTHEAST(
			TunnelHallwayData.HIGHWAY_NW_TO_NE,
			TunnelHallwayData.MAZE_NE_TO_E,
			TunnelHallwayData.HIGHWAY_NE_TO_SE,
			TunnelHallwayData.MAZE_N_TO_NE),
	WEST(TunnelHallwayData.MAZE_W_TO_NW, TunnelHallwayData.MAZE_SW_TO_W, TunnelHallwayData.CHEST_MID_TO_W, null),
	CHEST(
			TunnelHallwayData.CHEST_MID_TO_N,
			TunnelHallwayData.CHEST_MID_TO_S,
			TunnelHallwayData.CHEST_MID_TO_E,
			TunnelHallwayData.CHEST_MID_TO_W),
	EAST(TunnelHallwayData.MAZE_NE_TO_E, TunnelHallwayData.MAZE_E_TO_SE, null, TunnelHallwayData.CHEST_MID_TO_E),
	SOUTHWEST(
			TunnelHallwayData.MAZE_SW_TO_W,
			TunnelHallwayData.HIGHWAY_SE_TO_SW,
			TunnelHallwayData.MAZE_S_TO_SW,
			TunnelHallwayData.HIGHWAY_SW_TO_NW),
	SOUTH(TunnelHallwayData.CHEST_MID_TO_S, null, TunnelHallwayData.MAZE_SE_TO_S, TunnelHallwayData.MAZE_S_TO_SW),
	SOUTHEAST(
			TunnelHallwayData.MAZE_E_TO_SE,
			TunnelHallwayData.HIGHWAY_SE_TO_SW,
			TunnelHallwayData.HIGHWAY_NE_TO_SE,
			TunnelHallwayData.MAZE_SE_TO_S);

	private final TunnelHallwayData[] hallways = new TunnelHallwayData[4];

	private TunnelRoomData(TunnelHallwayData north, TunnelHallwayData south, TunnelHallwayData east,
			TunnelHallwayData west) {
		hallways[0] = north;
		hallways[1] = south;
		hallways[2] = east;
		hallways[3] = west;
	}

	public TunnelHallwayData getHallway(int direction) {
		return hallways[direction];
	}
}
