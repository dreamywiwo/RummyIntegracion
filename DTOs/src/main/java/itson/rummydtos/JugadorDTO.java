/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummydtos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class JugadorDTO {

    private String id;
    private String nombre;          
    private String avatarPath;     
    private boolean esTurno;       
    private int cantidadFichas;     
    private List<FichaDTO> fichasMano;

    public JugadorDTO() {
    }

    public JugadorDTO(String id, String nombre, String avatarPath) {
        this.id = id;
        this.nombre = nombre;
        this.avatarPath = avatarPath;
        this.fichasMano = new ArrayList<>();
        this.esTurno = false;
        this.cantidadFichas = 0;
    }

    public JugadorDTO(String id, String nombre, String avatarPath, List<FichaDTO> fichasMano) {
        this.id = id;
        this.nombre = nombre;
        this.avatarPath = avatarPath;
        this.fichasMano = fichasMano;
        this.cantidadFichas = (fichasMano != null) ? fichasMano.size() : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isEsTurno() {
        return esTurno;
    }

    public void setEsTurno(boolean esTurno) {
        this.esTurno = esTurno;
    }

    public int getCantidadFichas() {
        // Si tenemos la lista real, devolvemos su tamaño. Si no (oponente), devolvemos el int.
        if (fichasMano != null && !fichasMano.isEmpty()) {
            return fichasMano.size();
        }
        return cantidadFichas;
    }

    public void setCantidadFichas(int cantidadFichas) {
        this.cantidadFichas = cantidadFichas;
    }

    public List<FichaDTO> getFichasMano() {
        return fichasMano;
    }

    public void setFichasMano(List<FichaDTO> fichasMano) {
        this.fichasMano = fichasMano;
        if (fichasMano != null) {
            this.cantidadFichas = fichasMano.size();
        }
    }
}
