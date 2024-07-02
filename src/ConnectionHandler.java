import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.Socket;

public class ConnectionHandler extends Thread{
	private Socket socket;
	private BufferedReader inFromClient;
	private PrintWriter outToClient;

	private DataInputStream readFromFile;


	public ConnectionHandler(Socket socket){
		this.socket = socket;
		
		try{
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new PrintWriter(socket.getOutputStream(), true);

			readFromFile = new DataInputStream(socket.getInputStream());
			
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			
		}
		catch(Exception e){
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
		catch(Exception e){
			System.out.println(e.getMessage());
			clean();
		}
		
	}

	public void handleConnection() throws Exception,IOException{
		while(true){

			int payloadSize = inFromClient.read();
			char[] payload = new char[payloadSize];

			int bytesRead = inFromClient.read(payload);
		
			for(int i = 0; i < payloadSize; i++){
				System.out.println(payload[i]);
			}

			receiveFile("files/test.txt");
			
		}
	} 

	public void receiveFile(String fileName) throws Exception{
		int bytes = 0;

		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		long size = readFromFile.readLong();
		byte[] buffer = new byte[4*1024];

		while(
			size > 0 && (bytes = readFromFile.read(buffer, 0, (int)(Math.min(buffer.length, size)))) != -1
		){
			fileOutputStream.write(buffer, 0, bytes);
			size -= bytes;
		}

		System.out.println("\n\t|File received.");
		fileOutputStream.close();
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