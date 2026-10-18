package util;

import java.util.Collection;

/** Calcula el próximo código a partir de los códigos almacenados, sin estado global. */
public final class Codigos {
    private Codigos() {}

    public static String siguiente(String prefijo, int inicio, int paso, Collection<String> existentes) {
        int mayor = inicio;
        for (String codigo : existentes) {
            if (codigo == null || !codigo.startsWith(prefijo)) continue;
            try {
                mayor = Math.max(mayor, Integer.parseInt(codigo.substring(prefijo.length())));
            } catch (NumberFormatException ignored) {
                // Los códigos históricos con otro formato se conservan sin interpretarlos.
            }
        }
        int candidato = mayor + paso;
        String codigo;
        do {
            codigo = prefijo + candidato;
            candidato += paso;
        } while (existentes.contains(codigo));
        return codigo;
    }
}
