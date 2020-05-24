package shamon.region;

import com.google.gson.annotations.SerializedName;

public enum Rotation {

	@SerializedName("EAST") EAST(0),
	@SerializedName("NORTH") NORTH(1),
	@SerializedName("WEST") WEST(2),
	@SerializedName("SOUTH") SOUTH(3);

	private final int rotation;

	private static final Rotation[] ROTATIONS = Rotation.values();

	private Rotation(int rotation) {
		this.rotation = rotation;
	}

	public int offset() {
		return rotation;
	}

	public static Rotation getRotation(int index) {
		return ROTATIONS[index];
	}

}
