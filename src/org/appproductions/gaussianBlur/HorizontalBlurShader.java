package org.appproductions.gaussianBlur;

import org.appproductions.shaders.ShaderProgram;

public class HorizontalBlurShader extends ShaderProgram {
	 
    private static final String VERTEX_FILE = "/org/appproductions/gaussianBlur/horizontalBlurVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/org/appproductions/gaussianBlur/blurFragmentShader.glsl";
     
    private int location_targetWidth;
     
    protected HorizontalBlurShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    protected void loadTargetWidth(float width){
        super.loadFloat(location_targetWidth, width);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_targetWidth = super.getUniformLocation("targetWidth");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
     
}
