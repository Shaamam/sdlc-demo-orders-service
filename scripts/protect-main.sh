#!/usr/bin/env bash
# Require the three CI checks on main. No review requirement, so a solo presenter can merge.
set -euo pipefail
repo="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
gh api -X PUT "repos/$repo/branches/main/protection" --input - >/dev/null <<'JSON'
{"required_status_checks": {"strict": false, "contexts": ["format", "test", "api-docs"]},
 "enforce_admins": false, "required_pull_request_reviews": null, "restrictions": null,
 "allow_force_pushes": false, "allow_deletions": false}
JSON
echo "  main protected: checks format, test, api-docs required"
