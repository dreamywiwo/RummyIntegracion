/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.configurarpartida.controlador;

import itson.configurarpartida.modelo.ModeloConfiguracion;
import itson.registrarjugador.controlador.ControladorRegistro;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;

/**
 *
 * @author Dana Chavez
 */
public class ControladorConfigurarPartida {

    private ModeloConfiguracion modelo;
    private ControladorRegistro siguienteControlador;
    private SesionCliente sesion;

    public ControladorConfigurarPartida(ModeloConfiguracion modelo, SesionCliente sesion) {
        this.modelo = modelo;
        this.sesion = sesion;
    }

    public void setSiguienteControlador(ControladorRegistro siguienteControlador) {
        this.siguienteControlador = siguienteControlador;
    }

    // Navegación
    public void irAConfigurarPartida() {
        modelo.cambiarVista(TipoVista.CONFIGURAR_PARTIDA);
    }

    public void volverAlMenu() {
        modelo.volverAlMenu();
    }

    // Acción de Negocio 
    public void confirmarConfiguracion(int max, int comodines) {
        modelo.resetEstado(); 
        String miId = sesion.getJugadorLocal().getId();
        modelo.enviarConfiguracionPartida(miId, max, comodines);
    }

    public void navegarSiguiente() {
        if (siguienteControlador != null) {
            modelo.resetEstado();
            modelo.cambiarVista(null); 
            siguienteControlador.iniciarRegistro(); 
        }
    }

    public void unirseAPartida() {
        
        String miId = sesion.getJugadorLocal().getId();
        modelo.solicitarUnirse(miId);
    }
}
