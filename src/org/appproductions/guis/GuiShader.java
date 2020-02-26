package org.appproductions.guis;

import org.appproductions.shaders.ShaderProgram;
import org.joml.Matrix4f;

public class GUIShader extends ShaderProgram {

	private static final String VERTEX_FILE="/org/appproductions/guis/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE="/org/appproductions/guis/guiFragmentShader.glsl";
	
	private int uniform_transformationMatrix;
	
	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		uniform_transformationMatrix=getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(uniform_transformationMatrix, matrix);
	}

}
