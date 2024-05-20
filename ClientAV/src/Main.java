import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
			//Declaration
	        Scanner sc = new Scanner(System.in);
	        int volba;
	        boolean run = true;
	        
	        try {

				while(run)
				{
						//MENU
						System.out.println("Hello I am a autonomous car");
						System.out.println(" -- Welcome in menu -- ");	
						System.out.println(" -- Select the desired action -- ");
						System.out.println("**********************************************");
						System.out.println("1 .. Show basic project documentation"); 
						System.out.println("2 .. Start an insecure communication between server and client via MQTT protocol");
						System.out.println("3 .. Start an secure communication between server and client via MQTT with TLS protocol");
						System.out.println("4 .. Start an insecure communication between server and client via CoAP protocol");
						System.out.println("5 .. Start an secure communication between server and client via CoAP + DTLS protocol");
						System.out.println("****************************************************************************************************");
						volba= sc.nextInt();

						
						switch(volba)
						{
  
							case 1:			        
						           
						        //basic information about project and author
								System.out.println("Author: Daniel Prachař");
								 System.out.println("ID: 240969.");
								 System.out.println("BRNO UNIVERSITY OF TECHNOLOGY");
								 System.out.println("FACULTY OF ELECTRICAL ENGINEERING AND COMMUNICATION");
								 System.out.println("The program simulates an autonomous vehicle. It is designed to receive and store messages using MQTT and CoAP protocols. This simulator allows users to receive binary messages of different sizes through these communication protocols. ");
								 System.out.println("Like the Teleoperation Center, this program allows messages to be received using secure versions of the protocols, using TLS for MQTT and DTLS for CoAP.");
								 System.out.println("The program also supports monitoring and logging of received messages, allowing users to analyse vehicle operation and behaviour during the simulation.");
								 System.out.println("**********************************************************************************************");
        
								break;
							case 2:
								
								MqttSubscriberNotSecure Mqttconnect = new MqttSubscriberNotSecure();
					            Mqttconnect.ConnectToPublisherOverMqtt();
					          
								break;
							case 3:
								
								MqttSubscriberTls TlsMqttConnect = new MqttSubscriberTls();
								TlsMqttConnect.ConnectToPublisherOverMqttWithTLS();
								
								break;
								
							case 4:
					           

								ClientCoapNotSecure CoApconnect = new ClientCoapNotSecure();
								CoApconnect.ConnectionToServerOverCoAP();
								
								break;
								
							case 5:
								
								ClientCoapDtls DtlsCoAPconnect = new ClientCoapDtls();
								DtlsCoAPconnect.ConnectionToServerOverCoAPwithDTLS();
								
								break;
								
							 default:
				                    System.out.println("Wrong choice, please select correct number.");
				                    break;
						}
				             
						 System.out.println("If you want get back to the menu type 'x'");
				            while (true) {
				                String input = sc.next();
				                if (input.equals("x")) {
				                    break;
				                }
				            }
			            }
			        
	        }catch(Exception e){
	        	System.out.println(e);
	        	
	        }
	}
}
	
	

			


