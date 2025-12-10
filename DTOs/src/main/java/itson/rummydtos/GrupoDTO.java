/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummydtos;

import java.util.List;

/**
 *
 * @author jrasc
 */
public class GrupoDTO {

    private String id;
    private List<FichaDTO> fichas;
    private boolean valido;

    public GrupoDTO() {
    }

    public GrupoDTO(String id, List<FichaDTO> fichas, boolean valido) {
        this.id = id;
        this.fichas = fichas;
        this.valido = valido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FichaDTO> getFichas() {
        return fichas;
    }

    public void setFichas(List<FichaDTO> fichas) {
        this.fichas = fichas;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

}
