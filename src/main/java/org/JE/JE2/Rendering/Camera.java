package org.JE.JE2.Rendering;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Objects.Scripts.Common.Transform;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.*;

import java.lang.Math;
import java.lang.ref.PhantomReference;

public class Camera extends Script {
    public float zPos = 100;
    public Vector2f positionOffset = new Vector2f();
    public float zoomMultiplier = 1;
    public Vector4i viewportSize = Manager.defaultViewport();
    public Color backgroundColor = Color.BLACK;

    public Camera(){}

    @Override
    public void update() {
    }

    @Override
    public void start() {
    }

    @Override
    public void awake() {

    }

    private final Matrix4f view = new Matrix4f();
    public Matrix4f getPerspective(){
        view.identity();
        return view.perspective((float) Math.toRadians(45), (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y(), 0.1f, 100.0f);
    }

    /*public Matrix4f MVPPerspective(Transform t){
        Matrix4f model = getModel(t, true);
        Matrix4f view = getViewMatrix();
        return new Matrix4f().mul(getPerspective()).mul(view).mul(model);
    }*/

    private final Matrix4f model = new Matrix4f();
    public Matrix4f getModel(Transform t, boolean scale){
        model.identity();
        model.translate(t.position3D());
        Vector2f spriteSize = t.scale();

        model.translate(spriteSize.x()/2, spriteSize.y()/2, 0);
        model.rotate((float) Math.toRadians(t.rotation().z()), 0, 0, 1);
        model.translate(-spriteSize.x()/2, -spriteSize.y()/2, 0);

        if(scale)
            model.scale(t.scale().x(), t.scale().y(), 1);

        return model;
    }

    private final Vector2f position = new Vector2f();
    private final Matrix4f viewMatrix = new Matrix4f();
    public Matrix4f getViewMatrix(){

        
        if(getAttachedObject() == null) {
            position.set(0,0);
        }
        else
            position.set(getAttachedObject().getTransform().position());
        return viewMatrix.identity().translate(-(position.x + positionOffset.x), -(position.y + positionOffset.y), -zPos);
    }
    private final Matrix4f projection = new Matrix4f().identity();
    public Matrix4f getOrtho(){
        projection.identity();
        float aspect = (float) viewportSize.z / (float) viewportSize.w;
        float width = 4;
        float height = width/aspect;
        float near = 0;
        float far = 100;
        // take viewport position into account
        projection.translate(viewportSize.x, viewportSize.y, 0);
        projection.ortho(-width/2, width/2, -height/2f, height/2f, near, far);
        projection.scale(0.5f*zoomMultiplier);
        return projection;
    }


    private final Matrix4f mvpOrtho = new Matrix4f();
    public Matrix4f MVPOrtho(Transform t, boolean scale){
        mvpOrtho.identity();
        Matrix4f model = getModel(t,scale);
        Matrix4f view = getViewMatrix();

        return mvpOrtho.mul(getOrtho()).mul(view).mul(model);
    }
}
