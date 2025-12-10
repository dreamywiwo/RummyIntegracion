/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ejercerturno.modelo;

import itson.rummydtos.FichaDTO;
import itson.rummydtos.GrupoDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummypresentacion.utils.TipoVista;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dana Chavez
 */
public interface IModeloEjercerTurno extends ISubject {
    
    // Estado del tablero
    List<GrupoDTO> getGruposEnTablero();
    
    // Estado del jugador actual
    JugadorDTO getJugadorActual();
    List<FichaDTO> getFichasMano();
    boolean esTurnoDe(String jugadorID);
    
    // Estado de otros jugadores
    List<JugadorDTO> getOtrosJugadores();
    public Map<String, Integer> getMapaFichasOponentes();
    
    // Estado del juego
    int getFichasEnPozo();
    String getTurnoActual();
    boolean isPartidaTerminada();
    JugadorDTO getGanador();
    
    // Información de la última acción
    String getUltimaAccion();
    boolean isAccionValida();
    String getMensajeError();   

    public boolean juegoTerminado();

    public String getJugadorGanadorId();
    
    String getGrupoInvalidoId();
    
    public int getCantidadFichasSopa();
    public String getJugadorActivoId();
    
    public TipoVista getVistaActual();


}
