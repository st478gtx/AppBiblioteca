"""Genera documentación verificable desde el Java actual. Ejecutar desde la raíz del proyecto."""
from __future__ import annotations

import html
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
SRC = ROOT / "Biblioteca" / "src"
TEST = ROOT / "Biblioteca" / "test" / "PruebasBiblioteca.java"
OUT = ROOT / "docs" / "explorador"
MD = ROOT / "docs" / "flujos"
OUT.mkdir(parents=True, exist_ok=True)
MD.mkdir(parents=True, exist_ok=True)


def ref(cls: str, method: str, why: str, caller: str, next_call: str) -> dict:
    return dict(cls=cls, method=method, why=why, caller=caller, next=next_call)


FLOWS = [
    dict(id="arquitectura", title="Arquitectura general", layer="app", icon="◈",
         business=["El sistema organiza el catálogo y los préstamos en una aplicación de escritorio. El lector de esta sección es quien estudia el proyecto; no hay una operación de usuario que modifique datos.",
                   "Las ventanas reciben un BibliotecaContexto compartido. Las entidades viajan entre presentación, servicios y colecciones; los repositorios traducen a líneas de texto."],
         technical=["La dirección habitual es forms → service → colecciones → repository; app construye las dependencias. clases contiene entidades; util aporta rutas, códigos y la selección temporal.",
                    "No todas las acciones siguen idéntica persistencia: usuarios requieren Guardar, categorías guardan al operar, autores y libros usan una transacción coordinada, y préstamos guardan tres archivos."],
         data=["Estado: ArrayList en DatosUsuario, DatosCategoria, DatosAutor, DatosLibro, DatosPrestamo y DatosDetallePrestamo; DatosLibroDetalle conserva la selección temporal.",
               "Archivos: usuarios.txt (6 campos), categorias.txt (2), autores.txt (2), libros.txt (9), libroAutor.txt (2), prestamos.txt (4), detallePrestamo.txt (4). Los dos archivos de autores pueden no existir todavía."],
         debug=["Empieza en app.Main.main y entra con Step Into en el Runnable de EventQueue. Sigue BibliotecaContexto y luego el constructor de frmPrincipal.",
                "Inspecciona contexto, las referencias a servicios y las listas de Datos*. Step Over en la construcción de widgets; Step Into cuando se cree cada repositorio."],
         tests=["PruebasBiblioteca.main crea copias temporales y recorre operaciones de todas las capas. La inspección de la UI Swing y sus imágenes requiere ejecución manual en IntelliJ."],
         refs=[ref("app.Main","main","Inicia Swing en el EDT.","JVM","BibliotecaContexto, frmPrincipal"),
               ref("app.BibliotecaContexto","BibliotecaContexto","Compone repositorios, colecciones y servicios.","Main.main","TransaccionArchivos.recuperarPendiente, constructores Datos*"),
               ref("repository.ArchivoTextoRepository","cargar","Lee registros UTF-8.","Datos*.recuperar","Formatos.leer*"),
               ref("repository.ArchivoTextoRepository","guardar","Sustituye un archivo de texto.","Datos*.guardar","Formatos.escribir*"),
               ref("util.Rutas","dato","Resuelve la ubicación de un .txt.","BibliotecaContexto","Rutas.raiz")],
         seq=[("JVM","Main","main"),("Main","Contexto","new BibliotecaContexto"),("Contexto","Repositorio","crear repositorios"),("Contexto","Datos","cargar ArrayList"),("Contexto","Servicios","inyectar Datos"),("Main","frmPrincipal","new frmPrincipal(contexto)")],
         flow=[("A","Inicio JVM"),("B","Crear contexto"),("C","Recuperar pendientes"),("D","Cargar colecciones"),("E","Crear servicios"),("F","Mostrar ventana")],
         edges=[("A","B",""),("B","C",""),("C","D",""),("D","E",""),("E","F","")]),
    dict(id="arranque", title="Arranque e inicialización", layer="app", icon="▷",
         business=["El usuario ejecuta la configuración Biblioteca de IntelliJ. No introduce datos en este paso; espera que abra la ventana principal con catálogo y usuarios.",
                   "Un archivo inválido o una recuperación pendiente imposible impide abrir la aplicación y se muestra un mensaje de error."],
         technical=["Main.main configura la apariencia y programa la creación de frmPrincipal en EventQueue. BibliotecaContexto recupera transacciones pendientes antes de cargar colecciones.",
                    "Cada Datos* carga su repositorio al construirse. DatosAutor enlaza autores históricos en memoria si faltan los archivos nuevos. frmPrincipal crea menús, repisa y selector de usuarios."],
         data=["Lectura al arrancar: cinco archivos históricos; autores.txt y libroAutor.txt solo si existen ambos. Si una colección inicial está vacía, DatosUsuario, DatosCategoria o DatosLibro puede insertar y guardar valores iniciales.",
               "La migración histórica de autores no escribe al arrancar. La recuperación de transacciones sí puede restaurar respaldos si existe un marcador pendiente."],
         debug=["Breakpoint en Main.main:13, luego BibliotecaContexto.BibliotecaContexto y TransaccionArchivos.recuperarPendiente.",
                "Inspecciona biblioteca.home, Rutas.raiz(), existencia de marcadores, listas cargadas y tamaño de cada colección. Step Into en recuperar; Step Over sobre la creación de cada repositorio."],
         tests=["PruebasBiblioteca.main comprueba recarga, datos corruptos, P20 y recuperación de marcadores. Abrir la ventana y validar la apariencia es manual."],
         refs=[ref("app.Main","main","Punto de entrada único.","JVM","EventQueue.invokeLater, BibliotecaContexto"),
               ref("repository.TransaccionArchivos","recuperarPendiente","Restituye escrituras interrumpidas.","BibliotecaContexto","recuperarAutores, Files.copy"),
               ref("app.BibliotecaContexto","BibliotecaContexto","Construye el grafo de dependencias.","Main.main","Datos*, servicios"),
               ref("colecciones.DatosAutor","DatosAutor","Carga o deriva asociaciones históricas.","BibliotecaContexto","repositorio.cargar, validar"),
               ref("forms.frmPrincipal","frmPrincipal","Crea la ventana y conecta menús.","Main.main","cargarRepisa, agregarUsuariosCombo")],
         seq=[("JVM","Main","main"),("Main","EDT","invokeLater"),("EDT","Contexto","new"),("Contexto","Transaccion","recuperarPendiente"),("Contexto","Datos","recuperar"),("Datos","Repositorio","cargar"),("Repositorio","Datos","ArrayList"),("EDT","frmPrincipal","new + setVisible")],
         flow=[("A","Ejecutar app.Main"),("B","Recuperar marcadores"),("C","¿Archivos válidos?"),("D","Cargar datos y servicios"),("E","Mostrar frmPrincipal"),("X","Mostrar error")],
         edges=[("A","B",""),("B","C",""),("C","D","sí"),("C","X","no"),("D","E","")]),
    dict(id="usuarios", title="Gestión de usuarios", layer="service", icon="♙",
         business=["Desde Mantenimiento → Usuario se registran, buscan, modifican y eliminan lectores. Se introducen nombre, DNI, correo, teléfono y estado.",
                   "El servicio valida campos y formatos; DatosUsuario evita DNI duplicado. Solo se elimina un usuario en estado Registrado. Agregar, modificar y eliminar cambian memoria; el botón Guardar escribe usuarios.txt."],
         technical=["frmUsuario recibe UsuarioService de BibliotecaContexto. Los botones llaman a agregar, actualizar, eliminar o guardar. El formulario captura errores de validación y actualiza la tabla y el selector de la ventana principal.",
                    "UsuarioService valida; DatosUsuario asigna código U*, consulta ArrayList y delega guardar al repositorio. La búsqueda por código o DNI recorre la colección."],
         data=["Usuario usa codUser String. usuarios.txt: código;nombre;DNI;correo;teléfono;estado. Los cambios pendientes en memoria pueden perderse si se cierra sin pulsar Guardar.",
               "Formato real: Formatos.leerUsuario/escribirUsuario. El archivo se lee en DatosUsuario.recuperar y se escribe en DatosUsuario.guardar."],
         debug=["Breakpoint inicial en frmUsuario.actionPerformedBtnAgregar; para edición/eliminación usa actionPerformedBtnModificar o actionPerformedBtnEliminar.",
                "Sigue UsuarioService.validar, DatosUsuario.agregar/actualizar/eliminar y, al pulsar Guardar, ArchivoTextoRepository.guardar. Inspecciona DNI, estado, codUser y longitud del ArrayList antes y después."],
         tests=["PruebasBiblioteca.main registra, actualiza, guarda, recarga y elimina un usuario sobre copias temporales. La tabla, mensajes y necesidad de pulsar Guardar se verifican manualmente."],
         refs=[ref("forms.frmUsuario","actionPerformedBtnAgregar","Construye Usuario desde el formulario.","JButton Agregar","UsuarioService.agregar"),
               ref("service.UsuarioService","agregar","Valida antes de delegar.","frmUsuario.actionPerformedBtnAgregar","validar, DatosUsuario.agregar"),
               ref("service.UsuarioService","validar","Comprueba campos y formatos.","UsuarioService.agregar/actualizar","IllegalArgumentException"),
               ref("colecciones.DatosUsuario","agregar","Comprueba DNI y asigna código.","UsuarioService.agregar","asignarCodigo, ArrayList.add"),
               ref("forms.frmUsuario","actionPerformedBtnGuardar","Inicia persistencia explícita.","JButton Guardar","UsuarioService.guardar"),
               ref("colecciones.DatosUsuario","guardar","Envía copia de la lista al repositorio.","UsuarioService.guardar","Repositorio.guardar"),
               ref("repository.Formatos","escribirUsuario","Serializa seis campos.","ArchivoTextoRepository.guardar","getters de Usuario")],
         seq=[("Usuario","frmUsuario","clic Agregar"),("frmUsuario","UsuarioService","agregar(Usuario)"),("UsuarioService","DatosUsuario","agregar"),("DatosUsuario","UsuarioService","resultado en memoria"),("Usuario","frmUsuario","clic Guardar"),("frmUsuario","DatosUsuario","guardar vía servicio"),("DatosUsuario","Repositorio","guardar usuarios.txt")],
         flow=[("A","Ingresar datos"),("B","Validar formato"),("C","¿DNI duplicado?"),("D","Agregar en memoria"),("E","Pulsar Guardar"),("F","Escribir usuarios.txt"),("X","Mostrar error")],
         edges=[("A","B",""),("B","C","válido"),("B","X","inválido"),("C","X","sí"),("C","D","no"),("D","E",""),("E","F","")]),
    dict(id="categorias", title="Gestión de categorías", layer="service", icon="▤",
         business=["Desde Mantenimiento → Categoría se registran, buscan, modifican y eliminan géneros por nombre. La tabla refleja el catálogo disponible.",
                   "El código C* se asigna al agregar. El formulario muestra las excepciones; el servicio revierte el ArrayList si falla el guardado."],
         technical=["frmCategoria enlaza botones con CategoriaService. Este delega a DatosCategoria, que mantiene ArrayList y llama a Repositorio.guardar durante cada alta, edición o baja.",
                    "La búsqueda usa el texto normalizado sin tildes. frmLibro consulta categorías para su JComboBox y usa codCategoria en Libro."],
         data=["Categoria: codCategoria String y nombre. categorias.txt: código;nombre. El repositorio sustituye el archivo tras serializar cada registro.",
               "Eliminar una categoría no valida aquí si hay libros asociados; el flujo actual no implementa esa regla."],
         debug=["Empieza en frmCategoria.actionPerformedBtnAgregar. Sigue CategoriaService.agregar → DatosCategoria.agregar → DatosCategoria.guardar → ArchivoTextoRepository.guardar.",
                "Inspecciona nombre, codCategoria, lista antes/después y ruta de categorias.txt. Para búsqueda, entra en DatosCategoria.buscar."],
         tests=["PruebasBiblioteca.main comprueba alta, edición y baja de categoría con recarga. La interacción visual y la selección en frmLibro requieren revisión manual."],
         refs=[ref("forms.frmCategoria","actionPerformedBtnAgregar","Lee el nombre y crea Categoria.","JButton Agregar","CategoriaService.agregar"),
               ref("service.CategoriaService","agregar","Coordina mutación y guardado con reversión.","frmCategoria.actionPerformedBtnAgregar","DatosCategoria.agregar, guardar"),
               ref("colecciones.DatosCategoria","agregar","Asigna C* y añade al ArrayList.","CategoriaService.agregar","asignarCodigo"),
               ref("colecciones.DatosCategoria","buscar","Filtra nombres sin tildes.","CategoriaService.buscar","DatosLibro.quitarTildes"),
               ref("colecciones.DatosCategoria","guardar","Persiste la lista.","CategoriaService.agregar/actualizar/eliminar","Repositorio.guardar"),
               ref("repository.Formatos","escribirCategoria","Serializa código y nombre.","ArchivoTextoRepository.guardar","getters de Categoria")],
         seq=[("Usuario","frmCategoria","clic Agregar"),("frmCategoria","CategoriaService","agregar(Categoria)"),("CategoriaService","DatosCategoria","agregar"),("CategoriaService","DatosCategoria","guardar"),("DatosCategoria","Repositorio","guardar categorias.txt"),("Repositorio","CategoriaService","éxito o error"),("CategoriaService","frmCategoria","mensaje")],
         flow=[("A","Ingresar nombre"),("B","Asignar código C*"),("C","Agregar a ArrayList"),("D","Guardar archivo"),("E","¿Guardado correcto?"),("F","Actualizar tabla"),("X","Restaurar lista y mostrar error")],
         edges=[("A","B",""),("B","C",""),("C","D",""),("D","E",""),("E","F","sí"),("E","X","no")]),
    dict(id="autores", title="Gestión de autores", layer="service", icon="✎",
         business=["Desde Mantenimiento → Autor se puede registrar, buscar, renombrar o eliminar un autor. Cada libro tiene un autor principal; un autor puede figurar en varios libros.",
                   "Los nombres duplicados por espacios o mayúsculas se rechazan. No se elimina un autor con libros asociados. Renombrarlo cambia también el texto del autor en esos libros."],
         technical=["frmAutor llama AutorService. DatosAutor conserva dos ArrayList: autores y LibroAutor. Si no existen los archivos nuevos, su constructor deriva vínculos históricos en memoria, sin escritura inmediata.",
                    "AutorService.cambiar hace copia de las listas, aplica la mutación y guarda autores.txt, libroAutor.txt y libros.txt mediante TransaccionArchivos.ejecutarAutores; restaura memoria ante RuntimeException."],
         data=["Autor: A* String y nombre; LibroAutor: codLibro y codAutor String. autores.txt: código;nombre. libroAutor.txt: códigoLibro;códigoAutor. libros.txt conserva el autor en su tercera columna.",
               "Los tres archivos se escriben juntos al modificar autores o libros. Un marcador .autores-pendiente y respaldos permiten recuperar un guardado interrumpido."],
         debug=["Breakpoint en frmAutor.ejecutar y después AutorService.registrar/modificar/eliminar. En el cambio de nombre entra en el bucle de AutorService.modificar.",
                "Inspecciona clave normalizada, autor duplicado, relaciones, libros afectados y los tres archivos. Step Into en cambiar y ejecutarAutores; Step Over en la actualización de la tabla."],
         tests=["PruebasBiblioteca.probarAutores verifica vínculos históricos, duplicados, renombrado, baja bloqueada, persistencia y recuperación ante escrituras parciales. La ventana y el menú se prueban manualmente."],
         refs=[ref("forms.frmAutor","ejecutar","Envuelve la acción del botón y muestra errores.","JButtons de frmAutor","AutorService.registrar/modificar/eliminar"),
               ref("service.AutorService","registrar","Normaliza y rechaza duplicados.","frmAutor.ejecutar","DatosAutor.buscarNombre, cambiar"),
               ref("service.AutorService","modificar","Propaga el nombre a libros relacionados.","frmAutor.ejecutar","DatosAutor.reemplazar, DatosLibro.actualizar, cambiar"),
               ref("service.AutorService","eliminar","Bloquea bajas con libros asociados.","frmAutor.ejecutar","DatosAutor.tieneLibros, cambiar"),
               ref("service.AutorService","cambiar","Coordina memoria y tres archivos.","AutorService.registrar/modificar/eliminar","TransaccionArchivos.ejecutarAutores"),
               ref("colecciones.DatosAutor","DatosAutor","Carga o deriva autores históricos.","BibliotecaContexto","repositorio.cargar, validar"),
               ref("repository.TransaccionArchivos","ejecutarAutores","Crea respaldos y marcador.","AutorService.cambiar","recuperarAutores")],
         seq=[("Usuario","frmAutor","clic Modificar"),("frmAutor","AutorService","modificar(código,nombre)"),("AutorService","DatosAutor","buscarNombre/reemplazar"),("AutorService","DatosLibro","actualizar libros vinculados"),("AutorService","Transaccion","ejecutarAutores"),("Transaccion","Repositorio","guardar 3 archivos"),("Repositorio","frmAutor","resultado o error")],
         flow=[("A","Enviar nombre"),("B","Normalizar"),("C","¿Duplicado?"),("D","Cambiar ArrayList"),("E","Guardar tres archivos"),("F","¿Éxito?"),("G","Actualizar vista"),("X","Restaurar memoria/archivos")],
         edges=[("A","B",""),("B","C",""),("C","X","sí"),("C","D","no"),("D","E",""),("E","F",""),("F","G","sí"),("F","X","no")]),
    dict(id="libros", title="Gestión de libros y portadas", layer="service", icon="▣",
         business=["Desde Mantenimiento → Libro se registra, busca, edita o elimina un libro. Se introduce título, autor registrado, ISBN, categoría, año, cantidades y portada opcional.",
                   "El autor debe existir; el servicio conserva la relación autor-libro. La portada seleccionada se copia a imágenes con un nombre que evita sobrescribir otra portada."],
         technical=["frmLibro construye Libro y, si se eligió imagen, llama PortadaService.guardar. LibroService delega alta, edición y baja a AutorService para coordinar el catálogo y el vínculo.",
                    "AutorService resuelve el autor por nombre, actualiza DatosLibro y DatosAutor, y guarda libros.txt, autores.txt y libroAutor.txt juntos. DatosLibro.buscar filtra título o autor."],
         data=["Libro usa codLibro L* y codCategoria C*. libros.txt conserva nueve campos: código;título;autor;ISBN;categoría;año;disponible;total;rutaPortada.",
               "La imagen se copia antes de guardar el libro; si un guardado posterior falla, el código actual no revierte esa imagen copiada. Las rutas relativas se resuelven con Rutas.imagen."],
         debug=["Breakpoint en frmLibro.actionPerformedBtnAgregar. Step Into en guardarImagen y PortadaService.guardar si hay archivo seleccionado; después LibroService.agregar y AutorService.agregarLibro.",
                "Inspecciona cbxAutor, Libro.codLibro antes/después, rutaPortada, DatosAutor.relaciones y los tres archivos. Para búsqueda entra en DatosLibro.buscar."],
         tests=["PruebasBiblioteca.main cubre CRUD de libros y copia de portadas; probarAutores comprueba el vínculo de un libro nuevo. Quedan pendientes la inspección visual de miniaturas y selección de autor en IntelliJ."],
         refs=[ref("forms.frmLibro","actionPerformedBtnAgregar","Recoge el formulario y crea Libro.","JButton Agregar","guardarImagen, LibroService.agregar"),
               ref("forms.frmLibro","guardarImagen","Copia portada si se eligió un archivo.","frmLibro.actionPerformedBtnAgregar/Actualizar","PortadaService.guardar"),
               ref("service.PortadaService","guardar","Evita colisión de nombre de imagen.","frmLibro.guardarImagen","Files.copy"),
               ref("service.LibroService","agregar","Delega el alta coordinada.","frmLibro.actionPerformedBtnAgregar","AutorService.agregarLibro"),
               ref("service.AutorService","agregarLibro","Asocia el libro al autor registrado.","LibroService.agregar","DatosLibro.agregar, DatosAutor.asociar, cambiar"),
               ref("colecciones.DatosLibro","buscar","Filtra título o autor en memoria.","LibroService.buscar","ArrayList"),
               ref("repository.Formatos","escribirLibro","Serializa las nueve columnas.","ArchivoTextoRepository.guardar","getters de Libro")],
         seq=[("Usuario","frmLibro","clic Agregar"),("frmLibro","PortadaService","guardar imagen opcional"),("frmLibro","LibroService","agregar(Libro)"),("LibroService","AutorService","agregarLibro"),("AutorService","DatosLibro","agregar"),("AutorService","DatosAutor","asociar"),("AutorService","Transaccion","guardar tres archivos")],
         flow=[("A","Completar formulario"),("B","Seleccionar autor"),("C","¿Autor registrado?"),("D","Copiar portada opcional"),("E","Agregar Libro y vínculo"),("F","Guardar tres archivos"),("G","Actualizar repisa"),("X","Mostrar error")],
         edges=[("A","B",""),("B","C",""),("C","X","no"),("C","D","sí"),("D","E",""),("E","F",""),("F","G","éxito"),("F","X","fallo")]),
    dict(id="prestamos", title="Selección y registro de préstamos", layer="service", icon="⇄",
         business=["En la ventana principal se selecciona un usuario, uno o varios libros y las fechas. Cada clic en Agregar aumenta una unidad del libro seleccionado; Quitar reduce una unidad.",
                   "Al realizar el préstamo se exige usuario, al menos un libro y existencias suficientes por título. Se crea el préstamo, se registra cada cantidad y se descuenta del inventario. No hay devolución implementada."],
         technical=["frmPrincipal recibe eventos de TarjetasLibro y mantiene la selección mediante PrestamoService → DatosLibroDetalle. Al confirmar, convierte fechas dd/MM/yyyy a LocalDate y llama registrar.",
                    "PrestamoService valida, añade Prestamo a DatosPrestamo, crea DetallePrestamo por cada libro, descuenta stock y guarda préstamos, detalles y libros mediante TransaccionArchivos.ejecutar. Si falla, revierte las mutaciones en memoria."],
         data=["Selección temporal: ArrayList<DetalleLibro> con Libro y cantidad. Persistencia: prestamos.txt código;usuario;fechaPréstamo;fechaDevolución; detallePrestamo.txt códigoDetalle;códigoPréstamo;códigoLibro;cantidad; libros.txt stock disponible.",
               "Los códigos son String; fechas persistidas como ISO LocalDate. Los archivos históricos no se reparan por el registro de un préstamo nuevo."],
         debug=["Empieza en frmPrincipal.agregarLibro para la selección y luego en actionPerformedBtnEjecutarPrestamo para confirmar.",
                "Step Into en PrestamoService.registrar. Inspecciona seleccion.listar(), libro.cantidadDisponible, nuevosDetalles, descontados y codPrestamo. Step Over en cada guardado para identificar qué archivo se escribe."],
         tests=["PruebasBiblioteca.main rechaza falta de stock y registra un préstamo; probarPrestamoMultiple cubre dos libros, cantidades 2 y 3 y fallo durante el segundo libro."],
         refs=[ref("forms.frmPrincipal","agregarLibro","Recibe clic de tarjeta.","TarjetasLibro.catalogo","PrestamoService.seleccionar"),
               ref("util.DatosLibroDetalle","actualizarLista","Agrupa por codLibro y aumenta cantidad.","PrestamoService.seleccionar","DetalleLibro.setCantidad"),
               ref("forms.frmPrincipal","actionPerformedBtnEjecutarPrestamo","Lee usuario/fechas y solicita registro.","JButton Realizar préstamo","PrestamoService.registrar, frmBoleta"),
               ref("service.PrestamoService","registrar","Valida, crea detalles, descuenta y persiste.","frmPrincipal.actionPerformedBtnEjecutarPrestamo","DatosPrestamo.agregar, DatosDetallePrestamo.agregar, TransaccionArchivos.ejecutar"),
               ref("repository.TransaccionArchivos","ejecutar","Respalda tres archivos y revierte fallos.","PrestamoService.registrar","Runnable de guardar")],
         seq=[("Usuario","frmPrincipal","Agregar libro / Realizar préstamo"),("frmPrincipal","PrestamoService","seleccionar/registrar"),("PrestamoService","DatosLibro","obtener y comprobar stock"),("PrestamoService","DatosPrestamo","agregar"),("PrestamoService","DatosDetalle","agregar por libro"),("PrestamoService","DatosLibro","descontar"),("PrestamoService","Transaccion","guardar 3 archivos"),("PrestamoService","frmPrincipal","Prestamo o error")],
         flow=[("A","Leer selección y fechas"),("B","Validar usuario y libros"),("C","¿Stock suficiente?"),("D","Agregar Prestamo"),("E","Crear detalles y descontar"),("F","Guardar 3 archivos"),("G","¿Éxito?"),("H","Mostrar boleta"),("X","Revertir y mostrar error")],
         edges=[("A","B",""),("B","C",""),("C","X","no"),("C","D","sí"),("D","E",""),("E","F",""),("F","G",""),("G","H","sí"),("G","X","no")]),
    dict(id="detalle", title="Detalle de préstamos", layer="clases", icon="≡",
         business=["El detalle registra qué libros y cuántos ejemplares pertenecen a cada préstamo. No se introduce por una pantalla independiente: surge de la selección realizada antes de confirmar.",
                   "Cada registro debe tener cantidad positiva y referirse a un libro y préstamo existentes. P20 se conserva sin detalles históricos; no se crean líneas faltantes automáticamente."],
         technical=["PrestamoService.registrar itera DetalleLibro y llama DatosDetallePrestamo.agregar(codLibro, codPrestamo, cantidad). Esta colección valida referencias, asigna DP* y añade DetallePrestamo al ArrayList.",
                    "PrestamoService.detallesDe filtra por codPrestamo para boletas. DatosDetallePrestamo.guardar escribe la colección completa durante la transacción del préstamo."],
         data=["DetallePrestamo: codDetallePrestamo, codPrestamo, codLibro String; cantPrestamo int. detallePrestamo.txt tiene cuatro columnas separadas por ;.",
               "La validación al agregar no altera líneas históricas cargadas. El archivo se escribe junto con prestamos.txt y libros.txt."],
         debug=["Breakpoint en PrestamoService.registrar dentro del bucle de libros, seguido de DatosDetallePrestamo.agregar y validar.",
                "Inspecciona codPrestamo generado, codLibro, cantPrestamo, tamaño del ArrayList y DP*. Para lectura entra en PrestamoService.detallesDe."],
         tests=["PruebasBiblioteca.probarPrestamoMultiple comprueba dos detalles con cantidades 2 y 3, referencias inexistentes, cantidad cero y reversión parcial. main comprueba que P20 siga vacío."],
         refs=[ref("service.PrestamoService","registrar","Produce un detalle por libro seleccionado.","frmPrincipal.actionPerformedBtnEjecutarPrestamo","DatosDetallePrestamo.agregar"),
               ref("colecciones.DatosDetallePrestamo","agregar","Asigna DP* y añade a memoria.","PrestamoService.registrar","validar, Codigos.siguiente"),
               ref("colecciones.DatosDetallePrestamo","validar","Comprueba cantidad y referencias.","DatosDetallePrestamo.agregar/actualizar","DatosLibro.obtenerPorCodigo, DatosPrestamo.obtener"),
               ref("service.PrestamoService","detallesDe","Recupera detalles de un préstamo.","frmBoleta.generarBoleta","DatosDetallePrestamo.listar"),
               ref("colecciones.DatosDetallePrestamo","guardar","Persiste detalles completos.","PrestamoService.registrar","Repositorio.guardar"),
               ref("repository.Formatos","escribirDetalle","Serializa cuatro campos.","ArchivoTextoRepository.guardar","getters de DetallePrestamo")],
         seq=[("PrestamoService","DatosDetalle","agregar(codLibro,codPrestamo,cantidad)"),("DatosDetalle","DatosLibro","obtenerPorCodigo"),("DatosDetalle","DatosPrestamo","obtener"),("DatosDetalle","Codigos","siguiente DP*"),("DatosDetalle","PrestamoService","DetallePrestamo"),("PrestamoService","Repositorio","guardar detallePrestamo.txt"),("frmBoleta","PrestamoService","detallesDe")],
         flow=[("A","Tomar DetalleLibro"),("B","Validar cantidad > 0"),("C","¿Existen libro/préstamo?"),("D","Generar DP*"),("E","Agregar a ArrayList"),("F","Guardar junto al préstamo"),("X","Rechazar detalle")],
         edges=[("A","B",""),("B","C","sí"),("B","X","no"),("C","X","no"),("C","D","sí"),("D","E",""),("E","F","")]),
    dict(id="boletas", title="Boletas e historial", layer="forms", icon="▧",
         business=["Tras registrar un préstamo aparece una boleta con usuario, fechas y libros. El menú Proceso → Entrega de préstamo abre una lista histórica; al elegir una fila se muestra su boleta.",
                   "La opción se llama Entrega, pero actualmente solo consulta historial: no registra devolución ni repone existencias. P20 muestra una boleta sin líneas de detalle."],
         technical=["frmPrincipal crea frmBoleta tras registrar o frmListaPrestamo desde el menú. La lista obtiene PrestamoService.listar; un clic busca el préstamo por código y abre frmBoleta.",
                    "frmBoleta.generarBoleta consulta detallesDe, nombre de usuario y título actual de cada libro. El texto se compone en un JTextArea; no crea un archivo de boleta."],
         data=["Lectura en memoria: ArrayList<Prestamo>, ArrayList<DetallePrestamo>, usuarios y libros. Los datos ya fueron cargados desde prestamos.txt, detallePrestamo.txt, usuarios.txt y libros.txt al arrancar.",
               "Si se eliminó el libro, obtenerTituloLibro devuelve cadena vacía; el nombre de usuario ausente se muestra como Usuario no encontrado."],
         debug=["Breakpoint en frmPrincipal.actionPerformedMntmEntregaPrestamo para historial o en frmBoleta.generarBoleta para boleta nueva.",
                "Sigue frmListaPrestamo.cargarListado → mouseClickedTbPrestamo → PrestamoService.detallesDe. Inspecciona codPrestamo, filas, detalles y StringBuilder boleta."],
         tests=["PruebasBiblioteca comprueba recuperación de préstamos y detalles, pero no el texto ni la visualización de la boleta. Abrir P20 y otra boleta en IntelliJ queda como prueba manual."],
         refs=[ref("forms.frmPrincipal","actionPerformedMntmEntregaPrestamo","Abre historial.","Menú Entrega de préstamo","frmListaPrestamo"),
               ref("forms.frmListaPrestamo","cargarListado","Llena la tabla de préstamos.","constructor frmListaPrestamo","PrestamoService.listar"),
               ref("forms.frmListaPrestamo","mouseClickedTbPrestamo","Obtiene el préstamo seleccionado.","JTable mouseClicked","PrestamoService.obtener, frmBoleta"),
               ref("forms.frmBoleta","generarBoleta","Compone el texto de boleta.","constructor frmBoleta","PrestamoService.detallesDe, obtenerNombreUsuario, obtenerTituloLibro"),
               ref("service.PrestamoService","detallesDe","Filtra por código.","frmBoleta.generarBoleta","DatosDetallePrestamo.listar"),
               ref("forms.frmBoleta","obtenerTituloLibro","Resuelve título actual.","frmBoleta.generarBoleta","LibroService.obtener")],
         seq=[("Usuario","frmPrincipal","clic Entrega de préstamo"),("frmPrincipal","frmListaPrestamo","new"),("frmListaPrestamo","PrestamoService","listar"),("Usuario","frmListaPrestamo","clic fila"),("frmListaPrestamo","PrestamoService","obtener"),("frmListaPrestamo","frmBoleta","new"),("frmBoleta","PrestamoService","detallesDe"),("frmBoleta","Usuario","texto en pantalla")],
         flow=[("A","Registrar o abrir historial"),("B","Obtener Prestamo"),("C","Obtener detalles"),("D","¿Hay detalles?"),("E","Añadir líneas a boleta"),("F","Mostrar boleta"),("G","Boleta sin líneas")],
         edges=[("A","B",""),("B","C",""),("C","D",""),("D","E","sí"),("D","G","no"),("E","F",""),("G","F","")]),
    dict(id="persistencia", title="Persistencia y recuperación", layer="repository", icon="▦",
         business=["El sistema conserva las colecciones en archivos de texto. Un fallo de escritura debe evitar que un préstamo o una relación autor-libro queden a medias.",
                   "Al iniciar, una transacción pendiente se intenta recuperar antes de mostrar la interfaz. Un registro mal formado detiene la carga en lugar de sobrescribirlo."],
         technical=["ArchivoTextoRepository.cargar lee UTF-8 y aplica Formatos.leer*. guardar serializa todas las filas a un temporal y lo mueve al destino. Rutas.dato resuelve la carpeta de datos.",
                    "TransaccionArchivos.ejecutar coordina préstamos, detalles y libros. ejecutarAutores coordina autores, relaciones y libros, admite archivos nuevos y usa .autores-pendiente. recuperarPendiente revisa ambos mecanismos."],
         data=["Separador real: ;. Formatos exige 9/6/4/4/2/2/2 campos según entidad y parsea números y fechas. No se añade estado de préstamo a prestamos.txt.",
               "La sustitución individual es atómica si el sistema de archivos lo permite; las operaciones de varios archivos emplean respaldos y marcadores. Portadas son archivos de imagen separados, fuera de estas transacciones."],
         debug=["Breakpoint en ArchivoTextoRepository.guardar para una escritura y en TransaccionArchivos.ejecutar o ejecutarAutores para varias.",
                "Inspecciona archivo, lineas, temporal, marcador, respaldos y excepciones suprimidas. Para un arranque tras interrupción sigue recuperarPendiente antes de los constructores Datos*."],
         tests=["PruebasBiblioteca.main y probarAutores simulan fallos de archivo, marcador pendiente, datos corruptos y recarga. La recuperación frente a un apagado físico real no se ha ensayado."],
         refs=[ref("util.Rutas","raiz","Ubica Biblioteca/datos.","Rutas.dato","System.getProperty, Files.isDirectory"),
               ref("repository.ArchivoTextoRepository","cargar","Lee y parsea líneas UTF-8.","Datos*.recuperar","Formatos.leer*"),
               ref("repository.ArchivoTextoRepository","guardar","Escribe temporal y reemplaza destino.","Datos*.guardar","Formatos.escribir*, Files.move"),
               ref("repository.Formatos","campos","Comprueba número de columnas.","Formatos.leer*","String.split"),
               ref("repository.TransaccionArchivos","ejecutar","Coordina tres archivos de préstamo.","PrestamoService.registrar","respaldos, marcador"),
               ref("repository.TransaccionArchivos","ejecutarAutores","Coordina autores y libros.","AutorService.cambiar","respaldos, marcador"),
               ref("repository.TransaccionArchivos","recuperarPendiente","Recupera antes de cargar.","BibliotecaContexto","recuperarAutores, Files.copy")],
         seq=[("Service","Datos","guardar"),("Datos","Transaccion","ejecutar/ejecutarAutores"),("Transaccion","ArchivoTextoRepository","guardar"),("ArchivoTextoRepository","Formatos","escribir"),("ArchivoTextoRepository","Archivo","temporal + move"),("Archivo","Transaccion","éxito/error"),("Transaccion","Service","retorno o rollback")],
         flow=[("A","Solicitar guardado"),("B","Crear respaldos/marcador"),("C","Serializar a temporales"),("D","Reemplazar archivos"),("E","¿Falló?"),("F","Limpiar respaldos"),("X","Restaurar respaldos")],
         edges=[("A","B",""),("B","C",""),("C","D",""),("D","E",""),("E","F","no"),("E","X","sí")]),
]


def source_for(cls: str) -> Path:
    return SRC / Path(*cls.split(".")).with_suffix(".java")


def resolve_method(item: dict) -> None:
    path = source_for(item["cls"])
    if not path.is_file():
        raise ValueError(f"Archivo ausente: {path}")
    lines = path.read_text(encoding="utf-8").splitlines()
    name = item["method"]
    found = [(n, line) for n, line in enumerate(lines, 1)
             if re.search(r"\b" + re.escape(name) + r"\s*\(", line)
             and re.search(r"\b(public|private|protected)\b", line)
             and not line.lstrip().startswith("//")]
    if len(found) != 1:
        raise ValueError(f"Método ambiguo o ausente: {item['cls']}.{name}: {found}")
    number = found[0][0]
    item["line"] = number
    item["path"] = path.relative_to(ROOT).as_posix()
    signature_lines = []
    for line in lines[number - 1:]:
        signature_lines.append(line.strip())
        if "{" in line: break
    item["signature"] = " ".join(signature_lines)
    # Fragmento continuo del método; se detiene en su cierre o en doce líneas.
    fragment = []
    depth = 0
    started = False
    for line in lines[number - 1:]:
        fragment.append(line)
        without_literals = re.sub(r'"(?:\\.|[^"\\])*"', '""', line.split("//", 1)[0])
        depth += without_literals.count("{") - without_literals.count("}")
        started |= "{" in without_literals
        if (started and depth <= 0) or len(fragment) >= 12: break
    item["snippet"] = "\n".join(fragment)


def mermaid_sequence(flow: dict) -> str:
    parts = ["sequenceDiagram"]
    for a, b, label in flow["seq"]:
        parts.append(f"    {a}->>{b}: {label}")
    return "\n".join(parts)


def mermaid_flow(flow: dict) -> str:
    parts = ["flowchart TD"]
    labels = dict(flow["flow"])
    for a, b, label in flow["edges"]:
        left = f'{a}["{labels[a]}"]'
        right = f'{b}["{labels[b]}"]'
        parts.append(f"    {left} -->|{label}| {right}" if label else f"    {left} --> {right}")
    return "\n".join(parts)


def markdown(flow: dict) -> str:
    sections = [f"# {flow['title']}", "", "## A. Perspectiva de negocio", ""]
    for s in flow["business"]: sections += [s, ""]
    sections += ["## B. Perspectiva técnica", ""]
    for s in flow["technical"]: sections += [s, ""]
    sections += ["## C. Diagrama de secuencia", "", "```mermaid", mermaid_sequence(flow), "```", "",
                 "## D. Diagrama de flujo", "", "```mermaid", mermaid_flow(flow), "```", "",
                 "## E. Código Java explicado", ""]
    for m in flow["refs"]:
        link = "../../" + m["path"] + "#L" + str(m["line"])
        sections += [f"### `{m['cls']}.{m['method']}`", "",
                     f"**Firma:** `{m['signature']}`  ",
                     f"**Fuente:** [{m['path']}:{m['line']}]({link})  ",
                     f"**Por qué participa:** {m['why']}  ",
                     f"**Invocado por:** {m['caller']}  ",
                     f"**Invoca después:** {m['next']}", "", "```java", m["snippet"], "```", ""]
    sections += ["## F. Datos y persistencia", ""]
    for s in flow["data"]: sections += [s, ""]
    sections += ["## G. Depuración en IntelliJ", ""]
    for s in flow["debug"]: sections += [s, ""]
    sections += ["## H. Pruebas", ""]
    for s in flow["tests"]: sections += [s, ""]
    sections += ["[Volver al explorador](../explorador/index.html)", ""]
    return "\n".join(sections)


def main() -> None:
    for flow in FLOWS:
        for m in flow["refs"]: resolve_method(m)
        flow["sequenceMermaid"] = mermaid_sequence(flow)
        flow["flowMermaid"] = mermaid_flow(flow)
        (MD / f"{flow['id']}.md").write_text(markdown(flow), encoding="utf-8")
    inventory = {}
    dependencies = {}
    for path in sorted(SRC.rglob("*.java")):
        package = path.parent.name
        inventory.setdefault(package, []).append(path.stem)
        source = path.read_text(encoding="utf-8")
        internal = sorted(set(re.findall(r"^import\s+((?:app|forms|service|clases|colecciones|repository|util)\.[\w.]+);", source, re.M)))
        dependencies[path.relative_to(ROOT).as_posix()] = internal
    tests = [p.relative_to(ROOT).as_posix() for p in (ROOT / "Biblioteca" / "test").rglob("*.java")]
    payload = {"flows": FLOWS, "inventory": inventory, "tests": tests,
               "dependencies": dependencies,
               "classCount": sum(map(len, inventory.values())),
               "methodCount": len({(m['cls'], m['method']) for f in FLOWS for m in f['refs']})}
    architecture = MD / "arquitectura.md"
    with architecture.open("a", encoding="utf-8") as file:
        file.write("\n## Inventario de dependencias internas\n\n")
        file.write("Mapa de `import` explícitos entre tipos del proyecto; las clases del mismo paquete pueden llamarse sin import.\n\n")
        for path, imports in dependencies.items():
            file.write(f"- `{path}` → {', '.join('`' + name + '`' for name in imports) if imports else 'sin imports internos explícitos'}\n")
    template = (OUT / "template.html").read_text(encoding="utf-8")
    document = template.replace("__EXPLORER_DATA__", json.dumps(payload, ensure_ascii=False).replace("</", "<\\/"))
    (OUT / "index.html").write_text(document, encoding="utf-8")
    print(f"{len(FLOWS)} flujos, {payload['classCount']} tipos Java productivos, {payload['methodCount']} métodos explicados")


if __name__ == "__main__": main()
