/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.solicitarinicio.controlador;

import itson.ejercerturno.controlador.ControladorTurno;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;
import itson.solicitarinicio.modelo.ModeloSalaEspera;

/**
 *
 * @author Dana Chavez
 */
public class ControladorSalaEspera {

    private ModeloSalaEspera modelo;
    private SesionCliente sesion;
    private ControladorTurno siguienteControlador; 

    public ControladorSalaEspera(ModeloSalaEspera modelo, SesionCliente sesion) {
        this.modelo = modelo;
        this.sesion = sesion;
    }

    public void setSiguienteControlador(ControladorTurno siguiente) {
        this.siguienteControlador = siguiente;
    }

    public void iniciarSala() {
        sesion.getListenerProxy().activarModoSala(modelo); 
        
        modelo.cambiarVista(TipoVista.SALA_ESPERA);

        modelo.agregarJugador(sesion.getJugadorLocal());
        
        String miId = sesion.getJugadorLocal().getId();
        modelo.pedirListaJugadoresAlServidor(miId);
    }

    public void clickComenzarPartida() {
        // Lógica para pedir inicio al server
        System.out.println("Solicitando inicio de partida...");
        modelo.solicitarInicioJuego();
    }
    
    // Este método se llamará cuando el Server diga "Juego Iniciado"
    public void irAlTablero() {
        modelo.cambiarVista(null); // Ocultar sala
        if (siguienteControlador != null) {
            siguienteControlador.iniciarJuego();
        }
    }
}
