/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.producerdominio.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummyeventos.base.EventBase;
import itson.serializer.implementacion.JsonSerializer;

/**
 *
 * @author Dana Chavez
 */
public class BaseEmitter {
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
