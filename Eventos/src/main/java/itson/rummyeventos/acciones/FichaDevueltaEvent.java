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
public class FichaDevueltaEvent extends EventBase {
    
    private String grupoId;
    private String fichaId;
    
    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "ficha.devuelta";

    public FichaDevueltaEvent() {
        super();
    }
    
    public FichaDevueltaEvent (String grupoId, String fichaId) {
        super(TOPIC, EVENT_TYPE);
        this.grupoId = grupoId;
        this.fichaId = fichaId;
    }

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public String getFichaId() {
        return fichaId;
    }

    public void setFichaId(String fichaId) {
        this.fichaId = fichaId;
    }

}
