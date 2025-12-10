/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummypresentacion.utils;

import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummydtos.TableroDTO;
import itson.rummylistener.interfaces.IConfiguracionListener;
import itson.rummylistener.interfaces.IGameGlobalListener;
import itson.rummylistener.interfaces.ITurnoListener;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class ListenerProxy implements IGameGlobalListener {

    private IConfiguracionListener configListener;
    private ITurnoListener turnoListener;

    public void activarModoConfiguracion(IConfiguracionListener listener) {
        this.configListener = listener;
        this.turnoListener = null;
    }

    public void activarModoJuego(ITurnoListener listener) {
        this.turnoListener = listener;
        this.configListener = null;
    }

    @Override
    public void recibirError(String mensaje) {
        if (configListener != null) configListener.recibirError(mensaje);
        if (turnoListener != null) turnoListener.recibirError(mensaje);
    }

    @Override
    public void recibirTablero(TableroDTO tablero) {
        if (turnoListener != null) turnoListener.recibirTablero(tablero);
    }

    @Override
    public void recibirMano(List<FichaDTO> mano) {
        if (turnoListener != null) turnoListener.recibirMano(mano);
    }

    @Override
    public void recibirSopa(int cantidad) {
        if (turnoListener != null) turnoListener.recibirSopa(cantidad);
    }

    @Override
    public void terminoTurno(String jugadorActivoId) {
        if (turnoListener != null) turnoListener.terminoTurno(jugadorActivoId);
    }

    @Override
    public void marcarJuegoTerminado(JugadorDTO ganador) {
        if (turnoListener != null) turnoListener.marcarJuegoTerminado(ganador);
    }

    @Override
    public void resaltarGrupoInvalido(String grupoId) {
        if (turnoListener != null) turnoListener.resaltarGrupoInvalido(grupoId);
    }

    @Override
    public void actualizarFichasOponente(String jugadorId, int cantidad) {
        if (turnoListener != null) turnoListener.actualizarFichasOponente(jugadorId, cantidad);
    }
    
    @Override
    public void recibirConfirmacionPartida() {
        if (configListener != null) configListener.recibirConfirmacionPartida();
    }
    
}
