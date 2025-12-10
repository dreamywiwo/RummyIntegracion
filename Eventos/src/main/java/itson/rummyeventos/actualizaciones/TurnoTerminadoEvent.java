/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.actualizaciones;

import itson.rummyeventos.base.EventBase;

/**
 *
 * @author Dana Chavez
 */
public class TurnoTerminadoEvent extends EventBase {

    private String nuevoTurnoJugador;

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "turno.terminado";

    public TurnoTerminadoEvent() {
        super();
    }

    public TurnoTerminadoEvent(String nuevoTurnoJugador) {
        super(TOPIC, EVENT_TYPE);
        this.nuevoTurnoJugador = nuevoTurnoJugador;
    }

    public String getNuevoTurnoJugador() {
        return nuevoTurnoJugador;
    }

    public void setNuevoTurnoJugador(String nuevoTurnoJugador) {
        this.nuevoTurnoJugador = nuevoTurnoJugador;
    }

}
