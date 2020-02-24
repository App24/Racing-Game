package org.appproductions.postProcessing;

import org.appproductions.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/org/appproductions/postProcessing/contrastVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/org/appproductions/postProcessing/contrastFragmentShader.glsl";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
