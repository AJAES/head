import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MissandMatchServer {

    public class Client implements Serializable {
        String name;
        int score;
        int roomNumber;
        int turn;
        ObjectOutputStream dos;

        public Client(String name, int roomNumber, int opponent){
            this.name = name;
            this.roomNumber = roomNumber;
            this.turn = opponent;
        }

        @Override
        public boolean equals(Object obj) {
            if(((Client)obj).turn != this.turn && ((Client)obj).roomNumber == this.roomNumber){
                return true;
            }else{
                return false;
            }
        }
    }
    public class net_buffer implements Serializable {
        private String msg;
        private String user1_name;
        private String user2_name;
        private int set[] = new int[9];
        private int turn = 0;
        private int score;
        private int roomnum = 0;
        net_buffer(String msg, String user1_name, String user2_name, int set[], int turn, int score, int roomnum){
            this.msg = msg;
            this.user1_name = user1_name;
            this.user2_name = user2_name;
            this.set = set;
            this.turn = turn;
            this.score = score;
            this.roomnum = roomnum;
        }
        net_buffer(){

        }
        public void setMsg(String msg){
            this.msg = msg;
        }
        public void setUser1_name(String user1_name){
            this.user1_name = user1_name;
        }
        public void setUser2_name(String user2_name){
            this.user2_name = user2_name;
        }
        public void setSet(int set[]){
            this.set = set;
        }
        public void setTurn(int turn){
            this.turn = turn;
        }
        public void setScore(int score){
            this.score = score;
        }
        public void setRoomnum(int roomnum){
            this.roomnum = roomnum;
        }
        public String getMsg(){
            return this.msg;
        }
        public String getUser1_name(){
            return this.user1_name;
        }
        public String getUser2_name(){
            return this.user2_name;
        }
        public int getTurn(){
            return this.turn;
        }
        public int getScore(){
            return this.score;
        }
        public int getRoomnum(){
            return this.roomnum;
        }
        public int[] getSet(){
            return this.set;
        }
    }

    int serverRoomNum = 0;
    private HashMap<String, Client> clients;
    private ServerSocket serverSocket;
    private ArrayList<Client> waitingList = new ArrayList<Client>();
    private static final String REQ = "make";
    private static final String REQ_MATCH = "join";
    private static final String REQ_list = "list";
    ArrayList<Integer> ranNumber = new ArrayList<Integer>();

    int buffer = 0;
    Client user1;

    public static void main(String[] args){
        new MissandMatchServer().start();
    }

    public MissandMatchServer() {
        clients = new HashMap<String, Client>();
        for(int a =0 ; a<27; a++){
            ranNumber.add(a);
        }

        // 여러 스레드에서 접근할 것이므로 동기화
        Collections.synchronizedMap(clients);
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
        ObjectInputStream input;
        ObjectOutputStream output;
        Client newclient;

        public ServerReceiver(Socket socket){
            this.socket = socket;
            try{
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
            }catch(IOException e){
            }
        }

        @Override
        public void run(){
            net_buffer input_buffer = null;
            try {
                while (input != null) {
                    input_buffer = (net_buffer) input.readObject();
					System.out.println("%%[" + socket.getInetAddress() + ":" + socket.getPort() + "]");
                    if (input_buffer.getMsg().equals(REQ)) {//making room
                        //this.newclient = new Client(name, Integer.parseInt(input.readUTF()), Boolean.valueOf(input.readUTF()).booleanValue());
                        System.out.println(input_buffer.getUser1_name().toString() + "[" + socket.getInetAddress() + ":" + socket.getPort() + "]");

                        waitingList.get(waitingList.size()).roomNumber = ++serverRoomNum;
                        System.out.println(waitingList.get(waitingList.size()).roomNumber + ", " + this.newclient.roomNumber);

                        newclient = new Client(input_buffer.getUser1_name(), serverRoomNum, input_buffer.turn);
                        newclient.dos = output;
                        clients.put(input_buffer.getUser1_name(), newclient);
                        System.out.println("now" + clients.size() + "are online.");

                        int temp[] = null;
                        net_buffer init_buffer = new net_buffer("init", newclient.name, "", temp, newclient.turn, input_buffer.getScore(), serverRoomNum);
                        sendToAll(init_buffer, serverRoomNum);

                        waitingList.add(newclient);
                        System.out.println("now" + waitingList.size() + "are waiting$$$$$$$$$$$$");

                    }else if(input_buffer.getMsg().equals(REQ_list)){
                        System.out.println(input_buffer.getUser2_name().toString() + "[" + socket.getInetAddress() + ":" + socket.getPort() + "]");
                        //for(int i = 0; i < clients.size(); i++){
                        //}
                        newclient.dos = output;

                    }else if (input_buffer.getMsg().equals(REQ_MATCH)) {//join room
                        newclient = new Client(input_buffer.getUser2_name(), input_buffer.getRoomnum(), input_buffer.turn);
                        newclient.dos = output;
                        clients.put(input_buffer.getUser2_name(), newclient);
                        System.out.println("now" + clients.size() + "are online.");

                        for (int i = 0; i < waitingList.size(); i++) {
                            if (waitingList.get(i).roomNumber == input_buffer.getRoomnum()) {
                                waitingList.remove(i);
                                start_game(this.newclient.roomNumber);
                                break;
                            }
                        }
                    }
                }
            }
            catch (IOException e) {

            } catch(ClassNotFoundException e) {

            }
            finally{
                String remove_name = null;
                // if connection lost
                if(input_buffer.getTurn() == 0){
                    clients.remove(input_buffer.getUser1_name());
                    remove_name = input_buffer.getUser1_name();
                }else if(input_buffer.getTurn() == -1){
                    clients.remove(input_buffer.getUser2_name());
                    remove_name = input_buffer.getUser2_name();
                }

                System.out.println("현재 " + clients.size() + "명이 채팅앱에 접속 중입니다.");

                //if there is waitingList, discard
                for(int i = 0 ; i < waitingList.size(); i++){
                    if(waitingList.get(i).name.equals(remove_name)){
                        System.out.println("대기중인 사용자가 나갔습니다.");
                        waitingList.remove(i);
                    }
                }
            }
        }

        public void sendToAll(net_buffer so_buffer,int roomNumberCheck) {
            Iterator<String> it = clients.keySet().iterator();

            while (it.hasNext()) {
                try {
                    Client cli = clients.get(it.next());
                    ObjectOutputStream dos = cli.dos;
                    if(cli.roomNumber==roomNumberCheck){
                        dos.writeObject(so_buffer);
                        dos.flush();
                    }
                } catch (Exception e) {
                }
            }
        }
        public void start_game(int room_number){
            Collections.shuffle(ranNumber);
            net_buffer output_buffer = new net_buffer();
            output_buffer.setMsg("game");
            int[] tempbuf = new int[9];
            for(int i = 0; i < 9; i++){
                tempbuf[i] = ranNumber.get(i);
            }
            output_buffer.setSet(tempbuf);
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).roomNumber == room_number) {
                    if(clients.get(i).turn == 0){
                        output_buffer.setUser1_name(clients.get(i).name);
                    }else if(clients.get(i).turn == 1){
                        output_buffer.setUser2_name(clients.get(i).name);
                    }
                }
            }
            output_buffer.setRoomnum(room_number);
            output_buffer.setScore(0);
            output_buffer.setTurn(0);
            sendToAll(output_buffer, room_number);
        }
        public void do_game(int room_number){


        }
    }
}


