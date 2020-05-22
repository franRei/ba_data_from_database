import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Text_Writer {
    BufferedWriter writer;
    String filepath = "result_borr.csv";

    public Text_Writer() throws IOException {
        writer = new BufferedWriter(new FileWriter(filepath));
    }

    public void write_to_file (HashMap<Integer, ArrayList<String>> cognates) throws IOException {

            cognates.forEach((k, v) -> {
                System.out.format("key: %s , %s ", k, v.toString());
                try {
                    writer.write(k.toString());
                    int counter = 0;
                    for (String word : v) {
                        writer.write(", " + word);
                        counter++;
                        if((word.equals("true") || word.equals("false")) && counter != v.size()) {
                            writer.write("\n");
                        }
                    }
                    writer.write("\n");
                } catch (IOException ex) {
                }
            });

    writer.close();

    }


}
