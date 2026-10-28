<#
.SYNOPSIS
  Clean up after a /ship demo run so it can be run again.

.DESCRIPTION
  For one Jira key: closes its open PRs, deletes its feature branches (local and remote) and removes
  the agent's local run state. With -RestoreMain, also resets main to the 'demo-baseline' tag
  (needed only if you merged a demo PR). Jira is never touched: to rerun a ticket, clone it in Jira
  or re-use the key after running this.

.EXAMPLE
  .\scripts\reset-demo.ps1 -Key DEMO-2
.EXAMPLE
  .\scripts\reset-demo.ps1 -RestoreMain
#>
[CmdletBinding()]
param(
  [string]$Key = '',
  [switch]$RestoreMain
)
$ErrorActionPreference = 'Stop'

$repo = (gh repo view --json nameWithOwner -q .nameWithOwner).Trim()
$owner, $name = $repo -split '/'
$agentHome = if ($env:SDLC_AGENT_HOME) { $env:SDLC_AGENT_HOME } else { Join-Path (Join-Path $HOME '.config') 'sdlc-agent' }

if ($Key) {
  $Key = $Key.ToUpper()
  Write-Host "Resetting $Key in $repo"
  git fetch --prune origin | Out-Null
  $prs = gh pr list --state open --json number,headRefName | ConvertFrom-Json | Where-Object { $_.headRefName -like "*$Key*" }
  foreach ($pr in $prs) {
    gh pr close $pr.number --delete-branch --comment "Demo reset" | Out-Null
    Write-Host "  closed PR #$($pr.number) ($($pr.headRefName))"
  }
  git fetch --prune origin 2>$null | Out-Null
  $current = (git rev-parse --abbrev-ref HEAD).Trim()
  if ($current -like "*$Key*") { git switch main | Out-Null }
  foreach ($b in (git branch --format '%(refname:short)') | Where-Object { $_ -like "*$Key*" }) {
    git branch -D $b | Out-Null; Write-Host "  deleted local branch $b"
  }
  $remote = git ls-remote --heads origin | ForEach-Object { ($_ -split "\s+")[1] -replace '^refs/heads/', '' }
  foreach ($b in $remote | Where-Object { $_ -like "*$Key*" }) {
    git push origin --delete $b 2>$null | Out-Null; Write-Host "  deleted remote branch $b"
  }
  git fetch --prune origin 2>$null | Out-Null
  $state = Join-Path (Join-Path (Join-Path $agentHome $owner) $name) $Key
  if (Test-Path $state) { Remove-Item -Recurse -Force $state; Write-Host "  removed agent state $state" }
  $active = Join-Path $agentHome 'active.json'
  if (Test-Path $active) {
    $json = Get-Content $active -Raw | ConvertFrom-Json
    $props = @($json.PSObject.Properties | Where-Object { $_.Value.ticket -eq $Key })
    foreach ($p in $props) { $json.PSObject.Properties.Remove($p.Name) }
    if ($props.Count) { $json | ConvertTo-Json -Depth 5 | Set-Content $active; Write-Host "  released guardrails" }
  }
}

if ($RestoreMain) {
  Write-Host "Restoring main to tag demo-baseline"
  git fetch origin --tags | Out-Null
  $sha = (git rev-list -n 1 demo-baseline).Trim()
  $protection = gh api "repos/$repo/branches/main/protection" 2>$null
  if ($LASTEXITCODE -eq 0) { gh api -X DELETE "repos/$repo/branches/main/protection" | Out-Null }
  gh api -X PATCH "repos/$repo/git/refs/heads/main" -f sha=$sha -F force=true | Out-Null
  if ($protection) { & "$PSScriptRoot\protect-main.ps1" }
  git switch main | Out-Null; git reset --hard origin/main | Out-Null; git pull --ff-only | Out-Null
  Write-Host "  main is back at $sha"
}

if (-not $Key -and -not $RestoreMain) { Get-Help $MyInvocation.MyCommand.Path -Detailed }
