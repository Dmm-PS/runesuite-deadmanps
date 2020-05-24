package shamon.barrows.data;

import static shamon.barrows.data.BarrowsConstants.AHRIMS_MOUND_TOP;
import static shamon.barrows.data.BarrowsConstants.AHRIMS_STAIRS_POSITION;
import static shamon.barrows.data.BarrowsConstants.DHAROKS_MOUND_TOP;
import static shamon.barrows.data.BarrowsConstants.DHAROKS_STAIRS_POSITION;
import static shamon.barrows.data.BarrowsConstants.GUTHANS_MOUND_TOP;
import static shamon.barrows.data.BarrowsConstants.GUTHANS_STAIRS_POSITION;
import static shamon.barrows.data.BarrowsConstants.KARILS_MOUND_TOP;
import static shamon.barrows.data.BarrowsConstants.KARILS_STAIRS_POSITION;
import static shamon.barrows.data.BarrowsConstants.TORAGS_MOUND_TOP;
import static shamon.barrows.data.BarrowsConstants.TORAGS_STAIRS_POSITION;
import static shamon.barrows.data.BarrowsConstants.VERACS_MOUND_TOP;
import static shamon.barrows.data.BarrowsConstants.VERACS_STAIRS_POSITION;
import shamon.barrows.Barrows;
import shamon.region.Position;
import wind.model.players.Client;

public enum MoundData {

	AHRIM(AHRIMS_MOUND_TOP, AHRIMS_STAIRS_POSITION),
	DHAROK(DHAROKS_MOUND_TOP, DHAROKS_STAIRS_POSITION),
	GUTHAN(GUTHANS_MOUND_TOP, GUTHANS_STAIRS_POSITION),
	KARIL(KARILS_MOUND_TOP, KARILS_STAIRS_POSITION),
	TORAG(TORAGS_MOUND_TOP, TORAGS_STAIRS_POSITION),
	VERAC(VERACS_MOUND_TOP, VERACS_STAIRS_POSITION);

	private static final int MOUND_RADIUS = 3;
	
	/**
	 * Center of the mound above ground.
	 */
	private final Position center;

	/**
	 * The bottom position of the stairs where the player can stand.
	 */
	private final Position stairsBottom;

	private MoundData(Position center, Position below) {
		this.center = center;
		this.stairsBottom = below;
	}

	public static final MoundData[] MOUNDS = MoundData.values();

	public void teleportBelow(Client client) {
		Barrows barrows = client.getBarrows();

		if (barrows.getChest().hasLooted()) {
			barrows.resetGame();
		}
		client.teleportToX = stairsBottom.getX();
		client.teleportToY = stairsBottom.getY();
		client.heightLevel = stairsBottom.getHeight();
		client.sendMessage(BarrowsConstants.BARROWS_CRYPT_MESSAGE);
	}

	public boolean checkBounds(Client client) {
		return (client.absX >= (center.getX() - MOUND_RADIUS) && client.absX <= (center.getX() + MOUND_RADIUS))
				&& (client.absY >= (center.getY() - MOUND_RADIUS) && client.absY <= (center.getY() + MOUND_RADIUS));
	}

	public Position getCenter() {
		return center;
	}

	public Position getStairsBottom() {
		return stairsBottom;
	}
}
