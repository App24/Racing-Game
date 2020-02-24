package org.appproductions.rendererEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.appproductions.entities.Camera;
import org.appproductions.entities.Entity;
import org.appproductions.entities.EntityManager;
import org.appproductions.entities.Light;
import org.appproductions.frustumCulling.FrustumCullingFilter;
import org.appproductions.models.TexturedModel;
import org.appproductions.shaders.StaticShader;
import org.appproductions.skybox.SkyboxRenderer;
import org.appproductions.terrains.Terrain;
import org.appproductions.terrains.TerrainShader;
import org.appproductions.utils.Config;
import org.appproductions.utils.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class MasterRenderer {

	private static float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	private static final float RED = 1f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.25f;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader=new TerrainShader();

	private Matrix4f projectionMatrix;
	private Map<TexturedModel, List<Entity>> entities=new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains=new ArrayList<Terrain>();
	
	private FrustumCullingFilter frustumCulling=new FrustumCullingFilter();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer() {
		FOV=Config.setConfig("options").get("View", "FOV", float.class);
		enableCulling();
		createProjectionMatrix();
		if (Config.setConfig("misc").get("Debug", "Wireframe", boolean.class)) {
			enableWireframe();
		}
		renderer=new EntityRenderer(shader,projectionMatrix);
		terrainRenderer=new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer=new SkyboxRenderer(projectionMatrix);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public static void enableWireframe() {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		disableCulling();
	}

	public static void disableWireframe() {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		enableCulling();
	}

	public static void enableCulling() {
		if (!Config.setConfig("misc").get("Debug", "DisableCulling", boolean.class)) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		}
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(RED, GREEN, BLUE, 1f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void renderScene(List<Terrain> terrains, Light light, Camera camera) {
		frustumCulling.updateFrustum(projectionMatrix, Maths.createViewMatrix(camera));
		for(Terrain terrain:terrains) {
			processTerrain(terrain);
		}
		for(Entity entity:EntityManager.getEntities()) {
			processEntity(entity);
			frustumCulling.filter(entity, entity.getModel().getBoundingRadius());
		}
		render(light, camera);
	}

	public void render(Light light, Camera camera) {
		prepare();
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		entities.clear();
		terrains.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel=entity.getModel();
		List<Entity> batch=entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch=new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		skyboxRenderer.cleanUp();
	}

	public void createProjectionMatrix() {
		float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix.m33(0);
	}

}
