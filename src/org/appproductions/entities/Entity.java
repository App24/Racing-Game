package org.appproductions.entities;

import org.appproductions.models.TexturedModel;
import org.joml.Vector3f;

public class Entity {
	
	protected Vector3f position;
	protected float rotX, rotY, rotZ;
	protected float scale;
	private TexturedModel model;
	private int textureIndex=0;
	private boolean insideFrustum=true;
	private int id;
	private static int CURRENT_ID=0;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.position=position;
		this.rotX=rotX;
		this.rotY=rotY;
		this.rotZ=rotZ;
		this.scale=scale;
		this.model=model;
		id=CURRENT_ID;
		CURRENT_ID++;
	}
	
	public void setPosition(Vector3f position) {
		this.position=position;
	}
	
	public void setRotation(float rotX, float rotY, float rotZ) {
		this.rotX=rotX;
		this.rotY=rotY;
		this.rotZ=rotZ;
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureIndex) {
		this.position=position;
		this.rotX=rotX;
		this.rotY=rotY;
		this.rotZ=rotZ;
		this.scale=scale;
		this.model=model;
		this.textureIndex=textureIndex;
		id=CURRENT_ID;
		CURRENT_ID++;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		rotX+=dx;
		rotY+=dy;
		rotZ+=dz;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		position.x+=dx;
		position.y+=dy;
		position.z+=dz;
	}

	public float getTextureXOffset() {
		int column=textureIndex%model.getTexture().getNumberOfRows();
		return (float) column/(float)model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row=textureIndex/model.getTexture().getNumberOfRows();
		return (float) row/(float)model.getTexture().getNumberOfRows();
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}
	
	public void setInsideFrustum(boolean inside) {
		this.insideFrustum=inside;
	}
	
	public boolean IsinsideFrustum() {
		return insideFrustum;
	}
	
	public int getID() {
		return id;
	}
	
}
