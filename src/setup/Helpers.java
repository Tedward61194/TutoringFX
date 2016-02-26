package setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import models.DBProps;

class Helpers {

  private static final String SUBJECT = "subject";
  private static final String STUDENT = "student";
  private static final String TUTOR = "tutor";
  private static final String INTERACTION = "interaction";

  public static void createTables(Properties props) throws 
    IOException, SQLException, ClassNotFoundException {

    String url = props.getProperty("url");
    String username = props.getProperty("username");
    String password = props.getProperty("password");
    String driver = props.getProperty("driver");
    if (driver != null) {
      Class.forName(driver); // load driver if necessary
    }
    Connection cx = DriverManager.getConnection(url, username, password);

    Statement stmt = cx.createStatement();

    System.out.format("\n---- drop tables\n");
    for (String table : new String[]{INTERACTION, STUDENT, TUTOR, SUBJECT,}) {
      String sql = String.format("drop table if exists %s", table);
      System.out.println(sql);
      stmt.execute(sql);
    }

    System.out.format("\n---- create tables\n");
    for (String table : new String[]{SUBJECT, STUDENT, TUTOR, INTERACTION,}) {
      String filename = String.format("tables/%s-%s.sql", table, DBProps.which);
      String sql = getResourceContent(filename).trim();
      System.out.println(sql);
      stmt.execute(sql);
    }
  }

  static void populateTables(Properties props) throws SQLException, ClassNotFoundException {
    String url = props.getProperty("url");
    String username = props.getProperty("username");
    String password = props.getProperty("password");
    String driver = props.getProperty("driver");
    if (driver != null) {
      Class.forName(driver); // load driver if necessary
    }
    Connection cx = DriverManager.getConnection(url, username, password);
    PreparedStatement stmt;

    // these are for internal use to correspond names to table id values
    Map<String, Integer> subjectId = new HashMap<>();
    Map<String, Integer> tutorId = new HashMap<>();
    Map<String, Integer> studentId = new HashMap<>();
    int id;

    //========================================================
    System.out.println("\n--- add subjects");
    
    stmt = cx.prepareStatement(
      String.format("insert into %s (name) values(?)", SUBJECT)
    );
    
    String subjects[] = {"Chemistry", "Math", "Physics", "Biology"};

    id = 0;
    for (String subject : subjects) {
      System.out.println(subject);
      stmt.setString(1, subject);
      stmt.execute();
      subjectId.put(subject, ++id);
    }
    
    //========================================================
    System.out.println("\n--- add tutors");

    stmt = cx.prepareStatement(
      String.format("insert into %s (name,email,subject_id) values(?,?,?)", TUTOR)
    );
    
    Object tutors[][] = new Object[][]{
      new Object[]{"Guo,Cheryl", "guo33@yahoo.com", subjectId.get("Chemistry")}, 
      new Object[]{"Howse,Manuel", "mahow@hotmail.com", subjectId.get("Math")}, 
      new Object[]{"Lanier,Jennifer", "lanije@outlook.com", subjectId.get("Math")}, 
      new Object[]{"Sippel,John", "sipp45@live.com", subjectId.get("Physics")}, 
      new Object[]{"Brady,Chad", "chadbrad@aol.com", subjectId.get("Biology")}, 
      new Object[]{"Mazer,Dominique", "domaz11@juno.com", subjectId.get("Physics")}, 
      new Object[]{"Wooding,Bernard", "berwood@yandex.com", subjectId.get("Math")}, 
    };

    id = 0;
    for (Object[] triple : tutors) {
      stmt.setString(1, (String) triple[0]);
      stmt.setString(2, (String) triple[1]);
      stmt.setInt(3, (Integer) triple[2]);
      stmt.execute();
      
      System.out.println(Arrays.toString(triple));
      tutorId.put((String) triple[0], ++id);
    }

    //========================================================
    System.out.println("\n--- add students");

    stmt = cx.prepareStatement(
      String.format("insert into %s (name,enrolled,email) values(?,?,?)", STUDENT)
    );
    String students[][] = new String[][]{
      //new String[]{"Nimmo,Penney", "2010-08-18", "penim@gmail.com"},
      new String[]{"Carson,Susy", "2012-07-28","susy.carson@aol.com"},
      new String[]{"Bard,Thomas", "2014-12-28","the.bard@bitmail.com"},
      new String[]{"Mcgillis,Alysa", "2010-07-20","amcg@postpro.net"},
      new String[]{"Carson,James", "2011-11-21","jcarson@gmail.com"},
      new String[]{"Liggett,James", "2013-01-08","liggett@usa.net"},
      new String[]{"Rothman,Alonso", "2014-11-21","roth@yahoo.com"},
      new String[]{"Collier,Rosanne", "2013-07-12","r.collier@yahoo.com"},
      new String[]{"Farney,Tommy", "2013-03-25","tfarney@studentcenter.org"},
      new String[]{"Maddock,Amal", "2013-11-13","amal@yahoo.com"},
    };

    id = 0;
    for (String[] triple : students) {
      stmt.setString(1, triple[0]);
      stmt.setDate(2, Date.valueOf(triple[1]));
      stmt.setString(3, triple[2]);
      stmt.execute();
      
      System.out.println(Arrays.toString(triple));
      studentId.put(triple[0], ++id);
    }

    //========================================================
    System.out.println("\n--- add interactions");
    
    stmt = cx.prepareStatement(
      String.format("insert into %s (tutor_id,student_id,report) values(?,?,?)", INTERACTION));
    
    String interactions[][] = new String[][]{
      new String[]{"Lanier,Jennifer", "Collier,Rosanne", 
      "Most likely a hopeless case.\n"
      + "Student wants help with pre-calc but can't even\n"
      + "solve an equation with one variable!"
      },
      new String[]{"Guo,Cheryl", "Carson,James", 
      "We went through the Periodic table.\n"
      + "Struggling with basic things like the significance of H-2-O."
      },
      new String[]{"Sippel,John", "Collier,Rosanne", ""},
      new String[]{"Guo,Cheryl", "Farney,Tommy", ""},
    };
    
    for (String[] triple : interactions) {
      String tutor = triple[0];
      String student = triple[1];
      String report = triple[2];
      stmt.setInt(1, tutorId.get(tutor));
      stmt.setInt(2, studentId.get(student));
      stmt.setString(3, report);
      stmt.execute();
      
      System.out.format("*** %s <=> %s ***\n%s\n\n", tutor, student, report);
    }
  }

  // support functions
  static String getResourceContent(String filename) throws IOException {
    StringBuilder content = new StringBuilder("");
    InputStream istr = CreateTables.class.getResourceAsStream(filename);
    InputStreamReader irdr = new InputStreamReader(istr);
    BufferedReader brdr = new BufferedReader(irdr);
    String line;
    while ((line = brdr.readLine()) != null) {
      content.append(line + "\n");
    }
    brdr.close();
    return content.toString();
  }
}
