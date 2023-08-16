package org.extremely.engine.core.components;

import org.extremely.engine.core.SceneComponent;
import org.extremely.engine.rendering.Material;
import org.extremely.engine.rendering.Mesh;
import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Shader;

public class MeshRenderer extends SceneComponent {
    private final Mesh mesh;
    private final Material material;

    public MeshRenderer(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine) {
        shader.bind();
        shader.updateUniforms(getTransform().getWorldMatrix(), material, renderingEngine);
//        shader.updateUniforms(material, renderingEngine);
        mesh.draw();
    }
}
