#version 330 core

#define MAX_LIGHTS 32

in vec2 UV;
in vec3 FragPos;

struct Light{
    vec3 position;
    vec4 color;
	float intensity;
	vec3 ambient;
	vec3 diffuse;
    float radius;
    int has_bounds;
    vec2 bound_pos;
    vec2 bound_range;
    int type; // 0 - completely lit | 1 - point light | 2 - area light
};

uniform mat4 model;

uniform int light_count;
uniform Light lights[MAX_LIGHTS];
uniform sampler2D JE_Texture;
uniform sampler2D JE_Normal;
uniform int use_texture;
uniform vec3 world_position;
uniform vec4 base_color;

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
        vec3 normal = texture(JE_Normal, UV).rgb;

        float diffuse = 1;
        float falloff = 1;
        float intensity = lights[i].intensity;

        if(light.type == 1){
            // Calculate the falloff factor using the smoothstep function
            falloff = smoothstep(light.radius, light.radius - 2, dist);
            // Calculate the diffuse lighting
            diffuse = abs(dot(lightDir, normal));
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

        total_light += light.color.rgb * diffuse * falloff * intensity / (dist * dist);
    }
    if(use_texture == 1){
        color = texture(JE_Texture, UV) * vec4(total_light, 1.0);
    }
    else if (use_texture == 0){
        color = base_color * vec4(total_light, 1.0);
    }
}