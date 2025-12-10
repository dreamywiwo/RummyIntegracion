/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.acciones;

import itson.rummyeventos.base.EventBase;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class PerfilActualizadoEvent extends EventBase {
    
    private String jugadorId;
    private String nombre;
    private String avatarPath;
    private List<String> coloresFichas;
    
    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "perfil.actualizado";
    
    public PerfilActualizadoEvent() {
        super();
    }

    public PerfilActualizadoEvent(String jugadorId, String nombre, String avatarPath, List<String> coloresFichas) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
        this.nombre = nombre;
        this.avatarPath = avatarPath;
        this.coloresFichas = coloresFichas;
    }   

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public List<String> getColoresFichas() {
        return coloresFichas;
    }

    public void setColoresFichas(List<String> coloresFichas) {
        this.coloresFichas = coloresFichas;
    }
    
}
