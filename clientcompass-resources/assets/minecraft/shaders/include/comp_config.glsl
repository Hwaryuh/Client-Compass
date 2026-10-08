#ifndef COMP_CONFIG_GLSL
#define COMP_CONFIG_GLSL

// 바닐라 HUD 이동 (1: 켜기, 0: 끄기)
#define COMP_HUD_LOWER_ENABLED 1

// GUI 픽셀 기준 Y 이동량 (양수: 아래, 음수: 위)
// 기본값: 6.0
#define COMP_HUD_SHIFT_Y 6.0

// 하단에서 HUD로 판정할 높이 (체력이 많아 오차를 조정할 때 사용)
// 기본값: 128.0
#define COMP_HUD_REGION_HEIGHT 128.0

#if COMP_HUD_LOWER_ENABLED != 0 && COMP_HUD_LOWER_ENABLED != 1
#error COMP_HUD_LOWER_ENABLED must be 0 or 1
#endif

#endif
