package itson.dominiorummy.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tablero {

    private final Map<String, Grupo> grupos = new HashMap<>();
    private int contadorGrupos = 0;

    private final Map<String, String> removedOrigin = new HashMap<>();

    public Collection<Grupo> getGrupos() {
        return grupos.values();
    }

    public Grupo getGrupo(String id) {
        return grupos.get(id);
    }

    public String generarIdGrupo() {
        return "grupo_" + (contadorGrupos++);
    }

    public void agregarGrupo(Grupo grupo) {
        grupos.put(grupo.getId(), grupo);
    }

    public void removerGrupo(String grupoId) {
        grupos.remove(grupoId);
    }

    public boolean grupoEstaVacio(String grupoId) {
        Grupo g = grupos.get(grupoId);
        return g != null && g.estaVacio();
    }

    public void limpiarGrupo(String grupoId) {
        Grupo g = grupos.get(grupoId);
        if (g != null) {
            g.limpiar();
        }
    }

    public void agregarFichaAGrupo(String grupoId, FichaPlaced fp) {
        Grupo g = grupos.get(grupoId);
        if (g != null) {
            g.agregarFicha(fp);
        }
    }

    public List<FichaPlaced> obtenerFichasDeGrupo(String grupoId) {
        Grupo g = grupos.get(grupoId);
        if (g == null) {
            return Collections.emptyList();
        }

        return g.getFichas().stream()
                .map(FichaPlaced::clonar)
                .collect(Collectors.toList());
    }

    public FichaPlaced buscarFichaPlacedEnOtrosGrupos(String grupoOrigen, String fichaId) {

        for (Grupo g : grupos.values()) {
            if (g.getId().equals(grupoOrigen)) {
                continue;
            }

            Iterator<FichaPlaced> it = g.getFichas().iterator();
            while (it.hasNext()) {
                FichaPlaced fp = it.next();
                if (fp.getFicha().getId().equals(fichaId)) {
                    it.remove();
                    return fp;
                }
            }
        }

        return null;
    }

    public boolean validarReglasDeGrupo(String grupoId) {
        Grupo g = grupos.get(grupoId);
        if (g == null) {
            return false;
        }
        return g.validarReglas();
    }

    public Grupo crearGrupoDesdeFichasPlaced(List<FichaPlaced> fichas) {
        if (fichas == null || fichas.isEmpty()) {
            return null;
        }

        String nuevoId = generarIdGrupo();

        if (fichas.size() < 3) {
            if (fichas.size() == 2) {
                Ficha f1 = fichas.get(0).getFicha();
                Ficha f2 = fichas.get(1).getFicha();
                if (!f1.isEsComodin() && !f2.isEsComodin() && f1.getNumero() == f2.getNumero()) {
                    return new GrupoNumero(nuevoId, fichas);
                }
            }
            return new GrupoSecuencia(nuevoId, fichas);
        }

        GrupoNumero pruebaNumero = new GrupoNumero("temp", fichas);
        GrupoSecuencia pruebaSecuencia = new GrupoSecuencia("temp", fichas);

        boolean esNumero = pruebaNumero.validarReglas();
        boolean esSecuencia = pruebaSecuencia.validarReglas();

        if (esNumero) {
            return new GrupoNumero(nuevoId, fichas);
        }

        if (esSecuencia) {
            return new GrupoSecuencia(nuevoId, fichas);
        }

        return new GrupoSecuencia(nuevoId, fichas);
    }

    public Tablero clonar() {
        Tablero copia = new Tablero();

        copia.contadorGrupos = this.contadorGrupos;

        for (Grupo g : this.grupos.values()) {
            copia.agregarGrupo(g.clonar());
        }

        return copia;
    }

    public void restaurarEstado(Tablero backup) {
        this.grupos.clear();
        this.removedOrigin.clear();

        this.contadorGrupos = backup.contadorGrupos;

        for (Grupo g : backup.getGrupos()) {
            this.agregarGrupo(g.clonar());
        }
    }

    /**
     * Busca la FichaPlaced en TODO el tablero (no realiza ninguna
     * modificación). Retorna la instancia encontrada o null si no existe.
     */
    public FichaPlaced buscarFichaPlacedGlobal(String fichaId) {
        for (Grupo g : grupos.values()) {
            for (FichaPlaced fp : g.getFichas()) {
                if (fp.getFicha().getId().equals(fichaId)) {
                    return fp;
                }
            }
        }
        return null;
    }

    /**
     * Remueve una ficha (por id) de su grupo actual en el tablero. Registra el
     * grupo de origen para permitir restaurarla si hace falta. Si la ficha no
     * existe, no hace nada.
     */
    public void removerFichaGlobal(String fichaId) {
        var iterator = grupos.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            Grupo g = entry.getValue();
            var fichasIt = g.getFichas().iterator();
            while (fichasIt.hasNext()) {
                FichaPlaced fp = fichasIt.next();
                if (fp.getFicha().getId().equals(fichaId)) {
                    removedOrigin.put(fichaId, g.getId());
                    fichasIt.remove();
                    if (g.estaVacio()) {
                        iterator.remove();
                    }
                    return;
                }
            }
        }
    }

    /**
     * Restaura una ficha al grupo en el que estaba originalmente (si está
     * registrado). Si el grupo original ya no existe, se crea un nuevo grupo
     * con id generado y se agrega la ficha ahí para que no se pierda.
     */
    public void restaurarFicha(FichaPlaced fp) {
        if (fp == null) {
            return;
        }

        String fichaId = fp.getFicha().getId();
        String origenId = removedOrigin.remove(fichaId);

        if (origenId != null) {
            Grupo g = grupos.get(origenId);
            if (g != null) {
                g.agregarFicha(fp);
            } else {
                List<FichaPlaced> lista = new ArrayList<>();
                lista.add(fp);
                Grupo grupoRevivido = new GrupoSecuencia(origenId, lista);
                agregarGrupo(grupoRevivido);
            }
            return;
        }
        List<FichaPlaced> lista = new ArrayList<>();
        lista.add(fp);
        String nuevoId = generarIdGrupo();
        Grupo nuevo = new GrupoSecuencia(nuevoId, lista);
        agregarGrupo(nuevo);
    }
    
    /**
     * Busca una ficha en un grupo específico, la quita y devuelve la Ficha original.
     * Si el grupo queda vacío, devuelve true en 'grupoQuedoVacio'.
     */
    public Ficha quitarFichaDeGrupo(String grupoId, String fichaId) {
        Grupo grupo = grupos.get(grupoId);
        if (grupo == null) return null;

        java.util.Iterator<FichaPlaced> it = grupo.getFichas().iterator();
        while (it.hasNext()) {
            FichaPlaced fp = it.next();
            if (fp.getFicha().getId().equals(fichaId)) {
                it.remove();
                return fp.getFicha();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== TABLERO ===\n");

        if (grupos.isEmpty()) {
            sb.append("(sin grupos)\n");
        } else {
            for (Grupo g : grupos.values()) {
                sb.append(" - ").append(g.toString()).append("\n");
            }
        }

        sb.append("================\n");
        return sb.toString();
    }

    public List<Grupo> getGruposEnMesa() {
        return new ArrayList<>(grupos.values());
    }

    public void marcarFichasConfirmadas(String jugadorId) {
        this.removedOrigin.clear();
    }

    public void revivirGrupo(String grupoId, List<FichaPlaced> fichasParaRestaurar) {
        grupos.remove(grupoId);
        Grupo grupoRestaurado = new GrupoSecuencia(grupoId, fichasParaRestaurar);
        grupos.put(grupoId, grupoRestaurado);
    }

}
