/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.actualizaciones;

import itson.rummydtos.FichaDTO;
import itson.rummyeventos.base.EventBase;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class ManoActualizadaEvent extends EventBase {
    private String jugadorId; 
    private List<FichaDTO> manoSnapshot;

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "mano.actualizada";

    public ManoActualizadaEvent() {
        super();
    }

    public ManoActualizadaEvent(String jugadorId, List<FichaDTO> manoSnapshot) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
        this.manoSnapshot = manoSnapshot;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public List<FichaDTO> getManoSnapshot() {
        return manoSnapshot;
    }

    public void setManoSnapshot(List<FichaDTO> manoSnapshot) {
        this.manoSnapshot = manoSnapshot;
    }

}
