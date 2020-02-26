package org.appproductions.guis;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
	
	private static List<GUI> guis=new ArrayList<GUI>();
	
	public static void addGUI(GUI gui) {
		guis.add(gui);
	}
	
	public static List<GUI> getGUIs(){
		return guis;
	}
	
}
