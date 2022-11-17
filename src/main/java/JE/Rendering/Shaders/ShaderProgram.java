package JE.Rendering.Shaders;

import JE.Annotations.GLThread;
import JE.Logging.Logger;
import JE.Logging.Errors.ShaderError;
import JE.Manager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.File;
import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    public int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    public boolean vertexCompileStatus;
    public boolean fragmentCompileStatus;

    public ShaderProgram(){
        CreateShader(
                "#version 330 core\n" +
                        "layout(location = 0) in vec2 modelPos;\n" +
                        "\n" +
                        "uniform mat4 MVP;\n" +
                        "uniform float zPos;\n" +
                        "void main(){\n" +
                        "  vec4 pos = MVP * vec4(modelPos, zPos, 1);\n" +
                        "  gl_Position = pos;\n" +
                        "}",

                "#version 330 core\n" +
                        "out vec4 FragColor;" +
                        "\n" +
                        "void main(){\n" +
                        "  FragColor = vec4(1,0,0,1);\n" +
                        "}");
    }


    public ShaderProgram(String vertexShader, String fragmentShader){
        CreateShader(vertexShader, fragmentShader);
    }

    @GLThread
    public void setUniformMatrix4f(String name, FloatBuffer matrix){
        int location = glGetUniformLocation(programID, name);
        glUniformMatrix4fv(location, false, matrix);
    }

    @GLThread
    public void setUniform1f(String name, float value){
        int location = glGetUniformLocation(programID, name);
        glUniform1f(location, value);
    }

    @GLThread
    public void setUniform2f(String name, Vector2f value){
        int location = glGetUniformLocation(programID, name);
        glUniform2f(location, value.x, value.y);
    }
    @GLThread
    public void setUniform4f(String name, Vector4f value){
        int location = glGetUniformLocation(programID, name);
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void CreateShader(File vertex, File fragment){
        String vertexShader = "";
        String fragmentShader = "";

        FileInputStream vertexStream = null;
        FileInputStream fragmentStream = null;

        try {
            vertexStream = new FileInputStream(vertex);
            fragmentStream = new FileInputStream(fragment);

            int data = vertexStream.read();
            while(data != -1){
                vertexShader += (char)data;
                data = vertexStream.read();
            }

            data = fragmentStream.read();
            while(data != -1){
                fragmentShader += (char)data;
                data = fragmentStream.read();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                vertexStream.close();
                fragmentStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        CreateShader(vertexShader, fragmentShader);
    }

    public void CreateShader(String vertex, String fragment) {
        Runnable r = () -> {
            vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
            fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

            glShaderSource(vertexShaderID, vertex);
            glShaderSource(fragmentShaderID, fragment);
            glCompileShader(vertexShaderID);
            glCompileShader(fragmentShaderID);
            vertexCompileStatus = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == 1;
            fragmentCompileStatus = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == 1;

            if(!vertexCompileStatus){
                Logger.log(new ShaderError(vertex, true));
            }

            if(!fragmentCompileStatus)
            {
                Logger.log(new ShaderError(fragment, false));
            }

            programID = glCreateProgram();
            glAttachShader(programID, vertexShaderID);
            glAttachShader(programID, fragmentShaderID);
            glLinkProgram(programID);
            glValidateProgram(programID);

        };
        Manager.QueueGLFunction(r);
    }

    public void DeleteShader(){
        Runnable r = () -> {
            glDetachShader(programID, vertexShaderID);
            glDetachShader(programID, fragmentShaderID);
            glDeleteShader(vertexShaderID);
            glDeleteShader(fragmentShaderID);
            glDeleteProgram(programID);
        };
        Manager.QueueGLFunction(r);
    }
}
