import java.time.LocalDateTime;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class MqttPublisherNotSecure {
	


	//Client Publisher
	public void ConnectToSubscriberOverMqtt(){
		
		//Declarations
		Scanner scanner = new Scanner(System.in);
		String topic = "VehicleCommunication"; //Name of topic
        String broker = "tcp://192.168.10.2:1883"; //Set IP address + port
        String id = "PublisherClient"; 
        int qos = 2; //QoS level: 0 = at most once, 1 = at least once, 2 = exactly once
        MemoryPersistence mp = new MemoryPersistence();
       
		
        	//Verify the user-name and password that is stored in passwd, the link is in the mosquitto.conf
			System.out.println("Enter your username: ");
			String username = scanner.nextLine();
			
			System.out.println("Enter your password: ");
			byte[] password = scanner.nextLine().getBytes();
        
	        try {
	        	// Record the start of communication to the log
	        	LocalDateTime startTime = LocalDateTime.now();
	        	Logger.log("Communication with the broker has been started: " + " Time: " + startTime, "mqtt_publisher.log");
	        	
	            //MQTT client instance
	        	MqttClient client = new  MqttClient(broker, id, mp);
	        
	            //MQTT client settings
	            MqttConnectionOptions cOpt = new MqttConnectionOptions();
	            cOpt.setCleanStart(true);
	            cOpt.setUserName(username);
	            cOpt.setPassword(password);
	            //MQTT client connection to the broker
	            System.out.println("Connecting to the Broker: "+broker);
	            client.connect(cOpt);
	            System.out.println("Connected");
	            System.out.println("Hello " + username + " !");
	            
	            

	            while(true){
	            		
	            		//List of binary files that can be send
	            		//In normal use there is a while loop for sending files until i enter exit, the program is waiting for user input
	            		//For test purposes of delay measurement: one specific file is required
            			System.out.println("Binary files that can be send (1 B - 10 MB):");
            			System.out.println("binary_1B.bin, binary_10B.bin, binary_100B.bin, binary_1kB.bin, binary_10kB.bin, binary_100kB.bin, binary_1MB.bin, binary_10MB.bin");
            			System.out.println("Enter the file name to send or type 'exit' to quit: ");
	  	                //Normal use:
	  	                String fileName= scanner.nextLine();
	  	                //test use:
	  	                //String fileName= "binary_1B.bin";
	  	                
	                    // Record time of sending and save to log
	                    LocalDateTime sendTime = LocalDateTime.now();
	    	        	Logger.log("File: " + fileName + " sent at: " + " Time: " + sendTime, "mqtt_publisher.log");
	  	                  
	    	        	// Read the binary file 
	            		 File file = new File(fileName);
		                   if (file.exists() && !file.isDirectory()) { 
		                        FileInputStream fis = new FileInputStream(file);
		                        byte[] data = new byte[(int) file.length()];
		                        fis.read(data);
		                        fis.close();
		                        
		                        //MQTT message settings
		                        MqttMessage message = new MqttMessage();
			                    message.setPayload(data);
			                    message.setRetained(true); 
			                    message.setQos(qos);
			                    
			                    //Publish message based on the topic
			                    client.publish(topic, message);
			                    	 
			                    System.out.println("File '" + fileName + "' has been sent.");
					            System.out.println(sendTime);

			                } else {
			                    System.out.println("File '" + fileName + "' does not exist.");
			                }
		                   
		                   //test use
		                   //System.out.println("Do you want to continue? if not type exit");
		                   //fileName = scanner.nextLine();
		                   
		                   if(fileName.equalsIgnoreCase("exit"))
			                { 
			                	//Disconnected from MQTT broker
			                	client.disconnect();
			                	// Record the end of communication to the log
			                	LocalDateTime endTime = LocalDateTime.now();
			    	            Logger.log("Communication with the broken has been terminated: " + " Time: " + endTime + "\n","mqtt_publisher.log");
			                	System.out.println("Disconnected from MQTT broker");
			                	break; //exit the loop
			                }

	            }
	       	
	          }catch(MqttException | IOException e ) {
	          System.out.println("Error: "+ e);
	          } 
	            
	       
	    }
   
}
		
		
	

