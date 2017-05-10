/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import java.util.LinkedList;
import java.util.Queue;
import static plataformakpn.GUI.hardwareGraph;
import static plataformakpn.GUI.userThreadDebuging;
import plataformakpn.HardwareModel;

/**
 * This class contains the definition and the methods of the add process thread.
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 */
public class AddProcess extends Thread {

    /**
     * This variable represents the input queue 1 of the adder.
     */
    private volatile Queue<Float> queueIn1;
    /**
     * This variable represents the input queue 2 of the adder.
     */
    private volatile Queue<Float> queueIn2;
    /**
     * This variable represents the output queue of the adder.
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
     * queue with the adder input queue 1.
     */
    private volatile String queue1InputAssigned;
    /**
     * This variable is used to know the name of the thread how share output
     * queue with the adder input queue 2.
     */
    private volatile String queue2InputAssigned;
    /**
     * This variable is used to know the name of the thread how share input
     * queue with the adder output queue.
     */
    private volatile String queueOutputAssigned;

    /**
     * Class constructor.
     */
    public AddProcess() {
        this.queueIn1 = new LinkedList<>();
        this.queueIn2 = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queue1InputAssigned = "";
        this.queue2InputAssigned = "";
        this.queueOutputAssigned = "";
    }

    /**
     * This method implements the logic of the adder process.
     */
    @Override
    public void run() {
        //stop condition
        while (!killThread) {
            try {
                //iteration control condition
                while (!isPauseThread()) {
                    //logic
                    if (queueIn1.size() > 0 && queueIn2.size() > 0) {
                        this.queueOut.add(queueIn1.poll() + queueIn2.poll());
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
        hardwareGraph.updateToolTip(this.getName(), this.getQueueIn1(), this.getQueueIn2(), this.getQueueOut(), null);
    }

    /**
     * This method prints in the console the input queues and the output queue
     */
    public void printQueues() {
        System.out.println("Add:");
        System.out.println("    queueIn1:" + this.getQueueIn1());
        System.out.println("    queueIn2:" + this.getQueueIn2());
        System.out.println("    queueOut:" + this.getQueueOut());
        System.out.println("----------------------------------------");
    }

    /**
     * This method established the name of the current thread.
     * @param threadName 
     */
    public void setThreadName(String threadName) {
        this.setName(threadName);
    }

    /**
     * @return the queueIn1
     */
    public Queue<Float> getQueueIn1() {
        return queueIn1;
    }

    /**
     * @param queueIn1 the queueIn1 to set
     */
    public void setQueueIn1(Queue<Float> queueIn1) {
        this.queueIn1 = queueIn1;
    }

    /**
     * @return the queueIn2
     */
    public Queue<Float> getQueueIn2() {
        return queueIn2;
    }

    /**
     * @param queueIn2 the queueIn2 to set
     */
    public void setQueueIn2(Queue<Float> queueIn2) {
        this.queueIn2 = queueIn2;
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
     * @return the queue1InputAssigned
     */
    public boolean isQueue1InputAssigned() {
        return !queue1InputAssigned.equals("");
    }

    /**
     * @param queue1InputAssigned the queue1InputAssigned to set
     */
    public void setQueue1InputAssigned(String queue1InputAssigned) {
        this.queue1InputAssigned = queue1InputAssigned;
    }

    /**
     * @return the queue2InputAssigned
     */
    public boolean isQueue2InputAssigned() {
        return !queue2InputAssigned.equals("");
    }

    /**
     * @param queue2InputAssigned the queue2InputAssigned to set
     */
    public void setQueue2InputAssigned(String queue2InputAssigned) {
        this.queue2InputAssigned = queue2InputAssigned;
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
     * @return the queue1InputAssigned
     */
    public String getQueue1InputAssigned() {
        return queue1InputAssigned;
    }

    /**
     * @return the queue2InputAssigned
     */
    public String getQueue2InputAssigned() {
        return queue2InputAssigned;
    }

    /**
     * @return the queueOutputAssigned
     */
    public String getQueueOutputAssigned() {
        return queueOutputAssigned;
    }

    /**
     * This method creates the join beetween the queues of each add process, 
     * with all the other methods, guided by the hardware graph model.
     * @param name String
     * @param model HardwareModel
     */
    public void joinAddProcess(String name, HardwareModel model) {

        AddProcess addProcess = (AddProcess) KPNNetwork.searchThread(name); //current add process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = KPNNetwork.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) KPNNetwork.searchThread(inputName); //getting the process
                    joinInput_Add_Duplication(addProcess, duplicationInputProcess);
                    break;
                case 1: //add process case
                    AddProcess addInputProcess = (AddProcess) KPNNetwork.searchThread(inputName); //getting the process
                    joinInput_Add_Add(addProcess, addInputProcess);
                    break;
                case 2: //product process case
                    ProductProcess productInputProcess = (ProductProcess) KPNNetwork.searchThread(inputName); //getting the process
                    joinInput_Add_Product(addProcess, productInputProcess);
                    break;
                case 3: //constant generation process case
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) KPNNetwork.searchThread(inputName); //getting the process
                    joinInput_Add_ConstantGeneration(addProcess, constantGenerationInputProcess);
                    break;
                case 4: //sink process case
                    SinkProcess SinkInputProcess = (SinkProcess) KPNNetwork.searchThread(inputName); //getting the process
                    joinInput_Add_Sink(addProcess, SinkInputProcess);
                    break;
            }

        }
        //join the process output with the inputs
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) KPNNetwork.searchThread(outputName); //getting the process
                    joinOutput_Add_Duplication(addProcess, duplicationOutputProcess);
                    break;
                case 1: //add process case
                    AddProcess addOutputProcess = (AddProcess) KPNNetwork.searchThread(outputName); //getting the process
                    joinOutput_Add_Add(addProcess, addOutputProcess);
                    break;
                case 2: // product process case
                    ProductProcess productOutputProcess = (ProductProcess) KPNNetwork.searchThread(outputName); //getting the process
                    joinOutput_Add_Product(addProcess, productOutputProcess);
                    break;
                case 3: // constant generation process case
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) KPNNetwork.searchThread(outputName); //getting the process
                    joinOutput_Add_ConstantGeneration(addProcess, constantGenerationOutputProcess);
                    break;
                case 4: // sink process case
                    SinkProcess sinkOutputProcess = (SinkProcess) KPNNetwork.searchThread(outputName); //getting the process
                    joinOutput_Add_Sink(addProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    /**
     * This method makes the relation between an input of add process with the 
     * output of an duplication process
     * @param addProcess AddProcess
     * @param duplicationInputProcess  DuplicationProcess
     */
    private void joinInput_Add_Duplication(AddProcess addProcess, DuplicationProcess duplicationInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                addProcess.setQueueIn1(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(addProcess.getName());
                addProcess.setQueue1InputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                addProcess.setQueueIn1(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(addProcess.getName());
                addProcess.setQueue1InputAssigned(duplicationInputProcess.getName());
            }
        } else if (!addProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                addProcess.setQueueIn2(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(addProcess.getName());
                addProcess.setQueue2InputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                addProcess.setQueueIn2(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(addProcess.getName());
                addProcess.setQueue2InputAssigned(duplicationInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between an input of add process with the 
     * output of another add process
     * @param addProcess AddProcess
     * @param addInputProcess  AddProcess
     */
    private void joinInput_Add_Add(AddProcess addProcess, AddProcess addInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueIn1(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue1InputAssigned(addInputProcess.getName());
            }
        } else if (!addInputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueIn2(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue2InputAssigned(addInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between an input of add process with the 
     * output of a product process
     * @param addProcess AddProcess
     * @param productInputProcess ProductProcess
     */
    private void joinInput_Add_Product(AddProcess addProcess, ProductProcess productInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn1(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue1InputAssigned(productInputProcess.getName());
            }
        } else if (!productInputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn2(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue2InputAssigned(productInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between an input of add process with the 
     * output of a constant generation process
     * @param addProcess AddProcess
     * @param constantGenerationInputProcess ConstantGenerationProcess
     */
    private void joinInput_Add_ConstantGeneration(AddProcess addProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn1(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue1InputAssigned(constantGenerationInputProcess.getName());
            }
        } else if (!addProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn2(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue2InputAssigned(constantGenerationInputProcess.getName());
            }
        }

    }

    /**
     * This method makes the relation between an input of add process with the 
     * output of a sink process
     * @param addProcess AddProcess
     * @param SinkInputProcess SinkProcess
     */
    private void joinInput_Add_Sink(AddProcess addProcess, SinkProcess SinkInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn1(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue1InputAssigned(SinkInputProcess.getName());
            }
        } else if (!addProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn2(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(addProcess.getName());
                addProcess.setQueue2InputAssigned(SinkInputProcess.getName());
            }
        }
    }

    /**
     * This method makes the relation between an input of add process with the 
     * output of a duplication process
     * @param addProcess AddProcess
     * @param duplicationOutputProcess DuplicationProcess
     */
    private void joinOutput_Add_Duplication(AddProcess addProcess, DuplicationProcess duplicationOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the output of the add process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                addProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(duplicationOutputProcess.getName());
            }
        }

    }

    /**
     * This method makes the relation between an output of add process with the 
     * input of another add process
     * @param addProcess AddProcess
     * @param addOutputProcess AddProcess
     */
    private void joinOutput_Add_Add(AddProcess addProcess, AddProcess addOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(addOutputProcess.getName());
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
                addProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(addOutputProcess.getName());
            }
        }

    }

    /**
     * This method makes the relation between an output of add process with the 
     * input of a product process
     * @param addProcess AddProcess
     * @param productOutputProcess ProductProcess
     */
    private void joinOutput_Add_Product(AddProcess addProcess, ProductProcess productOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(productOutputProcess.getName());
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
                addProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(productOutputProcess.getName());
            }
        }

    }

    /**
     * This method makes the relation between an output of add process with the 
     * input of a constant generation process
     * @param addProcess AddProcess
     * @param constantGenerationOutputProcess ConstantGenerationProcess
     */
    private void joinOutput_Add_ConstantGeneration(AddProcess addProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(constantGenerationOutputProcess.getName());
            }
        }

    }

    /**
     * This method makes the relation between an output of add process with the 
     * input of a sink process
     * @param addProcess AddProcess
     * @param constantGenerationOutputProcess SinkProcess
     */
    private void joinOutput_Add_Sink(AddProcess addProcess, SinkProcess sinkOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(addProcess.getName());
                addProcess.setQueueOutputAssigned(sinkOutputProcess.getName());
            }
        }
    }
}
