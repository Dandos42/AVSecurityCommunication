import java.security.cert.Certificate;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.elements.util.SslContextUtil.Credentials;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConfig;
import org.eclipse.californium.scandium.config.DtlsConfig.DtlsRole;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.x509.NewAdvancedCertificateVerifier;
import org.eclipse.californium.scandium.dtls.x509.SingleCertificateProvider;
import org.eclipse.californium.scandium.dtls.x509.StaticNewAdvancedCertificateVerifier;

public class ClientCoapDtls {
	
	public void ConnectionToServerOverCoAPwithDTLS() {
		
		//Registration of UDP CoAP and DTLS protocol configuration parameters
		CoapConfig.register();
  		DtlsConfig.register();
  		UdpConfig.register();
  		Configuration conf = Configuration.getStandard();
		
  		//Declaration
        String serverSocket = "coaps://AVcommunication.com:5684/"; //Set IP address + port 
        String source = "VehicleCommunication"; //Name of source
        Scanner scanner = new Scanner(System.in);
        String end;
        
        try {
        		//You can use CoAP_client_RSA.p12 - it client cert with RSA 2048
            	//You can use CoAP_client_ECDSA.p12 - it client cert with ECDSA prime256v1 and hash sha256
        	
        	  	String cert = "CoAP_client_RSA.p12";
        	  	//String cert = "CoAP_client_ECDSA.p12";
	            String alias = "ClientCoAP";
	            char[] keystorePassword = "test123".toCharArray(); //the password that was created during the export
	            char[] keyPassword = "test123".toCharArray(); //the password that was created during the export
        		
	            //Load CA certificate
    			//You can use localhostCA.crt - it CA cert with RSA 2048
    			//You can use CA_ECDSA.crt - it CA cert with ECDSA prime256v1 and hash sha256
	            
	            String truststore = "localhostCA.crt";
	            //String truststore = "CA_ECDSA.crt";
	            
	            //Record the start of communication to the log 
	         	LocalDateTime startTime = LocalDateTime.now();
	         	Logger.log("Communication with the CoAP server has been started: " + " Time: " + startTime, "coap_client_dtls.log");
	            
	            //Reading a certificate and private key from a PKCS12 file
	            Credentials serverCredentials = SslContextUtil.loadCredentials(cert,alias,keystorePassword,keyPassword);
	            //Reading a CA file
	            Certificate[] caCredentials = SslContextUtil.loadTrustedCertificates(truststore);
	            
 	            //create configuration for DTLS connection
		        DtlsConnectorConfig.Builder builder = DtlsConnectorConfig.builder(conf);
		        
		        //create a server identity that is used for authentication.
		        //GET the key and parameters from the certificate
		        SingleCertificateProvider identity = new SingleCertificateProvider(serverCredentials.getPrivateKey(), serverCredentials.getCertificateChain());
		        
		        //interface for trusted certificate authentication.
		        //basically inserting the CA that signed the server and client certificate
		        NewAdvancedCertificateVerifier trust = StaticNewAdvancedCertificateVerifier.builder().setTrustedCertificates(caCredentials).setTrustAllCertificates().build();
		        
		        //setting the role on the client type
		        builder.set(DtlsConfig.DTLS_ROLE, DtlsRole.CLIENT_ONLY);
		        //Define a type of allowed cipher suites
		       // for certificates created with the RSA algorithm, use the first option (TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384)
		        builder.setAsList(DtlsConfig.DTLS_CIPHER_SUITES, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384);
		       // for certificates created with the ECDSA algorithm, use the second option (TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384)
		       // builder.setAsList(DtlsConfig.DTLS_CIPHER_SUITES, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384);
		        builder.setCertificateIdentityProvider(identity);
	            builder.setAdvancedCertificateVerifier(trust);	
	            
	            //creating and managing DTLS connections
	            DTLSConnector connector = new DTLSConnector(builder.build());

            System.out.println("CoAP client is running securely.");
            
            //creation and configuration the endpoint
            CoapEndpoint endpoint = new CoapEndpoint.Builder().setConnector(connector).build();
  
            //Launching the endpoint
            endpoint.start();       

         	
          //Create client instance
         //setting the ip address of the server and the topic for receiving message
            CoapClient client = new CoapClient();
          client.setURI(serverSocket+source);
            //adding a client endpoint
            client.setEndpoint(endpoint);
         
         // Send the GET request to the server to exchange binary files
            CoapResponse responseFile = client.get();
            if (responseFile != null && responseFile.isSuccess()) {
            	System.out.println("Connected to the CoAP server on port 5684");
   
                	//Get binary file and save
                	FileSaver.SaveFileCoapDTLS(responseFile.getPayload());
                	System.out.println("If you want to end type 'exit': ");
                    end = scanner.nextLine();
                    
                    if(end.equalsIgnoreCase("exit"))
                    {
                    	//Disconnected from CoAP server
                    	client.shutdown();
                    	endpoint.destroy();
                    	//Record the end of communication to the log 
                    	LocalDateTime endTime = LocalDateTime.now();
    					Logger.log("Communication with the CoAP server has been terminated: " + " Time: " + endTime + "\n", "coap_client_dtls.log");
                        System.out.println("Disconnected from CoAP server");
         
                    }
                }
                else {
                    System.err.println("Failed to exchange binary file with the server.");
                    return;
                    
                }
         	
        }catch (Exception e) {
			// TODO: handle exception
            System.err.println("Error communicating with the server: " + e.getMessage());

		}
       

	}
}
        
        
      
            