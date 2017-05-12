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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
 * This class is used in the XML exportation file generation
 *
 * @author Daniel Canessa Valverde
 * @version 1.0
 *
 */
public class XML {

    /**
     * Variable used to know the next fifo id
     */
    private int fifoCount;
    /**
     * Variable that contains all the fifo elements added
     */
    private List<FifoModel> fifoList;

    /**
     * Class constructor.
     */
    public XML() {
        this.fifoList = new ArrayList<>();
        this.fifoCount = 0;
        fillFifoList();
    }

    /**
     * This method is used to find a fifo by the hardware name associated
     *
     * @param name
     * @return
     */
    private FifoModel searchFifo(String name) {
        for (int i = 0; i < fifoList.size(); i++) {
            if (fifoList.get(i).getHardwareName().equals(name)) {
                return fifoList.get(i);
            }
        }
        return null;
    }

    /**
     * This method is used to initialized the fifo list.
     */
    private void fillFifoList() {
        fifoCount = addProcessList.size() + productProcessList.size()
                + duplicationProcessList.size() + sinkProcessList.size()
                + constantGenerationProcessList.size();

        for (int i = 0; i < addProcessList.size(); i++) {
            FifoModel model = new FifoModel();
            model.setHardwareName(addProcessList.get(i).getName());
            model.setIdFifo1(this.getFifoCount());
            fifoList.add(model);
        }
        for (int i = 0; i < productProcessList.size(); i++) {
            FifoModel model = new FifoModel();
            model.setHardwareName(productProcessList.get(i).getName());
            model.setIdFifo1(this.getFifoCount());
            fifoList.add(model);
        }
        for (int i = 0; i < duplicationProcessList.size(); i++) {
            FifoModel model = new FifoModel();
            model.setHardwareName(duplicationProcessList.get(i).getName());
            model.setIdFifo1(this.getFifoCount());
            model.setIdFifo2(this.getFifoCount());
            fifoList.add(model);
        }
        for (int i = 0; i < sinkProcessList.size(); i++) {
            FifoModel model = new FifoModel();
            model.setHardwareName(sinkProcessList.get(i).getName());
            model.setIdFifo1(this.getFifoCount());
            fifoList.add(model);
        }
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            FifoModel model = new FifoModel();
            model.setHardwareName(constantGenerationProcessList.get(i).getName());
            model.setIdFifo1(this.getFifoCount());
            fifoList.add(model);
        }
    }

    /**
     * This method is used for entry's insertion in the XML file.
     *
     * @param doc
     * @param module
     * @param element
     * @param tagName
     */
    private void insertEntry(Document doc, Element module, String element, String tagName) {
        Element tag;
        FifoModel model = searchFifo(element);
        if (element.contains("duplication")) {
            tag = doc.createElement("tagName");
            if (model.getOutput() == 1) {
                tag.appendChild(doc.createTextNode("fifo_" + model.getIdFifo1()
                        + "_1"));
            } else {
                tag.appendChild(doc.createTextNode("fifo_" + model.getIdFifo2()
                        + "_1"));
            }
        } else {
            tag = doc.createElement("tagName");
            tag.appendChild(doc.createTextNode("fifo_" + model.getIdFifo1() + "_1"));
        }

        module.appendChild(tag);
    }

    /**
     * This method is used to add the adders to the XML file.
     *
     * @param doc
     * @param rootElement
     */
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
            String element;
            element = addProcessList.get(i).getQueue1InputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_1");
            }
            element = addProcessList.get(i).getQueue2InputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_2");
            }
        }
    }

    /**
     * This method is used to add the producers to the XML file.
     *
     * @param doc
     * @param rootElement
     */
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
            String element;
            element = productProcessList.get(i).getQueue1InputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_1");
            }
            element = productProcessList.get(i).getQueue2InputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_2");
            }
        }
    }

    /**
     * This method is used to add the duplicators to the XML file.
     *
     * @param doc
     * @param rootElement
     */
    private void addDuplicationToXML(Document doc, Element rootElement) {
        for (int i = 0; i < duplicationProcessList.size(); i++) {
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
            String element;
            element = duplicationProcessList.get(i).getQueueInputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_1");
            }
        }
    }

    /**
     * This method is used to add the sinks to the XML file.
     *
     * @param doc
     * @param rootElement
     */
    private void addSinkToXML(Document doc, Element rootElement) {
        for (int i = 0; i < sinkProcessList.size(); i++) {
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
            String element;
            element = sinkProcessList.get(i).getQueueInputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_1");
            }
        }
    }

    /**
     * This method is used to add the constant generators to the XML file.
     *
     * @param doc
     * @param rootElement
     */
    private void addConstantGenerationToXML(Document doc, Element rootElement) {
        for (int i = 0; i < constantGenerationProcessList.size(); i++) {
            // staff elements
            Element module = doc.createElement("module");
            rootElement.appendChild(module);
            // set attribute to staff element
            module.setAttribute("id", getID(constantGenerationProcessList.get(i).getName()));
            module.setAttribute("type", "queue");
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
            module.setAttribute("elements", a.toString().replace("[", "").replace("]", ""));
            //tags
            String element = constantGenerationProcessList.get(i).getQueueInputAssigned();
            if (!element.equals("")) {
                this.insertEntry(doc, module, element, "entry_1");
            }
        }
    }

    /**
     * This method is used to add the fifos to the XML file.
     * @param doc
     * @param rootElement 
     */
    private void addFIFOToXML(Document doc, Element rootElement) {
        for (int i = 0; i < fifoList.size(); i++) {
            FifoModel model = fifoList.get(i);
            if (model.getIdFifo1() != 0) {
                addFIFOToXMLAux(doc, rootElement, model.getHardwareName(), String.valueOf(model.getIdFifo1()), "1");
            }
            if (model.getIdFifo2() != 0) {
                addFIFOToXMLAux(doc, rootElement, model.getHardwareName(), String.valueOf(model.getIdFifo2()), "2");
            }
        }
    }

    private void addFIFOToXMLAux(Document doc, Element rootElement, String hardwareName, String id, String output) {
        // staff elements
        Element module = doc.createElement("module");
        rootElement.appendChild(module);
        // set attribute to staff element
        module.setAttribute("id", id);
        module.setAttribute("type", "fifo");
        module.setAttribute("entries", "1");
        module.setAttribute("outputs", "1");
        module.setAttribute("elements", "");
        //tags
        Element tag = null;
        tag = doc.createElement("entry_1");
        tag.appendChild(doc.createTextNode(this.getHardwareNameXML(hardwareName) + "_" + this.getID(hardwareName)
                + "_" + output));

        module.appendChild(tag);
    }

    /**
     * This method manages all the XML file generation.
     * @param path 
     */
    public void exportKPNToXML(String path) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("modules");
            doc.appendChild(rootElement);
            //Adding the modules
            addFIFOToXML(doc, rootElement);
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

    /**
     * This method returns the id of any hardware. 
     * @param word
     * @return 
     */
    private String getID(String word) {
        return word.substring(word.indexOf("ID: ") + 4, word.indexOf(", Name"));
    }

    /**
     * This method returns the xml name of any hardware.
     * @param name
     * @return 
     */
    private String getHardwareNameXML(String name) {
        if (name.contains("duplication")) {
            return "split";
        } else if (name.contains("adder")) {
            return "adder";
        } else if (name.contains("product")) {
            return "multiplier";
        } else if (name.contains("constantGeneration")) {
            return "queue";
        } else if (name.contains("sink")) {
            return "fifo";
        } else {
            return "";
        }
    }

    /**
     * @return the fifoCount
     */
    public int getFifoCount() {
        fifoCount++;
        return fifoCount;
    }

    /**
     * @param fifoCount the fifoCount to set
     */
    public void setFifoCount(int fifoCount) {
        this.fifoCount = fifoCount;
    }

}
