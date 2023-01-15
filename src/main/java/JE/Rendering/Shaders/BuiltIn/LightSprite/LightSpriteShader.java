package JE.Rendering.Shaders.BuiltIn.LightSprite;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

public class LightSpriteShader extends ShaderProgram {
    public LightSpriteShader(){
        //CreateShader(new File("shaders/LightSprite/lightSprite.vert"), new File("shaders/LightSprite/lightSprite.frag"));
        CreateShader("#version 330 core\n" +
                        "\n" +
                        "layout(location = 0) in vec2 vertexPos;\n" +
                        "layout(location = 1) in vec2 texCoord;\n" +
                        "\n" +
                        "uniform mat4 MVP;\n" +
                        "\n" +
                        "uniform mat4 model;\n" +
                        "uniform mat4 view;\n" +
                        "uniform mat4 projection;\n" +
                        "\n" +
                        "uniform vec3 world_position;\n" +
                        "\n" +
                        "out vec2 UV;\n" +
                        "out vec3 FragPos;\n" +
                        "\n" +
                        "\n" +
                        "void main(){\n" +
                        "  gl_Position =  MVP * vec4(vertexPos, world_position.z, 1);\n" +
                        "  vec3 combinedPos = vec3(vertexPos, world_position.z);\n" +
                        "  FragPos = vec3(model* vec4(combinedPos,1));\n" +
                        "  UV = texCoord;\n" +
                        "}",

                "#version 330 core\n" +
                        "\n" +
                        "#define MAX_LIGHTS 32\n" +
                        "\n" +
                        "in vec2 UV;\n" +
                        "in vec3 FragPos;\n" +
                        "\n" +
                        "struct Light{\n" +
                        "    vec3 position;\n" +
                        "    vec4 color;\n" +
                        "\tfloat intensity;\n" +
                        "\tvec3 ambient;\n" +
                        "\tvec3 diffuse;\n" +
                        "    float radius;\n" +
                        "    int has_bounds;\n" +
                        "    vec2 bound_pos;\n" +
                        "    vec2 bound_range;\n" +
                        "    int type; // 0 - completely lit | 1 - point light | 2 - area light\n" +
                        "};\n" +
                        "\n" +
                        "uniform mat4 model;\n" +
                        "\n" +
                        "uniform int light_count;\n" +
                        "uniform Light lights[MAX_LIGHTS];\n" +
                        "uniform sampler2D JE_Texture;\n" +
                        "uniform sampler2D JE_Normal;\n" +
                        "uniform vec3 world_position;\n" +
                        "uniform vec4 base_color;\n" +
                        "\n" +
                        "out vec4 color;\n" +
                        "\n" +
                        "void main(){\n" +
                        "\n" +
                        "    vec3 total_light = vec3(0,0,0);\n" +
                        "\n" +
                        "    for(int i = 0; i < light_count; i++){\n" +
                        "        Light light = lights[i];\n" +
                        "\n" +
                        "        vec3 lightDir = light.position - FragPos;\n" +
                        "\n" +
                        "        // Calculate the distance between the fragment and the light position\n" +
                        "        float dist = length(lightDir);\n" +
                        "\n" +
                        "        lightDir = mat3(transpose(inverse(model))) * lightDir;\n" +
                        "\n" +
                        "        // Normalize the light direction vector\n" +
                        "        lightDir = normalize(lightDir);\n" +
                        "\n" +
                        "        // Look up the normal from the normal map\n" +
                        "        vec3 normal = texture(JE_Normal, UV).rgb;\n" +
                        "\n" +
                        "        float diffuse = 1;\n" +
                        "        float falloff = 1;\n" +
                        "        float intensity = lights[i].intensity;\n" +
                        "\n" +
                        "        if(light.type == 1){\n" +
                        "            // Calculate the falloff factor using the smoothstep function\n" +
                        "            falloff = smoothstep(light.radius, light.radius - 2, dist);\n" +
                        "            // Calculate the diffuse lighting\n" +
                        "            diffuse = abs(dot(lightDir, normal));\n" +
                        "        }\n" +
                        "        else if (light.type == 2){\n" +
                        "            dist = 1;\n" +
                        "            if(light.has_bounds == 1){\n" +
                        "                if(world_position.x < light.bound_pos.x || world_position.x > light.bound_pos.x + light.bound_range.x){\n" +
                        "                    intensity = 0;\n" +
                        "                }\n" +
                        "                else if(world_position.y < light.bound_pos.y || world_position.y > light.bound_pos.y + light.bound_range.y){\n" +
                        "                    intensity = 0;\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        else if (light.type == 0){\n" +
                        "            dist = 1;\n" +
                        "            intensity = 1;\n" +
                        "        }\n" +
                        "\n" +
                        "        total_light += light.color.rgb * diffuse * falloff * intensity / (dist * dist);\n" +
                        "    }\n" +
                        "    color = texture(JE_Texture, UV) * vec4(total_light, 1.0);\n" +
                        "}");
        supportsLighting = true;
    }
}
