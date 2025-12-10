/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.dominiorummy.entidades;

/**
 *
 * @author Dana Chavez
 */

public class Partida {

    public enum EstadoPartida {
        CONFIGURANDO,
        DISPONIBLE, 
        EN_CURSO,
        TERMINADA
    }

    private int maxNumFichas;
    private int cantidadComodines;
    private EstadoPartida estado;

    public Partida() {
        this.estado = EstadoPartida.CONFIGURANDO;
    }

    public void configurar(int maxNumFichas, int cantidadComodines) {
        this.maxNumFichas = maxNumFichas;
        this.cantidadComodines = cantidadComodines;
    }

    public void marcarDisponible() {
        this.estado = EstadoPartida.DISPONIBLE;
    }

    // Getters
    public EstadoPartida getEstado() {
        return estado;
    }

    public int getMaxNumFichas() {
        return maxNumFichas;
    }
}
