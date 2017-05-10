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
 * This class contains the definition and the methods of the product process
 * thread.
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 *
 */
public class ProductProcess extends Thread {

    /**
     * This variable represents the input queue 1 of the product process.
     */
    private volatile Queue<Float> queueIn1;
    /**
     * This variable represents the input queue 2 of the product process.
     */
    private volatile Queue<Float> queueIn2;
    /**
     * This variable represents the output queue of the product process.
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
     * queue with the product process input queue 1.
     */
    private volatile String queue1InputAssigned;
    /**
     * This variable is used to know the name of the thread how share output
     * queue with the product process input queue 2.
     */
    private volatile String queue2InputAssigned;
    /**
     * This variable is used to know the name of the thread how share input
     * queue with the product process output queue.
     */
    private volatile String queueOutputAssigned;

    public ProductProcess() {
        this.queueIn1 = new LinkedList<>();
        this.queueIn2 = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queue1InputAssigned = "";
        this.queue2InputAssigned = "";
        this.queueOutputAssigned = "";

    }

    /**
     * This method implements the logic of the product process.
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
                        this.queueOut.add(queueIn1.poll() * queueIn2.poll());

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
        System.out.println("Product:");
        System.out.println("    queueIn1:" + this.getQueueIn1());
        System.out.println("    queueIn2:" + this.getQueueIn2());
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

    public void joinProductProcess(String name, HardwareModel model) {

        ProductProcess productProcess = (ProductProcess) searchThread(name); //current product process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = KPNNetwork.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Duplication(productProcess, duplicationInputProcess);
                    break;
                case 1: //product process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Add(productProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Product(productProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_ConstantGeneration(productProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Sink(productProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = KPNNetwork.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Duplication(productProcess, duplicationOutputProcess);
                    break;
                case 1: //product process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Add(productProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Product(productProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_ConstantGeneration(productProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Sink(productProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_Product_Duplication(ProductProcess productProcess, DuplicationProcess duplicationInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                productProcess.setQueueIn1(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(productProcess.getName());
                productProcess.setQueue1InputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                productProcess.setQueueIn1(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(productProcess.getName());
                productProcess.setQueue1InputAssigned(duplicationInputProcess.getName());
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                productProcess.setQueueIn2(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(productProcess.getName());
                productProcess.setQueue2InputAssigned(duplicationInputProcess.getName());
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                productProcess.setQueueIn2(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(productProcess.getName());
                productProcess.setQueue2InputAssigned(duplicationInputProcess.getName());
            }
        }
    }

    private void JoinInput_Product_Add(ProductProcess productProcess, AddProcess addInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue1InputAssigned(addInputProcess.getName());
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue2InputAssigned(addInputProcess.getName());
            }
        }
    }

    private void JoinInput_Product_Product(ProductProcess productProcess, ProductProcess productInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue1InputAssigned(productInputProcess.getName());
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue2InputAssigned(productInputProcess.getName());
            }
        }
    }

    private void JoinInput_Product_ConstantGeneration(ProductProcess productProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue1InputAssigned(constantGenerationInputProcess.getName());
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue2InputAssigned(constantGenerationInputProcess.getName());
            }
        }

    }

    private void JoinInput_Product_Sink(ProductProcess productProcess, SinkProcess SinkInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue1InputAssigned(SinkInputProcess.getName());
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(productProcess.getName());
                productProcess.setQueue2InputAssigned(SinkInputProcess.getName());
            }
        }
    }

    private void JoinOutput_Product_Duplication(ProductProcess productProcess, DuplicationProcess duplicationOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the output of the product process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                productProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(duplicationOutputProcess.getName());
            }
        }
    }

    private void JoinOutput_Product_Add(ProductProcess productProcess, AddProcess addOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(addOutputProcess.getName());
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
                productProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(addOutputProcess.getName());
            }
        }

    }

    private void JoinOutput_Product_Product(ProductProcess productProcess, ProductProcess productOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(productOutputProcess.getName());
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
                productProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(productOutputProcess.getName());
            }
        }

    }

    private void JoinOutput_Product_ConstantGeneration(ProductProcess productProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(constantGenerationOutputProcess.getName());
            }
        }

    }

    private void JoinOutput_Product_Sink(ProductProcess productProcess, SinkProcess sinkOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(productProcess.getName());
                productProcess.setQueueOutputAssigned(sinkOutputProcess.getName());
            }
        }
    }

}
