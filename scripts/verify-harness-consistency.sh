#!/usr/bin/env bash
# 역할: 하네스 문서, skill mirror, shell script 기본 형식을 검증합니다.
# 입력/실행:
#   - ./scripts/verify-harness-consistency.sh
# 예상 결과:
#   - AGENTS/CLAUDE와 .ai-skills 원본이 유효하고, 로컬 mirror가 있으면 일치할 때 성공합니다.
#   - 불일치, 누락, 이전 프로젝트 residue, script 헤더 누락이 있으면 실패합니다.
# 주요 동작:
#   - 원본 skill 파일을 확인하고, 생성된 mirror가 있으면 비교합니다.
#   - scripts/*.sh 필수 헤더와 문법을 확인합니다.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
SOURCE_DIR="$REPO_ROOT/.ai-skills"
AGENTS_DEST="$REPO_ROOT/.agents/skills"
CLAUDE_DEST="$REPO_ROOT/.claude/skills"
FAIL=0

error() {
  echo "[harness] ERROR: $*" >&2
  FAIL=1
}

require_file() {
  local file="$1"
  if [[ ! -f "$file" ]]; then
    error "Missing file: $file"
  fi
}

require_dir() {
  local dir="$1"
  if [[ ! -d "$dir" ]]; then
    error "Missing directory: $dir"
  fi
}

require_file "$REPO_ROOT/AGENTS.md"
require_file "$REPO_ROOT/CLAUDE.md"
require_dir "$SOURCE_DIR"

CHECK_AGENTS_MIRROR=0
CHECK_CLAUDE_MIRROR=0
if [[ -d "$AGENTS_DEST" ]]; then
  CHECK_AGENTS_MIRROR=1
else
  echo "[harness] .agents mirror not found; skipped. Run ./scripts/sync-harness-docs.sh copy to generate it."
fi
if [[ -d "$CLAUDE_DEST" ]]; then
  CHECK_CLAUDE_MIRROR=1
else
  echo "[harness] .claude mirror not found; skipped. Run ./scripts/sync-harness-docs.sh copy to generate it."
fi

if [[ -f "$REPO_ROOT/AGENTS.md" && -f "$REPO_ROOT/CLAUDE.md" ]]; then
  if ! cmp -s "$REPO_ROOT/AGENTS.md" "$REPO_ROOT/CLAUDE.md"; then
    error "AGENTS.md and CLAUDE.md differ. Run ./scripts/sync-harness-docs.sh copy."
  fi
fi

check_skill_frontmatter() {
  local file="$1"
  if ! sed -n '1p' "$file" | grep -qx -- '---'; then
    error "Skill frontmatter start missing: $file"
  fi
  if ! grep -Eq '^name: [a-zA-Z0-9_-]+$' "$file"; then
    error "Skill name missing or invalid: $file"
  fi
  if ! grep -Eq '^description: .+' "$file"; then
    error "Skill description missing: $file"
  fi
}

skill_path_for_source() {
  local rel="$1"
  local without_ext="${rel%.md}"

  if [[ "$without_ext" == */* ]]; then
    local parent="${without_ext%/*}"
    local leaf="${without_ext##*/}"
    local parent_leaf="${parent##*/}"

    if [[ "$leaf" == "$parent_leaf" ]]; then
      printf '%s\n' "$parent"
      return
    fi
  fi

  printf '%s\n' "$without_ext"
}

compare_mirror_file() {
  local src="$1"
  local dest_root="$2"
  local label="$3"
  local rel="${src#${SOURCE_DIR}/}"
  local skill
  skill="$(skill_path_for_source "$rel")"
  local dest="$dest_root/$skill/SKILL.md"

  if [[ ! -f "$dest" ]]; then
    error "Missing $label mirror for $rel"
    return
  fi

  if ! cmp -s "$src" "$dest"; then
    error "$label mirror differs for $rel. Run ./scripts/sync-harness-docs.sh copy."
  fi
}

while IFS= read -r src; do
  check_skill_frontmatter "$src"
  if [[ "$CHECK_AGENTS_MIRROR" -eq 1 ]]; then
    compare_mirror_file "$src" "$AGENTS_DEST" "agents"
  fi
  if [[ "$CHECK_CLAUDE_MIRROR" -eq 1 ]]; then
    compare_mirror_file "$src" "$CLAUDE_DEST" "claude"
  fi
done < <(find "$SOURCE_DIR" -type f -name '*.md' | sort)

check_extra_mirror_files() {
  local dest_root="$1"
  local label="$2"

  while IFS= read -r dest; do
    local rel="${dest#${dest_root}/}"
    local skill="${rel%/SKILL.md}"
    local direct_source="$SOURCE_DIR/$skill.md"
    local self_named_source="$SOURCE_DIR/$skill/${skill##*/}.md"

    if [[ ! -f "$direct_source" && ! -f "$self_named_source" ]]; then
      error "Extra $label mirror without source: $rel"
    fi
  done < <(find "$dest_root" -type f -name 'SKILL.md' | sort)
}

if [[ "$CHECK_AGENTS_MIRROR" -eq 1 ]]; then
  check_extra_mirror_files "$AGENTS_DEST" "agents"
fi
if [[ "$CHECK_CLAUDE_MIRROR" -eq 1 ]]; then
  check_extra_mirror_files "$CLAUDE_DEST" "claude"
fi

check_shell_script_headers() {
  local script="$1"

  if ! sed -n '1p' "$script" | grep -qx '#!/usr/bin/env bash'; then
    error "Shell script shebang missing: $script"
  fi

  for header in '# 역할:' '# 입력/실행:' '# 예상 결과:' '# 주요 동작:'; do
    if ! sed -n '1,24p' "$script" | grep -q "^$header"; then
      error "Shell script header missing ($header): $script"
    fi
  done

  if ! bash -n "$script"; then
    error "Shell script syntax failed: $script"
  fi
}

while IFS= read -r script; do
  check_shell_script_headers "$script"
done < <(find "$REPO_ROOT/scripts" -maxdepth 1 -type f -name '*.sh' | sort)

if [[ "$FAIL" -ne 0 ]]; then
  exit 1
fi

echo "[harness] consistency check passed"
