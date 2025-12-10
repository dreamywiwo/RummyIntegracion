package ensambladores;

import ClaseDispatcher.ColaDispatcher;
import ClaseDispatcher.Dispatcher;
import ClaseDispatcher.SocketOut;
import claseReceptor.ColaReceptor;
import claseReceptor.Receptor;
import claseReceptor.SocketIN;
import com.mycompany.conexioninterfaces.IDispatcher;
import itson.dominiorummy.entidades.Partida; // Importante
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

public class EnsambladorDominio {

    private Dominio dominio;

    public void iniciarJuego(String ipBroker, int puertoBroker, int puertoEscuchaDominio) {
        
        JsonSerializer jsonSerializer = new JsonSerializer();
        SocketOut socketOut = new SocketOut();
        socketOut.start();
        ColaDispatcher colaDispatcher = new ColaDispatcher();
        colaDispatcher.attach(socketOut);
        IDispatcher dispatcher = new Dispatcher(colaDispatcher);

        EstadoJuegoEmitter estadoEmitter = new EstadoJuegoEmitter(jsonSerializer, dispatcher, ipBroker, puertoBroker);
        InicializarJuegoEmitter initEmitter = new InicializarJuegoEmitter(jsonSerializer, dispatcher, ipBroker, puertoBroker);
        IProducerDominio producer = new ProducerDominio(jsonSerializer, dispatcher, ipBroker, puertoBroker);

        Tablero tablero = new Tablero();
        Sopa sopa = new Sopa(new ArrayList<>()); 
        Turno turno = new Turno(new ArrayList<>(), 0); 

        this.dominio = new Dominio(tablero, producer, turno, sopa, null);
        
        EventMapper eventMapper = new EventMapper(jsonSerializer, this.dominio);
        TraducerDominio traducer = new TraducerDominio(jsonSerializer, eventMapper);
        Receptor receptor = new Receptor(traducer);
        ColaReceptor colaReceptor = new ColaReceptor();
        colaReceptor.attach(receptor);

        SocketIN socketIn = new SocketIN(puertoEscuchaDominio, colaReceptor);
        socketIn.start();

        System.out.println("EnsambladorDominio: Servidor listo en puerto " + puertoEscuchaDominio);
        
        String dominioId = "DOMINIO_SERVER"; 
        initEmitter.emitirRegistroDominioEvent(dominioId, "127.0.0.1", puertoEscuchaDominio);
        System.out.println("EnsambladorDominio: Solicitud de registro enviada al Broker.");
    }
    
    // El método comenzarPartida() ya no es necesario aquí porque el Dominio
    // lo hará automáticamente cuando se llenen los jugadores o se pida por red.
    // Pero si quieres mantenerlo para pruebas manuales desde Main:
    public void forzarInicioPartida() {
        if (this.dominio != null) {
             System.out.println("EnsambladorDominio: Forzando inicio...");
             this.dominio.prepararYArrancarJuego(); 
        }
    }
}