package itson.producerdominio.facade;

import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import itson.rummydtos.TableroDTO;
import java.util.List;

public interface IProducerDominio {

    // Evento público: tablero para todos
    public abstract void actualizarTablero(TableroDTO snapshotTablero);

    // Evento privado: mano solo para el jugador dueño
    public abstract void actualizarManoJugador(String jugadorId, List<FichaDTO> snapshotMano);

    // Evento público: número de fichas restantes en la sopa
    public abstract void actualizarSopa(int numFichasSopa);

    // Público: turno actual visible para todos
    public abstract void actualizarTurno(String nuevoTurno);

    // Público
    public abstract void mostrarError(String jugadorId, String mensajeError);

    // Público: juego terminado
    public abstract void juegoTerminado(JugadorDTO jugador);

    // Público: enviar solo cantidad de fichas del jugador
    public abstract void enviarCantidadFichasPublico(String jugadorId, int size);
    
    // Evento público: grupo invalido
    public abstract void highlightInvalidGroup(String jugadorId, String grupoId);
    
    public abstract void registrarDominio(String miId, String ipCliente, int miPuertoDeEscucha);

    public void enviarPartidaCreada();

}
