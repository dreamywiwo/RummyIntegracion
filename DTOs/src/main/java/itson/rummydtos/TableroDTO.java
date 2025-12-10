/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummydtos;

import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class TableroDTO {

    private List<GrupoDTO> grupos;

    public TableroDTO() {
    }

    public TableroDTO(List<GrupoDTO> grupos) {
        this.grupos = grupos;
    }

    public List<GrupoDTO> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<GrupoDTO> grupos) {
        this.grupos = grupos;
    }

}
