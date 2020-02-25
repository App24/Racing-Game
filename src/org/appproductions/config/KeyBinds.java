package org.appproductions.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

public class KeyBinds {
	
	private static Map<String, Integer> keys=new LinkedHashMap<String, Integer>();
	
	public static void init() {
		addKey("Forward", convertKeyToInt(Options.getOption("Forward Key")));
		addKey("Backward", convertKeyToInt(Options.getOption("Backward Key")));
		addKey("Left", convertKeyToInt(Options.getOption("Left Key")));
		addKey("Right", convertKeyToInt(Options.getOption("Right Key")));
		addKey("Mouse Lock", convertKeyToInt(Options.getOption("Mouse Lock Key")));
	}
	
	public static String keybindsToString() {
		String text="Keybinds:\n";
		for(String key:keys.keySet()) {
			int value=keys.get(key);
			text+=convertIntToKey(value)+"="+key+"\n";
		}
		return text;
	}
	
	private static String convertIntToKey(int key) {
		switch(key){
		case 256:
			return "Escape";
		case -1:
			return "no key";
		default:
			return GLFW.glfwGetKeyName(key, 0).toUpperCase();
		}
	}
	
	private static int convertKeyToInt(String key) {
		switch(key) {
		case "q":
			return GLFW.GLFW_KEY_Q;
		case "w":
			return GLFW.GLFW_KEY_W;
		case "e":
			return GLFW.GLFW_KEY_E;
		case "r":
			return GLFW.GLFW_KEY_R;
		case "t":
			return GLFW.GLFW_KEY_T;
		case "y":
			return GLFW.GLFW_KEY_Y;
		case "u":
			return GLFW.GLFW_KEY_U;
		case "i":
			return GLFW.GLFW_KEY_I;
		case "o":
			return GLFW.GLFW_KEY_O;
		case "p":
			return GLFW.GLFW_KEY_P;
		case "a":
			return GLFW.GLFW_KEY_A;
		case "s":
			return GLFW.GLFW_KEY_S;
		case "d":
			return GLFW.GLFW_KEY_D;
		case "f":
			return GLFW.GLFW_KEY_F;
		case "g":
			return GLFW.GLFW_KEY_G;
		case "h":
			return GLFW.GLFW_KEY_H;
		case "j":
			return GLFW.GLFW_KEY_J;
		case "k":
			return GLFW.GLFW_KEY_K;
		case "l":
			return GLFW.GLFW_KEY_L;
		case "z":
			return GLFW.GLFW_KEY_Z;
		case "x":
			return GLFW.GLFW_KEY_X;
		case "c":
			return GLFW.GLFW_KEY_C;
		case "v":
			return GLFW.GLFW_KEY_V;
		case "b":
			return GLFW.GLFW_KEY_B;
		case "n":
			return GLFW.GLFW_KEY_N;
		case "m":
			return GLFW.GLFW_KEY_M;
		case "esc":
			return GLFW.GLFW_KEY_ESCAPE;
		case "escape":
			return GLFW.GLFW_KEY_ESCAPE;
		case "tab":
			return GLFW.GLFW_KEY_TAB;
		case "lshift":
			return GLFW.GLFW_KEY_LEFT_SHIFT;
		case "lctrl":
			return GLFW.GLFW_KEY_LEFT_CONTROL;
		case "lalt":
			return GLFW.GLFW_KEY_LEFT_ALT;
		case "rshift":
			return GLFW.GLFW_KEY_RIGHT_SHIFT;
		case "rctrl":
			return GLFW.GLFW_KEY_RIGHT_CONTROL;
		case "ralt":
			return GLFW.GLFW_KEY_RIGHT_ALT;
		}
		return GLFW.GLFW_KEY_UNKNOWN;
	}
	
	private static void addKey(String key, int value) {
		keys.put(key, value);
	}
	
	public static int getKey(String key) {
		if(keys.containsKey(key)) {
			return keys.get(key);
		}
		return GLFW.GLFW_KEY_UNKNOWN;
	}
	
}
