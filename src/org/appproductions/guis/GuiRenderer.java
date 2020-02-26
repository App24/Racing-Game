package org.appproductions.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.appproductions.fontMeshCreator.FontType;
import org.appproductions.fontMeshCreator.Text;
import org.appproductions.fontMeshCreator.TextMeshData;
import org.appproductions.fontRendering.FontRenderer;
import org.appproductions.guis.components.GUIComponent;
import org.appproductions.guis.components.ImageComponent;
import org.appproductions.guis.components.TextComponent;
import org.appproductions.models.RawModel;
import org.appproductions.rendererEngine.Loader;
import org.appproductions.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GUIRenderer {

	private final RawModel quad;

	private GUIShader shader;
	private FontRenderer renderer;
	private static Map<FontType, List<Text>> texts = new HashMap<FontType, List<Text>>();

	public GUIRenderer() {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = Loader.loadToVAO(positions, 2);
		shader = new GUIShader();
		renderer = new FontRenderer();
	}

	public void initComponents() {
		for (GUI gui : GUIManager.getGUIs()) {
			for (GUIComponent component : gui.getComponents()) {
				component.loadComponent();
			}
		}
	}

	public static void addText(Text text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<Text> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<Text>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}

	public void render() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GUI gui : GUIManager.getGUIs()) {
			if (!gui.hidden) {
				ImageComponent imgComp = gui.getComponent(ImageComponent.class);
				if (imgComp != null) {
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, imgComp.getTexture().getTextureID());
					Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
					shader.loadTransformationMatrix(matrix);
					GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
				}

			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		for (GUI gui : GUIManager.getGUIs()) {
			if (!gui.isHidden()) {
				TextComponent txtComp = gui.getComponent(TextComponent.class);
				if (txtComp != null) {
					renderer.render(txtComp.getText());
				}
			}
		}
		update();
	}

	private void update() {
		for (GUI gui : GUIManager.getGUIs()) {
			gui.updateComponents();
		}
	}

	public void cleanUp() {
		shader.cleanUp();
		renderer.cleanUp();
	}

}
