package org.appproductions.guis;

import java.util.ArrayList;
import java.util.List;

import org.appproductions.guis.components.GUIComponent;
import org.joml.Vector2f;

public abstract class GUI {
	protected Vector2f position;
	protected Vector2f scale;
	protected boolean hidden = false;
	protected List<GUIComponent> components = new ArrayList<GUIComponent>();
	protected boolean isText=false;

	public GUI(Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale=scale;
	}
	
	public GUI(Vector2f position, float scale) {
		this.position = position;
		this.scale=new Vector2f(scale);
	}
	
	public boolean isText() {
		return isText;
	}
	
	public List<GUIComponent> getComponents(){
		return components;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
		for (GUIComponent component : components) {
			component.statusUpdate();
		}
	}

	public boolean isHidden() {
		return hidden;
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

	public void updateComponents() {
		for (GUIComponent component : components) {
			if (!hidden)
				component.update();
		}
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
}
