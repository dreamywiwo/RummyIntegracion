/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.traducerjugador.facade;

import com.mycompany.conexioninterfaces.IReceptorComponente;
import itson.rummyeventos.base.EventBase;
import itson.serializer.interfaces.ISerializer;
import itson.traducerjugador.mappers.EventMapper;

/**
 *
 * @author Dana Chavez
 */
public class TraducerJugador implements IReceptorComponente {

    private final ISerializer serializer;
    private final EventMapper mapper;

    public TraducerJugador(ISerializer serializer, EventMapper mapper) {
        this.serializer = serializer;
        this.mapper = mapper;
    }

    @Override
    public void recibirMensaje(String payload) {
        onMessage(payload);
    }

    public void onMessage(String payload) {
        try {
            EventBase base = serializer.deserialize(payload, EventBase.class);
            if (base == null || base.getEventType() == null) {
                System.out.println("Mensaje sin eventType: " + payload);
                return;
            }
            mapper.route(base, payload);
        } catch (Exception e) {
            System.err.println("Error traduciendo mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
