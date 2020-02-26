package org.appproductions.guis;

import org.appproductions.fontMeshCreator.FontType;
import org.appproductions.fontMeshCreator.Text;
import org.appproductions.guis.components.TextComponent;
import org.joml.Vector2f;

public class GUIText extends GUI {

	private Text text;

	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		super(position, new Vector2f(1, 1));
		this.text=new Text(text, fontSize, font, position, maxLineLength, centered, false);
		onLoad();
	}

	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, boolean left) {
		super(new Vector2f(position.x,1-position.y), new Vector2f(1, 1));
		this.text=new Text(text, fontSize, font, position, maxLineLength, centered, left);
		onLoad();
	}
	
	public void setLayer(int layer) {
		getComponent(TextComponent.class).setLayer(layer);
	}

	private void onLoad() {
		isText=true;
		addComponent(new TextComponent(text, this));
	}

	public void setColor(float r, float g, float b) {
		getComponent(TextComponent.class).getText().setColour(r, g, b);
	}
	
	public void setTextString(String text) {
		getComponent(TextComponent.class).getText().setTextString(text);
	}

}
