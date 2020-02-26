package org.appproductions.rendererEngine;

import org.appproductions.config.Options;
import org.appproductions.input.Keyboard;
import org.appproductions.input.Mouse;
import org.appproductions.toolbox.Sync;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class DisplayManager {

	private static long window;
	private static int width, height;
	private static int fps_cap;
	private static int current_max_fps;
	private static long lastTimeFrame;
	private static float delta;
	private static int frames;
	private static int fps;
	private static long time;
	private static String title;
	private static final String TITLE = "Racing Game";

	public static void createDisplay() {
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to Intialize GLFW");
		}

		width = Options.getOption("Width");
		height = Options.getOption("Height");

		fps_cap = Options.getOption("Max FPS");
		current_max_fps=fps_cap;

		boolean antialiasing = Options.getOption("Antialiasing");

		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		if (antialiasing) {
			GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);
		}
		window = GLFW.glfwCreateWindow(width, height, TITLE, 0, 0);

		if (window == 0) {
			throw new AssertionError("Failed to create the GLFW window");
		}

		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

		Image icon = Loader.loadImage("icon");
		GLFWImage iconImage = GLFWImage.malloc();
		GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
		iconImage.set(icon.getWidth(), icon.getHeight(), icon.getImage());
		iconBuffer.put(0, iconImage);
		GLFW.glfwSetWindowIcon(window, iconBuffer);

		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glViewport(0, 0, width, height);
		if (antialiasing)
			GL11.glEnable(GL13.GL_MULTISAMPLE);

		Mouse.createMouse();
		Keyboard.createKeyboard();

		loadCallbacks();

		GLFW.glfwShowWindow(window);
		GLFW.glfwSwapInterval(0);

		lastTimeFrame = System.currentTimeMillis();
	}

	private static void loadCallbacks() {
		GLFW.glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallbackI() {

			@Override
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
			}
		});
		
		GLFW.glfwSetWindowFocusCallback(window, new GLFWWindowFocusCallbackI() {
			
			@Override
			public void invoke(long window, boolean focus) {
				if(focus) {
					current_max_fps=fps_cap;
				}else {
					current_max_fps=10;
				}
				
			}
		});

		GLFW.glfwSetCursorPosCallback(window, Mouse.getCursorCallback());
		GLFW.glfwSetKeyCallback(window, Keyboard.getKeyCallback());
		GLFW.glfwSetMouseButtonCallback(window, Mouse.getMouseCallback());
		GLFW.glfwSetScrollCallback(window, Mouse.getScrollCallback());
	}

	public static void updateDisplay() {
		GLFW.glfwPollEvents();
		GLFW.glfwSwapBuffers(window);
		Sync.sync(current_max_fps);
		long currentTimeFrame = System.currentTimeMillis();
		delta = (currentTimeFrame - lastTimeFrame) / 1000f;
		lastTimeFrame = currentTimeFrame;
		title = TITLE;
		if ((boolean) Options.getOption("Show FPS")) {
			if (System.currentTimeMillis() > time + 1000) {
				time = System.currentTimeMillis();
				fps = frames;
				frames = 0;
			}
			frames++;
			title += " | FPS: " + fps;
		}
		if ((boolean) Options.getOption("Show Delta")) {
			title += " | Delta: " + delta;
		}
		if ((boolean) Options.getOption("Disable Culling")) {
			title += " | Culling: false";
		}
		GLFW.glfwSetWindowTitle(window, title);
	}

	public static boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public static int getFPS() {
		return fps;
	}

	public static void close() {
		Mouse.cleanUp();
		Keyboard.cleanUp();
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public static float getDelta() {
		return delta;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static long getWindow() {
		return window;
	}

}
