package org.appproductions.entities;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
	
	private static List<Entity> entities=new ArrayList<Entity>();
	
	public static void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public static void addAll(List<Entity> entities) {
		EntityManager.entities.addAll(entities);
	}
	
	public static void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public static List<Entity> getEntities(){
		return entities;
	}
	
}
