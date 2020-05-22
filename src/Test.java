import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    public static void testDescParser() throws IOException {
        DescendantsParser parser = new DescendantsParser();
        String word = "frijaz";
        String language = "pro-g";
        String descendants = "* {{desc|ang|frēo}}\n" +
                "** {{desc|enm|freo}}, {{l|enm|fre}}\n" +
                "*** {{desc|sco|fre}}\n" +
                "*** {{desc|en|free}}\n" +
                "**** {{desc|hif|firii}}\n" +
                "* {{desc|ofs|frī}}\n" +
                "** {{desc|stq|fräi}}\n" +
                "** {{desc|fy|frij}}\n" +
                "* {{desc|osx|frī}}\n" +
                "** {{desc|gml|vri}}, {{l|gml|vrig}}\n" +
                "*** {{desc|nds|fri}}, {{l|nds|free}}, {{l|nds|frigg}}\n" +
                "*** {{desc|fo|fríur}}\n" +
                "*** {{desc|no|fri}}\n" +
                "*** {{desc|sv|fri}}\n" +
                "*** {{desc|da|fri}}\n" +
                "* {{desctree|odt|frī}}\n" +
                "* {{desc|goh|frī}}\n" +
                "** {{desc|gmh|vrī}}\n" +
                "*** {{desc|de|frei}}\n" +
                "*** {{desc|lb|fräi}}\n" +
                "*** {{desc|wym|fraj|frȧj}}\n" +
                "* {{desc|got|?????}}";
        DescTree result = parser.parseDescendants(word, language, descendants, false);
        result.print();
    }

    public static void testNormalize(){
        System.out.println(DescendantsParser.normalize("frī"));
    }

    public static void testGetLanguage() { System.out.println(DescendantsParser.getLanguage("odt"));}

    public static void testRun() throws IOException{
        DescendantsParser parser = new DescendantsParser();
        parser.run();
        ArrayList<DescTree> results = parser.getResults();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(results.toString());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void testLog() throws IOException{
        Logger logger = new Logger("log.txt");
        logger.log("Error!!!");
        logger.log("Other Error.");
    }


    public static void testWriter() throws IOException {
        DescendantsParser parser = new DescendantsParser();
      //  parser.run();

        String word = "frijaz";
        String language = "pro-g";
        String descendants = "* {{desc|ang|frēo}}\n" +
                "** {{desc|enm|freo}}, {{l|enm|fre}}\n" +
                "*** {{desc|sco|fre}}\n" +
                "*** {{desc|en|free}}\n" +
                "**** {{desc|hif|firii}}\n" +
                "* {{desc|ofs|frī}}\n" +
                "** {{desc|stq|fräi}}\n" +
                "** {{desc|fy|frij}}\n" +
                "* {{desc|osx|frī|bor=1}}\n" +
                "** {{desc|gml|vri}}, {{l|gml|vrig}}\n" +
                "*** {{desc|nds|fri}}, {{l|nds|free}}, {{l|nds|frigg}}\n" +
                "*** {{desc|fo|fríur}}\n" +
                "*** {{desc|no|fri}}\n" +
                "*** {{desc|sv|fri}}\n" +
                "*** {{desc|da|fri}}\n" +
                "* {{desctree|odt|frī}}\n" +
                "* {{desc|goh|frī}}\n" +
                "** {{desc|gmh|vrī}}\n" +
                "*** {{desc|de|frei}}\n" +
                "*** {{desc|lb|fräi}}\n" +
                "*** {{desc|wym|frȧj}}\n" +
                "* {{desc|got|?????}}";

        String desc = "* {{desc|ang|*Seaxa}} (attested in plural {{l|ang|Seaxan}})\n" +
                "** {{desc|enm|Saxe}}, {{l|enm|Sax}}; {{l|enm|Saxon}} (partially)\n" +
                "*** {{desc|sco|Saxon}}\n" +
                "*** {{desc|en|Saxon}}\n" +
                "* {{desc|osx|Sahso}}\n" +
                "** {{desc|gml|sasse}}\n" +
                "*** {{desc|nds-de|Sasse}}\n" +
                "*** {{desc|nds-nl|Sakse}}\n" +
                "* {{desc|odt|*Sasso}}\n" +
                "** {{desc|dum|Sassen}} (plural)\n" +
                "* {{desc|goh|Sahso}}\n" +
                "** {{desc|gmh|Sahse}}\n" +
                "*** {{desc|de|Sachse|bor=1}}\n" +
                "* {{desc|non|Saxi}}, {{l|non|Saxar|pos=plural}}\n" +
                "* {{desc|la|Saxō}}\n" +
                "** {{desc|fro|saisoigne}}, {{l|fro|sesne}}\n" +
                "*** {{desc|xno|sessoun}}\n" +
                "** {{desc|enm|Saxon|bor=1}} (partially)\n" +
                "** {{desc|sga|Saxa|bor=1}}\n" +
                "*** {{desc|gd|sasunnach}}\n" +
                "**** {{desc|sco|Sassenach|bor=1}}\n" +
                "** {{desc|owl|Seis|bor=1}}\n" +
                "*** {{desc|cy|Sais}}";



        DescTree result = parser.parseDescendants(word, language, descendants, false);
        DescTree result5 = parser.parseDescendants(word, language, desc, false);
        ArrayList<DescTree> results = new ArrayList<>();
        results.add(result);
        results.add(result5);
                //parser.getResults();
        int counter = 0;


        Cognate_Grouper cognate_grouper = new Cognate_Grouper(results);
        cognate_grouper.run();

        HashMap<Integer, ArrayList<String>> cognates = cognate_grouper.getCognates();
        String path = "";
        Text_Writer text_writer = new Text_Writer();
        text_writer.write_to_file(cognates);
    }


    public static void testAll() throws IOException {
        DescendantsParser parser = new DescendantsParser();

        parser.run();
        ArrayList<DescTree> results = parser.getResults();

        Cognate_Grouper cognate_grouper = new Cognate_Grouper(results);
        cognate_grouper.run();

        HashMap<Integer, ArrayList<String>> cognates = cognate_grouper.getCognates();
        System.out.println(cognates.size());
        System.out.println(cognates.get(1));

        Text_Writer text_writer = new Text_Writer();
        text_writer.write_to_file(cognates);
    }


    public static void testLink(){
        System.out.println(DescendantsParser.findTitle("*hagal", "odt"));
    }
}
