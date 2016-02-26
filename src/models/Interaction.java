package models;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
/**
 *
 * @author Teddy Segal
 */

public final class Interaction extends Model {
 
  public static final String TABLE = "interaction";
 
  private int id = 0;
  private int student_id;
  private int tutor_id;
  private String report = "";
 
  public Interaction() {} 
  public Interaction(Student student, Tutor tutor) {
    this.student_id = student.getId();
    this.tutor_id = tutor.getId();
  }
 
  public Interaction(Student student, Tutor tutor, String report) {
    this.student_id = student.getId();
    this.tutor_id = tutor.getId();
    this.report = report;
  }
 
  public Interaction(Tutor tutor, Student student) {
    this.student_id = student.getId();
    this.tutor_id = tutor.getId();
  }
 
  public Interaction(Tutor tutor, Student student, String report) {
    this.student_id = student.getId();
    this.tutor_id = tutor.getId();
    this.report = report;
  }
 
  @Override
  public int getId() { return id;}
 
  public int getTutorId() { return tutor_id; }
  public int getStudentId() { return student_id; }
  public String getReport() { 
      if (report == null)
          return "";
      return report; 
  }
  
  public void setReport(String r){
      this.report = r;
  }

  @Override
  void load(ResultSet rs) throws SQLException {
    id = rs.getInt("id");
    tutor_id = rs.getInt("tutor_id");
    student_id = rs.getInt("student_id");
    report = rs.getString("report");
  }
 
  @Override
  void insert() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "insert into %s (tutor_id,student_id,report) values (?,?,?)", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setInt(++i, tutor_id);
    st.setInt(++i, student_id);
    st.setString(++i, report);
    st.executeUpdate();
    id = ORM.getMaxId(TABLE);
  }
 
  @Override
  public void update() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "update %s set report=? where id=?", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setString(++i, report);
    st.setInt(++i, id);
    st.executeUpdate();
  }
 
  @Override
  public String toString() {
    return String.format("(%s,%s,%s,%s)", id, tutor_id, student_id, report);
  }
}