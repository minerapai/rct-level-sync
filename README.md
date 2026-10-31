# RCT Level Sync (레벨 보정)

트레이너 배틀에서 내 포켓몬 레벨을 RCT 레벨 상한에 맞춰 낮춰 주는 모드입니다. 레벨이 너무 높아도 관장에게 도전할 수 있습니다. 배틀용 복제본만 바뀌고 실제 포켓몬은 그대로입니다.

- 버전: 1.5.0
- Minecraft 1.21.1 / Fabric
- 필요한 모드: Cobblemon, Radical Cobblemon Trainers (rctmod, rctapi), Fabric API
- 설치: 서버에만 넣으면 됩니다. 접속하는 사람(클라이언트)은 이 모드가 없어도 됩니다.

## 다운로드

**[⬇ rct-level-sync-1.5.0.jar 다운로드](https://github.com/minerapai/rct-level-sync/releases/download/v1.5.0/rct-level-sync-1.5.0.jar)**

받은 jar 파일을 마인크래프트(또는 서버) 폴더의 `mods` 폴더에 넣으면 됩니다. 다른 버전은 [Releases](https://github.com/minerapai/rct-level-sync/releases) 에 있습니다.

## 기능

- 트레이너 배틀 때만 레벨 보정 (상한 = 상대 에이스 레벨 + 5)
- 배틀이 끝나면 원래 레벨 그대로

## 사용법

넣기만 하면 동작합니다. 아래는 설정을 바꿀 때 쓰는 관리자 명령어입니다.

| 명령어 | 설명 |
|---|---|
| `/levelsync` | (OP) 지금 설정 보기 |
| `/levelsync scaleDown <true/false>` | (OP) 레벨 제한보다 높은 포켓몬을 제한까지 낮추기 (기본 켜짐) |
| `/levelsync scaleUp <true/false>` | (OP) 낮은 포켓몬을 올리기 (기본 켜짐) |
| `/levelsync offset <숫자>` | (OP) 올릴 때 목표 레벨 = 레벨 제한 + 숫자 (기본 0) |
| `/levelsync keyTrainersOnly <true/false>` | (OP) 관장, 사천왕, 챔피언 같은 주요 트레이너만 맞추기 (기본 켜짐) |
| `/levelsync othersIgnoreCap <true/false>` | (OP) 일반 트레이너에게는 레벨 제한 없이 도전 (기본 켜짐) |
| `/levelsync transferRewards <true/false>` | (OP) 경험치, 노력치, 진화 진행도를 원래 포켓몬에게 넘기기 (기본 켜짐) |
| `/levelsync reload` | (OP) 설정 파일 다시 읽기 |

설정 파일: `config/rct-level-sync.properties`

(OP) 표시가 있는 명령어는 서버 관리자(OP)만 쓸 수 있습니다.

## 참고

이 폴더는 Gradle 프로젝트가 아니라 소스(src)와 리소스(res)만 들어 있습니다.

## 라이선스

MIT License. 출처(minerapai)만 밝히면 누구나 자유롭게 쓰고 고치고 배포할 수 있습니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 보세요.
