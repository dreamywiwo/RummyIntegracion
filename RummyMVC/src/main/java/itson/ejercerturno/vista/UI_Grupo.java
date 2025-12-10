/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ejercerturno.vista;

import itson.rummypresentacion.utils.FichaTransferable;
import itson.rummydtos.FichaDTO;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Vista "Tonta" de un Grupo. Solo renderiza fichas y maneja el Drop. No valida
 * reglas.
 *
 * * @author Dana Chavez
 */
public class UI_Grupo extends JPanel {

    private final String idGrupo;
    private final List<FichaDTO> fichas = new ArrayList<>();
    private final Set<String> idsConError = new HashSet<>(); // Estado visual pasivo
    private UI_Tablero tablero;

    private static final int ANCHO_FICHA = 60;
    private static final int ALTO_FICHA = 90;
    private static final int GAP_H = 5;

    public UI_Grupo(String idGrupo, List<FichaDTO> fichasIniciales, UI_Tablero tablero) {
        super();
        this.idGrupo = idGrupo;
        this.tablero = tablero;
        if (fichasIniciales != null) {
            fichas.addAll(fichasIniciales);
        }
        configurarPanel();
        ordenar(); // Ordenamiento visual básico 
        refrescar();
        configurarDropTarget();
    }

    private void configurarPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, GAP_H, 0));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Método para que se le diga a la vista qué está mal.
     */
    public void marcarFichasInvalidas(Set<String> ids) {
        this.idsConError.clear();

        if (ids != null && !ids.isEmpty()) {
            this.idsConError.addAll(ids);
        }
        refrescar();
    }

    public void refrescar() {
        removeAll();

        for (FichaDTO ficha : fichas) {
            if (ficha != null) {
                UI_Ficha uiFicha = new UI_Ficha(ficha);

                if (idsConError.contains(ficha.getId())) {
                    uiFicha.setBordeError(true);
                } else {
                    uiFicha.setBordeError(false);
                }

                add(uiFicha);
            }
        }
        revalidate();
        repaint();
    }

    private void configurarDropTarget() {
        setDropTarget(new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_MOVE);
                    Transferable t = evt.getTransferable();

                    if (!t.isDataFlavorSupported(FichaTransferable.FICHA_FLAVOR)) {
                        evt.dropComplete(false);
                        return;
                    }

                    FichaTransferable fichaT = (FichaTransferable) t.getTransferData(FichaTransferable.FICHA_FLAVOR);
                    List<FichaDTO> recibidas = fichaT.getFichas();

                    // 1. Limpiar origen 
                    if (tablero != null) {
                        tablero.removerFichasDeOtrosContenedores(recibidas, UI_Grupo.this);
                    }

                    // 2. Agregar fichas nuevas 
                    Set<String> misIds = new HashSet<>();
                    for (FichaDTO f : fichas) {
                        if (f.getId() != null) {
                            misIds.add(f.getId());
                        }
                    }

                    boolean cambio = false;
                    for (FichaDTO f : recibidas) {
                        if (f != null && !misIds.contains(f.getId())) {
                            fichas.add(f);
                            cambio = true;
                        }
                    }

                    if (cambio) {
                        ordenar();
                        refrescar();
                        if (tablero != null) {
                            setSize(getPreferredSize());
                            // 3. NOTIFICAR AL CONTROLADOR
                            tablero.getVentanaPrincipal().notificarGrupoActualizado(idGrupo, getFichas());
                        }
                    }
                    evt.dropComplete(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    evt.dropComplete(false);
                }
            }
        }));
    }

    /**
     * Ordenamiento meramente VISUAL para que las fichas no queden desordenadas
     * al soltarlas. Esto no valida reglas, solo mejora la Ui un poquito jjj.
     */
    private void ordenar() {
        fichas.sort((f1, f2) -> {
            if (f1 == null) {
                return 1;
            }
            if (f2 == null) {
                return -1;
            }
            if (f1.isEsComodin()) {
                return 1; // Comodines al final visualmente
            }
            if (f2.isEsComodin()) {
                return -1;
            }

            int numCmp = Integer.compare(f1.getNumero(), f2.getNumero());
            if (numCmp != 0) {
                return numCmp;
            }

            String c1 = f1.getColor() != null ? f1.getColor() : "";
            String c2 = f2.getColor() != null ? f2.getColor() : "";
            return c1.compareTo(c2);
        });
    }

    @Override
    public Dimension getPreferredSize() {
        int n = Math.max(1, fichas.size());
        int anchoTotal = (n * ANCHO_FICHA) + ((n - 1) * GAP_H) + 30;
        int altoTotal = ALTO_FICHA + 30;
        return new Dimension(anchoTotal, altoTotal);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public List<FichaDTO> getFichas() {
        return new ArrayList<>(fichas);
    }

    public void removerFichasPorId(Set<String> ids) {
        if (fichas.removeIf(f -> ids.contains(f.getId()))) {
            ordenar();
            refrescar();
            setSize(getPreferredSize());
        }
    }

    public String getIdGrupo() {
        return idGrupo;
    }
}
