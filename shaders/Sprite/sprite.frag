#version 330 core
out vec4 FragColor;
uniform sampler2D JE_Texture;
in vec2 UV;

void main(){
    FragColor = texture(JE_Texture, UV);
}