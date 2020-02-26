package org.appproductions.guis.components;

import org.appproductions.fontMeshCreator.FontType;
import org.appproductions.fontMeshCreator.Text;
import org.appproductions.guis.GUI;
import org.joml.Vector2f;

public class TextComponent extends GUIComponent {

	private Text text;

	public TextComponent(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, GUI gui) {
		super(TEXT, gui);
		setText(text, fontSize, font, position, maxLineLength, centered, false);
		setPosition();
		createMesh();
	}

	public TextComponent(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, boolean left, GUI gui) {
		super(TEXT, gui);
		setText(text, fontSize, font, position, maxLineLength, centered, left);
		setPosition();
		createMesh();
	}

	public TextComponent(Text text, GUI gui) {
		super(TEXT, gui);
		this.text = text;
		setPosition();
		createMesh();
	}

	private void setText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, boolean left) {
		this.text = new Text(text, fontSize, font, position, maxLineLength, centered, left);
	}

	private void createMesh() {
		text.createMesh();
	}

	public void setLayer(int layer) {
		text.setLayer(layer);
	}

	public void setPosition() {
		if (!gui.isText()) {
			Vector2f guiPos = gui.getPosition();
			Vector2f textPosition = text.getPosition();
			text.setMaxLineSize(text.isCentered() ? gui.getScale().x : text.getMaxLineSize() - textPosition.x);
			text.setPosition(
					new Vector2f(
							text.isLeft()
									? (-guiPos.x + 1) / 2f + (textPosition.x * gui.getScale().x) + gui.getScale().x / 2f
									: (-guiPos.x + 1) / 2f + (textPosition.x * gui.getScale().x)
											- gui.getScale().x / 2f,
							(-guiPos.y + 1) / 2f + (textPosition.y * gui.getScale().y) - gui.getScale().y / 2f));
			// text.setPosition(new Vector2f(text.getPosition().x-0.5f,
			// text.getPosition().y));
		}
	}

	@Override
	protected void onUpdate() {

	}

	@Override
	public void onStatusUpdate() {

	}

	@Override
	protected void onLoad() {

	}

	public Text getText() {
		return text;
	}

}
