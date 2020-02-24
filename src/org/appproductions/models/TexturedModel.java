package org.appproductions.models;

import org.appproductions.textures.ModelTexture;

public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;
	private float boundingRadius=0;
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel=model;
		this.texture=texture;
	}
	
	public RawModel getRawModel() {
		return rawModel;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
	
	public float getBoundingRadius() {
		return boundingRadius;
	}
	
	public void setBoundingRadius(float boundingRadius) {
		this.boundingRadius=boundingRadius;
	}
	
}
