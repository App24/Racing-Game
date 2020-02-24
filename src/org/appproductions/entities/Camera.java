package org.appproductions.entities;

import org.appproductions.input.Mouse;
import org.joml.Vector3f;

public class Camera {
	private float angleAroundPlayer=0;
	private float distanceFromPlayer=30;
	
	private static final float MAX_ZOOM=20;
	private static final float MIN_ZOOM=80;
	private static final float MAX_PITCH=80;
	private static final float MIN_PITCH=-80;
	
	private Vector3f position=new Vector3f(0,0,0);
	private float pitch=15;
	private float yaw;
	private float roll;
	
	private Player player;
	
	public Camera(Player player) {
		this.player=player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance=calculateHorizontalDistance();
		float verticalDistance=calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		yaw=180-(angleAroundPlayer);
		//AudioMaster.setListenerData(player.getPosition().x, player.getPosition().y, player.getPosition().z);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float vertDistance) {
		float theta = 0+angleAroundPlayer;
		float offsetX=(float) (horizDistance*Math.sin(Math.toRadians(theta)));
		float offsetZ=(float) (horizDistance*Math.cos(Math.toRadians(theta)));
		position.x=player.getPosition().x-offsetX;
		position.z=player.getPosition().z-offsetZ;
		position.y=player.getPosition().y+vertDistance;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculatePitch() {
		if(Mouse.isGrabbed()) {
			float pitchChange=-Mouse.getDY()*0.1f;
			pitch-=pitchChange;
			if(pitch>MAX_PITCH)
				pitch=MAX_PITCH;
			else if(pitch<MIN_PITCH)
				pitch=MIN_PITCH;
		}
	}
	
	private void calculateZoom() {
		float zoomLevel=Mouse.getScrollDY()*1.5f;
		distanceFromPlayer-=zoomLevel;
		if(distanceFromPlayer<MAX_ZOOM)
			distanceFromPlayer=MAX_ZOOM;
		else if(distanceFromPlayer>MIN_ZOOM)
			distanceFromPlayer=MIN_ZOOM;
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isGrabbed()) {
			float angleChange=Mouse.getDX()*0.3f;
			angleAroundPlayer-=angleChange;
			if(angleAroundPlayer<-180)
				angleAroundPlayer=180;
			if(angleAroundPlayer>180)
				angleAroundPlayer=-180;
		}
	}
	
}
