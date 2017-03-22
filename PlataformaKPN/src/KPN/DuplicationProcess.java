/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import java.util.LinkedList;
import java.util.Queue;
import plataformakpn.GUI;

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

    private boolean queueInputAssigned;
    private boolean queueOutput1Assigned;
    private boolean queueOutput2Assigned;

    public DuplicationProcess() {
        this.queueIn1 = new LinkedList<>();
        this.queueOut2 = new LinkedList<>();
        this.queueOut1 = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = false;
        this.queueOutput1Assigned = false;
        this.queueOutput2Assigned = false;

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
                    setPauseThread(true);

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
        return queueInputAssigned;
    }

    /**
     * @param queueInputAssigned the queueInputAssigned to set
     */
    public void setQueueInputAssigned(boolean queueInputAssigned) {
        this.queueInputAssigned = queueInputAssigned;
    }

    /**
     * @return the queueOutput1Assigned
     */
    public boolean isQueueOutput1Assigned() {
        return queueOutput1Assigned;
    }

    /**
     * @param queueOutput1Assigned the queueOutput1Assigned to set
     */
    public void setQueueOutput1Assigned(boolean queueOutput1Assigned) {
        this.queueOutput1Assigned = queueOutput1Assigned;
    }

    /**
     * @return the queueOutput2Assigned
     */
    public boolean isQueueOutput2Assigned() {
        return queueOutput2Assigned;
    }

    /**
     * @param queueOutput2Assigned the queueOutput2Assigned to set
     */
    public void setQueueOutput2Assigned(boolean queueOutput2Assigned) {
        this.queueOutput2Assigned = queueOutput2Assigned;
    }

}
