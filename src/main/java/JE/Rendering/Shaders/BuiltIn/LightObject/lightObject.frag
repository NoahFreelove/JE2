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
};

uniform int lightCount;
uniform Light lights[MAX_LIGHTS];
uniform sampler2D JE_Texture;
uniform vec3 world_position;
uniform vec4 baseColor;

out vec4 FragColor;


vec3 CalcLight(Light light, vec3 normal, vec3 viewDir, vec3 fragPos, vec4 color){
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 ambient = light.ambient * color.rgb;
    vec3 diffuse = light.diffuse * diff * color.rgb;
    vec3 result = (ambient + diffuse) * color.rgb;
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    result *= attenuation;
    result *= light.intensity;

    return result;
}

void main(){
    vec3 result = vec3(0,0,0);
    vec2 size = vec2(1,1);
    vec3 pos = vec3( world_position.x * size.x ,world_position.y * size.y,0);

for(int i = 0; i < lightCount; i++){
    result += CalcLight(lights[i], vec3(0,0,1), vec3(0,0,1), pos, baseColor);

    result = clamp(result, 0.0, 1.0);


    //result += (ambient + diffuse + specular) * lights[i].color.rgb * baseColor.rgb;
    }

FragColor = vec4(result, 1.0);
}