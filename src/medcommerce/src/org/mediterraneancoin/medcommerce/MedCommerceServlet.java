package org.mediterraneancoin.medcommerce;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
 

/**
 *
 * @author test
 */
public class MedCommerceServlet extends HttpServlet {
    
    
    public MedCommerceServlet() throws MalformedURLException {
        
        /*
        // if wallet in not on same host, use this: 
         
        String medUser = "mediterraneancoinrpc";
        String medPassword = "xxxxxxxxx";
        String medPort = "9372";
        
        String host = "remotewallet";
        
        URL medUrl = new URL("http://"+medUser+':'+medPassword+"@"+host+":"+(medPort==null?"9372":medPort)+"/");
        
                
        BitcoinJSONRPCClient client  = new BitcoinJSONRPCClient(medUrl);
        */
        
        // the library tries to find username and password from .mediterraneancoin/mediterraneancoin.conf (or corresponding directory if on windows)
        client  = new BitcoinJSONRPCClient(false);  
    }
 
    BitcoinJSONRPCClient client;
    //BitcoinJSONRPCClient client  = new BitcoinJSONRPCClient(false);
    
    final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (MedCommerceHttpServer.DEBUG) {  
            System.out.println("method: " + request.getMethod());
        }        
        
        String type = request.getContentType(); 
        
        String authHeader = request.getHeader("authorization");
        int contentLength = Integer.parseInt( request.getHeader("content-length") );
                
        

        
        byte cbuf[] = new byte[contentLength]; 
        
        request.getInputStream().read(cbuf);
        
        String content = new String(cbuf); 
 

        if (MedCommerceHttpServer.DEBUG) {  
          System.out.println("content-len: " + contentLength);          
          System.out.println("content: " + content);          
        }        
        


        ObjectNode node = null;

        try {
            node = (ObjectNode) mapper.readTree(content);
 
        } catch (IOException ex) {
            Logger.getLogger(MedCommerceServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 


        String jsonMethod = node.get("method").asText();

        if (MedCommerceHttpServer.DEBUG) {
          System.out.println("jsonMethod: " + jsonMethod);
        }
        
        String answer = ""; // {\"result\": \"\", \"error\": null, \"id\": 1}";
        
        if (jsonMethod.equals("getnewaddress")) {
            
            // {"method": "getnewaddress", "params": [], "id": 99}
            
            String id = node.get("id").asText();
            
            try {
                String newAddress = client.getNewAddress();
                
                answer = "{\"result\": \"" + newAddress + "\", \"error\": null, \"id\": " + id + "}";
                
                if (MedCommerceHttpServer.DEBUG) {
                    System.out.println("getnewaddress: " + newAddress);
                }
                
            } catch (BitcoinException ex) {
                Logger.getLogger(MedCommerceServlet.class.getName()).log(Level.SEVERE, null, ex);
                
                throw new ServletException(ex);
            }
            
            
        } else if (jsonMethod.equals("getlastntransactionsforaddress")) {
            try {
                
                // {"method": "getlastntransactionsforaddress", "params": ["Mb9hgh4ThWUdSaUucYNqEzTVuBWHq5Q2W1", 10], "id": 99}
                
                String id = node.get("id").asText();
                
                System.out.println( node.get("params").getNodeType() );
                
                String medAddress = node.get("params").get(0).asText();
                
                int lastN = 10;
                
                if (node.get("params").size() > 1) {
                    lastN = node.get("params").get(1).asInt();
                }
                
                //JsonNode result = mapper.createObjectNode();
                ArrayNode resultArray = mapper.createArrayNode();
                
                 
            
                // params: number of blocks to go back, address to look for
                // returns an array containing: block number, transaction, MED amount
            
                int currentBlockCount = client.getBlockCount();
                
                if (MedCommerceHttpServer.DEBUG)
                    System.out.println("currentBlockCount: " + currentBlockCount);
                
                for (int blockCount = currentBlockCount; blockCount > currentBlockCount - 10; blockCount--) {
                    String blockHash = client.getBlockHash(blockCount);

                    if (MedCommerceHttpServer.DEBUG)
                        System.out.println(blockCount + " " + blockHash);                    
                    
                    Bitcoin.Block block = client.getBlock(blockHash);
                    
                    List<String> transactions = block.tx();
                    
                    
                    if (transactions != null && transactions.size() > 0) {
                        ListIterator<String> listIterator = transactions.listIterator();
                    
                        int txid = 0;

                        while (listIterator.hasNext()) {
                            
                                String tx = listIterator.next();

                                if (MedCommerceHttpServer.DEBUG)
                                    System.out.print("***tx*** " + tx);

                                try {
                                
                                    Bitcoin.RawTransaction rawTransaction = client.getRawTransaction(tx);

                                    //System.out.println(rawTransaction);                            

                                    ListIterator<Bitcoin.RawTransaction.Out> voutIterator = rawTransaction.vOut().listIterator();

                                    while (voutIterator.hasNext()) {

                                        Bitcoin.RawTransaction.Out next = voutIterator.next();

                                        double txValue = next.value();

                                        if (MedCommerceHttpServer.DEBUG)
                                            System.out.println("value: " + txValue);

                                        ListIterator<String> addressesIterator = next.scriptPubKey().addresses().listIterator();

                                        while (addressesIterator.hasNext()) {

                                            String outAddress = addressesIterator.next();

                                            if (MedCommerceHttpServer.DEBUG)
                                                System.out.println("address: " + outAddress);

                                            if (medAddress.equals( outAddress )) {
                                                // add to result
                                                ArrayNode item = resultArray.addArray();

                                                item.add(blockCount);
                                                item.add(tx);
                                                item.add(txValue);

                                            }

                                        }



                                    }
                                
                                } catch (BitcoinException ex) {}

                            }
                    
                        
                    }
                    
                }
            
                answer = resultArray.toString();
            
            } catch (BitcoinException ex) {                
                ex.printStackTrace();
                throw new ServletException(ex);
            }
            
        }

        //String answer = "{\"result\": \"Hello JSON-RPC\", \"error\": null, \"id\": 1}";
          
        if (MedCommerceHttpServer.DEBUG)
            System.out.println("ANSWER: " + answer);
        
        
        response.setContentType("application/json");
        
        response.setStatus(HttpServletResponse.SC_OK);
        //baseRequest.setHandled(true);
        
        
        
        response.getWriter().println(answer);        
        
                
    }
    
}
