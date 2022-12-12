#version 330 core

layout (location = 0) out vec4 color;

in DATA {
    vec2 textureCoordinates;
} fs_in;

uniform sampler2D tex;

void main() {
    color = texture(tex, fs_in.textureCoordinates);
    if (color.w < 0.5) discard;
}