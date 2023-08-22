#version 330 core
#ifdef GL_ES
precision mediump float;
#endif
out vec4 FragColor;

uniform float delta_time;
uniform vec2 render_size;
uniform sampler2D JE_Texture;
uniform int JE_Time;
uniform int JE_Time2;

const float PI = 3.14159265359;
const float speed = 0.2; // Speed of the ripple animation
const float frequency = 1.0; // Frequency of the ripples
const float amplitude = 3.0; // Amplitude of the ripples
in vec2 UV;
void main(void) {
    vec2 uv = UV;

    // Add time-based offset to create animation
    float offset = JE_Time * speed;

    // Calculate the displacement of the water surface based on ripples
    float displacement = amplitude * sin(2.0 * PI * frequency * (uv.x + uv.y) + offset);

    // Apply the displacement to the texture coordinates
    vec2 displacedUV = uv + vec2(0.0, displacement);

    // Sample the texture with the displaced coordinates
    vec4 color = texture2D(JE_Texture, displacedUV);

    FragColor = color;
}