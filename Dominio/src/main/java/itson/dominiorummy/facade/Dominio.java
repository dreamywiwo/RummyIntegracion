package itson.dominiorummy.facade;

import itson.dominiorummy.entidades.Ficha;
import itson.dominiorummy.entidades.FichaPlaced;
import itson.dominiorummy.entidades.Grupo;
import itson.dominiorummy.entidades.GrupoNumero;
import itson.dominiorummy.entidades.GrupoSecuencia;
import itson.dominiorummy.entidades.Jugador;
import itson.dominiorummy.entidades.Partida;
import itson.dominiorummy.entidades.Partida.EstadoPartida;
import itson.dominiorummy.entidades.Sopa;
import itson.dominiorummy.entidades.Tablero;
import itson.dominiorummy.entidades.Turno;
import itson.dominiorummy.mappers.FichaMapper;
import itson.dominiorummy.mappers.TableroMapper;
import itson.producerdominio.facade.IProducerDominio;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.JugadorDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Dominio implements IDominio {

    private final Tablero tablero;
    private final IProducerDominio producer;
    private final Turno turno;
    private final Sopa sopa;
    private final List<Ficha> fichas;
    private final Map<String, Jugador> jugadores;
    private Partida partida;

    // --- VARIABLES PARA EL SNAPSHOT ---
    private Tablero tableroBackup;
    private List<Ficha> manoJugadorBackup;

    private static final Logger LOG = Logger.getLogger(Dominio.class.getName());

    public Dominio(Tablero tablero, IProducerDominio producer, Turno turno, Sopa sopa, List<Ficha> fichas) {
        this.fichas = fichas;
        this.tablero = tablero;
        this.producer = producer;
        this.turno = turno;
        this.jugadores = new HashMap<>();
        this.sopa = sopa;
    }

    // ----------------------- CASO DE USO CONFIGURAR PARTIDA -------------------------------
    @Override
    public void configurarPartida(int maxNumFichas, int cantidadComodines) {
        if (validarExistenciaPartida()) {
            producer.mostrarError("Servidor", "Ya existe una partida en curso o configurada.");
            return;
        }

        if (!validarParametrosConfiguracion(maxNumFichas, cantidadComodines)) {
            producer.mostrarError("Servidor", "Parámetros inválidos: Verifique fichas (min 2) y comodines.");
            return;
        }

        try {
            this.partida = new Partida();
            this.partida.configurar(maxNumFichas, cantidadComodines);
            this.partida.marcarDisponible();

            producer.enviarPartidaCreada();

            System.out.println("[DOMINIO] Partida creada y en estado: " + this.partida.getEstado());

        } catch (Exception e) {
            this.partida = null;
            producer.mostrarError("Servidor", "Error interno al crear la partida: " + e.getMessage());
        }
    }

    public void procesarSolicitudEstado(String jugadorId) {
        System.out.println("[DOMINIO] Cliente listo. Enviando estado a: " + jugadorId);

        // 1. Obtener al jugador usando el nuevo método
        Jugador j = getJugadorById(jugadorId);

        if (j != null) {
            // Re-enviar Mano de ese jugador
            producer.actualizarManoJugador(
                    jugadorId,
                    FichaMapper.toDTO(j.getMano().getFichas())
            );
            System.out.println("   -> Mano reenviada (" + j.getMano().getFichas().size() + " fichas)");
        } else {
            System.err.println("   -> Error: Jugador no encontrado con ID: " + jugadorId);
        }

        // 2. Re-enviar Tablero (Estado Global)
        producer.actualizarTablero(TableroMapper.toDTO(tablero));

        // 3. Re-enviar Sopa (Estado Global)
        producer.actualizarSopa(sopa.getFichasRestantes());

        // 4. Re-enviar Turno Actual
        Jugador turnoActual = turno.getJugadorActual();
        if (turnoActual != null) {
            producer.actualizarTurno(turnoActual.getId());
        }
    }

    private boolean validarExistenciaPartida() {
        return this.partida != null && this.partida.getEstado() != EstadoPartida.TERMINADA;
    }

    private boolean validarParametrosConfiguracion(int fichas, int comodines) {
        if (fichas < 10 || fichas > 13) {
            return false;
        }
        if (comodines < 2 || comodines > 8) {
            return false;
        }
        return true;
    }

    // ----------------------- CASO DE USO EJERCER TURNO -----------------------------
    // INICIAR PARTIDA
    @Override
    public void iniciarPartida() {
        try {
            for (Jugador j : this.jugadores.values()) {
                producer.actualizarManoJugador(
                        j.getId(),
                        FichaMapper.toDTO(j.getMano().getFichas())
                );
                System.out.println("[DOMINIO] Mano inicial enviada a: " + j.getNombre());
            }

            producer.actualizarTablero(TableroMapper.toDTO(tablero));
            producer.actualizarSopa(sopa.getFichasRestantes());

            Jugador jugadorEnTurno = turno.getJugadorActual();
            if (jugadorEnTurno == null) {
                LOG.warning("No hay jugador actual definido al iniciar la partida.");
                return;
            }

            String jugadorIdTurno = jugadorEnTurno.getId();
            producer.actualizarTurno(jugadorIdTurno);

            guardarBackupInicioTurno();

        } catch (Exception e) {
            LOG.severe("Error al iniciar la partida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarBackupInicioTurno() {
        Jugador jugador = turno.getJugadorActual();
        if (jugador != null) {
            this.tableroBackup = tablero.clonar();
            this.manoJugadorBackup = new ArrayList<>(jugador.getMano().getFichas());
        }
    }

    private void restaurarBackup() {
        if (this.tableroBackup == null || this.manoJugadorBackup == null) {
            LOG.warning("No hay backup para restaurar.");
            return;
        }

        Jugador jugador = turno.getJugadorActual();

        this.tablero.restaurarEstado(this.tableroBackup);

        jugador.getMano().setFichas(new ArrayList<>(this.manoJugadorBackup));

        producer.actualizarTablero(TableroMapper.toDTO(this.tablero));
        producer.actualizarManoJugador(jugador.getId(), FichaMapper.toDTO(jugador.getMano().getFichas()));
    }

    // CREAR GRUPO
    @Override
    public void crearGrupo(List<FichaDTO> fichasDTO) {
        Jugador jugador = turno.getJugadorActual();
        String jugadorId = jugador.getId();
        int turnoActual = turno.getNumeroTurno();

        List<FichaPlaced> fichasAInsertar = new ArrayList<>();
        List<String> idsSacadosDeMano = new ArrayList<>();

        if (!recolectarFichas(fichasDTO, jugador, turnoActual, fichasAInsertar, idsSacadosDeMano)) {
            return;
        }

        Grupo grupoNuevo = tablero.crearGrupoDesdeFichasPlaced(fichasAInsertar);

        if (grupoNuevo == null || !grupoNuevo.validarReglas()) {
            rollbackCreacion(fichasAInsertar, idsSacadosDeMano, jugador);
            producer.mostrarError(jugadorId, "Movimiento inválido: Las fichas no forman una estructura correcta.");
            producer.actualizarTablero(TableroMapper.toDTO(tablero));
            producer.actualizarManoJugador(jugadorId, FichaMapper.toDTO(jugador.getMano().getFichas()));
            return;
        }

        tablero.agregarGrupo(grupoNuevo);
        producer.actualizarTablero(TableroMapper.toDTO(tablero));
        producer.actualizarManoJugador(jugadorId, FichaMapper.toDTO(jugador.getMano().getFichas()));
        producer.enviarCantidadFichasPublico(jugadorId, jugador.getMano().getFichas().size());
    }

    // ACTUALIZAR GRUPO
    @Override
    public void actualizarGrupo(String grupoId, List<FichaDTO> fichasEntrantesDTO) {
        Jugador jugador = turno.getJugadorActual();
        String jugadorId = jugador.getId();
        int turnoActual = turno.getNumeroTurno();

        List<FichaPlaced> fichasExistentes = tablero.obtenerFichasDeGrupo(grupoId);

        Set<String> idsExistentes = fichasExistentes.stream()
                .map(fp -> fp.getFicha().getId())
                .collect(Collectors.toSet());

        List<FichaDTO> fichasRealmenteNuevas = new ArrayList<>();

        for (FichaDTO dto : fichasEntrantesDTO) {
            if (!idsExistentes.contains(dto.getId())) {
                fichasRealmenteNuevas.add(dto);
            }
        }

        if (fichasRealmenteNuevas.isEmpty()) {
            return;
        }

        List<FichaPlaced> combinacionPrueba = fichasExistentes.stream()
                .map(FichaPlaced::clonar)
                .collect(Collectors.toList());

        List<FichaPlaced> fichasNuevasInsertar = new ArrayList<>();
        List<String> idsSacadosDeMano = new ArrayList<>();

        if (!recolectarFichas(fichasRealmenteNuevas, jugador, turnoActual, fichasNuevasInsertar, idsSacadosDeMano)) {
            return;
        }

        combinacionPrueba.addAll(fichasNuevasInsertar);

        Grupo grupoResultante = tablero.crearGrupoDesdeFichasPlaced(combinacionPrueba);
        boolean esValido = (grupoResultante != null && grupoResultante.validarReglas());

        if (esValido) {
            Grupo grupoFinal = forzarIdGrupo(grupoResultante, grupoId);
            tablero.removerGrupo(grupoId);
            tablero.agregarGrupo(grupoFinal);
        } else {
            producer.mostrarError(jugadorId, "La ficha no encaja. Se creó un nuevo grupo.");
            Grupo grupoRebote = tablero.crearGrupoDesdeFichasPlaced(fichasNuevasInsertar);
            if (grupoRebote != null) {
                tablero.agregarGrupo(grupoRebote);
            }
        }

        if (tablero.grupoEstaVacio(grupoId)) {
            tablero.removerGrupo(grupoId);
        }

        producer.actualizarTablero(TableroMapper.toDTO(tablero));
        producer.actualizarManoJugador(jugadorId, FichaMapper.toDTO(jugador.getMano().getFichas()));
        producer.enviarCantidadFichasPublico(jugadorId, jugador.getMano().getFichas().size());
    }

    private boolean recolectarFichas(List<FichaDTO> dtos, Jugador jugador, int turnoActual,
            List<FichaPlaced> destino, List<String> idsMano) {
        for (FichaDTO dto : dtos) {
            String fid = dto.getId();
            if (jugador.getMano().tieneFicha(fid)) {
                Ficha f = jugador.getMano().quitarFicha(fid);
                idsMano.add(fid);
                destino.add(new FichaPlaced(f, jugador.getId(), turnoActual));
                continue;
            }
            FichaPlaced fp = tablero.buscarFichaPlacedGlobal(fid);
            if (fp != null) {
                tablero.removerFichaGlobal(fid);
                destino.add(fp);
                continue;
            }
            producer.mostrarError(jugador.getId(), "Ficha no encontrada: " + fid);
            rollbackCreacion(destino, idsMano, jugador);
            return false;
        }
        return true;
    }

    private void rollbackCreacion(List<FichaPlaced> fichas, List<String> idsMano, Jugador jugador) {
        for (FichaPlaced fp : fichas) {
            if (idsMano.contains(fp.getFicha().getId())) {
                jugador.getMano().agregarFicha(fp.getFicha());
            } else {
                tablero.restaurarFicha(fp);
            }
        }
    }

    private Grupo forzarIdGrupo(Grupo g, String id) {
        if (g instanceof GrupoNumero) {
            return new GrupoNumero(id, g.getFichas());
        }
        return new GrupoSecuencia(id, g.getFichas());
    }

    // TOMAR FICHA
    @Override
    public void tomarFicha() {
        Jugador jugador = turno.getJugadorActual();
        String jugadorId = jugador.getId();

        if (jugador == null) {
            producer.mostrarError(jugadorId, "Jugador no encontrado.");
            return;
        }

        restaurarBackup();

        Ficha ficha = sopa.tomarFicha();
        if (ficha != null) {
            jugador.getMano().agregarFicha(ficha);
            producer.actualizarSopa(sopa.getFichasRestantes());
        } else {
            producer.mostrarError(jugadorId, "La sopa está vacía.");
        }

        jugador.setHaTomadoFicha(true);

        List<Ficha> fichasJugador = jugador.getMano().getFichas();
        producer.actualizarManoJugador(jugadorId, FichaMapper.toDTO(fichasJugador));
        producer.actualizarTablero(TableroMapper.toDTO(tablero));
        producer.enviarCantidadFichasPublico(jugadorId, fichasJugador.size());

        terminarTurnoInternal(true);
    }

    // AGREGAR JUGADOR
    @Override
    public void agregarJugador(Jugador jugador) {
        jugadores.put(jugador.getId(), jugador);
    }

    public Jugador getJugadorById(String id) {
        if (this.jugadores != null) {
            for (Jugador j : this.jugadores.values()) { 
                if (j.getId().equals(id)) {
                    return jugadores.get(id);
                }
            }
        }
        return null; 
    }

    // TERMINAR TURNO (Público)
    @Override
    public void terminarTurno() {
        terminarTurnoInternal(false);
    }

    private void terminarTurnoInternal(boolean terminacionForzada) {
        Jugador jugador = turno.getJugadorActual();
        String jugadorId = jugador.getId();
        int numeroTurnoActual = turno.getNumeroTurno();

        if (jugador == null) {
            producer.mostrarError(jugadorId, "Jugador actual no definido.");
            return;
        }

        try {
            Jugador jugadorActual = jugadores.get(jugadorId);

            if (!terminacionForzada) {

                boolean hayGruposInvalidos = false;

                for (Grupo grupo : tablero.getGruposEnMesa()) {
                    boolean reglaEstructural = grupo.validarReglas();
                    boolean reglaTamano = grupo.cumpleTamanoMinimo();

                    if (!reglaEstructural || !reglaTamano) {
                        producer.highlightInvalidGroup(jugadorId, grupo.getId());
                        hayGruposInvalidos = true;
                    }
                }

                if (hayGruposInvalidos) {
                    producer.mostrarError(jugadorId, "Tablero inválido: Revisa los grupos marcados en rojo.");
                    return;
                }

                if (jugadorActual.getMano().getFichas().size() >= manoJugadorBackup.size()) {
                    producer.mostrarError(jugadorId, "Debes bajar fichas o tomar una de la sopa.");
                    return;
                }

                int puntosDeBajada = 0;
                boolean huboMovimientoEnTablero = false;

                for (Grupo grupo : tablero.getGruposEnMesa()) {
                    if (!jugador.yaBajo30() && grupo.contieneFichasAntiguas(numeroTurnoActual)) {
                        boolean tieneNuevas = grupo.getFichas().stream()
                                .anyMatch(fp -> fp.getPlacedInTurn() == numeroTurnoActual);
                        if (tieneNuevas) {
                            producer.mostrarError(jugadorId, "No puedes manipular grupos ajenos hasta bajar tus 30 pts.");
                            return;
                        }
                    }

                    if (grupo.fueCreadoEnTurno(numeroTurnoActual, jugadorId)) {
                        puntosDeBajada += grupo.calcularPuntos();
                        huboMovimientoEnTablero = true;
                    } else {
                        boolean tieneNuevas = grupo.getFichas().stream()
                                .anyMatch(fp -> fp.getPlacedInTurn() == numeroTurnoActual && fp.getPlacedBy().equals(jugadorId));
                        if (tieneNuevas) {
                            huboMovimientoEnTablero = true;
                        }
                    }
                }

                if (!jugador.yaBajo30()) {
                    if (huboMovimientoEnTablero && puntosDeBajada < 30) {
                        producer.mostrarError(jugadorId, "Primera bajada insuficiente (" + puntosDeBajada + "/30 pts).");
                        return;
                    }
                    if (huboMovimientoEnTablero && puntosDeBajada >= 30) {
                        jugador.marcarPrimerBajada30Completada();
                    }
                }
            }

            tablero.marcarFichasConfirmadas(jugadorId);

            this.tableroBackup = null;
            this.manoJugadorBackup = null;

            if (jugadorActual.getMano().getFichas().isEmpty()) {
                JugadorDTO ganadorDTO = new JugadorDTO();
                ganadorDTO.setId(jugadorActual.getId());
                ganadorDTO.setNombre(jugadorActual.getNombre());
                ganadorDTO.setAvatarPath("");
                producer.juegoTerminado(ganadorDTO);
                return;
            }

            Jugador siguienteJugador = turno.avanzarTurno();
            if (siguienteJugador == null) {
                producer.mostrarError(jugadorId, "No se pudo determinar el siguiente jugador.");
                return;
            }

            guardarBackupInicioTurno();

            producer.actualizarTablero(TableroMapper.toDTO(tablero));
            
            producer.actualizarManoJugador(
                jugadorId, 
                FichaMapper.toDTO(jugadorActual.getMano().getFichas())
            );

            producer.actualizarManoJugador(
                siguienteJugador.getId(), 
                FichaMapper.toDTO(siguienteJugador.getMano().getFichas())
            );
            
            producer.actualizarTurno(siguienteJugador.getId());
            

        } catch (Exception e) {
            LOG.severe("Error al terminar turno: " + e.getMessage());
            e.printStackTrace();
            producer.mostrarError(jugadorId, "Ocurrió un error inesperado.");
        }
    }

    // DEVOLVER FICHA A MANO 
    @Override
    public void devolverFichaAMano(String grupoId, String fichaId) {
        Jugador jugador = turno.getJugadorActual();
        String jugadorId = jugador.getId();

        if (jugador == null) {
            return;
        }

        boolean eraMia = manoJugadorBackup.stream()
                .anyMatch(f -> f.getId().equals(fichaId));

        if (!eraMia) {
            producer.mostrarError(jugadorId, "No puedes llevarte a la mano una ficha que ya estaba en la mesa.");
            producer.actualizarTablero(TableroMapper.toDTO(tablero));
            return;
        }

        Ficha fichaRecuperada = tablero.quitarFichaDeGrupo(grupoId, fichaId);

        if (fichaRecuperada != null) {
            jugador.getMano().agregarFicha(fichaRecuperada);

            if (tablero.grupoEstaVacio(grupoId)) {
                tablero.removerGrupo(grupoId);
            }

            producer.actualizarTablero(TableroMapper.toDTO(tablero));
            producer.actualizarManoJugador(jugadorId, FichaMapper.toDTO(jugador.getMano().getFichas()));
            producer.enviarCantidadFichasPublico(jugadorId, jugador.getMano().getFichas().size());

        } else {
            producer.mostrarError(jugadorId, "La ficha ya no se encuentra en ese grupo.");
            producer.actualizarTablero(TableroMapper.toDTO(tablero));
        }
    }
}
