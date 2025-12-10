package itson.solicitarinicio.modelo;

import itson.producerjugador.facade.IProducerJugador;
import itson.rummydtos.JugadorDTO;
import itson.rummylistener.interfaces.ISalaListener;
import itson.rummypresentacion.utils.TipoVista;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo del MVC de Sala de Espera (Lobby).
 * @author Dana Chavez
 */
public class ModeloSalaEspera implements IModeloSalaEspera, ISubjectSalaEspera, ISalaListener {

    private IProducerJugador producer;
    private List<IObserverSalaEspera> observers;
    
    // ESTADO
    private List<JugadorDTO> jugadoresEnSala;
    private TipoVista vistaActual;
    private String miIdLocal; // ID propio para la UI

    public ModeloSalaEspera(IProducerJugador producer) {
        this.producer = producer;
        this.observers = new ArrayList<>();
        this.jugadoresEnSala = new ArrayList<>();
    }

    // --- SETTERS Y LÓGICA DE CONTROL ---

    public void setMiIdLocal(String id) {
        this.miIdLocal = id;
    }

    public void cambiarVista(TipoVista vista) {
        this.vistaActual = vista;
        notificarObservers();
    }

    /**
     * Método llamado por el Controlador cuando se pulsa "Comenzar".
     */
    public void solicitarInicioJuego() {
        try {
            if (producer != null) {
                // Asegúrate de tener este método en tu interfaz IProducerJugador
                // Si no lo tienes, deberás agregarlo (envía un evento SolicitarInicioEvent)
                // producer.solicitarInicioPartida(); 
                System.out.println("[ModeloSala] Solicitando inicio al servidor...");
                
                // TEMPORAL: Si aún no tienes el evento de red implementado, 
                // puedes simular que avanza (solo para probar la UI localmente)
                // setVistaActual(null); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pedirListaJugadoresAlServidor(String miId) {
        try {
            producer.solicitarInfoSala(miId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizarListaJugadores(List<JugadorDTO> listaActualizada) {
        this.jugadoresEnSala = new ArrayList<>(listaActualizada);
        notificarObservers();
    }

    public void agregarJugador(JugadorDTO jugador) {
        // Evitamos duplicados por si acaso
        boolean existe = jugadoresEnSala.stream()
                .anyMatch(j -> j.getId().equals(jugador.getId()));
        
        if (!existe) {
            jugadoresEnSala.add(jugador);
            notificarObservers();
        }
    }
    
    @Override
    public void recibirActualizacionSala(List<JugadorDTO> jugadores) {
        System.out.println("[ModeloSala] Recibida lista de " + jugadores.size() + " jugadores.");
        
        this.jugadoresEnSala = new ArrayList<>(jugadores); 
        notificarObservers(); 
    }

    // --- Getters para la Vista ---

    @Override
    public List<JugadorDTO> getJugadoresEnSala() {
        return jugadoresEnSala;
    }

    @Override
    public TipoVista getVistaActual() {
        return vistaActual;
    }

    @Override
    public String getMiId() {
        return miIdLocal;
    }

    // --- OBSERVER ---

    @Override
    public void suscribir(IObserverSalaEspera observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void notificarObservers() {
        for (IObserverSalaEspera observer : observers) {
            observer.update(this);
        }
    }


}