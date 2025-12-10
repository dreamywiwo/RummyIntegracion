/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.producerdominio.emitters;

import com.mycompany.conexioninterfaces.IDispatcher;
import itson.rummyeventos.sistema.RegistroDominioEvent;
import itson.serializer.implementacion.JsonSerializer;

/**
 *
 * @author Dana Chavez
 */
public class InicializarJuegoEmitter extends BaseEmitter {

    public InicializarJuegoEmitter(JsonSerializer jsonSerializer, IDispatcher dispatcher, String brokerIp, int brokerPort) {
        super(jsonSerializer, dispatcher, brokerIp, brokerPort);
    }

    public void emitirRegistroDominioEvent(String miId, String ipCliente, int miPuertoDeEscucha) {
        RegistroDominioEvent event = new RegistroDominioEvent(miId, ipCliente, miPuertoDeEscucha);
        enviarEvento(event);
    }
}
