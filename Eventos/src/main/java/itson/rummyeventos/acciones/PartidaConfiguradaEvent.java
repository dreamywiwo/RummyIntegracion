/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.acciones;

import itson.rummyeventos.base.EventBase;

/**
 *
 * @author Dana Chavez
 */
public class PartidaConfiguradaEvent extends EventBase {
    
    private int maxNumFichas;
    private int cantidadComodines;
    
    public static final String TOPIC = "acciones.jugador";
    public static final String EVENT_TYPE = "partida.configurada";

    public PartidaConfiguradaEvent() {
        super();
    }

    public PartidaConfiguradaEvent(int maxNumFichas, int cantidadComodines) {
        super(TOPIC, EVENT_TYPE);
        this.maxNumFichas = maxNumFichas;
        this.cantidadComodines = cantidadComodines;
    }

    public int getMaxNumFichas() {
        return maxNumFichas;
    }

    public void setMaxNumFichas(int maxNumFichas) {
        this.maxNumFichas = maxNumFichas;
    }

    public int getCantidadComodines() {
        return cantidadComodines;
    }

    public void setCantidadComodines(int cantidadComodines) {
        this.cantidadComodines = cantidadComodines;
    }
    
}
