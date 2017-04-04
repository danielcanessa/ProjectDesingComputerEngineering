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
public class SquareTest {
    
    public SquareTest() {
    }
    
    @Test
    public void test()
    {
        JUnitTesting test = new JUnitTesting();
        int output = test.square(5);
        assertEquals(25, output);
    }
    
}
