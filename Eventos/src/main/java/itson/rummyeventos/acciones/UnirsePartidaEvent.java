/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.acciones;

import itson.rummyeventos.base.EventBase;

/**
 *
 * @author Dana Chavez
 */
public class UnirsePartidaEvent extends EventBase {
    
    private String jugadorId;
    
    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "solicitud.unirse";

    public UnirsePartidaEvent() {
        super();
    }

    public UnirsePartidaEvent(String jugadorId) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
    }   

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }
}
