import java.io.*; 
import java.net.*;
import java.util.*;


public class FTPServer{
 
public static void main(String[] args) throws IOException {
            //String fromClient;
            //String clientCommand = "";
            byte[] data;
	    final int controlPort = 12000;
            ServerSocket welcomeSocket = null;
	    Socket connectionSocket = null;
            //Socket dataSocket = null;
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
              	connectionSocket = welcomeSocket.accept();
		
		/*	
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
            	
		//read client command
                fromClient = inFromClient.readUTF();
                 
                StringTokenizer tokens = new StringTokenizer(fromClient);
                clientCommand = tokens.nextToken();
		nextConnection = connectionSocket.getInetAddress().getHostAddress();
                port = inFromClient.readInt();

	      	System.out.println("Command "+clientCommand+" received from "+nextConnection+":"+port);	
		if (clientCommand.equals("connect")) {
			System.out.println("Received connection from: "+nextConnection+":"+port);	
		}
		else if (clientCommand.equals("list")) {
               		
			dataSocket = new Socket(nextConnection, port);
                      	//DataOutputStream  dataOutToClient = new DataOutputStream(new BufferedOutputStream(dataSocket.getOutputStream()));
               */
			try {
			ClientCommand request = new ClientCommand(connectionSocket);
			Thread requestThread = new Thread(request); 
			requestThread.start();
			}
			catch (Exception e) {
				System.out.println(e);
			}
		/*
		} 
		else if (clientCommand.equals("retr:")) {
                	
     		}
		else if (clientCommand.equals("stor:")) {

		}
     		//cleanup
		outToClient.close();
		inFromClient.close();	
		*/
	}
	welcomeSocket.close();
}  

private static class ClientCommand implements Runnable {
	Socket controlSocket;
	Socket dataSocket;
        String fromClient;
        String clientCommand = "";
	String nextConnection;
        int port;
	DataOutputStream outToClient;
	DataInputStream inFromClient;

	public ClientCommand(Socket ctrlSocket) throws Exception {
		controlSocket = ctrlSocket;

		outToClient = new DataOutputStream(controlSocket.getOutputStream());
                inFromClient = new DataInputStream(controlSocket.getInputStream());

		System.out.println("Client thread started.");
		//cmd = inFromClient.readUTF();
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
		
                //DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
			
		//if there's data to read
		while(true) {
		while (inFromClient.available() > 0) {
		
		//read command
                fromClient = inFromClient.readUTF();
                 System.out.println(fromClient);
                StringTokenizer tokens = new StringTokenizer(fromClient);
                clientCommand = tokens.nextToken();
		nextConnection = controlSocket.getInetAddress().getHostAddress();
				//String fileName = tokens.nextToken();
		
		//read port
                port = inFromClient.readInt();
		
	        System.out.println("Command "+clientCommand+" received from "+nextConnection+":"+port);

		switch(clientCommand) {
			case "connect":
				System.out.println("Received connection from: "+nextConnection+":"+port);	

				break;
			case "list":

				//establish data connection
				dataSocket = new Socket(nextConnection, port);
                		DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

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

				dataOutToClient.close();
        			dataSocket.close();
				break;

			case "stor:":
			fromClient = inFromClient.readUTF();
			System.out.println(fromClient);
		   StringTokenizer token2 = new StringTokenizer(fromClient);
		   clientCommand = token2.nextToken();
		   String fileName = token2.nextToken();
   			nextConnection = controlSocket.getInetAddress().getHostAddress();	
	   
				if(file.createNewFile()){
					FileOutputStream intoFile = new FileOutputStream(fromClient);
					//establish data connection
					dataSocket = new Socket(nextConnection, port);
					dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
					intoFile.write(fromClient.getBytes());
					intoFile.flush();
					intoFile.close();
				}
				break;

			case "retr:":
			fromClient = inFromClient.readUTF();
                 System.out.println(fromClient);
                StringTokenizer token3 = new StringTokenizer(fromClient);
				clientCommand = token3.nextToken();
				fileName = token3.nextToken();
		nextConnection = controlSocket.getInetAddress().getHostAddress();	
			
		//establish data connection
				dataSocket = new Socket(nextConnection, port);
				dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
				currDir = new File(".");
				fileList = currDir.listFiles();
					for(File f : fileList) {
						if(f.getName() == fileName){
							dataOutToClient.writeInt(200);
							dataOutToClient.writeUTF("ok");
							try(BufferedReader br = new BufferedReader(new FileReader(f))){
								String line;
								while((line = br.readLine()) != null){
								dataOutToClient.writeUTF(line);
								}
							}
						}else{
							dataOutToClient.writeInt(550);
						}
					}
					dataOutToClient.close();
					
					dataSocket.close();
				
					break;
		case "quit":
			controlSocket.clost();
		break;
		}

		}
		}
	//dataOutToClient.close();
        //dataSocket.close();
	//System.out.println("Thread terminated");
	}
}
}
