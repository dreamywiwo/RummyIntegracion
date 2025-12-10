/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jrasc
 */
public class SubscriptionRegistry {

    private final Map<String, List<String>> suscripciones = new HashMap<>();

    public List<String> getSuscriptores(String topic) {
        return suscripciones.getOrDefault(topic, new ArrayList<>());
    }

    public void addSuscriptor(String topic, String clienteID) {
        suscripciones.computeIfAbsent(topic, k -> new ArrayList<>()).add(clienteID);
        System.out.println("Se registro el cliente con id: " + "y se le asigno el tema: " + topic);
    }
    
    public void removeSuscriptor(String topic, String clienteID){
        if (suscripciones.containsKey(topic)) {
            suscripciones.get(topic).remove(clienteID);
        }
        if (suscripciones.get(topic).isEmpty()) {
            suscripciones.remove(topic);
        }
        System.out.println("Se elimino el cliente con id: " + "del tema: " + topic);
    }
    
}
