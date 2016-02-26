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
import javafx.scene.control.TextField;
import models.Interaction;
import models.ORM;
import models.Student;
import org.apache.commons.validator.routines.EmailValidator;
import static tutoringfx.TutoringController.studentInfo;

/**
 * FXML Controller class
 *
 * @author Teddy Segal
 */
public class AddStudentDialogController implements Initializable {

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
    void add(Event event) {
      String lastName = lastNameField.getText().trim();
      String firstName = firstNameField.getText().trim();
      String email = emailField.getText().trim();
      String fullName = lastName + "," + firstName;

    try {
        if (lastName.length() < 3) {
          throw new ExpectedException("Last name must have at least three letters.");
        }
        if (firstName.length() < 3) {
          throw new ExpectedException("First name must have at least three letters.");
        }
        Student studentWithName 
          = ORM.findOne(Student.class, "where name=?", new Object[]{fullName});
        if (studentWithName != null) {
          throw new ExpectedException("Existing student with the same name.");
        }
        if (!emailIsValid(email)){
            throw new ExpectedException("Email must be valid.");
        }
        
        Student newStudent = new Student(fullName, email, currentDate());
        mainController.studentlist.getItems().add(newStudent);
        newStudent.insert();
        
        mainController.studentlist.scrollTo(newStudent);
        mainController.studentlist.getSelectionModel().select(newStudent);
        mainController.tutorlist.getSelectionModel().clearSelection(); //clear tutor selection
        Collection<Interaction> interactions = ORM.findAll(Interaction.class, "where student_id=?", new Object[]{newStudent.getId()});
        mainController.studentTutorIds.clear();
        for (Interaction interaction : interactions) {
            mainController.studentTutorIds.add(interaction.getTutorId());
        }
        
        mainController.display.setText(studentInfo(newStudent)); 
        mainController.studentTutorIds.clear();    
        if(mainController.nameSortStyle){
            List<Student> tempList;
            tempList = mainController.studentlist.getItems();
            Collections.sort(tempList, (Student o1, Student o2) -> o1.getName().compareTo(o2.getName()));
            mainController.studentlist.setItems((ObservableList<Student>) tempList); 
        }
        else {
            List<Student> tempList;
            tempList = mainController.studentlist.getItems();
            Collections.sort(tempList, (Student o1, Student o2) -> o1.getDateEnrolled().compareTo(o2.getDateEnrolled()));
            mainController.studentlist.setItems((ObservableList<Student>) tempList);
        }
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

    static java.sql.Date currentDate() {
    long now = new java.util.Date().getTime();
    java.sql.Date date = new java.sql.Date(now);
    return date;
    }
    
    public boolean emailIsValid(String email){
        boolean isValid = true;
        EmailValidator eObject;
        eObject = EmailValidator.getInstance();
        isValid = eObject.isValid(email);
        return isValid;
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
}

