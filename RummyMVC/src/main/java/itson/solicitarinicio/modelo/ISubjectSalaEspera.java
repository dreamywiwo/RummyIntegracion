/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.solicitarinicio.modelo;


/**
 *
 * @author Dana Chavez
 */
public interface ISubjectSalaEspera {
    public void suscribir(IObserverSalaEspera observer);
    public void notificarObservers();   
}
