/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

/**
 *
 * @author jrasc
 */
public interface IBroker {

    public void publicar(String json);

    public void suscribir(String topic, String clienteID, String ip, int port);

    public void unsubscribe(String topic, String clienteID);
}
