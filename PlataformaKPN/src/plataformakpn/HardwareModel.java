/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plataformakpn;

import JConnector.DraggableLabel;
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
    
    private JLabel input;
    private JLabel output1;
    private JLabel output2;
    
    public HardwareModel()
    {
        
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
     * @return the input
     */
    public JLabel getInput() {
        return input;
    }

    /**
     * @param input the label to set
     */
    public void setInput(JLabel input) {
        this.input = input;
    }

    /**
     * @return the output1
     */
    public JLabel getOutput1() {
        return output1;
    }

    /**
     * @param output1 the output1 to set
     */
    public void setOutput1(JLabel output1) {
        this.output1 = output1;
    }

    /**
     * @return the output2
     */
    public JLabel getOutput2() {
        return output2;
    }

    /**
     * @param output2 the output2 to set
     */
    public void setOutput2(JLabel output2) {
        this.output2 = output2;
    }
    
}
