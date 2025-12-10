/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.directorio.interfaces;

import itson.directorio.implementacion.ConnectionEndpoint;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz pública del servicio Directorio.
 */
public interface IDirectorio {

    /**
     * Registra (o actualiza) el endpoint de un cliente.
     * @param clientId id único del cliente
     * @param ip dirección IP (string)
     * @param port puerto (entero)
     */
    void registerClient(String clientId, String ip, int port);

    /**
     * Obtiene el endpoint para un clientId.
     * @param clientId id del cliente
     * @return Optional con ConnectionEndpoint si existe
     */
    Optional<ConnectionEndpoint> getEndpoint(String clientId);

    /**
     * Elimina el cliente del directorio.
     * @param clientId id del cliente
     * @return true si existía y fue removido, false si no existía
     */
    boolean removeClient(String clientId);

    /**
     * Devuelve la lista de endpoints para una lista de clientIds.
     * Si algún clientId no existe, no estará en la lista resultante.
     * @param clientIds lista de ids
     * @return lista de ConnectionEndpoint existentes
     */
    List<ConnectionEndpoint> getEndpoints(List<String> clientIds);
}
