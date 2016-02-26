package tutoringfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Interaction;
import models.ORM;
import models.Student;
import models.Tutor;

/**
 * ModifyReportDialogController class
 *
 * @author Teddy Segal
 */
public class ModifyReportDialogController implements Initializable {

    @FXML
    Node top;
    TutoringController mainController;
    
    @FXML
    Label studentName;
    @FXML
    Label tutorName;
    @FXML
    TextArea newReportArea;
    
    String oldReport="";
    String newReport="";
    Boolean newTextEntered;

    @FXML
    void setup() {

        Student student = mainController.studentlist.getSelectionModel().getSelectedItem();
        studentName.setText("Student: \t" + student.getName());
        Tutor tutor = mainController.tutorlist.getSelectionModel().getSelectedItem();
        tutorName.setText("Tutor: \t" + tutor.getName());
        
        try{
            Interaction interaction = ORM.findOne(Interaction.class, "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
            oldReport = interaction.getReport();
            newReportArea.setText(oldReport);
        }
        catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    @FXML
    void modify(Event event) {
        Student student = mainController.studentlist.getSelectionModel().getSelectedItem();
        Tutor tutor = mainController.tutorlist.getSelectionModel().getSelectedItem();
        Interaction interaction = new Interaction();
        try{
            interaction = ORM.findOne(Interaction.class, "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
            newReport = newReportArea.getText();
            interaction.setReport(newReport);
            interaction.update();
        }
        catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        } 
        mainController.display.setText("Student: " + student.getName() + "\n"
                    + "Tutor: " + tutor.getName() + "\n"
                    + "--Report-- \n"
                    + interaction.getReport());
        
        top.getScene().getWindow().hide();
    }
    
    @FXML
    void cancel(Event event) {
        newReport = newReportArea.getText();
        if (oldReport.equals(newReport))
        {
            top.getScene().getWindow().hide();
        }
        else
        {
            try {
                URL fxml = getClass().getResource("CancelConfirmationDialog.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(fxml);
                fxmlLoader.load();

                CancelConfirmationDialogController dialogController =  fxmlLoader.getController();

                Scene scene = new Scene(fxmlLoader.getRoot());
                Stage dialogStage = new Stage();
                dialogStage.setScene(scene);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogController.mainController = this;
                dialogController.setup();
                dialogStage.show();
            }
            catch (IOException ex) {
                ex.printStackTrace(System.err);
                System.exit(1);
            } 

        }
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
