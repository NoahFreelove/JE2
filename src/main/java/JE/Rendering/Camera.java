package JE.Rendering;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Base.Component;
import JE.Objects.Components.Common.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera extends Component {
    public GameObject parentObject = new GameObject();
    public float zPos = 100;
    public Vector2f positionOffset = new Vector2f();
    public float zoomMultiplier = 1;

    public Camera(){
    }
    public Camera(GameObject parent){
        this.parentObject = parent;
    }

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
        Matrix4f model = getModel(t);
        Matrix4f view = getViewMatrix();
        return new Matrix4f().mul(getPerspective()).mul(view).mul(model);
    }

    public Matrix4f getModel(Transform t){
        Matrix4f model = new Matrix4f().identity();
        model.translate(t.position.x(), t.position.y(), t.zPos);
        model = model.rotate(t.rotation.x, new Vector3f(1, 0, 0));
        model = model.rotate(t.rotation.y, new Vector3f(0, 1, 0));
        model = model.rotate(t.rotation.z, new Vector3f(0, 0, 1));
        model = model.scale(t.scale.x, t.scale.y, 1);
        return model;
    }


    public Matrix4f getViewMatrix(){
        Vector2f position =  parentObject.getTransform().position;
        Vector2f finalPos = new Vector2f(position.x + positionOffset.x, position.y + positionOffset.y);

        return new Matrix4f().identity().translate(-finalPos.x(), -finalPos.y(), -zPos);
    }

    public Matrix4f getOrtho(){
        Matrix4f projection = new Matrix4f().identity();
        float aspect = (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y();
        projection = projection.ortho(-aspect, aspect, -1,1f, 0f, 1000);
        projection = projection.scale(0.5f*zoomMultiplier);
        return projection;
    }


    public Matrix4f MVPOrtho(Transform t){
        Matrix4f model = getModel(t);
        Matrix4f view = getViewMatrix();

        return new Matrix4f().mul(getOrtho()).mul(view).mul(model);
    }
}
