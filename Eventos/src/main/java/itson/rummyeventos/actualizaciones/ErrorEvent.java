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
public class ErrorEvent extends EventBase {
    private String jugadorId;
    private String mensajeError;

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "mensaje.error";

    public ErrorEvent() {
        super();
    }

    public ErrorEvent(String jugadorId, String mensajeError) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
        this.mensajeError = mensajeError;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

}
