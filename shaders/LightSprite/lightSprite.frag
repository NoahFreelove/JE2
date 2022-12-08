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

uniform int lightCount;
uniform Light lights[MAX_LIGHTS];
uniform sampler2D JE_Texture;
uniform vec3 world_position;
uniform vec4 baseColor;

out vec4 FragColor;

void main(){
    vec3 result = vec3(0,0,0);
    for(int i = 0; i < lightCount; i++){
        vec3 lightDir = normalize(lights[i].position - world_position);
        float distance = length(lights[i].position - world_position);
        float attenuation = 1.0 / (lights[i].constant + lights[i].linear * distance + lights[i].quadratic * (distance * distance));
        float intensity = lights[i].intensity * attenuation;
        float radius = lights[i].radius;
        float falloff = clamp(1.0 - distance / radius, 0.0, 1.0);
        float light = intensity * falloff;
        result += lights[i].color.rgb * light * lights[i].color.xyz;
    }
    result *= texture(JE_Texture, UV).rgb;

    FragColor = vec4(result, 1.0);
}