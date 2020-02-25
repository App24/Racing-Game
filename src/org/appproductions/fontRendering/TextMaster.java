package org.appproductions.fontRendering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.appproductions.fontMeshCreator.FontType;
import org.appproductions.fontMeshCreator.GUIText;
import org.appproductions.fontMeshCreator.TextMeshData;
import org.appproductions.guis.components.TextComponent;
import org.appproductions.rendererEngine.Loader;

public class TextMaster {

	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static Map<FontType, List<TextComponent>> textsComponent = new HashMap<FontType, List<TextComponent>>();
	private static FontRenderer renderer;

	public static void init() {
		renderer = new FontRenderer();
	}

	public static void render() {
		renderer.renderComponents(textsComponent);
		renderer.render(texts);
	}

	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
		sortTexts();
	}
	
	private static void sortTexts() {
		for(FontType font:texts.keySet()) {
			List<GUIText> text=texts.get(font);
			Collections.sort(text, new Comparator<GUIText>() {

				@Override
				public int compare(GUIText o1, GUIText o2) {
					return o1.getLayer()-o2.getLayer();
				}
			});
		}
	}

	public static void loadText(TextComponent text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<TextComponent> textBatch = textsComponent.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<TextComponent>();
			textsComponent.put(font, textBatch);
		}
		textBatch.add(text);
	}

	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	public static void removeText(TextComponent text) {
		if (textsComponent.containsKey(text.getFont())) {
			List<TextComponent> textBatch = textsComponent.get(text.getFont());
			textBatch.remove(text);
			if (textBatch.isEmpty()) {
				textsComponent.remove(text.getFont());
			}
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

}
