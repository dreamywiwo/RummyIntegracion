/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.sistema;

import itson.rummyeventos.base.EventBase;

/**
 *
 * @author Dana Chavez
 */
public class RegistroJugadorEvent extends EventBase {

    public static final String TOPIC = "sistema.registro";
    public static final String EVENT_TYPE = "jugador.registro";

    private String jugadorId;
    private String ip;
    private int puerto;

    public RegistroJugadorEvent() {
        super();
    }

    public RegistroJugadorEvent(String jugadorId, String ip, int puerto) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }
}
