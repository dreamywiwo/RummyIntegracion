package itson.rummypresentacion.utils;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class SlideWrapper extends JPanel {

    private final JPanel content;
    private int lastX;
    private int offsetX = 0;

    public SlideWrapper(JPanel content) {
        this.content = content;
        setLayout(null);
        setOpaque(false);

        add(content);

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - lastX;
                offsetX += dx;
                applyBounds();
                lastX = e.getX();
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private void applyBounds() {
        int viewWidth = getWidth(); 
        int contentWidth = content.getPreferredSize().width; 

        if (contentWidth <= viewWidth) {
            offsetX = 0;
        } else {
            if (offsetX > 0) {
                offsetX = 0;
            }

            int minOffset = viewWidth - contentWidth;
            if (offsetX < minOffset) {
                offsetX = minOffset;
            }
        }

        content.setLocation(offsetX, 0);
    }

    @Override
    public void doLayout() {
        Dimension pref = content.getPreferredSize();
        content.setSize(pref);
        applyBounds();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(content.getPreferredSize().width, content.getPreferredSize().height);
    }
}
