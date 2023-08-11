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
        FragColor = texture(JE_Texture, UV) * material.base_color;
    }
    else if (use_texture == 0){
        FragColor = material.base_color;
    }
}