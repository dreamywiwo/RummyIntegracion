package itson.rummyeventos.actualizaciones;

import itson.rummyeventos.base.EventBase;

public class InvalidGroupEvent extends EventBase {

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "grupo.invalido";

    private String jugadorId;
    private String grupoId;

    public InvalidGroupEvent() {
        super();
    }

    public InvalidGroupEvent(String jugadorId, String grupoId) {
        super(TOPIC, EVENT_TYPE);
        this.jugadorId = jugadorId;
        this.grupoId = grupoId;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public String getGrupoId() {
        return grupoId;
    }
}
