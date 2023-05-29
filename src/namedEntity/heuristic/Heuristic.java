package namedEntity.heuristic;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Heuristic implements Serializable {

	private static Map<String, String[]> categoryMap = new HashMap<>();

	/* Carga data en categoryMap */
	private static void loadCategoryMapFromJSON(String path) {
		try {
			String jsonStr = new String(Files.readAllBytes(Paths.get(path)));
			JSONObject jsonObj = new JSONObject(jsonStr);

			for (String key : jsonObj.keySet()) {
				JSONArray arr = jsonObj.getJSONArray(key);
				String[] values = new String[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					values[i] = arr.getString(i);
				}
				categoryMap.put(key, values);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	public static String getCategory(String entity) {
		String[] category = categoryMap.get(entity);

		if (category != null) {
			return category[0];
		}

		return "Other";
	}

	public static String getTopic(String entity) {
		String[] category = categoryMap.get(entity);

		if (category != null) {
			return category[1];
		}

		return "Other";
	}

	public abstract boolean isEntity(String word);

	/* Constructor */
	public Heuristic() {

		/* carga data en categoryMap desde un JSON */
		if (categoryMap.isEmpty())
			loadCategoryMapFromJSON("config/dictionary.json");
	}

}
