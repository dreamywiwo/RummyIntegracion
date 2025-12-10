package itson.rummyeventos.actualizaciones;

import itson.rummydtos.JugadorDTO;
import itson.rummyeventos.base.EventBase;

public class JuegoTerminadoEvent extends EventBase {
    
    private JugadorDTO jugadorGanador;
    
    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "juego.terminado";

    public JuegoTerminadoEvent() {
        super();
    }

    public JuegoTerminadoEvent(JugadorDTO jugadorGanador) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorGanador = jugadorGanador;
    }

    public JugadorDTO getJugadorGanador() { // Corregido nombre del getter
        return jugadorGanador;
    }

    public void setJugadorGanador(JugadorDTO jugadorGanador) {
        this.jugadorGanador = jugadorGanador;
    }
}