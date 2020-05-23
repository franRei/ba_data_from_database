import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.sql.*;
import java.util.ArrayList;

public class Database_pgermanic {
        public static ArrayList<String> open_Database() {
            String user = "user_name_here";
            String pw = "password_here";
            int synEx = 0;
            int count_this = 0;
            Searcher search = new Searcher();
            ArrayList<String> data = new ArrayList<>();
            //table name here
            String tablename = "proto_germanic_01_01";
            Database_wiki wiki_search = new Database_wiki();

            String esquel = "SELECT * FROM " + tablename;
            int another_count = 0;

            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proto?useUnicode=true&characterEncoding=UTF-8&useSSL=false&" +
                        "user=" + user + "&password=" + pw);

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(esquel);

                while (rs.next()) {

                    String r = rs.getString("g_word");
                    String s = rs.getString("g_description");
                    data.add(r);
                    System.out.println(r);
                    String subDesctree= search.search_description(s);
                    if(subDesctree!=""){

                        Statement st2 = conn.createStatement();
                        ResultSet rs2 = st2.executeQuery("SELECT * FROM wiki_db WHERE wiki_word LIKE '" + subDesctree +"';");
                        while (rs2.next()){
                            String word = rs2.getString("wiki_word");
                            if(word.length() == subDesctree.length()) {
                                System.out.println(rs2.getString("wiki_word"));
                                String next_description = rs2.getString("wiki_description");
                                //System.out.println(next_description);
                                String helfer = search.search_for_Links(rs2.getString("wiki_word"), next_description, subDesctree);
                                //System.out.println("HELFER: " + helfer);
                                if (!helfer.equals("false")) {
                                    another_count++;
                                }
                            }
                        }
                    }
                }
                st.close();
                conn.close();
            }
            catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
            catch (MySQLSyntaxErrorException e) {
                System.out.println("SyntaxException: " + synEx++);
            }
            catch (SQLException ex)           {System.err.println(ex.getMessage());}
            finally {
                System.out.println(another_count);
            }
            return data;
        }
}

