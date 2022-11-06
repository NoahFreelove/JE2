package JE.Rendering;

import JE.Logging.Logger;
import JE.Logging.Errors.ShaderError;
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
                        "layout(location = 0) in vec2 modelPos;\n" +
                        "layout(location = 1) in vec2 texCoord;\n" +
                        "\n" +
                        "uniform mat4 MVP;\n" +
                        "uniform float zPos;\n" +
                        "out vec2 UV;\n" +
                        "void main(){\n" +
                        "  vec4 pos = MVP * vec4(modelPos, zPos, 1);\n" +
                        "  gl_Position = pos;\n" +
                        "  UV = texCoord;\n" +
                        "}",

                "#version 330 core\n" +
                        "out vec4 FragColor;" +
                        "uniform sampler2D JE_Texture;\n" +
                        "in vec2 UV;\n" +
                        "\n" +
                        "void main(){\n" +
                        "  FragColor = texture(JE_Texture, UV);\n" +
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
