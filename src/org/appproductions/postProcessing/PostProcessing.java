package org.appproductions.postProcessing;

import org.appproductions.gaussianBlur.HorizontalBlur;
import org.appproductions.gaussianBlur.VerticalBlur;
import org.appproductions.models.RawModel;
import org.appproductions.rendererEngine.DisplayManager;
import org.appproductions.rendererEngine.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;

	public static void init() {
		quad = Loader.loadToVAO(POSITIONS, 2);
		contrastChanger=new ContrastChanger();
		hBlur=new HorizontalBlur(DisplayManager.getWidth(), DisplayManager.getHeight());
		vBlur=new VerticalBlur(DisplayManager.getWidth(), DisplayManager.getHeight());
	}

	public static void doPostProcessing(int colourTexture) {
		start();
		
		//hBlur.render(colourTexture);
		//vBlur.render(hBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		
		end();
	}

	public static void cleanUp() {
		hBlur.cleanUp();
		vBlur.cleanUp();
		contrastChanger.cleanUp();
	}

	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
