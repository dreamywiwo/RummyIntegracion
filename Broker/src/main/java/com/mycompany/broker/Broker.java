/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.broker;

import com.mycompany.conexioninterfaces.IDispatcher;
import com.mycompany.conexioninterfaces.IReceptorComponente;
import interfaces.IBroker;
import itson.directorio.implementacion.ConnectionEndpoint;
import itson.directorio.implementacion.Directorio;
import itson.directorio.interfaces.IDirectorio;
import itson.rummyeventos.base.EventBase;
import itson.serializer.interfaces.ISerializer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jrasc
 */
public class Broker implements IBroker {

    private final IDirectorio directorio;
    private final IDispatcher dispatcher;
    private final ISerializer serializer;
    private final SubscriptionRegistry registry;

    private final Map<String, List<String>> rutas = new HashMap<>();

    public Broker(IDirectorio directorio, IDispatcher dispatcher, ISerializer serializer, SubscriptionRegistry registry) {
        this.directorio = directorio;
        this.dispatcher = dispatcher;
        this.serializer = serializer;
        this.registry = registry;
    }

    @Override
    public void publicar(String json) {
        EventBase base = serializer.deserialize(json, EventBase.class);

        if (base == null || base.getEventType() == null) {
            return;
        }
        String topic = base.getTopic();

        List<String> clientesID = registry.getSuscriptores(topic);
        List<ConnectionEndpoint> endpoints = directorio.getEndpoints(clientesID);
        for (ConnectionEndpoint endpoint : endpoints) {
            System.out.println("Se envia a: " + endpoint.getClientId());
            dispatcher.enviar(json, endpoint.getPort(), endpoint.getIp());
        }
    }

    @Override
    public void suscribir(String topic, String clienteID, String ip, int port) {
        directorio.registerClient(clienteID, ip, port);
        registry.addSuscriptor(topic, clienteID);
        System.out.println("Se suscribio correctamente el jugador: " + clienteID);
    }

    @Override
    public void unsubscribe(String topic, String clienteID) {
        registry.removeSuscriptor(topic, clienteID);
    }

}
