import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.io.IOError;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescendantsParser {
    private ArrayList<DescTree> results;
    private Connection conn;
    private Logger logger;

    public DescendantsParser() {
        results = new ArrayList<DescTree>();
        String user = "franzi";
        String pw = "password";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proto?useUnicode=true&characterEncoding=UTF-8&useSSL=false&" +
                    "user=" + user + "&password=" + pw);
            logger = new Logger("log.txt");
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
        catch (ClassNotFoundException ex){
            System.err.println(ex.getMessage());
        }
    }

    public ArrayList<DescTree> getResults(){
        return results;
    }

    public void run() throws IOException{
        int count=1;
        String query_proto_germanic = "SELECT * FROM proto_germanic_01_01 order by g_word limit 100,100";
        //String query_proto_germanic = "SELECT * FROM proto_germanic_01_01";
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query_proto_germanic);
            while (rs.next()) {
                try {
                    System.out.print(count + " ");
                    count++;
                    DescTree result = parseDescendants(rs.getString("g_word"), "pro-g", rs.getString("g_description"));
                    results.add(result);
                }
                catch (SyntaxError ex){
                    System.err.println(ex.getMessage());
                }

            }
        }
        catch (SQLException ex) {
            System.err.println("SQL Exception in run:");
            System.err.println(ex.getMessage());
        }
    }

    public static String normalize(String str){
        Map<Character,String> replacements = new HashMap<Character,String>();
        //replacements.put('*',"");
        replacements.put('ī',"i");
        replacements.put('ġ', "g");
        replacements.put('ǣ',"æ");
        replacements.put('ā',"a");
        replacements.put('ō', "o");
        replacements.put('ē', "e");
        replacements.put('ë', "e");
        replacements.put('ē', "e");
        replacements.put('ċ', "c");
        replacements.put('ô',"o");
        replacements.put('ê',"e");
        replacements.put('ū', "u");
        replacements.put('î',"i");
        replacements.put('â',"a");
        replacements.put('ȳ',"y");


        StringBuilder result = new StringBuilder();
        for (int i=0; i<str.length(); i++){
            if(replacements.containsKey(str.charAt(i))) {
                result.append(replacements.get(str.charAt(i)));
            }
            else{
                result.append(str.charAt(i));
            }
        }
        return result.toString();
    }

    public static String getLanguage(String str){
        Map<String,String> languages = new HashMap<String,String>();
        languages.put("osx", "Old Saxon");
        languages.put("ang", "Old English");
        languages.put("da","Danish");
        languages.put("de", "German");
        languages.put("dum", "Middle Dutch");
        languages.put("en", "English");
        languages.put("enm", "Middle English");
        languages.put("es", "Spanish");
        languages.put("frk", "Frankish");
        languages.put("gmh", "Middle High German");
        languages.put("gml", "Middle Low German");
        languages.put("gmq-oda", "Old Danish");
        languages.put("gmq-osw", "Old Swedish");
        languages.put("gmw-pro", "Proto-West Germanic");
        languages.put("goh", "Old High German");
        languages.put("got", "Gothic");
        languages.put("la", "Latin");
        languages.put("nl", "Dutch");
        languages.put("non", "Old Norse");
        languages.put("odt", "Old Dutch");
        languages.put("gmq-pro", "Proto-Norse");
        languages.put("sv", "Swedish");
        languages.put("sw", "Swedish");
        languages.put("ofs", "Old Frisian");
        languages.put("fro", "Old French");
        languages.put("ru", "Russian");
        languages.put("af", "Afrikaans");
        languages.put("nn", "Norwegian Nynorsk");
        languages.put("nb", "Norwegian Bokmål");
        languages.put("no", "Norwegian");
        languages.put("nds-de", "German Low Saxon");
        languages.put("nds-nl", "Dutch Low Saxon");
        languages.put("frm", "Middle French");
        languages.put("fiu-fin-pro", "Proto-Finnic");
        languages.put("yi", "Yiddish");
        languages.put("vec", "Venetian");
        languages.put("non-own", "Old West Nordic");
        languages.put("it", "Italian");
        languages.put("fr", "French");
        languages.put("gmw-cfr", "Cental Franconian");
        languages.put("pap", "Papiamentu");
        languages.put("srn", "Sranan Tongo");
        languages.put("fi", "Finnish");
        languages.put("hrx", "Hunsrik");
        languages.put("axm", "Middle Armenian");
        languages.put("ja", "Japanese");
        languages.put("frr", "North Frisian");
        languages.put("nrn", "Norn");
        languages.put("gmw-ecg", "East Central German");
        languages.put("pdc", "Pennsylvania German");
        languages.put("smi-pro", "Proto-Samic");
        languages.put("gsw", "Alemannic German");
        languages.put("li", "Limburgish");
        languages.put("fo", "Faroese");
        languages.put("bar", "Bavarian");
        languages.put("gnh", "Lere");
        languages.put("swg", "Swabian");
        languages.put("gmq-gut", "Gutnish");
        languages.put("gmw-rfr", "Rhine Franconian");

        languages.put("en-geo", "language not found in wik_list (en-geo)");
        languages.put("non-ogt", "language not found in wik_list (non-ogt)");
        languages.put("pfl", "language not found in wik_list (pfl)");
        languages.put("sli", "language not found in wik_list (sli)");
        languages.put("ksh", "language not found in wik_list (ksh)");
        languages.put("sxu", "language not found in wik_list (sxu)");
        languages.put("ML.", "language not found in wik_list (ML.)");
        languages.put("VL.", "language not found in wik_list (VL.)");
        languages.put("lng", "language not found in wik_list (lng)");
        if(languages.containsKey(str)){
            return languages.get(str);
        }
        else{
            throw new SyntaxError("Language not found: "+str);
        }
    }

    public static String getReconstructionTitle(String word, String language){
        String language_long = getLanguage(language);
        return "Reconstruction:"+language_long+"/"+word;
    }

    public static String findTitle(String word, String language){

        if(word.charAt(0)=='*'){
            return getReconstructionTitle(word.replace("*",""), language);
        }
        else{
            return normalize(word);
        }
    }

    public DescTree parseDescendants(String word, String language, String descendants) throws IOException{
        System.out.println("Word: "+ word + ", language: "+ language);
        try{
            DescTree result = new DescTree(word, language);
            ArrayList<DescTree> levels = new ArrayList<DescTree>();
            levels.add(result);
            for (String line : descendants.split("\n")) {
                if(line.equals("")){
                    continue;
                }
                String[] firstSplit = line.split(" ",2);
                if(firstSplit.length<2){
                    warn("Warning: in word "+ word + ", ignoring line: " + line + ", firstSplit too short (so no whitespace)");
                    continue;
                }

                int level = firstSplit[0].length();

                boolean ignoreline=false;
                for(int i=0; i<level; i++){
                    if(firstSplit[0].charAt(i) != '*' && firstSplit[0].charAt(i) != ':'){

                        warn("Warning: in word "+ word + ", ignoring line: " + line + ", line does not begin with just '*'s or ':'s.");
                        ignoreline=true;
                        break;
                    }
                }
                if(ignoreline) continue;

                String[] secondSplit = firstSplit[1].split(", ");

               Pattern template = Pattern.compile("\\{\\{([^|]*)\\|([^|]*)\\|([^}|]*)(?:\\}\\}|\\|([^}|]*)\\}\\})?");
               // Pattern template = Pattern.compile("\\{\\{([^|]*)\\|([^|]*)\\|([^}|]*)(?:\\}\\}|\\|([^}|]*)\\}\\})");
                Matcher matcher = template.matcher(secondSplit[0]);

                if(matcher.find()){
                    String templateTag = matcher.group(1);
                    String language_child = "";
                    String word_child = "";

                    if(matcher.group(4) != null && matcher.group(2).startsWith("bor")) {
                        language_child = matcher.group(3);
                        word_child = matcher.group(4);
                    } else if (matcher.group(4) != null && matcher.group(2).startsWith("der")) {
                        language_child = matcher.group(3);
                        word_child = matcher.group(4);
                    } else {
                        language_child = matcher.group(2);
                        word_child = matcher.group(3);
                    }

                    // here, matcher.group(4) is null if we matched something of the form {{desc|en|bla}}, and is set to the fourth group if {{desc|en|bla|blubb}}.
                    // can use if(matcher.group(4) != null){...} to process this additional info below.
                    DescTree child = new DescTree(word_child, language_child);

                    if((matcher.group(4) != null && matcher.group(4).startsWith("bor")) || matcher.group(3).startsWith("bor")){
                        child.setBorrowed();
                    }

                    if(templateTag.equals("desctree")){
                        child = parseDescTree(word_child, language_child);
                    }
                    else if(templateTag.equals("desc") || templateTag.equals("l")){
                        ignoreline = false;
                        for (int i = 1; i < secondSplit.length; i++) {
                            Matcher matcher2 = template.matcher(secondSplit[i]);
                            if (matcher2.find()) {
                                child.words.add(matcher2.group(3));
                                //
                                if(matcher2.group(1).equals("desctree")) {
                                    child = parseDescTree(word_child, language_child);
                                }

                                if (!matcher2.group(1).equals("l")) {
                                    warn("Warning: in word "+ word + ", ignoring line: " + line + ", further template not link");
                                    ignoreline=true;
                                    break;
                                }
                                if (!matcher2.group(2).equals(language_child)){
                                    if(!matcher2.group(2).equals("nb") || !matcher2.group(2).equals("nn")){//||!matcher2.group(3).equals(language_child)) {
                                    warn("Warning: in word "+ word + ", ignoring line: " + line + ", differing languages in list");
                                    System.out.println(language_child + (", ") + matcher2.group(2)  + (", ") +  matcher2.group(3));
                                    ignoreline=true;
                                    break;
                                }}
                            } else {
                                warn("Warning: in word "+ word + ", ignoring line: " + line +", final case in desc template handler");
                            }
                        }
                        if(ignoreline) continue;
                    }
                    else{
                        warn("Warning: in word "+ word + ", ignoring line: " + line+ ", unexpected template:" + templateTag);
                    }


                    if(levels.size()<level){
                        warn("In word "+word+", problematic line: " + line + ", problem with tree levels. Filling in empty desctree nodes. Stars: "+firstSplit[0]+", raw description string:\n" + descendants + "\n\n");
                        for(int i=levels.size(); i<level; i++){
                            DescTree emptynode = new DescTree("ERROR IN "+word,language,true);
                            levels.get(i-1).addChild(emptynode);
                            levels.add(emptynode);
                        }
                    }
                    levels.get(level - 1).addChild(child);
                    if (levels.size() < level + 1) {
                        levels.add(child);
                    } else {
                        levels.set(level, child);
                    }
                }
                else{
                    warn("Warning: In word "+ word + ", ignoring line: " +line+", but adding dummy node so the tree connects correctly (for example for 'Westphalian' line that appears often.");
                    if(levels.size()<level){
                        for(int i=levels.size(); i<level; i++){
                            DescTree emptynode = new DescTree("ERROR IN "+word,language,true);
                            levels.get(i-1).addChild(emptynode);
                            levels.add(emptynode);
                        }
                    }
                    DescTree child = new DescTree("ERROR IN "+word,language,true);
                    levels.get(level - 1).addChild(child);
                    if (levels.size() < level + 1) {
                        levels.add(child);
                    } else {
                        levels.set(level, child);
                    }
                    continue;
                }
            }
            return result;
        }
        catch(SyntaxError ex){
            logger.log(ex.getMessage());
            return new DescTree("ERROR IN "+word,language,true);
        }
    }

    public DescTree parseDescTree(String raw_word, String language) throws IOException{
        System.out.println("Word: "+ raw_word+ ", language: " + language);
        String word = findTitle(raw_word, language);
        try{
            String query_allwords = "SELECT * FROM wiki_db_13_01 WHERE wiki_word = '" + word + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query_allwords);

            //hier enthält ResultSet manchmal nichts (z.B. wenn wir das ascii mapping verbockt haben), in dem Fall loggen.
            //Manchmal enthält es auch zu viele Einträge, weil SQL selbst scheinbar nicht case-sensitive sucht und selbst irgendwie zu ascii normalisiert.
            boolean found = false;
            String description="";
            ArrayList<String> words = new ArrayList<String>();
            while(rs.next()){
                words.add(rs.getString("wiki_word"));
                if(word.equals(rs.getString("wiki_word"))){
                    found=true;
                    description = rs.getString("wiki_description");
                }
            }
            if(!found){
                error("Not found in Database wiki_db: "+raw_word + " (raw), tried: " + word + " (normalized). Found instead: "+words.toString() + ". Maybe need to add another normalization rule.");
            }
            else{
                String[] cut1 = description.split("=="+getLanguage(language)+"==\n",2);
                if(cut1.length<2){
                    error("Syntax Error, word: "+raw_word+", did not find language "+getLanguage(language)+ " in description string.");
                }
                String languagesection = cut1[1].split("----",2)[0];
                String[] cut3 = languagesection.split("====Descendants====\n",2);
                if(cut3.length<2){
                    //no descendants
                    return new DescTree(raw_word,language);
                }
                String[] cut4 = cut3[1].split("\n\n",2);
                String descendants = "";
                if(!cut4[0].contains("{{")) {
                    descendants = cut4[1];
                    System.out.println(descendants);
                } else {
                    descendants = cut4[0];
                }
                //// Seems ok:
//                if(cut4.length<2){
//                    throw new SyntaxError("Syntax Error, word: "+raw_word+", language "+ getLanguage(language) +": descendants section did not end with empty line.\n"+cut3[1]+"\n\n");
//                }

                DescTree result = parseDescendants(raw_word, language, descendants);
                return result;
            }
        }
        catch (SQLException ex) {
            System.err.println("SQL Exception in parseDescTree:");
            System.err.println(ex.getMessage());
        }
        catch (SyntaxError ex){
            logger.log(ex.getMessage());
            return new DescTree(raw_word, "ERROR");
        }
        throw new SyntaxError("Something went wrong in word " + raw_word + ", Language: "+ language +". (parseDescTree did not return, this should NOT happen!!)");
    }

    private void error(String str) throws SyntaxError{
        throw new SyntaxError(str);
    }

    private void warn(String str) throws IOException{
        logger.log(str);
    }
}
