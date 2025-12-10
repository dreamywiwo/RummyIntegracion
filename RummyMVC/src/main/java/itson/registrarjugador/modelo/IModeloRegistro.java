/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.registrarjugador.modelo;

import itson.rummydtos.JugadorDTO;
import itson.rummypresentacion.utils.TipoVista;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public interface IModeloRegistro {

    public JugadorDTO getJugadorTemporal();

    public List<String> getAvataresDisponibles();

    public List<String> getColoresSistema();

    public TipoVista getVistaActual();
    
    public boolean isRegistroExitoso();
    
    public abstract String getMensajeError();
}
