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

    
    
    /*public static int factorial(int number)
    {
        int result=1;
        while(number>0)
        {           
            result=result*number;
            number--;            
        }
        return result;
    }
    
    public static int castStringToInt(String a)
    {
        int result=0;
        
        int factor=1;
        
        int negative=1;
        
        int stop=0;
       
        if(a.charAt(0)=='-')
        {
            negative*=-1;
            stop++;
            
        }
        
        for (int i = a.length()-1; i >=stop; i--) {        
            
            result= result+((a.charAt(i) - '0')*factor);
            factor*=10;
                        
        }
        
        result*=negative;
        
        return result;
    }*/

    public int adder(int a, int b)
    {
        int result;
        result = a + b;
        return result;
    }
    
    public int substraction(int a, int b)
    {        
        int result;
        result = a - b;
        return result;
    }
    
    public void main(String[] args) {
       
        String a ="-123456";
        
        
        
      /*  System.out.println("String to int: " + castStringToInt(a));
        
        int b=1234567;
        char  result[10];
        
        int i =0;
        while(b!=0)
        {
            result[i] = (char) (b%10 + '0');
        }
        
        System.out.println((7 + '0'));*/
        
        
        
        
     //   System.out.println(factorial(5));
        
        
       /* Hilo1 a = new Hilo1();
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
        System.out.println("Size of queue-" + size);*/
    }

}
