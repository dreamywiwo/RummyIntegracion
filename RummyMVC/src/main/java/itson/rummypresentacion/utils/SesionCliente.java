package itson.rummypresentacion.utils;

import itson.producerjugador.facade.IProducerJugador;
import itson.rummydtos.JugadorDTO;

/**
 * Singleton u Objeto compartido que mantiene la conexión viva mientras
 * cambiamos de modelos y vistas.
 */
public class SesionCliente {

    private final IProducerJugador producer;
    private final JugadorDTO jugadorLocal;
    private final ListenerProxy listenerProxy; 

    public SesionCliente(IProducerJugador producer, JugadorDTO jugadorLocal, ListenerProxy listenerProxy) {
        this.producer = producer;
        this.jugadorLocal = jugadorLocal;
        this.listenerProxy = listenerProxy;
    }

    public IProducerJugador getProducer() {
        return producer;
    }

    public JugadorDTO getJugadorLocal() {
        return jugadorLocal;
    }

    public ListenerProxy getListenerProxy() {
        return listenerProxy;
    }
}
