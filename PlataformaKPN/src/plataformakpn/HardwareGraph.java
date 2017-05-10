/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plataformakpn;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import javax.swing.JLabel;

/**
 * This class contains the definitions and the methods for the hardware graph structure 
 * @author Daniel Canessa Valverde
 * @version 1.0
 * 
 */
public class HardwareGraph {
    
    /**
     * This variable represents the sigleton variable of the class.
     */
    private List<HardwareModel> hardwareGraph;
    /**
     * This variable represents a counter of the amount of hardware abstractions 
     * inserted by the user.
     */
    private int hardwareIdentifier;

    /**
     * Class constructor.
     */
    public HardwareGraph() {
        this.hardwareGraph = new ArrayList<>();
        this.hardwareIdentifier = 0;
    }

    /**
     * This method verifys if the asigned output of an hardware is the same.
     * @param model HardwareModel
     * @param label JLabel
     * @return 
     */
    public boolean verifySameOutput(HardwareModel model, JLabel label) {
        int indexRepeat = 0;
        for (int i = 0; i < model.getOutputs().size(); i++) {
            if (model.getOutputs().get(i).equals(label)) {
                indexRepeat++;
            }        }
        return indexRepeat > 1;
    }

    /**
     * This method returns the hardware model in the position "i" of the hardware graph.
     * @param i int
     * @return 
     */
    public HardwareModel get(int i) {
        return getHardwareGraph().get(i);
    }

    /**
     * This method returns the hardware model which contains the "label".
     * @param label JLabel
     * @return 
     */
    public HardwareModel search(JLabel label) {
        for (int i = 0; i < getHardwareGraph().size(); i++) {
            if (getHardwareGraph().get(i).getLabel().equals(label)) {
                return getHardwareGraph().get(i);
            }
        }
        return null;
    }

    /**
     * This method inserts a hardware model in the hardware graph.
     * @param model HardwareModel
     */
    public void add(HardwareModel model) {
        getHardwareGraph().add(model);
    }

    /**
     * This method returns the amount of elements in the hardware graph.
     * @return 
     */
    public int size() {
        return getHardwareGraph().size();
    }

    /**
     * This method prints on console the graph structure.
     */
    public void printGraph() {
        for (int i = 0; i < getHardwareGraph().size(); i++) {
            System.out.println("Nodo: " + getHardwareGraph().get(i).getLabel().getName());
            System.out.println("    Entradas:");
            for (int j = 0; j < getHardwareGraph().get(i).getInputs().size(); j++) {
                System.out.println("        " + getHardwareGraph().get(i).getInputs().get(j).getName());
            }
            System.out.println("    Salidas:");
            for (int j = 0; j < getHardwareGraph().get(i).getOutputs().size(); j++) {
                System.out.println("        " + getHardwareGraph().get(i).getOutputs().get(j).getName());
            }
            System.out.println("    Cola entrada:");
            System.out.println("        " + getHardwareGraph().get(i).getInputQueue());

            System.out.println("    Generación constante: " + getHardwareGraph().get(i).isConstantGeneration() + ", Delay: " + getHardwareGraph().get(i).getDelayIterations());
        }
    }

    /**
     * This method removes an element of the hardware graph.
     * @param label JLabel
     */
    public void remove(JLabel label) {
        for (int i = 0; i < getHardwareGraph().size(); i++) {
            if (getHardwareGraph().get(i).getLabel().equals(label)) {
                getHardwareGraph().remove(i);
            } else {
                for (int j = 0; j < getHardwareGraph().get(i).getInputs().size(); j++) {
                    if (getHardwareGraph().get(i).getInputs().get(j).equals(label)) {
                        getHardwareGraph().get(i).getInputs().remove(j);
                    }
                }
                for (int j = 0; j < getHardwareGraph().get(i).getOutputs().size(); j++) {
                    if (getHardwareGraph().get(i).getOutputs().get(j).equals(label)) {
                        getHardwareGraph().get(i).getOutputs().remove(j);
                    }
                }
            }
        }
    }

    /**
     * @return the hardwareIdentifier
     */
    public int getHardwareIdentifier() {
        this.hardwareIdentifier++;
        return hardwareIdentifier;
    }

    /**
     * This method modifies the tooltip of an hardware model with the updated queue's
     * @param name String
     * @param queueIn1 Queue[Float]
     * @param queueIn2 Queue[Float]
     * @param queueOutput1 Queue[Float]
     * @param queueOutput2 Queue[Float]
     */
    public void updateToolTip(String name, Queue<Float> queueIn1, Queue<Float> queueIn2, Queue<Float> queueOutput1, Queue<Float> queueOutput2) {
        for (int i = 0; i < getHardwareGraph().size(); i++) {
            JLabel label = getHardwareGraph().get(i).getLabel();
            if (label.getName().equals(name)) {
                String text = label.getToolTipText().substring(0, label.getToolTipText().indexOf('.') + 1);
                if (queueIn1 != null) {
                    text += "<br>";
                    text += "Input Queue 1: " + queueIn1;
                }
                if (queueIn2 != null) {
                    text += "<br>";
                    text += "Input Queue 2: " + queueIn2;
                }
                if (queueOutput1 != null) {
                    text += "<br>";
                    text += "Output Queue 1: " + queueOutput1;
                }
                if (queueOutput2 != null) {
                    text += "<br>";
                    text += "Output Queue 2: " + queueOutput2;
                }
                text += "</html>";
                label.setToolTipText(text);
            }
        }
    }

    /**
     * @return the hardwareGraph
     */
    public List<HardwareModel> getHardwareGraph() {
        return hardwareGraph;
    }

    /**
     * @param hardwareGraph the hardwareGraph to set
     */
    public void setHardwareGraph(List<HardwareModel> hardwareGraph) {
        this.hardwareGraph = hardwareGraph;
    }

    /**
     * @param hardwareIdentifier the hardwareIdentifier to set
     */
    public void setHardwareIdentifier(int hardwareIdentifier) {
        this.hardwareIdentifier = hardwareIdentifier;
    }

}
