package org.appproductions.guis;

import org.appproductions.rendererEngine.Image;

public class GuiTexture {
	private int textureID;
	private int width, height;

	public GuiTexture(int textureID, Image image) {
		this.textureID=textureID;
		this.width=image.getWidth();
		this.height=image.getHeight();
	}
	
	public GuiTexture(int textureID) {
		this.textureID=textureID;
		width=1;
		height=1;
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
