/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.traducerdominio.mappers;

import itson.dominiorummy.facade.IDominio;
import itson.rummyeventos.acciones.EstadoSolicitadoEvent;
import itson.rummyeventos.acciones.FichaDevueltaEvent;
import itson.rummyeventos.acciones.FichaTomadaEvent;
import itson.rummyeventos.acciones.GrupoActualizadoEvent;
import itson.rummyeventos.acciones.GrupoCreadoEvent;
import itson.rummyeventos.acciones.PartidaConfiguradaEvent;
import itson.rummyeventos.acciones.TerminoTurnoEvent;
import itson.rummyeventos.base.EventBase;
import itson.serializer.interfaces.ISerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 *
 * @author Dana Chavez
 */
public class EventMapper {
    
    private final ISerializer serializer;
    private final IDominio dominio;
    
    private final Map<String, BiConsumer<String, ISerializer>> handlers = new HashMap<>();

    //agregar IDominio al constructor
    public EventMapper(ISerializer serializer, IDominio dominio) {
        this.serializer = serializer;
        this.dominio = dominio;

        // Registrar handlers
        register("grupo.creado", this::handleGrupoCreado);
        register("grupo.actualizado", this::handleGrupoActualizado);
        register("ficha.tomada", this::handleFichaTomada);
        register("termino.turno", this::handleTerminoTurno);
        register("ficha.devuelta", this::handleFichaDevuelta);
        register("partida.configurada", this::handlePartidaConfigurada);
        register("estado.solicitado", this::handleEstadoSolicitado);
    }
    
    public void register(String eventType, BiConsumer<String, ISerializer> handler) {
        handlers.put(eventType, handler);
    }
    
    public void route(EventBase base, String rawPayload) {
        String et = base.getEventType();
        
        BiConsumer<String, ISerializer> handler = handlers.get(et);
        
        if (handler == null) {
            System.out.println("No existe handler para " + et);
            return;
        }
        
        handler.accept(rawPayload, serializer);
    }

    // Handlers 
    private void handleGrupoCreado(String rawPayload, ISerializer serializer) {
        try {
            GrupoCreadoEvent event = serializer.deserialize(rawPayload, GrupoCreadoEvent.class);
            dominio.crearGrupo(event.getFichas());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleGrupoActualizado(String rawPayload, ISerializer serializer) {
        try {
            GrupoActualizadoEvent event = serializer.deserialize(rawPayload, GrupoActualizadoEvent.class);
            dominio.actualizarGrupo(event.getGrupoId(), event.getNuevasFichas());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleFichaTomada(String rawPayload, ISerializer serializer) {
        try {
            FichaTomadaEvent event = serializer.deserialize(rawPayload, FichaTomadaEvent.class);
            dominio.tomarFicha();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleTerminoTurno(String rawPayload, ISerializer serializer) {
        try {
            TerminoTurnoEvent event = serializer.deserialize(rawPayload, TerminoTurnoEvent.class);
            dominio.terminarTurno();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleFichaDevuelta(String rawPayload, ISerializer serializer) {
        try {
            FichaDevueltaEvent event = serializer.deserialize(rawPayload, FichaDevueltaEvent.class);
            dominio.devolverFichaAMano(event.getGrupoId(), event.getFichaId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handlePartidaConfigurada(String rawPayload, ISerializer serializer){
        try {
            PartidaConfiguradaEvent event = serializer.deserialize(rawPayload, PartidaConfiguradaEvent.class);
            dominio.configurarPartida(event.getMaxNumFichas(), event.getCantidadComodines());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleEstadoSolicitado(String rawPayload, ISerializer serializer){
        try {
            EstadoSolicitadoEvent event = serializer.deserialize(rawPayload, EstadoSolicitadoEvent.class);
            dominio.procesarSolicitudEstado(event.getJugadorId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
