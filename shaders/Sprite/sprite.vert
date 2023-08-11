#version 330 core

layout(location = 0) in vec2 modelPos;
layout(location = 1) in vec2 texCoord;

uniform mat4 MVP;
uniform float zPos;
uniform vec2 tile_factor;

out vec2 UV;

void main(){
    vec4 pos = MVP * vec4(modelPos, zPos, 1);
    gl_Position = pos;
    UV = vec2(texCoord.x * tile_factor.x, texCoord.y * tile_factor.y);
}