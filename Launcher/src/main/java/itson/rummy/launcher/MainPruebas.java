package itson.rummy.launcher;

import ensambladores.EnsambladorCliente;
import ensambladores.EnsambladorDominio;
import ensambladores.EnsambladorServidor;
import java.util.UUID;
import javax.swing.SwingUtilities;

public class MainPruebas {

    private static final String IP_LOCALHOST = "127.0.0.1";
    private static final int PUERTO_BROKER = 9999;
    private static final int PUERTO_DOMINIO = 9998;
    private static final int PUERTO_JUGADOR_1 = 9001;
    private static final int PUERTO_JUGADOR_2 = 9002;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación...");
        }));

        new Thread(() -> {
            try {
                System.out.println("[BROKER] Iniciando...");
                new EnsambladorServidor().iniciarBroker(PUERTO_BROKER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        esperar(1000);

        System.out.println("[DOMINIO] Iniciando servidor de juego...");
        EnsambladorDominio ensambladorDominio = new EnsambladorDominio();
        try {
            ensambladorDominio.iniciarJuego(IP_LOCALHOST, PUERTO_BROKER, PUERTO_DOMINIO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        esperar(1000);

        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        lanzarCliente(id1, PUERTO_JUGADOR_1);
        lanzarCliente(id2, PUERTO_JUGADOR_2);

        System.out.println("[MAIN] Sistema desplegado. Usa las ventanas para jugar.");
    }

    private static void lanzarCliente(String id, int puertoEscucha) {
        SwingUtilities.invokeLater(() -> {
            new EnsambladorCliente().iniciarAplicacion(
                    IP_LOCALHOST, PUERTO_BROKER, IP_LOCALHOST, puertoEscucha, id
            );
        });
    }

    private static void esperar(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
