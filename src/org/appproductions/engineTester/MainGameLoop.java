package org.appproductions.engineTester;

import java.util.ArrayList;
import java.util.List;

import org.appproductions.audio.AudioManager;
import org.appproductions.audio.AudioMaster;
import org.appproductions.cache.CachedModels;
import org.appproductions.config.ConfigManager;
import org.appproductions.config.KeyBinds;
import org.appproductions.config.Options;
import org.appproductions.entities.Camera;
import org.appproductions.entities.EntityManager;
import org.appproductions.entities.Light;
import org.appproductions.entities.Player;
import org.appproductions.fileParser.ModelLoader;
import org.appproductions.fileParser.WorldLoader;
import org.appproductions.fontMeshCreator.FontType;
import org.appproductions.guis.GUIImage;
import org.appproductions.guis.GUIManager;
import org.appproductions.guis.GUIRenderer;
import org.appproductions.guis.GUIText;
import org.appproductions.guis.GUITexture;
import org.appproductions.guis.components.ClickableComponent;
import org.appproductions.guis.components.TextComponent;
import org.appproductions.guis.components.interfaces.ICommand;
import org.appproductions.input.Keyboard;
import org.appproductions.input.Mouse;
import org.appproductions.postProcessing.Fbo;
import org.appproductions.postProcessing.PostProcessing;
import org.appproductions.rendererEngine.DisplayManager;
import org.appproductions.rendererEngine.Loader;
import org.appproductions.rendererEngine.MasterRenderer;
import org.appproductions.terrains.Terrain;
import org.appproductions.terrains.TerrainTexture;
import org.appproductions.terrains.TerrainTexturePack;
import org.appproductions.toolbox.MousePicker;
import org.appproductions.toolbox.Version;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MainGameLoop {

	public static void main(String[] args) {
		AudioMaster.init();
		ConfigManager.init();
		DisplayManager.createDisplay();

		MasterRenderer renderer = new MasterRenderer();

		AudioMaster.setListenerData(0, 0, 0);

		FontType font = new FontType(Loader.loadTexture("Fonts/arial"), "Fonts/arial.fnt");
		GUIText playerPosText = new GUIText("player pos", 1.5f, font, new Vector2f(0, 0), 1f, false);
		GUIText versionText = new GUIText(Version.getVersion(), 1.5f, font, new Vector2f(0, 0.95f), 1f, false, true);
		GUIText fpsText = new GUIText("fps", 1.5f, font, new Vector2f(0, 0), 1f, false, true);
		GUIText deltaText = new GUIText("delta", 1.5f, font, new Vector2f(0, 0.95f), 1f, false);
		GUIText keybindsText = new GUIText("keybinds", 1.75f, font, new Vector2f(0,0.05f), 0.5f, true);
		
		GUIManager.addGUI(playerPosText);
		GUIManager.addGUI(versionText);
		GUIManager.addGUI(fpsText);
		GUIManager.addGUI(deltaText);
		GUIManager.addGUI(keybindsText);
		
		keybindsText.setLayer(1);

		fpsText.setHidden(!(boolean) Options.getOption("Show FPS"));
		deltaText.setHidden(!(boolean) Options.getOption("Show Delta"));
		
		keybindsText.setHidden(true);
		keybindsText.setColor(0, 0, 0);
		keybindsText.setTextString(KeyBinds.keybindsToString());

		ModelLoader.loadModels();
		List<Terrain> terrains = new ArrayList<Terrain>();

		TerrainTexture backgroundTexture = new TerrainTexture(Loader.loadTexture("Map/grass"));
		TerrainTexture rTexture = new TerrainTexture(Loader.loadTexture("Map/mud"));
		TerrainTexture gTexture = new TerrainTexture(Loader.loadTexture("Map/grassFlower"));
		TerrainTexture bTexture = new TerrainTexture(Loader.loadTexture("Map/path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(Loader.loadTexture("Map/blendMap"));

		Terrain terrain1 = new Terrain(0, 0, texturePack, blendMap, "Map/heightmap");
		Terrain terrain2 = new Terrain(-1, 0, texturePack, blendMap, "Map/heightmap");
		Terrain terrain3 = new Terrain(0, -1, texturePack, blendMap, "Map/heightmap");
		Terrain terrain4 = new Terrain(-1, -1, texturePack, blendMap, "Map/heightmap");

		terrains.add(terrain1);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);
		WorldLoader.loadEntities();

		Light light = new Light(new Vector3f(1000000, 15000000, -700000), new Vector3f(1f, 1f, 1f));

		Player player = new Player(CachedModels.getTexturedModel(6), new Vector3f(0, 0, -6), 0, 0, 0, 2);

		EntityManager.addEntity(player);
		Camera camera = new Camera(player);
		// AudioMaster.setListenerData(camera.getPosition().x, camera.getPosition().y,
		// camera.getPosition().z);

		MousePicker.createPicker(camera, renderer.getProjectionMatrix(), terrains);

		GUIRenderer guiRenderer = new GUIRenderer();
		GUIImage gui = new GUIImage(new GUITexture(Loader.loadImage("button")),
				new Vector2f(0f, 0.85f), new Vector2f(0.15f, 0.15f));
		
		gui.addComponent(new ClickableComponent(new ICommand() {

			@Override
			public void onClick() {
				// player.reset();
				keybindsText.setHidden(!keybindsText.isHidden());
			}
		}, new GUITexture(Loader.loadImage("hover")), gui));
		gui.addComponent(new TextComponent("Keybinds", 1.5f, font, new Vector2f(0, 0.5f-(0.15f)), 1f, true, gui));
		gui.getComponent(TextComponent.class).getText().setColour(0, 0, 0);
		GUIManager.addGUI(gui);

		boolean antialiasing = Options.getOption("Antialiasing");
		boolean postprocessing = Options.getOption("Post Processing");

		Fbo multisampleFbo = new Fbo(DisplayManager.getWidth(), DisplayManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		Fbo outputFbo = new Fbo(DisplayManager.getWidth(), DisplayManager.getHeight(), Fbo.DEPTH_TEXTURE);
		if (antialiasing)
			multisampleFbo = new Fbo(DisplayManager.getWidth(), DisplayManager.getHeight());
		PostProcessing.init();
		
		guiRenderer.initComponents();
		
		while (!DisplayManager.shouldClose()) {
			camera.move();
			player.move(terrains);
			playerPosText.setTextString("x: " + String.format("%.2f", player.getPosition().x) + " y: "
					+ String.format("%.2f", player.getPosition().y) + " z: "
					+ String.format("%.2f", player.getPosition().z));
			fpsText.setTextString("FPS: " + DisplayManager.getFPS());
			deltaText.setTextString("Delta: "+DisplayManager.getDelta());

			MousePicker.update();
			if (Keyboard.isKeyPressed(KeyBinds.getKey("Mouse Lock"))) {
				Mouse.setGrabbed(!Mouse.isGrabbed());
			}

			multisampleFbo.bindFrameBuffer();
			renderer.renderScene(terrains, light, camera);
			multisampleFbo.unbindFrameBuffer();
			if (postprocessing) {
				multisampleFbo.resolveToFbo(outputFbo);
				PostProcessing.doPostProcessing(outputFbo.getColourTexture());
			} else {
				multisampleFbo.resolveToScreen();
			}

			guiRenderer.render();

			DisplayManager.updateDisplay();
		}

		PostProcessing.cleanUp();
		
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		
		Loader.cleanUp();
		
		renderer.cleanUp();
		guiRenderer.cleanUp();
		
		AudioManager.cleanUp();
		AudioMaster.cleanUp();
		DisplayManager.close();
	}

}
