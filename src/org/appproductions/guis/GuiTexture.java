package org.appproductions.guis;

import org.appproductions.rendererEngine.Image;
import org.appproductions.rendererEngine.Loader;

public class GUITexture {
	private int textureID;
	private int width, height;

	public GUITexture(Image image) {
		this.textureID=Loader.loadTexture(image);
		this.width=image.getWidth();
		this.height=image.getHeight();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getTextureID() {
		return textureID;
	}
}
