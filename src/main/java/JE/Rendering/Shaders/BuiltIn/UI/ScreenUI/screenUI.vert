#version 330 core

layout(location = 0) in vec2 vertexPos;
layout(location = 1) in vec2 texCoord;
uniform vec3 world_position;
uniform vec2 world_scale;

out vec2 UV;

void main(){
  gl_Position = vec4(vertexPos * world_scale ,0, 1);
  UV = texCoord;
}