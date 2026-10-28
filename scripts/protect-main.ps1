# Require the three CI checks on main. No review requirement, so a solo presenter can merge.
$ErrorActionPreference = 'Stop'
$repo = (gh repo view --json nameWithOwner -q .nameWithOwner).Trim()
$body = @{
  required_status_checks = @{ strict = $false; contexts = @('format', 'test', 'api-docs') }
  enforce_admins = $false
  required_pull_request_reviews = $null
  restrictions = $null
  allow_force_pushes = $false
  allow_deletions = $false
} | ConvertTo-Json -Depth 5
$body | gh api -X PUT "repos/$repo/branches/main/protection" --input - | Out-Null
Write-Host "  main protected: checks format, test, api-docs required"
