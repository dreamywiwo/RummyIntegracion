/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.directorio.implementacion;

import java.util.Objects;

/**
 * Valor inmutable que representa ip + puerto de un cliente.
 */
public final class ConnectionEndpoint {

    private final String clientId;
    private final String ip;
    private final int port;

    public ConnectionEndpoint(String clientId, String ip, int port) {
        this.clientId = Objects.requireNonNull(clientId, "clientId");
        this.ip = Objects.requireNonNull(ip, "ip");
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("port out of range");
        }
        this.port = port;
    }

    public String getClientId() {
        return clientId;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConnectionEndpoint)) {
            return false;
        }
        ConnectionEndpoint that = (ConnectionEndpoint) o;
        return port == that.port
                && clientId.equals(that.clientId)
                && ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, ip, port);
    }

    @Override
    public String toString() {
        return "ConnectionEndpoint{"
                + "clientId='" + clientId + '\''
                + ", ip='" + ip + '\''
                + ", port=" + port
                + '}';
    }
}
