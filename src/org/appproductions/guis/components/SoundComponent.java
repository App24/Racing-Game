package org.appproductions.guis.components;

import org.appproductions.audio.AudioManager;
import org.appproductions.audio.AudioMaster;
import org.appproductions.audio.Source;
import org.appproductions.guis.Gui;

public class SoundComponent extends GuiComponent {

	int buffer;
	Source source=new Source();
	
	public SoundComponent(Gui gui, String audioFile) {
		super(SOUND, gui);
		buffer=AudioMaster.loadSound(audioFile);
		AudioManager.addSource(source);
	}

	@Override
	protected void onUpdate() {
		
	}
	
	public void playSound() {
		source.play(buffer);
	}

	@Override
	protected void onStatusUpdate() {
		
	}

}
