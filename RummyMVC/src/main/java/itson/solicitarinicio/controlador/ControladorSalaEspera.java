/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.solicitarinicio.controlador;

import itson.ejercerturno.controlador.ControladorTurno;
import itson.rummydtos.JugadorDTO;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;
import itson.solicitarinicio.modelo.ModeloSalaEspera;
import java.util.List;

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
        System.out.println("[CtrlSala] Enviando estado LISTO al servidor...");
        String miId = sesion.getJugadorLocal().getId();
        modelo.enviarEstoyListo(miId);
    }
    
    public void irAlTablero() {
        if (siguienteControlador != null) {

            List<JugadorDTO> jugadoresListos = modelo.getJugadoresEnSala();
            
            siguienteControlador.configurarJugadoresIniciales(jugadoresListos);
            
            siguienteControlador.iniciarJuego();
        }
    }
}