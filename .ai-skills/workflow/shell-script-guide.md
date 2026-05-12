---
name: shell-script-guide
description: scripts/*.sh 또는 repo-tracked shell script를 만들거나 수정할 때 헤더, 실행 방식, 검증 기준을 맞추는 스킬.
---

# shell-script-guide

## 목적

Shell script를 열자마자 역할, 입력, 결과, 주요 동작을 파악할 수 있게 유지합니다.

## 사용 시점

- `scripts/*.sh`를 새로 만들 때
- 기존 shell script의 인자, 출력, 실패 조건을 바꿀 때
- `verifyHarnessConsistency`에서 검사하는 script 기준을 수정할 때

## 핵심 원칙

- script는 `#!/usr/bin/env bash`와 `set -euo pipefail`을 기본으로 사용합니다.
- shebang 바로 아래에 역할, 입력/실행, 예상 결과, 주요 동작을 주석으로 적습니다.
- 실패 메시지는 다음 행동을 알 수 있게 씁니다.
- repo-tracked script는 `bash -n`으로 문법 검사를 통과해야 합니다.

## 절차

1. shebang 바로 아래에 필수 헤더를 추가합니다.
2. 대표 실행 명령과 성공/실패 시 기대 결과를 적습니다.
3. 입력값이 있으면 기본값과 허용 명령을 명시합니다.
4. 위험한 삭제나 덮어쓰기가 있으면 target 안전장치를 둡니다.
5. `bash -n scripts/*.sh`와 관련 검증 명령을 실행합니다.

## 출력

- 헤더가 있는 shell script
- 실행 예시
- 검증 결과

## 점검

- `# 역할:`, `# 입력/실행:`, `# 예상 결과:`, `# 주요 동작:`이 모두 있는가
- 실패 시 non-zero로 종료하는가
- 삭제/동기화 target이 repo 내부로 제한되는가
- `bash -n`을 통과하는가

## 템플릿

```bash
#!/usr/bin/env bash
# 역할: 하네스 문서와 mirror를 동기화합니다.
# 입력/실행:
#   - ./scripts/sync-harness-docs.sh copy
# 예상 결과:
#   - AGENTS.md와 CLAUDE.md가 같아지고 skill mirror가 갱신됩니다.
# 주요 동작:
#   - source 문서를 읽습니다.
#   - mirror 디렉토리를 재생성합니다.
set -euo pipefail
```
