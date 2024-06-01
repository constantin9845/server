import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread{
	private Socket socket;
	private BufferedReader inFromClient;
	private PrintWriter outToClient;


	public ConnectionHandler(Socket socket){
		this.socket = socket;
		
		try{
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new PrintWriter(socket.getOutputStream(), true);
			
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			
		}
	}

	public void run(){
		try{
			handleConnection();	
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			clean();
		}
		
	}

	public void handleConnection() throws IOException{
		while(true){
		
			char line = inFromClient.read();
			// File transfer request
			if(line == '2'){
				outToClient.println(line);
				outToClient.flush();
			}
			// Message transfer request
			else if(line == '1'){
				System.out.println("\n\t|User request for message received. waiting for data.");
				
				String message = inFromClient.readLine();
				System.out.println("\n\t|User message: "+message);
				outToClient.println("Message received.");
			}
			// Empty input
			else if(line == '3'){
				outToClient.println("Server could not handle request. Userinput empty.");
			}
			// Error with input
			else{
				System.out.println("\n\t|Something went wrong with user request");
				outToClient.println("Server could not handle request. Check user input again.");
			}
			
		}
	}
	
	public void clean(){
		try{
			inFromClient.close();
			outToClient.close();
			socket.close();
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}