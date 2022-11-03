package JE.Rendering;

import JE.Objects.Components.Component;
import JE.Objects.Components.ComponentRestrictions;
import JE.Security.GetClassCaller;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

public class Renderer extends Component {
    protected VAO vao = new VAO();

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
    public void Render() {
        //if(!GetClassCaller.getCallerClass(1).equals("JE.Window.Window")) return;
        glUseProgram(vao.shaderProgram.programID);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL20.GL_ARRAY_BUFFER, vao.vertexBufferID);
        glVertexAttribPointer(0,2,GL_FLOAT,false,0,0);
        glDrawArrays(GL_TRIANGLES, 0, vao.vertices.length);
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
}
