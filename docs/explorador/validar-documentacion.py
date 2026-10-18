"""Comprueba rutas, líneas, fragmentos y diagramas del explorador generado."""
import json
import re
from pathlib import Path

root = Path(__file__).resolve().parents[2]
html = (root / "docs/explorador/index.html").read_text(encoding="utf-8")
match = re.search(r"^const DB = (\{.*\});$", html, re.M)
assert match, "No se encontró el índice de datos incrustado"
db = json.loads(match.group(1))
assert len(db["flows"]) == 10
assert len(db["dependencies"]) == db["classCount"]
assert not re.search(r'<(?:script|link|img)\b[^>]+(?:src|href)=["\']https?://', html, re.I), "Dependencia de red en HTML"

references = 0
for flow in db["flows"]:
    markdown = root / "docs/flujos" / (flow["id"] + ".md")
    assert markdown.is_file(), markdown
    body = markdown.read_text(encoding="utf-8")
    assert all(f"## {section}." in body for section in "ABCDEFGH"), flow["id"]
    assert body.count("```mermaid") == 2, flow["id"]
    assert flow["sequenceMermaid"].startswith("sequenceDiagram")
    assert flow["flowMermaid"].startswith("flowchart TD")
    ids = {x[0] for x in flow["flow"]}
    assert all(a in ids and b in ids for a, b, _ in flow["edges"])
    for method in flow["refs"]:
        path = root / method["path"]
        assert path.is_file(), path
        lines = path.read_text(encoding="utf-8").splitlines()
        number = method["line"]
        snippet = method["snippet"].splitlines()
        assert lines[number - 1:number - 1 + len(snippet)] == snippet, (path, number)
        assert f"{method['path']}:{number}" in body, (path, number)
        assert method["signature"] in body
        assert (markdown.parent / "../../" / method["path"]).resolve() == path.resolve()
        references += 1

print(f"OK: {len(db['flows'])} Markdown, {db['classCount']} tipos, {references} referencias con ruta, línea y fragmento exactos; 20 diagramas Mermaid")
