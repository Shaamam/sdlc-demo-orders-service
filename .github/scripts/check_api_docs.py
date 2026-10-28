#!/usr/bin/env python3
"""Fail if any Spring MVC endpoint in src/main/java is missing from docs/API.md.

An endpoint counts as documented when some line of docs/API.md contains its HTTP method and its
full path (class-level @RequestMapping + method mapping), e.g. "| `POST` | `/api/orders/{id}/pay` |".
"""
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
MAPPING = re.compile(
    r"@(Get|Post|Put|Patch|Delete|Request)Mapping\s*(?:\(\s*(?:(?:value|path)\s*=\s*)?\"([^\"]*)\"[^)]*\))?"
)


def endpoints(source: str):
    base = ""
    found = []
    for match in MAPPING.finditer(source):
        kind, path = match.group(1), match.group(2) or ""
        following = source[match.end() : match.end() + 200]
        if kind == "Request":
            if re.match(r"\s*(public\s+)?(final\s+)?class\b", following) or "class " in following.split("{")[0]:
                base = path
            continue
        found.append((kind.upper(), (base + path) or "/"))
    return found


def documented(doc_lines, method, path):
    for line in doc_lines:
        plain = line.replace("`", "")
        if re.search(rf"\b{method}\b", plain) and re.search(rf"(?<![\w/{{}}]){re.escape(path)}(?![\w/{{}}])", plain):
            return True
    return False


def main() -> int:
    doc = (ROOT / "docs" / "API.md").read_text(encoding="utf-8").splitlines()
    missing = []
    for java in sorted((ROOT / "src" / "main" / "java").rglob("*.java")):
        for method, path in endpoints(java.read_text(encoding="utf-8")):
            if not documented(doc, method, path):
                missing.append(f"{method} {path}  ({java.relative_to(ROOT)})")
    if missing:
        print("docs/API.md is missing these endpoints:")
        for m in missing:
            print(f"  - {m}")
        print("Add a row for each to the table in docs/API.md.")
        return 1
    print("docs/API.md documents every endpoint.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
