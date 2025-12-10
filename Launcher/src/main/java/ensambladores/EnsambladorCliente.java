package ensambladores;
import ClaseDispatcher.ColaDispatcher;
import ClaseDispatcher.Dispatcher;
import ClaseDispatcher.SocketOut;
import claseReceptor.ColaReceptor;
import claseReceptor.Receptor;
import claseReceptor.SocketIN;
import com.mycompany.conexioninterfaces.IDispatcher;
import itson.serializer.implementacion.JsonSerializer;
import itson.producerjugador.emitters.ConfigurarPartidaEmitter;
import itson.producerjugador.emitters.InicializarJuegoEmitter;
import itson.producerjugador.emitters.JugarTurnoEmitter;
import itson.producerjugador.facade.IProducerJugador;
import itson.producerjugador.facade.ProducerJugador;
import itson.traducerjugador.facade.TraducerJugador;
import itson.traducerjugador.mappers.EventMapper;

import itson.rummydtos.JugadorDTO;
import itson.rummypresentacion.utils.ListenerProxy;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;

import itson.configurarpartida.controlador.ControladorConfigurarPartida;
import itson.configurarpartida.modelo.ModeloConfiguracion;
import itson.configurarpartida.vista.UI_ConfigurarPartida;
import itson.configurarpartida.vista.UI_MenuRummy;

import itson.ejercerturno.controlador.ControladorTurno;
import itson.ejercerturno.modelo.ModeloEjercerTurno;
import itson.ejercerturno.vista.UI_TurnoJugador;

public class EnsambladorCliente {

    public void iniciarAplicacion(String ipBroker, int puertoBroker, String miIp, int miPuerto, String miId) {

        // 1. CAPA DE INFRAESTRUCTURA DE RED
        JsonSerializer jsonSerializer = new JsonSerializer();

        // --- SALIDA (Socket Out) ---
        SocketOut socketOut = new SocketOut();
        socketOut.start();
        ColaDispatcher colaDispatcher = new ColaDispatcher();
        colaDispatcher.attach(socketOut);
        IDispatcher dispatcher = new Dispatcher(colaDispatcher);

        // Facade Producer (Crea internamente sus Emitters)
        IProducerJugador producer = new ProducerJugador(jsonSerializer, dispatcher, ipBroker, puertoBroker, miId);

        // 2. PREPARACIÓN DE ENTRADA (LISTENER PROXY)
        ListenerProxy listenerProxy = new ListenerProxy();

        EventMapper eventMapper = new EventMapper(jsonSerializer);
        eventMapper.setListener(listenerProxy);
        eventMapper.setJugadorId(miId);

        // 3. SESIÓN COMPARTIDA Y DATOS INICIALES
        String rutaAvatar = (miPuerto % 2 == 0) ? "/imageFish.png" : "/imageBun.png";
        String nombreJugador = "Jugador " + (miPuerto % 100);
        JugadorDTO perfil = new JugadorDTO(miId, nombreJugador, rutaAvatar);

        SesionCliente sesion = new SesionCliente(producer, perfil, listenerProxy);

        // 4. CONSTRUCCIÓN MVC 2: EJERCER TURNO
        ModeloEjercerTurno modeloJuego = new ModeloEjercerTurno(producer);
        modeloJuego.setJugadorLocal(miId);

        ControladorTurno ctrlJuego = new ControladorTurno(modeloJuego, sesion);
        UI_TurnoJugador vistaJuego = new UI_TurnoJugador(ctrlJuego, perfil);

        modeloJuego.suscribir(vistaJuego);
        vistaJuego.setVisible(false);

        // 5. CONSTRUCCIÓN MVC 1: CONFIGURAR PARTIDA
        ModeloConfiguracion modeloConfig = new ModeloConfiguracion(producer);
        ControladorConfigurarPartida ctrlConfig = new ControladorConfigurarPartida(modeloConfig);

        ctrlConfig.setSiguienteControlador(ctrlJuego);

        UI_MenuRummy vistaMenu = new UI_MenuRummy(ctrlConfig);
        UI_ConfigurarPartida vistaConfig = new UI_ConfigurarPartida(ctrlConfig);

        modeloConfig.suscribir(vistaMenu);
        modeloConfig.suscribir(vistaConfig);

        listenerProxy.activarModoConfiguracion(modeloConfig);

        // 6. ARRANCAR RED ENTRANTE (SOCKET IN)
        TraducerJugador traducer = new TraducerJugador(jsonSerializer, eventMapper);
        Receptor receptor = new Receptor(traducer);
        ColaReceptor colaReceptor = new ColaReceptor();
        colaReceptor.attach(receptor);

        SocketIN socketIn = new SocketIN(miPuerto, colaReceptor);
        socketIn.start();

        System.out.println("Ensamblador: Cliente " + nombreJugador + " (" + miId + ") iniciado en " + miIp + ":" + miPuerto);

        // 7. INICIO DEL FLUJO
        // Usamos el Producer para registrar, ya no necesitamos el emitter suelto
        producer.registrarJugador(miId, miIp, miPuerto);
        System.out.println("Ensamblador: Solicitud de registro enviada.");

        modeloConfig.cambiarVista(TipoVista.MENU_PRINCIPAL);
    }
}