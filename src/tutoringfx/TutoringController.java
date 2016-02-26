package tutoringfx;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.*;

/**
 * TutoringController class
 *
 * @author Teddy Segal
 */
public class TutoringController implements Initializable {

    @FXML
    ListView<Tutor> tutorlist;
    @FXML
    ListView<Student> studentlist;
    @FXML
    TextArea display;
    
    static String tutorInfo(Tutor tutor) {
        return String.format(
             "id: %s\n"
           + "name: %s\n"
           + "email: %s\n"
           + "subjectID: %s\n",
           tutor.getId(),
           tutor.getName(),
           tutor.getEmail(),
           tutor.getSubjectId()
        );
    }
    static String studentInfo(Student student) {
        return String.format(
             "id: %s\n"
            + "name: %s\n"
            + "email: %s\n"
            + "enrolled: %s\n" ,
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getDateEnrolled()
        );
    }
 
    Collection<Integer> studentTutorIds = new HashSet<>(); 
    Collection<Integer> tutorStudentIds = new HashSet<>(); 
       
    public boolean nameSortStyle = true;

    @FXML
    void studentSelect(Event event) {
        Student student = studentlist.getSelectionModel().getSelectedItem();
        try {
            Collection<Interaction> interactions = ORM.findAll(Interaction.class, "where student_id=?", new Object[]{student.getId()});
            studentTutorIds.clear();
            for (Interaction interaction : interactions) {
                studentTutorIds.add(interaction.getTutorId());
            }
            tutorlist.refresh();
            display.setText(studentInfo(student));
        }
        catch (Exception ex) {
          ex.printStackTrace(System.err);
          System.exit(1);
        }
    }
    
    @FXML
    void tutorSelect(Event event) {
        Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
        try {
            Collection<Interaction> interactions = ORM.findAll(Interaction.class, "where tutor_id=?", new Object[]{tutor.getId()});
            tutorStudentIds.clear();
            for (Interaction interaction : interactions) {
                tutorStudentIds.add(interaction.getStudentId());
            }
            studentlist.refresh();
            display.setText(tutorInfo(tutor));
        }
        catch (Exception ex) {
          ex.printStackTrace(System.err);
          System.exit(1);
        }
    }
     
    @FXML
    void addStudentDialog(Event event) {
        try {
            URL fxml = getClass().getResource("AddStudentDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            fxmlLoader.load();

            AddStudentDialogController dialogController =  fxmlLoader.getController();

            Scene scene = new Scene(fxmlLoader.getRoot());

            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            dialogController.mainController = this;

            dialogStage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    @FXML
    void addTutorDialog(Event event) {
        try {
            URL fxml = getClass().getResource("AddTutorDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            fxmlLoader.load();

            AddTutorDialogController dialogController =  fxmlLoader.getController();

            Scene scene = new Scene(fxmlLoader.getRoot());

            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            dialogController.mainController = this;

            dialogStage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
        
    @FXML
    void addSubjectDialog(Event event) {
        try {
            URL fxml = getClass().getResource("AddSubjectDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            fxmlLoader.load();

            AddSubjectDialogController dialogController =  fxmlLoader.getController();

            Scene scene = new Scene(fxmlLoader.getRoot());
            
            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            dialogController.mainController = this;

            dialogStage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    @FXML
    void removeTutor(Event event) {
        Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
        try {
        if (tutor == null) {
            throw new ExpectedException("Must select tutor.");
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        Collection<Interaction> interactions = ORM.findAll(Interaction.class,
          "where tutor_id=?", new Object[]{tutor.getId()});
        if (! interactions.isEmpty() ) {
            for (Interaction interaction: interactions)
            {
                ORM.remove(interaction);
            }
        }
        ORM.remove(tutor);
        tutorlist.getItems().remove(tutor);

          Student student = studentlist.getSelectionModel().getSelectedItem();
        if (student != null) {
            display.setText(studentInfo(student));
        }
        else {
             tutorlist.getSelectionModel().clearSelection();
             display.setText("");
        }
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
    }

    @FXML
    void nameView(Event event) {
        List<Student> tempList;
        tempList = this.studentlist.getItems();
        Collections.sort(tempList, (Student o1, Student o2) -> o1.getName().compareTo(o2.getName()));
        this.studentlist.setItems((ObservableList<Student>) tempList);
        this.nameSortStyle = true;
    }
    
    @FXML
    void enrollView(Event event) {
      List<Student> tempList;
        tempList = this.studentlist.getItems();
        Collections.sort(tempList, (Student o1, Student o2) -> o1.getDateEnrolled().compareTo(o2.getDateEnrolled()));
        this.studentlist.setItems((ObservableList<Student>) tempList);
        this.nameSortStyle = false;
    }
  
    static java.sql.Date currentDate() {
        long now = new java.util.Date().getTime();
        java.sql.Date date = new java.sql.Date(now);
        return date;
      }
   
    @FXML
    void link(Event event) {
        Student student = studentlist.getSelectionModel().getSelectedItem();
        Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
        try {
            //if Student or Tutor are not selected
            if (student == null || tutor == null) {
              throw new ExpectedException("Must select student and tutor to link.");
            }  

            Interaction interaction = ORM.findOne(Interaction.class, 
                "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
            if (interaction != null) {
              throw new ExpectedException("Student and tutor are already linked.");
            }

            Collection<Tutor> tutors2 = ORM.findAll(Tutor.class, "where subject_id=?",
                    new Object[] {tutor.getSubjectId()});

            Interaction interaction2;
            for (Tutor t2: tutors2)
            {
                interaction2 = ORM.findOne(Interaction.class, 
                "where student_id=? and tutor_id=?", new Object[]{student.getId(), t2.getId()});
                if (interaction2 !=null){
                throw new ExpectedException("Student is already linked to a tutor for that subject.");
                }
            }
            interaction = new Interaction(tutor, student);
            ORM.store(interaction);
            ORM.store(tutor);
            studentTutorIds.add(tutor.getId());
            tutorStudentIds.add(student.getId());
            tutorlist.refresh();
            studentlist.refresh();
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
    }
    
    @FXML
    void viewReport(Event event) {
        Student student = studentlist.getSelectionModel().getSelectedItem();
        Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
        
        try {
            if (student == null || tutor == null) {
                
                throw new ExpectedException("Must select student and tutor.");  
            }
            Interaction interaction = ORM.findOne(Interaction.class, 
              "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
            if (interaction == null) {
                throw new ExpectedException("Selected student and tutor are not linked.");
            }
            display.setText("Student: " + student.getName() + "\n"
                    + "Tutor: " + tutor.getName() + "\n"
                    + "--Report-- \n"
                    + interaction.getReport());   
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
    }

    @FXML
    void editReport(Event event) {
        Student student = studentlist.getSelectionModel().getSelectedItem();
        Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
        try {
            if (student == null || tutor == null) {
                throw new ExpectedException("Must select student and tutor.");
            }
            Interaction interaction = ORM.findOne(Interaction.class, 
              "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
            if (interaction == null) {
                throw new ExpectedException("Selected student and tutor are not linked.");
            }
            
            URL fxml = getClass().getResource("ModifyReportDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            fxmlLoader.load();

            ModifyReportDialogController dialogController = fxmlLoader.getController();
            Scene scene = new Scene(fxmlLoader.getRoot());
            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogController.mainController = this;
            dialogController.setup();
            dialogStage.show();
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(final WindowEvent event)
                {
                    dialogController.cancel(event);
                }   
            });

        }
        catch (ExpectedException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
        }
        catch (Exception ex){
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    @FXML
    void clear(Event event) {
        studentlist.getSelectionModel().clearSelection();
        tutorlist.getSelectionModel().clearSelection();
        studentTutorIds.clear();
        tutorStudentIds.clear();
        studentlist.refresh();
        tutorlist.refresh();
        display.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StudentCellCallback studentCellFactory = new StudentCellCallback();
        studentlist.setCellFactory(studentCellFactory);
        TutorCellCallback tutorCellFactory = new TutorCellCallback();
        tutorlist.setCellFactory(tutorCellFactory);
        
        tutorCellFactory.tutorIds = studentTutorIds;
        studentCellFactory.studentIds = tutorStudentIds;
        
        try
        {
            ORM.init(DBProps.getProps());
            Collection<Tutor> tutors = ORM.findAll(Tutor.class);
            for (Tutor tutor : tutors) {
                tutorlist.getItems().add(tutor);
            }
            Collection<Student> students = ORM.findAll(Student.class);
            
            for (Student student : students) {
                studentlist.getItems().add(student);
            }
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        List<Student> tempList;
        tempList = this.studentlist.getItems();
        Collections.sort(tempList, (Student o1, Student o2) -> o1.getName().compareTo(o2.getName()));
        this.studentlist.setItems((ObservableList<Student>) tempList);
        List<Tutor> temptutList;
        temptutList = this.tutorlist.getItems();
        Collections.sort(temptutList, (Tutor o1, Tutor o2) -> o1.getName().compareTo(o2.getName()));
        this.tutorlist.setItems((ObservableList<Tutor>) temptutList);
    }
}
