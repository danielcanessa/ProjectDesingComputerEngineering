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
import plataformakpn.HardwareModel;

/**
 * This class contains the definition and the methods of the sink process
 * thread.
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 *
 */
public class SinkProcess extends Thread {

    /**
     * This variable represents the input queue of the sink process.
     */
    private volatile Queue<Float> queueIn;
    /**
     * This variable represents the output queue of the sink process.
     */
    private volatile Queue<Float> queueOut;
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
     * queue with the sink process input queue.
     */
    private volatile String queueInputAssigned;
    /**
     * This variable is used to know the name of the thread how share input
     * queue with the sink process output queue.
     */
    private volatile String queueOutputAssigned;

    /**
     * Class constructor.
     */
    public SinkProcess() {
        this.queueIn = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = "";
        this.queueOutputAssigned = "";
    }

    /**
     * This method implements the logic of the sink process.
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
                        this.queueOut.add(queueIn.poll());
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

    public void joinSinkProcess(String name, HardwareModel model) {

        SinkProcess sinkProcess = (SinkProcess) searchThread(name); //current sink process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = KPNNetwork.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Duplication(sinkProcess, duplicationInputProcess);
                    break;
                case 1: //sink process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Add(sinkProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Product(sinkProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_ConstantGeneration(sinkProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Sink(sinkProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Duplication(sinkProcess, duplicationOutputProcess);
                    break;
                case 1: //sink process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Add(sinkProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Product(sinkProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_ConstantGeneration(sinkProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Sink(sinkProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_Sink_Duplication(SinkProcess sinkProcess, DuplicationProcess duplicationInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                sinkProcess.setQueueIn(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(sinkProcess.getName());
                sinkProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                sinkProcess.setQueueIn(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(sinkProcess.getName());
                sinkProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            }
        }
    }

    private void JoinInput_Sink_Add(SinkProcess sinkProcess, AddProcess addInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) {//if the input 1 of the sink process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the sink process still without assignation
                sinkProcess.setQueueIn(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(sinkProcess.getName());
                sinkProcess.setQueueInputAssigned(addInputProcess.getName());
            }
        }
    }

    private void JoinInput_Sink_Product(SinkProcess sinkProcess, ProductProcess productInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueIn(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(sinkProcess.getName());
                sinkProcess.setQueueInputAssigned(productInputProcess.getName());
            }
        }
    }

    private void JoinInput_Sink_ConstantGeneration(SinkProcess sinkProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueIn(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(sinkProcess.getName());
                sinkProcess.setQueueInputAssigned(constantGenerationInputProcess.getName());
            }
        }
    }

    private void JoinInput_Sink_Sink(SinkProcess sinkProcess, SinkProcess SinkInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueIn(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(sinkProcess.getName());
                sinkProcess.setQueueInputAssigned(SinkInputProcess.getName());
            }
        }
    }

    private void JoinOutput_Sink_Duplication(SinkProcess sinkProcess, DuplicationProcess duplicationOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the output of the sink process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                sinkProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(duplicationOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_Sink_Add(SinkProcess sinkProcess, AddProcess addOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the sink process still without assignation
                sinkProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(addOutputProcess.getName());
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the sink process is already assigned but input 2 still without assignation
                sinkProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(addOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_Sink_Product(SinkProcess sinkProcess, ProductProcess productOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the sink process still without assignation
                sinkProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(productOutputProcess.getName());
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the sink process is already assigned but input 2 still without assignation
                sinkProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(productOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_Sink_ConstantGeneration(SinkProcess sinkProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(constantGenerationOutputProcess.getName());
            }
        }

    }

    private void JoinOutput_Sink_Sink(SinkProcess sinkProcess, SinkProcess sinkOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(sinkProcess.getName());
                sinkProcess.setQueueOutputAssigned(sinkOutputProcess.getName());
            }
        }

    }

}
