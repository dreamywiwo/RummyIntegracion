package itson.ejercerturno.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel que representa el avatar de un jugador.
 * Dibuja un círculo de estado (verde/azul) 
 */
public class UI_Jugador extends JPanel {

    private String nombre;
    private Image avatar;
    private boolean esTurno; 
    private int numeroFichas = 0; 

    private static final Color COLOR_TURNO_ACTIVO = new Color(88, 194, 78); 
    private static final Color COLOR_INACTIVO = new Color(98, 145, 184);       
    private static final Color COLOR_FONDO_FICHA = new Color(210, 246, 246); 
    private static final Color COLOR_TEXTO_FICHA = new Color(64, 70, 89);

    public UI_Jugador(String nombre, String rutaAvatar) {
        this.nombre = nombre;
        this.esTurno = false;
        
        setOpaque(false); 
        setPreferredSize(new Dimension(130, 130));

        cargarAvatar(rutaAvatar);
    }

    private void cargarAvatar(String ruta) {
        try {
            if (ruta != null && getClass().getResource(ruta) != null) {
                this.avatar = new ImageIcon(getClass().getResource(ruta)).getImage();
            } else {
                this.avatar = null; 
            }
        } catch (Exception e) {
            System.err.println("Error cargando avatar: " + ruta);
        }
    }

    public void setEsTurno(boolean esTurno) {
        this.esTurno = esTurno;
        repaint(); 
    }

    public void setNumeroFichas(int n) {
        this.numeroFichas = n;
        repaint(); 
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        setToolTipText(nombre); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        
        int size = Math.min(w, h) - 10; 
        int x = (w - size) / 2;
        int y = (h - size) / 2;

        if (esTurno) {
            g2.setColor(COLOR_TURNO_ACTIVO);
        } else {
            g2.setColor(COLOR_INACTIVO);
        }
        g2.fillOval(x, y, size, size);

        if (avatar != null) {
            int borderThickness = 6;
            int avatarSize = size - (borderThickness * 2);
            int avatarX = x + borderThickness;
            int avatarY = y + borderThickness;

            java.awt.Shape clipOriginal = g2.getClip();

            g2.setClip(new Ellipse2D.Float(avatarX, avatarY, avatarSize, avatarSize));
            g2.drawImage(avatar, avatarX, avatarY, avatarSize, avatarSize, this);

            g2.setClip(clipOriginal);
        }

        int badgeSize = 35;
        int badgeX = x + size - badgeSize; 
        int badgeY = y + size - badgeSize;

        g2.setColor(COLOR_FONDO_FICHA);
        g2.fillOval(badgeX, badgeY, badgeSize, badgeSize);

        g2.setColor(COLOR_TEXTO_FICHA);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        String numStr = String.valueOf(numeroFichas);
        FontMetrics fm = g2.getFontMetrics();
        int textX = badgeX + (badgeSize - fm.stringWidth(numStr)) / 2;
        int textY = badgeY + ((badgeSize - fm.getHeight()) / 2) + fm.getAscent();
        
        g2.drawString(numStr, textX, textY);
    }
}