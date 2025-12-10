/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package itson.producerdominio.facade;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.producerdominio.emitters.EstadoJuegoEmitter;
import itson.producerdominio.emitters.InicializarJuegoEmitter;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummydtos.TableroDTO;
import itson.serializer.implementacion.JsonSerializer;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class ProducerDominio implements IProducerDominio {

    private final InicializarJuegoEmitter inicializarJuegoEmitter;
    private final EstadoJuegoEmitter estadoJuegoEmitter;

    public ProducerDominio (JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        this.inicializarJuegoEmitter = new InicializarJuegoEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
        this.estadoJuegoEmitter = new EstadoJuegoEmitter(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    @Override
    public void actualizarTablero(TableroDTO snapshotTablero) {
        estadoJuegoEmitter.emitirTableroActualizadoEvent(snapshotTablero);
    }

    @Override
    public void actualizarManoJugador(String jugadorId, List<FichaDTO> snapshotMano) {
        estadoJuegoEmitter.emitirManoActualizadaEvent(jugadorId, snapshotMano);
    }

    @Override
    public void actualizarSopa(int numFichasSopa) {
        estadoJuegoEmitter.emitirSopaActualizadaEvent(numFichasSopa);
    }

    @Override
    public void actualizarTurno(String nuevoTurno) {
        estadoJuegoEmitter.emitirTurnoTerminadoEvent(nuevoTurno);
    }

    @Override
    public void mostrarError(String jugadorId, String mensajeError) {
        estadoJuegoEmitter.emitirErrorEvent(jugadorId, mensajeError);
    }

    @Override
    public void juegoTerminado(JugadorDTO jugador) {
        estadoJuegoEmitter.emitirJuegoTerminadoEvent(jugador);
    }

    @Override
    public void enviarCantidadFichasPublico(String jugadorId, int size) {
        estadoJuegoEmitter.emitirCantidadFichasPublicoEvent(jugadorId, size);
    }

    @Override
    public void highlightInvalidGroup(String jugadorId, String grupoId) {
        estadoJuegoEmitter.emitirHighlightInvalidGroupEvent(jugadorId, grupoId);
    }

    @Override
    public void registrarDominio(String miId, String ipCliente, int miPuertoDeEscucha) {
        inicializarJuegoEmitter.emitirRegistroDominioEvent(miId, ipCliente, miPuertoDeEscucha);
    }

    @Override
    public void enviarPartidaCreada() {
        estadoJuegoEmitter.emitirPartidaCreadaEvent();
    }

}
