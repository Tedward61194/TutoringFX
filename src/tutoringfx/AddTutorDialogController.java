package tutoringfx;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Interaction;
import models.ORM;
import models.Subject;
import models.Tutor;
import org.apache.commons.validator.routines.EmailValidator;
import static tutoringfx.TutoringController.tutorInfo;

/**
 * FXML Controller class
 *
 * @author Teddy Segal
 */
public class AddTutorDialogController implements Initializable {

    @FXML
    Node top;
    TutoringController mainController;

    @FXML
    TextField lastNameField;
    @FXML
    TextField firstNameField;
    @FXML
    TextField emailField;
    @FXML
    ComboBox<String> subjectSelection;

    @FXML
    void add(Event event) {
      String lastName = lastNameField.getText().trim();
      String firstName = firstNameField.getText().trim();
      String email = emailField.getText().trim();
      String subject = subjectSelection.getSelectionModel().getSelectedItem();
      int subjectId;
      String fullName = lastName + "," + firstName;
      
    try {
        if (lastName.length() < 3) {
          throw new ExpectedException("Last name must have at least three letters.");
        }
        if (firstName.length() < 3) {
          throw new ExpectedException("Last name must have at least three letters.");
        }
        Tutor tutorWithName 
          = ORM.findOne(Tutor.class, "where name=?", new Object[]{fullName});
        if (tutorWithName != null) {
          throw new ExpectedException("Existing tutor with the same name.");
        }
        if (!emailIsValid(email)){
            throw new ExpectedException("Email must be valid.");
        }
        Subject subs = ORM.findOne(Subject.class, "where name=?", new Object[]{subject});
        subjectId = subs.getId();
        if (subject == null) {
          throw new ExpectedException("Must select a subject.");
        }
        
        Tutor newTutor = new Tutor(fullName, email, subjectId);
        mainController.tutorlist.getItems().add(newTutor);      
        newTutor.insert();
        
        mainController.tutorlist.scrollTo(newTutor);
        mainController.tutorlist.getSelectionModel().select(newTutor);  
        mainController.studentlist.getSelectionModel().clearSelection();
        Collection<Interaction> interactions = ORM.findAll(Interaction.class, "where tutor_id=?", new Object[]{newTutor.getId()});
        mainController.tutorStudentIds.clear();
        for (Interaction interaction : interactions) {
                mainController.tutorStudentIds.add(interaction.getStudentId());
        }
        
        mainController.display.setText(tutorInfo(newTutor));

        List<Tutor> tempList;
        tempList = mainController.tutorlist.getItems();
        Collections.sort(tempList, (Tutor t1, Tutor t2) -> t1.getName().compareTo(t2.getName()));
        mainController.tutorlist.setItems((ObservableList<Tutor>) tempList);
        
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
    }//end cancel
    public boolean emailIsValid(String email){
        boolean isValid = true;
        EmailValidator eObject;
        eObject = EmailValidator.getInstance();
        isValid = eObject.isValid(email);
        return isValid;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            Collection<Subject> subs = ORM.findAll(Subject.class);
            for (Subject subject : subs){
                subjectSelection.getItems().add(subject.getName());
            }
        }
        catch (Exception ex){
               ex.printStackTrace(System.err);
               System.exit(1);
        }

    }
}
