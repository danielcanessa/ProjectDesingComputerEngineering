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
   
    private String hardwareName;    
    private int idFifo1;
    private int idFifo2;
    
    private int output;
    
    public FifoModel()
    {
        output=1;        
    }
    
    /**
     * @return the output
     */
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

    /**
     * @return the hardwareName
     */
    public String getHardwareName() {
        return hardwareName;
    }

    /**
     * @param hardwareName the hardwareName to set
     */
    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    /**
     * @return the idFifo1
     */
    public int getIdFifo1() {
        return idFifo1;
    }

    /**
     * @param idFifo1 the idFifo1 to set
     */
    public void setIdFifo1(int idFifo1) {
        this.idFifo1 = idFifo1;
    }

    /**
     * @return the idFifo2
     */
    public int getIdFifo2() {
        return idFifo2;
    }

    /**
     * @param idFifo2 the idFifo2 to set
     */
    public void setIdFifo2(int idFifo2) {
        this.idFifo2 = idFifo2;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(int output) {
        this.output = output;
    }
    
    
    
}
