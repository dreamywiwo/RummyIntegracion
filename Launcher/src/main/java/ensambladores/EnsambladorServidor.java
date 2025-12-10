package ensambladores;

import ClaseDispatcher.ColaDispatcher;
import ClaseDispatcher.Dispatcher;
import ClaseDispatcher.SocketOut;
import claseReceptor.ColaReceptor;
import claseReceptor.Receptor;
import claseReceptor.SocketIN;
import com.mycompany.broker.Broker;
import com.mycompany.broker.ProcesadorEventosBroker;
import com.mycompany.broker.SubscriptionRegistry;
import com.mycompany.conexioninterfaces.IDispatcher;
import itson.directorio.implementacion.Directorio;
import itson.directorio.interfaces.IDirectorio;
import itson.serializer.implementacion.JsonSerializer;
import itson.serializer.interfaces.ISerializer;

/**
 * Ensamblador del servidor (Broker de eventos)
 */
public class EnsambladorServidor {

    public void iniciarBroker(int puertoEscucha) {

        ISerializer serializer = new JsonSerializer();
        IDirectorio directorio = new Directorio();
        SubscriptionRegistry registry = new SubscriptionRegistry();

        SocketOut socketOut = new SocketOut();
        socketOut.start();

        ColaDispatcher colaDispatcher = new ColaDispatcher();
        colaDispatcher.attach(socketOut);

        IDispatcher dispatcher = new Dispatcher(colaDispatcher);

        Broker brokerLogic = new Broker(directorio, dispatcher, serializer, registry);

        ProcesadorEventosBroker brokerProcesador = new ProcesadorEventosBroker(brokerLogic, serializer);

        ColaReceptor colaReceptor = new ColaReceptor();
        colaReceptor.attach(new Receptor(brokerProcesador));

        SocketIN socketIN = new SocketIN(puertoEscucha, colaReceptor);
        socketIN.start();

        System.out.println("EnsambladorBroker: Servidor Broker escuchando en el puerto " + puertoEscucha);
    }
}
