package shamon.region;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public final class GameObjectParser {

	/**
	 * Load objects from a JSONn file.
	 */
	public static void loadObjects() {
		try {
			Gson gson = new Gson();

			GameObject[] objects = gson.fromJson(new FileReader("./data/objects.json"), GameObject[].class);

			for (GameObject object : objects) {
				GameObjectManager.placeGlobalObject(object);
			}
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
