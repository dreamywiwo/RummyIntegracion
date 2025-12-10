package itson.ejercerturno.modelo;

import itson.producerjugador.facade.IProducerJugador;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.GrupoDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummydtos.TableroDTO;
import itson.rummylistener.interfaces.ITurnoListener;
import itson.rummypresentacion.utils.TipoVista;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Modelo para la lógica de juego (MVC Ejercer Turno).
 * @author Dana ChavezW
 */
public class ModeloEjercerTurno implements IModeloEjercerTurno, ISubject, ITurnoListener {

    private IProducerJugador producer;
    private List<IObserver> observers;
    
    private TipoVista vistaActual = TipoVista.TABLERO_JUEGO;

    private String idJugadorLocal;
    
    private List<GrupoDTO> gruposEnTablero;
    private List<FichaDTO> fichasMano;
    
    private List<JugadorDTO> todosLosJugadores; 
    private Map<String, Integer> fichasOponentes; 
    
    // Estado del Juego
    private int fichasEnPozo = 0;
    private String turnoActual;
    private JugadorDTO jugadorGanador;
    private boolean juegoTerminado = false;
    
    // Estado de Acciones / Errores
    private String ultimaAccion;
    private boolean accionValida;
    private String mensajeError;
    private String grupoInvalidoId;

    public ModeloEjercerTurno(IProducerJugador producer) {
        this.producer = producer;
        this.observers = new ArrayList<>();
        
        // INICIALIZACIÓN SEGURA DE LISTAS
        this.gruposEnTablero = new ArrayList<>();
        this.fichasMano = new ArrayList<>();
        this.todosLosJugadores = new ArrayList<>();
        this.fichasOponentes = new HashMap<>();
    }

    // --- MÉTODOS DE CONTROL (ACCIONES) ---

    public void setJugadorLocal(String idJugador) {
        this.idJugadorLocal = idJugador;
    }

    public void cambiarVista(TipoVista nuevaVista) {
        this.vistaActual = nuevaVista;
        this.mensajeError = null;
        notificarObservers();
    }

    public void crearGrupo(List<FichaDTO> fichas) {
        producer.crearGrupo(fichas);
    }

    public void actualizarGrupo(String idGrupo, List<FichaDTO> fichas) {
        producer.actualizarGrupo(idGrupo, fichas);
    }

    public void terminarTurno() {
        producer.terminarTurno();
    }

    public void tomarFicha() {
        producer.tomarFicha();
    }

    public void devolverFicha(String grupoId, String fichaId) {
        producer.devolverFicha(grupoId, fichaId);
    }
    
    public void solicitarSincronizacion() {
        if (idJugadorLocal != null) {
            producer.solicitarEstadoJuego(this.idJugadorLocal);
        }
    }

    // --- IMPLEMENTACIÓN IModeloEjercerTurno (GETTERS SEGUROS) ---

    @Override
    public List<GrupoDTO> getGruposEnTablero() {
        if (gruposEnTablero == null) return new ArrayList<>();
        synchronized (this) {
            return new ArrayList<>(gruposEnTablero);
        }
    }

    @Override
    public JugadorDTO getJugadorActual() {
        // Busca en la lista completa al jugador local
        if (todosLosJugadores != null && idJugadorLocal != null) {
            return todosLosJugadores.stream()
                    .filter(j -> j.getId().equals(idJugadorLocal))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public List<FichaDTO> getFichasMano() {
        if (fichasMano == null) return new ArrayList<>();
        return new ArrayList<>(this.fichasMano);
    }

    @Override
    public List<JugadorDTO> getOtrosJugadores() {
        if (todosLosJugadores == null) {
            return new ArrayList<>();
        }
        // Filtramos para devolver todos MENOS yo
        if (idJugadorLocal != null) {
            return todosLosJugadores.stream()
                    .filter(j -> !j.getId().equals(idJugadorLocal))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(todosLosJugadores);
    }

    @Override
    public Map<String, Integer> getMapaFichasOponentes() {
        if (fichasOponentes == null) return new HashMap<>();
        return new HashMap<>(fichasOponentes);
    }

    @Override
    public int getFichasEnPozo() {
        return fichasEnPozo;
    }

    @Override
    public String getTurnoActual() {
        return turnoActual;
    }

    @Override
    public boolean isPartidaTerminada() {
        return juegoTerminado;
    }

    @Override
    public JugadorDTO getGanador() {
        return jugadorGanador;
    }

    @Override
    public String getUltimaAccion() {
        return ultimaAccion;
    }

    @Override
    public boolean isAccionValida() {
        return accionValida;
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }

    @Override
    public boolean juegoTerminado() {
        return juegoTerminado;
    }

    @Override
    public String getJugadorGanadorId() {
        return (jugadorGanador != null) ? jugadorGanador.getId() : null;
    }

    @Override
    public String getGrupoInvalidoId() {
        return this.grupoInvalidoId;
    }

    @Override
    public int getCantidadFichasSopa() {
        return fichasEnPozo;
    }

    @Override
    public String getJugadorActivoId() {
        return turnoActual;
    }
    
    @Override
    public TipoVista getVistaActual(){
        return vistaActual;
    }

    @Override
    public boolean esTurnoDe(String jugadorID) {
        return turnoActual != null && turnoActual.equals(jugadorID);
    }

    public void actualizarListaJugadores(List<JugadorDTO> lista) {
        if (lista != null) {
            this.todosLosJugadores = new ArrayList<>(lista);
            
            for (JugadorDTO j : lista) {
                if (!fichasOponentes.containsKey(j.getId())) {
                    fichasOponentes.put(j.getId(), 14); 
                }
            }
            notificarObservers();
        }
    }

    @Override
    public void terminoTurno(String jugadorActivoId) {
        this.turnoActual = jugadorActivoId;
        notificarObservers();
    }

    @Override
    public void recibirTablero(TableroDTO tableroDTO) {
        if (tableroDTO != null && tableroDTO.getGrupos() != null) {
            this.gruposEnTablero = new ArrayList<>(tableroDTO.getGrupos());
            this.grupoInvalidoId = null;
            notificarObservers();
        }
    }

    @Override
    public void recibirMano(List<FichaDTO> mano) {
        if (mano != null) {
            this.fichasMano = new ArrayList<>(mano); // Copia defensiva
        }
        notificarObservers();
    }

    @Override
    public void recibirSopa(int cantidad) {
        this.fichasEnPozo = cantidad;
        notificarObservers();
    }

    @Override
    public void recibirError(String mensaje) {
        this.mensajeError = mensaje;
        notificarObservers();
    }

    @Override
    public void marcarJuegoTerminado(JugadorDTO ganador) {
        this.juegoTerminado = true;
        this.jugadorGanador = ganador;
        notificarObservers();
    }

    @Override
    public void resaltarGrupoInvalido(String grupoId) {
        this.grupoInvalidoId = grupoId;
        notificarObservers();
    }

    @Override
    public void actualizarFichasOponente(String jugadorId, int size) {
        if (fichasOponentes != null) {
            fichasOponentes.put(jugadorId, size);
            notificarObservers();
        }
    }

    // --- OBSERVER PATTERN ---

    @Override
    public void suscribir(IObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void notificar(IObserver observer) {
        observer.update(this);
    }

    @Override
    public void notificarObservers() {
        for (IObserver observer : observers) {
            observer.update(this);
        }
    }
}