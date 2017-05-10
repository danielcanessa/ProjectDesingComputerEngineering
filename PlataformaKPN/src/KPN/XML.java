/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPN;

import static KPN.KPNNetwork.addProcessList;
import static KPN.KPNNetwork.constantGenerationProcessList;
import static KPN.KPNNetwork.duplicationProcessList;
import static KPN.KPNNetwork.productProcessList;
import static KPN.KPNNetwork.sinkProcessList;


import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 * 
 */
public class XML {
    
    
    private void addAdderToXML(Document doc, Element rootElement) {
        for (int i = 0; i < addProcessList.size(); i++) {
            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);

            // set attribute to staff element
            module.setAttribute("id", getID(addProcessList.get(i).getName()));
            module.setAttribute("type", "adder");
            module.setAttribute("entries", "2");
            module.setAttribute("outputs", "1");
            module.setAttribute("elements", "");

            //tags
            Element tag = null;

            String element = addProcessList.get(i).getQueue1InputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(addProcessList.get(i).getName(), element)));

                } else {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));

                }
                module.appendChild(tag);

            }
            element = addProcessList.get(i).getQueue2InputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_2");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(addProcessList.get(i).getName(), element)));

                } else {
                    tag = doc.createElement("entry_2");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));

                }
                module.appendChild(tag);
            }

        }
    }

    private void addProductToXML(Document doc, Element rootElement) {
        for (int i = 0; i < productProcessList.size(); i++) {
            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);

            // set attribute to staff element
            module.setAttribute("id", getID(productProcessList.get(i).getName()));
            module.setAttribute("type", "multiplier");
            module.setAttribute("entries", "2");
            module.setAttribute("outputs", "1");
            module.setAttribute("elements", "");

            //tags
            Element tag = null;

            String element = productProcessList.get(i).getQueue1InputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(productProcessList.get(i).getName(), element)));

                } else {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));

                }
                module.appendChild(tag);
            }

            element = productProcessList.get(i).getQueue2InputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_2");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(productProcessList.get(i).getName(), element)));
                } else {
                    tag = doc.createElement("entry_2");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));
                }
                module.appendChild(tag);
            }

        }
    }

    private void addDuplicationToXML(Document doc, Element rootElement) {
        for (int i = 0; i < KPNNetwork.duplicationProcessList.size(); i++) {
            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);

            // set attribute to staff element
            module.setAttribute("id", getID(duplicationProcessList.get(i).getName()));
            module.setAttribute("type", "split");
            module.setAttribute("entries", "1");
            module.setAttribute("outputs", "2");
            module.setAttribute("elements", "");

            //tags
            Element tag = null;

            String element = duplicationProcessList.get(i).getQueueInputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(duplicationProcessList.get(i).getName(), element)));

                } else {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));

                }
                module.appendChild(tag);
            }
        }
    }

    private void addSinkToXML(Document doc, Element rootElement) {
        for (int i = 0; i < KPNNetwork.sinkProcessList.size(); i++) {
            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);

            // set attribute to staff element
            module.setAttribute("id", getID(sinkProcessList.get(i).getName()));
            module.setAttribute("type", "fifo");
            module.setAttribute("entries", "1");
            module.setAttribute("outputs", "1");
            module.setAttribute("KPNOutput", "1");
            module.setAttribute("elements", "");

            //tags
            Element tag = null;

            String element = sinkProcessList.get(i).getQueueInputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(sinkProcessList.get(i).getName(), element)));

                } else {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));

                }
                module.appendChild(tag);
            }
        }
    }

    private void addConstantGenerationToXML(Document doc, Element rootElement) {
        for (int i = 0; i < KPNNetwork.constantGenerationProcessList.size(); i++) {
            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);

            // set attribute to staff element
            module.setAttribute("id", getID(constantGenerationProcessList.get(i).getName()));
            module.setAttribute("type", "fifo");
            module.setAttribute("entries", "1");
            module.setAttribute("outputs", "1");
            module.setAttribute("constantGeneration", Boolean.toString(constantGenerationProcessList.get(i).isConstantGeneration()));
            module.setAttribute("delay", Integer.toString(constantGenerationProcessList.get(i).getDelayIterations()));

            Queue<Float> a = new LinkedList<>();
            a.addAll(constantGenerationProcessList.get(i).getQueueIn());
            int delay = constantGenerationProcessList.get(i).getDelayIterations();

            for (int j = 0; j < delay; j++) {
                a.remove();
            }

            module.setAttribute("elements", a.toString());

            //tags
            Element tag = null;

            String element = constantGenerationProcessList.get(i).getQueueInputAssigned();
            if (!element.equals("")) {
                if (element.contains("duplication")) {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element)
                            + "_" + this.getDuplicationProcessOutput(constantGenerationProcessList.get(i).getName(), element)));

                } else {
                    tag = doc.createElement("entry_1");
                    tag.appendChild(doc.createTextNode(this.getHardwareNameXML(element) + "_" + this.getID(element) + "_" + "_1"));

                }
                module.appendChild(tag);
            }
        }
    }

    public void exportKPNToXML(String path) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("modules");
            doc.appendChild(rootElement);

            addAdderToXML(doc, rootElement);
            addProductToXML(doc, rootElement);
            addConstantGenerationToXML(doc, rootElement);
            addSinkToXML(doc, rootElement);
            addDuplicationToXML(doc, rootElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(path));
            // Output to console for testing
            //StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
        }

    }

    private String getID(String word) {
        return word.substring(word.indexOf("ID: ") + 4, word.indexOf(", Name"));
    }

    private String getHardwareNameXML(String name) {
        if (name.contains("duplication")) {
            return "split";
        } else if (name.contains("adder")) {
            return "adder";
        } else if (name.contains("product")) {
            return "multiplier";
        } else if (name.contains("constantGeneration")) {
            return "fifo";
        } else if (name.contains("sink")) {
            return "fifo";
        } else {
            return "";
        }
    }

    //to fix: caso en que amas esten conectadas al mismo bloque
    public String getDuplicationProcessOutput(String duplicationProcess, String hardware) {
        String result = "";
        for (int i = 0; i < KPNNetwork.duplicationProcessList.size(); i++) {

            if (duplicationProcessList.get(i).getName().equals(duplicationProcess)) {
                String out1 = duplicationProcessList.get(i).getQueueOutput1Assigned();
                String out2 = duplicationProcessList.get(i).getQueueOutput1Assigned();
                if (out1.equals(out2)) {
                    result = Integer.toString(duplicationProcessList.get(i).getXMLOutput());
                } else if (!out1.equals(hardware)) {
                    result = "1";

                } else if (!out2.equals(hardware)) {
                    result = "2";
                }

            }
        }
        return result;
    }
    
}
