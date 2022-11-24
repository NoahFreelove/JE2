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
        vec3 ambient = lights[i].ambient;
        vec3 norm = normalize(FragPos - world_position);
        vec3 lightDir = normalize(lights[i].position - world_position);
        float diff = max(dot(norm, lightDir), 0.0);
        vec3 diffuse = lights[i].diffuse * diff;

        vec3 viewDir = normalize(-world_position);
        vec3 reflectDir = reflect(-lightDir, norm);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0f);
        vec3 specular = lights[i].specular * spec;

        float distance = length(lights[i].position - world_position);
        float attenuation = 1.0 / (lights[i].constant + lights[i].linear * distance + lights[i].quadratic * (distance * distance));
        ambient *= attenuation;
        diffuse *= attenuation;
        specular *= attenuation;
        ambient *= lights[i].intensity;
        diffuse *= lights[i].intensity;
        specular *= lights[i].intensity;

        float radius = lights[i].radius;
        float lightDistance = length(lights[i].position - world_position);

        float lightIntensity = 1.0 - (lightDistance / radius);
        lightIntensity = clamp(lightIntensity, 0.0, 1.0);
        ambient *= lightIntensity;
        diffuse *= lightIntensity;
        specular *= lightIntensity;


        result += (ambient + diffuse + specular) * lights[i].color.xyz * texture(JE_Texture, UV).rgb;
    }

    FragColor = vec4(result, 1.0);
}