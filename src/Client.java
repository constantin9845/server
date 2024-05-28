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
			
			while((clientInput = readInput()) != null || !clientInput.equals("quit")){
				outToServer.println(clientInput);
				outToServer.flush();
				
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
		System.out.print("\nEnter sentence: ");
		String line = sc.nextLine();
		return line;
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














