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
public class PruebasEjemplos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Hilo1 a = new Hilo1();
        Hilo2 b = new Hilo2();
        
        
        b.setQueueIn(a.getQueueOut());
        // Adds elements {0, 1, 2, 3, 4} to queue
        for (int i = 0; i < 5; i++)
        {
            a.getQueueOut().add(i);
        }

        // Display contents of the queue.
        System.out.println("Elements of queue:" + b.getQueueIn());

        // To remove the head of queue.
        int removedele = b.getQueueIn().remove();
        System.out.println("removed element-" + removedele);

        System.out.println(b.getQueueIn());

        // To view the head of queue
        int head = b.getQueueIn().peek();
        System.out.println("head of queue-" + head);

        // Rest all methods of collection interface,
        // Like size and contains can be used with this
        // implementation.
        int size = b.getQueueIn().size();
        System.out.println("Size of queue-" + size);
    }

}
