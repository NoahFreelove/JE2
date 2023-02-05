package JE.Objects.Lights;

import JE.Annotations.GLThread;
import JE.Manager;
import JE.Objects.Scripts.Base.Script;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Scene.Scene;
import JE.UI.UIElements.Style.Color;
import org.joml.Vector3f;

public abstract class Light extends Script {
    protected Color color;
    public float intensity;
    public int type;
    public int[] affectedLayers = new int[]{0};

    public Light(Color color, float intensity, int type) {
        this.color = color;
        this.intensity = intensity;
        this.type = type;
    }

    @GLThread
    public final void setLighting(ShaderProgram shaderProgram, int index){
        if(getAttachedObject() == null)
            return;
        shaderProgram.setUniform3f("lights[" + index + "].position", new Vector3f(getAttachedObject().getTransform().position(), getAttachedObject().getTransform().zPos()));
        shaderProgram.setUniform1i("lights[" + index + "].type", type);
        shaderProgram.setUniform4f("lights[" + index + "].color", color.getVec4());
        shaderProgram.setUniform1f("lights[" + index + "].intensity", intensity);
        setLightSpecific(shaderProgram, index);
    }

    @GLThread
    public final void hideLighting(ShaderProgram shaderProgram, int index){
        if(getAttachedObject() == null)
            return;
        shaderProgram.setUniform3f("lights[" + index + "].position", new Vector3f(getAttachedObject().getTransform().position(), getAttachedObject().getTransform().zPos()));
        shaderProgram.setUniform1i("lights[" + index + "].type", type);
        shaderProgram.setUniform4f("lights[" + index + "].color", Color.TRANSPARENT.getVec4());
        shaderProgram.setUniform1f("lights[" + index + "].intensity", 0);
    }

    public Light setColor(Color c){
        this.color = c;
        return this;
    }

    @GLThread
    protected abstract void setLightSpecific(ShaderProgram shaderProgram, int index);

    @Override
    public void gameObjectAddedToScene(Scene scene) {
        scene.addLight(this);
    }

    @Override
    public void start() {
        Manager.activeScene().addLight(this);
    }
}
