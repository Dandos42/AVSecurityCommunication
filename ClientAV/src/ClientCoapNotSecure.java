import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.config.UdpConfig;


public class ClientCoapNotSecure {
	
	public void ConnectionToServerOverCoAP()
	{
		//Registration of UDP and CoAP protocol configuration parameters
		CoapConfig.register();
		UdpConfig.register();
		
		//Declaration
        String serverSocket = "coap://192.168.10.2:5683/"; //Set IP address + port
        String source = "VehicleCommunication"; //Name of source
        Scanner scanner = new Scanner(System.in);
        String end;
        
       
        try {
        // link to passwd file with user credentials 
         UserAuthenticator authenticator = new UserAuthenticator("passwd.txt");
         
     	//user authentication
			System.out.println("Enter your username: ");
			String username = scanner.nextLine();
			
			System.out.println("Enter your password: ");
			String password = scanner.nextLine();

        if (authenticator.authenticate(username, password)) {
            System.out.println("Authentication successful!");
        } else {
            System.out.println("Authentication failed!");
        }
        } catch (IOException e) {
        System.err.println("Error loading user credentials: " + e.getMessage());
        }
        
        try {
        	
        	//Record the start of communication to the log 
         	LocalDateTime startTime = LocalDateTime.now();
         	Logger.log("Communication with the CoAP server has been started: " + " Time: " + startTime, "coap_client.log");	
        
         //Create client instance
        CoapClient client = new CoapClient();
        
        System.out.println("CoAP client is running without security implementation.");
     
        //setting the ip address of the server and the topic for receiving binary files
     	client.setURI(serverSocket+source);

        		// Send the GET request to the server to exchange binary files
                CoapResponse responseFile = client.get();
                if (responseFile != null && responseFile.isSuccess()) {
                	
                	System.out.println("Connected to the CoAP server on port 5683");
                	
                	//Get binary file and save
                	FileSaver.SaveFileCoap(responseFile.getPayload());
                    
                	System.out.println("If you want to end type 'exit': ");
                    end = scanner.nextLine();
                    
                    if(end.equalsIgnoreCase("exit"))
                    {
                    	//Disconnected from CoAP server
                    	client.shutdown();
                    	//Record the end of communication to the log 
                    	LocalDateTime endTime = LocalDateTime.now();
    					Logger.log("Communication with the CoAP server has been terminated: " + " Time: " + endTime + "\n", "coap_client.log");
                        System.out.println("Disconnected from CoAP server");
                        

                    }
                }
                else {
                    System.err.println("Failed to exchange file.");
                    return;
                    
                }
        
            
        }catch (Exception e) {
			// TODO: handle exception
            System.err.println("Error communicating with the server: " + e.getMessage());

		}
       

	}
}
	
	
	


