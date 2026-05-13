---
name: ai-docs-guide
description: MVI Template의 AGENTS/CLAUDE/.ai-skills/.agents/.claude 문서를 추가하거나 수정할 때 정본, mirror, sync, 검증 기준을 맞추는 스킬.
---

# ai-docs-guide

## 목적

프로젝트 하네스 문서와 skill mirror가 서로 어긋나지 않게 유지합니다.
스킬 문서가 늘어나도 같은 흐름으로 읽히도록 목차, 트리거 설명, 섹션 책임, 중복 방지 기준을 함께 관리합니다.

## 사용 시점

- [AGENTS.md](../../AGENTS.md), [CLAUDE.md](../../CLAUDE.md)를 수정할 때
- [.ai-skills](../../.ai-skills) 하위 skill 원본을 추가, 수정, 삭제할 때
- [sync-harness-docs.sh](../../scripts/sync-harness-docs.sh) 또는 [verify-harness-consistency.sh](../../scripts/verify-harness-consistency.sh)를 수정할 때

## 핵심 원칙

### 정본과 동기화

- [.ai-skills](../../.ai-skills)가 skill 원본입니다.
- [.agents/skills](../../.agents/skills), [.claude/skills](../../.claude/skills)는 gitignored local sync 결과물이고, [CLAUDE.md](../../CLAUDE.md)는 tracked sync 결과물입니다.
- 같은 내용은 한 정본에만 적고 다른 문서에서는 링크합니다.

### 문서 구조

- 새 skill은 `목적`, `사용 시점`, `핵심 원칙`, `절차`, `출력`, `점검` 구조를 우선합니다.
- 스킬의 top-level 목차는 기본 6개 섹션을 유지합니다: `목적`, `사용 시점`, `핵심 원칙`, `절차`, `출력`, `점검`.
- 기본 골격 외 새 주제를 추가해야 하면 새 `##` 섹션을 만들지 말고, 가장 가까운 기본 섹션 아래 `###` 하위 목차로 추가합니다.
- 같은 레벨의 bullet이나 numbering이 6개 이상이면 내용에 맞는 2~4개 `###` 소제목으로 나눕니다.

### Skill 분리 기준

- 한 skill은 하나의 책임만 가집니다. 판단, 도구 사용, 문서 작성, 검증 책임이 커지면 별도 skill로 나눕니다.
- 새 주제가 기본 6개 섹션 아래에 자연스럽게 들어가지 않거나 책임이 커지면 같은 문서에 억지로 추가하지 않고 별도 skill로 분리합니다.
- 중복 제거와 `###` 소제목 분해 후에도 source `.ai-skills/*.md` 문서가 500줄을 넘으면 관심사별 skill로 분리합니다.

### Trigger와 참조

- frontmatter의 `description`은 스킬 자동 트리거를 위한 문장이므로, 무엇을 하는지보다 어떤 요청에서 써야 하는지를 구체적으로 씁니다.
- 실제 파일 경로는 가능한 Markdown 링크로 표기하고, 명령 예시는 code block에 둡니다.
- 스킬 내부에서 다른 스킬이나 로컬 파일을 참조할 때는 plain path나 inline code path 대신 Markdown 링크를 사용합니다.
- 기존 프로젝트명, 패키지명, 명령어가 남지 않도록 현재 모듈과 명령 기준으로 바꿉니다.

## 절차

### 원본 수정

1. 원본 문서를 [.ai-skills](../../.ai-skills) 또는 [AGENTS.md](../../AGENTS.md)에서 수정합니다.
2. 스킬 책임을 한 문장으로 정의하고, 사용자 요청이 어떤 표현일 때 트리거되어야 하는지 `description`에 적습니다.

### 구조 정리

1. flat bullet이나 중복 설명이 늘어나면 `목적`, `사용 시점`, `핵심 원칙`, `절차`, `출력`, `점검` 구조로 다시 정리합니다.
2. 기본 6개 섹션으로 본문을 먼저 작성하고, 추가 목차가 필요하면 기존 기본 섹션 아래 `###`로 넣습니다.
3. 같은 레벨의 bullet이나 numbering이 6개 이상이면 내용 기반 소제목으로 나눕니다.
4. 절차와 판단 기준을 분리합니다. 절차는 순서이고, 판단 기준은 분기 조건입니다.

### 섹션 책임

1. 출력 섹션에는 최종 답변, 파일, 템플릿, 명령 결과처럼 사용자가 받을 산출물을 적습니다.
2. 점검 섹션에는 끝내기 전에 확인할 실패 방지 기준을 적습니다.

### 동기화와 검증

1. `./scripts/sync-harness-docs.sh copy`를 실행합니다.
2. `./scripts/verify-harness-consistency.sh`로 mirror 정합성을 확인합니다.
3. Gradle task가 필요한 변경이면 `./gradlew verifyHarnessConsistency`도 실행합니다.

## 출력

- 수정된 원본 문서
- sync로 갱신된 [CLAUDE.md](../../CLAUDE.md)와 local mirror인 [.agents/skills](../../.agents/skills), [.claude/skills](../../.claude/skills)
- 실행한 검증 명령과 결과

### 기본 템플릿

```md
---
name: skill-name
description: 사용자가 ... 요청할 때 사용하는 스킬.
---

# skill-name

## 목적

## 사용 시점

## 핵심 원칙

## 절차

## 출력

## 점검
```

## 점검

### 구조 점검

- skill frontmatter에 `name`과 `description`이 있는가
- `description`만 보고도 언제 트리거될지 알 수 있는가
- 스킬 top-level 목차가 기본 6개 섹션을 유지하는가
- 추가 주제가 새 `##`가 아니라 기존 기본 섹션 아래 `###`로 들어갔는가
- 같은 레벨의 bullet이나 numbering이 6개 이상이면 소제목으로 나눴는가

### 내용 점검

- `사용 시점`과 `절차`가 섞이지 않았는가
- 판단 기준이 예/아니오로 검토 가능하게 쓰였는가
- 현재 프로젝트에 없는 명령, 패키지, 클래스명을 남기지 않았는가
- 중복 제거와 소제목 분해 후에도 source skill이 500줄 이하인가

### 동기화 점검

- mirror만 직접 수정하지 않았는가
- sync와 consistency 검증을 실행했는가
