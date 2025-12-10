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
public class SopaActualizadaEvent extends EventBase {

    private int numFichasRestantes;

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "sopa.actualizada";

    public SopaActualizadaEvent() {
        super();
    }

    public SopaActualizadaEvent(int numFichasRestantes) {
        super(TOPIC, EVENT_TYPE);
        this.numFichasRestantes = numFichasRestantes;
    }

    public int getNumFichasRestantes() {
        return numFichasRestantes;
    }

    public void setNumFichasRestantes(int numFichasRestantes) {
        this.numFichasRestantes = numFichasRestantes;
    }

}
