package wind.model.npcs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import wind.Config;
import wind.model.items.drop.Drop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class NPCDrops {

	/**
	 * The directory/location of drops.json
	 */
	private static final File DROP_DIR = new File(
			Config.DATA_LOC + "json/npcdrops.json");

	/**
	 * Our single instance of Gson
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.create();

	private static HashMap<Integer, Drop[]> npcDrops;

	public static final void init() {
		load();
	}

	public static Drop[] getDrops(int npcId) {
		return npcDrops.get(npcId);
	}

	private Map<Integer, ArrayList<Drop>> dropMapx = null;

	public Map<Integer, ArrayList<Drop>> getDropArray() {

		if (dropMapx == null)
			dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		for (int i : npcDrops.keySet()) {
			int npcId = i;
			ArrayList<Drop> temp = new ArrayList<Drop>();
			for (Drop mainDrop : npcDrops.get(npcId)) {
				temp.add(mainDrop);
			}
			dropMapx.put(i, temp);
		}

		return dropMapx;
	}

	long endTime = System.currentTimeMillis();

	private long startTime;
	long elapsed = endTime - startTime;

	private static void load() {
		final long start = System.currentTimeMillis();
		try {
			Type t = new TypeToken<HashMap<Integer, Drop[]>>() {
			}.getType();
			npcDrops = GSON.fromJson(new FileReader(DROP_DIR), t);
			System.out.println(Config.SYSTEM_NAME + "Loaded: " + npcDrops.size()
					+ " NPC Drops in " + (System.currentTimeMillis() - start)
					+ "ms.");
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<Integer, Drop[]> getDropMap() {
		return npcDrops;
	}
}