package itson.rummyeventos.sistema;

import itson.rummyeventos.base.EventBase;

public class RegistroDominioEvent extends EventBase {

    public static final String TOPIC = "sistema.registro";
    public static final String EVENT_TYPE = "dominio.registro";

    private String dominioId;
    private String ip;
    private int puerto;

    public RegistroDominioEvent() {
        super();
    }

    public RegistroDominioEvent(String dominioId, String ip, int puerto) {
        super(TOPIC, EVENT_TYPE);
        this.dominioId = dominioId;
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getDominioId() {
        return dominioId;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }
}