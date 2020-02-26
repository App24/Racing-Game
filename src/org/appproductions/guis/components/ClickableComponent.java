package org.appproductions.guis.components;

import org.appproductions.guis.GUI;
import org.appproductions.guis.GUITexture;
import org.appproductions.guis.components.interfaces.ICommand;
import org.appproductions.input.Mouse;
import org.appproductions.toolbox.MousePicker;
import org.lwjgl.glfw.GLFW;

public class ClickableComponent extends GUIComponent {

	private ICommand command;
	private GUITexture normal, hover, click;
	private boolean leave;
	private ImageComponent imgComp;
	private boolean hovered = false, leaved = false;

	public ClickableComponent(ICommand command, GUI gui) {
		super(CLICKABLE, gui);
		this.command = command;
	}

	public ClickableComponent(ICommand command, GUITexture hover, GUI gui) {
		super(CLICKABLE, gui);
		this.command = command;
		this.hover = hover;
		click = hover;
	}

	public ClickableComponent(ICommand command, GUITexture hover, GUITexture click, GUI gui) {
		super(CLICKABLE, gui);
		this.command = command;
		this.hover = hover;
		this.click = click;
	}

	@Override
	protected void onUpdate() {
		if (isInBounds()) {
			if (Mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
				command.onClick();
			}
			if (!Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
				imgComp.setTexture(hover);
				if(!hovered) {
					command.onHover();
					hovered=true;
				}
			} else if (Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1) && leave) {
				imgComp.setTexture(hover);
				if(!hovered) {
					command.onHover();
					hovered=true;
				}
			} else if (Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1) && !leave) {
				imgComp.setTexture(click);
			}
			leaved=false;
		} else {
			leave = true;
			imgComp.setTexture(normal);
			if(!leaved) {
				command.onLeave();
				leaved=true;
			}
			hovered=false;
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

	@Override
	protected void onStatusUpdate() {

	}

	@Override
	protected void onLoad() {
		imgComp = gui.getComponent(ImageComponent.class);
		normal = imgComp.getTexture();
		if (hover == null) {
			hover = normal;
			click = normal;
		}
	}

}
