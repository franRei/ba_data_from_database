import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.sql.*;

public class Database_wiki {
    public static void open_Database(String word_to_search) {
        String user = "franzi";
        String pw = "password";
        String tablename = "wiki";

        String esquel = "SELECT * FROM " + tablename +  " WHERE wiki_word = " + word_to_search;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proto?useUnicode=true&characterEncoding=UTF-8&useSSL=false&" +
                    "user=" + user + "&password=" + pw);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(esquel);

            while (rs.next()) {
                String r = rs.getString("wiki_word");
                String s = rs.getString("wiki_description");
                System.out.println("in wiki: " + r);
            }
            st.close();
            conn.close();
        }
        catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
        catch (SQLException ex)           {System.err.println(ex.getMessage());}
        finally {

        }
    }
}
