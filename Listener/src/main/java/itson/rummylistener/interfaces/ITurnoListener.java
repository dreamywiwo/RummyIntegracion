/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.rummylistener.interfaces;

import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummydtos.TableroDTO;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public interface ITurnoListener {

    void recibirTablero(TableroDTO tablero);

    void recibirMano(List<FichaDTO> mano);

    void recibirSopa(int cantidad);

    void terminoTurno(String jugadorActivoId);

    void recibirError(String mensaje); 

    void marcarJuegoTerminado(JugadorDTO ganador);

    void resaltarGrupoInvalido(String grupoId);

    void actualizarFichasOponente(String jugadorId, int cantidad);
}
