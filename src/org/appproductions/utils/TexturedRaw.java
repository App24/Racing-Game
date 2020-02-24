package org.appproductions.utils;

public class TexturedRaw {
	private String modelFile;
	private String textureFile;
	
	public TexturedRaw(String modelFile, String texturedFile) {
		this.modelFile=modelFile;
		this.textureFile=texturedFile;
	}
	
	public String getModelFile() {
		return modelFile;
	}
	
	public String getTextureFile() {
		return textureFile;
	}
}
