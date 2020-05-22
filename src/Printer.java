import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Printer {
public void write_file (ArrayList<String> data) throws IOException {
        FileWriter tsv_Writer = new FileWriter("entry.csv");

        for(String da : data) {
            tsv_Writer.append(da);
            tsv_Writer.append("\n");
        }



        tsv_Writer.flush();
        tsv_Writer.close();
        }
}
