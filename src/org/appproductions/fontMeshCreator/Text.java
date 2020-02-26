package org.appproductions.fontMeshCreator;

import org.appproductions.rendererEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Text {
	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f colour = new Vector3f(1f, 1f, 1f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false, leftText = false;
	
	private int layer=0;
	
	public Text(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.leftText = false;
	}

	public Text(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered,
			boolean left) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.leftText = left;
	}
	
	public Text createMesh() {
		TextMeshData data = font.loadText(this);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		setMeshInfo(vao, data.getVertexCount());
		return this;
	}

	public void setTextString(String text) {
		this.textString = text;
		createMesh();
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setLayer(int layer) {
		this.layer=layer;
	}
	
	public FontType getFont() {
		return font;
	}
	
	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public int getMesh() {
		return textMeshVao;
	}
	
	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public float getFontSize() {
		return fontSize;
	}
	
	public void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}
	
	public boolean isCentered() {
		return centerText;
	}

	public boolean isLeft() {
		return leftText;
	}
	
	public float getMaxLineSize() {
		return lineMaxSize;
	}
	
	public String getTextString() {
		return textString;
	}
	
	public void setMaxLineSize(float lineMaxSize) {
		this.lineMaxSize=lineMaxSize;
	}
	
	public void setPosition(Vector2f pos) {
		this.position=pos;
	}
}
