#version 330 core

layout (location = 0) out vec4 color;

in DATA {
    vec2 textureCoordinates;
} fs_in;

uniform sampler2D tex;
uniform int top;

void main() {
    vec2 textureCoordinates = fs_in.textureCoordinates;

    if (top == 1) {
        textureCoordinates.y = 1 - textureCoordinates.y;
    }

    color = texture(tex, textureCoordinates);
    if (color.w < 0.5) discard;
}