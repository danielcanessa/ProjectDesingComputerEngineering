package ComponentConnector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import plataformakpn.HardwareModel;
import plataformakpn.GUI;
import static plataformakpn.GUI.selectedJLabel;
import static plataformakpn.GUI.hardwareGraph;
import static plataformakpn.GUI.relationsFlag;
import static plataformakpn.GUI.removeFlag;
import static plataformakpn.GUI.selectedModelByQueueProcess;

public class DragLabel extends JLabel {

    Point sourcePoint;
    Point destinationPoint;
    DragProcessor dragProcessor = new DragProcessor();
    JPanel panel;
    JDialog jDialogFifo;

    public DragLabel(String title, String imagePath, String toolTip, JDialog jDialogFifo, String name) {
        super(title);
        this.jDialogFifo = jDialogFifo;
        //setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(1, 5, 1, 1)));     
        this.initializeJLabel(imagePath, toolTip, name,0,0);

    }

    public DragLabel(String title, String imagePath, String toolTip, JDialog jDialogFifo, String name,int posX, int posY) {
        super(title);
        this.jDialogFifo = jDialogFifo;
        //setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(1, 5, 1, 1)));     
        this.initializeJLabel(imagePath, toolTip, name,posX,posY);

    }
    
    private void initializeJLabel(String imagePath, String toolTip, String name,int posX, int posY) {
        addMouseListener(dragProcessor);
        addMouseMotionListener(dragProcessor);
        setIcon(new javax.swing.ImageIcon(getClass().getResource(imagePath)));
        setHorizontalAlignment(0);
        setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
        setBounds(posX, posY, 48, 48);
        int id = hardwareGraph.getHardwareIdentifier();
        setName(name + id);
        setToolTipText(toolTip + id);
    }

    protected class DragProcessor extends MouseAdapter implements MouseListener, MouseMotionListener {

        Window dragWindow = new JWindow() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                DragLabel.this.paint(g);
            }
        };

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JLabel label = (JLabel) e.getComponent();
               
                HardwareModel model = hardwareGraph.search(label);
                if (model.getHardwareType() == 5) //queue process
                {
                    selectedModelByQueueProcess = model;
                    jDialogFifo.setLocationRelativeTo(label);
                    jDialogFifo.setModal(true);
                    jDialogFifo.pack();
                    jDialogFifo.setVisible(true);

                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //System.out.println(e.getX() +", "+ e.getY());

            Point dragPoint = e.getPoint();
            int xDiff = sourcePoint.x - dragPoint.x;
            int yDiff = sourcePoint.y - dragPoint.y;

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

            sourcePoint = e.getPoint();
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

            destinationPoint = e.getPoint();
            dragWindow.setVisible(false);

            int xDiff = sourcePoint.x - destinationPoint.x;
            int yDiff = sourcePoint.y - destinationPoint.y;

            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            p.x -= xDiff;
            p.y -= yDiff;

            SwingUtilities.convertPointFromScreen(p, DragLabel.this.getParent());
            if (p.x <= 0) {
                p.x = 1;
            }
            if (p.x > DragLabel.this.getParent().getWidth() - b.width) {
                p.x = DragLabel.this.getParent().getWidth() - b.width;
            }
            if (p.y <= 0) {
                p.y = 1;
            }
            if (p.y > DragLabel.this.getParent().getHeight() - b.height) {
                p.y = DragLabel.this.getParent().getHeight() - b.height;
            }
            setLocation(p);
            getParent().repaint();

            //Update the JLabel position
            JLabel label = (JLabel) e.getComponent();
            for (int i = 0; i < hardwareGraph.size(); i++) {
                HardwareModel model = hardwareGraph.get(i);
                if (model.getLabel() == label) {
                    model.setPosX(label.getX());
                    model.setPosY(label.getY());
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
                    if (verifyModels(modelSource, modelDest)) {
                        modelSource.getOutputs().add(label);
                        modelDest.getInputs().add(selectedJLabel);
                    }

                    selectedJLabel = null;
                }
            }

            //deleting labels
            if (removeFlag) {
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
                    && modelDest.getInputs().size() < 2) {
                resultDest = true;
            }
            if ((modelDest.getHardwareType() == 0 || modelDest.getHardwareType() == 3 || modelDest.getHardwareType() == 4)
                    && modelDest.getInputs().size() < 1) {

                resultDest = true;
            }
            return resultSource & resultDest;

        }
    }
}
