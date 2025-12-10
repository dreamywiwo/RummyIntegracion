/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.solicitarinicio.modelo;

import itson.rummydtos.JugadorDTO;
import itson.rummypresentacion.utils.TipoVista;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public interface IModeloSalaEspera {
    
    public abstract List<JugadorDTO> getJugadoresEnSala();
    
    public abstract TipoVista getVistaActual();
    
    public abstract String getMiId();
}
