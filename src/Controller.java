import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private Scene scene;
    private AnchorPane mainPane;
    private TextArea infoArea;

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
            //VBox list = (VBox) scene.lookup("#subcriteriaList");
            ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
            //VBox list = (VBox) scrollPane.lookup("#subcriteriaList");
            try{
                parentArea.setText(criterion.getParentCriterium().getName());
            }
            catch (NullPointerException ex){
                parentArea.setText("no parent");
            }
            //VBox list = (VBox) scene.lookup("#subcriteriaList");
//            Platform.runLater(()-> {
//                VBox list = (VBox) scrollPane.lookup("#subcriteriaList");
//                for (Criterion sc : criterion.getSubCriteriaList()) {
//                    Button button = new Button(sc.getName());
//                    button.setPrefWidth(Double.MAX_VALUE);
//                    list.getChildren().add(button);
//                    button.setOnAction(e -> {
//                        displayCriterionWindow(sc, e);
//                    });
//                }
//            });
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
        ArrayList<Criterion> siblingsCriteria;

        try{
            Criterion parentCriterion = Criterion.findParentInHashMapByName(criteriaMap, name);
            String parentName = parentCriterion.getName();
            siblingsCriteria = Criterion.findSiblingsCriteriaByParentName(new ArrayList<>(criteriaMap.values()), parentName);
        }
        catch (NullPointerException ex){
            siblingsCriteria = Criterion.findSiblingsCriteriaByParentName(new ArrayList<>(criteriaMap.values()), null);
        }
        for(Criterion sc : siblingsCriteria){
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
            //tf.setId(criteriaMap.get(sc).toString());
            valueVBox.getChildren().add(tf);
        }
    }

}