import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import java.time.LocalDateTime;
import java.util.Scanner;

import java.io.FileInputStream;


public class MqttSubscriberTls {
	
    
    //Client Subscriber
    public void ConnectToPublisherOverMqttWithTLS() {
        //Declaration
        Scanner scanner = new Scanner(System.in);
        String topic        = "VehicleComunication"; //Name of topic
        String broker       = "ssl://AVcommunication.com:8883"; //IP address + port to connect to the broker Mosquitto via TCP protocol
        String clientId     = "SubscriberClient"; 
        int qos = 2; //QoS level: 0 = at most once, 1 = at least once, 2 = exactly once 
        MemoryPersistence persistence = new MemoryPersistence();
        
        
        try {
        	//Record the start of communication to the log 
        	LocalDateTime startTime = LocalDateTime.now();
            Logger.log("Communication with the broker has been started: " + " Time: " + startTime,"mqtt_subscriber_tls.log");
        	
            MqttClient client = new MqttClient(broker, clientId, persistence);
        	
            //defining custom functions that are executed when a message is received or when an error occurs
        	client.setCallback(new MqttCallback() {
        	
        	@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
	            System.out.println("Received a message on topic: " + topic);
	            FileSaver.saveFileTLS(message.getPayload(), topic); // Save message content to a file 
			}

			@Override
			public void authPacketArrived(int arg0, MqttProperties arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectComplete(boolean arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void deliveryComplete(IMqttToken arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void disconnected(MqttDisconnectResponse arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mqttErrorOccurred(MqttException arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
        	
        	 
        	  	//Load CA certificate
        		//You can use localhostCA.crt - it CA cert with RSA 2048
        		//You can use CA_ECDSA.crt - it CA cert with ECDSA prime256v1 and hash sha256
	            CertificateFactory caFac = CertificateFactory.getInstance("X.509");
	            //name of CA certificate 
	            //FileInputStream ca = new FileInputStream("localhostCA.crt"); 
	            FileInputStream ca = new FileInputStream("CA_ECDSA.crt");
	            X509Certificate caCert = (X509Certificate) 
	            caFac.generateCertificate(ca);

	            // Create a KeyStore instance for to store keys and certificates
	            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	            ks.load(null); //Create empty KeyStore
	            ks.setCertificateEntry("localhost", caCert); //Add certificate to the KeyStore
	            
	            //source for trusted certificates
	            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	            tmf.init(ks);
	            
	            // Store the client certificate in the KeyStore
	            //You can use client_sub_RSA.p12 - it client cert with RSA 2048
	            //You can use client_sub_ECDSA.p12 - it client cert with ECDSA prime256v1 and hash sha256
	            String password = "test123"; //Same password as used for exporting
	            KeyStore kmKs = KeyStore.getInstance("PKCS12"); //format of cert file
	            //kmKs.load(new FileInputStream("client_sub_RSA.p12"), password.toCharArray());
	            kmKs.load(new FileInputStream("client_sub_ECDSA.p12"), password.toCharArray());
	        

	            // Create the KeyManager
	            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	            kmf.init(kmKs, password.toCharArray());
	            
	            
	            //Create SSL context and TLS version specifications
	            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
	            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);    
	            //Set the list of allowed Cipher Suites for encryption and integrity 
	            sslContext.getDefaultSSLParameters().setCipherSuites(new String[]{"TLS_AES_256_GCM_SHA384"});
	         
	         //MQTT client settings
             MqttConnectionOptions connOpts = new MqttConnectionOptions();	
             connOpts.setSocketFactory(sslContext.getSocketFactory());
             connOpts.setCleanStart(true);
             System.out.println("Connecting to the Broker: " + broker);
             //MQTT client connection to the broker
             client.connect(connOpts);
             System.out.println("Connected");
             System.out.println(" Hello " + clientId + " ! ");
             System.out.println("Waits for data....");
             //Client subscribe message based on the topic
             client.subscribe(topic, qos);
                
                 System.out.println("If you want to end type 'exit': ");
                 String end = scanner.nextLine();
                 
                 if (end.equalsIgnoreCase("exit")) {
                 	//Disconnected from MQTT broker
                     client.disconnect();
                   //Record the end of communication to the log 
                 	LocalDateTime endTime = LocalDateTime.now();
     	            Logger.log("Communication with the broken has been terminated: " + " Time: " + endTime + "\n", "mqtt_subscriber_tls.log");
                    System.out.println("Disconnected from MQTT broker");
                 }
            
        } catch(MqttException | IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException e) {
            System.out.println("Error: "+e);
        }
    }
    
   
}


    
    
