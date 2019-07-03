#version 120

#define GAMMA 0.65
#define REGIONS 5.0

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform float zNear;
uniform float zFar;

varying vec2 texCoord;
varying vec2 oneTexel;

vec3 posterize(vec3 color) {
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
    float up     = depthSample(texCoord + vec2(0.0, -oneTexel.y));
    float down   = depthSample(texCoord + vec2(oneTexel.x, 0.0));
    float left   = depthSample(texCoord + vec2(-oneTexel.x, 0.0));
    float right  = depthSample(texCoord + vec2(0.0, oneTexel.y));
    float uDiff = center - up;
    float dDiff = center - down;
    float lDiff = center - left;
    float rDiff = center - right;
    float sum = uDiff + dDiff + lDiff + rDiff;
    float edgeStrength = clamp(sum * zFar, -1.0, 1.0);

    if (abs(edgeStrength) > 0.01) {
        gl_FragColor = vec4(vec3(0.95), 1.0);
    } else {
        gl_FragColor = vec4(posterized.r * 0.1,
                            posterized.g * 0.1,
                            clamp(posterized.b * 3.0, 0.33, 1.0),
                            1.0);
    }
}

