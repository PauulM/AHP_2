import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pawma on 30.03.2017.
 */
public class Controller {

    private ArrayList<Criterion> criteriaList;
    private Scene scene;
    private AnchorPane mainPane;
    private String pathToXml;
    private TextArea infoArea;

    public Controller(){
        this.criteriaList = new ArrayList<>();
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
        loadFxmlToMainPane("GUI_AddAlternative.fxml");
    }

    public void buttonAcceptAddingAlternative(ActionEvent event){
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        TextField textField = (TextField)scene.lookup("#alternativeNameField");
        String name = textField.getText();
        if(name.length() == 0){
            infoArea.setText("Name empty");
            return;
        }
        addAlternativeToList(name);
        textField.clear();
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
        System.out.println(alternativeName);
    }


    public void buttonAddCriterion(ActionEvent event) throws IOException{
        setSceneAndMainPane(((Node) event.getSource()).getScene());
        loadFxmlToMainPane("GUI_AddCriterion.fxml");
    }

    public void buttonAcceptAddingCriterion(ActionEvent event) {

    }
}

