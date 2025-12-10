/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.actualizaciones;

import itson.rummydtos.TableroDTO;
import itson.rummyeventos.base.EventBase;

/**
 *
 * @author Dana Chavez
 */
public class TableroActualizadoEvent extends EventBase {

    private TableroDTO tableroSnapshot;

    public static final String TOPIC = "actualizaciones.estado";
    public static final String EVENT_TYPE = "tablero.actualizado";

    public TableroActualizadoEvent() {
        super();
    }

    public TableroActualizadoEvent(TableroDTO tableroSnapshot) {
        super(TOPIC, EVENT_TYPE);
        this.tableroSnapshot = tableroSnapshot;
    }

    public TableroDTO getTableroSnapshot() {
        return tableroSnapshot;
    }

    public void setTableroSnapshot(TableroDTO tableroSnapshot) {
        this.tableroSnapshot = tableroSnapshot;
    }

}
