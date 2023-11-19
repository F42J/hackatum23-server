import java.sql.*;

public class Database {
    //CREATE TABLE Queries
    String CREATEUSERS = "CREATE TABLE IF NOT EXISTS users (" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", name VARCHAR(20)" +
            ", bio VARCHAR(5000)" +
            ", image BLOB" +
            ", token VARCHAR" +
            ", verified INTEGER)";

    String CREATESKILLS = "CREATE TABLE IF NOT EXISTS skills (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", description VARCHAR(100))";

    String CREATESKILL2USER = "CREATE TABLE IF NOT EXISTS skill2user (" +
            "userid INTEGER" +
            ", skillid INTEGER)";
    String CREATESKILL2INNOVATION = "CREATE TABLE IF NOT EXISTS skill2innovation (" +
            "innovationid INTEGER" +
            ", skillid INTEGER)";


    String CREATEINNOVATIONS = "CREATE TABLE IF NOT EXISTS innovations (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", title VARCHAR(100)" +
            ", description VARCHAR(5000)" +
            ", score INTEGER" +
            ", innovator INTEGER" +
            ", searching INTEGER" +
            ", creation INTEGER)";
    //REQUEST QUERIES
    String FETCHPROFILE = "SELECT " +
            "u.id,u.name,u.bio,u.verified, s.description " +
            "FROM " +
            "users u" +
            ",skills s" +
            ",skill2user s2u " +
            "WHERE " +
            "u.id=s2u.userid " +
            "AND s.id=s2u.skillid " +
            "AND u.id=?";
    PreparedStatement prepFetchProfile;
    String FETCHINNOVATION = "SELECT i.id,i.title,i.description,s.description as skill " +
            "FROM " +
            "innovations i" +
            ", skills s" +
            ", skill2innovation s2i " +
            "WHERE i.id=s2i.innovationid " +
            "AND s.id=s2i.skillid " +
            "AND i.id=?";
    PreparedStatement prepFetchInnovation;

    Connection conn;

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\J42F\\IdeaProjects\\Hackatum\\identifier.sqlite");
            var stmt = conn.createStatement();
            stmt.execute(CREATEUSERS);
            stmt.execute(CREATEINNOVATIONS);
            stmt.execute(CREATESKILLS);
            stmt.execute(CREATESKILL2USER);
            stmt.execute(CREATESKILL2INNOVATION);
            prepFetchProfile=conn.prepareStatement(FETCHPROFILE);
            prepFetchInnovation = conn.prepareStatement(FETCHINNOVATION);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't load database. Exisiting");
            System.exit(1);
        }
    }

    public ResultSet fetchProfile(String id) {
        try {
            prepFetchProfile.setInt(1,Integer.parseInt(id));
            boolean sucess=prepFetchProfile.execute();
            if (!sucess) return null;
            return prepFetchProfile.getResultSet();
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet fetchInnovation(String id) {
        try {
            prepFetchInnovation.setInt(1,Integer.parseInt(id));
            boolean sucess=prepFetchProfile.execute();
            if (!sucess) return null;
            return prepFetchInnovation.getResultSet();
        } catch (SQLException e) {
            return null;
        }
    }

}
