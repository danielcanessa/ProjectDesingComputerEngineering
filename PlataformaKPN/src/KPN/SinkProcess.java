/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Daniel
 */
public class SinkProcess extends Thread {

    private Queue<Float> queueIn;
    private Queue<Float> queueOut;
    private boolean killThread;
    private boolean pauseThread;
    private boolean queueInputAssigned;
    private boolean queueOutputAssigned;

    public SinkProcess() {
        this.queueIn = new LinkedList<>();
        this.queueOut = new LinkedList<>();
        this.killThread = false;
        this.queueInputAssigned = false;
        this.queueOutputAssigned = false;

    }

    @Override
    public void run() {
        while (!killThread) {
            try {
                while (!isPauseThread()) {
                    if (queueIn.size() > 0) {
                        this.queueOut.add(queueIn.poll());

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
