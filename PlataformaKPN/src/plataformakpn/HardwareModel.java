/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plataformakpn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JLabel;

/**
 * This class represents the hardware abstraction
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 *
 */
public class HardwareModel {

    /**
     * This variable contains the x position of the JLabel over the screen.
     */
    private int posX;
    /**
     * This variable constains the y position of the JLabel over the screen.
     */
    private int posY;
    /**
     * This variable contains the JLabel which represents the hardware
     * abstraction.
     */
    private JLabel label;
    /**
     * This variable contains all the hardware abstractions connected as output
     * of the main hardware abstraction.
     */
    private List<JLabel> outputs;
    /**
     * This variable contains all the hardware abstractions connected as input
     * of the main hardware abstraction.
     */
    private List<JLabel> inputs;
    /**
     * This variable contains the id of the hardware abstraction type. 0 means
     * duplication process 1 means add process 2 means product process 3 means
     * constant generation process 4 means sink process 5 means queue process 6
     * means view process
     */
    private int hardwareType;
    /**
     * This variable is initialized just in case that the hardware abstraction
     * contains pre-loaded data.
     */
    private Queue<Float> inputQueue;
    /**
     * This variable is initialized just in case that the hardware abstraction
     * contains pre-loaded data and this data is of constant generation.
     */
    private boolean constantGeneration;
    /**
     * This variable is initialized just in case the hardware abstraction block
     * has a delay.
     */
    private int delayIterations;

    /**
     * Class constructor.
     */
    public HardwareModel() {
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.inputQueue = new LinkedList<>();
        this.delayIterations = 0;
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

    /**
     * This method verifies if an output has already been assigned.
     *
     * @param label
     * @return true:false
     */
    public boolean verifyIfOutputAlreadyExist(JLabel label) {
        return outputs.contains(label);
    }

    /**
     * hardwareType: 0 means duplication process 1 means add process 2 means
     * product process 3 means constant generation process 4 means sink process
     * 5 means queue process 6 means view process
     *
     * @return the hardwareType
     */
    public int getHardwareType() {
        return hardwareType;
    }

    /**
     * @param hardwareType the hardwareType to set 0 means duplication process 1
     * means add process 2 means production process 3 means constant generation
     * process 4 means sink process 5 means queue process 6 means view process
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
    public Queue<Float> getInputQueue() {
        return inputQueue;
    }

    /**
     * @param inputQueue the inputQueue to set
     */
    public void setInputQueue(Queue<Float> inputQueue) {
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

    /**
     * This method returns the amount of inputs assigned to an hardware
     * abstraction excluding the view process.
     *
     * @return
     */
    public int getInputSize() {
        int result = 0;
        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).getName().contains("view")) {
                result++;
            }

        }
        System.out.println(result);
        return result;
    }

    /**
     * @return the delayIterations
     */
    public int getDelayIterations() {
        return delayIterations;
    }

    /**
     * @param delayIterations the delayIterations to set
     */
    public void setDelayIterations(int delayIterations) {
        this.delayIterations = delayIterations;
    }
}
