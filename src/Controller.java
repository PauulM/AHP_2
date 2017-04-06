import Jama.Matrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeMath.round;

/**
 * Created by pawma on 30.03.2017.
 */
public class Controller {

    private static Map<Integer, Criterion> criteriaMap = new HashMap<>();
    private static Integer criteriaCount = 0;
    private static boolean allCriteriaAdded = false;
    private static Map<Integer, Alternative> alternativesMap = new HashMap<>();
    private static Integer alternativesCount = 0;
    private static boolean allAlternativesAdded = false;
    private static String pathToXml;
    private static Double cr = 0.1;

    private static ArrayList<Criterion> currentSiblings = null;

    private Scene scene;
    private AnchorPane mainPane;
    private TextArea infoArea;

    private static ArrayList<Criterion> finalList = new ArrayList<>();

    public Controller() {
//        //this.criteriaList = new ArrayList<>();
//        this.criteriaMap = new HashMap<>();
//        //this.alternativesList = new ArrayList<>();
//        this.alternativesMap = new HashMap<>();
//        criteriaCount = 0;
//        alternativesCount = 0;
    }

    public void setSceneAndMainPane(Scene scene){
        this.scene = scene;
        this.mainPane = (AnchorPane) this.scene.lookup("#mainPane");
        this.infoArea = (TextArea) this.scene.lookup("#infoArea");
    }

    private void loadFxmlToMainPane(String fileName) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource(fileName));
        this.mainPane.getChildren().setAll(pane);
    }

    public void buttonAddAlternative(ActionEvent event) throws IOException{
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(allAlternativesAdded){
            infoArea.setText("Alternatives already accepted");
            return;
        }
        loadFxmlToMainPane("GUI_AddAlternative.fxml");
    }

    public void alternativesAcceptedButton(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(alternativesCount == 0){
            infoArea.setText("Add alternatives before accepting");
            return;
        }
        this.allAlternativesAdded = true;
        infoArea.setText("Alternatives accepted, you cannot change them now");
    }

    public void criteriaAcceptedButton(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(criteriaCount == 0){
            infoArea.setText("Add criteria before accepting");
            return;
        }
        this.allCriteriaAdded = true;
        infoArea.setText("Criteria accepted, you cannot change them now");
    }

    public void buttonAcceptAddingAlternative(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(allAlternativesAdded){
            return;
        }
        TextField textField = (TextField)scene.lookup("#alternativeNameField");
        String name = textField.getText();
        if(name.length() == 0){
            infoArea.setText("Name empty");
            return;
        }
        addAlternativeToList(name);
        Alternative alternative = new Alternative();
        alternative.setName(name);
        //alternativesList.add(alternative);
        alternativesCount++;
        alternativesMap.put(alternativesCount, alternative);
        textField.clear();
        infoArea.setText("Alternative added");
    }

    private void addAlternativeToList(String name){
        Button button = new Button(name);
        button.setPrefWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            displayAlternativeWindow(button.getText());
        });
        VBox box = (VBox)scene.lookup("#alternativesList");
        box.getChildren().add(button);
    }

    private void displayAlternativeWindow(String alternativeName){

    }


    public void buttonAddCriterion(ActionEvent event) throws IOException{
        setSceneAndMainPane(((Node) event.getSource()).getScene());

        if(allCriteriaAdded){
            infoArea.setText("Criteria already accepted");
            return;
        }

        loadFxmlToMainPane("GUI_AddCriterion.fxml");
    }

    public void buttonAcceptAddingCriterion(ActionEvent event) {
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(allCriteriaAdded){
            return;
        }
        TextField nameField = (TextField)scene.lookup("#criterionNameField");
        TextField parentField = (TextField)scene.lookup("#parentNameField");
        String name = nameField.getText();
        String parentName = parentField.getText();
        if(name.length() == 0){
            infoArea.setText("Name empty");
            return;
        }
        if(addCriterionToList(name, parentName)){
            infoArea.setText("Criterion added");
        }
        nameField.clear();
        parentField.clear();
    }

    private boolean addCriterionToList(String name, String parentName){
        Button button = new Button(name);
        button.setPrefWidth(Double.MAX_VALUE);
//        button.setOnAction(e -> {
//            displayCriterionWindow(button.getText(), e);
//        });

        Criterion criterion = new Criterion();
        criterion.setName(name);
        if(parentName.length() != 0){
            Criterion parentCriterion = Criterion.findCriterionInListByName(new ArrayList<>(criteriaMap.values()),
                    parentName);
            if(parentCriterion == null){
                infoArea.setText("Parent does not exist");
                return false;
            }
            parentCriterion.addSubCriteria(criterion);
            criterion.setParentCriterium(parentCriterion);
        }
        VBox box = (VBox)scene.lookup("#criteriaList");
        box.getChildren().add(button);
        criteriaCount++;
        //criteriaList.add(criterion);
        criteriaMap.put(criteriaCount, criterion);
        button.setOnAction(e -> {
            displayCriterionWindow(criterion, e);
        });
        return true;
    }

    private void displayCriterionWindow(Criterion criterion, ActionEvent event) {
        try{
            setSceneAndMainPane(((Node) event.getSource()).getScene());
            loadFxmlToMainPane("GUI_Criterion.fxml");
            TextArea nameArea = (TextArea) scene.lookup("#criterionName");
            nameArea.setText(criterion.getName());
            TextArea parentArea = (TextArea) scene.lookup("#criterionParent");
            ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
            try{
                parentArea.setText(criterion.getParentCriterium().getName());
            }
            catch (NullPointerException ex){
                parentArea.setText("no parent");
            }
            VBox list = new VBox();
            list.setPrefWidth(248);
            for (Criterion sc : criterion.getSubCriteriaList()) {
                Button button = new Button(sc.getName());
                button.setPrefWidth(Double.MAX_VALUE);
                list.getChildren().add(button);
                button.setOnAction(e -> {
                    displayCriterionWindow(sc, e);
                });
            }
            scrollPane.setContent(list);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("GUI_Criterion.fxml load error");
        }
    }

    public void addSubcriterion(ActionEvent event) throws IOException{
        String parentName = ((TextArea)(((Node)event.getSource()).getScene().lookup("#criterionName"))).getText();
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(allCriteriaAdded){
            infoArea.setText("Criteria already accepted");
            return;
        }
        loadFxmlToMainPane("GUI_AddCriterion.fxml");
        ((TextField)scene.lookup("#parentNameField")).setText(parentName);
    }

    public void setPathToXml(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        TextField textField = (TextField)scene.lookup("#pathToXmlValue");
        if(textField.getText().length() == 0){
            infoArea.setText("Path empty");
            return;
        }
        pathToXml = textField.getText();
        infoArea.setText("Path to XML set");
    }

    public void setCrValue(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        TextField textField = (TextField)scene.lookup("#crValue");
        if(textField.getText().length() == 0){
            infoArea.setText("Value empty");
            return;
        }
        cr = Double.parseDouble(textField.getText());
        infoArea.setText("Consistency ratio set");
    }

    public void specifyCriteriaWeights(ActionEvent event) throws IOException{
        String name = ((TextArea)(((Node)event.getSource()).getScene().lookup("#criterionName"))).getText();
        Criterion current = Criterion.findCriterionInListByName(new ArrayList<>(criteriaMap.values()),name);
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(!allCriteriaAdded){
            infoArea.setText("Finish adding all criteria");
            return;
        }
        loadFxmlToMainPane("GUI_SpecifyWeights.fxml");
        TextArea criterionNameArea = (TextArea) scene.lookup("#criterionName");
        criterionNameArea.setText(name);
        ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        HBox hBox = new HBox();
        hBox.setPrefWidth(248);
        VBox toVBox = new VBox();
        toVBox.setPrefWidth(123);
        VBox valueVBox = new VBox();
        valueVBox.setPrefWidth(123);
        hBox.getChildren().addAll(toVBox, valueVBox);
        scrollPane.setContent(hBox);
        //ArrayList<Criterion> siblingsCriteria;
        currentSiblings = new ArrayList<>();
        try{
            Criterion parentCriterion = Criterion.findParentInHashMapByName(criteriaMap, name);
            String parentName = parentCriterion.getName();
            //siblingsCriteria = Criterion.findSiblingsCriteriaByParentName(new ArrayList<>(criteriaMap.values()), parentName);
            currentSiblings = Criterion.findSiblingsCriteriaByParentName(new ArrayList<>(criteriaMap.values()), parentName);
        }
        catch (NullPointerException ex){
            //siblingsCriteria = Criterion.findSiblingsCriteriaByParentName(new ArrayList<>(criteriaMap.values()), null);
            currentSiblings = Criterion.findSiblingsCriteriaByParentName(new ArrayList<>(criteriaMap.values()), null);
        }
        for(Criterion sc : currentSiblings){
            if(sc.getName().equals(name))
                continue;
            Label label = new Label();
            label.setText(sc.getName());
            label.setPrefWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(14));
            toVBox.getChildren().add(label);
            TextField tf = new TextField();
            tf.setPrefWidth(Double.MAX_VALUE);
            try{
                tf.setText(current.findWeightValueToByName(sc.getName()).toString());
            }
            catch (Exception ex){}
            //tf.setId(criteriaMap.get(sc).toString());
            valueVBox.getChildren().add(tf);
        }
    }

    public void acceptSpecifyWeightsButton(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        String name = ((TextArea)scene.lookup("#criterionName")).getText();
        Criterion current = Criterion.findCriterionInListByName(currentSiblings, name);
        ScrollPane scrollPane = (ScrollPane)scene.lookup("#scrollPane");
        HBox hBox = (HBox) scrollPane.getContent();
        VBox nameBox = (VBox)hBox.getChildren().get(0);
        VBox valueBox = (VBox)hBox.getChildren().get(1);
        int i = 0;
        for(Criterion sc : currentSiblings){
            if(sc.getName().equals(name))
                continue;
            Weight weight = new Weight();
            weight.setTo(((Label)nameBox.getChildren().get(i)).getText());
            weight.setValue(Double.parseDouble(((TextField)valueBox.getChildren().get(i)).getText()));
            if(!Criterion.hasGivenWeight(current, weight))
                current.addWeight(weight);
            Weight oppositeWeight = new Weight();
            oppositeWeight.setValue(1d/Double.parseDouble(((TextField)valueBox.getChildren().get(i)).getText()));
            oppositeWeight.setTo(current.getName());
            Criterion opposite = Criterion.findCriterionInListByName(currentSiblings, sc.getName());
            if(!Criterion.hasGivenWeight(opposite, oppositeWeight))
                opposite.addWeight(oppositeWeight);
            i++;
        }
        infoArea.setText("Choice accepted");
    }

    public void applyWeights(ActionEvent event) throws Exception{
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        MyMatrix myMatrix = MyMatrix.createWeightMatrixFromSiblingCriteria(currentSiblings);
        Matrix matrix = myMatrix.toMatrix();
        try{
            Double ratio = Consistency.calculateConsistencyRatio(matrix);
            if(ratio > cr){
                infoArea.setText("Consistency ratio is " + ratio + " which is greater than acceptable " + ratio +
                        "\nTry again");
            }
            else{
                finalList.addAll(currentSiblings);
                infoArea.setText("Weights added, CR=" + ratio);
            }
        }
        catch (Exception ex){
            finalList.addAll(currentSiblings);
            infoArea.setText("Problem occurred, CR not calculated\n" + ex.getMessage());
        }
        currentSiblings = null;
    }

    public void specifyPrioritiesButton(ActionEvent event)throws IOException{
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(!allAlternativesAdded){
            infoArea.setText("Finish adding alternatives");
            return;
        }
        String name = ((TextArea)(((Node)event.getSource()).getScene().lookup("#criterionName"))).getText();
        Criterion current = Criterion.findCriterionInListByName(new ArrayList<>(criteriaMap.values()),name);
        // POWYŻEJ ZMIANA, PRÓBUJĘ ZROBIĆ ŻEBY NIE TRZEBA BYŁO OKREŚLAĆ WAG PRZED OKREŚLENIEM PRIORYTETÓW
        // WCZEŚNIEJ BYŁO findCriterionInListByName(currentSiblings,name);
        if(current == null){
            infoArea.setText("This criterion has not been accepted");
            return;
        }
        if(current.hasSubcriteria()){
            infoArea.setText("You cannot specify alternatives if the criterion has subcriteria");
            return;
        }
        loadFxmlToMainPane("GUI_SpecifyPriorities.fxml");
        TextArea criterionNameArea = (TextArea) scene.lookup("#criterionName");
        criterionNameArea.setText(name);
        ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        HBox hBox = new HBox();
        hBox.setPrefWidth(248);
        VBox toVBox = new VBox();
        toVBox.setPrefWidth(82);
        VBox baseVBox = new VBox();
        baseVBox.setPrefWidth(82);
        VBox valueVBox = new VBox();
        valueVBox.setPrefWidth(82);
        hBox.getChildren().addAll(baseVBox, toVBox, valueVBox);
        scrollPane.setContent(hBox);
        ArrayList<Alternative> alternatives = new ArrayList<>(alternativesMap.values());
        for (int i = 0; i < alternatives.size()-1; i++){
            for(int j = 0; j < alternatives.size(); j++){
                if(i >= j)
                    continue;
                Label labelBase = new Label();
                labelBase.setText(alternatives.get(i).getName());
                labelBase.setPrefWidth(Double.MAX_VALUE);
                labelBase.setAlignment(Pos.CENTER);
                labelBase.setFont(Font.font(14));
                baseVBox.getChildren().add(labelBase);
                Label labelTo = new Label();
                labelTo.setText(alternatives.get(j).getName());
                labelTo.setPrefWidth(Double.MAX_VALUE);
                labelTo.setAlignment(Pos.CENTER);
                labelTo.setFont(Font.font(14));
                toVBox.getChildren().add(labelTo);
                TextField tf = new TextField();
                tf.setPrefWidth(Double.MAX_VALUE);
                try{
                    tf.setText(current.findAlternativeByName(
                            alternatives.get(i).getName()).findPriorityToByName(alternatives.get(j).getName()).toString());
                }
                catch (Exception ex){}
                valueVBox.getChildren().add(tf);
            }
        }
    }

    public void acceptSpecifyPrioritiesButton(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        String name = ((TextArea)scene.lookup("#criterionName")).getText();
        Criterion current = Criterion.findCriterionInListByName(new ArrayList<>(criteriaMap.values()), name);
        // PATRZ METODA specifyPrioritiesButton
        ScrollPane scrollPane = (ScrollPane)scene.lookup("#scrollPane");
        HBox hBox = (HBox) scrollPane.getContent();
        VBox baseBox = (VBox)hBox.getChildren().get(0);
        VBox toBox = (VBox) hBox.getChildren().get(1);
        VBox valueBox = (VBox)hBox.getChildren().get(2);
        for(int i = 0; i < baseBox.getChildren().size(); i++){
            Alternative baseAlt = new Alternative();
            baseAlt.setName(((Label)baseBox.getChildren().get(i)).getText());
            Weight baseWeight = new Weight();
            baseWeight.setTo(((Label) toBox.getChildren().get(i)).getText());
            baseWeight.setValue(Double.parseDouble(((TextField) valueBox.getChildren().get(i)).getText()));
            if(current.findAlternativeByName(baseAlt.getName()) == null) {
                baseAlt.addPriority(baseWeight);
                current.addAlternative(baseAlt);
            }
            else{
                baseAlt = current.findAlternativeByName(baseAlt.getName());
                baseAlt.addPriority(baseWeight);
            }

            Alternative toAlt = new Alternative();
            toAlt.setName(((Label)toBox.getChildren().get(i)).getText());
            Weight toWeight = new Weight();
            toWeight.setTo(((Label) baseBox.getChildren().get(i)).getText());
            toWeight.setValue(Double.parseDouble(((TextField) valueBox.getChildren().get(i)).getText()));
            if(current.findAlternativeByName(toAlt.getName()) == null) {
                toAlt.addPriority(toWeight);
                current.addAlternative(toAlt);
            }
            else{
                toAlt = current.findAlternativeByName(toAlt.getName());
                toAlt.addPriority(toWeight);
            }
        }
        MyMatrix myMatrix = MyMatrix.createPriorityMatrixFromAlternativesList(current.getAlternativesList());
        Matrix matrix = myMatrix.toMatrix();
        try{
            Double ratio = Consistency.calculateConsistencyRatio(matrix);
            if(ratio > cr){
                infoArea.setText("Consistency ratio is " + ratio + " which is greater than acceptable " + ratio +
                        "\nTry again");
                current.getAlternativesList().clear();
                return;
            }
            infoArea.setText("Priorities set\nCR = " + ratio);
        }
        catch (Exception ex){
            infoArea.setText("Problem occurred, CR not calculated\n" + ex.getMessage());
        }
    }

    public void generateXmlButton(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        if(pathToXml == null){
            infoArea.setText("Path to XML not specified!");
            return;
        }
        if(!allAlternativesAdded){
            infoArea.setText("Add alternatives first");
            return;
        }
        if(!allCriteriaAdded){
            infoArea.setText("Add criteria first");
            return;
        }
        if(finalList.isEmpty()){
            infoArea.setText("Some information is missing");
            return;
        }
        File file = new File(pathToXml);
        file.delete();
        ToXml toXml = new ToXml();
        toXml.setCriteriaList(finalList);
        toXml.createXml(pathToXml);
        infoArea.setText("XML Successfully generated");
    }

}