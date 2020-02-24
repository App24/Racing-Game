package org.appproductions.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class AudioMaster {
	
	private static long context, device;

	private static ALCCapabilities alcCapabilities;
	@SuppressWarnings("unused")
	private static ALCapabilities alCapabilities;
	
	private static List<Integer> buffers=new ArrayList<Integer>();
	
	public static void init() {
		String defaultDeviceName = ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC11.alcOpenDevice(defaultDeviceName);
		
		int[] attributes = {0};
		context = ALC11.alcCreateContext(device, attributes);
		ALC11.alcMakeContextCurrent(context);
		
		alcCapabilities = ALC.createCapabilities(device);
		alCapabilities = AL.createCapabilities(alcCapabilities);
		AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE_CLAMPED);
	}
	
	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
		AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE_CLAMPED);
	}
	
	public static int loadSound(String file) {
		int buffer=AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile=WaveData.create(file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
	
	public static void cleanUp() {
		for(int buffer:buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		ALC11.alcDestroyContext(context);
		ALC11.alcCloseDevice(device);
	}
	
}
