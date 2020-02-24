package org.appproductions.utils;

import java.util.List;

import org.appproductions.entities.Camera;
import org.appproductions.input.Mouse;
import org.appproductions.rendererEngine.DisplayManager;
import org.appproductions.terrains.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MousePicker {
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private static Vector3f currentRay = new Vector3f();

	private static Matrix4f projectionMatrix;
	private static Matrix4f viewMatrix;
	private static Camera camera;
	
	private static List<Terrain> terrains;
	private static Vector3f currentTerrainPoint;
	private static Vector2f curren2DRay=new Vector2f();

	public static void createPicker(Camera cam, Matrix4f projection, List<Terrain> terrains) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = Maths.createViewMatrix(camera);
		MousePicker.terrains = terrains;
		currentTerrainPoint=new Vector3f(0,0,0);
	}
	
	public static Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}

	public static Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public static Vector2f getCurrent2DRay() {
		return curren2DRay;
	}

	public static void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		curren2DRay = calculate2DMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			//currentTerrainPoint = new Vector3f(0,0,0);
		}
	}

	private static Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private static Vector2f calculate2DMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		return normalizedCoords;
	}

	private static Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = viewMatrix.invert();
		Vector4f rayWorld = invertedView.transform(eyeCoords);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}

	private static Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = projectionMatrix.invert();
		Vector4f eyeCoords = invertedProjection.transform(clipCoords);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private static Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / DisplayManager.getWidth() - 1f;
		float y = (2.0f * mouseY) / DisplayManager.getHeight() - 1f;
		return new Vector2f(x, -y);
	}
	
	//**********************************************************
	
	private static Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Maths.addVectors(start, scaledRay);
	}
	
	private static Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.x, endPoint.z);
			if (terrain != null) {
				return endPoint;
			} else {
				//return new Vector3f(0,0,0);
				return currentTerrainPoint;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private static boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.x, testPoint.z);
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	private static Terrain getTerrain(float worldX, float worldZ) {
		for(Terrain terrain:terrains) {
			if(terrain.isInsideTerrain(worldX, worldZ)) {
				return terrain;
			}
		}
		return null;
	}
}
