/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import plataformakpn.GUI;

/**
 *
 * @author Daniel
 */
public class AddProcess extends Thread {

    private Queue<Float> queueIn1;
    private Queue<Float> queueIn2;
    private Queue<Float> queueOut;
    private boolean killThread;
    private boolean pauseThread;

    private boolean queue1InputAssigned;
    private boolean queue2InputAssigned;
    private boolean queueOutputAssigned;

    public AddProcess() {
        this.queueIn1 = new LinkedList<>();
        this.queueIn2 = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queue1InputAssigned = false;
        this.queue2InputAssigned = false;
        this.queueOutputAssigned = false;

    }

    @Override
    public void run() {
        while (!killThread) {
            try {
                while (!isPauseThread()) {
                    if (queueIn1.size() > 0 && queueIn2.size() > 0) {
                        this.queueOut.add(queueIn1.poll() + queueIn2.poll());

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
        System.out.println("Add:");
        System.out.println("    queueIn1:" + this.getQueueIn1());
        System.out.println("    queueIn2:" + this.getQueueIn2());
        System.out.println("    queueOut:" + this.getQueueOut());
        System.out.println("----------------------------------------");
    }

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
        return queue1InputAssigned;
    }

    /**
     * @param queue1InputAssigned the queue1InputAssigned to set
     */
    public void setQueue1InputAssigned(boolean queue1InputAssigned) {
        this.queue1InputAssigned = queue1InputAssigned;
    }

    /**
     * @return the queue2InputAssigned
     */
    public boolean isQueue2InputAssigned() {
        return queue2InputAssigned;
    }

    /**
     * @param queue2InputAssigned the queue2InputAssigned to set
     */
    public void setQueue2InputAssigned(boolean queue2InputAssigned) {
        this.queue2InputAssigned = queue2InputAssigned;
    }

    /**
     * @return the queueOutputAssigned
     */
    public boolean isQueueOutputAssigned() {
        return queueOutputAssigned;
    }

    /**
     * @param queueOutputAssigned the queueOutputAssigned to set
     */
    public void setQueueOutputAssigned(boolean queueOutputAssigned) {
        this.queueOutputAssigned = queueOutputAssigned;
    }

}
