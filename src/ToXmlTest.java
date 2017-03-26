import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pawma on 26.03.2017.
 */
public class ToXmlTest {

    private final String pathname = "C:\\Users\\pawma\\Informatyka\\Badania\\Projekt 2\\test.xml";

    @Test
    public void createXmlTest(){
        XmlData xmlData = new XmlData("example.xml");
        xmlData.initializeCriteriaList();
        ToXml toXml = new ToXml();
        toXml.setCriteriaList(xmlData.getCriteria());
        toXml.createXml(pathname);
    }

}