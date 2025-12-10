/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.producerjugador.facade;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.producerjugador.emitters.ConfigurarPartidaEmitter;
import itson.producerjugador.emitters.InicializarJuegoEmitter;
import itson.producerjugador.emitters.JugarTurnoEmitter;
import itson.producerjugador.emitters.RegistrarJugadorEmitter;
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
    private final RegistrarJugadorEmitter registrarJugadorEmitter;

    public ProducerJugador(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort, String jugadorId) {
        this.jugarTurnoEmitter = new JugarTurnoEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
        this.inicializarJuegoEmitter = new InicializarJuegoEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
        this.configurarPartidaEmitter = new ConfigurarPartidaEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
        this.registrarJugadorEmitter = new RegistrarJugadorEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
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
    public void configurarPartida(String idJugadorSolicitante, int maxNumFichas, int cantidadComodines) {
        configurarPartidaEmitter.emitirPartidaConfiguradaEvent(idJugadorSolicitante, maxNumFichas, cantidadComodines);
    }

    @Override
    public void solicitarEstadoJuego(String idJugadorLocal) {
        jugarTurnoEmitter.emitirEstadoSolicitadoEvent(idJugadorLocal);
    }

    @Override
    public void actualizarPerfil(String id, String nombre, String avatar, List<String> colores) {
        registrarJugadorEmitter.emitirPerfilActualizadoEvent(id, nombre, avatar, colores);
    }

    @Override
    public void solicitarUnirsePartida(String jugadorId) {
        registrarJugadorEmitter.emitirUnirsePartidaEvent(jugadorId);
    }

    @Override
    public void solicitarInfoSala(String id) {
        registrarJugadorEmitter.emitirInfoSalaSolicitadaEvent(id);
    }

}
