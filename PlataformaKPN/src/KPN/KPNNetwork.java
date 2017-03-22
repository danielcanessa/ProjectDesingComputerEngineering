/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import plataformakpn.HardwareGraph;
import plataformakpn.HardwareModel;

/**
 *
 * @author Daniel
 */
public class KPNNetwork {

    private List<AddProcess> addProcessList;
    private List<ConstantGenerationProcess> constantGenerationProcessList;
    private List<DuplicationProcess> duplicationProcessList;
    private List<ProductProcess> productProcessList;
    private List<SinkProcess> sinkProcessList;
    private HardwareGraph hardwareAbstraction;

    public KPNNetwork(HardwareGraph hardwareAbstraction) {
        addProcessList = new ArrayList<>();
        constantGenerationProcessList = new ArrayList<>();
        duplicationProcessList = new ArrayList<>();
        productProcessList = new ArrayList<>();
        sinkProcessList = new ArrayList<>();
        this.hardwareAbstraction = hardwareAbstraction;
        this.createThreads();
        this.joinThreads();
    }

    private void createThreads() {
        for (int i = 0; i < hardwareAbstraction.size(); i++) {
            int hardwareType = hardwareAbstraction.get(i).getHardwareType();
            switch (hardwareType) {
                case 0:
                    duplicationProcessList.add(createDuplicationProcess(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                case 1:
                    addProcessList.add(createAddThread(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                case 2:
                    productProcessList.add(createProductProcess(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                case 3:
                    constantGenerationProcessList.add(createConstantGenerationProcess(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                case 4:
                    sinkProcessList.add(createSinkProcess(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                default:
                    break;
            }
        }
    }

    private void joinThreads() {
        for (int i = 0; i < hardwareAbstraction.size(); i++) {

            int hardwareType = hardwareAbstraction.get(i).getHardwareType();
            String name = hardwareAbstraction.get(i).getLabel().getName();

            switch (hardwareType) {
                case 0:
                    duplicationProcessList.add(createDuplicationProcess(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                case 1:
                    joinAddProcess(name, hardwareAbstraction.get(i));
                    break;
                case 2:
                    joinProductProcess(name, hardwareAbstraction.get(i));
                    break;
                case 3:
                    //revisar si el input es un queue
                    joinConstantGenerationProcess(name, hardwareAbstraction.get(i));
                    break;
                case 4:
                    joinSinkProcess(name, hardwareAbstraction.get(i));
                    break;
                default:
                    break;
            }

        }

    }

    public Object searchThread(String name) {

        int hardwareType = getHardwareTypeByName(name);
        switch (hardwareType) {
            case 0:
                for (int i = 0; i < duplicationProcessList.size(); i++) {
                    if (duplicationProcessList.get(i).getName().equals(name)) {
                        return duplicationProcessList.get(i);

                    }
                }
                break;
            case 1:
                for (int i = 0; i < addProcessList.size(); i++) {
                    if (addProcessList.get(i).getName().equals(name)) {
                        return addProcessList.get(i);

                    }
                }
                break;
            case 2:
                for (int i = 0; i < productProcessList.size(); i++) {
                    if (productProcessList.get(i).getName().equals(name)) {
                        return productProcessList.get(i);

                    }
                }
                break;
            case 3:
                for (int i = 0; i < constantGenerationProcessList.size(); i++) {
                    if (constantGenerationProcessList.get(i).getName().equals(name)) {
                        return constantGenerationProcessList.get(i);
                    }
                }
                break;
            case 4:
                for (int i = 0; i < sinkProcessList.size(); i++) {
                    if (sinkProcessList.get(i).getName().equals(name)) {
                        return sinkProcessList.get(i);
                    }
                }
                break;
            default:
                System.out.println("Thread didnt find: " + name);
                return null;
        }
        System.out.println("Thread didnt find: " + name);
        return null;
    }

    public AddProcess createAddThread(String name) {
        AddProcess add = new AddProcess();
        add.setThreadName(name);
        return add;
    }

    public ConstantGenerationProcess createConstantGenerationProcess(String name) {
        ConstantGenerationProcess constantGeneration = new ConstantGenerationProcess();
        constantGeneration.setThreadName(name);
        return constantGeneration;
    }

    public DuplicationProcess createDuplicationProcess(String name) {
        DuplicationProcess duplication = new DuplicationProcess();
        duplication.setThreadName(name);
        return duplication;
    }

    public ProductProcess createProductProcess(String name) {
        ProductProcess product = new ProductProcess();
        product.setThreadName(name);
        return product;
    }

    public SinkProcess createSinkProcess(String name) {
        SinkProcess sink = new SinkProcess();
        sink.setThreadName(name);
        return sink;
    }

    private int getHardwareTypeByName(String name) {
        if (name.contains("duplication")) {
            return 0;
        } else if (name.contains("adder")) {
            return 1;
        } else if (name.contains("product")) {
            return 2;
        } else if (name.contains("constantGeneration")) {
            return 3;
        } else if (name.contains("sink")) {
            return 4;
        } else if (name.contains("queue")) {
            return 5;
        } else if (name.contains("view")) {
            return 6;

        } else {
            return -1;
        }

    }

    private void joinAddProcess(String name, HardwareModel model) {

        AddProcess addProcess = (AddProcess) searchThread(name); //current add process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = this.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_Add_Duplication(addProcess, duplicationInputProcess);
                    break;
                case 1: //add process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Add_Add(addProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Add_Product(addProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Add_ConstantGeneration(addProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Add_Sink(addProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = this.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Add_Duplication(addProcess, duplicationOutputProcess);
                    break;
                case 1: //add process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Add_Add(addProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Add_Product(addProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Add_ConstantGeneration(addProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Add_Sink(addProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_Add_Duplication(AddProcess addProcess, DuplicationProcess duplicationInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                addProcess.setQueueIn1(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(true);
                addProcess.setQueue1InputAssigned(true);
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                addProcess.setQueueIn1(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(true);
                addProcess.setQueue1InputAssigned(true);
            }
        } else if (!addProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                addProcess.setQueueIn2(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(true);
                addProcess.setQueue2InputAssigned(true);
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                addProcess.setQueueIn2(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(true);
                addProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinInput_Add_Add(AddProcess addProcess, AddProcess addInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueIn1(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue1InputAssigned(true);
            }
        } else if (!addInputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueIn2(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinInput_Add_Product(AddProcess addProcess, ProductProcess productInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn1(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue1InputAssigned(true);
            }
        } else if (!productInputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn2(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinInput_Add_ConstantGeneration(AddProcess addProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn1(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue1InputAssigned(true);
            }
        } else if (!addProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn2(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue2InputAssigned(true);
            }
        }

    }

    private void JoinInput_Add_Sink(AddProcess addProcess, SinkProcess SinkInputProcess) {
        if (!addProcess.isQueue1InputAssigned()) { //if the input 1 of the add process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn1(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue1InputAssigned(true);
            }
        } else if (!addProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueIn2(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(true);
                addProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinOutput_Add_Duplication(AddProcess addProcess, DuplicationProcess duplicationOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the output of the add process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                addProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Add_Add(AddProcess addProcess, AddProcess addOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
                addProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Add_Product(AddProcess addProcess, ProductProcess productOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the add process still without assignation
                addProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the add process is already assigned but input 2 still without assignation
                addProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Add_ConstantGeneration(AddProcess addProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Add_Sink(AddProcess addProcess, SinkProcess sinkOutputProcess) {
        if (!addProcess.isQueueOutputAssigned()) { //if the input 1 of the add process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                addProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(true);
                addProcess.setQueueOutputAssigned(true);
            }
        }

    }

    /*
     * 
     */
    private void joinProductProcess(String name, HardwareModel model) {

        ProductProcess productProcess = (ProductProcess) searchThread(name); //current product process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = this.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Duplication(productProcess, duplicationInputProcess);
                    break;
                case 1: //product process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Add(productProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Product(productProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_ConstantGeneration(productProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Product_Sink(productProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = this.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Duplication(productProcess, duplicationOutputProcess);
                    break;
                case 1: //product process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Add(productProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Product(productProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_ConstantGeneration(productProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Product_Sink(productProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_Product_Duplication(ProductProcess productProcess, DuplicationProcess duplicationInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                productProcess.setQueueIn1(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(true);
                productProcess.setQueue1InputAssigned(true);
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                productProcess.setQueueIn1(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(true);
                productProcess.setQueue1InputAssigned(true);
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                productProcess.setQueueIn2(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(true);
                productProcess.setQueue2InputAssigned(true);
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                productProcess.setQueueIn2(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(true);
                productProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinInput_Product_Add(ProductProcess productProcess, AddProcess addInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue1InputAssigned(true);
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinInput_Product_Product(ProductProcess productProcess, ProductProcess productInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue1InputAssigned(true);
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinInput_Product_ConstantGeneration(ProductProcess productProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue1InputAssigned(true);
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue2InputAssigned(true);
            }
        }

    }

    private void JoinInput_Product_Sink(ProductProcess productProcess, SinkProcess SinkInputProcess) {
        if (!productProcess.isQueue1InputAssigned()) { //if the input 1 of the product process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn1(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue1InputAssigned(true);
            }
        } else if (!productProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueIn2(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(true);
                productProcess.setQueue2InputAssigned(true);
            }
        }
    }

    private void JoinOutput_Product_Duplication(ProductProcess productProcess, DuplicationProcess duplicationOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the output of the product process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                productProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            }
        }
    }

    private void JoinOutput_Product_Add(ProductProcess productProcess, AddProcess addOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
                productProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Product_Product(ProductProcess productProcess, ProductProcess productOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the product process is already assigned but input 2 still without assignation
                productProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Product_ConstantGeneration(ProductProcess productProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Product_Sink(ProductProcess productProcess, SinkProcess sinkOutputProcess) {
        if (!productProcess.isQueueOutputAssigned()) { //if the input 1 of the product process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                productProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(true);
                productProcess.setQueueOutputAssigned(true);
            }
        }
    }

    /**
     *
     */
    private void joinSinkProcess(String name, HardwareModel model) {

        SinkProcess sinkProcess = (SinkProcess) searchThread(name); //current sink process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = this.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Duplication(sinkProcess, duplicationInputProcess);
                    break;
                case 1: //sink process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Add(sinkProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Product(sinkProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_ConstantGeneration(sinkProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess SinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_Sink_Sink(sinkProcess, SinkInputProcess);
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = this.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Duplication(sinkProcess, duplicationOutputProcess);
                    break;
                case 1: //sink process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Add(sinkProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Product(sinkProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_ConstantGeneration(sinkProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_Sink_Sink(sinkProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_Sink_Duplication(SinkProcess sinkProcess, DuplicationProcess duplicationInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                sinkProcess.setQueueIn(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(true);
                sinkProcess.setQueueInputAssigned(true);
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                sinkProcess.setQueueIn(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(true);
                sinkProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_Sink_Add(SinkProcess sinkProcess, AddProcess addInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) {//if the input 1 of the sink process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the sink process still without assignation
                sinkProcess.setQueueIn(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(true);
                sinkProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_Sink_Product(SinkProcess sinkProcess, ProductProcess productInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueIn(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(true);
                sinkProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_Sink_ConstantGeneration(SinkProcess sinkProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueIn(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(true);
                sinkProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_Sink_Sink(SinkProcess sinkProcess, SinkProcess SinkInputProcess) {
        if (!sinkProcess.isQueueInputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!SinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueIn(SinkInputProcess.getQueueOut());
                SinkInputProcess.setQueueOutputAssigned(true);
                sinkProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinOutput_Sink_Duplication(SinkProcess sinkProcess, DuplicationProcess duplicationOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the output of the sink process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                sinkProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            }
        }
    }

    private void JoinOutput_Sink_Add(SinkProcess sinkProcess, AddProcess addOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the sink process still without assignation
                sinkProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the sink process is already assigned but input 2 still without assignation
                sinkProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Sink_Product(SinkProcess sinkProcess, ProductProcess productOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the sink process still without assignation
                sinkProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the sink process is already assigned but input 2 still without assignation
                sinkProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Sink_ConstantGeneration(SinkProcess sinkProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_Sink_Sink(SinkProcess sinkProcess, SinkProcess sinkOutputProcess) {
        if (!sinkProcess.isQueueOutputAssigned()) { //if the input 1 of the sink process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                sinkProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(true);
                sinkProcess.setQueueOutputAssigned(true);
            }
        }

    }

    /**
     *
     *
     */
    private void joinConstantGenerationProcess(String name, HardwareModel model) {

        ConstantGenerationProcess constantGenerationProcess = (ConstantGenerationProcess) searchThread(name); //current constantGeneration process

        for (int j = 0; j < model.getInputs().size(); j++) { //join the process with the inputs

            String inputName = model.getInputs().get(j).getName();
            int hardwareTypeInput = this.getHardwareTypeByName(inputName);

            switch (hardwareTypeInput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationInputProcess = (DuplicationProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Duplication(constantGenerationProcess, duplicationInputProcess);
                    break;
                case 1: //constantGeneration process case
                    AddProcess addInputProcess = (AddProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Add(constantGenerationProcess, addInputProcess);
                    break;
                case 2:
                    ProductProcess productInputProcess = (ProductProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Product(constantGenerationProcess, productInputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationInputProcess = (ConstantGenerationProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_ConstantGeneration(constantGenerationProcess, constantGenerationInputProcess);
                    break;
                case 4:
                    SinkProcess sinkInputProcess = (SinkProcess) searchThread(inputName); //getting the process
                    JoinInput_ConstantGeneration_Sink(constantGenerationProcess, sinkInputProcess);
                    break;
                case 5:
                    JoinInput_ConstantGeneration_Queue(constantGenerationProcess, hardwareAbstraction.search(model.getInputs().get(j)));
                    break;
            }

        }
        for (int j = 0; j < model.getOutputs().size(); j++) {

            String outputName = model.getOutputs().get(j).getName();
            int hardwareTypeOutput = this.getHardwareTypeByName(outputName);

            switch (hardwareTypeOutput) {
                case 0: //duplication process case
                    DuplicationProcess duplicationOutputProcess = (DuplicationProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Duplication(constantGenerationProcess, duplicationOutputProcess);
                    break;
                case 1: //constantGeneration process case
                    AddProcess addOutputProcess = (AddProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Add(constantGenerationProcess, addOutputProcess);
                    break;
                case 2:
                    ProductProcess productOutputProcess = (ProductProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Product(constantGenerationProcess, productOutputProcess);
                    break;
                case 3:
                    ConstantGenerationProcess constantGenerationOutputProcess = (ConstantGenerationProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_ConstantGeneration(constantGenerationProcess, constantGenerationOutputProcess);
                    break;
                case 4:
                    SinkProcess sinkOutputProcess = (SinkProcess) searchThread(outputName); //getting the process
                    JoinOutput_ConstantGeneration_Sink(constantGenerationProcess, sinkOutputProcess);
                    break;
            }
        }
    }

    private void JoinInput_ConstantGeneration_Duplication(ConstantGenerationProcess constantGenerationProcess, DuplicationProcess duplicationInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!duplicationInputProcess.isQueueOutput1Assigned()) { //if ouput1 of the duplication process still without assignation
                constantGenerationProcess.setQueueIn(duplicationInputProcess.getQueueOut1());
                duplicationInputProcess.setQueueOutput1Assigned(true);
                constantGenerationProcess.setQueueInputAssigned(true);
            } else if (!duplicationInputProcess.isQueueOutput2Assigned()) { //if ouput2 of the duplication process still without assignation
                constantGenerationProcess.setQueueIn(duplicationInputProcess.getQueueOut2());
                duplicationInputProcess.setQueueOutput2Assigned(true);
                constantGenerationProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_ConstantGeneration_Add(ConstantGenerationProcess constantGenerationProcess, AddProcess addInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) {//if the input 1 of the constantGeneration process still without assignation
            if (!addInputProcess.isQueueOutputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueIn(addInputProcess.getQueueOut());
                addInputProcess.setQueueOutputAssigned(true);
                constantGenerationProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_ConstantGeneration_Product(ConstantGenerationProcess constantGenerationProcess, ProductProcess productInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!productInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(productInputProcess.getQueueOut());
                productInputProcess.setQueueOutputAssigned(true);
                constantGenerationProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_ConstantGeneration_ConstantGeneration(ConstantGenerationProcess constantGenerationProcess, ConstantGenerationProcess constantGenerationInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!constantGenerationInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(constantGenerationInputProcess.getQueueOut());
                constantGenerationInputProcess.setQueueOutputAssigned(true);
                constantGenerationProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_ConstantGeneration_Sink(ConstantGenerationProcess constantGenerationProcess, SinkProcess sinkInputProcess) {
        if (!constantGenerationProcess.isQueueInputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!sinkInputProcess.isQueueOutputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueIn(sinkInputProcess.getQueueOut());
                sinkInputProcess.setQueueOutputAssigned(true);
                constantGenerationProcess.setQueueInputAssigned(true);
            }
        }
    }

    private void JoinInput_ConstantGeneration_Queue(ConstantGenerationProcess constantGenerationProcess, HardwareModel model) {
        constantGenerationProcess.setQueueIn((Queue<Float>) model.getInputQueue());
        constantGenerationProcess.setConstantGeneration(model.isConstantGeneration());
    }

    private void JoinOutput_ConstantGeneration_Duplication(ConstantGenerationProcess constantGenerationProcess, DuplicationProcess duplicationOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the output of the constantGeneration process still without assignation
            if (!duplicationOutputProcess.isQueueInputAssigned()) { //if ouput1 of the duplication process still without assignation
                constantGenerationProcess.setQueueOut(duplicationOutputProcess.getQueueIn());
                duplicationOutputProcess.setQueueInputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            }
        }
    }

    private void JoinOutput_ConstantGeneration_Add(ConstantGenerationProcess constantGenerationProcess, AddProcess addOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!addOutputProcess.isQueue1InputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueOut(addOutputProcess.getQueueIn1());
                addOutputProcess.setQueue1InputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            } else if (!addOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the constantGeneration process is already assigned but input 2 still without assignation
                constantGenerationProcess.setQueueOut(addOutputProcess.getQueueIn2());
                addOutputProcess.setQueue2InputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_ConstantGeneration_Product(ConstantGenerationProcess constantGenerationProcess, ProductProcess productOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!productOutputProcess.isQueue1InputAssigned()) { //if ouput of the constantGeneration process still without assignation
                constantGenerationProcess.setQueueOut(productOutputProcess.getQueueIn1());
                productOutputProcess.setQueue1InputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            } else if (!productOutputProcess.isQueue2InputAssigned()) { //if the input 1 of the constantGeneration process is already assigned but input 2 still without assignation
                constantGenerationProcess.setQueueOut(productOutputProcess.getQueueIn2());
                productOutputProcess.setQueue2InputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_ConstantGeneration_ConstantGeneration(ConstantGenerationProcess constantGenerationProcess, ConstantGenerationProcess constantGenerationOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!constantGenerationOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueOut(constantGenerationOutputProcess.getQueueIn());
                constantGenerationOutputProcess.setQueueInputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            }
        }

    }

    private void JoinOutput_ConstantGeneration_Sink(ConstantGenerationProcess constantGenerationProcess, SinkProcess sinkOutputProcess) {
        if (!constantGenerationProcess.isQueueOutputAssigned()) { //if the input 1 of the constantGeneration process still without assignation
            if (!sinkOutputProcess.isQueueInputAssigned()) { //if ouput of the product process still without assignation
                constantGenerationProcess.setQueueOut(sinkOutputProcess.getQueueIn());
                sinkOutputProcess.setQueueInputAssigned(true);
                constantGenerationProcess.setQueueOutputAssigned(true);
            }
        }
    }

}
