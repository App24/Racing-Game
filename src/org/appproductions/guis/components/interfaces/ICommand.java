package org.appproductions.guis.components.interfaces;

public interface ICommand {
	
	public void onClick();
	
	default void onHover() {
	}
	
	default void onLeave() {
	}
	
}
