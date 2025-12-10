package itson.registrarjugador.vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * 
 * @author victoria
 */

public class AvatarComponent extends JComponent {

    private Image imagenOriginal;
    private BufferedImage imagenCircular;
    private boolean seleccionado;
    private String nombreArchivo;
    private final int diametro = 60; 
    private final Color colorBordeSeleccion = new Color(0, 120, 215);

    public interface AvatarClickListener {
        void onAvatarClicked(String nombreArchivo);
    }
    private AvatarClickListener listener;

    public AvatarComponent(String nombreArchivo, Image img, AvatarClickListener listener) {
        this.nombreArchivo = nombreArchivo;
        this.imagenOriginal = img;
        this.listener = listener;
        this.seleccionado = false;

        setOpaque(false);
        
        setPreferredSize(new Dimension(diametro + 8, diametro + 8));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearImagenCircular();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) listener.onAvatarClicked(nombreArchivo);
            }
        });
    }
    
    public String getNombreArchivo() { return nombreArchivo; }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
    }

    private void crearImagenCircular() {
        if (imagenOriginal == null) return;
        imagenCircular = new BufferedImage(diametro, diametro, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagenCircular.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D.Double circulo = new Ellipse2D.Double(0, 0, diametro, diametro);
        g2.setClip(circulo);
        g2.drawImage(imagenOriginal, 0, 0, diametro, diametro, null);
        g2.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 4; int y = 4;

        if (imagenCircular != null) {
            g2.drawImage(imagenCircular, x, y, null);
        }

        if (seleccionado) {
            g2.setColor(colorBordeSeleccion);
            g2.setStroke(new BasicStroke(3)); 
            g2.drawOval(x, y, diametro, diametro);
        }
        g2.dispose();
    }
}