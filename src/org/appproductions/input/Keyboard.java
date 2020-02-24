package org.appproductions.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard {

	private static GLFWKeyCallback keyCallback;
	private static boolean keys[] = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean keysPressed[] = new boolean[GLFW.GLFW_KEY_LAST];

	private static boolean locked = false;

	public static void createKeyboard() {
		keyCallback = new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mode) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
	}

	public static boolean isKeyPressed(int key) {
		if (!locked) {
			if (keys[key] && !keysPressed[key]) {
				keysPressed[key] = true;
				return true;
			} else if (!keys[key]) {
				keysPressed[key] = false;
			}
		}
		return false;
	}

	public static boolean isKeyDown(int key) {
		if (!locked)
			return keys[key];
		return false;
	}

	public static GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	public static void cleanUp() {
		keyCallback.free();
	}

	public static void setBlocked(boolean locked) {
		Keyboard.locked = locked;
	}

	public static boolean isBlocked() {
		return locked;
	}
}
