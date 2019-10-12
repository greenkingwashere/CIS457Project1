import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

class FTPClient { 
	
public static void main(String argv[]) throws Exception
    { 
    String sentence; 
    String modifiedSentence; 
    String statusCode;
    String fileName;
    StringTokenizer tokens;
    
   	int controlPort = 12000;
	int command_port = controlPort + 2;
	
	boolean isOpen = true;
	boolean connectionEstablished = false;
	boolean notEnd = true;
	boolean fileExists = false;
	
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;
	DataInputStream inData = null;
	
	ServerSocket welcomeData = null;
	Socket dataSocket = null;
	Socket ControlSocket = null;
	
	//byte[] fileContents;

	System.out.println("|| FTP Client Project 1 ~ CIS 457 ||");
	//wait for user input
	/*
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    	sentence = inFromUser.readLine();
    	StringTokenizer tokens = new StringTokenizer(sentence);

	String serverName = tokens.nextToken();
	controlPort = Integer.parseInt(tokens.nextToken());
	
    Socket ControlSocket = null;
    try {
    	System.out.println("Connecting to " + serverName + ":" + controlPort);
    	ControlSocket = new Socket(serverName, controlPort);
    	System.out.println("You are connected to " + serverName + ":" + controlPort);
    	isOpen = true;
    }
    catch (Exception e) {
    	System.out.println("Failed to set up socket.");
    }
    */
        //DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());
        //DataInputStream inFromServer = new DataInputStream(ControlSocket.getInputStream());

	while(isOpen) {

        //outToServer = new DataOutputStream(ControlSocket.getOutputStream());
        //inFromServer = new DataInputStream(ControlSocket.getInputStream());
          
        System.out.println("Input next command:");
    	
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	sentence = inFromUser.readLine();
    	tokens = new StringTokenizer(sentence);
    	String command = tokens.nextToken();
	//outToServer.writeUTF(command);
        
	if (command.equals("connect")) {
		
    		//sentence = inFromUser.readLine();
		String serverName = tokens.nextToken();
		controlPort = Integer.parseInt(tokens.nextToken());

    		try {
    			System.out.println("Connecting to " + serverName + ":" + controlPort);
				
    			if (ControlSocket != null) {
    				outToServer.close();
    				inFromServer.close();
    				ControlSocket.close();
    			}
			
    			ControlSocket = new Socket(serverName, controlPort);
    			System.out.println("You are connected to " + serverName + ":" + controlPort);
			connectionEstablished = true;

        		outToServer = new DataOutputStream(ControlSocket.getOutputStream());
        		inFromServer = new DataInputStream(ControlSocket.getInputStream());
    		}
    		catch (Exception e) {
    			System.out.println("Failed to set up socket.");
			System.out.println(e);
			isOpen = false;
		}

	}	
         else if(sentence.equals("quit")) {
        	 isOpen = false;
        	 System.out.println("Have a nice day!");
         }
	else if (connectionEstablished)
		{
		if(command.equals("list")) {
		
		command_port += 2;
		//outToServer.flush();
		outToServer.writeUTF(command);
		outToServer.writeInt(command_port);
		System.out.println("A POINT: "+command_port);
		//sendCommand(command);
		//outToServer.writeBytes (port + " " + sentence + " " + '\n');
		//controlPort = inFromServer.readInt();

		try {
		welcomeData = new ServerSocket(command_port);
		dataSocket = welcomeData.accept();
	 	inData = new DataInputStream(dataSocket.getInputStream());
		}
		catch (Exception e) {
		System.out.println(e);
		}

	 	//notEnd = true;
	        System.out.println("\nListing files on port "+command_port);
		while (true) {
		    try {
		    modifiedSentence = inData.readUTF();
	            //if (modifiedSentence == null)
	            //	notEnd = false;
	            //else
	            if (modifiedSentence.equals("EOF"))
			break;

		    System.out.println(modifiedSentence);
		    }
		    catch (EOFException e) {}
		    }
 
		System.out.println("\nAll files displayed.");
		inData.close();
		dataSocket.close();
		welcomeData.close();
	        }
         else if(command.equals("retr:")) {
        	/*
        	fileName = tokens.nextToken();
        	
        	ServerSocket welcomeFile = new ServerSocket(command_port);
		    Socket fileSocket = welcomeFile.accept();
		    DataInputStream dataIn = new DataInputStream(fileSocket.getInputStream());
		    
		    fileExists = true;
		    FileOutputStream fileOut = null;
		    try {
		    	fileOut = new FileOutputStream("."+fileName);
		    }
		    catch (FileNotFoundException e) {
		    	System.out.println("Requested file is already a directory.");
		    	fileExists = false;
		    }
		    			    
		    if (fileExists) {
		    	fileContents = null;
		    	dataIn.read(fileContents);
		    	fileOut.write(fileContents);
		    	System.out.println("Wrote file "+fileName+" successfully.");
		    }
		welcomeFile.close();
		fileSocket.close();
         */
	 }
         else if(sentence.equals("stor:")) {
        	fileName = tokens.nextToken();

         }
	}
    }
    if (ControlSocket != null) {
    	outToServer.close();
    	inFromServer.close();
    	ControlSocket.close();
    }
}    
}


/*
private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
	// Construct a 1K buffer to hold bytes on their way to the socket.
	byte[] buffer = new byte[1024];
	int bytes = 0;
	
	// Copy requested file into the socket's output stream.
	while ((bytes = fis.read(buffer)) != -1)
	    os.write(buffer, 0, bytes);
}
*/
/*
private static void sendCommand(String cmd) {
	command_port += 2;
	outToServer.writeUTF(cmd);
	outToServer.writeInt(command_port);
	//outToServer.writeBytes(command_port + " " + cmd + " " + '\n');
}
*/
/*
private void waitForServer() {
	while (
}
*/
