import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    BufferedWriter writer;

    public Logger(String filepath) throws IOException{
        writer = new BufferedWriter(new FileWriter(filepath));
    }


    public void log(String msg) throws IOException{
        writer.write(msg);
        writer.newLine();
        writer.flush();
        System.err.println(msg);
    }
}
