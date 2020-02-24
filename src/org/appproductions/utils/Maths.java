package org.appproductions.utils;

import org.appproductions.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f pos, float rotX, float rotY, float rotZ, float scale) {
		Matrix4f matrix=new Matrix4f();
		matrix.identity();
		matrix.translate(pos,matrix);
		matrix.rotate(rotateQuaternion(1, rotX, rotY, rotZ));
		matrix.scale(new Vector3f(scale, scale, scale),matrix);
		return matrix;
	}
	
	public static Vector3f addVectors(Vector3f vec1, Vector3f vec2) {
		return new Vector3f(vec1.x+vec2.x, vec1.y+vec2.y, vec1.z+vec2.z);
	}

    private static Quaternionf rotateQuaternion(float angle, float x, float y, float z) {
    	Quaternionf rotation=new Quaternionf();
        Quaternionf rotationy=new Quaternionf();
        rotationy.rotateY((float) Math.toRadians(angle*y));
        rotation.mul(rotationy);
        Quaternionf rotationx=new Quaternionf();
        rotationx.rotateX((float) Math.toRadians(angle*x));
        rotation.mul(rotationx);
        Quaternionf rotationz=new Quaternionf();
        rotationz.rotateZ((float) Math.toRadians(angle*z));
        rotation.mul(rotationz);
        rotation.normalize();
        return rotation;
    }
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix=new Matrix4f();
		matrix.identity();
		matrix.translate(translation.x, translation.y, 0, matrix);
		matrix.scale(new Vector3f(scale.x, scale.y, 1f), matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix=new Matrix4f();
		viewMatrix.identity();
		viewMatrix.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix);
		viewMatrix.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix);
		viewMatrix.rotate((float)Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix);
		Vector3f cameraPos=camera.getPosition();
		Vector3f negativeCameraPos=new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativeCameraPos, viewMatrix);
		return viewMatrix;
	}
	
	public static float lerp(float point1, float point2, float alpha)
	{
	    return point1 + alpha * (point2 - point1);
	}
	
	public static float clamp(float value, float min, float max) {
		if(value>max)
			value=max;
		if(value<min)
			value=min;
		return value;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
}
