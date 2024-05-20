import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		//Declaration 
		Scanner sc = new Scanner(System.in);
		int choice;
		boolean run = true;
		
		try {
			
			 while(run)
				{
						//MENU
		        		System.out.println("Hello I am a teleoperation center!");
						System.out.println(" -- Welcome in menu -- ");	
						System.out.println(" -- Select the desired action -- ");
						System.out.println("*********************************************************");
						System.out.println("1 .. Show basic project documentation");
						System.out.println("2 .. Start an insecure communication between server and client via MQTT protocol");
						System.out.println("3 .. Start an secure communication between server and client via MQTT with TLS protocol");
						System.out.println("4 .. Start an insecure communication between server and client via CoAP protocol");
						System.out.println("5 .. Start an secure communication between server and client via CoAP with DTLS protocol");
						System.out.println("********************************************************************************************");
						choice= sc.nextInt();

						switch(choice)
						{
							case 1: 
								
						        //basic information about project and author
								 System.out.println("............................");
								 System.out.println("Author: Daniel Prachař");
								 System.out.println("ID: 240969.");
								 System.out.println("BRNO UNIVERSITY OF TECHNOLOGY");
								 System.out.println("FACULTY OF ELECTRICAL ENGINEERING AND COMMUNICATION");
								 System.out.println("Description: ");
								 System.out.println("The Teleoperation Center is a program designed to simulate and control remote operations using MQTT and CoAP protocols. This application allows users to send binary messages of different sizes through these protocols.");
								 System.out.println("One of the main features of the application is the ability to also send messages using secure versions of these protocols, specifically TLS for MQTT and DTLS for CoAP.");
								 System.out.println("The Teleoperation Center provides a user interface that allows users to easily set up and manage communication between devices. Users can also monitor and log messages for further analysis and debugging.");
								 System.out.println("............................");	


								break;
							case 2:
								
								MqttPublisherNotSecure connectMqtt = new MqttPublisherNotSecure();
								connectMqtt.ConnectToSubscriberOverMqtt();
								
								break;
							case 3: 
								
								MqttPublisherTls connectMqttTls = new MqttPublisherTls();
								connectMqttTls.ConnectToSubscriberOverMqttWithTls();
								
								break;
							case 4: 
								
								ServerCoapNotSecure connectCoap = new ServerCoapNotSecure();
								connectCoap.ConnectionToClientOverCoap();
								
								break;
							case 5:
								
								ServerCoapSecureDtls connectCoapDtls = new ServerCoapSecureDtls();
								connectCoapDtls.ConnectionToClientOverDTLS();
								
								
								break;
							default:
				                    System.out.println("Wrong choice, please select correct number.");
				                    break;
						}
						
						 System.out.println("If you want get back to the menu type: 'x'");
				            while (true) {
				                String input = sc.next();
				                if (input.equals("x")) {
				                    break;
				                }
				            }	
				}
			
		}
		catch(Exception e){
			
			System.out.println(e);
			
		}
		

	}

}
