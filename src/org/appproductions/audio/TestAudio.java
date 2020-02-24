package org.appproductions.audio;

import java.io.IOException;

public class TestAudio {

	public static void main(String[] args) throws IOException, InterruptedException {
		AudioMaster.init();
		AudioMaster.setListenerData(0,0,0);
		
		int buffer=AudioMaster.loadSound("Audio/bounce");
		Source source=new Source();
		source.setLooping(true);
		source.play(buffer);
		
		float xPos=0;
		source.setPosition(xPos, 0, 0);
		
		char c=' ';
		while(c!='q') {
			//c=(char)System.in.read();
			
			xPos-=0.02f;
			source.setPosition(xPos, 0, 0);
			System.out.println(xPos);
			Thread.sleep(10);
		}

		source.delete();
		AudioMaster.cleanUp();
	}

}
