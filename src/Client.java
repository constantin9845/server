import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
	private Socket socket;
	private String host;
	private int port;
	private BufferedReader inFromServer;
	private BufferedReader inFromKeyboard;
	private PrintWriter outToServer;
	private Scanner sc;
	
	public Client(String host, int port){
		this.host = host;
		this.port = port;
		this.sc = new Scanner(System.in);
		requestService();
	}
	
	public void requestService(){
		try{
			this.socket = new Socket(this.host, this.port);
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			inFromKeyboard = new BufferedReader(new InputStreamReader(System.in));
			outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String clientInput;

			System.out.printf("\n\t|1: text input ; 2: file input ; 3: quit: ");

			while((clientInput = readInput()) != null || !clientInput.equals("quit")){

				// Message mode
				if(clientInput.equals("Text input")){
					outToServer.println("Message transfer requested");
					outToServer.flush();
					
					outToServer.println(clientInput);
					outToServer.flush();
				}
				// File mode
				else if(clientInput.equals("File input")){
					utToServer.println("File is being transfered");
					outToServer.flush();
				}
				// Empty input provided
				else if(clientInput.equals("Empty input")){
					outToServer.println("No input provided");
					outToServer.flush();
				}
				// error
				else{
					outToServer.println("Something went wrong with user input");
					outToServer.flush();
				}
				
				System.out.printf("\n|Message from server: <<%s>>", inFromServer.readLine());
			}
			
			clean();
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void clean(){
		try{
			inFromServer.close();
			inFromKeyboard.close();
			outToServer.close();
			socket.close();
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public String readInput(){
		System.out.print("\n\t|: ");
		String line = sc.nextLine();

		// text input
		if(line.length() == 1 && line.charAt(0) == '1'){
			return "Text input";
		}
		// file input
		else if(line.length() == 1 && line.charAt(0) == '2'){
			return "File input";
		}
		// quit
		else if(line.length() == 1 && line.charAt(0) == '3'){
			return "quit";
		}
		// empty input
		else if(line.length() == 0){
			return "Empty input";
		}
		else{
			return "Unknown input."
		}

	}
	
	public static void main(String[] args){
		if(args.length != 2){
			System.exit(1);
		}
		
		Client client = new Client(
			args[0],
			Integer.parseInt(args[1])
		);
	}
}














