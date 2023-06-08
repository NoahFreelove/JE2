#version 330 core

layout(location = 0) in vec2 vertexPos;
uniform mat4 MVP;
uniform vec3 world_position;

void main() {
    vec4 pos = MVP * vec4(vertexPos, world_position.z,1.0);
    gl_Position = pos;
}