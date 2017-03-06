/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plataformakpn;

import JConnector.DraggableLabel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author Daniel
 */
public class HardwareModel
{
    private int x;
    private int y;
    private JLabel label;    
    private List<JLabel> outputs;
    private List<JLabel> inputs;
    private int hardwareType;
    
        
    public HardwareModel()
    {
        outputs = new ArrayList<>();     
        inputs = new ArrayList<>();     
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
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
    
    
}
