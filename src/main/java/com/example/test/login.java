package com.example.test;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {
public static  SharedPreferences logined_user;
private  String locate = MainActivity.locate;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        setContentView(R.layout.login);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        final Button login=(Button)findViewById(R.id.login);
        Button toRegister=(Button)findViewById(R.id.LoginToRegister);

        login.setOnClickListener(new View.OnClickListener() {

            private static final String TAG = "MyActivity";
            TextView user,pw;
    
            String username, password,data,result;

            @Override
            public void onClick(View v) {

                user = findViewById(R.id.Username);
                pw = findViewById(R.id.Password);
                username = user.toString();
                password = pw.toString();

                Thread thread = new Thread(check);
                thread.start();
            }
            private Runnable check = new Runnable() {
                public void run() {
                    try {
                        username = user.getText().toString();
                        password = pw.getText().toString();

                        URL url = new URL(locate + "PHP/login.php");
                        data = "login=" + username + "&" + "password=" + password;
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setRequestMethod("POST");
                        conn.connect();
                        Log.i(TAG, "test" + url);
                        //send data
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();
                        //reader
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer response = new StringBuffer();

                        String line;
                        //READ LINE BY LINE
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        //RELEASE RES
                        br.close();
                        result = response.toString();

                    } catch (Exception e) {
                        result = e.toString();
                        Log.e(TAG, result);
                    }
                    Looper.prepare();
                    if (Integer.parseInt(result) == 1) {

                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "logn success", duration);
                        toast.show();
                        Log.i(TAG,"username");
                        Log.i(TAG,username);
                        logined_user = getSharedPreferences("DATA",MODE_PRIVATE);
                        logined_user.edit().putString("username",username).commit();
                        String test = logined_user.getString("username",null);
                        Log.i(TAG,"user");
                        Log.i(TAG,test);
                        Intent intent=new Intent(com.example.test.login.this,MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(login.this,"worng username or password",Toast.LENGTH_LONG).show();
                    }
                    Looper.loop();
                }
            };

        });

        toRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent toRegister=new Intent(com.example.test.login.this, register.class);
                startActivity(toRegister);

            }

        });

    }

}