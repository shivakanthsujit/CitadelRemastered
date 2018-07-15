package com.example.shivakanth.citadelremastered;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Data;

public class MainActivity extends AppCompatActivity {

    Button search;
    AutoCompleteTextView txt;
    ArrayList<String> history = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.button);
        txt = findViewById(R.id.name);
        int kk = checkDB();
        if(kk!=0)
            history = getDB();

        txt.setThreshold(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,history);
        txt.setAdapter(adapter);

        txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String t= (String)adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, t, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class );
                intent.putExtra("name",t);
                intent.putExtra("isHistory",1);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String t= txt.getText().toString();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class );
                intent.putExtra("name",t);
                intent.putExtra("isHistory",0);
                startActivityForResult(intent,1);
            }
        });


        }
        public void onActivityResult(int requestCode, int resultCode,Intent data)
        {
            super.onActivityResult(requestCode,resultCode,data);
            if (requestCode == 1)
            {
                if(resultCode == 1)
                {
                    history.add(data.getStringExtra("newC"));
                    history = delDup(history);
                    //Toast.makeText(this, data.getStringExtra("newC"), Toast.LENGTH_SHORT).show();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,history);
                    txt.setAdapter(adapter);

                }
                else if(resultCode == 0)
                {
                    Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
                }



            }
        }

    public void storeDB(ArrayList<String> t) {
        SQLiteDatabase DB = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS data (name VARCHAR(200))");
        int i;
        DB.execSQL("delete from data");
        for (i = 0; i < t.size(); ++i) {
            ContentValues row1 = new ContentValues();
            row1.put("name", t.get(i));
            DB.insert("data", null, row1);
        }
        DB.close();
    }

    public ArrayList<String> getDB() {
        ArrayList<String> t = new ArrayList<String>();
        SQLiteDatabase DB = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        Cursor myCursor = DB.rawQuery("select * from data", null);
        int i = 0;
        String temp = null;
        while (myCursor.moveToNext()) {
            temp = new String();
            temp = myCursor.getString(0);
            t.add(temp);
            i++;
        }

        myCursor.close();
        DB.close();
        return t;
    }

    public int checkDB() {
        SQLiteDatabase DB = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS data (name VARCHAR(200))");

        Cursor myCursor = DB.rawQuery("select * from data", null);
        int i = 0;

        while (myCursor.moveToNext()) {
            i++;
        }

        myCursor.close();
        DB.close();
        return i;
    }
    public ArrayList<String> delDup(ArrayList<String> tt)
    {

        List<String> al = new ArrayList<>(tt);
        Set<String> hs = new HashSet<>();
        hs.addAll(al);
        al.clear();
        al.addAll(hs);
        tt = new ArrayList<>(al);

        storeDB(tt);

        return tt;

    }
    }




