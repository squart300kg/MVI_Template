#!/usr/bin/env bash
# 역할: Android feature source가 starter의 navigation/global UI 경계를 지키는지 검증합니다.
# 입력/실행:
#   - ./scripts/verify-architecture-rules.sh
# 예상 결과:
#   - feature source에서 직접 navigation/global UI 위반이 없으면 성공합니다.
#   - 위반 파일과 라인을 출력하고 non-zero로 종료합니다.
# 주요 동작:
#   - feature/*/src/main/kotlin 하위 production source를 검색합니다.
#   - NavController 직접 사용과 전역 progress/dialog 직접 렌더링을 차단합니다.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
FEATURE_ROOT="$REPO_ROOT/feature"
FAIL=0

error() {
  echo "[architecture] ERROR: $*" >&2
  FAIL=1
}

if [[ ! -d "$FEATURE_ROOT" ]]; then
  echo "[architecture] no feature directory found"
  exit 0
fi

SOURCE_DIRS=()
while IFS= read -r dir; do
  SOURCE_DIRS+=("$dir")
done < <(find "$FEATURE_ROOT" -path '*/src/main/kotlin' -type d | sort)

if [[ ${#SOURCE_DIRS[@]} -eq 0 ]]; then
  echo "[architecture] no feature production source found"
  exit 0
fi

check_no_matches() {
  local rule_id="$1"
  local message="$2"
  local pattern="$3"
  local result_file

  result_file="$(mktemp)"
  if rg -n --glob '*.kt' "$pattern" "${SOURCE_DIRS[@]}" >"$result_file"; then
    cat "$result_file" >&2
    error "$rule_id: $message"
  fi
  rm -f "$result_file"
}

check_no_matches \
  "SA-NAV-001" \
  "feature Composable/ViewModel must not depend on NavHostController, NavController, or LocalUriHandler directly." \
  '\b(NavHostController|NavController|LocalUriHandler)\b'

check_no_matches \
  "SA-NAV-002" \
  "feature source must request navigation through BaseViewModel navigateTo/navigateBack/navigateWeb instead of direct navigation calls." \
  '\bnavController\.(navigate|popBackStack)\b|\.popBackStack\('

check_no_matches \
  "SA-GLOBAL-UI-001" \
  "feature source must not render global progress or error dialog directly." \
  '\b(BaseProgressBar|BaseCenterDialog)\b'

if [[ "$FAIL" -ne 0 ]]; then
  exit 1
fi

echo "[architecture] rules check passed"
