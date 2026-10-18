# Explorador de AppBiblioteca

Abre [index.html](index.html) con doble clic o desde Chrome/Edge. Funciona con `file://`, sin servidor ni conexión a Internet. La barra lateral cambia de flujo; las pestañas separan negocio, técnica, código, datos, depuración y pruebas. El buscador encuentra flujos, clases y métodos. Los controles `+`, `−` y `100 %` cambian el zoom de los diagramas.

Cada flujo también tiene un Markdown en `../flujos/`. Los diagramas están escritos en sintaxis Mermaid en esos archivos y en la vista **Mermaid** del HTML. Para mantener el HTML autocontenido, su visualización interactiva usa un SVG generado localmente desde los mismos participantes, pasos y decisiones; no descarga Mermaid ni otra biblioteca. La validación automatizada comprueba esos SVG y la estructura de los diagramas, pero no ejecuta el motor oficial de Mermaid.

Los enlaces a `.java` son rutas relativas y muestran `archivo:línea`. Un navegador que abre un archivo Java como texto puede ignorar el fragmento `#L...`; en IntelliJ usa **Navigate → File** y **Ctrl+G** para ir a la línea indicada.

## Regenerar y validar

Desde la raíz del repositorio:

```powershell
python docs/explorador/generar.py
python docs/explorador/validar-documentacion.py
node docs/explorador/validar-navegador.mjs
```

`generar.py` toma las firmas, números de línea y fragmentos directamente de `Biblioteca/src`. Los dos validadores son opcionales para leer la documentación. El primero usa Python estándar; el segundo usa Node 24 y Chrome local en modo headless para abrir `file://`, cambiar de flujo y pestañas, buscar, acercar diagramas y detectar excepciones JavaScript. No se requieren paquetes de terceros.

Las pruebas Java están en `Biblioteca/test/PruebasBiblioteca.java`; su ejecución escribe en copias temporales de los datos. La exploración visual de la aplicación Swing en IntelliJ sigue siendo manual.
