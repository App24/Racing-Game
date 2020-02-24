package org.appproductions.textures;

public class ModelTexture {
	
	private int textureID;
	private float shineDamper=1;
	private float reflectivity=0;
	
	private int numberOfRows=1;
	
	private boolean hasTransparency=false;
	private boolean useFakeLighting=false;
	
	public ModelTexture(int textureID) {
		this.textureID=textureID;
	}
	
	public void setShineDamper(float shineDamper) {
		this.shineDamper=shineDamper;
	}
	
	public void setFakeLighting(boolean fakeLighting) {
		this.useFakeLighting=fakeLighting;
	}
	
	public boolean getFakeLighting() {
		return useFakeLighting;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity=reflectivity;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}
	
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
}
