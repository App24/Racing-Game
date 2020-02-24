package org.appproductions.utils;

import java.io.File;
import java.io.IOException;

import org.ini4j.Profile.Section;
import org.ini4j.Wini;

public class Config {
	
	private Wini ini;
	
	private Config(Wini ini) {
		this.ini=ini;
	}
	
	public static Config setConfig(String file) {
		Wini ini = null;
		try {
			ini = new Wini(new File("Config/"+file+".ini"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Config(ini);
	}
	
	public boolean allActive() {
		for(Section section:ini.values()) {
			for(String key:section.keySet()) {
				if(!section.get(key,boolean.class))
					return false;
			}
		}
		return true;
	}
	
	public boolean anyActive() {
		for(Section section:ini.values()) {
			for(String key:section.keySet()) {
				if(section.get(key,boolean.class))
					return true;
			}
		}
		return false;
	}
	
	public <T> T get(String sectionName, String optionName, Class<T> clazz) {
		return ini.get(sectionName, optionName, clazz);
	}
	
}
