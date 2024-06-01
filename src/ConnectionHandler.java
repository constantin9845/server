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
		
			String line = inFromClient.readLine();
			// File transfer request
			if(line.equals("File is being transfered")){
				outToClient.println(line);
				outToClient.flush();
			}
			// Message transfer request
			else if(line.equals("Message transfer requested")){
				String message = inFromClient.readLine();
				System.out.println("\n\t|User message: "+message);
				outToClient.println("\n\t|Message received.");
			}
			// Empty input
			else if(line.equals("No input provided")){
				outToClient.println("\n\t|Server could not handle request. Userinput empty.");
			}
			// Error with input
			else{
				System.out.println("\n\t|Something went wrong with user request");
				outToClient.println("\n\t|Server could not handle request. Check user input again.");
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