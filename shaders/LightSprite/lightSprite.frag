#version 330 core

#define MAX_LIGHTS 32

in vec2 UV;
in vec3 FragPos;
struct Light{
    vec3 position;
    vec4 color;
	float constant;
	float linear;
	float quadratic;
	float intensity;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
    float radius;
};

uniform mat4 model;

uniform int light_count;
uniform Light lights[MAX_LIGHTS];
uniform sampler2D JE_Texture;
uniform sampler2D JE_Normal;
uniform vec3 world_position;
uniform vec4 base_color;

out vec4 color;

void main(){

    vec3 total_light = vec3(0,0,0);

    for(int i = 0; i < light_count; i++){

        vec3 lightDir = lights[i].position - FragPos;

        // Calculate the distance between the fragment and the light position
        float dist = length(lightDir);

        lightDir = mat3(transpose(inverse(model))) * lightDir;

        // Normalize the light direction vector
        lightDir = normalize(lightDir);

        // Look up the normal from the normal map
        vec3 normal = texture(JE_Normal, UV).rgb;

        // Calculate the diffuse lighting
        float diffuse = abs(dot(lightDir, normal));

        // Calculate the falloff factor using the smoothstep function
        float falloff = smoothstep(lights[i].radius, lights[i].radius - 2, dist);
        total_light += lights[i].color.rgb * diffuse * falloff * lights[i].intensity / (dist * dist);
    }
    color = texture(JE_Texture, UV) * vec4(total_light, 1.0);
}