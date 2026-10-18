# AppBiblioteca

Aplicación de escritorio Java 21 y Swing. Usa `ArrayList` en memoria y conserva los archivos de `Biblioteca/datos` como almacenamiento persistente.

## Abrir en IntelliJ IDEA

1. Abre la carpeta raíz `AppBiblioteca` como proyecto.
2. En **File → Project Structure → Project**, selecciona un JDK 21. El módulo `AppBiblioteca` debe mostrar `Biblioteca/src` como carpeta **Sources** y `Biblioteca/test` como **Test Sources**.
3. Selecciona la configuración **Biblioteca** y ejecútala. Su clase principal es `app.Main`; el directorio de trabajo es `Biblioteca`.
4. Si IntelliJ tenía abierto el proyecto antes de actualizar los metadatos, ciérralo y vuelve a abrir la carpeta raíz para recargar el módulo.

La aplicación localiza `Biblioteca/datos` desde la carpeta del proyecto o sus antecesoras. También acepta `-Dbiblioteca.home=<ruta absoluta a Biblioteca>` como opción de la VM. Los formatos de `usuarios.txt`, `categorias.txt`, `libros.txt`, `prestamos.txt` y `detallePrestamo.txt` siguen siendo los originales.

## Autores

**Mantenimiento → Autor** permite registrar, buscar, modificar y eliminar autores. Cada libro selecciona un autor principal registrado; un autor puede tener varios libros. No se puede eliminar un autor vinculado. Los nombres equivalentes por espacios y mayúsculas se consideran duplicados.

`autores.txt` guarda `codigo;nombre` y `libroAutor.txt` guarda `codigoLibro;codigoAutor`. El nombre del autor permanece en la tercera columna de `libros.txt` para conservar su formato. Al abrir un catálogo histórico sin los dos archivos nuevos, la aplicación calcula los vínculos en memoria a partir de los nombres existentes y no escribe nada. La primera operación de autores o libros guarda ambos archivos nuevos de manera coordinada con `libros.txt`. Si falta solo uno de los archivos nuevos, el arranque se detiene para evitar una asociación incierta.

## Compilar y probar sin dependencias externas

En PowerShell, desde la raíz del proyecto:

```powershell
$fuentes = @(Get-ChildItem Biblioteca/src,Biblioteca/test -Recurse -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 -d out $fuentes
java -cp out PruebasBiblioteca (Join-Path (Get-Location).Path 'Biblioteca')
```

Las pruebas crean copias temporales de los cinco archivos de datos y escriben únicamente en esas copias. Para iniciar desde la terminal después de compilar:

```powershell
java -cp out app.Main
```

## Estructura

- `app`: entrada y composición de las dependencias.
- `forms`: ventanas y tarjetas Swing.
- `service`: operaciones de usuarios, libros, categorías, autores, préstamos y portadas.
- `clases`: entidades.
- `colecciones`: estado en memoria mediante `ArrayList`.
- `repository`: contrato de persistencia, formato de líneas y almacenamiento `.txt`.
- `util`: códigos, rutas y selección de libros.

## Funcionalidades pendientes

La opción **Entrega de préstamo** actualmente muestra la lista y la boleta. No registra devoluciones ni repone existencias. Los préstamos anteriores no tienen estado persistido, por lo que no se recalcula su inventario automáticamente. `P20` se conserva sin detalles. No se agregaron restricciones nuevas según el estado del usuario.
