import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by pawma on 26.03.2017.
 */
public class ToXml {

    //private ArrayList<Alternative> alternativesList = new ArrayList<>();
    private ArrayList<Criterion> criteriaList = new ArrayList<>();
    private Document document;

    public ToXml(){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.newDocument();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void setAlternativesList(ArrayList<Alternative> alternativesList) {
//        this.alternativesList = alternativesList;
//    }

    public void setCriteriaList(ArrayList<Criterion> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public void writeToXml(String pathname){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(pathname));
            transformer.transform(source, result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createXml(){
        Element rootElement = document.createElement("goal");
        document.appendChild(rootElement);
        handleSiblingsCriteria(findRootCriteria(criteriaList), rootElement);

    }

    private void handleSiblingsCriteria(ArrayList<Criterion> siblingsCriteria, Element parentElement) {
        for(Criterion c : siblingsCriteria){
            Element criterion = document.createElement("criterium");
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(c.getName()));
            criterion.appendChild(name);
            for(Criterion c1 : siblingsCriteria){
                if(c.getName().equals(c1.getName()))
                    continue;
                Element weight = document.createElement("weight");
                Attr to = document.createAttribute("to");
                to.setValue(c1.getName());
                weight.setAttributeNode(to);
                weight.appendChild(document.createTextNode(c.findWeightValueToByName(c1.getName()).toString()));
                criterion.appendChild(weight);
            }
            parentElement.appendChild(criterion);
            if(c.hasSubcriteria()) {
                handleSiblingsCriteria(c.getSubCriteriaList(), criterion);
            }
            else{
                for(Alternative a : c.getAlternativesList()){
                    Element alt = document.createElement("alternative");
                    Element altName = document.createElement("name");
                    altName.appendChild(document.createTextNode(a.getName()));
                    alt.appendChild(altName);
                    for(Alternative a1 : c.getAlternativesList()){
                        if(a.getName().equals(a1.getName()))
                            continue;
                        Element priority = document.createElement("priority");
                        Attr altTo = document.createAttribute("to");
                        altTo.setValue(a1.getName());
                        priority.setAttributeNode(altTo);
                        priority.appendChild(document.createTextNode(a.findPriorityToByName(a1.getName()).toString()));
                        alt.appendChild(priority);
                    }
                    criterion.appendChild(alt);
                }
            }
        }
    }


//    public void createElement(String tagName){
//        Element rootElement = document.createElement(tagName);
//        document.appendChild(rootElement);
//        Element abc = document.createElement("name");
//        abc.appendChild(document.createTextNode("nazwa"));
//        rootElement.appendChild(abc);
//    }

    public static ArrayList<Criterion> findRootCriteria(ArrayList<Criterion> criteria) {
        ArrayList<Criterion> rootCriteria = new ArrayList<>();
        for (Criterion c : criteria) {
            if (c.getParentCriterium() == null)
                rootCriteria.add(c);
        }
        return rootCriteria;
    }

    private Criterion findSiblingCriterionByName(String name){
        Criterion result = null;
        for(Criterion c : criteriaList){
            if(c.getName().equals(name))
                result = c;
        }
        return result;
    }
}
