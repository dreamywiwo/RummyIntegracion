/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.actualizaciones;

import itson.rummydtos.JugadorDTO;
import itson.rummyeventos.base.EventBase;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class SalaActualizadaEvent extends EventBase {

    private List<JugadorDTO> jugadores;
    
    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "sala.actualizada";

    public SalaActualizadaEvent() {
        super(TOPIC, EVENT_TYPE);
    }

    public SalaActualizadaEvent(List<JugadorDTO> jugadores) {
        super(TOPIC, EVENT_TYPE);
        this.jugadores = jugadores;
    }

    public List<JugadorDTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<JugadorDTO> jugadores) {
        this.jugadores = jugadores;
    }
}
