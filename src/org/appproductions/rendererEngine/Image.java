package org.appproductions.rendererEngine;

import java.nio.ByteBuffer;

public class Image {
	
	private ByteBuffer image;
	private int width, height;
	
	public Image(ByteBuffer image, int width, int height) {
		this.image=image;
		this.width=width;
		this.height=height;
	}
	
	public ByteBuffer getImage() {
		return image;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
