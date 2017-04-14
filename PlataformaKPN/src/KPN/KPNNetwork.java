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
public class KPNNetwork {

    public static List<AddProcess> addProcessList;
    public static List<ConstantGenerationProcess> constantGenerationProcessList;
    public static List<DuplicationProcess> duplicationProcessList;
    public static List<ProductProcess> productProcessList;
    public static List<SinkProcess> sinkProcessList;
    private static HardwareGraph hardwareAbstraction;

    public KPNNetwork(HardwareGraph hardwareAbstraction) {
        addProcessList = new ArrayList<>();
        constantGenerationProcessList = new ArrayList<>();
        duplicationProcessList = new ArrayList<>();
        productProcessList = new ArrayList<>();
        sinkProcessList = new ArrayList<>();
        KPNNetwork.hardwareAbstraction = hardwareAbstraction;
        this.createThreads();
        this.joinThreads();
        this.intializeDelaysThread();

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
                    new DuplicationProcess().joinDuplicationProcess(name, hardwareAbstraction.get(i));
                    break;
                case 1:
                    new AddProcess().joinAddProcess(name, hardwareAbstraction.get(i));
                    break;
                case 2:
                    new ProductProcess().joinProductProcess(name, hardwareAbstraction.get(i));
                    break;
                case 3:
                    //revisar si el input es un queue
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

    private void intializeDelaysThread() {
        for (int i = 0; i < hardwareAbstraction.size(); i++) {

            int hardwareType = hardwareAbstraction.get(i).getHardwareType();
            String name = hardwareAbstraction.get(i).getLabel().getName();

            if (hardwareType == 3) {

                int delayIteration = hardwareAbstraction.get(i).getDelayIterations();

                ConstantGenerationProcess thread = (ConstantGenerationProcess) searchThread(name);

                Queue<Float> queueAux = new LinkedList<>();;

                //Adding 0's delay
                for (int j = 0; j < delayIteration; j++) {
                    queueAux.add(Float.valueOf(0));
                }

                queueAux.addAll(thread.getQueueIn());

                thread.getQueueIn().clear();

                thread.getQueueIn().addAll(queueAux);

                thread.setDelayIterations(delayIteration);

            }

        }

    }

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

    /*
     * 
     */
    /**
     *
     */
    /**
     *
     *
     */
    public void startKPNNetwork() {
        for (int i = 0; i < this.duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).start();
        }
        for (int i = 0; i < this.addProcessList.size(); i++) {
            addProcessList.get(i).start();
        }
        for (int i = 0; i < this.productProcessList.size(); i++) {
            productProcessList.get(i).start();
        }
        for (int i = 0; i < this.sinkProcessList.size(); i++) {
            sinkProcessList.get(i).start();
        }
        for (int i = 0; i < this.constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).start();
        }
    }

    public void pauseKPNNetwork() {
        for (int i = 0; i < this.constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).setPauseThread(true);
        }
        for (int i = 0; i < this.duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).setPauseThread(true);
        }
        for (int i = 0; i < this.addProcessList.size(); i++) {
            addProcessList.get(i).setPauseThread(true);
        }
        for (int i = 0; i < this.productProcessList.size(); i++) {
            productProcessList.get(i).setPauseThread(true);
        }

        for (int i = 0; i < this.sinkProcessList.size(); i++) {
            sinkProcessList.get(i).setPauseThread(true);
        }
    }

    public void resumeKPNNetwork() {
        for (int i = 0; i < this.duplicationProcessList.size(); i++) {
            duplicationProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < this.addProcessList.size(); i++) {
            addProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < this.productProcessList.size(); i++) {
            productProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < this.sinkProcessList.size(); i++) {
            sinkProcessList.get(i).setPauseThread(false);
        }
        for (int i = 0; i < this.constantGenerationProcessList.size(); i++) {
            constantGenerationProcessList.get(i).setPauseThread(false);
        }
    }

    public void printKPNNetwork() {
        for (int i = 0; i < this.duplicationProcessList.size(); i++) {
            System.out.println(duplicationProcessList.get(i).getName());
            System.out.println("    InputAssigned:" + duplicationProcessList.get(i).getQueueInputAssigned()
                    + ", Elements: " + duplicationProcessList.get(i).getQueueIn());
            System.out.println("    Output1Assigned:" + duplicationProcessList.get(i).getQueueOutput1Assigned()
                    + ", Elements: " + duplicationProcessList.get(i).getQueueOut1());
            System.out.println("    Output2Assigned:" + duplicationProcessList.get(i).getQueueOutput2Assigned()
                    + ", Elements: " + duplicationProcessList.get(i).getQueueOut2());

        }
        for (int i = 0; i < this.addProcessList.size(); i++) {
            System.out.println(addProcessList.get(i).getName());
            System.out.println("    Input1Assigned:" + addProcessList.get(i).getQueue1InputAssigned()
                    + ", Elements: " + addProcessList.get(i).getQueueIn1());
            System.out.println("    Input2Assigned:" + addProcessList.get(i).getQueue2InputAssigned()
                    + ", Elements: " + addProcessList.get(i).getQueueIn2());
            System.out.println("    OutputAssigned:" + addProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + addProcessList.get(i).getQueueOut());

        }
        for (int i = 0; i < this.productProcessList.size(); i++) {
            System.out.println(productProcessList.get(i).getName());
            System.out.println("    Input1Assigned:" + productProcessList.get(i).getQueue1InputAssigned()
                    + ", Elements: " + productProcessList.get(i).getQueueIn1());
            System.out.println("    Input2Assigned:" + productProcessList.get(i).getQueue2InputAssigned()
                    + ", Elements: " + productProcessList.get(i).getQueueIn2());
            System.out.println("    OutputAssigned:" + productProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + productProcessList.get(i).getQueueOut());

        }
        for (int i = 0; i < this.constantGenerationProcessList.size(); i++) {
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
        for (int i = 0; i < this.sinkProcessList.size(); i++) {
            System.out.println(sinkProcessList.get(i).getName());
            System.out.println("    InputAssigned:" + sinkProcessList.get(i).getQueueInputAssigned()
                    + ", Elements: " + sinkProcessList.get(i).getQueueIn());
            System.out.println("    OutputAssigned:" + sinkProcessList.get(i).getQueueOutputAssigned()
                    + ", Elements: " + sinkProcessList.get(i).getQueueOut());

        }
    }

    public String getKPNNetworkOutput() {
        String result = "";
        for (int i = 0; i < this.duplicationProcessList.size(); i++) {
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
        for (int i = 0; i < this.addProcessList.size(); i++) {
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
        for (int i = 0; i < this.productProcessList.size(); i++) {
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
        for (int i = 0; i < this.constantGenerationProcessList.size(); i++) {
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
        for (int i = 0; i < this.sinkProcessList.size(); i++) {
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


    public void exportKPNToXML(String path)
    {
        XML xml = new XML();
        xml.exportKPNToXML(path);
    }
}
