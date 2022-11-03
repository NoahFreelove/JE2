package JE.Rendering;

import JE.Manager;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    public int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(){
        CreateShader("void main(){gl_Position = vec4(0.0, 0.0, 0.0, 1.0);}", "void main(){gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);}");
    }

    public void CreateShader(String vertex, String fragment) {
        Runnable r = () -> {
            vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
            fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

            glShaderSource(vertexShaderID, vertex);
            glShaderSource(fragmentShaderID, fragment);
            glCompileShader(vertexShaderID);
            glCompileShader(fragmentShaderID);
            glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
            glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);

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
