import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//t
public class Server{
	private ServerSocket serverSocket;

	public Server(int port){
		try{
			serverSocket = new ServerSocket(port);

			System.out.println("\n\t|Listening on port: "+port);
			while(true){
				Socket connection = serverSocket.accept();
				ConnectionHandler handler = new ConnectionHandler(connection);	
				System.out.println("\n\t|Ipaddress connected: "+connection.getInetAddress());
				try{
					handler.handleConnection();
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		}
		catch(IOException k){
			System.out.println(k.getMessage());
		}
	}

	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("Invalid params");
		}
		
		Server server = new Server(Integer.parseInt(args[0]));
	}
}
