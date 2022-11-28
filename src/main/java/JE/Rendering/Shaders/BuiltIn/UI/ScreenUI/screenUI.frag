#version 330 core

uniform vec3 world_position;
uniform vec4 baseColor;
out vec4 FragColor;
in vec2 UV;
void main(){
    FragColor = vec4(1,1,1,1);
}