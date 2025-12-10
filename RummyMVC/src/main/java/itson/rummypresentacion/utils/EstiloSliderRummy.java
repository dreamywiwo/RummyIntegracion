/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummypresentacion.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author Dana Chavez
 */
public class EstiloSliderRummy extends BasicSliderUI {
    private static final int ALTURA_TRACK = 12; 
    private static final int ANCHO_THUMB = 14;  
    private static final int ALTO_THUMB = 24;   
    private static final Color COLOR_TRACK = Color.WHITE;
    private static final Color COLOR_THUMB = new Color(110, 115, 128); // Gris azulado

    public EstiloSliderRummy(JSlider b) {
        super(b);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(ANCHO_THUMB, ALTO_THUMB);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int trackY = trackRect.y + (trackRect.height - ALTURA_TRACK) / 2;
        
        Shape trackShape = new RoundRectangle2D.Float(trackRect.x, trackY, trackRect.width, ALTURA_TRACK, 10, 10);
        
        g2.setColor(COLOR_TRACK);
        g2.fill(trackShape);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int thumbX = thumbRect.x;
        int thumbY = thumbRect.y + (thumbRect.height - ALTO_THUMB) / 2;

        Shape thumbShape = new RoundRectangle2D.Float(thumbX, thumbY, ANCHO_THUMB, ALTO_THUMB, 10, 10);
        
        g2.setColor(COLOR_THUMB);
        g2.fill(thumbShape);
    }

    @Override
    public void paintFocus(Graphics g) {
    } 
}
