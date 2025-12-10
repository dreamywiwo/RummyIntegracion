package itson.registrarjugador.vista;

import java.awt.*;
import javax.swing.JButton;

/**
 * 
 * @author victoria
 */
public class TileComponent extends JButton {

    private Color colorFicha;
    private String textoCentro;
    private int indice;
    private boolean tieneColorAsignado;

    public TileComponent(int indice) {
        this.indice = indice;
        this.textoCentro = "?";
        this.colorFicha = Color.LIGHT_GRAY;
        this.tieneColorAsignado = false;

        setPreferredSize(new Dimension(60, 90));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setColorFicha(String hexColor) {
        this.colorFicha = Color.decode(hexColor);
        this.textoCentro = String.valueOf(indice + 1); 
        this.tieneColorAsignado = true;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();

        g2.setColor(new Color(245, 245, 240)); 
        g2.fillRoundRect(0, 0, ancho, alto, 15, 15);

        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(0, 0, ancho - 1, alto - 1, 15, 15);
        
        if (tieneColorAsignado) {
            g2.setColor(colorFicha);
        } else {
            g2.setColor(Color.GRAY);
        }
        
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2.getFontMetrics();
        
        int x = (ancho - fm.stringWidth(textoCentro)) / 2;
        int y = ((alto - fm.getHeight()) / 2) + fm.getAscent();
        
        g2.drawString(textoCentro, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("G" + (indice + 1), 5, 15);

        g2.dispose();
    }
}