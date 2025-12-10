package itson.registrarjugador.vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * 
 * @author victoria
 */

public class DialogoSelectorColor extends JDialog {

    private String colorSeleccionado = null;
    private List<String> coloresSistema;
    private List<String> coloresOcupados;

    public DialogoSelectorColor(JFrame parent, List<String> coloresSistema, List<String> coloresOcupados) {
        super(parent, "Seleccionar Color", true);
        this.coloresSistema = coloresSistema;
        this.coloresOcupados = coloresOcupados;
        
        initComponents();
        setSize(400, 200);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridLayout(2, 4, 10, 10));
        
        for (String hexColor : coloresSistema) {
            JButton btnColor = new JButton();
            btnColor.setBackground(Color.decode(hexColor));
            
            btnColor.setOpaque(true);
            btnColor.setBorderPainted(false);
            btnColor.setFocusPainted(false);
            
            if (coloresOcupados.contains(hexColor)) {
                btnColor.setEnabled(false);
                btnColor.setBackground(Color.GRAY);
            } else {
                btnColor.addActionListener(e -> {
                    this.colorSeleccionado = hexColor;
                    dispose();
                });
            }
            add(btnColor);
        }
    }

    public String getColorSeleccionado() { return colorSeleccionado; }
}