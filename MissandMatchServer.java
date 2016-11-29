import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MissandMatchServer {
    private ServerSocket serverSocket;
        
    public static void main(String[] args){
        new MissandMatchServer().start();
    }

    public MissandMatchServer() {

    }

    public void start(){
        try{
            Socket socket;            

            serverSocket = new ServerSocket(12345);
            System.out.println("start server");

            while(true){
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]");
				ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();
			}
        }catch(IOException e){
			e.printStackTrace();
        }
    }
	
	class ServerReceiver extends Thread{
		Socket socket;
		DataInputStream input;
		DataOutputStream output;
		
		public ServerReceiver(Socket socket){
			this.socket = socket;
			try{
				input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
			}catch(IOException e){
			}
		}
		
		@Override
		public void run(){
			String msg;
            String message;
			try{
				while(input != null){					
					msg = input.readUTF();
					System.out.println(msg + "[" + socket.getInetAddress() + ":" + socket.getPort() + "]");
					message = msg;
					output.writeUTF(message);
					output.flush();
				}
			}catch(IOException e){
			}
		}
	}
}
