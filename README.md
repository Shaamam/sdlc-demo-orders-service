# orders-service: demo target for `/ship`

A small Spring Boot 4 / Java 21 REST API used to demonstrate [**sdlc-agent**](https://github.com/Shaamam/sdlc-agent): a Claude Code skill that takes a Jira story all the way to a merged pull request.

- **[Demo ticket pack](docs/demo-tickets.md)**: six Jira tickets to paste into your project, with what the agent should do for each, scripted answers and review comments, and how to rerun.
- **[API reference](docs/API.md)**

## The service

| Area | Endpoints | Planted for the demo |
|---|---|---|
| Orders | create, list, get, pay, ship | a real pricing bug (quantity ignored), no cancel |
| Products | list, get, create (30 seeded) | no pagination |
| Customers | register, get | no email validation, duplicates allowed |
| Coupons | get by code (`WELCOME10`, `SUMMER25`, expired `SPRING15`) | no validity check |

The database is in-memory H2, re-seeded from `src/main/resources/data.sql` on every start, so nothing persists between runs.

## Run it

Needs **JDK 21**. Windows: `winget install EclipseAdoptium.Temurin.21.JDK`; macOS: `brew install openjdk@21`.

```powershell
.\mvnw.cmd spring-boot:run          # Windows PowerShell
./mvnw spring-boot:run              # macOS / Linux / Git Bash
```
Then:
```powershell
curl http://localhost:8080/api/products/1
curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" `
     -d '{\"customerEmail\":\"asha@example.com\",\"items\":[{\"sku\":\"BOOK-001\",\"quantity\":3}]}'
#   → "total": 12.99   (the planted bug: should be 38.97)
```
H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:orders`, user `sa`, empty password).

## Quality gates

Locally, `./mvnw -B verify` runs everything except the API-docs check:

| Gate | Tool | Fails when |
|---|---|---|
| Format | Spotless + google-java-format | code isn't formatted (`./mvnw spotless:apply` fixes it) |
| Tests | JUnit 5, MockMvc, AssertJ | any test fails |
| Architecture | ArchUnit | a controller uses a repository directly |
| Coverage | JaCoCo | line coverage drops below 80% |

CI (`.github/workflows/ci.yml`) runs on every pull request and push to `main`, as three checks that are all **required on `main`**:

| Check | Runs |
|---|---|
| `format` | `./mvnw spotless:check` |
| `test` | `./mvnw verify` (tests, ArchUnit, coverage) |
| `api-docs` | `.github/scripts/check_api_docs.py`: every controller endpoint must be listed in `docs/API.md` |

`api-docs` deliberately runs only in CI, like the checks real teams have that nobody runs locally. It gives the agent's CI-triage loop something genuine to catch when a ticket adds an endpoint.

## Resetting between demos

```powershell
.\scripts\reset-demo.ps1 -Key DEMO-3     # close the PR, delete branches, clear agent state for one ticket
.\scripts\reset-demo.ps1 -RestoreMain    # put main back to the demo-baseline tag (only needed after merging)
```
macOS / Linux / Git Bash: `scripts/reset-demo.sh DEMO-3`, `scripts/reset-demo.sh --restore-main`.

## Using your own copy

To demo from your own GitHub account or org:
```powershell
gh repo fork Shaamam/sdlc-demo-orders-service --clone      # or: create an empty repo and push this one to it
cd sdlc-demo-orders-service
git tag demo-baseline; git push origin demo-baseline
.\scripts\protect-main.ps1                                   # require format, test, api-docs on main
```
On a fork, enable Actions first (Actions tab → "I understand… enable").
