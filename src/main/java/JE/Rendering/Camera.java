package JE.Rendering;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Components.Component;
import JE.Objects.Components.Transform;
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
    public void Update() {
    }

    @Override
    public void Start() {

    }

    @Override
    public void awake() {

    }

    public Matrix4f getPerspective(Transform t){
        Matrix4f model = getModel(t);
        Matrix4f view = getView();
        Matrix4f projection = new Matrix4f().identity();

        projection = projection.perspective((float) Math.toRadians(45), (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y(), 0.1f, 100.0f);


        return new Matrix4f().mul(projection).mul(view).mul(model);
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


    public Matrix4f getView(){
        Vector2f position =  parentObject.getTransform().position;
        Vector2f finalPos = new Vector2f(position.x + positionOffset.x, position.y + positionOffset.y);

        return new Matrix4f().identity().translate(-finalPos.x(), -finalPos.y(), -zPos);
    }


    public Matrix4f getOrthographic(Transform t){
        Matrix4f model = getModel(t);
        Matrix4f view = getView();
        Matrix4f projection = new Matrix4f().identity();

        float aspect = (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y();
        projection = projection.ortho(-aspect, aspect, -1,1f, 0.1f, zPos);
        projection = projection.scale(0.5f*zoomMultiplier);

        return new Matrix4f().mul(projection).mul(view).mul(model);
    }
}
