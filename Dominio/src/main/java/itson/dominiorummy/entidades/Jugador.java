/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.dominiorummy.entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author victoria
 */
public class Jugador {

    private final String id;
    private String nombre;

    private final Mano mano;

    private boolean yaBajo30;
    private boolean haTerminado;
    private boolean enTurno;

    private boolean haTomadoFicha;

    private String avatarPath;
    private List<String> coloresFichas;

    private boolean listo = false;

    public Jugador() {
        this.id = UUID.randomUUID().toString();
        this.mano = new Mano();
        this.yaBajo30 = false;
        this.haTerminado = false;
        this.enTurno = false;
        this.nombre = null;
    }

    public Jugador(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.mano = new Mano();
        this.yaBajo30 = false;
        this.haTerminado = false;
        this.enTurno = false;
        this.coloresFichas = new ArrayList<>();
    }

    public Jugador(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.mano = new Mano();
        this.yaBajo30 = false;
        this.haTerminado = false;
        this.enTurno = false;
        this.coloresFichas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Mano getMano() {
        return mano;
    }

    public boolean yaBajo30() {
        return yaBajo30;
    }

    public boolean haTerminado() {
        return haTerminado;
    }

    public boolean isEnTurno() {
        return enTurno;
    }

    public void marcarPrimerBajada30Completada() {
        this.yaBajo30 = true;
    }

    public void marcarTerminado() {
        this.haTerminado = true;
    }

    public void setEnTurno(boolean enTurno) {
        this.enTurno = enTurno;
    }

    public boolean haTomadoFicha() {
        return haTomadoFicha;
    }

    public void setHaTomadoFicha(boolean haTomadoFicha) {
        this.haTomadoFicha = haTomadoFicha;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public List<String> getColoresFichas() {
        return coloresFichas;
    }

    public void setColoresFichas(List<String> coloresFichas) {
        this.coloresFichas = coloresFichas;
    }

    public void reiniciarEstadoTurno() {
        this.haTomadoFicha = false;
    }

    public boolean isListo() {
        return listo;
    }

    public void setListo(boolean listo) {
        this.listo = listo;
    }

    /**
     * Calcula los puntos de la mano actual (sirve para final de partida).
     */
    public int calcularPuntosEnMano() {
        return mano.getFichas().stream()
                .mapToInt(f -> f.getNumero())
                .sum();
    }

    @Override
    public String toString() {
        return "Jugador{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", fichasEnMano=" + mano.getFichas().size()
                + ", yaBajo30=" + yaBajo30
                + ", enTurno=" + enTurno
                + '}';
    }
}
