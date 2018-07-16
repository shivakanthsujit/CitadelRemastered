package com.example.shivakanth.citadelremastered;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    public int isOK;
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
        isOK =0;


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
                public void onResponse(Call<Character> call, Response<Character> response)
                {
                    if(response.body().getMessage().equals("Success")) {
                        c = response.body().getData();


                        int kk = checkDB(c.getName());
                        if(kk==0)
                        {
                            isOK = 1;
                            onLoad(0);
                        }
                        else
                        {
                            isOK = 2;
                            m=c.getName();
                            imgLoad n = new imgLoad();
                            n.execute();
                        }
                    }
                    else
                    {
                        isOK =0;
                        Intent intent = new Intent();
                        setResult(0, intent);
                        finish();
                    }
                    }

                @Override
                public void onFailure(Call<Character> call, Throwable t) {
                    Toast.makeText(DetailActivity.this, "There was an error calling the API", Toast.LENGTH_SHORT).show();
                    isOK = 0;
                    Intent intent = new Intent();
                    setResult(0, intent);
                    finish();
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
        if(c.getSpouse()!=null)
        {
            DataS temp=new DataS();
            temp.head="Spouse";
            temp.sub=String.valueOf(c.getSpouse());
            attr.add(temp);
        }
        if(c.getBooks()!=null)
        {
            DataS temp=new DataS();
            temp.head="Appears in";
            List<String> a = c.getBooks();
            String tt = "";
            for(int kk = 0;kk<a.size();++kk)
                if(kk!=a.size()-1)
                    tt+=a.get(kk) + ", ";
                else
                    tt+=a.get(kk);


            temp.sub=String.valueOf(tt);
            attr.add(temp);
        }
        ListAdapter lis = new ListAdapter(this,attr);
        list.setAdapter(lis);
    }



    @Override
    public void onBackPressed()
    {
        if(isOK==1) {
            String data = c.getName();
            Intent intent = new Intent();
            intent.putExtra("newC", data);
            setResult(1, intent);
            finish();
        }
        else if(isOK == 0)
        {

            Intent intent = new Intent();
            setResult(0, intent);
            finish();
        }
        else if(isOK == 2)
        {
            Intent intent = new Intent();
            setResult(2, intent);
            finish();

        }
    }
    public int checkDB(String tt) {
        SQLiteDatabase DB = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS data (name VARCHAR(200))");
        String s="select * from data ";

        Cursor myCursor = DB.rawQuery(s ,null);
        int i = 0;

        while (myCursor.moveToNext()) {
            String temp = new String();
            temp = myCursor.getString(0);
            if(tt.equals(temp))
            i++;
        }

        myCursor.close();
        DB.close();
        return i;
    }

}
