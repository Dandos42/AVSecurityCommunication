import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileSaver {

	 static int messageCount = 1; // Received message counter

	    // Method to save binary file over MQTT protocol
	    public static void saveFile(byte[] data, String topic) {
	        String fileName = "command_mqtt" + messageCount + ".bin"; // Name of file with command number
	        try {
	        //writing to file
	            FileOutputStream fos = new FileOutputStream(fileName);
	            fos.write(data);
	            fos.close();
	          //Inserting time into the log when receiving and saving a file
	           LocalDateTime rt = LocalDateTime.now();
	           System.out.println("File received and saved as: " + fileName);
	           Logger.log("Received file over MQTT and saved: " + fileName + " from topic: " + topic + " Time " + rt, "mqtt_subscriber.log");
	           messageCount++;

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // Method to save binary file over TLS protocol
	    public static void saveFileTLS(byte[] data, String topic) {
	        String fileName = "command_tls" + messageCount + ".bin"; // Name of file with command number
	        try {
	        	//writing to file
	            FileOutputStream fos = new FileOutputStream(fileName);
	            fos.write(data);
	            fos.close();
	            
	            //Inserting time into the log when receiving and saving a file
	            LocalDateTime rt = LocalDateTime.now();
		        System.out.println("File received and saved as: " + fileName);
		        Logger.log("Received file over MQTT with TLS and saved: " + fileName + " from topic: " + topic + " Time " + rt, "mqtt_subscriber_tls.log");
		        messageCount++;

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // Method to save binary file over CoAP protocol
	   public static void SaveFileCoap(byte[] data) {
	       String fileName = "command_coap" + messageCount + ".bin"; // Name of file with command number
	       try {
	    	   //writing to file
	           FileOutputStream fos = new FileOutputStream(fileName);
	           fos.write(data);
	           fos.close();
	           
	           //Inserting time into the log when receiving and saving a file
	           LocalDateTime rt = LocalDateTime.now();
	           System.out.println("File received and saved as: " + fileName);
	           Logger.log("Received file over CoAP and saved: " + fileName + " Time: " + rt, "coap_client.log");
	           messageCount++; 
	       } catch (IOException e) {
	           e.printStackTrace();;
	       }
		}
	   // Method to save binary file over DTLS protocol
	   public static void SaveFileCoapDTLS(byte[] data) {

	      String fileName = "command_coap_dtls" + messageCount + ".bin"; // Name of file with command number
	      try {
	    	//writing to file
	        FileOutputStream fos = new FileOutputStream(fileName);
	        fos.write(data);
	        fos.close();
	        
	        //Inserting time into the log when receiving and saving a file
	        LocalDateTime rt = LocalDateTime.now();
	        System.out.println("File received and saved as: " + fileName);
	        Logger.log("Received file over CoAP with DTLS and saved: " + fileName + " Time: " + rt, "coap_client_dtls.log");
	        messageCount++; 
	    } catch (IOException e) {
	        e.printStackTrace();;
	    }
	}
   
	
}
