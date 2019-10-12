import java.io.*; 
import java.net.*;
import java.util.*;


public class FTPServer{
 
public static void main(String[] args) throws IOException {
            String fromClient;
            String clientCommand = "";
            byte[] data;
            int port;
	    final int controlPort = 12000;
	    String nextConnection;
            ServerSocket welcomeSocket = null;
            Socket dataSocket = null;
            boolean isOpen = false;

          try {
            welcomeSocket = new ServerSocket(controlPort);
            isOpen = true;
	    System.out.println("Server set up on port "+controlPort);
          }catch(IOException ioEx){
            System.out.println("\nUnable to set up port \n");
            System.exit(1);
          }
            String frstln;
        
	while(isOpen) {
              	Socket connectionSocket = welcomeSocket.accept();
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
            	
		//read client command
                fromClient = inFromClient.readUTF();
                 
                StringTokenizer tokens = new StringTokenizer(fromClient);
                //frstln = tokens.nextToken();
                //port = Integer.parseInt(frstln);
                clientCommand = tokens.nextToken();
		nextConnection = connectionSocket.getInetAddress().getHostAddress();
                port = inFromClient.readInt();

	      	System.out.println("Command "+clientCommand+" received from "+nextConnection+":"+port);	
                //if (clientCommand.equals("connect") {

		//	}
		if (clientCommand.equals("list")) {
               		
			dataSocket = new Socket(nextConnection, port);
                      	//DataOutputStream  dataOutToClient = new DataOutputStream(new BufferedOutputStream(dataSocket.getOutputStream()));
                      	/*
			dataOutToClient.writeInt(port);
                        File currDir = new File(".");
                        File[] fileList = currDir.listFiles();
                        for(File f : fileList){
                            //if(f.isDirectory())
                                //System.out.println("TO BUFFER");
                            if(f.isFile())
                              dataOutToClient.writeUTF(f.getName());
                        }
			*/
			try {
			ClientCommand request = new ClientCommand(clientCommand, dataSocket);
			Thread requestThread = new Thread(request); 
			requestThread.start();
			}
			catch (Exception e) {
				System.out.println(e);
			}
		} 
		else if (clientCommand.equals("retr:")) {
                	
     		}
     		//cleanup
		outToClient.close();
		inFromClient.close();	
	}
	welcomeSocket.close();
}  

private static class ClientCommand implements Runnable {
	String command;
	Socket dataSocket;

	public ClientCommand(String cmd, Socket clientSocket) throws Exception {
		command = cmd;
		dataSocket = clientSocket;
	}

	public void run() {
		try {
		processRequest();
		}
		catch (Exception e) {
		System.out.println(e);
		}
	}

	private void processRequest() throws Exception {
		
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
		
		switch(command) {
			case "list":
				File currDir = new File(".");
                        	File[] fileList = currDir.listFiles();
                        	for(File f : fileList) {
                            //if(f.isDirectory())
                                //System.out.println("TO BUFFER");
                            	if(f.isFile()) {	
                              		//System.out.println("Wrote "+f.getName());
					dataOutToClient.writeUTF(f.getName());
					}
				}
				dataOutToClient.writeUTF("EOF");
				break;

			case "stor:":

				break;

			case "retr:":

				break;
		}
	
	dataOutToClient.close();
        dataSocket.close();
	System.out.println("Thread terminated");
	}
}
}
