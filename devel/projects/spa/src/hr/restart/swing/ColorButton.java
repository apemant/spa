package hr.restart.swing;

import hr.restart.util.Aus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ColorButton extends JButton implements MouseListener, MouseMotionListener
{
    private boolean hovered;
    private boolean clicked;
    private boolean dragged;
    
    private static boolean dragging;
   
    private static Point drag;
    private static JLabel jdrag;
    
    private DragHandler handler;
    
    public ColorButton()
    {
        setBackground(getBackground());
        addMouseListener(this);
        addMouseMotionListener(this);
        setContentAreaFilled(false);
    }
    
    public void setDragHandler(DragHandler h) {
      handler = h;
    }
    
    private Color lighter(double factor) {
      return Aus.halfTone(getBackground(), Color.white, (float) factor);
    }
    
    private Color darker(double factor) {
      return Aus.halfTone(getBackground(), Color.black, (float) factor);
    }
    
    /**
     * Overpainting component, so it can have different colors
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gp = null;

        if (clicked)
            gp = new GradientPaint(0, 0, darker(0.25), 0, getHeight(), darker(0.4));
        else if (hovered)
            gp = new GradientPaint(0, 0, lighter(0.25), 0, getHeight(), lighter(0.1));
        else
            gp = new GradientPaint(0, 0, getBackground(), 0, getHeight(), darker(0.15));

        g2d.setPaint(gp);

        // Draws the rounded opaque panel with borders
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // For High quality
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 7, 7);

        g2d.setColor(darker(0.4));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 7, 7);

        super.paintComponent(g);
    }
    
    
    public void mouseClicked(MouseEvent arg0)
    {
      if (dragging) arg0.consume();
    }

    public void mouseEntered(MouseEvent arg0)
    {
        if (drag != null) return;
        hovered = true;
        clicked = false;
        
        repaint();
    }

    public void mouseExited(MouseEvent arg0)
    {
        if (drag != null) return;
        hovered = false;
        clicked = false;

        repaint();
    }

    public void mousePressed(MouseEvent arg0)
    {
        hovered = true;
        clicked = true;
        dragged = false;
        
        drag = arg0.getPoint();
        
        repaint();
    }

    public void mouseReleased(MouseEvent arg0)
    {
        hovered = true;
        clicked = false;
        
        if (dragging) {
          dragging = false;
          hovered = false;
          JPanel glass = (JPanel) ((JFrame) getTopLevelAncestor()).getGlassPane();
          Point loc = getLocationOnScreen();
          Point off = glass.getLocationOnScreen();
          glass.setVisible(false);
          glass.remove(jdrag);
          jdrag = null;
          drag.x = loc.x - off.x - drag.x;
          drag.y = loc.y - off.y - drag.y;
          if (handler != null) 
            handler.dropAt(this, arg0.getX() - drag.x, arg0.getY() - drag.y);
        }
        drag = null;

        repaint();
    }
    public void mouseMoved(MouseEvent e) {
      
    }
    
    public void mouseDragged(MouseEvent e) {
      if (drag == null) return;
      if (!dragging && e.getPoint().distance(drag) > 4) {
        dragging = dragged = true;
        JPanel glass = (JPanel) ((JFrame) getTopLevelAncestor()).getGlassPane();
        glass.removeAll();
        glass.setVisible(true);
        Point loc = getLocationOnScreen();
        Point off = glass.getLocationOnScreen();
        
/*        idrag = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        idrag.createGraphics();
        idrag.getGraphics().setClip(0, 0, getWidth(), getHeight());
        System.out.println(idrag.getGraphics().getClipBounds());
        System.out.println(idrag.getGraphics().create().getClipBounds());
        paintComponent(idrag.getGraphics());
*/
        jdrag = new JLabel() {
          protected void paintComponent(Graphics g) {
            AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
            ((Graphics2D) g).setComposite(a);
            ColorButton.this.paintComponent(g);
          };
        };
        jdrag.setSize(this.getSize());
        drag.x = loc.x - off.x - drag.x;
        drag.y = loc.y - off.y - drag.y;
        jdrag.setLocation(e.getX() + drag.x, e.getY() + drag.y);
        glass.setLayout(null);
        glass.add(jdrag);
      } else if (dragging) 
        jdrag.setLocation(e.getX() + drag.x, e.getY() + drag.y);
      
    }
    public boolean wasDragged() {
      return dragged;
    }
    
    public static interface DragHandler {
      void dropAt(ColorButton b, int x, int y);
    }
}