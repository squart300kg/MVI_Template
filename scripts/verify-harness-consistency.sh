#!/usr/bin/env bash
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
require_dir "$AGENTS_DEST"
require_dir "$CLAUDE_DEST"

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

compare_mirror_file() {
  local src="$1"
  local dest_root="$2"
  local label="$3"
  local rel="${src#${SOURCE_DIR}/}"
  local skill="${rel%.md}"
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
  compare_mirror_file "$src" "$AGENTS_DEST" "agents"
  compare_mirror_file "$src" "$CLAUDE_DEST" "claude"
done < <(find "$SOURCE_DIR" -type f -name '*.md' | sort)

check_extra_mirror_files() {
  local dest_root="$1"
  local label="$2"

  while IFS= read -r dest; do
    local rel="${dest#${dest_root}/}"
    local source_rel="${rel%/SKILL.md}.md"
    if [[ ! -f "$SOURCE_DIR/$source_rel" ]]; then
      error "Extra $label mirror without source: $rel"
    fi
  done < <(find "$dest_root" -type f -name 'SKILL.md' | sort)
}

check_extra_mirror_files "$AGENTS_DEST" "agents"
check_extra_mirror_files "$CLAUDE_DEST" "claude"

if command -v rg >/dev/null 2>&1; then
  if rg -n 'ModifiedBaseIntentViewModel|assembleDevDebug|com\.pickstudio\.buddystock|BuddyStockTheme' "$SOURCE_DIR" >/tmp/mvi-template-harness-rg.txt; then
    cat /tmp/mvi-template-harness-rg.txt >&2
    error "Previous-project residue found in .ai-skills."
  fi
else
  if grep -R -n -E 'ModifiedBaseIntentViewModel|assembleDevDebug|com\.pickstudio\.buddystock|BuddyStockTheme' "$SOURCE_DIR" >/tmp/mvi-template-harness-grep.txt; then
    cat /tmp/mvi-template-harness-grep.txt >&2
    error "Previous-project residue found in .ai-skills."
  fi
fi

if [[ "$FAIL" -ne 0 ]]; then
  exit 1
fi

echo "[harness] consistency check passed"
