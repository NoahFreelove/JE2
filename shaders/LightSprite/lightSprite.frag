#version 330 core

#define MAX_LIGHTS 32

in vec2 UV;
in vec3 FragPos;
uniform mat4 model;
uniform vec3 world_position;

struct Light{
    vec3 position;
    vec4 color;
	float intensity;
	vec3 ambient;
	float quadratic;
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
uniform int use_lighting;

out vec4 color;

vec3 calculateLight(){
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

        vec3 other_factor = vec3(1,1,1);

        if(light.type == 1){
            // POINT LIGHT
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

        float diffuseFactor = max(dot(normal, lightDir), 0.0);

        total_light += light.color.rgb * falloff * intensity * other_factor;
    }
    return total_light;
}

void main(){

    vec3 total_light = vec3(1,1,1);
    if(use_lighting == 1){
        total_light = calculateLight();
    }

    if(use_texture == 1){
        color = texture(JE_Texture, UV) * vec4(total_light, 1.0);
    }
    else if (use_texture == 0){
        color = material.base_color * vec4(total_light, 1.0);
    }
}