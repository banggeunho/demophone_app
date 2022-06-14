package com.example.demophone;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// 얘는 받아서 그래프 변수에 잘 넣어주기만 하면 된다.
public class ServerThread extends Thread{
    @Override
    public void run() {
        int port = 5001;
        try {
            ServerSocket server = new ServerSocket(port);
            Log.d("ServerThread", "서버가 실행됨.");
            while(true){
                Socket socket = server.accept(); // server 대기상태. 클라이언트 접속 시 소켓 객체 리턴
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()); // 들어오는 데이터 처리
                Object input = inputStream.readObject();
                Log.d("ServerThread","input : "+input);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(input + "from server"); // 서버에서 보낸 데이터
                outputStream.flush();
                Log.d("ServerThread","output 보냄");
                socket.close(); // 연결을 유지할 필요 없으면 끊어줌.
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
