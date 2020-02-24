package org.appproductions.frustumCulling;

import java.util.List;

import org.appproductions.entities.Entity;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FrustumCullingFilter {

	private final Matrix4f prjViewMatrix;

    private FrustumIntersection frustumInt;

    public FrustumCullingFilter() {
        prjViewMatrix = new Matrix4f();
        frustumInt = new FrustumIntersection();
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        // Calculate projection view matrix
        prjViewMatrix.set(projMatrix);
        prjViewMatrix.mul(viewMatrix);
        // Update frustum intersection class
        frustumInt.set(prjViewMatrix);
    }

    private boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumInt.testSphere(x0, y0, z0, boundingRadius);
    }

	public void filter(List<Entity> gameItems, float meshBoundingRadius) {
		float boundingRadius;
		Vector3f pos;
		for (Entity gameItem : gameItems) {
			boundingRadius = gameItem.getScale() * meshBoundingRadius;
			pos = gameItem.getPosition();
			gameItem.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
		}
	}

	public void filter(Entity entity, float meshBoundingRadius) {
		float boundingRadius;
		Vector3f pos;
		boundingRadius = entity.getScale() * meshBoundingRadius;
		pos = entity.getPosition();
		entity.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
	}
}
