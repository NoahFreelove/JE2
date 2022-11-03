package JE.Rendering;

import JE.Manager;

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
                "layout(location = 0) in vec3 vertexPosition_modelspace;\n" +
                "void main(){  \n" +
                "  gl_Position.xyz = vertexPosition_modelspace;\n" +
                "  gl_Position.w = 1.0;\n" +
                "}\n",

                "#version 330 core\n" +
                "out vec3 color;\n" +
                "void main(){\n" +
                "  color = vec3(1,0,0);\n" +
                "}");
    }

    public void CreateShader(String vertex, String fragment) {
        Runnable r = () -> {
            vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
            fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

            glShaderSource(vertexShaderID, vertex);
            glShaderSource(fragmentShaderID, fragment);
            glCompileShader(vertexShaderID);
            glCompileShader(fragmentShaderID);
            //System.out.println("Vertex Shader Status: " + glGetShaderi(vertexShaderID, GL_COMPILE_STATUS));
            vertexCompileStatus = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == 1;
            //System.out.println("Fragment Shader Status: " + glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS));
            fragmentCompileStatus = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == 1;

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
