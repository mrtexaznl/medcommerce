package org.mediterraneancoin.medcommerce.test;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author test
 */
public class TestRpc {
    
    public TestRpc() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test 
    public void doGetnewaddress() throws MalformedURLException, BitcoinException {
        
        /*
        // if wallet in not on same host, use this: 
         
        String medUser = "mediterraneancoinrpc";
        String medPassword = "xxxxxxxxx";
        String medPort = "9372";
        
        String host = "remotewallet";
        
        URL medUrl = new URL("http://"+medUser+':'+medPassword+"@"+host+":"+(medPort==null?"9372":medPort)+"/");
        
                
        BitcoinJSONRPCClient client  = new BitcoinJSONRPCClient(medUrl);
        */
        
        BitcoinJSONRPCClient client  = new BitcoinJSONRPCClient(false);
        
        String newAddress = client.getNewAddress();
        
        System.out.println(newAddress);
        
    }
}