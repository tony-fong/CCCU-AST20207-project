package com.example.test;

import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import  android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity  {
    public static String locate ="http://192.168.0.134/AST20207/";

    private ArrayList<HashMap<String, String>> datalist;
    private static final String TAG = "MyActivity";
    private static final String TAG_USER = "username";
    Bitmap myBitmap;
    ListView listView;
    TextView textView;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datalist = new ArrayList<>();

        getJSON();
        final SharedPreferences user = getSharedPreferences("DATA",MODE_PRIVATE);
        boolean logined = false;
        try {
            username = user.getString("username", null);
            user.edit().commit();
            if (username != null) {
                logined = true;
                Log.i(TAG, "username");
                Log.i(TAG_USER, username);
            } else {
                Log.w(TAG_USER, "no login");
            }
        }catch (Exception e){}

        Button toRegister = (Button) findViewById(R.id.MainToRegister);
        Button loginbutton = (Button) findViewById(R.id.MaintoLogin);
        Button logout = (Button)findViewById(R.id.logout);
        final Button shoppingcar = (Button)findViewById(R.id.shopprt_car);
        listView = findViewById(R.id.movieList);
        textView = findViewById(R.id.text_username);
        if(logined == true){
            toRegister.setVisibility(View.GONE);
            loginbutton.setVisibility(View.GONE);
            shoppingcar.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(username);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toUserInfo = new Intent(MainActivity.this, user_info.class);
                    startActivity(toUserInfo);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                    Object o = listView.getItemAtPosition(position);
                    //Toast.makeText(MainActivity.this,Integer.toString((int)id+1),Toast.LENGTH_LONG).show();
                    Intent toMoiveinfo = new Intent(MainActivity.this, Moive_info.class);
                    toMoiveinfo.putExtra("id", (int) id);
                    startActivity(toMoiveinfo);
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.edit().clear().commit();
                    finish();
                    startActivity(getIntent());
                }
            });
            shoppingcar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toShoppingCar = new Intent(MainActivity.this, shopping_car.class);
                    startActivity(toShoppingCar);
                }
            });
        }else {

            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "to login");
                    Toast.makeText(MainActivity.this, "moving", Toast.LENGTH_LONG).show();
                    Intent toLogin = new Intent(MainActivity.this, login.class);
                    startActivity(toLogin);
                }
            });

            toRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "to register");
                    Toast.makeText(MainActivity.this, "moving", Toast.LENGTH_LONG).show();
                    Intent toRegister = new Intent(MainActivity.this, register.class);
                    startActivity(toRegister);
                }
            });

            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                    Toast.makeText(MainActivity.this,"please login",Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void getJSON() {

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
                    URL url = new URL(locate+"PHP/show.php");
                    Log.i(TAG, "loading data");
                    Log.i(TAG, url.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
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

    private void loadIntoListView(String json) throws JSONException, IOException, ExecutionException, InterruptedException {
        Log.i(TAG, json);
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            HashMap<String, String> data = new HashMap<>();
            String name = obj.getString("name");
            String prodution = obj.getString("prodution");
            String time = obj.getString("time");
            String price = obj.getString("price");
            String image = obj.getString("image");

            //save image
            String link = (locate + "image/" + image + ".png");
            Log.i(TAG,link);
            Bitmap bit = new GB().execute(link).get();

            Log.i(TAG, "data to save");
 //           Log.i(TAG, bit.toString());

            File FilesDir = getFilesDir();
            String imagename = image+".png";
            Log.i(TAG,imagename);
            File file = new File (FilesDir, imagename);
            //if(file.exists()){Log.i(TAG,"image delete");file.delete();};
            try {
                FileOutputStream out = new FileOutputStream(file);
                bit.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }
            if(file.exists()){
                Log.i(TAG,"input succe");
                Log.i(TAG,file.toString());
            }



            int resID = getResources().getIdentifier(image, "drawable", getPackageName());

            Log.i(TAG,"image id");
            Log.i(TAG,Integer.toString(resID));
            data.put("name", "name" + name);
            data.put("prodution", "prodution" + prodution);
            data.put("time","time" + time);
            data.put("price","price" + price);
            data.put("image", Integer.toString(resID));
            datalist.add(data);


        }


        Log.i(TAG, datalist.toString());
        listView = (ListView) findViewById(R.id.movieList);
        ListAdapter listAdapter = new SimpleAdapter(this, datalist, R.layout.movie_list,
                new String[]{"name", "prodution", "image","time","price"},
                new int[]{R.id.name, R.id.prodution, R.id.imageView,R.id.price,R.id.time});
        listView.setAdapter(listAdapter);

    }

    class GB extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... src) {
            Bitmap bit = null;
            try {
                java.net.URL url = new java.net.URL(src[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                input.close();
                return myBitmap;

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bit;
        }

    }
}



