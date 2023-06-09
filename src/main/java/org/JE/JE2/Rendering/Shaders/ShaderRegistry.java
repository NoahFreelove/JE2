package org.JE.JE2.Rendering.Shaders;

public class ShaderRegistry {
    public static final String DEFAULT_VERTEX = """
            #version 330 core
            layout(location = 0) in vec2 vertexPos;

            uniform mat4 MVP;
            uniform vec3 world_position;
            void main(){
              vec4 pos = MVP * vec4(vertexPos, world_position.z, 1);
              gl_Position = pos;
            }""";

    public static final String DEFAULT_FRAGMENT = """
            #version 330 core
            out vec4 FragColor;
            struct Material{
                vec4 base_color;
            };

            uniform Material material;
            void main() {
                FragColor = material.base_color;
            }""";

    public static final String SPRITE_VERTEX = """
            #version 330 core
            layout(location = 0) in vec2 modelPos;
            layout(location = 1) in vec2 texCoord;

            uniform mat4 MVP;
            uniform float zPos;
            out vec2 UV;
            void main(){
              vec4 pos = MVP * vec4(modelPos, zPos, 1);
              gl_Position = pos;
              UV = texCoord;
            }""";

    public static final String SPRITE_FRAGMENT = """
            #version 330 core
            out vec4 FragColor;
            uniform sampler2D JE_Texture;
            in vec2 UV;
            uniform int use_texture;

            struct Material{
                vec4 base_color;
                vec3 ambient;
                vec3 diffuse;
                vec3 specular;
                float shininess;
            };

            uniform Material material;

            void main(){
                if(use_texture == 1){
                    FragColor = texture(JE_Texture, UV);
                }
                else if (use_texture == 0){
                    FragColor = material.base_color;
                }
            }""";

    public static final String LIGHTSPRITE_VERTEX = """
            #version 330 core

            layout(location = 0) in vec2 vertexPos;
            layout(location = 1) in vec2 texCoord;

            uniform mat4 MVP;

            uniform mat4 model;
            uniform mat4 view;
            uniform mat4 projection;

            uniform vec3 world_position;

            out vec2 UV;
            out vec3 FragPos;


            void main(){
              gl_Position =  MVP * vec4(vertexPos, world_position.z, 1);
              vec3 combinedPos = vec3(vertexPos, world_position.z);
              FragPos = vec3(model* vec4(combinedPos,1));
              UV = texCoord;
            }""";

    public static final String LIGHTSPRITE_FRAGMENT = """
            #version 330 core

            #define MAX_LIGHTS 32

            in vec2 UV;
            in vec3 FragPos;
            uniform mat4 model;
            uniform vec3 world_position;

            struct Light{
                vec3 position;
                vec4 color;
            \tfloat intensity;
            \tvec3 ambient;
            \tfloat quadratic;
                float linear;
                float constant;
                float radius;
                int has_bounds;
                vec2 bound_pos;
                vec2 bound_range;
                int type; // 0 - completely lit | 1 - point light | 2 - area light
            };
            uniform int layer;

            struct Material{
                vec4 base_color;
                vec3 ambient;
                vec3 diffuse;
                vec3 specular;
                float shininess;
            };

            uniform Material material;
            uniform Light lights[MAX_LIGHTS];
            uniform int light_count;

            uniform sampler2D JE_Texture;
            uniform sampler2D JE_Normal;

            uniform int use_texture;

            out vec4 color;

            void main(){

                vec3 total_light = vec3(0,0,0);

                for(int i = 0; i < light_count; i++){
                    Light light = lights[i];

                    vec3 lightDir = light.position - FragPos;

                    // Calculate the distance between the fragment and the light position
                    float dist = length(lightDir);

                    lightDir = mat3(transpose(inverse(model))) * lightDir;

                    // Normalize the light direction vector
                    lightDir = normalize(lightDir);

                    // Look up the normal from the normal map

                    vec3 normal = normalize(vec3(0,0,1));
                    if(use_texture == 1) {
                        normal = texture(JE_Normal, UV).rgb;
                    }
                    float falloff = 1;
                    float intensity = light.intensity;

                    if(light.type == 1){
                        // Calculate the falloff factor using the smoothstep function
                        // Calculate the light intensity using falloff
                        float attenuation = 1.0 / (light.constant + light.linear * dist +
                        light.quadratic * (dist * dist));

                        // Adjust attenuation based on radius
                        attenuation = clamp(1.0 - (dist / light.radius), 0.0, 1.0) * attenuation;

                        // Calculate diffuse lighting using the fragment normal and light direction
                        falloff = smoothstep(0.0,1.0,attenuation);

                    }
                    else if (light.type == 2){
                        dist = 1;
                        if(world_position.x < light.bound_pos.x || world_position.x > light.bound_pos.x + light.bound_range.x){
                            intensity = 0;
                        }
                        else if(world_position.y < light.bound_pos.y || world_position.y > light.bound_pos.y + light.bound_range.y){
                            intensity = 0;
                        }
                    }
                    else if (light.type == 0){
                        dist = 1;
                        intensity = 1;
                    }
                   \s
                    total_light += light.color.rgb * falloff * intensity;
                }
                if(use_texture == 1){
                    color = texture(JE_Texture, UV) * vec4(total_light, 1.0);
                }
                else if (use_texture == 0){
                    color = material.base_color * vec4(total_light, 1.0);
                }
            }""";

    public static final String QUAD_VERTEX = """
            #version 330 core

            layout(location = 0) in vec2 vertexPosition;
            layout(location = 1) in vec2 vertexTexCoord;

            out vec2 UV;

            void main()
            {
                gl_Position = vec4(vertexPosition * 2.0 - 1.0, 0.0, 1.0);
                UV = vertexTexCoord;
            }""";
    public static final String QUAD_FRAGMENT = """
            #version 330 core

            in vec2 UV;
            out vec4 fragColor;

            uniform sampler2D textureSampler;

            void main()
            {
                fragColor = texture(textureSampler, UV);
            }
            """;
    public static final ShaderModule defaultShaderModule = new ShaderModule(DEFAULT_VERTEX,DEFAULT_FRAGMENT,false,false);
    public static final ShaderModule spriteShaderModule = new ShaderModule(SPRITE_VERTEX,SPRITE_FRAGMENT,true,false);
    public static final ShaderModule lightSpriteShaderModule = new ShaderModule(LIGHTSPRITE_VERTEX,LIGHTSPRITE_FRAGMENT,true,true);
    public static final ShaderModule quadShaderModule = new ShaderModule(QUAD_VERTEX,QUAD_FRAGMENT,false,false);

    public static ShaderProgram Default;
    public static ShaderProgram LightSprite;
    public static ShaderProgram Sprite;
    public static ShaderProgram Quad;

    public static void loadDefaultShaders(){
        Default = new ShaderProgram(defaultShaderModule);
        LightSprite = new ShaderProgram(lightSpriteShaderModule);
        Sprite = new ShaderProgram(spriteShaderModule);
        Quad = new ShaderProgram(quadShaderModule);
    }
}
