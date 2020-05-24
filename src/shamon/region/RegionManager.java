package shamon.region;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the world regions.
 * 
 * @author Graham Edgecombe
 *
 */
public class RegionManager {
	/**
	 * The size of the region.
	 */
	private static final int REGION_SIZE = 32; // Only loads objects in the region, if you want objects passed the "loading please wait" change to 64.

	/**
	 * The map of regions based on it's position.
	 */
	private static final Map<Position, Region> REGIONS = new HashMap<>();

	/**
	 * Get the surrounding regions based on a position.
	 * The center chunk is at (1, 1) or index 4.
	 * 
	 * @param position
	 *            The position to focus on.
	 * @return an array of regions.
	 */
	public static Region[] getSurroundingRegions(Position position) {
		int regionX = position.getX() / REGION_SIZE;
		int regionY = position.getY() / REGION_SIZE;
		int regionZ = position.getHeight();

		// Reorganized this so that the index relates to the position.
		// index = ((x + 1) + ((y + 1) * 3)
		Region[] surrounding = new Region[9];

		// Top Row
		surrounding[0] = getRegion(regionX - 1, regionY + 1, regionZ);
		surrounding[1] = getRegion(regionX, regionY + 1, regionZ);
		surrounding[2] = getRegion(regionX + 1, regionY + 1, regionZ);

		// Middle Row
		surrounding[3] = getRegion(regionX - 1, regionY, regionZ);
		surrounding[4] = getRegion(regionX, regionY, regionZ); // Center Chunk
		surrounding[5] = getRegion(regionX + 1, regionY, regionZ);

		// Bottom Row
		surrounding[6] = getRegion(regionX - 1, regionY - 1, regionZ);
		surrounding[7] = getRegion(regionX, regionY - 1, regionZ);
		surrounding[8] = getRegion(regionX + 1, regionY - 1, regionZ);
		return surrounding;
	}

	/**
	 * Get a region by its world position.
	 * 
	 * @param position
	 *            The position to focus on.
	 * @return a region.
	 */
	public static Region getRegionByLocation(Position position) {
		return getRegionByLocation(position.getX(), position.getY(), position.getHeight());
	}

	/**
	 * Get a region by its x, y, and z world coordinates.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The z coordinate.
	 * @return a region.
	 */
	public static Region getRegionByLocation(int x, int y, int z) {
		return getRegion(x / REGION_SIZE, y / REGION_SIZE, z);
	}

	/**
	 * Get a region by its x, y, z, region coordinates.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The z coordinate.
	 * @return a region.
	 */
	public static Region getRegion(int x, int y, int height) {
		Position coordinates = new Position(x, y, height);
		REGIONS.putIfAbsent(coordinates, new Region(coordinates));
		return REGIONS.get(coordinates);
	}

	/**
	 * Get a static object by it's position.
	 * 
	 * @param position
	 *            The position to focus on.
	 * @return a static object.
	 */
	public static Optional<GameObject> getStaticObject(Position position) {
		Region region = getRegionByLocation(position);
		return region.getStaticObject(position);
	}

	/**
	 * Get a dynamic object by it's position.
	 * 
	 * @param position
	 *            The position to focus on.
	 * @return a dynamic object.
	 */
	public static Optional<GameObject> getDynamicObject(Position position) {
		Region region = getRegionByLocation(position);
		return region.getDynamicObject(position);
	}

}
