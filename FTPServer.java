import java.io.*; 
import java.net.*;
import java.util.*;


public class FTPServer{
 
public static void main(String[] args) throws IOException {
            //String fromClient;
            //String clientCommand = "";
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
				
		   //IF the below line is commented out, the error becomes an IOException
		   String fileName = tokens.nextToken();
		   //Currently, causes NoSuchElementException error


		   //System.out.println(fileName);
		   //File currentDirectory = new File("./"+fileName);
   			nextConnection = controlSocket.getInetAddress().getHostAddress();		
			   String relativePath = new File("").getAbsolutePath();	
			   File file = new File(relativePath);
				try{
					file.createNewFile();
					System.out.println(fileName + "created in current directory");
					//establish data connection
					dataSocket = new Socket(nextConnection, port);
					FileOutputStream fileIn = new FileOutputStream("." + fileName);
					//int size = fileIn.readInt();
					//byte[] data = new byte[1024];
					BufferedReader dataIn = new BufferedReader(
				new InputStreamReader(dataSocket.getInputStream()));
					String nextLine = dataIn.readLine();
					if (nextLine.equals("EOF"))
						break;
						fileIn.write(nextLine.getBytes());
						fileIn.close();
						dataIn.close();
				//System.out.println(fileName + "Already in directory");
				}catch(Exception e){
					System.out.println(e);
				}
				dataSocket.close();
				break;

			case "retr:":
			fromClient = inFromClient.readUTF();
                 System.out.println("Attempting to find the file...");
				try{
				//establish data connection
				dataSocket = new Socket(nextConnection, port);
				fileName = tokens.nextToken();
				nextConnection = controlSocket.getInetAddress().getHostAddress();	
				dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
				currDir = new File(".");
				File newFile = new File(fileName);
				byte[] data = new byte[(int)newFile.length()];
				fileList = currDir.listFiles();
				FileInputStream fileIn = new FileInputStream(newFile);
				BufferedInputStream buffered = new BufferedInputStream(fileIn);
				DataInputStream inStream = new DataInputStream(buffered);
				for(File f : fileList) {
					if(f.getName() == fileName){
						System.out.println("File found");
						dataOutToClient.writeInt(200);
						dataOutToClient.writeUTF("OK");
						inStream.readFully(data, 0, data.length);
						dataOutToClient.writeInt(data.length);
						dataOutToClient.write(data, 0, data.length);
						inStream.close();
						dataSocket.close();
						dataOutToClient.flush();
					System.out.println("File finished sending to Client.");
					}else{
							dataOutToClient.writeInt(550);
						}
					}
					dataOutToClient.close();
				}catch(Exception e){
					System.out.println(e);
				}
					
					dataSocket.close();
					break;
		case "quit":
			controlSocket.close();
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
