package com.example.demophone;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// 얘는 받아서 그래프 변수에 잘 넣어주기만 하면 된다.
public class ServerThread extends Thread{

    public static ArrayList<PrintWriter> m_OutputList;

    DataInputStream is;
    DataOutputStream os;

    @Override
    public void run() {
        m_OutputList = new ArrayList<PrintWriter>();
        Socket socket = null;
        int port = 5002;
        try {
            ServerSocket server = new ServerSocket(port);
            Log.d("ServerThread", "서버가 실행됨.");
            while(true){
                socket = server.accept(); // server 대기상태. 클라이언트 접속 시 소켓 객체 리턴
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//                is = new DataInputStream(socket.getInputStream());
                String[] deviceName = in.readLine().split("\\+");

                ClientManagerThread c_thread = new ClientManagerThread();
                c_thread.setSocket(socket, deviceName[0]);


                m_OutputList.add(new PrintWriter(socket.getOutputStream()));
                Log.d("ServerThread", String.valueOf(socket.getOutputStream()));
                Log.d("ServerThread", String.valueOf(m_OutputList));
                Log.d("ServerThread", String.valueOf(m_OutputList.size()));
                c_thread.start();

            }
        } catch (Exception e){ // 대기중 에러
            e.printStackTrace();
        } finally { // 소켓 종료시 에러
                try {
                   if (socket != null && socket.isClosed() == false) {
                       socket.close();
                   }
                } catch (IOException e) {
                       e.printStackTrace();
                    }
            }
        }
}
