package itson.ejercerturno.vista;

import itson.rummypresentacion.utils.FichaTransferable;
import itson.rummypresentacion.utils.ContenedorFichas;
import itson.rummydtos.FichaDTO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class UI_Ficha extends JLabel {

    private FichaDTO ficha;
    private ContenedorFichas contenedor;
    private static final int RADIO_BORDE = 15;

    private double escalaActual = 1.0;

    private boolean seleccionada = false;
    private boolean tieneError = false;
    
    // Lista de colores personalizados (Hex Strings)
    private List<String> paletaColores; 

    // --- CONSTRUCTORES ---

    public UI_Ficha(FichaDTO ficha) {
        this(ficha, null, 1.0, null);
    }

    public UI_Ficha(FichaDTO ficha, ContenedorFichas contenedor) {
        this(ficha, contenedor, 1.0, null);
    }
    
    // Constructor completo con paleta de colores
    public UI_Ficha(FichaDTO ficha, ContenedorFichas contenedor, double escalaActual, List<String> paletaColores) {
        this.ficha = ficha;
        this.contenedor = contenedor;
        this.escalaActual = escalaActual;
        this.paletaColores = paletaColores;
        
        configurarComponente();
        habilitarDragAndDrop();
        habilitarSeleccion();
    }

    private void configurarComponente() {
        setOpaque(false);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setPreferredSize(new Dimension(60, 80));
        setBorder(new EmptyBorder(5, 5, 5, 5)); 
        actualizarVisual();
    }

    private void habilitarSeleccion() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getParent() instanceof UI_Mano) {
                    toggleSeleccion();
                }
            }
        });
    }

    public void toggleSeleccion() {
        this.seleccionada = !this.seleccionada;

        // Avisar a la mano que esta ficha cambió de estado
        if (getParent() instanceof UI_Mano) {
            ((UI_Mano) getParent()).notificarSeleccion(this);
        }
        repaint();
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
        repaint();
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    // --- PINTURA DEL COMPONENTE ---

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int radioBordeEscalado = (int) (RADIO_BORDE * escalaActual);
        float grosorBorde = (float) (2f * escalaActual);
        float grosorDestacado = (float) (4f * escalaActual); 

        // 1. Sombra
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fill(new RoundRectangle2D.Float(3, 3, width - 3, height - 3, radioBordeEscalado, radioBordeEscalado));

        // 2. Fondo (Usamos lógica de color de fondo)
        g2d.setColor(obtenerColorFondo());
        g2d.fill(new RoundRectangle2D.Float(0, 0, width - 3, height - 3, radioBordeEscalado, radioBordeEscalado));

        // 3. Borde (Selección, Error o Normal)
        if (tieneError) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(grosorDestacado));
        } else if (seleccionada) {
            g2d.setColor(new Color(50, 205, 50));
            g2d.setStroke(new BasicStroke(grosorDestacado));
        } else {
            g2d.setColor(obtenerColorBorde());
            g2d.setStroke(new BasicStroke(grosorBorde));
        }

        g2d.draw(new RoundRectangle2D.Float(1, 1, width - 4, height - 4, radioBordeEscalado, radioBordeEscalado));

        g2d.dispose();

        // Dibuja el texto del JLabel encima del fondo que acabamos de pintar
        super.paintComponent(g);
    }

    // --- GESTIÓN DE COLORES (SKINS) ---

    private void actualizarVisual() {
        int tamanoFuenteBase = ficha.isEsComodin() ? 36 : 32;
        int tamanoFuenteEscalado = (int) (tamanoFuenteBase * escalaActual);

        if (tamanoFuenteEscalado < 8) {
            tamanoFuenteEscalado = 8;
        }

        if (ficha.isEsComodin()) {
            setText("☺"); // Símbolo de comodín
            setFont(new Font("Arial", Font.BOLD, tamanoFuenteEscalado));
            setForeground(new Color(139, 69, 19));
        } else {
            setText(String.valueOf(ficha.getNumero()));
            setFont(new Font("Arial", Font.BOLD, tamanoFuenteEscalado));
            
            // AQUÍ APLICAMOS EL COLOR PERSONALIZADO AL TEXTO (NÚMERO)
            setForeground(obtenerColorTextoPersonalizado());
        }
        setToolTipText(ficha.toString());
    }

    /**
     * Obtiene el color del texto basado en la paleta personalizada.
     */
    private Color obtenerColorTextoPersonalizado() {
        // Si no hay paleta o es inválida, usar defaults
        if (paletaColores == null || paletaColores.size() < 4) {
            return obtenerColorTextoDefault();
        }

        try {
            switch (ficha.getColor().toUpperCase()) {
                case "ROJO":     return Color.decode(paletaColores.get(0));
                case "AZUL":     return Color.decode(paletaColores.get(1));
                case "NEGRO":    return Color.decode(paletaColores.get(2));
                case "AMARILLO": return Color.decode(paletaColores.get(3));
                default:         return new Color(139, 69, 19);
            }
        } catch (Exception e) {
            // Si falla el decode del hex, volver al default
            return obtenerColorTextoDefault();
        }
    }

    /**
     * Colores originales (Hardcoded) por si no hay skin.
     */
    private Color obtenerColorTextoDefault() {
        switch (ficha.getColor().toUpperCase()) {
            case "ROJO":     return new Color(220, 140, 90);
            case "AZUL":     return new Color(100, 149, 237);
            case "AMARILLO": return new Color(218, 165, 32);
            case "NEGRO":    return new Color(105, 105, 105);
            default:         return new Color(139, 69, 19);
        }
    }

    private Color obtenerColorFondo() {
        if (ficha.isEsComodin()) {
            return new Color(255, 228, 181);
        }
        // Fondos muy claros para resaltar el texto de color
        return new Color(255, 253, 240); 
    }

    /**
     * El borde también se pinta del color del tema, pero con transparencia.
     */
    private Color obtenerColorBorde() {
        Color baseColor = obtenerColorTextoPersonalizado();
        // Crear un color semitransparente (alpha 150) basado en el color principal
        return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 150);
    }

    // --- DRAG AND DROP ---

    private void habilitarDragAndDrop() {
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(
                this,
                DnDConstants.ACTION_MOVE,
                new DragGestureListener() {
            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                if (!isEnabled() || (getParent() != null && !getParent().isEnabled())) {
                    return;
                }
                FichaTransferable transferable;

                if (getParent() instanceof UI_Mano) {
                    UI_Mano mano = (UI_Mano) getParent();
                    List<FichaDTO> listaParaMover = mano.obtenerFichasParaMover(UI_Ficha.this);
                    transferable = new FichaTransferable(listaParaMover, contenedor);
                } else {
                    List<FichaDTO> sola = new ArrayList<>();
                    sola.add(ficha);
                    transferable = new FichaTransferable(sola, contenedor);
                }

                dge.startDrag(
                        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
                        transferable,
                        new DragSourceAdapter() {
                }
                );
            }
        });
    }

    // --- GETTERS Y SETTERS ---

    public void setContenedor(ContenedorFichas contenedor) {
        this.contenedor = contenedor;
    }

    public void setBordeError(boolean error) {
        this.tieneError = error;
        repaint();
    }
    
    public boolean tieneError() {
        return tieneError;
    }

    public FichaDTO getFicha() {
        return ficha;
    }
}