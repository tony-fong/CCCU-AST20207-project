package com.example.test;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class register extends AppCompatActivity {
    private  String locate = MainActivity.locate;

    public boolean check(EditText temp,String type) {
        if(TextUtils.isEmpty(temp.getText().toString())){
            temp.setError(type+"cant be empty");
            return false;
        }else {
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final Button register = (Button) findViewById(R.id.register);
        final Button backLogin = (Button)findViewById(R.id.backLogin);
        //signup
        register.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "MyActivity";
            int age;
            EditText temp;
            String username, password, repassword, email, result;
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
            boolean notEmpty = true;
            @Override
            public void onClick(View v) {
                temp = findViewById(R.id.username);
                if (check(temp, "username")) {
                    username = temp.getText().toString();
                } else {
                    notEmpty = false;
                }
                temp = findViewById(R.id.Password);
                if (check(temp, "password")) {
                    password = temp.getText().toString();
                } else {
                    notEmpty = false;
                }

                temp = findViewById(R.id.repassword);
                if (check(temp, "checkpassword")) {
                    repassword = temp.getText().toString();
                } else {
                    notEmpty = false;
                }

                temp = findViewById(R.id.age);
                if (check(temp, "age")) {
                    age = Integer.parseInt(temp.getText().toString());
                } else {
                    notEmpty = false;
                }
                temp = findViewById(R.id.email);
                if (check(temp, "email")) {
                    email = temp.getText().toString();
                } else {
                    notEmpty = false;
                }
                if (notEmpty) {
                    if (email.matches(regex)) {
                        if (password.matches(repassword)) {
                          //  Toast.makeText(register.this, "loading", Toast.LENGTH_SHORT).show();
                            Thread thread = new Thread(signup);
                            thread.start();
                        } else {
                            Toast.makeText(register.this, "password must be match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(register.this, "isValid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // i am lazy to make a funtion
            private Runnable signup = new Runnable() {
                public void run() {
                    try {
                        URL url = new URL(locate + "PHP/signup.php");
                        String data = "username=" + username + "&" + "password=" + password + "&" + "age=" + age + "&" + "email=" + email;
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.connect();
                        Log.i(TAG, url.toString() );
                        //send data
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();
                        wr.close();
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
                    Context context = getApplicationContext();
                    if(result.matches("register success")) {
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        Intent toLogin=new Intent(register.this, login.class);
                        startActivity(toLogin);
                    }
                    Looper.loop();

                }

            };
        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin=new Intent(register.this, login.class);
                startActivity(toLogin);
            }
        });

    }
}