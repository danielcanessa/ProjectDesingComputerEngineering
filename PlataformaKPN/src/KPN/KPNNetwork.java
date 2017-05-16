/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import plataformakpn.HardwareGraph;
import plataformakpn.HardwareModel;

/**
 * This class contains all the definitions and methods of the KPN
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 *
 */
public class KPNNetwork {

    /**
     * This variable contains all the threads that represents an adder
     */
    public static List<AddProcess> addProcessList;
    /**
     * This variable contains all the threads that represents constant generator
     */
    public static List<ConstantGenerationProcess> constantGenerationProcessList;
    /**
     * This variable contains all the threads that represents a duplication
     */
    public static List<DuplicationProcess> duplicationProcessList;
    /**
     * This variable contains all the threads that represents a duplicator
     */
    public static List<ProductProcess> productProcessList;
    /**
     * This variable contains all the threads that represents a sink
     */
    public static List<SinkProcess> sinkProcessList;
    /**
     * This variable contains the graph designed by the user
     */
    private static HardwareGraph hardwareAbstraction;

    /**
     * Class constructor.
     *
     * @param hardwareAbstraction
     */
    public KPNNetwork(HardwareGraph hardwareAbstraction) {
        //Threads list's initialization
        addProcessList = new ArrayList<>();
        constantGenerationProcessList = new ArrayList<>();
        duplicationProcessList = new ArrayList<>();
        productProcessList = new ArrayList<>();
        sinkProcessList = new ArrayList<>();
        //KPN initialization
        KPNNetwork.hardwareAbstraction = hardwareAbstraction;
        this.createThreads();
        this.joinThreads();
        this.intializeDelaysThread();
        this.updateToolTips();
    }

    /**
     * This method updates the graphic tooltip of the labels with the updated
     * queues of each process.
     */
    private void updateToolTips() {
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).updateToolTip();
        }
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).updateToolTip();
        }
        for (int i = 0; i < addProcessList.size(); i++) {
            addProcessList.get(i).updateToolTip();
        }
        for (int i = 0; i < productProcessList.size(); i++) {
            productProcessList.get(i).updateToolTip();
        }

        for (int i = 0; i < sinkProcessList.size(); i++) {
            sinkProcessList.get(i).updateToolTip();
        }
    }

    /**
     * This method is used to created all threads, based on the graph designed
     * by the user.
     */
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
                    constantGenerationProcessList.add(createConstantGenerationProcess(hardwareAbstraction.get(i)));
                    break;
                case 4:
                    sinkProcessList.add(createSinkProcess(hardwareAbstraction.get(i).getLabel().getName()));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * This method makes the relations between thread's queues.
     */
    private void joinThreads() {
        for (int i = 0; i < hardwareAbstraction.size(); i++) {
            int hardwareType = hardwareAbstraction.get(i).getHardwareType();
            String name = hardwareAbstraction.get(i).getLabel().getName();
            switch (hardwareType) {
                case 0:
                    new DuplicationProcess().joinDuplicationProcess(name, hardwareAbstraction.get(i));
                    break;
                case 1:
                    new AddProcess().joinAddProcess(name, hardwareAbstraction.get(i));
                    break;
                case 2:
                    new ProductProcess().joinProductProcess(name, hardwareAbstraction.get(i));
                    break;
                case 3:
                    new ConstantGenerationProcess().joinConstantGenerationProcess(name, hardwareAbstraction.get(i), hardwareAbstraction);
                    break;
                case 4:
                    new SinkProcess().joinSinkProcess(name, hardwareAbstraction.get(i));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * This method is used just in case the user has stablished any delay in a
     * constant generator.
     */
    private void intializeDelaysThread() {
        for (int i = 0; i < hardwareAbstraction.size(); i++) {
            int hardwareType = hardwareAbstraction.get(i).getHardwareType();
            String name = hardwareAbstraction.get(i).getLabel().getName();
            //Vefiying that the hardware is a constant generator
            if (hardwareType == 3) {
                int delayIteration = hardwareAbstraction.get(i).getDelayIterations();
                ConstantGenerationProcess thread = (ConstantGenerationProcess) searchThread(name);
                Queue<Float> queueAux = new LinkedList<>();
                //Adding 0's delay
                for (int j = 0; j < delayIteration; j++) {
                    queueAux.add(Float.valueOf(0));
                }
                //Adding old elements
                queueAux.addAll(thread.getQueueIn());
                //Clearing the queue
                thread.getQueueIn().clear();
                //Merging the new queue
                thread.getQueueIn().addAll(queueAux);
                //Setting the delay iteration variable
                thread.setDelayIterations(delayIteration);
            }
        }
    }

    /**
     * This method returns the thread searching by thread name.
     * @param name
     * @return 
     */
    public static Object searchThread(String name) {

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

    public ConstantGenerationProcess createConstantGenerationProcess(HardwareModel model) {
        ConstantGenerationProcess constantGeneration = new ConstantGenerationProcess();
        constantGeneration.setThreadName(model.getLabel().getName());
        constantGeneration.setDelayIterations(model.getDelayIterations());
        constantGeneration.setConstantGeneration(model.isConstantGeneration());
        constantGeneration.setQueueIn(model.getInputQueue());
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

    public static int getHardwareTypeByName(String name) {
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

    public void startKPNNetwork() {
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).start();
        }
        for (int i = 0; i < addProcessList.size(); i++) {
            addProcessList.get(i).start();
        }
        for (int i = 0; i < productProcessList.size(); i++) {
            productProcessList.get(i).start();
        }
        for (int i = 0; i < sinkProcessList.size(); i++) {
            sinkProcessList.get(i).start();
        }
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).start();
        }
    }

    public void pauseKPNNetwork() {

        for (int i = 0; i < duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).setPauseThread(true);
        }
        for (int i = 0; i < addProcessList.size(); i++) {
            addProcessList.get(i).setPauseThread(true);
        }
        for (int i = 0; i < productProcessList.size(); i++) {
            productProcessList.get(i).setPauseThread(true);
        }

        for (int i = 0; i < sinkProcessList.size(); i++) {
            sinkProcessList.get(i).setPauseThread(true);
        }
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).setPauseThread(true);
        }
    }

    public void resumeKPNNetwork() {
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < addProcessList.size(); i++) {
            addProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < productProcessList.size(); i++) {
            productProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < sinkProcessList.size(); i++) {
            sinkProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).setPauseThread(false);
        }
    }

    public void freeKPNNetwork() {
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).setKillThread(true);
        }
        for (int i = 0; i < addProcessList.size(); i++) {
            addProcessList.get(i).setKillThread(true);
        }
        for (int i = 0; i < productProcessList.size(); i++) {
            productProcessList.get(i).setKillThread(true);
        }
        for (int i = 0; i < sinkProcessList.size(); i++) {
            sinkProcessList.get(i).setKillThread(true);
        }
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).setKillThread(true);
        }
    }

    private void printDuplicationList() {
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            System.out.println(duplicationProcessList.get(i).getName());
            System.out.println("    InputAssigned:" + duplicationProcessList.get(i).getQueueInputAssigned()
                    + ", Elements: " + duplicationProcessList.get(i).getQueueIn());
            System.out.println("    Output1Assigned:" + duplicationProcessList.get(i).getQueueOutput1Assigned()
                    + ", Elements: " + duplicationProcessList.get(i).getQueueOut1());
            System.out.println("    Output2Assigned:" + duplicationProcessList.get(i).getQueueOutput2Assigned()
                    + ", Elements: " + duplicationProcessList.get(i).getQueueOut2());
        }
    }

    private void printAdderList() {
        for (int i = 0; i < addProcessList.size(); i++) {
            System.out.println(addProcessList.get(i).getName());
            System.out.println("    Input1Assigned:" + addProcessList.get(i).getQueue1InputAssigned()
                    + ", Elements: " + addProcessList.get(i).getQueueIn1());
            System.out.println("    Input2Assigned:" + addProcessList.get(i).getQueue2InputAssigned()
                    + ", Elements: " + addProcessList.get(i).getQueueIn2());
            System.out.println("    OutputAssigned:" + addProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + addProcessList.get(i).getQueueOut());

        }
    }

    private void printProductList() {
        for (int i = 0; i < productProcessList.size(); i++) {
            System.out.println(productProcessList.get(i).getName());
            System.out.println("    Input1Assigned:" + productProcessList.get(i).getQueue1InputAssigned()
                    + ", Elements: " + productProcessList.get(i).getQueueIn1());
            System.out.println("    Input2Assigned:" + productProcessList.get(i).getQueue2InputAssigned()
                    + ", Elements: " + productProcessList.get(i).getQueueIn2());
            System.out.println("    OutputAssigned:" + productProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + productProcessList.get(i).getQueueOut());
        }
    }

    private void printConstantGenerationList() {
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            System.out.println(constantGenerationProcessList.get(i).getName());
            System.out.println("    InputAssigned:" + constantGenerationProcessList.get(i).getQueueInputAssigned()
                    + ", Elements: " + constantGenerationProcessList.get(i).getQueueIn()
                    + ", ContantGeneration: " + constantGenerationProcessList.get(i).isConstantGeneration()
                    + ", Delay Elements: " + constantGenerationProcessList.get(i).getDelayIterations());
            System.out.println("    OutputAssigned:" + constantGenerationProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + constantGenerationProcessList.get(i).getQueueOut()
                    + ", ContantGeneration: " + constantGenerationProcessList.get(i).isConstantGeneration()
                    + ", Delay Elements: " + constantGenerationProcessList.get(i).getDelayIterations());

        }
    }

    private void printSinkList() {
        for (int i = 0; i < sinkProcessList.size(); i++) {
            System.out.println(sinkProcessList.get(i).getName());
            System.out.println("    InputAssigned:" + sinkProcessList.get(i).getQueueInputAssigned()
                    + ", Elements: " + sinkProcessList.get(i).getQueueIn());
            System.out.println("    OutputAssigned:" + sinkProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + sinkProcessList.get(i).getQueueOut());

        }
    }

    public void printKPNNetwork() {
        printDuplicationList();
        printAdderList();
        printProductList();
        printConstantGenerationList();
        printSinkList();
    }

    private String getKPNDuplicationOutput() {
        String result = "";
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            result = result + "<b>" + duplicationProcessList.get(i).getName() + "</b>" + "<br>";

            if (!duplicationProcessList.get(i).getQueueInputAssigned().equals("")) {
                result = result + "    Input assigned: " + duplicationProcessList.get(i).getQueueInputAssigned()
                        + ", Queue elements: " + duplicationProcessList.get(i).getQueueIn() + "<br>";
            }
            if (!duplicationProcessList.get(i).getQueueOutput1Assigned().equals("")) {
                result = result + "    Output 1 assigned: " + duplicationProcessList.get(i).getQueueOutput1Assigned()
                        + ", Queue elements: " + duplicationProcessList.get(i).getQueueOut1() + "<br>";
            }

            result = result + "    Output 2 assigned: " + duplicationProcessList.get(i).getQueueOutput2Assigned()
                    + ", Queue elements: " + duplicationProcessList.get(i).getQueueOut2() + "<br>";
        }
        return result;
    }

    private String getKPNAdderOutput() {
        String result = "";
        for (int i = 0; i < addProcessList.size(); i++) {
            result = result + "<b>" + addProcessList.get(i).getName() + "</b>" + "<br>";
            if (!addProcessList.get(i).getQueue1InputAssigned().equals("")) {
                result = result + "    Input 1 assigned: " + addProcessList.get(i).getQueue1InputAssigned()
                        + ", Queue elements: " + addProcessList.get(i).getQueueIn1() + "<br>";
            }
            if (!addProcessList.get(i).getQueue2InputAssigned().equals("")) {
                result = result + "    Input 2 assigned: " + addProcessList.get(i).getQueue2InputAssigned()
                        + ", Queue elements: " + addProcessList.get(i).getQueueIn2() + "<br>";
            }
            result = result + "    Output assigned: " + addProcessList.get(i).getQueueOutputAssigned()
                    + ", Queue elements: " + addProcessList.get(i).getQueueOut() + "<br>";

        }
        return result;
    }

    private String getKPNProductOutput() {
        String result = "";
        for (int i = 0; i < productProcessList.size(); i++) {
            result = result + "<b>" + productProcessList.get(i).getName() + "</b>" + "<br>";
            if (!productProcessList.get(i).getQueue1InputAssigned().equals("")) {
                result = result + "    Input 1 assigned: " + productProcessList.get(i).getQueue1InputAssigned()
                        + ", Queue elements: " + productProcessList.get(i).getQueueIn1() + "<br>";
            }
            if (!productProcessList.get(i).getQueue2InputAssigned().equals("")) {
                result = result + "    Input 2 assigned: " + productProcessList.get(i).getQueue2InputAssigned()
                        + ", Queue elements: " + productProcessList.get(i).getQueueIn2() + "<br>";
            }
            result = result + "    Output assigned: " + productProcessList.get(i).getQueueOutputAssigned()
                    + ", Queue elements: " + productProcessList.get(i).getQueueOut() + "<br>";

        }
        return result;
    }

    private String getKPNConstantGenerationOutput() {
        String result = "";
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            result = result + "<b>" + constantGenerationProcessList.get(i).getName() + "</b>" + "<br>";
            if (!constantGenerationProcessList.get(i).getQueueInputAssigned().equals("") || constantGenerationProcessList.get(i).getQueueIn().size() > 0) {
                result = result + "    Input assigned:" + constantGenerationProcessList.get(i).getQueueInputAssigned()
                        + ", Queue elements: " + constantGenerationProcessList.get(i).getQueueIn()
                        + ", Is constant Generation: " + constantGenerationProcessList.get(i).isConstantGeneration()
                        + ", Delay time: " + constantGenerationProcessList.get(i).getDelayIterations() + "<br>";
            }
            result = result + "    Output assigned:" + constantGenerationProcessList.get(i).getQueueOutputAssigned()
                    + ", Queue elements: " + constantGenerationProcessList.get(i).getQueueOut() + "<br>";

        }
        return result;
    }

    private String getKPNSinkOutput() {
        String result = "";
        for (int i = 0; i < sinkProcessList.size(); i++) {
            result = result + "<b>" + sinkProcessList.get(i).getName() + "</b>" + "<br>";
            if (!sinkProcessList.get(i).getQueueInputAssigned().equals("")) {
                result = result + "    Input assigned:" + sinkProcessList.get(i).getQueueInputAssigned()
                        + ", Queue elements: " + sinkProcessList.get(i).getQueueIn() + "<br>";
            }
            result = result + "    Output assigned:" + sinkProcessList.get(i).getQueueOutputAssigned()
                    + ", Queue elements: " + sinkProcessList.get(i).getQueueOut() + "<br>";

        }
        return result;
    }

    public String getKPNNetworkOutput() {
        String result = "";
        try {
            result = result + getKPNDuplicationOutput();
            result = result + getKPNAdderOutput();
            result = result + getKPNProductOutput();
            result = result + getKPNConstantGenerationOutput();
            result = result + getKPNSinkOutput();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public void exportKPNToXML(String path) {
        XML xml = new XML();
        xml.exportKPNToXML(path);
        
        //Esta es el path que ocupa
        System.out.println(path);
        
       
    }
}
