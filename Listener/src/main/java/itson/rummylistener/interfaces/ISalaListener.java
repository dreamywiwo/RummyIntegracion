/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.rummylistener.interfaces;

import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public interface ISalaListener {
    public abstract void recibirActualizacionSala(List<JugadorDTO> jugadores);
    
    public abstract void recibirMano(List<FichaDTO> mano);
}
