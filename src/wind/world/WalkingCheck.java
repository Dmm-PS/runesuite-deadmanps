package wind.world;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

import wind.Config;

public class WalkingCheck {
	public static Map<Integer, Boolean> tiles = new HashMap<Integer, Boolean>();

	public static void load() {
		try {
			File f1 = new File(Config.DATA_LOC + "ClipMap.bin");
			if (f1.exists()) {
				try {
					loadBin();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				System.out.println("ClipMap.bin not Found");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static void loadBin() throws IOException {
		FileChannel channel = new RandomAccessFile(
				Config.DATA_LOC + "ClipMap.bin", "r").getChannel();
		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		int size = buffer.getInt() - 1;
		int mySize = 0;
		for (int i = 0; i < size; i++) {
			int absX = buffer.getShort();
			int absY = buffer.getShort();
			int height = buffer.get();
			@SuppressWarnings("unused")
			int shift = buffer.getInt();
			tiles.put(height << 28 | absX << 14 | absY, true);
			mySize++;
		}
		System.out.println(Config.SYSTEM_NAME + "Loaded: " + mySize
				+ " clipped tiles.");
		channel.close();
		channel = null;
		buffer = null;
	}
}
