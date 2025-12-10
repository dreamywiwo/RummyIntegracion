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
public class GrupoActualizadoEvent extends EventBase {
    
    private String grupoId;
    private List<FichaDTO> nuevasFichas;
    
    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "grupo.actualizado";

    public GrupoActualizadoEvent() {
        super();
    }

    public GrupoActualizadoEvent(String grupoId, List<FichaDTO> nuevasFichas) {
        super(TOPIC, EVENT_TYPE);
        this.grupoId = grupoId;
        this.nuevasFichas = nuevasFichas;
    }

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public List<FichaDTO> getNuevasFichas() {
        return nuevasFichas;
    }

    public void setNuevasFichas(List<FichaDTO> nuevasFichas) {
        this.nuevasFichas = nuevasFichas;
    }

}
