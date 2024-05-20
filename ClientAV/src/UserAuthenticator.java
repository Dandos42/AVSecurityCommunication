import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserAuthenticator {
	
	//A user authentication class that compares the user's entered login credentials with those in the passwd.txt file
	
	  private Map<String, String> userCredentials = new HashMap<>();

	    public UserAuthenticator(String filePath) throws IOException {
	        loadUserCredentials(filePath);
	    }

	    private void loadUserCredentials(String filePath) throws IOException {
	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                String[] parts = line.split(" ");
	                if (parts.length == 2) {
	                    userCredentials.put(parts[0], parts[1]);
	                }
	            }
	        }
	    }

	    public boolean authenticate(String username, String password) {
	        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
	    }

}
