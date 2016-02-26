package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Teddy Segal
 */
public class Subject extends Model
{
    public static final String TABLE = "subject";
    
    private int id = 0;
    private String name;
    
    public Subject(){} {
    }
    public Subject (String name) {
        this.name = name;
    }
    
    @Override
    public int getId() { return id; }
    public String getName() { return name; }
   
    public void setName(String n) {
        this.name = n;
    }
    
    @Override
    void load(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        name = rs.getString("name");
    }
    
    @Override
    public void insert() throws SQLException
    {
        Connection cx = ORM.connection();
        String sql = String.format("insert into %s (name) values (?)", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, name);
        st.executeUpdate();
        id = ORM.getMaxId(TABLE);
    }
    
    @Override
    void update() throws SQLException {
        Connection cx = ORM.connection();
        String sql = String.format(
          "update %s set name=? where id=?", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, name);
        st.setInt(++i, id);
        st.executeUpdate();
    }
 
  @Override
  public String toString() {
    return String.format("(%s,%s)", id, name);
  }
}