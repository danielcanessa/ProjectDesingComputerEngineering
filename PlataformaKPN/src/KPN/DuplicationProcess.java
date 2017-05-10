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
 * This class contains the definition and the methods of the duplication process
 * thread.
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 */
public class DuplicationProcess extends Thread {

    /**
     * This variable represents the input queue of the duplication process.
     */
    private Queue<Float> queueIn;
    /**
     * This variable represents the output queue 1 of the duplication process.
     */
    private Queue<Float> queueOut1;
    /**
     * This variable represents the output queue 2 of the duplication process.
     */
    private Queue<Float> queueOut2;
    /**
     * This variable is used as thread stop condition.
     */
    private boolean killThread;
    /**
     * This variable is used as thread pause condition.
     */
    private boolean pauseThread;
    /**
     * This variable is used to know the name of the thread how share output
     * queue with the duplication process input queue.
     */
    private String queueInputAssigned;
    /**
     * This variable is used to know the name of the thread how share input
     * queue with the duplication process output queue 1.
     */
    private String queueOutput1Assigned;
    /**
     * This variable is used to know the name of the thread how share input
     * queue with the duplication process output queue 2.
     */
    private String queueOutput2Assigned;
    /**
     * This variable is used to know the output assigned in the XML generated.
     */
    private int XMLOutput;

    /**
     * Class constructor.
     */
    public DuplicationProcess() {
        this.queueIn = new LinkedList<>();
        this.queueOut2 = new LinkedList<>();
        this.queueOut1 = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = "";
        this.queueOutput1Assigned = "";
        this.queueOutput2Assigned = "";

        XMLOutput = 0;

    }

    /**
     * This method implements the logic of the duplication process.
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
                        float value = queueIn.poll();
                        this.queueOut1.add(value);
                        this.queueOut2.add(value);
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
        hardwareGraph.updateToolTip(this.getName(), this.getQueueIn(), null, this.getQueueOut1(), this.getQueueOut2());
    }

     /**
     * This method prints in the console the input queues and the output queue
     */
    public void printQueues() {
        System.out.println("Duplication:");
        System.out.println("    queueIn:" + this.getQueueIn());
        System.out.println("    queueOut2:" + this.getQueueOut2());
        System.out.println("    queueOut:" + this.getQueueOut1());
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
     * @return the queueOut2
     */
    public Queue<Float> getQueueOut2() {
        return queueOut2;
    }

    /**
     * @param queueOut2 the queueOut2 to set
     */
    public void setQueueOut2(Queue<Float> queueOut2) {
        this.queueOut2 = queueOut2;
    }

    /**
     * @return the queueOut1
     */
    public Queue<Float> getQueueOut1() {
        return queueOut1;
    }

    /**
     * @param queueOut1 the queueOut1 to set
     */
    public void setQueueOut1(Queue<Float> queueOut1) {
        this.queueOut1 = queueOut1;
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
     * @return the queueOutput1Assigned
     */
    public boolean isQueueOutput1Assigned() {
        return !queueOutput1Assigned.equals("");
    }

    /**
     * @param queueOutput1Assigned the queueOutput1Assigned to set
     */
    public void setQueueOutput1Assigned(String queueOutput1Assigned) {
        this.queueOutput1Assigned = queueOutput1Assigned;
    }

    /**
     * @return the queueOutput2Assigned
     */
    public boolean isQueueOutput2Assigned() {
        return !queueOutput2Assigned.equals("");
    }

    /**
     * @param queueOutput2Assigned the queueOutput2Assigned to set
     */
    public void setQueueOutput2Assigned(String queueOutput2Assigned) {
        this.queueOutput2Assigned = queueOutput2Assigned;
    }

    /**
     * @return the queueInputAssigned
     */
    public String getQueueInputAssigned() {
        return queueInputAssigned;
    }

    /**
     * @return the queueOutput1Assigned
     */
    public String getQueueOutput1Assigned() {
        return queueOutput1Assigned;
    }

    /**
     * @return the queueOutput2Assigned
     */
    public String getQueueOutput2Assigned() {
        return queueOutput2Assigned;
    }

    /**
     * This method creates the join beetween the queues of each constant
     * duplication process, with all the other methods, guided by the hardware
     * graph model.
     *
     * @param name String
     * @param model HardwareModel
     */
    public void joinDuplicationProcess(String name, HardwareModel model) {

        DuplicationProcess duplicationProcess = (DuplicationProcess) searchThread(name); //current duplication process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = KPNNetwork.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Duplication(duplicationProcess, duplicationInputProcess);
                    break;
                case 1: //add process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Add(duplicationProcess, addInputProcess);
                    break;
                case 2: //product process case
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Product(duplicationProcess, productInputProcess);
                    break;
                case 3: //constant generation process case
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_ConstantGeneration(duplicationProcess, constantGenerationInputProcess);
                    break;
                case 4: //sink process case
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Sink(duplicationProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) { //join the process with the outputs
            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Duplication(duplicationProcess, duplicationOutputProcess);
                    break;
                case 1: //add process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Add(duplicationProcess, addOutputProcess);
                    break;
                case 2: //product process case
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Product(duplicationProcess, productOutputProcess);
                    break;
                case 3: //constant generation process case
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_ConstantGeneration(duplicationProcess, constantGenerationOutputProcess);
                    break;
                case 4: //sink process case
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Sink(duplicationProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    /**
     * This method makes the relation between the input of the duplication
     * process with the output of another duplication process.
     *
     * @param duplicationProcess DuplicationProcess
     * @param duplicationInputProcess DuplicationProcess
     */
    private void JoinInput_Duplication_Duplication(DuplicationProcess duplicationProcess, DuplicationProcess duplicationInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                duplicationProcess.setQueueIn(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                duplicationProcess.setQueueIn(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            }

        }
    }

    /**
     * This method makes the relation between the input of the duplication
     * process with the output of an add process.
     *
     * @param duplicationProcess DuplicationProcess
     * @param addInputProcess AddProcess
     */
    private void JoinInput_Duplication_Add(DuplicationProcess duplicationProcess, AddProcess addInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the duplication process still without assignation
                duplicationProcess.setQueueIn(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(addInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the duplication
     * process with the output of a product process.
     *
     * @param duplicationProcess DuplicationProcess
     * @param productInputProcess ProductProcess
     */
    private void JoinInput_Duplication_Product(DuplicationProcess duplicationProcess, ProductProcess productInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueIn(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(productInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the duplication
     * process with the output of a constant generation process.
     *
     * @param duplicationProcess DuplicationProcess
     * @param constantGenerationInputProcess ConstantGenerationProcess
     */
    private void JoinInput_Duplication_ConstantGeneration(DuplicationProcess duplicationProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueIn(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(constantGenerationInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the input of the duplication
     * process with the output of a sink process.
     *
     * @param duplicationProcess DuplicationProcess
     * @param SinkInputProcess SinkProcess
     */
    private void JoinInput_Duplication_Sink(DuplicationProcess duplicationProcess, SinkProcess SinkInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueIn(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(SinkInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the output of the duplication
     * process with the input of another duplication process
     *
     * @param duplicationProcess DuplicationProcess
     * @param duplicationOutputProcess DuplicationProcess
     */
    private void JoinOutput_Duplication_Duplication(DuplicationProcess duplicationProcess, DuplicationProcess duplicationOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the output of the duplication process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                duplicationProcess.setQueueOut1(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(duplicationOutputProcess.getName());
            }
        } else if (!duplicationProcess.isQueueOutput2Assigned()) {
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                duplicationProcess.setQueueOut2(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(duplicationOutputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between the output of the duplication
     * process with the input of an add process
     *
     * @param duplicationProcess DuplicationProcess
     * @param addOutputProcess AddProcess
     */
    private void JoinOutput_Duplication_Add(DuplicationProcess duplicationProcess, AddProcess addOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the duplication process still without assignation
                duplicationProcess.setQueueOut1(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(addOutputProcess.getName());
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the duplication process is already assigned but input 2 still without assignation
                duplicationProcess.setQueueOut1(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(addOutputProcess.getName());
            }
        } else if (!duplicationProcess.isQueueOutput2Assigned()) {
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the duplication process still without assignation
                duplicationProcess.setQueueOut2(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(addOutputProcess.getName());
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the duplication process is already assigned but input 2 still without assignation
                duplicationProcess.setQueueOut2(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(addOutputProcess.getName());
            }

        }

    }

    /**
     * This method makes the relation between the output of the duplication
     * process with the input of a product process
     *
     * @param duplicationProcess DuplicationProcess
     * @param productOutputProcess ProductProcess
     */
    private void JoinOutput_Duplication_Product(DuplicationProcess duplicationProcess, ProductProcess productOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the duplication process still without assignation
                duplicationProcess.setQueueOut1(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(productOutputProcess.getName());
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the duplication process is already assigned but input 2 still without assignation
                duplicationProcess.setQueueOut1(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(productOutputProcess.getName());
            }
        } else if (!duplicationProcess.isQueueOutput2Assigned()) {
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the duplication process still without assignation
                duplicationProcess.setQueueOut2(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(productOutputProcess.getName());
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the duplication process is already assigned but input 2 still without assignation
                duplicationProcess.setQueueOut2(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(productOutputProcess.getName());
            }

        }

    }

    /**
     * This method makes the relation between the output of the duplication
     * process with the input of a constant generation process
     *
     * @param duplicationProcess DuplicationProcess
     * @param constantGenerationOutputProcess ConstantGenerationProcess
     */
    private void JoinOutput_Duplication_ConstantGeneration(DuplicationProcess duplicationProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut1(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(constantGenerationOutputProcess.getName());
            }
        } else if (!duplicationProcess.isQueueOutput2Assigned()) {
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut2(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(constantGenerationOutputProcess.getName());
            }

        }

    }

    /**
     * This method makes the relation between the output of the duplication
     * process with the input of a sink process
     *
     * @param duplicationProcess DuplicationProcess
     * @param sinkOutputProcess SinkProcess
     */
    private void JoinOutput_Duplication_Sink(DuplicationProcess duplicationProcess, SinkProcess sinkOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut1(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(sinkOutputProcess.getName());
            }
        } else if (!duplicationProcess.isQueueOutput2Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut2(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(sinkOutputProcess.getName());
            }
        }

    }

    /**
     * @return the XMLOutput
     */
    public int getXMLOutput() {
        switch (XMLOutput) {
            case 0:
                XMLOutput = 1;
                break;
            case 1:
                XMLOutput = 2;
                break;
            case 2:
                XMLOutput = 1;
                break;
            default:
                break;
        }
        return XMLOutput;
    }

}
