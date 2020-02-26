package org.appproductions.fileParser;

import java.util.List;

import org.appproductions.cache.CachedModels;
import org.appproductions.models.TexturedModel;
import org.appproductions.objConverter.OBJFileLoader;
import org.appproductions.rendererEngine.Loader;
import org.appproductions.textures.ModelTexture;

public class ModelLoader extends FileParser {
	
	private static final String FILE = "/res/loadModels.txt";
	private static final String MODELS_FORDER="/res/Models/";
	private static final String MODELS_FORDER_NO_RES="Models/";
	
	public static void loadModels() {
		List<String[]> result=readLines(FILE, 1);
		for(String[] components:result) {
			int id=getComponentInt(components, 0);
			String name=getComponentString(components, 1);
			TexturedModel model= loadModel(name, Integer.toString(id));
			CachedModels.addTexturedModel(id, model);
		}
	}
	
	private static TexturedModel loadModel(String name, String id) {
		String folderName=id+"_"+name;
		String path=MODELS_FORDER+folderName;
		String pathNoRes=MODELS_FORDER_NO_RES+folderName;
		List<String[]> result=readLines(path+"/info.txt", 1);
		String[] components=result.get(0);
		String objFile = getComponentString(components, 0);
		String textureFile = getComponentString(components, 1);
		boolean transparency = getComponentBool(components, 2);
		int numberOfRows = getComponentInt(components, 3);
		float boundingRadius=getComponentFloat(components, 4);
		float shineDamper=getComponentFloat(components, 5);
		float reflectivity=getComponentFloat(components, 6);
		boolean fakeLighting=getComponentBool(components, 7);
		ModelTexture texture = new ModelTexture(Loader.loadTexture(pathNoRes+"/"+textureFile));
		texture.setHasTransparency(transparency);
		texture.setNumberOfRows(numberOfRows);
		texture.setShineDamper(shineDamper);
		texture.setReflectivity(reflectivity);
		texture.setFakeLighting(fakeLighting);
		TexturedModel model = new TexturedModel(Loader.loadToVAO(OBJFileLoader.loadOBJ(pathNoRes+"/"+objFile)), texture);
		model.setBoundingRadius(boundingRadius);
		return model;
	}
	
}
