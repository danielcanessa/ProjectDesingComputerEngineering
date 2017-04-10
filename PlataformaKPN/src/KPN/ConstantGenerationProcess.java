/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import static KPN.KPNNetwork.searchThread;
import java.util.LinkedList;
import java.util.Queue;
import static plataformakpn.GUI.userThreadDebuging;
import plataformakpn.HardwareGraph;
import plataformakpn.HardwareModel;

/**
 *
 * @author Daniel
 */
public class ConstantGenerationProcess extends Thread {

    private volatile Queue<Float> queueIn;
    private volatile Queue<Float> queueOut;
    private volatile boolean constantGeneration;
    private volatile boolean killThread;
    private volatile boolean pauseThread;
    private volatile int delayIterations;

    private volatile String queueInputAssigned;
    private volatile String queueOutputAssigned;

    public ConstantGenerationProcess() {
        this.queueIn = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = "";
        this.queueOutputAssigned = "";
    }

    @Override
    public void run() {
        while (!killThread) {
            try {
                while (!isPauseThread()) {
                    if (queueIn.size() > 0) {
                        float nextNumber = queueIn.poll();

                        if (isConstantGeneration() && getDelayIterations() == 0) {
                            queueIn.add(nextNumber);
                        } else if (getDelayIterations() > 0) {
                            setDelayIterations(getDelayIterations() - 1);
                        }

                        this.queueOut.add(nextNumber);

                    }
                    //race condition
                    if (userThreadDebuging) {
                        setPauseThread(true);
                    }

                }
                Thread.sleep(100);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void printQueues() {
        System.out.println("Sink:");
        System.out.println("    queueIn:" + this.getQueueIn());
        System.out.println("    queueOut:" + this.getQueueOut());
        System.out.println("----------------------------------------");
    }

    public void setThreadName(String threadName) {
        this.setName(threadName);
    }

    /**
     * @return the queueIn
     */
    public Queue<Float> getQueueIn() {
        return queueIn;
    }

    /**
     * @param queueIn the queueIn to set
     */
    public void setQueueIn(Queue<Float> queueIn) {
        this.queueIn = queueIn;
    }

    /**
     * @return the queueOut
     */
    public Queue<Float> getQueueOut() {
        return queueOut;
    }

    /**
     * @param queueOut the queueOut to set
     */
    public void setQueueOut(Queue<Float> queueOut) {
        this.queueOut = queueOut;
    }

    /**
     * @return the killThread
     */
    public boolean isKillThread() {
        return killThread;
    }

    /**
     * @param killThread the killThread to set
     */
    public void setKillThread(boolean killThread) {
        this.killThread = killThread;
    }

    /**
     * @return the pauseThread
     */
    public boolean isPauseThread() {
        return pauseThread;
    }

    /**
     * @param pauseThread the pauseThread to set
     */
    public void setPauseThread(boolean pauseThread) {
        while (this.pauseThread != pauseThread) {
            this.pauseThread = pauseThread;
        }
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
     * @return the queueInputAssigned
     */
    public boolean isQueueInputAssigned() {
        return !queueInputAssigned.equals("");
    }

    /**
     * @param queueInputAssigned the queueInputAssigned to set
     */
    public void setQueueInputAssigned(String queueInputAssigned) {
        this.queueInputAssigned = queueInputAssigned;
    }

    /**
     * @return the queueOutputAssigned
     */
    public boolean isQueueOutputAssigned() {
        return !queueOutputAssigned.equals("");
    }

    /**
     * @param queueOutputAssigned the queueOutputAssigned to set
     */
    public void setQueueOutputAssigned(String queueOutputAssigned) {
        this.queueOutputAssigned = queueOutputAssigned;
    }

    /**
     * @return the queueInputAssigned
     */
    public String getQueueInputAssigned() {
        return queueInputAssigned;
    }

    /**
     * @return the queueOutputAssigned
     */
    public String getQueueOutputAssigned() {
        return queueOutputAssigned;
    }

    public void joinConstantGenerationProcess(String name, HardwareModel model, HardwareGraph hardwareAbstraction) {

        ConstantGenerationProcess constantGenerationProcess = (ConstantGenerationProcess) searchThread(name); //current constantGeneration process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = KPNNetwork.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Duplication(constantGenerationProcess, duplicationInputProcess);
                    break;
                case 1: //constantGeneration process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Add(constantGenerationProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Product(constantGenerationProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_ConstantGeneration(constantGenerationProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess sinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Sink(constantGenerationProcess, sinkInputProcess);
                    break;
                case 5:
                    JoinInput_ConstantGeneration_Queue(constantGenerationProcess, hardwareAbstraction.search(model.getInputs().get(j)));
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Duplication(constantGenerationProcess, duplicationOutputProcess);
                    break;
                case 1: //constantGeneration process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Add(constantGenerationProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Product(constantGenerationProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_ConstantGeneration(constantGenerationProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Sink(constantGenerationProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_ConstantGeneration_Duplication(ConstantGenerationProcess constantGenerationProcess, DuplicationProcess duplicationInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation

            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation

                constantGenerationProcess.setQueueIn(duplicationInputProcess.getQueueOut1());

                duplicationInputProcess.setQueueOutput1Assigned(constantGenerationProcess.getName());

                constantGenerationProcess.setQueueInputAssigned(duplicationInputProcess.getName());

            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                System.out.println("aqui2");

                constantGenerationProcess.setQueueIn(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            }
        }
    }

    private void JoinInput_ConstantGeneration_Add(ConstantGenerationProcess constantGenerationProcess, AddProcess addInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) {//if the input 1 of the constantGeneration process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueIn(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(addInputProcess.getName());
            }
        }
    }

    private void JoinInput_ConstantGeneration_Product(ConstantGenerationProcess constantGenerationProcess, ProductProcess productInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(productInputProcess.getName());
            }
        }
    }

    private void JoinInput_ConstantGeneration_ConstantGeneration(ConstantGenerationProcess constantGenerationProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(constantGenerationInputProcess.getName());
            }
        }
    }

    private void JoinInput_ConstantGeneration_Sink(ConstantGenerationProcess constantGenerationProcess, SinkProcess sinkInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!sinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(sinkInputProcess.getQueueOut());
                sinkInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(sinkInputProcess.getName());
            }
        }
    }

    private void JoinInput_ConstantGeneration_Queue(ConstantGenerationProcess constantGenerationProcess, HardwareModel model) {

        constantGenerationProcess.setQueueIn(model.getInputQueue());
        constantGenerationProcess.setConstantGeneration(model.isConstantGeneration());
    }

    private void JoinOutput_ConstantGeneration_Duplication(ConstantGenerationProcess constantGenerationProcess, DuplicationProcess duplicationOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the output of the constantGeneration process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                constantGenerationProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(duplicationOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_ConstantGeneration_Add(ConstantGenerationProcess constantGenerationProcess, AddProcess addOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(addOutputProcess.getName());
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the constantGeneration process is already assigned but input 2 still without assignation
                constantGenerationProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(addOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_ConstantGeneration_Product(ConstantGenerationProcess constantGenerationProcess, ProductProcess productOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(productOutputProcess.getName());
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the constantGeneration process is already assigned but input 2 still without assignation
                constantGenerationProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(productOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_ConstantGeneration_ConstantGeneration(ConstantGenerationProcess constantGenerationProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(constantGenerationOutputProcess.getName());
            }
        }

    }

    private void JoinOutput_ConstantGeneration_Sink(ConstantGenerationProcess constantGenerationProcess, SinkProcess sinkOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(sinkOutputProcess.getName());
            }
        }
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
