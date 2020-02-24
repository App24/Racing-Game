package org.appproductions.guis.components;

import org.appproductions.fontMeshCreator.FontType;
import org.appproductions.fontRendering.TextMaster;
import org.appproductions.guis.Gui;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TextComponent extends GuiComponent {

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

	public TextComponent(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, Gui gui) {
		super(TEXT, gui);
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.leftText = false;
		setPosition(position);
	}

	public TextComponent(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, boolean left, Gui gui) {
		super(TEXT, gui);
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.leftText = left;
		setPosition(position);
	}

	public void setPosition(Vector2f pos) {
		Vector2f guiPos = gui.getPosition();
		lineMaxSize=gui.getScale().x;
		position = new Vector2f(
				leftText ? (-guiPos.x + 1) / 2f + (pos.x * gui.getScale().x) + gui.getScale().x / 2f
						: (-guiPos.x + 1) / 2f + (pos.x * gui.getScale().x) - gui.getScale().x / 2f,
				(-guiPos.y + 1) / 2f + (pos.y * gui.getScale().y) - gui.getScale().y / 2f);
	}

	@Override
	protected void onUpdate() {

	}
	
	@Override
	public void onStatusUpdate() {
		if(!gui.isHidden())
			TextMaster.loadText(this);
		else
			TextMaster.removeText(this);
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the colour of the text.
	 * 
	 * @param r - red value, between 0 and 1.
	 * @param g - green value, between 0 and 1.
	 * @param b - blue value, between 0 and 1.
	 */
	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}

	/**
	 * @return the colour of the text.
	 */
	public Vector3f getColour() {
		return colour;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length that
	 *         is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space. (0,
	 *         0) is the top left corner of the screen, (1, 1) is the bottom right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for the
	 *         quads on which the text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * 
	 * @param vao           - the VAO containing all the vertex data for the quads
	 *                      on which the text will be rendered.
	 * @param verticesCount - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	public float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in loading).
	 * 
	 * @param number
	 */
	public void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	public boolean isCentered() {
		return centerText;
	}

	public boolean isLeft() {
		return leftText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	public float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	public String getTextString() {
		return textString;
	}

}
