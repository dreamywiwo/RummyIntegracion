/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.acciones;

import itson.rummydtos.FichaDTO;
import itson.rummyeventos.base.EventBase;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class FichaTomadaEvent extends EventBase {

    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "ficha.tomada";

    public FichaTomadaEvent() {
        super(TOPIC, EVENT_TYPE);
    }

}
