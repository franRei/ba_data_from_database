import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.io.IOError;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescendantsParser {
    private ArrayList<DescTree> results;
    private Connection conn;
    private Logger logger;

    public DescendantsParser() {
        results = new ArrayList<DescTree>();
        String user = "user_name_here";
        String pw = "password_here";
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
        String query_proto_germanic = "SELECT * FROM proto_germanic_01_01 order by g_word";
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query_proto_germanic);
            while (rs.next()) {
                try {
                    System.out.print(count + " ");
                    count++;
                    DescTree result = parseDescendants(rs.getString("g_word"), "pro-g", rs.getString("g_description"),false);
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
        languages.put("xno", "Anglo-Norman");
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

    public DescTree parseDescendants(String word, String language, String descendants, boolean borrowed) throws IOException{
        System.out.println("Word: "+ word + ", language: "+ language);
        ArrayList<DescTree> levels = new ArrayList<DescTree>();

        try{
            DescTree result = new DescTree(word, language);
            if(borrowed) result.setBorrowed();

            levels.add(result);
            for (String line : descendants.split("\\n")) {
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

                if(levels.size()<level){
                    warn("In word "+word+", problematic line: " + line + ", problem with tree levels. Filling in empty desctree nodes. Stars: "+firstSplit[0]+", raw description string:\n" + descendants + "\n\n");
                    for(int i=levels.size(); i<level; i++){
                        DescTree emptynode = new DescTree("ERROR IN "+word,language,true);
                        levels.get(i-1).addChild(emptynode);
                        levels.add(emptynode);
                    }
                }

                Pattern template = Pattern.compile("\\{\\{([^}]*)\\}\\}");
                Matcher matcher = template.matcher(firstSplit[1]);
                
                //this we use below to see whether we had an empty line with no relevant templates.
                boolean foundSomething = false;


                while(matcher.find()){
                    //here matcher.group(1) is the inner part of {{...}} and runs over all of them
                    boolean newBorrowed = borrowed;

                    String[] template_List = matcher.group(1).split("\\|");

                    List<String> templateList = Arrays.asList(template_List);

                    //now templateList contains a list of all the arguments of the template, which were separated by "|"
                    ArrayList<String> withoutOptions = new ArrayList<String>();
                    //we want to fill withoutOptions with all the ones that are not bor=1 etc
                    for(int i=0; i<templateList.size(); i++){
                        String[] optionSplit = templateList.get(i).split("=",2);
                        if(optionSplit.length == 2){
                            //in this case, we are dealing with an option (i.e. something of the form bor=1)
                            String optionName = optionSplit[0].trim();
                            String optionValue = optionSplit[1].trim();

                            if(optionName.equals("bor")){
                                newBorrowed = true;
                            }
                            //here we can do further else ifs to deal with other options. A complete list for the link or l template is at
                            //https://en.wiktionary.org/wiki/Template:link#Parameters
                            //and for the desc and desctree template, we have *additionally* the ones from
                            //https://en.wiktionary.org/wiki/Template:descendant
                            //
                            //The ones in the option list for l and link of the form 1=, 2= etc. refer to positional arguments, meaning:
                            //the first, second etc. argument which is not of the form bor=1. So in {desc|en|bor=1|bla}, desc is the template name,
                            //en is the first positional argument (i.e., according to the l options page, the language), and bla is the *second* positional argument,
                            //since the bor=1 thing doesn't count as positional argument. So bla is the word. We can have arbitrarily many options (i.e. things with =),
                            //and up to 5 positional arguments. See the l options page to see what they mean.
                        }
                        else{
                            //in this case, we are dealing with a positional argument. We put it into withoutOptions.
                            withoutOptions.add(templateList.get(i));
                        }
                    }
                    if(withoutOptions.size()<3){
                        //in this case, we have a template which is too short, we can ignore this.
                        continue;
                    }
                    //if we reach here, withoutOptions has at least three entries. These are tag name, language, word. Potentially, there are more
                    //(e.g. if there is a fourth, it controls how the word is displayed on the website)
                    String templateTag = withoutOptions.get(0);
                    String language_child = withoutOptions.get(1);
                    String word_child = withoutOptions.get(2);

                    DescTree newChild;

                    if(templateTag.equals("desc") ||  templateTag.equals("l")){
                        newChild = new DescTree(word_child, language_child);
                        if(newBorrowed){
                            newChild.setBorrowed();
                        }
                    }
                    else if(templateTag.equals("desctree")){
                        newChild = parseDescTree(word_child, language_child, newBorrowed);
                        ArrayList<DescTree> newChildList = newChild.getChildren();
                        if(newChildList.size()<1) {
                            warn("Warning: in word "+ word + ", in line: " + line +", desctree is empty: " + templateTag);
                        }
                    }
                    else{
                        warn("Warning: in word "+ word + ", in line: " + line +", unexpected template: " + templateTag);
                        //if this produces lots of unnecessary errors, we can turn this into an: else if(templateTag != ... && templateTag != ... &&...), where we include all the possible
                        //templates which we know we can safely ignore. Then this still warns us if we encounter one that we haven't thought about before.
                        continue;
                    }


                    levels.get(level-1).addChild(newChild);
                    levels.set(level-1,newChild);
                    //instead of working with a tree in which every node has an ArrayList of words, we now simply attach each of the {{l|..|..}} as word to the previous one in the tree.
                    //This also makes it so we don't have to deal with differing languages, subsequent words in the same line are just treated as descendants.
                    if(!foundSomething){ //first word of each line becomes parent to the ones in the descendants below, further words in the same line just become siblings in the tree, with no children of their own.
                        if (levels.size() < level + 1) {
                            levels.add(newChild);
                        }
                        else {
                            levels.set(level, newChild);
                        }
                    }
                    foundSomething = true;
                }


            if(!foundSomething){
                warn("Warning: In word "+ word + ", ignoring line: " +line+", but adding dummy node so the tree connects correctly (for example for 'Westphalian' line that appears often.");
                DescTree child = new DescTree("ERROR IN "+word,language,true);
                levels.get(level - 1).addChild(child);
                if (levels.size() < level + 1) {
                    levels.add(child);
                } else {
                    levels.set(level, child);
                   }
            }
            }
        return result;
        }
        catch(SyntaxError ex){
        logger.log(ex.getMessage());
        return new DescTree("ERROR IN "+word,language,true);
        }
    }



    public DescTree parseDescTree(String raw_word, String language, boolean borrowed) throws IOException{
        System.out.println("Word: "+ raw_word+ ", language: " + language);
        String word = findTitle(raw_word, language);
        try{
            String query_allwords = "SELECT * FROM wiki_db_13_01 WHERE wiki_word = '" + word + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query_allwords);

            //sometimes ResultSet empty (e.g. ascii mapping is wrong), in this case log
            //sometimes it containse to many entries, as SQL ignores case-sensitive
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
                String[] cut1 = description.split("=="+getLanguage(language)+"==\\n",2);
                if(cut1.length<2){
                    error("Syntax Error, word: "+raw_word+", did not find language "+getLanguage(language)+ " in description string.");
                }
                String languagesection = cut1[1].split("----",2)[0];
                String[] cut3 = languagesection.split("====Descendants====\\n",2);
                if(cut3.length<2){
                    //no descendants
                    return new DescTree(raw_word,language);
                }
                String descendants[] = cut3[1].split("\\r?\\n=");//takes everything up to the next line starting with an '=', or the rest of the file. Hopefully this covers all cases.
                String desc = descendants[0];
                DescTree result = parseDescendants(raw_word, language, desc, borrowed);
                return result;
            }
        }
        catch (SQLException ex) {
            System.err.println("SQL Exception in parseDescTree:");
            System.err.println(ex.getMessage());
        }
        catch (SyntaxError ex){
            logger.log(ex.getMessage());
            return new DescTree(raw_word, "ERROR",true);
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
