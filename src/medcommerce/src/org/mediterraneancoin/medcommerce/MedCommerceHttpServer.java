package org.mediterraneancoin.medcommerce;

import java.net.URL;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 *
 * @author test
 */
public class MedCommerceHttpServer {
    
    public static boolean DEBUG = false;

    public static void main(String[] args) throws Exception
    {
        
        int localport;
 
        
        String bindAddress = "localhost";
        localport = 8080;        

        int i = 0;
         

         while (i < args.length) {

             if (args[i].equals("-b")) {
                 i++;
                 bindAddress = args[i];
             }  else if (args[i].equals("-l")) {
                 i++;
                 localport = Integer.parseInt(args[i]);
             } else if (args[i].equals("-h") || args[i].equals("--help")) {
                   System.out.println("parameters:\n" +
                           "-s: hostname of wallet/pool (default: localhost)\n" + 
                           "-p: port of wallet/pool (default: 9372)\n" + 
                           "-b: bind to local address (default: )\n" +
                           "-l: local proxy port (default: 8080)\n" + 
                           "-t: min delta time (default: 50 ms)\n" + 
                           "-m: mininum queue length (default: 4)\n" + 
                           "-v: verbose"
                           );
                   return;                 
             } else if (args[i].equals("-v")) {
                 DEBUG = true;
             }
  
             i++;
         }       
        
        System.out.println("MEDcommerce server");
        System.out.println("parameters:\n" + 
                "bind to local address: " + bindAddress + "\n" +
                "local proxy port: " + localport + "\n"
                );
 
 
        QueuedThreadPool threadPool = new QueuedThreadPool(9,1);
       
        // default port: 8080
        Server server = new Server(threadPool);
        
        //server.addBean(new ScheduledExecutorScheduler());
        server.manage(threadPool);
 
         
    
        ServerConnector connector = new ServerConnector(server, 4, 4);
 
        connector.setHost(bindAddress);
        connector.setPort(localport);
        connector.setIdleTimeout(30000);
        connector.setStopTimeout(40000);
        
        System.out.println( "connector.getAcceptors(): " +  connector.getAcceptors() );
        System.out.println( "connector.getAcceptQueueSize(): " + connector.getAcceptQueueSize()) ;
 
        
        ServletHandler handler = new ServletHandler();
        
        
        server.setHandler(handler);
        handler.addServletWithMapping(MedCommerceServlet.class, "/*"); 
          
        
        server.addConnector(connector);
 
 
        server.start();
         
        server.join();
        
        
        
        
       
    }        
    
}
