#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:comp_config.glsl>

// Keep the 26.3 uniform layout; this shader is also used during startup.
layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    mat4 TextureMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
};
layout(std140) uniform Projection {
    mat4 ProjMat;
};

uniform sampler2D Sampler0;

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV0;
layout(location = 2) in vec4 Color;

layout(location = 0) out vec2 texCoord0;
layout(location = 1) out vec4 vertexColor;

const ivec3 COMP_TAG_RGB = ivec3(23, 241, 173);
const float COMP_EDGE_FADE_START = 68.0;
const float COMP_EDGE_FADE_END = 84.0;

void main() {
    vec3 position = Position;
    texCoord0 = UV0;
    vertexColor = Color;

    bool guiProjection = abs(ProjMat[3][0] + 1.0) < 0.001
        && abs(ProjMat[3][3] - 1.0) < 0.001;

    if (guiProjection) {
#ifdef VULKAN
        int corner = gl_VertexIndex % 4;
#else
        int corner = gl_VertexID % 4;
#endif
        // BlitRenderState: top-left, bottom-left, bottom-right, top-right.
        vec2 inward = vec2(corner < 2 ? 0.5 : -0.5,
            corner == 0 || corner == 3 ? 0.5 : -0.5);
        ivec2 atlasSize = textureSize(Sampler0, 0);
        ivec2 pixel = ivec2(floor(UV0 * vec2(atlasSize) + inward));
        pixel = clamp(pixel, ivec2(0), atlasSize - 1);
        ivec4 tag = ivec4(round(texelFetch(Sampler0, pixel, 0) * 255.0));

        bool tagged = all(equal(tag.rgb, COMP_TAG_RGB)) && (tag.a == 1 || tag.a == 2);
        if (tagged) {
            float guiHeight = 2.0 / abs(ProjMat[1][1]);
            position.y += COMP_TOP_Y - (guiHeight - 29.0);
            if (tag.a == 2) {
                float guiWidth = 2.0 / abs(ProjMat[0][0]);
                float dotCenter = Position.x + (corner < 2 ? 4.5 : -4.5);
                float neutralCenter = ceil((guiWidth - 9.0) / 2.0) + 4.5;
                float distanceToCenter = abs(dotCenter - neutralCenter);
                vertexColor.a *= 1.0 - smoothstep(COMP_EDGE_FADE_START, COMP_EDGE_FADE_END, distanceToCenter);
            }
        }
#if COMP_HUD_LOWER_ENABLED == 1
        bool hudTagged = all(equal(tag.rgb, COMP_TAG_RGB)) && tag.a == 3;
        if (hudTagged) {
            vec2 guiSize = ceil(vec2(2.0 / abs(ProjMat[0][0]), 2.0 / abs(ProjMat[1][1])) - 0.001);
            vec2 spriteCenter = Position.xy + vec2(corner < 2 ? 4.5 : -4.5,
                corner == 0 || corner == 3 ? 4.5 : -4.5);
            float screenCenter = floor(guiSize.x / 2.0);
            float horizontalDistance = abs(spriteCenter.x - screenCenter);
            bool inHudLane = horizontalDistance >= 14.49 && horizontalDistance <= 86.51;
            bool inLowerHud = spriteCenter.y >= max(0.0, guiSize.y - COMP_HUD_REGION_HEIGHT)
                && spriteCenter.y <= guiSize.y - 33.49;
            if (inHudLane && inLowerHud) {
                position.y += COMP_HUD_SHIFT_Y;
            }
        }
#endif
    }

    gl_Position = ProjMat * ModelViewMat * vec4(position, 1.0);
}
