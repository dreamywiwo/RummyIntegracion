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
public class TerminoTurnoEvent extends EventBase {

    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "termino.turno";

    public TerminoTurnoEvent() {
        super(TOPIC, EVENT_TYPE);
    }

}
