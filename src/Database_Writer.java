import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.sql.*;

public class Database_Writer {


    public static void open_Database() {
        String user = "franzi";
        String pw = "password";
        int synEx = 0;
        String tablename = "wiki";


//        System.out.println(description);
/*
        String esquel = "INSERT INTO " + tablename + " ( wiki_word, wiki_description ) "
                + " VALUES ( '" + word +  "','" + description + "');";*/
        String esquel = "SELECT * FROM proto_germanic";



        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proto?useSSL=false&" +
                    "user=" + user + "&password=" + pw);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(esquel);

            while (rs.next()) {
                String r = rs.getString("g_word");
                String s = rs.getString("g_description");
                System.out.println(s);
            }
            st.close();
            conn.close();
        }
        catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
       // catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
        //catch (InstantiationException ex) {System.err.println(ex.getMessage());}
        catch (MySQLSyntaxErrorException e) {
            System.out.println("SyntaxException: " + synEx++);
        }
        catch (SQLException ex)           {System.err.println(ex.getMessage());}
        finally {

        }
    }

}
