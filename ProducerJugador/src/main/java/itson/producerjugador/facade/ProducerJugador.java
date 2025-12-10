/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.producerjugador.facade;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.producerjugador.emitters.ConfigurarPartidaEmitter;
import itson.producerjugador.emitters.InicializarJuegoEmitter;
import itson.producerjugador.emitters.JugarTurnoEmitter;
import itson.rummydtos.FichaDTO;
import itson.serializer.implementacion.JsonSerializer;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class ProducerJugador implements IProducerJugador {

    private final JugarTurnoEmitter jugarTurnoEmitter;
    private final InicializarJuegoEmitter inicializarJuegoEmitter;
    private final ConfigurarPartidaEmitter configurarPartidaEmitter;

    public ProducerJugador(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort, String jugadorId) {
        this.jugarTurnoEmitter = new JugarTurnoEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
        this.inicializarJuegoEmitter = new InicializarJuegoEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
        this.configurarPartidaEmitter = new ConfigurarPartidaEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    @Override
    public void crearGrupo(List<FichaDTO> fichas) {
        jugarTurnoEmitter.emitirGrupoCreadoEvent(fichas);
    }

    @Override
    public void actualizarGrupo(String grupoId, List<FichaDTO> fichasNuevas) {
        jugarTurnoEmitter.emitirGrupoActualizadoEvent(grupoId, fichasNuevas);
    }

    @Override
    public void tomarFicha() {
        jugarTurnoEmitter.emitirFichaTomadaEvent();
    }

    @Override
    public void terminarTurno() {
        jugarTurnoEmitter.emitirTerminoTurnoEvent();
    }

    @Override
    public void registrarJugador(String miId, String ipCliente, int miPuertoDeEscucha) {
        inicializarJuegoEmitter.emitirRegistroJugadorEvent(miId, ipCliente, miPuertoDeEscucha);
    }

    @Override
    public void devolverFicha(String grupoId, String fichaId) {
        jugarTurnoEmitter.emitirFichaDevueltaEvent(grupoId, fichaId);
    }

    @Override
    public void configurarPartida(int maxNumFichas, int cantidadComodines) {
        configurarPartidaEmitter.emitirPartidaConfiguradaEvent(maxNumFichas, cantidadComodines);
    }

    @Override
    public void solicitarEstadoJuego(String idJugadorLocal) {
        jugarTurnoEmitter.emitirEstadoSolicitadoEvent(idJugadorLocal);
    }

}
