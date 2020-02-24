package org.appproductions.skybox;

import org.appproductions.entities.Camera;
import org.appproductions.models.RawModel;
import org.appproductions.rendererEngine.Loader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SkyboxRenderer {
	private static final float SIZE = 500f;

	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE,
			-SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
			-SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,
			SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE,
			-SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE,
			-SIZE, SIZE };

	private static String[] TEXTURE_FILES = { "Skybox/right", "Skybox/left", "Skybox/top", "Skybox/bottom",
			"Skybox/back", "Skybox/front" };
	// private static String[] NIGHT_TEXTURE_FILES=
	// {"nightRight","nightLeft","nightTop","nightBottom","nightBack","nightFront"};

	private RawModel cube;
	private int texture;
	// private int nightTexture;
	private SkyboxShader shader;
	// private float time=8000;

	// private Vector3f normalFogColor, nightFogColor;
	// private Vector3f normalSunColor, nightSunColor;

	public SkyboxRenderer(Matrix4f projectionMatrix) {
		cube = Loader.loadToVAO(VERTICES, 3);
		texture = Loader.loadCubeMap(TEXTURE_FILES);
		// nightTexture=Loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		// normalFogColor=MasterRenderer.getFogColor();
		// nightFogColor=new Vector3f(0,0,0.05f);
		// normalSunColor=new Vector3f(1,1,1f);
		// nightSunColor=new Vector3f(0.3f,0.3f,0.3f);
	}

	public void render(Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures() {
		/*
		 * time += DisplayManager.getFrameTimeSeconds() * 1000; time %= 24000; int
		 * texture1; int texture2; float blendFactor; if(time >= 0 && time < 5000){
		 * texture1 = nightTexture; texture2 = nightTexture;
		 * MasterRenderer.getLights().get(0).setColor(nightSunColor);
		 * MasterRenderer.setFogColor(nightFogColor.x, nightFogColor.y,
		 * nightFogColor.z); blendFactor = (time - 0)/(5000 - 0); }else if(time >= 5000
		 * && time < 8000){ texture1 = nightTexture; texture2 = texture; blendFactor =
		 * (time - 5000)/(8000 - 5000); Vector3f test=new
		 * Vector3f(Maths.lerp(nightFogColor.x, normalFogColor.x, blendFactor),
		 * Maths.lerp(nightFogColor.y, normalFogColor.y, blendFactor),
		 * Maths.lerp(nightFogColor.z, normalFogColor.z, blendFactor));
		 * MasterRenderer.setFogColor(test.x, test.y, test.z);
		 * MasterRenderer.getLights().get(0).setColor(new
		 * Vector3f(Maths.lerp(nightSunColor.x, normalSunColor.x, blendFactor),
		 * Maths.lerp(nightSunColor.y, normalSunColor.y, blendFactor),
		 * Maths.lerp(nightSunColor.z, normalSunColor.z, blendFactor))); }else if(time
		 * >= 8000 && time < 21000){ texture1 = texture; texture2 = texture;
		 * MasterRenderer.getLights().get(0).setColor(normalSunColor);
		 * MasterRenderer.setFogColor(normalFogColor.x, normalFogColor.y,
		 * normalFogColor.z); blendFactor = (time - 8000)/(21000 - 8000); }else{
		 * texture1 = texture; texture2 = nightTexture; blendFactor = (time -
		 * 21000)/(24000 - 21000); Vector3f test=new
		 * Vector3f(Maths.lerp(normalFogColor.x, nightFogColor.x, blendFactor),
		 * Maths.lerp(normalFogColor.y, nightFogColor.y, blendFactor),
		 * Maths.lerp(normalFogColor.z, nightFogColor.z, blendFactor));
		 * MasterRenderer.setFogColor(test.x, test.y, test.z);
		 * MasterRenderer.getLights().get(0).setColor(new
		 * Vector3f(Maths.lerp(normalSunColor.x, nightSunColor.x, blendFactor),
		 * Maths.lerp(normalSunColor.y, nightSunColor.y, blendFactor),
		 * Maths.lerp(normalSunColor.z, nightSunColor.z, blendFactor))); }
		 * 
		 * GL13.glActiveTexture(GL13.GL_TEXTURE0);
		 * GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		 * GL13.glActiveTexture(GL13.GL_TEXTURE1);
		 * GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		 * shader.loadBlendFactor(blendFactor);
		 */
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);

	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
