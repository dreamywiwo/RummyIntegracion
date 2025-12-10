package itson.producerjugador.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummydtos.FichaDTO;
import itson.rummyeventos.acciones.EstadoSolicitadoEvent;
import itson.rummyeventos.acciones.FichaDevueltaEvent;
import itson.rummyeventos.acciones.FichaTomadaEvent;
import itson.rummyeventos.acciones.GrupoActualizadoEvent;
import itson.rummyeventos.acciones.GrupoCreadoEvent;
import itson.rummyeventos.acciones.TerminoTurnoEvent;
import itson.serializer.implementacion.JsonSerializer;
import java.util.List;

public class JugarTurnoEmitter extends BaseEmitter {

    public JugarTurnoEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        super(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    public void emitirGrupoCreadoEvent(List<FichaDTO> fichas) {
        GrupoCreadoEvent event = new GrupoCreadoEvent(fichas);
        System.out.println("hasta aqui llego el grupo creado" + fichas);
        enviarEvento(event);
    }

    public void emitirGrupoActualizadoEvent(String grupoId, List<FichaDTO> fichasNuevas) {
        enviarEvento(new GrupoActualizadoEvent(grupoId, fichasNuevas));
    }

    public void emitirFichaTomadaEvent() {
        enviarEvento(new FichaTomadaEvent());
    }

    public void emitirTerminoTurnoEvent() {
        enviarEvento(new TerminoTurnoEvent());
    }

    public void emitirFichaDevueltaEvent(String grupoId, String fichaId) {
        enviarEvento(new FichaDevueltaEvent(grupoId, fichaId));
    }

    public void emitirEstadoSolicitadoEvent(String idJugadorLocal) {
        enviarEvento(new EstadoSolicitadoEvent(idJugadorLocal));
    }
}