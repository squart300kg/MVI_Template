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
- 같은 작업 맥락에서 기존 PR을 이어가면 기존 branch/worktree를 재사용합니다.
- base 최신화는 merge보다 rebase를 우선합니다.
- rebase 충돌은 `ours/theirs`를 기계적으로 고르지 않고 양쪽 의도를 비교해 해결합니다.
- PR 병합은 GitHub PR squash merge로 처리하고 로컬 merge로 대체하지 않습니다.
- 사용자가 `$worktree-merge-guide`로 작업 실행을 맡기면, 명시적으로 PR까지만 요청하지 않는 한 PR 생성에서 멈추지 않고 검증, squash merge, clean worktree 정리까지 완료합니다.
- `$worktree-merge-guide`를 시작할 때는 먼저 [divide-task](./divide-task.md) 기준으로 단일/멀티 에이전트 분배 필요성을 판단하고, 작은 작업이면 단일 에이전트로 진행한 이유를 짧게 남깁니다.
- 스킬 시작 시점이나 병합 직전에 base 변경이 발견되면 작업 브랜치를 최신 `origin/<base>` 위로 rebase하고, base 변경과 작업 브랜치 변경의 의도를 모두 살리도록 충돌과 의미 충돌을 해결한 뒤 검증을 다시 실행합니다.
- base 변경이 다른 PR merge 결과라면 해당 PR 설명과 실제 diff를 확인한 뒤 현재 작업과 충돌하지 않게 반영합니다.
- 열린 PR 여러 개를 수동으로 병합할 때는 의존성, 변경 범위, 오래된 PR 순으로 우선순위를 명시합니다.
- 검증 실패 상태에서는 merge하지 않습니다.
- 이 스킬이 만든 clean worktree만 정리합니다.

## 절차

1. [divide-task](./divide-task.md) 기준으로 작업을 단일 에이전트로 유지할지 멀티 에이전트로 나눌지 먼저 판단합니다.
2. `git status --short --branch`와 `git worktree list`로 현재 상태를 확인합니다.
3. `git fetch origin --prune` 후 base branch를 확정합니다.
4. 작업 branch와 worktree를 만들고 해당 worktree에서만 수정합니다.
5. 구현 전 base가 앞서 있으면 해당 변경 내용과 PR diff를 확인하고 rebase/resolve/검증을 끝낸 뒤 작업을 시작합니다.
6. 구현 후 [sync-harness-docs.sh](../../scripts/sync-harness-docs.sh) 실행이 필요한지 확인합니다.
7. `./gradlew qualityGateFast`를 실행합니다.
8. 변경을 커밋하고 작업 branch를 push합니다.
9. PR을 생성하거나 기존 PR 본문을 최신 작업 내용으로 갱신합니다.
10. merge 요청이면 최신 `origin/<base>` 위로 다시 rebase하고, base 변경이 있으면 충돌과 의미 충돌을 모두 resolve한 뒤 검증을 재실행합니다.
11. 여러 PR merge 요청이면 열린 PR 목록과 우선순위를 먼저 사용자에게 보고합니다.
12. 검증 통과 후 `gh pr merge --squash`로 병합합니다.
13. 병합된 branch와 이 스킬이 만든 clean worktree를 정리합니다.

## 출력

- base branch와 작업 branch
- worktree path
- PR URL
- PR 우선순위와 처리 순서
- rebase 결과
- 검증 명령과 결과
- merge/cleanup 결과

## 점검

- 현재 요청과 무관한 worktree나 dirty file을 건드리지 않았는가
- PR 생성 전 `git status --short`가 의도한 변경만 보여주는가
- `qualityGateFast` 결과를 PR 본문이나 최종 보고에 남겼는가
- merge 후 base branch가 최신 원격 head를 가리키는가
- 임시 worktree가 남아 branch 전환을 막지 않는가
