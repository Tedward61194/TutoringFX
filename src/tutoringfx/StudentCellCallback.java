package tutoringfx;

import java.util.Collection;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Student;

/**
 *
 * @author Teddy Segal
 */
public class StudentCellCallback implements Callback<ListView<Student>, ListCell<Student>> {  
  
    Collection<Integer> studentIds;
    
    @Override
    public ListCell<Student> call(ListView<Student> p) {
        ListCell<Student> cell = new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty) {
                    this.setText(null);
                    return;
                }
                
                this.setText(student.getName() + " | " + student.getDateEnrolled());

                String css = "-fx-text-fill:#606; -fx-font-weight:bold;";
                if (studentIds.contains(student.getId())) {
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