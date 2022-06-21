package com.example.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class shopping_car extends AppCompatActivity {
    private  String locate = MainActivity.locate;
    private ArrayList<HashMap<String, String>> datalist;
    public static SharedPreferences logined_user;
    final private String TAG="shopping_car";
    String username;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);{
            setContentView(R.layout.shopping_car);
            datalist = new ArrayList<>();
            final SharedPreferences user = getSharedPreferences("DATA", MODE_PRIVATE);
            username =  user.getString("username",null);
            getCAR();

            Button back = (Button)findViewById(R.id.backtolist);
            Button pay =(Button)findViewById(R.id.pay);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent BacktoList = new Intent(shopping_car.this,MainActivity.class);
                    startActivity(BacktoList);
                }
            });
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(shopping_car.this);
                    builder.setTitle("payment");
                    builder.setMessage("total pirce = " + total);
                    builder.setPositiveButton("pay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            PAY pay = new PAY();
                            pay.execute();
                            Intent BacktoList = new Intent(shopping_car.this,MainActivity.class);
                            startActivity(BacktoList);
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                         }
                    });
                    builder.show();
                }
            });
        }
    }
    private void getCAR() {

        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(locate + "PHP/shopping_car.php");
                    String data = "username=" + username;
                    Log.i(TAG, "loading data");
                    Log.i(TAG, url.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestMethod("POST");
                    con.connect();
                    Log.i(TAG, "test" + url);
                    //send data
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    String result = sb.toString();
                    bufferedReader.close();

                    return result;

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }
    private int total = 0;
    private void loadIntoListView(String json) throws JSONException, IOException, ExecutionException, InterruptedException {
        Log.i(TAG, json);
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            HashMap<String, String> data = new HashMap<>();
            String name = obj.getString("name");
            String prodution = obj.getString("prodution");
            String quantity = obj.getString("quantity");
            String time = obj.getString("time");
            String price = obj.getString("price");
            String image = obj.getString("image");

            int resID = getResources().getIdentifier(image, "drawable", getPackageName());


            data.put("name", "name" + name);
            data.put("prodution", "prodution" + prodution);
            data.put("quantity","quantity" + quantity);
            data.put("time","time"  + time);
            data.put("price", "prcie" + price);
            data.put("image", Integer.toString(resID));
            total = Integer.parseInt(price) * Integer.parseInt(quantity) + total;
            datalist.add(data);


        }


        Log.i(TAG, datalist.toString());
        listView = (ListView) findViewById(R.id.shopping_list);
        ListAdapter listAdapter = new SimpleAdapter(this, datalist, R.layout.shopping_car_list,
                new String[]{"name", "prodution", "image","quantity","time","price"},
                new int[]{R.id.name, R.id.prodution, R.id.imageView,R.id.quantity,R.id.price,R.id.time});
        listView.setAdapter(listAdapter);

    }
    class PAY extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG,"HI");
            try {

                String data;
                URL url = new URL(locate + "PHP/payment.php");
                data = "username=" + username + "&" + "&price=" + total;
                Log.i(TAG, url.toString());
                Log.i(TAG, data);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.connect();
                //sender
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                //reader
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                Looper.prepare();
                Toast.makeText(shopping_car.this,line,Toast.LENGTH_LONG).show();
                Looper.myLooper().quit();

            } catch (Exception e) {}
            return null;
        }
    }

}

