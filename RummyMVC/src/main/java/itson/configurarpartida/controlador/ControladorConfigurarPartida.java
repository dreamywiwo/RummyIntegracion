/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.configurarpartida.controlador;

import itson.configurarpartida.modelo.ModeloConfiguracion;
import itson.ejercerturno.controlador.ControladorTurno;
import itson.rummypresentacion.utils.TipoVista;

/**
 *
 * @author Dana Chavez
 */
public class ControladorConfigurarPartida {

    private ModeloConfiguracion modelo;
    private ControladorTurno siguienteControlador;

    public ControladorConfigurarPartida(ModeloConfiguracion modelo) {
        this.modelo = modelo;
    }

    public void setSiguienteControlador(ControladorTurno siguienteControlador) {
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
        modelo.enviarConfiguracionPartida(max, comodines);
    }

    public void navegarAlJuego() {
        if (siguienteControlador != null) {
            modelo.resetEstado();
            modelo.cambiarVista(null); 
            siguienteControlador.iniciarJuego(); 
        }
    }

    public void unirseAPartida() {
        System.out.println("[CtrlConfig] Uniéndose a partida existente...");

        if (siguienteControlador != null) {
            modelo.cambiarVista(null);

            siguienteControlador.iniciarJuego();
        }
    }
}
