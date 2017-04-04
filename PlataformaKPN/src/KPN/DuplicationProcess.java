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
import plataformakpn.HardwareModel;

/**
 *
 * @author Daniel
 */
public class DuplicationProcess extends Thread {

    private Queue<Float> queueIn1;
    private Queue<Float> queueOut2;
    private Queue<Float> queueOut1;
    private boolean killThread;
    private boolean pauseThread;

    private String queueInputAssigned;
    private String queueOutput1Assigned;
    private String queueOutput2Assigned;

    public DuplicationProcess() {
        this.queueIn1 = new LinkedList<>();
        this.queueOut2 = new LinkedList<>();
        this.queueOut1 = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = "";
        this.queueOutput1Assigned = "";
        this.queueOutput2Assigned = "";

    }

    @Override
    public void run() {
        while (!killThread) {
            try {
                while (!isPauseThread()) {
                    if (queueIn1.size() > 0) {
                        float value = queueIn1.poll();
                        this.queueOut1.add(value);
                        this.queueOut2.add(value);
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
        System.out.println("Duplication:");
        System.out.println("    queueIn:" + this.getQueueIn());
        System.out.println("    queueOut2:" + this.getQueueOut2());
        System.out.println("    queueOut:" + this.getQueueOut1());
        System.out.println("----------------------------------------");
    }

    public void setThreadName(String threadName) {
        this.setName(threadName);
    }

    /**
     * @return the queueIn1
     */
    public Queue<Float> getQueueIn() {
        return queueIn1;
    }

    /**
     * @param queueIn1 the queueIn1 to set
     */
    public void setQueueIn1(Queue<Float> queueIn1) {
        this.queueIn1 = queueIn1;
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
                case 1: //duplication process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Add(duplicationProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Product(duplicationProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_ConstantGeneration(duplicationProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Duplication_Sink(duplicationProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Duplication(duplicationProcess, duplicationOutputProcess);
                    break;
                case 1: //duplication process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Add(duplicationProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Product(duplicationProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_ConstantGeneration(duplicationProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Duplication_Sink(duplicationProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_Duplication_Duplication(DuplicationProcess duplicationProcess, DuplicationProcess duplicationInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                duplicationProcess.setQueueIn1(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                duplicationProcess.setQueueIn1(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(duplicationInputProcess.getName());
            }

        }
    }

    private void JoinInput_Duplication_Add(DuplicationProcess duplicationProcess, AddProcess addInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the duplication process still without assignation
                duplicationProcess.setQueueIn1(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(addInputProcess.getName());
            }
        }
    }

    private void JoinInput_Duplication_Product(DuplicationProcess duplicationProcess, ProductProcess productInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueIn1(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(productInputProcess.getName());
            }
        }
    }

    private void JoinInput_Duplication_ConstantGeneration(DuplicationProcess duplicationProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueIn1(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(constantGenerationInputProcess.getName());
            }
        }
    }

    private void JoinInput_Duplication_Sink(DuplicationProcess duplicationProcess, SinkProcess SinkInputProcess) {
        if (!duplicationProcess.isQueueInputAssigned()) { //if the input 1 of the duplication process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueIn1(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueInputAssigned(SinkInputProcess.getName());
            }
        }
    }

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

    private void JoinOutput_Duplication_ConstantGeneration(DuplicationProcess duplicationProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut1(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(constantGenerationOutputProcess.getName());
            }
        }
        else if(!duplicationProcess.isQueueOutput2Assigned())
        {
              if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut2(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(constantGenerationOutputProcess.getName());
            }
            
        }

    }

    private void JoinOutput_Duplication_Sink(DuplicationProcess duplicationProcess, SinkProcess sinkOutputProcess) {
        if (!duplicationProcess.isQueueOutput1Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut1(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput1Assigned(sinkOutputProcess.getName());
            }
        }
        else if (!duplicationProcess.isQueueOutput2Assigned()) { //if the input 1 of the duplication process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                duplicationProcess.setQueueOut2(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(duplicationProcess.getName());
                duplicationProcess.setQueueOutput2Assigned(sinkOutputProcess.getName());
            }
        }

    }

}
