package org.appproductions.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.spi.BeanTool;

public class ConfigManager {
	
	private static Map<String, String> options=new HashMap<String, String>();
	
	public static void init() {
		File file=new File("options.txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
				setDefaults();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		readFile();
		Options.init();
		KeyBinds.init();
	}
	
	private static void readFile() {
		File file=new File("options.txt");
		try {
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String line;
			while((line=reader.readLine())!=null) {
				String[] lineComponent=line.split(":");
				String key=lineComponent[0];
				String value=lineComponent[1];
				options.put(key, value);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> T getSetting(String key, Class<T> clazz) {
		if(options.containsKey(key)) {
			String value=options.get(key);
			return BeanTool.getInstance().parse(value, clazz);
		}
		System.err.println("Could not find key: "+key);
		return BeanTool.getInstance().zero(clazz);
	}
	
	public static String getSetting(String key) {
		if(options.containsKey(key)) {
			String value=options.get(key);
			return value;
		}
		System.err.println("Could not find key: "+key);
		return "";
	}
	
	public static String getKeybind(String key) {
		return getSetting("key."+key).toLowerCase();
	}
	
	private static void setDefaults() {
		try {
			FileWriter writer=new FileWriter(new File("options.txt"));
			writeConfig(writer, "maxFps",120);
			writeConfig(writer, "fov",70);
			writeConfig(writer, "anisotropic",true);
			writeConfig(writer, "antialiasing",true);
			writeConfig(writer, "postprocessing", true);
			writeConfig(writer, "width", 1280);
			writeConfig(writer, "height", 720);
			writeConfig(writer, "showFps", false);
			writeConfig(writer, "showDelta", false);
			writeConfig(writer, "disableCulling", false);
			writeConfig(writer, "key.forward", "w");
			writeConfig(writer, "key.backward", "s");
			writeConfig(writer, "key.left", "a");
			writeConfig(writer, "key.right", "d");
			writeConfig(writer, "key.mouselock", "esc");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static <T> void writeConfig(FileWriter writer, String key, T value) {
		try {
			writer.write(key+":"+value.toString()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
