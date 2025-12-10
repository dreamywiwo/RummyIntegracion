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
public class CantidadFichasPublicoEvent extends EventBase {
    private String jugadorId; 
    private int size;

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "fichas.jugador.cantidad";

    public CantidadFichasPublicoEvent() {
        super();
    }

    public CantidadFichasPublicoEvent(String jugadorId, int size) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
        this.size = size;
    }   

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
}
