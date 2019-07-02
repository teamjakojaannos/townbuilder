#version 120

#define GAMMA 0.45
#define REGIONS 5.0

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;

float sigmoid(float a, float f)
{
    return 1.0/(1.0+exp(-f*a));
}

vec3 posterize(vec3 color)
{
    color = pow(color, vec3(GAMMA, GAMMA, GAMMA));
    color = floor(color * REGIONS) / REGIONS;
    color = pow(color, vec3(1.0 / GAMMA));
    return color.rgb;
}

void main() {
    vec4 texColor = texture2D(DiffuseSampler, texCoord);
    vec4 posterized = vec4(posterize(texColor.rgb), 1.0);
    float edgeStrength = length(fwidth(posterized));
    edgeStrength = sigmoid(edgeStrength - 0.2, 32.0);

    if (edgeStrength > 0.9) {
        gl_FragColor = vec4(vec3(0.95), 1.0);
    } else {
        gl_FragColor = vec4(texColor.r * 0.1,
                            texColor.g * 0.1,
                            clamp(texColor.b * 2.0, 0.0, 1.0),
                            1.0);
    }
}
