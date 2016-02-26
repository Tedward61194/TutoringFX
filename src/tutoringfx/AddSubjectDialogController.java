package tutoringfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.ORM;
import models.Subject;

/**
 * AddSubjectDialogController class
 *
 * @author Teddy Segal
 */
public class AddSubjectDialogController implements Initializable {

    @FXML
    Node top;
    TutoringController mainController;

    @FXML
    TextField nameField;

    @FXML
    void add(Event event) {
        String name = nameField.getText().trim();
        
        try {
            if (name.length() < 3) {
                throw new ExpectedException("Subject name must have at least three letters.");
            }
            Subject subjectWithName 
              = ORM.findOne(Subject.class, "where name=?", new Object[]{name});
            if (subjectWithName != null) {
                 throw new ExpectedException("Existing subject with the same name.");
            }

            Subject newSub = new Subject(name);
            newSub.insert();

            top.getScene().getWindow().hide();
        }
        catch (ExpectedException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
        }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
      System.exit(1);
    }

        top.getScene().getWindow().hide();
    }

    @FXML
    void cancel(Event event) {
      top.getScene().getWindow().hide();
    }
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
}
