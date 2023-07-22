package org.JE.JE2.Rendering;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.joml.*;

import java.lang.Math;

public class Camera extends Script {
    public float zPos = 100;

    public boolean checkRenderDistance = true;
    public float renderDistance = 10;

    public Vector2f positionOffset = new Vector2f();
    public float zoomMultiplier = 1f;

    private float width = 5; // How many units can the camera see in any direction
    private float height;
    public Vector4f viewportSize = new Vector4f();
    public boolean useDefaultViewport = true;

    public Color backgroundColor = Color.BLACK;

    private Vector4f defaultViewportRef = new Vector4f();

    public Camera(){}

    @Override
    public void update() {
        defaultViewportRef = Manager.defaultViewport();
        if(useDefaultViewport)
            viewportSize.set(viewportSize.x,viewportSize.y,defaultViewportRef.z,defaultViewportRef.w);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void awake() {
        super.awake();
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

    private final Vector2f viewMatrixPos = new Vector2f();
    private final Matrix4f viewMatrix = new Matrix4f();
    public Matrix4f getViewMatrix(){
        if(getAttachedObject() == null) {
            viewMatrixPos.set(0,0);
        }
        else
            viewMatrixPos.set(getAttachedObject().getTransform().position());
        return viewMatrix.identity().translate(-(viewMatrixPos.x + positionOffset.x), -(viewMatrixPos.y + positionOffset.y), -zPos);
    }

    private final Matrix4f projection = new Matrix4f().identity();
    public Matrix4f getOrtho(){
        projection.identity();
        float aspect = (float) viewportSize.z / (float) viewportSize.w;

        height = width/aspect;
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

    public boolean withinRenderDistance(Vector2f pos, Vector2f scale){
        if(!checkRenderDistance)
            return true;

        if(getAttachedObject() == null){
            // Use position offset as camera position
            // Take objects x and y scale into account
            return (pos.x() + scale.x() > positionOffset.x - renderDistance &&
                    pos.x() - scale.x() < positionOffset.x + renderDistance &&
                    pos.y() + scale.y() > positionOffset.y - renderDistance &&
                    pos.y() - scale.y() < positionOffset.y + renderDistance);
        }
        else{
            // Use attached object position as camera position
            // Take objects x and y scale into account
            // Take position offset into account
            return (pos.x() + scale.x() > getAttachedObject().getTransform().position().x() - renderDistance + positionOffset.x() &&
                    pos.x() - scale.x() < getAttachedObject().getTransform().position().x() + renderDistance + positionOffset.x() &&
                    pos.y() + scale.y() > getAttachedObject().getTransform().position().y() - renderDistance + positionOffset.y() &&
                    pos.y() - scale.y() < getAttachedObject().getTransform().position().y() + renderDistance + positionOffset.y());


        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
