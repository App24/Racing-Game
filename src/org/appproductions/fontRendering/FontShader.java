package org.appproductions.fontRendering;

import org.appproductions.shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/org/appproductions/fontRendering/fontVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/org/appproductions/fontRendering/fontFragmentShader.glsl";

	private int location_color;
	private int location_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color=getUniformLocation("color");
		location_translation=getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
	}
	
	public void loadColor(Vector3f color) {
		loadVector(location_color, color);
	}
	
	public void loadTranslation(Vector2f translation) {
		load2DVector(location_translation, translation);
	}


}
