package itson.producerjugador.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummyeventos.acciones.PartidaConfiguradaEvent;
import itson.serializer.implementacion.JsonSerializer;

public class ConfigurarPartidaEmitter extends BaseEmitter {

    public ConfigurarPartidaEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        super(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    public void emitirPartidaConfiguradaEvent(int maxNumFichas, int cantidadComodines) {
        PartidaConfiguradaEvent event = new PartidaConfiguradaEvent(maxNumFichas, cantidadComodines);
        enviarEvento(event); 
    }
}