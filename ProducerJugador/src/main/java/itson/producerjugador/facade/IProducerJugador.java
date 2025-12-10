/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.producerjugador.facade;

import itson.rummydtos.FichaDTO;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public interface IProducerJugador {
    
    public abstract void crearGrupo(List<FichaDTO> fichas);
    
    public abstract void actualizarGrupo(String grupoId, List<FichaDTO> fichasNuevas);
    
    public abstract void tomarFicha();
    
    public abstract void terminarTurno();
    
    public abstract void registrarJugador(String miId, String ipCliente, int miPuertoDeEscucha);

    public abstract void devolverFicha(String grupoId, String fichaId);

    public abstract void configurarPartida(String idJugadorSolicitante, int maxNumFichas, int cantidadComodines);

    public abstract void solicitarEstadoJuego(String idJugadorLocal);
    
    public abstract void actualizarPerfil(String id, String nombre, String avatar, List<String> colores);
    
    public abstract void solicitarUnirsePartida(String jugadorId);
    
    public abstract void solicitarInfoSala(String id);
    
    public abstract void enviarJugadorListo(String jugadorId);
}
