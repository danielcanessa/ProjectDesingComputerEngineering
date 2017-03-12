/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plataformakpn;

import ComponentConnector.DragLabel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author Daniel
 */
public class HardwareModel
{
    private int posX;
    private int posY;
    private JLabel label;    
    private List<JLabel> outputs;
    private List<JLabel> inputs;
    private int hardwareType;      
    private List<Float> inputQueue;
    private boolean constantGeneration;
    
        
    public HardwareModel()
    {
        outputs = new ArrayList<>();     
        inputs = new ArrayList<>(); 
        inputQueue = new ArrayList<>();
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * @return the label
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(JLabel label) {
        this.label = label;
    }

    /**
     * @return the outputs
     */
    public List<JLabel> getOutputs() {
        return outputs;
    }

    /**
     * @param outputs the outputs to set
     */
    public void setOutputs(List<JLabel> outputs) {
        this.outputs = outputs;
    }
    
    public boolean verifyIfOutputAlreadyExist(JLabel label)
    {
        return outputs.contains(label);        
    }

    /**
     * @return the hardwareType
     * 0 means duplication process
     * 1 means add process
     * 2 means product process
     * 3 means constant generation process
     * 4 means sink process
     * 5 means queue process
     */
    public int getHardwareType() {
        return hardwareType;
    }

    /**
     * @param hardwareType the hardwareType to set
     * 0 means duplication process
     * 1 means add process
     * 2 means production process
     * 3 means constant generation process
     * 4 means sink process
     * 5 means queue process
     */
    public void setHardwareType(int hardwareType) {
        this.hardwareType = hardwareType;
    }

    /**
     * @return the inputs
     */
    public List<JLabel> getInputs() {
        return inputs;
    }

    /**
     * @param inputs the inputs to set
     */
    public void setInputs(List<JLabel> inputs) {
        this.inputs = inputs;
    }

    /**
     * @return the inputQueue
     */
    public List<Float> getInputQueue() {
        return inputQueue;
    }

    /**
     * @param inputQueue the inputQueue to set
     */
    public void setInputQueue(List<Float> inputQueue) {
        this.inputQueue = inputQueue;
    }

    /**
     * @return the constantGeneration
     */
    public boolean isConstantGeneration() {
        return constantGeneration;
    }

    /**
     * @param constantGeneration the constantGeneration to set
     */
    public void setConstantGeneration(boolean constantGeneration) {
        this.constantGeneration = constantGeneration;
    }
    
    
}
