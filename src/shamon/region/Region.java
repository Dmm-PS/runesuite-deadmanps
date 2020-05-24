package shamon.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Region {

	/**
	 * List of objects contained in the cache for this region.
	 */
	private final List<GameObject> staticObjects = new ArrayList<>();

	/**
	 * List of objects that are custom.
	 */
	private final List<GameObject> dynamicObjects = new ArrayList<>();

	/**
	 * The position of this region.
	 */
	private final Position coordinates;

	/**
	 * Create a new region.
	 * 
	 * @param coordinates
	 *            The position of this region.
	 */
	public Region(Position coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Register a object from the cache.
	 * 
	 * @param object
	 *            The object to register.
	 */
	public void registerStaticObject(GameObject object) {
		staticObjects.add(object);
	}

	/**
	 * Register a custom object.
	 * 
	 * @param object
	 *            The object to register.
	 */
	public void registerDynamicObject(GameObject object) {
		dynamicObjects.add(object);
	}

	/**
	 * Remove a custom object.
	 * 
	 * @param object
	 *            The object to remove.
	 */
	public void unregisterDynamicObject(GameObject object) {
		dynamicObjects.remove(object);
	}

	/**
	 * Get an object from the cache based on it's position.
	 * 
	 * @param position
	 *            The position of the object.
	 * @return the object.
	 */
	public Optional<GameObject> getStaticObject(Position position) {
		return staticObjects.stream().filter(obj -> obj.getPosition().equals(position)).findFirst();
	}

	/**
	 * Get a custom object based on it's position.
	 * 
	 * @param position
	 *            The position of the object.
	 * @return the object.
	 */
	public Optional<GameObject> getDynamicObject(Position position) {
		return dynamicObjects.stream().filter(obj -> obj.getPosition().equals(position)).findFirst();
	}

	/**
	 * @return A list of objects contained in the cache for this region.
	 */
	public List<GameObject> getStaticObjects() {
		return staticObjects;
	}

	/**
	 * @return A list of objects that are custom.
	 */
	public List<GameObject> getDynamicObjects() {
		return dynamicObjects;
	}

	/**
	 * @return The position of this region.
	 */
	public Position getCoordinates() {
		return coordinates;
	}
}
