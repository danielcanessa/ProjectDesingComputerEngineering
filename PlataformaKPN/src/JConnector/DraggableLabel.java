package JConnector;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.*;
import plataformakpn.HardwareModel;
import java.util.List;
import plataformakpn.GUI;
import static plataformakpn.GUI.selectedJLabel;
import static plataformakpn.GUI.hardwareGraph;
import static plataformakpn.GUI.relationsFlag;
import static plataformakpn.GUI.removeFlag;

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
            //System.out.println(e.getX() +", "+ e.getY());

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
            for (int i = 0; i < hardwareGraph.size(); i++) {
                HardwareModel model = hardwareGraph.get(i);
                if (model.getLabel() == label) {
                    model.setX(label.getX());
                    model.setY(label.getY());
                }
            }

            //Doing relations
            if (relationsFlag) {

                if (selectedJLabel == null) {
                    selectedJLabel = label;
                } else {
                   

                    HardwareModel modelSource = getModel(selectedJLabel);
                    HardwareModel modelDest = getModel(label);

                    System.out.println(verifyModels(modelSource, modelDest));
                    if (verifyModels(modelSource, modelDest)) 
                    {
                        modelSource.getOutputs().add(label);
                        modelDest.getInputs().add(selectedJLabel);
                    }

                    selectedJLabel = null;
                }
            }
            
            //deleting labels
            if(removeFlag)
            {
               hardwareGraph.remove(label);                 
            }

            //activating repaint
            GUI.repaintFlag = true;

        }

        private HardwareModel getModel(JLabel label) {
            for (int i = 0; i < hardwareGraph.size(); i++) {

                HardwareModel model = hardwareGraph.get(i);

                if (model.getLabel() == label) {

                    return model;
                }
            }
            return null;
        }

        /* 
        * Type Hardware
        * 0 means duplication process
        * 1 means add process
        * 2 means production process
        * 3 means constant generation process
        * 4 means sink process
         */
        private boolean verifyModels(HardwareModel modelSource, HardwareModel modelDest) {
            boolean resultSource = false;
            boolean resultDest = false;

            if (modelSource.getHardwareType() != 0 && modelSource.getOutputs().size() < 1) {
                resultSource = true;
            }
            if (modelSource.getHardwareType() == 0 && modelSource.getOutputs().size() < 2) {
                resultSource = true;
            }

            if ((modelDest.getHardwareType() == 1 || modelDest.getHardwareType() == 2) 
                    && modelDest.getInputs().size() < 2)  {
                resultDest = true;
            }
            if ((modelDest.getHardwareType() == 0 || modelDest.getHardwareType() == 3 || modelDest.getHardwareType() == 4) 
                    && modelDest.getInputs().size() < 1) {
               
                resultDest = true;
            }
            return resultSource&resultDest;
            
        }
    }
}
