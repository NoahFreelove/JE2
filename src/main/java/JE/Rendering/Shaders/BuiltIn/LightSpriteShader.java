package JE.Rendering.Shaders.BuiltIn;

import JE.Rendering.Shaders.ShaderProgram;

public class LightSpriteShader extends ShaderProgram {
    public LightSpriteShader(){
        CreateShader("#version 330 core\n" +
                        "\n" +
                        "layout(location = 0) in vec2 vertexPos;\n" +
                        "layout(location = 1) in vec2 texCoord;\n" +
                        "uniform mat4 MVP;\n" +
                        "uniform vec3 world_position;\n" +
                        "\n" +
                        "out vec2 UV;\n" +
                        "out vec3 FragPos;\n" +
                        "\n" +
                        "void main(){\n" +
                        "  vec4 pos = MVP * vec4(vertexPos, world_position.z, 1);\n" +
                        "  gl_Position = pos;\n" +
                        "  FragPos = vec3(vertexPos, world_position.z);\n" +
                        "  UV = texCoord;\n" +
                        "}",

                "#version 330 core\n" +
                        "\n" +
                        "in vec2 UV;\n" +
                        "in vec3 FragPos;\n" +
                        "\n" +
                        "struct Light{\n" +
                        "    vec3 position;\n" +
                        "\tfloat constant;\n" +
                        "\tfloat linear;\n" +
                        "\tfloat quadratic;\n" +
                        "\tfloat intensity;\n" +
                        "\tvec3 ambient;\n" +
                        "\tvec3 diffuse;\n" +
                        "\tvec3 specular;\n" +
                        "};\n" +
                        "\n" +
                        "uniform int lightCount;\n" +
                        "uniform Light lights[1];\n" +
                        "uniform sampler2D JE_Texture;\n" +
                        "uniform vec3 world_position;\n" +
                        "uniform vec4 baseColor;\n" +
                        "\n" +
                        "out vec4 FragColor;\n" +
                        "void main(){\n" +
                        "vec3 pos = world_position;\n" +
                        "vec3 ambient = lights[0].ambient;\n" +
                        "\n" +
                        "vec3 norm = normalize(pos);\n" +
                        "vec3 lightDir = normalize(lights[0].position - pos);\n" +
                        "float diff = max(dot(norm, lightDir), 0.0);\n" +
                        "vec3 diffuse = lights[0].diffuse * diff;\n" +
                        "\n" +
                        "vec3 viewDir = normalize(-pos);\n" +
                        "vec3 reflectDir = reflect(-lightDir, norm);\n" +
                        "float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);\n" +
                        "vec3 specular = lights[0].specular * spec;\n" +
                        "\n" +
                        "float distance = length(lights[0].position - pos);\n" +
                        "float attenuation = 1.0 / (lights[0].constant + lights[0].linear * distance + lights[0].quadratic * (distance * distance));\n" +
                        "ambient *= attenuation;\n" +
                        "diffuse *= attenuation;\n" +
                        "specular *= attenuation;\n" +
                        "\n" +
                        "ambient *= lights[0].intensity;\n" +
                        "\n" +
                        "vec3 result = (ambient + diffuse + specular) * texture(JE_Texture, UV).rgb;\n" +
                        "FragColor = vec4(result, 1.0);\n" +
                        "}");
    }
}
