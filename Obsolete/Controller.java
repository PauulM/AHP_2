import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pawma on 28.03.2017.
 */
public class Controller {

    private ToXml toXml;
    private Scene scene;
    private String pathToXml;
    private ArrayList<Label> alternatives;
    private ArrayList<Label> criteria;


//    public Controller(){
//        this.toXml = new ToXml();
//        this.alternatives = new ArrayList<>();
//        this.criteria = new ArrayList<>();
//    }
    @FXML
    public void initialize(){
        this.toXml = new ToXml();
        this.alternatives = new ArrayList<>();
        this.criteria = new ArrayList<>();
    }

    public void upadteScene(ActionEvent e){
        this.scene = ((Node)e.getSource()).getScene();

        this.mainPane = (AnchorPane) scene.lookup("#mainPane");

        displayCriteria();
        displayAlternatives();
    }

    private void displayCriteria(){
        VBox vBox = (VBox) scene.lookup("#criteriaList");
        vBox.getChildren().clear();
        for(Label tmpL : criteria){
            vBox.getChildren().add(tmpL);
        }
    }

    private void displayAlternatives(){
        VBox vBox = (VBox) scene.lookup("#alternativesList");
        vBox.getChildren().clear();
        for(Label tmpL : alternatives){
            vBox.getChildren().add(tmpL);
        }
    }

    public void buttonAddAlternative(ActionEvent event) throws IOException{
        changeScene("Obsolete/GUIAddAlternative.fxml", event);
    }

    private void changeScene(String fxmlFilePath, ActionEvent e)throws IOException{
        Parent home_page_parent = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.show();
        //upadteScene(e);
    }

    public void buttonAcceptAddingAlternative(ActionEvent event){
        upadteScene(event);
        Label label = createBoxLabel();
        alternatives.add(label);
        upadteScene(event);
    }

    private Label createBoxLabel(){
        String name = ((TextField)scene.lookup("#nameField")).getText();
        TextField textField = (TextField) scene.lookup("#nameField");
        textField.clear();
        Label label = new Label();
        label.setFont(new Font("Arial", 18));
        label.setText("-" + name);
        return label;
    }

    public void buttonAddCriterion(ActionEvent event) throws IOException{
        changeScene("GUIAddCriterion.fxml", event);
    }

    public void buttonAcceptAddingCriterion(ActionEvent event) {
        upadteScene(event);
        Label label = createBoxLabel();
        criteria.add(label);
        upadteScene(event);
    }
}
