
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.config.UdpConfig;


public class ServerCoapNotSecure {
	
	public void ConnectionToClientOverCoap(){
		//Registration of UDP and CoAP protocol configuration parameters
		CoapConfig.register();
		UdpConfig.register();
		
		//Declaration
		Scanner scanner = new Scanner(System.in);
		
		 try {
	         //Link to passwd file with user credentials   
			 UserAuthenticator authenticator = new UserAuthenticator("passwd.txt");

			 	//User authentication
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
			// Record the start of communication to the log
			LocalDateTime startTime = LocalDateTime.now();
			Logger.log("Communication with the CoAP client has been started: " + " Time: " + startTime,"coap_server.log");
			
			//CoAP server instance and set specific port 
			CoapServer server = new CoapServer(5683);

			System.out.println("CoAP server is running not securely on port 5683");
			System.out.println("Waits for client connection...");
			
			//Starting a CoAP server
			server.start();
	
		
			//Sending a binary files to the client
			server.add(new CoapResource("VehicleCommunication") {
				@Override
				public void handleGET(CoapExchange exchangefile) {
					
					System.out.println("CoAP Client connected. ");
				
					while(true) {
				
						try {
							//List of binary files that can be send
		            		//In normal use there is a while loop for sending files until I enter exit, the program is waiting for user input
		            		//For test purposes of delay measurement: one specific file is required
	                		System.out.println("Binary files that can be send (1 B - 1 kB):");
	      	                System.out.println("binary_1B.bin, binary_10B.bin, binary_100B.bin, binary_1kB.bin");
	      	                System.out.println("Enter the file name to send or type 'exit' to quit: ");
	      	                System.out.println("You have to press enter and after that you can type file name or exit: ");
		  	                //Normal use:
		  	                String fileName= scanner.nextLine();
		  	                //test use:
		  	               //String fileName= "binary_1kB.bin";
		  	                
   
		  	              // Record time of sending and save to log
		                    LocalDateTime sendTime = LocalDateTime.now();
		                    Logger.log("File: " + fileName + " sent at: " + " Time: " + sendTime,"coap_server.log");
		  	              
		  	                //Read binary file 
		                    File file = new File(fileName);
		                    if (file.exists() && !file.isDirectory()) {
		                        FileInputStream fis = new FileInputStream(file);
		                        byte[] binaryData = new byte[(int) file.length()];
		                        fis.read(binaryData);
		                        fis.close();
		
			                    //Send binary file
		                        exchangefile.respond(ResponseCode.CONTENT, binaryData);
		                        System.out.println("File '" + fileName + "' has been sent.");
		                        System.out.println(sendTime);
		                    } else {
		                        System.out.println("File '" + fileName + "' does not exist.");
		                        exchangefile.respond(ResponseCode.NOT_FOUND, "File not found");
		                    }
		                    
		                    	//test use
		                    	//System.out.println("Do you want to continue? if not type exit");
		                    	//fileName = scanner.nextLine();
		                    
		  				  if(fileName.equalsIgnoreCase("exit")) {
							  
		  					  //Disconnected from CoAP client
		  					  server.destroy();
		  					  //log record of the end of communication
		  					  LocalDateTime endTime = LocalDateTime.now();
		  					  Logger.log("Communication with the CoAP client has been terminated: " + " Time: " + endTime + "\n", "coap_server.log");
		  					  System.out.println("Disconnected from CoAP client");
		  					  break;
		  					 }

						} catch (IOException e) {
						
							System.out.println("Error: " + e);
							exchangefile.respond(ResponseCode.INTERNAL_SERVER_ERROR, "Error occurred");
						}
					}

				}
			});
		 
		
		}catch (Exception e) {
		// TODO: handle exception
		   System.out.println("Error: "+ e);
		}
		
	}
}


