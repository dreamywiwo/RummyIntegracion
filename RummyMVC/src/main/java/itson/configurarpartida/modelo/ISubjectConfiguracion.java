/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.configurarpartida.modelo;

/**
 *
 * @author Dana Chavez
 */
public interface ISubjectConfiguracion {
    public void suscribir(IObserverConfiguracion observer);
    public void notificar(IObserverConfiguracion observer);
    public void notificarObservers();       
}
