package org.appproductions.entities;

import java.util.List;

import org.appproductions.input.Keyboard;
import org.appproductions.models.TexturedModel;
import org.appproductions.rendererEngine.DisplayManager;
import org.appproductions.terrains.Terrain;
import org.appproductions.utils.Maths;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {

	private static final float SPEED = 80;
	private static final float TURN_SPEED = 160;
	private static final float ACCELERATION = 2;
	private static final float GRAVITY = -100;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean inAir = false;
	// private int buffer;
	// private Source source=new Source();

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		/*
		 * buffer=AudioMaster.loadSound("Audio/bounce"); source.setPosition(position.x,
		 * position.y, position.z); source.setVolume(100); source.play(buffer);
		 * source.pause(); AudioManager.addSource(source);
		 */
	}
	
	public void reset() {
		position=new Vector3f();
		rotX=0;
		rotY=0;
		rotZ=0;
		currentSpeed=0;
		currentTurnSpeed=0;
	}

	public void move() {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
		float distance = currentSpeed * DisplayManager.getDelta();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		// source.setPosition(getPosition().x, getPosition().y, getPosition().z+1);
	}

	public void move(List<Terrain> terrains) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
		float distance = currentSpeed * DisplayManager.getDelta();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getDelta();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getDelta(), 0);
		// source.setPosition(getPosition().x, getPosition().y, getPosition().z+1);
		Terrain terrain = null;
		Terrain terrainFront = null;
		Terrain terrainBack = null;
		for (Terrain t : terrains) {
			if (t.isInsideTerrain(super.getPosition().x, super.getPosition().z)) {
				terrain = t;
			}
			if (t.isInsideTerrain(super.getPosition().x + (2f * (float) Math.sin(Math.toRadians(rotY))),
					super.getPosition().z + (2f * (float) Math.cos(Math.toRadians(rotY))))) {
				terrainFront = t;
			}
			if (t.isInsideTerrain(super.getPosition().x - (2f * (float) Math.sin(Math.toRadians(rotY))),
					super.getPosition().z - (2f * (float) Math.cos(Math.toRadians(rotY))))) {
				terrainBack = t;
			}
		}
		float terrainHeight = 0;
		float terrainDifference = 0;
		if (terrain != null) {
			float terrainFrontHeight = 0;
			float terrainBackHeight = 0;
			if (terrainFront != null)
				terrainFrontHeight = terrainFront.getHeightOfTerrain(
						super.getPosition().x + (2f * (float) Math.sin(Math.toRadians(rotY))),
						super.getPosition().z + (2f * (float) Math.cos(Math.toRadians(rotY))));
			if (terrainBack != null)
				terrainBackHeight = terrainBack.getHeightOfTerrain(
						super.getPosition().x - (2f * (float) Math.sin(Math.toRadians(rotY))),
						super.getPosition().z - (2f * (float) Math.cos(Math.toRadians(rotY))));
			terrainDifference = terrainFrontHeight - terrainBackHeight;

			terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		}
		if (!inAir) {
			rotX = 360;
			if (Math.abs(Math.cos(Math.toRadians(rotY))) > Math.abs(Math.sin(Math.toRadians(rotY))))
				rotX *= (((float) Math.sin(Math.toRadians(terrainDifference * Math.cos(Math.toRadians(rotY))))))
						* -Math.cos(Math.toRadians(rotY));
			else
				rotX *= (((float) Math.sin(Math.toRadians(terrainDifference * Math.sin(Math.toRadians(rotY))))))
						* -Math.sin(Math.toRadians(rotY));
		}
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
			inAir = false;
		} else {
			inAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			// if(!source.isPlaying())
			// source.continuePlaying();
			currentSpeed += ACCELERATION;
		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			// if(!source.isPlaying())
			// source.continuePlaying();
			currentSpeed -= ACCELERATION;
		} else {
			currentSpeed = Maths.lerp(currentSpeed, 0, 5f * DisplayManager.getDelta());
			// source.pause();
		}

		currentSpeed = Maths.clamp(currentSpeed, -SPEED, SPEED);

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A))
			currentTurnSpeed = TURN_SPEED * currentSpeed / SPEED;
		else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D))
			currentTurnSpeed = -TURN_SPEED * currentSpeed / SPEED;
		else {
			currentTurnSpeed = 0;
		}

		currentTurnSpeed = Maths.clamp(currentTurnSpeed, -TURN_SPEED, TURN_SPEED);
	}

}
