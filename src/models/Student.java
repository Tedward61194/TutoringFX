package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Teddy Segal
 */

public final class Student extends Model {
    public static final String TABLE = "student";
    
    private int id = 0;
    private String name;
    private String email;
    private Date enrolled;
    
    public Student(){}
    public Student(String name, String email, Date enrolled) {
        this.name = name;
        this.email = email;
        this.enrolled = enrolled;
    }
    
    @Override
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Date getDateEnrolled() { return enrolled; }
    
    public void setName(String n) {
        this.name = n;
    }
    public void setEmail(String e) {
        this.email = e;
    }
    public void setDate(Date date) {
        this.enrolled = date;
    }
    
    @Override
    void load(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        name = rs.getString("name");
        email = rs.getString("email");
        enrolled = rs.getDate("enrolled");
    }
    
    @Override
    public void insert() throws SQLException {
        Connection cx = ORM.connection();
        String sql = String.format("insert into %s (name,email,enrolled) values (?,?,?)", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, name);
        st.setString(++i, email);
        st.setDate(++i, enrolled);
        st.executeUpdate();
        id = ORM.getMaxId(TABLE);
    }
    
    @Override
    void update() throws SQLException {
        Connection cx = ORM.connection();
        String sql = String.format(
          "update %s set name=?,email=?,enrolled=? where id=?", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, name);
        st.setString(++i, email);
        st.setDate(++i, enrolled);
        st.setInt(++i, id);
        st.executeUpdate();
      }
 
  @Override
  public String toString() {
    return String.format("(%s,%s,%s,%s)", id, name,email,enrolled);
  }
}