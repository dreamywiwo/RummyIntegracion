package itson.rummypresentacion.utils;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class TableroSlideWrapper extends JPanel {

    private final JPanel content;
    private int lastX, lastY;
    private int offsetX = 0;
    private int offsetY = 0;

    public TableroSlideWrapper(JPanel content) {
        this.content = content;
        setLayout(null);
        setOpaque(false);

        add(content);

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getXOnScreen() - lastX;
                int dy = e.getYOnScreen() - lastY;

                offsetX += dx;
                offsetY += dy;

                applyBounds();

                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }
        };

        content.addMouseListener(ma);
        content.addMouseMotionListener(ma);
        
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private void applyBounds() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        
        Dimension contentSize = content.getPreferredSize();
        int contentWidth = contentSize.width;
        int contentHeight = contentSize.height;

        // Lógica Horizontal
        if (contentWidth <= viewWidth) {
            offsetX = 0; 
        } else {
            if (offsetX > 0) offsetX = 0; 
            int minX = viewWidth - contentWidth;
            if (offsetX < minX) offsetX = minX;
        }

        // Lógica Vertical
        if (contentHeight <= viewHeight) {
            offsetY = 0; 
        } else {
            if (offsetY > 0) offsetY = 0; 
            int minY = viewHeight - contentHeight;
            if (offsetY < minY) offsetY = minY; 
        }

        content.setLocation(offsetX, offsetY);
    }

    @Override
    public void doLayout() {
        Dimension pref = content.getPreferredSize();
        content.setSize(pref);
        applyBounds();
    }
}