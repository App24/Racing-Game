package org.appproductions.guis;

import org.appproductions.guis.components.GUIComponent;
import org.appproductions.guis.components.ImageComponent;
import org.appproductions.rendererEngine.DisplayManager;
import org.joml.Vector2f;

public class GUIImage extends GUI{
	private GUITexture texture;

	public GUIImage(GUITexture texture, Vector2f position, Vector2f scale) {
		super(position, scale);
		this.texture = texture;
		onLoad();
	}

	public GUIImage(GUITexture texture, Vector2f position, Vector2f scale, boolean keepRatio) {
		super(position, scale);
		this.texture = texture;
		float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
		this.scale = keepRatio
				? new Vector2f((scale.x / texture.getHeight()) * texture.getWidth(),
						((scale.y / texture.getWidth()) * texture.getHeight()) * aspectRatio)
				: scale;
		this.position.y -= keepRatio ? this.scale.y : 0;
		onLoad();
	}

	public GUIImage(GUITexture texture, Vector2f position, float scale) {
		super(position, scale);
		this.texture = texture;
		onLoad();
	}
	
	private void onLoad() {
		addComponent(new ImageComponent(this, texture));
	}

	public void addComponent(GUIComponent component) {
		component.statusUpdate();
		components.add(component);
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> clazz) {
		for (GUIComponent component : components) {
			if (component.getClass() == clazz)
				return (T) component;
		}
		return null;
	}

	public void setTexture(GUITexture texture) {
		this.texture = texture;
	}

	public void updateComponents() {
		for (GUIComponent component : components) {
			if (!hidden)
				component.update();
		}
	}

	public GUITexture getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}
