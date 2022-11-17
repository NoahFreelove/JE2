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
    public Vector2f position = new Vector2f();
    public float zPos = 10;
    public Vector2f positionOffset = new Vector2f();

    public Camera(){
    }

    @Override
    public void update() {
        this.position = new Vector2f(parentObject.getTransform().position.x(), parentObject.getTransform().position.y()).add(positionOffset);
        this.zPos = parentObject.getTransform().zPos;
    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }

    public Matrix4f getMVP(Transform t){
        Matrix4f model = new Matrix4f().identity();
        model.translate(t.position.x(), t.position.y(), t.zPos);
        model = model.rotate(t.rotation.x, new Vector3f(1, 0, 0));
        model = model.rotate(t.rotation.y, new Vector3f(0, 1, 0));
        model = model.rotate(t.rotation.z, new Vector3f(0, 0, 1));
        model = model.scale(t.scale.x, t.scale.y, 1);

        Matrix4f projection = new Matrix4f().identity();
        projection = projection.perspective((float) Math.toRadians(45), (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y(), 0.1f, 100.0f);

        Matrix4f view = new Matrix4f().identity();
        Vector2f cameraPos = position;
        Vector3f cameraScenePos = new Vector3f(cameraPos, zPos);
        Vector3f direction = new Vector3f(0, 0, -1);
        Vector3f addedPos = new Vector3f();
        cameraScenePos.add(direction, addedPos);
        view = view.lookAt(cameraScenePos, addedPos, new Vector3f(0, 1, 0));

        return new Matrix4f().mul(projection).mul(view).mul(model);
    }
}
