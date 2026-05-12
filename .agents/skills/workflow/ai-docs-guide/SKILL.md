---
name: ai-docs-guide
description: MVI Template의 AGENTS/CLAUDE/.ai-skills/.agents/.claude 문서를 추가하거나 수정할 때 정본, mirror, sync, 검증 기준을 맞추는 스킬.
---

# ai-docs-guide

## 목적

프로젝트 하네스 문서와 skill mirror가 서로 어긋나지 않게 유지합니다.

## 사용 시점

- `AGENTS.md`, `CLAUDE.md`를 수정할 때
- `.ai-skills` 하위 skill 원본을 추가, 수정, 삭제할 때
- `scripts/sync-harness-docs.sh` 또는 `scripts/verify-harness-consistency.sh`를 수정할 때

## 핵심 원칙

- `.ai-skills`가 skill 원본입니다.
- `.agents/skills`, `.claude/skills`, `CLAUDE.md`는 sync 결과물입니다.
- 새 skill은 `목적`, `사용 시점`, `핵심 원칙`, `절차`, `출력`, `점검` 구조를 우선합니다.
- source `.ai-skills/*.md` 문서는 500줄을 넘기지 않습니다. 커지면 관심사별 skill로 분리합니다.
- 같은 레벨의 bullet이 6개 이상이거나 주제가 섞이면 2~4개 소제목으로 나눕니다.
- 실제 파일 경로는 가능한 Markdown 링크로 표기하고, 명령 예시는 code block에 둡니다.
- 기존 프로젝트명, 패키지명, 명령어가 남지 않도록 현재 모듈과 명령 기준으로 바꿉니다.

## 절차

1. 원본 문서를 `.ai-skills` 또는 `AGENTS.md`에서 수정합니다.
2. flat bullet이나 중복 설명이 늘어나면 `목적`, `사용 시점`, `핵심 원칙`, `절차`, `출력`, `점검` 구조로 다시 정리합니다.
3. `./scripts/sync-harness-docs.sh copy`를 실행합니다.
4. `./scripts/verify-harness-consistency.sh`로 mirror 정합성을 확인합니다.
5. Gradle task가 필요한 변경이면 `./gradlew verifyHarnessConsistency`도 실행합니다.

## 출력

- 수정된 원본 문서
- sync로 갱신된 `CLAUDE.md`, `.agents/skills`, `.claude/skills`
- 실행한 검증 명령과 결과

## 점검

- 원본이 아닌 mirror만 직접 수정하지 않았는가
- skill frontmatter에 `name`과 `description`이 있는가
- 현재 프로젝트에 없는 명령, 패키지, 클래스명을 남기지 않았는가
- source skill이 500줄 이하인가
- mirror만 직접 수정하지 않았는가
- sync와 consistency 검증을 실행했는가
