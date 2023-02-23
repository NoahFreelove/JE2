package JE.Rendering;

import JE.Manager;
import JE.Objects.Scripts.Base.Script;
import JE.Objects.Scripts.Common.Transform;
import JE.UI.UIElements.Style.Color;
import org.joml.*;

import java.lang.Math;

public class Camera extends Script {
    public float zPos = 100;
    public Vector2f positionOffset = new Vector2f();
    public float zoomMultiplier = 1;
    public Vector4i viewportSize = Manager.defaultViewport();
    public Color backgroundColor = Color.BLACK;

    @Override
    public void update() {
    }

    @Override
    public void start() {
    }

    @Override
    public void awake() {

    }

    public Matrix4f getPerspective(){
        return new Matrix4f().identity().perspective((float) Math.toRadians(45), (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y(), 0.1f, 100.0f);
    }

    public Matrix4f MVPPerspective(Transform t){
        Matrix4f model = getModel(t, true);
        Matrix4f view = getViewMatrix();
        return new Matrix4f().mul(getPerspective()).mul(view).mul(model);
    }

    public Matrix4f getModel(Transform t, boolean scale){
        Matrix4f model = new Matrix4f().identity();
        model.translate(t.position3D());
        Vector2f spriteSize = t.scale();

        model.translate(spriteSize.x()/2, spriteSize.y()/2, 0);
        model.rotate((float) Math.toRadians(t.rotation().z()), 0, 0, 1);
        model.translate(-spriteSize.x()/2, -spriteSize.y()/2, 0);

        if(scale)
            model.scale(t.scale().x(), t.scale().y(), 1);

        return model;
    }


    public Matrix4f getViewMatrix(){
        Vector2f position =  getAttachedObject().getTransform().position();
        Vector2f finalPos = new Vector2f(position.x + positionOffset.x, position.y + positionOffset.y);
        return new Matrix4f().identity().translate(-finalPos.x(), -finalPos.y(), -zPos);
    }

    public Matrix4f getOrtho(){
        Matrix4f projection = new Matrix4f().identity();
        float aspect = (float) viewportSize.z / (float) viewportSize.w;
        float width = 4;
        float height = width/aspect;
        float near = 0;
        float far = 100;
        // take viewport position into account
        projection = projection.translate(viewportSize.x, viewportSize.y, 0);
        projection = projection.ortho(-width/2, width/2, -height/2f, height/2f, near, far);
        projection = projection.scale(0.5f*zoomMultiplier);
        return projection;
    }


    public Matrix4f MVPOrtho(Transform t, boolean scale){
        Matrix4f model = getModel(t,scale);
        Matrix4f view = getViewMatrix();

        return new Matrix4f().mul(getOrtho()).mul(view).mul(model);
    }
}
