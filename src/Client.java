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
	private char[] payload;

	private DataOutputStream fileToServer;
	
	public Client(String host, int port){
		this.host = host;
		this.port = port;
		this.sc = new Scanner(System.in);
		try{
			requestService();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void requestService() throws Exception{
		try{
			this.socket = new Socket(this.host, this.port);
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			inFromKeyboard = new BufferedReader(new InputStreamReader(System.in));
			outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			fileToServer = new DataOutputStream(socket.getOutputStream());
			

			int line = 0;

			while(line != 3){
				System.out.printf("\n\t|1: text input ; 2: file input ; 3: quit: ");
				line = sc.nextInt();
				sc.nextLine();

				// 1. message
				if(line == 1){
					System.out.print("\n\t|Enter a message: ");
					String input = sc.nextLine();
					this.payload = toCharArray(input, 1);
					outToServer.write(getPayloadSize());
					outToServer.write(this.payload);
					outToServer.flush();
				}
				// 2. file
				else if(line == 2){
					sendFile();
				}
				else if (line == 3) {
                    System.out.println("\n\t|Quitting...");
                    break;
                }
				// 3. Error
				else{
					System.out.println("\n\t|Invalid...");
				}
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


	public char[] toCharArray(String input, int payloadType){
		char[] out = new char[input.length()+1];
		out[0] = (char)payloadType;

		for(int i = 0; i<input.length();i++){
			out[i+1] = input.charAt(i); 
		} 

		return out;
	}

	public int getPayloadSize(){
		return this.payload.length;
	}

	public void sendFile() throws Exception{
		System.out.print("\n\t|Enter path: ");
		String path = sc.nextLine();
		File f = new File(path);

		FileInputStream fileInputStream = new FileInputStream(f);

		fileToServer.writeLong(f.length());

		int bytes = 0;

		byte[] buffer = new byte[4*1024];
		while(
			(bytes = fileInputStream.read(buffer)) != -1)
		{
			fileToServer.write(buffer, 0, bytes);
			fileToServer.flush();
		}

		fileInputStream.close();
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














