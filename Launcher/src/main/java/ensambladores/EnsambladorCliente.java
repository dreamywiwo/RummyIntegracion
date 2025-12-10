package ensambladores;

import ClaseDispatcher.ColaDispatcher;
import ClaseDispatcher.Dispatcher;
import ClaseDispatcher.SocketOut;
import claseReceptor.ColaReceptor;
import claseReceptor.Receptor;
import claseReceptor.SocketIN;
import com.mycompany.conexioninterfaces.IDispatcher;
import itson.serializer.implementacion.JsonSerializer;

// Facade del Producer
import itson.producerjugador.facade.IProducerJugador;
import itson.producerjugador.facade.ProducerJugador;

// Traducer
import itson.traducerjugador.facade.TraducerJugador;
import itson.traducerjugador.mappers.EventMapper;

// Utils y DTOs
import itson.rummydtos.JugadorDTO;
import itson.rummypresentacion.utils.ListenerProxy;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;

// MVC 1: Configurar Partida
import itson.configurarpartida.controlador.ControladorConfigurarPartida;
import itson.configurarpartida.modelo.ModeloConfiguracion;
import itson.configurarpartida.vista.UI_ConfigurarPartida;
import itson.configurarpartida.vista.UI_MenuRummy;

// MVC 2: Registrar Jugador (Intermedio)
import itson.registrarjugador.controlador.ControladorRegistro;
import itson.registrarjugador.modelo.ModeloRegistro;
import itson.registrarjugador.vista.UI_Registro;

// MVC 3: Ejercer Turno (Juego)
import itson.ejercerturno.controlador.ControladorTurno;
import itson.ejercerturno.modelo.ModeloEjercerTurno;
import itson.ejercerturno.vista.UI_TurnoJugador;

public class EnsambladorCliente {

    /**
     * Construye toda la aplicación cliente, conecta los MVCs y arranca la red.
     */
    public void iniciarAplicacion(String ipBroker, int puertoBroker, String miIp, int miPuerto, String miId) {

        // ====================================================================
        // 1. CAPA DE INFRAESTRUCTURA DE RED (SOCKETS & SERIALIZADORES)
        // ====================================================================
        JsonSerializer jsonSerializer = new JsonSerializer();

        // --- SALIDA (Socket Out) ---
        SocketOut socketOut = new SocketOut();
        socketOut.start();
        ColaDispatcher colaDispatcher = new ColaDispatcher();
        colaDispatcher.attach(socketOut);
        IDispatcher dispatcher = new Dispatcher(colaDispatcher);

        // Facade Producer (Crea sus emitters internamente)
        IProducerJugador producer = new ProducerJugador(jsonSerializer, dispatcher, ipBroker, puertoBroker, miId);

        // ====================================================================
        // 2. PREPARACIÓN DE ENTRADA (LISTENER PROXY)
        // ====================================================================
        ListenerProxy listenerProxy = new ListenerProxy();

        EventMapper eventMapper = new EventMapper(jsonSerializer);
        eventMapper.setListener(listenerProxy);
        eventMapper.setJugadorId(miId);

        // ====================================================================
        // 3. SESIÓN COMPARTIDA Y DATOS INICIALES
        // ====================================================================
        // Valores por defecto
        String rutaAvatar = (miPuerto % 2 == 0) ? "/imageFish.png" : "/imageBun.png";
        String nombreJugador = "Jugador " + (miPuerto % 100);
        JugadorDTO perfil = new JugadorDTO(miId, nombreJugador, rutaAvatar);

        // Creamos la sesión para pasar dependencias entre controladores
        SesionCliente sesion = new SesionCliente(producer, perfil, listenerProxy);

        // ====================================================================
        // 4. CONSTRUCCIÓN MVC 3: EJERCER TURNO (DESTINO FINAL)
        // ====================================================================
        ModeloEjercerTurno modeloJuego = new ModeloEjercerTurno(producer);
        modeloJuego.setJugadorLocal(miId);

        ControladorTurno ctrlJuego = new ControladorTurno(modeloJuego, sesion);
        UI_TurnoJugador vistaJuego = new UI_TurnoJugador(ctrlJuego, perfil);

        modeloJuego.suscribir(vistaJuego);
        vistaJuego.setVisible(false);

        // ====================================================================
        // 5. CONSTRUCCIÓN MVC 2: REGISTRAR JUGADOR (INTERMEDIO)
        // ====================================================================
        ModeloRegistro modeloRegistro = new ModeloRegistro(producer);
        ControladorRegistro ctrlRegistro = new ControladorRegistro(modeloRegistro, sesion);
        
        // CONEXIÓN: Registro -> Juego
        ctrlRegistro.setSiguienteControlador(ctrlJuego);

        // CREACIÓN DE LA VISTA DE REGISTRO (Esto faltaba antes)
        UI_Registro vistaRegistro = new UI_Registro(ctrlRegistro);
        modeloRegistro.suscribir(vistaRegistro);
        vistaRegistro.setVisible(false);

        // ====================================================================
        // 6. CONSTRUCCIÓN MVC 1: CONFIGURAR PARTIDA (INICIO)
        // ====================================================================
        ModeloConfiguracion modeloConfig = new ModeloConfiguracion(producer);
        ControladorConfigurarPartida ctrlConfig = new ControladorConfigurarPartida(modeloConfig);

        // CONEXIÓN: Configurar -> Registro
        ctrlConfig.setSiguienteControlador(ctrlRegistro);

        UI_MenuRummy vistaMenu = new UI_MenuRummy(ctrlConfig);
        UI_ConfigurarPartida vistaConfig = new UI_ConfigurarPartida(ctrlConfig);

        modeloConfig.suscribir(vistaMenu);
        modeloConfig.suscribir(vistaConfig);

        // Configuración inicial del Proxy: Escuchar eventos de configuración
        listenerProxy.activarModoConfiguracion(modeloConfig);

        // ====================================================================
        // 7. ARRANCAR RED ENTRANTE (SOCKET IN)
        // ====================================================================
        TraducerJugador traducer = new TraducerJugador(jsonSerializer, eventMapper);
        Receptor receptor = new Receptor(traducer);
        ColaReceptor colaReceptor = new ColaReceptor();
        colaReceptor.attach(receptor);

        SocketIN socketIn = new SocketIN(miPuerto, colaReceptor);
        socketIn.start();

        System.out.println("Ensamblador: Cliente " + nombreJugador + " (" + miId + ") iniciado en " + miIp + ":" + miPuerto);

        // ====================================================================
        // 8. INICIO DEL FLUJO
        // ====================================================================
        // Enviamos registro al broker (Handshake)
        producer.registrarJugador(miId, miIp, miPuerto);
        System.out.println("Ensamblador: Solicitud de registro enviada.");

        // Mostramos el menú inicial
        modeloConfig.cambiarVista(TipoVista.MENU_PRINCIPAL);
    }
}