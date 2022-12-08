#version 330 core

layout(location = 0) in vec2 vertexPos;
layout(location = 1) in vec2 texCoord;
uniform mat4 MVP;
uniform vec3 world_position;

out vec2 UV;
out vec3 FragPos;

void main(){
  vec4 pos = MVP * vec4(vertexPos, world_position.z, 1);
  gl_Position = pos;
  FragPos = vec3(vertexPos, world_position.z);
  UV = texCoord;
}