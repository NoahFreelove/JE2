package JE.Rendering.Shaders.BuiltIn;

import JE.Rendering.Shaders.ShaderProgram;

public class SpriteShader extends ShaderProgram {

    public SpriteShader(){
        CreateShader("#version 330 core\n" +
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

}
