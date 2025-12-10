/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package itson.ejercerturno.vista;

import itson.ejercerturno.controlador.ControladorTurno;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.GrupoDTO;
import itson.rummydtos.JugadorDTO;
import itson.ejercerturno.modelo.IObserver;
import itson.rummypresentacion.utils.SlideWrapper;
import itson.rummypresentacion.utils.TableroSlideWrapper;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import itson.ejercerturno.modelo.IModeloEjercerTurno;
import itson.rummypresentacion.utils.TipoVista;

/**
 *
 * @author Dana Chavez
 */
public class UI_TurnoJugador extends javax.swing.JFrame implements IObserver {

    private UI_Tablero uiTablero;
    private UI_Mano uiMano;
    private ControladorTurno controlador;
    private String ultimoMensajeError = null;
    private javax.swing.JLabel lblContadorSopa;

    private JugadorDTO jugadorLocal;
    private String jugadorID;
    private UI_Jugador uiJugadorActual;
    private UI_Jugador uiJugadorOponente;

    /**
     * Creates new form UI_TurnoJugador
     */
    public UI_TurnoJugador(ControladorTurno controlador, JugadorDTO jugadorLocal) {
        initComponents();
        this.controlador = controlador;
        this.jugadorLocal = jugadorLocal;
        this.jugadorID = jugadorLocal.getId();

        this.setSize(1163, 784);

        configurarPanelSopa();
        configurarPanelesJugadores();

        PanelBloqueo glassPane = new PanelBloqueo();
        setGlassPane(glassPane);

        uiTablero = new UI_Tablero(this);
        setLocationRelativeTo(null);

        TableroSlideWrapper tableroDraggable = new TableroSlideWrapper(uiTablero);

        jPanelContenedorTablero.setLayout(new BorderLayout());

        jPanelContenedorTablero.add(tableroDraggable, BorderLayout.CENTER);

        uiMano = new UI_Mano(uiTablero, null);
        SlideWrapper manoDeslizable = new SlideWrapper(uiMano);
        jPanelContenedorMano.setLayout(new BorderLayout());
        jPanelContenedorMano.add(manoDeslizable, BorderLayout.CENTER);

        jPanelContenedorJugador.setOpaque(false);
        jPanelContenedorJugador1.setOpaque(false);
        jPanelContenedorJugador2.setOpaque(false);
        jPanelContenedorJugador3.setOpaque(false);
        jPanelContenedorTablero.setOpaque(false);
        jPanelSopa.setOpaque(false);

        configurarEventosBotones();
        actualizarEstadoBotones(false);
        glassPane.setVisible(true);
    }

    /**
     * Configura los listeners de los botones
     */
    private void configurarEventosBotones() {
        jButtonTerminarTurno.addActionListener(e -> controlador.terminarTurno());
        jButtonTomarFicha.addActionListener(e -> {
            controlador.tomarFicha();
        });
    }

    /**
     * Notifica al controlador que se creó un grupo
     *
     * @param fichas Lista de fichas del nuevo grupo
     */
    public void notificarGrupoCreado(List<FichaDTO> fichas) {
        controlador.crearGrupo(fichas);
    }

    public void notificarGrupoActualizado(String idGrupo, List<FichaDTO> fichas) {
        controlador.actualizarGrupo(idGrupo, fichas);
    }
    
    public void solicitarDevolverFicha(String grupoId, String fichaId) {
        controlador.devolverFicha(grupoId, fichaId); 
    }

    public UI_Tablero getUITablero() {
        return uiTablero;
    }

    public UI_Mano getUIMano() {
        return uiMano;
    }

    public String getJugadorID() {
        return jugadorID;
    }

    public ControladorTurno getControlador() {
        return controlador;
    }

    private void actualizarEstadoBotones(IModeloEjercerTurno modelo) {
        boolean esMiTurno = modelo.esTurnoDe(jugadorLocal.getId());
        actualizarEstadoBotones(esMiTurno);
    }

    private void actualizarEstadoBotones(boolean activo) {
        jButtonTomarFicha.setEnabled(activo);
        jButtonTerminarTurno.setEnabled(activo);

        if (uiMano != null) {
            uiMano.actualizarEstado(activo);
        }
    }

    private void configurarPanelSopa() {
        jPanelSopa.setLayout(new java.awt.BorderLayout());

        lblContadorSopa = new javax.swing.JLabel("??");

        try {
            java.net.URL imgURL = getClass().getResource("/sopaIcon.png");

            if (imgURL != null) {
                javax.swing.ImageIcon iconOriginal = new javax.swing.ImageIcon(imgURL);

                java.awt.Image imgEscalada = iconOriginal.getImage()
                        .getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);

                lblContadorSopa.setIcon(new javax.swing.ImageIcon(imgEscalada));
            } else {
                System.err.println("No se encontró la imagen: /sopaIcon.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lblContadorSopa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblContadorSopa.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblContadorSopa.setIconTextGap(10);

        lblContadorSopa.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        lblContadorSopa.setForeground(java.awt.Color.decode("#2F3437"));

        jPanelSopa.add(lblContadorSopa, java.awt.BorderLayout.CENTER);
        jPanelSopa.setOpaque(false);
    }

    private void configurarPanelesJugadores() {
        jPanelContenedorJugador.removeAll();
        jPanelContenedorJugador1.removeAll();

        jPanelContenedorJugador.setLayout(new java.awt.BorderLayout());
        jPanelContenedorJugador1.setLayout(new java.awt.BorderLayout());

        uiJugadorActual = new UI_Jugador(jugadorLocal.getNombre(), jugadorLocal.getAvatarPath());
        uiJugadorActual.setEsTurno(false);
        uiJugadorActual.setNumeroFichas(14); 
        jPanelContenedorJugador.add(uiJugadorActual, java.awt.BorderLayout.CENTER);

        String avatarRival = "/imageBun.png".equals(jugadorLocal.getAvatarPath())
                ? "/imageFish.png" : "/imageBun.png";

        uiJugadorOponente = new UI_Jugador("Rival", avatarRival);
        uiJugadorOponente.setEsTurno(false);
        uiJugadorOponente.setNumeroFichas(14);

        jPanelContenedorJugador1.add(uiJugadorOponente, java.awt.BorderLayout.CENTER);

        jPanelContenedorJugador1.setVisible(true);

        jPanelContenedorJugador2.setVisible(false);
        jPanelContenedorJugador3.setVisible(false);

        this.revalidate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSopa = new javax.swing.JPanel();
        jButtonTerminarTurno = new javax.swing.JButton();
        jButtonTomarFicha = new javax.swing.JButton();
        jPanelContenedorJugador3 = new javax.swing.JPanel();
        jPanelContenedorJugador2 = new javax.swing.JPanel();
        jPanelContenedorJugador1 = new javax.swing.JPanel();
        jPanelContenedorJugador = new javax.swing.JPanel();
        jPanelContenedorTablero = new javax.swing.JPanel();
        jPanelDecoracionMano = new javax.swing.JPanel();
        jPanelBaseMano = new javax.swing.JPanel();
        jPanelContenedorMano = new javax.swing.JPanel();
        jLabelFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1150, 743));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelSopa.setPreferredSize(new java.awt.Dimension(90, 90));

        javax.swing.GroupLayout jPanelSopaLayout = new javax.swing.GroupLayout(jPanelSopa);
        jPanelSopa.setLayout(jPanelSopaLayout);
        jPanelSopaLayout.setHorizontalGroup(
            jPanelSopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanelSopaLayout.setVerticalGroup(
            jPanelSopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelSopa, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, 120, 50));

        jButtonTerminarTurno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/terminarButton_1.png"))); // NOI18N
        jButtonTerminarTurno.setBorder(null);
        jButtonTerminarTurno.setBorderPainted(false);
        jButtonTerminarTurno.setContentAreaFilled(false);
        jButtonTerminarTurno.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jButtonTerminarTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 580, 100, 40));

        jButtonTomarFicha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomarButton_1.png"))); // NOI18N
        jButtonTomarFicha.setBorder(null);
        jButtonTomarFicha.setBorderPainted(false);
        jButtonTomarFicha.setContentAreaFilled(false);
        jButtonTomarFicha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonTomarFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTomarFichaActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonTomarFicha, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 630, 100, 100));

        jPanelContenedorJugador3.setPreferredSize(new java.awt.Dimension(125, 125));

        javax.swing.GroupLayout jPanelContenedorJugador3Layout = new javax.swing.GroupLayout(jPanelContenedorJugador3);
        jPanelContenedorJugador3.setLayout(jPanelContenedorJugador3Layout);
        jPanelContenedorJugador3Layout.setHorizontalGroup(
            jPanelContenedorJugador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelContenedorJugador3Layout.setVerticalGroup(
            jPanelContenedorJugador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelContenedorJugador3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, -1));

        jPanelContenedorJugador2.setPreferredSize(new java.awt.Dimension(125, 125));

        javax.swing.GroupLayout jPanelContenedorJugador2Layout = new javax.swing.GroupLayout(jPanelContenedorJugador2);
        jPanelContenedorJugador2.setLayout(jPanelContenedorJugador2Layout);
        jPanelContenedorJugador2Layout.setHorizontalGroup(
            jPanelContenedorJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelContenedorJugador2Layout.setVerticalGroup(
            jPanelContenedorJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelContenedorJugador2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jPanelContenedorJugador1.setPreferredSize(new java.awt.Dimension(125, 125));

        javax.swing.GroupLayout jPanelContenedorJugador1Layout = new javax.swing.GroupLayout(jPanelContenedorJugador1);
        jPanelContenedorJugador1.setLayout(jPanelContenedorJugador1Layout);
        jPanelContenedorJugador1Layout.setHorizontalGroup(
            jPanelContenedorJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelContenedorJugador1Layout.setVerticalGroup(
            jPanelContenedorJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelContenedorJugador1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jPanelContenedorJugador.setPreferredSize(new java.awt.Dimension(125, 125));

        javax.swing.GroupLayout jPanelContenedorJugadorLayout = new javax.swing.GroupLayout(jPanelContenedorJugador);
        jPanelContenedorJugador.setLayout(jPanelContenedorJugadorLayout);
        jPanelContenedorJugadorLayout.setHorizontalGroup(
            jPanelContenedorJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelContenedorJugadorLayout.setVerticalGroup(
            jPanelContenedorJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelContenedorJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 550, -1, -1));

        jPanelContenedorTablero.setPreferredSize(new java.awt.Dimension(803, 448));

        javax.swing.GroupLayout jPanelContenedorTableroLayout = new javax.swing.GroupLayout(jPanelContenedorTablero);
        jPanelContenedorTablero.setLayout(jPanelContenedorTableroLayout);
        jPanelContenedorTableroLayout.setHorizontalGroup(
            jPanelContenedorTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
        );
        jPanelContenedorTableroLayout.setVerticalGroup(
            jPanelContenedorTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelContenedorTablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, -1, 460));

        jPanelDecoracionMano.setBackground(new java.awt.Color(22, 24, 54));
        jPanelDecoracionMano.setPreferredSize(new java.awt.Dimension(739, 46));

        jPanelBaseMano.setBackground(new java.awt.Color(98, 101, 145));
        jPanelBaseMano.setPreferredSize(new java.awt.Dimension(739, 40));

        javax.swing.GroupLayout jPanelBaseManoLayout = new javax.swing.GroupLayout(jPanelBaseMano);
        jPanelBaseMano.setLayout(jPanelBaseManoLayout);
        jPanelBaseManoLayout.setHorizontalGroup(
            jPanelBaseManoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanelBaseManoLayout.setVerticalGroup(
            jPanelBaseManoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelDecoracionManoLayout = new javax.swing.GroupLayout(jPanelDecoracionMano);
        jPanelDecoracionMano.setLayout(jPanelDecoracionManoLayout);
        jPanelDecoracionManoLayout.setHorizontalGroup(
            jPanelDecoracionManoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBaseMano, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );
        jPanelDecoracionManoLayout.setVerticalGroup(
            jPanelDecoracionManoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDecoracionManoLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jPanelBaseMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanelDecoracionMano, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 700, 750, -1));

        jPanelContenedorMano.setBackground(new java.awt.Color(39, 41, 72));
        jPanelContenedorMano.setPreferredSize(new java.awt.Dimension(739, 110));

        javax.swing.GroupLayout jPanelContenedorManoLayout = new javax.swing.GroupLayout(jPanelContenedorMano);
        jPanelContenedorMano.setLayout(jPanelContenedorManoLayout);
        jPanelContenedorManoLayout.setHorizontalGroup(
            jPanelContenedorManoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanelContenedorManoLayout.setVerticalGroup(
            jPanelContenedorManoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelContenedorMano, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 590, 750, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mesadejuego.png"))); // NOI18N
        getContentPane().add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonTomarFichaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTomarFichaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonTomarFichaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonTerminarTurno;
    private javax.swing.JButton jButtonTomarFicha;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JPanel jPanelBaseMano;
    private javax.swing.JPanel jPanelContenedorJugador;
    private javax.swing.JPanel jPanelContenedorJugador1;
    private javax.swing.JPanel jPanelContenedorJugador2;
    private javax.swing.JPanel jPanelContenedorJugador3;
    private javax.swing.JPanel jPanelContenedorMano;
    private javax.swing.JPanel jPanelContenedorTablero;
    private javax.swing.JPanel jPanelDecoracionMano;
    private javax.swing.JPanel jPanelSopa;
    // End of variables declaration//GEN-END:variables

    /**
     * Método del Observer - Se llama cuando el Modelo cambia Actualiza toda la
     * vista con el nuevo estado
     */
    @Override
    public void update(IModeloEjercerTurno modelo) {
        System.out.println("UI RECIBIÓ ACTUALIZACIÓN para " + jugadorID);
        SwingUtilities.invokeLater(() -> {
            
            if (modelo.getVistaActual() == TipoVista.TABLERO_JUEGO) {
                this.setVisible(true);
            } else {
                this.setVisible(false);
            }
            
            JugadorDTO ganador = modelo.getGanador();

            if (ganador != null) {
                PanelBloqueo glass = (PanelBloqueo) getGlassPane();
                glass.setMensaje("¡JUEGO TERMINADO!\nGanador: " + ganador.getNombre());
                glass.setVisible(true);

                jButtonTerminarTurno.setEnabled(false);
                jButtonTomarFicha.setEnabled(false);
                if (uiMano != null) {
                    uiMano.actualizarEstado(false);
                }

                String mensaje = "¡La partida ha finalizado!\nEl ganador es: " + ganador.getNombre();
                javax.swing.JOptionPane.showMessageDialog(this, mensaje, "Game Over", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            boolean esMiTurno = modelo.esTurnoDe(jugadorID);

            if (getGlassPane() != null) {
                getGlassPane().setVisible(!esMiTurno);
            }

            List<GrupoDTO> gruposDelTablero = modelo.getGruposEnTablero();
            List<FichaDTO> fichasMano = modelo.getFichasMano();

            if (uiTablero != null) {
                uiTablero.setGruposDesdeDTO(gruposDelTablero);
            }
            if (uiMano != null) {
                uiMano.setFichas(fichasMano);
                uiMano.actualizarEstado(esMiTurno);
            }

            actualizarEstadoBotones(modelo);

            if (uiJugadorActual != null) {
                uiJugadorActual.setEsTurno(modelo.esTurnoDe(jugadorID));
                uiJugadorActual.setNumeroFichas(modelo.getFichasMano().size());
            }

            if (uiJugadorOponente != null) {
                uiJugadorOponente.setEsTurno(!esMiTurno);

                java.util.Map<String, Integer> oponentes = modelo.getMapaFichasOponentes();

                for (java.util.Map.Entry<String, Integer> entry : oponentes.entrySet()) {
                    String idOponente = entry.getKey();
                    if (!idOponente.equals(this.jugadorID)) {
                        uiJugadorOponente.setNumeroFichas(entry.getValue());
                        break;
                    }
                }
            }

            uiTablero.setGruposDesdeDTO(gruposDelTablero);
            uiMano.setFichas(fichasMano);

            actualizarEstadoBotones(modelo);

            uiTablero.revalidate();
            uiTablero.repaint();
            uiMano.revalidate();
            uiMano.repaint();

            int restantes = modelo.getCantidadFichasSopa();
            if (lblContadorSopa != null) {
                lblContadorSopa.setText(String.valueOf(restantes));
            }
            if (jPanelSopa != null) {
                jPanelSopa.setVisible(true);
            }

            String grupoInvalido = modelo.getGrupoInvalidoId();

            if (grupoInvalido != null && !grupoInvalido.isBlank()) {
                uiTablero.marcarGrupoComoInvalido(grupoInvalido);
            } else {
                uiTablero.limpiarGruposInvalidos();
            }

            String mensajeError = modelo.getMensajeError();
            if (mensajeError != null
                    && !mensajeError.isBlank()
                    && !mensajeError.equals(ultimoMensajeError)) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        mensajeError,
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );

                ultimoMensajeError = mensajeError;
            }
        });
    }

    /**
     * Clase interna para crear el efecto de bloqueo visual (GlassPane).
     * Intercepta eventos de mouse y teclado y dibuja un fondo semitransparente.
     */
    /**
     * Clase interna para crear el efecto de bloqueo visual (GlassPane).
     */
    private class PanelBloqueo extends javax.swing.JPanel {

        private javax.swing.JLabel labelEspera;

        public PanelBloqueo() {
            setOpaque(false);
            setLayout(new java.awt.GridBagLayout());

            labelEspera = new javax.swing.JLabel("ESPERANDO TURNO DEL OPONENTE...");
            labelEspera.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
            labelEspera.setForeground(java.awt.Color.WHITE);

            labelEspera.setBackground(new java.awt.Color(0, 0, 0, 100));
            labelEspera.setOpaque(true);
            labelEspera.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
            add(labelEspera);

            java.awt.event.MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
            addKeyListener(new java.awt.event.KeyAdapter() {
            });
            setFocusTraversalKeysEnabled(false);
        }

        public void setMensaje(String mensaje) {
            labelEspera.setText("<html><center>" + mensaje.replace("\n", "<br>") + "</center></html>");
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setColor(new java.awt.Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }

        @Override
        public void setVisible(boolean aFlag) {
            super.setVisible(aFlag);
            if (aFlag) {
                requestFocusInWindow();
            }
        }
    }
}
