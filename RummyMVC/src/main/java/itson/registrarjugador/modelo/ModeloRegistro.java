package itson.registrarjugador.modelo;

import itson.producerjugador.facade.IProducerJugador;
import itson.rummydtos.JugadorDTO;
import itson.rummylistener.interfaces.IRegistrarListener;
import itson.rummypresentacion.utils.TipoVista;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeloRegistro implements IModeloRegistro, ISubjectRegistro, IRegistrarListener {

    private IProducerJugador producer;
    private List<IObserverRegistro> observers = new ArrayList<>();
    private TipoVista vistaActual;
    private String mensajeError;

    private JugadorDTO jugadorTemporal;
    private List<String> avataresDisponibles;
    private List<String> coloresSistema;
    
    private boolean registroExitoso = false;

    public ModeloRegistro(IProducerJugador producer) {
        this.producer = producer;
        this.jugadorTemporal = new JugadorDTO();
        cargarRecursos();
    }

    private void cargarRecursos() {
        this.avataresDisponibles = Arrays.asList("conejo.png", "pezglobo.png", "perro.png", "comadreja.png");
        this.coloresSistema = Arrays.asList("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#FFA500", "#800080");
    }

    public void cambiarVista(TipoVista vista) {
        this.vistaActual = vista;
        this.mensajeError = null; 
        notificarObservers();
    }

    public void guardarDatosTemporales(String nombre, String avatar, List<String> colores) {
        jugadorTemporal.setNombre(nombre);
        jugadorTemporal.setAvatarPath(avatar);
        jugadorTemporal.setColoresFichas(colores);
    }

    public void confirmarRegistroEnServidor(String miId) {
        try {
            producer.actualizarPerfil(
                    miId,
                    jugadorTemporal.getNombre(),
                    jugadorTemporal.getAvatarPath(),
                    jugadorTemporal.getColoresFichas()
            );
            System.out.println("[ModeloRegistro] Perfil enviado al servidor.");
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }

    // --- GETTERS ---
    @Override
    public JugadorDTO getJugadorTemporal() {
        return jugadorTemporal;
    }

    @Override
    public List<String> getAvataresDisponibles() {
        return avataresDisponibles;
    }

    @Override
    public List<String> getColoresSistema() {
        return coloresSistema;
    }

    @Override
    public TipoVista getVistaActual() {
        return vistaActual;
    }
    
    @Override
    public boolean isRegistroExitoso() {
        return registroExitoso;
    }
    
    @Override
    public String getMensajeError() {
        return mensajeError;
    }

    // --- OBSERVER ---
    @Override
    public void suscribir(IObserverRegistro observer) {
        observers.add(observer);
    }

    @Override
    public void notificarObservers() {
        for (IObserverRegistro o : observers) {
            o.update(this);
        }
    }

    @Override
    public void recibirConfirmacionRegistro() {
        this.registroExitoso = true;
        notificarObservers();
    }
    
    public void resetEstado() {
        this.registroExitoso = false;
    }

    @Override
    public void recibirError(String mensaje) {
        this.mensajeError = mensaje;
        notificarObservers();
    }

    
}
