package org.appproductions.input;

import org.appproductions.rendererEngine.DisplayManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Mouse {

	private static boolean buttons[] = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	private static boolean buttonsPressed[] = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	private static double mouseX, mouseY, mouseDX, mouseDY;
	private static double scrollX, scrollY;

	private static GLFWMouseButtonCallback mouseCallback;
	private static GLFWCursorPosCallback cursorCallback;
	private static GLFWScrollCallback scrollCallback;
	private static boolean locked = false, grabbed=false;

	public static void createMouse() {
		mouseCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mode) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};

		cursorCallback = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long window, double x, double y) {
				mouseDX=x-mouseX;
				mouseDY=y-mouseY;
				mouseX = x;
				mouseY = y;
			}
		};

		scrollCallback = new GLFWScrollCallback() {

			@Override
			public void invoke(long window, double x, double y) {
				scrollX = x;
				scrollY = y;
			}
		};
	}

	public static boolean isButtonDown(int key) {
		return buttons[key];
	}

	public static void setBlocked(boolean locked) {
		Mouse.locked = locked;
	}

	public static boolean isBlocked() {
		return locked;
	}
	
	public static boolean isGrabbed() {
		return grabbed;
	}

	public static boolean isButtonPressed(int button) {
		if (!locked) {
			if (buttons[button] && !buttonsPressed[button]) {
				buttonsPressed[button] = true;
				return true;
			} else if (!buttons[button]) {
				buttonsPressed[button] = false;
			}
		}
		return false;
	}

	public static void cleanUp() {
		mouseCallback.free();
		scrollCallback.free();
		cursorCallback.free();
	}

	public static Vector2f getMousePos() {
		if (!locked)
			return new Vector2f((float) mouseX, (float) mouseY);
		return new Vector2f();
	}
	
	public static float getX() {
		return (float) mouseX;
	}
	
	public static float getY() {
		return (float) mouseY;
	}
	
	public static int getDX() {
		double tmp=mouseDX;
		mouseDX=0;
		return (int) tmp;
	}
	
	public static int getDY() {
		double tmp=mouseDY;
		mouseDY=0;
		return (int) tmp;
	}
	
	public static int getScrollDY() {
		double tmp=scrollY;
		scrollY=0;
		return (int) tmp;
	}

	public static Vector2f getScrollPos() {
		if (!locked)
			return new Vector2f((float) scrollX, (float) scrollY);
		return new Vector2f();
	}

	public static GLFWMouseButtonCallback getMouseCallback() {
		return mouseCallback;
	}

	public static GLFWCursorPosCallback getCursorCallback() {
		return cursorCallback;
	}

	public static GLFWScrollCallback getScrollCallback() {
		return scrollCallback;
	}
	
	public static void setGrabbed(boolean grabbed) {
		GLFW.glfwSetInputMode(DisplayManager.getWindow(), GLFW.GLFW_CURSOR, grabbed?GLFW.GLFW_CURSOR_DISABLED:GLFW.GLFW_CURSOR_NORMAL);
		if(!grabbed)
			GLFW.glfwSetCursorPos(DisplayManager.getWindow(), DisplayManager.getWidth()/2, DisplayManager.getHeight()/2);
		Mouse.grabbed=grabbed;
	}
	
	public static void setMousePosition(Vector2f pos) {
		GLFW.glfwSetCursorPos(DisplayManager.getWindow(), pos.x, pos.y);
	}
}
