import java.io.*; 
import java.net.*;
import java.util.*;


public class FTPServer{
  public static void main(String[] args) throws IOException{
            String fromClient;
            String clientCommand = "";
            byte[] data;
            int port;
            ServerSocket welcomeSocket = null;
            Socket dataSocket = null;
            boolean isOpen = false;

          try {
            welcomeSocket = new ServerSocket(12000);
            isOpen = true;
          }catch(IOException ioEx){
            System.out.println("\nUnable to set up port \n");
            System.exit(1);
          }
            String frstln;
        
          while(isOpen)
            {
                Socket connectionSocket = welcomeSocket.accept();
                DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            
                  fromClient = inFromClient.readLine();
                 
                  StringTokenizer tokens = new StringTokenizer(fromClient);
                  frstln = tokens.nextToken();
                  port = Integer.parseInt(frstln);
                  clientCommand = tokens.nextToken();
                  
                  if(clientCommand.equals("list"))
                  {        
                      dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                      DataOutputStream  dataOutToClient = new DataOutputStream(new BufferedOutputStream(dataSocket.getOutputStream()));
                      dataOutToClient.writeInt(port);
                        File currDir = new File(".");
                        File[] fileList = currDir.listFiles();
                        for(File f : fileList){
                            //if(f.isDirectory())
                                //System.out.println("TO BUFFER");
                            if(f.isFile())
                              dataOutToClient.writeUTF(f.getName());
                        }

     
                          }

                           dataSocket.close();
			   System.out.println("Data Socket closed");
                     }
        
			//Text HERE
             
                if(clientCommand.equals("retr:"))
                {
                //FiLL IN
     }
     welcomeSocket.close();
    }
}   