package itson.registrarjugador.vista;

import itson.registrarjugador.controlador.ControladorRegistro;
import itson.rummydtos.JugadorDTO;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class PanelConfirmacion extends JPanel {

    private ControladorRegistro controlador;
    private Image imagenFondo;
    
    private final Font fontTitulo = new Font("Arial", Font.BOLD, 36);
    private final Font fontTexto = new Font("Arial", Font.PLAIN, 20);
    private final Font fontBotones = new Font("Arial", Font.BOLD, 14);
    private final Color colorFondoPanel = new Color(210, 225, 235, 200);

    public PanelConfirmacion(ControladorRegistro controlador) {
        this.controlador = controlador;
        imagenFondo = cargarImagen("background.png");
        initComponents();
    }
    
    private Image cargarImagen(String nombre) {
        try {
            URL url = getClass().getResource("/" + nombre);
            return (url != null) ? new ImageIcon(url).getImage() : null;
        } catch (Exception e) { return null; }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }

    private void initComponents() {
        setLayout(new GridBagLayout()); 
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(colorFondoPanel);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        
        // OBTENER DATOS DEL MODELO
        JugadorDTO dto = controlador.getModelo().getJugadorTemporal();

        JLabel lblTitulo = new JLabel("CONFIRMAR DATOS");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));

        Image imgAvatar = cargarImagen(dto.getAvatarPath());
        if(imgAvatar != null) {
            AvatarComponent avatarShow = new AvatarComponent(dto.getAvatarPath(), imgAvatar, null);
            JPanel pCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pCenter.setOpaque(false);
            pCenter.add(avatarShow);
            panelContenido.add(pCenter);
        }
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel lblNombre = new JLabel("Jugador: " + dto.getNombre());
        lblNombre.setFont(fontTexto);
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblNombre);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel lblColores = new JLabel("Colores de Fichas:");
        lblColores.setFont(fontTexto);
        lblColores.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblColores);
        
        JPanel pnlColores = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlColores.setOpaque(false);
        if (dto.getColoresFichas() != null) {
            for(String c : dto.getColoresFichas()) {
                JPanel cuadrito = new JPanel();
                if(c != null) cuadrito.setBackground(Color.decode(c));
                cuadrito.setPreferredSize(new Dimension(40,40));
                cuadrito.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                pnlColores.add(cuadrito);
            }
        }
        panelContenido.add(pnlColores);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlBotones.setOpaque(false);

        JButton btnEditar = crearBoton("Editar", new Color(70, 130, 180)); 
        btnEditar.addActionListener(e -> controlador.volverAEdicion());

        JButton btnContinuar = crearBoton("CONTINUAR", new Color(34, 139, 34));
        btnContinuar.addActionListener(e -> controlador.confirmarYJugar());

        pnlBotones.add(btnEditar);
        pnlBotones.add(btnContinuar);
        
        panelContenido.add(pnlBotones);
        add(panelContenido);
    }
    
    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(fontBotones);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}