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
    
    int port = 12000;
	
	boolean isOpen = false;
	boolean clientgo = true;
	boolean notEnd = true;
	boolean fileExists = false;
	
	byte[] fileContents;
	    
	System.out.println("|| FTP Client Project 1 ~ CIS 457 ||\nEnter server address: (IP/server name) (port)");
	//wait for user input
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    sentence = inFromUser.readLine();
    StringTokenizer tokens = new StringTokenizer(sentence);

	String serverName = tokens.nextToken();
	int controlPort = Integer.parseInt(tokens.nextToken());
    
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
    
	while(isOpen && clientgo) {
        DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(
        	new BufferedInputStream (ControlSocket.getInputStream()));
          
        System.out.println("Input next command:");
    	sentence = inFromUser.readLine();
    	tokens = new StringTokenizer(sentence);
    	String command = tokens.nextToken();
    	
        if(command.equals("list")) {
		    port += 2; //should wrap back to initial value?
		    outToServer.writeBytes (port + " " + sentence + " " + '\n');
		    
	        ServerSocket welcomeData = new ServerSocket(port);
		    Socket dataSocket = welcomeData.accept();
	 	    DataInputStream inData = new DataInputStream(
	 	    	new BufferedInputStream(dataSocket.getInputStream()));
	 	    
	 	    notEnd = true;
	        while (notEnd) {
	            modifiedSentence = inData.readUTF();
	            if (modifiedSentence != null)
	            	notEnd = false;
	            else
	            	System.out.println(modifiedSentence);
	            }
	        
			welcomeData.close();
			dataSocket.close();
	        }
         else if(command.equals("retr:")) {
        	
        	fileName = tokens.nextToken();
        	
        	ServerSocket welcomeFile = new ServerSocket(port);
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
         }
         else if(sentence.equals("stor:")) {
        	 
         }
         else if(sentence.equals("quit")) {
        	 isOpen = false;
        	 System.out.println("Have a nice day!");
         }
    }
}

private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
	// Construct a 1K buffer to hold bytes on their way to the socket.
	byte[] buffer = new byte[1024];
	int bytes = 0;
	
	// Copy requested file into the socket's output stream.
	while ((bytes = fis.read(buffer)) != -1)
	    os.write(buffer, 0, bytes);
}
}