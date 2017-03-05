package JConnector;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.*;
import plataformakpn.HardwareModel;
import java.util.List;
import plataformakpn.GUI;
import static plataformakpn.GUI.hardwareList;
import static plataformakpn.GUI.relations;
import static plataformakpn.GUI.selectedJLabel;

public class DraggableLabel extends JLabel {

    Point pressPoint;
    Point releasePoint;
    DragProcessor dragProcessor = new DragProcessor();

    public DraggableLabel(String title) {
        super(title);
        setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(1, 5, 1, 1)));
        addMouseListener(dragProcessor);
        addMouseMotionListener(dragProcessor);

    }

    protected class DragProcessor extends MouseAdapter implements MouseListener, MouseMotionListener {

        Window dragWindow = new JWindow() {
            public void paint(Graphics g) {
                super.paint(g);
                DraggableLabel.this.paint(g);
            }
        };

        @Override
        public void mouseDragged(MouseEvent e) {
            //System.out.println("Primero");

            Point dragPoint = e.getPoint();
            int xDiff = pressPoint.x - dragPoint.x;
            int yDiff = pressPoint.y - dragPoint.y;

            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            p.x -= xDiff;
            p.y -= yDiff;

            dragWindow.setLocation(p);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //System.out.println("Segundo");
        }

        @Override
        public void mousePressed(MouseEvent e) {

            JLabel label = (JLabel) e.getComponent();

            pressPoint = e.getPoint();
            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            dragWindow.setBounds(b);
            dragWindow.setLocation(p);
            dragWindow.setVisible(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("Cuarto");

            releasePoint = e.getPoint();
            dragWindow.setVisible(false);

            int xDiff = pressPoint.x - releasePoint.x;
            int yDiff = pressPoint.y - releasePoint.y;

            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            p.x -= xDiff;
            p.y -= yDiff;

            SwingUtilities.convertPointFromScreen(p, DraggableLabel.this.getParent());
            if (p.x <= 0) {
                p.x = 1;
            }
            if (p.x > DraggableLabel.this.getParent().getWidth() - b.width) {
                p.x = DraggableLabel.this.getParent().getWidth() - b.width;
            }
            if (p.y <= 0) {
                p.y = 1;
            }
            if (p.y > DraggableLabel.this.getParent().getHeight() - b.height) {
                p.y = DraggableLabel.this.getParent().getHeight() - b.height;
            }
            setLocation(p);
            getParent().repaint();

            //Update the JLabel position
            JLabel label = (JLabel) e.getComponent();

            for (int i = 0; i < hardwareList.size(); i++) {
                HardwareModel model = hardwareList.get(i);
                if (model.getLabel() == label) {
                    model.setX(label.getX());
                    model.setY(label.getY());
                }
            }

            //Doing relations
            System.out.println("Relations init: " + relations);

            if (relations) {
                if (selectedJLabel == null) {
                    System.out.println("Seleccione a un label");
                    selectedJLabel = label;
                } else {

                    System.out.println("Estoy bucando al jlabel seleccionado en la lista");
                    for (int i = 0; i < hardwareList.size(); i++) {
                        HardwareModel model = hardwareList.get(i);
                        if (model.getLabel() == selectedJLabel) {
                            System.out.println("Asigne al label la salida");
                            model.setOutput1(label);
                        }
                    }

                    System.out.println("Limpie al label seleccionado");
                    selectedJLabel = null;
                }

            }

            //activating repaint
            GUI.repaint = true;

        }
    }
}
