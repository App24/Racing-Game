package org.appproductions.audio;

import java.util.ArrayList;
import java.util.List;

public class AudioManager {
	
	private static List<Source> sources=new ArrayList<Source>();
	
	public static void addSource(Source source) {
		sources.add(source);
	}
	
	public static void cleanUp() {
		for(Source source:sources) {
			source.delete();
		}
	}
	
}
