import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
 
 
public class simpleServer {
 
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("I'm waiting here: " + serverSocket.getLocalPort());            
                                 
            Socket socket = serverSocket.accept();
            System.out.println("from " + 
                socket.getInetAddress() + ":" + socket.getPort());
             
            OutputStream outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print("Server Send data ~~ ");
            printStream.close();
             
            socket.close();
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
}