package tutoringfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.ORM;
import models.Subject;

/**
 * FXML Controller class
 *
 * @author Teddy Segal
 */
public class CancelConfirmationDialogController implements Initializable {

    @FXML
    Node top;
    ModifyReportDialogController mainController;
    
    @FXML
    Label cancelMessage;
    
    @FXML
    void setup() {
        cancelMessage.setText("You modified the report. \nAre you sure you wish to cancel without saving?");
    }
    
    @FXML
    void yes(Event event) { 
        mainController.top.getScene().getWindow().hide();
        top.getScene().getWindow().hide();
    }
    
    @FXML
    void no(Event event) {
        top.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
