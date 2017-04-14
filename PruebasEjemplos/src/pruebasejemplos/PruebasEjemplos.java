/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebasejemplos;

import java.util.LinkedList;
import java.util.Queue;


import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    public int adder(int a, int b) {
        int result;
        result = a + b;
        return result;
    }

    public int substraction(int a, int b) {
        int result;
        result = a - b;
        return result;
    }

    public static void main(String argv[]) {
        
        
        

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("modules");
            doc.appendChild(rootElement);

            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);

            // set attribute to staff element
          
            
           
            module.setAttribute("id", "1");
            module.setAttribute("type", "adder");
            module.setAttribute("entries", "1");
            module.setAttribute("outputs", "2");
            module.setAttribute("elements", "");
         
                    
            // firstname elements
            Element tag = doc.createElement("entry_1");
            tag.appendChild(doc.createTextNode("fifo_1_1"));
            module.appendChild(tag);

            // lastname elements
            tag = doc.createElement("entry_2");
            tag.appendChild(doc.createTextNode("fifo_2_1"));
            module.appendChild(tag);

           
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
           
            
            //StreamResult result = new StreamResult(new File("file.xml"));

            // Output to console for testing
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
        }
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
