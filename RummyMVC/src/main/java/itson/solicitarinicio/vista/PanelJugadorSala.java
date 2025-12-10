package itson.solicitarinicio.vista;

import itson.rummydtos.JugadorDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelJugadorSala extends JPanel {
    
    public PanelJugadorSala(JugadorDTO jugador) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(200, 100));

        JLabel lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        
        try {
            URL url = getClass().getResource("/" + jugador.getAvatarPath());
            if (url != null) {
                ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
                lblAvatar.setIcon(icon);
            }
        } catch (Exception e) {}
        
        add(lblAvatar, BorderLayout.CENTER);

        JLabel lblNombre = new JLabel(jugador.getNombre());
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        
        add(lblNombre, BorderLayout.SOUTH);
    }
}