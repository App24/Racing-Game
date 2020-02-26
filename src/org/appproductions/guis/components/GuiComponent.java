package org.appproductions.guis.components;

import org.appproductions.guis.GUI;

public abstract class GUIComponent {
	
	protected static final int CLICKABLE=0, TEXT=1, SOUND=2, IMAGE=3;
	
	protected GUI gui;
	
	protected int id;
	
	public GUIComponent(int id, GUI gui) {
		this.id=id;
		this.gui=gui;
	}
	
	public void update() {
		onUpdate();
	}
	
	public void statusUpdate() {
		onStatusUpdate();
	}
	
	public void loadComponent() {
		onLoad();
	}
	
	protected abstract void onLoad();

	protected abstract void onUpdate();
	
	protected abstract void onStatusUpdate();
	
}
