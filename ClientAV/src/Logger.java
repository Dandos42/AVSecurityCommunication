import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class Logger {
	
	//Class for creating log files, the log name is defined in each class separately
	
	  private static final String DEFAULT_LOG_FILE = "aplication.log";

	    public static void log(String message, String nazevSouboru) {
	        try {
	            PrintWriter writer = new PrintWriter(new FileWriter(nazevSouboru, true));
	            writer.println(message);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void log(String message) {
	        log(message, DEFAULT_LOG_FILE);
	    }

}


