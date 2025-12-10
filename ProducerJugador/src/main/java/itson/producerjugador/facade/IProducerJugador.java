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

    public void devolverFicha(String grupoId, String fichaId);

    public void configurarPartida(int maxNumFichas, int cantidadComodines);

    public void solicitarEstadoJuego(String idJugadorLocal);
}
