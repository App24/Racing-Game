package org.appproductions.fileParser;

import java.util.ArrayList;
import java.util.List;

import org.appproductions.cache.CachedModels;
import org.appproductions.entities.Entity;
import org.appproductions.entities.EntityManager;
import org.appproductions.models.TexturedModel;
import org.appproductions.terrains.Terrain;
import org.joml.Vector3f;

public class WorldLoader extends FileParser {

	private static final String FILE = "/res/loadWorld.txt";

	public static void loadEntities() {
		List<String[]> result = readLines(FILE, 1);
		List<Entity> entities = new ArrayList<Entity>();
		for (String[] components : result) {
			TexturedModel model = CachedModels.getTexturedModel(getComponentInt(components, 0));
			Vector3f position = new Vector3f(getComponentFloat(components, 1), getComponentFloat(components, 2),
					getComponentFloat(components, 3));
			float rotX = getComponentFloat(components, 4);
			float rotY = getComponentFloat(components, 5);
			float rotZ = getComponentFloat(components, 6);
			float scale = getComponentFloat(components, 7);
			int textureIndex = getComponentInt(components, 8);
			Entity entity = new Entity(model, position, rotX, rotY, rotZ, scale, textureIndex);
			entities.add(entity);
		}
		EntityManager.addAll(entities);
	}

	public static void loadEntities(List<Terrain> terrains) {
		List<String[]> result = readLines(FILE, 1);
		List<Entity> entities = new ArrayList<Entity>();
		for (String[] components : result) {
			TexturedModel model = CachedModels.getTexturedModel(getComponentInt(components, 0));
			Vector3f position = new Vector3f(getComponentFloat(components, 1), getComponentFloat(components, 2),
					getComponentFloat(components, 3));
			for (Terrain terrain : terrains) {
				if (terrain.isInsideTerrain(position.x, position.y)) {
					position.y = terrain.getHeightOfTerrain(position.x, position.z);
				}
			}
			float rotX = getComponentFloat(components, 4);
			float rotY = getComponentFloat(components, 5);
			float rotZ = getComponentFloat(components, 6);
			float scale = getComponentFloat(components, 7);
			int textureIndex = getComponentInt(components, 8);
			Entity entity = new Entity(model, position, rotX, rotY, rotZ, scale, textureIndex);
			entities.add(entity);
		}
		EntityManager.addAll(entities);
	}

}
