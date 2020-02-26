package org.appproductions.skybox;

import org.appproductions.entities.Camera;
import org.appproductions.rendererEngine.DisplayManager;
import org.appproductions.shaders.ShaderProgram;
import org.appproductions.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE="/org/appproductions/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE="/org/appproductions/skybox/skyboxFragmentShader.glsl";
	
	private static final float ROTATE_SPEED=1f;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blenderFactor;
	
	private float rotation=0f;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix=Maths.createViewMatrix(camera);
		matrix=matrix.m30(0);
		matrix=matrix.m31(0);
		matrix=matrix.m32(0);
		rotation+=ROTATE_SPEED*DisplayManager.getDelta();
		matrix=matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadFogColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r,g,b));
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blenderFactor, blend);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix=super.getUniformLocation("projectionMatrix");
		location_viewMatrix=super.getUniformLocation("viewMatrix");
		location_fogColor=super.getUniformLocation("fogColor");
		location_blenderFactor=super.getUniformLocation("blendFactor");
		location_cubeMap=super.getUniformLocation("cubeMap");
		location_cubeMap2=super.getUniformLocation("cubeMap2");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
