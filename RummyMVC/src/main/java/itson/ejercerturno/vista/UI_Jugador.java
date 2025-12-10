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
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

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

    public void setAvatarPath(String ruta) {
        cargarAvatar(ruta);
        repaint();
    }

    private void cargarAvatar(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            this.avatar = null;
            return;
        }

        try {
            URL url = null;
            
            url = getClass().getResource(ruta);
            if (url == null && !ruta.startsWith("/")) {
                url = getClass().getResource("/" + ruta);
            }
            if (url == null) {
                String rutaLimpia = ruta.startsWith("/") ? ruta : "/" + ruta;
                url = getClass().getResource("/imagenes" + rutaLimpia);
            }

            if (url != null) {
                this.avatar = new ImageIcon(url).getImage();
            } else {
                this.avatar = null; 
            }
        } catch (Exception e) {
            System.err.println("Error cargando avatar: " + ruta);
            this.avatar = null;
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
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        
        int size = Math.min(w, h) - 20; 
        int x = (w - size) / 2;
        int y = (h - size) / 2 - 10; 

        if (esTurno) {
            g2.setColor(COLOR_TURNO_ACTIVO);
        } else {
            g2.setColor(COLOR_INACTIVO);
        }
        g2.fillOval(x, y, size, size);

        int borderThickness = 6;
        int avatarSize = size - (borderThickness * 2);
        int avatarX = x + borderThickness;
        int avatarY = y + borderThickness;

        if (avatar != null) {
            java.awt.Shape clipOriginal = g2.getClip();
            g2.setClip(new Ellipse2D.Float(avatarX, avatarY, avatarSize, avatarSize));
            g2.drawImage(avatar, avatarX, avatarY, avatarSize, avatarSize, this);
            g2.setClip(clipOriginal);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillOval(avatarX, avatarY, avatarSize, avatarSize);
            
            g2.setColor(COLOR_TEXTO_FICHA);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
            String inicial = (nombre != null && !nombre.isEmpty()) ? nombre.substring(0, 1).toUpperCase() : "?";
            
            FontMetrics fm = g2.getFontMetrics();
            int textX = avatarX + (avatarSize - fm.stringWidth(inicial)) / 2;
            int textY = avatarY + ((avatarSize - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(inicial, textX, textY);
        }

        int badgeSize = 35;
        int badgeX = x + size - badgeSize; 
        int badgeY = y + size - badgeSize;

        g2.setColor(COLOR_FONDO_FICHA);
        g2.fillOval(badgeX, badgeY, badgeSize, badgeSize);
        g2.setColor(new Color(0,0,0,50)); 
        g2.drawOval(badgeX, badgeY, badgeSize, badgeSize);

        g2.setColor(COLOR_TEXTO_FICHA);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        String numStr = String.valueOf(numeroFichas);
        FontMetrics fm = g2.getFontMetrics();
        int textX = badgeX + (badgeSize - fm.stringWidth(numStr)) / 2;
        int textY = badgeY + ((badgeSize - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(numStr, textX, textY);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        FontMetrics fmNombre = g2.getFontMetrics();
        String nombreMostrar = (nombre != null) ? nombre : "Jugador";
        int nombreX = (w - fmNombre.stringWidth(nombreMostrar)) / 2;
        int nombreY = y + size + 20; 
        
        g2.setColor(new Color(0,0,0,150));
        g2.drawString(nombreMostrar, nombreX + 1, nombreY + 1);
        
        g2.setColor(Color.WHITE);
        g2.drawString(nombreMostrar, nombreX, nombreY);
    }
}