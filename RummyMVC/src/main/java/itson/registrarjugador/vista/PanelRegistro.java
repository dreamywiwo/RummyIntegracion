package itson.registrarjugador.vista;

import itson.registrarjugador.controlador.ControladorRegistro;
import itson.rummydtos.JugadorDTO;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PanelRegistro extends JPanel {

    private ControladorRegistro controlador;
    private JTextField txtNombre;
    private List<AvatarComponent> listaAvataresVisuales;
    private String avatarSeleccionado;
    private List<TileComponent> fichasVisuales; 
    private List<String> coloresSeleccionados;
    private Image imagenFondo;
    
    private static final String RUTA_IMAGENES = "/"; 

    private final Font fontTitulo = new Font("Arial", Font.BOLD, 36);
    private final Font fontLabel = new Font("Arial", Font.BOLD, 20);
    private final Font fontInput = new Font("Arial", Font.PLAIN, 20);
    private final Font fontBotones = new Font("Arial", Font.BOLD, 14);

    public PanelRegistro(ControladorRegistro controlador) {
        this.controlador = controlador;
        this.coloresSeleccionados = new ArrayList<>();
        for(int i=0; i<4; i++) coloresSeleccionados.add(null);
        this.listaAvataresVisuales = new ArrayList<>();
        
        imagenFondo = cargarImagen("background.png");
        
        initComponents();
    }
    
    public void cargarDatosPrevios(JugadorDTO dto) {
        if (dto == null) return;
        if(dto.getNombre() != null) txtNombre.setText(dto.getNombre());
        if (dto.getAvatarPath() != null) seleccionarAvatar(dto.getAvatarPath());
        
        if (dto.getColoresFichas() != null) {
            for (int i = 0; i < dto.getColoresFichas().size(); i++) {
                if (i < 4) {
                    String color = dto.getColoresFichas().get(i);
                    coloresSeleccionados.set(i, color);
                    if(color != null && i < fichasVisuales.size()) {
                        fichasVisuales.get(i).setColorFicha(color);
                    }
                }
            }
        }
    }

    private Image cargarImagen(String nombreArchivo) {
        try {
            String ruta = RUTA_IMAGENES + nombreArchivo;
            URL url = getClass().getResource(ruta);
            
            if (url == null) {
                System.err.println("ERROR: No se encontró la imagen en: " + ruta);
                return null; 
            }
            
            return new ImageIcon(url).getImage();
        } catch (Exception e) { 
            e.printStackTrace();
            return null; 
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }

    private void seleccionarAvatar(String nombreArchivo) {
        this.avatarSeleccionado = nombreArchivo;
        for (AvatarComponent avComp : listaAvataresVisuales) {
            avComp.setSeleccionado(avComp.getNombreArchivo().equals(nombreArchivo));
        }
    }

    private void initComponents() {
        setLayout(new GridBagLayout()); 
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(new Color(210, 225, 235, 200));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel lblTitulo = new JLabel("REGISTRAR JUGADOR");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel lblAvatar = new JLabel("Selecciona tu Avatar:");
        lblAvatar.setFont(fontLabel);
        lblAvatar.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblAvatar);
        
        JPanel pnlAvatares = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlAvatares.setOpaque(false);
        
        List<String> listaAvatares = controlador.getModelo().getAvataresDisponibles();

        for (String nombreArchivo : listaAvatares) {
            Image img = cargarImagen(nombreArchivo);
            if (img != null) {
                AvatarComponent avatarVisual = new AvatarComponent(nombreArchivo, img, this::seleccionarAvatar);
                listaAvataresVisuales.add(avatarVisual);
                pnlAvatares.add(avatarVisual);
            }
        }
        panelContenido.add(pnlAvatares);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblUser = new JLabel("Nombre de Usuario:");
        lblUser.setFont(fontLabel);
        lblUser.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblUser);
        
        txtNombre = new JTextField();
        txtNombre.setFont(fontInput);
        txtNombre.setMaximumSize(new Dimension(350, 40));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        panelContenido.add(txtNombre);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblColors = new JLabel("Color de tus fichas:");
        lblColors.setFont(fontLabel);
        lblColors.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblColors);
        
        JPanel pnlFichas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlFichas.setOpaque(false);
        fichasVisuales = new ArrayList<>(); 
        
        for (int i = 0; i < 4; i++) {
            int indice = i;
            TileComponent fichaVisual = new TileComponent(indice);
            fichaVisual.setPreferredSize(new Dimension(60, 90)); 
            
            fichaVisual.addActionListener(e -> abrirSelectorColor(indice, fichaVisual));
            fichasVisuales.add(fichaVisual);
            pnlFichas.add(fichaVisual);
        }
        panelContenido.add(pnlFichas);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 35)));

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.setMaximumSize(new Dimension(500, 60));

        JButton btnRegresar = new JButton("REGRESAR");
        configurarEstiloBoton(btnRegresar, Color.GRAY);
        btnRegresar.addActionListener(e -> {
             // Opcional: controlador.volverAlMenu();
             JOptionPane.showMessageDialog(this, "Volver al menú (implementar en controlador)");
        });

        JButton btnSiguiente = new JButton("SIGUIENTE");
        configurarEstiloBoton(btnSiguiente, new Color(34, 139, 34));
        btnSiguiente.addActionListener(e -> accionSiguiente());

        pnlBotones.add(btnRegresar);
        pnlBotones.add(btnSiguiente);
        panelContenido.add(pnlBotones);

        add(panelContenido);
    }

    private void configurarEstiloBoton(JButton btn, Color color) {
        btn.setFont(fontBotones);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void abrirSelectorColor(int index, TileComponent fichaOrigen) {
        List<String> ocupados = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != index && coloresSeleccionados.get(i) != null) {
                ocupados.add(coloresSeleccionados.get(i));
            }
        }
        
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        DialogoSelectorColor dialogo = new DialogoSelectorColor(
            parentFrame, 
            controlador.getModelo().getColoresSistema(), 
            ocupados
        );
        dialogo.setVisible(true);

        if (dialogo.getColorSeleccionado() != null) {
            String nuevoColor = dialogo.getColorSeleccionado();
            coloresSeleccionados.set(index, nuevoColor);
            fichaOrigen.setColorFicha(nuevoColor);
        }
    }

    private void accionSiguiente() {
        String nombre = txtNombre.getText().trim();
        if (avatarSeleccionado == null) { mostrarError("Selecciona un avatar."); return; }
        if (coloresSeleccionados.contains(null)) { mostrarError("Elige color para todas las fichas."); return; }

        // Delegamos al controlador
        controlador.irAConfirmacion(nombre, avatarSeleccionado, new ArrayList<>(coloresSeleccionados));
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }
}