package itson.traducerjugador.mappers;

import itson.rummyeventos.actualizaciones.CantidadFichasPublicoEvent;
import itson.rummyeventos.actualizaciones.ErrorEvent;
import itson.rummyeventos.actualizaciones.InvalidGroupEvent;
import itson.rummyeventos.actualizaciones.JuegoTerminadoEvent;
import itson.rummyeventos.actualizaciones.ManoActualizadaEvent;
import itson.rummyeventos.actualizaciones.SopaActualizadaEvent;
import itson.rummyeventos.actualizaciones.TableroActualizadoEvent;
import itson.rummyeventos.actualizaciones.TurnoTerminadoEvent;
import itson.rummyeventos.base.EventBase;
import itson.rummylistener.interfaces.IGameGlobalListener;
import itson.serializer.interfaces.ISerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class EventMapper {

    private final ISerializer serializer;
    private IGameGlobalListener listener;

    private String miJugadorId;

    private final Map<String, BiConsumer<String, ISerializer>> handlers = new HashMap<>();

    public EventMapper(ISerializer serializer) {
        this.serializer = serializer;

        register("tablero.actualizado", this::handleTableroActualizado);
        register("mano.actualizada", this::handleManoActualizada);
        register("sopa.actualizada", this::handleSopaActualizada);
        register("turno.terminado", this::handleTurnoTerminado);
        register("mensaje.error", this::handleError);
        register("juego.terminado", this::handleJuegoTerminado);
        register("grupo.invalido", this::handleHighlightInvalidGroup);
        register("fichas.jugador.cantidad", this::handleCantidadFichas);
        register("partida.creada", this::handlePartidaCreadaExitosamente);

    }

    public void setListener(IGameGlobalListener listener) {
        this.listener = listener;
    }

    public void setJugadorId(String jugadorId) {
        this.miJugadorId = jugadorId;
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


    private void handleTableroActualizado(String rawPayload, ISerializer serializer) {
        try {
            TableroActualizadoEvent event = serializer.deserialize(rawPayload, TableroActualizadoEvent.class);
            if (listener != null) {
                listener.recibirTablero(event.getTableroSnapshot());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSopaActualizada(String rawPayload, ISerializer serializer) {
        try {
            SopaActualizadaEvent event = serializer.deserialize(rawPayload, SopaActualizadaEvent.class);
            if (listener != null) {
                listener.recibirSopa(event.getNumFichasRestantes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTurnoTerminado(String rawPayload, ISerializer serializer) {
        try {
            TurnoTerminadoEvent event = serializer.deserialize(rawPayload, TurnoTerminadoEvent.class);
            if (listener != null) {
                listener.terminoTurno(event.getNuevoTurnoJugador());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleJuegoTerminado(String rawPayload, ISerializer serializer) {
        try {
            JuegoTerminadoEvent event = serializer.deserialize(rawPayload, JuegoTerminadoEvent.class);
            if (listener != null) {
                listener.marcarJuegoTerminado(event.getJugadorGanador());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleManoActualizada(String rawPayload, ISerializer serializer) {
        try {
            ManoActualizadaEvent event = serializer.deserialize(rawPayload, ManoActualizadaEvent.class);

            if (!event.getJugadorId().equals(miJugadorId)) {
                return; 
            }

            if (listener != null) {
                listener.recibirMano(event.getManoSnapshot());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleError(String rawPayload, ISerializer serializer) {
        try {
            ErrorEvent event = serializer.deserialize(rawPayload, ErrorEvent.class);

            if (!event.getJugadorId().equals(miJugadorId)) {
                return; 
            }

            if (listener != null) {
                listener.recibirError(event.getMensajeError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleHighlightInvalidGroup(String rawPayload, ISerializer serializer) {
        try {
            InvalidGroupEvent event =
                serializer.deserialize(rawPayload, InvalidGroupEvent.class);


            if (!event.getJugadorId().equals(event.getJugadorId())) {
                return;
            }
            
            if (listener != null) {
                listener.resaltarGrupoInvalido(event.getGrupoId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleCantidadFichas(String rawPayload, ISerializer serializer) {
        try {
            CantidadFichasPublicoEvent event = serializer.deserialize(rawPayload, CantidadFichasPublicoEvent.class);
            
            if (listener != null) {
                listener.actualizarFichasOponente(event.getJugadorId(), event.getSize());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void handlePartidaCreadaExitosamente(String rawPayload, ISerializer serializer){
        try {
            if (listener != null) {
                listener.recibirConfirmacionPartida();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
