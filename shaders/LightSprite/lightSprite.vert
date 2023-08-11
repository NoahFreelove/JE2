#version 330 core

layout(location = 0) in vec2 vertexPos;
layout(location = 1) in vec2 texCoord;

uniform mat4 MVP;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

uniform vec3 world_position;
uniform vec2 tile_factor;

out vec2 UV;
out vec3 FragPos;


void main(){
  gl_Position =  MVP * vec4(vertexPos, world_position.z, 1);
  vec3 combinedPos = vec3(vertexPos, world_position.z);
  FragPos = vec3(model* vec4(combinedPos,1));
  UV = vec2(texCoord.x * tile_factor.x, texCoord.y * tile_factor.y);
}