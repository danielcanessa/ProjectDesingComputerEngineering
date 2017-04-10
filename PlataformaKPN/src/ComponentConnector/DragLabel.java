package ComponentConnector;

import KPN.AddProcess;
import KPN.ConstantGenerationProcess;
import KPN.DuplicationProcess;
import KPN.KPNNetwork;
import KPN.ProductProcess;
import KPN.SinkProcess;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import plataformakpn.HardwareModel;
import plataformakpn.GUI;
import static plataformakpn.GUI.selectedJLabel;
import static plataformakpn.GUI.hardwareGraph;
import static plataformakpn.GUI.labelsView;
import static plataformakpn.GUI.relationsFlag;
import static plataformakpn.GUI.removeFlag;
import static plataformakpn.GUI.selectedColor;
import static plataformakpn.GUI.textFieldView;
import static plataformakpn.GUI.selectedModelJDialog;

public class DragLabel extends JLabel {

    Point sourcePoint;
    Point destinationPoint;
    DragProcessor dragProcessor = new DragProcessor();
    JPanel panel;
    JDialog jDialog;
    JTextField jTextFieldFifo;
    JSpinner spinner;
    Component jGUI;

    public DragLabel(String imagePath, String toolTip, JDialog jDialog, String name, int posX, int posY, JTextField jTextFieldFifo, JSpinner spinner, Component jGUI) {
        this.jDialog = jDialog;
        this.jTextFieldFifo = jTextFieldFifo;
        this.spinner = spinner;
        this.jGUI = jGUI;
        this.initializeJLabel(imagePath, toolTip, name, posX, posY);

    }

    private void initializeJLabel(String imagePath, String toolTip, String name, int posX, int posY) {
        addMouseListener(dragProcessor);
        addMouseMotionListener(dragProcessor);
        setIcon(new javax.swing.ImageIcon(getClass().getResource(imagePath)));
        setHorizontalAlignment(0);
        setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
        setBounds(posX, posY, 48, 48);
        int id = hardwareGraph.getHardwareIdentifier();
        setName("ID: " + id + ", Name: " + name);
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

        private void makeVisibleJDialogFifo() {
            for (int i = 0; i < 4; i++) {
                textFieldView.get(i).setVisible(true);
                labelsView.get(i).setVisible(true);
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //double click over hardware abstraction
            if (e.getClickCount() == 2) {
                JLabel label = (JLabel) e.getComponent();
                //getting the data of the abstraction double clicked
                HardwareModel model = hardwareGraph.search(label);

                //constant generation process, jDialogDelay
                if (model.getHardwareType() == 3) {
                    selectedModelJDialog = model;

                    spinner.setValue(model.getDelayIterations());
                    jDialog.setLocationRelativeTo(label);

                    jDialog.setModal(true);
                    jDialog.pack();
                    jDialog.setVisible(true);

                } //queue process, jDialogFiFo
                else if (model.getHardwareType() == 5) {

                    selectedModelJDialog = model;

                    jTextFieldFifo.setText(model.getInputQueue().toString().replace("[", "").replace("]", ""));

                    jDialog.setLocationRelativeTo(label);
                    jDialog.setModal(true);
                    jDialog.pack();
                    jDialog.setVisible(true);
                } //view process, FDialogView
                else if (model.getHardwareType() == 6) {

                    makeVisibleJDialogFifo();
                    jDialog.setLocationRelativeTo(label);

                    if (model.getOutputs().get(0) != null) {
                        String hardware = model.getOutputs().get(0).getName();
                        if (hardware.contains("duplication")) {
                            DuplicationProcess thread = (DuplicationProcess) KPNNetwork.searchThread(hardware);

                            labelsView.get(1).setVisible(false);
                            textFieldView.get(1).setVisible(false);

                            textFieldView.get(0).setText(thread.getQueueIn().toString());
                            textFieldView.get(2).setText(thread.getQueueOut1().toString());
                            textFieldView.get(3).setText(thread.getQueueOut2().toString());

                        } else if (hardware.contains("add")) {
                            AddProcess thread = (AddProcess) KPNNetwork.searchThread(hardware);

                            labelsView.get(3).setVisible(false);
                            textFieldView.get(3).setVisible(false);

                            textFieldView.get(0).setText(thread.getQueueIn1().toString());
                            textFieldView.get(1).setText(thread.getQueueIn2().toString());
                            textFieldView.get(2).setText(thread.getQueueOut().toString());

                        } else if (hardware.contains("product")) {
                            ProductProcess thread = (ProductProcess) KPNNetwork.searchThread(hardware);

                            labelsView.get(3).setVisible(false);
                            textFieldView.get(3).setVisible(false);

                            textFieldView.get(0).setText(thread.getQueueIn1().toString());
                            textFieldView.get(1).setText(thread.getQueueIn2().toString());
                            textFieldView.get(2).setText(thread.getQueueOut().toString());

                        } else if (hardware.contains("constantGeneration")) {
                            ConstantGenerationProcess thread = (ConstantGenerationProcess) KPNNetwork.searchThread(hardware);

                            labelsView.get(1).setVisible(false);
                            textFieldView.get(1).setVisible(false);
                            labelsView.get(3).setVisible(false);
                            textFieldView.get(3).setVisible(false);

                            textFieldView.get(0).setText(thread.getQueueIn().toString());
                            textFieldView.get(2).setText(thread.getQueueOut().toString());
                        } else if (hardware.contains("sink")) {
                            SinkProcess thread = (SinkProcess) KPNNetwork.searchThread(hardware);

                            labelsView.get(1).setVisible(false);
                            textFieldView.get(1).setVisible(false);
                            labelsView.get(3).setVisible(false);
                            textFieldView.get(3).setVisible(false);

                            textFieldView.get(0).setText(thread.getQueueIn().toString());
                            textFieldView.get(2).setText(thread.getQueueOut().toString());
                        }

                    }

                    jDialog.setModal(true);
                    jDialog.pack();
                    jDialog.setVisible(true);

                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //moving the hardware abstraction
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
            //drawing again the lines
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

            //get the label selected
            JLabel label = (JLabel) e.getComponent();

            //Update the JLabel position
            updateHardwarePosition(label);

            //Create relations
            createNewRelation(label);

            //Delete labels
            deleteHardware(label);

            //activating repaint
            GUI.repaintFlag = true;
        }

        private void deleteHardware(JLabel label) {
            if (removeFlag) {
                hardwareGraph.remove(label);
            }
        }

        private void updateHardwarePosition(JLabel label) {
            for (int i = 0; i < hardwareGraph.size(); i++) {
                HardwareModel model = hardwareGraph.get(i);
                if (model.getLabel() == label) {
                    model.setPosX(label.getX());
                    model.setPosY(label.getY());
                }
            }
        }

        private void createNewRelation(JLabel label) {
            if (relationsFlag) {

                if (selectedJLabel == null) {
                    selectedJLabel = label;
                    selectedJLabel.setBorder(BorderFactory.createLineBorder(selectedColor));

                } else {

                    HardwareModel modelSource = getModel(selectedJLabel);
                    HardwareModel modelDest = getModel(label);

                    System.out.println(verifyModels(modelSource, modelDest));
                    if (verifyModels(modelSource, modelDest)) {
                        modelSource.getOutputs().add(label);
                        modelDest.getInputs().add(selectedJLabel);
                    } else {
                        //custom title, error icon          

                        JOptionPane.showMessageDialog(jGUI,
                                "Not supported operation.",
                                "Error creating connection",
                                JOptionPane.ERROR_MESSAGE
                        );

                    }

                    selectedJLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    selectedJLabel = null;

                }
            }
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

            if (modelSource.getHardwareType() != 0 && modelSource.getHardwareType() != 4 && modelSource.getOutputs().size() < 1) {
                if (modelSource.getHardwareType() == 5) { //queue process can only be connect to a constant generation process
                    if (modelDest.getHardwareType() == 3) {
                        resultSource = true;
                    }

                } else {
                    resultSource = true;
                }
            }
            if (modelSource.getHardwareType() == 0 && modelSource.getOutputs().size() < 2) {
                resultSource = true;
            }

            if ((modelDest.getHardwareType() == 1 || modelDest.getHardwareType() == 2)
                    && modelDest.getInputSize() < 2) {
                resultDest = true;
            }
            if ((modelDest.getHardwareType() == 0 || modelDest.getHardwareType() == 3
                    || modelDest.getHardwareType() == 4)
                    && modelDest.getInputSize() < 1) {

                resultDest = true;
            }

            if (modelSource.getHardwareType() == 6 && modelSource.getOutputs().size() < 1
                    && modelDest.getHardwareType() != 6) //it doesn't matter the amount of view process connected
            {
                return true;
            } else {
                return resultSource & resultDest;
            }

        }
    }
}
