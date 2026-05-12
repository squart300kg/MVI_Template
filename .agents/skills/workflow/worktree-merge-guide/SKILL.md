---
name: worktree-merge-guide
description: 작업을 git worktree로 격리하고 PR 생성/갱신, rebase, qualityGateFast 검증, squash merge, 정리까지 진행할 때 사용하는 스킬.
---

# worktree-merge-guide

## 목적

기존 작업 디렉터리를 오염시키지 않고 작업 브랜치와 PR을 안전하게 전달하거나 병합합니다.

## 사용 시점

- 사용자가 worktree, 새 브랜치, PR, merge를 명시할 때
- 여러 작업을 병렬로 진행해 충돌 위험이 있을 때
- 작업 완료 후 PR 기준 squash merge와 worktree 정리가 필요할 때

## 핵심 원칙

- base branch는 사용자가 지정한 값을 우선하고, 없으면 현재 tracking branch를 사용합니다.
- 기존 PR을 이어가는 요청이 아니라면 새 branch와 새 worktree를 만듭니다.
- base 최신화는 merge보다 rebase를 우선합니다.
- PR 병합은 GitHub PR squash merge로 처리하고 로컬 merge로 대체하지 않습니다.
- 검증 실패 상태에서는 merge하지 않습니다.
- 이 스킬이 만든 clean worktree만 정리합니다.

## 절차

1. `git status --short --branch`와 `git worktree list`로 현재 상태를 확인합니다.
2. `git fetch origin --prune` 후 base branch를 확정합니다.
3. 작업 branch와 worktree를 만들고 해당 worktree에서만 수정합니다.
4. 구현 후 `./scripts/sync-harness-docs.sh copy`가 필요한지 확인합니다.
5. `./gradlew qualityGateFast`를 실행합니다.
6. 변경을 커밋하고 작업 branch를 push합니다.
7. PR을 생성하거나 기존 PR 본문을 최신 작업 내용으로 갱신합니다.
8. merge 요청이면 최신 `origin/<base>` 위로 rebase 후 다시 검증합니다.
9. 검증 통과 후 `gh pr merge --squash`로 병합합니다.
10. 병합된 branch와 이 스킬이 만든 clean worktree를 정리합니다.

## 출력

- base branch와 작업 branch
- worktree path
- PR URL
- rebase 결과
- 검증 명령과 결과
- merge/cleanup 결과

## 점검

- 현재 요청과 무관한 worktree나 dirty file을 건드리지 않았는가
- PR 생성 전 `git status --short`가 의도한 변경만 보여주는가
- `qualityGateFast` 결과를 PR 본문이나 최종 보고에 남겼는가
- merge 후 base branch가 최신 원격 head를 가리키는가
