package ensambladores;

import ClaseDispatcher.ColaDispatcher;
import ClaseDispatcher.Dispatcher;
import ClaseDispatcher.SocketOut;
import claseReceptor.ColaReceptor;
import claseReceptor.Receptor;
import claseReceptor.SocketIN;
import com.mycompany.conexioninterfaces.IDispatcher;
import itson.dominiorummy.entidades.Ficha;
import itson.dominiorummy.entidades.Jugador;
import itson.dominiorummy.entidades.Sopa;
import itson.dominiorummy.entidades.Tablero;
import itson.dominiorummy.entidades.Turno;
import itson.dominiorummy.facade.Dominio;
import itson.producerdominio.emitters.EstadoJuegoEmitter;
import itson.producerdominio.emitters.InicializarJuegoEmitter;
import itson.producerdominio.facade.IProducerDominio;
import itson.producerdominio.facade.ProducerDominio;
import itson.serializer.implementacion.JsonSerializer;
import itson.traducerdominio.facade.TraducerDominio;
import itson.traducerdominio.mappers.EventMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EnsambladorDominio {

    private Dominio dominio;

    public void iniciarJuego(String ipBroker, int puertoBroker, int puertoEscuchaDominio, List<Jugador> jugadores) {
        
        // 1. Configuración de RED SALIENTE
        JsonSerializer jsonSerializer = new JsonSerializer();
        
        SocketOut socketOut = new SocketOut();
        socketOut.start();
        
        ColaDispatcher colaDispatcher = new ColaDispatcher();
        colaDispatcher.attach(socketOut);
        
        IDispatcher dispatcher = new Dispatcher(colaDispatcher);

        EstadoJuegoEmitter estadoEmitter = new EstadoJuegoEmitter(jsonSerializer, dispatcher, ipBroker, puertoBroker);

        InicializarJuegoEmitter initEmitter = new InicializarJuegoEmitter(jsonSerializer, dispatcher, ipBroker, puertoBroker);

        IProducerDominio producer = new ProducerDominio(jsonSerializer, dispatcher, ipBroker,puertoBroker); 

        // 2. INICIALIZACIÓN DE LÓGICA DE JUEGO
        List<Ficha> mazoCompleto = generarFichasRummy();
        Sopa sopa = new Sopa(mazoCompleto);
        Tablero tablero = new Tablero();
        
        if (jugadores != null && !jugadores.isEmpty()) {
            for (Jugador jugador : jugadores) {
                for (int i = 0; i < 14; i++) {
                    Ficha f = sopa.tomarFicha();
                    if (f != null) {
                        jugador.getMano().agregarFicha(f);
                    }
                }
                System.out.println("EnsambladorDominio: Se repartieron 14 fichas a " + jugador.getNombre());
            }
        }

        Turno turno = new Turno(jugadores, 0); 
        this.dominio = new Dominio(tablero, producer, turno, sopa, mazoCompleto);
        
        if (jugadores != null) {
            for(Jugador j : jugadores){
                this.dominio.agregarJugador(j);
            }
        }

        // 3. Configuración de RED ENTRANTE
        EventMapper eventMapper = new EventMapper(jsonSerializer, this.dominio);
        TraducerDominio traducer = new TraducerDominio(jsonSerializer, eventMapper);
        Receptor receptor = new Receptor(traducer);
        ColaReceptor colaReceptor = new ColaReceptor();
        colaReceptor.attach(receptor);

        SocketIN socketIn = new SocketIN(puertoEscuchaDominio, colaReceptor);
        socketIn.start();

        System.out.println("EnsambladorDominio: Lógica de juego lista en puerto " + puertoEscuchaDominio);
        
        // 4. HANDSHAKE (REGISTRO) AUTOMÁTICO

        String dominioId = "DOMINIO_SERVER"; 
        initEmitter.emitirRegistroDominioEvent(dominioId, "127.0.0.1", puertoEscuchaDominio);
        System.out.println("EnsambladorDominio: Solicitud de registro enviada al Broker.");
    }

    public void comenzarPartida() {
        if (this.dominio != null) {
            System.out.println("EnsambladorDominio: Ordenando inicio de partida...");
            this.dominio.iniciarPartida(); 
        } else {
            System.err.println("EnsambladorDominio: No se ha inicializado el juego.");
        }
    }

    private List<Ficha> generarFichasRummy() {
        List<Ficha> fichas = new ArrayList<>();
        String[] colores = {"ROJO", "AZUL", "NEGRO", "AMARILLO"}; 

        for (int set = 0; set < 2; set++) {
            for (String color : colores) {
                for (int numero = 1; numero <= 13; numero++) {
                    String idUnico = UUID.randomUUID().toString();
                    Ficha f = new Ficha(idUnico, numero, color, false);
                    fichas.add(f);
                }
            }
        }
        fichas.add(new Ficha(UUID.randomUUID().toString(), 0, "COMODIN", true));
        fichas.add(new Ficha(UUID.randomUUID().toString(), 0, "COMODIN", true));
        Collections.shuffle(fichas);
        return fichas;
    }
}