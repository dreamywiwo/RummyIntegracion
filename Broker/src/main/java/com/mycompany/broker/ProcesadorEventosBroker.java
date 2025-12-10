package com.mycompany.broker;

import com.mycompany.conexioninterfaces.IReceptorComponente;
import interfaces.IBroker;
import itson.rummyeventos.base.EventBase;
import itson.rummyeventos.sistema.RegistroDominioEvent; // Asegúrate de tener esta clase
import itson.rummyeventos.sistema.RegistroJugadorEvent;
import itson.serializer.interfaces.ISerializer;

/**
 * Recibe el JSON crudo del socket, identifica qué tipo de evento es
 * y decide si es una suscripción (configuración) o un mensaje para retransmitir.
 */
public class ProcesadorEventosBroker implements IReceptorComponente {

    private final IBroker broker;
    private final ISerializer serializer;

    public ProcesadorEventosBroker(IBroker broker, ISerializer serializer) {
        this.broker = broker;
        this.serializer = serializer;
    }

    @Override
    public void recibirMensaje(String json) {

        // 1. Deserialización base para ver el "eventType"
        EventBase base = serializer.deserialize(json, EventBase.class);

        if (base == null || base.getEventType() == null) {
            System.err.println("BROKER: Recibido JSON inválido o sin eventType.");
            return;
        }

        switch (base.getEventType()) {

            // CASO 1: REGISTRO DE UN JUGADOR
            case RegistroJugadorEvent.EVENT_TYPE -> {
                RegistroJugadorEvent ev = serializer.deserialize(json, RegistroJugadorEvent.class);
                
                System.out.println("BROKER: Registrando JUGADOR -> " + ev.getJugadorId());

                // A. Suscribir al tópico de sistema
                broker.suscribir(
                        RegistroJugadorEvent.TOPIC, 
                        ev.getJugadorId(),
                        ev.getIp(),
                        ev.getPuerto()
                );

                // Se suscribe a "actualizaciones.estado".
                broker.suscribir(
                        "actualizaciones.estado",
                        ev.getJugadorId(),
                        ev.getIp(),
                        ev.getPuerto()
                );
            }

            // CASO 2: REGISTRO DEL DOMINIO (SERVIDOR DE JUEGO)
            case RegistroDominioEvent.EVENT_TYPE -> {
                RegistroDominioEvent ev = serializer.deserialize(json, RegistroDominioEvent.class);
                
                System.out.println("BROKER: Registrando DOMINIO -> " + ev.getDominioId());

                // A. Suscribir al tópico de sistema
                broker.suscribir(
                        RegistroDominioEvent.TOPIC,
                        ev.getDominioId(),
                        ev.getIp(),
                        ev.getPuerto()
                );

                // Se suscribe a "acciones.jugador" (donde viaja 'termino.turno', 'grupo.creado', etc.)
                broker.suscribir(
                        "acciones.jugador",
                        ev.getDominioId(),
                        ev.getIp(),
                        ev.getPuerto()
                );
            }

            // CASO 3: CUALQUIER OTRO EVENTO (Ruteo normal)
            // Si llega 'termino.turno', 'mano.actualizada', etc., cae aquí.
            // El broker revisa el 'topic' del JSON y lo envía a quien esté suscrito.
            default -> {
                broker.publicar(json);
            }
        }
    }
}