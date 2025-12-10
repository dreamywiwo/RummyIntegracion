/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itson.rummy.launcher;

import ensambladores.EnsambladorCliente;
import ensambladores.EnsambladorDominio;
import ensambladores.EnsambladorServidor;
import itson.dominiorummy.entidades.Jugador;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class MainPruebas {

    private static final String IP_LOCALHOST = "127.0.0.1";

    private static final int PUERTO_BROKER = 9999;
    private static final int PUERTO_DOMINIO = 9998;

    private static final int PUERTO_JUGADOR_1 = 9001;
    private static final int PUERTO_JUGADOR_2 = 9002;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación y liberando puertos...");
        }));
        
        // 1. Iniciar Broker
        new Thread(() -> {
            try {
                System.out.println("[BROKER] Iniciando...");
                EnsambladorServidor servidor = new EnsambladorServidor();
                servidor.iniciarBroker(PUERTO_BROKER);
            } catch (Exception e) {
                System.err.println("Error en Broker: " + e.getMessage());
            }
        }).start();

        esperar(1000);

        // 2. Crear Jugadores y guardar referencias para obtener sus ids
        List<Jugador> listaJugadores = new ArrayList<>();

        // Al hacer new Jugador("Nombre"), internamente se genera un UUID. 
        Jugador jugador1 = new Jugador("Jugador1");
        Jugador jugador2 = new Jugador("Jugador2");

        listaJugadores.add(jugador1);
        listaJugadores.add(jugador2);

        System.out.println("[DOMINIO] IDs Generados -> J1: " + jugador1.getId() + " | J2: " + jugador2.getId());

        // 3. Iniciar Dominio
        System.out.println("[DOMINIO] Conectando al Broker y configurando juego...");
        EnsambladorDominio ensambladorDominio = new EnsambladorDominio();
        try {
            ensambladorDominio.iniciarJuego(IP_LOCALHOST, PUERTO_BROKER, PUERTO_DOMINIO, listaJugadores);
        } catch (Exception e) {
            System.err.println("Error al configurar el Dominio: " + e.getMessage());
        }

        // 4. Lanzar Clientes USANDO LOS UUID
        lanzarCliente(jugador1.getId(), PUERTO_JUGADOR_1);
        lanzarCliente(jugador2.getId(), PUERTO_JUGADOR_2);

        esperar(3000);

        System.out.println("[MAIN] Clientes listos. Indicando al Dominio que comience la partida...");
        ensambladorDominio.comenzarPartida();
    }

    private static void lanzarCliente(String id, int puertoEscucha) {
        SwingUtilities.invokeLater(() -> {
            EnsambladorCliente ensamblador = new EnsambladorCliente();

            ensamblador.iniciarAplicacion(
                    IP_LOCALHOST, // IP Broker
                    PUERTO_BROKER, // Puerto Broker
                    IP_LOCALHOST, // Mi IP
                    puertoEscucha, // Mi Puerto
                    id // Mi ID
            );
        });
    }

    private static void esperar(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
