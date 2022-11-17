package JE.Rendering.Shaders.BuiltIn;

import JE.Manager;
import JE.Objects.Lights.SimpleLight;
import JE.Rendering.Shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SimpleLightShader extends ShaderProgram {

    String vertex = "#version 330 core\n" +
            "layout(location = 0) in vec2 modelPos;\n" +
            "uniform mat4 MVP;\n" +
            "uniform float zPos;\n" +
            "uniform vec4 lightColor;\n" +
            "uniform vec2 lightSize;\n" +
            "out vec4 light_color;\n" +
            "out vec2 light_size;\n" +
            "out vec2 fragment_uv;\n" +
            "\n" +
            "void main(){\n" +
            "  glPosition = MVP * vec4(modelPos, zPos, 1);\n" +
            "  light_color = lightColor;\n" +
            "  light_size = lightSize;\n" +
            "  fragment_uv = vec2(modelPos.x, 1.0-modelPos.y);\n" +
            "}";
    String fragment = "#version 330 core\n" +
            "in vec4 light_color;\n" +
            "in vec2 light_size;\n" +
            "in vec2 fragment_uv;\n" +
            "out vec4 color;\n" +
            "void main(){\n" +
            "  float distance = length(vec2(1,1));\n" +
            "  color = vec4(light_color.xyz, light_color.w*(1.0-distance));\n" +
            "}";

    public SimpleLightShader(){
        CreateShader(vertex, fragment);
    }
    public SimpleLightShader(Vector2f size, Vector4f color){
        Runnable r = () -> {
            setUniform2f("lightSize", size);
            setUniform4f("lightColor", color);
            CreateShader(vertex, fragment);
        };
        Manager.QueueGLFunction(r);
    }
}
