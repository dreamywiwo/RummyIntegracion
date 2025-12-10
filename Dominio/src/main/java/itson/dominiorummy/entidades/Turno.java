package itson.dominiorummy.entidades;

import java.util.List;

public class Turno {

    private List<Jugador> ordenJugadores;
    private int indiceJugadorActual;   
    private int numeroTurno;           

    public Turno(List<Jugador> ordenJugadores, int indiceInicial) {
        setOrdenJugadores(ordenJugadores);
        setIndiceJugadorActual(indiceInicial);
        this.numeroTurno = 1;
    }

    public Jugador getJugadorActual() {
        return ordenJugadores.get(indiceJugadorActual);
    }

    public String getJugadorActualId() {
        return getJugadorActual().getId();
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public int getIndiceJugadorActual() {
        return indiceJugadorActual;
    }

    public List<Jugador> getOrdenJugadores() {
        return ordenJugadores;
    }

    /**
     * Indica si el id pasado corresponde al jugador actual.
     */
    public boolean esTurnoDelJugador(String jugadorId) {
        return getJugadorActual().getId().equals(jugadorId);
    }

    /**
     * Avanza al siguiente jugador (round-robin) y aumenta el contador.
     * Devuelve el nuevo jugador en turno.
     */
    public Jugador avanzarTurno() {
        indiceJugadorActual = (indiceJugadorActual + 1) % ordenJugadores.size();
        numeroTurno++;
        return getJugadorActual();
    }

    /**
     * Devuelve el siguiente jugador sin avanzar turno aún.
     */
    public Jugador verSiguienteJugador() {
        int siguiente = (indiceJugadorActual + 1) % ordenJugadores.size();
        return ordenJugadores.get(siguiente);
    }

    /**
     * Permite forzar qué jugador está en turno (para pruebas o reset).
     */
    public void setIndiceJugadorActual(int indice) {
        if (indice < 0 || indice >= ordenJugadores.size())
            indice = 0;
        this.indiceJugadorActual = indice;
    }

    /**
     * Reemplaza el orden de jugadores y reinicia el contador de turnos.
     */
    public void setOrdenJugadores(List<Jugador> jugadores) {
        this.ordenJugadores = jugadores;
        this.indiceJugadorActual = 0;
        this.numeroTurno = 1;
    }

    /**
     * Reinicia el número de turno a 1
     */
    public void reiniciarContadorTurno() {
        this.numeroTurno = 1;
    }
}
