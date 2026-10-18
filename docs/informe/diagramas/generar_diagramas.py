"""Produce diagramas PNG a partir de la estructura auditada; fuente Mermaid editable al lado."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

HERE = Path(__file__).resolve().parent
NAVY = '#14243b'
BLUE = '#235c94'
TEAL = '#087f8c'
PALE = '#edf4fa'
LINE = '#8ca3b9'
TEXT = '#172538'
WHITE = '#ffffff'
FONT = r'C:\Windows\Fonts\arial.ttf'
BOLD = r'C:\Windows\Fonts\arialbd.ttf'


def font(size=24, bold=False):
    return ImageFont.truetype(BOLD if bold else FONT, size)


def canvas(w, h, title):
    image = Image.new('RGB', (w, h), WHITE)
    d = ImageDraw.Draw(image)
    d.text((46, 26), title, font=font(34, True), fill=NAVY)
    d.line((46, 78, w - 46, 78), fill=LINE, width=2)
    return image, d


def box(d, xy, title, subtitle='', fill=PALE, accent=BLUE, title_size=24):
    x1, y1, x2, y2 = xy
    d.rounded_rectangle(xy, radius=17, fill=fill, outline=LINE, width=2)
    d.rounded_rectangle((x1, y1, x1 + 9, y2), radius=5, fill=accent)
    d.text((x1 + 24, y1 + 16), title, font=font(title_size, True), fill=NAVY)
    if subtitle:
        for i, line in enumerate(subtitle.split('\n')):
            d.text((x1 + 24, y1 + 52 + i * 28), line, font=font(20), fill=TEXT)


def arrow(d, a, b, color=BLUE, width=4):
    d.line((*a, *b), fill=color, width=width)
    import math
    angle = math.atan2(b[1] - a[1], b[0] - a[0])
    side = 13
    p1 = (int(b[0] - 22 * math.cos(angle) + side * math.sin(angle)),
          int(b[1] - 22 * math.sin(angle) - side * math.cos(angle)))
    p2 = (int(b[0] - 22 * math.cos(angle) - side * math.sin(angle)),
          int(b[1] - 22 * math.sin(angle) + side * math.cos(angle)))
    d.polygon([b, p1, p2], fill=color)


def label(d, xy, text, size=20, color=TEXT):
    d.text(xy, text, font=font(size), fill=color)


def arquitectura():
    im, d = canvas(1600, 850, 'Arquitectura implementada')
    boxes = [
        ((65, 124, 360, 264), 'app', 'Main\nBibliotecaContexto', NAVY),
        ((440, 124, 735, 264), 'forms', 'Ventanas y eventos\nSwing', BLUE),
        ((815, 124, 1110, 264), 'service', 'Casos de uso y\nvalidaciones', TEAL),
        ((1190, 124, 1485, 264), 'colecciones', 'Datos* y ArrayList\nen memoria', BLUE),
    ]
    for xy, title, subtitle, color in boxes:
        box(d, xy, title, subtitle, accent=color)
    arrow(d, (360, 194), (440, 194)); arrow(d, (735, 194), (815, 194)); arrow(d, (1110, 194), (1190, 194))
    box(d, (815, 345, 1110, 490), 'clases', 'Entidades de dominio\ny códigos String', accent=TEAL)
    box(d, (1190, 345, 1485, 490), 'repository', 'Formatos, lectura y\ntransacciones', accent=NAVY)
    arrow(d, (1338, 264), (1338, 345)); arrow(d, (962, 264), (962, 345))
    box(d, (1190, 580, 1485, 725), 'datos/*.txt', 'Persistencia UTF-8\ncon separador ;', accent=BLUE)
    arrow(d, (1338, 490), (1338, 580))
    box(d, (65, 345, 735, 490), 'util', 'Rutas, Codigos, DatosLibroDetalle y DetalleLibro', accent=LINE)
    label(d, (65, 580), 'Dirección principal de llamadas: forms → service → colecciones → repository', 23)
    label(d, (65, 620), 'BibliotecaContexto construye y comparte las dependencias.', 23)
    im.save(HERE / 'arquitectura.png')


def modelo():
    im, d = canvas(1700, 1030, 'Entidades y vínculos por código')
    box(d, (70, 132, 530, 300), 'Usuario', 'codUser: String\nnombre, DNI, correo, teléfono, estado', accent=BLUE)
    box(d, (620, 132, 1080, 300), 'Prestamo', 'codPrestamo, codUsuario: String\nfechaPrestamo, fechaDevolucion: LocalDate', accent=TEAL)
    box(d, (1170, 132, 1630, 300), 'DetallePrestamo', 'codDetallePrestamo, codPrestamo\ncodLibro: String; cantidad: int', accent=BLUE)
    box(d, (1170, 440, 1630, 625), 'Libro', 'codLibro, codCategoria, autor: String\ntítulo, ISBN, año, existencias, portada', accent=TEAL)
    box(d, (620, 440, 1080, 625), 'LibroAutor', 'codLibro: String\ncodAutor: String', accent=BLUE)
    box(d, (70, 440, 530, 625), 'Autor', 'codigo: String\nnombre: String', accent=NAVY)
    box(d, (1170, 760, 1630, 925), 'Categoria', 'codCategoria: String\nnombre: String', accent=NAVY)
    arrow(d, (530, 218), (620, 218)); label(d, (540, 236), '1 : N', 17)
    arrow(d, (1080, 218), (1170, 218)); label(d, (1088, 236), '0 : N*', 17)
    arrow(d, (1400, 300), (1400, 440)); label(d, (1418, 355), 'libro', 18)
    arrow(d, (530, 532), (620, 532)); label(d, (540, 550), '1 : N', 17)
    arrow(d, (1080, 532), (1170, 532)); label(d, (1090, 550), '1 : 1', 17)
    arrow(d, (1400, 625), (1400, 760)); label(d, (1418, 674), 'categoría', 18)
    label(d, (70, 805), '* La historia permite P20 sin detalles; los nuevos préstamos registran detalles.', 22)
    im.save(HERE / 'modelo_clases.png')


def casos():
    im, d = canvas(1650, 880, 'Casos de uso observados')
    # actor
    d.ellipse((135, 170, 205, 240), outline=NAVY, width=5)
    d.line((170, 240, 170, 380), fill=NAVY, width=5)
    d.line((90, 285, 250, 285), fill=NAVY, width=5)
    d.line((170, 380, 95, 470), fill=NAVY, width=5)
    d.line((170, 380, 245, 470), fill=NAVY, width=5)
    label(d, (112, 490), 'Operador', 27)
    entries = [
        (100, 'Mantener usuarios', 'alta, consulta, cambio, baja y guardar'),
        (222, 'Mantener categorías', 'alta, consulta, cambio y baja'),
        (344, 'Mantener autores', 'autor principal y asociación'),
        (466, 'Mantener libros', 'catálogo, categoría y portada'),
        (588, 'Registrar préstamo', 'selección, detalle y existencias'),
        (710, 'Consultar historial y boleta', 'préstamo registrado'),
    ]
    for y, title, sub in entries:
        box(d, (520, y, 1500, y + 100), title, sub, accent=TEAL if y >= 588 else BLUE)
        arrow(d, (270, 315), (520, y + 50), color=LINE, width=3)
    im.save(HERE / 'casos_uso.png')


def secuencia():
    im, d = canvas(1700, 1210, 'Secuencia principal de registro de préstamo')
    cols = [100, 410, 750, 1090, 1430]
    names = ['frmPrincipal', 'PrestamoService', 'Datos* (memoria)', 'TransaccionArchivos', 'Archivos .txt']
    for x, name in zip(cols, names):
        box(d, (x, 120, x + 210, 205), name, accent=BLUE, title_size=17)
        d.line((x + 105, 205, x + 105, 1060), fill=LINE, width=2)
    steps = [
        (275, 0, 1, 'operador confirma; registrar(usuario, fechas)'),
        (365, 1, 2, 'validar libro y cantidad'),
        (455, 1, 2, 'agregar préstamo y detalles'),
        (545, 1, 2, 'descontar existencias'),
        (635, 1, 3, 'ejecutar(guardar tres archivos)'),
        (725, 3, 4, 'respaldos + marcador'),
        (815, 3, 4, 'prestamos, detalle, libros'),
        (905, 3, 1, 'éxito o excepción'),
        (995, 1, 0, 'préstamo / mostrar boleta'),
    ]
    for y, src, dst, desc in steps:
        xa, xb = cols[src] + 105, cols[dst] + 105
        arrow(d, (xa, y), (xb, y), color=TEAL if src > dst else BLUE, width=3)
        label(d, (min(xa, xb) + 8, y - 32), desc, 17)
    label(d, (100, 1100), 'Si falla: se restauran archivos, préstamo, detalles y existencias en memoria.', 23)
    im.save(HERE / 'secuencia_prestamo.png')


def flujo_validacion():
    im, d = canvas(1700, 1560, 'Validaciones del registro de préstamo')
    red = '#a8323e'
    pasos = [
        ('Operador pulsa «Realizar préstamo»', 'frmPrincipal', NAVY, None),
        ('Convertir fechas dd/MM/yyyy', 'LocalDate.parse', BLUE, 'Mensaje técnico de fecha'),
        ('¿Usuario seleccionado?', 'PrestamoService.registrar', TEAL, '«Seleccione un usuario»'),
        ('¿Al menos un libro?', 'PrestamoService.registrar', TEAL, '«Seleccione al menos un libro»'),
        ('¿Cada libro sigue en el catálogo?', 'DatosLibro.obtenerPorCodigo', TEAL, '«El libro seleccionado ya no existe»'),
        ('¿Cantidad > 0 y ≤ disponible?', 'por cada libro seleccionado', TEAL, '«No hay ejemplares disponibles de …»'),
        ('Crear préstamo y detalles; descontar stock', 'DatosPrestamo, DatosDetallePrestamo, DatosLibro', BLUE, None),
        ('Guardar tres archivos', 'TransaccionArchivos.ejecutar', BLUE, 'Revertir memoria y archivos; informar error'),
        ('Mostrar boleta', 'frmBoleta', NAVY, None),
    ]
    y = 115
    for i, (titulo, sub, color, rechazo) in enumerate(pasos):
        box(d, (120, y, 1000, y + 120), titulo, sub, accent=color, title_size=23)
        if rechazo:
            box(d, (1150, y + 10, 1620, y + 110), 'Rechazo', rechazo, fill='#fbeef0', accent=red, title_size=20)
            arrow(d, (1000, y + 60), (1150, y + 60), color=red, width=3)
            label(d, (1030, y + 24), 'no' if '¿' in titulo else 'error', 18, red)
        if i < len(pasos) - 1:
            arrow(d, (560, y + 120), (560, y + 160))
            if '¿' in titulo or rechazo:
                label(d, (575, y + 124), 'sí' if '¿' in titulo else 'ok', 18)
        y += 160
    label(d, (120, y + 5), 'Ningún cambio se guarda si una validación falla; el inventario queda igual.', 22)
    im.save(HERE / 'flujo_validacion_prestamo.png')


def persistencia():
    im, d = canvas(1700, 980, 'Escritura y recuperación de archivos')
    filas = [
        (130, 'Usuarios', 'botón Guardar datos', 'Escritura individual', 'temporal + reemplazo', 'usuarios.txt'),
        (300, 'Categorías', 'cada alta, cambio o baja', 'Escritura individual', 'restaura la lista si falla', 'categorias.txt'),
        (470, 'Autores y libros', 'cada operación', 'ejecutarAutores', 'respaldo .autores.bak', 'autores.txt\nlibroAutor.txt\nlibros.txt'),
        (640, 'Préstamos', 'Realizar préstamo', 'ejecutar', 'respaldo .prestamo.bak', 'prestamos.txt\ndetallePrestamo.txt\nlibros.txt'),
    ]
    for y, modulo, cuando, mecanismo, detalle, archivos in filas:
        box(d, (60, y, 500, y + 140), modulo, cuando, accent=BLUE)
        box(d, (610, y, 1070, y + 140), mecanismo, detalle, accent=TEAL)
        box(d, (1180, y, 1640, y + 140), 'datos/', archivos, accent=NAVY)
        arrow(d, (500, y + 70), (610, y + 70)); arrow(d, (1070, y + 70), (1180, y + 70))
    label(d, (60, 815), 'Cada colección se guarda en un archivo temporal que luego reemplaza al original.', 22)
    label(d, (60, 855), 'Las transacciones dejan un marcador (.autores-pendiente / .prestamo-pendiente);', 22)
    label(d, (60, 895), 'si el programa se cierra a mitad, BibliotecaContexto restaura los respaldos al iniciar.', 22)
    im.save(HERE / 'persistencia.png')


MERMAID = {
    'flujo_validacion_prestamo': '''flowchart TD
  A[Operador pulsa Realizar préstamo] --> B[Convertir fechas dd/MM/yyyy]
  B -->|error| X1[Mensaje técnico de fecha]
  B --> C{¿Usuario seleccionado?}
  C -->|no| X2[Seleccione un usuario]
  C -->|sí| D{¿Al menos un libro?}
  D -->|no| X3[Seleccione al menos un libro]
  D -->|sí| E{¿Cada libro sigue en el catálogo?}
  E -->|no| X4[El libro seleccionado ya no existe]
  E -->|sí| F{¿Cantidad > 0 y <= disponible?}
  F -->|no| X5[No hay ejemplares disponibles]
  F -->|sí| G[Crear préstamo y detalles; descontar stock]
  G --> H[TransaccionArchivos.ejecutar]
  H -->|error| X6[Revertir memoria y archivos; informar error]
  H -->|ok| I[Mostrar boleta]
''',
    'persistencia': '''flowchart LR
  U[Usuarios: Guardar datos] --> W1[Escritura individual] --> F1[usuarios.txt]
  C[Categorías: cada operación] --> W2[Escritura individual] --> F2[categorias.txt]
  A[Autores y libros: cada operación] --> T1[TransaccionArchivos.ejecutarAutores] --> F3[autores.txt, libroAutor.txt, libros.txt]
  P[Préstamos: Realizar préstamo] --> T2[TransaccionArchivos.ejecutar] --> F4[prestamos.txt, detallePrestamo.txt, libros.txt]
  S[Arranque] --> R[recuperarPendiente: restaura respaldos si hay marcador]
''',
    'arquitectura': '''flowchart LR
  Main[app.Main] --> C[BibliotecaContexto]
  C --> F[forms Swing]
  F --> S[service]
  S --> M[colecciones Datos y ArrayList]
  M --> R[repository]
  R --> T[datos txt]
  S --> D[clases entidades]
  C --> U[util Rutas y Codigos]
''',
    'modelo_clases': '''classDiagram
  Usuario "1" --> "0..*" Prestamo : codUsuario
  Prestamo "1" --> "0..*" DetallePrestamo : codPrestamo
  DetallePrestamo "0..*" --> "1" Libro : codLibro
  Autor "1" --> "0..*" LibroAutor : codAutor
  Libro "1" --> "1" LibroAutor : codLibro
  Categoria "1" --> "0..*" Libro : codCategoria
  class Usuario {
    +String codUser
  }
  class Prestamo {
    +String codPrestamo
    +String codUsuario
  }
  class DetallePrestamo {
    +String codDetallePrestamo
    +int cantPrestamo
  }
  class Libro {
    +String codLibro
    +int cantidadDisponible
  }
  class LibroAutor {
    +String codLibro
    +String codAutor
  }
  class Autor {
    +String codigo
  }
  class Categoria {
    +String codCategoria
  }
''',
    'casos_uso': '''flowchart LR
  O[Operador] --> U((Mantener usuarios))
  O --> C((Mantener categorías))
  O --> A((Mantener autores))
  O --> L((Mantener libros))
  O --> P((Registrar préstamo))
  O --> H((Consultar historial y boleta))
  P --> S((Seleccionar libros))
''',
    'secuencia_prestamo': '''sequenceDiagram
  actor Operador
  participant Form as frmPrincipal
  participant Servicio as PrestamoService
  participant Memoria as DatosPrestamo, DatosDetallePrestamo y DatosLibro
  participant Tx as TransaccionArchivos
  participant Disco as prestamos.txt, detallePrestamo.txt y libros.txt
  Operador->>Form: Confirmar préstamo
  Form->>Servicio: registrar(usuario, fechas)
  Servicio->>Servicio: validar selección y disponibilidad
  Servicio->>Memoria: agregar préstamo y detalles; descontar stock
  Servicio->>Tx: ejecutar(guardar tres archivos)
  Tx->>Disco: respaldar y escribir
  alt éxito
    Tx-->>Servicio: completar
    Servicio-->>Form: Prestamo
    Form-->>Operador: mostrar boleta
  else error
    Tx-->>Servicio: excepción y restauración de archivos
    Servicio->>Memoria: revertir préstamo, detalles y stock
    Form-->>Operador: informar error
  end
''',
}


if __name__ == '__main__':
    arquitectura(); modelo(); casos(); secuencia(); flujo_validacion(); persistencia()
    for name, source in MERMAID.items():
        (HERE / f'{name}.mmd').write_text(source, encoding='utf-8')
    print(f'DIAGRAMAS=6 PNG + {len(MERMAID)} MMD')
