package org.appproductions.guis;

import java.util.ArrayList;
import java.util.List;

import org.appproductions.guis.components.GuiComponent;
import org.joml.Vector2f;

public class Gui {
	private GuiTexture texture;
	private Vector2f position;
	private Vector2f scale;
	private List<GuiComponent> components=new ArrayList<GuiComponent>();
	
	public Gui(GuiTexture texture, Vector2f positiong, Vector2f scale) {
		this.texture=texture;
		this.position=positiong;
		this.scale=scale;
	}
	
	public Gui(GuiTexture texture, Vector2f positiong, Vector2f scale, boolean keepRatio) {
		this.texture=texture;
		this.position=positiong;
		this.scale=keepRatio?new Vector2f((scale.x/texture.getHeight())*texture.getWidth(), (scale.y/texture.getWidth())*texture.getHeight()):scale;
	}
	
	public Gui(GuiTexture texture, Vector2f positiong, float scale) {
		this.texture=texture;
		this.position=positiong;
		this.scale=new Vector2f(scale, scale);
	}
	
	public void addComponent(GuiComponent component) {
		components.add(component);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> clazz) {
		for(GuiComponent component:components) {
			if(component.getClass()==clazz)
				return (T) component;
		}
		return null;
	}
	
	public void setTexture(GuiTexture texture) {
		this.texture=texture;
	}
	
	public void updateComponents() {
		for(GuiComponent component:components) {
			component.update();
		}
	}
	
	public GuiTexture getTexture() {
		return texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
}
