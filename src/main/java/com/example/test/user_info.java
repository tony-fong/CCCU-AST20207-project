package com.example.test;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class user_info extends AppCompatActivity {
    private String TAG = "user_info";
    private  String locate = MainActivity.locate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        final SharedPreferences user = getSharedPreferences("DATA", MODE_PRIVATE);
        String username = user.getString("username", null);
        getinfo(username);
    }
    public void getinfo(String username){

        class getdata  extends AsyncTask<String, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoListView(s);
                }catch (Exception e){}
            }
            @Override
            protected String doInBackground(String... username) {
                try {
                    String user_name = username[0];
                    URL url = new URL(locate + "PHP/show_user_info.php");
                    String  data = "username=" + user_name;
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    connect.setRequestMethod("POST");
                    connect.connect();
                    Log.i(TAG, "test" + url);
                    //send data
                    OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    //read
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    String result = sb.toString();
                    bufferedReader.close();
                    return result;
                }catch (Exception e){
                    return null;
                }
            }
        }
        getdata Getdata =new getdata();
        Getdata.execute(username);

    }
    private void loadIntoListView(String json) throws JSONException, IOException, ExecutionException, InterruptedException {
        String id;
        String name;
        String email;
        String age;
        String credits;

        TextView text_id = (TextView)findViewById(R.id.text_user_id);
        TextView text_name= (TextView)findViewById(R.id.text_user_name);
        TextView text_email = (TextView)findViewById(R.id.text_user_email);
        TextView text_age = (TextView)findViewById(R.id.text_user_age);
        TextView text_credits = (TextView)findViewById(R.id.text_user_credits);

        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            id = obj.getString("id");
            name = obj.getString("username");
            email = obj.getString("email");
            age =  obj.getString("age");
            credits = obj.getString("credits");

            text_id.setText("id: "+id);
            text_name.setText("username: "+name);
            text_email.setText("email: "+email);
            text_age.setText("age: "+age);
            text_credits.setText("credits: " + credits);
        }



    }

}
