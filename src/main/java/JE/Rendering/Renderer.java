package JE.Rendering;

import JE.Manager;
import JE.Objects.Components.Component;
import JE.Objects.Components.ComponentRestrictions;
import JE.Objects.Components.Transform;
import JE.Security.GetClassCaller;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Component {
    protected VAO vao = new VAO();
    protected int drawMode = GL_TRIANGLES;

    public Renderer(){
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public Renderer(VAO vao){
        this.vao = vao;
        restrictions = new ComponentRestrictions(false, true, true);
    }

    public void SetVertices(Vector2f[] vertices){
        vao.setVertices(vertices);
    }
    public void SetShader(ShaderProgram shader){
        vao.setShaderProgram(shader);
    }

    // When GL methods are called from the window class, we don't need to wrap in a runnable because its already in the GL thread
    public void Render(Transform t) {
        //if(!GetClassCaller.getCallerClass(1).equals("JE.Window.Window")) return;

        Matrix4f model = new Matrix4f().identity();
        model.translate(t.position.x(), t.position.y(), t.zPos);
        model = model.rotate(t.rotation.x, new Vector3f(1, 0, 0));
        model = model.rotate(t.rotation.y, new Vector3f(0, 1, 0));
        model = model.rotate(t.rotation.z, new Vector3f(0, 0, 1));
        model = model.scale(t.scale.x, t.scale.y, 1);


        Matrix4f projection = new Matrix4f().identity();
        projection = projection.perspective((float) Math.toRadians(45), (float) Manager.getWindowSize().x() / (float) Manager.getWindowSize().y(), 0.1f, 100.0f);
        Matrix4f view = new Matrix4f().identity();

        Vector2f cameraPos = Manager.getActiveScene().activeCamera.position;
        Vector3f cameraScenePos = new Vector3f(cameraPos, Manager.getActiveScene().activeCamera.zPos);
        Vector3f direction = new Vector3f(0, 0, -1);
        Vector3f addedPos = new Vector3f();
        cameraScenePos.add(direction, addedPos);
        view = view.lookAt(cameraScenePos, addedPos, new Vector3f(0, 1, 0));


        Matrix4f mvp = new Matrix4f().mul(projection).mul(view).mul(model);


        int mvpID = glGetUniformLocation(vao.shaderProgram.programID, "MVP");
        glUniformMatrix4fv(mvpID, false, mvp.get(BufferUtils.createFloatBuffer(16)));

        int zPos = glGetUniformLocation(vao.shaderProgram.programID, "zPos");
        glUniform1f(zPos,t.zPos);

        glUseProgram(vao.shaderProgram.programID);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL20.GL_ARRAY_BUFFER, vao.vertexBufferID);
        glVertexAttribPointer(0,2,GL_FLOAT,false,0,0);
        glDrawArrays(drawMode, 0, vao.vertices.length);
        glDisableVertexAttribArray(0);


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

    public void setDrawMode(int mode){
        drawMode = mode;
    }

}
