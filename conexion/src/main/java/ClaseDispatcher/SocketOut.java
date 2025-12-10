/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClaseDispatcher;

import interfaces.IDispatcherObserver;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jrasc
 */
public class SocketOut implements IDispatcherObserver {

    private ExecutorService worker;

    public void start() {
        this.worker = Executors.newCachedThreadPool();
    }

    public void close() {
        if (worker != null && !worker.isShutdown()) {
            worker.shutdown();
        }
    }

    @Override
    public void update(String json, int port, String ip) {
        System.out.println("Recibido. Enviando a " + ip + ":" + port);
        worker.submit(() -> {
            try (Socket socket = new Socket(ip, port); PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Enviando '" + json + "' a " + ip + ":" + port);
                out.println(json);
                System.out.println("Envío completado");

            } catch (Exception e) {
                System.err.println("Error al enviar - " + e.getMessage());
            }
        });
    }
}
