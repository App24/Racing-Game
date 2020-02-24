package org.appproductions.guis.components;

import org.appproductions.guis.Gui;
import org.appproductions.guis.GuiTexture;
import org.appproductions.guis.components.interfaces.ICommand;
import org.appproductions.input.Mouse;
import org.appproductions.utils.MousePicker;
import org.lwjgl.glfw.GLFW;

public class ClickableComponent extends GuiComponent {

	private ICommand command;
	private GuiTexture normal, hover, click;
	private boolean leave;

	public ClickableComponent(ICommand command, Gui gui) {
		super(CLICKABLE, gui);
		this.command = command;
		normal = gui.getTexture();
		hover = gui.getTexture();
		click = gui.getTexture();
	}

	public ClickableComponent(ICommand command, GuiTexture hover, Gui gui) {
		super(CLICKABLE, gui);
		this.command = command;
		normal = gui.getTexture();
		this.hover = hover;
		click = hover;
	}

	public ClickableComponent(ICommand command, GuiTexture hover, GuiTexture click, Gui gui) {
		super(CLICKABLE, gui);
		this.command = command;
		normal = gui.getTexture();
		this.hover = hover;
		this.click = click;
	}

	@Override
	protected void onUpdate() {
		if (isInBounds()) {
			if (Mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
				command.onClick();
			}
			if(!Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
				gui.setTexture(hover);
				leave=false;
			}
			else if(Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)&&leave) {
				gui.setTexture(hover);
			}
			else if (Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)&&!leave) {
				gui.setTexture(click);
			}
		} else {
			leave=true;
			gui.setTexture(normal);
		}
	}

	private boolean isInBounds() {
		float x = gui.getPosition().x - gui.getScale().x;
		float y = gui.getPosition().y - gui.getScale().y;
		float width = gui.getPosition().x + gui.getScale().x;
		float height = gui.getPosition().y + gui.getScale().y;
		float mouseX = MousePicker.getCurrent2DRay().x;
		float mouseY = MousePicker.getCurrent2DRay().y;
		if (mouseX >= x && mouseX <= width && mouseY >= y && mouseY <= height)
			return true;
		return false;
	}

}
