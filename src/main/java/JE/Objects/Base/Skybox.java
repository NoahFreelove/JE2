package JE.Objects.Base;

import JE.Objects.Square;
import JE.Rendering.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_POLYGON;

public class Skybox extends Sprite {
    public Skybox(Vector4f color){
        super( // Vertices
                new Vector2f[]{
                        new Vector2f(Integer.MIN_VALUE,Integer.MIN_VALUE),
                        new Vector2f(Integer.MAX_VALUE,Integer.MIN_VALUE),
                        new Vector2f(Integer.MAX_VALUE,Integer.MAX_VALUE),
                        new Vector2f(Integer.MIN_VALUE,Integer.MAX_VALUE)
                },
                // UVs
                new Vector2f[]{
                        new Vector2f(0,0),
                        new Vector2f(1,0),
                        new Vector2f(1,1),
                        new Vector2f(0,1)
                },
                new ShaderProgram(
                        "#version 330 core\n" +
                                "layout(location = 0) in vec2 modelPos;\n" +
                                "\n" +
                                "uniform mat4 MVP;\n" +
                                "uniform float zPos;\n" +
                                "void main(){\n" +
                                "  gl_Position = MVP * vec4(modelPos, zPos, 100);\n" +
                                "}",

                        "#version 330 core\n" +
                                "out vec4 FragColor;" +
                                "\n" +
                                "void main(){\n" +
                                "  FragColor = vec4(" + color.x + ", " + color.y + ", " + color.z + "," + color.w + ");\n" +
                                "}"

                ));
        renderer.setDrawMode(GL_POLYGON);
    }
}
