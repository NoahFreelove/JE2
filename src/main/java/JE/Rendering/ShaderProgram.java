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
                        "layout(location = 0) in vec2 modelPos;\n" +
                        "uniform mat4 MVP;\n" +
                        "uniform float zPos;\n" +
                        "out vec2 UV;\n" +
                        "void main(){\n" +
                        "  vec4 pos = MVP * vec4(modelPos, zPos, 1);\n" +
                        "  gl_Position = pos;\n" +
                        "  UV = modelPos;\n" +
                        "}\n",

                "#version 330 core\n" +
                        "out vec3 color;\n" +
                        "uniform sampler2D JE_Texture;\n" +
                        "in vec2 UV;\n" +
                        "\n" +
                        "void main(){\n" +
                        "  color = texture(JE_Texture, UV).rgb;\n" +
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
            System.out.println("Vertex Shader Status: " + glGetShaderi(vertexShaderID, GL_COMPILE_STATUS));
            vertexCompileStatus = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == 1;
            System.out.println("Fragment Shader Status: " + glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS));
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
