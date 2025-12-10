/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package itson.ejercerturno.vista;

import itson.rummypresentacion.utils.FichaTransferable;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.GrupoDTO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UI_Tablero extends javax.swing.JPanel {

    private final UI_TurnoJugador ventanaPrincipal;
    private final List<UI_Grupo> gruposVisuales = new ArrayList<>();
    private String grupoInvalidoActual = null;

    public UI_Tablero(UI_TurnoJugador ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        configurarPanel();
        habilitarDrop();
    }

    private void configurarPanel() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(803, 448));
    }

    private void habilitarDrop() {
        setDropTarget(new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_MOVE);
                    Object data = evt.getTransferable().getTransferData(FichaTransferable.FICHA_FLAVOR);

                    if (!(data instanceof FichaTransferable)) {
                        evt.dropComplete(false);
                        return;
                    }

                    FichaTransferable ft = (FichaTransferable) data;
                    List<FichaDTO> fichas = ft.getFichas();

                    if (fichas == null || fichas.isEmpty()) {
                        evt.dropComplete(false);
                        return;
                    }

                    Point puntoMouse = evt.getLocation();

                    removerFichasDeOtrosContenedores(fichas, null);

                    revalidate();
                    repaint();

                    crearGrupoVisual(fichas, puntoMouse);

                    evt.dropComplete(true);

                    if (ventanaPrincipal != null) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            ventanaPrincipal.notificarGrupoCreado(fichas);
                        });
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    evt.dropComplete(false);
                }
            }
        }));
    }

    private void crearGrupoVisual(List<FichaDTO> fichas, Point puntoMouse) {
        String idGrupo = java.util.UUID.randomUUID().toString();
        UI_Grupo grupoPanel = new UI_Grupo(idGrupo, fichas, this);

        Dimension size = grupoPanel.getPreferredSize();
        grupoPanel.setSize(size);

        int x = puntoMouse.x - (size.width / 2);
        int y = puntoMouse.y - (size.height / 2);

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }

        Point ubicacionDeseada = new Point(x, y);
        Point ubicacionFinal = ubicacionDeseada;

        if (!esPosicionValida(x, y, size.width, size.height, null)) {
            Point encontrado = encontrarPosicionLibre(ubicacionDeseada, size);
            if (encontrado != null) {
                ubicacionFinal = encontrado;
            }
        }

        grupoPanel.setLocation(ubicacionFinal);
        grupoPanel.setVisible(true);

        gruposVisuales.add(grupoPanel);
        add(grupoPanel);

        verificarExpansionTablero(ubicacionFinal, size);
        setComponentZOrder(grupoPanel, 0);

        grupoPanel.doLayout();
        revalidate();
        repaint();

        if (getParent() != null) {
            getParent().revalidate();
            getParent().repaint();
        }

        System.out.println("[UI_TABLERO] Grupo creado visualmente: " + idGrupo + " con " + fichas.size() + " fichas");
    }

    private Point encontrarPosicionLibre(Point deseado, Dimension dim) {
        int x = deseado.x;
        int y = deseado.y;
        int step = 10;

        for (int layer = 1; layer < 200; layer++) {
            for (int dx = -layer; dx <= layer; dx++) {
                int nx1 = x + dx * step;
                int ny1 = y - layer * step;
                if (checkPoint(nx1, ny1, dim)) {
                    return new Point(nx1, ny1);
                }
                int nx2 = x + dx * step;
                int ny2 = y + layer * step;
                if (checkPoint(nx2, ny2, dim)) {
                    return new Point(nx2, ny2);
                }
            }
            for (int dy = -layer + 1; dy < layer; dy++) {
                int nx1 = x - layer * step;
                int ny1 = y + dy * step;
                if (checkPoint(nx1, ny1, dim)) {
                    return new Point(nx1, ny1);
                }
                int nx2 = x + layer * step;
                int ny2 = y + dy * step;
                if (checkPoint(nx2, ny2, dim)) {
                    return new Point(nx2, ny2);
                }
            }
        }
        return null;
    }

    private boolean checkPoint(int px, int py, Dimension dim) {
        if (px < 0 || py < 0) {
            return false;
        }

        Dimension pref = getPreferredSize();
        if (px + dim.width > Math.max(pref.width, getWidth() + 1000)) {

        }
        return esPosicionValida(px, py, dim.width, dim.height, null);
    }

    /**
     * Valida si un rectángulo choca con otros componentes VISIBLES
     *
     * @param ignoreComponent Componente a ignorar (útil si estamos moviendo uno
     * existente)
     */
    public boolean esPosicionValida(int x, int y, int w, int h, Component ignoreComponent) {
        Rectangle rectNuevo = new Rectangle(x, y, w, h);

        for (Component c : getComponents()) {
            if (!c.isVisible() || c == ignoreComponent) {
                continue;
            }
            Rectangle r = c.getBounds();
            if (r.intersects(rectNuevo)) {
                return false;
            }
        }
        return true;
    }

    private void verificarExpansionTablero(Point p, Dimension d) {
        int maxX = p.x + d.width + 150;
        int maxY = p.y + d.height + 150;

        Dimension current = getPreferredSize();
        boolean cambiar = false;

        if (maxX > current.width) {
            current.width = maxX;
            cambiar = true;
        }
        if (maxY > current.height) {
            current.height = maxY;
            cambiar = true;
        }

        if (cambiar) {
            setPreferredSize(current);
            revalidate();

            if (getParent() != null) {
                getParent().doLayout();
                getParent().repaint();
            }
        }
    }

    public void setGruposDesdeDTO(List<GrupoDTO> gruposDTO) {
        removeAll();
        gruposVisuales.clear();

        if (gruposDTO != null) {
            int nextX = 10;
            int nextY = 10;
            int maxHeightInRow = 0;

            for (GrupoDTO dto : gruposDTO) {
                List<FichaDTO> fichasGrupo = (dto.getFichas() != null) ? dto.getFichas() : new ArrayList<>();
                UI_Grupo grupoPanel = new UI_Grupo(dto.getId(), fichasGrupo, this);

                Dimension size = grupoPanel.getPreferredSize();
                grupoPanel.setSize(size);

                if (nextX + size.width > getWidth()) {
                    nextX = 10;
                    nextY += maxHeightInRow + 10;
                    maxHeightInRow = 0;
                }

                grupoPanel.setLocation(nextX, nextY);
                nextX += size.width + 10;
                maxHeightInRow = Math.max(maxHeightInRow, size.height);

                gruposVisuales.add(grupoPanel);
                add(grupoPanel);
            }
        }
        revalidate();
        repaint();
    }

    public List<UI_Grupo> getGruposVisuales() {
        return new ArrayList<UI_Grupo>(gruposVisuales);
    }

    public UI_TurnoJugador getVentanaPrincipal() {
        return ventanaPrincipal;
    }

    public void removerFichasDeOtrosContenedores(List<FichaDTO> fichasMovidas, Object destino) {
        Set<String> ids = new HashSet<>();
        for (FichaDTO f : fichasMovidas) {
            ids.add(f.getId());
        }

        if (ventanaPrincipal != null) {
            UI_Mano mano = ventanaPrincipal.getUIMano();
            if (mano != null && mano != destino) {
                mano.removerFichasPorId(ids);
            }
        }

        for (UI_Grupo grupo : gruposVisuales) {
            if (grupo != destino) {
                grupo.removerFichasPorId(ids);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Busca un grupo visual por ID y marca todas sus fichas con borde de error.
     * También limpia los errores de los demás grupos.
     */
    public void marcarGrupoComoInvalido(String idGrupoInvalido) {

        for (UI_Grupo grupoVisual : gruposVisuales) {

            if (grupoVisual.getIdGrupo().equals(idGrupoInvalido)) {

                Set<String> todosLosIds = new HashSet<>();
                for (FichaDTO f : grupoVisual.getFichas()) {
                    todosLosIds.add(f.getId());
                }

                grupoVisual.marcarFichasInvalidas(todosLosIds);

            } else {
                grupoVisual.marcarFichasInvalidas(new HashSet<>());
            }
        }
        revalidate();
        repaint();
    }

    public void limpiarGruposInvalidos() {
        for (UI_Grupo grupoVisual : gruposVisuales) {
            grupoVisual.marcarFichasInvalidas(new java.util.HashSet<>());
        }
        revalidate();
        repaint();
    }
    
    /**
     * Busca en qué grupo visual se encuentra una ficha específica.
     * Retorna el ID del grupo o null si no está en el tablero.
     */
    public String obtenerIdGrupoDeFicha(String fichaId) {
        for (UI_Grupo g : gruposVisuales) {
            for (FichaDTO f : g.getFichas()) {
                if (f.getId().equals(fichaId)) {
                    return g.getIdGrupo();
                }
            }
        }
        return null; 
    }

}
