import java.util.Scanner;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import java.time.LocalDateTime;

public class MqttSubscriberNotSecure {
   
	//Client Subscriber
	public void ConnectToPublisherOverMqtt() {
		   
			//Declaration
			Scanner scanner = new Scanner(System.in);
      		String topic = "VehicleCommunication"; //Name of topic
      		String broker = "tcp://192.168.10.2:1883"; //IP address + port to connect to the broker Mosquitto via TCP protocol
            String id = "SubscriberClient"; 
            int qos = 2; //QoS level: 0 = at most once, 1 = at least once, 2 = exactly once 
            MemoryPersistence mp = new MemoryPersistence();
      		
              	//Verify the user-name and password that is stored in passwd, the link is in the mosquitto.conf 
      			System.out.println("Enter your username: ");
      			String username = scanner.nextLine();
      			
      			System.out.println("Enter your password: ");
      			byte[] password = scanner.nextLine().getBytes();
        
        try {
        	
        	//Record the start of communication to the log 
        	LocalDateTime startTime = LocalDateTime.now();
        	Logger.log("Communication with the broker has been started: " + " Time: " + startTime, "mqtt_subscriber.log");
            
        	//MQTT client instance
            MqttClient client = new MqttClient(broker, id, mp);
         
            //MQTT client settings
            MqttConnectionOptions cOpt = new MqttConnectionOptions();
            cOpt .setCleanStart(true);
            cOpt .setUserName(username);
            cOpt .setPassword(password);
            System.out.println("Connecting to the Broker: "+broker);
            
            //defining custom functions that are executed when a message is received or when an error occurs
           client.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
	            System.out.println("Received a message on topic: " + topic);
	            FileSaver.saveFile(message.getPayload(), topic); // Save message content to the file 
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
           //MQTT client connection to the broker
            client.connect(cOpt);
            System.out.println("Connected");
            System.out.println(" Hello " + username + " ! ");
            System.out.println("Waits for data....");
            
            //Client subscribe message based on the topic
            client.subscribe(topic,qos);
               
                System.out.println("If you want to end type 'exit': ");
                String end = scanner.nextLine();
                
                if (end.equalsIgnoreCase("exit")) {
                	//Disconnected from MQTT broker
                    client.disconnect();
                  //Record the end of communication to the log 
                	LocalDateTime endTime = LocalDateTime.now();
                 	Logger.log("Communication with the broken has been terminated: " + " Time: " + endTime + "\n","mqtt_subscriber.log");
                    System.out.println("Disconnected from MQTT broker");
                }
               
        } catch(MqttException me) {
            System.out.println("Error: "+me);
        }
    }
    

}
	

