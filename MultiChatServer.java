
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
 
public class MultiChatServer {
	int serverRoomNum = 0;
	////////////////////////////////////////////////1
	////////////////////////////////////////////////1
	////////////////////////////////////////////////1
	////////////////////////////////////////////////1
    private HashMap<String, Client> clients;
    private ServerSocket serverSocket;
    
    private static final String REQ = "I SERCHING PEOPLE";
    private static final String REQ_MATCH = "I SERCHING YOU";
    
    
    private ArrayList<Client> waitingList = new ArrayList<Client>();
    
    
 
    public static void main(String[] args) {
        new MultiChatServer().start();
    }
 
    public MultiChatServer() {
        clients = new HashMap<String, Client>();
 
        // 여러 스레드에서 접근할 것이므로 동기화
        Collections.synchronizedMap(clients);
    }
 
    public void start() {
        try {
            Socket socket;
 
            // 리스너 소켓 생성
            serverSocket = new ServerSocket(8000);
            System.out.println("서버가 시작되었습니다.");
 
            // 클라이언트와 연결되면
            while (true) {
                // 통신 소켓을 생성하고 스레드 생성(소켓은 1:1로만 연결된다)
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream input;
        DataOutputStream output;
        Client clientSetting;
 
        public ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }
 
        @Override
        public void run() {
            String name = "";
            try {
                // 클라이언트가 서버에 접속하면 대화방에 알린다.
                name = input.readUTF();
//                sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
//                        + socket.getPort() + "]" + "님이 채팅에 접속했습니다..");
                //모두에게 알릴필요는 없는것같다. 그냥 두명이 매칭되면 두명에게만 뿌려주자.
 
//                clients.put(name, output);
                System.out.println(name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 채팅에 접속했습니다.");
//                System.out.println("현재 " + clients.size() + "명이 채팅앱에 접속 중입니다.");
                //필요없을거같다. 왜냐하면 소켓만들고, 아이디 다 만들고 하기때문에 밑에꺼까지 모두 보낸다 한번에
 
                // 메세지 전송
                while (input != null) {
                	String inputMessage = input.readUTF();
                	if(inputMessage.equals(REQ)){
                		this.clientSetting = new Client(name
                				,Integer.parseInt(input.readUTF())
                				,Boolean.valueOf(input.readUTF()).booleanValue()
                				);
                		clientSetting.dos = output;
                		clients.put(name,clientSetting);
                		 System.out.println("현재 " + clients.size() + "명이 채팅앱에 접속 중입니다.");
                		if(waitingList.size()>0){
                			boolean exist = false;
                			for(int i = 0 ; i < waitingList.size(); i++){
                    			if(waitingList.get(i).equals(this.clientSetting)){
                    				
                    				exist = true;
                    				
                    				waitingList.get(i).roomNumber = ++serverRoomNum;
                    				this.clientSetting.roomNumber = serverRoomNum;
                    				
                    				String waitingNname = waitingList.get(i).name;//웨이팅하는 놈의 이름을 땃다그냥.remove하기전에.
                    				
                    				System.out.println(waitingList.get(i).roomNumber+","+this.clientSetting.roomNumber);
                    				waitingList.remove(i);
                    				
                    				System.out.println("현재 대기 클라이언트 : " + waitingList.size()+"$$$$$$$$$$$$");
                    				//조금 천천히 결과를 주기위함이다.
                    				try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                    				//////////////////////////////////////
                    				sendToAll("[Server] : 대화방에 입장되었습니다.",this.clientSetting.roomNumber);
                    				Thread.sleep(200);
                    				sendToAll(waitingNname+" , " + this.clientSetting.name +"입장하겠습니다.",this.clientSetting.roomNumber);
                    				break;
                    				
                    			}
                    			
                    		}
                			if(!exist){
                    			waitingList.add(clientSetting);
                    			System.out.println("현재 대기 클라이언트 : " + waitingList.size()+"@@@@@@@@@@@@@");

                			}
                			
                		}else{
                			waitingList.add(clientSetting);
                			System.out.println("현재 대기 클라이언트 : " + waitingList.size()+"#########################");
                		}
                		
                		
                	}else if(inputMessage.equals(REQ_MATCH)){
                		
                	}else{
                		sendToAll("["+name+"]"+inputMessage,this.clientSetting.roomNumber);
                	}
                    
                }
            } catch (IOException | InterruptedException e) {
            } finally {
                // 접속이 종료되면
                clients.remove(name);
                sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 채팅방에서 나갔습니다.",this.clientSetting.roomNumber);
                System.out.println(name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 채팅방에서 나갔습니다.");
                System.out.println("현재 " + clients.size() + "명이 채팅앱에 접속 중입니다.");
                
                //만약에 기다리고있던 방이 있다면, 없애줘야지
                for(int i = 0 ; i < waitingList.size(); i++){
                	if(waitingList.get(i).name.equals(clientSetting.name)){
                		System.out.println("대기중인 사용자가 나갔습니다.");
                		waitingList.remove(i);
                	}
                	
                }
            }
        }
 
        public void sendToAll(String message,int roomNumberCheck) {
            Iterator<String> it = clients.keySet().iterator();
 
            while (it.hasNext()) {
                try {
                	Client cli = clients.get(it.next());
                	DataOutputStream dos = cli.dos;
//                    DataOutputStream dos = clients.get(it.next()).dos;
                    if(cli.roomNumber==roomNumberCheck){
                        dos.writeUTF(message);
                    }
                } catch (Exception e) {
                }
            }
        }
        
        public void sendToAll(String message) {
            Iterator<String> it = clients.keySet().iterator();
 
            while (it.hasNext()) {
                try {
                    DataOutputStream dos = clients.get(it.next()).dos;
                    dos.writeUTF(message);
                } catch (Exception e) {
                }
            }
        }
    }
}