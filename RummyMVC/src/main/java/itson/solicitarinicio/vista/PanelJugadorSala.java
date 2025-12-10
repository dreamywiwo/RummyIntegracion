package itson.solicitarinicio.vista;

import itson.rummydtos.JugadorDTO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel visual para representar a un jugador en la sala.
 * Estilo visual: Tarjeta redondeada con avatar circular.
 */
public class PanelJugadorSala extends JPanel {
    
    public PanelJugadorSala(JugadorDTO jugador) {
        setOpaque(false); 
        setPreferredSize(new Dimension(220, 80)); 
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblAvatar = new JLabel();
        try {
            String ruta = "/" + jugador.getAvatarPath();
            URL url = getClass().getResource(ruta);
            
            if(url == null) url = getClass().getResource("/" + jugador.getAvatarPath());

            if (url != null) {
                ImageIcon iconOriginal = new ImageIcon(url);
                ImageIcon iconCircular = crearAvatarCircular(iconOriginal.getImage(), 60); 
                lblAvatar.setIcon(iconCircular);
            } else {
                lblAvatar.setText("?");
                lblAvatar.setForeground(Color.WHITE);
                lblAvatar.setFont(new Font("Arial", Font.BOLD, 24));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar avatar: " + jugador.getAvatarPath());
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2; 
        gbc.insets = new Insets(0, 10, 0, 10); 
        gbc.anchor = GridBagConstraints.WEST;
        add(lblAvatar, gbc);

        JLabel lblNombre = new JLabel(jugador.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(Color.WHITE);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1.0; 
        gbc.insets = new Insets(5, 0, 0, 10);
        gbc.anchor = GridBagConstraints.SOUTHWEST; 
        add(lblNombre, gbc);

        JLabel lblEstado;
        if (jugador.isListo()) {
            lblEstado = new JLabel("Listo!");
            lblEstado.setForeground(new Color(100, 255, 100)); 
        } else {
            lblEstado = new JLabel("Esperando...");
            lblEstado.setForeground(new Color(200, 200, 200)); 
        }
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 5, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        add(lblEstado, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.dispose();
        super.paintComponent(g);
    }

    private ImageIcon crearAvatarCircular(Image image, int size) {
        BufferedImage avatar = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = avatar.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(image, 0, 0, size, size, null);
        
        g2.setClip(null);
        g2.setColor(new Color(255, 255, 255, 200));
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawOval(1, 1, size-2, size-2);

        g2.dispose();
        return new ImageIcon(avatar);
    }
}