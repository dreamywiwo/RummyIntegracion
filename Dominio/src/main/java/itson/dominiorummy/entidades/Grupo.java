package itson.dominiorummy.entidades;

import java.util.ArrayList;
import java.util.List;

public abstract class Grupo {

    protected final String id;
    protected final List<FichaPlaced> fichas;

    public Grupo(String id, List<FichaPlaced> fichasIniciales) {
        this.id = (id != null) ? id : "unknown_" + System.currentTimeMillis();
        this.fichas = new ArrayList<>();
        if (fichasIniciales != null) {
            this.fichas.addAll(fichasIniciales);
        }
    }

    public String getId() {
        return id;
    }

    public List<FichaPlaced> getFichas() {
        return fichas;
    }

    public void limpiar() {
        fichas.clear();
    }

    public void agregarFicha(FichaPlaced fp) {
        if (fp != null) {
            fichas.add(fp);
        }
    }

    public boolean estaVacio() {
        return fichas.isEmpty();
    }
    
    // Regla de Fin de Turno: Mínimo 3 fichas.
    public boolean cumpleTamanoMinimo() {
        return this.fichas.size() >= 3;
    }
    
    // Regla Estructural
    public abstract boolean validarReglas();
    
    public abstract int calcularPuntos();
    
    public abstract Grupo clonar();
    
    /**
     * Verifica si TODAS las fichas de este grupo fueron colocadas en el turno indicado.
     * Sirve para identificar grupos bajados en el turno actual.
     */
    public boolean fueCreadoEnTurno(int turnoActual, String jugadorId) {
        if (fichas.isEmpty()) return false;
        
        for (FichaPlaced fp : fichas) {
            // Si alguna ficha NO es de este turno o NO es de este jugador...
            if (fp.getPlacedInTurn() != turnoActual || !fp.getPlacedBy().equals(jugadorId)) {
                return false; // El grupo ya existía o es una mezcla (manipulación)
            }
        }
        return true;
    }
    
    /**
     * Verifica si AL MENOS UNA ficha es de un turno anterior.
     * Para validar que no se toquen grupos ajenos antes de bajar 30 pts.
     */
    public boolean contieneFichasAntiguas(int turnoActual) {
        for (FichaPlaced fp : fichas) {
            if (fp.getPlacedInTurn() < turnoActual) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return id + " (" + this.getClass().getSimpleName() + "): " + fichas.size() + " fichas";
    }
}