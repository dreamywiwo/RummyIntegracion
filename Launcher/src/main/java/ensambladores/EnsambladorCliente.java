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

// MVC 2: Registrar Jugador
import itson.registrarjugador.controlador.ControladorRegistro;
import itson.registrarjugador.modelo.ModeloRegistro;
import itson.registrarjugador.vista.UI_Registro;

// MVC 3: Sala de Espera (Lobby)
import itson.solicitarinicio.controlador.ControladorSalaEspera;
import itson.solicitarinicio.modelo.ModeloSalaEspera;
import itson.solicitarinicio.vista.UI_SalaEspera;

// MVC 4: Ejercer Turno (Juego)
import itson.ejercerturno.controlador.ControladorTurno;
import itson.ejercerturno.modelo.ModeloEjercerTurno;
import itson.ejercerturno.vista.UI_TurnoJugador;

public class EnsambladorCliente {

    public void iniciarAplicacion(String ipBroker, int puertoBroker, String miIp, int miPuerto, String miId) {

        JsonSerializer jsonSerializer = new JsonSerializer();
        SocketOut socketOut = new SocketOut();
        socketOut.start();
        ColaDispatcher colaDispatcher = new ColaDispatcher();
        colaDispatcher.attach(socketOut);
        IDispatcher dispatcher = new Dispatcher(colaDispatcher);

        IProducerJugador producer = new ProducerJugador(jsonSerializer, dispatcher, ipBroker, puertoBroker, miId);

        ListenerProxy listenerProxy = new ListenerProxy();
        EventMapper eventMapper = new EventMapper(jsonSerializer);
        eventMapper.setListener(listenerProxy);
        eventMapper.setJugadorId(miId);
        
        JugadorDTO perfil = new JugadorDTO();
        perfil.setId(miId); 
        
        SesionCliente sesion = new SesionCliente(producer, perfil, listenerProxy);

        ModeloEjercerTurno modeloJuego = new ModeloEjercerTurno(producer);
        modeloJuego.setJugadorLocal(miId);
        ControladorTurno ctrlJuego = new ControladorTurno(modeloJuego, sesion);
        UI_TurnoJugador vistaJuego = new UI_TurnoJugador(ctrlJuego, perfil);
        modeloJuego.suscribir(vistaJuego);
        vistaJuego.setVisible(false);

        ModeloSalaEspera modeloSala = new ModeloSalaEspera(producer);
        modeloSala.setMiIdLocal(miId);
        ControladorSalaEspera ctrlSala = new ControladorSalaEspera(modeloSala, sesion);
        ctrlSala.setSiguienteControlador(ctrlJuego); 

        UI_SalaEspera vistaSala = new UI_SalaEspera(ctrlSala);
        modeloSala.suscribir(vistaSala);
        vistaSala.setVisible(false);

        ModeloRegistro modeloRegistro = new ModeloRegistro(producer);
        ControladorRegistro ctrlRegistro = new ControladorRegistro(modeloRegistro, sesion);
        ctrlRegistro.setSiguienteControlador(ctrlSala); 

        UI_Registro vistaRegistro = new UI_Registro(ctrlRegistro);
        modeloRegistro.suscribir(vistaRegistro);
        vistaRegistro.setVisible(false);

        ModeloConfiguracion modeloConfig = new ModeloConfiguracion(producer);
        ControladorConfigurarPartida ctrlConfig = new ControladorConfigurarPartida(modeloConfig, sesion);
        ctrlConfig.setSiguienteControlador(ctrlRegistro);

        UI_MenuRummy vistaMenu = new UI_MenuRummy(ctrlConfig);
        UI_ConfigurarPartida vistaConfig = new UI_ConfigurarPartida(ctrlConfig);
        modeloConfig.suscribir(vistaMenu);
        modeloConfig.suscribir(vistaConfig);

        listenerProxy.activarModoConfiguracion(modeloConfig);

        TraducerJugador traducer = new TraducerJugador(jsonSerializer, eventMapper);
        Receptor receptor = new Receptor(traducer);
        ColaReceptor colaReceptor = new ColaReceptor();
        colaReceptor.attach(receptor);
        SocketIN socketIn = new SocketIN(miPuerto, colaReceptor);
        socketIn.start();

        System.out.println("Ensamblador: Cliente iniciado (" + miId + ") en " + miIp + ":" + miPuerto);

        producer.registrarJugador(miId, miIp, miPuerto);
        
        modeloConfig.cambiarVista(TipoVista.MENU_PRINCIPAL);
    }
}