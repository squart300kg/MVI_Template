#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
COMMAND="${1:-copy}"
SOURCE_DIR="$REPO_ROOT/.ai-skills"
AGENTS_DEST="$REPO_ROOT/.agents/skills"
CLAUDE_DEST="$REPO_ROOT/.claude/skills"

usage() {
  cat <<USAGE
Usage: $(basename "$0") [copy]

Commands:
  copy    Sync AGENTS.md to CLAUDE.md and mirror .ai-skills into .agents/.claude
USAGE
}

if [[ "$COMMAND" != "copy" ]]; then
  usage >&2
  exit 1
fi

if [[ ! -f "$REPO_ROOT/AGENTS.md" ]]; then
  echo "AGENTS.md not found." >&2
  exit 1
fi

if [[ ! -d "$SOURCE_DIR" ]]; then
  echo "Source skill directory not found: $SOURCE_DIR" >&2
  exit 1
fi

cp "$REPO_ROOT/AGENTS.md" "$REPO_ROOT/CLAUDE.md"
echo "[docs] AGENTS.md -> CLAUDE.md"

SOURCE_DIR="$(cd "$SOURCE_DIR" && pwd -P)"
mkdir -p "$AGENTS_DEST" "$CLAUDE_DEST"
AGENTS_DEST="$(cd "$AGENTS_DEST" && pwd -P)"
CLAUDE_DEST="$(cd "$CLAUDE_DEST" && pwd -P)"

is_unsafe_target() {
  local target="$1"
  [[ "$target" == "/" || "$target" == "$HOME" || "$target" == "$REPO_ROOT" ]]
}

is_global_home_skill_target() {
  local target="$1"
  [[ "$target" == "$HOME/.codex/skills" || "$target" == "$HOME/.codex/skills/"* ||
    "$target" == "$HOME/.agents/skills" || "$target" == "$HOME/.agents/skills/"* ||
    "$target" == "$HOME/.agent/skills" || "$target" == "$HOME/.agent/skills/"* ]]
}

for target in "$AGENTS_DEST" "$CLAUDE_DEST"; do
  if is_unsafe_target "$target" || is_global_home_skill_target "$target"; then
    echo "Unsafe sync target: $target" >&2
    exit 1
  fi
done

SOURCES=()
while IFS= read -r src; do
  SOURCES+=("$src")
done < <(find "$SOURCE_DIR" -type f -name '*.md' | sort)

if [[ ${#SOURCES[@]} -eq 0 ]]; then
  echo "No .ai-skills markdown files found." >&2
  exit 1
fi

sync_dest() {
  local dest_root="$1"
  local label="$2"
  local count=0

  rm -rf "$dest_root"
  mkdir -p "$dest_root"

  for src in "${SOURCES[@]}"; do
    local rel="${src#${SOURCE_DIR}/}"
    local skill="${rel%.md}"
    local dest_file="$dest_root/$skill/SKILL.md"

    mkdir -p "$(dirname "$dest_file")"
    cp "$src" "$dest_file"
    count=$((count + 1))
    printf '[%s] %s\n' "$label" "$dest_file"
  done

  printf '[%s] synced %d skills\n' "$label" "$count"
}

sync_dest "$AGENTS_DEST" "agents"
sync_dest "$CLAUDE_DEST" "claude"

echo "Sync complete:"
echo "- source : $SOURCE_DIR"
echo "- agents : $AGENTS_DEST"
echo "- claude : $CLAUDE_DEST"
