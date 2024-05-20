import java.time.LocalDateTime;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;


public class MqttPublisherTls {
	

	//Client Publisher
	public void ConnectToSubscriberOverMqttWithTls() {
		
		//Declaration
		Scanner scanner = new Scanner(System.in);
		String topic = "VehicleComunication"; //Name of topic
        String broker = "ssl://AVcommunication.com:8883"; //Set IP address + port
        String clientId = "PublisherClient"; 
        int qos = 2; //QoS level: 0 = at most once, 1 = at least once, 2 = exactly once
        MemoryPersistence persistence = new MemoryPersistence();
      
        
	        try {
	        	
	        	// Record the start of communication to the log
	            LocalDateTime startTime = LocalDateTime.now();
	        	Logger.log("Communication with the broker has been started: " + " Time: " + startTime, "mqtt_publisher_tls.log");
	        	
	         	//MQTT client instance
	            MqttClient client = new MqttClient(broker, clientId, persistence);
	           
	            
	            // Load CA certificate
	            // You can use localhostCA.crt - it is CA certificate with RSA 2048
	            // You can use CA_ECDSA.crt - it is CA certificate with ECDSA prime256v1 and hash sha256
	            
	            CertificateFactory caFac = CertificateFactory.getInstance("X.509");
	            //Name of CA certificates
	            //FileInputStream caF = new FileInputStream("localhostCA.crt");
	            FileInputStream caF = new FileInputStream("CA_ECDSA.crt");
	            X509Certificate caCert = (X509Certificate) caFac.generateCertificate(caF);

	            //Create a KeyStore instance for to store keys and certificates
	            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	            ks.load(null); //Create empty KeyStore 
	            ks.setCertificateEntry("localhost", caCert); //add certs to KeyStore
	            
	            
	           //Source for trusted certificates
	            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	            tmf.init(ks);
	            
	            // Store the client certificate in the KeyStore
	            // You can use client_sub_RSA.p12 - it client cert with RSA 2048
	            // You can use client_sub_ECDSA.p12 - it client cert with ECDSA prime256v1 and hash sha256
	            String password = "test123"; //Same password as used for exporting
	            KeyStore kmKs = KeyStore.getInstance("PKCS12"); //format of cert file
	            //kmKs.load(new FileInputStream("client_pub_RSA.p12"), password.toCharArray());
	            kmKs.load(new FileInputStream("client_pub_ECDSA.p12"), password.toCharArray());

	            // Create the KeyManager
	            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	            kmf.init(kmKs, password.toCharArray());
	            
	            //Create SSL context 
	            SSLContext sslContext = SSLContext.getInstance("TLSv1.3"); //Version of TLS protocol
	            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
	            //Set the list of allowed Cipher suites for encryption and integrity
	            sslContext.getDefaultSSLParameters().setCipherSuites(new String[] {"TLS_AES_256_GCM_SHA384"});
	            
	            //MQTT client settings
	            MqttConnectionOptions connOptions = new  MqttConnectionOptions();
	            connOptions.setSocketFactory(sslContext.getSocketFactory());
	            connOptions.setCleanStart(true);
	            //MQTT client connection to the broker
	            System.out.println("Connecting to the Broker: "+broker);
	            client.connect(connOptions);
	            System.out.println("Connected");
	            System.out.println("Hello!");

	            while(true) {
	            	
            		//List of binary files that can be send
            		//In normal use there is a while loop for sending files until I enter exit, the program is waiting for user input
            		//For test purposes of delay measurement: one specific file is required
            		System.out.println("Binary files that can be send (1 B - 10 MB):");
  	                System.out.println("binary_1B.bin, binary_10B.bin, binary_100B.bin, binary_1kB.bin, binary_10kB.bin, binary_100kB.bin, binary_1MB.bin, binary_10MB.bin");
  	                System.out.println("Enter the file name to send or type 'exit' to quit: ");
  	                //Normal use:
  	                String fileName= scanner.nextLine();
  	                //test use:
  	                //String fileName= "binary_10MB.bin";
  	                
  	                // Record time of sending and save to log
                    LocalDateTime sendTime = LocalDateTime.now();
                    Logger.log("File: " + fileName + " sent at: " + " Time: " + sendTime, "mqtt_publisher_tls.log");
                    
	            	
                    // Read the file
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
	                 	Logger.log("Communication with the broken has been terminated: " + " Time: " + endTime + "\n", "mqtt_publisher_tls.log");
	                 	System.out.println("Disconnected from MQTT broker");
	                	break; //exit the loop
	                }
	                	
	            }
	       	
	          }catch(MqttException | IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e ) {
	          System.out.println("Error: "+ e);
	          } 
	            
	       
	    }
	
   
}
		
		
	

