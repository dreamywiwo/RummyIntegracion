package itson.registrarjugador.controlador;

import itson.ejercerturno.controlador.ControladorTurno;
import itson.registrarjugador.modelo.ModeloRegistro;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ControladorRegistro {

    private ModeloRegistro modelo;
    private ControladorTurno siguienteControlador;
    private SesionCliente sesion;

    public ControladorRegistro(ModeloRegistro modelo, SesionCliente sesion) {
        this.modelo = modelo;
        this.sesion = sesion;
    }

    public void setSiguienteControlador(ControladorTurno siguienteControlador) {
        this.siguienteControlador = siguienteControlador;
    }

    public ModeloRegistro getModelo() {
        return modelo;
    }

    public void iniciarRegistro() {
        sesion.getListenerProxy().activarModoRegistro(modelo);
        modelo.cambiarVista(TipoVista.REGISTRAR_JUGADOR);
    }

    public void irAConfirmacion(String nombre, String avatar, List<String> colores) {
        if (!validarFormatoNombre(nombre)) {
            mostrarError("Nombre inválido (3-12 caracteres alfanuméricos).");
            return;
        }
        
        modelo.guardarDatosTemporales(nombre, avatar, colores);
        modelo.cambiarVista(TipoVista.CONFIRMACION_REGISTRO);
    }

    public void volverAEdicion() {
        modelo.cambiarVista(TipoVista.REGISTRAR_JUGADOR);
    }

    public void confirmarYJugar() {
        try {
            var datos = modelo.getJugadorTemporal();
            String miId = sesion.getJugadorLocal().getId();

            System.out.println("[CtrlRegistro] Enviando solicitud al servidor...");

            modelo.resetEstado();
            modelo.confirmarRegistroEnServidor(miId); 

            
        } catch (Exception e) {
        
        }
    }

    public void navegarAlJuego() {
        if (siguienteControlador != null) {
            modelo.resetEstado();
            modelo.cambiarVista(null); 
            siguienteControlador.iniciarJuego();
        }
    }

    // --- VALIDACIONES ---
    public boolean validarFormatoNombre(String nombre) {
        return nombre != null && Pattern.matches("^[a-zA-Z0-9]{3,12}$", nombre);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }
}