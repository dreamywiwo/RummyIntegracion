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
public class EstadoSolicitadoEvent extends EventBase {
    
    private String jugadorId;
    
    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "estado.solicitado";

    public EstadoSolicitadoEvent() {
        super();
    }

    public EstadoSolicitadoEvent(String jugadorId) {
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
