package tutoringfx;

import java.util.Collection;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.ORM;
import models.Subject;
import models.Tutor;

/**
 *
 * @author Teddy Segal
 */
public class TutorCellCallback implements Callback<ListView<Tutor>, ListCell<Tutor>> {  
    
    Collection<Integer> tutorIds;
    
    @Override
    public ListCell<Tutor> call(ListView<Tutor> p) {
        ListCell<Tutor> cell;
        cell = new ListCell<Tutor>() {
            @Override
            protected void updateItem(Tutor tutor, boolean empty) {
                super.updateItem(tutor, empty);
                if (empty) {
                    this.setText(null);
                    return;
                }
                
                Subject subjectWithId = new Subject();
                try {
                    subjectWithId = ORM.findOne(Subject.class, "where id=?", new Object[]{tutor.getSubjectId()});
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
                
                this.setText(tutor.getName() + " | " + subjectWithId.getName());
                
                //highlights appropriate tutors when appropriate student selected
                String css = "-fx-text-fill:#606; -fx-font-weight:bold;";
                if (tutorIds.contains(tutor.getId())) {
                    this.setStyle(css);
                }
                else {
                    this.setStyle(null);
                }
            }
        };
      return cell;
    }
  }