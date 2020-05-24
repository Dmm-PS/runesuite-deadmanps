package org.apollo.cache.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.archive.Archive;
import org.apollo.util.BufferUtil;
import org.apollo.util.CompressionUtil;

import shamon.region.GameObject;
import shamon.region.Position;
import shamon.region.Region;
import shamon.region.RegionManager;
import shamon.region.Rotation;

/**
 * A class which parses the placement information of in-game objects on the map.
 * 
 * @author Chris Fletcher
 */
public final class StaticObjectParser {

	private static int count = 0;

	/**
	 * The indexed file system.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates a new static object parser.
	 * 
	 * @param fs
	 *            The indexed file system.
	 */
	public StaticObjectParser(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Parses all static objects and places them in the returned array.
	 * 
	 * @return The parsed objects.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void parse() throws IOException {
		Archive versionList = Archive.decode(fs.getFile(0, 5));
		ByteBuffer buffer = versionList.getEntry("map_index").getBuffer();

		int indices = buffer.getShort() & 0xFFFF;
		int[] areas = new int[indices];
		int[] landscapes = new int[indices];

		for (int i = 0; i < indices; i++) {
			areas[i] = buffer.getShort() & 0xFFFF;

			buffer.getShort();

			landscapes[i] = buffer.getShort() & 0xFFFF;

			//@SuppressWarnings("unused")
			//boolean members = (buffer.get() & 0xFF) == 1;
		}

		for (int i = 0; i < indices; i++) {
			ByteBuffer compressed = fs.getFile(4, landscapes[i]);
			ByteBuffer uncompressed = ByteBuffer.wrap(CompressionUtil.degzip(compressed));

			parseArea(areas[i], uncompressed);
		}

		System.out.println("Loaded " + count + " objects.");

		/*int count2 = 0;
		for (Region region : RegionManager.getRegions().values()) {
			count2 += region.getStaticObjects().size();
		}
		System.out.println("Loaded " + count2 + " objects.");*/
	}

	/**
	 * Parses a single area from the specified buffer.
	 * 
	 * @param area
	 *            The identifier of that area.
	 * @param buffer
	 *            The buffer which holds the area's data.
	 * @return A collection of all parsed objects.
	 */
	private void parseArea(int area, ByteBuffer buffer) {

		int regionX = ((area >> 8) & 0xFF);
		int regionY = (area & 0xFF);

		int x = regionX << 6;
		int y = regionY << 6;

		int id = -1;
		int idOffset;

		while ((idOffset = BufferUtil.readSmart(buffer)) != 0) {
			id += idOffset;

			int pos = 0;
			int positionOffset;

			while ((positionOffset = BufferUtil.readSmart(buffer)) != 0) {
				pos += positionOffset - 1;

				int localX = pos >> 6 & 0x3F;
				int localY = pos & 0x3F;
				int height = pos >> 12;

				int info = buffer.get() & 0xFF;
				int type = info >> 2;
				int rotation = info & 3;

				Position position = new Position(x + localX, y + localY, height);
				GameObject object = new GameObject(id, position, type, Rotation.getRotation(rotation));

				Region region = RegionManager.getRegionByLocation(position);
				region.registerStaticObject(object);
				count++;
			}
		}

	}
}