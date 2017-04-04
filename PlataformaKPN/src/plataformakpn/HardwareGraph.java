/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plataformakpn;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author Daniel
 */
public class HardwareGraph {

    public List<HardwareModel> hardwareGraph;
    private int hardwareIdentifier;

    public HardwareGraph() {
        hardwareGraph = new ArrayList<>();
        hardwareIdentifier =0;
    }
    
    public boolean verifySameOutput(HardwareModel model, JLabel label)
    {       
        int indexRepeat = 0;
        for (int i = 0; i < model.getOutputs().size(); i++) {
            if(model.getOutputs().get(i).equals(label))
            {
                indexRepeat++;
            }            
        }
        return indexRepeat>1;
        
    }

    public HardwareModel get(int i) {
        return hardwareGraph.get(i);
    }
    
    public HardwareModel search(JLabel label) {
        for (int i = 0; i < hardwareGraph.size(); i++) {
            if(hardwareGraph.get(i).getLabel().equals(label))
            {
                return hardwareGraph.get(i);                
            }            
        }
        return null;
    }

    public void add(HardwareModel model) {
        hardwareGraph.add(model);
    }

    public int size() {
        return hardwareGraph.size();
    }

    public void printGraph()
    {
        for (int i = 0; i < hardwareGraph.size(); i++) {
            System.out.println("Nodo: "+hardwareGraph.get(i).getLabel().getName());
            System.out.println("    Entradas:");
            for (int j = 0; j < hardwareGraph.get(i).getInputs().size(); j++) {
                System.out.println("        "+hardwareGraph.get(i).getInputs().get(j).getName());               
            }
            System.out.println("    Salidas:");
            for (int j = 0; j < hardwareGraph.get(i).getOutputs().size(); j++) {
                System.out.println("        "+hardwareGraph.get(i).getOutputs().get(j).getName());
            }
            System.out.println("    Cola entrada:");
            System.out.println("        "+hardwareGraph.get(i).getInputQueue());
            
            System.out.println("    Generación constante: "+hardwareGraph.get(i).isConstantGeneration() +", Delay: "+hardwareGraph.get(i).getDelayIterations());   
        }
    }
    
    public void remove(JLabel label) {
        for (int i = 0; i < hardwareGraph.size(); i++) {

            if (hardwareGraph.get(i).getLabel().equals(label)) {
                hardwareGraph.remove(i);
            } else {
                for (int j = 0; j < hardwareGraph.get(i).getInputs().size(); j++) {
                    if (hardwareGraph.get(i).getInputs().get(j).equals(label)) {
                        hardwareGraph.get(i).getInputs().remove(j);
                    }
                }
                
                for (int j = 0; j < hardwareGraph.get(i).getOutputs().size(); j++) {
                    if (hardwareGraph.get(i).getOutputs().get(j).equals(label)) {
                        hardwareGraph.get(i).getOutputs().remove(j);
                    }
                }

            }

        }

    }

    /**
     * @return the hardwareIdentifier
     */
    public int getHardwareIdentifier() {
        this.hardwareIdentifier++;
        return hardwareIdentifier;
    }

    

}
