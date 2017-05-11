/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

/**
 *
 * @author Daniel
 */
public class FifoModel {    
   
    String hardwareName;    
    int idFifo1;
    int idFifo2;
    
    int output;
    
    public FifoModel()
    {
        output=1;        
    }
    
    public int getOutput()
    {
      if(output==1)
      {
          output=2;
          return 1;
      }
      else
      {
          output=1;
          return 2;
      }
    }
    
    
    
}
