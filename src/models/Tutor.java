package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Teddy Segal
 */

public final class Tutor extends Model
{
    public static final String TABLE = "tutor";
    
    private int id = 0;
    private String name;
    private String email;
    private int subject_id = 0;
    
    public Tutor(){}
    public Tutor(String name, String email, int subject_id)
    {
        this.name = name;
        this.email = email;
        this.subject_id = subject_id;
    }
    public Tutor(int id, String name, String email, int subject_id){
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject_id = subject_id;
        
    }
    
    @Override
    public int getId(){ return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getSubjectId() { return subject_id; }
    
    public void setId(int i) {
        this.id = i;
    }
    public void setName(String n) {
        this.name = n;
    }
    public void setEmail(String e) {
        this.email = e;
    }
    public void setSubjectId(int subject_id) {
        this.subject_id = subject_id;
    }
    
    @Override
    void load(ResultSet rs) throws SQLException
    {
        id = rs.getInt("id");
        name = rs.getString("name");
        email = rs.getString("email");
        subject_id = rs.getInt("subject_id");
    }
    
    @Override
    public void insert() throws SQLException
    {
        Connection cx = ORM.connection();
        String sql = String.format("insert into %s (name,email, subject_id) values (?,?,?)", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, name);
        st.setString(++i, email);
        st.setInt(++i, subject_id);
        st.executeUpdate();
        id = ORM.getMaxId(TABLE);
    }
    
    @Override
    void update() throws SQLException {
        Connection cx = ORM.connection();
        String sql = String.format(
          "update %s set name=?,email=?,subject_id=? where id=?", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, name);
        st.setString(++i, email);
        st.setInt(++i, subject_id);
        st.setInt(++i, id);
        st.executeUpdate();
      }
 
  @Override
  public String toString() {
    return String.format("(%s,%s,%s,%s)", id, name,email,subject_id);
  }
}