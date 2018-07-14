package com.example.shivakanth.citadelremastered;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import data.ApiUtils;
import data.CDB;
import data.Character;
import data.Data;
import data.GOTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private GOTService mService;
    Data c;
    ListView list;
    TextView n;
    ImageView img;
    String m;
    ArrayList<Data> history;
    private static final String DATABASE_NAME = "char_db";
    private CDB CharDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        list = findViewById(R.id.det);
        img = findViewById(R.id.image);
        n = findViewById(R.id.name);



        CharDatabase = Room.databaseBuilder(getApplicationContext(),
                CDB.class, DATABASE_NAME)
                .build();

        Intent intent = getIntent();
        final String message = intent.getStringExtra("name");
        m=message;
        int i = intent.getIntExtra("isHistory", 0);

        if (i == 0)
        {

            mService = ApiUtils.getGOTService();
            Call<Character> call = ApiUtils.getGOTService().getCharacter(message);
            call.enqueue(new Callback<Character>() {
                @Override
                public void onResponse(Call<Character> call, Response<Character> response) {
                    c = response.body().getData();
                    Toast.makeText(DetailActivity.this, c.getName(), Toast.LENGTH_SHORT).show();
                    onLoad(0);
                    }

                @Override
                public void onFailure(Call<Character> call, Throwable t) {
                    Toast.makeText(DetailActivity.this, "There was an error calling the API", Toast.LENGTH_SHORT).show();
                }

            });
        }
        else if(i==1)
        {
            //Toast.makeText(this, "Accessing from Database...", Toast.LENGTH_SHORT).show();
            imgLoad n = new imgLoad();
            n.execute();
        }
    }

    private class imgLoad extends AsyncTask<String, Integer, Long>
    {
        protected Long doInBackground(String... t)
        {
            Long i = 0L;
            c = CharDatabase.cDAO().fetchC(m);

            return i;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            onLoad(1);


        }
    }
    public void onLoad(int i)
    {
        if(i==0)
        Toast.makeText(this, "Accessing from API...", Toast.LENGTH_SHORT).show();
        else
        Toast.makeText(this, "Accessing from Database...", Toast.LENGTH_SHORT).show();
        ArrayList<DataS> attr = new ArrayList<>();
        String link = "https://api.got.show/";



        link += c.getImageLink();
        Picasso.with(DetailActivity.this).load(link).into(img);
        if(i==0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Data newCharacter = new Data();
                    newCharacter = c;
                    CharDatabase.cDAO().insertC(newCharacter);
                }
            }).start();
        }
        if(c.getHouse()!=null)
        {
            DataS temp=new DataS();
            temp.head="House";
            temp.sub=c.getHouse();
            attr.add(temp);
        }
        if(c.getMale()!=true)
        {
            DataS temp=new DataS();
            temp.head="Gender";
            temp.sub="Female";
            attr.add(temp);
        }
        else
        {
            DataS temp=new DataS();
            temp.head="Gender";
            temp.sub="Male";
            attr.add(temp);
        }
        if(c.getName()!=null)
        {
            n.setText(c.getName());
        }
        if(c.getDateOfBirth()!=null)
        {
            DataS temp=new DataS();
            temp.head="Date of Birth";
            temp.sub=String.valueOf(c.getDateOfBirth());
            attr.add(temp);
        }
        ListAdapter lis = new ListAdapter(this,attr);
        list.setAdapter(lis);
    }



    @Override
    public void onBackPressed()
    {
        String data = c.getName();
        Intent intent = new Intent();
        intent.putExtra("newC",data);
        setResult(1,intent);
        finish();
    }
}
