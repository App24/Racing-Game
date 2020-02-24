package org.appproductions.rendererEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.appproductions.models.RawModel;
import org.appproductions.objConverter.ModelData;
import org.appproductions.utils.Config;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import utils.UtilsResources;

public class Loader {

	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();

	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public static int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	public static RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	public static RawModel loadToVAO(ModelData modelData) {
		int vaoID = createVAO();
		bindIndicesBuffer(modelData.getIndices());
		storeDataInAttributeList(0, 3, modelData.getVertices());
		storeDataInAttributeList(1, 2, modelData.getTextureCoords());
		storeDataInAttributeList(2, 3, modelData.getNormals());
		unbindVAO();
		return new RawModel(vaoID, modelData.getIndices().length);
	}

	public static Image loadImage(String fileName) {
		ByteBuffer image;
		int width, height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer comp = stack.mallocInt(1);
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);

			InputStream imageFile = UtilsResources.class.getResourceAsStream("/res/" + fileName + ".png");
			byte[] imageData = null;
			try {
				imageData = IOUtils.toByteArray(imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
			imageBuffer.put(imageData);
			imageBuffer.flip();
			image = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4);
			if (image == null)
				System.err.println("Couldn't load " + fileName);
			width = w.get();
			height = h.get();
		}
		return new Image(image, width, height);
	}

	public static int loadTexture(String fileName) {
		Image image = loadImage(fileName);
		int textureID = GL13.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, image.getImage());
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic
				&& Config.setConfig("options").get("Graphics", "Anisotropic", boolean.class)) {
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		} else if (!GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			System.out.println("Anisotropic not supported");
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		textures.add(textureID);
		return textureID;
	}

	public static int loadFontTextureAtlas(String fileName) {
		Image image = loadImage(fileName);
		int textureID = GL13.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, image.getImage());
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
		if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic
				&& Config.setConfig("options").get("Graphics", "Anisotropic", boolean.class)) {
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		} else if (!GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			System.out.println("Anisotropic not supported");
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		textures.add(textureID);
		return textureID;
	}

	public static int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			Image image = loadImage(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, image.getWidth(),
					image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image.getImage());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		return texID;
	}

	/*
	 * public static int loadTexture(String fileName) { Texture texture=null; try {
	 * texture = TextureLoader.getTexture("PNG",
	 * Class.class.getResourceAsStream("/res/" + fileName + ".png"));
	 * GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
	 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
	 * GL11.GL_LINEAR_MIPMAP_LINEAR); GL11.glTexParameterf(GL11.GL_TEXTURE_2D,
	 * GL14.GL_TEXTURE_LOD_BIAS, -0.4f); }catch(IOException e) {
	 * e.printStackTrace(); } int textureID=texture.getTextureID();
	 * textures.add(textureID); return textureID; }
	 */

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	public static void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL30.glDeleteTextures(texture);
		}
	}

	private static void storeDataInAttributeList(int attribute, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribute, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private static void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
