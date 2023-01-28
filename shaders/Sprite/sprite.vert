#version 330 core

layout(location = 0) in vec2 modelPos;
layout(location = 1) in vec2 texCoord;

uniform mat4 MVP;
uniform float zPos;
out vec2 UV;

void main(){
    vec4 pos = MVP * vec4(modelPos, zPos, 1);
    gl_Position = pos;
    UV = texCoord;
}