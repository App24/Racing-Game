package org.appproductions.guis.components;

import org.appproductions.guis.Gui;

public abstract class GuiComponent {
	
	protected static final int CLICKABLE=0, TEXT=1, SOUND=2;
	
	protected Gui gui;
	
	protected int id;
	
	public GuiComponent(int id, Gui gui) {
		this.id=id;
		this.gui=gui;
	}
	
	public void update() {
		onUpdate();
	}
	
	public void statusUpdate() {
		onStatusUpdate();
	}

	protected abstract void onUpdate();
	
	protected abstract void onStatusUpdate();
	
}
