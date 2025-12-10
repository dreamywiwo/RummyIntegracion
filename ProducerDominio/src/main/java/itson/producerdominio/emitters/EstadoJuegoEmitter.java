/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.producerdominio.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummydtos.TableroDTO;
import itson.rummyeventos.actualizaciones.CantidadFichasPublicoEvent;
import itson.rummyeventos.actualizaciones.ErrorEvent;
import itson.rummyeventos.actualizaciones.InvalidGroupEvent;
import itson.rummyeventos.actualizaciones.JuegoTerminadoEvent;
import itson.rummyeventos.actualizaciones.ManoActualizadaEvent;
import itson.rummyeventos.actualizaciones.PartidaCreadaExitoEvent;
import itson.rummyeventos.actualizaciones.SopaActualizadaEvent;
import itson.rummyeventos.actualizaciones.TableroActualizadoEvent;
import itson.rummyeventos.actualizaciones.TurnoTerminadoEvent;
import itson.serializer.implementacion.JsonSerializer;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class EstadoJuegoEmitter extends BaseEmitter {

    public EstadoJuegoEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        super(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    public void emitirTableroActualizadoEvent(TableroDTO snapshotTablero) {
        TableroActualizadoEvent event = new TableroActualizadoEvent(snapshotTablero);
        enviarEvento(event);
    }

    public void emitirManoActualizadaEvent(String jugadorDestino, List<FichaDTO> snapshotMano) {
        ManoActualizadaEvent event = new ManoActualizadaEvent(jugadorDestino, snapshotMano);
        enviarEvento(event);
    }

    public void emitirSopaActualizadaEvent(int numFichasSopa) {
        SopaActualizadaEvent event = new SopaActualizadaEvent(numFichasSopa);
        enviarEvento(event);
    }

    public void emitirTurnoTerminadoEvent(String nuevoTurno) {
        TurnoTerminadoEvent event = new TurnoTerminadoEvent(nuevoTurno);
        enviarEvento(event);
    }

    public void emitirErrorEvent(String jugadorId, String mensajeError) {
        ErrorEvent event = new ErrorEvent(jugadorId, mensajeError);
        enviarEvento(event);
    }

    public void emitirJuegoTerminadoEvent(JugadorDTO jugador) {
        JuegoTerminadoEvent event = new JuegoTerminadoEvent(jugador);
        enviarEvento(event);
    }

    public void emitirCantidadFichasPublicoEvent(String jugadorId, int size) {
        CantidadFichasPublicoEvent event = new CantidadFichasPublicoEvent(jugadorId, size);
        enviarEvento(event);
    }

    public void emitirHighlightInvalidGroupEvent(String jugadorId, String grupoId) {
        InvalidGroupEvent event = new InvalidGroupEvent(jugadorId, grupoId);
        enviarEvento(event);
    }

    public void emitirPartidaCreadaEvent() {
        PartidaCreadaExitoEvent event = new PartidaCreadaExitoEvent();
        enviarEvento(event);
    }
}
