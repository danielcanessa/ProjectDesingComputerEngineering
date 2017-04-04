/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import static org.junit.Assert.*;
import testing.JUnitTesting;

/**
 *
 * @author Daniel
 */
public class CountATest {
    
    public CountATest() {
    }
    
    @Test
    public void test(){
        JUnitTesting test = new JUnitTesting();
        int output=test.countA("lala");
        assertEquals(2,output);
    }
    
    @Test
    public void test1(){
        JUnitTesting test = new JUnitTesting();
        int output=test.countA("lalala");
        assertEquals(3,output);
    }
}
