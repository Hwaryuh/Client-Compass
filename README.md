<div align="center">

# Client-Compass

</div>

* * *

클라이언트 렌더 기반 나침반입니다. 리소스팩을 의존하며, 1.21.6 이후 업데이트된 Locator Bar를 활용합니다.

https://github.com/user-attachments/assets/515d3d21-3d8d-4530-81b8-a62e0cb902df

### 지원 환경

- Minecraft Java 26.3 (Paper)


### 적용

`/compass on|off`

### 바닐라 HUD

Locator Bar가 상단으로 이동한 만큼 바닐라 HUD를 하단으로 조작합니다. 

[comp_config.glsl](assets\minecraft\shaders\include\comp_config.glsl) 을 수정하여 비활성화할 수 있습니다.


### 주의

- 리소스팩만으로 방향 눈금이 표시되지 않습니다. 서버의 Waypoint 패킷을 수신해야 합니다.
- 같은 GUI 셰이더를 수정하는 팩과 충돌할 수 있습니다.
- 경험치 바, 점프 바가 표시되는 동안 표시되지 않습니다. (바닐라 한계)

### 라이브러리

- [Kotlin stdlib](https://github.com/JetBrains/kotlin)
- [Paper API](https://papermc.io)
