package itson.producerjugador.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummyeventos.sistema.RegistroJugadorEvent;
import itson.serializer.implementacion.JsonSerializer;

public class InicializarJuegoEmitter extends BaseEmitter {

    public InicializarJuegoEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        super(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }
    
    public void emitirRegistroJugadorEvent(String miId, String ipCliente, int miPuertoDeEscucha) {
        RegistroJugadorEvent event = new RegistroJugadorEvent(miId, ipCliente, miPuertoDeEscucha);
        enviarEvento(event);
    }
}