/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebasejemplos;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Daniel
 */
public class Hilo1 {

    private Queue<Integer> queueIn;
    private Queue<Integer> queueOut;

    public Hilo1()
    {
        this.queueIn = new LinkedList<>();
        this.queueOut = new LinkedList<>();
    }
    
    public void setIn(Queue<Integer> queueIn){
        this.setQueueIn(queueIn);
    }

    /**
     * @return the queueIn
     */
    public Queue<Integer> getQueueIn() {
        return queueIn;
    }

    /**
     * @return the queueOut
     */
    public Queue<Integer> getQueueOut() {
        return queueOut;
    }

    /**
     * @param queueIn the queueIn to set
     */
    public void setQueueIn(Queue<Integer> queueIn) {
        this.queueIn = queueIn;
    }

    /**
     * @param queueOut the queueOut to set
     */
    public void setQueueOut(Queue<Integer> queueOut) {
        this.queueOut = queueOut;
    }

}
