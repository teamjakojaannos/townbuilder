#version 120

// Grid
#define LINE_WIDTH 1.5
#define CELL_COLUMNS 10

// Posterization
#define GAMMA 0.3
#define REGIONS 8.0

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform vec2 InSize;
uniform vec3 CamPos;
uniform vec2 CamRot;
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
    return texture2D(DepthSampler, u).x;
}

float grid(vec2 coord, float resolution) {
    vec2 grid = fract(coord * resolution);
    return step(resolution, grid.x) * step(resolution, grid.y);
}

void main() {
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
    float edgeStrength = abs(clamp(sum * zFar, -1.0, 1.0));

    if (edgeStrength > 0.075) {
        gl_FragColor = vec4(vec3(0.75), 1.0);
    } else if (edgeStrength > 0.01) {
        gl_FragColor = vec4(vec3(0.35), 1.0);
    } else {
        vec4 texColor = texture2D(DiffuseSampler, texCoord);
        vec4 posterized = vec4(posterize(texColor.rgb), 1.0);
        float val = max(max(posterized.r, posterized.g), posterized.b);
        gl_FragColor = vec4(posterized.r * 0.15,
        posterized.g * 0.15,
        clamp(posterized.b * 2.0 + val, 0.15, 0.85),
        1.0);
    }

    float gridScaledWidth = InSize.x / LINE_WIDTH;
    float gridResolution = 1.0 / gridScaledWidth * CELL_COLUMNS;
    float ratio = InSize.y / InSize.x;

    float camPitch = CamRot.y;
    float camYaw = CamRot.y;
    float camX = CamPos.x;
    float camZ = CamPos.z;
    float sy = sin(radians(camYaw));
    float cy = cos(radians(camYaw));
    float cp = cos(radians((90.0 - camPitch))) * 4.5;
    float x = camX * sy * cp + camZ * cy * cp;
    float z = camX * cy * cp - camZ * sy * cp;
    float xx = x * cp;
    float zz = z * cp;
    // TODO: Figure out how to scale offsets according to screen size (4.5 above is just a magic number which happens to be close to correct with certain screen sizes)
    vec2 worldCoord = vec2(xx, zz * (1.0 - ratio));

    float gridValue = grid(vec2(texCoord.x, texCoord.y * ratio) * gridScaledWidth - worldCoord, gridResolution);
    if (gridValue == 0.0) {
        float value = max(max(gl_FragColor.x, gl_FragColor.y), gl_FragColor.z);
        float lightened = clamp(value * 1.0, 0.0, 1.0);
        gl_FragColor = vec4(vec3(0.65), 1.0);
    }
}
