/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ejercerturno.controlador;

import itson.rummydtos.FichaDTO;
import itson.ejercerturno.modelo.ModeloEjercerTurno;
import itson.rummypresentacion.utils.SesionCliente;
import itson.rummypresentacion.utils.TipoVista;
import java.util.List;

/**
 * Controlador del turno del jugador
 *
 * @author Dana Chavez
 */
public class ControladorTurno {

private ModeloEjercerTurno modelo;
    private SesionCliente sesion; 

    public ControladorTurno(ModeloEjercerTurno modelo, SesionCliente sesion) {
        this.modelo = modelo;
        this.sesion = sesion;
    }
    
    // Este método es vital para la transición de MVCs
    public void iniciarJuego() {
        System.out.println("[CtrlTurno] Iniciando lógica de juego...");

        // 1. CAMBIAR EL CANAL DEL PROXY
        sesion.getListenerProxy().activarModoJuego(modelo);

        // 2. CAMBIAR LA VISTA
        modelo.cambiarVista(TipoVista.TABLERO_JUEGO);
        modelo.solicitarSincronizacion();
    }

    /**
     * Maneja la creación de un nuevo grupo
     *
     * @param fichas Lista de fichas del nuevo grupo
     */
    public void crearGrupo(List<FichaDTO> fichas) {
        modelo.crearGrupo(fichas);
    }

    /**
     * Maneja la actualización de un grupo existente
     *
     * @param idGrupo ID del grupo actualizado
     * @param fichas Lista actualizada de fichas
     */
    public void actualizarGrupo(String idGrupo, List<FichaDTO> fichas) {
        System.out.println("CONTROLADOR: Actualizar grupo " + idGrupo + " con " + fichas.size() + " fichas");
        for (FichaDTO ficha : fichas) {
            System.out.println("   - " + ficha.getId());
        }
        modelo.actualizarGrupo(idGrupo, fichas);
    }

    /**
     * Maneja el movimiento de una ficha
     *
     * @param ficha Ficha movida
     * @param origen Contenedor de origen
     * @param destino Contenedor de destino
     */
    public void moverFicha(FichaDTO ficha, String origen, String destino) {
        // TODO: Registrar el movimiento
        // TODO: Validar si el movimiento es legal
    }

    /**
     * Maneja el intento de terminar el turno
     */
    public void terminarTurno() {
        modelo.terminarTurno();
    }

    /**
     * Maneja la acción de tomar una ficha del pozo
     */
    public void tomarFicha() {
        modelo.tomarFicha();
    }

    /**
     * Maneja el abandono de la partida
     */
    public void abandonarPartida() {
        // TODO: Confirmar abandono
        // TODO: Notificar a otros jugadores
        // TODO: Cerrar ventana o volver al menú
    }

    public void devolverFicha(String grupoId, String fichaId) {
        modelo.devolverFicha(grupoId, fichaId);
    }
}
