/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.configurarpartida.modelo;

import itson.rummypresentacion.utils.TipoVista;

/**
 *
 * @author Dana Chavez
 */
public interface IModeloConfiguracion {
    public abstract TipoVista getVistaActual();
    public abstract String getMensajeError();
    public abstract boolean isConfiguracionExitosa();
}
