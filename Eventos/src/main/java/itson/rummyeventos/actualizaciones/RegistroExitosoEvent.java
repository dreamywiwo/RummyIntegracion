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
public class RegistroExitosoEvent extends EventBase {
    
    private String jugadorId;
    
    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "registro.exitoso";

    public RegistroExitosoEvent() {
        super();
    }

    public RegistroExitosoEvent(String jugadorId) {
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
