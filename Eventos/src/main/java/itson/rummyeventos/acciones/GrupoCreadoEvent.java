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
public class GrupoCreadoEvent extends EventBase {

    private List<FichaDTO> fichas;

    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "grupo.creado";

    public GrupoCreadoEvent() {
        super();
    }

    public GrupoCreadoEvent(List<FichaDTO> fichas) {
        super(TOPIC, EVENT_TYPE);
        this.fichas = fichas;
    }

    public List<FichaDTO> getFichas() {
        return fichas;
    }

}
