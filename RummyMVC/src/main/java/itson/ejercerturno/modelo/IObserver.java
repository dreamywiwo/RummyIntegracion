/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ejercerturno.modelo;

/**
 *
 * @author Dana Chavez
 */
public interface IObserver {
    /**
     * Se llama cuando el modelo cambia
     * @param modelo Estado actual del modelo (solo lectura)
     */
    void update(IModeloEjercerTurno modelo);    
}
