#version 330 core
out vec4 FragColor;
struct Material{
    vec4 base_color;
};

uniform Material material;
void main() {
    FragColor = material.base_color;
}
