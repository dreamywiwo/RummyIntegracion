package itson.registrarjugador.vista;

import itson.registrarjugador.controlador.ControladorRegistro;
import itson.registrarjugador.modelo.IModeloRegistro;
import itson.registrarjugador.modelo.IObserverRegistro;
import itson.rummypresentacion.utils.TipoVista;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class UI_Registro extends JFrame implements IObserverRegistro {

    private ControladorRegistro controlador;
    private PanelRegistro panelFormulario;
    private PanelConfirmacion panelConfirmacion;
    private String ultimoMensajeError = null;

    public UI_Registro(ControladorRegistro controlador) {
        this.controlador = controlador;
        this.panelFormulario = new PanelRegistro(controlador);
        this.panelConfirmacion = new PanelConfirmacion(controlador);
        
        setTitle("Rummy - Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(panelFormulario);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void update(IModeloRegistro modelo) {
        SwingUtilities.invokeLater(() -> {

            if (modelo.isRegistroExitoso()) {
                this.setVisible(false);
                this.dispose();
                controlador.navegarAlJuego();
                return;
            }

            TipoVista vista = modelo.getVistaActual();

            if (vista == null) {
                this.setVisible(false);
                this.dispose();
                return;
            }

            if (vista == TipoVista.REGISTRAR_JUGADOR) {
                panelFormulario.cargarDatosPrevios(controlador.getModelo().getJugadorTemporal());

                setContentPane(panelFormulario);
                this.setVisible(true);
                this.revalidate();
                this.repaint();
            } else if (vista == TipoVista.CONFIRMACION_REGISTRO) {
                setContentPane(panelConfirmacion);
                this.setVisible(true);
                this.revalidate();
                this.repaint();
            } else {
                this.setVisible(false);
            }

            String mensajeError = modelo.getMensajeError();
            if (mensajeError != null
                    && !mensajeError.isBlank()
                    && !mensajeError.equals(ultimoMensajeError)) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        mensajeError,
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );

                ultimoMensajeError = mensajeError;
            }
        });
    }
}