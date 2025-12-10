/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClaseDispatcher;

import interfaces.IDispatcherObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author jrasc
 */
public class ColaDispatcher {

    private BlockingQueue<String> salida = new LinkedBlockingQueue<>();

    private List<IDispatcherObserver> observadores = new ArrayList<>();

    public void encolar(String json, int port, String ip) {
        try {
            salida.put(json);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (IDispatcherObserver obs : observadores) {
            obs.update(json, port, ip);
        }
    }

    public void attach(IDispatcherObserver observador) {
        observadores.add(observador);
    }
}
