#version 120

#define GAMMA 0.45
#define REGIONS 5.0

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform float zNear;
uniform float zFar;

varying vec2 texCoord;
varying vec2 oneTexel;

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

float linearizeDepth(float z) {
    return (zNear * z) / (zFar - z * (zFar - zNear));
}

float depthSample(vec2 u) {
    return linearizeDepth(texture2D(DepthSampler, u).x);
}

void main() {
    vec4 texColor = texture2D(DiffuseSampler, texCoord);
    vec4 posterized = vec4(posterize(texColor.rgb), 1.0);

    float center = depthSample(texCoord);
    float up     = depthSample(texCoord + vec2(        0.0, -oneTexel.y));
    float down   = depthSample(texCoord + vec2( oneTexel.x,         0.0));
    float left   = depthSample(texCoord + vec2(-oneTexel.x,         0.0));
    float right  = depthSample(texCoord + vec2(        0.0,  oneTexel.y));
    float uDiff = center - up;
    float dDiff = center - down;
    float lDiff = center - left;
    float rDiff = center - right;
    float edgeStrength = clamp(uDiff + dDiff + lDiff + rDiff, 0.0, 1.0);
    //edgeStrength = sigmoid(edgeStrength - 0.2, 32.0);

    if (edgeStrength > 0.5) {
        gl_FragColor = vec4(vec3(edgeStrength), 1.0);
    } else {
        gl_FragColor = vec4(posterized.r * 0.1,
                            posterized.g * 0.1,
                            clamp(posterized.b * 2.0, 0.0, 1.0),
                            1.0);
    }
}