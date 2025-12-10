/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.configurarpartida.modelo;

import itson.configurarpartida.modelo.IModeloConfiguracion;

/**
 *
 * @author Dana Chavez
 */
public interface IObserverConfiguracion {
    /**
     * Se llama cuando el modelo cambia
     * @param modelo Estado actual del modelo (solo lectura)
     */
    void update(IModeloConfiguracion modelo);   
}
