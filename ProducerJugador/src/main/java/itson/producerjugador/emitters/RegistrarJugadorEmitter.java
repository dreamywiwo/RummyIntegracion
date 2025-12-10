/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.producerjugador.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummyeventos.acciones.InfoSalaSolicitadaEvent;
import itson.rummyeventos.acciones.JugadorListoEvent;
import itson.rummyeventos.acciones.PerfilActualizadoEvent;
import itson.rummyeventos.acciones.UnirsePartidaEvent;
import itson.serializer.implementacion.JsonSerializer;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class RegistrarJugadorEmitter extends BaseEmitter {

    public RegistrarJugadorEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        super(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    public void emitirPerfilActualizadoEvent(String id, String nombre, String avatar, List<String> colores) {
        PerfilActualizadoEvent event = new PerfilActualizadoEvent(id, nombre, avatar, colores);
        enviarEvento(event);
    }

    public void emitirUnirsePartidaEvent(String jugadorId) {
        UnirsePartidaEvent event = new UnirsePartidaEvent(jugadorId);
        enviarEvento(event);
    }

    public void emitirInfoSalaSolicitadaEvent(String id) {
        InfoSalaSolicitadaEvent event = new InfoSalaSolicitadaEvent(id);
        enviarEvento(event);
    }

    public void emitirJugadorListoEvent(String jugadorId) {
        JugadorListoEvent event = new JugadorListoEvent(jugadorId);
        enviarEvento(event);
    }
    
}
