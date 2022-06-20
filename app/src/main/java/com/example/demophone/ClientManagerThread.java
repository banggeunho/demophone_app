package com.example.demophone;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManagerThread extends Thread{

    private Socket m_socket;
    private String m_ID;

    @Override
    public void run() {
        super.run();

        try {
            Log.d("Client Thread", "Connect!"+m_ID);
            BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            String text;

            while (true) {
                if (in == null) break;

                text = in.readLine();
//                System.out.println(text);
//                Log.d("Client Thread", text);
                if (text != null) {
                    Log.d("Client Thread", text);
//                    Log.d("c", text.split("\\+")[1]);
                    if (text.contains("HeartR")) {
                        MainActivity.addEntry(MainActivity.hrChart, Double.parseDouble(text.split("\\+")[3]));
                    }
                    if (text.contains("StepC")) {

                        MainActivity.addEntry(MainActivity.scChart, Double.parseDouble(text.split("\\+")[3]));
                    }
                }
                else if (text.equals("Stop"))
                {
                    Log.d("Client Thread", "Remove Connect");
                    m_socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket _socket, String _id){
        m_socket = _socket;
        m_ID = _id;
    }
}
