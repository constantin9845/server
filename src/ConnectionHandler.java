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

			int payloadSize = inFromClient.read();
			char[] payload = new char[payloadSize];

			int bytesRead = inFromClient.read(payload);
		
			for(int i = 0; i < payloadSize; i++){
				System.out.println(payload[i]);
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