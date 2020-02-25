package org.appproductions.config;

import java.util.HashMap;
import java.util.Map;

public class Options {
	
	private static Map<String, Object> options=new HashMap<String, Object>();
	
	public static void init() {
		addOption("Max FPS", ConfigManager.getSetting("maxFps", int.class));
		addOption("Width", ConfigManager.getSetting("width", int.class));
		addOption("Height", ConfigManager.getSetting("height", int.class));
		addOption("Fov", ConfigManager.getSetting("fov", float.class));
		addOption("Anisotropic", ConfigManager.getSetting("anisotropic", boolean.class));
		addOption("Antialiasing", ConfigManager.getSetting("antialiasing", boolean.class));
		addOption("Post Processing", ConfigManager.getSetting("postprocessing", boolean.class));
		addOption("Show FPS", ConfigManager.getSetting("showFps", boolean.class));
		addOption("Show Delta", ConfigManager.getSetting("showDelta", boolean.class));
		addOption("Disable Culling", ConfigManager.getSetting("disableCulling", boolean.class));

		addOption("Forward Key", ConfigManager.getKeybind("forward"));
		addOption("Backward Key", ConfigManager.getKeybind("backward"));
		addOption("Left Key", ConfigManager.getKeybind("left"));
		addOption("Right Key", ConfigManager.getKeybind("right"));
		addOption("Mouse Lock Key", ConfigManager.getKeybind("mouselock"));
	}
	
	private static void addOption(String key, Object value) {
		options.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getOption(String key) {
		if(options.containsKey(key)) {
			Object value=options.get(key);
			return (T) value;
		}
		return null;
	}
}
