/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.directorio.implementacion;

import itson.directorio.interfaces.IDirectorio;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Directorio implements IDirectorio {

    private final ConcurrentHashMap<String, ConnectionEndpoint> endpoints = new ConcurrentHashMap<>();

    @Override
    public void registerClient(String clientId, String ip, int port) {
        validateClientId(clientId);
        validateIp(ip);
        validatePort(port);
        ConnectionEndpoint ep = new ConnectionEndpoint(clientId, ip, port);
        endpoints.put(clientId, ep);
    }

    @Override
    public Optional<ConnectionEndpoint> getEndpoint(String clientId) {
        if (clientId == null) return Optional.empty();
        return Optional.ofNullable(endpoints.get(clientId));
    }

    @Override
    public boolean removeClient(String clientId) {
        if (clientId == null) return false;
        return endpoints.remove(clientId) != null;
    }

    @Override
    public List<ConnectionEndpoint> getEndpoints(List<String> clientIds) {
        if (clientIds == null || clientIds.isEmpty()) return Collections.emptyList();
        return clientIds.stream()
                .map(endpoints::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void validateClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("clientId cannot be null or blank");
        }
    }

    private void validatePort(int port) {
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("invalid port: " + port);
        }
    }

    private void validateIp(String ip) {
        if (ip == null || ip.isBlank()) throw new IllegalArgumentException("ip cannot be null/blank");
        if (!ip.matches("[0-9a-zA-Z:\\.\\-]+")) {
            throw new IllegalArgumentException("invalid ip/host format: " + ip);
        }
    }
}