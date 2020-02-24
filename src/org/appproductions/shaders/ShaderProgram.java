package org.appproductions.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import utils.UtilsSrc;

public abstract class ShaderProgram {

	private int vertexShaderID, fragmentShaderID, programID;
	
	private FloatBuffer matrixBuffer=MemoryUtil.memAllocFloat(16);

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}

	protected abstract void bindAttributes();

	protected abstract void getAllUniformLocations();
	
	protected void bindAttribute(int attributeNumber, String variableName) {
		GL20.glBindAttribLocation(programID, attributeNumber, variableName);
	}
	
	protected int getUniformLocation(String uniformLocation) {
		return GL20.glGetUniformLocation(programID, uniformLocation);
	}
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadBoolean(int location, boolean value) {
		GL20.glUniform1i(location, value?1:0);
	}
	
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void load2DVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(matrixBuffer);
		
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	private int loadShader(String file, int type) {
		StringBuilder shaderSource=new StringBuilder();
		try {
			InputStream in=UtilsSrc.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int shaderID=GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL20.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 512));
			System.err.println("Couldn't compile shader: "+file);
			System.exit(-1);
		}
		return shaderID;
	}

}
