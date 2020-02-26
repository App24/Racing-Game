package org.appproductions.guis.components;

import org.appproductions.guis.GUI;
import org.appproductions.guis.GUITexture;

public class ImageComponent extends GUIComponent {
	
	private GUITexture texture;
	
	public ImageComponent(GUI gui, GUITexture texture) {
		super(IMAGE, gui);
		this.texture=texture;
	}

	@Override
	protected void onUpdate() {
		
	}

	@Override
	protected void onStatusUpdate() {
		
	}
	
	public GUITexture getTexture() {
		return texture;
	}
	
	public void setTexture(GUITexture texture) {
		this.texture=texture;
	}

	@Override
	protected void onLoad() {
		
	}
}
