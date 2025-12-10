/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ejercerturno.modelo;

/**
 *
 * @author Dana Chavez
 */
public interface ISubject {
    public void suscribir(IObserver observer);
    public void notificar(IObserver observer);
    public void notificarObservers();   
}
