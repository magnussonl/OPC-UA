/**
 * Prosys OPC UA Java SDK
 *
 * Copyright (c) Prosys PMS Ltd., <http://www.prosysopc.com>. All rights
 * reserved.
 */
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.UaClient;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * A very minimal client application. Connects to the server and reads one
 * variable. Works with a non-secure connection.
 */
public class SimpleClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UaClient client = new UaClient("opc.tcp://192.168.1.145:26543/NodeOPCUA-Server");
            client.setSecurityMode(SecurityMode.NONE);
            initialize(client);
            client.connect();
            Double d = new Double(3);
            NodeId nodeid = new NodeId(1, "FanSpeed");
            
            
            for (int i = 0; i < 10; i++) {
                DataValue value = client.readValue(nodeid, d);
                System.out.println(value);
                Thread.sleep(1000);
            }
            
            
            client.disconnect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(SimpleClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecureIdentityException ex) {
            Logger.getLogger(SimpleClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(SimpleClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StatusException ex) {
            Logger.getLogger(SimpleClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimpleClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Define a minimal ApplicationIdentity. If you use secure connections, you
     * will also need to define the application instance certificate and manage
     * server certificates. See the SampleConsoleClient.initialize() for a full
     * example of that.
     */
    protected static void initialize(UaClient client)
            throws SecureIdentityException, IOException, UnknownHostException {
        // *** Application Description is sent to the server
        ApplicationDescription appDescription = new ApplicationDescription();
        appDescription.setApplicationName(new LocalizedText("SimpleClient", Locale.ENGLISH));
        // 'localhost' (all lower case) in the URI is converted to the actual
        // host name of the computer in which the application is run
        appDescription.setApplicationUri("urn:localhost:UA:SimpleClient");
        appDescription.setProductUri("urn:prosysopc.com:UA:SimpleClient");
        appDescription.setApplicationType(ApplicationType.Client);

        final ApplicationIdentity identity = new ApplicationIdentity();
        identity.setApplicationDescription(appDescription);
        client.setApplicationIdentity(identity);
    }

}
