"""Prepara HTML de importación para Word a partir del Markdown del informe.

Uso: python docs/informe/generar_word.py <archivo-html-temporal>
Después abrir el HTML en Word y guardarlo como DOCX. Se conserva esta fuente para
que el equipo pueda regenerar el borrador sin mantener dos contenidos divergentes.
"""
from pathlib import Path
from html import escape
import re
import sys

ROOT = Path(__file__).resolve().parent
MD = ROOT / 'Informe_Integral_AppBiblioteca_Borrador.md'


def inline(s):
    s = escape(s)
    s = re.sub(r'!\[([^]]*)\]\(([^)]+)\)', lambda m: '<img alt="' + m[1] + '" src="' +
               (ROOT / m[2]).resolve().as_uri() + '" />', s)
    s = re.sub(r'\[([^]]+)\]\(([^)]+)\)', lambda m: '<a href="' +
               ((ROOT / m[2].split('#')[0]).resolve().as_uri() if not m[2].startswith('#') else m[2]) +
               '">' + m[1] + '</a>', s)
    s = re.sub(r'\*\*(.+?)\*\*', r'<strong>\1</strong>', s)
    s = re.sub(r'`([^`]+)`', r'<code>\1</code>', s)
    return s.replace('  \n', '<br/>')


def render(text):
    lines = text.splitlines()
    out = []
    # Portada propia: los campos no deben quedar justificados como prosa.
    out.append('<p class="school">CIBERTEC</p>')
    out.append('<p class="coverkind">INFORME DE PROYECTO</p>')
    out.append('<h1 class="reporttitle">Informe integral de AppBiblioteca</h1>')
    out.append('<p class="coversub">Borrador académico para revisión grupal</p>')
    out.append('<table class="coverfields"><tbody>')
    fields = [
        ('Programa o carrera', 'PENDIENTE DE COMPLETAR'),
        ('Curso y sección', 'PENDIENTE DE COMPLETAR'),
        ('Docente', 'PENDIENTE DE COMPLETAR'),
        ('Integrantes y coordinador', 'PENDIENTE DE COMPLETAR'),
        ('Sede y fecha de entrega', 'PENDIENTE DE COMPLETAR'),
        ('Elaboración del borrador', '18/10/2026'),
    ]
    for key, value in fields:
        out.append(f'<tr><td><strong>{key}</strong></td><td>{value}</td></tr>')
    out.append('</tbody></table>')
    out.append('<p class="coverversion">Versión analizada: rama feature/CIB-001, código disponible en el repositorio al elaborar este borrador.</p>')
    out.append('<p class="covernote"><strong>Criterio de evidencia.</strong> Este informe describe el código ejecutable actual. Los documentos iniciales y el informe preliminar sirven para reconstruir la motivación académica; sus propuestas que no aparecen en el código se indican como pendientes. Los ejemplos de registros son ilustrativos y no reproducen datos personales reales.</p>')
    i = next(j for j, line in enumerate(lines) if line.strip() == '## Índice')
    chapter = 0
    in_cover = True
    while i < len(lines):
        raw = lines[i]
        s = raw.strip()
        if not s:
            i += 1
            continue
        if s.startswith('```'):
            lang = s[3:]
            block = []
            i += 1
            while i < len(lines) and not lines[i].strip().startswith('```'):
                block.append(lines[i])
                i += 1
            out.append('<pre class="code">' + escape('\n'.join(block)) + '</pre>')
            i += 1
            continue
        if s.startswith('|') and i + 1 < len(lines) and re.match(r'^\|[ :\-|]+\|$', lines[i + 1].strip()):
            headers = [x.strip() for x in s.strip('|').split('|')]
            i += 2
            rows = []
            while i < len(lines) and lines[i].strip().startswith('|'):
                rows.append([x.strip() for x in lines[i].strip().strip('|').split('|')])
                i += 1
            out.append('<table><thead><tr>' + ''.join('<th>' + inline(x) + '</th>' for x in headers) + '</tr></thead><tbody>')
            for k, row in enumerate(rows):
                out.append('<tr class="' + ('alt' if k % 2 else '') + '">' + ''.join('<td>' + inline(x).replace(';', ';&#8203;') + '</td>' for x in row) + '</tr>')
            out.append('</tbody></table>')
            continue
        if s.startswith('# '):
            title = s[2:]
            if title.startswith('Capítulo'):
                chapter += 1
                out.append('<h1 class="chapter">' + inline(title) + '</h1>')
            elif title == 'Anexos':
                out.append('<h1 class="chapter">Anexos</h1>')
            else:
                out.append('<h1 class="reporttitle">' + inline(title) + '</h1>')
            i += 1
            continue
        if s.startswith('## '):
            title = s[3:]
            if title == 'Índice':
                out.append('<p class="index">Índice</p><p>INDICE_AUTOMATICO</p>')
                i += 1
                while i < len(lines) and (not lines[i].strip() or re.match(r'^\d+\. \[', lines[i].strip())):
                    i += 1
                continue
            out.append('<h2>' + inline(title) + '</h2>')
            i += 1
            continue
        if s.startswith('### '):
            out.append('<h3>' + inline(s[4:]) + '</h3>')
            i += 1
            continue
        m = re.match(r'^\*((Tabla|Figura) \d+\..*)\*$', s)
        if m:
            out.append('<p class="caption ' + m[2].lower() + '">' + inline(m[1]) + '</p>')
            i += 1
            continue
        if s.startswith('> '):
            out.append('<p class="note">' + inline(s[2:]) + '</p>')
            i += 1
            continue
        if s.startswith('!['):
            out.append('<p class="diagram">' + inline(s) + '</p>')
            i += 1
            continue
        if s.startswith('- '):
            items = []
            while i < len(lines) and lines[i].strip().startswith('- '):
                items.append(lines[i].strip()[2:]); i += 1
            out.append('<ul>' + ''.join('<li>' + inline(x) + '</li>' for x in items) + '</ul>')
            continue
        paragraph = [raw]
        i += 1
        while i < len(lines) and lines[i].strip() and not re.match(r'^(#|>|\||```|- |!\[|\*(Tabla|Figura) )', lines[i].strip()):
            paragraph.append(lines[i]); i += 1
        content = '<br/>'.join(inline(x.rstrip()) for x in paragraph)
        out.append('<p>' + content + '</p>')
    return '\n'.join(out)


CSS = '''
@page { size: letter; margin: 2.2cm 2.15cm 2.15cm 2.35cm; }
body { font-family: Aptos, Calibri, Arial, sans-serif; font-size: 10.5pt; color: #1b2430; line-height: 1.3; }
p { margin: 0 0 8pt 0; text-align: justify; }
h1, h2, h3 { font-family: Aptos Display, Calibri, Arial, sans-serif; color: #000000; page-break-after: avoid; }
h1.reporttitle { font-size: 24pt; margin: 64pt 0 18pt 0; text-align: center; }
p.school { font-size: 15pt; font-weight: bold; text-align: center; margin: 32pt 0 5pt 0; }
p.coverkind { font-size: 10pt; letter-spacing: 2pt; text-align: center; }
p.coversub { font-size: 13pt; text-align: center; margin: 0 0 55pt 0; }
table.coverfields { width: 84%; margin: 0 auto 28pt auto; font-size: 10pt; }
table.coverfields td { border: 0; padding: 5pt 8pt; background: white; }
table.coverfields td:first-child { width: 43%; }
p.coverversion { text-align: center; font-size: 9pt; margin: 20pt 0; }
p.covernote { font-size: 9pt; text-align: left; margin: 45pt 35pt 0 35pt; }
p.index { page-break-before: always; font-size: 16pt; font-weight: bold; margin: 30pt 0 15pt 0; text-align: left; }
h1.chapter { page-break-before: always; font-size: 16pt; margin: 16pt 0 16pt 0; }
h2 { font-size: 12.5pt; margin: 16pt 0 7pt 0; }
h3 { font-size: 11pt; margin: 12pt 0 6pt 0; }
table { border-collapse: collapse; width: 100%; margin: 10pt 0 14pt 0; font-size: 8.5pt; page-break-inside: auto; }
th, td { border: 1px solid #d9d9d9; padding: 6pt 7pt; vertical-align: middle; text-align: left; }
th { background: #213f62; color: #ffffff; font-weight: bold; }
tr.alt td { background: #f1f5f9; }
tr { page-break-inside: avoid; }
code { font-family: Consolas, monospace; font-size: 9pt; }
pre.code { font-family: Consolas, monospace; font-size: 8.5pt; background: #f2f5f8; padding: 8pt; white-space: pre-wrap; }
p.note { margin: 12pt 0; font-size: 9.5pt; }
p.caption { font-size: 9pt; font-weight: bold; color: #213f62; text-align: left; margin: 12pt 0 3pt 0; page-break-after: avoid; }
p.caption.figura { text-align: center; }
p.diagram { text-align: center; margin: 12pt 0; }
p.diagram img { width: 590px; height: auto; }
a { color: #225a91; text-decoration: none; }
ul { margin-top: 2pt; margin-bottom: 10pt; }
li { margin-bottom: 5pt; }
'''

if __name__ == '__main__':
    output = Path(sys.argv[1])
    output.write_text('<!DOCTYPE html><html><head><meta charset="utf-8"><style>' + CSS +
                      '</style></head><body>' + render(MD.read_text(encoding='utf-8')) + '</body></html>',
                      encoding='utf-8')
    print(f'HTML_WORD={output}')
