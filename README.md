# RCT Level Sync (레벨 보정)

트레이너 배틀에서 내 포켓몬 레벨을 RCT 레벨 상한에 맞춰 낮춰 주는 모드입니다. 레벨이 너무 높아도 관장에게 도전할 수 있습니다. 배틀용 복제본만 바뀌고 실제 포켓몬은 그대로입니다.

- 버전: 1.5.0
- Minecraft 1.21.1 / Fabric
- 필요한 모드: Cobblemon, Radical Cobblemon Trainers (rctmod, rctapi), Fabric API
- 설치: 서버와 클라이언트 모두에 넣어야 화면(UI)을 쓸 수 있습니다. 모드가 없는 사람도 서버에 접속은 할 수 있습니다.

## 다운로드

**[⬇ rct-level-sync-1.5.0.jar 다운로드](https://github.com/minerapai/rct-level-sync/releases/download/v1.5.0/rct-level-sync-1.5.0.jar)**

받은 jar 파일을 마인크래프트(또는 서버) 폴더의 `mods` 폴더에 넣으면 됩니다. 다른 버전은 [Releases](https://github.com/minerapai/rct-level-sync/releases) 에 있습니다.

## 기능

- 트레이너 배틀 때만 레벨 보정 (상한 = 상대 에이스 레벨 + 5)
- 배틀이 끝나면 원래 레벨 그대로

## 사용법

| 명령어 | 설명 |
|---|---|
| `/levelsync enabled <true|false>` | (OP) 켜기/끄기 |
| `/levelsync offset <값>` | (OP) 보정 레벨 조정 |

(OP) 표시가 있는 명령어는 서버 관리자(OP)만 쓸 수 있습니다.

## 참고

이 폴더는 Gradle 프로젝트가 아니라 소스(src)와 리소스(res)만 들어 있습니다.

## 라이선스

MIT License. 출처(minerapai)만 밝히면 누구나 자유롭게 쓰고 고치고 배포할 수 있습니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 보세요.
