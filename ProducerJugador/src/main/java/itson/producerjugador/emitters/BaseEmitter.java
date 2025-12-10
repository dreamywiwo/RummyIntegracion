package itson.producerjugador.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.serializer.implementacion.JsonSerializer;
import itson.rummyeventos.base.EventBase; // Asumiendo que tus eventos heredan de EventBase

public abstract class BaseEmitter {
    
    protected final JsonSerializer jsonSerializer;
    protected final IDispatcher dispatcher;
    protected final String brokerIp;
    protected final int brokerPort;

    public BaseEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        this.jsonSerializer = jsonSerializer;
        this.dispatcher = dispatcher;
        this.brokerIp = brokerIp;
        this.brokerPort = brokerPort;
    }

    protected void enviarEvento(EventBase event) {
        String json = jsonSerializer.serialize(event);
        dispatcher.enviar(json, brokerPort, brokerIp);
    }
}