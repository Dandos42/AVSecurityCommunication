import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.cert.Certificate;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.elements.util.SslContextUtil.Credentials;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConfig;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.x509.NewAdvancedCertificateVerifier;
import org.eclipse.californium.scandium.dtls.x509.SingleCertificateProvider;
import org.eclipse.californium.scandium.dtls.x509.StaticNewAdvancedCertificateVerifier;


public class ServerCoapSecureDtls {
	
	 public void ConnectionToClientOverDTLS(){
		 	
		 //Registration of UDP CoAP and DTLS protocol configuration parameters
		 DtlsConfig.register();
	     CoapConfig.register();
	     UdpConfig.register();
	      
	     //Declaration
	     Scanner scanner = new Scanner(System.in);
	     //loading the standard configuration 
	     Configuration configuration = Configuration.getStandard();
            
	        
	        try {
	        	
	        	//You can use CoAP_client_RSA.p12 - it client cert with RSA 2048
            	//You can use CoAP_client_ECDSA.p12 - it client cert with ECDSA prime256v1 and hash sha256
	        	
	        	String cert = "CoAP_server_RSA.p12";
	            //String cert = "CoAP_server_ECDSA.p12";
	            String alias = "ServerCoAP";
	            char[] keystorePassword = "test123".toCharArray();
	            char[] keyPassword = "test123".toCharArray();
	            
	      		//Load CA certificate
	    		//You can use localhostCA.crt - it CA cert with RSA 2048
	    		//You can use CA_ECDSA.crt - it CA cert with ECDSA prime256v1 and hash sha256
	            
	            String truststore = "localhostCA.crt";
	            //String truststore = "CA_ECDSA.crt";
	            
	            // Record the start of communication to the log
	            LocalDateTime startTime = LocalDateTime.now();
				Logger.log("Communication with the CoAP client has been started: " + " Time: " + startTime, "coap_server_dtls.log");
	            
	            
	            //Reading a certificate and private key from a PKCS12 file
	            Credentials serverCredentials = SslContextUtil.loadCredentials(cert,alias,keystorePassword,keyPassword);
	            //Reading a CA file
	            Certificate[] caCredentials = SslContextUtil.loadTrustedCertificates(truststore);
	            
 	            //create configuration for DTLS connection
		        DtlsConnectorConfig.Builder builder = DtlsConnectorConfig.builder(configuration);
		        builder.setAddress(new InetSocketAddress(5684));
		        
		        //create a server identity that is used for authentication.
		        //GET the key and parameters from the certificate
		        SingleCertificateProvider identity = new SingleCertificateProvider(serverCredentials.getPrivateKey(), serverCredentials.getCertificateChain());
		    
		        
		        //interface for trusted certificate authentication.
		        //basically inserting the CA that signed the server and client certificate
		        NewAdvancedCertificateVerifier trust = StaticNewAdvancedCertificateVerifier.builder().setTrustedCertificates(caCredentials).setTrustAllCertificates().build();
		        
		        //Define a type of allowed cipher suites
		        //For certificates created with the RSA algorithm, use the first option (TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384)
		        builder.setAsList(DtlsConfig.DTLS_CIPHER_SUITES, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384);
		        //For certificates created with the ECDSA algorithm, use the second option (TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384)
	            //builder.setAsList(DtlsConfig.DTLS_CIPHER_SUITES, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384);
	            builder.setAdvancedCertificateVerifier(trust);
	            builder.setCertificateIdentityProvider(identity);

	            //creating and managing DTLS connections
		        DTLSConnector connector = new DTLSConnector(builder.build());
		        
				System.out.println("CoAP server is running securely on port 5684.");
				
				//creation and configuration the endpoint
	            CoapEndpoint endpoint = new CoapEndpoint.Builder().setConnector(connector).build();
	            
	        		        	
			    CoapServer server = new CoapServer();
	            server.addEndpoint(endpoint);


	            System.out.println("Waits for client connection...");
	            
	            //Starting the CoAP server
	            server.start();
	            
				
				//Sending binary files to client
				server.add(new CoapResource("VehicleCommunication") {
	    			@Override
	    			public void handleGET(CoapExchange exchangefile) {
	    				
	    				System.out.println("CoAP Client connected securly. ");
	    				
	    				while(true) {
	    				
	                    try {
	                    	//List of binary files that can be send
		            		//In normal use there is a while loop for sending files until i enter exit, the program is waiting for user input
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
		  	                Logger.log("File: " + fileName + " sent at: " + " Time: " + sendTime, "coap_server_dtls.log");

		  	              //Read binary file 
	                        File file = new File(fileName);
	                        if (file.exists() && !file.isDirectory()) {
	                            FileInputStream fis = new FileInputStream(file);
	                            byte[] data = new byte[(int) file.length()];
	                            fis.read(data);
	                            fis.close();
	                            
	                          //Send binary file
	                            exchangefile.respond(ResponseCode.CONTENT, data);
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
	      					  endpoint.destroy();
	      					//Record the end of communication to the log 
	      					  LocalDateTime endTime = LocalDateTime.now();
	      					  Logger.log("Communication with the CoAP client has been terminated: " + " Time: " + endTime + "\n", "coap_server_dtls.log");
	      					  System.out.println("Disconnected from CoAP client");
	      					  break; //exit the loop
	      					 }

	                    } catch (IOException e) {
	                        System.out.println("Error: " + e);
	                        exchangefile.respond(ResponseCode.INTERNAL_SERVER_ERROR, "Error occurred");
	                    }
	    			
	    				}
	                }
	              });


	        } catch (Exception e) {
	            System.out.println("Error: " + e);
	        }
	    }
	}

