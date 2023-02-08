#version 330 core
out vec4 FragColor;
uniform sampler2D JE_Texture;
in vec2 UV;
uniform int use_texture;
uniform vec4 base_color;

void main(){
    if(use_texture == 1){
        FragColor = texture(JE_Texture, UV);
    }
    else if (use_texture == 0){
        FragColor = base_color;
    }
}