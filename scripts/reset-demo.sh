#!/usr/bin/env bash
# Clean up after a /ship demo run so it can be run again (macOS/Linux/Git Bash).
#   scripts/reset-demo.sh DEMO-2            close PRs, delete branches, remove agent state for one key
#   scripts/reset-demo.sh --restore-main    reset main to the demo-baseline tag (only if you merged)
# Jira is never touched: clone the ticket in Jira, or re-use the key after a reset.
set -euo pipefail
repo="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
agent_home="${SDLC_AGENT_HOME:-$HOME/.config/sdlc-agent}"

reset_key() {
  local key; key="$(printf '%s' "$1" | tr '[:lower:]' '[:upper:]')"
  echo "Resetting $key in $repo"
  git fetch --prune origin >/dev/null
  gh pr list --state open --json number,headRefName -q ".[] | select(.headRefName | contains(\"$key\")) | .number" |
    while read -r n; do gh pr close "$n" --delete-branch --comment "Demo reset" >/dev/null; echo "  closed PR #$n"; done
  case "$(git rev-parse --abbrev-ref HEAD)" in *"$key"*) git switch main >/dev/null ;; esac
  git branch --format '%(refname:short)' | grep -F "$key" | while read -r b; do git branch -D "$b" >/dev/null; echo "  deleted local branch $b"; done || true
  git branch -r --format '%(refname:short)' | grep -F "origin/" | grep -F "$key" | sed 's#^origin/##' |
    while read -r b; do git push origin --delete "$b" >/dev/null; echo "  deleted remote branch $b"; done || true
  local state="$agent_home/$repo/$key"
  [ -d "$state" ] && rm -rf "$state" && echo "  removed agent state $state"
  if [ -f "$agent_home/active.json" ]; then
    python3 - "$agent_home/active.json" "$key" <<'PY'
import json, sys
path, key = sys.argv[1], sys.argv[2]
data = json.load(open(path))
kept = {k: v for k, v in data.items() if v.get("ticket") != key}
if kept != data:
    json.dump(kept, open(path, "w"), indent=2)
    print("  released guardrails")
PY
  fi
}

restore_main() {
  echo "Restoring main to tag demo-baseline"
  git fetch origin --tags >/dev/null
  local sha; sha="$(git rev-list -n 1 demo-baseline)"
  local protected=0
  gh api "repos/$repo/branches/main/protection" >/dev/null 2>&1 && protected=1
  [ "$protected" = 1 ] && gh api -X DELETE "repos/$repo/branches/main/protection" >/dev/null
  gh api -X PATCH "repos/$repo/git/refs/heads/main" -f sha="$sha" -F force=true >/dev/null
  [ "$protected" = 1 ] && "$(dirname "$0")/protect-main.sh"
  git switch main >/dev/null 2>&1 || true
  git fetch origin >/dev/null && git reset --hard origin/main >/dev/null
  echo "  main is back at $sha"
}

case "${1:-}" in
  --restore-main) restore_main ;;
  "" | -h | --help) sed -n '2,5p' "$0" ;;
  *) reset_key "$1" ;;
esac
