package shamon.region;

import org.apollo.cache.def.ObjectDefinition;

public class GameObject {
	/**
	 * The object's id.
	 */
	private final int id;

	/**
	 * The object's position.
	 */
	private final Position position;

	/**
	 * The object type.
	 */
	private final int type;

	/**
	 * The object's rotation.
	 */
	private final Rotation rotation;

	/**
	 * Creates a new static object.
	 * 
	 * @param def
	 *            The object's id.
	 * @param position
	 *            The position.
	 * @param type
	 *            The type code of the object.
	 * @param rotation
	 *            The rotation of the object.
	 */
	public GameObject(int id, Position position, int type, Rotation rotation) {
		this.id = id;
		this.position = position;
		this.type = type;
		this.rotation = rotation;
	}

	/**
	 * The definition of the object for its id.
	 * 
	 * @return the objects definition.
	 */
	public ObjectDefinition getDefinition() {
		return ObjectDefinition.lookup(id);
	}

	/**
	 * Gets the id of the object.
	 * 
	 * @return The object id.
	 */
	public int getID() {
		return id;
	}

	/**
	 * The position of the object.
	 * 
	 * @return the position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the type code of the object.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the object's rotation.
	 * 
	 * @return The rotation.
	 */
	public Rotation getRotation() {
		return rotation;
	}

	
}
