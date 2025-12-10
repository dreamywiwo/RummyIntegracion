package itson.ejercerturno.vista;

import itson.rummypresentacion.utils.FichaTransferable;
import itson.rummydtos.FichaDTO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

public class UI_Mano extends JPanel {

    private final List<FichaDTO> fichas = new ArrayList<>();
    private final List<UI_Ficha> fichasSeleccionadasVisualmente = new ArrayList<>();
    private final UI_Tablero tableroPanel;
    private boolean habilitado = true;

    private static final int ANCHO_FICHA = 60;
    private static final int GAP = 8;

    public UI_Mano(UI_Tablero tableroPanel) {
        this(tableroPanel, null);
    }

    public UI_Mano(UI_Tablero tableroPanel, List<FichaDTO> fichasIniciales) {
        this.tableroPanel = tableroPanel;

        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, GAP, 25));

        if (fichasIniciales != null) {
            fichas.addAll(fichasIniciales);
        }

        configurarDropTarget();
        refrescar();
    }

    public void setFichas(List<FichaDTO> nuevas) {
        limpiarSeleccion();

        fichas.clear();
        if (nuevas != null) {
            fichas.addAll(nuevas);
        }
        ordenar();
        refrescar();
    }

    public List<FichaDTO> getFichas() {
        return new ArrayList<>(fichas);
    }

    public void removerFichasPorId(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        boolean cambio = fichas.removeIf(f -> f.getId() != null && ids.contains(f.getId()));
        if (cambio) {
            ordenar();
            refrescar();
        }
    }

    public List<FichaDTO> obtenerFichasParaMover(UI_Ficha fichaIniciadora) {
        List<FichaDTO> dtosAMover = new ArrayList<>();

        if (fichaIniciadora.isSeleccionada()) {
            for (UI_Ficha ui : fichasSeleccionadasVisualmente) {
                dtosAMover.add(ui.getFicha());
            }
        } else {

            limpiarSeleccion();

            dtosAMover.add(fichaIniciadora.getFicha());
            fichaIniciadora.setSeleccionada(true);
            fichasSeleccionadasVisualmente.add(fichaIniciadora);
        }

        return dtosAMover;
    }

    public void notificarSeleccion(UI_Ficha ficha) {
        if (ficha.isSeleccionada()) {
            if (!fichasSeleccionadasVisualmente.contains(ficha)) {
                fichasSeleccionadasVisualmente.add(ficha);
            }
        } else {
            fichasSeleccionadasVisualmente.remove(ficha);
        }
    }

    public void limpiarSeleccion() {
        for (UI_Ficha ui : new ArrayList<>(fichasSeleccionadasVisualmente)) {
            ui.setSeleccionada(false);
            ui.repaint();
        }
        fichasSeleccionadasVisualmente.clear();
    }

    private void ordenar() {
        fichas.sort((f1, f2) -> {
            if (f1.isEsComodin() && !f2.isEsComodin()) {
                return 1;
            }
            if (!f1.isEsComodin() && f2.isEsComodin()) {
                return -1;
            }
            if (f1.isEsComodin() && f2.isEsComodin()) {
                return 0;
            }

            if (f1.getNumero() != f2.getNumero()) {
                return Integer.compare(f1.getNumero(), f2.getNumero());
            }
            return f1.getColor().compareTo(f2.getColor());
        });
    }

    public void refrescar() {
        this.removeAll();
        fichasSeleccionadasVisualmente.clear();

        for (FichaDTO dto : fichas) {
            UI_Ficha uiFicha = new UI_Ficha(dto);
            this.add(uiFicha);
        }

        actualizarTamanoPanel();

        this.revalidate();
        this.repaint();
    }

    private void actualizarTamanoPanel() {
        int cantidadFichas = fichas.size();
        int anchoNecesario = (cantidadFichas * (ANCHO_FICHA + GAP)) + 50;

        int anchoFinal = Math.max(780, anchoNecesario);

        setPreferredSize(new Dimension(anchoFinal, 110));
    }

    private void configurarDropTarget() {
        setDropTarget(new DropTarget() {
            @Override
            public synchronized void dragOver(DropTargetDragEvent dtde) {
                repaint();
            }

            @Override
            public synchronized void dragExit(DropTargetEvent dte) {
                repaint();
            }

            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                if (!habilitado) {
                    evt.rejectDrop();
                    return;
                }
                try {
                    evt.acceptDrop(DnDConstants.ACTION_MOVE);

                    Transferable t = evt.getTransferable();
                    if (!t.isDataFlavorSupported(FichaTransferable.FICHA_FLAVOR)) {
                        evt.dropComplete(false);
                        return;
                    }

                    FichaTransferable fichaT = (FichaTransferable) t.getTransferData(FichaTransferable.FICHA_FLAVOR);
                    List<FichaDTO> recibidas = fichaT.getFichas();

                    if (recibidas == null || recibidas.isEmpty()) {
                        evt.dropComplete(false);
                        return;
                    }

                    boolean vienenDelTablero = false;

                    for (FichaDTO f : recibidas) {
                        String grupoOrigenId = tableroPanel.obtenerIdGrupoDeFicha(f.getId());

                        if (grupoOrigenId != null) {
                            tableroPanel.getVentanaPrincipal().solicitarDevolverFicha(grupoOrigenId, f.getId());
                            vienenDelTablero = true;
                        }
                    }

                    if (vienenDelTablero) {
                        evt.dropComplete(true);
                        return;
                    }

                    Set<String> misIds = new HashSet<>();
                    for (FichaDTO f : fichas) {
                        if (f.getId() != null) {
                            misIds.add(f.getId());
                        }
                    }

                    boolean cambio = false;
                    for (FichaDTO f : recibidas) {
                        if (!misIds.contains(f.getId())) {
                            fichas.add(f);
                            cambio = true;
                        }
                    }

                    if (cambio) {
                        limpiarSeleccion();
                        ordenar();
                        refrescar();
                    }

                    evt.dropComplete(true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    evt.dropComplete(false);
                } finally {
                    repaint();
                }
            }
        });
    }

    public void actualizarEstado(boolean esMiTurno) {
        this.habilitado = esMiTurno;
        if (!habilitado) {
            setBackground(new Color(200, 200, 200, 50));
        } else {
            setOpaque(false);
        }
        repaint();
    }
}
