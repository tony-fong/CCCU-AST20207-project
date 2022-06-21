package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Moive_info extends AppCompatActivity {
    private  String locate = MainActivity.locate;
    private String TAG = "Moive_info";
    private  Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.moive_info);
        Intent toMoiveinfo = getIntent();
        final int ID =  toMoiveinfo.getIntExtra("id",1) + 1;
        Toast.makeText(Moive_info.this,Integer.toString(ID),Toast.LENGTH_LONG).show();
        getinfo(ID);

        Button buy = (Button)findViewById(R.id.buyDvD);
        Button InfoToList = (Button)findViewById(R.id.InfoToList);

        buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(Moive_info.this,"buyed",Toast.LENGTH_LONG).show();
                Thread thread = new Thread(addproduct);
                thread.start();
                Intent backtolist = new Intent(Moive_info.this,MainActivity.class);
                startActivity(backtolist);
            }
        });
        InfoToList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Moive_info.this,MainActivity.class);
                startActivity(back);
            }
        });

    }
    public void getinfo(int id){

        class getdata  extends AsyncTask<Integer, Void, String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoListView(s);
                }catch (Exception e){}
            }
            @Override
            protected String doInBackground(Integer... id) {
                try {
                    int ID = id[0];
                    URL url = new URL(locate + "PHP/show_moive_info.php");
                    String  data = "ID=" + ID;
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
        Getdata.execute(id);

    }
    private void loadIntoListView(String json) throws JSONException, IOException, ExecutionException, InterruptedException {
        String name;
        String prodution;
        String time;
        String price;
        String info;
        String image;

        int resID;

        TextView text_name = (TextView)findViewById(R.id.text_name);
        TextView text_prodution = (TextView)findViewById(R.id.text_prodution);
        TextView text_time = (TextView)findViewById(R.id.text_time);
        TextView text_price = (TextView)findViewById(R.id.text_price);
        TextView text_info = (TextView)findViewById(R.id.text_info);
        ImageView imageview = (ImageView)findViewById(R.id.moive_image1);

        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            name = obj.getString("name");
            prodution = obj.getString("prodution");
            time = obj.getString("time");
            price = obj.getString("price");
            info = obj.getString("info");
            image = obj.getString("image");



            resID = getResources().getIdentifier(image, "drawable", getPackageName());
            imageview.setImageResource(resID);
            text_name.setText("name" + name);
            text_prodution.setText("prodution" + prodution);
            text_time.setText("time" + time);
            text_price.setText("price" + price);
            text_info.setText(info);

        }



    }

    Runnable addproduct = new Runnable() {
        @Override
        public void run() {
            try {
                final SharedPreferences user = getSharedPreferences("DATA",MODE_PRIVATE);
                String username = user.getString("username",null);
                Intent toMoiveinfo = getIntent();
                final int ID =  toMoiveinfo.getIntExtra("id",1) + 1;
                URL url = new URL(locate + "PHP/buy_product.php");
                Log.i(TAG,url.toString());
                String data = "username="+username+"&id="+ID;
                Log.i(TAG,data);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.connect();
                //send data
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(data);
                wr.flush();
                //reader
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
                String result = sb.toString();
                Looper.prepare();
                Toast.makeText(Moive_info.this,"add to shopping car",Toast.LENGTH_LONG).show();
                Looper.loop();
                Looper.myLooper().quit();
            }catch (Exception e){

            }
        }
    };
}

