package com.example.demophone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.demophone.ServerThread;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServerThread thread = new ServerThread();
        thread.start();
    }
}