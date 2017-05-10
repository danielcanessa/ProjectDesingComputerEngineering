/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import static KPN.KPNNetwork.searchThread;
import java.util.LinkedList;
import java.util.Queue;
import static plataformakpn.GUI.hardwareGraph;
import static plataformakpn.GUI.userThreadDebuging;
import plataformakpn.HardwareGraph;
import plataformakpn.HardwareModel;

/**
 * This class contains the definition and the methods of the constant generation
 * process thread.
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 */
public class ConstantGenerationProcess extends Thread {

    /**
     * This variable represents the input queue of the constant generation
     * block.
     */
    private volatile Queue<Float> queueIn;
    /**
     * This variable represents the input queue of the constant generation
     * block.
     */
    private volatile Queue<Float> queueOut;
    /**
     * This variable is used to know if the thread has to be of constant
     * generation.
     */
    private volatile boolean constantGeneration;
    /**
     * This variable is used to know the amount of delay that the process has.
     */
    private volatile int delayIterations;
    /**
     * This variable is used as thread stop condition.
     */
    private volatile boolean killThread;
    /**
     * This variable is used as thread pause condition.
     */
    private volatile boolean pauseThread;
    /**
     * This variable is used to know the name of the thread how share output
     * queue with the constant generation input queue.
     */
    private volatile String queueInputAssigned;
    /**
     * This variable is used to know the name of the thread how share input
     * queue with the constant generation output queue.
     */
    private volatile String queueOutputAssigned;

    /**
     * Class constructor.
     */
    public ConstantGenerationProcess() {
        this.queueIn = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = "";
        this.queueOutputAssigned = "";
    }

    /**
     * This method implements the logic of the constant generation process.
     */
    @Override
    public void run() {
        //stop condition
        while (!killThread) {
            try {
                //iteration control condition
                while (!isPauseThread()) {
                    //logic
                    if (queueIn.size() > 0) {
                        float nextNumber = queueIn.poll();
                        //in case of constant generation the number is reloaded at the end of the queue
                        if (isConstantGeneration() && getDelayIterations() == 0) {
                            queueIn.add(nextNumber);
                        } //in case that a delay exist 
                        else if (getDelayIterations() > 0) {
                            setDelayIterations(getDelayIterations() - 1);
                        }
                        this.queueOut.add(nextNumber);
                    }
                    //just in case infinite loop
                    if (userThreadDebuging) {
                        setPauseThread(true);
                    }
                    //waiting for another threads
                    Thread.sleep(200);
                    //updating GUI
                    updateToolTip();
                }
                //join threads
                Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * This method updates the tooltip message of the labels with the most
     * updated information about the queues.
     */
    public void updateToolTip() {
        hardwareGraph.updateToolTip(this.getName(), this.getQueueIn(), null, this.getQueueOut(), null);
    }

    /**
     * This method prints in the console the input queues and the output queue
     */
    public void printQueues() {
        System.out.println("Sink:");
        System.out.println("    queueIn:" + this.getQueueIn());
        System.out.println("    queueOut:" + this.getQueueOut());
        System.out.println("----------------------------------------");
    }

    /**
     * This method established the name of the current thread.
     *
     * @param threadName
     */
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

    /**
     * This method creates the join beetween the queues of each constant
     * generation process, with all the other methods, guided by the hardware
     * graph model.
     *
     * @param name String
     * @param model HardwareModel
     */
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
                case 1: //add process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Add(constantGenerationProcess, addInputProcess);
                    break;
                case 2: //product process case
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Product(constantGenerationProcess, productInputProcess);
                    break;
                case 3: //constante generation process case
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_ConstantGeneration(constantGenerationProcess, constantGenerationInputProcess);
                    break;
                case 4: //sink process case
                    SinkProcess sinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Sink(constantGenerationProcess, sinkInputProcess);
                    break;
                case 5: //queue process case
                    JoinInput_ConstantGeneration_Queue(constantGenerationProcess, hardwareAbstraction.search(model.getInputs().get(j)));
                    break;
            }

        } // joining the process output
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Duplication(constantGenerationProcess, duplicationOutputProcess);
                    break;
                case 1: //add process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Add(constantGenerationProcess, addOutputProcess);
                    break;
                case 2: //product process case
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Product(constantGenerationProcess, productOutputProcess);
                    break;
                case 3: //constant generation process case
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_ConstantGeneration(constantGenerationProcess, constantGenerationOutputProcess);
                    break;
                case 4: //sink process case
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Sink(constantGenerationProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    /**
     * This method makes the relation between the input of the constant
     * generation process with the output of an duplication process.
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param duplicationInputProcess DuplicationProcess
     */
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

    /**
     * This method makes the relation between the input of the constant
     * generation process with the output of an add process.
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param addInputProcess AddProcess
     */
    private void JoinInput_ConstantGeneration_Add(ConstantGenerationProcess constantGenerationProcess, AddProcess addInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) {//if the input 1 of the constantGeneration process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueIn(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(addInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the constant
     * generation process with the output of an product process.
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param productInputProcess ProductProcess
     */
    private void JoinInput_ConstantGeneration_Product(ConstantGenerationProcess constantGenerationProcess, ProductProcess productInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(productInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the constant
     * generation process with the output of another constant generation
     * process.
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param constantGenerationInputProcess ConstantGenerationProcess
     */
    private void JoinInput_ConstantGeneration_ConstantGeneration(ConstantGenerationProcess constantGenerationProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(constantGenerationInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the constant
     * generation process with the output of a sink process.
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param sinkInputProcess SinkProcess
     */
    private void JoinInput_ConstantGeneration_Sink(ConstantGenerationProcess constantGenerationProcess, SinkProcess sinkInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!sinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(sinkInputProcess.getQueueOut());
                sinkInputProcess.setQueueOutputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueInputAssigned(sinkInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the constant
     * generation process with the output of a queue.
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param model HardwareModel
     */
    private void JoinInput_ConstantGeneration_Queue(ConstantGenerationProcess constantGenerationProcess, HardwareModel model) {

        constantGenerationProcess.setQueueIn(model.getInputQueue());
        constantGenerationProcess.setConstantGeneration(model.isConstantGeneration());
    }

    /**
     * This method makes the relation between the output of the constant
     * generation process with the input of a duplication process
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param duplicationOutputProcess DuplicationProcess
     */
    private void JoinOutput_ConstantGeneration_Duplication(ConstantGenerationProcess constantGenerationProcess, DuplicationProcess duplicationOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the output of the constantGeneration process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                constantGenerationProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(duplicationOutputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the output of the constant
     * generation process with the input of a add process
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param addOutputProcess AddProcess
     */
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

    /**
     * This method makes the relation between the output of the constant
     * generation process with the input of a product process
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param productOutputProcess ProductProcess
     */
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

    /**
     * This method makes the relation between the output of the cpnstant
     * generation process with the input of another constant generation process
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param constantGenerationOutputProcess ConstantGenerationProcess
     */
    private void JoinOutput_ConstantGeneration_ConstantGeneration(ConstantGenerationProcess constantGenerationProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(constantGenerationProcess.getName());
                constantGenerationProcess.setQueueOutputAssigned(constantGenerationOutputProcess.getName());
            }
        }

    }

    /**
     * This method makes the relation between the output of the constant
     * generation process with the input of a sink process
     *
     * @param constantGenerationProcess ConstantGenerationProcess
     * @param sinkOutputProcess SinkProcess
     */
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
