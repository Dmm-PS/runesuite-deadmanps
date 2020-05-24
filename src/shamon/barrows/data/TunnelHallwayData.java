package shamon.barrows.data;

import shamon.barrows.TunnelDoor;
import wind.model.players.Client;

public enum TunnelHallwayData {

	CHEST_MID_TO_W(
			TunnelDoors.getDoor(3545, 9694),
			TunnelDoors.getDoor(3541, 9694),
			TunnelDoors.getDoor(3541, 9695),
			TunnelDoors.getDoor(3545, 9695)),
	MAZE_NE_TO_E(
			TunnelDoors.getDoor(3569, 9705),
			TunnelDoors.getDoor(3568, 9705),
			TunnelDoors.getDoor(3569, 9701),
			TunnelDoors.getDoor(3568, 9701)),
	MAZE_E_TO_SE(
			TunnelDoors.getDoor(3569, 9688),
			TunnelDoors.getDoor(3568, 9688),
			TunnelDoors.getDoor(3568, 9684),
			TunnelDoors.getDoor(3569, 9684)),
	CHEST_MID_TO_E(
			TunnelDoors.getDoor(3558, 9695),
			TunnelDoors.getDoor(3558, 9694),
			TunnelDoors.getDoor(3562, 9694),
			TunnelDoors.getDoor(3562, 9695)),
	MAZE_SW_TO_W(
			TunnelDoors.getDoor(3535, 9688),
			TunnelDoors.getDoor(3534, 9688),
			TunnelDoors.getDoor(3534, 9684),
			TunnelDoors.getDoor(3535, 9684)),
	HIGHWAY_SE_TO_SW(
			TunnelDoors.getDoor(3535, 9671),
			TunnelDoors.getDoor(3534, 9671),
			TunnelDoors.getDoor(3569, 9671),
			TunnelDoors.getDoor(3568, 9671)),
	MAZE_N_TO_NE(
			TunnelDoors.getDoor(3558, 9712),
			TunnelDoors.getDoor(3558, 9711),
			TunnelDoors.getDoor(3562, 9711),
			TunnelDoors.getDoor(3562, 9712)),
	MAZE_NW_TO_N(
			TunnelDoors.getDoor(3541, 9712),
			TunnelDoors.getDoor(3541, 9711),
			TunnelDoors.getDoor(3545, 9711),
			TunnelDoors.getDoor(3545, 9712)),
	CHEST_MID_TO_S(
			TunnelDoors.getDoor(3552, 9688),
			TunnelDoors.getDoor(3551, 9688),
			TunnelDoors.getDoor(3551, 9684),
			TunnelDoors.getDoor(3552, 9684)),
	MAZE_SE_TO_S(
			TunnelDoors.getDoor(3562, 9677),
			TunnelDoors.getDoor(3558, 9677),
			TunnelDoors.getDoor(3558, 9678),
			TunnelDoors.getDoor(3562, 9678)),
	MAZE_W_TO_NW(
			TunnelDoors.getDoor(3534, 9701),
			TunnelDoors.getDoor(3535, 9701),
			TunnelDoors.getDoor(3535, 9705),
			TunnelDoors.getDoor(3534, 9705)),
	HIGHWAY_NE_TO_SE(
			TunnelDoors.getDoor(3575, 9678),
			TunnelDoors.getDoor(3575, 9677),
			TunnelDoors.getDoor(3575, 9712),
			TunnelDoors.getDoor(3575, 9711)),
	HIGHWAY_NW_TO_NE(
			TunnelDoors.getDoor(3534, 9718),
			TunnelDoors.getDoor(3535, 9718),
			TunnelDoors.getDoor(3568, 9718),
			TunnelDoors.getDoor(3569, 9718)),
	CHEST_MID_TO_N(
			TunnelDoors.getDoor(3552, 9705),
			TunnelDoors.getDoor(3551, 9705),
			TunnelDoors.getDoor(3551, 9701),
			TunnelDoors.getDoor(3552, 9701)),
	MAZE_S_TO_SW(
			TunnelDoors.getDoor(3541, 9678),
			TunnelDoors.getDoor(3541, 9677),
			TunnelDoors.getDoor(3545, 9677),
			TunnelDoors.getDoor(3545, 9678)),
	HIGHWAY_SW_TO_NW(
			TunnelDoors.getDoor(3528, 9677),
			TunnelDoors.getDoor(3528, 9678),
			TunnelDoors.getDoor(3528, 9711),
			TunnelDoors.getDoor(3528, 9712));

	private final TunnelDoor leftDoorOne;
	private final TunnelDoor rightDoorOne;

	private final TunnelDoor leftDoorTwo;
	private final TunnelDoor rightDoorTwo;

	private TunnelHallwayData(TunnelDoor leftDoorOne, TunnelDoor rightDoorOne, TunnelDoor leftDoorTwo,
			TunnelDoor rightDoorTwo) {
		this.leftDoorOne = leftDoorOne;
		this.rightDoorOne = rightDoorOne;
		this.leftDoorTwo = leftDoorTwo;
		this.rightDoorTwo = rightDoorTwo;
	}

	public void closeDoors(Client client) {
		leftDoorOne.closeDoor(client, BarrowsConstants.CLOSED_LEFT_DOOR);
		leftDoorTwo.closeDoor(client, BarrowsConstants.CLOSED_LEFT_DOOR);
		rightDoorOne.closeDoor(client, BarrowsConstants.CLOSED_RIGHT_DOOR);
		rightDoorTwo.closeDoor(client, BarrowsConstants.CLOSED_RIGHT_DOOR);
	}

	public void openDoors(Client client) {
		leftDoorOne.resetDoor(client);
		leftDoorTwo.resetDoor(client);
		rightDoorOne.resetDoor(client);
		rightDoorTwo.resetDoor(client);
	}

}
