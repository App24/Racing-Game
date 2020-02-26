package org.appproductions.cache;

import java.util.HashMap;
import java.util.Map;

import org.appproductions.models.TexturedModel;

public class CachedModels {

	private static Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
	private static Map<Integer, TexturedModel> models = new HashMap<Integer, TexturedModel>();

	public static void addTexturedModel(int id, TexturedModel model) {
		if (!models.keySet().contains(id))
			models.put(id, model);
	}
	
	public static void addModel() {
		
	}

	public static TexturedModel getTexturedModel(int id) {
		addModelToCache(id);
		return models.get(id);
	}

	private static void addModelToCache(int id) {
		if (cache.containsKey(id)) {
			int used = cache.get(id);
			used+=1;
			cache.replace(id, used);
		} else {
			cache.put(id, 1);
		}
	}

	public static void unbindTexturedModel(int id) {
		removeModelFromCache(id);
	}

	private static void removeModelFromCache(int id) {
		if (cache.containsKey(id)) {
			TexturedModel model = models.get(id);
			Map<Integer, TexturedModel> batch = new HashMap<Integer, TexturedModel>();
			batch.put(id, model);
			int used = cache.get(id);
			if (used > 1) {
				used-=1;
				cache.replace(id, used);
			} else {
				cache.remove(id);
			}
		}
	}

}
