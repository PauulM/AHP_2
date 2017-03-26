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

    private ArrayList<Alternative> alternativesList = new ArrayList<>();
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

    public void setAlternativesList(ArrayList<Alternative> alternativesList) {
        this.alternativesList = alternativesList;
    }

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
}
