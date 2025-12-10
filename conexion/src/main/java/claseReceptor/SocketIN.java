/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package claseReceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jrasc
 */
public class SocketIN {

    private ServerSocket server;
    private ExecutorService pool;
    private int puertoEscucha;

    private ColaReceptor colaReceptor;

    public SocketIN(int puerto, ColaReceptor cola) {
        this.puertoEscucha = puerto;
        this.colaReceptor = cola;
    }

    public void start() {
        pool = Executors.newCachedThreadPool();

        new Thread(() -> {
            try {
                server = new ServerSocket(puertoEscucha);
                System.out.println("Servidor escuchando en puerto " + puertoEscucha);

                while (server != null && !server.isClosed()) {
                    Socket clienteSocket = server.accept();
                    pool.submit(new ClienteHandler(clienteSocket, colaReceptor));
                }
            } catch (IOException e) {
                if (server != null && !server.isClosed()) {
                    System.err.println("Error en el ServerSocket - " + e.getMessage());
                } else {
                    System.err.println("El SocketIN se detuvo o no pudo iniciar en puerto " + puertoEscucha + ": " + e.getMessage());
                }
            }
        }).start();
    }

    public void close() {
        try {
            if (pool != null && !pool.isShutdown()) {
                pool.shutdown();
            }
            if (server != null && !server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar servidor - " + e.getMessage());
        }
    }

    private static class ClienteHandler implements Runnable {

        private Socket socket;
        private ColaReceptor colaReceptor;

        public ClienteHandler(Socket socket, ColaReceptor cola) {
            this.socket = socket;
            this.colaReceptor = cola;
        }

        @Override
        public void run() {
            String ip = "Desconocida";
            int port = 0;

            try {
                ip = socket.getInetAddress().getHostAddress();
                port = socket.getPort();
                System.out.println("Conexión recibida de " + ip + ":" + port);

                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String json;
                    while ((json = in.readLine()) != null) {
                        System.out.println("JSON recibido '" + json + "'");
                        colaReceptor.recibir(json, port, ip);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error de lectura - " + e.getMessage());
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    // Ignorar error al cerrar
                }
                System.out.println("Conexión cerrada con " + ip);
            }
        }
    }
}
