package org.mediterraneancoin.medcommerce.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import org.json.rpc.client.HttpJsonRpcClientTransport;
import org.json.rpc.client.JsonRpcInvoker;
 
 
 

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
public class TestClient {
    
    private ByteArrayOutputStream baos;
 
    
    public TestClient() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() { 
        baos = new ByteArrayOutputStream();        
    }
    
    @After
    public void tearDown() {
        //client = null;

    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
 
    private class JsonRpcAnswer {
        
        
        
    }

    
    @Test
    public void getNewAddress() throws Throwable {
        
        // where the servlet is hosted
        String url = "http://127.0.0.1:8080/"; 

        HttpJsonRpcClientTransport transport = new HttpJsonRpcClientTransport(new URL(url));

        JsonRpcInvoker invoker = new JsonRpcInvoker();
        
        String request = "{\"method\": \"getnewaddress\", \"params\": [], \"id\": 1}";

        String call = transport.call(request);

        //Calculator calc = invoker.get(transport, "calc", Calculator.class);

        //double result = calc.add(1.2, 7.5);        

        System.out.println("REQUEST: " + request);
        
        System.out.println("ANSWER: " + call);
    }
    
    @Test
    public void getLastNTransactionsForAddress() throws Throwable {
        
        String url = "http://127.0.0.1:8080/"; 

        HttpJsonRpcClientTransport transport = new HttpJsonRpcClientTransport(new URL(url));

        JsonRpcInvoker invoker = new JsonRpcInvoker();
        
        String request = "{\"method\": \"getlastntransactionsforaddress\", \"params\": [\"Mb9hgh4ThWUdSaUucYNqEzTVuBWHq5Q2W1\", 5], \"id\": 1}";

        String call = transport.call( request );        
        
        
        System.out.println("REQUEST: " + request);
        
        System.out.println("ANSWER: " + call);
    }
        
        
    
    
}